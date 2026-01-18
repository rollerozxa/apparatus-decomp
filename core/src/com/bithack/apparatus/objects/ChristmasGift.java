package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.graphics.TextureFactory;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.Random;
import java.util.jar.JarOutputStream;

/* loaded from: classes.dex */
public class ChristmasGift extends GrabableObject implements PrimaryObject {
    private static BodyDef _bd;
    private static FixtureDef _fd;
    private static boolean _initialized = false;
    private static PolygonShape _shape;
    public static Texture texture;
    private int img = 0;
    private Fixture f = null;
    public Vector2 size = new Vector2(0.5f, 0.25f);
    private Color color = Color.WHITE;
    public float[] material = {0.058f, 0.11000001f, 0.166f, 1.0f, 0.145f, 0.275f, 0.415f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f};

    public ChristmasGift(World world) {
        if (!_initialized) {
            _init();
        }
        this.body = world.createBody(_bd);
        this.properties = new BaseObject.Property[]{new BaseObject.Property("sx", BaseObject.Property.Type.FLOAT, Float.valueOf(0.5f)), new BaseObject.Property("sy", BaseObject.Property.Type.FLOAT, Float.valueOf(0.25f))};
        this.body.setUserData(this);
        reshape();
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.StaticBody;
        set_property("i", Integer.valueOf(new Random().nextInt(4)));
    }

    public static void _init() {
        _initialized = true;
        _bd = new BodyDef();
        _bd.type = BodyDef.BodyType.StaticBody;
        _shape = new PolygonShape();
        _shape.setAsBox(0.5f, 0.25f);
        _fd = new FixtureDef();
        _fd.shape = _shape;
        _fd.friction = 0.4f;
        _fd.density = 0.4f;
        _fd.restitution = 0.3f;
        texture = TextureFactory.load("data/christmaswrapping.jpg");
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
        BaseObject.State s = get_state();
        Vector2 pos = s.position;
        float angle = s.angle;
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, 0.75f + this.layer);
        G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
        G.gl.glScalef(this.size.x * 2.0f, this.size.y * 2.0f, 1.0f);
        G.gl.glMatrixMode(5890);
        G.gl.glPushMatrix();
        G.gl.glTranslatef(this.img / 4.0f, -0.25f, 0.0f);
        G.gl.glScalef(0.25f * this.size.x * 1.0f, this.size.y * 4.0f * 1.1f, 1.0f);
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
        MiscRenderer.Acubemesh.render(4);
        G.gl.glMatrixMode(5890);
        G.gl.glPopMatrix();
        G.gl.glMatrixMode(GL10.GL_MODELVIEW);
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

    public static void init_materials() {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
        if (this.f != null && this.body.getFixtureList().contains(this.f, false)) {
            this.body.destroyFixture(this.f);
        }
        _shape.setAsBox(this.size.x, this.size.y);
        this.f = this.body.createFixture(_fd);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
        translate(x, y);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void set_property(String name, Object value) {
        if (name.equals("sx")) {
            this.size.x = ((Float) value).floatValue();
            reshape();
        }
        if (name.equals("sy")) {
            this.size.y = ((Float) value).floatValue();
            reshape();
        }
        if (name.equals("i")) {
            this.img = ((Integer) value).intValue();
        }
        super.set_property(name, value);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
        set_property("sx", Float.valueOf(this.size.x));
        set_property("sy", Float.valueOf(this.size.y));
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void write_to_stream(JarOutputStream s) throws IOException {
        update_properties();
        super.write_to_stream(s);
    }
}
