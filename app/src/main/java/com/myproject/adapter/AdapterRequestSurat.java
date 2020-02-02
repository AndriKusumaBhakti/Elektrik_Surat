package com.myproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myproject.R;
import com.myproject.model.ModelRequestSurat;
import com.myproject.util.StringUtil;

import java.util.ArrayList;

public class AdapterRequestSurat extends RecyclerView.Adapter<AdapterRequestSurat.ViewHolder> {
    private ArrayList<ModelRequestSurat> mDataSet;
    private Context mContext;
    ClickListener listener;

    public AdapterRequestSurat(Context context, ArrayList<ModelRequestSurat> DataSet, ClickListener listener){
        mDataSet = DataSet;
        mContext = context;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView onApprove;
        public ImageView onReject;
        public TextView single_nama;
        public TextView single_alamat;
        public TextView single_jenis;
        public ViewHolder(View v){
            super(v);
            onApprove = (ImageView) v.findViewById(R.id.onApprove);
            onReject = (ImageView) v.findViewById(R.id.onReject);
            single_nama = (TextView) v.findViewById(R.id.single_nama);
            single_alamat = (TextView) v.findViewById(R.id.single_alamat);
            single_jenis = (TextView) v.findViewById(R.id.single_jenis);
        }
    }

    @Override
    public AdapterRequestSurat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_single_layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        if (StringUtil.checkNullString(mDataSet.get(position).getStatus_surat()).isEmpty()){
            holder.onApprove.setVisibility(View.VISIBLE);
            holder.onReject.setVisibility(View.VISIBLE);
        }else {
            if (mDataSet.get(position).getStatus_surat().equals("1")) {
                /*holder.foto_single_avatar.setVisibility(View.GONE);*/
                holder.onApprove.setVisibility(View.GONE);
                holder.onReject.setVisibility(View.GONE);
            } else {
                /*holder.foto_single_avatar.setVisibility(View.VISIBLE);*/
                holder.onApprove.setVisibility(View.GONE);
                holder.onReject.setVisibility(View.GONE);
            }
        }
        holder.single_nama.setText(String.valueOf(mDataSet.get(position).getNama()+" ("+mDataSet.get(position).getNik_penduduk()+")"));
        holder.single_alamat.setText(String.valueOf(mDataSet.get(position).getAlamat()));
        holder.single_jenis.setText(String.valueOf(mDataSet.get(position).getJenis_surat()));
        holder.onApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onApprove(mDataSet.get(position));
            }
        });
        holder.onReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReject(mDataSet.get(position));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onView(mDataSet.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface ClickListener {
        void onApprove(ModelRequestSurat item);
        void onReject(ModelRequestSurat item);
        void onView(ModelRequestSurat item);
    }
}
