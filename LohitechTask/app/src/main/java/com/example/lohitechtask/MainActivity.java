package com.example.lohitechtask;

import static com.example.lohitechtask.Notification.channelId;
import static com.example.lohitechtask.Notification.messageExtra;
import static com.example.lohitechtask.Notification.notificationId;
import static com.example.lohitechtask.Notification.titleExtra;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;

import com.example.lohitechtask.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


// Create notification channel
        createNotificationChannel();

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotification();
            }
        });
    }

    private void scheduleNotification() {
        Intent intent = new Intent(this, Notification.class);
        String title = binding.titleET.getText().toString();
        String message = binding.messageET.getText().toString();
        intent.putExtra(titleExtra, title);
        intent.putExtra(messageExtra, message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                notificationId,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long time = getTime();
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
        showAlert(time, title, message);
    }

    private void showAlert(long time, String title, String message) {
        Date date = new Date(time);
        java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(this);
        java.text.DateFormat timeFormat = DateFormat.getTimeFormat(this);
        new AlertDialog.Builder(this)
                .setTitle("Notification Scheduled")
                .setMessage(
                        "Title: " + title +
                                "\nMessage: " + message +
                                "\nAt: "+ dateFormat.format(date) + " " + timeFormat.format(date)
                )
                .setPositiveButton("Okay", null)
                .show();
    }

    private long getTime() {
        int minute = binding.timePicker.getMinute();
        int hour = binding.timePicker.getHour();
        int day = binding.datePicker.getDayOfMonth();
        int month = binding.datePicker.getMonth();
        int year = binding.datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

    private void createNotificationChannel() {
        String name = "Notif Channel";
        String desc = "A description of the channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription(desc);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
}