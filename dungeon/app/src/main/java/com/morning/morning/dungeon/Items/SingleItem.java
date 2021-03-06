package com.morning.morning.dungeon.Items;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by juchan on 2015. 12. 6..
 */
public class SingleItem implements Parcelable {

    public String typeItem; // 아이템 타입
    public String thumbnailLink; // 메인이미지 링크
    public String qty; // 세일율
    public String groupProductNo;
    public String couponName; // 쿠폰이름
    public String couponSubName; // 부쿠폰 이름
    public String couponPrice; // 가격
    public String couponSalePrice; // 세일가격
    public String sliderObject; // 슬라이더 이미지 오브젝트
    public String contentObject; // 본문 이미지 오브젝트
    public String Qa; // 본문 이미지 오브젝트

    public SingleItem() {

    }

    protected SingleItem(Parcel in) {
        typeItem = in.readString();
        thumbnailLink = in.readString();
        qty = in.readString();
        groupProductNo = in.readString();
        couponName = in.readString();
        couponSubName = in.readString();
        couponPrice = in.readString();
        couponSalePrice = in.readString();
        sliderObject = in.readString();
        contentObject = in.readString();
        Qa = in.readString();
    }

    public static final Creator<SingleItem> CREATOR = new Creator<SingleItem>() {
        @Override
        public SingleItem createFromParcel(Parcel in) {
            return new SingleItem(in);
        }

        @Override
        public SingleItem[] newArray(int size) {
            return new SingleItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(typeItem);
        dest.writeString(thumbnailLink);
        dest.writeString(qty);
        dest.writeString(groupProductNo);
        dest.writeString(couponName);
        dest.writeString(couponSubName);
        dest.writeString(couponPrice);
        dest.writeString(couponSalePrice);
        dest.writeString(sliderObject);
        dest.writeString(contentObject);
        dest.writeString(Qa);
    }
}
