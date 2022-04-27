package com.jotangi.nickyen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AppUtility {
    public static AlertDialog appDialog;

    public interface OnBtnClickListener {
        void onCheck();

        void onCancel();
    }

    public static String base64(final String s) {

        if (s == null) {
            return "";
        }
        byte[] data = new byte[0];
        data = s.getBytes(StandardCharsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * Base64 to Hex
     *
     * @param arg
     * @return String
     */
    public static String toHex(String arg) {
        //Input byte array has incorrect ending byte的報錯是轉譯內可能有\n之類的空字
        String s = arg.trim();
        byte[] decoded = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decoded = java.util.Base64.getDecoder().decode(s);
        }
        return String.format("%x", new BigInteger(1, decoded));
    }

    //AES加密，帶入byte[]型態的16位英數組合文字、32位英數組合Key、需加密文字
    public static byte[] EncryptAES(byte[] iv, byte[] key, byte[] text) {
        try {
            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
            Cipher mCipher = null;
            mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec, mAlgorithmParameterSpec);

            return mCipher.doFinal(text);
        } catch (Exception ex) {
            return null;
        }
    }

    //AES加密
    public static String EncryptAES2(String str) {
        try {
            byte[] text = str.getBytes("UTF-8");
            byte[] key = "AwBHMEUCIQCi7omUvYLm0b2LobtEeRAY".getBytes("UTF-8");
            byte[] iv = "77215989@jotangi".getBytes("UTF-8");

            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
            Cipher mCipher = null;
            mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mCipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec, mAlgorithmParameterSpec);

            return Base64.encodeToString(mCipher.doFinal(text), Base64.DEFAULT);
        } catch (Exception ex) {
            return null;
        }
    }

    //AES解密，帶入byte[]型態的16位英數組合文字、32位英數組合Key、需解密文字
    public static byte[] DecryptAES(byte[] iv, byte[] key, byte[] text) {
        try {
            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
            Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mCipher.init(Cipher.DECRYPT_MODE,
                    mSecretKeySpec,
                    mAlgorithmParameterSpec);

            return mCipher.doFinal(text);
        } catch (Exception ex) {
            return null;
        }
    }

    //AES解密
    public static String DecryptAES2(String str) {
        try {
            byte[] text = Base64.decode(str.getBytes("UTF-8"), Base64.DEFAULT);
            byte[] key = "AwBHMEUCIQCi7omUvYLm0b2LobtEeRAY".getBytes("UTF-8");
            byte[] iv = "77215989@jotangi".getBytes("UTF-8");

            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
            Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mCipher.init(Cipher.DECRYPT_MODE,
                    mSecretKeySpec,
                    mAlgorithmParameterSpec);

            return new String(mCipher.doFinal(text), "UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    // region ---- 小型confirm視窗
    public static void showMyDialog(Activity activity, String message, String checkString, String cancelString, @NonNull final OnBtnClickListener listener) {
        // 注意:若是強行在Activity銷毀時將已有跳的Dialog未手動關閉視窗，將會造成 WindowLeaked
        // 須在發生該Activity的onDestroy處理dismiss，MainActivity已處理
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog, viewGroup, false);
        buildDialog(dialogView, activity, message, checkString, cancelString, listener);
    }

    public static void buildDialog(View dialogView, Activity activity, String message, String checkString, String cancelString, @NonNull final OnBtnClickListener listener) {
        TextView messageTV = dialogView.findViewById(R.id.messageTV);
        Button checkBtn = dialogView.findViewById(R.id.btnCheck);
        Button cancelBtn = dialogView.findViewById(R.id.btnCancel);

        messageTV.setText(message);

        if (checkString == null || checkString.equals("")) {
            checkBtn.setVisibility(View.GONE);
        } else {
            checkBtn.setText(checkString);
        }

        if (cancelString == null || cancelString.equals("")) {
            cancelBtn.setVisibility(View.GONE);
        } else {
            cancelBtn.setText(cancelString);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                listener.onCancel();
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                listener.onCheck();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(true);

        appDialog = builder.create();
        appDialog.show();
    }

    // create popup window
    public static PopupWindow buildPopupWindow(View popupWindowView, boolean isSoftInput) {
        PopupWindow window = new PopupWindow(popupWindowView);
        window.setFocusable(true);
        window.setOutsideTouchable(false);
        if (isSoftInput) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        window.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        return window;
    }

    // endregion
    public static void setWindowAlpha(Window window, float alpha) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;

        if (alpha == 1.0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        window.setAttributes(lp);
    }

    public static String getShoppingAreaChinese(String shoppingArea) {
        String status = "";
        switch (shoppingArea) {
            case "1":
                status = "桂花巷商圈";
                break;
            case "2":
                status = "內灣商圈";
                break;
            case "3":
                status = "北埔商圈";
                break;
            case "4":
                status = "金三角商圈";
                break;
            case "5":
                status = "角板山商圈";
                break;
        }
        return status;
    }

    public static String reserveStatus(String str) {
        String status = " ";
        switch (str) {
            case "0":
                status = "預約尚未確認";
                break;
            case "1":
                status = "預約已回覆，請前往查看";
                break;
            case "2":
                status = "預約已取消";
                break;
            case "3":
                status = "預約已完成";
                break;
        }
        return status;
    }

    /**
     * 公尺轉換公里
     * 不足1000顯示公尺超過轉換公里
     *
     * @param distance：距離
     * @return String
     */
    public static String mToKm(String distance) {
        String dis;
        int AAA = Integer.parseInt(distance);

        if (AAA < 1000) {
            dis = distance + "m";

        } else {
            double BBB = AAA / 100;
            BigDecimal bigDecimal = new BigDecimal(BBB);
            double f1 = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_DOWN).doubleValue() / 10;
            String str = String.valueOf(f1);
            dis = str + "Km";
        }

        return dis;
    }

    /**
     * 強密碼(必須包含大小寫字母和數字的組合，不能使用特殊字元，長度在8-16字元之間)
     * 中密碼(必須包含英文字母和數字的組合，不能使用特殊字元，長度在8-16字元之間)
     *
     * @param password：密码
     * @return boolean
     */
    public static boolean isPasswordRegex(String password) {
//        String pattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$"; //強密碼
//        String pattern = "[a-zA-Z0-9]{8,16}$"; //中密碼
        String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Z-a-z]{8,16}$"; //中密碼
        return Pattern.matches(pattern, password);
    }

    /**
     * 名字(中文、英文、數字但不包括下劃線等符號，長度在1-30字元之間)
     *
     * @param name：名字
     * @return boolean
     */
    public static boolean isNameRegex(String name) {
        String pattern = "^[\\u4E00-\\u9FA5A-Za-z0-9]{1,30}$"; //中密碼
        return Pattern.matches(pattern, name);
    }

    /**
     * 信箱格式()
     *
     * @param mail：信箱
     * @return boolean
     */
    public static boolean isMailRegex(String mail) {
        String pattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
//        String pattern = "^\\w{1,30}@[a-zA-Z0-9]{2,30}\\.[a-zA-Z]{2,30}(\\.[a-zA-Z]{2,30})?$";
        return Pattern.matches(pattern, mail);
    }

    /**
     * 手機MAC地址()
     *
     * @param context：context
     * @return String
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    /**
     * 手機IP地址()
     *
     * @param context：context
     * @return String
     */
    public static String getIPAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        long ip = wifiInfo.getIpAddress();
        if (ip != 0)
            return String.format("%d.%d.%d.%d",
                    (ip & 0xff),
                    (ip >> 8 & 0xff),
                    (ip >> 16 & 0xff),
                    (ip >> 24 & 0xff));
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {

        }
        return "0.0.0.0";
    }

    /**
     * Dp轉Px
     *
     * @param context
     * @param dpVal
     * @return int
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * @param s
     * @return String
     */
    public static String convertStringToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    /**
     * 判斷是否時間過期
     *
     * @param sDate
     * @param isTime 是否為含時分秒
     * @return boolean
     */
    public static boolean isExpire(String sDate, boolean isTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat;

        if (isTime) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (isTime) {
            return systemTime > date.getTime();
        } else {
            return systemTime > date.getTime() + 8640000;
        }
    }

    /**
     * 判斷是否啟用時間
     *
     * @param sDate
     * @param isTime 是否為含時分秒
     * @return boolean
     */
    public static boolean unEnable(String sDate, boolean isTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat;

        if (isTime) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return systemTime < date.getTime();
    }

    /**
     * 根據當前日期獲得是星期幾
     * time=yyyy-MM-dd
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int wek = c.get(Calendar.DAY_OF_WEEK);
        if (wek == 1) {
            Week = "星期日";
        }
        if (wek == 2) {
            Week = "星期一";
        }
        if (wek == 3) {
            Week = "星期二";
        }
        if (wek == 4) {
            Week = "星期三";
        }
        if (wek == 5) {
            Week = "星期四";
        }
        if (wek == 6) {
            Week = "星期五";
        }
        if (wek == 7) {
            Week = "星期六";
        }
        return Week;
    }

    /**
     * 檢查字串是否可以轉換為數字
     *
     * @param str 字串
     * @return
     */
    public static boolean isStr2Int(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param str  欲解析字串
     * @param str1 擷取標的物1
     * @param str2 擷取標的物2 (type3會用到)
     * @param type 1.擷取標的物前, 2.擷取標的物後, 3.擷取標的物區段內字串
     * @return 解析字串
     */
    public static String substringFunction(String str, String str1, String str2, int type) {
        String str3 = null;
        String substring = str.substring(0, str.indexOf(str1));
        if (type == 1) {
            //擷取str1之前字串(不含str1)
            str3 = substring;
        } else if (type == 2) {
            //擷取str1之後字串(不含str1)
            str3 = substring;
            str3 = str.substring(str3.length() + 1, str.length());
        } else if (type == 3) {
            //擷取str1~str2之間字串(不含str1.str2)
            str3 = str.substring(str.indexOf(str1) + 1, str.indexOf(str2));
        }
        return str3;
    }

    /**
     * @param d
     * @return
     */

    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    /**
     * 根據兩個位置的經緯度，來計算兩地的距離（單位為KM）
     * 引數為String型別
     *
     * @param lat1 使用者經度
     * @param lng1 使用者緯度
     * @param lat2 商家經度
     * @param lng2 商家緯度
     * @return
     **/
    private static final double EARTH_RADIUS = 6378137.0;

    public static String getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
        Double lat1 = Double.valueOf(Double.parseDouble(lat1Str));
        Double lng1 = Double.valueOf(Double.parseDouble(lng1Str));
        Double lat2 = Double.valueOf(Double.parseDouble(lat2Str));
        Double lng2 = Double.valueOf(Double.parseDouble(lng2Str));

//        double radLat1 = rad(lat1);
//        double radLat2 = rad(lat2);
        final double difference = rad(lat1) - rad(lat2);
        double mDifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(rad(lat1)) * Math.cos(rad(lat2))
                * Math.pow(Math.sin(mDifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;
        String distanceStr = distance + "";
        distanceStr = distanceStr.
                substring(0, distanceStr.indexOf("."));

        return distanceStr;
    }

    public static String getDistance2(Double lat1, Double lon1, Double lat2, Double lon2) {
        if ((Objects.equals(lat1, lat2)) && (Objects.equals(lon1, lon2))) {
            return "0";
        } else {
//            double theta = lon1 - lon2;
//            double dist = Math.sin(deg2rad(lat1))
//                    * Math.sin(deg2rad(lat2))
//                    + Math.cos(deg2rad(lat1))
//                    * Math.cos(deg2rad(lat2))
//                    * Math.cos(deg2rad(theta));
//            dist = Math.acos(dist);
//            dist = rad2deg(dist);
//            dist = dist * 60 * 1.1515 * 1.603 * 100;
//            String distanceStr = Math.round(dist) + "";
//            return distanceStr;

//            float[] results = new float[1];
//            Location.distanceBetween(lat1, lon1, lat2, lon2, results);
//            return  String.valueOf(Math.round(results[0]));

            final double EARTH_RADIUS = 6378.137;

            double radLat1 = rad(lat1);

            double radLat2 = rad(lat2);

            double a = radLat1 - radLat2;

            double b = rad(lon1) - rad(lon2);

            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                    * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

            s = s * EARTH_RADIUS;

            s = Math.round(s * 10000000) / 10000;

            String ss = s + "";
            return ss.substring(0, ss.indexOf("."));

        }
    }

    /**
     * 將字串每三位加逗號
     *
     * @param str
     * @return
     */
    public static String strAddComma(String str) {
        if (str == null) {
            str = "";
        }
        String addCommaStr = ""; // 需要添加逗号的字符串（整數）
        String tmpCommaStr = ""; // 小數，等逗号添加完後，最後在末尾補上
        if (str.contains(".")) {
            addCommaStr = str.substring(0, str.indexOf("."));
            tmpCommaStr = str.substring(str.indexOf("."), str.length());
        } else {
            addCommaStr = str;
        }
        // 將傳遞數字進行反轉
        String reverseStr = new StringBuilder(addCommaStr).reverse().toString();
        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
        // 將 "5,000,000," 中最後一个","去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }
        // 將數字重新反轉，並將小數拼接到末尾
        String resultStr = new StringBuilder(strTemp).reverse().toString() + tmpCommaStr;
        return resultStr;
    }

}
