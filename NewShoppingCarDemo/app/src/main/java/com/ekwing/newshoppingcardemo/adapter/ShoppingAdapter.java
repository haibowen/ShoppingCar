package com.ekwing.newshoppingcardemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekwing.newshoppingcardemo.R;
import com.ekwing.newshoppingcardemo.bean.ShoppingDataBean;
import com.ekwing.newshoppingcardemo.utils.ToastUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingAdapter extends BaseExpandableListAdapter {
    private Context mContext;//上下文
    private ImageView iv_all_selet;//全选
    private ImageView iv_select_all;//全选
    private Button bt_order;//结算
    private Button bt_delete;//删除
    private RelativeLayout rl_total_price;//所有钱
    private TextView tv_toal_price;//所有钱
    private List<ShoppingDataBean.DataBean> mData;//店铺信息
    private boolean isSelectAll = false;//
    private double total_price;//总钱数
    private OnDeleteListener mDeleteListener;//商品删除的接口
    private ChangeCountListener mChangeCountListener;//商品数量改变的接口

    /**
     * 构造方法
     *
     * @param mContext
     * @param
     * @param iv_select_all
     * @param bt_order
     * @param bt_delete
     * @param tv_toal_price
     * @param
     */

    public ShoppingAdapter(Context mContext, ImageView iv_all_selet, ImageView iv_select_all, Button bt_order, Button bt_delete, TextView tv_toal_price) {
        this.mContext = mContext;
        this.iv_all_selet = iv_all_selet;
        this.iv_select_all = iv_select_all;
        this.bt_order = bt_order;
        this.bt_delete = bt_delete;
        this.tv_toal_price = tv_toal_price;

    }

    /**
     * 自定义设置数据方法
     * 并刷新数据
     *
     * @return
     */
    public void setmData(List<ShoppingDataBean.DataBean> data) {
        this.mData = data;
        notifyDataSetChanged();

    }

    /**
     * 统计店铺的量
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        if (mData != null && mData.size() > 0) {
            return mData.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getGroup(int i) {
        return mData.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final GroupViewHolder groupViewHolder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.el_item_group, null);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        //设置店铺的名字
        final ShoppingDataBean.DataBean mDataBean = setShopName(i, groupViewHolder);
        //店铺内的所有商品都被选中的时候，店铺也被选中
        selectShop(mDataBean);
        //set之后重新get 选中的状态
        final boolean isSelecShop = getShopState(groupViewHolder, mDataBean);
        //店铺选择框的点击事件
        shopOnListener(groupViewHolder, mDataBean, isSelecShop);
        //当所有商品被选中的时候，全选按钮也被选中
        selectAllGoods();
        //底部全选的点击事件
        selectAllBotome();
        //合计的计算
        toCountPrice();
        //去结算的点击事件
        toJieSuan();
        //删除商品
        toDelete();
        return view;
    }

    private void toDelete() {
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteListener != null) {
                    mDeleteListener.onDelete();
                }
            }
        });
    }

    private void toJieSuan() {
        bt_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ShoppingDataBean.DataBean.GoodsBean> temp = new ArrayList<>();
                for (int j = 0; j < mData.size(); j++) {
                    List<ShoppingDataBean.DataBean.GoodsBean> goodsBeans = mData.get(j).getGoodsBeans();
                    for (int k = 0; k < goodsBeans.size(); k++) {
                        ShoppingDataBean.DataBean.GoodsBean goodsBean = goodsBeans.get(k);
                        boolean isSelect = goodsBean.isSelect();
                        if (isSelect) {
                            temp.add(goodsBean);
                        }
                    }

                }
                if (temp != null && temp.size() > 0) {
                    ToastUtil.makeText(mContext, "跳转到结算界面");

                } else {
                    ToastUtil.makeText(mContext, "请加入商品");
                }
            }
        });
    }

    private void toCountPrice() {
        total_price = 0.0;
        tv_toal_price.setText("$0.00");
        for (int j = 0; j < mData.size(); j++) {

            List<ShoppingDataBean.DataBean.GoodsBean> goodsBeans = mData.get(j).getGoodsBeans();
            for (int k = 0; k < goodsBeans.size(); k++) {

                ShoppingDataBean.DataBean.GoodsBean goodsBean = goodsBeans.get(k);
                boolean isSelect = goodsBean.isSelect();
                if (isSelect) {
                    String num = goodsBean.getGoodsNum();
                    String price = goodsBean.getGoodsPrice();
                    if (num != "" && price != null) {
                        double v = Double.parseDouble(num);///查一下相关的使用   Caused by: java.lang.NumberFormatException: empty String
                        double v1 = Double.parseDouble(price);//
                        total_price += v * v1;
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        tv_toal_price.setText("¥" + decimalFormat.format(total_price));
                    }

                }
            }
        }
    }

    private void selectAllBotome() {
        iv_all_selet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelectAll = !isSelectAll;
                if (isSelectAll) {
                    setGoodsSelect(isSelectAll);

                } else {

                    setGoodsSelect(isSelectAll);
                }
                notifyDataSetChanged();
            }

        });
    }

    private void setGoodsSelect(boolean b) {
        for (int j = 0; j < mData.size(); j++) {
            List<ShoppingDataBean.DataBean.GoodsBean> goodsBeans = mData.get(j).getGoodsBeans();

            for (int k = 0; k < goodsBeans.size(); k++) {
                ShoppingDataBean.DataBean.GoodsBean goodsBean = goodsBeans.get(k);
                goodsBean.setSelect(b);

            }

        }
    }

    private void selectAllGoods() {
        for (int k = 0; k < mData.size(); k++) {
            List<ShoppingDataBean.DataBean.GoodsBean> goods = mData.get(k).getGoodsBeans();
            for (int y = 0; y < goods.size(); y++) {
                ShoppingDataBean.DataBean.GoodsBean goodsBean = goods.get(y);
                boolean isSelect = goodsBean.isSelect();
                if (isSelect) {
                    isSelectAll = true;
                } else {
                    isSelectAll = false;
                    break;//根据标记，跳出嵌套循环
                }
            }
        }
        if (isSelectAll) {
            iv_select_all.setBackgroundResource(R.mipmap.select);
        } else {
            iv_select_all.setBackgroundResource(R.mipmap.unselect);
        }
    }

    private void shopOnListener(GroupViewHolder groupViewHolder, final ShoppingDataBean.DataBean mDataBean, final boolean isSelecShop) {
        groupViewHolder.ll_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataBean.setSelect_shop(!isSelecShop);
                List<ShoppingDataBean.DataBean.GoodsBean> mGoodsData = mDataBean.getGoodsBeans();
                for (int j = 0; j < mGoodsData.size(); j++) {
                    ShoppingDataBean.DataBean.GoodsBean goodsBean = mGoodsData.get(j);
                    goodsBean.setSelect(!isSelecShop);


                }
                notifyDataSetChanged();
            }
        });
    }

    private boolean getShopState(GroupViewHolder groupViewHolder, ShoppingDataBean.DataBean mDataBean) {
        final boolean isSelecShop = mDataBean.isSelect_shop();
        if (isSelecShop) {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.select);

        } else {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.unselect);

        }
        return isSelecShop;
    }

    /**
     * 所有商品被选中，店铺也被选中
     *
     * @param mDataBean
     */
    private void selectShop(ShoppingDataBean.DataBean mDataBean) {
        for (int j = 0; j < mDataBean.getGoodsBeans().size(); j++) {
            ShoppingDataBean.DataBean.GoodsBean mGoodsBean = mDataBean.getGoodsBeans().get(j);
            boolean isSelect = mGoodsBean.isSelect();
            if (isSelect) {
                mDataBean.setSelect_shop(true);
            } else {
                mDataBean.setSelect_shop(false);
                break;

            }

        }
    }

    /**
     * 设置店铺的名字
     *
     * @param i
     * @param groupViewHolder
     * @return
     */
    private ShoppingDataBean.DataBean setShopName(int i, GroupViewHolder groupViewHolder) {
        final ShoppingDataBean.DataBean mDataBean = mData.get(i);
        String store_id = mDataBean.getStroreId();
        String store_name = mDataBean.getStoreName();
        if (store_name != null) {
            groupViewHolder.tv_store_name.setText(store_name);
        } else {
            groupViewHolder.tv_store_name.setText("");
        }
        return mDataBean;
    }

    public void setOnDeleteListener(OnDeleteListener deleteListener) {

        mDeleteListener = deleteListener;
    }

    //删除的回调
    public interface OnDeleteListener {
        void onDelete();
    }

    static class GroupViewHolder {
        ImageView ivSelect;
        TextView tv_store_name;
        LinearLayout ll_group;

        GroupViewHolder(View view) {
            ivSelect = view.findViewById(R.id.iv_select);
            tv_store_name = view.findViewById(R.id.tv_store_name);
            ll_group = view.findViewById(R.id.ll);
        }
    }
    //*************************************************************************************8

    /**
     * 统计每个店铺的货物量
     *
     * @param i
     * @return
     */

    @Override
    public int getChildrenCount(int i) {
        if (mData.get(i).getGoodsBeans() != null && mData.get(i).getGoodsBeans().size() > 0) {
            return mData.get(i).getGoodsBeans().size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getChild(int i, int i1) {
        return mData.get(i).getGoodsBeans().get(i1);
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildeViewHolder childeViewHolder;
        if (view == null) {

            view = View.inflate(mContext, R.layout.el_item_child, null);
            childeViewHolder = new ChildeViewHolder(view);
            view.setTag(childeViewHolder);

        } else {

            childeViewHolder = (ChildeViewHolder) view.getTag();

        }
        final ShoppingDataBean.DataBean datasBean = mData.get(i);
        //店铺ID
        String store_id = datasBean.getStroreId();
        //店铺名称
        String store_name = datasBean.getStoreName();
        //店铺是否在购物车中被选中
        final boolean isSelect_shop = datasBean.isSelect_shop();
        final ShoppingDataBean.DataBean.GoodsBean goodsBean = datasBean.getGoodsBeans().get(i1);
        //商品图片
        String goods_image = goodsBean.getGoodsImage();
        //商品ID
        final String goods_id = goodsBean.getGoodsId();
        //商品名称
        String goods_name = goodsBean.getGoodsName();
        //商品价格
        String goods_price = goodsBean.getGoodsPrice();
        //商品数量
        String goods_num = goodsBean.getGoodsNum();
        //商品的库存
        String goods_num_all = goodsBean.getGoodsAllNum();
        //商品是否被选中
        final boolean isSelect = goodsBean.isSelect();

        Glide.with(mContext)
                .load(goods_image)
                .into(childeViewHolder.iv_detail);
        //商品的初始化
        initChildView(childeViewHolder, goods_name, goods_price, goods_num, goods_num_all);

        //商品是否被选中
        if (isSelect) {
            childeViewHolder.iv_select_child.setImageResource(R.mipmap.select);
        } else {
            childeViewHolder.iv_select_child.setImageResource(R.mipmap.unselect);
        }
        //商品选择框的点击事件
        goodsToSelect(childeViewHolder, datasBean, goodsBean, isSelect);
        //商品整体的点击事件
        LongClickListener(childeViewHolder);

        //加号的点击事件
        addOnListener(childeViewHolder, goodsBean, goods_id);
        //减号的点击事件
        subOnListener(childeViewHolder, goodsBean, goods_id);
        return view;
    }

    private void LongClickListener(ChildeViewHolder childeViewHolder) {
        childeViewHolder.ll_shopping_detail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ToastUtil.makeText(mContext, "我是长按事件，具体待实现的逻辑稍后添加");
                return false;
            }
        });
    }

    private void subOnListener(ChildeViewHolder childeViewHolder, final ShoppingDataBean.DataBean.GoodsBean goodsBean, final String goods_id) {
        childeViewHolder.iv_substract_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟减号操作
                String num = goodsBean.getGoodsNum();
                Integer integer = Integer.valueOf(num);
                if (integer > 1) {
                    integer--;
                    goodsBean.setGoodsNum(integer + "");
                    /**
                     /**
                     * 实际开发中，通过回调请求后台接口实现数量的加减
                     */
                    if (mChangeCountListener != null) {
                        mChangeCountListener.changeCount(goods_id);
                    }
                } else {
                    ToastUtil.makeText(mContext, "商品不能再减少了");
                }
                notifyDataSetChanged();
            }
        });
    }

    private void addOnListener(ChildeViewHolder childeViewHolder, final ShoppingDataBean.DataBean.GoodsBean goodsBean, final String goods_id) {
        childeViewHolder.iv_add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟加号操作
                String num = goodsBean.getGoodsNum();
                Integer integer = Integer.valueOf(num);
                integer++;
                goodsBean.setGoodsNum(integer + "");
                notifyDataSetChanged();


                /**
                 * 实际开发中，通过回调请求后台接口实现数量的加减
                 */
                if (mChangeCountListener != null) {
                    mChangeCountListener.changeCount(goods_id);
                }
            }
        });
    }

    private void goodsToSelect(ChildeViewHolder childeViewHolder, final ShoppingDataBean.DataBean datasBean, final ShoppingDataBean.DataBean.GoodsBean goodsBean, final boolean isSelect) {
        //商品选择框的点击事件
        childeViewHolder.iv_select_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsBean.setSelect(!isSelect);
                if (!isSelect == false) {
                    datasBean.setSelect_shop(false);
                }
                notifyDataSetChanged();
            }
        });
    }

    private void initChildView(ChildeViewHolder childeViewHolder, String goods_name, String goods_price, String goods_num, String goods_num_all) {
        if (goods_name != null) {
            childeViewHolder.tv_detail_content.setText(goods_name);
        } else {
            childeViewHolder.tv_detail_content.setText("");
        }
        if (goods_price != null) {
            childeViewHolder.tv_price.setText(goods_price);
        } else {
            childeViewHolder.tv_price.setText("");
        }
        if (goods_num != null) {
            childeViewHolder.tv_num_detail.setText(goods_num);
        } else {
            childeViewHolder.tv_num_detail.setText("");

        }
        if (goods_num.equals("0")) {
            childeViewHolder.tv_detail_content.setText("失效宝贝");
            childeViewHolder.ll_shopping_detail.setBackgroundResource(R.color.colorHide);
            childeViewHolder.iv_select_child.setVisibility(View.INVISIBLE);

        }else {
            childeViewHolder.iv_select_child.setVisibility(View.VISIBLE);
        }

    }

    static class ChildeViewHolder {
        ImageView iv_select_child;//选中
        ImageView iv_detail;//商品图片
        TextView tv_detail_content;//商品描述
        TextView tv_price;//商品价格
        TextView tv_num_detail;//商品数目
        ImageView iv_substract_child;//减少商品数量
        ImageView iv_add_child;//增加商品数量
        LinearLayout ll_shopping_detail;//商品框

        public ChildeViewHolder(View view) {
            iv_select_child = view.findViewById(R.id.iv_select_child);
            iv_detail = view.findViewById(R.id.iv_detail);
            tv_detail_content = view.findViewById(R.id.tv_detail_content);
            tv_price = view.findViewById(R.id.tv_price_child);
            tv_num_detail = view.findViewById(R.id.tv_num_detail);
            iv_substract_child = view.findViewById(R.id.iv_subtract_child);
            iv_add_child = view.findViewById(R.id.iv_add_child);
            ll_shopping_detail = view.findViewById(R.id.ll_shopping_detail);
        }
    }

    public interface ChangeCountListener {
        void changeCount(String goodsId);
    }

    public void setChangeCountListener(ChangeCountListener changeCountListener) {
        mChangeCountListener = changeCountListener;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
