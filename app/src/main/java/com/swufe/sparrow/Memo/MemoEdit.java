package com.swufe.sparrow.Memo;

import android.os.Bundle;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.swufe.sparrow.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoEdit extends Activity implements OnClickListener {
    private TextView tv_date;//日期
    private EditText et_content;//内容
    private Button btn_ok;//确定按钮
    private Button btn_cancel;//取消按钮
    private com.swufe.sparrow.Memo.DBHelper DBHelper;
    public int enter_state = 0;//用来区分是新建一个note还是更改原来的note
    public String last_content;//用来获取edittext内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        InitView();
    }

    private void InitView() {
        tv_date = findViewById(R.id.tv_date);
        et_content = findViewById(R.id.et_content);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel =  findViewById(R.id.btn_cancel);
        DBHelper = new DBHelper(this);

        //获取此时时刻时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = sdf.format(date);
        tv_date.setText(dateString);

        //接收内容和id
        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        enter_state = myBundle.getInt("enter_state");
        et_content.setText(last_content);

        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                //点击确认按钮
                SQLiteDatabase db = DBHelper.getReadableDatabase();
                // 获取edittext内容
                String content = et_content.getText().toString();

                // 添加一个新的日志
                if (enter_state == 0) {
                    if (!content.equals("")) {
                        //获取此时时刻时间
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String dateString = sdf.format(date);

                        //向数据库添加信息
                        ContentValues values = new ContentValues();
                        values.put("CONTENT", content);
                        values.put("DATE", dateString);
                        db.insert("tb_memo", null, values);
                        finish();
                    } else {
                        Toast.makeText(MemoEdit.this, "请输入你的内容！", Toast.LENGTH_SHORT).show();
                    }
                }
                //若enter_state=1，查看并修改一个已有的日志
                else {
                    ContentValues values = new ContentValues();
                    values.put("CONTENT", content);
                    db.update("tb_memo", values, "content = ?", new String[]{last_content});
                    finish();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}

