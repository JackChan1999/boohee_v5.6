package com.boohee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RefundResult implements Parcelable {
    public static final String                ACCEPTED = "accepted";
    public static final String                CANCELED = "canceled";
    public static final Creator<RefundResult> CREATOR  = new Creator<RefundResult>() {
        public RefundResult createFromParcel(Parcel source) {
            return new RefundResult(source);
        }

        public RefundResult[] newArray(int size) {
            return new RefundResult[size];
        }
    };
    public static final String                FINISHED = "finished";
    public static final String                INITIAL  = "initial";
    public static final String                PAYBACK  = "payback";
    public static final String                REFUSED  = "refused";
    public String account;
    public int    id;
    public String money_go_where;
    public String note;
    public String reason;
    public float  refund_amount;
    public String shipment_no;
    public String state;
    public String title;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeFloat(this.refund_amount);
        dest.writeString(this.money_go_where);
        dest.writeString(this.state);
        dest.writeString(this.note);
        dest.writeString(this.reason);
        dest.writeInt(this.id);
        dest.writeString(this.shipment_no);
        dest.writeString(this.account);
    }

    protected RefundResult(Parcel in) {
        this.title = in.readString();
        this.refund_amount = in.readFloat();
        this.money_go_where = in.readString();
        this.state = in.readString();
        this.note = in.readString();
        this.reason = in.readString();
        this.id = in.readInt();
        this.shipment_no = in.readString();
        this.account = in.readString();
    }
}
