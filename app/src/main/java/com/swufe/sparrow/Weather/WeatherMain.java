package com.swufe.sparrow.Weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.swufe.sparrow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherMain extends AppCompatActivity implements View.OnClickListener {

    private EditText weather;
    private EditText temperature;
    private String Weather;
    private String Tempeature;
    Button sendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);
        sendRequest = findViewById(R.id.send_request);
        weather = findViewById(R.id.Weather);
        temperature = findViewById(R.id.Temperature);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_request) {
            sendRequestWithOkHttp();
        }
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建一个OkHttp实例
                    Request request = new Request.Builder().url("https://api.seniverse.com/v3/weather/now.json?key=SrvH71t8JeTOXNLJP&location=chengdu&language=zh-Hans&unit=c").build();//创建Request对象发起请求,记得替换成你自己的key
                    Response response = client.newCall(request).execute();//创建call对象并调用execute获取返回的数据
                    String responseData = response.body().string();
                    showResPonse();//显示原始数据和解析后的数据
                    parseJSONWithJSONObject(responseData);//解析SSON数据
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String jsonData) {//用JSONObect解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            Log.i("parseJSON", "parseJSONWithJSONObject: " + jsonObject);
            JSONArray results = jsonObject.getJSONArray("results");   //得到键为results的JSONArray
            JSONObject now = results.getJSONObject(0).getJSONObject("now");//得到键值为"now"的JSONObject
            Weather = now.getString("text");//得到"now"键值的JSONObject下的"text"属性,即天气信息
            WeatherMain.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (Weather.equals("晴")) {
                        sendRequest.setBackgroundResource(R.drawable.qing);
                    } else if (Weather.contains("多云")) {
                        sendRequest.setBackgroundResource(R.drawable.yun);
                    } else if (Weather.contains("阴")) {
                        sendRequest.setBackgroundResource(R.drawable.yin);
                    } else if (Weather.contains("雨")) {
                        sendRequest.setBackgroundResource(R.drawable.yu);
                    } else if (Weather.contains("雪")) {
                        sendRequest.setBackgroundResource(R.drawable.xue);
                    }
                }
            });


            Log.i("parseJSON", "parseJSONWithJSONObject: " + Weather);
            Tempeature = now.getString("temperature"); //获取温度
            Log.i("parseJSON", "parseJSONWithJSONObject: " + Tempeature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showResPonse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                weather.setText(Weather);
                temperature.setText(Tempeature);
            }
        });
    }
}

