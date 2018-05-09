package com.dataservicios.ttauditalicorppreventagalleta.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.adapter.RouteAdapterRecyclerView;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Route;
import com.dataservicios.ttauditalicorppreventagalleta.repo.CompanyRepo;
import com.dataservicios.ttauditalicorppreventagalleta.repo.RouteRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;


public class RouteFragment extends Fragment {
    private static final String LOG_TAG = RouteFragment.class.getSimpleName();

    private SessionManager              session;
    private ProgressDialog pDialog;
    private int                         user_id;
    private RouteRepo                   routeRepo ;
    private RouteAdapterRecyclerView    routeAdapterRecyclerView;
    private RecyclerView                routeRecycler;
    private int                         company_id;
    private Company                     company;
    private CompanyRepo                 companyRepo;

    public RouteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        session = new SessionManager(getActivity());
        HashMap<String, String> userSesion = session.getUserDetails();
        user_id = Integer.valueOf(userSesion.get(SessionManager.KEY_ID_USER)) ;

        DatabaseManager.init(getActivity());

        companyRepo = new CompanyRepo(getActivity());
        ArrayList<Company> companies = (ArrayList<Company>) companyRepo.findAll();
        company = (Company)  companies.get(0);
        company_id = company.getId();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        //RecyclerView routeRecycler  = (RecyclerView) view.findViewById(R.id.routeRecycler);
        routeRecycler  = (RecyclerView) view.findViewById(R.id.routeRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        routeRecycler.setLayoutManager(linearLayoutManager);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"fff", Toast.LENGTH_SHORT).show();
            }
        };


        routeRepo = new RouteRepo(getActivity());


        ArrayList<Route> routes = (ArrayList<Route>) routeRepo.findAll();
        for (Route r: routes) {
            routeRepo.create(r);
        }

        routeAdapterRecyclerView =  new RouteAdapterRecyclerView(routes, R.layout.cardview_router, getActivity());
        routeRecycler.setAdapter(routeAdapterRecyclerView);

        // new loadRoute().execute();


        return view;
    }



//    class loadRoute extends AsyncTask<Void, String, ArrayList<Route>> {
//
//        /**
//         * Antes de comenzar en el hilo determinado, Mostrar progresión
//         * */
//        boolean failure = false;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage(getString(R.string.text_loading));
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected ArrayList<Route> doInBackground(Void... params) {
//            // TODO Auto-generated method stub
//            return AuditUtil.getListRoutes(user_id,company_id);
//
//        }
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(ArrayList<Route> routes) {
//
//            //RouteAdapterRecyclerView routeAdapterRecyclerView =  new RouteAdapterRecyclerView(routes, R.layout.cardview_router, getActivity());
//            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(getContext(),"fff",Toast.LENGTH_SHORT).show();
//                }
//            };
//
//            if (routes.size() > 0 )  {
//
//                routeRepo = new RouteRepo(getActivity());
//                routeRepo.deleteAll();
//
//                for (Route r: routes) {
//                    routeRepo.create(r);
//                }
//
//                routeAdapterRecyclerView =  new RouteAdapterRecyclerView(routes, R.layout.cardview_router, getActivity());
//                routeRecycler.setAdapter(routeAdapterRecyclerView);
//
//            } else  {
//
//                Toast.makeText(getActivity() , R.string.message_no_found, Toast.LENGTH_LONG).show();
//
//            }
//
//
//            pDialog.dismiss();
//
//
//        }
//
//    }

    @Override
    public void onResume() {
        super.onResume();


    }


}
