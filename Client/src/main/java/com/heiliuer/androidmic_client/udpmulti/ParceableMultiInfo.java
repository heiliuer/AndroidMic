package com.heiliuer.androidmic_client.udpmulti;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class ParceableMultiInfo implements Parcelable {

    private MulticastInfo info;

    public ParceableMultiInfo(MulticastInfo info) {
        this.info = info;
    }


    protected ParceableMultiInfo(Parcel in) {
        info = new MulticastInfo();
        info.setRecordPort(in.readInt());
        info.setRecordIp(in.readString());
        info.setMore(in.readString());
        info.setName(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(info.getRecordPort());
        dest.writeString(info.getRecordIp());
        dest.writeString(info.getMore());
        dest.writeString(info.getName());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParceableMultiInfo> CREATOR = new Creator<ParceableMultiInfo>() {
        @Override
        public ParceableMultiInfo createFromParcel(Parcel in) {
            return new ParceableMultiInfo(in);
        }

        @Override
        public ParceableMultiInfo[] newArray(int size) {
            return new ParceableMultiInfo[size];
        }
    };

    public MulticastInfo getInfo() {
        return info;
    }

    public void setInfo(MulticastInfo info) {
        this.info = info;
    }
}
