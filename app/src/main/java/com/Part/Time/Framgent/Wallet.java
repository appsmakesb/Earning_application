package com.Part.Time.Framgent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Part.Time.Adapter.PaymentAdapter;
import com.Part.Time.Constant;
import com.Part.Time.Model.Payment;
import com.Part.Time.R;

import static android.content.Context.MODE_PRIVATE;

public class Wallet extends Fragment {


    RecyclerView recyclerView;
    PaymentAdapter paymentAdapter;
    List<Payment> list;
    Constant constant;
    String userid;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView history_show;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("savedata",MODE_PRIVATE);
        userid = sharedPreferences.getString("USERID","0");
        recyclerView = view.findViewById(R.id.notification_recycle);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        history_show = view.findViewById(R.id.history_show);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(list,getActivity());
        recyclerView.setAdapter(paymentAdapter);
        GetWithInfoData();
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list = new ArrayList<>();
                paymentAdapter = new PaymentAdapter(list,getActivity());
                recyclerView.setAdapter(paymentAdapter);
                GetWithInfoData();
            }
        });
        return view;
    }

    private void GetWithInfoData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.WALLET_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Payment notification = new Payment(
                                object.getString(constant.PAYMENT_IMAGE),
                                object.getString(constant.PAYMENT_MINIMUM),
                                object.getString(constant.PAYMENT_METHOD)
                        );
                        list.add(notification);
                        paymentAdapter = new PaymentAdapter(list, getActivity());
                        recyclerView.setAdapter(paymentAdapter);
                        swipeRefreshLayout.setRefreshing(false);
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
                Map<String,String> map = new HashMap<>();
                map.put(constant.APP_API_TAG,constant.APP_API);
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}