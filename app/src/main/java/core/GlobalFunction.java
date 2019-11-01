package core;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.guoguang.ggbrowser.R;

import java.io.File;

/**
 * 通用方法库
 *
 * @author yisu
 */

@SuppressLint("SimpleDateFormat")
public class GlobalFunction {

    /**
     * 获取应用版本号（安卓要求每次更新后，该版本号都要+1）
     *
     * @param context
     * @return
     */
    public static int GetVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * 获取应用版本号名称
     * 由于versionName是给用户看的，不太容易比较大小，升级检查时，就可以检查versionCode
     *
     * @param context
     * @return
     */
    public static String GetVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * 初始化每个Activity的UI，因为每个Activity里都要调用，所以独立出来一个函数，避免重复代码
     *
     * @param activity
     */
    public static void InitUI(Activity activity) {
        // 添加Activity
        GlobalFunction.AddActivity(activity);

        // 导航栏
        ActionBar actionBar = activity.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // 显示导航按钮(将应用程序图标设置为可点击的按钮，并在图标上添加向左箭头)

        // 设置全屏，主要用来去掉顶层的系统栏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * 判断文件夹是否存在，如果不存在则创建
     *
     * @param directory
     */
    public static void CheckAndMakeDir(String directory) {
        System.out.println("进入判断文件夹<" + directory + ">是否存在流程!");
        File file = new File(directory);
        if (!file.exists()) {
            System.out.println(directory + " 不存在!");
            file.mkdir();
            System.out.println(directory + " 创建成功!");
        }
    }


    /**
     * 显示加载的效果
     *
     * @param activity
     */
    @SuppressLint("InflateParams")
    public static void ShowLoading(Activity activity) {
//		View layout = activity.getLayoutInflater().inflate(R.layout.self_loading, null);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
//		toast.setView(layout);
        toast.show();
    }


    /**
     * 显示一个普通的信息提示对话框
     *
     * @param context - 上下文
     * @param message - 信息内容
     */
    public static void ShowMessage(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("系统提示")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }


    /**
     * 显示一个警示的对话框
     *
     * @param context           - 上下文
     * @param title             - 标题
     * @param message           - 信息内容
     * @param OkButtonCaption   - 确定按钮的文字
     * @param okBtnClickHandler - 确定按钮的点击事件
     */
    public static void Alert(Context context, String title, String message, String OkButtonCaption, OnClickListener okBtnClickHandler) {
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.tuichu)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(OkButtonCaption, okBtnClickHandler)
                .show();
    }


    /**
     * 显示一个提示的对话框
     *
     * @param context               - 上下文
     * @param title                 - 标题
     * @param message               - 信息内容
     * @param OkButtonCaption       - 确定按钮的文字
     * @param okBtnClickHandler     - 确定按钮的点击事件
     * @param CancelButtonCaption   - 取消按钮的文字
     * @param cancelBtnClickHandler - 取消按钮的点击事件
     */
    public static void ShowPrompt(Context context, String title, String message,
                                  String OkButtonCaption, OnClickListener okBtnClickHandler,
                                  String CancelButtonCaption, OnClickListener cancelBtnClickHandler) {
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.icon)
                .setCancelable(false)   // 不可通过返回键取消
                .setPositiveButton(OkButtonCaption, okBtnClickHandler)
                .setNegativeButton(CancelButtonCaption, cancelBtnClickHandler)
                .show();
    }


    /**
     * 显示一个带自定义View的对话框
     *
     * @param context               - 上下文
     * @param title                 - 标题
     * @param view                  - 自定义View
     * @param message               - 信息内容
     * @param OkButtonCaption       - 确定按钮的文字
     * @param okBtnClickHandler     - 确定按钮的点击事件
     * @param CancelButtonCaption   - 取消按钮的文字
     * @param cancelBtnClickHandler - 取消按钮的点击事件
     */
    public static void ShowPrompt(Context context, View view, String title, String message,
                                  String OkButtonCaption, OnClickListener okBtnClickHandler,
                                  String CancelButtonCaption, OnClickListener cancelBtnClickHandler,
	                              String OtherButtonCaption, OnClickListener OtherBtnClickHandler) {
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.icon)
                .setTitle(title)
                .setMessage(message)
                .setView(view)
                .setPositiveButton(OkButtonCaption, okBtnClickHandler)
                .setNegativeButton(CancelButtonCaption, cancelBtnClickHandler)
                .setNeutralButton(OtherButtonCaption, OtherBtnClickHandler)
                .show();
    }


    /**
     * 将当前的Activity加入全局应用实例
     *
     * @param activity
     */
    public static void AddActivity(Activity activity) {
        MyActivityManager.getInstance().addActivity(activity);
    }


    /**
     * 退出应用
     */
    public static void ExitApp(boolean isExitApp) {
        MyActivityManager.getInstance().exit(isExitApp);
    }


    /**
     * 重启系统
     */
    public static void RebootSystem() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot"});  // 重启
            proc.waitFor();
        } catch (Exception ex) {
//			GlobalConfig.LogLocal.info("[Reboot] 发生异常：" + ex.toString());    // 记录日志
        } finally {
            // Toast.makeText(this, "重启系统", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 关闭系统
     */
    public static void ShutdownSystem() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot -p"});  // 关机（-p参数）
            proc.waitFor();
        } catch (Exception ex) {
//			GlobalConfig.LogLocal.info("[Shutdown] 发生异常：" + ex.toString());    // 记录日志
        } finally {
            // Toast.makeText(this, "关闭系统", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 组建返回报文(GlobalConfig.PktLength位报文长度，不足长度位则前补0)
     *
     * @param sdata
     * @return
     */
    public static String WrapResponsePkt(String sdata) {
        int len;
        try {
            len = sdata.getBytes("GBK").length;  // 有效数据长度(针对Linux处理的话，要gbk)
        } catch (Exception ex) {
            len = sdata.getBytes().length;  // 有效数据长度
        }

//        return String.format("%0" + GlobalConfig.PktLength + "d", len) + sdata;
        return sdata;
    }


    /**
     * 处理交易发生异常的通用函数
     * @param ClassName
     * @param ex
     * @return
     */
//    public static String Deal_TransException(String ClassName, Exception ex)
//    {
//    	GlobalConfig.LogLocal.error(ClassName + " -WorkFlow()函数发生异常:" + ex.toString());
//        return GetErrorInfo("01") + ex.getMessage() + "|";  // 返回错误信息：01|对不起，交易失败
//    }


    /**
     * 返回错误码（从错误码表中查询）
     * @param error_code
     * @return
     */
//    public static String GetErrorInfo(String error_code)
//    {
//        return error_code + "|" + GlobalConfig.ErrorCodeFile.getProfileString("Error_Code", error_code, "");
//    }

}
