package com.bithack.apparatus.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.bithack.apparatus.ApparatusApp;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.Screen;
import com.bithack.apparatus.Settings;
import com.bithack.apparatus.SoundManager;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.TextureFactory;

public class MainMenu extends Screen implements InputProcessor {
    private final Texture bgtex = TextureFactory.load("data/apparatusmenu.png");
    final ApparatusApp tp;

    public MainMenu(ApparatusApp tp) {
        this.tp = tp;
    }

    @Override
    public int tick() {
        return 0;
    }

    @Override
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
        G.gl.glEnable(3042);
        G.gl.glBlendFunc(770, 771);
        LevelMenu.lchecktex.bind();
        if (Game.enable_sound) {
            G.gl.glPushMatrix();
            G.gl.glTranslatef(-0.84749997f, -0.8025f, 0.0f);
            G.gl.glScalef(0.04f, 0.06666667f, 1.0f);
            MiscRenderer.draw_textured_box();
            G.gl.glPopMatrix();
        }
        if (Game.enable_music) {
            G.gl.glPushMatrix();
            G.gl.glTranslatef(-0.46f, -0.8025f, 0.0f);
            G.gl.glScalef(0.04f, 0.06666667f, 1.0f);
            MiscRenderer.draw_textured_box();
            G.gl.glPopMatrix();
        }
        G.gl.glDepthMask(true);
        G.gl.glEnable(2929);
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(this);
        SoundManager.play_music();
    }

    @Override
    public boolean screen_to_world(int x, int y, Vector2 out) {
        return false;
    }

    @Override
    public boolean ready() {
        return false;
    }

    @Override
    public boolean keyDown(int arg0) {
        if (arg0 == 4) {
            Settings.save();
            ApparatusApp.backend.exit2();
            return false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char arg0) {
        return false;
    }

    @Override
    public boolean keyUp(int arg0) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int btn) {
		float px = ((float) x) / ((float) G.realwidth);
		float py = ((float) y) / ((float) G.realheight);
        Gdx.app.log("px,py", String.valueOf(px) + " " + py + " " + G.realwidth + " " + G.realheight);

        if (py <= 0.26f || py >= 0.53f) {
            if (py > 0.53f && py < 0.73f) {
                if (px > 0.04f && px < 0.34f) {
                    this.tp.open_sandbox();
                } else if (px > 0.41f && px < 0.76f) {
                    ApparatusApp.backend.open_settings();
                }
            }
        } else if (px > 0.03f && px < 0.34f) {
            ApparatusApp.backend.open_packchooser();
        } else if (px > 0.34f && px < 0.8f) {
            ApparatusApp.backend.open_community();
        }
        if (px > 0.04f && px < 0.12f && py > 0.81f) {
            Game.enable_sound = !Game.enable_sound;
            if (Game.enable_sound) {
                SoundManager.enable_sound();
            } else {
                SoundManager.disable_sound();
            }
        } else if (px > 0.21f && px < 0.3f && py > 0.81f) {
            Game.enable_music = !Game.enable_music;
            if (Game.enable_music) {
                SoundManager.enable_music();
            } else {
                SoundManager.disable_music();
            }
        }
        if (px > 0.82f && py > 0.82f) {
            Settings.save();
            ApparatusApp.backend.exit2();
        }
        Gdx.input.setInputProcessor(this);
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int btn) {
        return false;
    }

    @Override
    public boolean scrolled(int arg0) {
        return false;
    }

    @Override
    public boolean mouseMoved(int arg0, int arg1) {
        return false;
    }
}
