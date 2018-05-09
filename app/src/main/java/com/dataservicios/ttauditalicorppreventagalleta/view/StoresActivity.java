package com.dataservicios.ttauditalicorppreventagalleta.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.adapter.StoreAdapterReciclerView;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Route;
import com.dataservicios.ttauditalicorppreventagalleta.model.Store;
import com.dataservicios.ttauditalicorppreventagalleta.repo.RouteRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.StoreRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class StoresActivity extends AppCompatActivity {
    private static final String LOG_TAG = StoresActivity.class.getSimpleName();

    private SessionManager              session;
    private Activity activity =  this;
    private TextView tvFullName;
//    private TextView tvId;
    private TextView tvTotalStores;
//    private TextView tvAudits;
    private ProgressDialog pDialog;
    private int                         user_id;
    private int                         route_id;
    private RouteRepo                   routeRepo ;
    private StoreRepo                   storeRepo ;
    private StoreAdapterReciclerView    storeAdapterRecyclerView;
    private RecyclerView                storeRecycler;
    private Route                       route ;
    private ArrayList<Store> stores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        showToolbar(getString(R.string.title_activity_Stores_search),true);

        DatabaseManager.init(this);

        Bundle bundle = getIntent().getExtras();
        route_id = bundle.getInt("route_id");

        session = new SessionManager(activity);
        HashMap<String, String> userSesion = session.getUserDetails();
        user_id = Integer.valueOf(userSesion.get(SessionManager.KEY_ID_USER)) ;

        tvFullName      = (TextView) findViewById(R.id.tvFullName);
        tvTotalStores   = (TextView) findViewById(R.id.tvTotalStores);



        routeRepo = new RouteRepo(activity);
        route = (Route) routeRepo.findById(route_id);


        storeRecycler  = (RecyclerView) findViewById(R.id.storeRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        storeRecycler.setLayoutManager(linearLayoutManager);

       // new loadStores().execute();

        storeRepo = new StoreRepo(activity);
        //stores = (ArrayList<Store>) storeRepo.findByRouteId(route_id);
        stores = (ArrayList<Store>) storeRepo.findAll();

        if (stores.size() > 0) {
//            tvFullName.setText(R.string.text_search_store);
//            tvId.setText(String.valueOf(route.getId()));
            tvTotalStores.setText(getString(R.string.text_total_stores_found) + " " + String.valueOf(stores.size()));
//            tvAudits.setText(String.valueOf(route.getAudit()));
        }

        storeAdapterRecyclerView =  new StoreAdapterReciclerView(stores, R.layout.cardview_store, activity);
        storeRecycler.setAdapter(storeAdapterRecyclerView);

        FabSpeedDial fabMenuMaps = (FabSpeedDial) findViewById(R.id.fabMenuMaps);
        fabMenuMaps.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                // TODO: Do something with yout menu items, or return false if you don't want to show them
                return true;
            }
        });


        //FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        fabMenuMaps.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.action_location:
                       // Toast.makeText(activity, menuItem.getTitle().toString() , Toast.LENGTH_SHORT).show();
                        //                int route_id = store.getId();
                        //Toast.makeText(activity, String.valueOf(route_id), Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("route_id", Integer.valueOf(route_id));
                        Intent intent = new Intent(activity,MapsRouteActivity.class);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        break;
                    case R.id.action_location_all:
                        Toast.makeText(activity, menuItem.getTitle().toString() , Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(activity, menuItem.getTitle().toString() , Toast.LENGTH_SHORT).show();
                        break;


                    default:
                        break;
                }
                return false;
            }
        });


    }



    public void showToolbar(String title, boolean upButton){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //storeAdapterRecyclerView.getFilter().filter(newText.toString());
                final ArrayList<Store> filteredMStoreList = filter(stores, newText);
                //adapter.setFilter(filteredModelList);
                storeAdapterRecyclerView.setFilter(filteredMStoreList);
                return false;
            }
        });
        return true;
    }

    private ArrayList<Store> filter(ArrayList<Store> models, String query) {

        query = query.toLowerCase();
        final ArrayList<Store> filteredModelList = new ArrayList<>();
        for (Store s : models) {
            final String fullName = s.getFullname().toLowerCase().trim();
            final String store_id = String.valueOf(s.getId()).toLowerCase().trim();
            if (fullName.contains(query) || store_id.contains(query)) {
                filteredModelList.add(s);
            }

//            if (s.getFullname().toUpperCase().trim().contains(query.toString().toUpperCase().trim())) {
//                        //fRecords.add(s);
//                        //fRecords.add(s);
//                filteredModelList.add(s);
//                    }
        }
        return filteredModelList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                //showSnackBar("Comenzar a buscar...");
                return true;

        }

        return super.onOptionsItemSelected(item);
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
