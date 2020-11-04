package com.thdz.ywqx.ui.Activity.station;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.MonitorUnitAdapter;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.UnitBean;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.VUtils;

import java.util.List;

import butterknife.BindView;

/**
 * 单元列表页面---用于告警历史搜索
 */
public class UnitListActivity extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;

    private MonitorUnitAdapter unitAdapter;
    private List<UnitBean> unitList;
    private String StnId; // 站点id
    private String tip = "没有相关的监控单元";
    private boolean isFromSearch; // 是否用于搜索

    Intent mIntent;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_monitor);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("监控单元列表");
        setBackActive();
        setRightTopGone();
        mIntent = getIntent();
        StnId = mIntent.getStringExtra("StnId");
        isFromSearch = mIntent.getBooleanExtra("isFromSearch", false);

        loadInit();

        // TODO Fragment页面 setResult，或者Activity页面 setResult
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (VUtils.isFastDoubleClick()) {
                    return;
                } else {
                    UnitBean item = unitList.get(position);
                    if (isFromSearch) {
                        mIntent.putExtra("UnitId", item.getUnitId());
                        mIntent.putExtra("UnitName", item.getUnitName());
                        setResult(Activity.RESULT_OK, mIntent);
                        finish();
                        return;
                    }

                    Intent intent = new Intent(context, UnitDetailActivity.class);
                    intent.putExtra("id", item.getUnitId());
                    intent.putExtra("name", item.getUnitName());
                    intent.putExtra("bean", item);
                    startActivity(intent);
                }
            }
        });

        unitAdapter = new MonitorUnitAdapter(context);
        listview.setAdapter(unitAdapter);

        RequestData();
    }

    @Override
    public void cycleRequest(){
    }

    private void RequestData() {
        try {
            unitList = DataUtils.getUnitListByStnId(StnId);
            unitAdapter.setDataList(unitList);
            unitAdapter.notifyDataSetChanged();
            loadOK();
        } catch (Exception e) {
            e.printStackTrace();
            toast(tip);
        }
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
