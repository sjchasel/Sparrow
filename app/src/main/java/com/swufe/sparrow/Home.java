package com.swufe.sparrow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.swufe.sparrow.Cal.Calculator;
import com.swufe.sparrow.Memo.MemoMain;
import com.swufe.sparrow.Task.Task;
import com.swufe.sparrow.Weather.WeatherMain;
import com.swufe.sparrow.ZiXun.ZiXun;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.zixun:
                Intent intent1 = new Intent(this, ZiXun.class);
                startActivity(intent1);
                break;
            case R.id.task:
                Intent intent2 = new Intent(this, Task.class);
                startActivity(intent2);
                break;
            case R.id.cal:
                Intent intent3 = new Intent(this, Calculator.class);
                startActivity(intent3);
                break;
            case R.id.memo:
                Intent intent4 = new Intent(this, MemoMain.class);
                startActivity(intent4);
                break;
            case R.id.weather:
                Intent intent5 = new Intent(this, WeatherMain.class);
                startActivity(intent5);
            default:
                break;
        }
    }
}
