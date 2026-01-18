package com.bithack.apparatus;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.TextureFactory;

/* loaded from: classes.dex */
public class LoadingScreen extends Screen {
    public static final int LEVELMENU = 2;
    public static final int MAINMENU = 3;
    public static final int SANDBOX = 4;
    Texture loadingtxt;
    final int next;
    ApparatusApp tp;
    public static Runnable ready_runnable = null;
    static boolean initialized = false;
    private boolean rendered = false;
    private int init_counter = 0;

    public LoadingScreen(ApparatusApp tp, int next) {
        this.next = next;
        G.set_clear_color(1.0f, 1.0f, 1.0f);
        this.loadingtxt = TextureFactory.load("data/loading.png");
        this.tp = tp;
    }

    @Override // com.bithack.apparatus.Screen
    public int tick() {
        return 0;
    }

    @Override // com.bithack.apparatus.Screen
    public void render() {
        G.set_clear_color(0.15294118f, 0.15294118f, 0.15294118f);
        G.clear();
        if (!this.rendered) {
            G.gl.glEnable(3042);
            G.gl.glBlendFunc(770, 771);
            G.gl.glEnable(3553);
            G.cam_p.apply(G.gl);
            G.gl.glTranslatef(G.width / 2, G.height / 2, 0.0f);
            G.gl.glScalef(256.0f, 256.0f, 1.0f);
            this.loadingtxt.bind();
            MiscRenderer.draw_textured_box();
            this.rendered = true;
            return;
        }
        if (!initialized) {
            switch (this.init_counter) {
                case 0:
                    Gdx.input.setCatchBackKey(true);
                    ApparatusApp.font = new BitmapFont(Gdx.files.getFileHandle("data/misc/tesla.fnt", Files.FileType.Internal), Gdx.files.getFileHandle("data/misc/tesla.png", Files.FileType.Internal), false);
                    ApparatusApp.font2 = new BitmapFont(Gdx.files.getFileHandle("data/misc/msg.fnt", Files.FileType.Internal), Gdx.files.getFileHandle("data/misc/msg.png", Files.FileType.Internal), false);
                    ApparatusApp.PATH_BASE = String.valueOf(Gdx.files.getExternalStoragePath()) + "/ApparatusLevels/";
                    ApparatusApp.PATH_LEVELS = String.valueOf(ApparatusApp.PATH_BASE) + "level/";
                    ApparatusApp.PATH_BACKGROUNDS = String.valueOf(ApparatusApp.PATH_BASE) + "Backgrounds/";
                    ObjectFactory._init();
                    ObjectFactory.set_adapter(new ObjectFactoryAdapter());
                    ApparatusApp.game = new Game(this.tp);
                    break;
                case 1:
                    this.tp.levelmenu = new LevelMenu(this.tp);
                    break;
                case 2:
                    this.tp.mainmenu = new MainMenu(this.tp);
                    break;
                case 3:
                    this.tp.sandbox = new SandboxMenu(this.tp);
                    break;
                case 5:
                    System.gc();
                    break;
                case 6:
                    System.gc();
                    if (ready_runnable == null) {
                        this.tp.open_mainmenu();
                    } else {
                        ready_runnable.run();
                    }
                    initialized = true;
                    break;
            }
            this.init_counter++;
            G.gl.glEnable(3042);
            G.gl.glBlendFunc(770, 771);
            G.gl.glEnable(3553);
            G.cam_p.apply(G.gl);
            G.gl.glTranslatef(G.width / 2, G.height / 2, 0.0f);
            G.gl.glScalef(256.0f, 256.0f, 1.0f);
            this.loadingtxt.bind();
            MiscRenderer.draw_textured_box();
            return;
        }
        G.gl.glEnable(3042);
        G.gl.glBlendFunc(770, 771);
        G.gl.glEnable(3553);
        G.cam_p.apply(G.gl);
        G.gl.glTranslatef(G.width / 2, G.height / 2, 0.0f);
        G.gl.glScalef(256.0f, 256.0f, 1.0f);
        this.loadingtxt.bind();
        MiscRenderer.draw_textured_box();
    }

    @Override // com.bithack.apparatus.Screen
    public void resume() {
        G.set_clear_color(1.0f, 1.0f, 1.0f);
        this.init_counter = 0;
        initialized = false;
    }

    @Override // com.bithack.apparatus.Screen
    public boolean screen_to_world(int x, int y, Vector2 out) {
        return false;
    }

    @Override // com.bithack.apparatus.Screen
    public boolean ready() {
        return false;
    }
}
