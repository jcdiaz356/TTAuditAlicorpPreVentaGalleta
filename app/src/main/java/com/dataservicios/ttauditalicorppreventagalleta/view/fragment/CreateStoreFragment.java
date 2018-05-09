package com.dataservicios.ttauditalicorppreventagalleta.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Audit;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Poll;
import com.dataservicios.ttauditalicorppreventagalleta.model.Product;
import com.dataservicios.ttauditalicorppreventagalleta.model.RouteStoreTime;
import com.dataservicios.ttauditalicorppreventagalleta.model.Store;
import com.dataservicios.ttauditalicorppreventagalleta.repo.AuditRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.RouteStoreTimeRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.StoreRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.AuditUtil;
import com.dataservicios.ttauditalicorppreventagalleta.util.GPSTracker;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;
import com.dataservicios.ttauditalicorppreventagalleta.view.NewStoreActivity;
import com.dataservicios.ttauditalicorppreventagalleta.view.StoresActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Created by usuario on 08/01/2015.
 */
public class CreateStoreFragment extends Fragment {

    private static final String LOG_TAG = CreateStoreFragment.class.getSimpleName();
    // Movies json url

    private Activity            activity;
    private ProgressDialog      pDialog;
    private SessionManager      session;
    private String              email_user, user_id, name_user;
    private String              keyword;
    private Button              btNew;
    private Button              btSearchStore;
    private SearchView          svSearchStore;
    private CompanyRepo         companyRepo;
    private RouteStoreTimeRepo  routeStoreTimeRepo;
    private StoreRepo           storeRepo;
    private ArrayList<Store>    stores;
    private Company             company;
    private GPSTracker          gpsTracker;


    public CreateStoreFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = getActivity();
        DatabaseManager.init(activity);

        session = new SessionManager(getActivity());

        HashMap<String, String> user = session.getUserDetails();
        name_user = user.get(SessionManager.KEY_NAME);
        email_user = user.get(SessionManager.KEY_EMAIL);
        user_id = user.get(SessionManager.KEY_ID_USER);
        //Añadiendo parametros para pasar al Json por metodo POST

        companyRepo             = new CompanyRepo(activity);
        routeStoreTimeRepo      = new RouteStoreTimeRepo(activity);
        storeRepo               = new StoreRepo(activity);
        gpsTracker              = new GPSTracker(activity);

        company = (Company) companyRepo.findFirstReg();

        if(gpsTracker.canGetLocation()){
//            GlobalConstant.latitude_open = gpsTracker.getLatitude();
//            GlobalConstant.longitude_open= gpsTracker.getLongitude();
        }else{

            gpsTracker.showSettingsAlert();
        }

        View rootView = inflater.inflate(R.layout.fragment_create_store, container, false);
        btNew           = (Button) rootView.findViewById(R.id.btNew);
        btSearchStore   = (Button) rootView.findViewById(R.id.btSearchStore);
        svSearchStore   = (SearchView) rootView.findViewById(R.id.svSearchStore);


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.text_loading));
        pDialog.setCancelable(false);

        btNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StoreRepo storeRepo = new StoreRepo(activity);
                storeRepo.deleteAll();

                routeStoreTimeRepo.deleteAll();

                Intent intent;
                intent = new Intent(getActivity().getApplicationContext(), NewStoreActivity.class);
                //intent.putExtras(argRuta);
                startActivity(intent);
            }
        });

        btSearchStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                keyword = svSearchStore.getQuery().toString().trim();

                if(keyword.equals("")){
                    Toast.makeText(activity, R.string.message_input_keyword,Toast.LENGTH_LONG).show();
                    return;
                }

                new searchStores().execute();
            }
        });

        svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               keyword = svSearchStore.getQuery().toString().trim();

                if(keyword.equals("")){
                    Toast.makeText(activity, R.string.message_input_keyword,Toast.LENGTH_LONG).show();
                    return false;
                }

                new searchStores().execute();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return rootView;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        //hidePDialog();
    }



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    class searchStores extends AsyncTask<Void , Integer , Boolean> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage(getString(R.string.text_loading));
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub

            stores = AuditUtil.getSearchStores(company.getId(), keyword);

                if(stores.size() == 0){
                    return false;
                }

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted
            if (result == true){

                storeRepo.deleteAll();
               for (Store m:stores){
                   storeRepo.create(m);
               }

                Bundle bundle = new Bundle();
                bundle.putInt("route_id", Integer.valueOf(0));
                Intent intent = new Intent(activity,StoresActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);

            } else {
                Toast.makeText(activity , R.string.message_no_stores_show, Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();
        }
    }

}
