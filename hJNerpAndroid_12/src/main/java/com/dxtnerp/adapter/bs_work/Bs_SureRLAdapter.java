package com.dxtnerp.adapter.bs_work;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dxtnerp.common.Constant;
import com.dxtnerp.R;
import com.dxtnerp.model.bs_work.TextModule;
import com.dxtnerp.model.bs_work.TextModule2;

import java.util.List;

/**
 * @author zhangdongdong
 * 订单数据选择适配器
 */
public class Bs_SureRLAdapter extends RecyclerView.Adapter<Bs_SureRLAdapter.ViewHolder> {

    private Context context;
    //数据类型
    private int intDataType;
    private List<TextModule> list;
    private List<TextModule2> list2;
    public static OnItemClickLitener onItemClickLitener;

    public static void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        Bs_SureRLAdapter.onItemClickLitener = onItemClickLitener;
    }

    public Bs_SureRLAdapter(Context context, int intDataType,
                            List<TextModule> list, List<TextModule2> list2) {
        this.context = context;
        this.intDataType = intDataType;
        switch (intDataType) {
            case Constant.OperationIDNumb2:
                this.list = list;
                break;
            case Constant.OperationIDNumb3:
                this.list2 = list2;
                break;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.module_layout_bs_sure_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        switch (intDataType) {
            case 2:
                viewHolder.bs_sure_item_tv.setText(list.get(position).getName());
                viewHolder.bs_sure_item_ck.setChecked(list.get(position).isCheck());
                break;
            case 3:
                viewHolder.bs_sure_item_tv.setText(list2.get(position).getName());
                viewHolder.bs_sure_item_ck.setChecked(list2.get(position).isCheck());
                break;
        }

        viewHolder.bs_sure_item_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intDataType == Constant.OperationIDNumb2)
                {
                    for (TextModule data : list) {
                        data.setCheck(false);
                    }
                    list.get(position).setCheck(true);
                }else if (intDataType == Constant.OperationIDNumb3)
                {
                    for (TextModule2 data : list2) {
                        data.setCheck(false);
                    }
                    list2.get(position).setCheck(true);
                }

                notifyDataSetChanged();
                if (onItemClickLitener != null)
                    onItemClickLitener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (intDataType == Constant.OperationIDNumb2)
        {
            return list.size();
        }else if (intDataType == Constant.OperationIDNumb3)
        {
            return list2.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout bs_sure_item_ll;
        CheckBox bs_sure_item_ck;
        TextView bs_sure_item_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            bs_sure_item_ll = (LinearLayout) itemView.findViewById(R.id.bs_sure_item_ll);
            bs_sure_item_ck = (CheckBox) itemView.findViewById(R.id.bs_sure_item_ck);
            bs_sure_item_tv = (TextView) itemView.findViewById(R.id.bs_sure_item_tv);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(int position);
    }

}
