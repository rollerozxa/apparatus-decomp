package com.bithack.apparatus;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.bithack.apparatus.ApparatusApp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ApparatusDesktop implements ApparatusApp.Backend {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.stencil = 1;
        config.depth = 16;
        config.width = 1280;
        config.height = 720;
        //config.useGL20 = false;
        config.title = "Apparatus";
        ApparatusApp.backend = new ApparatusDesktop();

        Settings.set_adapter(new DesktopSettingsAdapter());
        new LwjglApplication(new ApparatusApp(), config);
    }

    public void exit2() {
        Settings.save();
        Gdx.app.exit();
        System.exit(0);
    }

    public void open_autosave_challenge_dialog() {
        // never open autosaves
        int n = Game.autosave_id;
        ApparatusApp.instance.play(1, n);
        SoundManager.play_startlevel();
    }

    public void open_autosave_dialog() {
        // stub
    }

    public void open_community() {
        // stub
    }

    public void open_infobox(String str) {
        // stub
    }

    public void open_ingame_back_community_menu() {
        // stub
    }

    public void open_ingame_back_menu() {
        ApparatusApp.instance.open_levelmenu();
    }

    public void open_ingame_menu() {
        // stub
    }

    public void open_ingame_sandbox_back_menu() {
        // don't ask to save sandbox changes, just go back
        ApparatusApp.instance.open_mainmenu();
    }

    public void open_ingame_sandbox_menu() {
        // stub
    }

    public void open_level_list() {
        // stub
    }

    public void open_packchooser() {
        LevelMenu.category = 0;
        ApparatusApp.instance.open_levelmenu();
    }

    public void open_sandbox_info() {
        // stub
    }

    public void open_sandbox_play_options() {
        // stub
    }

    public void open_save_dialog() {
        // stub
    }

    public void open_settings() {
        // stub
    }
}
