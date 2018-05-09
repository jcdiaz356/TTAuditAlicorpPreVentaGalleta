package com.dataservicios.ttauditalicorppreventagalleta.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Distributor;
import com.dataservicios.ttauditalicorppreventagalleta.model.Order;
import com.dataservicios.ttauditalicorppreventagalleta.model.OrderDetail;
import com.dataservicios.ttauditalicorppreventagalleta.model.Product;
import com.dataservicios.ttauditalicorppreventagalleta.model.Store;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.DistributorRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.OrderDetailRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.OrderRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.ProductRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.StoreRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.AuditUtil;
import com.dataservicios.ttauditalicorppreventagalleta.util.GPSTracker;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;


import java.util.ArrayList;
import java.util.HashMap;

public class ProductOrdersTotalActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProductOrdersTotalActivity.class.getSimpleName();
    private SessionManager session;
    private Activity                activity =  this;
    private ProgressDialog          pDialog;
    private TextView                tvStoreFullName,tvStoreId,tvAddress ,tvReferencia,tvDistrict,tvAuditoria, tvType ;
    private Button                  btSave;
    private LinearLayout            lyTable;
    private int                     user_id;
    private int                     store_id;
    private int                     audit_id;
    private int                     company_id;
    private int                     category_product_id;
    private int                     publicity_id;
    private int                     product_id;

    private StoreRepo               storeRepo ;
    private CompanyRepo             companyRepo ;
    private OrderRepo               orderRepo;
    private OrderDetailRepo         orderDetailRepo;
    private ProductRepo             productRepo;
    private DistributorRepo         distributorRepo;
    private Product                 product;
    private Distributor             distributor;
    private Store                   store ;
    private GPSTracker gpsTracker;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_orders_total);


        DatabaseManager.init(this);

        gpsTracker = new GPSTracker(activity);
        if(!gpsTracker.canGetLocation()){
            gpsTracker.showSettingsAlert();
        }

        Bundle bundle = getIntent().getExtras();
        store_id            = bundle.getInt("store_id");
        audit_id            = bundle.getInt("audit_id");

        session = new SessionManager(activity);
        HashMap<String, String> userSesion = session.getUserDetails();
        user_id = Integer.valueOf(userSesion.get(SessionManager.KEY_ID_USER)) ;


        storeRepo           = new StoreRepo(activity);
        companyRepo         = new CompanyRepo(activity);
        productRepo         = new ProductRepo(activity);
        orderRepo           = new OrderRepo(activity);
        orderDetailRepo     = new OrderDetailRepo(activity);
        distributorRepo     = new DistributorRepo(activity);

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

        lyTable           = (LinearLayout)findViewById(R.id.lyTable);

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

//                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                builder.setTitle(R.string.message_save);
//                builder.setMessage(R.string.message_save_information);
//                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        finish();
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.show();
//                builder.setCancelable(false);
                finish();
            }
        });

        showToolbar(getString(R.string.text_orders),false);

        tableOrderDetail();
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

        super.onBackPressed ();

    }


    private void alertDialogBasico(String message) {

        // 1. Instancia de AlertDialog.Builder con este constructor
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        // 2. Encadenar varios métodos setter para ajustar las características del diálogo
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.show();

    }

    /**
     * Guarda la pregunta
     */
    class savePoll extends AsyncTask<Void, Integer , Boolean> {
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

//             if (!AuditUtil.closeAuditStore(audit_id, store_id, company_id, route.getId())) return false;

            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){

//                auditRoadStore.setAuditStatus(1);
//                auditRoadStoreRepo.update(auditRoadStore);

                ArrayList<Product> products = (ArrayList<Product>) productRepo.findAll();
                for (Product m: products){
                    m.setStatus(0);
                    productRepo.update(m);
                }

            } else {
                Toast.makeText(activity , R.string.message_no_save_data , Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void tableOrderDetail(){

        ArrayList<Order> orders = (ArrayList<Order>) orderRepo.findByOrder(company_id,store_id,1);

        for (Order o: orders) {

            distributor = (Distributor) distributorRepo.findById(o.getDistributor_id());

            LinearLayout lyOrderDetail = new LinearLayout(this);
            lyOrderDetail.setOrientation(LinearLayout.HORIZONTAL);
            lyOrderDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            lyOrderDetail.setPadding(8,18,8,28);

            TableLayout table = new TableLayout(this);
            table.setStretchAllColumns(true);
            table.setShrinkAllColumns(true);
//            table.setBackgroundColor(Color.parseColor("#FF0000"));
            table.setBackgroundResource(R.color.colorPrimaryDark);
            table.setPadding(5, 5, 5, 5);

//            ---------------------------------------------
//            CREANDO UNA FILA PARA  Y AÑADIENDO UN TEXTBOX CON EL DISTRIBUIDOR
//            ---------------------------------------------
            TableRow rowTitle = new TableRow(this);
            rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            TextView title = new TextView(this);
            title.setTextColor(getColor(R.color.colorWhite));
            title.setText("Distribuidor: " + distributor.getFullName());

            title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            title.setGravity(Gravity.LEFT);
            title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.span = 5;

            rowTitle.addView(title, params);
            table.addView(rowTitle);
//            ---------------------------------------------
//            END
//            ---------------------------------------------


            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowHeader = inflater.inflate(R.layout.row_header, null, false);
            table.addView(rowHeader);

            ArrayList<OrderDetail> orderDetails = (ArrayList<OrderDetail>) orderDetailRepo.findByOrderDetail(o.getId());
            for(OrderDetail d: orderDetails){

                product = (Product) productRepo.findById(d.getProduct_id());

                LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View fullRow = inflater2.inflate(R.layout.rows, null, false);

                TextView tvCant = (TextView) fullRow.findViewById(R.id.tvCant);
                tvCant.setText(String.valueOf(d.getQuantity()));
                tvCant.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);

                TextView tvCode = (TextView) fullRow.findViewById(R.id.tvCode);
                tvCode.setText(String.valueOf(product.getCode()));
                tvCode.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                // tvCode.setId(p.getId());

                TextView tvProductName = (TextView) fullRow.findViewById(R.id.tvProductName);
                tvProductName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                tvProductName.setText(String.valueOf(product.getFullname().substring(0,5)));


                TextView tvPrice = (TextView) fullRow.findViewById(R.id.tvPrice);
                tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                tvPrice.setText(String.valueOf(d.getPrice()));

                TextView tvTotal = (TextView) fullRow.findViewById(R.id.tvTotal);
                tvTotal.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                tvTotal.setText(String.valueOf(d.getTotal()));
                table.addView(fullRow);
            }
//            ---------------------------------------------
//            CREANDO UNA FILA PARA  Y AÑADIENDO UN TEXTBOX PARA TOTAL
//            ---------------------------------------------
            TableRow rowTotal = new TableRow(this);
            rowTotal.setGravity(Gravity.CENTER_HORIZONTAL);
            TextView tvTotal = new TextView(this);
            tvTotal.setText("Total: " + String.valueOf(o.getMount_total()));

            tvTotal.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            tvTotal.setGravity(Gravity.RIGHT);
            tvTotal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tvTotal.setTextColor(getResources().getColor(R.color.colorAccent));
            tvTotal.setPadding(0,3,0,3);

            TableRow.LayoutParams paramsTotal = new TableRow.LayoutParams();
            paramsTotal.span = 5;

            rowTotal.addView(tvTotal, paramsTotal);
            table.addView(rowTotal);
//            ---------------------------------------------
//            CREANDO UNA FILA PARA  Y AÑADIENDO UN TEXTBOX
//            ---------------------------------------------

            lyOrderDetail.addView(table);
            lyTable.addView(lyOrderDetail);

        }

    }
}
