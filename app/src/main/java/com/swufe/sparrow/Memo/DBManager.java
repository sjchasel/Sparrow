package com.swufe.sparrow.Memo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DBHelper dbHelper;
    private String TBNAME;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }

    public void add(MemoItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", item.getContent());
        values.put("date", item.getDate());
        db.insert(TBNAME, null, values);
        db.close();
    }

    public void addAll(List<MemoItem> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (MemoItem item : list) {
            ContentValues values = new ContentValues();
            values.put("content", item.getContent());
            values.put("date", item.getDate());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, null, null);
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void update(MemoItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", item.getContent());
        values.put("date", item.getDate());
        db.update(TBNAME, values, "ID=?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public List<MemoItem> listAll() {
        List<MemoItem> memoList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if (cursor != null) {
            memoList = new ArrayList<MemoItem>();
            while (cursor.moveToNext()) {
                MemoItem item = new MemoItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
                item.setDate(cursor.getString(cursor.getColumnIndex("DATE")));

                memoList.add(item);
            }
            cursor.close();
        }
        db.close();
        return memoList;

    }

    public MemoItem findById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null, null, null);
        MemoItem memoItem = null;
        if (cursor != null && cursor.moveToFirst()) {
            memoItem = new MemoItem();
            memoItem.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            memoItem.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
            memoItem.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            cursor.close();
        }
        db.close();
        return memoItem;
    }
}
