package com.myproject.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.myproject.R;
import com.myproject.adapter.SpinnerAdapter;
import com.myproject.util.ScreenUtil;
import com.myproject.util.StringUtil;

import java.util.ArrayList;

public class SelectionDialogFragment extends DialogFragment {

    public interface SelectionDialogFragmenListener{
        public void onSelection(String selection);
    }

    private SelectionDialogFragmenListener listener;

     SpinnerAdapter adapter;
     TextView theTitle;
     ListView listView;
     TextView cancelBtn;
     Dialog dialog;

    protected String title;
    protected ArrayList<String> contentString;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItems(ArrayList<String> StringOfData, SelectionDialogFragmenListener listener) {
        this.listener = listener;
        this.contentString = StringOfData;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        dialog = new Dialog(getActivity(), R.style.AppTheme_FullScreenDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.spinner_dialog_box);
        dialog.show();

        initViews();

        if(contentString == null){
            contentString = new ArrayList<>();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listener != null) {
                    String selectedSTring = contentString.get(i);
                    listener.onSelection(selectedSTring);
                    dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();
            }
        });

        adapter = new SpinnerAdapter(getActivity(),
                R.layout.spinner_row_layout, contentString);
        listView.setAdapter(adapter);
        theTitle.setText(StringUtil.checkNullString(title));

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixelsDp = (int) (210 * scale + 0.5f);

        int pixels = ScreenUtil.getListViewHeightBasedOnChildrenWithMaxSize(listView, pixelsDp);

        int margins = (int) (10 * scale + 0.5f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixels);
        params.setMargins(0, margins, 0, margins);

        listView.setLayoutParams(params);

        return dialog;
    }

    private void initViews() {
        listView = (ListView) dialog.findViewById(R.id._lsvw_11);
        theTitle = (TextView) dialog.findViewById(R.id._txvw_header);
        cancelBtn = (TextView) dialog.findViewById(R.id.close_x_text);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
