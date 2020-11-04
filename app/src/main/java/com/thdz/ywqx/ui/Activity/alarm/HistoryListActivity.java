package com.thdz.ywqx.ui.Activity.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.AlarmListAdapter;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.AlarmBean;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.VUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 历史告警搜索结果页面<br/>
 * 1 增加缓存<br/>
 * 2 增加默认选择条件<br/>
 * 3 增加跳转至搜搜页面的入口
 */
public class HistoryListActivity extends BaseActivity {

    @BindView(R.id.swipy_layout)
    SwipyRefreshLayout swipy_layout;// 上下刷新

    @BindView(R.id.listview)
    ListView listview;

    private AlarmListAdapter alarmAdapter;

    private List<AlarmBean> alarmList; //

    private String start_time = ""; // 起始时间值
    private String end_time = ""; // 结束时间值

    private String dept_id = ""; // 线路值
    private String stn_id = ""; // 站点值
    private String unit_id = ""; // 单元值
    private String nHandle = ""; //  处理类型，0：不限，1：未处理，2：已处理
    private String sAlarmType = ""; // 告警类型，0：不限；20：预警，21：小物体告警，22：工务告警，30：异物告警

    private String tip = "没有符合条件的告警信息！";

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_history_list);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();

        start_time = bundle.getString("sDtBegin");
        end_time = bundle.getString("sDtEnd");
        dept_id = bundle.getString("nRBId");
        stn_id = bundle.getString("nStnId");
        unit_id = bundle.getString("nUnitId");
        nHandle = bundle.getString("nHandle");
        sAlarmType = bundle.getString("sAlarmType");

        setTitle("告警历史搜索");
        setBackActive();
        setRightTopGone();
        loadInit();

        VUtils.setSwipyColor(swipy_layout);
        swipy_layout.setDirection(SwipyRefreshLayoutDirection.TOP); // 只能从上面刷新
        swipy_layout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                switch (direction) {
                    case TOP:
                        RequestData();
                        break;
                    case BOTTOM:
                        RequestData();
                        break;
                }
            }
        });

        swipy_layout.setRefreshing(false);
        alarmAdapter = new AlarmListAdapter(context);
        listview.setAdapter(alarmAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (VUtils.isFastDoubleClick()) {
                    return;
                } else {
                    Intent intent = new Intent(context, AlarmDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("AlarmNo", alarmList.get(position).getAlarmNo());
                    bundle.putString("AlarmRcdId", alarmList.get(position).getAlarmRcdId());
                    bundle.putSerializable("bean", alarmList.get(position));
                    intent.putExtras(bundle);

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
     * 请求数据<br/>
     */
    private void RequestData() {
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetHisAlarm, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                swipy_layout.setRefreshing(false);
                loadFail();
                toast(errorTip);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                swipy_layout.setRefreshing(false);
                String value = DataUtils.getRespString(response);
                showView(value);
            }
        });
    }


    private String createParams() {
        try {
            if ("0".equalsIgnoreCase(sAlarmType) || TextUtils.isEmpty(sAlarmType)) {
                sAlarmType = "(30,22,20,21)";
            }
            org.json.JSONObject jsonObj = new org.json.JSONObject();

            jsonObj.put("sDtBegin", start_time);
            jsonObj.put("sDtEnd", end_time);
            jsonObj.put("nRBId", dept_id);
            jsonObj.put("nStnId", stn_id);
            jsonObj.put("nUnitId", unit_id);
            jsonObj.put("nPcdtId", "0"); //
            jsonObj.put("sAlarmType", sAlarmType); // 告警类型，0：不限；20：预警，21：小物体告警，22：工务告警，30：异物告警
            jsonObj.put("nHandle", nHandle); //  处理类型，0：不限，1：未处理，2：已处理

            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 根据返回数据展示listview
     */
    private void showView(final String response) {
        Log.i(TAG, "解析出json，返回参数是：" + response);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String dataStr = DataUtils.getReturnData(response);
                    alarmList = com.alibaba.fastjson.JSONArray.parseArray(dataStr, AlarmBean.class);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO 只选出前10条，要不条目过多太卡了
//                            if (Finals.IS_TEST) {
//                                if (alarmList.size() > 10) {
//                                    alarmList = alarmList.subList(0, 10);
//                                }
//                            }
                            if (alarmList.size() <= 0) {
                                loadFail();
                                toast(tip);
                                return;
                            }
                            alarmAdapter.setDataList(alarmList);
                            alarmAdapter.notifyDataSetChanged();
                            Log.i(TAG, "解析json为data = " + alarmList);
                            loadOK();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadFail();
                            toast(tip);
                        }
                    });
                }
            }
        }).start();
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
