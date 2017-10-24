package com.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;


import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

/**
 * This class echoes a string called from JavaScript.
 */
public class GDTPlugin extends CordovaPlugin {

  BannerView bannerView = null;
  boolean isClicked = false;

  public boolean autoBannerInit() {
    if(bannerView == null){

      final GDTPlugin me = this;
      final Activity ui = this.cordova.getActivity();

      ui.runOnUiThread(new Runnable() {
        public void run() {
          me.bannerView = new BannerView(ui, ADSize.BANNER, android.R.string.gdt_app_id, android.R.string.gdt_banner_pos_id);

          me.bannerView.setRefresh(30);
          me.bannerView.setADListener(new AbstractBannerADListener() {
            public void onADClicked(int arg0) {
              Log.i("AD_DEMO", "Banner clicked");
              me.isClicked = true;
            }

            @Override
            public void onNoAD(int arg0) {
              Log.i("AD_DEMO", "BannerNoAD，eCode=" + arg0);
            }

            @Override
            public void onADReceiv() {
              Log.i("AD_DEMO", "ONBannerReceive");
            }
          });

          RelativeLayout.LayoutParams bannerParams = new RelativeLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
          );

          bannerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
          bannerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
          me.bannerView.setLayoutParams(bannerParams);

          RelativeLayout layout = new RelativeLayout(ui);
          RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
          );

          layout.setLayoutParams(params);
          layout.addView(me.bannerView);
          ui.addContentView (layout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
          bannerView.loadAD();
        }
      });
      return false;
    }else{
      return true;
    }

  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

    if (action.equals("show")&&autoBannerInit()&&!isClicked) {
      this.cordova.getActivity().runOnUiThread(new Runnable() {
        public void run() {
          bannerView.setVisibility(View.VISIBLE);
        }});
    }else if (action.equals("close")) {
      if(bannerView!=null){
        this.cordova.getActivity().runOnUiThread(new Runnable() {
          public void run() {
            bannerView.setVisibility(View.INVISIBLE);
          }});
      }
    }
    return true;
  }
}
