package com.thdz.ywqx.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.thdz.ywqx.app.MyApplication;
import com.thdz.ywqx.bean.PushBeanBase;
import com.thdz.ywqx.db.DBManager;
import com.thdz.ywqx.event.AlarmDetailCMDBackEvent;
import com.thdz.ywqx.event.AlarmDetailRefreshEvent;
import com.thdz.ywqx.event.AlarmListEvent;
import com.thdz.ywqx.event.ClientIdEvent;
import com.thdz.ywqx.event.PicEvent;
import com.thdz.ywqx.event.UnitDetailCMDBackEvent;
import com.thdz.ywqx.event.UnitDetailRefreshEvent;
import com.thdz.ywqx.event.UpdateInfoEvent;
import com.thdz.ywqx.ui.Activity.MainActivity;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.NotifyUtil;
import com.thdz.ywqx.util.SpUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 个推 推送需要的
 * com.thdz.ywqx.service.PushBackReceiver
 */
public class PushBackReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView ==
     * null)
     */
    // public StringBuilder payloadData = new StringBuilder();
    private String TAG = "PushBackReceiver";
    // 通知id
//    private int notyId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        int mAction = bundle.getInt(PushConsts.CMD_ACTION);
//        Log.i(TAG, "个推：action = " + mAction);

        switch (mAction) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                String messageid = bundle.getString("messageid");
                String taskid = bundle.getString("taskid");

                byte[] payload = bundle.getByteArray("payload");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(
                        context, taskid, messageid, 90001);
                Log.i(TAG, "Ywqx推送返回：" + (result ? "成功" : "失败"));

                if (payload != null) {
                    try {
                        String resultStr = new String(payload);
                        Log.i(TAG, "推送data = " + resultStr);
                        String data = DataUtils.getReturnData(resultStr);
                        PushBeanBase pushBean = JSON.parseObject(data, PushBeanBase.class);

                        // 处理推送
                        handlePush(context, pushBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case PushConsts.GET_CLIENTID: // 保存打本地，登录时上传到服务器
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                String clientid = bundle.getString("clientid");
                SpUtil.save(Finals.SP_CLIENTID, clientid); // 登陆页面保存
                ClientIdEvent clientEvent = new ClientIdEvent();
                clientEvent.setClientId(clientid);
                EventBus.getDefault().postSticky(clientEvent);
                Log.i(TAG, "clientid = " + clientid);

                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid =
                 * bundle.getString("actionid"); String result =
                 * bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
                 * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
                 * actionid); Log.d("GetuiSdkDemo", "result = " + result);
                 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }


    /**
     * TODO 推送请求处理
     * 1 根据Code 分别处理，
     * 2 使用EventBus
     * 3 是否需要打开询问对话框
     * 4 StnNo, UnitNo, CodeId, CodeTm
     * 结论： </br>
     * 1 新告警：通知栏提示，点击调用MainActivity，刷新告警列表
     * 2 某条告警的图片，控制命令回复时，如果使用EventBus，如果是当前告警页，则Toast提示，展示查看按钮，刷新Status请求，刷新按钮样式。
     * 如果是其他页面，则不予以提示。
     * 3
     */
    private void handlePush(Context context, PushBeanBase pushBean) {
        if (pushBean == null) {
            return;
        }
        String codeId = pushBean.getCodeId();
        String stnNo = pushBean.getStnNo(); // 站点no -- 用来查询监控点名称
//        String unitNo = pushBean.getUnitNo(); // 单元no
        String codeTm = pushBean.getCodeTm();

        if (Finals.IS_TEST) {
            Log.i(TAG, "推送codeId = " + codeId + ", StnNo = " + stnNo);
        }

        int code = Integer.parseInt(codeId);

        switch (code) {
            case Finals.CODE_NEW_ALARM: // 来新告警
                // 1 告警页列表刷新
                AlarmListEvent listEvent = new AlarmListEvent(pushBean);
                EventBus.getDefault().postSticky(listEvent);

                // 2 弹出通知栏消息
                MyApplication.notyId++;
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String StnName = DBManager.getInstance().getStnNameByStnNo(stnNo);
                NotifyUtil.showNotification(context, "有新告警！",
                        StnName + "  " + codeTm, intent, MyApplication.notyId);
                break;
            case Finals.CODE_CANCEL_ALARM: // 新告警取消了
                MyApplication.notyId++;
                intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                StnName = DBManager.getInstance().getStnNameByStnNo(stnNo);
                NotifyUtil.showNotification(context, "告警取消！",
                        StnName + "  " + codeTm, intent, MyApplication.notyId);

                listEvent = new AlarmListEvent(pushBean);
                EventBus.getDefault().postSticky(listEvent);

                AlarmDetailRefreshEvent event = new AlarmDetailRefreshEvent(pushBean);
                EventBus.getDefault().post(event);

                break;

            // 告警图片 到达
            case Finals.CODE_PIC_YUJING: // 告警图片
            case Finals.CODE_PIC_SMALL_YUJING:
            case Finals.CODE_PIC_SMALL_GAOJING:
            case Finals.CODE_PIC_YIWU_ARRIVE:
            case Finals.CODE_IPC_RT_ARRIVE:
                // 其他图片
            case Finals.CODE_REALITME_PIC_BACK:     // 手动抓取实时图片 回复
            case Finals.CODE_CORP_PIC_BACK:         // 抠图：查看局部高清图 回复
            case Finals.CODE_RADAR_ALARM_BACK:      // 雷达图片到达
            case Finals.CODE_RADAR_RT_BACK:         // 监控单元的最新一张雷达图片--回复

                PicEvent picEvent = new PicEvent(pushBean);
                EventBus.getDefault().post(picEvent);
                break;

            // 告警详情页--发送控制命令--的回复（是否发送成功） AlarmDetailActivity 和 StationDetailFragment
            case Finals.CODE_OPEN_ALARM_DEV_BACK: // GBC打开现场报警设备回复
            case Finals.CODE_CLOSE_ALARM_DEV_BACK: // GBC关闭现场报警设备回复
            case Finals.CODE_OPEN_UPALARM_BACK: // 手动打开上行告警灯回复
            case Finals.CODE_CLOSE_UPALARM_BACK: // 手动关闭上行告警灯回复
            case Finals.CODE_OPEN_DOWNALARM_BACK: // 手动打开下行告警灯回复
            case Finals.CODE_CLOSE_DOWNALARM_BACK: // 手动关闭下行告警灯回复
            case Finals.CODE_OPEN_UPTCG_BACK: // 手动打开上行列调回复
            case Finals.CODE_CLOSE_UPTCG_BACK: // 手动关闭上行列调回复


            case Finals.CODE_OPEN_DOWNTCG_BACK: // 手动打开下行列调回复
            case Finals.CODE_CLOSE_DOWNTCG_BACK: // 手动关闭下行列调回复
            case Finals.CODE_RESET_GMDT_BACK: // 监控点--GMDT重启 --- 系统重启


            case Finals.CODE_OPEN_NORMAL_MODE_BACK: // 监控点--开启正常模式
                AlarmDetailCMDBackEvent alarmDetailCMDBackEvent = new AlarmDetailCMDBackEvent(pushBean);
                EventBus.getDefault().post(alarmDetailCMDBackEvent);

                break;
            case Finals.CODE_CLEAR_ALARM_BACK: // 远程取消告警回复
            case Finals.CODE_SURE_ALARM_BACK: // 确认告警 - 回复
                alarmDetailCMDBackEvent = new AlarmDetailCMDBackEvent(pushBean);
                EventBus.getDefault().post(alarmDetailCMDBackEvent);

                // 告警页列表刷新
                listEvent = new AlarmListEvent(pushBean);
                EventBus.getDefault().postSticky(listEvent);
                break;

            // 监控点升级包
            case Finals.CODE_GET_UPDATE_VERSION_BACK: // 升级包版本信息--回复
                UpdateInfoEvent updateEvent = new UpdateInfoEvent(pushBean);
                EventBus.getDefault().post(updateEvent);
                break;

            // 告警详情页面 、监控点详情页面状态发生变化时，刷新状态
            case Finals.CODE_STN_STATE_UP_LIGHT_OK: //
            case Finals.CODE_STN_STATE_UP_LIGHT_FAIL: //
            case Finals.CODE_STN_STATE_Down_LIGHT_OK: //
            case Finals.CODE_STN_STATE_Down_LIGHT_FAIL: //

            case Finals.CODE_STN_STATE_Alc_OK: //
            case Finals.CODE_STN_STATE_Alc_FAIL: //

            case Finals.CODE_STN_STATE_UP_LIGHT_Real_OK: //
            case Finals.CODE_STN_STATE_UP_LIGHT_Real_FAIL: //
            case Finals.CODE_STN_STATE_Down_LIGHT_Real_OK: //
            case Finals.CODE_STN_STATE_Down_LIGHT_Real_FAIL: //

            case Finals.CODE_STN_STATE_UP_Alc_OK: //
            case Finals.CODE_STN_STATE_UP_Alc_FAIL: //
            case Finals.CODE_STN_STATE_Down_Alc_OK: //
            case Finals.CODE_STN_STATE_Down_Alc_FAIL: //

            case Finals.CODE_STN_STATE_Conn_OK: //
            case Finals.CODE_STN_STATE_Conn_FAIL: //

            case Finals.CODE_STN_GmdtRunState_RT: //
            case Finals.CODE_STN_GmdtRunState_Sure_Alarm: //
            case Finals.CODE_STN_GmdtRunState_Cancel_Alarm: //

            case Finals.CODE_STN_STATE_Conn_Gmdt_Adr_Conn_OK: //
            case Finals.CODE_STN_STATE_Conn_Gmdt_Adr_Conn_FAIL: //

            case Finals.CODE_STN_GmdtRunMode_Ok: //
            case Finals.CODE_STN_GmdtRunMode_SystemTest: //
            case Finals.CODE_STN_GmdtRunMode_UnitTest: //
            case Finals.CODE_STN_GmdtRunMode_Manual: //

            case Finals.CODE_STN_Gmdt_Monitor_Ok: //
            case Finals.CODE_STN_Gmdt_Monitor_Fail: //
            case Finals.CODE_STN_Gmdt_Work_Ok: //
            case Finals.CODE_STN_Gmdt_Work_Fail: //

            case Finals.CODE_STN_Alarm_Xunshi: //
            case Finals.CODE_STN_Alarm_Yujing: //
            case Finals.CODE_STN_Alarm_SmallObject: //
            case Finals.CODE_STN_Alarm_Gongwu: //
            case Finals.CODE_STN_Alarm_Yiwu: //

            case Finals.CODE_STN_Up_Down_LightExistState_OK: // 3344;   // 上、下行告警灯存在
            case Finals.CODE_STN_Up_Down_LightExistState_FAIL: // 3345; // 上、下行告警灯丢失

            case Finals.CODE_STN_GPSState_OK:        // 3346;        // gps模块工作状态   正常
            case Finals.CODE_STN_GPSState_FAIL:      // 3347;       // gps模块工作状态   异常
            case Finals.CODE_STN_AlarmDevState_OK:    // 3348;     // 其他告警设备工作状态，3348-正常
            case Finals.CODE_STN_AlarmDevState_FAIL:  // 3349;    // 其他告警设备工作状态，3349-异常

            case Finals.CODE_STN_TrainPassing_ING:     // 3360;   // 火车通行状态   正在通过
            case Finals.CODE_STN_TrainPassing_DONE:     // 3361;  // 火车通行状态   已通过
            case Finals.CODE_STN_AlarmDevRealState_OPEN: // 3952;  // 其他告警设备运行状态  打开
            case Finals.CODE_STN_AlarmDevRealState_CLOSE: // 3953; // 其他告警设备运行状态  关闭

            case Finals.CODE_STN_SystemAlarm_OK:   // 4007;     // 系统告警状态  4007 系统正常
            case Finals.CODE_STN_SystemAlarm_FAIL: // 4006;       // 系统告警状态  4006 系统告警
                event = new AlarmDetailRefreshEvent(pushBean);
                EventBus.getDefault().post(event);
                break;

            // 单元详情页面：UnitDetailActivity -- 通知：“控制命令发送成功”

            case Finals.CODE_OPEN_ALERT_BACK: // 手动打开警号回复
            case Finals.CODE_CLOSE_ALERT_BACK: // 手动关闭警号回复
            case Finals.CODE_OPEN_GSTR_BACK: // 启用地面监测单元主控(GSTR)--回复
            case Finals.CODE_CLOSE_GSTR_BACK: // 屏蔽地面监测单元主控(GSTR)--回复
            case Finals.CODE_SET_GSTR_NORMAL_BACK: // 设置地面监测单元主控(GSTR)警号正常模式--回复
                UnitDetailCMDBackEvent unitEvent = new UnitDetailCMDBackEvent(pushBean);
                EventBus.getDefault().post(unitEvent);
                break;

            // 告警详情页面 、监控点详情页面状态发生变化时，刷新状态

            case Finals.CODE_Unit_State_ACB_OK: // 设置地面监测单元主控(GSTR)警号正常模式
            case Finals.CODE_Unit_State_ACB_FAIL: // 设置地面监测单元主控(GSTR)警号正常模式

            case Finals.CODE_Unit_State_Alert_OK: // 警号状态正常
            case Finals.CODE_Unit_State_Alert_FAIL: // 警号状态不正常

            case Finals.CODE_Unit_State_Alert_Real_OK: // 警号实际工作状态正常
            case Finals.CODE_Unit_State_Alert_Real_FAIL: // 警号实际工作状态不正常
            case Finals.CODE_Unit_State_Alf_OK: // 区域内无异物
            case Finals.CODE_Unit_State_Alf_FAIL: // 区域内有异物

            case Finals.CODE_Unit_State_2Iac_OK: // Gstr和Iac连接状态    3518：正常
            case Finals.CODE_Unit_State_2Iac_FAIL: // Gstr和Iac连接状态   3519：异常
            case Finals.CODE_Unit_State_2IO_OK:// Gstr和IO连接状态     3520：正常
            case Finals.CODE_Unit_State_2IO_FAIL: // Gstr和IO连接状态	   3521：异常

            case Finals.CODE_Unit_State_2Nspch_OK: // Gstr和内部备用通道连接状态     3524：正常
            case Finals.CODE_Unit_State_2Nspch_FAIL: // Gstr和内部备用通道连接状态	   3525：异常
            case Finals.CODE_Unit_State_2Radar_OK: // Gstr和雷达连接状态      3510：正常
            case Finals.CODE_Unit_State_2Radar_FAIL: // Gstr和雷达连接状态     3511：异常


            case Finals.CODE_Unit_State_Monitor_OK: // Gstr监控状态     3970：正常
            case Finals.CODE_Unit_State_Monitor_FAIL: // Gstr监控状态	   3971：异常
            case Finals.CODE_Unit_State_Work_OK: // Gstr工作状态   4004：正常
            case Finals.CODE_Unit_State_Work_FAIL: // Gstr工作状态   4005：异常

            case Finals.Gstr_State_Run_OK: // Gstr工作模式   3964：正常
            case Finals.Gstr_State_Run_FAIL: // Gstr工作模式
            case Finals.Gstr_State_Real_OK: // Gstr工作模式
            case Finals.Gstr_State_Real_FAIL: // Gstr工作模式

            case Finals.CODE_Unit_State_Iac_OK:     // Iac运行状态
            case Finals.CODE_Unit_State_Iac_FAIL:   // Iac运行状态
            case Finals.CODE_Unit_State_IO_OK:      // IO运行状态
            case Finals.CODE_Unit_State_IO_FAIL:    // IO运行状态

            case Finals.CODE_Unit_State_Nspch_OK:    // 内部备用通道状态
            case Finals.CODE_Unit_State_Nspch_FAIL:  // 内部备用通道状态
            case Finals.CODE_Pcdt2Gstr_OK:           // Pcdt和gstr连接状态
            case Finals.CODE_Pcdt2Gstr_FAIL:         // Pcdt和gstr连接状态

            case Finals.CODE_Unit_State_Occlude_OK:   // 雷达遮挡状态
            case Finals.CODE_Unit_State_Occlude_FAIL: // 雷达遮挡状态

            case Finals.CODE_Unit_State_Radar_OK:    // 雷达状态
            case Finals.CODE_Unit_State_Radar_FAIL:   // 雷达状态
            case Finals.CODE_Unit_State_VSB_OK:       // 内部备用通道状态
            case Finals.CODE_Unit_State_VSB_FAIL:     // 内部备用通道状态

            case Finals.CODE_Unit_GstrSysState_OK:     // GstrSysState
            case Finals.CODE_Unit_GstrSysState_FAIL:    // GstrSysState
            case Finals.CODE_Unit_Nspch485State_OK:     // Nspch485State
            case Finals.CODE_Unit_Nspch485State_FAIL:   // Nspch485State

                UnitDetailRefreshEvent event_unit_refresh = new UnitDetailRefreshEvent(pushBean);
                EventBus.getDefault().post(event_unit_refresh);
                break;

            default:
                break;
        }
    }

}