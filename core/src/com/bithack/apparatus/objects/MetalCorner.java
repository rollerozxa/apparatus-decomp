package com.bithack.apparatus.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.Level;
import com.bithack.apparatus.SilhouetteMesh;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.jar.JarOutputStream;

/* loaded from: classes.dex */
public class MetalCorner extends GrabableObject {
    static BodyDef _bd;
    static FixtureDef _fd;
    public static Mesh _mesh;
    static PolygonShape _shape;
    public static SilhouetteMesh _silhouette;
    public static float[] cornervertices;
    public float angle;
    public int b1_id;
    public int b2_id;
    public float baselen;
    public Vector2 center;
    public float height;
    public boolean invalid;
    MetalBar m_a;
    MetalBar m_b;
    private boolean shadow_created;
    public boolean should_render;
    int side_a;
    int side_b;
    protected Mesh silhouette_mesh;
    private static boolean _initialized = false;
    private static Vector2 tmp = new Vector2();
    private static Vector2 pos = new Vector2(0.0f, 0.0f);

    public MetalCorner(World world) {
        this.should_render = false;
        this.shadow_created = false;
        this.center = new Vector2();
        this.invalid = false;
        if (!_initialized) {
            _init();
        }
        init_properties();
        this.ingame_type = BodyDef.BodyType.StaticBody;
        this.build_type = BodyDef.BodyType.StaticBody;
    }

    public MetalCorner(World world, MetalBar a, MetalBar b, int sidea, int sideb) {
        this.should_render = false;
        this.shadow_created = false;
        this.center = new Vector2();
        this.invalid = false;
        if (!_initialized) {
            _init();
        }
        this.side_a = sidea;
        this.side_b = sideb;
        this.angle = 0.0f;
        this.baselen = 0.0f;
        this.height = 0.0f;
        this.b1_id = a.__unique_id;
        this.b2_id = b.__unique_id;
        this.should_render = false;
        init_properties();
        if (!setup(world, a, b)) {
            this.invalid = true;
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void play() {
        if (this.body != null) {
            this.body.setActive(true);
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void pause() {
        if (this.body != null) {
            this.body.setActive(true);
        }
    }

    public boolean setup(World world, MetalBar b1, MetalBar b2) {
        return setup(world, b1, b2, null);
    }

    public boolean setup(World world, MetalBar b1, MetalBar b2, Vector2 cornertop) {
        int a;
        this.m_a = b1;
        this.m_b = b2;
        if (this.side_a == 0) {
            if (this.m_a.attached_a) {
                this.m_a = null;
                this.m_b = null;
                return false;
            }
            this.m_a.attached_a = true;
            this.m_a.corner_a = this;
            a = 0;
        } else {
            if (this.m_a.attached_b) {
                this.m_a = null;
                this.m_b = null;
                return false;
            }
            this.m_a.attached_b = true;
            this.m_a.corner_b = this;
            a = 1;
        }
        if (this.side_b == 0) {
            if (this.m_b.attached_a) {
                if (a == 0) {
                    this.m_a.attached_a = false;
                } else {
                    this.m_a.attached_b = false;
                }
                this.m_a = null;
                this.m_b = null;
                return false;
            }
            this.m_b.attached_a = true;
            this.m_b.corner_a = this;
        } else {
            if (this.m_b.attached_b) {
                if (a == 0) {
                    this.m_a.attached_a = false;
                } else {
                    this.m_a.attached_b = false;
                }
                this.m_a = null;
                this.m_b = null;
                return false;
            }
            this.m_b.attached_b = true;
            this.m_b.corner_b = this;
        }
        if (this.should_render && Level.version < 5) {
            this.body = world.createBody(_bd);
            _shape.set(new Vector2[]{new Vector2((-this.baselen) / 2.0f, 0.0f), new Vector2(this.baselen / 2.0f, 0.0f), new Vector2(0.0f, this.height)});
            this.body.createFixture(_fd);
            this.body.setTransform(this.center, this.angle);
            this.body.setUserData(this);
        }
        this.m_a.reshape_corners();
        this.m_b.reshape_corners();
        return true;
    }

    public MetalCorner(World world, MetalBar a, MetalBar b, Vector2 cornertop, Vector2 basepos1, Vector2 basepos2, int sidea, int sideb) {
        this.should_render = false;
        this.shadow_created = false;
        this.center = new Vector2();
        this.invalid = false;
        if (!_initialized) {
            _init();
        }
        this.should_render = true;
        this.side_a = sidea;
        this.side_b = sideb;
        this.layer = a.layer;
        tmp.set(basepos1).add(basepos2).mul(0.5f);
        this.center.set(tmp);
        basepos2.sub(basepos1);
        this.baselen = basepos2.len();
        this.height = (float) Math.sqrt(1.0f - ((this.baselen / 2.0f) * (this.baselen / 2.0f)));
        this.angle = (float) Math.atan2(basepos2.y, basepos2.x);
        if (cornertop.x == cornertop.y) {
            this.angle = (float) (this.angle - 3.141592653589793d);
        }
        this.b1_id = a.__unique_id;
        this.b2_id = b.__unique_id;
        init_properties();
        if (!setup(world, a, b, null)) {
            this.invalid = true;
        }
    }

    private void init_properties() {
        BaseObject.Property[] propertyArr = new BaseObject.Property[10];
        propertyArr[0] = new BaseObject.Property("r", BaseObject.Property.Type.BOOLEAN, this.should_render ? Boolean.TRUE : Boolean.FALSE);
        propertyArr[1] = new BaseObject.Property("cx", BaseObject.Property.Type.FLOAT, new Float(this.center.x));
        propertyArr[2] = new BaseObject.Property("cy", BaseObject.Property.Type.FLOAT, new Float(this.center.y));
        propertyArr[3] = new BaseObject.Property("b1", BaseObject.Property.Type.INT, new Integer(this.b1_id));
        propertyArr[4] = new BaseObject.Property("b2", BaseObject.Property.Type.INT, new Integer(this.b2_id));
        propertyArr[5] = new BaseObject.Property("blen", BaseObject.Property.Type.FLOAT, new Float(this.baselen));
        propertyArr[6] = new BaseObject.Property("h", BaseObject.Property.Type.FLOAT, new Float(this.height));
        propertyArr[7] = new BaseObject.Property("a", BaseObject.Property.Type.FLOAT, new Float(this.angle));
        propertyArr[8] = new BaseObject.Property("sidea", BaseObject.Property.Type.INT, new Integer(this.side_a));
        propertyArr[9] = new BaseObject.Property("sideb", BaseObject.Property.Type.INT, new Integer(this.side_b));
        this.properties = propertyArr;
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void write_to_stream(JarOutputStream s) throws IOException {
        super.write_to_stream(s);
    }

    public static void _init() {
        if (!_initialized) {
            _silhouette = new SilhouetteMesh(new SilhouetteMesh.Edge[]{new SilhouetteMesh.Edge(new Vector3(0.0f, 1.0f, 0.5f), new Vector3(-0.5f, 0.0f, 0.5f), 0, 1), new SilhouetteMesh.Edge(new Vector3(0.5f, 0.0f, 0.5f), new Vector3(0.0f, 1.0f, 0.5f), 0, 2), new SilhouetteMesh.Edge(new Vector3(-0.5f, 0.0f, 0.5f), new Vector3(0.5f, 0.0f, 0.5f), 0, 3), new SilhouetteMesh.Edge(new Vector3(0.0f, 1.0f, -0.5f), new Vector3(0.0f, 1.0f, 0.5f), 2, 1), new SilhouetteMesh.Edge(new Vector3(-0.5f, 0.0f, -0.5f), new Vector3(-0.5f, 0.0f, 0.5f), 1, 3), new SilhouetteMesh.Edge(new Vector3(0.0f, 1.0f, -0.5f), new Vector3(-0.5f, 0.0f, -0.5f), 1, 4), new SilhouetteMesh.Edge(new Vector3(0.5f, 0.0f, -0.5f), new Vector3(0.0f, 1.0f, -0.5f), 2, 4), new SilhouetteMesh.Edge(new Vector3(0.5f, 0.0f, -0.5f), new Vector3(0.5f, 0.0f, 0.5f), 2, 3), new SilhouetteMesh.Edge(new Vector3(-0.5f, 0.0f, -0.5f), new Vector3(0.5f, 0.0f, -0.5f), 3, 4)}, new SilhouetteMesh.Face[]{new SilhouetteMesh.Face(new Vector3(0.0f, 0.0f, 1.0f)), new SilhouetteMesh.Face(new Vector3(-1.0f, 0.0f, 0.0f)), new SilhouetteMesh.Face(new Vector3(1.0f, 0.0f, 0.0f)), new SilhouetteMesh.Face(new Vector3(0.0f, -1.0f, 0.0f)), new SilhouetteMesh.Face(new Vector3(0.0f, 0.0f, -1.0f))});
            _bd = new BodyDef();
            _bd.type = BodyDef.BodyType.StaticBody;
            _shape = new PolygonShape();
            _fd = new FixtureDef();
            _fd.shape = _shape;
            _mesh = new Mesh(true, 9, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(8, 3, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
            float[] v = {0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.125f, 0.375f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.125f, 0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.575f, 0.5f, 1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f};
            cornervertices = v;
            _mesh.setVertices(v);
            _initialized = true;
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void set_property(String name, Object value) {
        if (name.equals("r")) {
            this.should_render = ((Boolean) value).booleanValue();
            Gdx.app.log("sjk", new StringBuilder(String.valueOf(this.should_render)).toString());
        } else if (name.equals("cx")) {
            this.center.x = ((Float) value).floatValue();
        } else if (name.equals("cy")) {
            this.center.y = ((Float) value).floatValue();
        } else if (name.equals("b1")) {
            this.b1_id = ((Integer) value).intValue();
        } else if (name.equals("b2")) {
            this.b2_id = ((Integer) value).intValue();
        } else if (name.equals("blen")) {
            this.baselen = ((Float) value).floatValue();
        } else if (name.equals("h")) {
            this.height = ((Float) value).floatValue();
        } else if (name.equals("sidea")) {
            this.side_a = ((Integer) value).intValue();
        } else if (name.equals("sideb")) {
            this.side_b = ((Integer) value).intValue();
        } else if (name.equals("a")) {
            this.angle = ((Float) value).floatValue();
        }
        super.set_property(name, value);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
        G.gl.glPushMatrix();
        G.gl.glTranslatef(this.center.x, this.center.y, this.layer);
        G.gl.glRotatef((float) (this.angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
        G.gl.glScalef(this.baselen / 2.0f, this.height, 1.0f);
        _mesh.render(4);
        G.gl.glPopMatrix();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void step(float deltatime) {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void translate(float x, float y) {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public Vector2 get_position() {
        return pos;
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public float get_bb_radius() {
        return 0.0f;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public float get_angle() {
        return 0.0f;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void dispose(World world) {
        if (this.m_a != null) {
            if (this.side_a == 0) {
                this.m_a.attached_a = false;
                this.m_a.corner_a = null;
            } else {
                this.m_a.attached_b = false;
                this.m_a.corner_b = null;
            }
            this.m_a.reshape_corners();
        }
        if (this.m_b != null) {
            if (this.side_b == 0) {
                this.m_b.attached_a = false;
                this.m_b.corner_a = null;
            } else {
                this.m_b.attached_b = false;
                this.m_b.corner_b = null;
            }
            this.m_b.reshape_corners();
        }
        if (this.body != null) {
            world.destroyBody(this.body);
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void set_angle(float angle) {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
