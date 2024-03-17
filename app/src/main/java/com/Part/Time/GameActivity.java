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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcolony.sdk.*;
import com.adcolony.sdk.AdColony;
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
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.unity3d.ads.UnityAds;

import com.facebook.ads.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static maes.tech.intentanim.CustomIntent.customType;

public class GameActivity extends AppCompatActivity {

    Handler Gamehandler = new Handler();
    Runnable Gamerunnable;
    Random Gamerandom = new Random();
    int random_play, random_int, fixed_int;
    TextView p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, count;
    Button start;
    Random random = new Random();
    int count_time, bonus_amount;
    Handler handler = new Handler();
    Runnable runnable;
    String userid;
    int imp_count, admin_imp_count;
    Constant constant;
    private static final int COUNT_TIMER = 4;
    Handler handler1 = new Handler();
    Runnable refresh;
    SimpleArcDialog mDialog;
    TextView timer;
    LinearLayout task_tab;
    String unity;
    CardView click_tab;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViewById(R.id.back_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                customType(GameActivity.this, getString(R.string.animation_type));
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("savedata", MODE_PRIVATE);
        userid = sharedPreferences.getString("USERID", "0");
        mDialog = new SimpleArcDialog(this);
        mDialog.setConfiguration(new ArcConfiguration(this));
        mDialog.show();
        start = findViewById(R.id.start);
        count = findViewById(R.id.count);
        timer = findViewById(R.id.timer);
        task_tab = findViewById(R.id.task_tab);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);
        p5 = findViewById(R.id.p5);
        p6 = findViewById(R.id.p6);
        p7 = findViewById(R.id.p7);
        p8 = findViewById(R.id.p8);
        p9 = findViewById(R.id.p9);
        p10 = findViewById(R.id.p10);
        p11 = findViewById(R.id.p11);
        p12 = findViewById(R.id.p12);
        p13 = findViewById(R.id.p13);
        p14 = findViewById(R.id.p14);
        p15 = findViewById(R.id.p15);
        Timer();
        PlayGames();

        VPNdata();
        AudienceNetworkAds.initialize(this);
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setKeepScreenOn(true)
                .setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true)
                .setPrivacyConsentString(AdColonyAppOptions.GDPR, String.valueOf(1));
        AdColony.configure(this, appOptions, getString(R.string.APP_ID), getString(R.string.ZONE_IDS));



        interstitialAd = new InterstitialAd(GameActivity.this, constant.FACEBOOK_INTER_ADS_CODE);
                        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                            }
                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                            }
                            @Override
                            public void onError(Ad ad, AdError adError) {
                            }
                            @Override
                            public void onAdLoaded(Ad ad) {
                                interstitialAd.show();
                            }
                            @Override
                            public void onAdClicked(Ad ad) {
                            }
                            @Override
                            public void onLoggingImpression(Ad ad) {
                            }
                        };
                        interstitialAd.loadAd(
                                interstitialAd.buildLoadAdConfig()
                                        .withAdListener(interstitialAdListener)
                                        .build());



        GetImpData();
        refresh = new Runnable() {
            public void run() {
                handler1.postDelayed(refresh, 1000);
                TimeGone();
            }
        };
        handler1.post(refresh);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(false);
                count_time = COUNT_TIMER;
                if (imp_count >= admin_imp_count) {
                    TimeAdd();
                } else {
                    Gamehandler.post(Gamerunnable);
                    random_play = Gamerandom.nextInt(15 - 1) + 1;
                    StartAppAd startAppAd = new StartAppAd(GameActivity.this);
                    startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
                    startAppAd.showAd();
                    AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
                        @Override
                        public void onRequestFilled(AdColonyInterstitial ad) {
                            ad.show();
                        }
                    };
                    AdColony.requestInterstitial(getString(R.string.ZONE_IDS), listener);
                }
            }
        });


        click_tab = findViewById(R.id.click_tab);
        CLickTAbData();
        findViewById(R.id.click_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(GameActivity.this);
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
                        StartAppAd startAppAd = new StartAppAd(GameActivity.this);
                        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
                        startAppAd.showAd(new AdDisplayListener() {
                            @Override
                            public void adHidden(com.startapp.sdk.adsbase.Ad ad) {

                            }
                            @Override
                            public void adDisplayed(com.startapp.sdk.adsbase.Ad ad) {

                            }
                            @Override
                            public void adClicked(com.startapp.sdk.adsbase.Ad ad) {
                                DailyPointAddData(String.valueOf(bonus_amount));
                                dialog.dismiss();
                                finish();
                            }
                            @Override
                            public void adNotDisplayed(com.startapp.sdk.adsbase.Ad ad) {

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
                    if (jsonObject.getString(constant.GAME_CLICK_AMOUNT).equals("1")){
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
                        customType(GameActivity.this, getString(R.string.animation_type));
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
                map.put(constant.ACTION, "one");
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void Timer() {
        runnable = new Runnable() {
            public void run() {
                if (count_time == 0) {
                    start.setEnabled(true);
                    start.setText("PLAY GAME");
                    GetImpPointAddData(String.valueOf(random_int));
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else {
                    handler.postDelayed(runnable, 1000);
                    count_time--;
                    start.setText(String.valueOf("Wait " + count_time + " s"));
                }
            }
        };
    }

    private void GetImpData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    imp_count = Integer.parseInt(jsonObject.getString(constant.MATH_IMP_COUNT_TAG));
                    admin_imp_count = Integer.parseInt(jsonObject.getString(constant.ADMIN_MATH_IMP_COUNT_TAG));
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.MATH_IMP_POINT_ADD_URL, new Response.Listener<String>() {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.MATH_TIME_ADD_URL, new Response.Listener<String>() {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.MATH_TIME_GONE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(constant.ERROR) == true) {
                        mDialog.dismiss();
                        timer.setVisibility(View.GONE);
                        task_tab.setVisibility(View.VISIBLE);
                        GetImpData();
                        handler1.removeCallbacks(refresh);
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

    private void VPNdata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.VPN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("countryCode").equals("BD")) {
                        final Dialog dialog = new Dialog(GameActivity.this);
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
                                customType(GameActivity.this, getString(R.string.animation_type));
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
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void PlayGames() {
        Gamerunnable = new Runnable() {
            @Override
            public void run() {
                if (random_int == random_play) {
                    Gamehandler.removeCallbacks(Gamerunnable);
                    final Dialog dialog = new Dialog(GameActivity.this);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.all_dialog);
                    ImageView all_image = dialog.findViewById(R.id.all_image);
                    TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                    Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                    all_image.setImageResource(R.drawable.ic_gift_box);
                    all_text.setText("Congratulation you win " + random_int + " point !");
                    all_btn.setText("CLAIM");
                    all_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handler.post(runnable);
                            dialog.dismiss();
                            StartAppAd startAppAd = new StartAppAd(GameActivity.this);
                    		startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
                    		startAppAd.showAd();
                            if (UnityAds.isReady()) {
                                UnityAds.show(GameActivity.this);
                            } else {
                                UnityAds.initialize(GameActivity.this, unity);
                            }

                        }
                    });
                    dialog.show();
                } else {
                    Gamehandler.postDelayed(Gamerunnable, 100);
                    fixed_int++;
                }
                if (fixed_int == 1) {
                    random_int = 15;
                    p15.setBackgroundResource(R.drawable.game_deep_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 2) {
                    random_int = 14;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_deep_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 3) {
                    random_int = 13;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_deep_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 4) {
                    random_int = 12;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_deep_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 5) {
                    random_int = 11;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_deep_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 6) {
                    random_int = 10;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_deep_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 7) {
                    random_int = 9;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_deep_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 8) {
                    random_int = 8;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_deep_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 9) {
                    random_int = 7;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_deep_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 10) {
                    random_int = 6;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_deep_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 11) {
                    random_int = 5;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_deep_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 12) {
                    random_int = 4;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_deep_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 13) {
                    random_int = 3;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_deep_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 14) {
                    random_int = 2;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_deep_bg);
                    p1.setBackgroundResource(R.drawable.game_bg);
                } else if (fixed_int == 15) {
                    random_int = 1;
                    p15.setBackgroundResource(R.drawable.game_bg);
                    p14.setBackgroundResource(R.drawable.game_bg);
                    p13.setBackgroundResource(R.drawable.game_bg);
                    p12.setBackgroundResource(R.drawable.game_bg);
                    p11.setBackgroundResource(R.drawable.game_bg);
                    p10.setBackgroundResource(R.drawable.game_bg);
                    p9.setBackgroundResource(R.drawable.game_bg);
                    p8.setBackgroundResource(R.drawable.game_bg);
                    p7.setBackgroundResource(R.drawable.game_bg);
                    p6.setBackgroundResource(R.drawable.game_bg);
                    p5.setBackgroundResource(R.drawable.game_bg);
                    p4.setBackgroundResource(R.drawable.game_bg);
                    p3.setBackgroundResource(R.drawable.game_bg);
                    p2.setBackgroundResource(R.drawable.game_bg);
                    p1.setBackgroundResource(R.drawable.game_deep_bg);
                    fixed_int = 0;
                }
            }
        };
    }

    private void OnlinedCode(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    unity = jsonObject.getString(constant.UNITY);
                    UnityAds.initialize(GameActivity.this, jsonObject.getString(constant.UNITY));
                    StartAppSDK.init(GameActivity.this, jsonObject.getString(constant.STARTAPP), false);
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        finish();
        customType(GameActivity.this, getString(R.string.animation_type));
    }
}