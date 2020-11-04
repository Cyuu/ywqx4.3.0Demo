package com.thdz.ywqx.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.MonitorUnitAdapter;
import com.thdz.ywqx.base.BaseLazyFragment;
import com.thdz.ywqx.bean.StationBean;
import com.thdz.ywqx.bean.UnitBean;
import com.thdz.ywqx.bean.UnitDetailStatusBean;
import com.thdz.ywqx.ui.Activity.station.UnitDetailActivity;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.VUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 局--线路--站点---<b><font color="red"> 单元列表 </font></b>
 * 在站点详情页的第一个Fragment
 */
public class UnitListFragment extends BaseLazyFragment {

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;

    @BindView(R.id.listview)
    ListView listview;

    private MonitorUnitAdapter unitAdapter;
    private List<UnitBean> mUnitList;

    private List<UnitDetailStatusBean> stateList = null; // 用于设置监控单元的连接状态：连接，掉线

    private StationBean stnBean;

    private String RBId;
    private String StnId; // 站点id

    private String tip = "没有可以查看的监控单元";

    @Override
    public void cycleRequest() {
    }

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup arg1, Bundle arg2) {
        return inflater.inflate(R.layout.fragment_unit, arg1, false);
    }

    @Override
    public void initView(Bundle savedInstanceState, View view, LayoutInflater inflater) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            stnBean = (StationBean) bundle.getSerializable("stnBean");
        }

        RBId = stnBean.getRBId();
        StnId = stnBean.getStnId();

        loadInit();

        swipe_layout.setRefreshing(false);
        VUtils.setSwipeColor(swipe_layout);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestUnitState();
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (VUtils.isFastDoubleClick()) {
                    return;
                } else {
                    UnitBean item = mUnitList.get(position);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("unitBean", item);

                    Intent intent = new Intent(context, UnitDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        unitAdapter = new MonitorUnitAdapter(context);
        listview.setAdapter(unitAdapter);

        isPrepared = true;
//        lazyLoad();
    }

    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        // 处理
        RequestData();
        isPrepared = false; // 已加载过不再重复加载
    }

    private void RequestData() {
        try {
            mUnitList = DataUtils.getUnitListByStnId(StnId);
            if (mUnitList == null || mUnitList.isEmpty()) {
                toast("该监控点没有监控单元");
                loadFail();
                return;
            }
            unitAdapter.setDataList(mUnitList);
            unitAdapter.notifyDataSetChanged();
            loadOK();
            RequestUnitState();
        } catch (Exception e) {
            e.printStackTrace();
            loadFail();
            toast(tip);
        }
    }


    /**
     * 状态信息
     */
    private void RequestUnitState() {
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetUnitStatusByIds, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                swipe_layout.setRefreshing(false);
//                toast("监控单元状态信息获取失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                swipe_layout.setRefreshing(false);
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "监控单元列表状态信息，返回值：" + value);
                    stateList = JSONArray.parseArray(DataUtils.getReturnData(value), UnitDetailStatusBean.class);
                    Log.i(TAG, "stateList 长度 = " + stateList.size());
                    unitAdapter.setStateList(stateList);
                    unitAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
//                    toast("监控单元状态信息获取失败");
                }
            }
        });
    }

    /**
     * nStnId、nPcdtId、nUnitId,有效，其他传0
     */
    private String createParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("nStnId", StnId);
            jsonObj.put("nPcdtId", "0"); // itemBean.getPcdtId()
            jsonObj.put("nUnitId", "0");

            jsonObj.put("nAppId", "0");
            jsonObj.put("nRBId", RBId);
            jsonObj.put("nRLId", "0");
            jsonObj.put("nRSId", "0");
            jsonObj.put("nRWIId", "0");

            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
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
