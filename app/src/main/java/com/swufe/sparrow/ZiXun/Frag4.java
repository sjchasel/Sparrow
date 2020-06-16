package com.swufe.sparrow.ZiXun;

import android.content.Intent;
import android.net.Uri;
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

public class Frag4 extends ListFragment implements AdapterView.OnItemClickListener, Runnable {

    private ListView listView;
    Handler handler;
    private SimpleAdapter listItemAdapter;
    private ArrayList<HashMap<String, Object>> listItems;

    public Frag4() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag4, null);
        listView = view.findViewById(android.R.id.list);
        initListView();
        this.setListAdapter(listItemAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    private void initListView() {
        listItems = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("Title", "wait..."); // 标题文字
        listItems.add(map);

        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(getActivity(), listItems, // listItems数据源
                R.layout.item4,
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

        setListAdapter(listItemAdapter);
        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 5) {
                    List<HashMap<String, String>> tList = (List<HashMap<String, String>>) msg.obj;
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), tList, // listItems数据源
                            R.layout.item4, // ListItem的XML布局实现
                            new String[]{"Title", "url"},
                            new int[]{R.id.title, R.id.url});
                    setListAdapter(adapter);
                    Log.i("handler", "reset list...");
                }
                super.handleMessage(msg);

            }
        };
    }


    public void onListItemClick(ListView l, View v, int position, long id) {
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


    public void run() {

        List<HashMap<String, String>> testList = new ArrayList<HashMap<String, String>>();
        try {
            Document doc4 = Jsoup.connect("https://tophub.today/n/X12owXzvNV").get();
            Elements als4 = doc4.getElementsByClass("al");
            for (int i = 0; i <= 19; i++) {
                String content = als4.get(i).getElementsByTag("a").attr("href");
                Log.i("Frag4", "run: " + content);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", content);
                String spanStr = als4.get(i).text();
                Log.i("title", spanStr);
                map.put("Title", spanStr);
                testList.add(map);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        msg.obj = testList;
        handler.sendMessage(msg);

    }

}
