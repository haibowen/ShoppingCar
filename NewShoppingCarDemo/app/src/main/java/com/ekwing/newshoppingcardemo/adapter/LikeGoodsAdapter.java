package com.ekwing.newshoppingcardemo.adapter;

import android.content.Context;
import android.util.Log;
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

public class LikeGoodsAdapter extends RecyclerView.Adapter<LikeGoodsAdapter.ViewHolder>  {
    private Context mContext;//上下文
    private List<LikeGoodsBean> mData;//数据源
    private OnItemClickLongCallBack mOnItemClickLongCallBack;//长按的回调接口

    public LikeGoodsAdapter(Context mContext, List<LikeGoodsBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPicture;//展示图片
        private TextView tvDetail;//商品描述
        private TextView tvPrice;//商品价格

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture=itemView.findViewById(R.id.iv_picture);
            tvDetail=itemView.findViewById(R.id.tv_detail);
            tvPrice=itemView.findViewById(R.id.tv_price);
        }
    }

    @NonNull
    @Override
    public LikeGoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_layout, parent, false);

        final ViewHolder mViewHolder=new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LikeGoodsAdapter.ViewHolder holder, final int position) {

        LikeGoodsBean likeGoodsBean = mData.get(position);
        holder.tvDetail.setText(likeGoodsBean.getmDetialText());
        holder.tvPrice.setText(likeGoodsBean.getmDetailPrice());
        Glide.with(mContext).load(likeGoodsBean.getmImagId()).into(holder.ivPicture);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnItemClickLongCallBack != null){
                    mOnItemClickLongCallBack.itemLongClick(holder.itemView, position);
                    Log.e("333333", "onLongClick: "+"6666666" );
                }

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface  OnItemClickLongCallBack{
        void itemLongClick(View view,int postion);

    }
    public void setOnItemClickLongCallBack(OnItemClickLongCallBack longCallBack){
        mOnItemClickLongCallBack=longCallBack;


    }


}
