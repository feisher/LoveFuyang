package com.yusong.configlibrary.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by wenxin
 */
public class LogUtils {
    private static String TAG = LogUtils.class.getSimpleName();
    public static Boolean isOutLog = true;

    public static void d(String log) {
        log(log);
    }

    public static void d(String... log) {

    }

    public static void v(String log) {
        log(log);
    }

    public static void v(String... log) {

    }

    public static void e(String log) {
        log(log);
    }

    public static void e(String... log) {

    }

    public static void i(String log) {
        log(log);
    }

    public static void i(String... log) {

    }

    /**
     * 自定义日志输出方法-
     *
     * @param log
     */
    public static void log(String log) {
        mylog(TAG, log, null);
    }

    /**
     * 自定义日志输出方法
     *
     * @param t
     * @param log
     */
    public static void log(String t, String log) {
        mylog(t, log, null);
    }

    /**
     * 不限制参数自定义日志输出方法
     *
     * @param t
     * @param msgFormat
     * @param args
     */
    public static void log(String t, String msgFormat, Object... args) {
        mylog(t, String.format(msgFormat, args), null);
    }

    /**
     * 自定义日志输出方法
     */
    private static void mylog(String tag, String log, Throwable t) {
        if (isOutLog) {
            if (t != null) {
                Log.e(tag, log, t);
            } else {
                Log.e(tag, log);
            }
        }

    }

    /**
     * 异常输出方法
     *
     * @param obj1
     * @param t
     */
    public static void log(Object obj1, Throwable t) {
        mylog("异常", obj1.toString(), t);
    }

    /**
     * 异常输出(整合的时候需要删除)
     *
     * @param e
     */
    public static void logException(Exception e) {
        log(TAG + "-异常信息-Exception", e.getMessage());
    }

    private static void log(final int pType, final Throwable t,
                            final Object s1, final Object... args) {
        if (pType == Log.ERROR || isOutLog) {
            final StackTraceElement stackTraceElement = Thread.currentThread()
                    .getStackTrace()[4];

            final String fullClassName = stackTraceElement.getClassName();
            final String className = fullClassName.substring(fullClassName
                    .lastIndexOf(".") + 1);
            final int lineNumber = stackTraceElement.getLineNumber();
            final String method = stackTraceElement.getMethodName();

            final String tag = className + ":" + lineNumber;

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(method);
            stringBuilder.append("(): ");

            if (s1 != null) {
                final String message = (args == null) ? s1.toString() : String
                        .format((String) s1, args);
                stringBuilder.append(message);
            }

            switch (pType) {
                case Log.VERBOSE:
                    if (t != null) {
                        Log.v(tag, stringBuilder.toString(), t);
                    } else {
                        Log.v(tag, stringBuilder.toString());
                    }
                    break;

                case Log.DEBUG:
                    if (t != null) {
                        Log.d(tag, stringBuilder.toString(), t);
                    } else {
                        Log.d(tag, stringBuilder.toString());
                    }
                    break;

                case Log.INFO:
                    if (t != null) {
                        Log.i(tag, stringBuilder.toString(), t);
                    } else {
                        Log.i(tag, stringBuilder.toString());
                    }
                    break;

                case Log.WARN:
                    if (t != null) {
                        Log.w(tag, stringBuilder.toString(), t);
                    } else {
                        Log.w(tag, stringBuilder.toString());
                    }
                    break;

                case Log.ERROR:
                    if (t != null) {
                        Log.e(tag, stringBuilder.toString(), t);
                    } else {
                        Log.e(tag, stringBuilder.toString());
                    }
                    break;
            }
        }
    }

    /**
     * Customize the log tag for your application, so that other apps
     * using Volley don't mix their logs with yours.
     * <br />
     * Enable the log property for your tag before starting your app:
     * <br />
     * {@code adb shell setprop log.tag.&lt;tag&gt;}
     */
    public static void setTag(String tag) {
        d("Changing log tag to %s", tag);
        TAG = tag;
    }

    public static void v(String format, Object... args) {
        if (isOutLog) {
            Log.v(TAG, buildMessage(format, args));
        }
    }

    public static void d(String format, Object... args) {
        Log.d(TAG, buildMessage(format, args));
    }

    public static void e(String format, Object... args) {
        Log.e(TAG, buildMessage(format, args));
    }

    public static void e(Throwable tr, String format, Object... args) {
        Log.e(TAG, buildMessage(format, args), tr);
    }

    public static void wtf(String format, Object... args) {
        Log.wtf(TAG, buildMessage(format, args));
    }

    public static void wtf(Throwable tr, String format, Object... args) {
        Log.wtf(TAG, buildMessage(format, args), tr);
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside of VolleyLog.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogUtils.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s",
                Thread.currentThread().getId(), caller, msg);
    }


    /**
     * json 格式化
     *
     * @param bodyString
     * @return
     */
    public static String jsonFormat(String bodyString) {
        String message;
        try {
            if (bodyString.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(bodyString);
                message = jsonObject.toString(4);
            } else if (bodyString.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(bodyString);
                message = jsonArray.toString(4);
            } else {
                message = bodyString;
            }
        } catch (JSONException e) {
            message = bodyString;
        }
        return message;
    }

}
