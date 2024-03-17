package com.Part.Time;

import static maes.tech.intentanim.CustomIntent.customType;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    TextView version_txt;
    Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        version_txt = findViewById(R.id.version_txt);
        version_txt.setText("V" + BuildConfig.VERSION_NAME);

        InternetCheck();
    }

    private void InternetCheck() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    AppUpdate();
                } else {
                    final Dialog dialog = new Dialog(SplashActivity.this);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.all_dialog);
                    ImageView all_image = dialog.findViewById(R.id.all_image);
                    TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                    Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                    all_image.setImageResource(R.drawable.ic_online_education);
                    all_text.setText("Internet Connection Needed");
                    all_btn.setText("CONNECT");
                    all_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    });
                    dialog.show();
                }
            }
        }, 1500);
    }

    private void AppUpdate() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    LoginData();
                    StartAppSDK.init(SplashActivity.this, jsonObject.getString(constant.STARTAPP), false);
                    StartAppAd.disableSplash();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap();
                map.put(constant.APP_API_TAG, constant.APP_API);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void LoginData() {
        SharedPreferences sharedPreferences = getSharedPreferences("savedata", MODE_PRIVATE);
        String userid = sharedPreferences.getString("USERID", "0");
        if (userid.equals(String.valueOf(0))) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            customType(SplashActivity.this, getString(R.string.home_animation_type));
        } else {
            Loginvalue(userid);
        }
    }

    private void Loginvalue(final String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.CHECK_SPLASH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(constant.MESSAGE).equals("BLOCK")) {
                        final Dialog dialog = new Dialog(SplashActivity.this);
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
                    if (jsonObject.getString(constant.MESSAGE).equals("active")) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                        customType(SplashActivity.this, getString(R.string.home_animation_type));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put(constant.APP_API_TAG, constant.APP_API);
                map.put(constant.USERID_TAG, userid);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            AppUpdate();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}