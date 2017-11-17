package iandroid.club.heightlinedemo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Guojie on 2016/2/23.
 */
public class LogUtils {

    private static Context mContext;
    private static final String TAG = LogUtils.class.getName() + "~!@#$%^&*()==========>";
    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "═════════════════";
    private static final String SINGLE_DIVIDER = "─────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    /**
     * logUtils的初始化方法
     */
    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 日志打印
     *
     * @param msg
     */
    public static void log(String msg) {
        if (mContext == null) {
            Log.v(TAG, "请调用LogUtils的初始化方法");
            return;
        }
        if (isApkDebugable(mContext)) {
            e(TAG, msg, null);
        }
    }

    /**
     * 判断当前App的运行环境
     *
     * @param context
     * @return true： debug falst:release
     */
    public static boolean isApkDebugable(Context context) {
        try {
            PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pkgInfo != null) {
                ApplicationInfo info = pkgInfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void e(String tag, String msg, Object[] params) {
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        String finalTag = getFinalTag(tag);

        Log.e(finalTag, TOP_BORDER);
        Log.e(finalTag, HORIZONTAL_DOUBLE_LINE + toString(targetStackTraceElement));
        Log.e(finalTag, MIDDLE_BORDER);
        Log.e(finalTag, HORIZONTAL_DOUBLE_LINE + String.format(msg, params));
        Log.e(finalTag, BOTTOM_BORDER);
    }

    private static String getFinalTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            return tag;
        }
        return TAG;
    }

    private static StackTraceElement getTargetStackTraceElement() {
        //find the target invoked method
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(LogUtils.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }

    public static String toString(StackTraceElement targetStackTraceElement) {
        StringBuilder buf = new StringBuilder();

        buf.append("类名：" + targetStackTraceElement.getClassName() + "\n");
        buf.append(MIDDLE_BORDER + "\n");
        buf.append(HORIZONTAL_DOUBLE_LINE + "方法名：" + targetStackTraceElement.getMethodName() + "\n");
        buf.append(MIDDLE_BORDER + "\n");
        String fName = targetStackTraceElement.getFileName();

        if (fName == null) {
            buf.append("(Unknown Source)");
        } else {
            int lineNum = targetStackTraceElement.getLineNumber();
            buf.append(HORIZONTAL_DOUBLE_LINE + "所在行号：.");
            buf.append('(');
            buf.append(fName);
            if (lineNum >= 0) {
                buf.append(':');
                buf.append(lineNum);
            }
            buf.append(')');
        }
        return buf.toString();
    }
}
