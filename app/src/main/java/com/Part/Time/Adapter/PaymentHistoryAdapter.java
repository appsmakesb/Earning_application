package com.Part.Time.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.Part.Time.Model.PaymentHistory;
import com.Part.Time.R;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHolder> {

    List<PaymentHistory> list;
    Context context;

    public PaymentHistoryAdapter(List<PaymentHistory> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.payment_item_history,parent,false);
       return new PaymentHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull PaymentHolder holder, int position) {
        PaymentHistory paymentHistory = list.get(position);
        holder.amount.setText(paymentHistory.getPayment_amount());
        holder.date.setText(paymentHistory.getPayment_date());
        holder.userid.setText(paymentHistory.getPayment_userid());
        Picasso.get().load(paymentHistory.getIcon_image()).placeholder(R.drawable.ic_pay).into(holder.imageView);
        if (paymentHistory.getPayment_pending().equals("PENDING")){
           holder.pending.setText(paymentHistory.getPayment_pending());
           holder.pending.setBackground(context.getDrawable(android.R.color.holo_red_dark));
        }else if (paymentHistory.getPayment_pending().equals("REJECT")){
            holder.pending.setText(paymentHistory.getPayment_pending());
            holder.pending.setBackground(context.getDrawable(android.R.color.holo_red_dark));
        }else{
            holder.pending.setText(paymentHistory.getPayment_pending());
            holder.pending.setBackground(context.getDrawable(android.R.color.holo_green_dark));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentHolder extends RecyclerView.ViewHolder {
        TextView pending,amount,date,userid;
        ImageView imageView;
        public PaymentHolder(@NonNull View itemView) {
            super(itemView);
            pending = itemView.findViewById(R.id.payment_pending);
            amount = itemView.findViewById(R.id.payment_amount);
            date = itemView.findViewById(R.id.payment_date);
            userid = itemView.findViewById(R.id.payment_userid);
            imageView = itemView.findViewById(R.id.payment_imageview);
        }
    }
}
