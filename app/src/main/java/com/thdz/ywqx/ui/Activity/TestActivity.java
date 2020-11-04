package com.thdz.ywqx.ui.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.ui.Activity.alarm.AlarmDetailActivity;
import com.thdz.ywqx.ui.Activity.alarm.SearchActivity;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.NotifyUtil;
import com.thdz.ywqx.util.SpUtil;
import com.thdz.ywqx.util.VUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 单独测试各种功能页面<br/>
 * <br/>
 */
public class TestActivity extends BaseActivity {

    @BindView(R.id.test_radar_btn)
    Button test_radar_btn;

    @BindView(R.id.test_search_btn)
    Button test_search_btn;

    @BindView(R.id.test_progress_btn)
    Button test_progress_btn;

    @BindView(R.id.test_progress2_btn)
    Button test_progress2_btn;

    @BindView(R.id.test_rtsp_btn)
    Button test_rtsp_btn;//rtsp视频

    @BindView(R.id.test_path_btn)
    Button test_path_btn;//本地mp4视频

    @BindView(R.id.mp4_net_btn)
    View mp4_net_btn;

    @BindView(R.id.test_mp3_btn)
    Button test_mp3_btn;

    @BindView(R.id.test_cmd_sure)
    Button test_cmd_sure;
    @BindView(R.id.test_cmd_liediao)
    Button test_cmd_liediao;
    @BindView(R.id.test_cmd_open_light)
    Button test_cmd_open_light;

    @BindView(R.id.test_coordinate_btn)
    Button test_coordinate_btn;// coordinate

    @BindView(R.id.test_shortcut_btn)
    Button test_shortcut_btn;// 创建快捷方式

    @BindView(R.id.test_cut_btn)
    Button test_cut_btn; // 抠图

    @BindView(R.id.test_notify_db)
    Button test_notify_db;// 打开通知

    @BindView(R.id.test_result_tv)
    TextView test_result_tv; //

    @BindView(R.id.test_conn_dj)
    Button test_conn_dj;// 连接 dejia sqlserver

    @BindView(R.id.test_ws_btn)
    Button test_ws_btn;

    private int notify_index = 1;

//    // 轮厂DB连接日志：
//    private String UserName = "sa";
//    private String Password = "Galaxy123";
//    private String ipStr = "133.124.42.186"; // 196.128.1.29
//    private String connStr = "jdbc:jtds:sqlserver://133.124.42.186:1433/TrainWheelDB";
//    private String sql1 = "SELECT * FROM pa_ModelTable";
//
//    // 异物侵限DB连接日志：
//    private String UserName2 = "sa";
//    private String Password2 = "123456";
//    private String ipStr2 = "133.124.42.54";
//    private String connStr2 = "jdbc:jtds:sqlserver://133.124.42.54:1433/RailwayForeignInvasion";
//    private String sql2 = "SELECT t.StnNo, t.StnName, t.WatchKeeper FROM dbo.E_MonitorStation t where t.StnNo <> '0'";

    private final static int CODE_SUCCESS = 1;
    private final static int CODE_ERROR = 0;

    @Override
    public void cycleRequest() {
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        // EventBus.getDefault().register(this);
        setTitle("功能测试页面");
        setBackActive();

        test_search_btn.setOnClickListener(this);
        test_progress_btn.setOnClickListener(this);
        test_progress2_btn.setOnClickListener(this);
        test_radar_btn.setOnClickListener(this);
        test_rtsp_btn.setOnClickListener(this);
        test_path_btn.setOnClickListener(this);
        mp4_net_btn.setOnClickListener(this);
        test_mp3_btn.setOnClickListener(this);
        test_cmd_sure.setOnClickListener(this);
        test_cmd_liediao.setOnClickListener(this);
        test_cmd_open_light.setOnClickListener(this);

        test_coordinate_btn.setOnClickListener(this);
        test_shortcut_btn.setOnClickListener(this);
        test_cut_btn.setOnClickListener(this);
        test_notify_db.setOnClickListener(this);
        test_conn_dj.setOnClickListener(this);
        test_ws_btn.setOnClickListener(this);

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (VUtils.isFastDoubleClick()) {
            return;
        } else {
            switch (v.getId()) {
                case R.id.test_radar_btn:
                    intent = new Intent(context, RadarViewActivity.class);
                    startActivity(intent);
                    toast("查看自定义雷达数据");
                    break;

                case R.id.test_search_btn:
                    intent = new Intent(context, SearchActivity.class);
                    startActivity(intent);

                    break;
                case R.id.test_notify_db: // 创建通知
                    notify_index += 1;
                    intent = new Intent(context, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NotifyUtil.CreateNotify(context, "标题", "打开这个通知", notify_index, intent);
                    toast("有未读通知");
                    // test_result_tv.setText("开始连接");
                    // createThread(connStr, UserName, Password);
                    break;

                case R.id.test_progress_btn:
                    showPwdDialog(sureAlarmListener);
                    break;
                case R.id.test_progress2_btn:
                    showPwdDialog(cancelAlarmListener);
                    break;

                case R.id.test_rtsp_btn://rtsp视频
//                    toast("rtsp视频");
                    intent = new Intent(context, VideoActivity.class);
                    intent.putExtra("type", "rtsp"); // http
//                    intent.putExtra("url", Finals.Url_Video_rt); // Url_rtsp_zhuhai  Url_Video_rt
                    startActivity(intent);
                    break;
                case R.id.test_path_btn: // 本地MP4
                    VUtils.gotoVideoPreview(context, "", "");
                    break;
                case R.id.mp4_net_btn:
                    VUtils.gotoVideoHistory(context, Finals.MP4_URL_DEMO);
                    break;
                case R.id.test_mp3_btn: //
                    playMp3();
                    break;
                case R.id.test_cmd_sure: // TODO 发指令
                    toast("测试");
                    break;
                case R.id.test_cmd_liediao: // TODO 发指令
                    toast("测试");
                    break;
                case R.id.test_cmd_open_light: // TODO 发指令
                    toast("测试");
//                    sendCMD(Finals.CODE_OPEN_UPTCG);
                    break;
                case R.id.test_coordinate_btn: // TODO 告警详情页
                    intent = new Intent(context, AlarmDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isFromTest", true);
                    bundle.putString("AlarmNo", "12");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.test_shortcut_btn:
                    toast("创建快捷方式");
                    break;
                case R.id.test_cut_btn:
                    intent = new Intent(context, ImageActivity.class);
                    intent.putExtra("path", "http://pic1.nipic.com/20090310/861515_230208055_2.jpg");
                    intent.putExtra("picId", "13233");
                    intent.putExtra("stnNo", "123");
                    intent.putExtra("unitNo", "1233");
                    intent.putExtra("picCamNo", "12");
                    intent.putExtra("picFpp", "34");
                    int code = Integer.parseInt("3006") - 3000;
                    intent.putExtra("CodeId", code + "");

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.test_conn_dj: // 访问数据库
                    // TODO 测试插入数据
                    toast("deprecated");
                    break;
                case R.id.test_ws_btn: // 访问webservice
                    // requestWS();
                    showProgressDialog();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 发指令
     *
     * @param code 命令对应的code
     */
    private void sendCMD(int code) {
        String dataStr = DataUtils.CreateCommandParams(context, "stnNo", "UnitNo", code);
        String url = DataUtils.createReqUrl4Get(
                application.getIP(), application.getUid(), Finals.CMD_SystemControlCmd, dataStr);

        // TODO 测试url
        url = "http://133.124.42.193/WebService.asmx/GetRecentAlarm?nUserId=2&sDTBegin=2016-08-18 08:00:00&isConfirmed=false"; // 最新告警列表
        url = "http://133.124.42.193/WebService.asmx/GetStnList?nUserId=uid"; // 监控点列表
        url = "http://133.124.42.222/WebService1.asmx/getlatestAlarmList?page=1"; // 222电脑上的地址
        url = "http://133.124.42.193/WebService.asmx/RailwayForeignInvasionWebService?sCmd={\"DATA\":{\"nUserId\":\"2\"},\"CMD\":\"3\",\"PF\":\"3\",\"UID\":\"2\"}";
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                toast("请求失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG, "webservice直接返回的response = " + response);

//                try {
//                    String value = DataUtils.getRespString(response);
//                    Log.i(TAG, "返回值：" + value);
//                    toast(DataUtils.getReturnMsg(value));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    toast("指令发送异常!");
//                }
            }
        });
    }


    MediaPlayer mediaPlayer = null;

    /**
     * 创建网络mp3
     */
    private void playMp3() {
        try {
            // 本地MP3
            mediaPlayer = MediaPlayer.create(this, R.raw.call);
            // 网络MP3
            // String url="http://192.168.1.100:8080/media/beatit.mp3";
            // mediaPlayer = new MediaPlayer();
            // mediaPlayer.setDataSource(url);

            mediaPlayer.start();
            //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
            //以便其他应用程序可以使用该资源:
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();//释放音频资源
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // 确认告警
    private DialogInterface.OnClickListener sureAlarmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imm.hideSoftInputFromWindow(pEdit.getWindowToken(), 0);// 关闭输入法
                    toast("密码：" + pEdit.getText().toString().trim());

                    String inputStr = pEdit.getText().toString(); // 密码
                    if (inputStr.isEmpty()) {
                        toast("请输入密码");
                    } else {
                        String pwdStr = SpUtil.getData("pwd");
                        if (pwdStr.equals(inputStr)) {
                            toast("发送取消告警请求");
                            // TODO 发起http请求
                        } else {
                            toast("密码错误");
                        }
                    }
                }
            });
        }
    };

    // 取消告警
    private DialogInterface.OnClickListener cancelAlarmListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imm.hideSoftInputFromWindow(pEdit.getWindowToken(), 0);// 关闭输入法
                    toast("密码：" + pEdit.getText().toString().trim());

                    String inputStr = pEdit.getText().toString();
                    if (inputStr.isEmpty()) {
                        toast("请输入密码");
                    } else {
                        String pwdStr = SpUtil.getData("pwd");
                        if (pwdStr.equals(inputStr)) {
                            toast("发送取消告警请求");
                            // TODO 发起http请求
                        } else {
                            toast("密码错误");
                        }
                    }
                }
            });
        }
    };


    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            hideProgressDialog();
            switch (msg.what) {
                case CODE_SUCCESS:
                    Log.i(TAG, "请求成功：" + msg.obj.toString());
                    test_result_tv.setText(msg.obj.toString());
                    // data = getData(result); // String --> List
                    // 展示到listview
                    break;
                case CODE_ERROR:
                    test_result_tv.setText(msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();

    }
}
