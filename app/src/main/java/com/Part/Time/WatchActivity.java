package com.Part.Time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinSdk;
import com.facebook.ads.*;
import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.unity3d.ads.UnityAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class WatchActivity extends AppCompatActivity {

    Button play_btn;
    Constant constant;
    int imp_count, admin_imp_count, bonus_amount, video_amount;
    private static final int COUNT_TIMER = 4;
    Handler handler = new Handler();
    Runnable runnable;
    int count_time;
    String userid;
    TextView count;
    Handler handler1 = new Handler();
    Runnable refresh;
    RelativeLayout task_tab;
    TextView timer;
    SimpleArcDialog mDialog;
    CardView click_tab;
    private InterstitialAd interstitialAd;
    private AppLovinInterstitialAdDialog adDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        SharedPreferences sharedPreferences = getSharedPreferences("savedata", MODE_PRIVATE);
        userid = sharedPreferences.getString("USERID", "0");
        findViewById(R.id.back_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                handler1.removeCallbacks(refresh);
                finish();
                customType(WatchActivity.this, getString(R.string.animation_type));
            }
        });
        VPNdata();
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setKeepScreenOn(true)
                .setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true)
                .setPrivacyConsentString(AdColonyAppOptions.GDPR, String.valueOf(1));
        AdColony.configure(this, appOptions, getString(R.string.APP_ID), getString(R.string.ZONE_IDS));
        AudienceNetworkAds.initialize(this);



        interstitialAd = new InterstitialAd(WatchActivity.this, constant.FACEBOOK_INTER_ADS_CODE);
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {
            }
            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
            }
            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {
            }
            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                interstitialAd.show();
            }
            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
            }
            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());





        mDialog = new SimpleArcDialog(WatchActivity.this);
        mDialog.setConfiguration(new ArcConfiguration(WatchActivity.this));
        mDialog.show();
        play_btn = findViewById(R.id.play_btn);
        count = findViewById(R.id.count);
        task_tab = findViewById(R.id.task_tab);
        timer = findViewById(R.id.timer);
        Timer();
        GetImpData();
        refresh = new Runnable() {
            public void run() {
                handler1.postDelayed(refresh, 1000);
                TimeGone();
            }
        };
        handler1.post(refresh);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_btn.setEnabled(false);
                count_time = COUNT_TIMER;
                if (imp_count >= admin_imp_count) {
                    TimeAdd();
                } else {
                    final Dialog dialog = new Dialog(WatchActivity.this);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.all_dialog);
                    ImageView all_image = dialog.findViewById(R.id.all_image);
                    TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                    Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                    all_image.setImageResource(R.drawable.ic_gift_box);
                    all_text.setText("Congratulation you win " + video_amount + " point !");
                    all_btn.setText("CLAIM");
                    all_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handler.post(runnable);
                            dialog.dismiss();
                            AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
                                @Override
                                public void onRequestFilled(AdColonyInterstitial ad) {
                                    ad.show();
                                }
                            };
                            AdColony.requestInterstitial(getString(R.string.ZONE_IDS), listener);
                            StartAppAd startAppAd = new StartAppAd(WatchActivity.this);
                            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
                            startAppAd.showAd();
                            adDialog = AppLovinInterstitialAd.create( AppLovinSdk.getInstance( WatchActivity.this ), WatchActivity.this );
                            adDialog.show();
                        }
                    });
                    dialog.show();
                }
            }
        });

        click_tab = findViewById(R.id.click_tab);
        CLickTAbData();
        findViewById(R.id.click_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(WatchActivity.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.all_dialog);
                ImageView all_image = dialog.findViewById(R.id.all_image);
                TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                all_image.setImageResource(R.drawable.ic_gift_box);
                all_text.setText("Compelete the Survey and earn "+bonus_amount+" Point. Without Compelete you can not earn point.");
                all_btn.setText("OKEY");
                all_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adDialog = AppLovinInterstitialAd.create( AppLovinSdk.getInstance( WatchActivity.this ), WatchActivity.this );
                        adDialog.show();
                        adDialog.setAdClickListener(new AppLovinAdClickListener() {
                            @Override
                            public void adClicked(AppLovinAd ad) {
                                DailyPointAddData(String.valueOf(bonus_amount));
                                dialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }

    private void CLickTAbData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(constant.VIDEO_CLICK_AMOUNT).equals("1")){
                        click_tab.setVisibility(View.GONE);
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
                Map<String, String> map = new HashMap();
                map.put(constant.APP_API_TAG, constant.APP_API);
                map.put(constant.USERID_TAG, userid);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void DailyPointAddData(final String amount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.BONUS_POINT_ADD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(constant.ERROR) == true) {
                        finish();
                        customType(WatchActivity.this, getString(R.string.animation_type));
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
                map.put(constant.AMOUNT_TAG, amount);
                map.put(constant.ACTION, "three");
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void Timer() {
        runnable = new Runnable() {
            public void run() {
                if (count_time == 0) {
                    play_btn.setEnabled(true);
                    play_btn.setText("PLAY");
                    GetImpPointAddData(String.valueOf(video_amount));
                } else {
                    handler.postDelayed(runnable, 1000);
                    count_time--;
                    play_btn.setText(String.valueOf("Wait " + count_time + " s"));
                }
            }
        };
    }

    private void VPNdata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.VPN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("countryCode").equals("BD")) {
                        final Dialog dialog = new Dialog(WatchActivity.this);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.all_dialog);
                        ImageView all_image = dialog.findViewById(R.id.all_image);
                        TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                        Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                        all_image.setImageResource(R.drawable.ic_online_education);
                        all_text.setText("You can not access this task from Bangladesh.try again later");
                        all_btn.setText("OKAY");
                        all_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                customType(WatchActivity.this, getString(R.string.animation_type));
                            }
                        });
                        dialog.show();
                    }else{
                        StartAppAd startAppAd = new StartAppAd(WatchActivity.this);
                        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
                        startAppAd.showAd();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void GetImpData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    imp_count = Integer.parseInt(jsonObject.getString(constant.VIDEO_IMP_COUNT_TAG));
                    admin_imp_count = Integer.parseInt(jsonObject.getString(constant.ADMIN_VIDEO_IMP_COUNT_TAG));
                    video_amount = Integer.parseInt(jsonObject.getString(constant.VIDEO_IMP_AMOUNT_TAG));
                    bonus_amount = Integer.parseInt(jsonObject.getString(constant.BONUS_AMOUNT_TAG));
                    count.setText(String.valueOf(imp_count + "/" + admin_imp_count));
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


    private void GetImpPointAddData(final String amount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.VIDEO_IMP_POINT_ADD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(constant.ERROR) == true) {
                        GetImpData();
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
                map.put(constant.AMOUNT_TAG, amount);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void TimeAdd() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.WATCH_TIME_ADD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(constant.ERROR) == true) {
                        TimeGone();
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
                Map<String, String> map = new HashMap<>();
                map.put(constant.USERID_TAG, userid);
                map.put(constant.APP_API_TAG, constant.APP_API);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void TimeGone() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.WATCH_TIME_GONE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(constant.ERROR) == true) {
                        timer.setVisibility(View.GONE);
                        task_tab.setVisibility(View.VISIBLE);
                        GetImpData();
                        handler1.removeCallbacks(refresh);
                        mDialog.dismiss();
                        OnlinedCode();
                    } else {
                        mDialog.dismiss();
                        timer.setText(jsonObject.getString(constant.MESSAGE));
                        timer.setVisibility(View.VISIBLE);
                        task_tab.setVisibility(View.GONE);
                        OnlinedCode();
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
                Map<String, String> map = new HashMap<>();
                map.put(constant.USERID_TAG, userid);
                map.put(constant.APP_API_TAG, constant.APP_API);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void OnlinedCode(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    UnityAds.initialize(WatchActivity.this, jsonObject.getString(constant.UNITY));
                    StartAppSDK.init(WatchActivity.this, jsonObject.getString(constant.STARTAPP), false);
                    StartAppAd.disableSplash();
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
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        handler1.removeCallbacks(refresh);
        finish();
        customType(WatchActivity.this, getString(R.string.animation_type));
    }
}