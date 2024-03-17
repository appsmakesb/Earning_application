package com.Part.Time;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import java.util.Random;

import static maes.tech.intentanim.CustomIntent.customType;

public class SignupActivity extends AppCompatActivity {

    EditText email,password,cpass,refer,username;
    Constant constant;
    Random random = new Random();
    int user_id = random.nextInt(999999);
    String imie;
    SimpleArcDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cpass = findViewById(R.id.c_password);
        refer = findViewById(R.id.refer_code);

        findViewById(R.id.signup_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imie = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                if (username.getText().toString().trim().isEmpty()){
                    username.setError("Please Enter Username");
                } else if (email.getText().toString().trim().isEmpty()){
                    email.setError("Please Enter Email");
                }else if (!email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    email.setError("Invalid Email Address");
                }else if (password.getText().toString().isEmpty()){
                    password.setError("Please Enter Password");
                }else if (password.getText().toString().length()!=6){
                    password.setError("Password must be Maximum 6 digit");
                }else if (cpass.getText().toString().isEmpty()){
                    cpass.setError("Please Confirm Password");
                }else if (!password.getText().toString().equals(cpass.getText().toString().trim())){
                    cpass.setError("Password Doesn't match");
                }else if (refer.getText().toString().isEmpty()){
                    refer.setError("Please Enter Refer Code");
                }else {
                    Signupvalue(username.getText().toString().trim(),email.getText().toString().trim(),password.getText().toString().trim(),refer.getText().toString().trim());
                    mDialog = new SimpleArcDialog(SignupActivity.this);
                    mDialog.setConfiguration(new ArcConfiguration(SignupActivity.this));
                    mDialog.show();
                }
            }
        });
        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
                customType(SignupActivity.this,getString(R.string.home_animation_type));
            }
        });
    }

    private void Signupvalue(final String name, final String email, final String password, final String refercode){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.USER_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(constant.ERROR)==true){
                        mDialog.dismiss();
                        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                        finish();
                        customType(SignupActivity.this,getString(R.string.home_animation_type));
                    }else {
                        mDialog.dismiss();
                        final Dialog dialog = new Dialog(SignupActivity.this);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.all_dialog);
                        ImageView all_image = dialog.findViewById(R.id.all_image);
                        TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                        Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                        all_image.setImageResource(R.drawable.tourist);
                        all_text.setText(jsonObject.getString(constant.MESSAGE));
                        all_btn.setText("OKAY");
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
                mDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap();
                map.put(constant.APP_API_TAG,constant.APP_API);
                map.put(constant.USERID_TAG,String.valueOf(user_id));
                map.put(constant.NAME_TAG,name);
                map.put(constant.APP_EMAIL,email);
                map.put(constant.APP_PASSWORD,password);
                map.put(constant.APP_REFER,refercode);
                map.put(constant.APP_IMIE,imie);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        finish();
        customType(SignupActivity.this,getString(R.string.home_animation_type));
    }
}