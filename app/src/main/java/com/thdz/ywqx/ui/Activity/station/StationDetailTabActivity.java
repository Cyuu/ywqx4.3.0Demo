package com.thdz.ywqx.ui.Activity.station;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.thdz.ywqx.R;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.StationBean;
import com.thdz.ywqx.ui.fragment.StationDetailFragment;
import com.thdz.ywqx.ui.fragment.UnitListFragment;
import com.thdz.ywqx.util.VUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 站点详情页页面，包含三个fragment<br/>
 * 1 该站点的单元列表；2 该单元的控制页面，3该站点的球机IPC列表
 */
public class StationDetailTabActivity extends BaseActivity {

    /**
     * 顶部tab的父布局
     */
    @BindView(R.id.tablayout)
    TabLayout tablayout;

    @BindView(R.id.stn_detail_vp)
    ViewPager stn_detail_vp;

    private Bundle bundle;
    private StationBean stnBean;

    private FragmentManager fragmentManager;

    private StationDetailFragment stnFragment;
    private UnitListFragment listFragment;
//    private IPCListFragment ipcFragment; // TODO 没有视频模块，先屏蔽IPC

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private String[] titles = {"站点详情", "单元列表"}; // , "IPC列表"  // R.array.titles

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_station_tab_detail);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        bundle = getIntent().getExtras();
        if (bundle != null) {
            stnBean = (StationBean) bundle.getSerializable("stnBean");
            String title = stnBean.getStnName();
            setTitle(title);
        }

        setBackActive();
        setRightTopGone();


        fragmentManager = getSupportFragmentManager();
        initFragments();

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        StnPagerAdapter mAdapter = new StnPagerAdapter(fragmentManager);
        stn_detail_vp.setAdapter(mAdapter);//给ViewPager设置适配器
        stn_detail_vp.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(stn_detail_vp);//将TabLayout和ViewPager关联起来。

    }

    private void initFragments() {

        stnFragment = new StationDetailFragment();
        stnFragment.setArguments(bundle);
        listFragment = new UnitListFragment();
        listFragment.setArguments(bundle);

        mFragments.add(stnFragment);
        mFragments.add(listFragment);

    }

    /**
     * 适配器
     */
    class StnPagerAdapter extends FragmentPagerAdapter {

        public StnPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }


        @Override
        public int getCount() {
            return mFragments.size();
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            Fragment fragment = null;
//            switch (position) {
//                case 0:
//                    fragment = (UnitListFragment) instantiateItem(container, position);
//                    break;
//                case 1:
//                    fragment = (StationDetailFragment) instantiateItem(container, position);
//                    break;
//                case 2:
//                    fragment = (IPCListFragment) instantiateItem(container, position);
//                    break;
//            }
//            return fragment;
//        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
        }

    }

    @Override
    public void cycleRequest() {
    }

    @Override
    public void onClick(View v) {
        if (VUtils.isFastDoubleClick()) {
            return;
        } else {
            switch (v.getId()) {
                default:
                    break;
            }
        }
    }

}
