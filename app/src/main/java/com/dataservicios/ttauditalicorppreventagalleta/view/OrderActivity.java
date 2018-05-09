package com.dataservicios.ttauditalicorppreventagalleta.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Order;
import com.dataservicios.ttauditalicorppreventagalleta.model.Product;
import com.dataservicios.ttauditalicorppreventagalleta.model.Route;
import com.dataservicios.ttauditalicorppreventagalleta.model.Store;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.OrderDetailRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.OrderRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.ProductRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.StoreRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.GPSTracker;
import com.dataservicios.ttauditalicorppreventagalleta.util.GlobalConstant;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderActivity extends AppCompatActivity {

    private static final String LOG_TAG = OrderActivity.class.getSimpleName();
    private SessionManager                  session;
    private Activity                        activity =  this;
    private ProgressDialog                  pDialog;
    private TextView                        tvStoreFullName,tvStoreId,tvAddress ,tvReferencia,tvDistrict,tvAuditoria, tvType ;
    private Button                          btSave,btViewOrder,btOrder;
    private LinearLayout                    lyOptions;
    private int                             user_id;
    private int                             store_id;
    private int                             audit_id;
    private int                             company_id;
    private StoreRepo                       storeRepo ;
    private CompanyRepo                     companyRepo ;
    private ProductRepo                     productRepo;
    private OrderRepo                       orderRepo;
    private OrderDetailRepo                 orderDetailRepo;
    private Product                         product;
    private Store                           store ;
    private GPSTracker                      gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        DatabaseManager.init(this);

        gpsTracker = new GPSTracker(activity);
        if(!gpsTracker.canGetLocation()){
            gpsTracker.showSettingsAlert();
        }

        Bundle bundle = getIntent().getExtras();
        store_id            = bundle.getInt("store_id");
        audit_id            = GlobalConstant.audit_id[0];



        session = new SessionManager(activity);
        HashMap<String, String> userSesion = session.getUserDetails();
        user_id = Integer.valueOf(userSesion.get(SessionManager.KEY_ID_USER)) ;


        storeRepo               = new StoreRepo(activity);
        companyRepo             = new CompanyRepo(activity);
        productRepo             = new ProductRepo(activity);
        orderRepo               = new OrderRepo(activity);
        orderDetailRepo         = new OrderDetailRepo(activity);


        ArrayList<Company> companies = (ArrayList<Company>) companyRepo.findAll();
        for (Company c: companies){
            company_id = c.getId();
        }

        tvStoreFullName     = (TextView)    findViewById(R.id.tvStoreFullName) ;
        tvStoreId           = (TextView)    findViewById(R.id.tvStoreId) ;
        tvAddress           = (TextView)    findViewById(R.id.tvAddress) ;
        tvReferencia        = (TextView)    findViewById(R.id.tvReferencia) ;
        tvDistrict          = (TextView)    findViewById(R.id.tvDistrict) ;
        tvAuditoria         = (TextView)    findViewById(R.id.tvAuditoria) ;
        tvType              = (TextView)    findViewById(R.id.tvType) ;

        btSave              = (Button)      findViewById(R.id.btSave);
        btViewOrder         = (Button)      findViewById(R.id.btViewOrder);
        btOrder             = (Button)      findViewById(R.id.btOrder);
        lyOptions           = (LinearLayout)findViewById(R.id.lyOptions);

        store               = (Store)           storeRepo.findById(store_id);

        tvStoreFullName.setText(String.valueOf(store.getFullname()));
        tvStoreId.setText(String.valueOf(store.getId()));
        tvAddress.setText(String.valueOf(store.getAddress()));
        tvReferencia.setText(String.valueOf(store.getUrbanization()));
        tvDistrict.setText(String.valueOf(store.getDistrict()));

        tvType.setText(String.valueOf(store.getType()) + " (" + store.getCadenRuc() + ")");

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.message_save);
                builder.setMessage(R.string.message_save_information);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                        //new savePoll().execute();
//                        Poll poll = new Poll();
//                        poll.setOrder(13);
//                        PollActivity.createInstance((Activity) activity, store_id,audit_id,poll);
                        finish();
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

        btOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counterSelected =0;

                Bundle bundle = new Bundle();
                bundle.putInt("store_id", Integer.valueOf(store_id));
                bundle.putInt("audit_id", Integer.valueOf(audit_id));
                Intent intent = new Intent(activity,OrderProductsActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);

            }
        });

        btViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("store_id", Integer.valueOf(store_id));
                bundle.putInt("audit_id", Integer.valueOf(audit_id));
//                bundle.putInt("distributor_id", Integer.valueOf(selectedOptions));
                Intent intent = new Intent(activity,ProductOrdersTotalActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        showToolbar(getString(R.string.text_orders),false);


    }


    private void showToolbar(String title, boolean upButton){
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
        finish();
        startActivity(getIntent());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {

//        if(products.size() == 0 ) {
//            super.onBackPressed ();
//        } else {
//            alertDialogBasico(getString(R.string.message_save_audit_products));
//        }

//        super.onBackPressed ();

        ArrayList<Order> orders = (ArrayList<Order>) orderRepo.findByOrder(company_id,store_id,1);

        if(orders.size() >0 ) {
            alertDialog(getString(R.string.message_pending_order_finalize),0);
        } else  {
            super.onBackPressed ();
        }

    }


    /**
     * Alerta dialogo
     * @param message
     * @param type 0= typo ye, 1= yes no
     */
    private void alertDialog(String message,int type) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        switch (type){

            case 0:

                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                builder.show();
                break;
            case 1:

                builder.setMessage(getString(R.string.message_exit_not_save_information));
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return ;
                    }
                });
                builder.show();
                break;
        }
    }
}
