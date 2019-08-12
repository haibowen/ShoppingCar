package com.ekwing.newshoppingcardemo.helper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ekwing.newshoppingcardemo.R;
import com.ekwing.newshoppingcardemo.view.ItemMaskLayout;
import com.ekwing.newshoppingcardemo.view.RippleLayout;

public class ItemLongClickMaskHelper {

    private FrameLayout mRootFrameLayout; //列表Item根布局FrameLayout
    private ItemMaskLayout mMaskItemLayout;//遮罩层
    private AnimatorSet mAnimSet;//动画
    private ItemMaskClickListener mItemMaskClickListener;//回调的接口

    public ItemLongClickMaskHelper(Context context) {
        mMaskItemLayout = new ItemMaskLayout(context);
        TextView btnFindSame = mMaskItemLayout.findViewById(R.id.btn_find_same);
        TextView btnCollect = mMaskItemLayout.findViewById(R.id.btn_collect);
        //初始化动画效果
        initAnimatorSet(context, btnFindSame, btnCollect);
        //遮罩层的点击事件
        onClickMaskItemLayout();
        //遮罩层长按的点击事件
        onLongClickMaskItem();
        //找相似的点击事件
        FindSame(btnFindSame);
        //收藏的点击事件
        collectSame(btnCollect);
    }

    private void initAnimatorSet(Context context, TextView btnFindSame, TextView btnCollect) {
        mAnimSet = new AnimatorSet();
//        int dip55 = dip2px(context, 55);
//        int dip45 = dip2px(context, 45);
        int dip55 = dip2px(context, 65);
        int dip45 = dip2px(context, -40);
        ObjectAnimator mAnimRipp = ((RippleLayout)mMaskItemLayout.findViewById(R.id.rp_bg)).getRadiusAnimator();
        mAnimSet.play(mAnimRipp)
                .with(ObjectAnimator.ofFloat(btnCollect, "translationX", 0, -dip55))
                .with(ObjectAnimator.ofFloat(btnFindSame, "translationX", 0, dip55));
        ObjectAnimator mAnim = ObjectAnimator.ofFloat(btnCollect, "translationX", -dip55, -dip45);
        mAnimSet.play(mAnim).with(ObjectAnimator.ofFloat(btnFindSame, "translationX", dip55, dip45));
        mAnimSet.play(mAnimRipp).before(mAnim);
        mAnimSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimSet.setDuration(100);
    }

    private void collectSame(TextView btnCollect) {
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemMaskClickListener != null) {
                    dismissMaskLayout();
                    mItemMaskClickListener.collect();
                }
            }
        });
    }

    private void FindSame(TextView btnFindSame) {
        btnFindSame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemMaskClickListener != null) {
                    dismissMaskLayout();
                    mItemMaskClickListener.findSame();
                }
            }
        });
    }

    private void onLongClickMaskItem() {
        mMaskItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dismissMaskLayout();
                return true;
            }
        });
    }

    private void onClickMaskItemLayout() {
        mMaskItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissMaskLayout();
            }
        });
    }

    /**
     * 设置背景的出现
     *
     * @param frameLayout
     */
    public synchronized void setRootFrameLayout(FrameLayout frameLayout) {
        if (mRootFrameLayout != null) {
            mRootFrameLayout.removeView(mMaskItemLayout);
        }
        mRootFrameLayout = frameLayout;
        mRootFrameLayout.addView(mMaskItemLayout);
        mAnimSet.start();
    }

    public void dismissMaskLayout() {
        if (mRootFrameLayout != null) {
            mRootFrameLayout.removeView(mMaskItemLayout);
        }
    }

    public void setMaskItemListener(ItemMaskClickListener listener) {
        this.mItemMaskClickListener = listener;
    }

    public interface ItemMaskClickListener {
        void findSame();

        void collect();
    }

    /**
     * 根据手机的分辨率将px转换成dp
     *
     * @param context
     * @param dip
     * @return
     */
    private int dip2px(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dip + 0.5f);
    }
}
