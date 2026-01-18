package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.bithack.apparatus.Settings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class DesktopSettingsAdapter extends Settings.Adapter {
    Properties props = new Properties();

    public DesktopSettingsAdapter() {
        try {
            this.props.load(new FileInputStream("~/.apparatus/settings"));
        } catch (Exception e) {
        }
    }

    public String get(String key) {
        return this.props.getProperty(key) != null ? this.props.getProperty(key) : "";
    }

    public void set(String key, String value) {
        this.props.setProperty(key, value);
    }

    public void save() {
        File f = new File("~/.apparatus/settings");
        try {
            f.createNewFile();
            this.props.store(new FileOutputStream(f), (String) null);
        } catch (IOException e) {
        }
    }

    public void msg(String s) {
        Gdx.app.log("MSG", s);
    }

    public File get_tmp_file() {
        try {
            return File.createTempFile("tplvl", (String) null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void start_tracing() {

    }

    public void stop_tracing() {

    }
}
