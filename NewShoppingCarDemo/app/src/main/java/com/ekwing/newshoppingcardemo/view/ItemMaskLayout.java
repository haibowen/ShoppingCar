package com.ekwing.newshoppingcardemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ekwing.newshoppingcardemo.R;

public class ItemMaskLayout extends LinearLayout {

    public ItemMaskLayout(Context context) {
        this(context, null);
    }

    public ItemMaskLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemMaskLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_item_mask, this, true);
    }

}
