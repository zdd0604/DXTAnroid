package com.dxtnerp.business.bs_work;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxtnerp.R;
import com.dxtnerp.common.ActivityBaseHeader;
import com.dxtnerp.common.Constant;
import com.dxtnerp.model.bs_work.ProductInfo;
import com.dxtnerp.util.date.DateUtil;
import com.dxtnerp.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhangdongdong
 * 销售订单新增
 */
public class SalesOrderAddActivity extends ActivityBaseHeader implements View.OnClickListener {

    //添加按钮
    @BindView(R.id.salesOrder_addBtn)
    Button salesOrder_addBtn;

    //产品列表
    @BindView(R.id.salesOrder_ll)
    LinearLayout salesOrder_ll;
    //订单日期
    @BindView(R.id.bs_SalesOrderAdd_date_opr)
    TextView bs_SalesOrderAdd_date_opr;
    //交货日期
    @BindView(R.id.bs_SalesOrderAdd_date_demand)
    TextView bs_SalesOrderAdd_date_demand;
    //订单类型
    @BindView(R.id.bs_SalesOrderAdd_title_id_ordtype)
    TextView bs_SalesOrderAdd_title_id_ordtype;
    @BindView(R.id.bs_SalesOrderAdd_id_ordtype)
    TextView bs_SalesOrderAdd_id_ordtype;
    //开票客户
    @BindView(R.id.bs_SalesOrderAdd_title_id_corr)
    TextView bs_SalesOrderAdd_title_id_corr;
    @BindView(R.id.bs_SalesOrderAdd_id_corr)
    TextView bs_SalesOrderAdd_id_corr;
    //送货地址
    @BindView(R.id.bs_SalesOrderAdd_var_addr)
    TextView bs_SalesOrderAdd_var_addr;
    //收货客户
    @BindView(R.id.bs_SalesOrderAdd_name_terminalr)
    TextView bs_SalesOrderAdd_name_terminalr;
    //录入人
    @BindView(R.id.bs_SalesOrderAdd_id_recorder)
    TextView bs_SalesOrderAdd_id_recorder;

    private LayoutInflater inflater;
    private List<View> productViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_activity_sales_order_add);

        ButterKnife.bind(this);

        initView();
    }


    private void initView() {
        setBase_rightTvType(View.VISIBLE);
        setBase_rightTv(getString(R.string.action_right_content_commit));

        inflater = LayoutInflater.from(this);

        bs_SalesOrderAdd_date_opr.setText(DateUtil.getTime(Constant.TIME_yyyy_MM_dd));
        bs_SalesOrderAdd_date_demand.setOnClickListener(this);
        bs_SalesOrderAdd_id_ordtype.setOnClickListener(this);
        bs_SalesOrderAdd_id_corr.setOnClickListener(this);
        salesOrder_addBtn.setOnClickListener(this);
    }

    @Override
    protected void onRightClick() {
        super.onRightClick();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bs_SalesOrderAdd_date_demand:
                showCalendar(bs_SalesOrderAdd_date_demand);
                break;
            case R.id.bs_SalesOrderAdd_id_ordtype:
                mBundle.putString(Constant.OperationName, getTvVaule(bs_SalesOrderAdd_title_id_ordtype));
                mBundle.putInt(Constant.OperationID, Constant.OperationIDNumb1);
                intentActivity(BillsSureActivity.class, mBundle, Constant.OperationIDNumb1);
                break;
            case R.id.bs_SalesOrderAdd_id_corr:
                mBundle.putString(Constant.OperationName, getTvVaule(bs_SalesOrderAdd_title_id_corr));
                mBundle.putInt(Constant.OperationID, Constant.OperationIDNumb10);
                intentActivity(OrderSearchActivity.class, mBundle, Constant.OperationIDNumb10);
                break;
            case R.id.salesOrder_addBtn:
                mBundle.putString(Constant.OperationName, "产品信息");
                mBundle.putInt(Constant.OperationID, Constant.OperationIDNumb11);
                intentActivity(OrderSearchActivity.class, mBundle, Constant.OperationIDNumb11);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //实体类需要更改
        if (requestCode == Constant.OperationIDNumb10 && resultCode == 100) {
            //开票客户
            bs_SalesOrderAdd_id_ordtype.setText(Constant.textModule.getName());
        } else if (requestCode == Constant.OperationIDNumb11 && resultCode == 100) {
            //产品信息
            addView();
        }
    }

    /**
     * 设置数据
     */
    private void addView() {

        productViewList = new ArrayList<>();

        clearOrderView();

        //删除
        TextView bs_sales_order_item_delete;
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

        for (int i = 0; i < Constant.productInfos.size(); i++) {
            final ProductInfo productInfo = Constant.productInfos.get(i);
            
            final View view = inflater.inflate(R.layout.module_layout_sales_order_item, null);
            bs_sales_order_item_delete = (TextView) view.findViewById(R.id.bs_sales_order_item_delete);

            bs_sales_order_item_title = (TextView) view.findViewById(R.id.bs_sales_order_item_title);
            bs_sales_order_item_id_uom = (TextView) view.findViewById(R.id.bs_sales_order_item_id_uom);
            bs_sales_order_item_id_item = (TextView) view.findViewById(R.id.bs_sales_order_item_id_item);
            bs_sales_order_item_id_prodarea = (TextView) view.findViewById(R.id.bs_sales_order_item_id_prodarea);
            bs_sales_order_item_dec_qty = (ClearEditText) view.findViewById(R.id.bs_sales_order_item_dec_qty);
            checkBox = (CheckBox) view.findViewById(R.id.bs_sales_order_item_check);
            bs_sales_order_item_delete.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.GONE);

            bs_sales_order_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletelLayoutView(view);
                }
            });

            bs_sales_order_item_title.setText(productInfo.getName_item());
            bs_sales_order_item_id_uom.setText(productInfo.getName_uom());
            bs_sales_order_item_id_item.setText(productInfo.getVar_spec());
            bs_sales_order_item_id_prodarea.setText(productInfo.getName_prodarea());
            if (Integer.valueOf(productInfo.getDec_qty()) > 0)
                bs_sales_order_item_dec_qty.setText(productInfo.getDec_qty());

            salesOrder_ll.addView(view);
            productViewList.add(view);

            bs_sales_order_item_dec_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s)) {
                        productInfo.setDec_qty("");
                    } else {
                        productInfo.setDec_qty(s.toString().trim());
                    }
                }
            });
        }
    }


    /**
     * 清楚原有布局的数据
     */
    private void clearOrderView() {
        salesOrder_ll.removeAllViews();
        productViewList.clear();
    }


    /**
     * 删除View
     *
     * @param view
     */
    private void deletelLayoutView(final View view) {
        final Dialog noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_withcancel);
        TextView dialog_cancel_rl, dialog_confirm_rl;
        TextView notice = (TextView) noticeDialog
                .findViewById(R.id.dialog_notice_tv);
        notice.setText("是否删除");
        dialog_cancel_rl = (TextView) noticeDialog.findViewById(R.id.dialog_cancel_tv);
        TextView dialog_cancel_tv = (TextView) noticeDialog.findViewById(R.id.dialog_cancel_tv);
        dialog_cancel_tv.setText("取消");
        dialog_confirm_rl = (TextView) noticeDialog.findViewById(R.id.dialog_confirm_tv);
        TextView dialog_confirm_tv = (TextView) noticeDialog.findViewById(R.id.dialog_confirm_tv);
        dialog_confirm_tv.setText("删除");
        dialog_cancel_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
            }
        });
        dialog_confirm_rl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
                salesOrder_ll.removeView(view);
                productViewList.remove(view);
            }
        });
        noticeDialog.show();
    }

}
