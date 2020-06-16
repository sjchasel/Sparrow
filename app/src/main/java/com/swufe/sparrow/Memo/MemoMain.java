package com.swufe.sparrow.Memo;

import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

public class MemoMain extends ListActivity implements OnItemLongClickListener, AdapterView.OnItemClickListener {

    ListView listview;
    SimpleAdapter simple_adapter;
    List<Map<String, Object>> dataList;
    com.swufe.sparrow.Memo.DBHelper DBHelper;
    Button addNote;//添加事件的按钮
    TextView tv_content;//内容
    DBHelper DbHelper;
    SQLiteDatabase DB;
    DBManager dbManager;
    String TAG = "MemoMain";

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
        dbManager = new DBManager(MemoMain.this);
        tv_content = findViewById(R.id.tv_content);
        listview = findViewById(android.R.id.list);
        dataList = new ArrayList<Map<String, Object>>();
        addNote = findViewById(R.id.btn_editnote);
        DbHelper = new DBHelper(this);
        DB = DbHelper.getReadableDatabase();


    }


    //刷新listview
    public void RefreshNotesList() {
        Log.i(TAG, "RefreshNotesList: 刷新列表");
        //如果dataList已经有的内容，全部删掉
        //并且更新simp_adapter
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simple_adapter.notifyDataSetChanged();
        }

        for (MemoItem memoItem : dbManager.listAll()) {
            String content = memoItem.getContent();
            String date = memoItem.getDate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("CONTENT", content);
            map.put("DATE", date);
            dataList.add(map);
        }

        simple_adapter = new SimpleAdapter(this, dataList, R.layout.item,
                new String[]{"CONTENT", "DATE"}, new int[]{
                R.id.tv_content, R.id.tv_date});
        listview.setAdapter(simple_adapter);
    }


    // 点击listview中某一项的点击监听事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        //获取listview中此个item中的内容
        String content = listview.getItemAtPosition(arg2) + "";
        String content1 = content.substring(content.lastIndexOf("=") + 1, content.lastIndexOf("}"));

        Intent myIntent = new Intent(MemoMain.this, MemoEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString("info", content1);
        bundle.putInt("enter_state", 1);
        myIntent.putExtras(bundle);
        startActivity(myIntent);

    }

    // 点击listview中某一项长时间的点击事件
    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position,
                                   long id) {
        Builder builder = new Builder(this);
        builder.setTitle("删除该记事");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = getListView().getItemAtPosition(position) + "";
                Log.i("MemoMain", "onClick: " + content);
                String content1 = content.substring(content.lastIndexOf("=") + 1, content.lastIndexOf("}"));

                //根据content的内容删除数据库中的记录，再刷新列表数据
//                DB.delete("tb_memo", "content = ?", new String[]{content1});
//                RefreshNotesList();

//                //根据position删除列表的数据，再通知适配器
//                Log.i(TAG, "onClick: dataList"+dataList);
//                dataList.remove(position);
//                Log.i(TAG, "onClick: dataList"+dataList);
//                simple_adapter.notifyDataSetChanged();
//
//
                //先获得这个item在数据库中的id，再根据id用manager中的delete方法删除数据，再刷新。
//                Cursor cursor = DB.query("tb_memo", null, null, null, null, null, null);
//                startManagingCursor(cursor);
//                String select_ImgUrl = null;
//                cursor.moveToPosition(position);// cursor移动到点击的当前行
//                MemoItem memoItem = new MemoItem(cursor.getString(1),cursor.getString(2));
//
//                for (int i=0;i<dbManager.listAll().size();i++){
//                    Log.i(TAG, "onClick:删除前的 "+(dbManager.listAll().get(i).getContent()));
//                }
//                dbManager.delete(memoItem.getId());
//                for (int i=0;i<dbManager.listAll().size();i++){
//                    Log.i(TAG, "onClick:删除后的 "+(dbManager.listAll().get(i).getContent()));
//                }
//                Log.i(TAG, "onClick: "+memoItem.getId());
//
//
//                //SQLiteDatabase db = DBHelper.getReadableDatabase();
                Log.i(TAG, "onClick: content1" + content1);
                DB.delete("tb_memo", "content = ?", new String[]{content1});
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
