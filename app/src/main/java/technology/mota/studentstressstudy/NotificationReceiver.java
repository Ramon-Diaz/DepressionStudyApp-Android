package technology.mota.studentstressstudy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Objects;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(context));
        SharedPreferences.Editor editor =  pref.edit();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifIntent = new Intent(context, MainActivity.class);
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pending = PendingIntent.getActivity(context, 0, notifIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("studentDep", "Student Depression Study", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Student Depression Study");
            if (nm != null) {
                nm.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "studentDep");
        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("{Time to upload data}")
                .setContentTitle("Student Depression Study")
                .setContentText("Time to upload data")
                .setContentInfo("INFO")
                .setContentIntent(pending);
        if (nm != null) {
            nm.notify(1, builder.build());
            Calendar nextNotifTime = Calendar.getInstance();
            nextNotifTime.add(Calendar.DATE, 1);
            editor.putLong("nextNotifyTime", nextNotifTime.getTimeInMillis());
            editor.apply();
        }


    }
}
