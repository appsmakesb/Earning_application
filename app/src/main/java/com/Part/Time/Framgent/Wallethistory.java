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

import com.Part.Time.Adapter.PaymentHistoryAdapter;
import com.Part.Time.Constant;
import com.Part.Time.Model.PaymentHistory;
import com.Part.Time.R;

import static android.content.Context.MODE_PRIVATE;


public class Wallethistory extends Fragment {

    RecyclerView HistoryrecyclerView;
    PaymentHistoryAdapter paymentHistoryAdapter;
    List<PaymentHistory> historyList;
    Constant constant;
    String userid;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView history_show;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallethistory, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("savedata",MODE_PRIVATE);
        userid = sharedPreferences.getString("USERID","0");
        HistoryrecyclerView = view.findViewById(R.id.notification_recycle_list);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        history_show = view.findViewById(R.id.history_show);
        HistoryrecyclerView.setHasFixedSize(true);
        HistoryrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyList = new ArrayList<>();
        paymentHistoryAdapter = new PaymentHistoryAdapter(historyList,getActivity());
        HistoryrecyclerView.setAdapter(paymentHistoryAdapter);
        GetWithHistoryData();
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                historyList = new ArrayList<>();
                paymentHistoryAdapter = new PaymentHistoryAdapter(historyList,getActivity());
                HistoryrecyclerView.setAdapter(paymentHistoryAdapter);
                GetWithHistoryData();
            }
        });
        return view;
    }

    private void GetWithHistoryData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.WALLET_LIST_HISTORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("total").equals("0")){
                        history_show.setVisibility(View.VISIBLE);
                        HistoryrecyclerView.setVisibility(View.GONE);
                    }else {
                        history_show.setVisibility(View.GONE);
                        HistoryrecyclerView.setVisibility(View.VISIBLE);
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        PaymentHistory paymentHistory = new PaymentHistory(
                                object.getString(constant.STATUS),
                                object.getString(constant.AMOUNT_TAG),
                                object.getString(constant.DATE),
                                object.getString(constant.USERID_TAG),
                                object.getString(constant.SLIDER_PHOTO_TAG)
                        );
                        historyList.add(paymentHistory);
                        paymentHistoryAdapter = new PaymentHistoryAdapter(historyList,getActivity());
                        HistoryrecyclerView.setAdapter(paymentHistoryAdapter);
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
                map.put(constant.USERID_TAG,userid);
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}