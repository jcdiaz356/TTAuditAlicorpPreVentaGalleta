package com.dataservicios.ttauditalicorppreventagalleta.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Store;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.StoreRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.AuditUtil;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;

import java.util.HashMap;


public class EditStoreActivity extends AppCompatActivity {
    private static final String LOG_TAG = EditStoreActivity.class.getSimpleName();

    private SessionManager session;
    private Activity activity =  this;
    private ProgressDialog pDialog;
    private LinearLayout lyContentControl;
    private TextView tvStoreFullName;
    private TextView tvStoreId;
    private TextView tvAddress;
    private TextView tvReferencia;
    private TextView tvDistrict;
    private TextView tvType;
    private TextView tvTitleEdit;
    private TextView[]      arrayTextView;
    private EditText[]      arrayEditText;
    private Button btSave;
    private StoreRepo       storeRepo;
    private CompanyRepo     companyRepo;
    private Store           store;
    private Company         company;
    private int             user_id;
    private int             store_id;
    private int             action;
    private String address,reference;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);

        lyContentControl    = (LinearLayout) findViewById(R.id.lyContentControl);
        tvStoreFullName     = (TextView) findViewById(R.id.tvStoreFullName);
        tvStoreId           = (TextView) findViewById(R.id.tvStoreId);
        tvAddress           = (TextView) findViewById(R.id.tvAddress);
        tvReferencia        = (TextView) findViewById(R.id.tvReferencia);
        tvDistrict          = (TextView) findViewById(R.id.tvDistrict);
        tvType              = (TextView) findViewById(R.id.tvType);
        tvTitleEdit         = (TextView) findViewById(R.id.tvTitleEdit);
        btSave              = (Button) findViewById(R.id.btSave);

        Bundle bundle = getIntent().getExtras();
        store_id    = bundle.getInt("store_id");
        action      = bundle.getInt("action");

        session = new SessionManager(activity);
        HashMap<String, String> userSesion = session.getUserDetails();
        user_id = Integer.valueOf(userSesion.get(SessionManager.KEY_ID_USER)) ;
        userName = String.valueOf(userSesion.get(SessionManager.KEY_NAME)) ;

        storeRepo   = new StoreRepo(activity);
        companyRepo = new CompanyRepo(activity);

        store       = (Store) storeRepo.findById(store_id);
        company     = (Company) companyRepo.findFirstReg();

        tvStoreFullName.setText(String.valueOf(store.getFullname()));
        tvStoreId.setText(String.valueOf(store.getId()));
        tvAddress.setText(String.valueOf(store.getAddress()));
        tvReferencia.setText(String.valueOf(store.getUrbanization()));
        tvDistrict.setText(String.valueOf(store.getDistrict()));
        tvType.setText(String.valueOf(store.getType()));

        createControlEdition();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.save);
                builder.setMessage(R.string.saveInformation);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (action){
                            case 1:
                                address     = arrayEditText[0].getText().toString();
                                reference   = arrayEditText[1].getText().toString();
                                new saveChangAddressStore().execute();
                                break;
                        }



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

    public void showToolbar(String title, boolean upButton){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);


    }

    private void createControlEdition(){
        switch (action){
            case 1:
                showToolbar(getString(R.string.text_edit_address),false);
                tvTitleEdit.setText(R.string.text_edit_address);

                arrayTextView = new TextView[2];
                arrayTextView[0] = new TextView(activity);
                arrayTextView[0].setText(R.string.text_address);
                arrayTextView[0].setTypeface(arrayTextView[0].getTypeface(), Typeface.BOLD);

                arrayTextView[1] = new TextView(activity);
                arrayTextView[1].setText(R.string.text_reference);
                arrayTextView[1].setTypeface(arrayTextView[1].getTypeface(), Typeface.BOLD);


                arrayEditText = new EditText[2];
                arrayEditText[0] = new EditText(activity);
                arrayEditText[0].setText(store.getAddress());

                arrayEditText[1] = new EditText(activity);
                arrayEditText[1].setText(store.getUrbanization());
                //arrayEditText[0].setHint(store.getAddress());

                lyContentControl.addView(arrayTextView[0]);
                lyContentControl.addView(arrayEditText[0]);
                lyContentControl.addView(arrayTextView[1]);
                lyContentControl.addView(arrayEditText[1]);

                break;
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.message_exit_not_save);
        builder.setMessage(R.string.message_exit_not_save_information);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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


    private class saveChangAddressStore extends AsyncTask<Void, String, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            boolean result;
            result = AuditUtil.saveChangeAddressStore(store_id,user_id,company.getId(),address,reference,userName,store.getFullname(),"");
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
                store.setAddress(address);
                store.setUrbanization(reference);
                storeRepo.update(store);
                finish();
            } else {
                Toast.makeText(activity,R.string.message_no_save_data, Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


    }


}
