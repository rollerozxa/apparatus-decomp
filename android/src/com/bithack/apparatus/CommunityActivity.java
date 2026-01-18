package com.bithack.apparatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;

public class CommunityActivity extends Activity {
    public static final int LOADING_DIALOG = 2;
    public static final int MENU = 4;
    public static final int PAGELOADING_DIALOG = 3;
    public static final int REGISTERING_DIALOG = 5;
    Bundle bundle;
    public WebView webview;

    @Override
    public void onResume() {
        super.onResume();
        Gdx.app.log("RESUMEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", "DSAAAAAAAAAAAAAAAAAA");
        this.webview.restoreState(this.bundle);
    }

    @Override
    public void onCreate(Bundle b) {
        Gdx.app.log("CREATEEEEEEEEEEEEEEEEE", "DSAJKJKLKKKKKKKKKKKKKKKKKKKK");
        super.onCreate(b);
        this.bundle = b;
        requestWindowFeature(1);
        setContentView(R.layout.main);
        this.webview = (WebView) findViewById(R.id.webview);
        this.webview.setScrollBarStyle(0);
        this.webview.setBackgroundColor(android.R.color.background_dark);
        WebSettings settings = this.webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setUserAgentString("Apparatus Android (1.2.2 Beta 3)");
        this.webview.setWebViewClient(new WebViewClient() { // from class: com.bithack.apparatus.CommunityActivity.1
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) throws NumberFormatException {
                Uri uri = Uri.parse(url);
                if (url.substring(0, 12).equals("apparatus://")) {
                    if (url.equals("apparatus://register")) {
                        CommunityActivity.this.showDialog(0);
                    } else if (url.equals("apparatus://login")) {
                        CommunityActivity.this.showDialog(1);
                    } else {
                        int id = Integer.parseInt(url.substring(12), 10);
                        Intent i = new Intent(CommunityActivity.this, (Class<?>) ApparatusApplication.class);
                        i.putExtra("id", id);
                        CommunityActivity.this.webview.saveState(CommunityActivity.this.bundle);
                        Settings.set("c_url", CommunityActivity.this.webview.getUrl());
                        CommunityActivity.this.startActivity(i);
                        CommunityActivity.this.finish();
                    }
                } else if (uri.getHost().equals("apparatus-web.voxelmanip.se")) {
                    view.loadUrl(url);
                } else {
                    Intent browserIntent = new Intent("android.intent.action.VIEW", uri);
                    CommunityActivity.this.startActivity(browserIntent);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                CommunityActivity.this.showDialog(3);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    CommunityActivity.this.dismissDialog(3);
                } catch (Exception e) {
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                try {
                    CommunityActivity.this.dismissDialog(3);
                } catch (Exception e) {
                }
                Toast.makeText(CommunityActivity.this, L.get("error_connecting_to_community_server"), 1).show();
                CommunityActivity.this.webview.loadData("", "text/html", "utf8");
                System.gc();
                CommunityActivity.this.finish();
            }
        });
        String url = Settings.get("c_url");
        if (url != null && !url.equals("")) {
            this.webview.loadUrl(url);
            return;
        }
        String token = Settings.get("community-token");
        if (token != null && token.length() == 40) {
            this.webview.loadUrl("http://apparatus-web.voxelmanip.se/internal/tokenlogin.php?t=" + token);
        } else {
            this.webview.loadUrl("http://apparatus-web.voxelmanip.se/");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Settings.set("c_url", this.webview.getUrl());
        this.webview.saveState(this.bundle);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog ret = null;
        switch (id) {
            case 0:
                ret = new RegisterDialog(this).get_dialog();
                break;
            case 1:
                ret = new LoginDialog(this).get_dialog();
                break;
            case 2:
                ret = ProgressDialog.show(this, "", L.get("signingin"), true, false);
                break;
            case 3:
                ret = ProgressDialog.show(this, "", L.get("loading"), true, false);
                break;
            case 4:
                AlertDialog.Builder bld = new AlertDialog.Builder(this);
                CharSequence[] sbitems = {L.get("mainmenu"), L.get("quit")};
                bld.setTitle(L.get("menu"));
                bld.setItems(sbitems, new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.CommunityActivity.2
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ApparatusApp.instance.open_mainmenu();
                                ApparatusApp.instance.fade = 0.0f;
                                Settings.set("c_url", CommunityActivity.this.webview.getUrl());
                                CommunityActivity.this.finish();
                                break;
                            case 1:
                                Settings.save();
                                CommunityActivity.this.webview.loadData("", "text/html", "utf8");
                                CommunityActivity.this.webview.clearCache(false);
                                CommunityActivity.this.webview.clearHistory();
                                CommunityActivity.this.webview.clearView();
                                Settings.set("c_url", CommunityActivity.this.webview.getUrl());
                                CommunityActivity.this.moveTaskToBack(true);
                                break;
                        }
                    }
                });
                ret = bld.create();
                break;
            case 5:
                ret = ProgressDialog.show(this, "", L.get("registeringaccount"), true, false);
                break;
        }
        if (ret != null) {
            ret.getWindow().setFlags(1024, 1024);
            return ret;
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent ev) {
        if (code == 4) {
            if (this.webview.canGoBack()) {
                this.webview.goBack();
                return true;
            }
            ApparatusApp.instance.open_mainmenu();
            ApparatusApp.instance.fade = 0.0f;
            System.gc();
            Settings.set("c_url", this.webview.getUrl());
            finish();
            return true;
        }
        if (code == 82) {
            showDialog(4);
            return true;
        }
        return false;
    }
}
