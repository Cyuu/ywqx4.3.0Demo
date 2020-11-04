package com.thdz.ywqx.ui.Activity.station;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.adapter.DeptListAdapter;
import com.thdz.ywqx.app.MyApplication;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.DeptBean;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.VUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;


/**
 * 局列表页面---用于告警历史搜索
 */
public class DeptListActivity extends BaseActivity {


    @BindView(R.id.listview)
    ListView listview;

    private DeptListAdapter deptAdapter;
    private List<DeptBean> deptList;

    private boolean isFromSearch; // 是否用于搜索
    private String tip = "没有可查看的局监控点";
    private String msgFail = "连接服务器失败";
    Intent mIntent;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_monitor);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("局列表");
        setBackGone();
        setRightTopGone();
        mIntent = getIntent();
        isFromSearch = mIntent.getBooleanExtra("isFromSearch", false);

        loadInit();

        deptAdapter = new DeptListAdapter(context);
        listview.setAdapter(deptAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (VUtils.isFastDoubleClick()) {
                    return;
                } else {
                    DeptBean item = deptList.get(position);
                    if (isFromSearch) {
                        mIntent.putExtra("RBId", item.getRBId());
                        mIntent.putExtra("RBName", item.getRBName());
                        setResult(Activity.RESULT_OK, mIntent);
                        finish();
                        return;
                    }

                    Intent intent = new Intent(context, StationListActivity.class);
                    intent.putExtra("RBId", item.getRBId());
                    startActivity(intent);
                }
            }
        });

        RequestData();
    }


    @Override
    public void cycleRequest(){
    }


    private void RequestData() {
        if (MyApplication.monitorList == null || MyApplication.monitorList.isEmpty()) {
            String url = DataUtils.createReqUrl4Get(
                    application.getIP(), application.getUid(), Finals.CMD_GetStnList, createParams());
            doRequestGet(url, new StringCallback() {
                @Override
                public void onError(Call call, final Exception e, int id) {
                    loadFail();
                    toast(msgFail);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(String response, int id) {
                    String value = DataUtils.getRespString(response);
                    showDeptView(value); // 处理方式3
                }
            });
        } else {
            deptList = DataUtils.getDeptList();
            deptAdapter.setDataList(deptList);
            deptAdapter.notifyDataSetChanged();
            loadOK();
        }
    }

    private String createParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("nUserId", application.getUid());
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解析监控点数据并展示
     */
    private void showDeptView(final String response) {
        try {
            Log.i(TAG, "解析出json，返回参数是：" + response);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DataUtils.dealMntList(response);

                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (MyApplication.monitorList == null || MyApplication.monitorList.isEmpty()) {
                                    loadFail();
                                    toast(tip);
                                    return;
                                }
                                deptList = DataUtils.getDeptList();
                                deptAdapter.setDataList(deptList);
                                deptAdapter.notifyDataSetChanged();
                                Log.i(TAG, "解析json为data = " + deptList);
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
        } catch (Exception e) {
            loadFail();
            e.printStackTrace();
            toast(msgFail);
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
                default:
                    break;
            }
        }
    }

}
