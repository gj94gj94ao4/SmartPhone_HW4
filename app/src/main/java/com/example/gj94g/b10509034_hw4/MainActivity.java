package com.example.gj94g.b10509034_hw4;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gj94g.b10509034_hw4.urilite.AlarmReceiver;
import com.example.gj94g.b10509034_hw4.urilite.DBContract;
import com.example.gj94g.b10509034_hw4.urilite.DBHelper;
import com.example.gj94g.b10509034_hw4.urilite.MyNotification;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static FloatingActionButton myFab;
    public static RecyclerView mRecyclerView;
    public static RecyclerViewAdapter mAdapter;
    public static DBHelper gj94DBHelper;

    public static final int TASK_LOADER_ID = 564;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gj94DBHelper = new DBHelper(MainActivity.this);

        //RecyclerView
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter(this, getResources().getStringArray(R.array.week), gj94DBHelper, this);
        mRecyclerView.setAdapter(mAdapter);


        // NOTE: FAB setting
        myFab = findViewById(R.id.myfab);
        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.alerttime, null);
                Spinner sp = view.findViewById(R.id.week_spinner);
                sp.setAdapter(new ArrayAdapter<CharSequence>(MainActivity.this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.week)));

                new AlertDialog.Builder(MainActivity.this)
                        .setView(view)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = view.findViewById(R.id.class_name);
                                TimePicker timePicker = view.findViewById(R.id.class_time);
                                Spinner sp = view.findViewById(R.id.week_spinner);
                                int sp_week = sp.getSelectedItemPosition()+1;

                                // NOTE: Database insert
                                SQLiteDatabase db = gj94DBHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(DBContract.ClockEntry.COLUMN_NAME,editText.getText().toString());
                                values.put(DBContract.ClockEntry.COLUMN_ENABLE,"y");
                                values.put(DBContract.ClockEntry.COLUMN_TIME,String.format("%1$02d:%2$02d",timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
                                values.put(DBContract.ClockEntry.COLUMN_WEEK, String.valueOf(sp_week));

                                long id = -1;
                                try {
                                    id = db.insert(DBContract.ClockEntry.TABLE_NAME, null, values);
                                }catch (SQLException r){
                                    Log.d("MYDEBUG", r.toString());
                                }

                                if(id > -1) {
                                    Toast.makeText(MainActivity.this,
                                            "Success insert in " + id + ": "+ editText.getText().toString() + timePicker.getCurrentHour() + timePicker.getCurrentMinute() + " " + sp_week,
                                            Toast.LENGTH_SHORT).show();

                                    // NOTE: AlarmManager init
                                    Calendar cal = Calendar.getInstance();
                                    Calendar nextCal = Calendar.getInstance();

                                    Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                                    intent.putExtra(DBContract.ClockEntry._ID, id);

                                    nextCal.set(Calendar.DAY_OF_WEEK,sp_week);
                                    nextCal.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                                    nextCal.set(Calendar.MINUTE,timePicker.getMinute() - 5);
                                    nextCal.add(Calendar.MILLISECOND,5000);

                                    Log.i("Time", String.format("現在:%d/%d/%d(%s), %d:%d  Mili:%d"
                                            ,cal.get(Calendar.YEAR)
                                            ,cal.get(Calendar.MONTH)
                                            ,cal.get(Calendar.DATE)
                                            ,whichDayOfWeek(cal.get(Calendar.DAY_OF_WEEK))
                                            ,cal.get(Calendar.HOUR_OF_DAY)
                                            ,cal.get(Calendar.MINUTE)
                                            ,cal.getTimeInMillis()));
                                    Log.i("Time2", String.format("下個:%d/%d/%d(%s), %d:%d  Mili:%d"
                                            ,nextCal.get(Calendar.YEAR)
                                            ,nextCal.get(Calendar.MONTH)
                                            ,nextCal.get(Calendar.DATE)
                                            ,whichDayOfWeek(nextCal.get(Calendar.DAY_OF_WEEK))
                                            ,nextCal.get(Calendar.HOUR_OF_DAY)
                                            ,nextCal.get(Calendar.MINUTE)
                                            ,nextCal.getTimeInMillis()));
                                    if(nextCal.getTimeInMillis()<cal.getTimeInMillis())
                                        nextCal.add(Calendar.MILLISECOND,604800000);

                                    Log.i("Time3", String.format("最新的個:%d/%d/%d(%s), %d:%d  Mili:%d"
                                            ,nextCal.get(Calendar.YEAR)
                                            ,nextCal.get(Calendar.MONTH)
                                            ,nextCal.get(Calendar.DATE)
                                            ,whichDayOfWeek(nextCal.get(Calendar.DAY_OF_WEEK))
                                            ,nextCal.get(Calendar.HOUR_OF_DAY)
                                            ,nextCal.get(Calendar.MINUTE)
                                            ,nextCal.getTimeInMillis()));

                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,(int)id,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                                    AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                                    am.setRepeating(AlarmManager.RTC_WAKEUP,nextCal.getTimeInMillis(), 604800000 , pendingIntent);
                                    Log.d("INTERVAL",  String.valueOf(nextCal.getTimeInMillis() - cal.getTimeInMillis()));

                                    getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,MainActivity.this);
                                } else {
                                    Toast.makeText(MainActivity.this, "insert failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
            }
            private String whichDayOfWeek(int day){
                switch (day){
                    case Calendar.SUNDAY:
                        return "日";
                    case Calendar.MONDAY:
                        return "一";
                    case Calendar.TUESDAY:
                        return "二";
                    case Calendar.WEDNESDAY:
                        return "三";
                    case Calendar.THURSDAY:
                        return "四";
                    case Calendar.FRIDAY:
                        return "五";
                    case Calendar.SATURDAY:
                        return "六";
                }
                return "";
            }

        });
        getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // COMPLETED (5) Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data

                try {
                    return gj94DBHelper.getReadableDatabase().
                            query(DBContract.ClockEntry.TABLE_NAME,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void restartManageLoader(){
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,MainActivity.this);
    }


}
