package com.lt.hm.wovideo.adapter.order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.BillList;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/7
 */
public class BillAdapter extends BaseQuickAdapter<BillList.OrderListBean>{
    OnPayBtnClick listener;
    onLongClick longListener;

    public void setLongListener(onLongClick longListener) {
        this.longListener = longListener;
    }

    public void setListener(OnPayBtnClick listener) {
        this.listener = listener;
    }

    public BillAdapter(Context context, List<BillList.OrderListBean> data) {
        super(context, R.layout.layout_order_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BillList.OrderListBean order) {
        if (order.getGoodsId().equals("953")){
            holder.setText(R.id.order_video_name,"0元免流量包");
        }else{
            holder.setText(R.id.order_video_name,order.getName());
        }
//        holder.setText(R.id.order_video_type,"类别"+order.getType());
//        holder.setText(R.id.order_video_price,"$"+order.getPrice()+"元");
//        if (order.get!=null){
//            holder.setImageUrl(R.id.order_video_img,order.getImg_url());
//        }else{
//            holder.setImageResource(R.id.order_video_img,R.drawable.img_1);
//        }
        if (order.getState().equals("1")){
            holder.setVisible(R.id.order_video_btn,true);
            holder.setVisible(R.id.order_video_text_status,false);
        }else if(order.getState().equals("2")){
            holder.setVisible(R.id.order_video_text_status,true);
            holder.setText(R.id.order_video_text_status,"已付款");
            holder.setVisible(R.id.order_video_btn,false);
        }

                holder.setText(R.id.order_video_price,"￥"+order.getMoneys()+"元");

        holder.setText(R.id.order_video_type,"订单号："+order.getOrderNo());
        Button imageView= (Button) holder.convertView.findViewById(R.id.order_video_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onPay(order);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final CharSequence[] items = {"copy"};

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle("copy the orderNo");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (longListener!=null){
                            longListener.onLongClick(order);
                        }
                    }
                });
                builder.show();
                return true;
            }
        });

    }

    public interface  onLongClick{
        void onLongClick(BillList.OrderListBean order);
    }

    public interface  OnPayBtnClick{
        void onPay(BillList.OrderListBean bean
        );
    }

}
