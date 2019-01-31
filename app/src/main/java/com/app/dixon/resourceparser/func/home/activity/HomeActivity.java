package com.app.dixon.resourceparser.func.home.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.parser.ParseQueue;
import com.app.dixon.resourceparser.core.pub.parser.Parser;
import com.app.dixon.resourceparser.core.util.LogUtil;
import com.app.dixon.resourceparser.model.MovieOutline;

import java.util.List;

public class HomeActivity extends Activity {

    //    private String url = "http://www.qiushibaike.com/8hr/page/1/";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);

        ParseQueue queue = Parser.newParseQueue();

        /*
        Request<List<MovieOutline>> request = new Request<>(url, new Request.ParseListener<List<MovieOutline>>() {
            @Override
            public List<MovieOutline> onParse(Document doc) {
                List<MovieOutline> movies = new ArrayList<>();
                Elements divs = doc.select("div.co_content222");
                for (int i = 0; i < divs.size(); i++) {
                    Elements nodeAList = divs.get(i).select("a");
                    for (int j = 0; j < nodeAList.size(); j++) {
                        Element nodeA = nodeAList.get(j);
                        String title = nodeA.attr("title");
                        String url = nodeA.attr("href"); //只能解析对应标签下的属性
                        movies.add(new MovieOutline(title, url));
                    }
                }
                return movies;
            }
        }, new Response.Listener<List<MovieOutline>>() {
            @Override
            public void onResponse(List<MovieOutline> response) {
                tv.setText(response.toString());
                LogUtil.e("testkkk", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(ParseError error) {
                tv.setText(error.getMsg());
            }
        });

        queue.add(request);
        */

        queue.add(new MovieOutlineRequest(new MovieOutlineRequest.Listener() {
            @Override
            public void onSuccess(List<MovieOutline> list) {
                tv.setText(list.toString());
                LogUtil.e("testkkk", list.toString());
            }

            @Override
            public void onFail(String msg) {
                tv.setText(msg);
            }
        }));

    }
}
