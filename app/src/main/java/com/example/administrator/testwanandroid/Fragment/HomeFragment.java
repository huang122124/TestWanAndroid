package com.example.administrator.testwanandroid.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Adapter;
import android.widget.ProgressBar;

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
private ProgressBar home_progressbar;
private List<String>title;
private List<String>url;
private Boolean refresh;
private int currentPage=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
             View view=inflater.inflate(R.layout.fragment_home,container,false);
              return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        banner=view.findViewById(R.id.home_banner);
        mList=new ArrayList<>();
        home_progressbar=view.findViewById(R.id.home_progressbar);
         refreshLayout=view.findViewById(R.id.smart_refresh);
         refreshLayout.setOnRefreshListener(new OnRefreshListener() {
             @Override
             public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                         loadBanner();
                         refresh=true;
                         loadNews(view);
                         adapter.notifyDataSetChanged();
                         refreshLayout.finishRefresh(1000);
             }
         });
         refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
             @Override
             public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadBanner();
                        loadMoreNews(view);
                    }
                },1000);
             }
         });
        refresh=false;       //首次加载
         loadBanner();
         loadNews(view);  //加载RecyclerView

    }

    private void loadMoreNews(View view) {
        home_list=view.findViewById(R.id.home_newslist);
        currentPage++;
        String url="http://www.wanandroid.com/article/list/"+currentPage+"/json";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                ParsingJson(t);
                //加载动画
                int resId = R.anim.layout_animation_from_bottom;
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(),resId);
                home_list.setLayoutAnimation(animation);
                adapter.notifyDataSetChanged();
                home_list.scheduleLayoutAnimation();
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onFailure(VolleyError error) {
                L.e(error.toString());
            }
        });

    }

    private void loadNews(View view) {
        home_list=view.findViewById(R.id.home_newslist);
        String url="http://www.wanandroid.com/article/list/"+0+"/json";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                ParsingJson(t);
                Message msg=new Message();
                msg.what=1;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(VolleyError error) {
                L.e(error.toString());
            }
        });

    }

    private void ParsingJson(String t)  {
        if (t!=null) {
            try {
                JSONObject jsonObject = new JSONObject(t);
                JSONObject jsonData = jsonObject.getJSONObject("data");
                JSONArray array = jsonData.getJSONArray("datas");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject datas = (JSONObject) array.get(i);
                    String title = datas.getString("title");
                    String url = datas.getString("link");
                    HomeData data = new HomeData();
                    data.setTitle(title);
                    data.setAuthor(datas.getString("author"));
                    data.setChapterName(datas.getString("chapterName"));
                    data.setDesc(datas.getString("desc"));
                    data.setNiceDate(datas.getString("niceDate"));
                    data.setUrl(url);
                    mList.add(data);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

public Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
         switch (msg.what){
             case 1:
                 handler.postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         adapter = new HomeAdapter(getActivity(),mList);
                         LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                         home_list.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));   //设置分割线
                         //加载动画
                         if (!refresh) {
                             int resId = R.anim.layout_animation_from_bottom;
                             LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
                             home_list.setLayoutAnimation(animation);
                         }
                         home_list.setLayoutManager(layoutManager);
                         home_list.setAdapter(adapter);
                         home_progressbar.setVisibility(View.GONE);      //进度条消失
                     }
                 },2000);
                 break;
         }
    }
};
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

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }
}
