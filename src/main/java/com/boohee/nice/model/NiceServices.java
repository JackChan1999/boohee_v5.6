package com.boohee.nice.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import java.util.List;

public class NiceServices {
    public List<ServicesBean> services;

    public static class ServicesBean implements Parcelable {
        public static final Creator<ServicesBean> CREATOR = new Creator<ServicesBean>() {
            public ServicesBean createFromParcel(Parcel source) {
                return new ServicesBean(source);
            }

            public ServicesBean[] newArray(int size) {
                return new ServicesBean[size];
            }
        };
        public double base_price;
        public int    month;
        public String sku;
        public String state;
        public String title;
        public double usd_price;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.sku);
            dest.writeDouble(this.base_price);
            dest.writeDouble(this.usd_price);
            dest.writeString(this.title);
            dest.writeInt(this.month);
            dest.writeString(this.state);
        }

        protected ServicesBean(Parcel in) {
            this.sku = in.readString();
            this.base_price = in.readDouble();
            this.usd_price = in.readDouble();
            this.title = in.readString();
            this.month = in.readInt();
            this.state = in.readString();
        }
    }
}
