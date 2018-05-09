package com.dataservicios.ttauditalicorppreventagalleta;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.adapter.NavDrawerListAdapter;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.NavDrawerItem;
import com.dataservicios.ttauditalicorppreventagalleta.model.User;
import com.dataservicios.ttauditalicorppreventagalleta.repo.AuditRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.MediaRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.OrderDetailRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.OrderRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.PollOptionRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.PollRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.ProductRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.UserRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.BitmapLoader;
import com.dataservicios.ttauditalicorppreventagalleta.util.GPSTracker;
import com.dataservicios.ttauditalicorppreventagalleta.util.GlobalConstant;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;
import com.dataservicios.ttauditalicorppreventagalleta.util.SyncData;
import com.dataservicios.ttauditalicorppreventagalleta.view.MarkCardActivity;
import com.dataservicios.ttauditalicorppreventagalleta.view.fragment.CreateStoreFragment;
import com.dataservicios.ttauditalicorppreventagalleta.view.fragment.MediasFragment;
import com.dataservicios.ttauditalicorppreventagalleta.view.fragment.RouteFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PanelAdminActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Activity activity;
    private SessionManager              session;
    private DrawerLayout mDrawerLayout;
    private CharSequence mTitle;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private String[]                    navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter        adapter;
    private TextView tvUser;
    private TextView tvCampaign;
    private CircleImageView             imcPhoto;
    private int                         user_id;
    private int                         company_id;
    private UserRepo                    userRepo;
    private CompanyRepo                 companyRepo;
    private PollRepo                    pollRepo;
    private PollOptionRepo              pollOptionRepo;
    private ProductRepo                 productRepo;
    private AuditRepo                   auditRepo;
    private User                        user;
    private Fragment fragment;
    private File filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        tvUser      = (TextView) findViewById(R.id.tvUser);
        tvCampaign  = (TextView) findViewById(R.id.tvCampaign);
        //imcPhoto  = (ImageView) findViewById(R.id.imcPhoto);
        imcPhoto    = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.imcPhoto);

        setSupportActionBar(toolbar);
        activity = (Activity) this;

        GPSTracker gpsTracker = new GPSTracker(activity);
        if(!gpsTracker.canGetLocation()){
            gpsTracker.showSettingsAlert();
        }

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> userSesion = session.getUserDetails();
        user_id = Integer.valueOf(userSesion.get(SessionManager.KEY_ID_USER)) ;



        DatabaseManager.init(activity);
        userRepo            = new UserRepo(activity);
        companyRepo         = new CompanyRepo(activity);
        pollRepo            = new PollRepo(activity);
        pollOptionRepo      = new PollOptionRepo(activity);
        auditRepo           = new AuditRepo(activity);
        productRepo         = new ProductRepo(activity);

        Company company = (Company) companyRepo.findFirstReg();

        company_id = company.getId();

        user = new User();
        user = (User) userRepo.findById(user_id);

        tvUser.setText(user.getEmail());
        tvCampaign.setText(company.getFullname());

        Picasso.with(activity)
                .load(GlobalConstant.URL_USER_IMAGES + user.getImage())
                .error(R.drawable.avataruser)
                .into(imcPhoto);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTitle = mDrawerTitle = getTitle();
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId( 0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId( 1 , -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId( 2 , -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId( 3 , -1), true , "0"));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId( 4 , -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId( 5 , -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId( 6 , -1)));

        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.setAdapter(adapter);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                //getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {

                getSupportActionBar().setTitle(mDrawerTitle);
                Log.i(LOG_TAG, String.valueOf(navDrawerItems.get(3).getCount() )) ;
                MediaRepo mediaRepo = new MediaRepo(activity);
                long Total = mediaRepo.countReg();
                navDrawerItems.get(3).setCount(String.valueOf(Total));
                adapter.notifyDataSetChanged();
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            System.exit(0);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            //AlertDialog.Builder builder = new AlertDialog.Builder(this,2);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.text_title_close_app)
                    .setMessage(R.string.message_close_app)
                    .setPositiveButton(R.string.yes, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener).show();
        }
//        super.onBackPressed();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.panel_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        DialogInterface.OnClickListener dialogClickListener;
        AlertDialog.Builder builder;
        switch (item.getItemId()) {

            case R.id.mnuCloseSesion:
                // DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //super.onBackPressed();
                                //session.logoutUser();
                                //finish();
                                session.logoutUser(LoginActivity.class);
                                //finish();
                                // Termina Toda las actividades abiertas
                                //for api 16+ use finishAffinity(); and for api <16 use ActivityCompat.finishAffinity(this); (with import import android.support.v4.app.ActivityCompat)
                                ActivityCompat.finishAffinity(activity);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                //AlertDialog.Builder builder = new AlertDialog.Builder(this,2);
                 builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.text_title_close_sesion)
                        .setMessage(R.string.message_close_sesion)
                        .setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener).show();

                return true;
            case R.id.mnuExit:

                // DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                System.exit(0);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                //AlertDialog.Builder builder = new AlertDialog.Builder(this,2);
                builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.text_title_close_app)
                        .setMessage(R.string.message_close_app)
                        .setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener).show();

                return true;


            case R.id.mnuAbout:
                Intent intent = new Intent(activity,About.class);
                startActivity(intent);
                return true;
            default:
                //return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private class SlideMenuClickListener implements  ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
         fragment = null; //= null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        switch (position) {
            case 0:
//                fragment = new RouteFragment();
                fragment = new CreateStoreFragment();
                break;
            case 1:

                filePath = BitmapLoader.getAlbumDirBackup(activity);
                try {

                    if (filePath.isDirectory()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri myUri = Uri.parse(String.valueOf(filePath));
                        intent.setDataAndType(myUri , "resource/folder");
                        startActivity(intent);
                    } else {
                        Toast.makeText(activity,R.string.message_directory_backup_no_found, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(activity,R.string.message_app_no_installing, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.estrongs.android.pop&hl=es"));
                    startActivity(intent);
                }finally {

                }


                break;
            case 2:
                filePath = BitmapLoader.getAlbumDirTemp(activity);
                try {

                    if (filePath.isDirectory()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri myUri = Uri.parse(String.valueOf(filePath));
                        intent.setDataAndType(myUri , "resource/folder");
                        startActivity(intent);
                    } else {
                        Toast.makeText(activity,R.string.message_directory_temp_no_found, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(activity,R.string.message_app_no_installing, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.estrongs.android.pop&hl=es"));
                    startActivity(intent);
                }finally {

                }

                break;

            case 3:
                fragment = new MediasFragment();
                break;
            case 4:
                builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.message_sync_store);
                builder.setMessage(R.string.message_sync_store_information);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AsyncTask syncData = new SyncData(activity, user_id, company_id, new SyncData.AsyncResponse() {
                            @Override
                            public void processFinish(Boolean output) {

                                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                                    // mDrawerLayout.closeDrawer(mDrawerList); //CLOSE Nav Drawer!
                                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                                }

                                fragment = new RouteFragment();
                                //getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .addToBackStack(null).commit();

                                //onRestart();

                            }
                        }).execute();

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


                break;
            case 5:
                builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.message_sync_campagna);
                builder.setMessage(R.string.message_sync_campagna_information);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        companyRepo.deleteAll();
                        pollRepo.deleteAll();
                        pollOptionRepo.deleteAll();
                        auditRepo.deleteAll();
                        productRepo.deleteAll();

                        OrderRepo orderRepo = new OrderRepo(activity);
                        orderRepo.deleteAll();

                        OrderDetailRepo orderDetailRepo = new OrderDetailRepo(activity);
                        orderDetailRepo.deleteAll();

                        Intent intent = new Intent(activity,MainActivity.class);
                        startActivity(intent);
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
                break;

            case 6:


                        Intent intent = new Intent(activity,MarkCardActivity.class);
                        startActivity(intent);

                break;

            default:
                break;
        }

        if (fragment != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
           //mDrawerLayout.
            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
               // mDrawerLayout.closeDrawer(mDrawerList); //CLOSE Nav Drawer!
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
//            else{
//                mDrawerLayout.openDrawer(mDrawerList); //OPEN Nav Drawer!
//            }
            //mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
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
}
