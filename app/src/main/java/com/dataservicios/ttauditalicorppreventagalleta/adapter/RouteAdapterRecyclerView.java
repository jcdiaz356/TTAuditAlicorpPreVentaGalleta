package com.dataservicios.ttauditalicorppreventagalleta.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Route;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.view.StoresActivity;

import java.util.ArrayList;

/**
 * Created by jcdia on 30/04/2017.
 */

public class RouteAdapterRecyclerView extends RecyclerView.Adapter<RouteAdapterRecyclerView.RouteViewHolder> {
    private ArrayList<Route> routes;
    private int                 resource;
    private Activity activity;
    private ProgressDialog pDialog;
    private int                 route_id;
    private int                 company_id;
    private Company             company;
    private CompanyRepo         companyRepo;


    public RouteAdapterRecyclerView(ArrayList<Route> routes, int resource , Activity activity) {
        this.routes = routes;
        this.resource = resource;
        this.activity = activity;
        DatabaseManager.init(activity);
        companyRepo = new CompanyRepo(activity);
        ArrayList<Company> companies = (ArrayList<Company>) companyRepo.findAll();
        company = (Company)  companies.get(0);
        company_id = company.getId();

    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        DatabaseManager.init(activity);

        view.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v)
            {
                //action
                //Toast.makeText(activity,"dfgdfg",Toast.LENGTH_SHORT).show();
            }
        });

        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        final Route route = routes.get(position);

        holder.tvId.setText(String.valueOf(route.getId()));
        holder.tvFullName.setText(route.getFullname());
        holder.tvTotalStores.setText( String.valueOf(route.getTotal_store()));
        holder.tvAudits.setText(String.valueOf(route.getAudit()));

        holder.btShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "ID Ruta: " + route.getId() + " \n Tienda: " + route.getFullname()  ;
                String shareSub = "Ruta";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_TITLE, shareBody);
                activity.startActivity(Intent.createChooser(sharingIntent, "Share using"));

            }
        });

        holder.btShowStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                route_id = route.getId();
                //Toast toast= Toast.makeText(getActivity(), selected, Toast.LENGTH_SHORT).show();
//                Bundle bundle = new Bundle();
//                bundle.putInt("route_id", Integer.valueOf(route_id));
//                Intent intent = new Intent(activity,StoresActivity.class);
//                intent.putExtras(bundle);
//                activity.startActivity(intent);



//                StoreRepo storeRepo = new StoreRepo(activity);
//                ArrayList<Store> stores = (ArrayList<Store>) storeRepo.findAll();
//
//                for (Store s: stores){
//                    storeRepo.create(s);
//                }

                Bundle bundle = new Bundle();
                bundle.putInt("route_id", Integer.valueOf(route_id));
                Intent intent = new Intent(activity,StoresActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);

               // new loadStores().execute(route_id);

            }
        });

    }




    @Override
    public int getItemCount() {
        return routes.size();
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId;
        private TextView tvFullName;
        private TextView tvTotalStores;
        private TextView tvAudits;
        private Button btShared;
        private Button btShowStores;


        public RouteViewHolder(View itemView) {
            super(itemView);
            tvId            = (TextView) itemView.findViewById(R.id.tvId);
            tvFullName      = (TextView) itemView.findViewById(R.id.tvFullName);
            tvTotalStores   = (TextView) itemView.findViewById(R.id.tvTotalStores);
            tvAudits        = (TextView) itemView.findViewById(R.id.tvAudits);
            btShared        = (Button) itemView.findViewById(R.id.btShared);
            btShowStores    = (Button) itemView.findViewById(R.id.btShowStores);
        }


    }


//    class loadStores extends AsyncTask<Integer, String, ArrayList<Store>> {
//
//        /**
//         * Antes de comenzar en el hilo determinado, Mostrar progresión
//         * */
//        boolean failure = false;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(activity);
//            pDialog.setMessage(activity.getString(R.string.text_loading));
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected ArrayList<Store> doInBackground(Integer... params) {
//            // TODO Auto-generated method stub
//
//            Integer route_id= params[0];
//            return AuditUtil.getListStores(route_id,company_id);
//
//        }
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(ArrayList<Store> stores) {
//
//            //RouteAdapterRecyclerView storeAdapterRecyclerView =  new RouteAdapterRecyclerView(routes, R.layout.cardview_router, getActivity());
//
//
//            if (stores.size() > 0 )  {
//
//                DatabaseManager.init(activity);
//
//                StoreRepo storeRepo = new StoreRepo(activity);
//                storeRepo.deleteAll();
//
//                for (Store s: stores){
//                    storeRepo.create(s);
//                }
//
//                Bundle bundle = new Bundle();
//                bundle.putInt("route_id", Integer.valueOf(route_id));
//                Intent intent = new Intent(activity,StoresActivity.class);
//                intent.putExtras(bundle);
//                activity.startActivity(intent);
//
//
//               // Toast.makeText(activity , String.valueOf(route_id), Toast.LENGTH_LONG).show();
//
//            } else  {
//                Toast.makeText(activity , R.string.message_no_found, Toast.LENGTH_LONG).show();
//            }
//            pDialog.dismiss();
//
//        }
//
//
//    }
}
