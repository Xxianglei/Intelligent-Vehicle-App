package com.example.heath.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.heath.PlayAlarmActivity;


public class SnoozeReceiver extends BroadcastReceiver {
    public static final String ID_SNOOZE_FLAG = "id_snooze";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SnoozeReceiver","接收到新广播");
        Intent i = new Intent(context,PlayAlarmActivity.class);
        i.putExtra(PlayAlarmActivity.ALARM_ID, intent.getStringExtra(ID_SNOOZE_FLAG));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
