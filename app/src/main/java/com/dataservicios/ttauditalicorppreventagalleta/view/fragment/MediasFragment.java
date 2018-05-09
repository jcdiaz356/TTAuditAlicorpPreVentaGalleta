package com.dataservicios.ttauditalicorppreventagalleta.view.fragment;


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
import com.dataservicios.ttauditalicorppreventagalleta.adapter.MediaAdapterReciclerView;
import com.dataservicios.ttauditalicorppreventagalleta.db.DatabaseManager;
import com.dataservicios.ttauditalicorppreventagalleta.model.Media;
import com.dataservicios.ttauditalicorppreventagalleta.repo.MediaRepo;
import com.dataservicios.ttauditalicorppreventagalleta.util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediasFragment extends Fragment {


    private static final String LOG_TAG = MediasFragment.class.getSimpleName();

    private SessionManager              session;
    private int                         user_id;
    private MediaAdapterReciclerView    mediaAdapterRecyclerView;
    private RecyclerView                mediaRecycler;
    private Media                       media;
    private MediaRepo                   mediaRepo;


    public MediasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        session = new SessionManager(getActivity());
        HashMap<String, String> userSesion = session.getUserDetails();
        user_id = Integer.valueOf(userSesion.get(SessionManager.KEY_ID_USER)) ;

        DatabaseManager.init(getActivity());

        mediaRepo = new MediaRepo(getActivity());
        ArrayList<Media> medias = (ArrayList<Media>) mediaRepo.findAll();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medias, container, false);
        //RecyclerView mediaRecycler  = (RecyclerView) view.findViewById(R.id.mediaRecycler);
        mediaRecycler  = (RecyclerView) view.findViewById(R.id.mediaRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mediaRecycler.setLayoutManager(linearLayoutManager);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"fff", Toast.LENGTH_SHORT).show();
            }
        };





        mediaAdapterRecyclerView =  new MediaAdapterReciclerView(medias, R.layout.cardview_media, getActivity());
        mediaRecycler.setAdapter(mediaAdapterRecyclerView);

        // new loadRoute().execute();


        return view;
    }



    @Override
    public void onResume() {
        super.onResume();


    }


}
