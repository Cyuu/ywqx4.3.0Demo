package com.thdz.ywqx.ui.Activity.station;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.VideoListAdapter;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.PicBean;
import com.thdz.ywqx.bean.PushBeanBase;
import com.thdz.ywqx.bean.RadarPicBean;
import com.thdz.ywqx.bean.UnitBean;
import com.thdz.ywqx.bean.UnitDetailStatusBean;
import com.thdz.ywqx.event.PicEvent;
import com.thdz.ywqx.event.UnitDetailCMDBackEvent;
import com.thdz.ywqx.event.UnitDetailRefreshEvent;
import com.thdz.ywqx.ui.Activity.ImageActivity;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.SpUtil;
import com.thdz.ywqx.util.UrlUtils;
import com.thdz.ywqx.util.VUtils;
import com.thdz.ywqx.view.NoScrollListView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 单元详情页面<br/>
 * 获取当前页面的StnNo， UnitNo的方式 -- unitBean， <br/>
 * <br/>
 */
public class UnitDetailActivity extends BaseActivity {

    @BindView(R.id.control_layout)
    SwipeRefreshLayout control_layout;

    @BindView(R.id.layout_new_image)
    RelativeLayout layout_new_image;

    @BindView(R.id.layout_new_radar)
    RelativeLayout layout_new_radar;

    @BindView(R.id.layout_get_hdpic)
    RelativeLayout layout_get_hdpic;

    @BindView(R.id.layout_look_hdpic)
    RelativeLayout layout_look_hdpic;

    @BindView(R.id.layout_look_video)
    RelativeLayout layout_look_video;

    @BindView(R.id.layout_his_video)
    RelativeLayout layout_his_video;

    // 实时状态

    @BindView(R.id.unit_gstr_monitor)
    TextView unit_gstr_monitor;
    @BindView(R.id.unit_gstr_run)
    TextView unit_gstr_run;

    @BindView(R.id.unit_detail_control)
    LinearLayout unit_detail_control;


    // 站点控制控件
    @BindView(R.id.open_alarm_btn)
    TextView open_alarm_btn; // 打开警号

    @BindView(R.id.close_alarm_btn)
    TextView close_alarm_btn; // 关闭警号

    @BindView(R.id.open_gstr_btn)
    TextView open_gstr_btn; // 启用地面监测单元主控(GSTR)

    @BindView(R.id.close_gstr_btn)
    TextView close_gstr_btn; // 屏蔽地面监测单元主控(GSTR)

    @BindView(R.id.set_unitnormal)
    TextView set_unitnormal; // 设置地面监测单元主控(GSTR)警号正常模式

    @BindView(R.id.layout_rt_video)
    View layout_rt_video; // 实时视频布局

    @BindView(R.id.lv_rt_video)
    NoScrollListView lv_rt_video; // 实时视频列表



    private VideoListAdapter videoRTAdapter;
    private List<String> rtUrlList = new ArrayList<>();


    private UnitDetailStatusBean detailState;

    private PicBean picBean;
    private RadarPicBean radarPicBean;

    private int index = -1;// 同页面不同的控件出发对话框关闭后的回调标识

    private boolean isInfoOK = true;
    /**
     * 用途：<br/>
     * 1 协助判断对话框的展示和隐藏<br/>
     * 2 状态信息变化推送信息返回时，判断是否正在请求状态信息，如果是，则不再发起新的请求。<br/>
     */
    private boolean isStateOK = true;

    private UnitBean unitBean;
    private String unitId;

    private String unit_image_url;          // 最新的巡视或者报警图片url
    private String unit_radar_image_url;    // 最新的雷达图片url
    private String koutu_url;               // 高清局部图片url -- 抠图url

    private boolean hasNewImage = false; // 是否新的图片到来
    private boolean hasNewRadar = false; // 是否新的雷达图片到来
    private boolean hasNewKoutu = false; // 是否新的抠图图片



    @Override
    public void setContentView() {
        setContentView(R.layout.activity_unit_detail);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        try {
            if (SpUtil.getBoolean(Finals.SP_CONTROL_FLAG)) {
                unit_detail_control.setVisibility(View.VISIBLE);
            } else {
                unit_detail_control.setVisibility(View.GONE);
            }

            setTitle("监控单元详情");
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                unitBean = (UnitBean) bundle.getSerializable("unitBean");
                unitId = unitBean.getUnitId();
                setTitle(unitBean.getUnitName());
            }

            layout_new_image.setOnClickListener(this);
            layout_new_radar.setOnClickListener(this);
            layout_get_hdpic.setOnClickListener(this);
            layout_look_hdpic.setOnClickListener(this);
            layout_look_video.setOnClickListener(this);
            layout_his_video.setOnClickListener(this);

            open_alarm_btn.setOnClickListener(this);
            close_alarm_btn.setOnClickListener(this);
            open_gstr_btn.setOnClickListener(this);
            close_gstr_btn.setOnClickListener(this);
            set_unitnormal.setOnClickListener(this);

            setBackActive();
            setRightTopGone();

            VUtils.setSwipeColor(control_layout);
            control_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    RequestDetailState();
                    control_layout.setRefreshing(false);
                }
            });

//           RequestDetailState();
            setTimerEnable(true);

            videoRTAdapter = new VideoListAdapter(context);
            lv_rt_video.setAdapter(videoRTAdapter);
            lv_rt_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String url = rtUrlList.get(position);
                    Log.i(TAG, "即将打开的实时视频预览地址：" + url);
                    VUtils.gotoVideoPreview(context, url, unitBean.getUnitName());
                }
            });
            layout_rt_video.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public void cycleRequest() {
        RequestDetailState();
    }


    /**
     * 状态信息
     */
    private void RequestDetailState() {
        isStateOK = false;
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetUnitStatusByIds, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                isStateOK = true;
                hideAllProgressDialog();
                toast("状态信息获取失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                isStateOK = true;
                hideAllProgressDialog();
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "单元状态信息，返回值：" + value);

//                    detailState = JSONArray.parseArray(DataUtils.getReturnData(value), UnitDetailStatusBean.class).get(0);
                    List<UnitDetailStatusBean> stateList = JSON.parseArray(DataUtils.getReturnData(value), UnitDetailStatusBean.class);
                    for (UnitDetailStatusBean iii : stateList) {
                        if (iii.getStnId().equals(unitBean.getStnId()) && iii.getUnitId().equals(unitId)) {
                            detailState = iii;
                            break;
                        }
                    }

                    showDetailStatusByCode();
                } catch (Exception e) {
                    e.printStackTrace();
                    toast("状态信息获取失败");
                }
            }
        });
    }


    private void hideAllProgressDialog() {
        if (isInfoOK && isStateOK) {
            hideProgressDialog();
        }
    }


    /**
     * 根据Ids，请求最新的IPC图片信息<br/>
     */
    private void RequestUnitIPCImage() {
        showProgressDialog();
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetLastPic, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                hasNewImage = false;
                hideProgressDialog();
                e.printStackTrace();
                toast("尚无图片，请稍后重试");
            }

            @Override
            public void onResponse(String response, int id) {
                hasNewImage = false;
                hideProgressDialog();
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "返回值：" + value);
                    picBean = JSON.parseObject(DataUtils.getReturnData(value), PicBean.class);
                    unit_image_url = picBean.getPicPath().substring(1);
                    unit_image_url = UrlUtils.createUrlPath(application.getIP(), unit_image_url);

                    gotoCorpImage();

                } catch (Exception e) {
                    e.printStackTrace();
                    toast("尚无图片，请稍后重试");
                }
            }
        });
    }


    /**
     * 根据Ids， 请求最新的雷达图
     */
    private void RequestUnitRadarImage() {
        showProgressDialog();
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetLastRadar, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                hasNewRadar = false;
                hideProgressDialog();
                e.printStackTrace();
                toast("尚无雷达图片，请稍后重试");
            }

            @Override
            public void onResponse(String response, int id) {
                hasNewRadar = false;
                hideProgressDialog();
                try {
                    DataUtils.showRadarView((Activity) context, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    toast("尚无雷达图，请稍后重试");
                }
            }
        });
    }


    /**
     * 主动请求抓图、抠图 图片信息 请求，返回图片信息
     */
    private void RequestManualPic() {
        showProgressDialog();
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetLargePic, createManualPicParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                hasNewKoutu = false;
                hideProgressDialog();
                toast("图片获取失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                hasNewKoutu = false;
                hideProgressDialog();
                try {
                    // TODO 获取图片、视频的url，跳转到图片详情页
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "返回值：" + value);
                    String data = DataUtils.getReturnData(value);
                    PicBean picBean = JSON.parseObject(data, PicBean.class);
                    koutu_url = picBean.getPicPath().substring(1);
                    koutu_url = UrlUtils.createUrlPath(application.getIP(), koutu_url);
                    VUtils.gotoImage(context, koutu_url);

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
    private String createManualPicParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("nCodeId", Finals.CODE_CORP_PIC_BACK + "");
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
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

                    JSONObject jsonObj = new JSONObject(dataStr);
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
    }

    /**
     * 展示详情信息  String --> AlarmDetailInfoBean
     */
    private void showDetailStatusByCode() {

        // 地面监测单元主控(GSTR)监测状态- 3970 正常  3971 异常
        showTvValue(unit_gstr_monitor, detailState.getGstrMonitorState(), 3970, 3971);
        showTvColor(unit_gstr_monitor, detailState.getGstrMonitorState(), 3970);

        // 地面监测单元主控(GSTR)运行 -4004 正常    4005 异常
        showTvValue(unit_gstr_run, detailState.getUnitWorkState(), 4004, 4005);
        showTvColor(unit_gstr_run, detailState.getUnitWorkState(), 4004);

    }


    /**
     * 视频预览url
     */
    private String createParams4PreviewUrl() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("id", unitId);//单元/PCDT/STN ID
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
     * nStnId、nPcdtId、nUnitId,有效，其他传0
     */
    private String createParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("nRBId", unitBean.getRBId());
            jsonObj.put("nRLId", unitBean.getRLId());
            jsonObj.put("nStnId", unitBean.getStnId());
            jsonObj.put("nPcdtId", unitBean.getPcdtId());
            jsonObj.put("nUnitId", unitId);

            jsonObj.put("nRSId", unitBean.getRSId());
            jsonObj.put("nRWIId", unitBean.getRWIid());
            jsonObj.put("nAppId", "0");
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
                case R.id.video_tv: // 看视频
                    toast("尚未开放");
                    break;
                case R.id.layout_new_image:// 实时图
                    RequestUnitIPCImage();
                    break;
                case R.id.layout_new_radar: // 雷达图
                    RequestUnitRadarImage();
                    break;
                case R.id.layout_get_hdpic:// 进入抠图页面
                    if (picBean == null || TextUtils.isEmpty(picBean.getPicCamNo())) {
                        toast("尚不能获取高清局部图，请稍后重试");
                        return;
                    }

                    gotoCorpImage();
                    break;
                case R.id.layout_look_hdpic: // 查看已到达的高清局部图
                    if (!hasNewKoutu && !TextUtils.isEmpty(koutu_url)) {
                        VUtils.gotoImage(context, koutu_url);
                    } else {
                        RequestManualPic();
                    }

                    break;
                case R.id.layout_look_video:
                    RequestVideoPreviewUrl();
                    break;
                case R.id.layout_his_video:

                    break;
                case R.id.open_alarm_btn:
                    index = 0;
                    showPwdDialog(controlListener);
                    break;
                case R.id.close_alarm_btn:
                    index = 1;
                    showPwdDialog(controlListener);
                    break;
                case R.id.open_gstr_btn:
                    index = 2;
                    showPwdDialog(controlListener);
                    break;
                case R.id.close_gstr_btn:
                    index = 3;
                    showPwdDialog(controlListener);
                    break;
                case R.id.set_unitnormal:
                    index = 4;
                    showPwdDialog(controlListener);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 去抠图
     */
    private void gotoCorpImage() {
        Intent intent = new Intent(context, ImageActivity.class);

        intent.putExtra("path", unit_image_url);
        intent.putExtra("stnNo", unitBean.getStnNo());
        intent.putExtra("unitNo", unitBean.getUnitNo());
        intent.putExtra("picCamNo", picBean.getPicCamNo());
        intent.putExtra("picFpp", picBean.getPicFpp());
        intent.putExtra("picId", picBean.getPicId());
        int code = Integer.parseInt(picBean.getCodeId()) - 3000;
        intent.putExtra("CodeId", code + "");

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


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
//                    Log.i(TAG, "密码： " + pEdit.getText().toString().trim());
                    String inputStr = pEdit.getText().toString();
                    if (TextUtils.isEmpty(inputStr)) {
                        toast("请输入密码");
                    } else {
                        String pwdStr = SpUtil.getData(Finals.SP_PWD);
                        if (pwdStr.equals(inputStr)) {
                            switch (index) {
                                case 0: // 手动打开警号
                                    RequestControlCMD(unitBean.getStnNo(), unitBean.getUnitNo(), Finals.CODE_OPEN_ALERT);
                                    break;
                                case 1: // 手动关闭警号
                                    RequestControlCMD(unitBean.getStnNo(), unitBean.getUnitNo(), Finals.CODE_CLOSE_ALERT);
                                    break;
                                case 2: // 启用地面监测单元主控(GSTR)
                                    RequestControlCMD(unitBean.getStnNo(), unitBean.getUnitNo(), Finals.CODE_OPEN_GSTR);
                                    break;
                                case 3: // 屏蔽地面监测单元主控(GSTR)
                                    RequestControlCMD(unitBean.getStnNo(), unitBean.getUnitNo(), Finals.CODE_CLOSE_GSTR);
                                    break;
                                case 4: // 设置地面监测单元主控(GSTR)警号正常模式
                                    RequestControlCMD(unitBean.getStnNo(), unitBean.getUnitNo(), Finals.CODE_SET_GSTR_NORMAL);
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
     * 相应推送的：控制命令发送成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealUnitBackEvent(UnitDetailCMDBackEvent event) {
        Log.i(TAG, "UnitDetailCMDBackEvent处理: ");
        toast("控制命令发送成功");
    }


    /**
     * 相应推送的：单元详情状态刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealUnitRefreshEvent(UnitDetailRefreshEvent event) {
        PushBeanBase pushBean = event.getPushBean();
        String pStnNo = pushBean.getStnNo();
        String pUnitNo = pushBean.getUnitNo();
        // 判断是不是该监控单元 TODO 优点：节省资源 // 问题：接口刷新不及时
        if (pStnNo.equals(unitBean.getStnNo()) && pUnitNo.equals(unitBean.getUnitNo())) {
            Log.i(TAG, "UnitDetailRefreshEvent处理: ");
//            toast("监控单元状态发生变化");
            if (isStateOK) {
                RequestDetailState();
            }
        }

    }


    /**
     * 推送处理：各种告警图片到达,Toast通知，请求图片，直接跳转至图片详情页
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealPicEvent(PicEvent event) {
        PushBeanBase pushBean = event.getPushBean();
        String codeId = pushBean.getCodeId();
        String pStnNo = pushBean.getStnNo();
        String pUnitNo = pushBean.getUnitNo();
//        Log.i(TAG, "推送来的：StnNo: " + pStnNo + ",UnitNo=" + pUnitNo);
//        Log.i(TAG, "本页面的：StnNo: " + StnNo + ",UnitNo=" + UnitNo);
        try {
            // 判断是不是该条告警
            if (pStnNo.equals(unitBean.getStnNo())) { // && pUnitNo.equals(unitBean.getUnitNo())
                Log.i(TAG, "PicEvent处理: ");
                int code = Integer.parseInt(codeId);
                switch (code) {
                    case Finals.CODE_PIC_YUJING: // 预警图片到达
                    case Finals.CODE_PIC_SMALL_YUJING: // 小物体预警图片到达
                    case Finals.CODE_PIC_SMALL_GAOJING: // 小物体告警图片到达
                    case Finals.CODE_PIC_YIWU_ARRIVE: // 异物告警图片到达
                    case Finals.CODE_REALITME_PIC_BACK: // 手动抓图
                    case Finals.CODE_IPC_RT_ARRIVE:
                        hasNewImage = true;
                        toast("新图片到达，请及时查看");
                        break;
                    case Finals.CODE_RADAR_RT_BACK: // 雷达数据--实时图片
                    case Finals.CODE_RADAR_ALARM_BACK: // 雷达数据--告警图片
                        hasNewRadar = true;
                        toast("雷达图片到达，请及时查看");
                        break;
                    case Finals.CODE_CORP_PIC_BACK:
                        hasNewKoutu = true;
                        toast("高清局部图片到达，请及时查看");
                        layout_look_hdpic.setVisibility(View.VISIBLE);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
