package com.socket;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class clientSocketTools {
	/**
	 * get localhost IP address
	 * 
	 * @return IP address
	 */
	@SuppressLint("LongLogTag")
    public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()&& !inetAddress.isLinkLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}	
	/**
	 * 

	 */
	public static float getScreenDensity(Context context) {
		try {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			return dm.density;
		} catch (Exception ex) {

		}
		return 1.0f;
	}
	/**
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
	/**

	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		try {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) context .getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			return dm;
		} catch (Exception ex) {

		}
		return null;
	}
    /**

     */
    public static String byte2hex(byte[] b,int len) { //一个字节的数，
        // 转成16进制字符串
        String hs = "";
        String tmp = "";
        for (int n = 0; n < len; n++) {
            //整数转成十六进制表示
            tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                hs = hs + "0" + tmp;
            } else {
                hs = hs + tmp;
            }
        }
        tmp = null;
        return hs.toUpperCase(); //转成大写
    }
    /**

     */
    public static String byte2hex(byte[] b,int start, int len) { //一个字节的数，
        // 转成16进制字符串
        String hs = "";
        String tmp = "";
        for (int n = start; n < len; n++) {
            //整数转成十六进制表示
 
            tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                hs = hs + "0" + tmp;
            } else {
                hs = hs + tmp;
            }
        }
        tmp = null;
        return hs.toUpperCase(); //转成大写
    }
    /** 

     */  
    public static int byte2int(byte[] b, int index) {    
        int temp;                                             
        temp = b[index + 0];                                  
        temp &= 0xff;                                         
        temp |= (b[index + 1] << 8);                   
        temp &= 0xffff;                                       
        temp |= (b[index + 2] << 16);                  
        temp &= 0xffffff;                                     
        temp |= (b[index + 3] << 24);                  
        return temp;                    
    }  
    /** 

     */  
    public static float byte2float(byte[] b, int index) {    
        int temp;                                             
        temp = b[index + 0];                                  
        temp &= 0xff;                                         
        temp |= ((long) b[index + 1] << 8);                   
        temp &= 0xffff;                                       
        temp |= ((long) b[index + 2] << 16);                  
        temp &= 0xffffff;                                     
        temp |= ((long) b[index + 3] << 24);                  
        return Float.intBitsToFloat(temp);                    
    }  
}
