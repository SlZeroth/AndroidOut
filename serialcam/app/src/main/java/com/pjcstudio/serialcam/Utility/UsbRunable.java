package com.pjcstudio.serialcam.Utility;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by juchanpark on 2015. 9. 27..
 */
public class UsbRunable implements Runnable {

    private UsbDevice mDevice;
    private UsbManager mUsbManager;
    private Context mContext;
    private Activity mActivity;

    public UsbRunable(UsbDevice device, Context context, Activity activity) {
        mDevice = device;
        mContext = context;
        mActivity = activity;
        UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
    }

    @Override
    public void run() {
        UsbDeviceConnection conn = mUsbManager.openDevice(mDevice);
        if (!conn.claimInterface(mDevice.getInterface(1), true)) {
            return;
        }
        UsbEndpoint epIN = null;
        UsbEndpoint epOUT = null;

        UsbInterface usbIf = mDevice.getInterface(1);
        for (int i = 0; i < usbIf.getEndpointCount(); i++) {
            if (usbIf.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (usbIf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN)
                    epIN = usbIf.getEndpoint(i);
                else
                    epOUT = usbIf.getEndpoint(i);
            }
        }

        for(;;) {
            final byte[] recvByte = new byte[1024];
            conn.bulkTransfer(epIN, recvByte, recvByte.length, 30000);
            Log.d("RECV", recvByte.toString());
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, recvByte.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
