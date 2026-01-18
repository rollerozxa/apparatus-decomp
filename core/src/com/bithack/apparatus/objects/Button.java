package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.ObjectFactory;
import com.bithack.apparatus.SoundManager;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.objects.BaseObject;
import com.bithack.apparatus.objects.Hub;
import java.io.IOException;
import java.util.jar.JarOutputStream;

/* loaded from: classes.dex */
public class Button extends GrabableObject implements FreeObject {
    protected static BodyDef _bd;
    protected static FixtureDef _fd;
    protected static PolygonShape _shape;
    public Fixture f;
    public Fixture sensor;
    private final World world;
    protected static boolean initialized = false;
    protected static Vector2 _tmp = new Vector2();
    public int type = 0;
    public boolean attached = false;
    private boolean active = false;
    public PanelCable panel_cable = null;
    public PanelCableEnd panel_cable_end = null;

    public Button(World world) {
        if (!initialized) {
            init();
        }
        if (!Plank._initialized) {
            Plank._init();
        }
        this.world = world;
        this.layer = 0;
        reshape();
        this.sandbox_only = true;
        this.fixed_layer = true;
        this.properties = new BaseObject.Property[0];
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.StaticBody;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void translate(float x, float y) {
        super.translate(x, y);
    }

    private static void init() {
        if (!initialized) {
            _shape = new PolygonShape();
            _shape.setAsBox(0.4f, 0.1f, new Vector2(0.0f, 0.2f), 0.0f);
            _fd = new FixtureDef();
            _fd.shape = _shape;
            _fd.density = 0.0f;
            _fd.friction = 0.1f;
            _fd.restitution = 0.0f;
            initialized = true;
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1);
            G.gl.glRotatef(get_state().angle * 57.295776f, 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(1.0f, 0.5f, 0.5f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
        }
    }

    public void render_btn() {
        if (!this.culled) {
            Vector2 pos = get_state().position;
            float angle = get_state().angle;
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.0f);
            G.gl.glRotatef((float) (angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            if (this.active) {
                G.gl.glTranslatef(0.0f, 0.15f, 0.0f);
            } else {
                G.gl.glTranslatef(0.0f, 0.25f, 0.0f);
            }
            G.gl.glScalef(0.8f, 0.25f, 0.4f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
        }
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
        super.set_property(name, value);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void write_to_stream(JarOutputStream s) throws IOException {
        super.write_to_stream(s);
    }

    @Override // com.bithack.apparatus.objects.FreeObject
    public void set_layer() {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
        init();
        if (this.body != null) {
            this.world.destroyBody(this.body);
        }
        this.body = ObjectFactory.create_rectangular_body(this.world, 0.5f, 0.25f, 1.0f, 1.0f, 0.0f);
        this.sensor = this.body.createFixture(_fd);
        this.sensor.setUserData(this);
        this.f = this.body.getFixtureList().get(0);
        this.body.setUserData(this);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void play() {
        super.play();
        this.active = false;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void pause() {
        super.pause();
        this.active = false;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void dispose(World world) {
        if (this.attached) {
            this.panel_cable_end.detach();
        }
        super.dispose(world);
    }

    public void activate() {
        if (!this.active) {
            this.active = true;
            SoundManager.play_battery_switch();
            if (this.attached) {
                Battery b = this.panel_cable.get_battery();
                if (b != null) {
                    b.toggle_onoff();
                    return;
                }
                RocketEngine e = this.panel_cable.get_rengine();
                if (e != null) {
                    if (e.active) {
                        e.set_output(0.0f);
                        return;
                    } else {
                        e.set_output(1.0f);
                        return;
                    }
                }
                Hub h = this.panel_cable.get_hub();
                if (h != null) {
                    for (int x = 0; x < h.connections.length; x++) {
                        if (!h.connections[x].available) {
                            Hub.Connection conn = h.connections[x];
                            if (conn.type == 1) {
                                Battery b2 = ((PanelCable) conn.cable).get_battery();
                                if (b2 != null) {
                                    b2.force_toggle_onoff();
                                } else {
                                    RocketEngine e2 = ((PanelCable) conn.cable).get_rengine();
                                    if (e2 != null) {
                                        if (e2.active) {
                                            e2.set_output(0.0f);
                                        } else {
                                            e2.set_output(1.0f);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
