package com.myproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myproject.R;
import com.myproject.model.ModelJenisSurat;
import com.myproject.model.ModelSuratSaya;

import java.util.ArrayList;

public class AdapterSuratSaya extends RecyclerView.Adapter<AdapterSuratSaya.ViewHolder> {
    private ArrayList<ModelSuratSaya> mDataSet;
    private Context mContext;
    ClickListener listener;

    public AdapterSuratSaya(Context context, ArrayList<ModelSuratSaya> DataSet, ClickListener listener){
        mDataSet = DataSet;
        mContext = context;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView icon_request;
        public ViewHolder(View v){
            super(v);
            name = (TextView) v.findViewById(R.id.single_nama);
            icon_request = (ImageView) v.findViewById(R.id.icon_request);
        }
    }

    @Override
    public AdapterSuratSaya.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_jenis_surat,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        if (mDataSet.get(position).getApprove().equals("1")){
            /*holder.foto_single_avatar.setVisibility(View.GONE);*/
            holder.icon_request.setVisibility(View.VISIBLE);
            holder.icon_request.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_download_blue));
        }else{
            /*holder.foto_single_avatar.setVisibility(View.VISIBLE);*/
            holder.icon_request.setVisibility(View.GONE);
        }
        holder.name.setText(mDataSet.get(position).getJenis_surat());
        holder.icon_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDownload(mDataSet.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface ClickListener {
        void onDownload(ModelSuratSaya item);
    }
}
