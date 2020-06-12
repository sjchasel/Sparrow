package com.swufe.sparrow.Task;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.swufe.sparrow.R;

import java.util.ArrayList;
import java.util.List;

public class Task extends ListActivity implements AdapterView.OnItemLongClickListener {
    EditText ed;
    Button button;
    ArrayAdapter<String> adapter;
    ListView listView;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        list = new ArrayList<>();
        list.add("看书");
        list.add("散步");
        getListView().setOnItemLongClickListener(this);
        button = (Button)findViewById(R.id.button);
        ed = (EditText)findViewById(R.id.edit);
        adapter = new ArrayAdapter<String>(Task.this, android.R.layout.simple_list_item_1,list);
        listView = findViewById(android.R.id.list);
        listView.setAdapter(adapter);//关联适配器
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String add = ed.getText().toString();
                list.add(add);
            }
        });
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i("Task", "onItemLongClick: 长按列表项position=" + position);
        //删除操作
        //构造对话框进行确认操作
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Task", "onClick: 对话框事件处理");
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);
        builder.create().show();
        Log.i("Task", "onItemLongClick: size=" + list.size());

        return true;
    }
}
