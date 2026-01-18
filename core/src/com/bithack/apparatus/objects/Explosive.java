package com.bithack.apparatus.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.SoundManager;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.TextureFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public abstract class Explosive extends GrabableObject implements QueryCallback, RayCastCallback {
    private static BodyDef _bd;
    private static FixtureDef _fd;
    public static Texture _firetex;
    public static Texture _lighttex;
    private static PolygonShape _shape;
    public static Texture _texture;
    private Fixture f;
    public boolean triggered = false;
    protected static boolean _initialized = false;
    private static Vector2 tmp2 = new Vector2();
    static final float[] _material = {0.27450982f, 0.26862746f, 0.20196079f, 0.2f, 0.49411765f, 0.48352942f, 0.3635294f, 0.9f, 0.54901963f, 0.5372549f, 0.40392157f, 1.0f, 10.0f, 0.0f, 0.0f, 0.0f};
    static Random _r = new Random();
    public static ArrayList<Fire> fires = new ArrayList<>();
    public static ArrayList<Projectile> projectiles = new ArrayList<>();
    public static ArrayList<Light> lights = new ArrayList<>();

    public static void _init() {
        if (!_initialized) {
            _bd = new BodyDef();
            _bd.type = BodyDef.BodyType.DynamicBody;
            _fd = new FixtureDef();
            _shape = new PolygonShape();
            _shape.set(new Vector2[]{new Vector2(0.5f, 0.5f).mul(0.5f), new Vector2(-0.5f, 0.5f).mul(0.5f), new Vector2(-1.0f, -0.5f).mul(0.5f), new Vector2(1.0f, -0.5f).mul(0.5f)});
            _fd.density = 1.0f;
            _fd.friction = 0.9f;
            _fd.restitution = 0.0f;
            _fd.shape = _shape;
            _firetex = TextureFactory.load("data/explosion.png");
            _lighttex = TextureFactory.load("data/light.png");
            _texture = TextureFactory.load("data/mine.png");
            _initialized = true;
        }
    }

    public static void init_materials() {
        G.gl.glMaterialfv(1032, GL10.GL_AMBIENT, _material, 0);
        G.gl.glMaterialfv(1032, GL10.GL_DIFFUSE, _material, 4);
        G.gl.glMaterialfv(1032, GL10.GL_SPECULAR, _material, 8);
        G.gl.glMaterialfv(1032, GL10.GL_SHININESS, _material, 12);
    }

    @Override
    public void pause() {
        this.f.setSensor(false);
        super.pause();
        if (Game.sandbox) {
            this.body.setType(BodyDef.BodyType.DynamicBody);
        } else {
            this.body.setType(BodyDef.BodyType.StaticBody);
        }
        this.triggered = false;
    }

    @Override
    public void play() {
        this.f.setSensor(false);
        this.triggered = false;
        super.play();
        this.body.setType(BodyDef.BodyType.DynamicBody);
    }

    public Explosive(World world) {
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
    public float reportRayFixture(Fixture f, Vector2 point, Vector2 arg2, float arg3) {
        Body b = f.getBody();
        if (b != this.body && b.getType() == BodyDef.BodyType.DynamicBody) {
            tmp2.set(get_position());
            tmp2.sub(point);
            float dist = tmp2.len();
            tmp2.nor();
            Object o = b.getUserData();
            if (!(o instanceof RopeEnd) && !(o instanceof CableEnd) && !(o instanceof PanelCableEnd) && dist > 0.1f) {
                tmp2.x = (1.0f / dist) * tmp2.x;
                tmp2.y = (1.0f / dist) * tmp2.y;
                tmp2.mul(-8.0f);
                b.applyLinearImpulse(tmp2, point, true);
            }
        }
        return 1.0f;
    }

    @Override
    public boolean reportFixture(Fixture f1) {
        Body b = f1.getBody();
        if (b != this.body && b.getType() == BodyDef.BodyType.DynamicBody) {
            tmp2.set(get_position());
            tmp2.sub(b.getPosition());
            float dist = tmp2.len();
            tmp2.nor();
            if (dist > 0.1f) {
                tmp2.x = (2.0f / dist) * tmp2.x;
                tmp2.y = (2.0f / dist) * tmp2.y;
                tmp2.mul(-10000.0f);
                Gdx.app.log("tmp2", new StringBuilder().append(tmp2).toString());
                b.applyForce(tmp2, b.getWorldCenter(), true);
            }
        }
        return true;
    }

    public void trigger(World world) {
        if (!this.triggered) {
            Vector2 pos = get_position();
            this.triggered = true;
            for (int x = 0; x < 24; x++) {
                world.rayCast(this, pos, tmp2.cpy().set((float) (Math.cos(x * 0.2617993877991494d) * 5.0d), (float) (Math.sin(x * 0.2617993877991494d) * 5.0d)).add(pos));
            }
            for (int x2 = 0; x2 < 30; x2++) {
                fires.add(new Fire(x2, get_position()));
            }
            for (int x3 = 0; x3 < 10; x3++) {
                projectiles.add(new Projectile(get_position()));
            }
            lights.add(new Light(get_position()));
            SoundManager.play_explosion();
            this.f.setSensor(true);
        }
    }

    public static class Fire {
        Vector2 pos = new Vector2();
        float time;

        public Fire(int p, Vector2 origo) {
            this.time = 0.0f;
            this.pos.set(origo);
            this.pos.x += (Explosive._r.nextFloat() - 1.0f) * 2.0f;
            this.pos.y += (Explosive._r.nextFloat() - 1.0f) * 2.0f;
            this.time = 0.5f - ((p / 10) * 0.5f);
        }
    }

    public static class Projectile {
        float time = 0.0f;
        Vector2 pos = new Vector2();
        Vector2 dir = new Vector2();

        public Projectile(Vector2 pos) {
            this.pos.set(pos);
            float angle = (float) (Explosive._r.nextFloat() * 6.283185307179586d);
            this.dir.set((float) Math.cos(angle), (float) Math.sin(angle));
        }
    }

    public static class Light {
        Vector2 pos = new Vector2();
        float time;

        public Light(Vector2 pos) {
            this.pos.set(pos);
            this.time = 0.0f;
        }
    }

    public static void render_explosions() {
        fires.size();
        G.batch.setBlendFunction(770, 771);
        G.batch.begin();
        Iterator<Projectile> i = projectiles.iterator();
        while (i.hasNext()) {
            Projectile l = i.next();
            l.time += G.delta;
            if (l.time > 0.5f) {
                i.remove();
            } else {
                G.batch.setColor(0.0f, 0.0f, 0.0f, 1.0f - (l.time * 2.0f));
                tmp2.set(l.dir).mul(l.time * 32.0f);
                tmp2.y += l.time * l.time * (-4.8f);
                tmp2.add(l.pos);
                G.batch.draw(_firetex, tmp2.x, tmp2.y, 0.0f, 0.0f, 0.3f, 0.3f, 1.0f, 1.0f, 0.0f, 0, 0, 128, 128, false, false);
            }
        }
        Iterator<Fire> i2 = fires.iterator();
        while (i2.hasNext()) {
            Fire f = i2.next();
            f.time += G.delta * 6.0f;
            if (f.time >= 0.0f) {
                float time = f.time;
                float alpha = 1.0f;
                if (time > 1.0f) {
                    alpha = 1.0f - (time - 1.0f);
                    time = 1.0f;
                    if (alpha <= 0.0f) {
                        i2.remove();
                    }
                }
                float t1 = time < 0.0f ? 0.0f : time;
                float t2 = t1 * t1;
                G.batch.setColor(1.0f - (0.8f * t2), 1.0f - t2, 1.0f - t1, alpha);
                G.batch.draw(_firetex, f.pos.x, f.pos.y, 0.0f, 0.0f, 2.0f, 2.0f, 1.0f, 1.0f, 0.0f, 0, 0, 128, 128, false, false);
            }
        }
        Iterator<Light> i3 = lights.iterator();
        while (i3.hasNext()) {
            Light l2 = i3.next();
            l2.time += G.delta * 8.0f;
            if (l2.time > 1.0f) {
                i3.remove();
            } else {
                G.batch.setColor(1.0f, 1.0f, 0.95f, (1.0f - l2.time) * 0.5f);
                G.batch.draw(_lighttex, l2.pos.x - 8.0f, l2.pos.y - 8.0f, 0.0f, 0.0f, 16.0f, 16.0f, 1.0f, 1.0f, 0.0f, 0, 0, 32, 32, false, false);
                G.batch.setColor(1.0f, 1.0f, 0.95f, 1.0f - (l2.time * l2.time));
                G.batch.draw(_lighttex, l2.pos.x - 2.0f, l2.pos.y - 2.0f, 0.0f, 0.0f, 4.0f, 4.0f, 1.0f, 1.0f, 0.0f, 0, 0, 32, 32, false, false);
            }
        }
        G.batch.end();
    }
}
