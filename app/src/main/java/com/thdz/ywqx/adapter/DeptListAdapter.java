package com.thdz.ywqx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.bean.DeptBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 局列表页面listview的适配器
 */
public class DeptListAdapter extends BaseAdapter {

    private List<DeptBean> dataList = null;
    private Context mContext;

    public DeptListAdapter(Context context) {
        this.mContext = context;
    }

    public DeptListAdapter(Context context, List<DeptBean> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    public List<DeptBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DeptBean> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dept, null);
            holder = new ViewHolder(convertView);

//            holder.dept_name = (TextView) convertView.findViewById(R.id.dept_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DeptBean bean = dataList.get(position);

        holder.dept_name.setText(bean.getRBName());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.dept_name)
        TextView dept_name;// 局名

        //		TextView dept_desc;// 描述
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
