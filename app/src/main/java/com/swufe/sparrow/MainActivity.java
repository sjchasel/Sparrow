package com.swufe.sparrow;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.TimeZone;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText anhao;
    Button dl;
    TextView tv;
    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.wenhao);
        tv.setText("你好，今天是"+time());



    }


    public void click(View view) {
        anhao = findViewById(R.id.anhao2);
        String ah = anhao.getText().toString();
        if(ah.equals("孜孜以求")){
            Toast.makeText(MainActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,Home.class);
            startActivity(intent);

        }else{
            Toast.makeText(MainActivity.this,"暗号错误，登陆失败！",Toast.LENGTH_SHORT).show();
        }

    }

    public String time(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return mYear + "年" + mMonth + "月" + mDay+"日"+"/星期"+mWay;
    }
}