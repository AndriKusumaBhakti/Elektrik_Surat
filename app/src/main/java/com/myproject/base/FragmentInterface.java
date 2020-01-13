package com.myproject.base;

import android.view.View;

public interface FragmentInterface {
    public void initView(View view);
    public void setUICallbacks();
    public void updateUI();
    public String getPageTitle();
    public int getFragmentLayout();
    public String getTag();
}
