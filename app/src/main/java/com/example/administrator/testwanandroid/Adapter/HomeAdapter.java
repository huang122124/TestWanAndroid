package com.example.administrator.testwanandroid.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.testwanandroid.Entity.HomeData;
import com.example.administrator.testwanandroid.R;
import com.example.administrator.testwanandroid.Utils.L;

import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder> {
     private List<HomeData>mList;
     private HomeData data;
     public HomeAdapter(List<HomeData>mList){
         this.mList=mList;
     }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_item,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        data=mList.get(i);
        L.d(data.getTitle());
        holder.tv_title.setText(data.getTitle());

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

   class Holder extends RecyclerView.ViewHolder{
        TextView tv_title;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
        }
    }
}
