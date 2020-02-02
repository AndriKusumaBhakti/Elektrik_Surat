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
import com.myproject.model.ModelRequestSurat;
import com.myproject.model.response.ResponseJenisSurat;
import com.myproject.util.StringUtil;

import java.util.ArrayList;

public class AdapterJenisSurat extends RecyclerView.Adapter<AdapterJenisSurat.ViewHolder> {
    private ArrayList<ModelJenisSurat> mDataSet;
    private Context mContext;
    ClickListener listener;

    public AdapterJenisSurat(Context context, ArrayList<ModelJenisSurat> DataSet, ClickListener listener){
        mDataSet = DataSet;
        mContext = context;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView form_tambahan;
        public ImageView icon_request;
        public ViewHolder(View v){
            super(v);
            name = (TextView) v.findViewById(R.id.single_nama);
            form_tambahan = (TextView) v.findViewById(R.id.form_tambahan);
            icon_request = (ImageView) v.findViewById(R.id.icon_request);
        }
    }

    @Override
    public AdapterJenisSurat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_jenis_surat,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        holder.name.setText(mDataSet.get(position).getJenis_surat());
        if (StringUtil.checkNullString(mDataSet.get(position).getForm_tambahan()).isEmpty()){
            holder.form_tambahan.setText("");
        }else{
            holder.form_tambahan.setText("Data tambahan ("+mDataSet.get(position).getForm_tambahan()+")");
        }
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
        void onDownload(ModelJenisSurat item);
    }
}
