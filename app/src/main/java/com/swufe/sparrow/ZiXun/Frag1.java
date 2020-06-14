package com.swufe.sparrow.ZiXun;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.util.Map;
public class Frag1 extends ListFragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    Handler handler;
    private SimpleAdapter listItemAdapter;
    private ArrayList<HashMap<String, Object>> listItems;
    private int flag;
    private ProgressDialog progressDialog;
    String TAG = "Frag1";

    public Frag1() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag1, null);
        listView = view.findViewById(android.R.id.list);//得到列表控件
        initListView();//数据初始化
        this.setListAdapter(listItemAdapter);//将list的适配器设置为listItemAdapter
        listView.setOnItemClickListener(this);//为list设置点击事件
        //保存数据的sp
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myTest", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//要获得一个edit对象才可以改变sharedPreferences
        editor.commit();
        //gengxinpinlv(sharedPreferences);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 3) {

                    List<HashMap<String, String>> tList = (List<HashMap<String, String>>) msg.obj;
//                    ListAdapter adapter = new ArrayAdapter<String>(TestListActivity.this, android.R.layout.simple_list_item_1, tList);
//                    setListAdapter(adapter);
//                    Log.i("handler", "reset list...");
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), tList, // listItems数据源
                            R.layout.item1, // ListItem的XML布局实现
                            new String[]{"Title", "url"},
                            new int[]{R.id.title, R.id.url});
                    setListAdapter(adapter);
                    Log.i("handler", "reset list...");

                }
                super.handleMessage(msg);
            }
        };

        return view;
    }

    private void initListView() {
        listItems = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("Title", "wait..."); // 标题文字
        listItems.add(map);

        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(getActivity(), listItems, // listItems数据源
                R.layout.item1,
                new String[]{"Title", "url"},
                new int[]{R.id.title, R.id.url}
        );
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new DownloadTask().execute();


    }


    public void onListItemClick (ListView l, View v,int position, long id){
        try {
            //从ListView中获取选中数据
            HashMap<String, String> map = (HashMap<String, String>) getListView().getItemAtPosition(position);
            String titleStr = map.get("Title");
            String u = map.get("url");

            //打开新的页面
            Uri uri = Uri.parse(u);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            return;
        }
    }

//    public void gengxinpinlv(SharedPreferences sharedPreferences){
//        String updateDate = sharedPreferences.getString("update_date","");
//
//        //获取当前系统时间
//        Date today = Calendar.getInstance().getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        final String todayStr = sdf.format(today);
//
//        Log.i(TAG, "onCreate: sp updateDate=" + updateDate);
//        Log.i(TAG, "onCreate: todayStr=" + todayStr);
//
////判断时间
//        if(!todayStr.equals(updateDate)){
//            Log.i(TAG, "onCreate: 需要更新");
//            //开启子线程
//            Thread t = new Thread();
//            t.start();
//        }else{
//            Log.i(TAG, "onCreate: 不需要更新");
//        }
//    }

    class DownloadTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            Log.i("thread", "run... ");
            List<HashMap<String, String>> testList = new ArrayList<HashMap<String, String>>();
            try {
                //Thread.sleep(1000);

                        Document doc = Jsoup.connect("https://tophub.today/n/Y3QeLGAd7k").get();
                        Elements als = doc.getElementsByClass("al");
                        for (int i = 0; i <= 19; i++) {
                            String content = als.get(i).getElementsByTag("a").attr("href");
                            Log.i("Frag2", "run: " + content);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("url", content);
                            //testList.add(map);
                            String spanStr = als.get(i).text();
                            Log.i("title", spanStr);
                            map.put("Title", spanStr);
                            testList.add(map);
                            //putHashMapData(getActivity(), "data", map);
                        }




            } catch (MalformedURLException e) {
                Log.e("www", e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("www", e.toString());
                e.printStackTrace();
            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Message msg = handler.obtainMessage(3);
            msg.obj = testList;
            handler.sendMessage(msg);
            Log.i("thread", "sendMessage.....");

            return null;
        }

        @Override
        protected void onPreExecute() {
            //progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            //progressDialog.setMessage("当前下载进度：" + values[0] + "%");
            Toast.makeText(getActivity(), "当前下载进度：" + values[0] + "%", Toast.LENGTH_SHORT).show();
        }


        protected void onPostExecute(Boolean result) {
            //progressDialog.dismiss();
            if (result) {
                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
