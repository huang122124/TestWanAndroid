package com.example.administrator.testwanandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.testwanandroid.Activity.HomeWebView;
import com.example.administrator.testwanandroid.Entity.HomeData;
import com.example.administrator.testwanandroid.R;
import com.example.administrator.testwanandroid.Utils.L;

import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder> {
     private List<HomeData>mList;
     private HomeData data;
     private Context context;
     public HomeAdapter(Context context,List<HomeData>mList){
         this.mList=mList;
         this.context=context;
     }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_item,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int i) {
        if (mList!=null) {
            data = mList.get(i);
            holder.tv_title.setText(data.getTitle());
            holder.tv_author.setText(data.getAuthor());
            holder.tv_desc.setText(data.getDesc());
            holder.tv_niceDate.setText(data.getNiceDate());
            holder.tv_chapterName.setText(data.getChapterName());
        }
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "You click "+i, Toast.LENGTH_SHORT).show();
               String url=mList.get(i).getUrl();
               String title=mList.get(i).getTitle();
               L.d(title+url);
                Intent intent=new Intent(context,HomeWebView.class);
                intent.putExtra("url",url);
                intent.putExtra("title",title);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;

            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

   class Holder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_desc;
        TextView tv_author;
        TextView tv_niceDate;
        TextView tv_chapterName;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_author=itemView.findViewById(R.id.tv_author);
            tv_desc=itemView.findViewById(R.id.tv_desc);
            tv_niceDate=itemView.findViewById(R.id.tv_niceDate);
            tv_chapterName=itemView.findViewById(R.id.tv_chapterName);
        }
    }
}
