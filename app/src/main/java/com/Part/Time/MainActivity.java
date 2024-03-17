package com.Part.Time;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.onesignal.OneSignal;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.Part.Time.Adapter.SliderAdapterExample;
import com.Part.Time.Model.SliderItem;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.unity3d.ads.UnityAds;

import static maes.tech.intentanim.CustomIntent.customType;

public class MainActivity  extends AppCompatActivity {

    TextView name_lbl,email_lbl, convert_amount_txt, invite_code_txt, invite_money_txt;
    String userid, today_string, telegram, how, privacy, about,convert_amount_string,facebook;
    int daily_amount;
    Constant constant;
    List<SliderItem> list;
    SliderView sliderView;
    private SliderAdapterExample adapter;
    private AppLovinInterstitialAdDialog adDialog;

    String banner,intersial,video;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private AdView adView;
    private RewardedVideoAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(constant.ONESIGNAL_APP_ID);
        OnlinedCode();

        SharedPreferences sharedPreferences = getSharedPreferences("savedata", MODE_PRIVATE);
        userid = sharedPreferences.getString("USERID", "0");
        String name = sharedPreferences.getString("NAME", "0");
        String email = sharedPreferences.getString("EMAIL", "0");
        name_lbl = findViewById(R.id.name_lbl);
        email_lbl = findViewById(R.id.email_lbl);
        invite_code_txt = findViewById(R.id.invite_code_txt);
        name_lbl.setText(name);
        email_lbl.setText(email);
        invite_code_txt.setText(String.valueOf(userid));
        convert_amount_txt = findViewById(R.id.convert_amount_txt);
        invite_money_txt = findViewById(R.id.invite_money_txt);
        AppInvite();
        GetAmount();
        DailyPointData();
        BottomBar();
        GetAdminData();
        GetNotification();
        findViewById(R.id.notification_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.notify_dialog);
                TextView textView = dialog.findViewById(R.id.notification_txt);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            textView.setText(jsonObject.getString(constant.MSG));
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
                Volley.newRequestQueue(MainActivity.this).add(stringRequest);
                dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        findViewById(R.id.convert_amount_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert_amount_txt.setText(convert_amount_string);
                AppInvite();
                GetAmount();
                DailyPointData();
                GetAdminData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        convert_amount_txt.setText("Tap for Balance");
                    }
                },2000);
            }
        });



        findViewById(R.id.spin_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vpn()){
                        startActivity(new Intent(MainActivity.this, SpinActivity.class));
                        customType(MainActivity.this,getString(R.string.home_animation_type));

                }else {
                    vpnAlert("Connect");
                }
            }
        });
        findViewById(R.id.Scratch_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vpn()){
                    startActivity(new Intent(MainActivity.this,ScratchActivity.class));
                    customType(MainActivity.this,getString(R.string.home_animation_type));

                }else {
                    vpnAlert("Connect");
                }
            }
        });
        findViewById(R.id.read_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vpn()){
                    startActivity(new Intent(MainActivity.this,GameActivity.class));
                    customType(MainActivity.this,getString(R.string.home_animation_type));

                }else {
                    vpnAlert("Connect");
                }
            }
        });
        findViewById(R.id.watch_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vpn()){
                    startActivity(new Intent(MainActivity.this, WatchActivity.class));
                    customType(MainActivity.this,getString(R.string.home_animation_type));

                }else {
                    vpnAlert("Connect");
                }
            }
        });

        findViewById(R.id.daily_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                today_string = year + "" + month + "" + day;
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("save", MODE_PRIVATE);
                boolean currentday = sharedPreferences.getBoolean(today_string, false);
                if (!currentday) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.all_dialog);
                    ImageView all_image = dialog.findViewById(R.id.all_image);
                    TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                    Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                    all_image.setImageResource(R.drawable.ic_gift_box);
                    all_text.setText(String.valueOf("Congratulation \n you win " + daily_amount));
                    all_btn.setText("CLAIM");
                    all_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DailyPointAddData(String.valueOf(daily_amount));
                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("save", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(today_string, true);
                            editor.apply();
                            dialog.dismiss();
                            adDialog = AppLovinInterstitialAd.create( AppLovinSdk.getInstance( MainActivity.this ), MainActivity.this );
                            adDialog.show();
                        }
                    });
                    dialog.show();
                } else {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.all_dialog);
                    ImageView all_image = dialog.findViewById(R.id.all_image);
                    TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                    Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                    all_image.setImageResource(R.drawable.ic_gift_box);
                    all_text.setText(String.valueOf("Today Bonus you claimed.Wait for next day"));
                    all_btn.setText("DONE");
                    all_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
        findViewById(R.id.facebook_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent builder = new CustomTabsIntent.Builder().build();
                builder.launchUrl(MainActivity.this, Uri.parse(facebook));
            }
        });
        findViewById(R.id.telegram_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent builder = new CustomTabsIntent.Builder().build();
                builder.launchUrl(MainActivity.this, Uri.parse(telegram));
            }
        });
        findViewById(R.id.sahre_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = "Download Link : " + constant.UPDATE_LINK_URL + getPackageName() + " Refer Code : " + userid;
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download And Earn Real Cash");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Send"));
            }
        });

        list = new ArrayList<>();
        sliderView = findViewById(R.id.imageSlider);
        adapter = new SliderAdapterExample(this, list);
        sliderView.setSliderAdapter(adapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(5);
        sliderView.setIndicatorGravity(Gravity.RIGHT);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        GetSliderData();
    }

    private void GetSliderData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.BANNER_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SliderItem sliderItem = new SliderItem(
                                jsonObject.getString(constant.NAME_TAG),
                                jsonObject.getString(constant.SLIDER_PHOTO_TAG)
                        );
                        list.add(sliderItem);
                        adapter = new SliderAdapterExample(MainActivity.this, list);
                        sliderView.setSliderAdapter(adapter);
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
                map.put(constant.APP_API_TAG, constant.APP_API);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }




    public boolean vpn() {

        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                //Log.d("DEBUG", "IFACE NAME: " + iface);
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    public void vpnAlert(String command) {
        SweetAlertDialog sweetAlertDialog =  new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setContentText("Please " + command + " US.UK.Canada VPN first");
        sweetAlertDialog.setConfirmText("Try agin");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                finish();
            }
        });
        sweetAlertDialog.show();
    }

    private void GetAmount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    convert_amount_string = jsonObject.getString(constant.AMOUNT_TAG);
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

    private void DailyPointData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    daily_amount = Integer.parseInt(jsonObject.getString(constant.DAILY_AMOUNT_TAG));
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

    private void DailyPointAddData(final String amount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.DAILY_POINT_ADD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(constant.ERROR) == true) {
                        GetAmount();
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
    private void GetAPP(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(constant.APP_COUNT).equals("1")){
                        final Dialog mdialog = new Dialog(MainActivity.this);
                        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        mdialog.setCancelable(false);
                        mdialog.setContentView(R.layout.sub_dialog);
                        ImageView imageView = mdialog.findViewById(R.id.image);
                        imageView.setImageResource(R.drawable.youtube);
                        TextView text = (TextView) mdialog.findViewById(R.id.sub_txt);
                        text.setText("Subcribe our Channel");
                        Button dialogButton = (Button) mdialog.findViewById(R.id.sub_btn);
                        dialogButton.setText("Subscribe");
                        mdialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mdialog.dismiss();
                            }
                        });
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(jsonObject.getString(constant.HOW))));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                SharedPreferences sharedPreferences = getSharedPreferences("show",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("SHOW",1);
                                editor.apply();
                            }
                        });
                        if(!((Activity) MainActivity.this).isFinishing()) {
                            mdialog.show();
                        }
                    }
                    if (jsonObject.getString(constant.APP_COUNT).equals("2")){

                        final Dialog mmdialog = new Dialog(MainActivity.this);
                        mmdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        mmdialog.setCancelable(false);
                        mmdialog.setContentView(R.layout.sub_dialog);
                        ImageView imageView = mmdialog.findViewById(R.id.image);
                        imageView.setImageResource(R.drawable.telegram);
                        TextView text = (TextView) mmdialog.findViewById(R.id.sub_txt);
                        text.setText("Join our Telegram Channel");
                        mmdialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mmdialog.dismiss();
                            }
                        });
                        Button dialogButton = (Button) mmdialog.findViewById(R.id.sub_btn);
                        dialogButton.setText("Join");
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(jsonObject.getString(constant.TELEGRAM))));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                SharedPreferences sharedPreferences = getSharedPreferences("show",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("SHOW",1);
                                editor.apply();
                            }
                        });
                        if(!((Activity) MainActivity.this).isFinishing()) {
                            mmdialog.show();
                        }
                    }
                    if (jsonObject.getString(constant.APP_COUNT).equals("3")){
                        final Dialog mmmdialog = new Dialog(MainActivity.this);
                        mmmdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        mmmdialog.setCancelable(false);
                        mmmdialog.setContentView(R.layout.sub_dialog);
                        ImageView imageView = mmmdialog.findViewById(R.id.image);
                        if (imageView.getVisibility()==View.VISIBLE){
                            imageView.setVisibility(View.GONE);
                        }
                        TextView text = (TextView) mmmdialog.findViewById(R.id.sub_txt);
                        text.setText("If you want to get payment, you have to join our telegram channel. If you don't join our channel, you will not get payment.");
                        mmmdialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mmmdialog.dismiss();
                            }
                        });
                        Button dialogButton = (Button) mmmdialog.findViewById(R.id.sub_btn);
                        dialogButton.setText("Join");
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(jsonObject.getString(constant.TELEGRAM))));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                SharedPreferences sharedPreferences = getSharedPreferences("show",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("SHOW",1);
                                editor.apply();
                            }
                        });
                        if(!((Activity) MainActivity.this).isFinishing()) {
                            mmmdialog.show();
                        }
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
                Map<String,String> map = new HashMap();
                map.put(constant.APP_API_TAG,constant.APP_API);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void AppInvite() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    invite_money_txt.setText("Share your refferral code and invite your friends via SMS/EMAIL/WHATSAPP. You earn upto " + jsonObject.getString(constant.INVITE) + "%");
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
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void VPNdata(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.VPN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("countryCode").equals("BD")){
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("VPN Needed")
                                .setMessage("Please Use VPN and selected US or UK for more Revenue . Other we are not pay you...")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        System.exit(0);
                                    }
                                })
                                .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        System.exit(0);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setCancelable(false)
                                .show();
                    }else {
                        SharedPreferences sharedPreferen = getSharedPreferences("show",MODE_PRIVATE);
                        if (sharedPreferen.getInt("SHOW",0)!=1){
                            GetAPP();
                        }

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
    private void GetAdminData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    telegram = jsonObject.getString(constant.TELEGRAM);
                    how = jsonObject.getString(constant.HOW);
                    privacy = jsonObject.getString(constant.PRIVACY);
                    about = jsonObject.getString(constant.ABOUT);
                    facebook = jsonObject.getString(constant.FACEBOOK);
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
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void BottomBar() {
        findViewById(R.id.home_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
                customType(MainActivity.this,"fadein-to-fadeout");
            }
        });
        findViewById(R.id.redeem_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WalletActivity.class));
                customType(MainActivity.this,getString(R.string.home_animation_type));
            }
        });
        findViewById(R.id.rate_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(constant.RATE_US + getPackageName())));
            }
        });


        findViewById(R.id.about_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent builder = new CustomTabsIntent.Builder().build();
                builder.launchUrl(MainActivity.this, Uri.parse(about));
            }
        });
        findViewById(R.id.how_to_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent builder = new CustomTabsIntent.Builder().build();
                builder.launchUrl(MainActivity.this, Uri.parse(how));
            }
        });
        findViewById(R.id.privacy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent builder = new CustomTabsIntent.Builder().build();
                builder.launchUrl(MainActivity.this, Uri.parse(privacy));
            }
        });
    }

    private void GetNotification() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(constant.APP_COUNT).equals("1")) {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.notification_dialog);
                        ImageView all_image = dialog.findViewById(R.id.all_image);
                        TextView all_text = (TextView) dialog.findViewById(R.id.all_text);
                        Button all_btn = (Button) dialog.findViewById(R.id.all_btn);
                        Picasso.get().load(jsonObject.getString(constant.IMAGE)).placeholder(R.drawable.loading).into(all_image);
                        all_text.setText(jsonObject.getString(constant.TEXT));
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put(constant.APP_API_TAG, constant.APP_API);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.findViewById(R.id.close_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.rating_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(constant.RATE_US + getPackageName())));
            }
        });
        dialog.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.show();
    }


    private void OnlinedCode(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, constant.COUNT_URL_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    UnityAds.initialize(MainActivity.this, jsonObject.getString(constant.UNITY));
                    StartAppSDK.init(MainActivity.this, jsonObject.getString(constant.STARTAPP), false);
                    StartAppAd.disableSplash();

                    banner = jsonObject.getString(constant.BANNER);
                    intersial = jsonObject.getString(constant.INTERSIAL);
                    video = jsonObject.getString(constant.VIDEO);
                    //ADmobloadAlldata();

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

    private void ADmobloadAlldata(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(banner);
        LinearLayout linearLayout = findViewById(R.id.banner_container);
        linearLayout.addView(adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(intersial);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
            }
            @Override
            public void onAdOpened() {
            }
            @Override
            public void onAdClicked() {
            }
            @Override
            public void onAdLeftApplication() {
            }
            @Override
            public void onAdClosed() {
            }
        });

        rewardedAd = MobileAds.getRewardedVideoAdInstance(MainActivity.this);
        rewardedAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
            }
            @Override
            public void onRewardedVideoAdLoaded() {
                rewardedAd.show();
            }
            @Override
            public void onRewardedVideoAdOpened() {
            }
            @Override
            public void onRewardedVideoStarted() {
            }
            @Override
            public void onRewardedVideoAdClosed() {
            }
            @Override
            public void onRewardedVideoAdLeftApplication() {
            }
            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
            }
            @Override
            public void onRewardedVideoCompleted() {
            }
        });
        rewardedAd.loadAd(video, new AdRequest.Builder().build());

    }


}