package com.thdz.ywqx.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.DeptListAdapter;
import com.thdz.ywqx.adapter.MonitorStationAdapter;
import com.thdz.ywqx.app.MyApplication;
import com.thdz.ywqx.base.BaseFragment;
import com.thdz.ywqx.bean.DeptBean;
import com.thdz.ywqx.bean.MonitorBean;
import com.thdz.ywqx.bean.StationBean;
import com.thdz.ywqx.bean.StnDetailStateBean;
import com.thdz.ywqx.db.DBManager;
import com.thdz.ywqx.ui.Activity.station.StationDetailTabActivity;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.VUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 局---站点---单元 --管理三层机构
 */
public class MonitorFragment extends BaseFragment {

    @BindView(R.id.monitor_layout)
    SwipeRefreshLayout monitor_layout; // 局

    @BindView(R.id.listview)
    ListView listview; // 局

    @BindView(R.id.stn_layout)
    SwipeRefreshLayout stn_layout; // 站点

    @BindView(R.id.listview_stn)
    ListView listview_stn; // 站点

    @BindView(R.id.level_layout)
    RelativeLayout level_layout;

    @BindView(R.id.all_tv)
    TextView all_tv; // 全部

    @BindView(R.id.dept_tv)
    TextView dept_tv; // 局

    @BindView(R.id.previous_tv)
    TextView previous_tv; // 上一级

    public String RBId; // 局id -- 用于获取线路列表
    public String StnId; // 站点id -- 用于获取单元列表

    private DeptListAdapter deptAdapter;
    private List<DeptBean> deptList;

    private MonitorStationAdapter stnAdapter;
    private List<StationBean> stnList;

    private final int CODE_LEVEL_DEPT = 1;
    private final int CODE_LEVEL_STN = 2;
    private int CODE_CURRENT_LEVEL = CODE_LEVEL_DEPT;

    private List<StnDetailStateBean> stateList = null; // 用于设置监控点的连接状态：连接，掉线

    private String tip = "没有可查看的监控点";


    @Override
    public void cycleRequest() {
    }

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup arg1, Bundle arg2) {
        return inflater.inflate(R.layout.fragment_monitor, arg1, false);
    }

    @Override
    public void initView(Bundle savedInstanceState, View view, LayoutInflater inflater) {

        all_tv.setOnClickListener(this);
        dept_tv.setOnClickListener(this);
        previous_tv.setOnClickListener(this);

        view.findViewById(R.id.right_img).setOnClickListener(this);
        loadInit();

        VUtils.setSwipeColor(monitor_layout);
        monitor_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryDeptList();
//                RequestData();
            }
        });

        VUtils.setSwipeColor(stn_layout);
        stn_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestStnState();
            }
        });

        setTitle(view, "监控点列表");
        setBackGone(view);
        setRightTopGone(view);


        monitor_layout.setVisibility(View.VISIBLE);
        stn_layout.setVisibility(View.GONE);

        deptAdapter = new DeptListAdapter(context);
        listview.setAdapter(deptAdapter);

        stnAdapter = new MonitorStationAdapter(context);
        listview_stn.setAdapter(stnAdapter);

//        RequestData();
        queryDeptList();
        level_layout.setVisibility(View.GONE);

    }


    /**
     * 获取数据 并 处理展示
     */
    public void RequestData() {
        if (Finals.IS_TEST) {
            toast("网络数据获取----");
        }
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetStnList, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                monitor_layout.setRefreshing(false);
                if (e.getMessage().contains("failed to connect")) {
                    toast("连接服务器失败");
                    Log.i(TAG, "连接服务器失败");
                } else {
                    toast("没有获取到监控点信息");
                    Log.i(TAG, "没有获取到监控点列表信息");
                }
                loadFail();
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                monitor_layout.setRefreshing(false);
                String value = DataUtils.getRespString(response);
                saveData(value);
            }
        });
    }


    private String createParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("nUserId", application.getUid());
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            toast(errorTip);
        }
        return "";
    }

    /**
     * 从数据库查询监控点数据
     */
    public void queryDeptList() {
        if (Finals.IS_TEST) {
            toast("本地数据读取~");
        }
        // 正在请求，那么重新请求，不去做异步等待
        if (MyApplication.isRequestingMonitors) {
            MyApplication.isRequestingMonitors = false;
            RequestData();
            return;
        } else { // 如果已经完成请求，则使用数据库的数据加载监控点
            if (MyApplication.monitorList == null || MyApplication.monitorList.isEmpty()) {
                MyApplication.isRequestingMonitors = false;
                RequestData();
                return;
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            deptList = DBManager.getInstance().queryDeptBeanList();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    monitor_layout.setRefreshing(false);
                                    showDeptView();
                                    loadOK();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    monitor_layout.setRefreshing(false);
                                    loadFail();
                                    toast(tip);
                                }
                            });
                        }
                    }
                }).start();
            }
        }


    }

    // 解析并保存数据
    private void saveData(final String value) {
        Log.i(TAG, "解析出的json = " + value);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataUtils.dealMntList(value);
                    deptList = DBManager.getInstance().queryDeptBeanList();
                    List<MonitorBean> moniList = DBManager.getInstance().queryMonitorBeanList();
                    if (moniList == null || moniList.isEmpty()) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadFail();
                                toast(tip);
                                return;
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showDeptView();
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

    private void showDeptView() {
        monitor_layout.setRefreshing(false);
        monitor_layout.setVisibility(View.VISIBLE);
        stn_layout.setVisibility(View.GONE);

        deptAdapter.setDataList(deptList);
        deptAdapter.notifyDataSetChanged();
        loadOK();
        CODE_CURRENT_LEVEL = CODE_LEVEL_DEPT;
        level_layout.setVisibility(View.GONE);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 更新id
                DeptBean bean = deptList.get(position);
                RBId = bean.getRBId();
                showStnView(RBId);
                dept_tv.setText(bean.getRBName());

            }
        });
    }

    private void showStnView(String mRBId) {
        monitor_layout.setVisibility(View.GONE);
        stn_layout.setVisibility(View.VISIBLE);

        // 更新ui
        stnAdapter.setStateList(null);
        stnList = DataUtils.getStationListByDeptId(mRBId);
        stnAdapter.setDataList(stnList);
        stnAdapter.notifyDataSetChanged();
        loadOK();

        // 加载该站点列表的连接状态
        RequestStnState();

        level_layout.setVisibility(View.VISIBLE);
        dept_tv.setVisibility(View.VISIBLE);
        CODE_CURRENT_LEVEL = CODE_LEVEL_STN;

        listview_stn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 更新id  StationDetailActivity  StationDetailTabActivity
                StationBean stnBean = stnList.get(position);
                StnId = stnBean.getStnId();
                Intent intent = new Intent(context, StationDetailTabActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("stnBean", stnBean);
                intent.putExtras(bundle);

//                intent.putExtra("RBId", RBId);
//                intent.putExtra("StnId", StnId);
//                intent.putExtra("StnName", stnBean.getStnName());
//                intent.putExtra("StnNo", stnBean.getStnNo());
                startActivity(intent);

            }
        });
    }

    /**
     * 状态信息
     */
    private void RequestStnState() {
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetStnStatusByIds, createStateParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                stn_layout.setRefreshing(false);
//                toast("站点状态信息获取失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                stn_layout.setRefreshing(false);
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "监控点列表状态信息，返回值：" + value);
                    stateList = JSON.parseArray(DataUtils.getReturnData(value), StnDetailStateBean.class);
                    stnAdapter.setStateList(stateList);
                    stnAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
//                    toast("站点状态信息获取失败");
                }
            }
        });
    }

    /**
     * RBId有效，其他默认0, 用于查询该局下的所有站点状态
     */
    private String createStateParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("nStnId", "0");

            jsonObj.put("nPcdtId", "0");
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


    /**
     * 是否显示了站这一次层级
     */
    public boolean isStationLevelShow() {
        return level_layout.getVisibility() == View.VISIBLE;
    }


    public void doPrevious() {
        if (CODE_CURRENT_LEVEL == CODE_LEVEL_DEPT) {
            if (level_layout.getVisibility() == View.VISIBLE) {
                level_layout.setVisibility(View.GONE);
                showDeptView();
            }
        } else if (CODE_CURRENT_LEVEL == CODE_LEVEL_STN) {
            showDeptView();
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
                case R.id.all_tv:
                    if (deptList != null || !deptList.isEmpty()) {
                        showDeptView();
                    }
                    dept_tv.setVisibility(View.GONE);
                    break;
                case R.id.dept_tv:
                    showStnView(RBId);
                    break;
                // TODO 处理的对吗
                case R.id.previous_tv:
                    doPrevious();
                    break;
//                case R.id.right_img: // 站点检索
//
//                    ((MainActivity)context).goActivity(MonitorSearchActivity.class, null);
//                    break;
                default:
                    break;
            }
        }
    }


}
