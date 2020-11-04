package com.thdz.ywqx.ui.Activity.alarm;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.VideoListAdapter;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.AlarmBean;
import com.thdz.ywqx.bean.AlarmDetailInfoBean;
import com.thdz.ywqx.bean.PicBean;
import com.thdz.ywqx.bean.PushBeanBase;
import com.thdz.ywqx.event.AlarmDetailCMDBackEvent;
import com.thdz.ywqx.event.AlarmDetailRefreshEvent;
import com.thdz.ywqx.event.PicEvent;
import com.thdz.ywqx.ui.Activity.ImageActivity;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.SpUtil;
import com.thdz.ywqx.util.UrlUtils;
import com.thdz.ywqx.util.Utils;
import com.thdz.ywqx.util.VUtils;
import com.thdz.ywqx.view.NoScrollListView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 告警详情页面<br/>
 * 1 获取告警详情信息<br/>
 * 2 获取告警图片url和雷达图：RadarView<br/>
 * 3 获取实时视频，gfc从前台要视频，rtsp或者MP4，TODO 目前还没实现<br/>
 * 4 缩略图尚未实现。<br/>
 * （5）该页面初始化时需要访问3个接口：1 基本信息，2状态信息，3 处理情况信息<br/>
 * TODO 请求抓图命令，如果一开始没有返回告警图片信息，点击抓图按钮时们需要先获取告警图片信息，然后再请求抓图命令（抓图需要告警图片接口返回的数据）<br/>
 * 平时，隐藏look_tv，局部高清图来了之后，rt_look_tv  koutu_look_tv.setVisibility(View.VISIBLE)
 */
public class AlarmDetailActivity extends BaseActivity {


    @BindView(R.id.control_layout)
    SwipeRefreshLayout control_layout;

    @BindView(R.id.alarm_image_tv)
    TextView alarm_image_tv; // 看告警图

    @BindView(R.id.radar_image_tv)
    TextView radar_image_tv; // 看雷达图

    @BindView(R.id.rt_image_layout)
    LinearLayout rt_image_layout; // 实时图片布局

    @BindView(R.id.koutu_image_layout)
    LinearLayout koutu_image_layout; // 抠图图片布局

    @BindView(R.id.rt_image_tv)
    TextView rt_image_tv; // 抓取实时图片

    @BindView(R.id.rt_look_tv)
    TextView rt_look_tv;// 查看实时图片

    @BindView(R.id.koutu_image_tv)
    TextView koutu_image_tv; // 抓取高清局部图片

    @BindView(R.id.koutu_look_tv)
    TextView koutu_look_tv;// 查看高清局部图片

    @BindView(R.id.rt_video_tv)
    View rt_video_tv;

    @BindView(R.id.his_video_tv)
    View his_video_tv;

    /* 基本信息 */

    @BindView(R.id.dept_name)
    TextView dept_name; // 局

    @BindView(R.id.line_name)
    TextView line_name; // 线路

    @BindView(R.id.station_name)
    TextView station_name; // 站点名称

    @BindView(R.id.alarm_time)
    TextView alarm_time; // 告警开始时间

    @BindView(R.id.alarm_cancel_time)
    TextView alarm_cancel_time; // 告警取消时间

    @BindView(R.id.keeper_name)
    TextView keeper_name; // 责任人

    @BindView(R.id.keeper_tel)
    TextView keeper_tel; // 责任人电话

    @BindView(R.id.stn_sim)
    TextView stn_sim; // 现场SIM卡

    @BindView(R.id.rs_name)
    TextView rs_name; // 工务段

    @BindView(R.id.rwi_name)
    TextView rwi_name; // 工区


    @BindView(R.id.running_status_tv)
    TextView running_status_tv; // 站点开通状态

    @BindView(R.id.alarm_handle_user)
    TextView alarm_handle_user;
    @BindView(R.id.alarm_handle_time)
    TextView alarm_handle_time;
    @BindView(R.id.alarm_handle_type)
    TextView alarm_handle_type;

    /* 控制区域，根据权限判断是否显示 */

    @BindView(R.id.alarm_handle_control)
    LinearLayout alarm_handle_control;

    /* 按钮们 */
    @BindView(R.id.sure_alarm_btn)
    TextView sure_alarm_btn; // 确认告警

    @BindView(R.id.cancel_alarm_btn)
    TextView cancel_alarm_btn; // 取消告警

    @BindView(R.id.send_handle_btn)
    TextView send_handle_btn; // 提交处理意见

    @BindView(R.id.handleinfo)
    EditText handleinfo; // 输入和展示处理意见

    @BindView(R.id.layout_his_alarm_video)
    View layout_his_alarm_video; // 历史告警视频布局

    @BindView(R.id.lv_his_video)
    NoScrollListView lv_his_video; // 历史告警视频列表

    @BindView(R.id.layout_rt_video)
    View layout_rt_video; // 实时视频布局

    @BindView(R.id.lv_rt_video)
    NoScrollListView lv_rt_video; // 实时视频列表


    private String alarm_no; // 告警id
    private String AlarmRcdId; // 告警记录号

    private AlarmBean itemBean;
    private AlarmDetailInfoBean detailInfo;
    private Intent mIntent = null;

    private VideoListAdapter videoHisAdapter;
    private List<String> hisUrlList = new ArrayList<>();

    private VideoListAdapter videoRTAdapter;
    private List<String> rtUrlList = new ArrayList<>();

    private int index = -1;// 同页面不同的控件出发对话框关闭后的回调标识
    private PicBean picBean; // 告警图对象

    private String alarm_img_url;       // 最新的告警图片url
    private String rt_img_url;          // 实时图片url
    private String koutu_url;           // 高清局部图url

    private boolean hasNewRtImage = false; // 是否新的抠图图片
    private boolean hasNewKoutuImage = false; // 是否新的抠图图片

    private boolean isInfoOk = true;
    /**
     * 用途：<br/>
     * 1 协助判断对话框的展示和隐藏<br/>
     * 2 状态信息变化推送信息返回时，判断是否正在请求状态信息，如果是，则不再发起新的请求。<br/>
     */
    private boolean isStateOK = true;

    private String StnId;
    private String StnNo;
    private String UnitNo;

    private final String HANDLE_ALARM_SURE = "0";
    private final String HANDLE_ALARM_CANCEL = "1";


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_alarm_detail);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        setTitle("告警详情");

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setBackActive();
        setRightTopGone();

        if (SpUtil.getBoolean(Finals.SP_ALARM_HANDLE_FLAG)) {
            alarm_handle_control.setVisibility(View.VISIBLE);
        } else {
            alarm_handle_control.setVisibility(View.GONE);
        }

        // 隐藏输入框自动弹出的输入法
        handleinfo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                                && KeyEvent.ACTION_DOWN == event.getAction())) {
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(pEdit.getWindowToken(), 0);// 关闭输入法
                    }
                }
                return true;
            }
        });

        VUtils.setSwipeColor(control_layout);
        control_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestAlarmTimeInfo();
                RequestDetailInfo();
                RequestAlarmImage(false);
                control_layout.setRefreshing(false);
            }
        });

        mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        alarm_no = bundle.getString("AlarmNo");
        AlarmRcdId = bundle.getString("AlarmRcdId");
        itemBean = (AlarmBean) bundle.getSerializable("bean");
        StnId = itemBean.getStnId();
        StnNo = itemBean.getStnNo();
        UnitNo = itemBean.getUnitNo();
        if (TextUtils.isEmpty(UnitNo)) {
            UnitNo = "0";
        }

        showAlarmHandleInfo(itemBean);

        if (TextUtils.isEmpty(alarm_no)) {
            Log.i(TAG, "未产生该告警详情");
            toast("未产生该告警详情");
            finish();
        }

        // 图和告警
        alarm_image_tv.setOnClickListener(this);
        radar_image_tv.setOnClickListener(this);
        rt_image_tv.setOnClickListener(this);
        rt_look_tv.setOnClickListener(this);
        koutu_image_tv.setOnClickListener(this);
        koutu_look_tv.setOnClickListener(this);
        rt_video_tv.setOnClickListener(this);
        his_video_tv.setOnClickListener(this);

        sure_alarm_btn.setOnClickListener(this);
        cancel_alarm_btn.setOnClickListener(this);

        // 发送控制命令按钮
        send_handle_btn.setOnClickListener(this);

        RequestDetailInfo();
        RequestAlarmImage(false);

        videoHisAdapter = new VideoListAdapter(context);
        lv_his_video.setAdapter(videoHisAdapter);
        lv_his_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = Finals.Url_Http + application.getIP() + hisUrlList.get(position).replace("~", "");
                Log.i(TAG, "即将打开的告警历史视频地址：" + url);
                VUtils.gotoVideoHistory(context, url);
            }
        });

        videoRTAdapter = new VideoListAdapter(context);
        lv_rt_video.setAdapter(videoRTAdapter);
        lv_rt_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = rtUrlList.get(position);
                Log.i(TAG, "即将打开的实时视频预览地址：" + url);
                VUtils.gotoVideoPreview(context, url, itemBean.getUnitName());
            }
        });

        layout_his_alarm_video.setVisibility(View.GONE);
        layout_rt_video.setVisibility(View.GONE);

    }


    @Override
    public void cycleRequest() {
    }


    /**
     * 详情数据请求 -- 告警处理意见
     */
    private void RequestAlarmTimeInfo() {
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetAlarmRecordByAlarmRcdId, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "返回值：" + value);
                    AlarmBean bean = JSONObject.parseObject(DataUtils.getReturnData(value), AlarmBean.class);
                    showAlarmHandleInfo(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 告警处理意见
     */
    private void showAlarmHandleInfo(AlarmBean bean) {
        if (bean != null) {
            String time1 = bean.getAlarmOpenTm();
            String time2 = bean.getAlarmCancelTm();
            alarm_time.setText("告警开始：" + (TextUtils.isEmpty(time1) ? "" : time1));
            alarm_cancel_time.setText("告警取消：" + (TextUtils.isEmpty(time2) ? "" : time2));

            alarm_handle_user.setText("处理人：" + (TextUtils.isEmpty(bean.getHandleUser()) ? "" : bean.getHandleUser()));
            alarm_handle_time.setText("处理时间：" + (TextUtils.isEmpty(bean.getHandleTime()) ? "" : bean.getHandleTime()));
            alarm_handle_type.setText("处理类型：" + getHandleTypeValue(bean.getHandleType()));

            String handleInfoTxt = bean.getHandleInfo();
            handleinfo.setText(handleInfoTxt);
            if (TextUtils.isEmpty(handleInfoTxt)) {
                send_handle_btn.setText("  提交处理意见  ");
            } else {
                send_handle_btn.setText("  修改处理意见  ");
            }
        }
    }


    private String getHandleTypeValue(String HandleType) {
        if (TextUtils.isEmpty(HandleType)) {
            return "";
        }
        if (HandleType.equals("0")) {
            return "正常告警";
        } else if (HandleType.equals("1")) {
            return "虚警";
        }
        return "";
    }


    /**
     * 详情数据请求 -- 告警基本信息
     */
    private void RequestDetailInfo() {
        isInfoOk = false;
        showProgressDialog();
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetStnInfoByAlarmRcdId, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                isInfoOk = true;
                hideAllProgressDialog();
                toast("未获取到告警信息");
                e.printStackTrace();

            }

            @Override
            public void onResponse(String response, int id) {
                isInfoOk = true;
                hideAllProgressDialog();
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "基本信息返回值：" + value);

//                    detailInfo = JSONObject.parseArray(DataUtils.getReturnData(value), AlarmDetailInfoBean.class).get(0);
                    List<AlarmDetailInfoBean> infoList = JSON.parseArray(DataUtils.getReturnData(value), AlarmDetailInfoBean.class);
                    for (AlarmDetailInfoBean iii : infoList) {
                        if (iii.getStnId().equals(StnId)) {
                            detailInfo = iii;
                            break;
                        }
                    }

                    showDetailInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                    toast("未获取到告警信息");
                }
            }
        });
    }

    /**
     * 展示详情信息  String --> AlarmDetailInfoBean
     */
    private void showDetailInfo() {
        dept_name.setText("铁路局：" + detailInfo.getRBName());
        line_name.setText("铁路线：" + detailInfo.getRLName());
        station_name.setText("铁路站：" + detailInfo.getStnName());

        keeper_name.setText("责任人：" + detailInfo.getWatchKeeper());
        keeper_tel.setText("责任人电话：" + detailInfo.getWatchKeeperTel());
        if (!TextUtils.isEmpty(detailInfo.getWatchKeeperTel())) {
            keeper_tel.setText("责任人电话：" + detailInfo.getWatchKeeperTel() + " (点击复制)");
            keeper_tel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(null, detailInfo.getWatchKeeperTel());// 把数据复制到剪贴板
                    clipboard.setPrimaryClip(clipData);
                    toast("电话号码已经复制");
                }
            });
        }

        stn_sim.setText("现场SIM卡：" + detailInfo.getStnSim()); // 现场SIM卡
        rs_name.setText("工务段：" + detailInfo.getRSName()); //工务段
        rwi_name.setText("工    区：" + detailInfo.getRWIName()); // 工区

        String runningState = detailInfo.getRunningStatus();
        String runningStateName = "站点开通情况：";
        running_status_tv.setTextColor(getResources().getColor(R.color.black));
        if (runningState.equals("true")) {
            runningStateName = "站点已开通";
            running_status_tv.setTextColor(getResources().getColor(R.color.green_color));
        } else if (runningState.equals("false")) {
            runningStateName = "站点尚未开通";
            running_status_tv.setTextColor(getResources().getColor(R.color.red_color));
        }
        running_status_tv.setText(runningStateName);

//        alarm_handle_user.setText("处理人：" + (TextUtils.isEmpty(detailInfo.getHandleUser()) ? "" : detailInfo.getHandleUser()));
//        alarm_handle_time.setText("处理时间：" + (TextUtils.isEmpty(detailInfo.getHandleTime()) ? "" : detailInfo.getHandleTime()));
//        alarm_handle_type.setText("处理类型：" + getHandleTypeValue(detailInfo.getHandleType()));
    }


    /**
     * 根据站点id获取视频预览url
     */
    private void RequestVideoPreviewUrl() {
        showProgressDialog();

        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CODE_GetPlatformPreviewRtspUrl + "", createParams4PreviewUrl());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                hideProgressDialog();
                e.printStackTrace();
                toast("未获取到视频预览url");
                layout_rt_video.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response, int id) {
                hideProgressDialog();
                String error = "无效的视频预览url";
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "返回值：" + value);
                    String dataStr = DataUtils.getReturnData(value);

                    org.json.JSONObject jsonObj = new org.json.JSONObject(dataStr);
                    error = jsonObj.getString("error");
                    String listUrlStr = jsonObj.getString("listUrl");
                    rtUrlList = JSONArray.parseArray(listUrlStr, String.class);
                    videoRTAdapter.setDataList(rtUrlList);
                    videoRTAdapter.notifyDataSetChanged();
                    layout_rt_video.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(error);
                    layout_rt_video.setVisibility(View.GONE);
                }
            }
        });


//        String url = DataUtils.createReqUrl4Get4PreviewUrl(application.getIP(), application.getUid(),
//                Finals.CMD_Station_Video_PreviewUrl, StnNo);
//        doRequestGet(url, new StringCallback() {
//            @Override
//            public void onError(Call call, final Exception e, int id) {
//                hideProgressDialog();
//                e.printStackTrace();
//                toast("未获取到视频预览url");
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                hideProgressDialog();
//                try {
//                    // Todo 这里需要解析一个code和msg结构的协议，例如：请求能正常返回，但预览Url为空，需要提示错误信息。
//                    String value = DataUtils.getRespString(response);
//                    Log.i(TAG, "返回值：" + value);
//                    String dataStr = DataUtils.getReturnData(value);
//                    PreviewUrlBean obj = JSON.parseObject(dataStr, PreviewUrlBean.class);
//                    String previewUrl = obj.getsPreviewUrl();
//                    String stnNo = StnNo;
//                    if (!TextUtils.isEmpty(previewUrl)) {
//                        VUtils.gotoVideoPreview(context, previewUrl, stnNo);
//                    } else {
//                        toast("无效的视频预览url");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    toast("未获取到视频预览url");
//                }
//            }
//        });
    }

    /**
     * 视频预览url
     */
    private String createParams4PreviewUrl() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("id", itemBean.getUnitId());//单元/PCDT/STN ID
            jsonObj.put("idType", 0);//表示上边的id类型：0为单元，1为PCDT，2为Stn监控点
            jsonObj.put("internet", 0);//TODO 0表示获取内网url，1表示获取公网url
            jsonObj.put("StreamType", 0);//0主码流，1子码流
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 根据告警id获取历史告警视频url列表
     */
    private void RequestVideoHisUrl() {
        showProgressDialog();

        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CODE_GetVideoList_ByAlarmRcdId + "", createParams4VideoHis());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                hideProgressDialog();
                e.printStackTrace();
                toast("未获取到历史告警视频");
                layout_his_alarm_video.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response, int id) {
                hideProgressDialog();
                String error = "无效的历史告警视频url";
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "返回值：" + value);
                    String dataStr = DataUtils.getReturnData(value);
                    hisUrlList = JSONArray.parseArray(dataStr, String.class);
                    videoHisAdapter.setDataList(hisUrlList);
                    videoHisAdapter.notifyDataSetChanged();
                    layout_his_alarm_video.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(error);
                    toast("未获取到历史告警视频");
                    layout_his_alarm_video.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * 提交处理意见
     */
    private void RequestHandleOpinion(String handleWords) {
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_UpdateHandleInfo, createHandleOpinionParams(handleWords));

        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                toast("处理意见提交失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "返回值：" + value);
                    if (DataUtils.getReturnResult(value).equals("true")) {
                        toast("处理意见提交成功!");
                        RequestAlarmTimeInfo();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toast("处理意见提交失败!");
                }
            }
        });
    }


    /**
     * 告警大图请求
     *
     * @param needLook 是否需要查看，也就是区分是初始化时的自动请求（不需要查看），还是用户点击按钮的主动请求（需要查看）<br/>
     *                 needToast 主要主动请求图片需要Toast提示或者Dialog告知,目前不需要了
     */
    private void RequestAlarmImage(final boolean needLook) {
        if (needLook) {
            showProgressDialog();
        }

        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetIPCPicByAlarmNo, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                if (needLook) {
                    hideProgressDialog();
                }
                hideAllProgressDialog();
                e.printStackTrace();
                if (needLook) {
                    toast("未获取到告警图片");
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (needLook) {
                    hideProgressDialog();
                }
                hideAllProgressDialog();
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "返回值：" + value);
                    String data = DataUtils.getReturnData(value);
                    picBean = JSONArray.parseArray(data, PicBean.class).get(0);

                    alarm_img_url = picBean.getPicPath().substring(1);
                    alarm_img_url = UrlUtils.createUrlPath(application.getIP(), alarm_img_url);

                    if (needLook) {
                        gotoCorpImage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (needLook) {
                        toast("未获取到告警图片");
                    }
                }
            }
        });
    }


    /**
     * 28. 远程取消/确认告警<br/>
     * handleCode 0 确认， 1 取消
     */
    private void RequestHandleAlarm(final String handleCode) {
        final String name = handleCode.equals("0") ? "远程确认告警" : "远程取消告警";
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_AlarmDeal, createHandleAlarmParams(handleCode));

        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                toast(name + "失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "返回值：" + value);
                    if (value.contains("sMsg")) {
                        String dataStr = DataUtils.getReturnData(value);
                        org.json.JSONObject failObj = new org.json.JSONObject(dataStr);
                        String msgStr = failObj.getString("sMsg");
                        toast(msgStr);
                    } else {
                        if (DataUtils.getReturnResult(value).equals("true")) {
                            toast(name + "成功");
                        }
                    }
                    if (handleCode.equals(HANDLE_ALARM_SURE + "")) {
                        RequestControlCMD(StnNo, UnitNo, Finals.CODE_SURE_ALARM);
                    } else {
                        RequestControlCMD(StnNo, UnitNo, Finals.CODE_CLEAR_ALARM);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(name + "失败");
                }
            }
        });
    }


    /**
     * 去抠图
     */
    private void gotoCorpImage() {
        Intent intent = new Intent(context, ImageActivity.class);

        intent.putExtra("path", alarm_img_url);

        intent.putExtra("AlarmRcdId", AlarmRcdId);
        intent.putExtra("stnNo", StnNo);
        intent.putExtra("unitNo", UnitNo);
        intent.putExtra("picCamNo", picBean.getPicCamNo());
        intent.putExtra("picFpp", picBean.getPicFpp());
        intent.putExtra("picId", picBean.getPicId());
        int code = Integer.parseInt(picBean.getCodeId()) - 3000;
        intent.putExtra("CodeId", code + "");

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    private void hideAllProgressDialog() {
        if (isInfoOk && isStateOK) {
            hideProgressDialog();
        }
    }


    /**
     * 雷达大图 请求
     */
    private void RequestRadarPic() {
        showProgressDialog();
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetRadarPicByAlarmNo, AlarmRcdId); // 2018.8.17 和zzx商量后修改 createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                hideProgressDialog();
                e.printStackTrace();
                toast("未获取到雷达图片");
            }

            @Override
            public void onResponse(String response, int id) {
                hideProgressDialog();
                try {
                    DataUtils.showRadarView((Activity) context, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    toast("未获取到雷达图片");
                }
            }
        });
    }


    /**
     * 主动请求抓图、抠图 图片信息 请求，返回图片信息
     * Finals.CODE_REALITME_PIC 抓图，Finals.CODE_CORP_PIC 抠图
     */
    private void RequestManualPic(final String code) {
        showProgressDialog();
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetLargePic, createManualPicParams(code));
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                if (code.equals(Finals.CODE_REALITME_PIC_BACK + "")) {
                    hasNewRtImage = false;
                } else if (code.equals(Finals.CODE_CORP_PIC_BACK + "")) {
                    hasNewKoutuImage = false;
                }
                hideProgressDialog();
                toast("图片获取失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                if (code.equals(Finals.CODE_REALITME_PIC_BACK + "")) {
                    hasNewRtImage = false;
                } else if (code.equals(Finals.CODE_CORP_PIC_BACK + "")) {
                    hasNewKoutuImage = false;
                }
                hideProgressDialog();
                try {
                    // TODO 获取图片、视频的url，跳转到图片详情页
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "高清局部图/抓图 返回值：" + value);
                    String data = DataUtils.getReturnData(value);
                    PicBean picBean = JSON.parseObject(data, PicBean.class);
                    String url = picBean.getPicPath(); // PicPath
                    url = url.substring(1);
                    url = UrlUtils.createUrlPath(application.getIP(), url);
                    if (code.equals(Finals.CODE_REALITME_PIC_BACK + "")) {
                        rt_img_url = url;
                        VUtils.gotoImage(context, rt_img_url);
                    } else if (code.equals(Finals.CODE_CORP_PIC_BACK + "")) {
                        koutu_url = url;
                        VUtils.gotoImage(context, koutu_url);
                    }

                    VUtils.gotoImage(context, url);

                } catch (Exception e) {
                    e.printStackTrace();
                    toast("图片获取失败");
                }
            }
        });
    }


    /**
     * 抓图、抠图
     */
    private String createManualPicParams(String codeId) {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("nCodeId", codeId);
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    private String createParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("lAlarmRcdId", AlarmRcdId);
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String createParams4VideoHis() {
        return AlarmRcdId;
    }


    /**
     * TODO 远程确认、取消告警<br/>
     * AlarmRcdId、Handle、HandleType、HandleTime、HandleUser(使用登录用户名)
     */
    private String createHandleAlarmParams(String handleCode) {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("Id", "0"); // 处理意见，传空
            jsonObj.put("AlarmRcdId", AlarmRcdId);
            jsonObj.put("Handle", "true"); // 是否已处理
            jsonObj.put("HandleUser", SpUtil.getData(Finals.SP_USERNAME)); // 处理人
            jsonObj.put("HandleTime", Utils.getSysNowTime());
            jsonObj.put("HandleType", handleCode); // 处理告警类型，0-正常告警(4900)，1-系统虚警(4528)
            jsonObj.put("HandleInfo", ""); // 处理意见，传空


            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 告警处理意见
     */
    private String createHandleOpinionParams(String handleWords) {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("AlarmRcdId", AlarmRcdId);
            jsonObj.put("HandleInfo", handleWords);
            jsonObj.put("HandleTime", Utils.getSysNowTime());
            jsonObj.put("HandleUser", SpUtil.getData(Finals.SP_USERNAME));
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 抓图请求
     */
    private String CreateGetPicParams(int codeId, String param1, String param2, String param3, String param4) {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();

            String userName = SpUtil.getData(Finals.SP_USERNAME);
            String ClientId = SpUtil.getData(Finals.SP_CLIENTID);

            jsonObj.put("StnNo", Integer.parseInt(StnNo)); // 监控点编号(tid)
            jsonObj.put("UnitNo", Integer.parseInt(UnitNo));  // 单元编号(sid)
            jsonObj.put("CodeId", codeId);

            jsonObj.put("UserName", userName);
            jsonObj.put("ClientId", ClientId);
            jsonObj.put("CodeTm", DataUtils.getCurrentTm());
            jsonObj.put("No", DataUtils.getCMD_No());
            jsonObj.put("Platform", 3);  // 平台
            jsonObj.put("Param1", param1);
            jsonObj.put("Param2", param2);
            jsonObj.put("Param3", param3);
            jsonObj.put("Param4", param4);
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
                case R.id.alarm_image_tv:// 看大图
                    RequestAlarmImage(true);
                    break;
                case R.id.radar_image_tv: // 看雷达图
                    RequestRadarPic();
                    break;
                case R.id.rt_image_tv: // 抓取实时图片
                    if (picBean == null || TextUtils.isEmpty(picBean.getPicCamNo())) {
                        toast("尚不能抓取实时图片，请稍后重试");// TODO 怎么提示
                        return;
                    }
                    String dataStr = CreateGetPicParams(Finals.CODE_REALITME_PIC,
                            UnitNo, picBean.getPicCamNo(), picBean.getPicFpp(), "0");
                    String url = DataUtils.createReqUrl4Get(
                            application.getIP(), application.getUid(), Finals.CMD_SystemControlCmd, dataStr);
                    doRequestGet(url, null);
                    toast("抓图命令已发送");

                    break;
                case R.id.rt_look_tv:// 查看已到达的实时图
                    if (!hasNewRtImage && !TextUtils.isEmpty(rt_img_url)) {
                        VUtils.gotoImage(context, rt_img_url);
                    } else {
                        RequestManualPic(Finals.CODE_REALITME_PIC_BACK + "");
                    }
                    break;
                case R.id.koutu_image_tv:// 进入抠图页面
                    if (picBean == null || TextUtils.isEmpty(picBean.getPicCamNo())) {
                        toast("尚不能获取高清局部图，请稍后重试");
                        return;
                    }

                    gotoCorpImage();
                    break;
                case R.id.koutu_look_tv: // 查看已到达的高清局部图
                    if (!hasNewKoutuImage && !TextUtils.isEmpty(koutu_url)) {
                        VUtils.gotoImage(context, koutu_url);
                    } else {
                        RequestManualPic(Finals.CODE_CORP_PIC_BACK + "");
                    }
                    break;
                case R.id.rt_video_tv:
                    RequestVideoPreviewUrl();
                    break;
                case R.id.his_video_tv:// 历史告警视频
                    RequestVideoHisUrl();
                    break;
                case R.id.sure_alarm_btn://确认告警，需要输入密码校验
                    index = 6;
                    showPwdDialog(controlListener);
                    break;
                case R.id.cancel_alarm_btn://取消告警，需要输入密码校验
                    index = 7;
                    showPwdDialog(controlListener);
                    break;
                case R.id.send_handle_btn: // 提交处理意见
                    String content = handleinfo.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        toast("请输入处理意见");
                        return;
                    }
                    RequestHandleOpinion(content);

                    break;
                default:
                    break;
            }
        }
    }


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
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                @Override
                public void run() {
                    imm.hideSoftInputFromWindow(pEdit.getWindowToken(), 0);// 关闭输入法
                    // Log.i(TAG, "密码： " + pEdit.getText().toString().trim());
                    String inputStr = pEdit.getText().toString();
                    if (TextUtils.isEmpty(inputStr)) {
                        toast("请输入密码");
                    } else {
                        String pwdStr = SpUtil.getData(Finals.SP_PWD);
                        if (pwdStr.equals(inputStr)) {
                            switch (index) {
                                case 6: // 确认告警
                                    RequestHandleAlarm(HANDLE_ALARM_SURE);
                                    break;
                                case 7: // 取消告警
                                    RequestHandleAlarm(HANDLE_ALARM_CANCEL);
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
     * 推送处理：控制命令发送成功 -- TODO 可以不接收
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealAlarmBackEvent(AlarmDetailCMDBackEvent event) {
        PushBeanBase pushBean = event.getPushBean();
        String pStnNo = pushBean.getStnNo();
        String pUnitNo = pushBean.getUnitNo();
        if (pStnNo.equals(StnNo)) { //  && pUnitNo.equals(UnitNo)
            Log.i(TAG, "AlarmDetailBackEvent处理: ");
            toast("控制命令发送成功");
            if (Finals.CODE_CLEAR_ALARM_BACK == Integer.parseInt(pushBean.getCodeId()) || Finals.CODE_SURE_ALARM_BACK == Integer.parseInt(pushBean.getCodeId())) {
                //finish();
            }
        }
    }


    /**
     * 推送处理：告警详情状态刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealAlarmEvent(AlarmDetailRefreshEvent event) {
        PushBeanBase pushBean = event.getPushBean();
        String pStnNo = pushBean.getStnNo();
        String pUnitNo = pushBean.getUnitNo();
        // 判断是不是该条告警 TODO 优点：节省资源 // 问题：接口刷新不及时
        if (pStnNo.equals(StnNo)) { // 不判断UnitNo，因为上下行灯，列调，gmdt都和UnitNo没关系  && pUnitNo.equals(UnitNo)
            if (isStateOK) {
                Log.i(TAG, "AlarmDetailRefreshEvent处理: ");
                toast("告警点状态发生变化");
                RequestAlarmTimeInfo();
            }
        }
    }


    /**
     * 推送处理：各种告警图片到达,Toast通知，请求图片，直接跳转至图片详情页
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void dealPicEvent(PicEvent event) {
        PushBeanBase pushBean = event.getPushBean();
        String codeId = pushBean.getCodeId();
        String pStnNo = pushBean.getStnNo();
        String pUnitNo = pushBean.getUnitNo();
        try {
            final int code = Integer.parseInt(codeId);
            // 判断是不是该条告警
            if (pStnNo.equals(StnNo)) { // 不处理巡视图片  && pUnitNo.equals(UnitNo)
                Log.i(TAG, "PicEvent处理: ");

                switch (code) {
                    case Finals.CODE_PIC_YUJING:        // 预警图片到达
                    case Finals.CODE_PIC_SMALL_YUJING:  // 设备告警图片到达
                    case Finals.CODE_PIC_SMALL_GAOJING: // 小物体告警图片到达
                    case Finals.CODE_PIC_YIWU_ARRIVE:   // 异物告警图片到达
//                        toast(name + "到达，请及时查看");
                        toast("告警图片到达，请及时查看");
                        break;
                    case Finals.CODE_REALITME_PIC_BACK: // 实时抓取图片
                        hasNewRtImage = true;
                        toast("实时抓取图片到达，请及时查看");
                        rt_look_tv.setVisibility(View.VISIBLE);
                        break;
                    case Finals.CODE_CORP_PIC_BACK:     // 抠图：局部高清图片
                        hasNewKoutuImage = true;
                        toast("局部高清图片到达，请及时查看");
                        koutu_look_tv.setVisibility(View.VISIBLE);
                        break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
