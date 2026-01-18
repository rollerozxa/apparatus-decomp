package com.bithack.apparatus;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.TextureFactory;

/* loaded from: classes.dex */
public class SandboxMenu extends Screen implements InputProcessor {
    private final Texture bgtex = TextureFactory.load("data/sandboxmenu.png");
    final ApparatusApp tp;

    public SandboxMenu(ApparatusApp tp) {
        this.tp = tp;
    }

    @Override // com.bithack.apparatus.Screen
    public int tick() {
        return 0;
    }

    @Override // com.bithack.apparatus.Screen
    public void render() {
        G.gl.glMatrixMode(GL10.GL_PROJECTION);
        G.gl.glLoadIdentity();
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        G.gl.glLoadIdentity();
        G.gl.glDisable(2929);
        G.gl.glDepthMask(false);
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        G.gl.glEnable(3553);
        this.bgtex.bind();
        MiscRenderer.draw_textured_box();
        G.gl.glDepthMask(true);
        G.gl.glEnable(2929);
    }

    @Override // com.bithack.apparatus.Screen
    public void resume() {
        Gdx.input.setInputProcessor(this);
        FileHandle h = Gdx.files.getFileHandle("/ApparatusLevels/.autosave.jar", Files.FileType.External);
        if (h.exists()) {
            ApparatusApp.backend.open_autosave_dialog();
        }
    }

    @Override // com.bithack.apparatus.Screen
    public boolean screen_to_world(int x, int y, Vector2 out) {
        return false;
    }

    @Override // com.bithack.apparatus.Screen
    public boolean ready() {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyDown(int code) {
        switch (code) {
            case 4:
            case Input.Keys.B /* 30 */:
                this.tp.open_mainmenu();
                break;
        }
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyTyped(char arg0) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean keyUp(int arg0) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDown(int x, int y, int pointer, int btn) {
		float px = ((float) x) / ((float) G.realwidth);
		float py = ((float) y) / ((float) G.realheight);
        Gdx.app.log("px,py", String.valueOf(px) + " " + py);
        if (x < 64 && y > G.realheight - 100) {
            this.tp.open_mainmenu();
        } else if (x <= G.realwidth - 64 || y <= G.realheight - 100) {
            if (py > 0.27f && py < 0.49f && px > 0.29f && px < 0.7f) {
                Game game = ApparatusApp.game;
                Game.sandbox = true;
                Game game2 = ApparatusApp.game;
                Game.from_community = false;
                ApparatusApp.game.create_level(0);
                this.tp.play();
                SoundManager.play_startlevel();
            }
            if (py > 0.47f && py < 0.77f) {
                if (px > 0.08f && px < 0.5f) {
                    Game game3 = ApparatusApp.game;
                    Game.sandbox = true;
                    Game game4 = ApparatusApp.game;
                    Game.from_community = false;
                    ApparatusApp.game.create_level(1);
                    SoundManager.play_startlevel();
                    this.tp.play();
                } else if (px > 0.5f && px < 0.92f) {
                    Settings.msg(L.get("coming_soon"));
                }
            }
            if (px > 0.67d && px < 0.85d && py > 0.77f && py < 0.9f) {
                ApparatusApp.backend.open_level_list();
            }
        } else {
            ApparatusApp.backend.open_sandbox_info();
        }
        return true;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean touchUp(int x, int y, int pointer, int btn) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean scrolled(int arg0) {
        return false;
    }

    @Override // com.badlogic.gdx.InputProcessor
    public boolean mouseMoved(int arg0, int arg1) {
        return false;
    }
}
