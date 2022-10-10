package technology.mota.mymooddiarymexico;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            Intent alarmIntent = new Intent(context, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,alarmIntent,0);
            AlarmManager manager =  (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(context));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, pref.getInt("dailyNotificationHour", 19));
            calendar.set(Calendar.MINUTE, pref.getInt("dailyNotificationMin", 0));
            calendar.set(Calendar.SECOND, 1);
            Calendar newC = new GregorianCalendar();
            newC.setTimeInMillis(pref.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis()));
            if (calendar.after(newC)) {
                calendar.add(Calendar.HOUR, 1);
            }
            if  (manager != null) {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }


        }
    }
}
