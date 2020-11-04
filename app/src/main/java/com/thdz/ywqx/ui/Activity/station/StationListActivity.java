package com.thdz.ywqx.ui.Activity.station;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.MonitorStationAdapter;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.StationBean;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.VUtils;

import java.util.List;

import butterknife.BindView;

/**
 * 站点列表页面---用于告警历史搜索
 */
public class StationListActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;

    private MonitorStationAdapter stationAdapter;
    private List<StationBean> stationList;

    private boolean isFromSelect = false;

    private String RBID; // 线路id

    private String tip = "没有可以查看的站点";

    Intent mIntent;
    private boolean isFromSearch;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_monitor);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("监控点列表");
        setBackActive();
        setRightTopGone();
        mIntent = getIntent();
        isFromSearch = mIntent.getBooleanExtra("isFromSearch", false);
        RBID = mIntent.getStringExtra("RBID");
        loadInit();

        stationAdapter = new MonitorStationAdapter(context);
        stationAdapter.setFromSelect(isFromSelect);
        listview.setAdapter(stationAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (VUtils.isFastDoubleClick()) {
                    return;
                } else {
                    StationBean item = stationList.get(position);
                    if (isFromSearch) {
                        mIntent.putExtra("StnId", item.getStnId());
                        mIntent.putExtra("StnName", item.getStnName());
                        setResult(Activity.RESULT_OK, mIntent);
                        finish();
                        return;
                    }

                    Intent intent = new Intent(context, StationDetailTabActivity.class);
                    intent.putExtra("StnId", item.getStnId());
                    intent.putExtra("StnName", item.getStnName());
                    startActivity(intent);
                }
            }
        });

        RequestData();

    }

    @Override
    public void cycleRequest(){
    }

    /**
     * 获取数据 并 处理展示
     */
    public void RequestData() {
        stationList = DataUtils.getStationListByDeptId(RBID);
        if (stationList == null || stationList.isEmpty()) {
            loadFail();
            toast(tip);
            return;
        }

        stationAdapter.setDataList(stationList);
        stationAdapter.notifyDataSetChanged();
        loadOK();
    }


    @Override
    public void onClick(View v) {
        if (VUtils.isFastDoubleClick()) {
            return;
        } else {
            switch (v.getId()) {
                case R.id.common_null_tv:
                    RequestData();
                    break;

                default:
                    break;
            }
        }
    }

}
