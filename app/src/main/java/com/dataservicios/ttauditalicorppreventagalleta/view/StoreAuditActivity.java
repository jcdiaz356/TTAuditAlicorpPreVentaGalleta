package com.dataservicios.ttauditalicorppreventagalleta.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Audit;
import com.dataservicios.ttauditalicorppreventagalleta.model.AuditRoadStore;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Poll;
import com.dataservicios.ttauditalicorppreventagalleta.model.Route;
import com.dataservicios.ttauditalicorppreventagalleta.model.RouteStoreTime;
import com.dataservicios.ttauditalicorppreventagalleta.model.Store;
import com.dataservicios.ttauditalicorppreventagalleta.repo.AuditRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.AuditRoadStoreRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.RouteRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.RouteStoreTimeRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.StoreRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.AuditUtil;
import com.dataservicios.ttauditalicorppreventagalleta.util.GPSTracker;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StoreAuditActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = StoreAuditActivity.class.getSimpleName();
    private SessionManager        session;
    private GoogleMap             mMap;
    private Activity activity =  this;
    private ProgressDialog pDialog;
    private TextView tvRouteFullName,tvRouteId,tvStoreFullName,tvStoreId,tvAddress ,tvReferencia,tvDistrict,tvType ;
    private Button btSaveGeo;
    private Button btcloseRouteStore;
    private ImageButton ibEditAddress;
    private ImageButton imgAuditStore;
    private ImageButton imgShared;
    private int                   user_id;
    private int                   store_id;
    private int                   company_id;
    private double                gpsLat;
    private double                gpsLon;
    private Bitmap bmpMarker;
    private RouteRepo             routeRepo ;
    private AuditRoadStoreRepo    auditRoadStoreRepo ;
    private StoreRepo             storeRepo ;
    private CompanyRepo           companyRepo ;
    private Route                 route ;
    private Store                 store ;
    private Company               company ;
    private GPSTracker            gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_audit);

        showToolbar(getString(R.string.title_activity_Stores_Audit),true);

        DatabaseManager.init(this);

        gpsTracker = new GPSTracker(activity);
        if(!gpsTracker.canGetLocation()){
            gpsTracker.showSettingsAlert();
        }

        Bundle bundle = getIntent().getExtras();
        store_id = bundle.getInt("store_id");

        session = new SessionManager(activity);
        HashMap<String, String> userSesion = session.getUserDetails();
        user_id = Integer.valueOf(userSesion.get(SessionManager.KEY_ID_USER)) ;

        routeRepo           = new RouteRepo(activity);
        storeRepo           = new StoreRepo(activity);
        companyRepo         = new CompanyRepo(activity);
        auditRoadStoreRepo  = new AuditRoadStoreRepo(activity);

        ArrayList<Company> companies = (ArrayList<Company>) companyRepo.findAll();
        for (Company c: companies){
            company_id = c.getId();
        }

        tvRouteFullName     = (TextView) findViewById(R.id.tvRouteFullName) ;
        tvRouteId           = (TextView) findViewById(R.id.tvRouteId) ;
        tvStoreFullName     = (TextView) findViewById(R.id.tvStoreFullName) ;
        tvStoreId           = (TextView) findViewById(R.id.tvStoreId) ;
        tvAddress           = (TextView) findViewById(R.id.tvAddress) ;
        tvReferencia        = (TextView) findViewById(R.id.tvReferencia) ;
        tvDistrict          = (TextView) findViewById(R.id.tvDistrict) ;
        tvType              = (TextView) findViewById(R.id.tvType) ;
        btSaveGeo           = (Button)   findViewById(R.id.btSaveGeo);
        btcloseRouteStore   = (Button)   findViewById(R.id.btcloseRouteStore);
        ibEditAddress       = (ImageButton) findViewById(R.id.ibEditAddress);
        imgAuditStore       = (ImageButton) findViewById(R.id.imgAuditStore);
        imgShared           = (ImageButton) findViewById(R.id.imgShared);


        store = (Store) storeRepo.findById(store_id);
        route = (Route) routeRepo.findById(store.getRoute_id());

        tvRouteFullName.setText(String.valueOf(route.getFullname()));
        tvRouteId.setText(String.valueOf(route.getId()));
        tvStoreFullName.setText(String.valueOf(store.getFullname()));
        tvStoreId.setText(String.valueOf(store.getId()));
        tvAddress.setText(String.valueOf(store.getAddress()));
        tvReferencia.setText(String.valueOf(store.getUrbanization()));
        tvDistrict.setText(String.valueOf(store.getDistrict()));
        tvType.setText(String.valueOf(store.getType()));

        ibEditAddress.setVisibility(View.VISIBLE);
        ibEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "No disponible",Toast.LENGTH_SHORT).show();
                Bundle bundle  =  new Bundle();
                bundle.putInt("store_id",store.getId());
                bundle.putInt("action",1); // Action 0= edit Adress
                Intent intent = new Intent(activity,EditStoreActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        imgAuditStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(activity);
                builder.setTitle(R.string.message_sync_audits_store);
                builder.setMessage(R.string.message_sync_audits_store_information);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new syncAuditRoadStore().execute();

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                builder.setCancelable(false);

            }
        });

        imgShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "ID Store: " + store.getId() + "\nTienda: " + store.getFullname()  ;
                String shareSub = "Ruta";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_TITLE, shareBody);
                activity.startActivity(Intent.createChooser(sharingIntent, "Share using"));

            }
        });

        btSaveGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gpsTracker.canGetLocation()) {
                    gpsTracker.showSettingsAlert();
                } else {
                    gpsTracker.getLocation();
                    gpsLat = gpsTracker.getLatitude();
                    gpsLon = gpsTracker.getLongitude();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.message_update_gps);
                builder.setMessage(R.string.message_update_gps_information);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new saveLatLongStore().execute();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                builder.setCancelable(false);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        LinearLayout lyContentButtons = (LinearLayout) findViewById(R.id.lyContentButtons);
        ArrayList<AuditRoadStore> auditRoadStores = (ArrayList<AuditRoadStore>) auditRoadStoreRepo.findByStoreId(store_id);


        Button[] buttonArray = new Button[auditRoadStores.size()];
        int i =0 ;
        for (AuditRoadStore ar: auditRoadStores) {
            //tvPrueba.append(String.valueOf(ar.getAudit_id()) +   String.valueOf(ar.getList().getFullname()) +"\n");
            buttonArray[i] = new Button(this,null,R.attr.button_style_r);
            buttonArray[i].setText(ar.getList().getFullname().toString());
            buttonArray[i].setTag(String.valueOf(ar.getList().getId()));
            if(ar.getAuditStatus() == 1) {
                buttonArray[i].setEnabled(false);
            }
            lyContentButtons.addView(buttonArray[i]);

            buttonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,v.getTag().toString(), Toast.LENGTH_SHORT).show();

                    int audit_id= Integer.valueOf(v.getTag().toString());

                        AuditRepo auditRepo = new AuditRepo(activity);
                        Audit audit = (Audit) auditRepo.findById(audit_id);
                        audit.getCompany_audit_id();

                        Poll poll = new Poll();
                        poll.setOrder(1);
                        PollActivity.createInstance((Activity) activity, store_id,audit_id,poll);

                }
            });
            i ++ ;
        }


        btcloseRouteStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gpsTracker.canGetLocation()) {
                    gpsTracker.showSettingsAlert();
                } else {
                    gpsTracker.getLocation();
                    gpsLat = gpsTracker.getLatitude();
                    gpsLon = gpsTracker.getLongitude();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.message_save);
                builder.setMessage(R.string.message_save_information);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new saveCloseRouteStore().execute();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                builder.setCancelable(false);

            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        new loadMarkerPointMap().execute();
    }

    public void showToolbar(String title, boolean upButton){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        finish();
        startActivity(getIntent());
    }



    private void  getPositionStoreMap() {
        mMap.clear();
        LatLng latLong              = new LatLng(store.getLatitude(), store.getLongitude());
        MarkerOptions markerOption  = new MarkerOptions();
        markerOption.position(latLong);
        markerOption.title(store.getFullname());

        if (bmpMarker == null) markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_point)) ; else markerOption.icon(BitmapDescriptorFactory.fromBitmap(bmpMarker));
        Marker marker = mMap.addMarker(markerOption);
        marker.showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLong).zoom(15).build();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        pDialog.dismiss();
    }
    /**
     * Carga el Bitmap del marquer desde internet
     */
    private class loadMarkerPointMap extends AsyncTask<Void, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getString(R.string.text_loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Bitmap doInBackground(Void... params) {
            // TODO Auto-generated method stub
            URL url ;
            Bitmap bmp ;
            try {
                //companyRepo = new CompanyRepo(activity);
                Company company = (Company) companyRepo.findById(company_id);
                url = new URL(company.getMarkerPoint().toString());
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (Exception e) {
                bmp = null;
                e.printStackTrace();
            }
            return  bmp;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Bitmap bmp) {
            bmpMarker = bmp;
            getPositionStoreMap();
            pDialog.dismiss();
        }

    }
    private class saveLatLongStore extends AsyncTask<Void, String, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            boolean result;
            result = AuditUtil.saveLatLongStore(store_id,gpsLat,gpsLon);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getString(R.string.text_loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                store.setLatitude(gpsLat);
                store.setLongitude(gpsLon);
                storeRepo.update(store);

            }
            pDialog.dismiss();
            getPositionStoreMap();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


    }
    private class saveCloseRouteStore extends AsyncTask<Void , Integer , Boolean> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        @Override
        protected void onPreExecute() {
            //tvCargando.setText("Cargando ProductDetail...");
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getString(R.string.text_loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String time_close = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
            RouteStoreTimeRepo routeStoreTimeRepo= new RouteStoreTimeRepo(activity);
            RouteStoreTime routeStoreTime ;
            routeStoreTime = (RouteStoreTime) routeStoreTimeRepo.findFirstReg();
            routeStoreTime.setLat_close(gpsLat);
            routeStoreTime.setLon_close(gpsLon);
            routeStoreTime.setTime_close(time_close);

            if(!AuditUtil.closeRouteStore(routeStoreTime)) return false;

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){
                Store store ;
                store = (Store) storeRepo.findById(store_id);
                store.setStatus(1);
                storeRepo.update(store);

                ArrayList<Store> stores = (ArrayList<Store>) storeRepo.findByRouteId(route.getId());
                int counterStatus = 0;
                for (Store s:stores){
                    if(s.getStatus()==1){
                        counterStatus ++;
                    }
                }
                route.setAudit(counterStatus);
                routeRepo.update(route);

                finish();
            } else {
                Toast.makeText(activity , R.string.message_no_save_data, Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();
        }
    }
    private class syncAuditRoadStore extends AsyncTask<Void, String, ArrayList<AuditRoadStore>> {
        @Override
        protected ArrayList<AuditRoadStore> doInBackground(Void... params) {

            ArrayList<AuditRoadStore> auditRoadStores;

            auditRoadStores = AuditUtil.getAuditRoadStore(company_id,route.getId(),store_id);
            return auditRoadStores;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getString(R.string.text_loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<AuditRoadStore> auditRoadStores) {
            super.onPostExecute(auditRoadStores);
            if (auditRoadStores.size() >0 ) {
                AuditRepo auditRepo = new AuditRepo(activity);
                for(AuditRoadStore m: auditRoadStores){
                    Audit audit = (Audit) auditRepo.findById(m.getAudit_id());
                    m.setList(audit);
                    auditRoadStoreRepo.update(m);
                }
                ArrayList<AuditRoadStore> lista = (ArrayList<AuditRoadStore>) auditRoadStoreRepo.findByStoreId(store_id);
                onRestart();
            } else {
                Toast.makeText(activity,R.string.mesaage_no_get_data, Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


    }


}
