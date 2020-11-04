package com.thdz.ywqx.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.bean.UnitBean;
import com.thdz.ywqx.bean.UnitDetailStatusBean;
import com.thdz.ywqx.util.Finals;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 单元列表页面listview的适配器
 */
public class MonitorUnitAdapter extends BaseAdapter {

    private Context mContext;

    private List<UnitBean> dataList = null;
    private List<UnitDetailStatusBean> stateList = null; // 用于设置监控单元的连接状态：连接，掉线

    private int redColor;
    private int greenColor;

    public MonitorUnitAdapter(Context context) {
        this.mContext = context;
        redColor = context.getResources().getColor(R.color.red_color);
        greenColor = context.getResources().getColor(R.color.green_color);
    }

    public MonitorUnitAdapter(Context context, List<UnitBean> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        redColor = context.getResources().getColor(R.color.red_color);
        greenColor = context.getResources().getColor(R.color.green_color);
    }

    public void setStateList(List<UnitDetailStatusBean> stateList) {
        this.stateList = stateList;
    }

    public List<UnitBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<UnitBean> dataList) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_unit_fragment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final UnitBean bean = dataList.get(position);
        holder.unit_name.setText(bean.getUnitName());

        if (stateList != null) {
            String state = getStateById(bean.getUnitId());
            Log.i("MonitorUnitAdapter", "该单元状态 = " + state);
            holder.station_state.setText(state);
            if (state.contains("掉线")) {
                holder.station_state.setTextColor(redColor);
            } else {
                holder.station_state.setTextColor(greenColor);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.unit_name)
        TextView unit_name;// 单元名
        @BindView(R.id.station_state)
        TextView station_state; // 站点状态

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 用于判断是否掉线
     */
    private String getStateById(String id) {
        for (UnitDetailStatusBean bean : stateList) {
            if (!TextUtils.isEmpty(id)
                    && !TextUtils.isEmpty(bean.getStnId()) && bean.getUnitId().equals(id)) {
                String state = bean.getPcdt2GstrConncetState();
                String value = "";
                if (state.equals(Finals.CODE_Pcdt2Gstr_OK + "")) {
                    value = "连接";
                } else if (state.equals(Finals.CODE_Pcdt2Gstr_FAIL + "")) {
                    value = "掉线";
                } else {
                    value = "掉线";
                }
                return value;
            }
        }
        return "";
    }

}
