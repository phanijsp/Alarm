package com.example.alarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton addalarm;
    TimePicker timePicker;
    Button timepickerdone;
    Button timepickercancel;
    MediaPlayer mp;
    ConstraintLayout alarmconstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        addalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settimeselectorvisible();
            }
        });
        timepickercancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settimeselectorinvisible();
            }
        });

        timepickerdone.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                settimeselectorinvisible();
                addalarm(timePicker.getHour()+":"+timePicker.getMinute());
            }
        });
        starthandler();

    }

    public void init() {

        addalarm = findViewById(R.id.floatingActionButton);
        timePicker = findViewById(R.id.timepicker);
        timepickerdone = findViewById(R.id.button2);
        timepickercancel = findViewById(R.id.timepickercancel);
        mp=MediaPlayer.create(getApplicationContext(), R.raw.reflection);
        alarmconstraintLayout   =   findViewById(R.id.cl);

    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public void addalarm(String time) {
        Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();
        String myDate = time;
        String s = time;
        String[] data = s.split(":");

        int hours  = Integer.parseInt(data[0]);
        int minutes = Integer.parseInt(data[1]);
        int seconds = 00;

        int timex = seconds + 60 * minutes + 3600 * hours;

        Toast.makeText(this,"time in millis = " + TimeUnit.MILLISECONDS.convert(timex, TimeUnit.MILLISECONDS),Toast.LENGTH_SHORT).show();
       startAlert(timex-getcurrentms());
    }
    public void starthandler(){
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                String val = isalarmawake.getVal();
                if(val.equals("Awake")){
                    Toast.makeText(getApplicationContext(),"Awake Awake Awake ! ",Toast.LENGTH_SHORT).show();
                    isalarmawake.setVal("");
                    startmusic();
                    alarmconstraintLayout.setVisibility(View.VISIBLE);
                    delayedchangevisibilty(alarmconstraintLayout);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnableCode);
    }
    public void delayedchangevisibilty(final View view){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
                mp.stop();
            }
        };
        handler.postDelayed(runnable,20000);
    }
    public void startmusic(){
        if(mp.isPlaying()){

        }else{
            mp.start();
        }
    }

    public int getcurrentms(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(cal.getTime());
        String data[]=time.split(":");
        int hours  = Integer.parseInt(data[0]);
        int minutes = Integer.parseInt(data[1]);
        int seconds = Integer.parseInt(data[2]);
        int timex = seconds + 60 * minutes + 3600 * hours;
        return timex;
    }
    public void settimeselectorvisible() {
        timePicker.setVisibility(View.VISIBLE);
        timepickerdone.setVisibility(View.VISIBLE);
        timepickercancel.setVisibility(View.VISIBLE);
    }

    public void settimeselectorinvisible() {
        timePicker.setVisibility(View.INVISIBLE);
        timepickerdone.setVisibility(View.INVISIBLE);
        timepickercancel.setVisibility(View.INVISIBLE);
    }

    public void startAlert(int triggeratms){

        int i = triggeratms;
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Toast.makeText(this, "Alarm set in\n" +triggeratms +"\n"+System.currentTimeMillis()+"seconds",Toast.LENGTH_LONG).show();
    }
}
