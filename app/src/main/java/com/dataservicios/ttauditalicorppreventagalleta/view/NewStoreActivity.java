package com.dataservicios.ttauditalicorppreventagalleta.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.model.Audit;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Departament;
import com.dataservicios.ttauditalicorppreventagalleta.model.District;
import com.dataservicios.ttauditalicorppreventagalleta.model.Poll;
import com.dataservicios.ttauditalicorppreventagalleta.model.Product;
import com.dataservicios.ttauditalicorppreventagalleta.model.RouteStoreTime;
import com.dataservicios.ttauditalicorppreventagalleta.model.Store;
import com.dataservicios.ttauditalicorppreventagalleta.model.TypeStore;
import com.dataservicios.ttauditalicorppreventagalleta.repo.AuditRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.DepartamentRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.DistrictRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.OrderDetailRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.OrderRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.ProductRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.RouteStoreTimeRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.StoreRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.TypeStoreRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.AuditUtil;
import com.dataservicios.ttauditalicorppreventagalleta.util.GPSTracker;
import com.dataservicios.ttauditalicorppreventagalleta.util.GlobalConstant;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class NewStoreActivity extends AppCompatActivity {
    public static final String LOG_TAG = NewStoreActivity.class.getSimpleName();
    private Activity activity = this ;
    private SessionManager          session;
    private String                  email_user, name_user;
    private Spinner                 spDistrict;
    private Spinner                 spDepartament;
    private Spinner                 spGiro;
    private EditText                etFullname, etAddress, etTelefono,etCodDistributor,etVendorFullname,etRuc ;
    private Button                  btSave;
    private double                  lat;
    private double                  lon;
    private int                     store_id;
    private int                     user_id;
    private int                     audit_id;
    private ArrayAdapter<String>    adapter;
    private List<String>            list;
    private ProgressDialog          pDialog;
    private StoreRepo               storeRepo;
    private CompanyRepo             companyRepo;
    private DepartamentRepo         departamentRepo;
    private DistrictRepo            districtRepo;
    private TypeStoreRepo           typeStoreRepo;
    private RouteStoreTimeRepo      routeStoreTimeRepo;
    private ProductRepo             productRepo;
    private Company                 company;
    private Store                   store ;
    private Audit                   audit;
    private ArrayList<Departament>  departaments;
    private ArrayList<District>     districts;
    private ArrayList<TypeStore>    typeStores;
    private GPSTracker              gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_store);


        session = new SessionManager(activity);
        HashMap<String, String> user = session.getUserDetails();
        name_user   = user.get(SessionManager.KEY_NAME);
        email_user  = user.get(SessionManager.KEY_EMAIL);
        user_id     = Integer.valueOf(user.get(SessionManager.KEY_ID_USER));

        spDistrict          = (Spinner) findViewById(R.id.spDistrict) ;
        spDepartament       = (Spinner) findViewById(R.id.spDepartament) ;
        spGiro              = (Spinner) findViewById(R.id.spGiro) ;
        etFullname          = (EditText) findViewById(R.id.etFullname);
        etRuc               = (EditText) findViewById(R.id.etRuc);
        //etCodDistributor  = (EditText) findViewById(R.id.etCodDistributor);
        etVendorFullname    = (EditText) findViewById(R.id.etVendorFullname);
        etAddress           = (EditText) findViewById(R.id.etAddress) ;
        etTelefono          = (EditText) findViewById(R.id.etTelefono) ;
        btSave              = (Button) findViewById(R.id.btSave);

        companyRepo         = new CompanyRepo(activity);
        storeRepo           = new StoreRepo(activity);
        gpsTracker          = new GPSTracker(activity);
        departamentRepo     = new DepartamentRepo(activity);
        districtRepo        = new DistrictRepo(activity);
        typeStoreRepo       = new TypeStoreRepo(activity);
        routeStoreTimeRepo  = new RouteStoreTimeRepo(activity);
        productRepo         = new ProductRepo(activity);
        gpsTracker          = new GPSTracker(activity);
        store = new Store();

        company         = (Company) companyRepo.findFirstReg();
        departaments    = (ArrayList<Departament>) departamentRepo.findAll();
        audit_id        = GlobalConstant.audit_id[0];



        ArrayAdapter<Departament> spinnerAdapter = new ArrayAdapter<Departament>(activity, android.R.layout.simple_spinner_item, departaments);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDepartament.setAdapter(spinnerAdapter);


        spDepartament.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String label = parent.getItemAtPosition(position).toString();
                int departament_id = ((Departament) spDepartament.getSelectedItem()).getId () ;
                String label = ((Departament) spDepartament.getSelectedItem () ).getName () ;


                districts = (ArrayList<District>) districtRepo.findByForDepartamentId(departament_id);
                ArrayAdapter<District> spinnerAdapter = new ArrayAdapter<District>(activity, android.R.layout.simple_spinner_item, districts);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDistrict.setAdapter(spinnerAdapter);

                //Toast.makeText(parent.getContext(), "Seleciono: " + label + " ID: " + String.valueOf(departament_id) ,Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String label = parent.getItemAtPosition(position).toString();
                int district_id = ((District) spDistrict.getSelectedItem()).getId () ;
                String label = ((District) spDistrict.getSelectedItem () ).getName () ;

                typeStores = (ArrayList<TypeStore>) typeStoreRepo.findAll();
                ArrayAdapter<TypeStore> spinnerAdapter = new ArrayAdapter<TypeStore>(activity, android.R.layout.simple_spinner_item, typeStores);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spGiro.setAdapter(spinnerAdapter);

                //Toast.makeText(parent.getContext(), "Seleciono: " + label + " ID: " + String.valueOf(departament_id) ,Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(etFullname.getText().toString().equals("")) {
                    Toast.makeText(activity,R.string.text_requiere_fullname, Toast.LENGTH_LONG).show();
                    etFullname.requestFocus();
                    return;
                }



//                if(etAddress.getText().toString().equals("")) {
//                    Toast.makeText(activity,R.string.text_requiere_address,Toast.LENGTH_LONG).show();
//                    etAddress.requestFocus();
//                    return;
//                }



                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.message_save);
                builder.setMessage(R.string.message_save_information);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(gpsTracker.canGetLocation()){
                            lat = gpsTracker.getLatitude();
                            lon = gpsTracker.getLongitude();
                        }

                        store.setFullname(etFullname.getText().toString());
                        //store.setCode("Alicorp");
                        store.setCode("NewStore");
                        store.setAddress(etAddress.getText().toString());
                        store.setCadenRuc(etRuc.getText().toString());
                        store.setCodCliente("");
                        store.setDex("");
                        store.setDistrict(spDistrict.getSelectedItem().toString());
                        store.setDepartament(spDepartament.getSelectedItem().toString());
                        store.setGiro(spGiro.getSelectedItem().toString());
                        store.setPhone(etTelefono.getText().toString());
                        store.setExecutive(etVendorFullname.getText().toString());
                        new saveNewStore().execute();
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

    class saveNewStore extends AsyncTask<Void , Integer , Integer> {
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
        protected Integer doInBackground(Void... params) {
            // TODO Auto-generated method stub

            store_id = AuditUtil.newStore(company.getId(), store);
            if (store_id > 0) {
                AuditUtil.saveLatLongStore(store_id,lat, lon);
                store.setId(store_id);
                storeRepo.create(store);
            }
            return store_id;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Integer result) {
            // dismiss the dialog once product deleted
            if (result == 0){
                Toast.makeText(activity , R.string.message_no_save_data, Toast.LENGTH_LONG).show();
            } else {

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(c.getTime());

                RouteStoreTime routeStoreTime = new RouteStoreTime();
                routeStoreTime.setCompany_id(company.getId());
                routeStoreTime.setLat_open(lat);
                routeStoreTime.setLon_open(lon);
                routeStoreTime.setRoute_id(0);
                routeStoreTime.setStore_id(store_id);
                routeStoreTime.setTime_open(strDate);
                routeStoreTime.setUser_id(user_id);

                routeStoreTimeRepo.create(routeStoreTime);

                AuditRepo auditRepo = new AuditRepo(activity);
                Audit audit = (Audit) auditRepo.findById(audit_id);
                audit.getCompany_audit_id();


                ArrayList<Product> products = (ArrayList<Product>) productRepo.findAll();
                for(Product p: products){
                        p.setStatus(0);
                        productRepo.update(p);
                }


                OrderRepo orderRepo = new OrderRepo(activity);
                orderRepo.deleteAll();

                OrderDetailRepo orderDetailRepo = new OrderDetailRepo(activity);
                orderDetailRepo.deleteAll();

                int store_id = store.getId();
//              Toast.makeText(activity, String.valueOf(store.id), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putInt("store_id", Integer.valueOf(store_id));
                Intent intent = new Intent(activity,OrderActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);


                finish();
            }
            pDialog.dismiss();
        }
    }



}
