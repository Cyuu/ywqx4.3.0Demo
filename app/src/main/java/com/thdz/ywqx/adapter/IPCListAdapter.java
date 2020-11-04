package com.thdz.ywqx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.bean.IPCBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 站点列表页面listview的适配器<br/>
 * TODO 怎么去判断是否有图片/视频能力
 */
public class IPCListAdapter extends BaseAdapter {

    private List<IPCBean> dataList = null;
    private Context mContext;

    public IPCListAdapter(Context context) {
        this.mContext = context;
    }

    public IPCListAdapter(Context context, List<IPCBean> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    public List<IPCBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<IPCBean> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ipc, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IPCBean bean = dataList.get(position);

        holder.ipc_sid_tv.setText("IPC设备编号：" + bean.getIpcRSId());
        holder.nvr_sid_tv.setText("NVR设备编号：" + bean.getNvrSid());
        holder.nvr_channel_tv.setText("NVR通道：" + bean.getNvrChannel());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ipc_sid_tv)
        TextView ipc_sid_tv; // 名称
        @BindView(R.id.nvr_sid_tv)
        TextView nvr_sid_tv;// 图片
        @BindView(R.id.nvr_channel_tv)
        TextView nvr_channel_tv;// 视频

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
