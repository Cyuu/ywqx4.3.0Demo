package com.thdz.ywqx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thdz.ywqx.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 视频url列表
 */
public class VideoListAdapter extends BaseAdapter {

    private List<String> dataList = null;
    private Context mContext;

    public VideoListAdapter(Context context) {
        this.mContext = context;
    }

    public VideoListAdapter(Context context, List<String> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_video, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String url = dataList.get(position);
        try {
            String[] urls = url.split("/");
            holder.video_title.setText(urls[urls.length - 1]);
        } catch (Exception e) {
            e.printStackTrace();
            holder.video_title.setText(url);
        }

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.video_title)
        TextView video_title;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
