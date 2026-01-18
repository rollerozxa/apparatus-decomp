package com.bithack.apparatus;

import android.content.SharedPreferences;
import android.os.Debug;
import android.widget.Toast;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.bithack.apparatus.Settings;
import java.io.File;
import java.io.IOException;

public class AndroidSettingsAdapter extends Settings.Adapter {
    public static AndroidSettingsAdapter instance;
    AndroidApplication app;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public AndroidSettingsAdapter(AndroidApplication app) {
        instance = this;
        this.prefs = app.getPreferences(0);
        this.editor = this.prefs.edit();
        this.app = app;
    }

    @Override
    public String get(String key) {
        return this.prefs.getString(key, "");
    }

    @Override
    public void set(String key, String value) {
        this.editor.putString(key, value);
    }

    @Override
    public void save() {
        this.editor.commit();
    }

    @Override
    public void msg(final String s) {
        this.app.runOnUiThread(new Runnable() { // from class: com.bithack.apparatus.AndroidSettingsAdapter.1
            @Override
            public void run() {
                Toast.makeText(AndroidSettingsAdapter.this.app, s, 0).show();
            }
        });
    }

    @Override
    public File get_tmp_file() {
        try {
            return File.createTempFile("lvl", "tmp", this.app.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void start_tracing() {
        Debug.startMethodTracing("apparatus");
    }

    @Override
    public void stop_tracing() {
        Debug.stopMethodTracing();
    }
}
