package com.Part.Time;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Constant constant;
    SimpleArcDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Please Enter Email");
                }else if (!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    email.setError("Invalid Email Address");
                }else if (password.getText().toString().isEmpty()){
                    password.setError("Please Enter Password");
                }else if (password.getText().toString().length()!=6){
                    password.setError("Password must be Maximum 6 digit");
                }else {
                    Loginvalue(email.getText().toString().trim(),password.getText().toString().trim());
                    mDialog = new SimpleArcDialog(LoginActivity.this);
                    mDialog.setConfiguration(new ArcConfiguration(LoginActivity.this));
                    mDialog.show();
                }
            }
        });
        findViewById(R.id.signup_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
                customType(LoginActivity.this,getString(R.string.home_animation_type));
            }
        });
    }

    private void Loginvalue(final String email, final String pass){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.CHECK_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(constant.MESSAGE).equals("not_access")){
                        mDialog.dismiss();
                        final Dialog dialog = new Dialog(LoginActivity.this);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.all_dialog);
                        ImageView all_image = dialog.findViewById(R.id.all_image);
                        TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                        Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                        all_image.setImageResource(R.drawable.ic_close);
                        all_text.setText("Email or password not match our database");
                        all_btn.setText("AGAIN");
                        all_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    if (jsonObject.getString(constant.MESSAGE).equals("BLOCK")){
                        mDialog.dismiss();
                        final Dialog dialog = new Dialog(LoginActivity.this);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.all_dialog);
                        ImageView all_image = dialog.findViewById(R.id.all_image);
                        TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                        Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                        all_image.setImageResource(R.drawable.ic_ban);
                        all_text.setText("You are blocked by admin");
                        all_btn.setText("EXIT");
                        all_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                System.exit(0);
                            }
                        });
                        dialog.show();
                    }
                    if (jsonObject.getString(constant.MESSAGE).equals("active")){
                        mDialog.dismiss();
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("savedata",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("USERID",String.valueOf(jsonObject.getString(constant.USERID_TAG)));
                        editor.putString("NAME",String.valueOf(jsonObject.getString(constant.NAME_TAG)));
                        editor.putString("EMAIL",String.valueOf(jsonObject.getString(constant.APP_EMAIL)));
                        editor.putString("REFER_CODE",String.valueOf(jsonObject.getString(constant.APP_REFER)));
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                        customType(LoginActivity.this,getString(R.string.home_animation_type));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap();
                map.put(constant.APP_API_TAG,constant.APP_API);
                map.put(constant.APP_EMAIL,email);
                map.put(constant.APP_PASSWORD,pass);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}