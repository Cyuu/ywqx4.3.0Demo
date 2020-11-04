package com.thdz.ywqx.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZZX on 2016-07-07.
 */
public class UpdateBean implements Parcelable {

    private String version;
    private String desc;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.version);
        dest.writeString(this.desc);
        dest.writeString(this.url);
    }

    public UpdateBean() {
    }

    protected UpdateBean(Parcel in) {
        this.version = in.readString();
        this.desc = in.readString();
        this.url = in.readString();
    }

    public final Creator<UpdateBean> CREATOR = new Creator<UpdateBean>() {
        @Override
        public UpdateBean createFromParcel(Parcel source) {
            return new UpdateBean(source);
        }

        @Override
        public UpdateBean[] newArray(int size) {
            return new UpdateBean[size];
        }
    };
}
