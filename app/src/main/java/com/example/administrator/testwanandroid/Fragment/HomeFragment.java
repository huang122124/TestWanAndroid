package com.example.administrator.testwanandroid.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.administrator.testwanandroid.Adapter.HomeAdapter;
import com.example.administrator.testwanandroid.Entity.HomeData;
import com.example.administrator.testwanandroid.R;
import com.example.administrator.testwanandroid.Statics.Constants;
import com.example.administrator.testwanandroid.Utils.GlideImageLoader;
import com.example.administrator.testwanandroid.Utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
private SmartRefreshLayout refreshLayout;
private Banner banner;
private List<String>images;
private List<String>titles;
private List<String>urls;
private RecyclerView home_list;
private HomeAdapter adapter;
private List<HomeData>mList;
private List<String>title;
private List<String>url;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
             View view=inflater.inflate(R.layout.fragment_home,container,false);
              return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
         refreshLayout=view.findViewById(R.id.smart_refresh);
         refreshLayout.setOnRefreshListener(new OnRefreshListener() {
             @Override
             public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                 refreshLayout.finishRefresh(2000);
             }
         });
         refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
             @Override
             public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                 refreshLayout.finishLoadMore(2000);
             }
         });
         banner=view.findViewById(R.id.home_banner);
         mList=new ArrayList<>();
         loadBanner();

         loadNews(view);  //加载RecyclerView


    }

    private void loadNews(View view) {
        home_list=view.findViewById(R.id.home_newslist);
        String url="http://www.wanandroid.com/article/list/0/json";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.d(t);
                ParsingJson(t);
            }

            @Override
            public void onFailure(VolleyError error) {
                L.e(error.toString());
            }
        });
        if (mList!=null) {
            adapter = new HomeAdapter(mList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            home_list.setLayoutManager(layoutManager);
            home_list.setAdapter(adapter);
        }
    }

    private void ParsingJson(String t)  {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONObject jsonData=jsonObject.getJSONObject("data");
            JSONArray array=jsonData.getJSONArray("datas");
            for (int i=0;i<array.length();i++){
                JSONObject datas=array.getJSONObject(i);
                String title=datas.getString("title");
                String url=datas.getString("link");
                HomeData data=new HomeData();
                data.setTitle(title);
                data.setAuthor(datas.getString("author"));
                data.setChapterName(datas.getString("chapterName"));
                data.setDesc(datas.getString("desc"));
                data.setNiceDate(datas.getString("niceDate"));
                mList.add(data);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void loadBanner() {
      titles=new ArrayList<>();
      images=new ArrayList<>();
      urls=new ArrayList<>();

      final String url=Constants.BANNER_URL;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                try {
                    JSONObject object=new JSONObject(t);
                    JSONArray array=object.getJSONArray("data");
                    //遍历 array 数组
                    if (array.length()>0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject=array.getJSONObject(i);
                            images.add(jsonObject.getString("imagePath"));
                            titles.add(jsonObject.getString("title"));
                            urls.add(jsonObject.getString("url"));
                        }
                        setBanner(images,titles);
                    }
                } catch (JSONException e) {
                    L.e(e.toString());
                }

            }
        });
    }
    private void setBanner(List<String>images,List<String>titles) {

        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images)
                .setBannerTitles(titles);//设置图片集合
        banner.isAutoPlay(true);
        banner.start();                     //banner设置方法全部调用完毕时最后调用
    }

}
