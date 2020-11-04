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

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.AlarmListAdapter;
import com.thdz.ywqx.base.BaseFragment;
import com.thdz.ywqx.bean.AlarmBean;
import com.thdz.ywqx.bean.PushBeanBase;
import com.thdz.ywqx.event.AlarmListEvent;
import com.thdz.ywqx.ui.Activity.alarm.AlarmDetailActivity;
import com.thdz.ywqx.ui.Activity.alarm.SearchActivity;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.VUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 最新告警<br/>
 * 1 增加缓存<br/>
 * 2 增加默认选择条件<br/>
 * 3 增加跳转至搜搜页面的入口
 */
public class AlarmFragment extends BaseFragment {

    @BindView(R.id.right_img)
    ImageView right_img;

    @BindView(R.id.swipy_layout)
    SwipyRefreshLayout swipy_layout;// 上下刷新

    @BindView(R.id.listview)
    ListView listview;

    private AlarmListAdapter alarmAdapter;

    // 数据Datalist
    private List<AlarmBean> alarmList;

    private String tip = "没有未确认的告警信息！";
    private String msgFail = "连接服务器失败";

    @Override
    public void cycleRequest(){
    }

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup arg1, Bundle arg2) {
        return inflater.inflate(R.layout.fragment_alarm, arg1, false);
    }


    @Override
    public void initView(Bundle savedInstanceState, View view, LayoutInflater inflater) {
        EventBus.getDefault().register(this);
        right_img.setOnClickListener(this);

        setTitle(view, "最新告警");
        setBackGone(view);
//        setRightTopGone(view);
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
     * @param index 向前推迟的小时数
     */
    public String getStartDateStr(int index) {
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_MONTH, 0 - index);

        calendar.add(Calendar.HOUR_OF_DAY, 0 - index);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String value = sdf.format(date);
        return value;

    }


    /**
     * 请求数据<br/>
     */
    private void RequestData() {
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetRecentAlarm, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                swipy_layout.setRefreshing(false);
                loadFail();
                toast(msgFail);
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
            jsonObj.put("DtBegin", getStartDateStr(12));
            jsonObj.put("BConfirmed", "false");
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
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
                case R.id.right_img:
                    Intent intent = new Intent(context, SearchActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            Log.i(TAG, "注册bus");
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    /**
     * 推送处理：有最新告警，则刷新列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void dealListEvent(AlarmListEvent event) {
        PushBeanBase pushBean = event.getPushBean();
        EventBus.getDefault().removeStickyEvent(event); // FIXME 解决收到多条推送告警的bug
        RequestData();
    }

}
