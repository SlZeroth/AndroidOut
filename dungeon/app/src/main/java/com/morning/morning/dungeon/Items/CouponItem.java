package com.morning.morning.dungeon.Items;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by juchan on 2015. 12. 6..
 */
public class CouponItem implements Parcelable {

    public String saleDisplayNo;
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
    public ArrayList<String> List_couponName;
    public ArrayList<String> List_productNo;
    public ArrayList<String> List_saleProductNum;


    public static final String GROUP_ITEM = "GROUP";
    public static final String SINGLE_ITEM = "SINGLE";

    // 타입만 넣는 생성자
    public CouponItem(String _typeItem) {
        typeItem = _typeItem;
        List_couponName = new ArrayList<String>();
        List_productNo = new ArrayList<String>();
        List_saleProductNum = new ArrayList<String>();
    }

    protected CouponItem(Parcel in) {
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
        List_couponName = in.createStringArrayList();
        List_productNo = in.createStringArrayList();
        List_saleProductNum = in.createStringArrayList();
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
        dest.writeStringList(List_couponName);
        dest.writeStringList(List_productNo);
        dest.writeStringList(List_saleProductNum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CouponItem> CREATOR = new Creator<CouponItem>() {
        @Override
        public CouponItem createFromParcel(Parcel in) {
            return new CouponItem(in);
        }

        @Override
        public CouponItem[] newArray(int size) {
            return new CouponItem[size];
        }
    };
}
