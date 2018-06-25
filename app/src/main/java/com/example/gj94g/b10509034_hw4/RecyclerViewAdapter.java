package com.example.gj94g.b10509034_hw4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gj94g.b10509034_hw4.urilite.DBContract;
import com.example.gj94g.b10509034_hw4.urilite.DBHelper;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private String[] weeks;
    private DBHelper dbHelper;
    private MainActivity main;



    public RecyclerViewAdapter(Context context, String[] weeks, DBHelper dbHelper, MainActivity main) {
        mContext = context;
        this.weeks = weeks;
        this.dbHelper = dbHelper;
        this.main = main;
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor == c)
            return null;
        Cursor temp = mCursor;
        this.mCursor = c;
        if (c != null)
            this.notifyDataSetChanged();
        return temp;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.myclass, parent, false);
        view.findViewById(R.id.class_checkbok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = database.rawQuery(
                        "SELECT * FROM " + DBContract.ClockEntry.TABLE_NAME +
                                " WHERE " + DBContract.ClockEntry._ID +
                                " IS " + String.valueOf(v.getTag()),
                        null);
                int indexId = cursor.getColumnIndex(DBContract.ClockEntry._ID);
                int indexName = cursor.getColumnIndex(DBContract.ClockEntry.COLUMN_NAME);
                int indexEnable = cursor.getColumnIndex(DBContract.ClockEntry.COLUMN_ENABLE);
                cursor.moveToFirst();
                String target = cursor.getString(indexEnable).compareTo("y") == 0 ? "n" : "y";
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBContract.ClockEntry.COLUMN_ENABLE, target);
                database.update(DBContract.ClockEntry.TABLE_NAME, contentValues, String.format("%s=%d", DBContract.ClockEntry._ID, cursor.getLong(indexId)), null);

                main.restartManageLoader();
                Log.d("MYTAG", String.format("%s:%s", String.valueOf(cursor.getString(indexName)), String.valueOf(cursor.getString(indexEnable))));
            }
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int indexId = mCursor.getColumnIndex(DBContract.ClockEntry._ID);
        int indexName = mCursor.getColumnIndex(DBContract.ClockEntry.COLUMN_NAME);
        int indexEnable = mCursor.getColumnIndex(DBContract.ClockEntry.COLUMN_ENABLE);
        int indexTime = mCursor.getColumnIndex(DBContract.ClockEntry.COLUMN_TIME);
        int indexWeek = mCursor.getColumnIndex(DBContract.ClockEntry.COLUMN_WEEK);

        mCursor.moveToPosition(position);

        holder.classWeek.setText(weeks[Integer.parseInt(mCursor.getString(indexWeek))-1]);
        holder.className.setText(mCursor.getString(indexName));
        holder.classCheckBox.setChecked(mCursor.getString(indexEnable).compareTo("y")==0);
        holder.classTime.setText(mCursor.getString(indexTime));

        holder.classCheckBox.setTag(mCursor.getLong(indexId));
    }

    @Override
    public int getItemCount() {
        SQLiteDatabase db = MainActivity.gj94DBHelper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.ClockEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        return cursor.getCount();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox classCheckBox;
        TextView className;
        TextView classWeek;
        TextView classTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            classCheckBox = itemView.findViewById(R.id.class_checkbok);
            className = itemView.findViewById(R.id.class_name);
            classWeek = itemView.findViewById(R.id.class_week);
            classTime = itemView.findViewById(R.id.class_time);
        }
    }
}
