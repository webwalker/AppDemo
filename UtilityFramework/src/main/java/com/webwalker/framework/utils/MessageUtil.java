package com.webwalker.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.webwalker.framework.R;

import java.text.MessageFormat;

/**
 * @author xu.jian
 */
public class MessageUtil extends Activity {
    public static final int ID = 0;
    public static final int Text = 1;
    private static Context context;
    public static Handler handler = null;

    public static void setContext(Context c) {
        context = c;
    }

    public static void setHandlerContext(Context c) {
        setContext(c);
        handler = new Handler(c.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ID:
                        if (msg.obj != null)
                            showCustomToast(context, context.getString(Integer
                                            .valueOf(msg.obj.toString())),
                                    Toast.LENGTH_SHORT, Gravity.BOTTOM, 100);
                        break;
                    case Text:
                        if (msg.obj != null)
                            showCustomToast(context, msg.obj.toString(),
                                    Toast.LENGTH_SHORT, Gravity.BOTTOM, 100);
                        break;
                }
            }
        };
    }

    public static void showAysncToast(int resId) {
        handler.sendMessage(MessageUtil.getMessage(ID, resId));
    }

    public static void showAysncToast(String message) {
        if (message.equals(""))
            return;
        handler.sendMessage(MessageUtil.getMessage(Text, message));
    }

    public static void longToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    public static void longToast(Context context, int msgId) {
        showToast(context, msgId, Toast.LENGTH_LONG);
    }

    public static void shortToast(Context context, int msgId) {
        shortToast(context, context.getString(msgId));
    }

    public static void shortToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int msgId, int time) {
        showToast(context, context.getString(msgId), time);
    }

    public static void showToast(Context context, String msg, int time) {
        Toast toast = Toast.makeText(context, msg, time);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static Message getMessage(int what, Object data) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = data;
        return msg;
    }

    public static String format(String format, String... strings) {
        MessageFormat msg = new MessageFormat(format);
        return msg.format(strings);
    }

    public static void showCustomToast(Context context, int msgId, int gravity) {
        showCustomToast(context, context.getString(msgId), gravity);
    }

    public static void showCustomToast(Context context, String msg, int gravity) {
        showCustomToast(context, msg, Toast.LENGTH_SHORT, gravity, 20);
    }

    public static void showCustomToast(Context context, String msg, int time,
                                       int gravity, int offset) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastRoot = inflater.inflate(R.layout.toast, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        message.setText(msg);

        Toast toastStart = new Toast(context);
        toastStart.setGravity(gravity, 0, offset);
        toastStart.setDuration(time);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

}
