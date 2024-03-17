package com.Part.Time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.Part.Time.Adapter.MyAdapter;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.tabs.TabLayout;

import static maes.tech.intentanim.CustomIntent.customType;

public class WalletActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private InterstitialAd interstitialAd;
    Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        findViewById(R.id.back_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                customType(WalletActivity.this,getString(R.string.animation_type));
            }
        });
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Payment Method"));
        tabLayout.addTab(tabLayout.newTab().setText("Payment History"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        interstitialAd = new InterstitialAd(WalletActivity.this, constant.FACEBOOK_INTER_ADS_CODE);
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

    }
    @Override
    public void onBackPressed() {
        finish();
        customType(WalletActivity.this,getString(R.string.animation_type));
    }
}