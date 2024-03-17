package com.Part.Time.Framgent;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.Part.Time.Constant;
import com.Part.Time.R;


public class Withdraw extends DialogFragment {

    EditText editbox_point,editbox_account;
    Button nxt_btn,close_btn;
    Integer minimum_amount,user_amount;
    Constant constant;
    TextView show_txt,youramount_txt,minimum_txt,method_txt;
    String userid,method,image_s,refer_code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("savedata",MODE_PRIVATE);
        userid = sharedPreferences.getString("USERID","0");
        refer_code = sharedPreferences.getString("REFER_CODE","0");
        String name = sharedPreferences.getString("NAME","0");
        String email = sharedPreferences.getString("EMAIL","0");
        Bundle mArgs = getArguments();
        minimum_amount = Integer.parseInt(mArgs.getString("MINIMUM"));
        method = mArgs.getString("METHOD");
        image_s = mArgs.getString("IMAGE");
        user_amount = Integer.parseInt(mArgs.getString("USER_AMOUNT"));
        nxt_btn = view.findViewById(R.id.nxt_btn);
        close_btn = view.findViewById(R.id.close_btn);
        editbox_point =  view.findViewById(R.id.editbox_point);
        editbox_account =  view.findViewById(R.id.editbox_account);

        youramount_txt =  view.findViewById(R.id.youramount_txt);
        minimum_txt =  view.findViewById(R.id.minimum_txt);
        method_txt =  view.findViewById(R.id.method_txt);
        youramount_txt.setText(String.valueOf("Your amount : "+user_amount));
        method_txt.setText(String.valueOf("Selected method : "+method));
        minimum_txt.setText(String.valueOf("Minimum amount : "+minimum_amount));

        show_txt =  view.findViewById(R.id.show_txt);
        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nxt_btn.setVisibility(View.GONE);
                if (editbox_account.getText().toString().isEmpty()){
                    editbox_account.setError("Enter Account Address");
                }else if (editbox_point.getText().toString().isEmpty()){
                    editbox_point.setError("Enter Amount here");
                }else {
                    Integer amount_box_int =  Integer.parseInt(editbox_point.getText().toString());
                    if (user_amount>=amount_box_int){
                        if (amount_box_int>=minimum_amount){
                            RedeemAddAmount(name,email,editbox_account.getText().toString(),image_s);
                        }
                    }else{
                        show_txt.setVisibility(View.VISIBLE);
                        show_txt.setTextColor(getResources().getColor(R.color.red));
                        show_txt.setText("Invalid Amount");
                        close_btn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        close_btn.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }
    private void RedeemAddAmount(final String name, final String email, final String num,final String image){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,constant.REDEEM_AMOUNT_INSERT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(constant.ERROR)==true){
                        show_txt.setVisibility(View.VISIBLE);
                        show_txt.setTextColor(getResources().getColor(R.color.green));
                        show_txt.setText(jsonObject.getString(constant.MESSAGE));
                        close_btn.setVisibility(View.VISIBLE);
                    }else{
                        show_txt.setVisibility(View.VISIBLE);
                        show_txt.setTextColor(getResources().getColor(R.color.red));
                        show_txt.setText(jsonObject.getString(constant.MESSAGE));
                        close_btn.setVisibility(View.VISIBLE);
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
                map.put(constant.USERID_TAG,userid);
                map.put(constant.NAME_TAG,name);
                map.put(constant.NUMBER_TAG,num);
                map.put(constant.SLIDER_PHOTO_TAG,image);
                map.put(constant.APP_EMAIL,email);
                map.put(constant.METHOD_TAG,method);
                map.put(constant.APP_REFER,refer_code);
                map.put(constant.AMOUNT_TAG,String.valueOf(editbox_point.getText().toString()));
                map.put(constant.APP_API_TAG,constant.APP_API);
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}