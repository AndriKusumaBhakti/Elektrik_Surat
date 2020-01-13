package com.myproject.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.myproject.R;
import com.myproject.base.OnActionbarListener;
import com.myproject.util.Constants;
import com.myproject.util.LogTrackContent;
import com.myproject.util.StringUtil;

public class DetailContentFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private String url, title;

    WebView web;
    ProgressBar progressBar;

    public DetailContentFragment() {

    }

    public static DetailContentFragment newInstance(){
        return newInstance("","");
    }

    public static DetailContentFragment newInstance(String param1, String param2) {
        DetailContentFragment fragment = new DetailContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            url = getArguments().getString(Constants.URL);
            title = getArguments().getString(Constants.TITLE);
        }
    }

    @Override
    public void initView(View view) {
        web = (WebView) view.findViewById(R.id.loadView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);

        LogTrackContent.setViewEvent(StringUtil.capitalizeAllString(title)+" CONTENT","MORE","CONTENT-1");
    }

    @Override
    public void setUICallbacks() {
        getBaseActivity().setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                getFragmentManager().popBackStack();
            }

            @Override
            public void onRightIconClick() {

            }

            @Override
            public void onRight2IconClick() {

            }
        });
    }

    @Override
    public void updateUI() {
        getBaseActivity().setLeftIcon(R.drawable.back_white);
        getBaseActivity().setRightIcon2(0);
        getBaseActivity().setRightIcon(0);
        getBaseActivity().showDisplayLogoTitle(false);
        getBaseActivity().changeHomeToolbarBackground(true);

        web.setWebViewClient(new myWebClient());
        web.loadUrl(url);

        web.clearCache(true);
        getBaseActivity().deleteDatabase("webview.db");
        getBaseActivity().deleteDatabase("webviewCache.db");
    }

    @Override
    public String getPageTitle() {
        return title;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_detail_ads;
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}
