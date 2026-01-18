package com.bithack.apparatus;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ApparatusApp implements ApplicationListener {
    public static String PATH_BACKGROUNDS;
    public static String PATH_BASE;
    public static String PATH_LEVELS;
    public static BitmapFont bigfont;
    public static Screen current;
    public static BitmapFont font;
    public static BitmapFont font2;
    public static BitmapFont hugefont;
    public static ApparatusApp instance;
    public float fade;
    int fade_dir;
    private int last_category;
    private int last_id;
    LevelMenu levelmenu;
    Screen loading_screen;
    MainMenu mainmenu;
    Screen menu;
    private String next_level;
    Screen next_screen;
    Screen sandbox;
    public static Backend backend = null;
    private static ArrayList<Runnable> scheduled = new ArrayList<>();
    public static Game game = null;
    public static boolean enable_music = true;
    public static boolean enable_lighting = true;
    public static int num_levels = 44;
    public static int num_completed = 0;
    public static int _start = 3;
    public static int num_interactive_levels = 1;
    public static int num_christmas_levels = 10;
    public boolean fading = false;
    private int next_level_id = 0;
    boolean load_next = false;
    public boolean load_autosave = false;

    public interface Backend {
        void exit2();

        void open_autosave_challenge_dialog();

        void open_autosave_dialog();

        void open_community();

        void open_infobox(String str);

        void open_ingame_back_community_menu();

        void open_ingame_back_menu();

        void open_ingame_menu();

        void open_ingame_sandbox_back_menu();

        void open_ingame_sandbox_menu();

        void open_level_list();

        void open_packchooser();

        void open_sandbox_info();

        void open_sandbox_play_options();

        void open_save_dialog();

        void open_settings();

    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void create() {
        instance = this;
        G.init(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        MiscRenderer.initialize();
        this.loading_screen = new LoadingScreen(this, _start);
        current = this.loading_screen;
        current.resume();
    }

    public void play() {
        this.next_level = null;
        this.fading = true;
        if (this.fade <= 1.0E-4f) {
            this.fade = 1.0f;
        }
        this.fade_dir = 0;
        this.next_screen = game;
    }

    public void play(int type, int id) {
        Game.from_community = false;
        Game.sandbox = false;
        Game.from_sandbox = false;
        this.next_level = "0/" + id;
        this.next_level_id = id;
        this.fading = true;
        if (this.fade <= 1.0E-4f) {
            this.fade = 1.0f;
        }
        this.fade_dir = 0;
        this.next_screen = game;
        this.last_category = type;
        this.last_id = id;
    }

    public void play_next() {
        Game.from_community = false;
        Game.sandbox = false;
        Game.from_sandbox = false;
        if (this.last_id < num_levels - 1 && num_completed / 2 >= (this.last_id + 1) / 4) {
            play(this.last_category, this.last_id + 1);
        } else {
            open_levelmenu();
        }
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void dispose() {
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void pause() {
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void render() {
        G.delta = Gdx.graphics.getDeltaTime();
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (scheduled.size() > 0 && LoadingScreen.initialized) {
            Iterator<Runnable> it = scheduled.iterator();
            while (it.hasNext()) {
                Runnable r = it.next();
                r.run();
            }
            scheduled.clear();
        }
        if (this.load_next) {
            this.load_next = false;
            if (current == game && this.next_level != null) {
                try {
                    if (this.last_category == 2) {
                        game.open_interactive(this.next_level_id);
                    } else if (this.load_autosave) {
                        this.load_autosave = false;
                        game.open_autosave(LevelMenu.category, this.next_level_id);
                    } else {
                        game.open(LevelMenu.category, this.next_level_id);
                    }
                } catch (Exception e) {
                    Settings.msg(L.get("unable_to_open_level"));
                    this.next_screen = this.levelmenu;
                    e.printStackTrace();
                }
            }
            current.resume();
            Settings.save();
        }
        current.tick();
        current.render();
        if (this.fading) {
            if (this.fade_dir == 0) {
                this.fade -= Math.min(G.delta * 4.0f, 0.064f);
                if (this.fade <= 0.0f) {
                    this.fade_dir = 1;
                    this.fade = 0.0f;
                    current = this.next_screen;
                    this.load_next = true;
                }
            } else if (this.fade_dir == 1) {
                this.fade += Math.min(G.delta * 3.0f, 0.048f);
                if (this.fade >= 1.0f) {
                    this.fading = false;
                }
            }
            G.gl.glDisable(2929);
            G.gl.glEnable(3042);
            G.gl.glDisable(3553);
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f - this.fade);
            G.gl.glBlendFunc(770, 771);
            G.gl.glMatrixMode(GL10.GL_PROJECTION);
            G.gl.glLoadIdentity();
            G.gl.glMatrixMode(GL10.GL_MODELVIEW);
            G.gl.glLoadIdentity();
            G.gl.glScalef(2.0f, 2.0f, 1.0f);
            MiscRenderer.draw_colored_box();
            G.gl.glEnable(2929);
            if (this.load_next) {
                G.batch.begin();
                G.batch.setColor(Color.BLACK);
                G.font.setColor(Color.BLACK);
                G.font.draw(G.batch, "LOADING...", (G.realwidth / 2) - 50, G.realheight / 2);
                G.batch.setColor(Color.WHITE);
                G.font.setColor(Color.WHITE);
                G.batch.end();
            }
        }
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void resize(int arg0, int arg1) {
    }

    @Override // com.badlogic.gdx.ApplicationListener
    public void resume() {
    }

    public void open_levelmenu() {
        this.next_screen = this.levelmenu;
        this.fading = true;
        if (this.fade <= 1.0E-4f) {
            this.fade = 1.0f;
        }
        this.fade_dir = 0;
    }

    public void open_mainmenu() {
        this.next_screen = this.mainmenu;
        this.fading = true;
        if (this.fade <= 1.0E-4f) {
            this.fade = 1.0f;
        }
        this.fade_dir = 0;
    }

    public void open_sandbox() {
        this.next_screen = this.sandbox;
        this.fading = true;
        if (this.fade <= 1.0E-4f) {
            this.fade = 1.0f;
        }
        this.fade_dir = 0;
    }

    public static void schedule(Runnable r) {
        scheduled.add(r);
    }
}
