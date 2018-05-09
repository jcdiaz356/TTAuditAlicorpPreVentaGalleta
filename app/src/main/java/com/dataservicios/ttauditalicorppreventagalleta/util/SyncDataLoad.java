package com.dataservicios.ttauditalicorppreventagalleta.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;


/**
 * Created by jcdia on 2/06/2017.
 */

public class SyncDataLoad {


    public static final String LOG_TAG = SyncData.class.getSimpleName();
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private ProgressDialog pDialog;
    private Context context;
    private String app_id;

    public AsyncResponse delegate = null;

    public SyncDataLoad(Context context, AsyncResponse asyncResponse) {

        this.context    =   context;
        this.delegate   = asyncResponse;
        PackageInfo pckInfo ;
        try {
            pckInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            app_id = pckInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public interface AsyncResponse {
        void processFinish(Boolean output);
    }



    public class syncCompany extends AsyncTask<Void, String, Boolean> {

        @Override
        protected void onProgressUpdate(String... values) {
            //super.onProgressUpdate(values);
            pDialog.setMessage(values[0].toString());
        }
        @Override
        protected void onPreExecute() {

            DatabaseManager.init(context);
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(context.getString(R.string.text_download_init));
            pDialog.setIndeterminate(true);
            // pDialog.setMax(100);
            // pDialog.setProgress(0);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            // super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub

            publishProgress(context.getString(R.string.text_download_init));
            publishProgress(context.getString(R.string.text_download_companies));

            Company company = AuditUtil.getCompany(1,1,app_id);
            if( company.getId() != 0 ) {
                CompanyRepo companyRepo = new CompanyRepo(context);
                companyRepo.deleteAll();
                companyRepo.create(company);
            }

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

//            if(result == true) {
//
//            } else {
//
//            }
            delegate.processFinish(result);
            pDialog.dismiss();
        }
    }

}
