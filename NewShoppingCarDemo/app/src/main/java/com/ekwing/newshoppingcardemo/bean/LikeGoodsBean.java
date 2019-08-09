package com.ekwing.newshoppingcardemo.bean;

public class LikeGoodsBean {

    private String mImagId;//图片的id
    private String mDetialText;//商品的详情
    private String mDetailPrice;//商品的价格

    public LikeGoodsBean(String mImagId, String mDetialText, String mDetailPrice) {
        this.mImagId = mImagId;
        this.mDetialText = mDetialText;
        this.mDetailPrice = mDetailPrice;
    }

    public String getmImagId() {
        return mImagId;
    }

    public void setmImagId(String mImagId) {
        this.mImagId = mImagId;
    }

    public String getmDetialText() {
        return mDetialText;
    }

    public void setmDetialText(String mDetialText) {
        this.mDetialText = mDetialText;
    }

    public String getmDetailPrice() {
        return mDetailPrice;
    }

    public void setmDetailPrice(String mDetailPrice) {
        this.mDetailPrice = mDetailPrice;
    }
}
