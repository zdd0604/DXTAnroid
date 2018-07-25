package com.dxtnerp.adapter.bs_work;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxtnerp.R;
import com.dxtnerp.common.ActionBarWidgetActivity;
import com.dxtnerp.common.Constant;
import com.dxtnerp.model.bs_work.ProductInfo;
import com.dxtnerp.util.StringUtil;
import com.dxtnerp.util.myscom.StringUtils;
import com.dxtnerp.widget.ClearEditText;

import java.util.List;


/**
 * 销售订单申请->产品信息查询
 */
public class OrderSearchAdapter extends RecyclerView.Adapter<OrderSearchAdapter.ViewHolder> {

    private Context context;
    //判断那个功能
    private int operationID;
    private List<ProductInfo> productInfoList;

    public static OrderSearchkListener orderSearchkListener;

    public static void setOrderSearchkListener(OrderSearchkListener orderSearchkListener) {
        OrderSearchAdapter.orderSearchkListener = orderSearchkListener;
    }

    public OrderSearchAdapter(Context context, int operationID, List<ProductInfo> productInfoList) {
        this.context = context;
        this.operationID = operationID;
        this.productInfoList = productInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.module_layout_sales_order_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.bs_sales_order_item_title.setText(productInfoList.get(position).getName_item());
        holder.bs_sales_order_item_id_uom.setText(productInfoList.get(position).getName_uom());
        holder.bs_sales_order_item_id_item.setText(productInfoList.get(position).getVar_spec());
        holder.bs_sales_order_item_id_prodarea.setText(productInfoList.get(position).getName_prodarea());

        holder.checkBox.setChecked(productInfoList.get(position).isCheck());
        holder.bs_sales_order_item_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderSearchkListener.onCheckedListener(position);
            }
        });


//        //判断是否有TextWatcher监听事件，有的话先移除
//        if (holder.bs_sales_order_item_dec_qty.getTag(R.id.bs_sales_order_item_dec_qty) instanceof TextWatcher) {
//            holder.bs_sales_order_item_dec_qty.removeTextChangedListener(((TextWatcher)
//                    holder.bs_sales_order_item_dec_qty.getTag(R.id.bs_sales_order_item_dec_qty)));
//        }

//        //移除了TextWatcher事件后设置item对应的文本
//        if (Integer.valueOf(productInfoList.get(position).getDec_qty()) > 0)
//            bs_sales_order_item_dec_qty.setText(productInfoList.get(position).getDec_qty());
//
//        TextWatcher textWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                productInfoList.get(position).setDec_qty(s.toString().trim());
//                orderSearchkListener.onEdInputListener(position,s.toString().trim());
//            }
//        };
//        //设置tag为TextWatcher
//        holder.bs_sales_order_item_dec_qty.addTextChangedListener(textWatcher);
//        holder.bs_sales_order_item_dec_qty.setTag(R.id.bs_sales_order_item_dec_qty, textWatcher);

    }

    @Override
    public int getItemCount() {
        return productInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout bs_sales_order_item_rl;
        //标题
        TextView bs_sales_order_item_title;
        //单位
        TextView bs_sales_order_item_id_uom;
        //规格
        TextView bs_sales_order_item_id_item;
        //产地
        TextView bs_sales_order_item_id_prodarea;
        //数量
        ClearEditText bs_sales_order_item_dec_qty;
        //复选框
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            bs_sales_order_item_rl = (RelativeLayout) itemView.findViewById(R.id.bs_sales_order_item_rl);
            bs_sales_order_item_title = (TextView) itemView.findViewById(R.id.bs_sales_order_item_title);
            bs_sales_order_item_id_uom = (TextView) itemView.findViewById(R.id.bs_sales_order_item_id_uom);
            bs_sales_order_item_id_item = (TextView) itemView.findViewById(R.id.bs_sales_order_item_id_item);
            bs_sales_order_item_id_prodarea = (TextView) itemView.findViewById(R.id.bs_sales_order_item_id_prodarea);
            bs_sales_order_item_dec_qty = (ClearEditText) itemView.findViewById(R.id.bs_sales_order_item_dec_qty);
            checkBox = (CheckBox) itemView.findViewById(R.id.bs_sales_order_item_check);
            bs_sales_order_item_dec_qty.setFocusable(false);
        }
    }


    public interface OrderSearchkListener {
        void onCheckedListener(int pos);

        void onEdInputListener(int pos, String content);
    }

}
