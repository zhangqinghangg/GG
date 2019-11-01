package utlis;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.guoguang.ggbrowser.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class AppVersion {
    /**
     * 描述：获取应用程序名称
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述：获取应用程序版本名称信息
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 描述：[获取应用程序版本名称信息
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 描述：获取应用程序版本名称信息
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述：获取图标 bitmap
     *
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * 描述：将获取的版本信息整理为json格式，需要调用
     *
     * @param context 上下文，MainActivity.this
     * @return json格式的版本信息，供js调用
     */
    public String getAppVersion(Context context) {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("appname", getAppName(context));
            jsonParam.put("PackageName", getPackageName(context));
            jsonParam.put("VersionName", getVersionName(context));
            jsonParam.put("VersionCode", getVersionCode(context));

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(jsonParam);
    }

    /**
     * 描述：将获取的手机硬件信息整理为json格式，需要调用
     *
     * @return json格式的手机硬件信息，供js调用
     */
    public String getDeviceInfo() {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("主板", Build.BOARD);
            jsonParam.put("系统启动程序版本号", Build.BOOTLOADER);
            jsonParam.put("系统定制商", Build.BRAND);
            jsonParam.put("cpu指令集", Build.CPU_ABI);
            jsonParam.put("cpu指令集2", Build.CPU_ABI2);
            jsonParam.put("设置参数", Build.DEVICE);
            jsonParam.put("显示屏参数", Build.DISPLAY);
            jsonParam.put("无线电固件版本", Build.getRadioVersion());
            jsonParam.put("硬件识别码", Build.FINGERPRINT);
            jsonParam.put("硬件名称", Build.HARDWARE);
            jsonParam.put("HOST", Build.HOST);
            jsonParam.put("修订版本列表", Build.ID);
            jsonParam.put("硬件制造商", Build.MANUFACTURER);
            jsonParam.put("版本", Build.MODEL);
            jsonParam.put("硬件序列号", Build.SERIAL);
            jsonParam.put("手机制造商", Build.PRODUCT);
            jsonParam.put("描述Build的标签", Build.TAGS);
            jsonParam.put("TIME", Build.TIME);
            jsonParam.put("builder类型", Build.TYPE);
            jsonParam.put("USER", Build.USER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(jsonParam);
    }

}
