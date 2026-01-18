package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.bithack.apparatus.objects.ChristmasGift;
import com.bithack.apparatus.objects.GrabableObject;
import com.bithack.apparatus.objects.Marble;
import com.bithack.apparatus.objects.Plank;
import com.bithack.apparatus.objects.RocketEngine;
import com.bithack.apparatus.objects.Wheel;
import java.util.Random;

/* loaded from: classes.dex */
public class SoundManager {
    static Sound snd_marble_hit = null;
    static Sound snd_metal_hit = null;
    static Sound snd_rocket_launch = null;
    static Sound snd_lvlcomplete = null;
    static Sound snd_startlevel = null;
    static Sound[] snd_gift_hit = new Sound[3];
    static Sound[] snd_wood_hit = new Sound[4];
    static Sound[] snd_explosion = new Sound[3];
    static Music snd_marble_roll = null;
    static Music snd_rocket_thrust = null;
    static Music snd_battery_switch = null;
    static Sound snd_connect = null;
    static Sound snd_disconnect = null;
    static Sound snd_hammer = null;
    static Sound snd_detach = null;
    static Music music = null;
    static Marble[] rolling = new Marble[4];
    static int num_rolling = 0;
    private static Vector2 tmp = new Vector2();
    public static boolean init_music = false;
    public static boolean init_sound = false;
    private static long last_explosion = 0;
    static long last_wood_hit = 0;
    private static long last_marble_hit = 0;
    private static long last_metal_hit = 0;
    static Random _r = new Random();
    static long last_gift_hit = 0;
    static long last_connect = 0;
    static long last_rocket_launch = 0;

    public static boolean _init() {
        try {
            if (!init_sound) {
                snd_rocket_launch = Gdx.audio.newSound(Gdx.files.internal("data/snd/rocket_launch.wav"));
                snd_connect = Gdx.audio.newSound(Gdx.files.internal("data/snd/connect.wav"));
                snd_disconnect = Gdx.audio.newSound(Gdx.files.internal("data/snd/disconnect.wav"));
                snd_startlevel = Gdx.audio.newSound(Gdx.files.internal("data/snd/open_sandbox.mp3"));
                snd_lvlcomplete = Gdx.audio.newSound(Gdx.files.internal("data/snd/lvlcomplete.mp3"));
                snd_marble_hit = Gdx.audio.newSound(Gdx.files.internal("data/snd/marblehit.wav"));
                snd_metal_hit = Gdx.audio.newSound(Gdx.files.internal("data/snd/metalhit.wav"));
                snd_hammer = Gdx.audio.newSound(Gdx.files.internal("data/snd/hammer_2.wav"));
                snd_detach = Gdx.audio.newSound(Gdx.files.internal("data/snd/detach.wav"));
                snd_marble_roll = Gdx.audio.newMusic(Gdx.files.internal("data/snd/marbleroll.wav"));
                snd_rocket_thrust = Gdx.audio.newMusic(Gdx.files.internal("data/snd/rocket_thrust.wav"));
                for (int x = 0; x < snd_gift_hit.length; x++) {
                    snd_gift_hit[x] = Gdx.audio.newSound(Gdx.files.internal("data/snd/paket_" + x + ".wav"));
                }
                for (int x2 = 0; x2 < snd_explosion.length; x2++) {
                    snd_explosion[x2] = Gdx.audio.newSound(Gdx.files.internal("data/snd/explosion" + x2 + ".mp3"));
                }
                for (int x3 = 0; x3 < snd_wood_hit.length; x3++) {
                    snd_wood_hit[x3] = Gdx.audio.newSound(Gdx.files.internal("data/snd/woodhit" + (x3 + 1) + ".wav"));
                }
                snd_battery_switch = Gdx.audio.newMusic(Gdx.files.internal("data/snd/batteryon.wav"));
                init_sound = true;
            }
            if (!init_music) {
                music = Gdx.audio.newMusic(Gdx.files.internal("data/music/squashed.mp3"));
                music.setLooping(true);
                music.setVolume(0.66f);
                init_music = true;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Settings.msg(L.get("audio_failed"));
            dispose();
            return false;
        }
    }

    private static void dispose() {
        try {
            if (music != null) {
                music.dispose();
            }
            if (snd_hammer != null) {
                snd_hammer.dispose();
            }
            if (snd_rocket_launch != null) {
                snd_rocket_launch.dispose();
            }
            if (snd_detach != null) {
                snd_detach.dispose();
            }
            if (snd_startlevel != null) {
                snd_startlevel.dispose();
            }
            if (snd_connect != null) {
                snd_connect.dispose();
            }
            if (snd_disconnect != null) {
                snd_disconnect.dispose();
            }
            if (snd_lvlcomplete != null) {
                snd_lvlcomplete.dispose();
            }
            if (snd_marble_hit != null) {
                snd_marble_hit.dispose();
            }
            if (snd_metal_hit != null) {
                snd_metal_hit.dispose();
            }
            if (snd_marble_roll != null) {
                snd_marble_roll.dispose();
            }
            if (snd_rocket_thrust != null) {
                snd_rocket_thrust.dispose();
            }
            for (int x = 0; x < snd_gift_hit.length; x++) {
                if (snd_gift_hit[x] != null) {
                    snd_gift_hit[x].dispose();
                }
            }
            for (int x2 = 0; x2 < snd_explosion.length; x2++) {
                if (snd_explosion[x2] != null) {
                    snd_explosion[x2].dispose();
                }
            }
            for (int x3 = 0; x3 < snd_wood_hit.length; x3++) {
                if (snd_wood_hit[x3] != null) {
                    snd_wood_hit[x3].dispose();
                }
            }
            if (snd_battery_switch != null) {
                snd_battery_switch.dispose();
            }
            snd_marble_hit = null;
            snd_metal_hit = null;
            snd_lvlcomplete = null;
            snd_connect = null;
            snd_disconnect = null;
            snd_startlevel = null;
            snd_detach = null;
            snd_battery_switch = null;
            snd_hammer = null;
            snd_rocket_launch = null;
            snd_rocket_thrust = null;
            for (int x4 = 0; x4 < snd_wood_hit.length; x4++) {
                snd_wood_hit[x4] = null;
            }
            for (int x5 = 0; x5 < snd_gift_hit.length; x5++) {
                snd_gift_hit[x5] = null;
            }
            for (int x6 = 0; x6 < snd_explosion.length; x6++) {
                snd_explosion[x6] = null;
            }
            snd_marble_roll = null;
            snd_battery_switch = null;
            music = null;
            init_sound = false;
            init_music = false;
        } catch (Exception e) {
        }
    }

    public static void play_music() {
        if (Game.enable_music && _init() && !music.isPlaying()) {
            music.setLooping(true);
            music.play();
        }
        if (Game.enable_sound && _init() && !snd_marble_roll.isPlaying()) {
            snd_marble_roll.setLooping(true);
            snd_marble_roll.setVolume(0.0f);
            snd_marble_roll.play();
        }
    }

    public static void disable_sound() {
        Game.enable_sound = false;
        if (snd_marble_roll != null && snd_marble_roll.isPlaying()) {
            snd_marble_roll.stop();
        }
    }

    public static void disable_music() {
        Game.enable_music = false;
        if (music != null && music.isPlaying()) {
            music.stop();
        }
    }

    public static void enable_music() {
        Game.enable_music = true;
        if (_init() && !music.isPlaying()) {
            music.setLooping(true);
            music.play();
            music.setLooping(true);
        }
    }

    public static void enable_sound() {
        Game.enable_sound = true;
        if (_init() && snd_marble_roll != null && !snd_marble_roll.isPlaying()) {
            snd_marble_roll.setLooping(true);
            snd_marble_roll.setVolume(0.0f);
            snd_marble_roll.play();
        }
    }

    public static void tick() {
        if (init_sound && Game.enable_sound) {
            if (!RocketEngine.lights.isEmpty()) {
                float volume = Math.min(RocketEngine.lights.size(), 4) / 4.0f;
                snd_rocket_thrust.setVolume(0.2f + (volume * 0.8f));
            }
            if (Game.enable_music && !music.isPlaying()) {
                music.play();
            }
            if (num_rolling > 0) {
                float volume2 = 0.0f;
                for (int x = 0; x < num_rolling; x++) {
                    float mul = rolling[x].body.getLinearVelocity().len2();
                    volume2 += Math.min(mul / 100.0f, 1.0f);
                }
                snd_marble_roll.setVolume((volume2 / num_rolling) * 0.8f);
            }
        }
    }

    public static void stop_all() {
        if (Game.enable_sound && init_sound) {
            num_rolling = 0;
            snd_marble_roll.setVolume(0.0f);
        }
    }

    public static void start_marble_roll(Marble m) {
        if (Game.enable_sound && init_sound && num_rolling < 4) {
            if (!snd_marble_roll.isPlaying()) {
                snd_marble_roll.play();
            }
            rolling[num_rolling] = m;
            num_rolling++;
        }
    }

    public static void stop_marble_roll(Marble m) {
        if (init_sound && Game.enable_sound && num_rolling > 0) {
            num_rolling--;
            if (num_rolling == 0) {
                snd_marble_roll.setVolume(0.0f);
            }
        }
    }

    public static void handle_marble_hit(Marble m, GrabableObject b) {
        if (init_sound && Game.enable_sound) {
            play_marble_hit(Math.min(m.body.getLinearVelocity().len2() / 100.0f, 1.0f));
            start_marble_roll(m);
        }
    }

    public static void handle_metal_hit(GrabableObject o, GrabableObject b) {
        if (init_sound && Game.enable_sound) {
            play_metal_hit(Math.min(o.body.getLinearVelocity().len2() / 200.0f, 1.0f));
        }
    }

    public static void handle_wood_hit(GrabableObject o, GrabableObject b) {
        if (init_sound && Game.enable_sound) {
            long now = System.currentTimeMillis();
            if (now - last_wood_hit >= 80) {
                if ((b instanceof Plank) || (b instanceof Wheel)) {
                    tmp.set(o.body.getLinearVelocity()).sub(b.body.getLinearVelocity());
                    play_wood_hit(Math.min((tmp.len2() * 2.0f) / 70.0f, 0.8f));
                    last_wood_hit = now;
                } else if (b instanceof Marble) {
                    handle_marble_hit((Marble) b, o);
                } else if (b instanceof ChristmasGift) {
                    handle_gift_hit((ChristmasGift) b, o);
                }
            }
        }
    }

    public static void handle_gift_hit(ChristmasGift m, GrabableObject b) {
        if (init_sound && Game.enable_sound) {
            long now = System.currentTimeMillis();
            if (now - last_gift_hit >= 80) {
                int x = _r.nextInt(snd_gift_hit.length);
                tmp.set(m.body.getLinearVelocity()).sub(b.body.getLinearVelocity());
                float vol = Math.min((tmp.len2() * 2.0f) / 70.0f, 0.7f);
                snd_gift_hit[x].play(0.8f * vol);
                last_gift_hit = now;
            }
        }
    }

    public static void play_wood_hit(float vol) {
        if (init_sound && Game.enable_sound) {
            int x = _r.nextInt(snd_wood_hit.length);
            snd_wood_hit[x].play(0.7f * vol);
        }
    }

    public static void play_connect() {
        if (init_sound && Game.enable_sound) {
            long now = System.currentTimeMillis();
            if (snd_connect != null && now - last_connect > 20) {
                last_connect = now;
            }
            snd_connect.play();
        }
    }

    public static void play_disconnect() {
        if (init_sound && Game.enable_sound) {
            long now = System.currentTimeMillis();
            if (snd_disconnect != null && now - last_connect > 20) {
                last_connect = now;
            }
            snd_disconnect.play();
        }
    }

    public static void play_marble_hit(float vol) {
        if (init_sound && Game.enable_sound) {
            long now = System.currentTimeMillis();
            if (now - last_marble_hit > 60) {
                snd_marble_hit.play(vol);
                last_marble_hit = now;
            }
        }
    }

    public static void play_lvlcomplete() {
        if (init_sound && Game.enable_sound) {
            snd_lvlcomplete.play(1.0f);
        }
    }

    public static void play_metal_hit(float vol) {
        if (init_sound && Game.enable_sound) {
            long now = System.currentTimeMillis();
            if (now - last_metal_hit > 60) {
                snd_metal_hit.play(vol);
                last_metal_hit = now;
            }
        }
    }

    public static void play_rocket_launch() {
        if (init_sound && Game.enable_sound) {
            long now = System.currentTimeMillis();
            if (now - last_rocket_launch > 60) {
                snd_rocket_launch.play(1.0f);
                last_rocket_launch = now;
            }
            start_rocket_thrust();
        }
    }

    public static void play_startlevel() {
        if (init_sound && Game.enable_sound && snd_startlevel != null) {
            snd_startlevel.play();
        }
    }

    public static void play_hammer() {
        if (init_sound && Game.enable_sound && snd_hammer != null) {
            snd_hammer.play();
        }
    }

    public static void play_detach() {
        if (!init_sound || !Game.enable_sound || snd_detach == null) {
            return;
        }
        snd_detach.play();
    }

    public static void play_battery_switch() {
        if (init_sound && Game.enable_sound) {
            snd_battery_switch.play();
        }
    }

    public static void stop_music() {
        if (Game.enable_music && init_music && music != null && music.isPlaying()) {
            music.stop();
        }
        if (Game.enable_sound && init_sound && snd_marble_roll != null && snd_marble_roll.isPlaying()) {
            snd_marble_roll.stop();
        }
    }

    public static void play_explosion() {
        if (init_sound && Game.enable_sound) {
            long now = System.currentTimeMillis();
            if (now - last_explosion > 20) {
                snd_explosion[_r.nextInt(snd_explosion.length)].play();
                last_explosion = now;
            }
        }
    }

    public static void start_rocket_thrust() {
        if (init_sound && Game.enable_sound && !snd_rocket_thrust.isPlaying()) {
            snd_rocket_thrust.setLooping(true);
            snd_rocket_thrust.play();
        }
    }

    public static void stop_rocket_thrust() {
        if (init_sound && Game.enable_sound) {
            snd_rocket_thrust.stop();
        }
    }
}
