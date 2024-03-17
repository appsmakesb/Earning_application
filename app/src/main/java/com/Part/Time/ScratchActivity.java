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

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAppOptions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anupkumarpanwar.scratchview.ScratchView;
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

public class ScratchActivity extends AppCompatActivity {

    Constant constant;
    int admin_imp_count, imp_count,bonus_amount;
    String userid;
    TextView count, show_point;
    ScratchView scratchView;
    Random random = new Random();
    int random_amount = random.nextInt(50);
    SimpleArcDialog mDialog;
    Handler handler1 = new Handler();
    Runnable refresh;
    LinearLayout task_tab;
    TextView timer;
    String unity;
    CardView click_tab;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        findViewById(R.id.back_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                customType(ScratchActivity.this, getString(R.string.animation_type));
            }
        });

        VPNdata();
        AudienceNetworkAds.initialize(this);

        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setKeepScreenOn(true)
                .setPrivacyFrameworkRequired(AdColonyAppOptions.GDPR, true)
                .setPrivacyConsentString(AdColonyAppOptions.GDPR, String.valueOf(1));
        AdColony.configure(this, appOptions, getString(R.string.APP_ID), getString(R.string.ZONE_IDS));

        

        interstitialAd = new InterstitialAd(ScratchActivity.this, constant.FACEBOOK_INTER_ADS_CODE);
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




        mDialog = new SimpleArcDialog(this);
        task_tab = findViewById(R.id.task_tab);
        timer = findViewById(R.id.timer);
        mDialog.setCancelable(false);
        mDialog.setConfiguration(new ArcConfiguration(this));
        mDialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("savedata", MODE_PRIVATE);
        userid = sharedPreferences.getString("USERID", "0");
        GetImpData();
        refresh = new Runnable() {
            public void run() {
                handler1.postDelayed(refresh, 1000);
                TimeGone();
            }
        };
        handler1.post(refresh);
        scratchView = findViewById(R.id.scratch_view);
        show_point = findViewById(R.id.show_point);
        count = findViewById(R.id.count);
        show_point.setText("You win " + random_amount + " Point");
        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                final Dialog dialog = new Dialog(ScratchActivity.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.all_dialog);
                ImageView all_image = dialog.findViewById(R.id.all_image);
                TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                all_image.setImageResource(R.drawable.ic_gift_box);
                all_text.setText("Congratulation you win " + random_amount + " point !");
                all_btn.setText("CLAIM");
                all_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (imp_count >= admin_imp_count) {
                            TimeAdd();
                            if (UnityAds.isReady()) {
                                UnityAds.show(ScratchActivity.this);
                            } else {
                                UnityAds.initialize(ScratchActivity.this, unity);
                            }
                            GetImpPointAddData(String.valueOf(random_amount));
                        } else {
                            StartAppAd startAppAd = new StartAppAd(ScratchActivity.this);
                            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
                            startAppAd.showAd();
                            if (UnityAds.isReady()) {
                                UnityAds.show(ScratchActivity.this);
                            } else {
                                UnityAds.initialize(ScratchActivity.this, unity);
                            }
                            GetImpPointAddData(String.valueOf(random_amount));
                        }
                    }
                });
                dialog.show();
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

            }
        });

        click_tab = findViewById(R.id.click_tab);
        CLickTAbData();
        findViewById(R.id.click_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ScratchActivity.this);
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
                        StartAppAd startAppAd = new StartAppAd(ScratchActivity.this);
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
                    if (jsonObject.getString(constant.SCRATCH_CLICK_AMOUNT).equals("1")){
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
                        customType(ScratchActivity.this, getString(R.string.animation_type));
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
                map.put(constant.ACTION, "two");
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }


    private void VPNdata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.VPN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("countryCode").equals("BD")) {
                        final Dialog dialog = new Dialog(ScratchActivity.this);
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
                                customType(ScratchActivity.this, getString(R.string.animation_type));
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

    private void GetImpData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    imp_count = Integer.parseInt(jsonObject.getString(constant.USER_SCRATCH_IMP_COUNT_TAG));
                    admin_imp_count = Integer.parseInt(jsonObject.getString(constant.USER_ADMIN_SCRATCH_IMP_COUNT_TAG));
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.SRATCH_IMP_POINT_ADD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    /*if (jsonObject.getBoolean(constant.ERROR) == true) {
                        finish();
                        customType(ScratchActivity.this, getString(R.string.animation_type));
                    }*/
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

    private void TimeAdd() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.SCRATCH_TIME_ADD_URL, new Response.Listener<String>() {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.SCRATCH_TIME_GONE_URL, new Response.Listener<String>() {
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
                    unity = jsonObject.getString(constant.UNITY);
                    UnityAds.initialize(ScratchActivity.this, jsonObject.getString(constant.UNITY));
                    StartAppSDK.init(ScratchActivity.this, jsonObject.getString(constant.STARTAPP), false);
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
        finish();
        customType(ScratchActivity.this, getString(R.string.animation_type));
    }
}