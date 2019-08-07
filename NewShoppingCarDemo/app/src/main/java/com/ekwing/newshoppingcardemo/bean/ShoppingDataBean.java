package com.ekwing.newshoppingcardemo.bean;

import java.util.List;

public class ShoppingDataBean implements Cloneable {

    private int code;
    private List<DataBean> mData;
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public List<DataBean> getmData() {
        return mData;
    }
    public void setmData(List<DataBean> mData) {
        this.mData = mData;
    }
    //**************************************************************************************

    public static class DataBean implements Cloneable{

        private String stroreId;//店铺的id
        private String storeName;//店铺的名字
        private boolean isSelect_shop;//店铺是否被选中
        private List<GoodsBean> goodsBeans;//店铺的商品

        public DataBean clone(){
            DataBean dataBean=null;
            try {
                dataBean= (DataBean) super.clone();
            }catch (Exception e){
                e.printStackTrace();
            }
            return dataBean;

        }
        public String getStroreId() {
            return stroreId;
        }

        public void setStroreId(String stroreId) {
            this.stroreId = stroreId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public boolean isSelect_shop() {
            return isSelect_shop;
        }

        public void setSelect_shop(boolean select_shop) {
            isSelect_shop = select_shop;
        }

        public List<GoodsBean> getGoodsBeans() {
            return goodsBeans;
        }

        public void setGoodsBeans(List<GoodsBean> goodsBeans) {
            this.goodsBeans = goodsBeans;
        }

        //#####################################################################################
        public static class  GoodsBean{

            private String goodsId;//商品的id
            private String goodsName;//商品的名字
            private String goodsImage;//商品的图片
            private String goodsNum;//商品数目
            private String goodsPrice;//商品价格
            private boolean isSelect;//商品是否被选中

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsImage() {
                return goodsImage;
            }

            public void setGoodsImage(String goodsImage) {
                this.goodsImage = goodsImage;
            }

            public String getGoodsNum() {
                return goodsNum;
            }

            public void setGoodsNum(String goodsNum) {
                this.goodsNum = goodsNum;
            }

            public String getGoodsPrice() {
                return goodsPrice;
            }

            public void setGoodsPrice(String goodsPrice) {
                this.goodsPrice = goodsPrice;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }


}
