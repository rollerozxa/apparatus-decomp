package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.SilhouetteMesh;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.TextureFactory;

public class Weight extends GrabableObject {
    private static BodyDef _bd;
    private static FixtureDef _fd;
    public static Mesh _mesh;
    private static PolygonShape _shape;
    protected static SilhouetteMesh _silhouette;
    public static Texture _texture;
    private Fixture f;
    protected float last_angle = 4512.0f;
    protected Mesh silhouette_mesh;
    protected static boolean _initialized = false;
    private static Vector2 tmp2 = new Vector2();

    public static void _init() {
        if (!_initialized) {
            Vector3 a = new Vector3(0.5f, -0.5f, -0.5f);
            Vector3 b = new Vector3(0.25f, 0.5f, -0.5f);
            Vector3 c = new Vector3(0.25f, 0.5f, 0.5f);
            _texture = TextureFactory.load_unfiltered("data/iron.png");
            a.sub(b);
            c.sub(b);
            b.set(a).crs(c);
            b.mul(-1.0f).nor();
            _silhouette = new SilhouetteMesh(new SilhouetteMesh.Edge[]{new SilhouetteMesh.Edge(new Vector3(-0.25f, 0.5f, 0.5f), new Vector3(-0.5f, -0.5f, 0.5f), 0, 1), new SilhouetteMesh.Edge(new Vector3(0.25f, 0.5f, 0.5f), new Vector3(-0.25f, 0.5f, 0.5f), 0, 2), new SilhouetteMesh.Edge(new Vector3(0.5f, -0.5f, 0.5f), new Vector3(0.25f, 0.5f, 0.5f), 0, 3), new SilhouetteMesh.Edge(new Vector3(-0.5f, -0.5f, 0.5f), new Vector3(0.5f, -0.5f, 0.5f), 0, 4), new SilhouetteMesh.Edge(new Vector3(-0.25f, 0.5f, -0.5f), new Vector3(-0.25f, 0.5f, 0.5f), 2, 1), new SilhouetteMesh.Edge(new Vector3(-0.5f, -0.5f, -0.5f), new Vector3(-0.5f, -0.5f, 0.5f), 1, 4), new SilhouetteMesh.Edge(new Vector3(-0.25f, 0.5f, -0.5f), new Vector3(-0.5f, -0.5f, -0.5f), 1, 5), new SilhouetteMesh.Edge(new Vector3(0.25f, 0.5f, -0.5f), new Vector3(-0.25f, 0.5f, -0.5f), 2, 5), new SilhouetteMesh.Edge(new Vector3(0.25f, 0.5f, -0.5f), new Vector3(0.25f, 0.5f, 0.5f), 2, 3), new SilhouetteMesh.Edge(new Vector3(0.5f, -0.5f, -0.5f), new Vector3(0.25f, 0.5f, -0.5f), 3, 5), new SilhouetteMesh.Edge(new Vector3(0.5f, -0.5f, -0.5f), new Vector3(0.5f, -0.5f, 0.5f), 4, 3), new SilhouetteMesh.Edge(new Vector3(-0.5f, -0.5f, -0.5f), new Vector3(0.5f, -0.5f, -0.5f), 4, 5)}, new SilhouetteMesh.Face[]{new SilhouetteMesh.Face(new Vector3(0.0f, 0.0f, 1.0f)), new SilhouetteMesh.Face(new Vector3(-b.x, b.y, b.z)), new SilhouetteMesh.Face(new Vector3(0.0f, 1.0f, 0.0f)), new SilhouetteMesh.Face(new Vector3(b)), new SilhouetteMesh.Face(new Vector3(0.0f, -1.0f, 0.0f)), new SilhouetteMesh.Face(new Vector3(0.0f, 0.0f, -1.0f))});
            _mesh = new Mesh(true, 30, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
            float[] v = {-0.25f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f, -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.25f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f, 0.25f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f, -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, -0.25f, 0.5f, -0.5f, -b.x, b.y, b.z, 0.0f, 1.0f, -0.5f, -0.5f, -0.5f, -b.x, b.y, b.z, 0.0f, 0.0f, -0.25f, 0.5f, 0.5f, -b.x, b.y, b.z, 1.0f, 1.0f, -0.25f, 0.5f, 0.5f, -b.x, b.y, b.z, 1.0f, 1.0f, -0.5f, -0.5f, -0.5f, -b.x, b.y, b.z, 0.0f, 0.0f, -0.5f, -0.5f, 0.5f, -b.x, b.y, b.z, 1.0f, 0.0f, 0.25f, 0.5f, 0.5f, b.x, b.y, b.z, 0.0f, 1.0f, 0.5f, -0.5f, 0.5f, b.x, b.y, b.z, 0.0f, 0.0f, 0.25f, 0.5f, -0.5f, b.x, b.y, b.z, 1.0f, 1.0f, 0.25f, 0.5f, -0.5f, b.x, b.y, b.z, 1.0f, 1.0f, 0.5f, -0.5f, 0.5f, b.x, b.y, b.z, 0.0f, 0.0f, 0.5f, -0.5f, -0.5f, b.x, b.y, b.z, 1.0f, 0.0f, -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f, -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, -0.25f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, -0.25f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.25f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.25f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, -0.25f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.25f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f};
            _mesh.setVertices(v);
            _bd = new BodyDef();
            _bd.type = BodyDef.BodyType.DynamicBody;
            _fd = new FixtureDef();
            _shape = new PolygonShape();
            _shape.set(new Vector2[]{new Vector2(0.5f, 0.5f), new Vector2(-0.5f, 0.5f), new Vector2(-1.0f, -0.5f), new Vector2(1.0f, -0.5f)});
            _fd.density = 20.0f;
            _fd.friction = 0.3f;
            _fd.restitution = 0.0f;
            _fd.shape = _shape;
            _initialized = true;
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (Game.sandbox) {
            this.body.setType(BodyDef.BodyType.DynamicBody);
        } else {
            this.body.setType(BodyDef.BodyType.StaticBody);
        }
    }

    @Override
    public void play() {
        super.play();
        this.body.setType(BodyDef.BodyType.DynamicBody);
    }

    public Weight(World world) {
        this.f = null;
        if (!_initialized) {
            _init();
        }
        this.sandbox_only = true;
        this.body = world.createBody(_bd);
        this.f = this.body.createFixture(_fd);
        this.body.setUserData(this);
        this.layer = 0;
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.StaticBody;
    }

    @Override
    public final void render() {
        if (!this.culled) {
            Vector2 pos = this.body.getPosition();
            float angle = get_angle();
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
            G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(2.0f, 1.0f, 1.0f);
            _mesh.render(4);
            G.gl.glPopMatrix();
        }
    }

    public final void draw_shadow_volume(Vector3 lightdir) {
        float angle = get_angle();
        tmp2.set(get_position());
        float z = 0.125f + this.layer;
        if (Math.abs(angle - this.last_angle) > 0.01d) {
            SilhouetteMesh transformed = _silhouette.transform_mark_backfaces(tmp2.x, tmp2.y, z, 2.0f, 1.0f, 1.0f, 57.29578f * get_angle(), lightdir);
            this.silhouette_mesh = transformed.generate_mesh(this.silhouette_mesh, lightdir, this.layer);
            this.last_angle = angle;
        }
        G.gl.glPushMatrix();
        G.gl.glTranslatef(tmp2.x, tmp2.y, z);
        this.silhouette_mesh.render(4);
        G.gl.glPopMatrix();
    }

    @Override
    public void on_click() {
    }

    @Override
    public void step(float deltatime) {
    }

    @Override
    public Vector2 get_position() {
        return this.body.getPosition();
    }

    @Override
    public float get_bb_radius() {
        return 0.0f;
    }

    @Override
    public void reshape() {
        this.body.destroyFixture(this.f);
        this.f = this.body.createFixture(_fd);
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }

    @Override
    public void update_properties() {
    }
}
