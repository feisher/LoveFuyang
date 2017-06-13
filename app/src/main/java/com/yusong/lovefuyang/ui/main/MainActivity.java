package com.yusong.lovefuyang.ui.main;

import android.widget.TextView;

import com.yusong.configlibrary.mvp.MVPBaseActivity;
import com.yusong.lovefuyang.R;

import butterknife.BindView;

public class MainActivity extends MVPBaseActivity<MainContract.View, MainPresenter> implements MainContract.View {

    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }
}
