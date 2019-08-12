package com.ekwing.newshoppingcardemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ekwing.newshoppingcardemo.R;
import com.ekwing.newshoppingcardemo.bean.LikeGoodsBean;

import java.util.List;

public class InvidateAdapter extends RecyclerView.Adapter<InvidateAdapter.ViewHolder> {

    private Context mContext;//
    private List<LikeGoodsBean> mData;

    public InvidateAdapter(Context mContext, List<LikeGoodsBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivShow;//失效的商品的图片
        private TextView tvShow;//失效的商品的名字



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivShow=itemView.findViewById(R.id.iv_show);
            tvShow=itemView.findViewById(R.id.tv_show);

        }
    }

    @NonNull
    @Override
    public InvidateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.item_invalid_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InvidateAdapter.ViewHolder holder, int position) {

        LikeGoodsBean likeGoodsBean=mData.get(position);
        holder.tvShow.setText(likeGoodsBean.getmDetialText());
        Glide.with(mContext).load(likeGoodsBean.getmImagId()).into(holder.ivShow);



    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
