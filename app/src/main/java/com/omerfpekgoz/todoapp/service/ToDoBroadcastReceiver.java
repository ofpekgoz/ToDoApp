package com.omerfpekgoz.todoapp.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.omerfpekgoz.todoapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ToDoBroadcastReceiver extends BroadcastReceiver {

    public static final String TYPE_ONE_TIME = "OnceTimeAlarm1";
    public static final String TYPE_REPEATING = "OnceTimeAlarm";
    private static final String CHANNEL_ID = "channel_notify";
    private static final CharSequence CHANNEL_NAME = "Alarm Channel";
    private static final String EXTRA_TYPE = "type";
    private static final String EXTRA_MESSAGE = "message";

    private static final int ID_ONETIME = 100;
    private static final int ID_REPEATING = 101;


    public ToDoBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        String title = type.equalsIgnoreCase(TYPE_ONE_TIME) ? TYPE_ONE_TIME : TYPE_REPEATING;
        int notifId = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONETIME : ID_REPEATING;

        showAlarmNotification(context, type, message, notifId);


    }

    private void showAlarmNotification(Context context, String title, String description, int notfId) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_alarm)
                .setContentTitle(title)
                .setContentText(description)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setSound(alarmSound);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000});
            mBuilder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Notification notification = mBuilder.build();
        if (notificationManager != null) {
            notificationManager.notify(notfId, notification);
        }

    }

    public void setOneTimeAlarm(Context context, String title, String date, String time, String description) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ToDoBroadcastReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, description);
        intent.putExtra(EXTRA_TYPE, title);

        String dateArray[] = date.split("/");
        String timeArray[] = time.split(":");

        Log.e("Mesaj", dateArray[2].toString());
        Log.e("Mesaj", dateArray[1].toString());
        Log.e("Mesaj", dateArray[0].toString());
        Log.e("Mesaj", timeArray[1].toString());
        Log.e("Mesaj", timeArray[0].toString());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[0]));

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 00);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_ONETIME, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Log.e("Mesaj", "One time alarm");

    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ToDoBroadcastReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONETIME : ID_REPEATING;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public boolean isAlarmSet(Context context, String type) {
        Intent intent = new Intent(context, ToDoBroadcastReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONETIME : ID_REPEATING;

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    public boolean isDateInvalid(String date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

}
