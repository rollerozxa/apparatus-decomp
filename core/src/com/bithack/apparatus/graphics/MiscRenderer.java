package com.bithack.apparatus.graphics;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpStatus;
import com.bithack.apparatus.Game;

public class MiscRenderer {
    public static Mesh Acubemesh = null;
    public static Mesh Acylindermesh = null;
    public static Mesh Ametalmesh = null;
    public static Mesh Aplankmesh = null;
    public static final int CYLINDER_DIV = 24;
    public static final int SMALLCYLINDER_DIV = 5;
    public static Mesh axesmesh;
    public static Mesh boxmesh;
    public static Mesh circlemesh;
    public static Mesh cubemesh;
    public static Mesh cubesides;
    public static float[] cubevertices;
    public static Mesh cylindermesh;
    public static Mesh hqcubemesh;
    public static Mesh hqcylindermesh;
    public static Mesh hqmetalmesh;
    public static Mesh hqplankmesh;
    static float[] sbox_verts = new float[16];
    public static Mesh sboxmesh;
    public static Mesh smallcylindermesh;
    public static Mesh spheremesh;
    private static Mesh squaremesh;
    public static Texture stippletex;

    private static void create_hqcylindermesh() {
        float[] v = new float[1008];
        int c = 0;
        for (int x = 0; x <= 24; x++) {
            float a = 0.2617994f * x;
            v[c] = (float) Math.cos(a);
            v[c + 1] = (float) Math.sin(a);
            v[c + 2] = 0.5f;
            v[c + 3] = (float) Math.cos(a - 0.2617994f);
            v[c + 4] = (float) Math.sin(a - 0.2617994f);
            v[c + 5] = 0.0f;
            v[c + 6] = x / 24.0f;
            v[c + 7] = 1.0f;
            normalize(v, c + 3);
            int c2 = c + 8;
            v[c2] = (float) Math.cos(a);
            v[c2 + 1] = (float) Math.sin(a);
            v[c2 + 2] = -0.5f;
            v[c2 + 3] = v[(c2 + 3) - 8];
            v[c2 + 4] = v[(c2 + 4) - 8];
            v[c2 + 5] = 0.0f;
            v[c2 + 6] = x / 24.0f;
            v[c2 + 7] = 0.0f;
            normalize(v, c2 + 3);
            int c3 = (c2 + 8) - 16;
            v[c3 + HttpStatus.SC_BAD_REQUEST] = ((float) Math.cos(a)) * 0.9f;
            v[c3 + HttpStatus.SC_BAD_REQUEST + 1] = ((float) Math.sin(a)) * 0.9f;
            v[c3 + HttpStatus.SC_BAD_REQUEST + 2] = 0.6f;
            v[c3 + HttpStatus.SC_BAD_REQUEST + 3] = 0.0f;
            v[c3 + HttpStatus.SC_BAD_REQUEST + 4] = 0.0f;
            v[c3 + HttpStatus.SC_BAD_REQUEST + 5] = 1.0f;
            v[c3 + HttpStatus.SC_BAD_REQUEST + 6] = x / 24.0f;
            v[c3 + HttpStatus.SC_BAD_REQUEST + 7] = 1.0f;
            int c4 = c3 + 8;
            v[c4 + HttpStatus.SC_BAD_REQUEST] = (float) Math.cos(a);
            v[c4 + HttpStatus.SC_BAD_REQUEST + 1] = (float) Math.sin(a);
            v[c4 + HttpStatus.SC_BAD_REQUEST + 2] = 0.5f;
            v[c4 + HttpStatus.SC_BAD_REQUEST + 3] = (float) Math.cos(a);
            v[c4 + HttpStatus.SC_BAD_REQUEST + 4] = (float) Math.sin(a);
            v[c4 + HttpStatus.SC_BAD_REQUEST + 5] = 0.0f;
            v[c4 + HttpStatus.SC_BAD_REQUEST + 6] = x / 24.0f;
            v[c4 + HttpStatus.SC_BAD_REQUEST + 7] = 0.0f;
            normalize(v, c4 + HttpStatus.SC_BAD_REQUEST + 3);
            c = c4 + 8;
            v[((x + 1) * 8) + 800 + 0] = ((float) Math.cos(a)) * 0.9f;
            v[((x + 1) * 8) + 800 + 1] = ((float) Math.sin(a)) * 0.9f;
            v[((x + 1) * 8) + 800 + 2] = 0.6f;
            v[((x + 1) * 8) + 800 + 3] = (float) Math.cos(a);
            v[((x + 1) * 8) + 800 + 4] = (float) Math.sin(a);
            v[((x + 1) * 8) + 800 + 5] = 1.0f;
            normalize(v, ((x + 1) * 8) + 800 + 3);
            v[((x + 1) * 8) + 800 + 6] = (((float) Math.cos(a)) * 0.5f) + 0.5f;
            v[((x + 1) * 8) + 800 + 7] = (((float) Math.sin(a)) * 0.5f) + 0.5f;
        }
        v[800] = 0.0f;
        v[801] = 0.0f;
        v[802] = 0.6f;
        v[803] = 0.0f;
        v[804] = 0.0f;
        v[805] = 1.0f;
        v[806] = 0.5f;
        v[807] = 0.5f;
        hqcylindermesh = new Mesh(true, 126, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        hqcylindermesh.setVertices(v);
    }

    private static void create_cylindermesh() {
        float[] v = new float[608];
        int c = 0;
        for (int x = 0; x <= 24; x++) {
            float a = 0.2617994f * x;
            v[c] = (float) Math.cos(a);
            v[c + 1] = (float) Math.sin(a);
            v[c + 2] = 0.5f;
            v[c + 3] = (float) Math.cos(a - 0.2617994f);
            v[c + 4] = (float) Math.sin(a - 0.2617994f);
            v[c + 5] = 0.0f;
            v[c + 6] = x / 24.0f;
            v[c + 7] = 1.0f;
            normalize(v, c + 3);
            int c2 = c + 8;
            v[c2] = (float) Math.cos(a);
            v[c2 + 1] = (float) Math.sin(a);
            v[c2 + 2] = -0.5f;
            v[c2 + 3] = v[(c2 + 3) - 8];
            v[c2 + 4] = v[(c2 + 4) - 8];
            v[c2 + 5] = 0.0f;
            v[c2 + 6] = x / 24.0f;
            v[c2 + 7] = 0.0f;
            normalize(v, c2 + 3);
            c = c2 + 8;
            v[((x + 1) * 8) + HttpStatus.SC_BAD_REQUEST + 0] = (float) Math.cos(a);
            v[((x + 1) * 8) + HttpStatus.SC_BAD_REQUEST + 1] = (float) Math.sin(a);
            v[((x + 1) * 8) + HttpStatus.SC_BAD_REQUEST + 2] = 0.5f;
            v[((x + 1) * 8) + HttpStatus.SC_BAD_REQUEST + 3] = (float) Math.cos(a);
            v[((x + 1) * 8) + HttpStatus.SC_BAD_REQUEST + 4] = (float) Math.sin(a);
            v[((x + 1) * 8) + HttpStatus.SC_BAD_REQUEST + 5] = 1.0f;
            normalize(v, ((x + 1) * 8) + HttpStatus.SC_BAD_REQUEST + 3);
            v[((x + 1) * 8) + HttpStatus.SC_BAD_REQUEST + 6] = (((float) Math.cos(a)) * 0.5f) + 0.5f;
            v[((x + 1) * 8) + HttpStatus.SC_BAD_REQUEST + 7] = (((float) Math.sin(a)) * 0.5f) + 0.5f;
        }
        v[400] = 0.0f;
        v[401] = 0.0f;
        v[402] = 0.5f;
        v[403] = 0.0f;
        v[404] = 0.0f;
        v[405] = 1.0f;
        v[406] = 0.5f;
        v[407] = 0.5f;
        cylindermesh = new Mesh(true, 76, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        cylindermesh.setVertices(v);
    }

    private static void create_smallcylindermesh() {
        float[] v = new float[Input.Keys.NUMPAD_8];
        int c = 0;
        for (int x = 0; x <= 5; x++) {
            float a = 1.2566371f * x;
            v[c] = (float) Math.cos(a);
            v[c + 1] = (float) Math.sin(a);
            v[c + 2] = 0.5f;
            v[c + 3] = (float) Math.cos(a);
            v[c + 4] = (float) Math.sin(a);
            v[c + 5] = 0.3f;
            v[c + 6] = x / 5.0f;
            v[c + 7] = 1.0f;
            normalize(v, c + 3);
            int c2 = c + 8;
            v[c2] = (float) Math.cos(a);
            v[c2 + 1] = (float) Math.sin(a);
            v[c2 + 2] = -0.5f;
            v[c2 + 3] = v[(c2 + 3) - 8];
            v[c2 + 4] = v[(c2 + 4) - 8];
            v[c2 + 5] = 0.0f;
            v[c2 + 6] = x / 5.0f;
            v[c2 + 7] = 0.0f;
            normalize(v, c2 + 3);
            c = c2 + 8;
            v[((x + 1) * 8) + 96 + 0] = (float) Math.cos(a);
            v[((x + 1) * 8) + 96 + 1] = (float) Math.sin(a);
            v[((x + 1) * 8) + 96 + 2] = 0.5f;
            v[((x + 1) * 8) + 96 + 3] = (float) Math.cos(a);
            v[((x + 1) * 8) + 96 + 4] = (float) Math.sin(a);
            v[((x + 1) * 8) + 96 + 5] = 2.0f;
            normalize(v, ((x + 1) * 8) + 96 + 3);
            v[((x + 1) * 8) + 96 + 6] = (((float) Math.cos(a)) * 0.5f) + 0.5f;
            v[((x + 1) * 8) + 96 + 7] = (((float) Math.sin(a)) * 0.5f) + 0.5f;
        }
        v[96] = 0.0f;
        v[97] = 0.0f;
        v[98] = 0.5f;
        v[99] = 0.0f;
        v[100] = 0.0f;
        v[101] = 1.0f;
        v[102] = 0.5f;
        v[103] = 0.5f;
        smallcylindermesh = new Mesh(true, 19, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        smallcylindermesh.setVertices(v);
    }

    private static void normalize(float[] v, int offs) {
        float x = (float) Math.sqrt((v[offs] * v[offs]) + (v[offs + 1] * v[offs + 1]) + (v[offs + 2] * v[offs + 2]));
        if (x > 1.0E-4f) {
            v[offs] = v[offs] / x;
            int i = offs + 1;
            v[i] = v[i] / x;
            int i2 = offs + 2;
            v[i2] = v[i2] / x;
        }
    }

    private static void create_spheremesh() {
        float[] v = new float[10368];
        int c = 0;
        spheremesh = new Mesh(true, 1296, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        for (int b = 0; b <= 180; b += 20) {
            for (int a = 0; a <= 340; a += 20) {
                v[c] = ((float) (Math.sin((a / 180.0f) * 3.141592653589793d) * Math.sin((b / 180.0f) * 3.141592653589793d))) * 0.5f;
                v[c + 1] = (-((float) (Math.cos((a / 180.0f) * 3.141592653589793d) * Math.sin((b / 180.0f) * 3.141592653589793d)))) * 0.5f;
                v[c + 2] = ((float) Math.cos((b / 180.0f) * 3.141592653589793d)) * 0.5f;
                v[c + 3] = v[c] * 2.0f;
                v[c + 4] = v[c + 1] * 2.0f;
                v[c + 5] = v[c + 2] * 2.0f;
                v[c + 6] = (b * 2) / 360.0f;
                v[c + 7] = a / 360.0f;
                int c2 = c + 8;
                v[c2] = ((float) (Math.sin((a / 180.0f) * 3.141592653589793d) * Math.sin(((b + 20) / 180.0f) * 3.141592653589793d))) * 0.5f;
                v[c2 + 1] = (-((float) (Math.cos((a / 180.0f) * 3.141592653589793d) * Math.sin(((b + 20) / 180.0f) * 3.141592653589793d)))) * 0.5f;
                v[c2 + 2] = ((float) Math.cos(((b + 20) / 180.0f) * 3.141592653589793d)) * 0.5f;
                v[c2 + 3] = v[c2] * 2.0f;
                v[c2 + 4] = v[c2 + 1] * 2.0f;
                v[c2 + 5] = v[c2 + 2] * 2.0f;
                v[c2 + 6] = ((b + 20) * 2) / 360.0f;
                v[c2 + 7] = a / 360.0f;
                int c3 = c2 + 8;
                v[c3] = ((float) (Math.sin(((a + 20) / 180.0f) * 3.141592653589793d) * Math.sin((b / 180.0f) * 3.141592653589793d))) * 0.5f;
                v[c3 + 1] = (-((float) (Math.cos(((a + 20) / 180.0f) * 3.141592653589793d) * Math.sin((b / 180.0f) * 3.141592653589793d)))) * 0.5f;
                v[c3 + 2] = ((float) Math.cos((b / 180.0f) * 3.141592653589793d)) * 0.5f;
                v[c3 + 3] = v[c3] * 2.0f;
                v[c3 + 4] = v[c3 + 1] * 2.0f;
                v[c3 + 5] = v[c3 + 2] * 2.0f;
                v[c3 + 6] = (b * 2) / 360.0f;
                v[c3 + 7] = (a + 20) / 360.0f;
                int c4 = c3 + 8;
                v[c4] = ((float) (Math.sin(((a + 20) / 180.0f) * 3.141592653589793d) * Math.sin(((b + 20) / 180.0f) * 3.141592653589793d))) * 0.5f;
                v[c4 + 1] = (-((float) (Math.cos(((a + 20) / 180.0f) * 3.141592653589793d) * Math.sin(((b + 20) / 180.0f) * 3.141592653589793d)))) * 0.5f;
                v[c4 + 2] = ((float) Math.cos(((b + 20) / 180.0f) * 3.141592653589793d)) * 0.5f;
                v[c4 + 3] = v[c4] * 2.0f;
                v[c4 + 4] = v[c4 + 1] * 2.0f;
                v[c4 + 5] = v[c4 + 2] * 2.0f;
                v[c4 + 6] = ((b + 20) * 2) / 360.0f;
                v[c4 + 7] = (a + 20) / 360.0f;
                c = c4 + 8;
            }
        }
        spheremesh.setVertices(v);
    }

    public static void initialize() {
        squaremesh = new Mesh(true, 4, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoord"));
        boxmesh = new Mesh(true, 4, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoord"));
        sboxmesh = new Mesh(false, 4, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoord"));
        axesmesh = new Mesh(true, 4, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(4, 4, ShaderProgram.COLOR_ATTRIBUTE));
        circlemesh = new Mesh(true, 24, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE));
        create_spheremesh();
        create_cylindermesh();
        create_hqcylindermesh();
        create_smallcylindermesh();
        float[] v = {-1.0f, 1.0f, 0.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f};
        boxmesh.setVertices(v);
        v[0] = -0.5f;
        v[1] = 0.5f;
        v[2] = 0.0f;
        v[3] = 0.0f;
        v[4] = -0.5f;
        v[5] = -0.5f;
        v[6] = 8.0f;
        v[7] = 0.0f;
        v[8] = 0.5f;
        v[9] = -0.5f;
        v[10] = 8.0f;
        v[11] = 8.0f;
        v[12] = 0.5f;
        v[13] = 0.5f;
        v[14] = 8.0f;
        v[15] = 0.0f;
        squaremesh.setVertices(v);
        v[0] = 0.0f;
        v[1] = 2.0f;
        v[2] = Color.toFloatBits(1.0f, 0.0f, 0.0f, 1.0f);
        v[3] = 0.0f;
        v[4] = 0.0f;
        v[5] = Color.toFloatBits(1.0f, 0.0f, 0.0f, 1.0f);
        v[6] = 0.0f;
        v[7] = 0.0f;
        v[8] = Color.toFloatBits(0.0f, 1.0f, 0.0f, 1.0f);
        v[9] = 2.0f;
        v[10] = 0.0f;
        v[11] = Color.toFloatBits(0.0f, 1.0f, 0.0f, 1.0f);
        axesmesh.setVertices(v, 0, 12);
        float[] v2 = new float[48];
        for (int x = 0; x < 24; x++) {
            v2[(x * 2) + 0] = (float) Math.cos(x * 0.2617994f);
            v2[(x * 2) + 1] = (float) Math.sin(x * 0.2617994f);
        }
        circlemesh.setVertices(v2);
        cubemesh = new Mesh(true, 30, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        hqmetalmesh = new Mesh(true, 162, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        hqplankmesh = new Mesh(true, 162, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        hqcubemesh = new Mesh(true, 54, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        cubesides = new Mesh(true, 24, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        float[] v3 = {-0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.375f, -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.375f, 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.375f, -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.5f, 0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.5f, -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.375f, -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.125f, 0.375f, -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.125f, 0.375f, -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.125f, 0.5f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.875f, 0.375f, 0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.875f, 0.5f, 0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.375f, 0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.375f, 0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.875f, 0.5f, 0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.125f, 0.25f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.125f, 0.375f, 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.875f, 0.25f, 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.875f, 0.25f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.125f, 0.375f, 0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.875f, 0.375f, -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.125f, 0.625f, -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.125f, 0.5f, 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.875f, 0.625f, 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.875f, 0.625f, -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.125f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.875f, 0.5f};
        cubemesh.setVertices(v3);
        cubevertices = v3;
        cubesides.setVertices(new float[]{-0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f});
        float[] v4 = {-0.492f, 0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.375f, -0.492f, -0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.5f, 0.492f, 0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.375f, 0.492f, 0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.375f, -0.492f, -0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.5f, 0.492f, -0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.5f, -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.375f, -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, 0.5f, 0.48f, -1.0f, 0.0f, 0.0f, 0.125f, 0.375f, -0.5f, 0.5f, 0.48f, -1.0f, 0.0f, 0.0f, 0.125f, 0.375f, -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, -0.5f, 0.48f, -1.0f, 0.0f, 0.0f, 0.125f, 0.5f, 0.5f, 0.5f, 0.48f, 1.0f, 0.0f, 0.0f, 0.875f, 0.375f, 0.5f, -0.5f, 0.48f, 1.0f, 0.0f, 0.0f, 0.875f, 0.5f, 0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.375f, 0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.375f, 0.5f, -0.5f, 0.48f, 1.0f, 0.0f, 0.0f, 0.875f, 0.5f, 0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f, -0.5f, 0.48f, 0.0f, -1.0f, 0.0f, 0.125f, 0.25f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.125f, 0.375f, 0.5f, -0.5f, 0.48f, 0.0f, -1.0f, 0.0f, 0.875f, 0.25f, 0.5f, -0.5f, 0.48f, 0.0f, -1.0f, 0.0f, 0.875f, 0.25f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.125f, 0.375f, 0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.875f, 0.375f, -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.125f, 0.625f, -0.5f, 0.5f, 0.48f, 0.0f, 1.0f, 0.0f, 0.125f, 0.5f, 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.875f, 0.625f, 0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.875f, 0.625f, -0.5f, 0.5f, 0.48f, 0.0f, 1.0f, 0.0f, 0.125f, 0.5f, 0.5f, 0.5f, 0.48f, 0.0f, 1.0f, 0.0f, 0.875f, 0.5f, -0.5f, 0.5f, 0.48f, -1.0f, 0.0f, 0.0f, 0.125f, 0.375f, -0.5f, -0.5f, 0.48f, -1.0f, 0.0f, 0.0f, 0.125f, 0.5f, -0.492f, 0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.375f, -0.492f, 0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.375f, -0.5f, -0.5f, 0.48f, -1.0f, 0.0f, 0.0f, 0.125f, 0.5f, -0.492f, -0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.5f, 0.492f, 0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.375f, 0.492f, -0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.5f, 0.5f, 0.5f, 0.48f, 1.0f, 0.0f, 0.0f, 0.875f, 0.375f, 0.5f, 0.5f, 0.48f, 1.0f, 0.0f, 0.0f, 0.875f, 0.375f, 0.492f, -0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.5f, 0.5f, -0.5f, 0.48f, 1.0f, 0.0f, 0.0f, 0.875f, 0.5f, -0.5f, 0.5f, 0.48f, 0.0f, 1.0f, 0.0f, 0.125f, 0.625f, -0.492f, 0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.5f, 0.5f, 0.5f, 0.48f, 0.0f, 1.0f, 0.0f, 0.875f, 0.625f, 0.5f, 0.5f, 0.48f, 0.0f, 1.0f, 0.0f, 0.875f, 0.625f, -0.492f, 0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.5f, 0.492f, 0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.5f, -0.492f, -0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.125f, 0.625f, -0.5f, -0.5f, 0.48f, 0.0f, -1.0f, 0.0f, 0.125f, 0.5f, 0.5f, -0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.625f, 0.5f, -0.45f, 0.5f, 0.0f, 0.0f, 1.0f, 0.875f, 0.625f, -0.5f, -0.5f, 0.48f, 0.0f, -1.0f, 0.0f, 0.125f, 0.5f, 0.5f, -0.5f, 0.48f, 0.0f, -1.0f, 0.0f, 0.875f, 0.5f};
        hqcubemesh.setVertices(v4);
        float[] nv = new float[v4.length * 3];
        for (int x2 = 0; x2 < (v4.length / 8) * 3; x2++) {
            int vx = x2 % (v4.length / 8);
            int p = x2 / (v4.length / 8);
            switch (p) {
                case 0:
                    nv[(x2 * 8) + 0] = v4[(vx * 8) + 0] * 2.0f;
                    nv[(x2 * 8) + 1] = v4[(vx * 8) + 1] * 0.5f;
                    nv[(x2 * 8) + 2] = v4[(vx * 8) + 2] * 0.5f;
                    nv[(x2 * 8) + 3] = v4[(vx * 8) + 3];
                    nv[(x2 * 8) + 4] = v4[(vx * 8) + 4];
                    nv[(x2 * 8) + 5] = v4[(vx * 8) + 5];
                    nv[(x2 * 8) + 6] = v4[(vx * 8) + 6];
                    nv[(x2 * 8) + 7] = v4[(vx * 8) + 7];
                    break;
                case 1:
                    nv[(x2 * 8) + 0] = v4[(vx * 8) + 0] * 4.0f;
                    nv[(x2 * 8) + 1] = v4[(vx * 8) + 1] * 0.5f;
                    nv[(x2 * 8) + 2] = v4[(vx * 8) + 2] * 0.5f;
                    nv[(x2 * 8) + 3] = v4[(vx * 8) + 3];
                    nv[(x2 * 8) + 4] = v4[(vx * 8) + 4];
                    nv[(x2 * 8) + 5] = v4[(vx * 8) + 5];
                    nv[(x2 * 8) + 6] = v4[(vx * 8) + 6];
                    nv[(x2 * 8) + 7] = v4[(vx * 8) + 7];
                    break;
                case 2:
                    nv[(x2 * 8) + 0] = v4[(vx * 8) + 0] * 8.0f;
                    nv[(x2 * 8) + 1] = v4[(vx * 8) + 1] * 0.5f;
                    nv[(x2 * 8) + 2] = v4[(vx * 8) + 2] * 0.5f;
                    nv[(x2 * 8) + 3] = v4[(vx * 8) + 3];
                    nv[(x2 * 8) + 4] = v4[(vx * 8) + 4];
                    nv[(x2 * 8) + 5] = v4[(vx * 8) + 5];
                    nv[(x2 * 8) + 6] = v4[(vx * 8) + 6];
                    nv[(x2 * 8) + 7] = v4[(vx * 8) + 7];
                    break;
            }
        }
        hqplankmesh.setVertices(nv);
        for (int x3 = 0; x3 < (v4.length / 8) * 3; x3++) {
            int vx2 = x3 % (v4.length / 8);
            int p2 = x3 / (v4.length / 8);
            switch (p2) {
                case 0:
                    nv[(x3 * 8) + 0] = v4[(vx2 * 8) + 0] * 2.0f;
                    nv[(x3 * 8) + 1] = v4[(vx2 * 8) + 1];
                    nv[(x3 * 8) + 2] = v4[(vx2 * 8) + 2] * 2.0f;
                    nv[(x3 * 8) + 3] = v4[(vx2 * 8) + 3];
                    nv[(x3 * 8) + 4] = v4[(vx2 * 8) + 4];
                    nv[(x3 * 8) + 5] = v4[(vx2 * 8) + 5];
                    nv[(x3 * 8) + 6] = v4[(vx2 * 8) + 6];
                    nv[(x3 * 8) + 7] = v4[(vx2 * 8) + 7];
                    break;
                case 1:
                    nv[(x3 * 8) + 0] = v4[(vx2 * 8) + 0] * 4.0f;
                    nv[(x3 * 8) + 1] = v4[(vx2 * 8) + 1];
                    nv[(x3 * 8) + 2] = v4[(vx2 * 8) + 2] * 2.0f;
                    nv[(x3 * 8) + 3] = v4[(vx2 * 8) + 3];
                    nv[(x3 * 8) + 4] = v4[(vx2 * 8) + 4];
                    nv[(x3 * 8) + 5] = v4[(vx2 * 8) + 5];
                    nv[(x3 * 8) + 6] = v4[(vx2 * 8) + 6];
                    nv[(x3 * 8) + 7] = v4[(vx2 * 8) + 7];
                    break;
                case 2:
                    nv[(x3 * 8) + 0] = v4[(vx2 * 8) + 0] * 8.0f;
                    nv[(x3 * 8) + 1] = v4[(vx2 * 8) + 1];
                    nv[(x3 * 8) + 2] = v4[(vx2 * 8) + 2] * 2.0f;
                    nv[(x3 * 8) + 3] = v4[(vx2 * 8) + 3];
                    nv[(x3 * 8) + 4] = v4[(vx2 * 8) + 4];
                    nv[(x3 * 8) + 5] = v4[(vx2 * 8) + 5];
                    nv[(x3 * 8) + 6] = v4[(vx2 * 8) + 6];
                    nv[(x3 * 8) + 7] = v4[(vx2 * 8) + 7];
                    break;
            }
        }
        hqmetalmesh.setVertices(nv);
        try {
            stippletex = TextureFactory.load("data/misc/stipple.png", Files.FileType.Internal, Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        } catch (Exception e) {
            Gdx.app.log("could not load stipple tex", "");
        }
        update_quality();
    }

    public static void update_quality() {
        Gdx.app.log("updating quality", new StringBuilder().append(Game.enable_hqmeshes).toString());
        if (Game.enable_hqmeshes) {
            Ametalmesh = hqmetalmesh;
            Acubemesh = hqcubemesh;
            Acylindermesh = hqcylindermesh;
            Aplankmesh = hqplankmesh;
            return;
        }
        Ametalmesh = cubemesh;
        Acubemesh = cubemesh;
        Acylindermesh = cylindermesh;
        Aplankmesh = cubemesh;
    }

    public static void draw_line(float x1, float y1, float x2, float y2) {
        sbox_verts[0] = x1;
        sbox_verts[1] = y1;
        sbox_verts[4] = x2;
        sbox_verts[5] = y2;
        sboxmesh.setVertices(sbox_verts, 0, 8);
        sboxmesh.render(1);
    }

    public static void draw_point(float x, float y) {
        sbox_verts[0] = x;
        sbox_verts[1] = y;
        sboxmesh.setVertices(sbox_verts, 0, 4);
        sboxmesh.render(0);
    }

    public static void draw_textured_box(Vector2 vlower, Vector2 vupper, float z, Vector2 txtlower, Vector2 txtupper) {
        sbox_verts[0] = vlower.x;
        sbox_verts[1] = vupper.y;
        sbox_verts[2] = txtlower.x;
        sbox_verts[3] = txtupper.y;
        sbox_verts[4] = vlower.x;
        sbox_verts[5] = vlower.y;
        sbox_verts[6] = txtlower.x;
        sbox_verts[7] = txtlower.y;
        sbox_verts[8] = vupper.x;
        sbox_verts[9] = vlower.y;
        sbox_verts[10] = txtupper.x;
        sbox_verts[11] = txtlower.y;
        sbox_verts[12] = vupper.x;
        sbox_verts[13] = vupper.y;
        sbox_verts[14] = txtupper.x;
        sbox_verts[15] = txtupper.y;
        sboxmesh.setVertices(sbox_verts);
        sboxmesh.render(6);
    }

    public static void draw_textured_stipple_square(Vector2 vlower, Vector2 vupper) {
        sbox_verts[0] = vlower.x;
        sbox_verts[1] = vupper.y;
        sbox_verts[2] = 0.0f;
        sbox_verts[3] = 0.0f;
        sbox_verts[4] = vlower.x;
        sbox_verts[5] = vlower.y;
        sbox_verts[6] = 0.0f;
        sbox_verts[7] = (vlower.y - vupper.y) / 2.0f;
        sbox_verts[8] = vupper.x;
        sbox_verts[9] = vlower.y;
        sbox_verts[10] = (vlower.x - vupper.x) / 2.0f;
        sbox_verts[11] = (vlower.y - vupper.y) / 2.0f;
        sbox_verts[12] = vupper.x;
        sbox_verts[13] = vupper.y;
        sbox_verts[14] = 0.0f;
        sbox_verts[15] = (vlower.y - vupper.y) / 2.0f;
        sboxmesh.setVertices(sbox_verts);
        sboxmesh.render(2);
    }

    public static void draw_textured_stipple_square(float scale_x, float scale_y, boolean onscreen) {
    }

    public static void draw_colored_square(Vector2 vlower, Vector2 vupper, float z) {
        sbox_verts[0] = vlower.x;
        sbox_verts[1] = vupper.y;
        sbox_verts[4] = vlower.x;
        sbox_verts[5] = vlower.y;
        sbox_verts[8] = vupper.x;
        sbox_verts[9] = vlower.y;
        sbox_verts[12] = vupper.x;
        sbox_verts[13] = vupper.y;
        sboxmesh.setVertices(sbox_verts);
        sboxmesh.render(2);
    }

    public static void draw_colored_square() {
        squaremesh.render(2);
    }

    public static void draw_textured_box() {
        boxmesh.render(6);
    }

    public static void draw_colored_box() {
        squaremesh.render(6);
    }

    public static void draw_colored_circle() {
        circlemesh.render(2);
    }

    public static void draw_colored_ball() {
        circlemesh.render(6);
    }

    public static void draw_axes() {
    }

    public static void draw_colored_sphere() {
        spheremesh.render(5);
    }

    public static void draw_colored_cube() {
        cubemesh.render(4);
    }

    public static void draw_autocylinder() {
    }

    public static void draw_hqcylinder() {
        hqcylindermesh.render(5, 0, 100);
        hqcylindermesh.render(6, 100, 26);
    }

    public static void draw_cylinder() {
        cylindermesh.render(5, 0, 50);
        cylindermesh.render(6, 50, 26);
    }

    public static void draw_smallcylinder() {
        smallcylindermesh.render(5, 0, 12);
        smallcylindermesh.render(6, 12, 7);
    }
}
