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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Frag4 extends ListFragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    Handler handler;
    private SimpleAdapter listItemAdapter;
    private ArrayList<HashMap<String, Object>> listItems;
    private int flag;
    private ProgressDialog progressDialog;

    public Frag4() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag4, null);
        listView = view.findViewById(android.R.id.list);


        initListView();
        this.setListAdapter(listItemAdapter);

        listView.setOnItemClickListener(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myTest", Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();//要获得一个edit对象才可以改变sharedPreferences
        editor.commit();

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
        new Frag4.DownloadTask().execute();


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

    class DownloadTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            Log.i("thread", "run... ");
            List<HashMap<String, String>> testList = new ArrayList<HashMap<String, String>>();
            try {
                //Thread.sleep(1000);

                Document doc4 = Jsoup.connect("https://tophub.today/n/X12owXzvNV").get();
                Elements als4 = doc4.getElementsByClass("al");
                for (int i = 0; i <= 19; i++) {
                    String content = als4.get(i).getElementsByTag("a").attr("href");
                    Log.i("Frag2", "run: " + content);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("url", content);
                    //testList.add(map);
                    String spanStr = als4.get(i).text();
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
