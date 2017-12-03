package com.kiminonawa.mydiary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import static com.kiminonawa.mydiary.db.DBStructure.DiaryEntry_V2;
import static com.kiminonawa.mydiary.db.DBStructure.DiaryItemEntry_V2;
import static com.kiminonawa.mydiary.db.DBStructure.TopicEntry;
import static com.kiminonawa.mydiary.db.DBStructure.TopicOrderEntry;

public class DBManager {


    //TODO add SQLiteException
    private Context context;
    private SQLiteDatabase db;
    private DBHelper mDBHelper;

    public DBManager(Context context) {
        this.context = context;
    }

    public DBManager(SQLiteDatabase db) {
        this.db = db;
    }

    /*
     * DB IO
     */

    public void opeDB() throws SQLiteException {
        mDBHelper = new DBHelper(context);
        // Gets the data repository in write mode
        this.db = mDBHelper.getWritableDatabase();
    }


    public void closeDB() {
        mDBHelper.close();
    }


    public void beginTransaction() {
        db.beginTransaction();
    }

    public void setTransactionSuccessful() {
        db.setTransactionSuccessful();
    }

    public void endTransaction() {
        db.endTransaction();
    }

    /*
     * Topic
     */

    public long insertTopic(String name, int type, int color) {
        return db.insert(
                TopicEntry.TABLE_NAME,
                null,
                this.createTopicCV(name, type, color));
    }

    public long insertTopicOrder(long topicId, long order) {
        ContentValues values = new ContentValues();
        values.put(TopicOrderEntry.COLUMN_ORDER, order);
        values.put(TopicOrderEntry.COLUMN_REF_TOPIC__ID, topicId);
        return db.insert(
                TopicOrderEntry.TABLE_NAME,
                null,
                values);
    }

    public long updateTopic(long topicId, String name, int color) {
        ContentValues values = new ContentValues();
        values.put(TopicEntry.COLUMN_NAME, name);
        values.put(TopicEntry.COLUMN_COLOR, color);
        return db.update(
                TopicEntry.TABLE_NAME,
                values,
                TopicEntry._ID + " = ?",
                new String[]{String.valueOf(topicId)});
    }

    /**
     * Select Topic & order for show in Topic list
     *
     * @return
     */
    public Cursor selectTopic() {
        Cursor c = db.rawQuery("SELECT * FROM " + TopicEntry.TABLE_NAME
                        + " LEFT OUTER JOIN " + TopicOrderEntry.TABLE_NAME
                        + " ON " + TopicEntry._ID + " = " + TopicOrderEntry.COLUMN_REF_TOPIC__ID
                        + " ORDER BY " + TopicOrderEntry.COLUMN_ORDER + " DESC "
                , null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public long deleteAllCurrentTopicOrder() {
        return db.delete(
                TopicOrderEntry.TABLE_NAME,
                null, null);
    }

    public int getDiaryCountByTopicId(long topicId) {
        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM " + DiaryEntry_V2.TABLE_NAME + " WHERE " + DiaryEntry_V2.COLUMN_REF_TOPIC__ID + "=?",
                new String[]{String.valueOf(topicId)});
        int count = 0;
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }


    public long delTopic(long topicId) {
        return db.delete(
                TopicEntry.TABLE_NAME,
                TopicEntry._ID + " = ?"
                , new String[]{String.valueOf(topicId)});
    }

    private ContentValues createTopicCV(String name, int type, int color) {
        ContentValues values = new ContentValues();
        values.put(TopicEntry.COLUMN_NAME, name);
        values.put(TopicEntry.COLUMN_TYPE, type);
        values.put(TopicEntry.COLUMN_COLOR, color);
        return values;
    }


    /*
     * Diary
     */
    public long insertDiaryInfo(long time, String title,
                                int mood, int weather, boolean attachment,
                                long refTopicId, String locationName) {
        return db.insert(
                DiaryEntry_V2.TABLE_NAME,
                null,
                this.createDiaryInfoCV(time, title,
                        mood, weather, attachment, refTopicId, locationName));
    }

    public long insertDiaryContent(int type, int position, String content, long diaryId) {
        return db.insert(
                DiaryItemEntry_V2.TABLE_NAME,
                null,
                this.createDiaryContentCV(type, position, content, diaryId));
    }

    public long updateDiary(long diaryId, long time, String title,
                            int mood, int weather, String location, boolean attachment) {
        ContentValues values = new ContentValues();
        values.put(DiaryEntry_V2.COLUMN_TIME, time);
        values.put(DiaryEntry_V2.COLUMN_TITLE, title);
        values.put(DiaryEntry_V2.COLUMN_MOOD, mood);
        values.put(DiaryEntry_V2.COLUMN_WEATHER, weather);
        values.put(DiaryEntry_V2.COLUMN_LOCATION, location);
        values.put(DiaryEntry_V2.COLUMN_ATTACHMENT, attachment);

        return db.update(
                DiaryEntry_V2.TABLE_NAME,
                values,
                DiaryEntry_V2._ID + " = ?",
                new String[]{String.valueOf(diaryId)});
    }

    public long delDiary(long diaryId) {
        return db.delete(
                DiaryEntry_V2.TABLE_NAME,
                DiaryEntry_V2._ID + " = ?"
                , new String[]{String.valueOf(diaryId)});
    }

    public long delAllDiaryInTopic(long topicId) {
        return db.delete(
                DiaryEntry_V2.TABLE_NAME,
                DiaryEntry_V2.COLUMN_REF_TOPIC__ID + " = ?"
                , new String[]{String.valueOf(topicId)});
    }

    public long delAllDiaryItemByDiaryId(long diaryId) {
        return db.delete(
                DiaryItemEntry_V2.TABLE_NAME,
                DiaryItemEntry_V2.COLUMN_REF_DIARY__ID + " = ?"
                , new String[]{String.valueOf(diaryId)});
    }


    public Cursor selectDiaryList(long topicId) {
        Cursor c = db.query(DiaryEntry_V2.TABLE_NAME, null, DiaryEntry_V2.COLUMN_REF_TOPIC__ID + " = ?", new String[]{String.valueOf(topicId)}, null, null,
                DiaryEntry_V2.COLUMN_TIME + " DESC , " + DiaryEntry_V2._ID + " DESC", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor selectDiaryInfoByDiaryId(long diaryId) {
        Cursor c = db.query(DiaryEntry_V2.TABLE_NAME, null, DiaryEntry_V2._ID + " = ?", new String[]{String.valueOf(diaryId)},
                null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor selectDiaryContentByDiaryId(long diaryId) {
        Cursor c = db.query(DiaryItemEntry_V2.TABLE_NAME, null, DiaryItemEntry_V2.COLUMN_REF_DIARY__ID + " = ?", new String[]{String.valueOf(diaryId)},
                null, null, DiaryItemEntry_V2.COLUMN_POSITION + " ASC", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    private ContentValues createDiaryInfoCV(long time, String title,
                                            int mood, int weather, boolean attachment, long refTopicId,
                                            String locationName) {
        ContentValues values = new ContentValues();
        values.put(DiaryEntry_V2.COLUMN_TIME, time);
        values.put(DiaryEntry_V2.COLUMN_TITLE, title);
        values.put(DiaryEntry_V2.COLUMN_MOOD, mood);
        values.put(DiaryEntry_V2.COLUMN_WEATHER, weather);
        values.put(DiaryEntry_V2.COLUMN_ATTACHMENT, attachment);
        values.put(DiaryEntry_V2.COLUMN_REF_TOPIC__ID, refTopicId);
        values.put(DiaryEntry_V2.COLUMN_LOCATION, locationName);
        return values;
    }

    private ContentValues createDiaryContentCV(int type, int position, String content, long diaryId) {
        ContentValues values = new ContentValues();
        values.put(DiaryItemEntry_V2.COLUMN_TYPE, type);
        values.put(DiaryItemEntry_V2.COLUMN_POSITION, position);
        values.put(DiaryItemEntry_V2.COLUMN_CONTENT, content);
        values.put(DiaryItemEntry_V2.COLUMN_REF_DIARY__ID, diaryId);
        return values;
    }


    /**
     * For version 4 onUpgrade
     */
    public Cursor selectAllV1Diary() {
        Cursor c = db.query(DBStructure.DiaryEntry.TABLE_NAME, null, null, null,
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    /*
     * Debug
     */

    //For Debug
    public void showCursor(Cursor cursor) {
        for (int i = 0; i < cursor.getCount(); i++) {
            StringBuilder sb = new StringBuilder();
            int columnsQty = cursor.getColumnCount();
            for (int idx = 0; idx < columnsQty; ++idx) {
                sb.append(" " + idx + " = ");
                sb.append(cursor.getString(idx));
                if (idx < columnsQty - 1)
                    sb.append(" ; ");
            }
            Log.e("test", String.format("Row: %d, Values: %s", cursor.getPosition(), sb.toString()));
            cursor.moveToNext();
        }
        //Revert Cursor
        cursor.moveToFirst();
    }

}
