package com.example.gj94g.b10509034_hw4.urilite;

import android.provider.BaseColumns;

public class DBContract {

    public static final class ClockEntry implements BaseColumns{
        public static final String TABLE_NAME = "clocks";

        public static final String COLUMN_ENABLE = "enable";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_WEEK = "week";
    }

}
