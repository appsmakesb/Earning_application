package com.Part.Time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.facebook.ads.*;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.unity3d.ads.UnityAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static maes.tech.intentanim.CustomIntent.customType;

public class SpinActivity extends AppCompatActivity {

    private LuckyWheel lw;
    List<WheelItem> wheelItems;
    Random random_spin = new Random();
    int random_amount, imp_count;
    Button start;
    Constant constant;
    int admin_imp_count, bonus_amount;
    private static final int COUNT_TIMER = 4;
    Handler handler = new Handler();
    Runnable runnable;
    int count_time;
    String userid;
    TextView count;
    Handler handler1 = new Handler();
    Runnable refresh;
    LinearLayout task_tab;
    TextView timer;
    SimpleArcDialog mDialog;
    CardView click_tab;
    private InterstitialAd interstitialAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);
        findViewById(R.id.back_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                handler1.removeCallbacks(refresh);
                finish();
                customType(SpinActivity.this, getString(R.string.animation_type));
            }
        });
        VPNdata();

        AudienceNetworkAds.initialize(this);
        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setKeepScreenOn(true)
                .setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true)
                .setPrivacyConsentString(AdColonyAppOptions.GDPR, String.valueOf(1));
        AdColony.configure(this, appOptions, getString(R.string.APP_ID), getString(R.string.ZONE_IDS));

        adView = new AdView(this, constant.FACEBOOK_BANNER_ADS_CODE, AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();


        interstitialAd = new InterstitialAd(SpinActivity.this, constant.FACEBOOK_INTER_ADS_CODE);
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



        mDialog = new SimpleArcDialog(SpinActivity.this);
        mDialog.setConfiguration(new ArcConfiguration(SpinActivity.this));
        mDialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("savedata", MODE_PRIVATE);
        userid = sharedPreferences.getString("USERID", "0");
        generateWheelItems();
        lw = findViewById(R.id.lwv);
        start = findViewById(R.id.start);
        count = findViewById(R.id.count);
        task_tab = findViewById(R.id.task_tab);
        timer = findViewById(R.id.timer);
        lw.addWheelItems(wheelItems);
        lw.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                final Dialog dialog = new Dialog(SpinActivity.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.all_dialog);
                ImageView all_image = dialog.findViewById(R.id.all_image);
                TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                all_image.setImageResource(R.drawable.ic_gift_box);
                all_text.setText("Congratulation you win " + wheelItems.get(random_amount - 1).text + " point !");
                all_btn.setText("CLAIM");
                all_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.post(runnable);
                        dialog.dismiss();
                        StartAppAd startAppAd = new StartAppAd(SpinActivity.this);
                        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
                        startAppAd.showAd();
                    }
                });
                dialog.show();
            }
        });
        Timer();
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
                    random_amount = random_spin.nextInt(6 - 1) + 1;
                    lw.rotateWheelTo(random_amount);
                    StartAppAd startAppAd = new StartAppAd(SpinActivity.this);
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
                final Dialog dialog = new Dialog(SpinActivity.this);
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
                        StartAppAd startAppAd = new StartAppAd(SpinActivity.this);
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
                    if (jsonObject.getString(constant.SPIN_CLICK_AMOUNT).equals("1")){
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
                        customType(SpinActivity.this, getString(R.string.animation_type));
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
                map.put(constant.ACTION, "four");
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
                    start.setText("Play Spin");
                    GetImpPointAddData(wheelItems.get(random_amount - 1).text);
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
                    imp_count = Integer.parseInt(jsonObject.getString(constant.SPIN_IMP_COUNT_TAG));
                    admin_imp_count = Integer.parseInt(jsonObject.getString(constant.ADMIN_SPIN_IMP_COUNT_TAG));
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

    private void VPNdata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.VPN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("countryCode").equals("BD")) {
                        final Dialog dialog = new Dialog(SpinActivity.this);
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
                                customType(SpinActivity.this, getString(R.string.animation_type));
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

    private void GetImpPointAddData(final String amount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.SPIN_IMP_POINT_ADD_URL, new Response.Listener<String>() {
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
                map.put(constant.AMOUNT_TAG, amount);
                map.put(constant.USERID_TAG, userid);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void generateWheelItems() {
        wheelItems = new ArrayList<>();
        wheelItems.add(new WheelItem(getResources().getColor(R.color.purple_700), BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_spin_dollar_faka), String.valueOf(random_spin.nextInt(15 - 10) + 10)));
        wheelItems.add(new WheelItem(getResources().getColor(R.color.purple_500), BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_spin_dollar_fill), String.valueOf(random_spin.nextInt(6 - 2) + 2)));
        wheelItems.add(new WheelItem(getResources().getColor(R.color.purple_200), BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_spin_dollar_faka), String.valueOf(random_spin.nextInt(7 - 5) + 5)));
        wheelItems.add(new WheelItem(getResources().getColor(R.color.purple_700), BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_spin_dollar_fill), String.valueOf(random_spin.nextInt(3 - 1) + 1)));
        wheelItems.add(new WheelItem(getResources().getColor(R.color.purple_500), BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_spin_dollar_faka), String.valueOf(random_spin.nextInt(10 - 5) + 5)));
        wheelItems.add(new WheelItem(getResources().getColor(R.color.purple_200), BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_spin_dollar_fill), String.valueOf(random_spin.nextInt(5 - 2) + 2)));
    }

    private void TimeAdd() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.SPIN_TIME_ADD_URL, new Response.Listener<String>() {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.SPIN_TIME_GONE_URL, new Response.Listener<String>() {
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

    private void OnlinedCode(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    UnityAds.initialize(SpinActivity.this, jsonObject.getString(constant.UNITY));
                    StartAppSDK.init(SpinActivity.this, jsonObject.getString(constant.STARTAPP), false);
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
        handler.removeCallbacks(runnable);
        handler1.removeCallbacks(refresh);
        finish();
        customType(SpinActivity.this, getString(R.string.animation_type));
    }
}