package com.swufe.sparrow.ZiXun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swufe.sparrow.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Frag1 extends ListFragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private DownloadTask mTask;
    String todayStr;
    private SimpleAdapter listItemAdapter;
    String TAG = "Frag1";
    SharedPreferences sharedPreferences;
    List<HashMap<String, String>> testList;

    public Frag1() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag1, null);
        listView = view.findViewById(android.R.id.list);//得到列表控件
        initListView();//数据初始化
        this.setListAdapter(listItemAdapter);//将list的适配器设置为listItemAdapter
        listView.setOnItemClickListener(this);//为list设置点击事件
        //存数据的sp（list要转成json才能存）
        sharedPreferences = getActivity().getSharedPreferences("myTest", Activity.MODE_PRIVATE);
        return view;
    }

    private void initListView() {
        testList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Title", "wait..."); // 标题文字
        testList.add(map);

        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(getActivity(), testList, // listItems数据源
                R.layout.item1,
                new String[]{"Title", "url"},
                new int[]{R.id.title, R.id.url}
        );
        listView.setAdapter(listItemAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("myTest", Activity.MODE_PRIVATE);
        String updateDate = sharedPreferences.getString("update_date", "");
        Log.i(TAG, "onActivityCreated: 上次存储的时间是" + updateDate);
        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayStr = sdf.format(today);
        Log.i(TAG, "onActivityCreated: 当前时间是" + todayStr);
        //判断时间
        if (!todayStr.equals(updateDate)) {

            Log.i(TAG, "onActivityCreated: 需要更新");
            //开启异步处理
            mTask = new DownloadTask();
            mTask.execute();
            //this.setListAdapter(listItemAdapter);
            SharedPreferences sp = getActivity().getSharedPreferences("myTest", Activity.MODE_PRIVATE);
            String listJson = sp.getString("KEY_USER_DATA", "");
            Gson gson = new Gson();
            testList = gson.fromJson(listJson, new TypeToken<List<HashMap>>() {
            }.getType());


        } else {
            SharedPreferences sp = getActivity().getSharedPreferences("myTest", Activity.MODE_PRIVATE);
            String listJson = sp.getString("KEY_USER_DATA", "");
            Log.i(TAG, "onActivityCreated: listJson" + listJson);
            if (!listJson.equals("")) {
                Gson gson = new Gson();
                testList = gson.fromJson(listJson, new TypeToken<List<HashMap>>() {
                }.getType());
                Log.i(TAG, "onActivityCreated:testList " + testList);
                listItemAdapter = new SimpleAdapter(getActivity(), testList, // listItems数据源
                        R.layout.item1,
                        new String[]{"Title", "url"},
                        new int[]{R.id.title, R.id.url}
                );
                setListAdapter(listItemAdapter);

            }
            Log.i(TAG, "onCreate: 不需要更新");
        }


    }


    public void onListItemClick(ListView l, View v, int position, long id) {
        try {
            //从ListView中获取选中数据
            HashMap<String, String> map = (HashMap<String, String>) getListView().getItemAtPosition(position);
            String u = map.get("url");
            //打开新的页面
            Uri uri = Uri.parse(u);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            return;
        }
    }


    class DownloadTask extends AsyncTask<Integer, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(Integer... integers) {
            Log.i(TAG, "doInBackground: 异步处理");
            List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            try {
                Document doc = Jsoup.connect("https://tophub.today/n/Y3QeLGAd7k").get();
                Elements als = doc.getElementsByClass("al");
                for (int i = 0; i <= 19; i++) {
                    //获取链接
                    String content = als.get(i).getElementsByTag("a").attr("href");
                    Log.i("Frag1", "run:链接是 " + content);
                    //新建hashmap
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("url", content);//把链接放入
                    //获取标题
                    String spanStr = als.get(i).text();
                    Log.i("title", spanStr);
                    map.put("Title", spanStr);//放入标题

                    Log.i(TAG, "doInBackground: 此时map的内容是" + map);
                    //map存入list
                    list.add(map);
                }
            } catch (MalformedURLException e) {
                Log.e("www", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("www", e.toString());
                e.printStackTrace();
            }

            //把testList转成json存入；把当前时间存入。
            Gson user_gson = new Gson();
            String user_jsonStr = user_gson.toJson(list);
            SharedPreferences.Editor editor = sharedPreferences.edit();//要获得一个edit对象才可以改变sharedPreferences
            editor.putString("KEY_USER_DATA", user_jsonStr);
            editor.putString("update_date", todayStr);
            Log.i(TAG, "doInBackground: 已存入当前时间" + todayStr);
            editor.apply();
            return list;
        }

        @Override
        protected void onPreExecute() {
            //progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            Toast.makeText(getActivity(), "当前下载进度：" + values[0] + "%", Toast.LENGTH_SHORT).show();
        }


        protected void onPostExecute(List<HashMap<String, String>> result) {
            if (result == null || result.size() == 0) {
                Log.i(TAG, "onPostExecute: hashmap为空");
            } else {
                if (testList == null) {
                    testList = new ArrayList<>();
                }
                for (HashMap<String, String> li : result) {
                    testList.add(li);
                    //将下载的数据添加到list容器后，刷新每个Item的内容
                    listItemAdapter.notifyDataSetChanged();
                    Log.i(TAG, "onPostExecute: 已通知adapter去刷新");
                    listItemAdapter = new SimpleAdapter(getActivity(), testList, // listItems数据源
                            R.layout.item1,
                            new String[]{"Title", "url"},
                            new int[]{R.id.title, R.id.url}
                    );
                    setListAdapter(listItemAdapter);
                }
                super.onPostExecute(result);
            }

        }
    }
}
