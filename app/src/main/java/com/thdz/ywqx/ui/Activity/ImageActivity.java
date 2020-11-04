package com.thdz.ywqx.ui.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.thdz.ywqx.R;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.ALPBean;
import com.thdz.ywqx.bean.Dot;
import com.thdz.ywqx.bean.PicBean;
import com.thdz.ywqx.bean.PushBeanBase;
import com.thdz.ywqx.event.PicEvent;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.FileUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.SpUtil;
import com.thdz.ywqx.util.UrlUtils;
import com.thdz.ywqx.util.VUtils;
import com.thdz.ywqx.view.CutImageView;
import com.thdz.ywqx.view.TouchImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 大图片展示页面<br/>不支持左右滑动切换图片<br/>, TODO 需要图片id，唯一标识<br/>
 * 抠图在这里完成。
 */
public class ImageActivity extends BaseActivity {

    @BindView(R.id.image_zoom)
    TouchImageView image_zoom; // 可放大拉伸图片控件

    @BindView(R.id.image_save)
    TextView image_save;

    @BindView(R.id.image_cutter)
    TextView image_cutter; // 抠图按钮

    @BindView(R.id.quit_cutter)
    TextView quit_cutter; // 退出抠图 按钮

    @BindView(R.id.image_cutter_layout)
    RelativeLayout image_cutter_layout;

    @BindView(R.id.cutter_image)
    CutImageView cutter_image;

    private String imgPath;
    // private boolean isUrl;

    private Bitmap bitmap;

    private String stnNo;
    private String unitNo;
    private String picCamNo; // 镜头号
    private String picFpp; // 预置位
    private String picId; // 图片id
    private String CodeId; // 请求图片的CodeId

    /**
     * 截图获取到的mOldX, mOldY, mCurrentX, mCurrentY
     */
    private int topX, topY, bottomX, bottomY;
    private Intent mIntent;

    @Override
    public void cycleRequest(){
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_image);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
//        EventBus.getDefault().register(this);
        setTitle("查看大图");
        setBackActive();
        setRightTopGone();

        mIntent = getIntent();

        stnNo = mIntent.getStringExtra("stnNo");
        unitNo = mIntent.getStringExtra("unitNo");
        picCamNo = mIntent.getStringExtra("picCamNo"); // 镜头号
        picFpp = mIntent.getStringExtra("picFpp"); // 预置位
        picId = mIntent.getStringExtra("picId"); // 图片id
        CodeId = mIntent.getStringExtra("CodeId"); // 请求图片的CodeId

        image_save.setOnClickListener(this);
        image_cutter.setOnClickListener(this);
        quit_cutter.setOnClickListener(this);

        imgPath = mIntent.getStringExtra("path");
        if (TextUtils.isEmpty(imgPath)) {
            toast("图片不存在");
            finish();
            return;
        }
        showBmpByUrl(imgPath);

        cutter_image.setOnCutterPicture(new CutImageView.OnCutterPicListener() {
            @Override
            public void getSelectLocation(float mOldX, float mOldY, float mCurrentX, float mCurrentY) {
                topX = (int) mOldX;
                topY = (int) mOldY;
                bottomX = (int) mCurrentX;
                bottomY = (int) mCurrentY;

                sureCutter();

            }
        });

    }


    private void showBmpByUrl(String imgPath) {
        OkHttpUtils
                .get()
                .url(imgPath)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        toast("图片不存在");
                    }

                    @Override
                    public void onResponse(final Bitmap response, int id) {
                        bitmap = response;
                        image_zoom.setImageBitmap(response);
                        image_zoom.setZoom(1f);

                        if (!TextUtils.isEmpty(picId)) {// 如果picId为空，则不需要抠图
                            image_cutter.setVisibility(View.VISIBLE);
                        }

                    }
                });
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
        try {
            int code = Integer.parseInt(codeId);
            // 判断是不是该条告警
            if (pStnNo.equals(stnNo) ) { // 不处理巡视图片 && pUnitNo.equals(unitNo)
                Log.i(TAG, "PicEvent处理: ");
                /**
                 * 1 局部高清图，即时打开
                 * 2 其他图片，发送推迟通知
                 */
                switch (code) {
                    case Finals.CODE_PIC_YUJING:        // 预警图片到达
                    case Finals.CODE_PIC_SMALL_YUJING:  // 小物体预警图片到达
                    case Finals.CODE_PIC_SMALL_GAOJING: // 小物体告警图片到达
                    case Finals.CODE_PIC_YIWU_ARRIVE:   // 异物告警图片到达
                    case Finals.CODE_REALITME_PIC_BACK: // 手动抓图
                    case Finals.CODE_RADAR_ALARM_BACK:  // 雷达数据--图片
                        // 是否即刻请求url，打开图片？ 目前不处理。
                        break;
                    case Finals.CODE_CORP_PIC_BACK: // 抠图：高清局部图 -- 当前页能看
                        RequestManualPic();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 主动请求抓图、抠图 图片信息 请求，返回图片信息
     * Finals.CODE_REALITME_PIC 抓图，Finals.CODE_CORP_PIC 抠图
     */
    private void RequestManualPic() {
        showProgressDialog();
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetLargePic, createManualPicParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                hideProgressDialog();
                toast("图片获取失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                hideProgressDialog();
                try {
                    // TODO 获取图片、视频的url，跳转到图片详情页
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "高清局部图返回值：" + value);
                    String data = DataUtils.getReturnData(value);

                    PicBean picBean = JSON.parseObject(data, PicBean.class);
                    String url = picBean.getPicPath(); // PicPath
                    url = url.substring(1);
                    url = UrlUtils.createUrlPath(application.getIP(), url);

                    imgPath = url;
                    showBmpByUrl(imgPath);
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
     * 处理抠图参数， 发送抠图请求
     */
    private void RequestCorpPic(Dot dotTop, Dot dotBottom) {
        try {
            // param1 alpBean
            ALPBean alpBean = new ALPBean();
            alpBean.setGstrSid(unitNo);
            alpBean.setPic_Id(picId);
            alpBean.setIpcSid(picCamNo);
            alpBean.setFPP(picFpp);
            alpBean.setRes(CodeId);


            // param2 dotOrg
            Dot dotOrg = new Dot(bitmap.getWidth() + "", bitmap.getHeight() + "");

            // param3 dotTop，将屏幕图尺寸按照原图尺寸缩放：
            dotTop = getOrgDimenDot(dotTop);

            // param4 dotBottom，将屏幕图尺寸按照原图尺寸缩放：
            dotBottom = getOrgDimenDot(dotBottom);

            String dataStr = CreateGetPicParams(Finals.CODE_CORP_PIC, alpBean, dotOrg, dotTop, dotBottom);
            dataStr = dataStr.replace("\"x\"", "\"X\"").replace("\"y\"", "\"Y\"");

            String url = DataUtils.createReqUrl4Get(
                    application.getIP(), application.getUid(), Finals.CMD_SystemControlCmd, dataStr);

            doRequestGet(url, null);
            toast("抓图命令已发送");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据原图尺寸，计算截取图的坐标
     *
     * @param mDot 需要转化的点坐标
     *             bitmap 原图的bitmap
     */
    private Dot getOrgDimenDot(Dot mDot) {
        int dotX = Integer.parseInt(mDot.getX());
        int dotY = Integer.parseInt(mDot.getY());
        dotX = dotX * bitmap.getWidth() / application.screenWidth;
        dotY = dotY * bitmap.getWidth() / application.screenWidth;
        // param4 dotBottom，将屏幕图尺寸按照原图尺寸缩放：
        return new Dot(dotX + "", dotY + "");
    }


    /**
     * 抠图请求
     */
    private String CreateGetPicParams(int code, ALPBean alpBean, Dot dot2, Dot dot3, Dot dot4) {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();

            String userName = SpUtil.getData(Finals.SP_USERNAME);
            String ClientId = SpUtil.getData(Finals.SP_CLIENTID);

            jsonObj.put("StnNo", Integer.parseInt(stnNo)); // 监控点编号(tid)
            jsonObj.put("UnitNo", Integer.parseInt(unitNo));  // 单元编号(sid)
            jsonObj.put("CodeId", code); // Finals里定义的code

            // param1
            org.json.JSONObject alpObj = new org.json.JSONObject();
            alpObj.put("GstrSid", unitNo);
            alpObj.put("Pic_Id", picId); // String-->int
            alpObj.put("IpcSid", picCamNo);
            alpObj.put("FPP", picFpp);
            alpObj.put("Res", CodeId);

            // param2
            String param2 = com.alibaba.fastjson.JSONObject.toJSON(dot2).toString();

            // param3
            String param3 = com.alibaba.fastjson.JSONObject.toJSON(dot3).toString();

            // param4
            String param4 = com.alibaba.fastjson.JSONObject.toJSON(dot4).toString();

            jsonObj.put("UserName", userName);
            jsonObj.put("ClientId", ClientId);
            jsonObj.put("CodeTm", DataUtils.getCurrentTm());
            jsonObj.put("No", DataUtils.getCMD_No());
            jsonObj.put("Platform", 3);  // 平台
            jsonObj.put("Param1", alpObj);
            jsonObj.put("Param2", new JSONObject(param2));
            jsonObj.put("Param3", new JSONObject(param3));
            jsonObj.put("Param4", new JSONObject(param4));

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
                case R.id.image_save:
                    try {
                        FileUtils.saveImage(context, bitmap, imgPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.image_cutter:
                    setTitle("正在选择区域...");
                    image_zoom.setZoom(1f);
                    image_cutter_layout.setVisibility(View.VISIBLE);
                    image_cutter.setVisibility(View.GONE);
                    quit_cutter.setVisibility(View.VISIBLE);

                    initCutterImageview();

                    break;
                case R.id.quit_cutter:
                    setTitle("查看大图");
                    image_cutter_layout.setVisibility(View.GONE);
                    cutter_image.setImageBitmap(null);
                    image_cutter.setVisibility(View.VISIBLE);
                    quit_cutter.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 重新加载Bitmap到cutter_image，消除那个黑色的方框
     */
    private void initCutterImageview(){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int wid = getScreenWidth();
        int hei = wid * h / w;
        Log.i(TAG, "w, h, wid, hei = " + w + "," + h + "," + wid + "," + hei);
        cutter_image.setLayoutParams(new RelativeLayout.LayoutParams(wid, hei));
        cutter_image.setImageBitmap(bitmap);
    }


    /**
     * 选择好点后弹出对话框
     */
    private void sureCutter() {
        showDialog("确认该图片截取区域？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setTitle("查看大图");
                image_cutter_layout.setVisibility(View.GONE);
                cutter_image.setImageBitmap(null);

                image_cutter.setVisibility(View.VISIBLE);
                quit_cutter.setVisibility(View.GONE);

                Dot dotTop = new Dot(topX + "", topY + "");
                Dot dotBottom = new Dot(bottomX + "", bottomY + "");

//                int left = cutter_image.getLeft();
//                int top = cutter_image.getTop();
//                int right = cutter_image.getRight();
//                int bottom = cutter_image.getBottom();
//                Log.i(TAG, "大图的范围：left, top, right, bottom = " + left + ", " + top + ", " + right + ", " + bottom);

                RequestCorpPic(dotTop, dotBottom);

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setTitle("查看大图");
                image_cutter_layout.setVisibility(View.VISIBLE);
                cutter_image.setImageBitmap(bitmap);

//                image_cutter.setVisibility(View.VISIBLE);
//                quit_cutter.setVisibility(View.GONE);

            }
        });
    }


    /**
     * 打开确认对话框，
     */
    public void showDialog(String tip, DialogInterface.OnClickListener sureListener,
                           DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View mView = layoutInflater.inflate(R.layout.dialog_sure, null);
        TextView dialog_sure_tv = (TextView) mView.findViewById(R.id.dialog_sure_tv);
        dialog_sure_tv.setText(tip);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("确认", sureListener);
        mBuilder.setNeutralButton("取消", cancelListener);
        AlertDialog sureDialog = mBuilder.create();
        sureDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


}
