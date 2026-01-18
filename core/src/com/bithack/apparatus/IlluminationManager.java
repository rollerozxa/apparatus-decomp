package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.bithack.apparatus.ObjectManager;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.objects.BaseMotor;
import com.bithack.apparatus.objects.BaseObject;
import com.bithack.apparatus.objects.BaseRope;
import com.bithack.apparatus.objects.Battery;
import com.bithack.apparatus.objects.Bucket;
import com.bithack.apparatus.objects.Button;
import com.bithack.apparatus.objects.Cable;
import com.bithack.apparatus.objects.ChristmasGift;
import com.bithack.apparatus.objects.Damper;
import com.bithack.apparatus.objects.DynamicMotor;
import com.bithack.apparatus.objects.Explosive;
import com.bithack.apparatus.objects.Hinge;
import com.bithack.apparatus.objects.Hub;
import com.bithack.apparatus.objects.Knob;
import com.bithack.apparatus.objects.Marble;
import com.bithack.apparatus.objects.MetalBar;
import com.bithack.apparatus.objects.MetalWheel;
import com.bithack.apparatus.objects.Mine;
import com.bithack.apparatus.objects.Panel;
import com.bithack.apparatus.objects.PanelCable;
import com.bithack.apparatus.objects.Plank;
import com.bithack.apparatus.objects.RocketEngine;
import com.bithack.apparatus.objects.Rope;
import com.bithack.apparatus.objects.RopeEnd;
import com.bithack.apparatus.objects.StaticMotor;
import com.bithack.apparatus.objects.Weight;
import com.bithack.apparatus.objects.Wheel;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class IlluminationManager {
    static final float SHADOW_COLOR = 0.6f;
    static final float SHADOW_FACTOR = 1.2f;
    static final float SHADOW_FACTOR2 = 1.5f;
    static final float SHADOW_SCALE = 0.12f;
    private static Mesh hinge_penumbra;
    private static Mesh marble_penumbra;
    private static Mesh marble_umbra;
    private static Mesh metal_penumbra;
    private static Mesh metal_umbra;
    private static Mesh misc_penumbra;
    private static Mesh misc_umbra;
    private static Mesh plank_penumbra;
    private static Mesh plank_umbra;
    private static Mesh wheel_penumbra;
    private static Mesh wheel_umbra;
    private static boolean _initialized = false;
    static final Vector2 tmp = new Vector2();

    private IlluminationManager() {
    }

    public static void init(Game game) {
        if (!_initialized) {
            _initialized = true;
            generate_cube_projections(game.lightdir);
            generate_plank_projections(game.lightdir);
            generate_wheel_projections(game.lightdir);
            generate_marble_projections(game.lightdir);
            generate_metal_projections(game.lightdir);
            generate_hinge_penumbras(game.lightdir);
        }
    }

    private static void generate_hinge_penumbras(Vector3 lightdir) {
        float[] penumbra = new float[Input.Keys.BUTTON_START];
        hinge_penumbra = new Mesh(true, 54, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(2, 4, ShaderProgram.COLOR_ATTRIBUTE));
        for (int i = 0; i < 9; i++) {
            float x = (float) Math.cos(i * 0.7853982f);
            float y = (float) Math.sin(i * 0.7853982f);
            penumbra[(i * 6 * 2) + 0] = 0.09f * x;
            penumbra[(i * 6 * 2) + 1] = 0.09f * y;
            penumbra[(i * 6 * 2) + 2] = 0.0f;
            penumbra[(i * 6 * 2) + 3] = 0.0f;
            penumbra[(i * 6 * 2) + 4] = 0.0f;
            penumbra[(i * 6 * 2) + 5] = 0.6f;
            penumbra[(i * 6 * 2) + 6] = 0.16f * x;
            penumbra[(i * 6 * 2) + 7] = 0.16f * y;
            penumbra[(i * 6 * 2) + 8] = 0.0f;
            penumbra[(i * 6 * 2) + 9] = 0.0f;
            penumbra[(i * 6 * 2) + 10] = 0.0f;
            penumbra[(i * 6 * 2) + 11] = 0.0f;
        }
        hinge_penumbra.setVertices(penumbra);
        hinge_penumbra.setAutoBind(false);
    }

    private static void generate_marble_projections(Vector3 lightdir) {
        float[] umbra = new float[84];
        float[] penumbra = new float[Input.Keys.F9];
        marble_umbra = new Mesh(true, 63, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        marble_penumbra = new Mesh(true, 126, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(2, 4, ShaderProgram.COLOR_ATTRIBUTE));
        for (int i = 0; i < 21; i++) {
            float x = (float) Math.cos(i * 0.31415927f);
            float y = (float) Math.sin(i * 0.31415927f);
            umbra[(i * 4) + 0] = 0.8f * x * 0.5f;
            umbra[(i * 4) + 1] = 0.8f * y * 0.5f;
            umbra[(i * 4) + 2] = 0.8f * x * 0.5f;
            umbra[(i * 4) + 3] = 0.8f * y * 0.5f;
            penumbra[(i * 6 * 2) + 0] = 0.8f * x * 0.5f;
            penumbra[(i * 6 * 2) + 1] = 0.8f * y * 0.5f;
            penumbra[(i * 6 * 2) + 2] = 0.0f;
            penumbra[(i * 6 * 2) + 3] = 0.0f;
            penumbra[(i * 6 * 2) + 4] = 0.0f;
            penumbra[(i * 6 * 2) + 5] = 0.6f;
            penumbra[(i * 6 * 2) + 6] = 1.1f * x * 0.5f;
            penumbra[(i * 6 * 2) + 7] = 1.1f * y * 0.5f;
            penumbra[(i * 6 * 2) + 8] = 0.0f;
            penumbra[(i * 6 * 2) + 9] = 0.0f;
            penumbra[(i * 6 * 2) + 10] = 0.0f;
            penumbra[(i * 6 * 2) + 11] = 0.0f;
        }
        marble_umbra.setVertices(umbra);
        marble_penumbra.setVertices(penumbra);
        marble_umbra.setAutoBind(false);
        marble_penumbra.setAutoBind(false);
    }

    private static void generate_wheel_projections(Vector3 lightdir) {
        float[] umbra = new float[600];
        float[] penumbra = new float[1800];
        wheel_umbra = new Mesh(true, Input.Keys.NUMPAD_6, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        wheel_penumbra = new Mesh(true, HttpStatus.SC_MULTIPLE_CHOICES, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(2, 4, ShaderProgram.COLOR_ATTRIBUTE));
        for (int i = 0; i < 25; i++) {
            float x = (float) Math.cos(i * 0.2617994f);
            float y = (float) Math.sin(i * 0.2617994f);
            umbra[(i * 4) + 0] = 0.8f * x * 0.5f;
            umbra[(i * 4) + 1] = 0.8f * y * 0.5f;
            umbra[(i * 4) + 2] = 0.8f * x * 0.5f;
            umbra[(i * 4) + 3] = 0.8f * y * 0.5f;
            penumbra[(i * 6 * 2) + 0] = 0.8f * x * 0.5f;
            penumbra[(i * 6 * 2) + 1] = 0.8f * y * 0.5f;
            penumbra[(i * 6 * 2) + 2] = 0.0f;
            penumbra[(i * 6 * 2) + 3] = 0.0f;
            penumbra[(i * 6 * 2) + 4] = 0.0f;
            penumbra[(i * 6 * 2) + 5] = 0.6f;
            penumbra[(i * 6 * 2) + 6] = 1.1f * x * 0.5f;
            penumbra[(i * 6 * 2) + 7] = 1.1f * y * 0.5f;
            penumbra[(i * 6 * 2) + 8] = 0.0f;
            penumbra[(i * 6 * 2) + 9] = 0.0f;
            penumbra[(i * 6 * 2) + 10] = 0.0f;
            penumbra[(i * 6 * 2) + 11] = 0.0f;
            umbra[(i * 4) + 100 + 0] = 0.8f * x;
            umbra[(i * 4) + 100 + 1] = 0.8f * y;
            umbra[(i * 4) + 100 + 2] = 0.8f * x;
            umbra[(i * 4) + 100 + 3] = 0.8f * y;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 0] = 0.8f * x;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 1] = 0.8f * y;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 2] = 0.0f;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 3] = 0.0f;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 4] = 0.0f;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 5] = 0.6f;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 6] = 1.1f * x;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 7] = 1.1f * y;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 8] = 0.0f;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 9] = 0.0f;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 10] = 0.0f;
            penumbra[(i * 6 * 2) + HttpStatus.SC_MULTIPLE_CHOICES + 11] = 0.0f;
            umbra[(i * 4) + HttpStatus.SC_OK + 0] = 1.8f * x;
            umbra[(i * 4) + HttpStatus.SC_OK + 1] = 1.8f * y;
            umbra[(i * 4) + HttpStatus.SC_OK + 2] = 1.8f * x;
            umbra[(i * 4) + HttpStatus.SC_OK + 3] = 1.8f * y;
            penumbra[(i * 6 * 2) + 600 + 0] = 1.8f * x;
            penumbra[(i * 6 * 2) + 600 + 1] = 1.8f * y;
            penumbra[(i * 6 * 2) + 600 + 2] = 0.0f;
            penumbra[(i * 6 * 2) + 600 + 3] = 0.0f;
            penumbra[(i * 6 * 2) + 600 + 4] = 0.0f;
            penumbra[(i * 6 * 2) + 600 + 5] = 0.6f;
            penumbra[(i * 6 * 2) + 600 + 6] = 2.1f * x;
            penumbra[(i * 6 * 2) + 600 + 7] = 2.1f * y;
            penumbra[(i * 6 * 2) + 600 + 8] = 0.0f;
            penumbra[(i * 6 * 2) + 600 + 9] = 0.0f;
            penumbra[(i * 6 * 2) + 600 + 10] = 0.0f;
            penumbra[(i * 6 * 2) + 600 + 11] = 0.0f;
            umbra[(i * 4) + HttpStatus.SC_MULTIPLE_CHOICES + 0] = 0.5f * x * 0.4f;
            umbra[(i * 4) + HttpStatus.SC_MULTIPLE_CHOICES + 1] = 0.5f * y * 0.4f;
            umbra[(i * 4) + HttpStatus.SC_MULTIPLE_CHOICES + 2] = 0.5f * x * 0.4f;
            umbra[(i * 4) + HttpStatus.SC_MULTIPLE_CHOICES + 3] = 0.5f * y * 0.4f;
            penumbra[(i * 6 * 2) + 900 + 0] = 0.5f * x * 0.4f;
            penumbra[(i * 6 * 2) + 900 + 1] = 0.5f * y * 0.4f;
            penumbra[(i * 6 * 2) + 900 + 2] = 0.0f;
            penumbra[(i * 6 * 2) + 900 + 3] = 0.0f;
            penumbra[(i * 6 * 2) + 900 + 4] = 0.0f;
            penumbra[(i * 6 * 2) + 900 + 5] = 0.6f;
            penumbra[(i * 6 * 2) + 900 + 6] = 1.4f * x * 0.5f;
            penumbra[(i * 6 * 2) + 900 + 7] = 1.4f * y * 0.5f;
            penumbra[(i * 6 * 2) + 900 + 8] = 0.0f;
            penumbra[(i * 6 * 2) + 900 + 9] = 0.0f;
            penumbra[(i * 6 * 2) + 900 + 10] = 0.0f;
            penumbra[(i * 6 * 2) + 900 + 11] = 0.0f;
            umbra[(i * 4) + HttpStatus.SC_BAD_REQUEST + 0] = 0.5f * x;
            umbra[(i * 4) + HttpStatus.SC_BAD_REQUEST + 1] = 0.5f * y;
            umbra[(i * 4) + HttpStatus.SC_BAD_REQUEST + 2] = 0.5f * x;
            umbra[(i * 4) + HttpStatus.SC_BAD_REQUEST + 3] = 0.5f * y;
            penumbra[(i * 6 * 2) + 1200 + 0] = 0.5f * x;
            penumbra[(i * 6 * 2) + 1200 + 1] = 0.5f * y;
            penumbra[(i * 6 * 2) + 1200 + 2] = 0.0f;
            penumbra[(i * 6 * 2) + 1200 + 3] = 0.0f;
            penumbra[(i * 6 * 2) + 1200 + 4] = 0.0f;
            penumbra[(i * 6 * 2) + 1200 + 5] = 0.6f;
            penumbra[(i * 6 * 2) + 1200 + 6] = 1.4f * x;
            penumbra[(i * 6 * 2) + 1200 + 7] = 1.4f * y;
            penumbra[(i * 6 * 2) + 1200 + 8] = 0.0f;
            penumbra[(i * 6 * 2) + 1200 + 9] = 0.0f;
            penumbra[(i * 6 * 2) + 1200 + 10] = 0.0f;
            penumbra[(i * 6 * 2) + 1200 + 11] = 0.0f;
            umbra[(i * 4) + HttpStatus.SC_INTERNAL_SERVER_ERROR + 0] = SHADOW_FACTOR2 * x;
            umbra[(i * 4) + HttpStatus.SC_INTERNAL_SERVER_ERROR + 1] = SHADOW_FACTOR2 * y;
            umbra[(i * 4) + HttpStatus.SC_INTERNAL_SERVER_ERROR + 2] = SHADOW_FACTOR2 * x;
            umbra[(i * 4) + HttpStatus.SC_INTERNAL_SERVER_ERROR + 3] = SHADOW_FACTOR2 * y;
            penumbra[(i * 6 * 2) + 1500 + 0] = SHADOW_FACTOR2 * x;
            penumbra[(i * 6 * 2) + 1500 + 1] = SHADOW_FACTOR2 * y;
            penumbra[(i * 6 * 2) + 1500 + 2] = 0.0f;
            penumbra[(i * 6 * 2) + 1500 + 3] = 0.0f;
            penumbra[(i * 6 * 2) + 1500 + 4] = 0.0f;
            penumbra[(i * 6 * 2) + 1500 + 5] = 0.6f;
            penumbra[(i * 6 * 2) + 1500 + 6] = 2.5f * x;
            penumbra[(i * 6 * 2) + 1500 + 7] = 2.5f * y;
            penumbra[(i * 6 * 2) + 1500 + 8] = 0.0f;
            penumbra[(i * 6 * 2) + 1500 + 9] = 0.0f;
            penumbra[(i * 6 * 2) + 1500 + 10] = 0.0f;
            penumbra[(i * 6 * 2) + 1500 + 11] = 0.0f;
        }
        wheel_umbra.setVertices(umbra);
        wheel_penumbra.setVertices(penumbra);
        wheel_umbra.setAutoBind(false);
        wheel_penumbra.setAutoBind(false);
    }

    private static void generate_metal_projections(Vector3 lightdir) {
        metal_umbra = new Mesh(true, 24, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        metal_penumbra = new Mesh(true, 90, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(2, 4, ShaderProgram.COLOR_ATTRIBUTE));
        float[] umbra = {1.0f, 0.5f, 1.0f, 0.5f, -1.0f, 0.5f, -1.0f, 0.5f, -1.0f, -0.5f, -1.0f, -0.5f, 1.0f, -0.5f, 1.0f, -0.5f, 2.0f, 0.5f, 2.0f, 0.5f, -2.0f, 0.5f, -2.0f, 0.5f, -2.0f, -0.5f, -2.0f, -0.5f, 2.0f, -0.5f, 2.0f, -0.5f, 4.0f, 0.5f, 4.0f, 0.5f, -4.0f, 0.5f, -4.0f, 0.5f, -4.0f, -0.5f, -4.0f, -0.5f, 4.0f, -0.5f, 4.0f, -0.5f, 1.0f, 0.5f, 1.0f, 0.5f, -1.0f, 0.5f, -1.0f, 0.5f, -1.0f, -0.5f, -1.0f, -0.5f, 1.0f, -0.5f, 1.0f, -0.5f, 2.0f, 0.5f, 2.0f, 0.5f, -2.0f, 0.5f, -2.0f, 0.5f, -2.0f, -0.5f, -2.0f, -0.5f, 2.0f, -0.5f, 2.0f, -0.5f, 4.0f, 0.5f, 4.0f, 0.5f, -4.0f, 0.5f, -4.0f, 0.5f, -4.0f, -0.5f, -4.0f, -0.5f, 4.0f, -0.5f, 4.0f, -0.5f};
        for (int x = 48; x < 96; x += 4) {
            umbra[x] = umbra[x + (-48)] < 0.0f ? umbra[x - 48] + 0.14400001f : umbra[x - 48] - 0.14400001f;
            umbra[x + 1] = umbra[(x + 1) + (-48)] < 0.0f ? umbra[(x + 1) - 48] + 0.14400001f : umbra[(x + 1) - 48] - 0.14400001f;
            umbra[x + 2] = umbra[x];
            umbra[x + 3] = umbra[x + 1];
        }
        float[] penumbra = {1.0f, 0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.3f, 0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.3f, 0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.3f, -0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, -0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.3f, -0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.3f, 0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, 0.856f, 0.356f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.144f, 0.644f, 0.0f, 0.0f, 0.0f, 0.0f, -0.856f, 0.356f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.144f, 0.644f, 0.0f, 0.0f, 0.0f, 0.0f, -0.856f, -0.356f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.144f, -0.644f, 0.0f, 0.0f, 0.0f, 0.0f, 0.856f, -0.356f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.144f, -0.644f, 0.0f, 0.0f, 0.0f, 0.0f, 0.856f, 0.356f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.144f, 0.644f, 0.0f, 0.0f, 0.0f, 0.0f, 0.64f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.15f, 0.884f, 0.0f, 0.0f, 0.0f, 0.0f, -0.64f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.15f, 0.884f, 0.0f, 0.0f, 0.0f, 0.0f, -0.64f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.15f, -0.884f, 0.0f, 0.0f, 0.0f, 0.0f, 0.64f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.15f, -0.884f, 0.0f, 0.0f, 0.0f, 0.0f, 0.64f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.15f, 0.884f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.3f, 0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, 0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.3f, 0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, -0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.3f, -0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, -0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.3f, -0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.3f, 0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, 1.856f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.18f, 0.68f, 0.0f, 0.0f, 0.0f, 0.0f, -1.82f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.18f, 0.68f, 0.0f, 0.0f, 0.0f, 0.0f, -1.82f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.18f, -0.68f, 0.0f, 0.0f, 0.0f, 0.0f, 1.82f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.18f, -0.68f, 0.0f, 0.0f, 0.0f, 0.0f, 1.82f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.18f, 0.68f, 0.0f, 0.0f, 0.0f, 0.0f, 1.28f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.15f, 0.884f, 0.0f, 0.0f, 0.0f, 0.0f, -1.28f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.15f, 0.884f, 0.0f, 0.0f, 0.0f, 0.0f, -1.28f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.15f, -0.884f, 0.0f, 0.0f, 0.0f, 0.0f, 1.28f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.15f, -0.884f, 0.0f, 0.0f, 0.0f, 0.0f, 1.28f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.15f, 0.884f, 0.0f, 0.0f, 0.0f, 0.0f, 4.0f, 0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.3f, 0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, -4.0f, 0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.3f, 0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, -4.0f, -0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.3f, -0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, 4.0f, -0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.3f, -0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, 4.0f, 0.5f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.3f, 0.79999995f, 0.0f, 0.0f, 0.0f, 0.0f, 3.82f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.18f, 0.68f, 0.0f, 0.0f, 0.0f, 0.0f, -3.82f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.18f, 0.68f, 0.0f, 0.0f, 0.0f, 0.0f, -3.82f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.18f, -0.68f, 0.0f, 0.0f, 0.0f, 0.0f, 3.82f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.18f, -0.68f, 0.0f, 0.0f, 0.0f, 0.0f, 3.82f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.18f, 0.68f, 0.0f, 0.0f, 0.0f, 0.0f, 2.56f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.15f, 0.884f, 0.0f, 0.0f, 0.0f, 0.0f, -2.56f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.15f, 0.884f, 0.0f, 0.0f, 0.0f, 0.0f, -2.56f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.15f, -0.884f, 0.0f, 0.0f, 0.0f, 0.0f, 2.56f, -0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.15f, -0.884f, 0.0f, 0.0f, 0.0f, 0.0f, 2.56f, 0.32f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.15f, 0.884f, 0.0f, 0.0f, 0.0f, 0.0f};
        metal_umbra.setVertices(umbra);
        metal_penumbra.setVertices(penumbra);
        metal_umbra.setAutoBind(false);
        metal_penumbra.setAutoBind(false);
    }

    private static void generate_cube_projections(Vector3 lightdir) {
        misc_umbra = new Mesh(true, 28, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        misc_penumbra = new Mesh(true, 66, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(2, 4, ShaderProgram.COLOR_ATTRIBUTE));
        float[] umbra = {0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -1.0f, -0.5f, -1.0f, -0.5f, 1.0f, -0.5f, 1.0f, -0.5f, 0.0f, 0.90000004f, 0.0f, 0.90000004f, 0.0f, 1.3000001f, 0.0f, 1.3000001f, -0.5f, 1.1f, -0.5f, 1.1f, -0.5f, 0.90000004f, -0.5f, 0.90000004f, -1.125f, 0.90000004f, -1.125f, 0.90000004f, -1.125f, -0.90000004f, -1.125f, -0.90000004f, 1.125f, -0.90000004f, 1.125f, -0.90000004f, 1.125f, 0.90000004f, 1.125f, 0.90000004f, 0.0f, 0.90000004f, 0.0f, 0.90000004f, 0.0f, 1.3000001f, 0.0f, 1.3000001f, -0.5f, 1.1f, -0.5f, 1.1f, -0.5f, 0.90000004f, -0.5f, 0.90000004f, -1.125f, 0.90000004f, -1.125f, 0.90000004f, -1.125f, -0.90000004f, -1.125f, -0.90000004f, 1.125f, -0.90000004f, 1.125f, -0.90000004f, 1.125f, 0.90000004f, 1.125f, 0.90000004f, 2.1f, 1.1f, 2.1f, 1.1f, -2.1f, 1.1f, -2.1f, 1.1f, -2.1f, -1.1f, -2.1f, -1.1f, 2.1f, -1.1f, 2.1f, -1.1f};
        float[] penumbra = new float[396];
        umbra_create_penumbra(umbra, 0, penumbra, 0, 4);
        umbra_create_penumbra(umbra, 16, penumbra, 60, 4);
        umbra_create_penumbra(umbra, 32, penumbra, 120, 8);
        umbra_create_penumbra(umbra, 64, penumbra, 228, 8);
        umbra_create_penumbra(umbra, 96, penumbra, 336, 4);
        misc_umbra.setVertices(umbra);
        misc_penumbra.setVertices(penumbra);
        misc_umbra.setAutoBind(false);
        misc_penumbra.setAutoBind(false);
    }

    private static int umbra_create_penumbra(float[] in, int in_offs, float[] out, int out_offs, int count) {
        for (int x = 0; x < count + 1; x++) {
            float ix = in[((x % count) * 4) + in_offs + 0];
            float iy = in[((x % count) * 4) + in_offs + 1];
            tmp.set(ix, iy);
            tmp.nor();
            tmp.mul(0.25f);
            out[(x * 12) + out_offs + 0] = ix;
            out[(x * 12) + out_offs + 1] = iy;
            out[(x * 12) + out_offs + 2] = 0.0f;
            out[(x * 12) + out_offs + 3] = 0.0f;
            out[(x * 12) + out_offs + 4] = 0.0f;
            out[(x * 12) + out_offs + 5] = 0.6f;
            out[(x * 12) + out_offs + 6] = tmp.x + ix;
            out[(x * 12) + out_offs + 7] = tmp.y + iy;
            out[(x * 12) + out_offs + 8] = 0.0f;
            out[(x * 12) + out_offs + 9] = 0.0f;
            out[(x * 12) + out_offs + 10] = 0.0f;
            out[(x * 12) + out_offs + 11] = 0.0f;
        }
        return (count * 12) + 12;
    }

    private static void generate_plank_projections(Vector3 lightdir) {
        plank_umbra = new Mesh(true, 36, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        plank_penumbra = new Mesh(true, 90, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(2, 4, ShaderProgram.COLOR_ATTRIBUTE));
        float[] umbra = {1.0f, 0.25f, 1.0f, 0.25f, -1.0f, 0.25f, -1.0f, 0.25f, -1.0f, -0.25f, -1.0f, -0.25f, 1.0f, -0.25f, 1.0f, -0.25f, 2.0f, 0.25f, 2.0f, 0.25f, -2.0f, 0.25f, -2.0f, 0.25f, -2.0f, -0.25f, -2.0f, -0.25f, 2.0f, -0.25f, 2.0f, -0.25f, 4.0f, 0.25f, 4.0f, 0.25f, -4.0f, 0.25f, -4.0f, 0.25f, -4.0f, -0.25f, -4.0f, -0.25f, 4.0f, -0.25f, 4.0f, -0.25f, 1.0f, 0.25f, 1.0f, 0.25f, -1.0f, 0.25f, -1.0f, 0.25f, -1.0f, -0.25f, -1.0f, -0.25f, 1.0f, -0.25f, 1.0f, -0.25f, 2.0f, 0.25f, 2.0f, 0.25f, -2.0f, 0.25f, -2.0f, 0.25f, -2.0f, -0.25f, -2.0f, -0.25f, 2.0f, -0.25f, 2.0f, -0.25f, 4.0f, 0.25f, 4.0f, 0.25f, -4.0f, 0.25f, -4.0f, 0.25f, -4.0f, -0.25f, -4.0f, -0.25f, 4.0f, -0.25f, 4.0f, -0.25f, 1.0f, 0.25f, 1.0f, 0.25f, -1.0f, 0.25f, -1.0f, 0.25f, -1.0f, -0.25f, -1.0f, -0.25f, 1.0f, -0.25f, 1.0f, -0.25f, 2.0f, 0.25f, 2.0f, 0.25f, -2.0f, 0.25f, -2.0f, 0.25f, -2.0f, -0.25f, -2.0f, -0.25f, 2.0f, -0.25f, 2.0f, -0.25f, 4.0f, 0.25f, 4.0f, 0.25f, -4.0f, 0.25f, -4.0f, 0.25f, -4.0f, -0.25f, -4.0f, -0.25f, 4.0f, -0.25f, 4.0f, -0.25f};
        for (int x = 0; x < 48; x += 4) {
            umbra[x + 48] = umbra[x] < 0.0f ? umbra[x] + 0.14400001f : umbra[x] - 0.14400001f;
            umbra[x + 48 + 1] = umbra[x + 1] < 0.0f ? umbra[x + 1] + 0.14400001f : umbra[x + 1] - 0.14400001f;
            umbra[x + 48 + 2] = umbra[x + 48];
            umbra[x + 48 + 3] = umbra[x + 48 + 1];
            umbra[x + 96] = umbra[x] < 0.0f ? umbra[x] + 0.2592f : umbra[x] - 0.2592f;
            umbra[x + 96 + 1] = umbra[x + 1] < 0.0f ? umbra[x + 1] + 0.2592f : umbra[x + 1] - 0.2592f;
            umbra[x + 96 + 2] = umbra[x + 96];
            umbra[x + 96 + 3] = umbra[x + 96 + 1];
        }
        float[] penumbra = {1.0f, 0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.12f, 0.37f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.12f, 0.37f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.12f, -0.37f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, -0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.12f, -0.37f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.12f, 0.37f, 0.0f, 0.0f, 0.0f, 0.0f, 0.856f, 0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.144f, 0.394f, 0.0f, 0.0f, 0.0f, 0.0f, -0.856f, 0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.144f, 0.394f, 0.0f, 0.0f, 0.0f, 0.0f, -0.856f, -0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -1.144f, -0.394f, 0.0f, 0.0f, 0.0f, 0.0f, 0.856f, -0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.144f, -0.394f, 0.0f, 0.0f, 0.0f, 0.0f, 0.856f, 0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 1.144f, 0.394f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, 1.2592f, 0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, -0.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, -1.2592f, 0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, -0.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, -1.2592f, -0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, 1.2592f, -0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, 0.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, 1.2592f, 0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.12f, 0.37f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, 0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.12f, 0.37f, 0.0f, 0.0f, 0.0f, 0.0f, -2.0f, -0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.12f, -0.37f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, -0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.12f, -0.37f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.12f, 0.37f, 0.0f, 0.0f, 0.0f, 0.0f, 1.856f, 0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.144f, 0.394f, 0.0f, 0.0f, 0.0f, 0.0f, -1.856f, 0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.144f, 0.394f, 0.0f, 0.0f, 0.0f, 0.0f, -1.856f, -0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -2.144f, -0.394f, 0.0f, 0.0f, 0.0f, 0.0f, 1.856f, -0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.144f, -0.394f, 0.0f, 0.0f, 0.0f, 0.0f, 1.856f, 0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 2.144f, 0.394f, 0.0f, 0.0f, 0.0f, 0.0f, 1.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, 2.2592f, 0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, -1.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, -2.2592f, 0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, -1.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, -2.2592f, -0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, 1.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, 2.2592f, -0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, 1.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, 2.2592f, 0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, 4.0f, 0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.12f, 0.37f, 0.0f, 0.0f, 0.0f, 0.0f, -4.0f, 0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.12f, 0.37f, 0.0f, 0.0f, 0.0f, 0.0f, -4.0f, -0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.12f, -0.37f, 0.0f, 0.0f, 0.0f, 0.0f, 4.0f, -0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.12f, -0.37f, 0.0f, 0.0f, 0.0f, 0.0f, 4.0f, 0.25f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.12f, 0.37f, 0.0f, 0.0f, 0.0f, 0.0f, 3.856f, 0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.144f, 0.394f, 0.0f, 0.0f, 0.0f, 0.0f, -3.856f, 0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.144f, 0.394f, 0.0f, 0.0f, 0.0f, 0.0f, -3.856f, -0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, -4.144f, -0.394f, 0.0f, 0.0f, 0.0f, 0.0f, 3.856f, -0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.144f, -0.394f, 0.0f, 0.0f, 0.0f, 0.0f, 3.856f, 0.10599999f, 0.0f, 0.0f, 0.0f, SHADOW_COLOR, 4.144f, 0.394f, 0.0f, 0.0f, 0.0f, 0.0f, 3.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, 4.2592f, 0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, -3.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, -4.2592f, 0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, -3.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, -4.2592f, -0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, 3.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, 4.2592f, -0.5092f, 0.0f, 0.0f, 0.0f, 0.0f, 3.7408f, 0.0f, 0.0f, 0.0f, 0.0f, 0.40000004f, 4.2592f, 0.5092f, 0.0f, 0.0f, 0.0f, 0.0f};
        plank_umbra.setVertices(umbra);
        plank_penumbra.setVertices(penumbra);
        plank_umbra.setAutoBind(false);
        plank_penumbra.setAutoBind(false);
    }

    private static void render_projected_ropeend_umbra(RopeEnd e) {
        Vector2 pos = e.get_state().position;
        G.gl.glMatrixMode(5890);
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, 0.0f);
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, -0.499f);
        wheel_umbra.render(6, (e.layer * 75) + 0, 25);
        G.gl.glPopMatrix();
        G.gl.glMatrixMode(5890);
        G.gl.glPopMatrix();
    }

    public static void render_projected_umbras_blended(ObjectManager om) {
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.gl.glEnable(2929);
        G.gl.glDisable(2960);
        G.gl.glDisable(3042);
        G.gl.glColorMask(true, true, true, true);
        G.gl.glDepthMask(true);
        G.gl.glColor4f(0.39999998f, 0.39999998f, 0.39999998f, 1.0f);
        G.gl.glDisable(3553);
        plank_umbra.bind();
        Iterator<Plank> it = om.layer0.planks.iterator();
        while (it.hasNext()) {
            Plank w = it.next();
            if (!w.culled) {
                int start = (w.size.x < 2.1f ? 0 : w.size.x < 4.1f ? 1 : 2) * 4;
                Vector2 pos = w.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos.x, pos.y, -0.499f);
                G.gl.glRotatef((float) (w.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                plank_umbra.render(6, start, 4);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Plank> it2 = om.layer1.planks.iterator();
        while (it2.hasNext()) {
            Plank w2 = it2.next();
            if (!w2.culled) {
                int start2 = (w2.size.x < 2.1f ? 0 : w2.size.x < 4.1f ? 1 : 2) * 4;
                Vector2 pos2 = w2.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos2.x, pos2.y, -0.499f);
                G.gl.glRotatef((float) (w2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                plank_umbra.render(6, start2 + 12, 4);
                G.gl.glPopMatrix();
            }
        }
        marble_umbra.bind();
        Iterator<Marble> it3 = om.layer0.marbles.iterator();
        while (it3.hasNext()) {
            Marble w3 = it3.next();
            if (!w3.culled) {
                Vector2 pos3 = w3.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos3.x, pos3.y, -0.499f);
                marble_umbra.render(6);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Marble> it4 = om.layer1.marbles.iterator();
        while (it4.hasNext()) {
            Marble w4 = it4.next();
            if (!w4.culled) {
                Vector2 pos4 = w4.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos4.x, pos4.y, -0.499f);
                marble_umbra.render(6);
                G.gl.glPopMatrix();
            }
        }
        wheel_umbra.bind();
        Iterator<Rope> it5 = om.ropes.iterator();
        while (it5.hasNext()) {
            Rope w5 = it5.next();
            if (!w5.culled) {
                render_projected_ropeend_umbra((RopeEnd) w5.g1);
                render_projected_ropeend_umbra((RopeEnd) w5.g2);
            }
        }
        Iterator<MetalWheel> it6 = om.layer0.metalwheels.iterator();
        while (it6.hasNext()) {
            Wheel w6 = it6.next();
            if (!w6.culled) {
                int start3 = (w6.size < 1.0f ? 0 : w6.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos5 = w6.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos5.x, pos5.y, -0.499f);
                wheel_umbra.render(6, start3, 25);
                G.gl.glPopMatrix();
            }
        }
        Iterator<MetalWheel> it7 = om.layer1.metalwheels.iterator();
        while (it7.hasNext()) {
            Wheel w7 = it7.next();
            if (!w7.culled) {
                int start4 = (w7.size < 1.0f ? 0 : w7.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos6 = w7.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos6.x, pos6.y, -0.499f);
                wheel_umbra.render(6, start4 + 75, 25);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Wheel> it8 = om.layer0.wheels.iterator();
        while (it8.hasNext()) {
            Wheel w8 = it8.next();
            if (!w8.culled) {
                int start5 = (w8.size < 1.0f ? 0 : w8.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos7 = w8.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos7.x, pos7.y, -0.499f);
                wheel_umbra.render(6, start5, 25);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Wheel> it9 = om.layer1.wheels.iterator();
        while (it9.hasNext()) {
            Wheel w9 = it9.next();
            if (!w9.culled) {
                int start6 = (w9.size < 1.0f ? 0 : w9.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos8 = w9.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos8.x, pos8.y, -0.499f);
                wheel_umbra.render(6, start6 + 75, 25);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Knob> it10 = om.layer0.knobs.iterator();
        while (it10.hasNext()) {
            Knob w10 = (Knob) it10.next();
            if (!w10.culled) {
                int start7 = (w10.size < 1.0f ? 0 : w10.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos9 = w10.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos9.x, pos9.y, -0.499f);
                wheel_umbra.render(6, start7, 25);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Knob> it11 = om.layer1.knobs.iterator();
        while (it11.hasNext()) {
            Knob w11 = it11.next();
            if (!w11.culled) {
                int start8 = (w11.size < 1.0f ? 0 : w11.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos10 = w11.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos10.x, pos10.y, -0.499f);
                wheel_umbra.render(6, start8 + 75, 25);
                G.gl.glPopMatrix();
            }
        }
        misc_umbra.bind();
        Iterator<DynamicMotor> it12 = om.layer0.dynamicmotors.iterator();
        while (it12.hasNext()) {
            DynamicMotor m = it12.next();
            if (!m.culled) {
                Vector2 pos11 = m.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos11.x, pos11.y, -0.499f);
                G.gl.glRotatef((float) (m.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
            }
        }
        Iterator<DynamicMotor> it13 = om.layer1.dynamicmotors.iterator();
        while (it13.hasNext()) {
            DynamicMotor m2 = it13.next();
            if (!m2.culled) {
                Vector2 pos12 = m2.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos12.x, pos12.y, -0.499f);
                G.gl.glRotatef((float) (m2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Panel> it14 = om.layer0.controllers.iterator();
        while (it14.hasNext()) {
            Panel m3 = it14.next();
            if (!m3.culled) {
                Vector2 pos13 = m3.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos13.x, pos13.y, -0.499f);
                G.gl.glRotatef((float) (m3.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(1.8f, 1.1f, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Battery> it15 = om.layer0.batteries.iterator();
        while (it15.hasNext()) {
            Battery b = it15.next();
            if (!b.culled) {
                Vector2 pos14 = b.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos14.x, pos14.y, -0.499f);
                G.gl.glRotatef((float) (b.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                if (b.size == 1) {
                    misc_umbra.render(6, 0, 4);
                } else {
                    misc_umbra.render(6, 8, 8);
                }
                G.gl.glPopMatrix();
            }
        }
        Iterator<Weight> it16 = om.layer0.weights.iterator();
        while (it16.hasNext()) {
            Weight b2 = it16.next();
            if (!b2.culled) {
                Vector2 pos15 = b2.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos15.x, pos15.y, -0.499f);
                G.gl.glRotatef((float) (b2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                misc_umbra.render(6, 4, 4);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Mine> it17 = om.layer0.mines.iterator();
        while (it17.hasNext()) {
            Mine b3 = it17.next();
            if (!b3.culled && !b3.triggered) {
                Vector2 pos16 = b3.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos16.x, pos16.y, -0.499f);
                G.gl.glRotatef((float) (b3.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.5f, 0.5f, 1.0f);
                misc_umbra.render(6, 4, 4);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Mine> it18 = om.layer1.mines.iterator();
        while (it18.hasNext()) {
            Mine b4 = it18.next();
            if (!b4.culled && !b4.triggered) {
                Vector2 pos17 = b4.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos17.x, pos17.y, -0.499f);
                G.gl.glRotatef((float) (b4.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.5f, 0.5f, 1.0f);
                misc_umbra.render(6, 4, 4);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Bucket> it19 = om.layer0.buckets.iterator();
        while (it19.hasNext()) {
            Bucket b5 = it19.next();
            if (!b5.culled) {
                Vector2 pos18 = b5.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos18.x, pos18.y, -0.499f);
                G.gl.glRotatef((float) (b5.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glTranslatef(0.0f, -0.15f, 0.0f);
                misc_umbra.render(6, 24, 4);
                G.gl.glPopMatrix();
            }
        }
        misc_umbra.unbind();
        metal_umbra.bind();
        if (om.layer1.bars.size() > 0) {
            Iterator<MetalBar> it20 = om.layer1.bars.iterator();
            while (it20.hasNext()) {
                MetalBar m4 = it20.next();
                if (!m4.culled) {
                    int start9 = (m4.size.x < 2.1f ? 0 : m4.size.x < 4.1f ? 1 : 2) * 4;
                    Vector2 pos19 = m4.get_state().position;
                    G.gl.glPushMatrix();
                    G.gl.glTranslatef(pos19.x, pos19.y, -0.499f);
                    G.gl.glRotatef((float) (m4.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                    metal_umbra.render(6, start9 + 12, 4);
                    G.gl.glPopMatrix();
                }
            }
        }
        Iterator<Damper> it21 = om.layer1.dampers.iterator();
        while (it21.hasNext()) {
            Damper w12 = it21.next();
            Gdx.app.log("render", "dsdsa");
            if (!w12.culled) {
                Gdx.app.log("render", "dsdsa");
                Vector2 pos20 = w12.get_position();
                float angle = w12.g2.body.getPosition().cpy().sub(w12.g1.body.getPosition()).angle();
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos20.x, pos20.y, -0.499f);
                G.gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                metal_umbra.render(6, 16, 4);
                G.gl.glPopMatrix();
            }
        }
        metal_umbra.unbind();
    }

    public static void render_projected_umbras(ObjectManager om) {
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.gl.glEnable(2929);
        G.gl.glDisable(2960);
        G.gl.glDisable(3042);
        G.gl.glColorMask(true, true, true, true);
        G.gl.glDepthMask(true);
        G.gl.glColor4f(0.39999998f, 0.39999998f, 0.39999998f, 1.0f);
        G.gl.glEnable(3553);
        Game.bgtex.bind();
        G.gl.glMatrixMode(5890);
        G.gl.glLoadIdentity();
        G.gl.glScalef(9.765625E-4f, -0.00390625f, 1.0f);
        G.gl.glScalef(56.0f, 14.0f, 1.0f);
        plank_umbra.bind();
        Iterator<Plank> it = om.layer0.planks.iterator();
        while (it.hasNext()) {
            Plank w = it.next();
            if (!w.culled) {
                int start = (w.size.x < 2.1f ? 0 : w.size.x < 4.1f ? 1 : 2) * 4;
                Vector2 pos = w.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos.x, pos.y, 0.0f);
                G.gl.glRotatef((float) (w.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos.x, pos.y, -0.499f);
                G.gl.glRotatef((float) (w.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                plank_umbra.render(6, start, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Plank> it2 = om.layer1.planks.iterator();
        while (it2.hasNext()) {
            Plank w2 = it2.next();
            if (!w2.culled) {
                int start2 = (w2.size.x < 2.1f ? 0 : w2.size.x < 4.1f ? 1 : 2) * 4;
                Vector2 pos2 = w2.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos2.x, pos2.y, 0.0f);
                G.gl.glRotatef((float) (w2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos2.x, pos2.y, -0.499f);
                G.gl.glRotatef((float) (w2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                plank_umbra.render(6, start2 + 12, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        marble_umbra.bind();
        Iterator<Marble> it3 = om.layer0.marbles.iterator();
        while (it3.hasNext()) {
            Marble w3 = it3.next();
            if (!w3.culled) {
                Vector2 pos3 = w3.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos3.x, pos3.y, 0.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos3.x, pos3.y, -0.499f);
                marble_umbra.render(6);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Marble> it4 = om.layer1.marbles.iterator();
        while (it4.hasNext()) {
            Marble w4 = it4.next();
            if (!w4.culled) {
                Vector2 pos4 = w4.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos4.x, pos4.y, 0.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos4.x, pos4.y, -0.499f);
                marble_umbra.render(6);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        wheel_umbra.bind();
        Iterator<Rope> it5 = om.ropes.iterator();
        while (it5.hasNext()) {
            Rope w5 = it5.next();
            if (!w5.culled) {
                render_projected_ropeend_umbra((RopeEnd) w5.g1);
                render_projected_ropeend_umbra((RopeEnd) w5.g2);
            }
        }
        Iterator<MetalWheel> it6 = om.layer0.metalwheels.iterator();
        while (it6.hasNext()) {
            Wheel w6 = it6.next();
            if (!w6.culled) {
                int start3 = (w6.size < 1.0f ? 0 : w6.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos5 = w6.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos5.x, pos5.y, 0.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos5.x, pos5.y, -0.499f);
                wheel_umbra.render(6, start3, 25);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<MetalWheel> it7 = om.layer1.metalwheels.iterator();
        while (it7.hasNext()) {
            Wheel w7 = it7.next();
            if (!w7.culled) {
                int start4 = (w7.size < 1.0f ? 0 : w7.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos6 = w7.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos6.x, pos6.y, 0.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos6.x, pos6.y, -0.499f);
                wheel_umbra.render(6, start4 + 75, 25);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Wheel> it8 = om.layer0.wheels.iterator();
        while (it8.hasNext()) {
            Wheel w8 = it8.next();
            if (!w8.culled) {
                int start5 = (w8.size < 1.0f ? 0 : w8.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos7 = w8.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos7.x, pos7.y, 0.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos7.x, pos7.y, -0.499f);
                wheel_umbra.render(6, start5, 25);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Wheel> it9 = om.layer1.wheels.iterator();
        while (it9.hasNext()) {
            Wheel w9 = it9.next();
            if (!w9.culled) {
                int start6 = (w9.size < 1.0f ? 0 : w9.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos8 = w9.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos8.x, pos8.y, 0.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos8.x, pos8.y, -0.499f);
                wheel_umbra.render(6, start6 + 75, 25);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Knob> it10 = om.layer0.knobs.iterator();
        while (it10.hasNext()) {
            Knob w10 = (Knob) it10.next();
            if (!w10.culled) {
                int start7 = (w10.size < 1.0f ? 0 : w10.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos9 = w10.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos9.x, pos9.y, 0.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos9.x, pos9.y, -0.499f);
                wheel_umbra.render(6, start7, 25);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Knob> it11 = om.layer1.knobs.iterator();
        while (it11.hasNext()) {
            Knob w11 = it11.next();
            if (!w11.culled) {
                int start8 = (w11.size < 1.0f ? 0 : w11.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos10 = w11.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos10.x, pos10.y, 0.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos10.x, pos10.y, -0.499f);
                wheel_umbra.render(6, start8 + 75, 25);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        misc_umbra.bind();
        Iterator<DynamicMotor> it12 = om.layer0.dynamicmotors.iterator();
        while (it12.hasNext()) {
            DynamicMotor m = it12.next();
            if (!m.culled) {
                Vector2 pos11 = m.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos11.x, pos11.y, 0.0f);
                G.gl.glRotatef((float) (m.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos11.x, pos11.y, -0.499f);
                G.gl.glRotatef((float) (m.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<DynamicMotor> it13 = om.layer1.dynamicmotors.iterator();
        while (it13.hasNext()) {
            DynamicMotor m2 = it13.next();
            if (!m2.culled) {
                Vector2 pos12 = m2.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos12.x, pos12.y, 0.0f);
                G.gl.glRotatef((float) (m2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos12.x, pos12.y, -0.499f);
                G.gl.glRotatef((float) (m2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Button> it14 = om.buttons.iterator();
        while (it14.hasNext()) {
            Button m3 = it14.next();
            if (!m3.culled) {
                Vector2 pos13 = m3.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos13.x, pos13.y, 0.0f);
                G.gl.glRotatef((float) (m3.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.8f, 0.4f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos13.x, pos13.y, -0.499f);
                G.gl.glRotatef((float) (m3.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.8f, 0.4f, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        if (!om.christmasgifts.isEmpty()) {
            Iterator<ChristmasGift> it15 = om.christmasgifts.iterator();
            while (it15.hasNext()) {
                ChristmasGift m4 = it15.next();
                if (!m4.culled) {
                    Vector2 pos14 = m4.get_state().position;
                    G.gl.glMatrixMode(5890);
                    G.gl.glPushMatrix();
                    G.gl.glTranslatef(pos14.x, pos14.y, 0.0f);
                    G.gl.glRotatef((float) (m4.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                    G.gl.glScalef(m4.size.x * 2.0f, m4.size.y * 2.0f, 1.0f);
                    G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                    G.gl.glPushMatrix();
                    G.gl.glTranslatef(pos14.x, pos14.y, -0.499f);
                    G.gl.glRotatef((float) (m4.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                    G.gl.glScalef(m4.size.x * 2.0f, m4.size.y * 2.0f, 1.0f);
                    misc_umbra.render(6, 0, 4);
                    G.gl.glPopMatrix();
                    G.gl.glMatrixMode(5890);
                    G.gl.glPopMatrix();
                }
            }
        }
        Iterator<Hub> it16 = om.hubs.iterator();
        while (it16.hasNext()) {
            Hub m5 = it16.next();
            if (!m5.culled) {
                Vector2 pos15 = m5.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos15.x, pos15.y, 0.0f);
                G.gl.glRotatef((float) (m5.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(2.0f, 0.5f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos15.x, pos15.y, -0.499f);
                G.gl.glRotatef((float) (m5.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(2.0f, 0.5f, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<RocketEngine> it17 = om.rocketengines.iterator();
        while (it17.hasNext()) {
            RocketEngine m6 = it17.next();
            if (!m6.culled) {
                Vector2 pos16 = m6.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos16.x, pos16.y, 0.0f);
                G.gl.glRotatef((float) (m6.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(1.0f, 2.0f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos16.x, pos16.y, -0.499f);
                G.gl.glRotatef((float) (m6.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(1.0f, 2.0f, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Panel> it18 = om.layer0.controllers.iterator();
        while (it18.hasNext()) {
            Panel m7 = it18.next();
            if (!m7.culled) {
                Vector2 pos17 = m7.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos17.x, pos17.y, 0.0f);
                G.gl.glRotatef((float) (m7.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(2.0f, SHADOW_FACTOR, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos17.x, pos17.y, -0.499f);
                G.gl.glRotatef((float) (m7.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(1.8f, 1.1f, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Battery> it19 = om.layer0.batteries.iterator();
        while (it19.hasNext()) {
            Battery b = it19.next();
            if (!b.culled) {
                Vector2 pos18 = b.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos18.x, pos18.y, 0.0f);
                G.gl.glRotatef((float) (b.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos18.x, pos18.y, -0.499f);
                G.gl.glRotatef((float) (b.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                if (b.size == 1) {
                    misc_umbra.render(6, 0, 4);
                } else {
                    misc_umbra.render(6, 8, 8);
                }
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Weight> it20 = om.layer0.weights.iterator();
        while (it20.hasNext()) {
            Weight b2 = it20.next();
            if (!b2.culled) {
                Vector2 pos19 = b2.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos19.x, pos19.y, 0.0f);
                G.gl.glRotatef((float) (b2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos19.x, pos19.y, -0.499f);
                G.gl.glRotatef((float) (b2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                misc_umbra.render(6, 4, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Mine> it21 = om.layer0.mines.iterator();
        while (it21.hasNext()) {
            Mine b3 = it21.next();
            if (!b3.culled && !b3.triggered) {
                Vector2 pos20 = b3.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos20.x, pos20.y, 0.0f);
                G.gl.glRotatef((float) (b3.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.5f, 0.5f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos20.x, pos20.y, -0.499f);
                G.gl.glRotatef((float) (b3.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.5f, 0.5f, 1.0f);
                misc_umbra.render(6, 4, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Mine> it22 = om.layer1.mines.iterator();
        while (it22.hasNext()) {
            Mine b4 = it22.next();
            if (!b4.culled && !b4.triggered) {
                Vector2 pos21 = b4.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos21.x, pos21.y, 0.0f);
                G.gl.glRotatef((float) (b4.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.5f, 0.5f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos21.x, pos21.y, -0.499f);
                G.gl.glRotatef((float) (b4.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.5f, 0.5f, 1.0f);
                misc_umbra.render(6, 4, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Bucket> it23 = om.layer0.buckets.iterator();
        while (it23.hasNext()) {
            Bucket b5 = it23.next();
            if (!b5.culled) {
                Vector2 pos22 = b5.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos22.x, pos22.y, 0.0f);
                G.gl.glRotatef((float) (b5.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glTranslatef(0.0f, -0.15f, 0.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos22.x, pos22.y, -0.499f);
                G.gl.glRotatef((float) (b5.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glTranslatef(0.0f, -0.15f, 0.0f);
                misc_umbra.render(6, 24, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        misc_umbra.unbind();
        metal_umbra.bind();
        Iterator<MetalBar> it24 = om.layer1.bars.iterator();
        while (it24.hasNext()) {
            MetalBar m8 = it24.next();
            if (!m8.culled) {
                int start9 = (m8.size.x < 2.1f ? 0 : m8.size.x < 4.1f ? 1 : 2) * 4;
                Vector2 pos23 = m8.get_state().position;
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos23.x, pos23.y, 0.0f);
                G.gl.glRotatef((float) (m8.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos23.x, pos23.y, -0.499f);
                G.gl.glRotatef((float) (m8.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                metal_umbra.render(6, start9 + 12, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Damper> it25 = om.layer1.dampers.iterator();
        while (it25.hasNext()) {
            Damper w12 = it25.next();
            if (!w12.culled) {
                Vector2 pos24 = w12.get_position();
                float angle = w12.g2.body.getPosition().cpy().sub(w12.g1.body.getPosition()).angle();
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos24.x, pos24.y, 0.0f);
                G.gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(((((PrismaticJoint) w12.joint1).getJointTranslation() + w12.size) + 0.5f) / 4.0f, 1.0f, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos24.x, pos24.y, -0.499f);
                G.gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(((((PrismaticJoint) w12.joint1).getJointTranslation() + w12.size) + 0.5f) / 4.0f, 1.0f, 1.0f);
                metal_umbra.render(6, 16, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Damper> it26 = om.layer0.dampers.iterator();
        while (it26.hasNext()) {
            Damper w13 = it26.next();
            if (!w13.culled) {
                Vector2 pos25 = w13.get_position();
                float angle2 = w13.g2.body.getPosition().cpy().sub(w13.g1.body.getPosition()).angle();
                G.gl.glMatrixMode(5890);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos25.x, pos25.y, 0.0f);
                G.gl.glRotatef(angle2, 0.0f, 0.0f, 1.0f);
                G.gl.glScalef((((((PrismaticJoint) w13.joint1).getJointTranslation() + w13.size) + 0.5f) / 4.0f) + 0.1f, SHADOW_FACTOR2, 1.0f);
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos25.x, pos25.y, -0.499f);
                G.gl.glRotatef(angle2, 0.0f, 0.0f, 1.0f);
                G.gl.glScalef((((((PrismaticJoint) w13.joint1).getJointTranslation() + w13.size) + 0.5f) / 4.0f) + 0.1f, SHADOW_FACTOR2, 1.0f);
                metal_umbra.render(6, 16, 4);
                G.gl.glPopMatrix();
                G.gl.glMatrixMode(5890);
                G.gl.glPopMatrix();
            }
        }
        metal_umbra.unbind();
        G.gl.glDisable(3553);
        G.gl.glMatrixMode(5890);
        G.gl.glLoadIdentity();
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    public static void render_projected_penumbras(ObjectManager om) {
        G.gl.glEnable(3042);
        G.gl.glDepthMask(false);
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        marble_penumbra.bind();
        Iterator<Marble> it = om.layer0.marbles.iterator();
        while (it.hasNext()) {
            Marble m = it.next();
            if (!m.culled) {
                Vector2 pos = m.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos.x, pos.y, -0.5f);
                marble_penumbra.render(5);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Marble> it2 = om.layer1.marbles.iterator();
        while (it2.hasNext()) {
            Marble m2 = it2.next();
            if (!m2.culled) {
                Vector2 pos2 = m2.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos2.x, pos2.y, -0.5f);
                marble_penumbra.render(5);
                G.gl.glPopMatrix();
            }
        }
        wheel_penumbra.bind();
        Iterator<StaticMotor> it3 = om.static_motors.iterator();
        while (it3.hasNext()) {
            StaticMotor w = it3.next();
            if (!w.culled) {
                Vector2 pos3 = w.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos3.x, pos3.y, -0.5f);
                G.gl.glScalef(1.4f, 1.4f, 1.0f);
                wheel_penumbra.render(5, 0, 50);
                G.gl.glPopMatrix();
            }
        }
        Iterator<MetalWheel> it4 = om.layer0.metalwheels.iterator();
        while (it4.hasNext()) {
            Wheel w2 = it4.next();
            if (!w2.culled) {
                int start = (w2.size < 1.0f ? 0 : w2.size < 2.0f ? 1 : 2) * 50;
                Vector2 pos4 = w2.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos4.x, pos4.y, -0.5f);
                wheel_penumbra.render(5, start, 50);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Rope> it5 = om.ropes.iterator();
        while (it5.hasNext()) {
            Rope w3 = it5.next();
            if (!w3.culled) {
                Vector2 pos5 = ((RopeEnd) w3.g1).get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos5.x, pos5.y, -0.5f);
                wheel_penumbra.render(5, ((RopeEnd) w3.g1).layer * Input.Keys.NUMPAD_6, 50);
                G.gl.glPopMatrix();
                Vector2 pos6 = ((RopeEnd) w3.g2).get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos6.x, pos6.y, -0.5f);
                wheel_penumbra.render(5, ((RopeEnd) w3.g2).layer * Input.Keys.NUMPAD_6, 50);
                G.gl.glPopMatrix();
            }
        }
        Iterator<MetalWheel> it6 = om.layer1.metalwheels.iterator();
        while (it6.hasNext()) {
            Wheel w4 = it6.next();
            if (!w4.culled) {
                int start2 = (w4.size < 1.0f ? 0 : w4.size < 2.0f ? 1 : 2) * 50;
                Vector2 pos7 = w4.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos7.x, pos7.y, -0.5f);
                wheel_penumbra.render(5, start2 + Input.Keys.NUMPAD_6, 50);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Wheel> it7 = om.layer0.wheels.iterator();
        while (it7.hasNext()) {
            Wheel w5 = it7.next();
            if (!w5.culled) {
                int start3 = (w5.size < 1.0f ? 0 : w5.size < 2.0f ? 1 : 2) * 50;
                Vector2 pos8 = w5.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos8.x, pos8.y, -0.5f);
                wheel_penumbra.render(5, start3, 50);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Wheel> it8 = om.layer1.wheels.iterator();
        while (it8.hasNext()) {
            Wheel w6 = it8.next();
            if (!w6.culled) {
                int start4 = (w6.size < 1.0f ? 0 : w6.size < 2.0f ? 1 : 2) * 50;
                Vector2 pos9 = w6.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos9.x, pos9.y, -0.5f);
                wheel_penumbra.render(5, start4 + Input.Keys.NUMPAD_6, 50);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Knob> it9 = om.layer0.knobs.iterator();
        while (it9.hasNext()) {
            Knob w7 = (Knob) it9.next();
            if (!w7.culled) {
                int start5 = (w7.size < 1.0f ? 0 : w7.size < 2.0f ? 1 : 2) * 50;
                Vector2 pos10 = w7.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos10.x, pos10.y, -0.5f);
                wheel_penumbra.render(5, start5, 50);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Knob> it10 = om.layer1.knobs.iterator();
        while (it10.hasNext()) {
            Knob w8 = it10.next();
            if (!w8.culled) {
                int start6 = (w8.size < 1.0f ? 0 : w8.size < 2.0f ? 1 : 2) * 50;
                Vector2 pos11 = w8.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos11.x, pos11.y, -0.5f);
                wheel_penumbra.render(5, start6 + Input.Keys.NUMPAD_6, 50);
                G.gl.glPopMatrix();
            }
        }
        metal_penumbra.bind();
        int sz = om.layer0.bars.size();
        for (int i = 0; i < sz; i++) {
            MetalBar w9 = om.layer0.bars.get(i);
            if (!w9.culled) {
                Vector2 pos12 = w9.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos12.x, pos12.y, -0.5f);
                G.gl.glRotatef((float) (w9.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                int start7 = (w9.size.x < 2.1f ? 0 : w9.size.x < 4.1f ? 1 : 2) * 30;
                metal_penumbra.render(5, (w9.layer * 5 * 2) + start7, 10);
                G.gl.glPopMatrix();
            }
        }
        int sz2 = om.layer1.bars.size();
        for (int i2 = 0; i2 < sz2; i2++) {
            MetalBar w10 = om.layer1.bars.get(i2);
            if (!w10.culled) {
                Vector2 pos13 = w10.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos13.x, pos13.y, -0.5f);
                G.gl.glRotatef((float) (w10.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                int start8 = (w10.size.x < 2.1f ? 0 : w10.size.x < 4.1f ? 1 : 2) * 30;
                metal_penumbra.render(5, (w10.layer * 5 * 2) + start8, 10);
                G.gl.glPopMatrix();
            }
        }
        int sz3 = om.layer1.dampers.size();
        for (int i3 = 0; i3 < sz3; i3++) {
            Damper w11 = om.layer1.dampers.get(i3);
            if (!w11.culled) {
                Vector2 pos14 = w11.get_position();
                float angle = w11.g2.body.getPosition().cpy().sub(w11.g1.body.getPosition()).angle();
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos14.x, pos14.y, -0.5f);
                G.gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(((((PrismaticJoint) w11.joint1).getJointTranslation() + w11.size) + 0.5f) / 2.0f, 1.0f, 1.0f);
                metal_penumbra.render(5, (w11.layer * 5 * 2) + 0, 10);
                G.gl.glPopMatrix();
            }
        }
        int sz4 = om.layer0.dampers.size();
        for (int i4 = 0; i4 < sz4; i4++) {
            Damper w12 = om.layer0.dampers.get(i4);
            if (!w12.culled) {
                Vector2 pos15 = w12.get_position();
                float angle2 = w12.g2.body.getPosition().cpy().sub(w12.g1.body.getPosition()).angle();
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos15.x, pos15.y, -0.5f);
                G.gl.glRotatef(angle2, 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(((((PrismaticJoint) w12.joint1).getJointTranslation() + w12.size) + 0.5f) / 2.0f, 1.0f, 1.0f);
                metal_penumbra.render(5, (w12.layer * 5 * 2) + 0, 10);
                G.gl.glPopMatrix();
            }
        }
        plank_penumbra.bind();
        Iterator<Plank> it11 = om.layer0.planks.iterator();
        while (it11.hasNext()) {
            Plank w13 = it11.next();
            if (!w13.culled) {
                Vector2 pos16 = w13.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos16.x, pos16.y, -0.5f);
                G.gl.glRotatef((float) (w13.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                int start9 = (w13.size.x < 2.1f ? 0 : w13.size.x < 4.1f ? 1 : 2) * 30;
                plank_penumbra.render(5, (w13.layer * 5 * 2) + start9, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Plank> it12 = om.layer1.planks.iterator();
        while (it12.hasNext()) {
            Plank w14 = it12.next();
            if (!w14.culled) {
                Vector2 pos17 = w14.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos17.x, pos17.y, -0.5f);
                G.gl.glRotatef((float) (w14.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                int start10 = (w14.size.x < 2.1f ? 0 : w14.size.x < 4.1f ? 1 : 2) * 30;
                plank_penumbra.render(5, (w14.layer * 5 * 2) + start10, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Plank> it13 = om.layer2.planks.iterator();
        while (it13.hasNext()) {
            Plank w15 = it13.next();
            if (!w15.culled) {
                Vector2 pos18 = w15.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos18.x, pos18.y, -0.5f);
                G.gl.glRotatef((float) (w15.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                int start11 = (w15.size.x < 2.1f ? 0 : w15.size.x < 4.1f ? 1 : 2) * 30;
                plank_penumbra.render(5, (w15.layer * 5 * 2) + start11, 10);
                G.gl.glPopMatrix();
            }
        }
        misc_penumbra.bind();
        Iterator<RocketEngine> it14 = om.rocketengines.iterator();
        while (it14.hasNext()) {
            RocketEngine w16 = it14.next();
            if (!w16.culled) {
                Vector2 pos19 = w16.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos19.x, pos19.y, -0.5f);
                G.gl.glRotatef((float) (w16.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(1.0f, 2.0f, 1.0f);
                misc_penumbra.render(5, 0, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Hub> it15 = om.hubs.iterator();
        while (it15.hasNext()) {
            Hub w17 = it15.next();
            if (!w17.culled) {
                Vector2 pos20 = w17.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos20.x, pos20.y, -0.5f);
                G.gl.glRotatef((float) (w17.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(2.0f, 0.5f, 1.0f);
                misc_penumbra.render(5, 0, 10);
                G.gl.glPopMatrix();
            }
        }
        if (!om.christmasgifts.isEmpty()) {
            Iterator<ChristmasGift> it16 = om.christmasgifts.iterator();
            while (it16.hasNext()) {
                ChristmasGift w18 = it16.next();
                if (!w18.culled) {
                    Vector2 pos21 = w18.get_state().position;
                    G.gl.glPushMatrix();
                    G.gl.glTranslatef(pos21.x, pos21.y, -0.5f);
                    G.gl.glRotatef((float) (w18.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                    G.gl.glScalef(w18.size.x * 2.0f, w18.size.y * 2.0f, 1.0f);
                    misc_penumbra.render(5, 0, 10);
                    G.gl.glPopMatrix();
                }
            }
        }
        Iterator<Button> it17 = om.buttons.iterator();
        while (it17.hasNext()) {
            Button w19 = it17.next();
            if (!w19.culled) {
                Vector2 pos22 = w19.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos22.x, pos22.y, -0.5f);
                G.gl.glRotatef((float) (w19.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.8f, 0.4f, 1.0f);
                misc_penumbra.render(5, 0, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<DynamicMotor> it18 = om.layer0.dynamicmotors.iterator();
        while (it18.hasNext()) {
            DynamicMotor w20 = it18.next();
            if (!w20.culled) {
                Vector2 pos23 = w20.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos23.x, pos23.y, -0.5f);
                G.gl.glRotatef((float) (w20.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                misc_penumbra.render(5, 0, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<DynamicMotor> it19 = om.layer1.dynamicmotors.iterator();
        while (it19.hasNext()) {
            DynamicMotor w21 = it19.next();
            if (!w21.culled) {
                Vector2 pos24 = w21.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos24.x, pos24.y, -0.5f);
                G.gl.glRotatef((float) (w21.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                misc_penumbra.render(5, 0, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Panel> it20 = om.layer0.controllers.iterator();
        while (it20.hasNext()) {
            Panel w22 = it20.next();
            if (!w22.culled) {
                Vector2 pos25 = w22.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos25.x, pos25.y, -0.5f);
                G.gl.glRotatef((float) (w22.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(1.8f, 1.1f, 1.0f);
                misc_penumbra.render(5, 0, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Battery> it21 = om.layer0.batteries.iterator();
        while (it21.hasNext()) {
            Battery w23 = it21.next();
            if (!w23.culled) {
                Vector2 pos26 = w23.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos26.x, pos26.y, -0.5f);
                G.gl.glRotatef((float) (w23.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                if (w23.size == 1) {
                    misc_penumbra.render(5, 0, 10);
                } else {
                    misc_penumbra.render(5, 20, 18);
                }
                G.gl.glPopMatrix();
            }
        }
        Iterator<Weight> it22 = om.layer0.weights.iterator();
        while (it22.hasNext()) {
            Weight w24 = it22.next();
            if (!w24.culled) {
                Vector2 pos27 = w24.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos27.x, pos27.y, -0.5f);
                G.gl.glRotatef((float) (w24.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                misc_penumbra.render(5, 10, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Mine> it23 = om.layer0.mines.iterator();
        while (it23.hasNext()) {
            Mine w25 = it23.next();
            if (!w25.culled && !w25.triggered) {
                Vector2 pos28 = w25.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos28.x, pos28.y, -0.5f);
                G.gl.glRotatef((float) (w25.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.5f, 0.5f, 1.0f);
                misc_penumbra.render(5, 10, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Mine> it24 = om.layer1.mines.iterator();
        while (it24.hasNext()) {
            Mine w26 = it24.next();
            if (!w26.culled && !w26.triggered) {
                Vector2 pos29 = w26.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos29.x, pos29.y, -0.5f);
                G.gl.glRotatef((float) (w26.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(0.5f, 0.5f, 1.0f);
                misc_penumbra.render(5, 10, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Bucket> it25 = om.layer0.buckets.iterator();
        while (it25.hasNext()) {
            Bucket b = it25.next();
            if (!b.culled) {
                Vector2 pos30 = b.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos30.x, pos30.y, -0.5f);
                G.gl.glRotatef((float) (b.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glTranslatef(0.0f, -0.15f, 0.0f);
                misc_penumbra.render(5, 56, 10);
                G.gl.glPopMatrix();
            }
        }
        misc_penumbra.unbind();
        G.gl.glDepthMask(true);
        G.gl.glDisable(3042);
    }

    private static void render_layer1_shadow_projections(ObjectManager om, float depth) {
        G.gl.glDepthFunc(518);
        G.gl.glEnable(3042);
        G.gl.glColor4f(0.0f, 0.0f, 0.0f, SHADOW_COLOR);
        G.gl.glDepthMask(false);
        ObjectManager.Layer l = om.layer2;
        plank_umbra.bind();
        Iterator<Plank> it = l.planks.iterator();
        while (it.hasNext()) {
            Plank w = it.next();
            if (!w.culled) {
                int start = (w.size.x < 2.1f ? 0 : w.size.x < 4.1f ? 1 : 2) * 4;
                Vector2 pos = w.get_state().position;
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos.x, pos.y, depth);
                G.gl.glRotatef((float) (w.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                plank_umbra.render(6, start, 4);
                G.gl.glPopMatrix();
            }
        }
        plank_penumbra.bind();
        G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        Iterator<Plank> it2 = l.planks.iterator();
        while (it2.hasNext()) {
            Plank w2 = it2.next();
            if (!w2.culled) {
                Vector2 pos2 = w2.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos2.x, pos2.y, depth);
                G.gl.glRotatef((float) (w2.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                int start2 = (w2.size.x < 2.1f ? 0 : w2.size.x < 4.1f ? 1 : 2) * 30;
                plank_penumbra.render(5, ((w2.layer - 2) * 5 * 2) + start2, 10);
                G.gl.glPopMatrix();
            }
        }
        plank_penumbra.unbind();
        G.gl.glDepthMask(true);
        G.gl.glDepthFunc(513);
        G.gl.glDisable(3042);
    }

    private static void render_layer0_shadow_projections(ObjectManager om, float depth) {
        G.gl.glDepthFunc(518);
        G.gl.glEnable(3042);
        G.gl.glColor4f(0.0f, 0.0f, 0.0f, SHADOW_COLOR);
        G.gl.glDepthMask(false);
        ObjectManager.Layer l = om.layer1;
        misc_umbra.bind();
        Iterator<DynamicMotor> it = om.layer1.dynamicmotors.iterator();
        while (it.hasNext()) {
            DynamicMotor m = it.next();
            if (!m.culled) {
                Vector2 pos = m.get_state().position;
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos.x, pos.y, depth);
                G.gl.glRotatef((float) (m.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                misc_umbra.render(6, 0, 4);
                G.gl.glPopMatrix();
            }
        }
        wheel_umbra.bind();
        Iterator<MetalWheel> it2 = om.layer1.metalwheels.iterator();
        while (it2.hasNext()) {
            Wheel w = it2.next();
            if (!w.culled) {
                int start = (w.size < 1.0f ? 0 : w.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos2 = w.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos2.x, pos2.y, depth);
                wheel_umbra.render(6, start, 25);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Wheel> it3 = om.layer1.wheels.iterator();
        while (it3.hasNext()) {
            Wheel w2 = it3.next();
            if (!w2.culled) {
                int start2 = (w2.size < 1.0f ? 0 : w2.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos3 = w2.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos3.x, pos3.y, depth);
                wheel_umbra.render(6, start2, 25);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Knob> it4 = om.layer1.knobs.iterator();
        while (it4.hasNext()) {
            Knob w3 = (Knob) it4.next();
            if (!w3.culled) {
                int start3 = (w3.size < 1.0f ? 0 : w3.size < 2.0f ? 1 : 2) * 25;
                Vector2 pos4 = w3.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos4.x, pos4.y, depth);
                wheel_umbra.render(6, start3, 25);
                G.gl.glPopMatrix();
            }
        }
        plank_umbra.bind();
        Iterator<Plank> it5 = l.planks.iterator();
        while (it5.hasNext()) {
            Plank w4 = it5.next();
            if (!w4.culled) {
                int start4 = (w4.size.x < 2.1f ? 0 : w4.size.x < 4.1f ? 1 : 2) * 4;
                Vector2 pos5 = w4.get_state().position;
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos5.x, pos5.y, depth);
                G.gl.glRotatef((float) (w4.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                plank_umbra.render(6, start4, 4);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Plank> it6 = om.layer2.planks.iterator();
        while (it6.hasNext()) {
            Plank w5 = it6.next();
            if (!w5.culled) {
                int start5 = (w5.size.x < 2.1f ? 0 : w5.size.x < 4.1f ? 1 : 2) * 4;
                Vector2 pos6 = w5.get_state().position;
                G.gl.glMatrixMode(GL10.GL_MODELVIEW);
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos6.x, pos6.y, depth);
                G.gl.glRotatef((float) (w5.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                plank_umbra.render(6, start5 + 12, 4);
                G.gl.glPopMatrix();
            }
        }
        plank_penumbra.bind();
        G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        Iterator<Plank> it7 = om.layer1.planks.iterator();
        while (it7.hasNext()) {
            Plank w6 = it7.next();
            if (!w6.culled) {
                Vector2 pos7 = w6.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos7.x, pos7.y, depth);
                G.gl.glRotatef((float) (w6.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                int start6 = (w6.size.x < 2.1f ? 0 : w6.size.x < 4.1f ? 1 : 2) * 30;
                plank_penumbra.render(5, ((w6.layer - 1) * 5 * 2) + start6, 10);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Plank> it8 = om.layer2.planks.iterator();
        while (it8.hasNext()) {
            Plank w7 = it8.next();
            if (!w7.culled) {
                Vector2 pos8 = w7.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos8.x, pos8.y, depth);
                G.gl.glRotatef((float) (w7.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                int start7 = (w7.size.x < 2.1f ? 0 : w7.size.x < 4.1f ? 1 : 2) * 30;
                plank_penumbra.render(5, ((w7.layer - 1) * 5 * 2) + start7, 10);
                G.gl.glPopMatrix();
            }
        }
        wheel_penumbra.bind();
        Iterator<MetalWheel> it9 = om.layer1.metalwheels.iterator();
        while (it9.hasNext()) {
            Wheel w8 = it9.next();
            if (!w8.culled) {
                int start8 = (w8.size < 1.0f ? 0 : w8.size < 2.0f ? 1 : 2) * 50;
                Vector2 pos9 = w8.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos9.x, pos9.y, depth);
                wheel_penumbra.render(5, start8, 50);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Wheel> it10 = om.layer1.wheels.iterator();
        while (it10.hasNext()) {
            Wheel w9 = it10.next();
            if (!w9.culled) {
                int start9 = (w9.size < 1.0f ? 0 : w9.size < 2.0f ? 1 : 2) * 50;
                Vector2 pos10 = w9.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos10.x, pos10.y, depth);
                wheel_penumbra.render(5, start9, 50);
                G.gl.glPopMatrix();
            }
        }
        Iterator<Knob> it11 = om.layer1.knobs.iterator();
        while (it11.hasNext()) {
            Knob w10 = (Knob) it11.next();
            if (!w10.culled) {
                int start10 = (w10.size < 1.0f ? 0 : w10.size < 2.0f ? 1 : 2) * 50;
                Vector2 pos11 = w10.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos11.x, pos11.y, depth);
                wheel_penumbra.render(5, start10, 50);
                G.gl.glPopMatrix();
            }
        }
        misc_penumbra.bind();
        Iterator<DynamicMotor> it12 = om.layer1.dynamicmotors.iterator();
        while (it12.hasNext()) {
            DynamicMotor w11 = it12.next();
            if (!w11.culled) {
                Vector2 pos12 = w11.get_state().position;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos12.x, pos12.y, depth);
                G.gl.glRotatef((float) (w11.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(SHADOW_FACTOR, SHADOW_FACTOR, 1.0f);
                misc_penumbra.render(5, 0, 10);
                G.gl.glPopMatrix();
            }
        }
        misc_penumbra.unbind();
        G.gl.glDepthFunc(513);
        G.gl.glDisable(3042);
        G.gl.glDepthMask(true);
    }

    private static void render_layer_bottom(ObjectManager.Layer layer, int layer_n, ObjectManager om) {
        G.gl.glDisable(3553);
        Marble._init_materials();
        MiscRenderer.spheremesh.setAutoBind(false);
        MiscRenderer.spheremesh.bind();
        G.gl.glDisable(GL10.GL_NORMALIZE);
        render_marbles(layer.marbles);
        G.gl.glEnable(GL10.GL_NORMALIZE);
        MiscRenderer.spheremesh.setAutoBind(true);
        if (layer.batteries.size() > 0 || (layer_n == 0 && !om.buttons.isEmpty())) {
            MiscRenderer.Acubemesh.setAutoBind(false);
            MiscRenderer.Acubemesh.bind();
            Iterator<Battery> it = layer.batteries.iterator();
            while (it.hasNext()) {
                Battery m = it.next();
                m.render_btn();
            }
            if (layer_n == 0) {
                Iterator<Button> it2 = om.buttons.iterator();
                while (it2.hasNext()) {
                    Button m2 = it2.next();
                    m2.render_btn();
                }
            }
            MiscRenderer.Acubemesh.setAutoBind(true);
        }
        G.gl.glEnable(3553);
        Plank.texture.bind();
        Plank.init_materials();
        if (layer_n < 2 && om.ropes.size() > 0) {
            MiscRenderer.Acylindermesh.setAutoBind(false);
            MiscRenderer.Acylindermesh.bind();
            Iterator<Rope> it3 = om.ropes.iterator();
            while (it3.hasNext()) {
                Rope r = it3.next();
                if (r.g1.layer == layer_n) {
                    ((RopeEnd) r.g1).render_inner_half();
                }
                if (r.g2.layer == layer_n) {
                    ((RopeEnd) r.g2).render_inner_half();
                }
            }
            MiscRenderer.Acylindermesh.setAutoBind(true);
        }
        MiscRenderer.Aplankmesh.setAutoBind(false);
        MiscRenderer.Aplankmesh.bind();
        if (Game.enable_hqmeshes) {
            G.gl.glDisable(GL10.GL_NORMALIZE);
            render_planks(layer.planks);
            G.gl.glEnable(GL10.GL_NORMALIZE);
        } else {
            render_planks_lq(layer.planks);
        }
        MiscRenderer.Aplankmesh.setAutoBind(true);
        MiscRenderer.Acubemesh.setAutoBind(false);
        MiscRenderer.Acubemesh.bind();
        if (layer_n == 0) {
            Iterator<RocketEngine> it4 = om.rocketengines.iterator();
            while (it4.hasNext()) {
                RocketEngine m3 = it4.next();
                m3.render_box();
            }
            Iterator<Hub> it5 = om.hubs.iterator();
            while (it5.hasNext()) {
                Hub m4 = it5.next();
                m4.render_box();
            }
        }
        if (!layer.dampers.isEmpty()) {
            Iterator<Damper> it6 = layer.dampers.iterator();
            while (it6.hasNext()) {
                Damper x = it6.next();
                x.render_edge_boxes();
            }
        }
        Iterator<DynamicMotor> it7 = layer.dynamicmotors.iterator();
        while (it7.hasNext()) {
            DynamicMotor m5 = it7.next();
            m5.render_box();
        }
        if (!layer.controllers.isEmpty()) {
            Panel.init_materials();
            Iterator<Panel> it8 = layer.controllers.iterator();
            while (it8.hasNext()) {
                Panel m6 = it8.next();
                m6.render_box();
            }
        }
        MetalBar.init_materials();
        Weight._texture.bind();
        Iterator<Battery> it9 = layer.batteries.iterator();
        while (it9.hasNext()) {
            Battery m7 = it9.next();
            m7.render();
        }
        if (!layer.dampers.isEmpty()) {
            Iterator<Damper> it10 = layer.dampers.iterator();
            while (it10.hasNext()) {
                Damper d = it10.next();
                d.render_edges();
            }
        }
        if (layer_n == 0) {
            Iterator<Button> it11 = om.buttons.iterator();
            while (it11.hasNext()) {
                Button m8 = it11.next();
                m8.render();
            }
            Iterator<RocketEngine> it12 = om.rocketengines.iterator();
            while (it12.hasNext()) {
                RocketEngine e = it12.next();
                e.render();
            }
            if (!om.christmasgifts.isEmpty()) {
                ChristmasGift.texture.bind();
                Iterator<ChristmasGift> it13 = om.christmasgifts.iterator();
                while (it13.hasNext()) {
                    ChristmasGift g = it13.next();
                    g.render();
                }
            }
        }
        MiscRenderer.Acubemesh.unbind();
        MiscRenderer.Acubemesh.setAutoBind(true);
    }

    private static void render_layer_top(ObjectManager.Layer layer, int layer_n, ObjectManager om) {
        MiscRenderer.Acylindermesh.setAutoBind(false);
        MiscRenderer.Acylindermesh.bind();
        if (!layer.knobs.isEmpty()) {
            G.gl.glDisable(3553);
            G.gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            Knob.init_materials();
            if (Game.enable_hqmeshes) {
                render_knobs(layer.knobs);
            } else {
                render_knobs_lq(layer.knobs);
            }
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            G.gl.glEnable(3553);
        }
        Plank.init_materials();
        Plank.texture.bind();
        if (Game.enable_hqmeshes) {
            render_wheels(layer.wheels);
        } else {
            render_wheels_lq(layer.wheels);
        }
        Iterator<Rope> it = om.ropes.iterator();
        while (it.hasNext()) {
            Rope r = it.next();
            if (r.g1.layer == layer_n) {
                ((RopeEnd) r.g1).render();
            }
            if (r.g2.layer == layer_n) {
                ((RopeEnd) r.g2).render();
            }
        }
        if (Game.enable_hqmeshes) {
            render_metalwheels_inner(layer.metalwheels);
        } else {
            render_metalwheels_inner_lq(layer.metalwheels);
        }
        Weight._texture.bind();
        MetalBar.init_materials();
        if (Game.enable_hqmeshes) {
            render_metalwheels(layer.metalwheels);
        } else {
            render_metalwheels_lq(layer.metalwheels);
        }
        MiscRenderer.Acylindermesh.setAutoBind(true);
        MetalBar.texture.bind();
        if (Game.enable_hqmeshes) {
            G.gl.glDisable(GL10.GL_NORMALIZE);
        }
        MiscRenderer.Ametalmesh.setAutoBind(false);
        MiscRenderer.Ametalmesh.bind();
        if (Game.enable_hqmeshes) {
            render_metalbars(layer.bars);
        } else {
            render_metalbars_lq(layer.bars);
        }
        MiscRenderer.Ametalmesh.setAutoBind(true);
        G.gl.glEnable(GL10.GL_NORMALIZE);
        Weight._texture.bind();
        MiscRenderer.Acubemesh.setAutoBind(false);
        MiscRenderer.Acubemesh.bind();
        Iterator<Panel> it2 = layer.controllers.iterator();
        while (it2.hasNext()) {
            Panel m = it2.next();
            m.render_sockets();
        }
        if (layer_n == 0) {
            Iterator<Hub> it3 = om.hubs.iterator();
            while (it3.hasNext()) {
                Hub m2 = it3.next();
                m2.render_sockets();
            }
        }
        if (layer.buckets.size() > 0) {
            if (ApparatusApp.game.background_n == 10) {
                Marble._init_diffuse_christmas_materials();
            } else {
                Marble._init_diffuse_materials();
            }
            G.gl.glDisable(3553);
            int sz = layer.buckets.size();
            ArrayList<Bucket> buckets = layer.buckets;
            for (int x = 0; x < sz; x++) {
                Bucket m3 = buckets.get(x);
                if (!m3.culled) {
                    m3.render();
                }
            }
            G.gl.glEnable(3553);
            MetalBar.init_materials();
        }
        MiscRenderer.Acubemesh.unbind();
        MiscRenderer.Acubemesh.setAutoBind(true);
        if (!layer.weights.isEmpty() || !layer.mines.isEmpty()) {
            Weight._mesh.setAutoBind(false);
            Weight._mesh.bind();
            Iterator<Weight> it4 = layer.weights.iterator();
            while (it4.hasNext()) {
                Weight m4 = it4.next();
                m4.render();
            }
            if (!layer.mines.isEmpty()) {
                Mine._texture.bind();
                Mine.init_materials();
                Iterator<Mine> it5 = layer.mines.iterator();
                while (it5.hasNext()) {
                    Mine m5 = it5.next();
                    m5.render();
                }
            }
            Weight._mesh.setAutoBind(true);
        }
    }

    public static void render_reflections(ArrayList<Hinge> hinges, ObjectManager om) {
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.gl.glEnable(2929);
        G.gl.glDepthMask(false);
        G.gl.glColorMask(false, false, false, false);
        G.gl.glEnable(2960);
        G.gl.glStencilFunc(519, 1, 255);
        G.gl.glDisable(3553);
        G.gl.glDisable(3042);
        G.gl.glDepthFunc(515);
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        G.gl.glStencilOp(7680, 7680, 7681);
        MiscRenderer.boxmesh.setAutoBind(false);
        MiscRenderer.boxmesh.bind();
        ArrayList<MetalBar> bars = om.layer0.bars;
        int sz = bars.size();
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        for (int x = 0; x < sz; x++) {
            MetalBar b = bars.get(x);
            if (!b.culled) {
                BaseObject.State s = b.get_state();
                Vector2 pos = s.position;
                float angle = s.angle;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos.x, pos.y, (((-0.5f) + b.layer) + b.z) - 0.05f);
                G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(((b.size.x / 2.0f) * 0.98f) + 0.015f, ((b.size.y / 2.0f) * 0.92f) + 0.03f, 1.0f);
                MiscRenderer.boxmesh.render(6);
                G.gl.glPopMatrix();
            }
        }
        ArrayList<MetalBar> bars2 = om.layer1.bars;
        int sz2 = bars2.size();
        for (int x2 = 0; x2 < sz2; x2++) {
            MetalBar b2 = bars2.get(x2);
            if (!b2.culled) {
                BaseObject.State s2 = b2.get_state();
                Vector2 pos2 = s2.position;
                float angle2 = s2.angle;
                G.gl.glPushMatrix();
                G.gl.glTranslatef(pos2.x, pos2.y, (((-0.5f) + (b2.layer * 1.0f)) + b2.z) - 0.05f);
                G.gl.glRotatef((float) (angle2 * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                G.gl.glScalef(((b2.size.x / 2.0f) * 0.98f) + 0.015f, ((b2.size.y / 2.0f) * 0.92f) + 0.03f, 1.0f);
                MiscRenderer.boxmesh.render(6);
                G.gl.glPopMatrix();
            }
        }
        G.gl.glDisable(2929);
        G.gl.glDepthMask(false);
        G.gl.glColorMask(true, true, true, true);
        G.gl.glEnable(3553);
        Game.newbgtex.bind();
        G.gl.glStencilFunc(514, 1, 255);
        G.gl.glStencilOp(7680, 7680, 7680);
        G.gl.glPushMatrix();
        G.gl.glScalef(512.0f, 128.0f, 1.0f);
        G.gl.glTranslatef(0.0f, 0.0f, -20.0f);
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.gl.glMatrixMode(5890);
        G.gl.glScalef(2.0f, 1.0f, 1.0f);
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, SHADOW_COLOR);
        G.gl.glEnable(3042);
        G.gl.glBlendFunc(770, 771);
        G.gl.glStencilMask(0);
        MiscRenderer.boxmesh.render(6);
        G.gl.glStencilMask(255);
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        G.gl.glMatrixMode(5890);
        G.gl.glLoadIdentity();
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        G.gl.glPopMatrix();
        MiscRenderer.boxmesh.unbind();
        MiscRenderer.boxmesh.setAutoBind(true);
        G.gl.glDisable(2960);
    }

    public static void render_scene(Camera cam, ArrayList<Hinge> hinges, ObjectManager om, boolean render_bg) {
        G.gl.glDisable(3042);
        if (om.static_motors.size() > 0) {
            G.gl.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
            MiscRenderer.cylindermesh.setAutoBind(false);
            MiscRenderer.cylindermesh.bind();
            Iterator<StaticMotor> it = om.static_motors.iterator();
            while (it.hasNext()) {
                StaticMotor m = it.next();
                m.render();
            }
            MiscRenderer.cylindermesh.unbind();
            MiscRenderer.cylindermesh.setAutoBind(true);
        }
        G.gl.glEnable(3553);
        G.gl.glEnable(GL10.GL_LIGHTING);
        G.gl.glEnable(GL10.GL_NORMALIZE);
        render_layer_bottom(om.layer0, 0, om);
        G.gl.glDisable(GL10.GL_NORMALIZE);
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.gl.glDisable(3553);
        if (Game.enable_shadows) {
            render_layer0_shadow_projections(om, SHADOW_FACTOR);
        }
        G.gl.glEnable(GL10.GL_LIGHTING);
        G.gl.glEnable(3553);
        G.gl.glEnable(GL10.GL_NORMALIZE);
        render_layer_top(om.layer0, 0, om);
        G.gl.glDisable(GL10.GL_NORMALIZE);
        G.gl.glDisable(GL10.GL_LIGHTING);
        if (om.layer0.batteries.size() > 0) {
            render_battery_decos(om.layer0.batteries);
        }
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (Game.enable_shadows) {
            render_layer0_shadow_projections(om, 1.4f);
        }
        G.gl.glDisable(3553);
        G.gl.glDisable(3042);
        MiscRenderer.cubemesh.setAutoBind(false);
        MiscRenderer.cubemesh.bind();
        Iterator<Cable> it2 = om.cables.iterator();
        while (it2.hasNext()) {
            Cable c = it2.next();
            c.render_edges();
        }
        Iterator<PanelCable> it3 = om.pcables.iterator();
        while (it3.hasNext()) {
            PanelCable r = it3.next();
            r.render_edges();
        }
        G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        Iterator<StaticMotor> it4 = om.static_motors.iterator();
        while (it4.hasNext()) {
            StaticMotor m2 = it4.next();
            m2.render_box();
        }
        MiscRenderer.cubemesh.unbind();
        MiscRenderer.cubemesh.setAutoBind(true);
        G.gl.glEnable(GL10.GL_LIGHTING);
        G.gl.glEnable(GL10.GL_NORMALIZE);
        G.gl.glEnable(3553);
        render_layer_bottom(om.layer1, 1, om);
        G.gl.glDisable(GL10.GL_NORMALIZE);
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.gl.glDisable(3553);
        if (Game.enable_shadows) {
            render_layer1_shadow_projections(om, 2.0f);
        }
        G.gl.glEnable(GL10.GL_LIGHTING);
        G.gl.glEnable(3553);
        G.gl.glEnable(GL10.GL_NORMALIZE);
        render_layer_top(om.layer1, 1, om);
        G.gl.glDisable(GL10.GL_NORMALIZE);
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.gl.glDisable(3553);
        if (Game.enable_shadows) {
            render_layer1_shadow_projections(om, 2.35f);
        }
        G.gl.glEnable(GL10.GL_LIGHTING);
        G.gl.glEnable(3553);
        G.gl.glEnable(GL10.GL_NORMALIZE);
        render_layer_bottom(om.layer2, 2, om);
        G.gl.glDisable(3553);
        MetalBar.init_materials();
        G.gl.glDisable(3042);
        MiscRenderer.smallcylindermesh.setAutoBind(false);
        MiscRenderer.smallcylindermesh.bind();
        Iterator<Hinge> it5 = hinges.iterator();
        while (it5.hasNext()) {
            Hinge h = it5.next();
            h.render();
        }
        Iterator<StaticMotor> it6 = om.static_motors.iterator();
        while (it6.hasNext()) {
            StaticMotor m3 = it6.next();
            m3.render_hinge();
        }
        Iterator<DynamicMotor> it7 = om.layer1.dynamicmotors.iterator();
        while (it7.hasNext()) {
            DynamicMotor m4 = it7.next();
            m4.render_hinge();
        }
        Iterator<DynamicMotor> it8 = om.layer0.dynamicmotors.iterator();
        while (it8.hasNext()) {
            DynamicMotor m5 = it8.next();
            m5.render_hinge();
        }
        G.gl.glEnable(GL10.GL_NORMALIZE);
        MiscRenderer.smallcylindermesh.unbind();
        MiscRenderer.smallcylindermesh.setAutoBind(true);
        G.gl.glDisable(GL10.GL_NORMALIZE);
        G.gl.glDisable(GL10.GL_LIGHTING);
        G.gl.glEnable(3553);
        if (Game.enable_shadows) {
            render_projected_umbras(om);
        }
        G.gl.glEnable(3553);
        if (render_bg) {
            Game.bgtex.bind();
            G.gl.glPushMatrix();
            G.gl.glScalef(512.0f, 128.0f, 1.0f);
            G.gl.glTranslatef(0.0f, 0.0f, -0.6f);
            G.gl.glMatrixMode(5890);
            G.gl.glScalef(56.0f, 14.0f, 1.0f);
            G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            G.gl.glDepthMask(false);
            MiscRenderer.boxmesh.render(6);
        }
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        G.gl.glMatrixMode(5890);
        G.gl.glLoadIdentity();
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        G.gl.glPopMatrix();
        MiscRenderer.boxmesh.setAutoBind(false);
        MiscRenderer.boxmesh.bind();
        BaseMotor._dirtex.bind();
        G.gl.glEnable(3042);
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        G.gl.glDisable(2884);
        G.gl.glDepthFunc(515);
        Iterator<StaticMotor> it9 = om.static_motors.iterator();
        while (it9.hasNext()) {
            StaticMotor m6 = it9.next();
            m6.render_deco();
        }
        G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        Iterator<DynamicMotor> it10 = om.layer0.dynamicmotors.iterator();
        while (it10.hasNext()) {
            DynamicMotor m7 = it10.next();
            m7.render_deco();
        }
        Iterator<DynamicMotor> it11 = om.layer1.dynamicmotors.iterator();
        while (it11.hasNext()) {
            DynamicMotor m8 = it11.next();
            m8.render_deco();
        }
        G.gl.glEnable(2884);
        MiscRenderer.boxmesh.unbind();
        MiscRenderer.boxmesh.setAutoBind(true);
        G.gl.glDisable(3553);
        if (Game.enable_shadows) {
            render_projected_penumbras(om);
        }
        G.gl.glDisable(2884);
        render_ropes(cam, om.pcables, om.ropes, om.cables);
        G.batch.setProjectionMatrix(G.p_cam.combined);
        G.batch.setTransformMatrix(BaseRope.ztransform);
        Explosive.render_explosions();
        G.batch.setTransformMatrix(RocketEngine.ztransform);
        RocketEngine.render_fire();
        G.batch.setTransformMatrix(RocketEngine.ztransform_l);
        RocketEngine.render_lights();
        G.batch.setTransformMatrix(BaseRope.idttransform);
        G.gl.glEnable(2884);
        G.gl.glLoadMatrixf(RocketEngine.ztransform_l.val, 0);
        G.gl.glDepthMask(true);
    }

    private static void render_ropes(Camera cam, ArrayList<PanelCable> pcables, ArrayList<Rope> ropes, ArrayList<Cable> cables) {
        pcables.size();
        ropes.size();
        ropes.size();
        G.batch.setProjectionMatrix(cam.combined);
        G.batch.setTransformMatrix(BaseRope.shadow_ztransform);
        if (Game.enable_shadows) {
            G.batch.begin();
            G.batch.setColor(0.0f, 0.0f, 0.0f, 0.2f);
            Iterator<PanelCable> it = pcables.iterator();
            while (it.hasNext()) {
                PanelCable r = it.next();
                r.render();
            }
            Iterator<Rope> it2 = ropes.iterator();
            while (it2.hasNext()) {
                Rope r2 = it2.next();
                r2.render();
            }
            Iterator<Cable> it3 = cables.iterator();
            while (it3.hasNext()) {
                Cable r3 = it3.next();
                r3.render();
            }
            G.batch.end();
        }
        G.batch.setTransformMatrix(BaseRope.ztransform);
        G.batch.begin();
        G.batch.setColor(PanelCable.color);
        Iterator<PanelCable> it4 = pcables.iterator();
        while (it4.hasNext()) {
            PanelCable r4 = it4.next();
            r4.render();
        }
        G.batch.setColor(Rope.color);
        Iterator<Rope> it5 = ropes.iterator();
        while (it5.hasNext()) {
            Rope r5 = it5.next();
            r5.render();
        }
        G.batch.setColor(Cable.color);
        Iterator<Cable> it6 = cables.iterator();
        while (it6.hasNext()) {
            Cable r6 = it6.next();
            r6.render();
        }
        G.batch.end();
        G.batch.setTransformMatrix(BaseRope.idttransform);
    }

    private static void render_battery_decos(ArrayList<Battery> batteries) {
        MiscRenderer.boxmesh.setAutoBind(false);
        MiscRenderer.boxmesh.bind();
        G.gl.glEnable(3042);
        G.gl.glDepthMask(false);
        G.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Battery._texture.bind();
        Iterator<Battery> it = batteries.iterator();
        while (it.hasNext()) {
            Battery b = it.next();
            b.render_sign();
        }
        G.gl.glColor4f(SHADOW_COLOR, 1.0f, SHADOW_COLOR, 0.2f);
        BaseRope._texture.bind();
        Iterator<Battery> it2 = batteries.iterator();
        while (it2.hasNext()) {
            Battery b2 = it2.next();
            Battery b3 = b2;
            if (b3.on) {
                b3.render_light();
            }
        }
        G.gl.glDisable(3553);
        G.gl.glDisable(3042);
        Iterator<Battery> it3 = batteries.iterator();
        while (it3.hasNext()) {
            Battery b4 = it3.next();
            b4.render_levels();
        }
        G.gl.glDepthMask(true);
        MiscRenderer.boxmesh.unbind();
        MiscRenderer.boxmesh.setAutoBind(true);
    }

    private static void render_metalwheels_lq(ArrayList<MetalWheel> wheels) {
        Iterator<MetalWheel> it = wheels.iterator();
        while (it.hasNext()) {
            MetalWheel w = it.next();
            w.render_lq();
        }
    }

    private static void render_metalwheels(ArrayList<MetalWheel> wheels) {
        Iterator<MetalWheel> it = wheels.iterator();
        while (it.hasNext()) {
            MetalWheel w = it.next();
            w.render();
        }
    }

    private static void render_metalwheels_inner_lq(ArrayList<MetalWheel> wheels) {
        Iterator<MetalWheel> it = wheels.iterator();
        while (it.hasNext()) {
            MetalWheel w = it.next();
            w.render_inner_lq();
        }
    }

    private static void render_metalwheels_inner(ArrayList<MetalWheel> wheels) {
        Iterator<MetalWheel> it = wheels.iterator();
        while (it.hasNext()) {
            MetalWheel w = it.next();
            w.render_inner();
        }
    }

    private static void render_knobs_lq(ArrayList<Knob> knobs) {
        int sz = knobs.size();
        for (int x = 0; x < sz; x++) {
            Wheel w = knobs.get(x);
            if (!w.culled) {
                w.render_lq();
            }
        }
    }

    private static void render_knobs(ArrayList<Knob> knobs) {
        int sz = knobs.size();
        for (int x = 0; x < sz; x++) {
            Wheel w = knobs.get(x);
            if (!w.culled) {
                w.render();
            }
        }
    }

    private static void render_wheels_lq(ArrayList<Wheel> wheels) {
        int num_fixed = 0;
        int sz = wheels.size();
        for (int x = 0; x < sz; x++) {
            Wheel w = wheels.get(x);
            if (!w.culled) {
                if (w.fixed_dynamic) {
                    num_fixed++;
                } else {
                    w.render_lq();
                }
            }
        }
        if (num_fixed > 0) {
            Plank.init_dark_material();
            for (int x2 = 0; x2 < sz; x2++) {
                Wheel w2 = wheels.get(x2);
                if (!w2.culled) {
                    if (w2.fixed_dynamic) {
                        w2.render_lq();
                        num_fixed--;
                    }
                    if (num_fixed == 0) {
                        break;
                    }
                }
            }
            Plank.init_light_material();
        }
    }

    private static void render_wheels(ArrayList<Wheel> wheels) {
        int num_fixed = 0;
        int sz = wheels.size();
        for (int x = 0; x < sz; x++) {
            Wheel w = wheels.get(x);
            if (!w.culled) {
                if (w.fixed_dynamic) {
                    num_fixed++;
                } else {
                    w.render();
                }
            }
        }
        if (num_fixed > 0) {
            Plank.init_dark_material();
            for (int x2 = 0; x2 < sz; x2++) {
                Wheel w2 = wheels.get(x2);
                if (!w2.culled) {
                    if (w2.fixed_dynamic) {
                        w2.render();
                        num_fixed--;
                    }
                    if (num_fixed == 0) {
                        break;
                    }
                }
            }
            Plank.init_light_material();
        }
    }

    private static void render_metalbars_lq(ArrayList<MetalBar> bars) {
        int sz = bars.size();
        for (int i = 0; i < sz; i++) {
            MetalBar m = bars.get(i);
            if (!m.culled) {
                m.render_lq();
            }
        }
    }

    private static void render_metalbars(ArrayList<MetalBar> bars) {
        int sz = bars.size();
        for (int i = 0; i < sz; i++) {
            MetalBar m = bars.get(i);
            if (!m.culled) {
                m.render();
            }
        }
    }

    private static void render_marbles(ArrayList<Marble> marbles) {
        int msz = marbles.size();
        for (int x = 0; x < msz; x++) {
            Marble m = marbles.get(x);
            if (!m.culled) {
                m.render();
            }
        }
    }

    private static void render_planks_lq(ArrayList<Plank> planks) {
        int num_fixed = 0;
        int sz = planks.size();
        for (int x = 0; x < sz; x++) {
            Plank p = planks.get(x);
            if (!p.culled) {
                if (p.fixed_dynamic) {
                    num_fixed++;
                } else {
                    p.render_lq();
                }
            }
        }
        if (num_fixed > 0) {
            Plank.init_dark_material();
            for (int x2 = 0; x2 < sz; x2++) {
                Plank p2 = planks.get(x2);
                if (!p2.culled) {
                    if (p2.fixed_dynamic) {
                        p2.render_lq();
                        num_fixed--;
                    }
                    if (num_fixed == 0) {
                        break;
                    }
                }
            }
            Plank.init_light_material();
        }
    }

    private static void render_planks(ArrayList<Plank> planks) {
        int num_fixed = 0;
        int sz = planks.size();
        for (int x = 0; x < sz; x++) {
            Plank p = planks.get(x);
            if (!p.culled) {
                if (p.fixed_dynamic) {
                    num_fixed++;
                } else {
                    p.render();
                }
            }
        }
        if (num_fixed > 0) {
            Plank.init_dark_material();
            for (int x2 = 0; x2 < sz; x2++) {
                Plank p2 = planks.get(x2);
                if (!p2.culled) {
                    if (p2.fixed_dynamic) {
                        p2.render();
                        num_fixed--;
                    }
                    if (num_fixed == 0) {
                        break;
                    }
                }
            }
            Plank.init_light_material();
        }
    }

    public static void render_shadow_volumes(Game game, Vector3 lightdir) {
    }

    public static void render_bloom(PCameraHandler camera, ObjectManager om) {
        G.gl.glDepthMask(false);
        G.gl.glEnable(2929);
        G.gl.glBlendFunc(770, 1);
        G.gl.glEnable(3553);
        G.gl.glEnable(3042);
        Game.bloomtex.bind();
        Iterator<MetalBar> it = om.layer0.bars.iterator();
        while (it.hasNext()) {
            MetalBar m = it.next();
            if (!m.culled) {
                Vector2 pos = m.get_state().position;
                float dist = pos.dst(camera.camera_pos.x, camera.camera_pos.y - 10.0f);
                float dist2 = Math.max((300.0f - Math.min(300.0f, dist * dist)) / 300.0f, 0.05f);
                if (dist2 > 0.01f) {
                    G.gl.glPushMatrix();
                    G.gl.glTranslatef(pos.x, pos.y, m.layer + m.z + 0.125f + 1.0f);
                    G.gl.glRotatef((float) (m.get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
                    G.gl.glColor4f(1.0f, 1.0f, 1.0f, Math.min(dist2, 0.7f));
                    G.gl.glScalef((m.size.x / 2.0f) + (0.45f * dist2) + 0.1f, 0.9f, 1.0f);
                    MiscRenderer.draw_textured_box();
                    G.gl.glPopMatrix();
                }
            }
        }
        G.gl.glColor4f(0.7882353f, 0.70980394f, 0.5372549f, 0.2f);
        G.gl.glEnable(2929);
        G.gl.glDisable(3553);
        G.gl.glDepthMask(true);
    }
}
