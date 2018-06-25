package com.example.gj94g.b10509034_hw4.urilite;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        DBHelper gj94DBHelper = new DBHelper(context);
        SQLiteDatabase db = gj94DBHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBContract.ClockEntry.TABLE_NAME +
                        " WHERE " + DBContract.ClockEntry._ID +
                        " IS " + String.valueOf(intent.getLongExtra(DBContract.ClockEntry._ID, -1)),
                null);
        cursor.moveToFirst();
        int indexName = cursor.getColumnIndex(DBContract.ClockEntry.COLUMN_NAME);
        int indexEnable = cursor.getColumnIndex(DBContract.ClockEntry.COLUMN_ENABLE);
        if(cursor.getString(indexEnable).compareTo("y") == 0)
            MyNotification.sendNotification(context, cursor.getString(indexName), "上課啦");

       /* Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10080);

        Log.i("Time", String.format("下次註冊:%d/%d/%d(%d), %d:%d  Mili:%d"
                , cal.get(Calendar.YEAR)
                , cal.get(Calendar.MONTH)
                , cal.get(Calendar.DATE)
                , cal.get(Calendar.DAY_OF_WEEK)
                , cal.get(Calendar.HOUR_OF_DAY)
                , cal.get(Calendar.MINUTE)
                , cal.getTimeInMillis()));
        Intent innerIntent = new Intent(context, AlarmReceiver.class);
        innerIntent.putExtra(DBContract.ClockEntry._ID, intent.getLongExtra(DBContract.ClockEntry._ID, -1));
        PendingIntent pendingInnerIntent = PendingIntent.getBroadcast(context, (int) intent.getLongExtra(DBContract.ClockEntry._ID, -1), innerIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingInnerIntent);*/
    }

      /*  if(intent.getStringExtra(DBContract.ClockEntry.COLUMN_ENABLE).compareTo("y")==0){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 10080);
            Log.i("Time", String.format("下次註冊:%d/%d/%d(%d), %d:%d  Mili:%d"
                    ,cal.get(Calendar.YEAR)
                    ,cal.get(Calendar.MONTH)
                    ,cal.get(Calendar.DATE)
                    ,cal.get(Calendar.DAY_OF_WEEK)
                    ,cal.get(Calendar.HOUR_OF_DAY)
                    ,cal.get(Calendar.MINUTE)
                    ,cal.getTimeInMillis()));

            Intent innerIntent = new Intent(context, AlarmReceiver.class);
            innerIntent.putExtra("te","te");
            PendingIntent pendingInnerIntent = PendingIntent.getBroadcast(context,1,innerIntent,PendingIntent.FLAG_ONE_SHOT);
            AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis() , pendingInnerIntent);
        }*/
}

