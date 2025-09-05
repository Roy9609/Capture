package com.roy.capture.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public abstract class CaptureBaseFragment extends Fragment {


    private View vRootView;
    private boolean mIsNeedInit = false;


    protected FragmentActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (FragmentActivity) context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (vRootView == null) {
            mIsNeedInit = true;
            vRootView = LayoutInflater.from(activity).inflate(inflateBaseLayoutRes(), container, false);

        } else {
            mIsNeedInit = false;
            ViewGroup parent = (ViewGroup) vRootView.getRootView().getParent();
            if (parent != null) {
                parent.removeView(vRootView.getRootView());
            }
        }
        return vRootView.getRootView();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsNeedInit) {
            initConfig(view);
            initView(view);
            initEvents();
            initData();
        }
    }



    protected abstract void initView(View view);

    protected abstract void initEvents();

    protected abstract void initData();



    /**
     * 填充的基础布局文件
     *
     * @return 布局资源res
     */
    protected abstract int inflateBaseLayoutRes();


    /**
     * 初始化相关配置
     *
     * @param view
     */
    protected abstract void initConfig(View view);


}
