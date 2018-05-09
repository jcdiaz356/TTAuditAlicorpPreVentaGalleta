package com.dataservicios.ttauditalicorppreventagalleta.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Store;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.StoreRepo;
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
import java.util.ArrayList;

public class MapsRouteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LOG_TAG = MapsRouteActivity.class.getSimpleName();
    private Activity activity =  this;
    private GoogleMap           mMap;
    private ProgressDialog pDialog;
    private ArrayList<Marker> myMarkerPoints = new ArrayList<Marker>();
    private int                 route_id;
    private int                 company_id;
    private CompanyRepo         companyRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_route);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_maps_route);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseManager.init(activity);
        companyRepo = new CompanyRepo(activity);
        ArrayList<Company> companies = (ArrayList<Company>) companyRepo.findAll();
        for (Company c: companies){
            company_id = c.getId();
        }


        Bundle bundle = getIntent().getExtras();
        route_id = bundle.getInt("route_id");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    class loadMarkerPointMap extends AsyncTask<Void, String, Bitmap> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */

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
            Bitmap bmp = null;
            try {
                companyRepo = new CompanyRepo(activity);
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


            StoreRepo storeRepo = new StoreRepo(activity);
            ArrayList<Store> stores = (ArrayList<Store>) storeRepo.findByRouteId(route_id);

            if (stores.size() > 0 )  {
                Double latInit = stores.get(0).getLatitude();
                Double lonInit = stores.get(0).getLongitude();

                for(Store s: stores){
                    LatLng          latLong      = new LatLng(s.getLatitude(), s.getLongitude());
                    MarkerOptions   markerOption = new MarkerOptions();

                    markerOption.position(latLong);
                    markerOption.title(s.getFullname());

                    if(s.getStatus()==0){
                        if (bmp == null) markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_point)) ; else markerOption.icon(BitmapDescriptorFactory.fromBitmap(bmp));
                        myMarkerPoints.add(mMap.addMarker(markerOption));
                    }

                }
                myMarkerPoints.get(0).showInfoWindow();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latInit,lonInit)).zoom(15).build();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latInit,lonInit)));
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            } else  {
                Toast.makeText(activity , R.string.message_no_found, Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();

        }

    }
}
