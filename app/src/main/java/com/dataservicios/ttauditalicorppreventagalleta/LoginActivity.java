package com.dataservicios.ttauditalicorppreventagalleta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.User;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.UserRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.AuditUtil;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;
import com.dataservicios.ttauditalicorppreventagalleta.util.SyncData;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    public static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private Button btLogin;
    private TextInputEditText   etUsuario,etPassword;
    private CheckBox ckSavePassword;
    private UserRepo            userRepo ;
    private ProgressDialog pDialog;
    private Activity activity = (Activity) this;
    private SessionManager      session;
    private String userLogin, passwordLogin, simSNLogin;
    private int                 company_id;
    private CompanyRepo         companyRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogin     = (Button) findViewById(R.id.btLogin);
        etUsuario   = (TextInputEditText) findViewById(R.id.etUser);
        etPassword  = (TextInputEditText) findViewById(R.id.etPassword);
        ckSavePassword  = (CheckBox) findViewById(R.id.ckSavePassword);

//        etUsuario.setText("jcdiaz356@hotmail.com");
//        etPassword.setText("123456");

        session = new SessionManager(getApplicationContext());

        DatabaseManager.init(activity);
        userRepo = new UserRepo(activity);
        companyRepo = new CompanyRepo(activity);



        ArrayList<Company> companies = (ArrayList<Company>) companyRepo.findAll();
        for(Company c:companies ){
            company_id = c.getId();
        }

        if( userRepo.findAll().size() > 0) {
            //User users = new User();
            List<User> usersList = (List<User>) userRepo.findAll();
            if(usersList.size()>0) {
                User users = new User();
                users=usersList.get(0);
                etUsuario.setText(users.getEmail());
                etPassword.setText(users.getPassword());
            }
        }
        
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsuario.getText().toString().trim().equals("") )
                {
                    Toast toast = Toast.makeText(activity, R.string.message_requiere_user , Toast.LENGTH_SHORT);
                    toast.show();
                    etUsuario.requestFocus();
                    return;
                }else if (etPassword.getText().toString().trim().equals("")) {
                    Toast toast = Toast.makeText(activity, R.string.message_requiere_password, Toast.LENGTH_SHORT);
                    toast.show();
                    etPassword.requestFocus();
                    return;
                }else {

                    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    ///String imei = tm.getDeviceId();
                    //String imei1 = tm.getLine1Number();
                    simSNLogin = tm.getSimSerialNumber();
                    userLogin = etUsuario.getText().toString();
                    passwordLogin = etPassword.getText().toString();
                    new loadLogin().execute();
                }
            }
        });
    }

    class loadLogin extends AsyncTask<Void, Integer, User> {

        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage(getString(R.string.text_loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected User doInBackground(Void... params) {
            // TODO Auto-generated method stub

            User user;
            user = AuditUtil.userLogin(userLogin, passwordLogin, simSNLogin);

            return user;

        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(User user) {
            // dismiss the dialog once product deleted

            if( user.getId() == 0) {
                Toast.makeText(activity, R.string.message_login_error, Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            } else {
                userRepo.deleteAll();
                if (!ckSavePassword.isChecked()){
                    user.setPassword("");
                }
                userRepo.create(user);
                session.createLoginSession(user.getFullname().toString(), user.getEmail(), String.valueOf(user.getId()));

                pDialog.dismiss();
                AsyncTask syncData = new SyncData(activity, user.getId(), company_id, new SyncData.AsyncResponse() {
                    @Override
                    public void processFinish(Boolean output) {
                         Intent i = new Intent(activity, PanelAdminActivity.class);
                         startActivity(i);
                         finish();
                         Toast.makeText(activity, R.string.message_login_success, Toast.LENGTH_LONG).show();
                    }
                }).execute();

            }


        }


        @Override
        protected void onProgressUpdate(Integer... values) {

            //super.onProgressUpdate(values);
//            switch (values[10]) {
//
//
//                case 10:
//                    pDialog.setMessage(getString(R.string.text_load_route));
//                case 20:
//                    pDialog.setMessage(getString(R.string.text_load_stores));
//            }
        }
    }
}
