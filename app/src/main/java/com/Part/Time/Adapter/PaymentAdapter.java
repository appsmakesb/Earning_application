package com.Part.Time.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Part.Time.Constant;
import com.Part.Time.Model.Payment;
import com.Part.Time.R;
import com.Part.Time.Framgent.Withdraw;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentHolder> {

    List<Payment> list;
    Context context;
    SharedPreferences sharedPreferences;
    String userid;
    Integer mini_amount;
    Constant constant;

    public PaymentAdapter(List<Payment> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.payment_item,parent,false);
       return new PaymentHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull PaymentHolder holder, int position) {
        Payment payment = list.get(position);
        holder.payment_text.setText(payment.getPayment_method());
        holder.payment_text_minimum.setText(payment.getPayment_minimum());
        Picasso.get().load(payment.getPayment_image()).placeholder(R.drawable.ic_pay).into(holder.payment_image);
        holder.payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAmount(holder.payment_text_minimum.getText().toString(),holder.payment_text.getText().toString(),payment.getPayment_image());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentHolder extends RecyclerView.ViewHolder {
        TextView payment_text,payment_text_minimum;
        CardView payment_btn;
        ImageView payment_image;
        public PaymentHolder(@NonNull View itemView) {
            super(itemView);
            payment_text = itemView.findViewById(R.id.payment_text);
            payment_btn = itemView.findViewById(R.id.payment_btn);
            payment_image = itemView.findViewById(R.id.payment_image);
            payment_text_minimum = itemView.findViewById(R.id.payment_text_minimum);
        }
    }

    private void GetAmount(String minimum,String method,String image){
        sharedPreferences = context.getSharedPreferences("savedata",Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("USERID",null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    mini_amount = Integer.parseInt(jsonObject.getString(constant.AMOUNT_TAG));
                    if (mini_amount>=Integer.parseInt(minimum)){
                        Bundle args = new Bundle();
                        args.putString("MINIMUM", minimum);
                        args.putString("USER_AMOUNT", String.valueOf(mini_amount));
                        args.putString("METHOD", method);
                        args.putString("IMAGE", image);
                        Withdraw bottomSheet = new Withdraw();
                        bottomSheet.setArguments(args);
                        bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(),bottomSheet.getTag());
                    }else {
                        final Dialog dialog = new Dialog(context);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.all_dialog);
                        ImageView all_image = dialog.findViewById(R.id.all_image);
                        TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                        Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                        all_image.setImageResource(R.drawable.ic_gift_box);
                        all_text.setText("Minimum Payment is "+minimum+" Point");
                        all_btn.setText("DISMISS");
                        all_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap();
                map.put(constant.APP_API_TAG,constant.APP_API);
                map.put(constant.USERID_TAG,userid);
                return map;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }
}
