package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.objects.BaseObject;

/* loaded from: classes.dex */
public class Marble extends GrabableObject implements PrimaryObject {
    private static BodyDef _bd;
    private static FixtureDef _fd;
    private static CircleShape _shape;
    public static Mesh silhouette_mesh;
    public static Texture texture;
    private static boolean _initialized = false;
    public static float[] material = {0.058f, 0.11000001f, 0.166f, 1.0f, 0.145f, 0.275f, 0.415f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 40.0f, 0.0f, 0.0f, 0.0f};
    public static float[] diffuse_christmas_material = {0.158f, 0.06600001f, 0.06600001f, 1.0f, 0.395f, 0.165f, 0.165f, 1.0f, 0.47400004f, 0.19800001f, 0.19800001f, 1.0f, 20.0f, 0.0f, 0.0f, 0.0f};
    public static float[] diffuse_material = {0.058f, 0.11000001f, 0.166f, 1.0f, 0.145f, 0.275f, 0.415f, 1.0f, 0.174f, 0.33f, 0.498f, 1.0f, 20.0f, 0.0f, 0.0f, 0.0f};

    public Marble(World world) {
        if (!_initialized) {
            _init();
        }
        this.body = world.createBody(_bd);
        this.body.createFixture(_fd);
        this.body.setUserData(this);
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.StaticBody;
        this.fixed_rotation = true;
    }

    public static void _init() {
        _initialized = true;
        _bd = new BodyDef();
        _bd.type = BodyDef.BodyType.StaticBody;
        _shape = new CircleShape();
        _shape.setRadius(0.5f);
        _fd = new FixtureDef();
        _fd.shape = _shape;
        _fd.friction = 0.1f;
        _fd.density = 1.0f;
        _fd.restitution = 0.3f;
        silhouette_mesh = new Mesh(true, 34, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE));
        float[] v = new float[102];
        Vector3 light_dir = new Vector3(Game.light_pos);
        Vector3 tmp = new Vector3();
        Vector3 axis = new Vector3(0.0f, 0.0f, 1.0f);
        axis.crs(light_dir);
        Matrix4 rotation = new Matrix4();
        rotation.setToRotation(axis, (float) Math.acos(axis.dot(light_dir) * 57.29577951308232d));
        for (int x = 0; x < 17; x++) {
            double ac = x * 0.39269908169872414d;
            int o = x * 6;
            tmp.set(((float) Math.cos(ac)) * 0.5f, ((float) Math.sin(ac)) * 0.5f, 0.0f);
            tmp.mul(rotation);
            v[o + 0] = tmp.x;
            v[o + 1] = tmp.y;
            v[o + 2] = tmp.z;
            tmp.set(light_dir).mul(-20.0f).add(v[o + 0], v[o + 1], v[o + 2]);
            v[o + 3] = tmp.x;
            v[o + 4] = tmp.y;
            v[o + 5] = tmp.z;
        }
        silhouette_mesh.setVertices(v);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
    }

    public static void _init_materials() {
        G.gl.glMaterialfv(1032, GL10.GL_AMBIENT, material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, material, 4);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, material, 8);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, material, 12);
    }

    public static void _init_diffuse_materials() {
        G.gl.glMaterialfv(1032, GL10.GL_AMBIENT, diffuse_material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, diffuse_material, 4);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, diffuse_material, 8);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, diffuse_material, 12);
    }

    public static void _init_diffuse_christmas_materials() {
        G.gl.glMaterialfv(1032, GL10.GL_AMBIENT, diffuse_christmas_material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, diffuse_christmas_material, 4);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, diffuse_christmas_material, 8);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, diffuse_christmas_material, 12);
    }

    public static void _init_materials(boolean shadowed) {
        G.gl.glColor4f(0.232f, 0.44000003f, 0.664f, 1.0f);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
        BaseObject.State s = get_state();
        Vector2 pos = s.position;
        float angle = s.angle;
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, 0.75f + this.layer);
        G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
        MiscRenderer.spheremesh.render(5);
        G.gl.glPopMatrix();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void step(float deltatime) {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public Vector2 get_position() {
        return this.body.getPosition();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public float get_bb_radius() {
        return 0.0f;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void play() {
        this.body.setType(BodyDef.BodyType.DynamicBody);
        super.play();
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void pause() {
        super.pause();
        if (Game.sandbox) {
            this.body.setType(BodyDef.BodyType.DynamicBody);
        } else {
            this.body.setType(BodyDef.BodyType.StaticBody);
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void grab() {
        this.body.setType(BodyDef.BodyType.DynamicBody);
        this.grabbed = true;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void release() {
        this.grabbed = false;
        this.body.setTransform(this.body.getPosition(), this.body.getAngle());
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public Body[] get_body_list() {
        return null;
    }

    public void draw_shadow_volume(Vector3 lightdir) {
        Vector2 pos = get_position();
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, 0.75f + this.layer);
        silhouette_mesh.render(5);
        G.gl.glPopMatrix();
    }

    public static void init_materials() {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
        translate(x, y);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
    }
}
