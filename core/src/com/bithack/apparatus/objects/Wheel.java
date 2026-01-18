package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.ApparatusApp;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.Level;
import com.bithack.apparatus.SilhouetteMesh;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.jar.JarOutputStream;

/* loaded from: classes.dex */
public class Wheel extends GrabableObject implements FreeObject {
    protected static BodyDef _bd;
    protected static FixtureDef _fd;
    protected static CircleShape _shape;
    protected Fixture f = null;
    public float size;
    protected static boolean _initialized = false;
    protected static Vector2 _tmp = new Vector2();
    public static Mesh silhouette_mesh_05 = null;
    public static Mesh silhouette_mesh_1 = null;
    public static Mesh silhouette_mesh_2 = null;
    public static SilhouetteMesh silhouette = null;

    public Wheel(World world, float size) {
        if (!_initialized) {
            _init();
        }
        if (!Plank._initialized) {
            Plank._init();
        }
        this.size = size;
        this.layer = 0;
        this.body = world.createBody(_bd);
        reshape();
        this.body.setUserData(this);
        this.sandbox_only = false;
        this.properties = new BaseObject.Property[]{new BaseObject.Property("size", BaseObject.Property.Type.FLOAT, Float.valueOf(this.size))};
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.DynamicBody;
    }

    public static void _init() {
        if (!_initialized) {
            _bd = new BodyDef();
            _fd = new FixtureDef();
            _shape = new CircleShape();
            _bd.type = BodyDef.BodyType.DynamicBody;
            _fd.density = 1.0f;
            _fd.friction = 0.8f;
            _fd.shape = _shape;
            _initialized = true;
            Vector3 light_dir = new Vector3(Game.light_pos);
            light_dir.nor();
            light_dir.mul(-1.0f);
            SilhouetteMesh.Edge[] edges = new SilhouetteMesh.Edge[72];
            SilhouetteMesh.Face[] faces = new SilhouetteMesh.Face[26];
            for (int c = 0; c < 24; c++) {
                float x = (float) Math.cos(c * 0.2617993877991494d);
                float y = (float) Math.sin(c * 0.2617993877991494d);
                if (c == 0) {
                    edges[c] = new SilhouetteMesh.Edge(new Vector3(x, y, -0.25f), new Vector3(x, y, 0.25f), c, 23);
                } else {
                    edges[c] = new SilhouetteMesh.Edge(new Vector3(x, y, -0.25f), new Vector3(x, y, 0.25f), c - 1, c);
                }
                faces[c] = new SilhouetteMesh.Face(new Vector3(x, y, 0.0f));
            }
            for (int c2 = 0; c2 < 24; c2++) {
                float x2 = (float) Math.cos(c2 * 0.2617993877991494d);
                float y2 = (float) Math.sin(c2 * 0.2617993877991494d);
                float nx = (float) Math.cos((c2 + 1) * 0.2617993877991494d);
                float ny = (float) Math.sin((c2 + 1) * 0.2617993877991494d);
                edges[(c2 * 2) + 24] = new SilhouetteMesh.Edge(new Vector3(x2, y2, 0.25f), new Vector3(nx, ny, 0.25f), c2, 25);
                edges[(c2 * 2) + 24 + 1] = new SilhouetteMesh.Edge(new Vector3(x2, y2, -0.25f), new Vector3(nx, ny, -0.25f), c2, 24);
            }
            faces[25] = new SilhouetteMesh.Face(new Vector3(0.0f, 0.0f, 1.0f));
            faces[24] = new SilhouetteMesh.Face(new Vector3(0.0f, 0.0f, -1.0f));
            silhouette = new SilhouetteMesh(edges, faces);
            SilhouetteMesh transformed = silhouette.transform(0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 1.6f, 0.0f);
            transformed.mark_back_faces(light_dir);
            silhouette_mesh_05 = transformed.generate_projected_mesh(null, light_dir);
            SilhouetteMesh transformed2 = silhouette.transform(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.6f, 0.0f);
            transformed2.mark_back_faces(light_dir);
            silhouette_mesh_1 = transformed2.generate_projected_mesh(null, light_dir);
            SilhouetteMesh transformed3 = silhouette.transform(0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 1.6f, 0.0f);
            transformed3.mark_back_faces(light_dir);
            silhouette_mesh_2 = transformed3.generate_projected_mesh(null, light_dir);
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
        G.gl.glPushMatrix();
        BaseObject.State s = get_state();
        Vector2 pos = s.position;
        G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
        G.gl.glRotatef((float) (s.angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
        G.gl.glScalef(this.size, this.size, 0.8f);
        MiscRenderer.draw_hqcylinder();
        G.gl.glPopMatrix();
    }

    public void render_lq() {
        G.gl.glPushMatrix();
        BaseObject.State s = get_state();
        Vector2 pos = s.position;
        G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
        G.gl.glRotatef((float) (s.angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
        G.gl.glScalef(this.size, this.size, 0.8f);
        MiscRenderer.draw_cylinder();
        G.gl.glPopMatrix();
    }

    public void draw_shadow_projection(Vector3 light_pos) {
        G.gl.glPushMatrix();
        Vector2 pos = get_position();
        G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
        if (this.size == 1.0f) {
            silhouette_mesh_1.render(4);
        } else if (this.size == 2.0f) {
            silhouette_mesh_2.render(4);
        } else {
            silhouette_mesh_05.render(4);
        }
        G.gl.glPopMatrix();
    }

    public void draw_shadow_volume(Vector3 light_pos) {
        G.gl.glPushMatrix();
        Vector2 pos = get_position();
        G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
        if (this.size == 1.0f) {
            silhouette_mesh_1.render(4);
        } else if (this.size == 2.0f) {
            silhouette_mesh_2.render(4);
        } else {
            silhouette_mesh_05.render(4);
        }
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

    @Override // com.bithack.apparatus.objects.BaseObject
    public void set_property(String name, Object value) {
        if (name.equals("size")) {
            this.size = ((Float) value).floatValue();
            reshape();
        }
        super.set_property(name, value);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
        super.set_property("size", Float.valueOf(this.size));
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void write_to_stream(JarOutputStream s) throws IOException {
        update_properties();
        super.write_to_stream(s);
    }

    public static void init_materials() {
    }

    @Override // com.bithack.apparatus.objects.FreeObject
    public void set_layer() {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
        ApparatusApp.game.remove_potential_fixture_pair(this.body);
        if (this.f != null) {
            this.body.destroyFixture(this.f);
        }
        _shape.setRadius(this.size);
        _fd.density = 1.0f;
        if (Level.version >= 5) {
            _fd.restitution = 0.1f;
        } else {
            _fd.restitution = 0.0f;
        }
        this.f = this.body.createFixture(_fd);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
