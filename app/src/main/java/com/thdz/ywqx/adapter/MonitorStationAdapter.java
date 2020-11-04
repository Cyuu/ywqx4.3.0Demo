package com.thdz.ywqx.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.bean.StationBean;
import com.thdz.ywqx.bean.StnDetailStateBean;
import com.thdz.ywqx.util.Finals;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 站点列表页面listview的适配器
 */
public class MonitorStationAdapter extends BaseAdapter {

    private boolean isFromSelect;
    private Context mContext;
    private int redColor;
    private int greenColor;

    private List<StationBean> dataList = null;
    private List<StnDetailStateBean> stateList = null; // 用于设置监控点的连接状态：连接，掉线

    public boolean isFromSelect() {
        return isFromSelect;
    }

    public void setFromSelect(boolean fromSelect) {
        isFromSelect = fromSelect;
    }

    public MonitorStationAdapter(Context context) {
        this.mContext = context;
        redColor = context.getResources().getColor(R.color.red_color);
        greenColor = context.getResources().getColor(R.color.green_color);
    }

    public MonitorStationAdapter(Context context, List<StationBean> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        redColor = context.getResources().getColor(R.color.red_color);
        greenColor = context.getResources().getColor(R.color.green_color);
    }

    public void setStateList(List<StnDetailStateBean> stateList) {
        this.stateList = stateList;
    }

    public void setDataList(List<StationBean> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_station_fragment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(dataList != null && position < dataList.size()) {
            final StationBean bean = dataList.get(position);
            if (bean != null) {
                holder.station_name.setText(bean.getStnName());

                if (stateList != null && stateList.size() > 0) {

                    StnDetailStateBean stateBean = getStateBeanByStnId(bean.getStnId());

                    if (stateBean != null) {
                        // 是否掉线
                        String state = getStateByStateCode(stateBean.getStnConnState());
                        holder.station_state.setText(state);
                        if (state.contains("掉线")) {
                            holder.station_state.setTextColor(redColor);
                        } else {
                            holder.station_state.setTextColor(greenColor);
                        }

                        // 开通状态
                        String runningStaus = stateBean.getRunningStatus();
                        if (runningStaus.toLowerCase().equals("true")) {
                            holder.open_state.setText("站点已开通");
                            holder.open_state.setTextColor(greenColor);
                        } else if (runningStaus.equals("false")) {
                            holder.open_state.setText("站点尚未开通");
                            holder.open_state.setTextColor(redColor);
                        } else {
                            holder.open_state.setText(" ");
                        }
                    }
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.open_state)
        TextView open_state;// 开通状态
        @BindView(R.id.station_name)
        TextView station_name;// 名称
        @BindView(R.id.station_state)
        TextView station_state; // 站点状态

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


//    /**
//     * 用于判断是否掉线
//     */
//    private String getStateById(String id) {
//        for (StnDetailStateBean bean : stateList) {
//            if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(bean.getStnId()) && bean.getStnId().equals(id)) {
//                String state = bean.getStnConnState();
//                String value = "";
//                if (state.equals(Finals.CODE_STN_STATE_Conn_OK + "")) {
//                    value = "连接";
//                } else if (state.equals(Finals.CODE_STN_STATE_Conn_FAIL + "")) {
//                    value = "掉线";
//                } else {
//                    value = "";
//                }
//                return value;
//            }
//        }
//        return "";
//    }


    /**
     * 用于判断是否掉线
     */
    private String getStateByStateCode(String state) {
        String value = "";
        if (state.equals(Finals.CODE_STN_STATE_Conn_OK + "")) {
            value = "连接";
        } else if (state.equals(Finals.CODE_STN_STATE_Conn_FAIL + "")) {
            value = "掉线";
        }
        return value;
    }


    private StnDetailStateBean getStateBeanByStnId(String stnId){
        for (StnDetailStateBean bean : stateList) {
            if (!TextUtils.isEmpty(stnId) && !TextUtils.isEmpty(bean.getStnId()) && bean.getStnId().equals(stnId)) {
                return bean;
            }
        }
        return null;
    }


}
