package com.bithack.apparatus.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class G {
    public static OrthographicCamera cam;
    public static OrthographicCamera cam_p;
    public static OrthographicCamera cam_wscreen;
    public static BitmapFont font;
    public static BitmapFont gamefont;
    public static GL11 gl;
    public static int height;
    public static PerspectiveCamera p_cam;
    public static int realheight;
    public static int realwidth;
    private static Vector3 tmp_v;
    public static int width;
    public static SpriteBatch batch = new SpriteBatch();
    public static final Vector3 vec_rot = new Vector3(0.0f, 0.0f, 1.0f);
    public static boolean smallscreen = false;
    private static final float[] _clear_col = {0.0f, 0.0f, 0.0f};
    public static float delta = 0.0f;

    public static void set_clear_color(float r, float g, float b) {
        _clear_col[0] = r;
        _clear_col[1] = g;
        _clear_col[2] = b;
        if (gl != null) {
            gl.glClearColor(_clear_col[0], _clear_col[1], _clear_col[2], 0.0f);
        }
    }

    public static void clear() {
        gl.glClear(16640);
        gl.glDisable(2929);
    }

    public static void color(float r, float g, float b, float a) {
        gl.glColor4f(r, g, b, a);
    }

    public static void tint(float r, float g, float b, float a) {
    }

    public static void set_wscreen_proj_mat() {
    }

    public static void reset_cam() {
        cam.viewportHeight = height / 16.0f;
        cam.viewportWidth = width / 16.0f;
        cam.position.set(0.0f, 0.0f, 0.0f);
        cam.update();
    }

    public static void init(int width2, int height2) {
        gl = Gdx.graphics.getGL11();
        cam = new OrthographicCamera(width / 16.0f, height / 16.0f);
        cam.position.set(0.0f, 0.0f, 0.0f);
        cam.update();
        cam_p = new OrthographicCamera(width, height);
        cam_p.position.set(width / 2, height / 2, 0.0f);
        cam_p.far = 500.0f;
        cam_p.update();
        p_cam = new PerspectiveCamera(45.0f, width / 16.0f, height / 16.0f);
        p_cam.position.set(0.0f, 0.0f, 20.0f);
        p_cam.far = 256.0f;
        p_cam.near = 0.1f;
        p_cam.direction.set(-1.8f, 2.0f, -1.0f).nor();
        p_cam.update();
        font = new BitmapFont();

        refresh_size(width2, height2);
    }

    /**
     * Call when we need to resize viewports
     */
    public static void refresh_size(int width2, int height2){
        realwidth = width2;
        realheight = height2;

        float d = (160.0f * Gdx.graphics.getDensity()) / 2.54f;
        float scale = 0.018f * d;

        if (width2 < 800) {
            smallscreen = true;
        }

        width = (int)Math.floor(realwidth * scale);
        height = (int)Math.floor(realheight * scale);
        Gdx.app.log("resize", "w: " + width + " h: " + height);

        cam.viewportWidth = width / 16.0f;
        cam.viewportHeight = height / 16.0f;
        cam.update();

        p_cam.viewportWidth = width / 16.0f;
        p_cam.viewportHeight = height / 16.0f;
        p_cam.update();

        cam_p.viewportWidth = width;
        cam_p.viewportHeight = height;
        cam_p.position.set(width / 2, height / 2, 0.0f);
        cam_p.update();
    }
}
