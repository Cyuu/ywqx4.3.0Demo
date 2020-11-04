package com.thdz.ywqx.ui.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.thdz.ywqx.R;
import com.thdz.ywqx.base.BaseLazyFragment;
import com.thdz.ywqx.bean.PushBeanBase;
import com.thdz.ywqx.bean.StationBean;
import com.thdz.ywqx.bean.StnDetailInfoBean;
import com.thdz.ywqx.bean.StnDetailStateBean;
import com.thdz.ywqx.event.AlarmDetailCMDBackEvent;
import com.thdz.ywqx.event.AlarmDetailRefreshEvent;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.DialogUtil;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.SpUtil;
import com.thdz.ywqx.util.VUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 站点----详细信息，状态，控制<br>
 * ----------<br>
 * 根据code获取value ---> 写死在每个显示项目上，根据每个固定的code判断
 */
public class StationDetailFragment extends BaseLazyFragment {

    @BindView(R.id.control_layout)
    SwipeRefreshLayout control_layout;

    /*--------基本信息-------*/

    @BindView(R.id.dept_name)
    TextView dept_name; // 局名称

    @BindView(R.id.line_name)
    TextView line_name; // 线路名称

    @BindView(R.id.station_name)
    TextView station_name; // 站点名称

    @BindView(R.id.keeper_name)
    TextView keeper_name; // 责任人

    @BindView(R.id.stn_sim)
    TextView stn_sim; // 责任人电话

    @BindView(R.id.stn_sim_no)
    TextView stn_sim_no; // 现场SIM卡

    @BindView(R.id.rs_name)
    TextView rs_name; // 工务段

    @BindView(R.id.rwi_name)
    TextView rwi_name; // 工区

    @BindView(R.id.stn_hard_ver)
    TextView stn_hard_ver; // 硬件版本

    @BindView(R.id.stn_soft_ver)
    TextView stn_soft_ver; // 软件版本

    @BindView(R.id.journey_route)
    TextView journey_route; // 出行路线

    /*-------- 系统监测状态 -------*/

    @BindView(R.id.tv_open_status)
    TextView tv_open_status;
    @BindView(R.id.tv_monitor_status)
    TextView tv_monitor_status;
    @BindView(R.id.tv_force_status)

    TextView tv_force_status;
    @BindView(R.id.tv_work_mode)
    TextView tv_work_mode;

    @BindView(R.id.tv_device_status)
    TextView tv_device_status;
    @BindView(R.id.tv_run_status)
    TextView tv_run_status;
    @BindView(R.id.tv_fourg_status)
    TextView tv_fourg_status;

    /*-------- 控制按钮 -------*/

    @BindView(R.id.stn_detail_control)
    LinearLayout stn_detail_control;

    @BindView(R.id.open_alarm_dev)
    TextView open_alarm_dev; // 打开告警设备
    @BindView(R.id.close_alarm_dev)
    TextView close_alarm_dev; // 关闭告警设备

    @BindView(R.id.reset_gmdt)
    TextView reset_gmdt; // GMDT重启
    @BindView(R.id.clear_alarm)
    TextView clear_alarm; // 清楚报警

    @BindView(R.id.oepn_normal_mode)
    TextView oepn_normal_mode; // 开启正常模式

    private boolean isPrepared;// 标志位，标志已经初始化完成。

    private StationBean stnBean;

    private String RBId;
    private String StnId;
    private String StnNo;

    private StnDetailInfoBean detailInfo;
    private StnDetailStateBean detailState;
    private int index = -1;// 同页面不同的控件出发对话框关闭后的回调标识

    private String routePath = null;

    /**
     * 用途：<br/>
     * 状态信息变化推送信息返回时，判断是否正在请求状态信息，如果是，则不再发起新的请求。<br/>
     */
    private boolean isStateOK = true;

    @Override
    public void cycleRequest() {
        RequestdetailState();
    }

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup arg1, Bundle arg2) {
        return inflater.inflate(R.layout.fragment_stn_control, arg1, false);
    }

    @Override
    public void initView(Bundle savedInstanceState, View view, LayoutInflater inflater) {
        EventBus.getDefault().register(this);

        if (SpUtil.getBoolean(Finals.SP_CONTROL_FLAG)) {
            stn_detail_control.setVisibility(View.VISIBLE);
        } else {
            stn_detail_control.setVisibility(View.GONE);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            stnBean = (StationBean) bundle.getSerializable("stnBean");
        }

        RBId = stnBean.getRBId();
        StnId = stnBean.getStnId();
        StnNo = stnBean.getStnNo();

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        // 发送控制命令按钮
        open_alarm_dev.setOnClickListener(this);
        close_alarm_dev.setOnClickListener(this);

        reset_gmdt.setOnClickListener(this);
        clear_alarm.setOnClickListener(this);
        oepn_normal_mode.setOnClickListener(this);
        journey_route.setOnClickListener(this);
        journey_route.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线

        VUtils.setSwipeColor(control_layout);
        control_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestData();
                control_layout.setRefreshing(false);
            }
        });
        control_layout.setRefreshing(false);

        isPrepared = true;
        lazyLoad();

    }

    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        // 处理
//        RequestData();
        RequestDetailInfo();
        setTimerEnable(true);
        isPrepared = false; // 已加载过不再重复加载
    }

    /**
     * 数据只需要刷新即可，不需要选择mode
     */
    private void RequestData() {
        RequestDetailInfo();
        RequestdetailState();
    }


    /**
     * 基本信息
     */
    private void RequestDetailInfo() {
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetStnInfoByIds, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                toast(failTip);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "监控点基本信息，返回值：" + value);

//                    detailInfo = JSONArray.parseArray(DataUtils.getReturnData(value), StnDetailInfoBean.class).get(0);
                    List<StnDetailInfoBean> infoList = JSON.parseArray(DataUtils.getReturnData(value), StnDetailInfoBean.class);
                    for (StnDetailInfoBean iii : infoList) {
                        if (iii.getStnId().equals(StnId)) {
                            detailInfo = iii;
                            break;
                        }
                    }

                    showDetailInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(errorTip);
                }
            }
        });
    }

    /**
     * 状态信息
     */
    private void RequestdetailState() {
        isStateOK = false;
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetStnStatusByIds, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                isStateOK = true;
                control_layout.setRefreshing(false);
//                toast("监控点连接状态获取失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                isStateOK = true;
                control_layout.setRefreshing(false);
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "监控点状态信息，返回值：" + value);

//                    detailState = JSON.parseArray(DataUtils.getReturnData(value), StnDetailStateBean.class).get(0);
                    List<StnDetailStateBean> stateList = JSON.parseArray(DataUtils.getReturnData(value), StnDetailStateBean.class);
                    for (StnDetailStateBean iii : stateList) {
                        if (iii.getStnId().equals(StnId)) {
                            detailState = iii;
                            break;
                        }
                    }

                    showStateValueByCode();
                } catch (Exception e) {
                    e.printStackTrace();
//                    toast("监控点连接状态获取失败");
                }
            }
        });
    }

    /**
     * 获取数据并展示
     */
    private void showDetailInfo() {

//        // TODO 接口返回的站点名称和监控点列表返回的数据是一致的。
//        String stnName = DBManager.getInstance().getStnNameByStnNo(detailInfo.getStnNo());
//        toast("接口返回的：" + detailInfo.getStnName() + ", db里获取的：" + stnName);

        routePath = detailInfo.getStnPath();

        dept_name.setText("铁路局：" + detailInfo.getRBName());        // 局名称
        line_name.setText("铁路线：" + detailInfo.getRLName());         // 线路名称
        station_name.setText("铁路站：" + detailInfo.getStnName());      // 站点名称
        keeper_name.setText("责任人：" + detailInfo.getWatchKeeper());    // 责任人
        stn_sim.setText("责任人电话：" + detailInfo.getWatchKeeperTel());  // 责任人电话
        if (!TextUtils.isEmpty(detailInfo.getWatchKeeperTel())) {
            stn_sim.setText("责任人电话：" + detailInfo.getWatchKeeperTel() + " (点击复制)");
            stn_sim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(null, detailInfo.getWatchKeeperTel());// 把数据复制到剪贴板
                    clipboard.setPrimaryClip(clipData);
                    toast("电话号码已经复制");
                }
            });
        }

        stn_sim_no.setText("现场SIM卡：" + detailInfo.getStnSim()); // 现场SIM卡
        rs_name.setText("工务段：" + detailInfo.getRSName());       // 工务段
        rwi_name.setText("工    区：" + detailInfo.getRWIName());   // 工区


        // 硬件版本  SysVersion 0: 2.0  1: 3.0  其他：4.0
        int sysver = detailInfo.getSysVersion();
        String verName = "";
        if (sysver == 0) {
            verName = "异物侵限2.0";
        } else if (sysver == 1) {
            verName = "异物侵限3.0";
        } else if (sysver == 2) {
            verName = "异物侵限4.0";
        }
        stn_hard_ver.setText("硬件版本：" + verName);

        String running_status = detailInfo.getRunningStatus();
        String openstatus = "";
        tv_open_status.setTextColor(getResources().getColor(R.color.black));
        if (running_status.equals("true")) {
            openstatus = "已开通";
            tv_open_status.setTextColor(getResources().getColor(R.color.green_deep_color));
        } else if (running_status.equals("false")) {
            openstatus = "未开通";
            tv_open_status.setTextColor(getResources().getColor(R.color.red_color));
        }
        tv_open_status.setText(openstatus);
    }


    /**
     * 展示详情信息  String --> AlarmDetailInfoBean, TODO 根据code转为汉字提示
     */
    private void showStateValueByCode() {

        // 软件版本  GMCS_Pack_Ver
        stn_soft_ver.setText("软件版本：" + detailState.getGMCS_Pack_Ver());

        // -------------- 系统监测状态 ------------- 没有的显示空

        // 监测状态   4007： 正常  绿色 /// 4006 告警 其他： 不显示  红色
        showTvValue(tv_monitor_status, detailState.getSystemAlarm(), 4007, 4006);
        showTvColor(tv_monitor_status, detailState.getSystemAlarm(), 4007);

        // 强制状态  3995 正常  绿色 ////  3996 强制报警  3997 强制消警 红色
        String force_status = "";
        if (detailState.getGmdtRunState().equals(3995 + "")) {
            force_status = "正常";
        } else if (detailState.getGmdtRunState().equals(3996 + "")) {
            force_status = "强制报警";
        } else if (detailState.getGmdtRunState().equals(3997 + "")) {
            force_status = "强制消警";
        }
        tv_force_status.setText(force_status);
        showTvColor(tv_force_status, detailState.getGmdtRunState(), 3995);

        // 工作模式  3991 正常 绿色 /// 3992  系统测试  3993 单元测试  3994 手动， 红色
        String work_mode = "";
        if (detailState.getGmdtRunMode().equals(3991 + "")) {
            work_mode = "正常";
        } else if (detailState.getGmdtRunMode().equals(3992 + "")) {
            work_mode = "系统测试";
        } else if (detailState.getGmdtRunMode().equals(3993 + "")) {
            work_mode = "单元测试";
        } else if (detailState.getGmdtRunMode().equals(3994 + "")) {
            work_mode = "单元测试";
        }
        tv_work_mode.setText(work_mode);
        showTvColor(tv_work_mode, detailState.getGmdtRunMode(), 3991);

        // 设备状态  3998 正常 绿色 ///  3999 异常 红色
        showTvValue(tv_device_status, detailState.getGmdtMonitorState(), 3998, 3999);
        showTvColor(tv_device_status, detailState.getGmdtMonitorState(), 3998);

        // 运行状态  4000 正常 绿色 ///  4001 异常 红色
        showTvValue(tv_run_status, detailState.getGmdtWorkState(), 4000, 4001);
        showTvColor(tv_run_status, detailState.getGmdtWorkState(), 4000);

        // 4G状态  1 正常 绿色   ///  0 异常 红色  其他 空
        showTvValue(tv_fourg_status, detailState.getDevstate_4g(), 1, 0);
        showTvColor(tv_fourg_status, detailState.getDevstate_4g(), 1);

    }


    /**
     * nStnId有效，其他默认0
     */
    private String createParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("nStnId", stnBean.getStnId());
            jsonObj.put("nRBId", stnBean.getRBId());
            jsonObj.put("nRSId", stnBean.getRSId());
            jsonObj.put("nRWIId", stnBean.getRWIId());

            jsonObj.put("nPcdtId", "0");
            jsonObj.put("nUnitId", "0");
            jsonObj.put("nAppId", "0");
            jsonObj.put("nRLId", "0");

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
                case R.id.journey_route:
                    if (TextUtils.isEmpty(routePath)) {
                        routePath = "尚无出行路线信息";
                    }
                    DialogUtil.showOnly(context, routePath);
                    break;
                case R.id.open_alarm_dev:
                    index = 1;
                    showPwdDialog(controlListener);
                    break;
                case R.id.close_alarm_dev:
                    index = 2;
                    showPwdDialog(controlListener);
                    break;
                case R.id.reset_gmdt:
                    index = 3;
                    showPwdDialog(controlListener);
                    break;
                case R.id.clear_alarm:
                    index = 4;
                    showPwdDialog(controlListener);
                    break;
                case R.id.oepn_normal_mode:
                    index = 5;
                    showPwdDialog(controlListener);
                    break;
                case R.id.sure_alarm_btn://确认告警，需要输入密码校验
                    index = 6;
                    showPwdDialog(controlListener);
                    break;
                case R.id.cancel_alarm_btn://取消告警，需要输入密码校验
                    index = 7;
                    showPwdDialog(controlListener);
                    break;
//                case R.id.stn_sim: //拨打责任人电话
//                    dialKeeper();
//                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 致电责任人
     */
//    private void dialKeeper() {
//        String phoneNum = detailInfo.getWatchKeeperTel();
//        if (!TextUtils.isEmpty(phoneNum)) {
//            Intent callint = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + phoneNum));
//            startActivity(callint);
//        }
//    }

    /**
     * 输入密码后，点击确认的响应
     */
    private DialogInterface.OnClickListener controlListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imm.hideSoftInputFromWindow(pEdit.getWindowToken(), 0);// 关闭输入法
//                    Log.i(TAG, "密码： " + pEdit.getText().toString().trim());
                    String inputStr = pEdit.getText().toString();
                    if (inputStr.isEmpty()) {
                        toast("请输入密码");
                    } else {
                        String pwdStr = SpUtil.getData(Finals.SP_PWD);
                        if (pwdStr.equals(inputStr)) {
                            switch (index) {
                                case 1: // 打开告警设备
                                    RequestControlCMD(StnNo, "0", Finals.CODE_OPEN_ALARM_DEV);
                                    break;
                                case 2: // 关闭告警设备
                                    RequestControlCMD(StnNo, "0", Finals.CODE_CLOSE_ALARM_DEV);
                                    break;
                                case 3: // GMDT重启
                                    RequestControlCMD(StnNo, "0", Finals.CODE_RESET_GMDT);
                                    break;
                                case 4: // 清除告警
                                    RequestControlCMD(StnNo, "0", Finals.CODE_CLEAR_ALARM);
                                    break;
                                case 5: // 开启正常模式
                                    RequestControlCMD(StnNo, "0", Finals.CODE_OPEN_NORMAL_MODE);
                                    break;
                                case 6:// 确认告警

                                    break;
                                case 7:// 取消告警

                                    break;
                                default:
                                    break;
                            }
                        } else {
                            toast("密码错误");
                        }
                    }
                }
            });
        }
    };

    /**
     * 推送处理：控制命令发送成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealAlarmBackEvent(AlarmDetailCMDBackEvent event) {
        Log.i(TAG, "AlarmDetailBackEvent处理: ");
        toast("控制命令发送成功");
    }


    /**
     * 推送处理：告警详情状态刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealAlarmEvent(AlarmDetailRefreshEvent event) {
        PushBeanBase pushBean = event.getPushBean();
        String pStnNo = pushBean.getStnNo();
        // 判断是不是该站点 TODO 优点：节省资源 // 问题：接口刷新不及时
        if (pStnNo.equals(StnNo)) {
            Log.i(TAG, "AlarmDetailRefreshEvent处理: ");
//            toast("监控点状态发生变化");
            if (isStateOK) {
                RequestdetailState();
            }
        }
    }

}
