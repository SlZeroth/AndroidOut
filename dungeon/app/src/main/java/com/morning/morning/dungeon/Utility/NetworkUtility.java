package com.morning.morning.dungeon.Utility;

import java.util.List;

import retrofit.client.Header;

/**
 * Created by juchan on 2015. 10. 23..
 */
public class NetworkUtility {

    public static String getHeaderData(List<Header> headerList, String findStr) {
        String retn = null;
        for(Header header : headerList) {
            if(header.getName().equals(findStr)) {
                retn = header.getValue();
                break;
            }
        }
        return retn;
    }
}
