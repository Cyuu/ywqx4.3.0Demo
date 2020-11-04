package com.thdz.ywqx.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.AlarmListAdapter;
import com.thdz.ywqx.base.BaseFragment;
import com.thdz.ywqx.bean.AlarmBean;
import com.thdz.ywqx.ui.Activity.alarm.AlarmDetailActivity;
import com.thdz.ywqx.ui.Activity.alarm.SearchActivity;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.VUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 历史告警<br/>
 * 1 增加缓存<br/>
 * 2 增加默认选择条件<br/>
 * 3 增加跳转至搜搜页面的入口
 */
public class HistoryFragment extends BaseFragment {

    @BindView(R.id.swipy_layout)
    SwipyRefreshLayout swipy_layout;// 上下刷新

    @BindView(R.id.listview)
    ListView listview;

    @BindView(R.id.selection_layout)
    RadioGroup selection_layout;

    @BindView(R.id.title_tv)
    TextView title_tv;

    /**
     * 搜索
     */
    @BindView(R.id.right_img)
    ImageView right_img;

    private AlarmListAdapter alarmAdapter;

    private List<AlarmBean> alarmList;

    private String tip = "没有最近2天的告警信息！";

    @Override
    public void cycleRequest(){
    }

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup arg1, Bundle arg2) {
        return inflater.inflate(R.layout.fragment_story, arg1, false);
    }


    @Override
    public void initView(Bundle savedInstanceState, View view, LayoutInflater inflater) {
        right_img.setOnClickListener(this);

        setTitle(view, "最近2天告警");
        setBackGone(view);
        loadInit();

        VUtils.setSwipyColor(swipy_layout);
        swipy_layout.setDirection(SwipyRefreshLayoutDirection.TOP); // z只能从上面刷新
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


    /**
     * 获取当前时间字符串，格式：2016-08-11 08：08:08
     *
     * @param index 向前推迟的天数
     */
    public String getStartDateStr(int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 0 - index);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String value = sdf.format(date);
        return value;

    }

    /**
     * 请求数据<br/>
     * 1 mode 下拉刷新的3种模式 <br/>
     * 2 params_date, 时间段<br/>
     * 3 flag 是否按了radiobutton
     */
    private void RequestData() {
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetRecentAlarm, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                swipy_layout.setRefreshing(false);
                loadFail();
                toast(failTip);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                swipy_layout.setRefreshing(false);
                String value = DataUtils.getRespString(response);
                showView(value); // 处理方式3
            }
        });
    }

    /**
     * 1 nUserId 用户uid<br/>
     * 2 sDTBegin, 开始时间，格式：2016-08-11 08：08:08<br/>
     * 3 isConfirmed 是否确认，需要填false
     */
    private String createParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("DtBegin", getStartDateStr(2));
            jsonObj.put("BConfirmed", "false");
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            toast(errorTip);
        }
        return "";
    }


    /**
     * 获取数据并展示在
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
                case R.id.right_img: // 精确搜索
                    Intent intent = new Intent(context, SearchActivity.class);
                    startActivity(intent);

                    break;
                default:
                    break;
            }
        }
    }
}
