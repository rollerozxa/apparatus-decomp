package com.bithack.apparatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bithack.apparatus.PublishDialog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/* loaded from: classes.dex */
public class RegisterDialog {
    final Activity activity;
    Dialog dialog;

    public RegisterDialog(Activity app) {
        this.activity = app;
        AlertDialog.Builder builder = new AlertDialog.Builder(app);
        final View view = LayoutInflater.from(app).inflate(R.layout.registerdialog, (ViewGroup) null);
        builder.setTitle(L.get("register_account"));
        builder.setView(view);
        builder.setNeutralButton(L.get("register"), new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.RegisterDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                RegisterDialog.this.new RegisterTask().execute(((TextView) view.findViewById(R.id.username)).getText().toString(), ((TextView) view.findViewById(R.id.password)).getText().toString(), ((TextView) view.findViewById(R.id.password_rpt)).getText().toString(), ((TextView) view.findViewById(R.id.email)).getText().toString());
            }
        });
        builder.setNegativeButton(L.get("cancel"), new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.RegisterDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.dialog = builder.create();
    }

    public Dialog get_dialog() {
        return this.dialog;
    }

    protected class RegisterTask extends AsyncTask<String, String, String> {
        protected RegisterTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(String... params) throws IllegalStateException {
            String result = "";
            try {
                HttpClient client = PublishDialog.HttpUtils.getNewHttpClient();
                HttpGet req = new HttpGet("http://apparatus-web.voxelmanip.se/internal/register.php?u=" + URLEncoder.encode(params[0]) + "&p=" + URLEncoder.encode(params[1]) + "&pt=" + URLEncoder.encode(params[2]) + "&e=" + URLEncoder.encode(params[3]));
                HttpResponse res = client.execute(req);
                InputStream in = res.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder str = new StringBuilder();
                while (true) {
                    String data = reader.readLine();
                    if (data != null) {
                        str.append(data);
                    } else {
                        in.close();
                        result = str.toString();
                        return result;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            try {
                if (RegisterDialog.this.activity instanceof CommunityActivity) {
                    RegisterDialog.this.activity.showDialog(5);
                } else {
                    RegisterDialog.this.activity.showDialog(13);
                }
            } catch (Exception e) {
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x00a8 -> B:35:0x0030). Please report as a decompilation issue!!! */
        @Override // android.os.AsyncTask
        public void onPostExecute(String result) {
            String result2 = result.trim();
            boolean error = false;
            if (result2.length() > 0 && result2.substring(0, 3).equals("OK:")) {
                String token = result2.substring(3).trim();
                if (token.length() != 40) {
                    error = true;
                    result2 = L.get("temporary_server_error");
                } else {
                    Settings.set("community-token", token);
                    Settings.save();
                    Toast.makeText(RegisterDialog.this.activity, L.get("registered_and_logged_in"), 0).show();
                    try {
                        if (RegisterDialog.this.activity instanceof CommunityActivity) {
                            ((CommunityActivity) RegisterDialog.this.activity).webview.loadUrl("http://apparatus-web.voxelmanip.se/internal/tokenlogin.php?t=" + token);
                        } else {
                            RegisterDialog.this.activity.showDialog(2);
                        }
                    } catch (Exception e) {
                    }
                }
            } else {
                error = true;
            }
            try {
                if (RegisterDialog.this.activity instanceof CommunityActivity) {
                    RegisterDialog.this.activity.dismissDialog(5);
                } else {
                    RegisterDialog.this.activity.dismissDialog(13);
                }
            } catch (Exception e2) {
            }
            if (error) {
                Toast.makeText(RegisterDialog.this.activity, result2, 0).show();
                try {
                    if (RegisterDialog.this.activity instanceof CommunityActivity) {
                        RegisterDialog.this.activity.showDialog(0);
                    } else {
                        RegisterDialog.this.activity.showDialog(12);
                    }
                } catch (Exception e3) {
                }
            }
        }
    }
}
