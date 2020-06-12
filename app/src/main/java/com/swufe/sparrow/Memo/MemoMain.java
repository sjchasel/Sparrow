package com.swufe.sparrow.Memo;

import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.swufe.sparrow.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoMain extends ListActivity implements  OnItemLongClickListener,AdapterView.OnItemClickListener {

    ListView listview;
    SimpleAdapter simple_adapter;
    List<Map<String, Object>> dataList;
    Button addNote;
    TextView tv_content;
    DBHelper DbHelper;
    SQLiteDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_main);

        InitView();

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
        addNote.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MemoMain.this, MemoEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                bundle.putInt("enter_state", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    //在activity显示的时候更新listview
    @Override
    protected void onStart() {
        super.onStart();

        RefreshNotesList();
    }


    private void InitView() {
        tv_content = findViewById(R.id.tv_content);
        listview = findViewById(android.R.id.list);
        dataList = new ArrayList<Map<String, Object>>();
        addNote = findViewById(R.id.btn_editnote);
        DbHelper = new DBHelper(this);
        DB = DbHelper.getReadableDatabase();




    }


    //刷新listview
    public void RefreshNotesList() {
        //如果dataList已经有的内容，全部删掉
        //并且更新simp_adapter
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simple_adapter.notifyDataSetChanged();
        }

        //从数据库读取信息
        Cursor cursor = DB.query("note", null, null, null, null, null, null);
        startManagingCursor(cursor);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", name);
            map.put("tv_date", date);
            dataList.add(map);
        }
        simple_adapter = new SimpleAdapter(this, dataList, R.layout.item,
                new String[]{"tv_content", "tv_date"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview.setAdapter(simple_adapter);
    }


    // 点击listview中某一项的点击监听事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        //获取listview中此个item中的内容
        String content = listview.getItemAtPosition(arg2) + "";
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));

        Intent myIntent = new Intent(MemoMain.this, MemoEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString("info", content1);
        bundle.putInt("enter_state", 1);
        myIntent.putExtras(bundle);
        startActivity(myIntent);

    }

    // 点击listview中某一项长时间的点击事件
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,
                                   long arg3) {
        Builder builder = new Builder(this);
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取listview中此个item中的内容
                //删除该行后刷新listview的内容
                String content = getListView().getItemAtPosition(arg2) + "";
                String content1 = content.substring(content.indexOf("=") + 1,
                        content.indexOf(","));
                DB.delete("note", "content = ?", new String[]{content1});
                RefreshNotesList();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
        return true;
    }
}
