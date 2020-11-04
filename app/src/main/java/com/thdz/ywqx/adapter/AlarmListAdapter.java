package com.thdz.ywqx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thdz.ywqx.bean.AlarmBean;
import com.thdz.ywqx.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 告警列表适配器
 */
public class AlarmListAdapter extends BaseAdapter {

    private List<AlarmBean> dataList = null;
    private Context mContext;

    public AlarmListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public AlarmListAdapter(Context mContext, List<AlarmBean> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    public List<AlarmBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<AlarmBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return null == dataList ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlarmListAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_alarm_simple, null);
            holder = new AlarmListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AlarmListAdapter.ViewHolder) convertView.getTag();
        }

        final AlarmBean bean = dataList.get(position);
        holder.dept_name.setText(bean.getRBName());
        holder.station_name.setText(bean.getStnName());
        holder.unit_name.setText(bean.getUnitName());
        holder.start_time.setText(bean.getAlarmOpenTm());
        holder.stop_time.setText(bean.getAlarmCancelTm());
        String alarmCode = bean.getAlarmCode();
        if (alarmCode.equals("10")) { // 巡视
            holder.alarm_type.setText("巡视");
            holder.alarm_type.setBackgroundResource(R.drawable.bg_orange_left_corner);
        } else if (alarmCode.equals("20")) { // 预警
            holder.alarm_type.setText("预警");
            holder.alarm_type.setBackgroundResource(R.drawable.bg_red_left_corner);
        } else if (alarmCode.equals("21")) { // 小物体告警
            holder.alarm_type.setText("小物体告警");
            holder.alarm_type.setBackgroundResource(R.drawable.bg_red_left_corner);
        } else if (alarmCode.equals("22")) { // 工务告警
            holder.alarm_type.setText("工务告警");
            holder.alarm_type.setBackgroundResource(R.drawable.bg_red_left_corner);
        } else if (alarmCode.equals("30")) { // 异物告警
            holder.alarm_type.setText("异物告警");
            holder.alarm_type.setBackgroundResource(R.drawable.bg_red_left_corner);
        } else { // 没有
            holder.alarm_type.setText("");
            holder.alarm_type.setBackgroundResource(R.color.transparent);
        }

        String value = bean.getRunningStatus();
        if (value.equals("true")) {
            holder.running_state.setText(mContext.getResources().getString(R.string.open_true));
            holder.running_state.setTextColor(mContext.getResources().getColor(R.color.green_deep_color));
        } else {
            holder.running_state.setText(mContext.getResources().getString(R.string.open_false));
            holder.running_state.setTextColor(mContext.getResources().getColor(R.color.red_color));
        }

        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.dept_name)
        TextView dept_name; // 局名称
        @BindView(R.id.station_name)
        TextView station_name; // 站点名称
        @BindView(R.id.unit_name)
        TextView unit_name; // 单元名称
        @BindView(R.id.running_state)
        TextView running_state; // 该站点是否开通了...
        @BindView(R.id.start_time)
        TextView start_time; // 告警开始时间
        @BindView(R.id.stop_time)
        TextView stop_time; // 取消结束时间

        @BindView(R.id.alarm_type)
        TextView alarm_type; // 告警类型名称

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
