package com.dataservicios.ttauditalicorppreventagalleta.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dataservicios.ttauditalicorppreventagalleta.app.AppController;
import com.dataservicios.ttauditalicorppreventagalleta.model.Media;
import com.dataservicios.ttauditalicorppreventagalleta.repo.MediaRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.AuditUtil;
import com.dataservicios.ttauditalicorppreventagalleta.util.Connectivity;

public class UpdateService extends Service {


    private final String TAG = UpdateService.class.getSimpleName();
    private final Integer contador = 0;

    private Context context = this;

    static final int DELAY =  60000; //2 minutos de espera
    //static final int DELAY = 9000; //9 segundo de espera
    private boolean runFlag = false;
    private Updater updater;

    private AppController application;

    private MediaRepo mediaRepo;
    private Media media;
    private AuditUtil auditUtil;

    public UpdateService() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = (AppController) getApplication();
        updater = new Updater();
        mediaRepo = new MediaRepo(this);
        media = new Media();
        auditUtil = new AuditUtil(context);
        Log.d(TAG, "onCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        runFlag = false;
        application.setServiceRunningFlag(false);
        updater.interrupt();
        updater = null;
        Log.d(TAG, "onDestroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!runFlag){
            runFlag = true;
            application.setServiceRunningFlag(true);
            updater.start();
        }

        Log.d(TAG, "onStarted");
        return START_STICKY;
    }

    private class Updater extends Thread {
        public Updater(){
            super("UpdaterService-UpdaterThread");
        }


        @Override
        public void run() {

            UpdateService updaterService = UpdateService.this;
            while (updaterService.runFlag) {
                Log.d(TAG, "UpdaterThread running");
                try{

                    if(Connectivity.isConnected(context)) {
                        if (Connectivity.isConnectedFast(context)) {

                            Log.i(TAG," Conexión rápida" );
                            if (mediaRepo.countReg() >0 ) {
                                media = (Media) mediaRepo.findFirstReg();
                                boolean response = auditUtil.uploadMedia(media,1);
                                if (response) {
                                    mediaRepo.delete(media);
                                    Log.i(TAG," Send success images database server and delete local database and file " );
                                }
                            } else{
                                Log.i(TAG, "No found records in media table for send");
                            }

                        }else {
                            Log.i(TAG," Connectivity slow" );
                        }
                    } else {
                        Log.i(TAG," No internet connection" );
                    }
                    Thread.sleep(DELAY);
                }catch(InterruptedException e){
                    updaterService.runFlag = false;
                    application.setServiceRunningFlag(true);
                }

            }
        }


    }
}
