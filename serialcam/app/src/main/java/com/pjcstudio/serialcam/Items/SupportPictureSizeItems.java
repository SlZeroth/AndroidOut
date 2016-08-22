package com.pjcstudio.serialcam.Items;

/**
 * Created by juchanpark on 2015. 9. 16..
 */
public class SupportPictureSizeItems {

    public String strResolution;
    public int width, height;

    public SupportPictureSizeItems(String resolution, int width, int height) {
        strResolution = resolution;
        this.width = width;
        this.height = height;
    }
}
