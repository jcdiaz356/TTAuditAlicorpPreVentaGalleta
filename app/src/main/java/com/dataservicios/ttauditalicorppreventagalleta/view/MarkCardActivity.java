package com.dataservicios.ttauditalicorppreventagalleta.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.AssistControl;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.ImageTemp;
import com.dataservicios.ttauditalicorppreventagalleta.model.User;
import com.dataservicios.ttauditalicorppreventagalleta.repo.AssistControlRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.ImageTempRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.UserRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.BitmapLoader;
import com.dataservicios.ttauditalicorppreventagalleta.util.GPSTracker;
import com.dataservicios.ttauditalicorppreventagalleta.util.GlobalConstant;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MarkCardActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = MarkCardActivity.class.getSimpleName();
    private static final int        TAKE_PICTURE = 1;
    private String mCurrentPhotoPath, currentDate,currentTime;
    private SessionManager          session;
    private GoogleMap               mMapInPut,mMapOutPut;
    private Activity activity =  this;
    private TextView etFullName;
    private ImageButton btPhotoInPut, btPhotoOutPut;
    private CircleImageView         imcUser;
    private ImageView imgInPut, imgOutPut ;
    private Button btOutPut,btInPut;
    private int                     user_id;
    private User                    user;
    private Company                 company;
    private AssistControl           assistControl;
    private ImageTemp               imageTemp;
    private UserRepo                userRepo ;
    private CompanyRepo             companyRepo;
    private AssistControlRepo       assistControlRepo;
    private ImageTempRepo           imageTempRepo;
    private GPSTracker              gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_card);

        DatabaseManager.init(this);

        gpsTracker = new GPSTracker(activity);
        if(!gpsTracker.canGetLocation()){
            gpsTracker.showSettingsAlert();
        }

        etFullName      = (TextView) findViewById(R.id.etFullName);
        btPhotoInPut    = (ImageButton) findViewById(R.id.btPhotoInPut);
        btPhotoOutPut   = (ImageButton) findViewById(R.id.btPhotoOutPut);
        btInPut         = (Button) findViewById(R.id.btInPut) ;
        btOutPut        = (Button) findViewById(R.id.btOutPut) ;
        imcUser         = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.imcUser);

        session = new SessionManager(activity);
        HashMap<String, String> userSesion = session.getUserDetails();
        user_id = Integer.valueOf(userSesion.get(SessionManager.KEY_ID_USER)) ;

        userRepo            = new UserRepo(activity);
        companyRepo         = new CompanyRepo(activity);
        assistControlRepo   = new AssistControlRepo(activity);
        imageTempRepo       = new ImageTempRepo(activity);

        user                = (User) userRepo.findById(user_id);
        company             = (Company) companyRepo.findFirstReg();

        String time_close   = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());

        etFullName.setText(user.getFullname().toString());

        Picasso.with(activity)
               .load(GlobalConstant.URL_USER_IMAGES + user.getImage().toString())
               .error(R.drawable.avataruser)
               .into(imcUser);

        SupportMapFragment mapFragmentOutPut = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapOutPut);
        mapFragmentOutPut.getMapAsync(this);

        SupportMapFragment mapFragmentInPut = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapInPut);
        mapFragmentInPut.getMapAsync(this);

        btPhotoInPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create intent with ACTION_IMAGE_CAPTURE action
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Bundle bundle = getIntent().getExtras();
                //String idPDV = bundle.getString("idPDV");
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = String.format("%06d", Integer.parseInt(String.valueOf(user.getId()))) + "_" + company.getId() + GlobalConstant.JPEG_FILE_PREFIX + timeStamp;

                File albumF = BitmapLoader.getAlbumDir(activity); // getAlbumDir();
                // to save picture remove comment
                File file = new File(albumF,imageFileName+GlobalConstant.JPEG_FILE_SUFFIX);

                Uri photoPath = Uri.fromFile(file);
                mCurrentPhotoPath = BitmapLoader.getAlbumDir(activity).getAbsolutePath();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(activity, "com.dataservicios.ttauditalicorppreventagalleta.fileProvider", file);
                    //intent.setDataAndType(contentUri, type);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
                }
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapInPut = googleMap;
        mMapOutPut = googleMap;
        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMapInPut.setMyLocationEnabled(true);
       // mMapInPut.getUiSettings().setZoomControlsEnabled(true);
        mMapInPut.getUiSettings().setCompassEnabled(true);
        mMapInPut.getCameraPosition();

        mMapOutPut.setMyLocationEnabled(true);
        //mMapOutPut.getUiSettings().setZoomControlsEnabled(true);
        mMapOutPut.getUiSettings().setCompassEnabled(true);
        mMapOutPut.getCameraPosition();
        //new StoreAuditActivity.loadMarkerPointMap().execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            }
        }
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            galleryAddPic();
            mCurrentPhotoPath = null;
            Bundle bundle = getIntent().getExtras();
            Intent i = new Intent( MarkCardActivity.this , MarkCardActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }

    public void getFromSdcard()
    {

//
//        File file= new File(BitmapLoader.getAlbumDir(activity).getAbsolutePath());
//
//
//        if (file.isDirectory())
//        {
//            listFile = file.listFiles();
//            if (listFile != null){
//                for (int i = 0; i < listFile.length; i++)
//                {
//                    if (  listFile[i].getName().substring(0,6).equals(String.format("%06d", Integer.parseInt(store_id.toString())) ))
//                    {
//                        // f.add(listFile[i].getAbsolutePath());
//                        if(listFile[i].exists()){
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 8;
//                            Bitmap myBitmap = BitmapFactory.decodeFile(listFile[i].getAbsolutePath(), options);
//
//                            //thumbnail.setImageBitmap(myBitmap);
//                            thumbnail.setImageBitmap(BitmapLoader.rotateImage(myBitmap,90));
//
//                            thumbnail.setTag(listFile[i].getName().toString());
//                        }
//                    }
//
//                }
//            }
//
//
//        }
    }

}
