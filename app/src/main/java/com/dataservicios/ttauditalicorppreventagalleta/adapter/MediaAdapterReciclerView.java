package com.dataservicios.ttauditalicorppreventagalleta.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dataservicios.ttauditalicorppreventagalleta.R;
import com.dataservicios.ttauditalicorppreventagalleta.model.Media;
import com.dataservicios.ttauditalicorppreventagalleta.util.BitmapLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jcdia on 1/06/2017.
 */

public class MediaAdapterReciclerView extends RecyclerView.Adapter<MediaAdapterReciclerView.MediaViewHolder> {
    private ArrayList<Media> medias;
    private int                 resource;
    private Activity activity;

    public MediaAdapterReciclerView(ArrayList<Media> medias, int resource, Activity activity) {
        this.medias     = medias;
        this.resource   = resource;
        this.activity   = activity;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        view.setOnClickListener(new  View.OnClickListener(){
            public void onClick(View v)
            {
                //action
                //Toast.makeText(activity,"dfgdfg",Toast.LENGTH_SHORT).show();
            }
        });
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {

        final Media media = medias.get(position);

        holder.tvId.setText("ID : " + String.valueOf(media.getId()));
        holder.tvFullName.setText(media.getFile());
        String pathFile = BitmapLoader.getAlbumDirTemp(activity).getAbsolutePath() + "/" + media.getFile() ;
        File imgFile = new File(pathFile);
        Picasso.with(activity)
                .load(imgFile)
                .error(R.drawable.avataruser)
                .into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return medias.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId;
        private TextView tvFullName;
        private ImageView imgPhoto;
        public MediaViewHolder(View itemView) {
            super(itemView);
            tvId            = (TextView)    itemView.findViewById(R.id.tvId);
            tvFullName      = (TextView)    itemView.findViewById(R.id.tvFullName);
            imgPhoto        = (ImageView)   itemView.findViewById(R.id.imgPhoto);
        }
    }
}
