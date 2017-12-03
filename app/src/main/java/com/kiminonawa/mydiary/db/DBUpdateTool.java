package com.kiminonawa.mydiary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.kiminonawa.mydiary.db.DBStructure.TopicEntry;

/**
 * Created by daxia on 2017/2/5.
 */

public class DBUpdateTool {


    //TODO add SQLiteException
    private SQLiteDatabase db;

    public DBUpdateTool(SQLiteDatabase db) {
        this.db = db;
    }

    /*
     * Version 6
     */


    /**
     * Old selectTopic method 
     * @return
     */
    
    public Cursor version_6_SelectTopic() {
        Cursor c = db.query(TopicEntry.TABLE_NAME, null, null, null, null, null,
                TopicEntry._ID + " DESC");
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    /*
     * Version 7
     */

    public long version_7_InsertTopicOrder(long topicId, long order) {
        ContentValues values = new ContentValues();
        values.put(DBStructure.TopicOrderEntry.COLUMN_ORDER, order);
        values.put(DBStructure.TopicOrderEntry.COLUMN_REF_TOPIC__ID, topicId);
        return db.insert(
                DBStructure.TopicOrderEntry.TABLE_NAME,
                null,
                values);
    }

    public Cursor version_7_SelectTopic() {
        Cursor c = db.query(TopicEntry.TABLE_NAME, null, null, null, null, null,
                TopicEntry._ID + " ASC");
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


}
