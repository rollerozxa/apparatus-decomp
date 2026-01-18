package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.SoundManager;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.objects.Hub;

public class CableEnd extends GrabableObject implements BaseCableEnd {
    public static DistanceJointDef _djd;
    protected static FixtureDef _fd;
    public final Cable cable;
    private Fixture f;
    private DistanceJoint joint;
    private final World world;
    private static Vector2 tmp = new Vector2();
    public static boolean _initialized = false;
    private GrabableObject pending = null;
    Hub attached_hub = null;
    GrabableObject attached_object = null;
    private boolean attached = false;
    private Vector2 anchor = new Vector2();
    public int oid = -1;
    public int saved_oid = -1;
    private Hub.Connection hub_connection = null;

    public CableEnd(World world, Cable cable) {
        this.f = null;
        this.world = world;
        this.cable = cable;
        this.body = world.createBody(Rope._bd);
        _fd.isSensor = true;
        this.f = this.body.createFixture(_fd);
        this.body.setUserData(this);
        this.sandbox_only = false;
        this.fixed_layer = true;
        this.fixed_rotation = true;
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.DynamicBody;
        this.layer = 1;
    }

    public static void _init() {
        if (!_initialized) {
            _fd = new FixtureDef();
            _fd.density = 0.05f;
            _fd.friction = 0.0f;
            _fd.restitution = 0.0f;
            _fd.shape = new CircleShape();
            _fd.shape.setRadius(0.4f);
            _djd = new DistanceJointDef();
            _djd.collideConnected = false;
            _djd.frequencyHz = 30.0f;
            _djd.dampingRatio = 0.0f;
            _initialized = true;
        }
    }

    @Override
    public void on_click() {
    }

    @Override
    public void translate(float x, float y) {
        if (this.attached) {
            if (this.attached_hub != null) {
                this.anchor.set(this.attached_hub.body.getWorldPoint(this.hub_connection.pos));
                Hub.Connection c = this.attached_hub.get_available_socket(tmp.set(x, y), this);
                if (this.hub_connection != c) {
                    this.hub_connection.detach();
                    this.hub_connection = c;
                    c.attach(this);
                    create_joint();
                }
            }
            if (this.body.getPosition().dst(x, y) > 3.0f) {
                detach();
                super.translate(x, y);
                return;
            }
            return;
        }
        super.translate(x, y);
    }

    public void render(float angle) {
        Vector2 pos = get_state().position;
        if (this.attached) {
            G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, 1.2f);
            G.gl.glRotatef(this.attached_object.get_state().angle * 57.295776f, 0.0f, 0.0f, 1.0f);
            G.gl.glPushMatrix();
            G.gl.glScalef(0.25f, 0.25f, 0.5f);
            MiscRenderer.draw_colored_cube();
            G.gl.glPopMatrix();
            G.gl.glPopMatrix();
            return;
        }
        G.gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        G.gl.glPushMatrix();
        G.gl.glTranslatef(pos.x, pos.y, 1.6f);
        G.gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
        G.gl.glPushMatrix();
        G.gl.glScalef(0.25f, 0.5f, 0.15f);
        MiscRenderer.draw_colored_cube();
        G.gl.glPopMatrix();
        G.gl.glColor4f(0.9f, 0.9f, 0.9f, 1.0f);
        G.gl.glPushMatrix();
        G.gl.glTranslatef(0.05f, -0.4f, 0.0f);
        G.gl.glScalef(0.05f, 0.4f, 0.1f);
        MiscRenderer.draw_colored_cube();
        G.gl.glPopMatrix();
        G.gl.glPushMatrix();
        G.gl.glTranslatef(-0.05f, -0.4f, 0.0f);
        G.gl.glScalef(0.05f, 0.4f, 0.1f);
        MiscRenderer.draw_colored_cube();
        G.gl.glPopMatrix();
        G.gl.glPopMatrix();
    }

    @Override
    public void render() {
        render(0.0f);
    }

    @Override
    public void step(float deltatime) {
    }

    @Override
    public Vector2 get_position() {
        return this.attached ? this.anchor : this.body.getPosition();
    }

    @Override
    public float get_bb_radius() {
        return 0.0f;
    }

    @Override
    public void dispose(World world) {
        if (this.attached) {
            if (this.joint != null) {
                world.destroyJoint(this.joint);
            }
            if (this.hub_connection != null) {
                this.hub_connection.detach();
            }
            if (this.attached_object != null) {
                this.attached_object.cabled = false;
                this.attached_object.cable = null;
            }
        }
        world.destroyBody(this.body);
    }

    @Override
    public void reshape() {
        this.body.destroyFixture(this.f);
        this.f = this.body.createFixture(_fd);
    }

    private void fix_anchor() {
        if (this.attached_object instanceof Battery) {
            if (((Battery) this.attached_object).size == 1) {
                this.anchor.set(this.attached_object.body.getWorldPoint(tmp.set(-0.3f, -0.3f)));
                return;
            } else {
                this.anchor.set(this.attached_object.body.getWorldPoint(tmp.set(-0.8f, -0.6f)));
                return;
            }
        }
        if (this.attached_hub != null) {
            this.anchor.set(this.attached_hub.body.getWorldPoint(this.hub_connection.pos));
        } else {
            this.anchor.set(this.attached_object.body.getWorldPoint(tmp.set(0.0f, -0.5f)));
        }
    }

    private void create_joint() {
        if (this.joint != null) {
            this.world.destroyJoint(this.joint);
            this.joint = null;
        }
        fix_anchor();
        super.translate(this.anchor.x, this.anchor.y);
        _djd.initialize(this.body, this.attached_object.body, this.anchor, this.anchor);
        this.joint = (DistanceJoint) this.world.createJoint(_djd);
        if (Game.do_connectanims) {
            Game.connectanims.add(new Game.ConnectAnim(0, this.anchor.x, this.anchor.y));
        }
        this.cable.on_connect();
    }

    public void tick() {
        if (this.pending != null) {
            if (this.pending.cabled && !(this.pending instanceof Hub)) {
                this.pending = null;
                return;
            }
            if (this.pending instanceof Battery) {
                if (((Battery) this.pending).size == 1) {
                    this.anchor.set(this.pending.body.getWorldPoint(tmp.set(-0.3f, -0.3f)));
                } else {
                    this.anchor.set(this.pending.body.getWorldPoint(tmp.set(-0.8f, -0.6f)));
                }
            } else if (this.pending instanceof Hub) {
                this.anchor.set(this.pending.body.getWorldPoint(tmp.set(0.0f, -0.5f)));
                Hub.Connection c = ((Hub) this.pending).get_available_socket(get_position(), this);
                if (c != null) {
                    c.attach(this);
                    this.hub_connection = c;
                    Hub hub = (Hub) this.pending;
                    this.attached_hub = hub;
                    this.attached_object = hub;
                    this.attached = true;
                } else {
                    this.pending = null;
                    return;
                }
            }
            this.attached_object = this.pending;
            this.pending = null;
            this.attached_object.cabled = true;
            this.attached_object.cable = this.cable;
            this.attached = true;
            int i = this.attached_object.__unique_id;
            this.oid = i;
            this.saved_oid = i;
            create_joint();
            if (Game.do_connectanims) {
                SoundManager.play_connect();
                return;
            }
            return;
        }
        if (this.attached) {
            fix_anchor();
            float reaction = this.joint.getReactionForce(50.0f).len2();
            boolean destroy = false;
            if (Game.mode == Game.MODE_PLAY) {
                if (reaction > 62500.0f) {
                    destroy = true;
                }
            } else if (reaction > 1.0E7f) {
                destroy = true;
            }
            if (destroy) {
                if (Game.mode != Game.MODE_PLAY && Game.do_connectanims) {
                    Game.connectanims.add(new Game.ConnectAnim(1, this.anchor.x, this.anchor.y));
                } else if (Game.mode == Game.MODE_PLAY) {
                    SoundManager.play_disconnect();
                }
                if (this.joint != null) {
                    this.world.destroyJoint(this.joint);
                    this.joint = null;
                }
                detach();
                this.cable.on_disconnect();
            }
        }
    }

    @Override
    public int get_unique_id() {
        return this.__unique_id;
    }

    @Override
    public void play() {
        this.saved_oid = this.oid;
        super.play();
    }

    @Override
    public void detach() {
        if (this.attached) {
            this.oid = -1;
            if (Game.mode != Game.MODE_PLAY) {
                this.saved_oid = -1;
            }
            this.attached_object.cabled = false;
            if (this.attached_object instanceof BaseMotor) {
                ((BaseMotor) this.attached_object).update();
            } else if (this.attached_hub != null) {
                this.hub_connection.detach();
            }
            this.attached_object.cable = null;
            if (this.joint != null) {
                this.world.destroyJoint(this.joint);
                this.joint = null;
            }
            this.attached = false;
            this.attached_object = null;
            this.attached_hub = null;
            if (Game.mode == Game.MODE_PLAY || Game.do_connectanims) {
                SoundManager.play_disconnect();
            }
            this.cable.update();
        }
    }

    public void attach_to(GrabableObject o) {
        if (!o.cabled && !this.attached) {
            this.pending = o;
        }
    }

    public void attach_to_hub(Hub b) {
        if (!this.attached && Game.mode != Game.MODE_PLAY && this.cable.get_hub() == null) {
            this.pending = b;
        }
    }

    @Override
    public void update_pos() {
        fix_anchor();
        this.body.setTransform(this.anchor, this.body.getAngle());
    }

    @Override
    public BaseRope get_baserope() {
        return this.cable;
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }

    @Override
    public void update_properties() {
    }
}
