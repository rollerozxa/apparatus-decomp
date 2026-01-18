package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.TextureFactory;

/* loaded from: classes.dex */
public class ModeMenu extends Screen implements InputProcessor {
    private final Texture bgtex = TextureFactory.load("data/modemenu.png");
    final ApparatusApp tp;

    public ModeMenu(ApparatusApp tp) {
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
        } else if (px > 0.28f && px < 0.68f) {
            if (py > 0.26f && py < 0.45f) {
                LevelMenu.type = 1;
                this.tp.open_levelmenu();
            } else if (py > 0.54f && py < 0.72f) {
                LevelMenu.type = 2;
                this.tp.open_levelmenu();
            }
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
