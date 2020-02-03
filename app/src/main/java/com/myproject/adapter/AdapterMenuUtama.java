package com.myproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myproject.R;

public class AdapterMenuUtama extends RecyclerView.Adapter<AdapterMenuUtama.ViewHolder> {
    private String[] mDataSet;
    private Integer[] mThumbIds;
    private Context mContext;
    ClickListener listener;

    public AdapterMenuUtama(Context context, String[] DataSet, Integer[] mThumbIds, ClickListener listener){
        this.mDataSet = DataSet;
        this.mThumbIds = mThumbIds;
        this.mContext = context;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView icon_request;
        public TextView txtNama;
        public ViewHolder(View v){
            super(v);
            icon_request = (ImageView) v.findViewById(R.id.icon_request);
            txtNama = (TextView) v.findViewById(R.id.txtNama);
        }
    }

    @Override
    public AdapterMenuUtama.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_menu_utama,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        holder.txtNama.setText(String.valueOf(mDataSet[position]));
        holder.icon_request.setImageResource(mThumbIds[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCLick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    public interface ClickListener {
        void onCLick(int posisi);
    }
}
