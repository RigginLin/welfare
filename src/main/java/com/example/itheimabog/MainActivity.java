package com.example.itheimabog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.lv)
    ListView mLv;
    private  Gson gson = new Gson();
    List<ResultBean.ResultsBean> mList = new ArrayList<ResultBean.ResultsBean>();
    private GirlAdapter mAdapter;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //sendSyncRequest();
        mAdapter = new GirlAdapter(this,mList);
        mLv.setAdapter(mAdapter);
        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE){
                    if (mLv.getLastVisiblePosition() == mList.size() -1 && !isLoading){
                        loadMoreData();
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        sendAsynRequest();

    }

    private void loadMoreData() {
        isLoading = true;
        OkHttpClient okHttpClient = new OkHttpClient();
        int i  = mList.size() / 10 + 1;
        String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/"+i;
        Request request = new Request.Builder().get().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            //成功回调该方法,该方法是在子线程中运行的
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                ResultBean bean = gson.fromJson(json, ResultBean.class);
                mList.addAll(bean.getResults());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                });

                // Log.d(TAG, "onResponse: "+response.body().string());
            }
        });
    }

    //网络异步请求
    private void sendAsynRequest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1";
        Request request = new Request.Builder().get().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            //成功回调该方法,该方法是在子线程中运行的
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                ResultBean bean = gson.fromJson(json, ResultBean.class);
                mList.addAll(bean.getResults());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
                // Log.d(TAG, "onResponse: "+response.body().string());
            }
        });
    }

    //同步网络请求
    private void sendSyncRequest() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                OkHttpClient okHttpClient = new OkHttpClient();
                String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1";
                Request request = new Request.Builder().get().url(url).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    Log.d(TAG, "sendSyncRequest: "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
