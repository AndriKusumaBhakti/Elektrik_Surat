package com.myproject.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.myproject.activity.BaseActivity;
import com.myproject.aplication.APP;
import com.myproject.base.FragmentInterface;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment extends Fragment implements FragmentInterface {

    private LinearLayout backButton;
    protected List<AsyncTask> threadList;
    private WeakReference<Activity> wrActivity;
    protected Activity activity;

    final protected static String DIALOG_FRAGMENT_FLAG = "DF_FLAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initView(view);
        /*if (view.findViewById(R.id.imbn_back) != null) {
            backButton = (LinearLayout) view.findViewById(R.id.imbn_back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }*/
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        wrActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
            wrActivity = new WeakReference<Activity>((Activity) context);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        APP.log("CREATED  WITH : " + getPageTitle());
        getBaseActivity().setDefaultActionbarIcon();
        updateUI();
        setUICallbacks();
        getBaseActivity().setActionBarTitle(getPageTitle());
    }

    public BaseActivity getBaseActivity() {
        return ((BaseActivity) activity);
    }

    @Override
    public void onStop() {
        Fragment f = getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_FLAG);
        if (f != null) {
            if (isFragmentSafety()) {
                DialogFragment df = (DialogFragment) f;
                df.dismiss();
                getFragmentManager().beginTransaction().remove(f).commit();
            }
        }

        //kill all thread when onstop being called
        if (threadList != null) {
            for (AsyncTask task : threadList) {
                if (task != null) {
                    try {
                        if (task.getStatus() == AsyncTask.Status.RUNNING) {
                            task.cancel(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            threadList.clear();
        }

        super.onStop();
    }

    public Activity getWeakReferenceActivity() {
        if (wrActivity == null) {
            return null;
        } else {
            return wrActivity.get();
        }
    }

    public String checkNullString(String string) {
        return (string == null) ? "" : string;
    }

    public boolean isFragmentSafety() {
        if (getWeakReferenceActivity() != null) {
            if (!getWeakReferenceActivity().isFinishing() && isAdded()) {
                return true;
            }
        }
        return false;
    }

    public void registerTask(AsyncTask task) {
        if (threadList == null) {
            threadList = new ArrayList<>();
        }
        threadList.add(task);
    }

    public void removeTask(AsyncTask task) {
        if (threadList != null) {
            threadList.remove(task);
        }
    }
}
