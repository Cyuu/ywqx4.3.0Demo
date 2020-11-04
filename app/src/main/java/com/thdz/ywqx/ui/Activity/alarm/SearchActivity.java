package com.thdz.ywqx.ui.Activity.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.andrew.datechoosewheelviewdemo.DateChooseWheelViewDialog;
import com.thdz.ywqx.R;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.AlarmTypeBean;
import com.thdz.ywqx.ui.Activity.station.DeptListActivity;
import com.thdz.ywqx.ui.Activity.station.StationListActivity;
import com.thdz.ywqx.ui.Activity.station.UnitListActivity;
import com.thdz.ywqx.util.VUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * 历史告警搜索页面<br/>
 * 1 增加默认选择条件<br/>
 * 2 站点层级选择，如果不深入选择，则默认为空<br/>
 */
public class SearchActivity extends BaseActivity {

    /**
     * 历史告警搜索 选择站点，setresult用的code
     */
    private final int CODE_SELECT_DEPT = 5002;
    private final int CODE_SELECT_STN = 5003;
    private final int CODE_SELECT_UNIT = 5004;

    @BindView(R.id.start_time_tv)
    EditText start_time_tv; // 时间起始

    @BindView(R.id.end_time_tv)
    EditText end_time_tv; //时间结束

    @BindView(R.id.dept_name_tv)
    EditText dept_name_tv; // 局

    @BindView(R.id.stn_name_tv)
    EditText stn_name_tv; // 站点

    @BindView(R.id.unit_name_tv)
    EditText unit_name_tv; // 单元

    @BindView(R.id.sure_btn)
    TextView sure_btn; // 搜索按钮

    @BindView(R.id.reset_btn)
    TextView reset_btn; // 重置按钮

    @BindView(R.id.rg_search_handle)
    RadioGroup rg_search_handle; //

    @BindView(R.id.rg_search_type)
    RadioGroup rg_search_type;

//    @BindView(R.id.spinner_type)
//    Spinner spinner_type;

    @BindView(R.id.start_time_tip)
    TextView start_time_tip;

    @BindView(R.id.end_time_tip)
    TextView end_time_tip;

    private String start_time = "0"; //起始时间值
    private String end_time = "0"; //结束时间值

    private String dept_id = "-1"; // 线路值
    private String dept_name = ""; // 线路值
    private String stn_id = "-1"; // 站点值
    private String stn_name = ""; // 站点值
    private String unit_id = "-1"; // 单元值
    private String unit_name = ""; // 单元值

    private String handleStr = "0";// 处理类型，0：不限，1：未处理，2：已处理

    private List<AlarmTypeBean> typeList;
    private String typeStr = "0";// 告警类型，0：不限；20：预警，21：小物体告警，22：工务告警，30：异物告警

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setTitle("历史告警搜索");
        setBackActive();
        setRightTopGone();

        start_time_tv.setOnClickListener(this);
        end_time_tv.setOnClickListener(this);

        dept_name_tv.setOnClickListener(this);
        stn_name_tv.setOnClickListener(this);
        unit_name_tv.setOnClickListener(this);

        sure_btn.setOnClickListener(this);
        reset_btn.setOnClickListener(this);

        // 是否处理: 0：不限，1：未处理，2：已处理
        rg_search_handle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_handle_all:
                        handleStr = "0";
                        break;
                    case R.id.rb_handle_yes:
                        handleStr = "1";
                        break;
                    case R.id.rb_handle_no:
                        handleStr = "2";
                        break;
                }
            }
        });

        // 0：不限；20：预警，21：小物体告警，22：工务告警，30：异物告警
        rg_search_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_type_all: // 不限
                        typeStr = "(30,22,20,21)";
                        break;
                    case R.id.rb_type_yw: // 异物告警
                        typeStr = "(30)";
                        break;
                    case R.id.rb_type_gw: // 工务告警
                        typeStr = "(22)";
                        break;
                    case R.id.rb_type_yj: // 预警
                        typeStr = "(20)";
                        break;
                    case R.id.rb_type_xmt: // 小物体告警
                        typeStr = "(21)";
                        break;
                }
            }
        });

        // 类型
//        typeList = DataUtils.getSearchTypeList();
//        AlarmTypeAdaper4Spinner spinnerAdapter = new AlarmTypeAdaper4Spinner(context, typeList);
//        spinner_type.setAdapter(spinnerAdapter);
//        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                typeStr = typeList.get(position).getValue();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

    @Override
    public void cycleRequest(){
    }


    private Calendar parseCalendar(String timestr) {
        Calendar ret = Calendar.getInstance(Locale.CHINA);
        int year = Integer.parseInt(timestr.substring(0, 4));
        int month = Integer.parseInt(timestr.substring(4, 6));
        int day = Integer.parseInt(timestr.substring(6, 8));

        ret.set(Calendar.YEAR, year);
        ret.set(Calendar.MONTH, month - 1);
        ret.set(Calendar.DAY_OF_MONTH, day);
        return ret;
    }

    private boolean checkCondition() {
        // 判断时间
        String stime = start_time_tv.getText().toString();
        String etime = end_time_tv.getText().toString();
        if (TextUtils.isEmpty(stime)) {
            toast("请重新选择开始时间");
            start_time_tip.setVisibility(View.VISIBLE);
            return false;
        }
        if (TextUtils.isEmpty(etime)) {
            toast("请重新选择结束时间");
            end_time_tip.setVisibility(View.VISIBLE);
            return false;
        }

        stime = stime.replace(":", "").replace("-", "").replace(" ", "");
        Calendar startDate = parseCalendar(stime);
        stime = stime.substring(0, stime.length() - 3);
        etime = etime.replace(":", "").replace("-", "").replace(" ", "");
        Calendar endDate = parseCalendar(etime);
        etime = etime.substring(0, etime.length() - 3);
        Log.i(TAG, "stime = " + stime + ",etime" + etime);
        if (Double.parseDouble(stime) > Double.parseDouble(etime)) {
            toast("开始时间不能大于结束时间，请重新选择");
            end_time_tip.setVisibility(View.VISIBLE);
            return false;
        }

        startDate.add(Calendar.MONTH, 1);
        if (endDate.compareTo(startDate) > 0) {
            toast("搜索时间范围不能超过30天，请重新选择");
            end_time_tip.setVisibility(View.VISIBLE);
            return false;
        }

//        // TODO 其他三个条件是否有限制？
//        if (TextUtils.isEmpty(dept_id) && TextUtils.isEmpty(stn_id) && TextUtils.isEmpty(unit_id)) {
//            toast("请选择监控点");
//            return false;
//        }
        return true;
    }


    /**
     * 清空条件
     */
    private void reSetSelection() {
        start_time_tv.setText("");
        end_time_tv.setText("");
        dept_name_tv.setText("");
        stn_name_tv.setText("");
        unit_name_tv.setText("");

        start_time = "-1"; //起始时间值
        end_time = "-1"; //结束时间值
        dept_id = "-1"; // 线路值
        dept_name = ""; // 线路值
        stn_id = "-1"; // 站点值
        stn_name = ""; // 站点值
        unit_id = "-1"; // 单元值
        unit_name = ""; // 单元值

        rg_search_handle.check(R.id.rb_handle_all);
        handleStr = "0";
//        spinner_type.setSelection(0);
        rg_search_type.check(R.id.rb_type_all);
        typeStr = "0";

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        if (VUtils.isFastDoubleClick()) {
            return;
        } else {
            switch (v.getId()) {
                case R.id.start_time_tv: // 起始时间
                    DateChooseWheelViewDialog startDateChooseDialog =
                            new DateChooseWheelViewDialog(context,
                                    new DateChooseWheelViewDialog.DateChooseInterface() {
                                        @Override
                                        public void getDateTime(String time, boolean longTimeChecked) {
                                            start_time_tv.setText(time);
                                            start_time_tip.setVisibility(View.INVISIBLE);
                                        }
                                    });
                    startDateChooseDialog.setDateDialogTitle("开始时间");
                    startDateChooseDialog.showDateChooseDialog();

                    break;
                case R.id.end_time_tv: // 结束时间
                    DateChooseWheelViewDialog endDateChooseDialog =
                            new DateChooseWheelViewDialog(context,
                                    new DateChooseWheelViewDialog.DateChooseInterface() {
                                        @Override
                                        public void getDateTime(String time, boolean longTimeChecked) {
                                            end_time_tv.setText(time);
                                            end_time_tip.setVisibility(View.INVISIBLE);
                                        }
                                    });
                    endDateChooseDialog.setDateDialogTitle("结束时间");
                    endDateChooseDialog.showDateChooseDialog();

                    break;
                case R.id.dept_name_tv:
                    Intent intent = new Intent(context, DeptListActivity.class);
                    intent.putExtra("isFromSearch", true);
                    startActivityForResult(intent, CODE_SELECT_DEPT);
                    break;
                case R.id.stn_name_tv: // 选择站点
                    if (TextUtils.isEmpty(dept_id) || dept_id.equals("-1")) {
                        toast("请先选择局");
                        return;
                    }
                    intent = new Intent(context, StationListActivity.class);
                    intent.putExtra("isFromSearch", true);
                    intent.putExtra("RBID", dept_id);

                    startActivityForResult(intent, CODE_SELECT_STN);
                    break;
                case R.id.unit_name_tv:
                    if (TextUtils.isEmpty(stn_id) || stn_id.equals("-1")) {
                        toast("请先选择站点");
                        return;
                    }
                    intent = new Intent(context, UnitListActivity.class);
                    intent.putExtra("isFromSearch", true);
                    intent.putExtra("StnId", stn_id);
                    startActivityForResult(intent, CODE_SELECT_UNIT);
                    break;
                case R.id.sure_btn:
                    sendSearchData();

                    break;
                case R.id.reset_btn:
                    reSetSelection();
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * 参数传过去，下一页再请求数据
     */
    private void sendSearchData() {
        if (checkCondition()) {//将数据传递到结果页

            start_time = start_time_tv.getText().toString().trim();
            end_time = end_time_tv.getText().toString().trim();
            Intent intent = new Intent(context, HistoryListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("sDtBegin", start_time); // 开始时间
            bundle.putString("sDtEnd", end_time); // 结束时间
            bundle.putString("nRBId", dept_id); // 线路
            bundle.putString("nStnId", stn_id); // 站点
            bundle.putString("nUnitId", unit_id); // 单元

            bundle.putString("nHandle", handleStr); //  处理类型，0：不限，1：未处理，2：已处理
            bundle.putString("sAlarmType", typeStr); // 告警类型，0：不限；20：预警，21：小物体告警，22：工务告警，30：异物告警

            intent.putExtras(bundle);
            startActivity(intent);
        }
//        toast("进入搜索结果页");
    }


    /**
     * 子层，返回的 requestCode 必须是 Activity.RESULT_OK
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_SELECT_DEPT:
                if (data != null || data.getStringExtra("RBId") != null) {
                    dept_id = data.getStringExtra("RBId");
                    dept_name = data.getStringExtra("RBName");
                    if (!TextUtils.isEmpty(dept_name)) {
                        dept_name_tv.setText(dept_name);
                    }

                    stn_id = "-1"; // 站点值
                    stn_name = ""; // 站点值
                    stn_name_tv.setText("");
                    unit_id = "-1"; // 单元值
                    unit_name = ""; // 单元值
                    unit_name_tv.setText("");

                }
                break;
            case CODE_SELECT_STN:
                if (data != null || data.getStringExtra("StnId") != null) {
                    stn_id = data.getStringExtra("StnId");
                    stn_name = data.getStringExtra("StnName");
                    if (!TextUtils.isEmpty(stn_name)) {
                        stn_name_tv.setText(stn_name);
                    }

                    unit_id = "-1"; // 单元值
                    unit_name = ""; // 单元值
                    unit_name_tv.setText("");
                }
                break;
            case CODE_SELECT_UNIT:
                if (data != null || data.getStringExtra("UnitId") != null) {
                    unit_id = data.getStringExtra("UnitId");
                    unit_name = data.getStringExtra("UnitName");
                    if (!TextUtils.isEmpty(unit_name)) {
                        unit_name_tv.setText(unit_name);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
