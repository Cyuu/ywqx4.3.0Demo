package com.thdz.ywqx.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.thdz.ywqx.app.MyApplication;
import com.thdz.ywqx.bean.DeptBean;
import com.thdz.ywqx.bean.MonitorBean;
import com.thdz.ywqx.bean.RadarTargetBean;
import com.thdz.ywqx.bean.StationBean;
import com.thdz.ywqx.bean.UnitBean;
import com.thdz.ywqx.bean.UpdateBean;
import com.thdz.ywqx.db.DBManager;
import com.thdz.ywqx.event.RadarBundle;
import com.thdz.ywqx.ui.Activity.RadarViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataUtils {

    private static final String TAG = "DataUtils";


    /**
     * 获取get请求的url<br/>
     */
    public static String createReqUrl4Get(String ip, String uid, String cmdStr, String dataStr) {
        if (TextUtils.isEmpty(uid)) {
            uid = "0";
        }
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("CMD", Integer.parseInt(cmdStr));
            jsonObj.put("UID", uid);
            jsonObj.put("PF", 3); // 1-服务器，2-PC客户端，3-手机客户端, 4 web，

            jsonObj.put("loginstr", EncryptUtil.getEncryptString()); // 加密   loginstr

            try {
                jsonObj.put("DATA", new JSONObject(dataStr));
            } catch (JSONException e) {
                jsonObj.put("DATA", dataStr);
            }

            String result = Finals.Url_Http + ip + Finals.Url_Head + "?sCmd=" + jsonObj.toString();
//            if (ip.contains("193")) {
//                result = "http://" + ip + Finals.Url_Head + "?sCmd=" + jsonObj.toString();
//            } else if (ip.contains("225")) {
//                result = "http://" + ip + ":8088" +Finals.Url_Head + "?sCmd=" + jsonObj.toString();
//            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }


    /**
     * 获取get请求的url<br/>
     */
    public static String createReqUrl4Get4Login(String ip, String uid, String cmdStr, String dataStr, String username, String password) {
        if (TextUtils.isEmpty(uid)) {
            uid = "0";
        }
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("CMD", Integer.parseInt(cmdStr));
            jsonObj.put("UID", uid);
            jsonObj.put("PF", 3); // 1-服务器，2-PC客户端，3-手机客户端, 4 web，

            jsonObj.put("loginstr", EncryptUtil.getEncryptString4Login(username, password)); // 加密 loginstr

            try {
                jsonObj.put("DATA", new JSONObject(dataStr));
            } catch (JSONException e) {
                jsonObj.put("DATA", dataStr);
            }

            String result = Finals.Url_Http + ip + Finals.Url_Head + "?sCmd=" + jsonObj.toString();
//            if (ip.contains("193")) {
//                result = "http://" + ip + Finals.Url_Head + "?sCmd=" + jsonObj.toString();
//            } else if (ip.contains("225")) {
//                result = "http://" + ip + ":8088" +Finals.Url_Head + "?sCmd=" + jsonObj.toString();
//            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }



    /**
     * 版本名称
     */
    public static String getVersionName(Context context) {
        PackageInfo info = getPackageInfo(context.getApplicationContext());
        return info.versionName;
    }

    /**
     * 版本号
     */
    public static int getVersionCode(Context context) {
        PackageInfo info = getPackageInfo(context.getApplicationContext());
        if (info == null) {
            return 1;
        }
        return info.versionCode;
    }

    /**
     * 获取版本号
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getApplicationContext().getPackageManager();
            pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }


    /**
     * 获取版本号
     */
    public static String getVersion12e12ee(Context context) {
        try {
            PackageManager manager = context.getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 获取版本号
     */
    public static String getVersion(Context context) {
        String value = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            value = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * 所有发送控制命令的入参 - 参数
     */
    public static String CreateCommandParams(Context context, String stnNo, String unitNo, int codeId) {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();

            String userName = SpUtil.getData(Finals.SP_USERNAME);
            String ClientId = SpUtil.getData(Finals.SP_CLIENTID);

            jsonObj.put("StnNo", Integer.parseInt(stnNo)); // 监控点编号(tid)
            jsonObj.put("UnitNo", Integer.parseInt(unitNo));  // 单元编号(sid)
            jsonObj.put("CodeId", codeId);

            jsonObj.put("UserName", userName);
            jsonObj.put("ClientId", ClientId);
            jsonObj.put("CodeTm", DataUtils.getCurrentTm());
            jsonObj.put("No", DataUtils.getCMD_No());
            jsonObj.put("Platform", 3);  // 平台
            jsonObj.put("Param1", "0");
            jsonObj.put("Param2", unitNo);
            jsonObj.put("Param3", "0");
            jsonObj.put("Param4", "0");
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


//    /**
//     * 获取post请求的url
//     */
//    public static String getReqUrlHead4Post(Context context) {
//        return "http://" + SpUtil.getIP(context) + Finals.Url_Head;
//    }


    /**
     * 获取4个监控点list，不嵌套，放在该工具类的静态变量里
     */
    public static void dealMntList(String jsonStr) throws Exception {
        // 0 根据json获取MonitorBean的list,需要遍历的变量
        String dataStr = DataUtils.getReturnData(jsonStr);
        MyApplication.monitorList = com.alibaba.fastjson.JSONArray.parseArray(dataStr, MonitorBean.class);
        DBManager.getInstance().insertMonitorBeanList(MyApplication.monitorList);

    }

    public static List<DeptBean> getDeptList() {
        List<DeptBean> deptList = new ArrayList<>();
        DeptBean deptBean = null; // 1 创建 局bean
        for (MonitorBean item : MyApplication.monitorList) {
            // （1）局
            deptBean = new DeptBean();
            deptBean.setRBId(item.getRBId());
            deptBean.setRBName(item.getRBName());
            if (!deptList.contains(deptBean)) {
                deptList.add(deptBean);
            }
        }
        return deptList;
    }


    /**
     * 通过lineId，获取所有该线路下的站点list
     */
    public static List<StationBean> getStationListByDeptId(String key) {

        if (MyApplication.monitorList == null || MyApplication.monitorList.size() == 0) {
            return null;
        }
        List<StationBean> stnList = new ArrayList<>();
        StationBean stnBean = null;
        for (MonitorBean item : MyApplication.monitorList) {
            if (item.getRBId().equals(key)) {
                stnBean = new StationBean();
                stnBean.setRBId(item.getRBId());
                stnBean.setRLId(item.getRLId());
                stnBean.setRLName(item.getRLName());
                stnBean.setStnId(item.getStnId());
                stnBean.setStnName(item.getStnName());
                stnBean.setStnNo(item.getStnNo());
                stnBean.setRWIId(item.getRWIId());
                stnBean.setRSId(item.getRSId());
                if (!stnList.contains(stnBean)) {
                    stnList.add(stnBean);
                }
            }
        }

        return stnList;
    }


    /**
     * 通过stnId，获取所有该线路下的单元list
     */
    public static List<UnitBean> getUnitListByStnId(String key) {

        List<UnitBean> uList = new ArrayList<UnitBean>();
        UnitBean uBean = null;
        for (MonitorBean item : MyApplication.monitorList) {
            if (item.getStnId().equals(key)) {
                uBean = new UnitBean();
                uBean.setRBId(item.getRBId());
                uBean.setRLId(item.getRLId());
                uBean.setStnId(item.getStnId());
                uBean.setStnNo(item.getStnNo());

                uBean.setUnitId(item.getUnitId());
                uBean.setUnitName(item.getUnitName());
                uBean.setUnitNo(item.getUnitNo());

                uBean.setPcdtId(item.getPcdtId());
                uBean.setRSId(item.getRSId());
                uBean.setRWIid(item.getRWIId());
                if (!uList.contains(uBean)) {
                    uList.add(uBean);
                }
            }
        }
        return uList;
    }


    /**
     * 解析出完整的json结构
     */
    public static String getRespString(String response) {
        if (response.contains("{") && response.contains("}")) {
            int firstIndex = response.indexOf("{");
            int lastIndex = response.lastIndexOf("}");
            String value = response.substring(firstIndex, lastIndex + 1);
            return value;
        }
        return null;
    }

    public static boolean isReturnOK(String value) throws Exception {
        JSONObject jsonObj = new JSONObject(value);
        String returnCode = jsonObj.getString("Result");
        if (returnCode.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }


    public static String getReturnMsg(String value) throws Exception {
        JSONObject jsonObj = new JSONObject(value);
        String msg = jsonObj.getString("Msg");
        return msg;
    }


    /**
     * 新的信息返回：{"CMD":22,"UID":-1,"PF":1,"DATA":{"bResult":true,"sMsg":null}}
     */
    public static String getReturnResult(String value) throws Exception {
        JSONObject jsonObj = new JSONObject(value);
        String DataStr = jsonObj.getString("DATA");
        JSONObject jsonObj2 = new JSONObject(DataStr);
        String msg = jsonObj2.getString("bResult");
        return msg;
    }


    public static String getReturnUserId(String value) throws Exception {
        JSONObject jsonObj = new JSONObject(value);
        String uid = jsonObj.getString("UserId");
        return uid;
    }

    /**
     * 从json结构中解析中完整DATA结构
     */
    public static String getReturnData(String value) throws Exception {
        JSONObject jsonObj = new JSONObject(value);
        String msg = jsonObj.getString("DATA");
        return msg;
    }


//    /**
//     * 从json结构中解析中给定key的value值
//     */
//    public static String getValueByKey(String value, String keyStr) throws Exception {
//        JSONObject jsonObj = new JSONObject(value);
//        String msg = jsonObj.getString(keyStr);
//        return msg;
//    }

    /**
     * 获取更新bean
     */
    public static UpdateBean getUpdateBean() {
        UpdateBean bean = new UpdateBean();
        bean.setVersion("20");
        bean.setDesc("有更新");
        bean.setUrl(Finals.Url_test_update);
        return bean;
    }


    /**
     * 生成随即请求No，Scope:(1 - 65535)
     */
    public static String getCMD_No() {
        int temp = (int) (Math.random() * 65534 + 1);
        return temp + "";
    }


    /**
     * 生成随即请求No，Scope:(1 - 65535)
     */
    public static String getCurrentTm() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }


    /**
     * 获取MyApplication所在包的名字
     */
    public static String getPackageName() {
        return MyApplication.class.getPackage().getName();
    }


    /**
     * 获取防区数据
     *
     * @param str json字符串
     * @param r   半径
     */
    private static PointF[][] getDefendArea(String str, int r) {
        JSONArray bgArray = JSONArray.parseArray(str);
        // 数组格式
        PointF[][] pointsArray = new PointF[bgArray.size()][];

        for (int index = 0; index < bgArray.size(); index++) {
            List<PointF> pointListUp = new ArrayList<>();
            List<PointF> pointListDown = new ArrayList<>();
            JSONArray subArray = bgArray.getJSONArray(index);

            for (int i = 0; i < subArray.size(); i++) {
                com.alibaba.fastjson.JSONObject area = subArray.getJSONObject(i);
                int yUp = area.getIntValue("x") * 10;
                int yDown = area.getIntValue("y") * 10;
                if (yUp == yDown) {
                    continue;
                }
                float curX = i * 10 - r;
                pointListUp.add(new PointF(curX, yUp));
                if (pointListDown.isEmpty()) {
                    pointListDown.add(new PointF(curX, yDown));
                } else {
                    pointListDown.add(0, new PointF(curX, yDown));
                }
            }
            // 拼接
            pointListUp.addAll(pointListDown);
            PointF[] pointsItem = new PointF[pointListUp.size()];
            pointsArray[index] = pointListUp.toArray(pointsItem);

        }
        return pointsArray;
    }

    /**
     * 解析雷达数据，展示RadarView
     */
    public static void showRadarView(Activity activity, String respStr) {
        try {

            // 解析和跳转
            String respValue = DataUtils.getRespString(respStr); // root
            Log.i(TAG, "查看雷达图，返回值：" + respValue);
            String dataStr = DataUtils.getReturnData(respValue); // "data"
            org.json.JSONObject obj = new org.json.JSONObject(dataStr);
            // 雷达数据类型20006
            org.json.JSONObject radarData = obj.getJSONObject("sRadarData");
            int radarDataType = radarData.getInt("RadarDataType");
            String label = radarData.getString("sRadarLabel");
            boolean isAlarm = radarData.getString("sRadarLabelColor").toLowerCase().equals("red");

            // 雷达扫描半径
            org.json.JSONObject paramObj = new org.json.JSONObject(radarData.getString("radarParam"));
            int radius = paramObj.getInt("srad");

            // 雷达扫描点
            JSONArray scanArray = JSONArray.parseArray(radarData.getString("listScanData"));
            float[] radars = new float[scanArray.size()];
            for (int i = 0; i < scanArray.size(); i++) {
                radars[i] = scanArray.getJSONObject(i).getIntValue("data");
            }

            // 雷达告警目标
            JSONArray targetJsonArray = JSONArray.parseArray(radarData.getString("listAlarmTargetData"));
            RadarTargetBean[] targets = new RadarTargetBean[targetJsonArray.size()];
            for (int i = 0; i < targetJsonArray.size(); i++) {
                com.alibaba.fastjson.JSONObject attrObj = targetJsonArray.getJSONObject(i).getJSONObject("obj_attr");
                int objtype = attrObj.getIntValue("objtype");
                int cprad = attrObj.getIntValue("cp_rad");
                int x = attrObj.getJSONObject("p_info").getIntValue("x");
                int y = attrObj.getJSONObject("p_info").getIntValue("y");
                RadarTargetBean bean = new RadarTargetBean(x, y, radarDataType, objtype, cprad);
                targets[i] = bean;
            }

            // 背景区
            String areaStr1 = radarData.getString("listParamBack");
            PointF[][] areas1 = getDefendArea(areaStr1, radius);

            // 预警区
            String areaStr2 = radarData.getString("listParamPre");
            PointF[][] areas2 = getDefendArea(areaStr2, radius);

            // 告警区
            String areaStr3 = radarData.getString("listParamAlarm");
            PointF[][] areas3 = getDefendArea(areaStr3, radius);

            Bundle bundle = new Bundle();
            bundle.putInt("radius", radius);
            bundle.putFloatArray("radar", radars);
            bundle.putSerializable("target", targets);
            bundle.putSerializable("area1", areas1);
            bundle.putSerializable("area2", areas2);
            bundle.putSerializable("area3", areas3);
            bundle.putString("label", label);
            bundle.putBoolean("isAlarm", isAlarm);

            Intent intent = new Intent(activity, RadarViewActivity.class);
//            intent.putExtras(bundle);
            activity.startActivity(intent);
            EventBus.getDefault().postSticky(new RadarBundle(bundle));

        } catch (Exception e) {
            e.printStackTrace();
            TsUtil.toast("尚无雷达数据，请稍后再试");
        }
    }


    /**
     * 获取get请求的url<br/>
     * 方法调用： createReqUrl4Get(ip,uid,cmdStr,dataStr)<br/>
     * "DATA"节点的value是字符串类型，from 2016-08-31 <br/>
     * 193 对应80端口，225对应8088，其他待定<br/>
     * 2016.9.30 确认 端口都改为：8088<br/>
     */
    public static String createReqUrl4Get4PreviewUrl(String ip, String uid, String cmdStr, String dataStr) {
        if (TextUtils.isEmpty(uid)) {
            uid = "0";
        }
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("CMD", getIntValue(cmdStr));
            jsonObj.put("UID", getIntValue(uid));
            // 1-服务器，2-PC客户端，3-手机客户端, 4 web， String --> int
            jsonObj.put("PF", 3);
            jsonObj.put("DATA", dataStr);

            return Finals.Url_Http + ip + Finals.Url_Head + "?sCmd=" + jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static int getIntValue(String value) {
        int tmp = 0;
        try {
            if (TextUtils.isEmpty(value)) {
                value = "0";
            }
            tmp = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            tmp = 0;
        }

        return tmp;
    }


}
