package com.knotworking.numbers.counter;

import android.database.Cursor;

import com.knotworking.numbers.database.CounterContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BRL on 17/04/17.
 */

public class CounterCursorConverter {

    public static List<CounterItem> getData (Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        List<CounterItem> counterItems = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String entryName = cursor.getString(cursor.getColumnIndex(CounterContract.Counters.NAME));
                int entryCount = cursor.getInt(cursor.getColumnIndex(CounterContract.Counters.COUNT));
                counterItems.add(new CounterItem(entryName, entryCount));
            } while (cursor.moveToNext());
        }

        return counterItems;
    }

}
