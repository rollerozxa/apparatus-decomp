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
import com.bithack.apparatus.objects.Panel;

/* loaded from: classes.dex */
public class PanelCableEnd extends GrabableObject implements BaseCableEnd {
    static final int BATTERY = 1;
    static final int BUTTON = 5;
    static final int HUB = 4;
    static final int PANEL = 2;
    static final int ROCKETENGINE = 3;
    public static DistanceJointDef _djd;
    protected static FixtureDef _fd;
    public final PanelCable cable;
    private Fixture f;
    private DistanceJoint joint;
    private final World world;
    private static Vector2 tmp = new Vector2();
    public static boolean _initialized = false;
    private GrabableObject pending = null;
    boolean attached = false;
    private Vector2 anchor = new Vector2();
    int attached_type = 0;
    int socket = -1;
    Panel.Connection connection = null;
    Hub.Connection hub_connection = null;
    public Panel attached_panel = null;
    public Battery attached_battery = null;
    public RocketEngine attached_rengine = null;
    public Hub attached_hub = null;
    public Button attached_button = null;
    public GrabableObject attached_object = null;
    public int oid = -1;
    public int saved_oid = -1;

    public PanelCableEnd(World world, PanelCable cable) {
        this.f = null;
        if (!_initialized) {
            _init();
        }
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
            _fd.density = 0.1f;
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

    @Override // com.bithack.apparatus.objects.BaseObject
    public void on_click() {
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

    @Override // com.bithack.apparatus.objects.BaseObject
    public void render() {
        render(0.0f);
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void step(float deltatime) {
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public Vector2 get_position() {
        return this.attached ? this.anchor : this.body.getPosition();
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public float get_bb_radius() {
        return 0.0f;
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void dispose(World world) {
        detach();
        world.destroyBody(this.body);
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void reshape() {
        this.body.destroyFixture(this.f);
        this.f = this.body.createFixture(_fd);
    }

    private void fix_anchor_pos() {
        if (this.attached_type == 1) {
            if (this.attached_battery.size == 1) {
                this.anchor.set(this.attached_battery.body.getWorldPoint(tmp.set((-0.0f) + (this.socket * 0.3f), -0.3f)));
                return;
            } else {
                this.anchor.set(this.attached_battery.body.getWorldPoint(tmp.set((-0.2f) + (this.socket * 0.4f), -0.6f)));
                return;
            }
        }
        if (this.attached_type == 2) {
            this.anchor.set(this.attached_panel.body.getWorldPoint(this.connection.pos));
            return;
        }
        if (this.attached_type == 3) {
            this.anchor.set(this.attached_rengine.body.getWorldPoint(tmp.set(-0.25f, 1.0f)));
        } else if (this.attached_type == 4) {
            this.anchor.set(this.attached_hub.body.getWorldPoint(this.hub_connection.pos));
        } else if (this.attached_type == 5) {
            this.anchor.set(this.attached_button.body.getWorldPoint(tmp.set(0.0f, 0.0f)));
        }
    }

    public void tick() {
        Hub p;
        Hub.Connection c;
        Panel p2;
        Panel.Connection c2;
        if (this.pending != null) {
            if (this.pending instanceof Battery) {
                if (this.cable.get_battery() == null) {
                    Battery b = (Battery) this.pending;
                    int i = b.get_available_socket();
                    this.socket = i;
                    if (i != -1) {
                        this.attached_battery = b;
                        this.attached_object = b;
                        this.attached_type = 1;
                        this.attached = true;
                        int i2 = b.__unique_id;
                        this.oid = i2;
                        this.saved_oid = i2;
                        b.connect_socket(this.socket, this);
                    }
                }
            } else if (this.pending instanceof Panel) {
                if (this.cable.get_panel() == null && ((this.cable.get_hub() == null || (!this.cable.get_hub().is_connected_to_panel((Panel) this.pending) && this.cable.get_hub().get_first_button() == null)) && (c2 = (p2 = (Panel) this.pending).get_available_socket(get_position(), this.__unique_id)) != null)) {
                    c2.attach(this);
                    this.connection = c2;
                    this.attached_panel = p2;
                    this.attached_object = p2;
                    this.attached_type = 2;
                    this.attached = true;
                    int i3 = p2.__unique_id;
                    this.oid = i3;
                    this.saved_oid = i3;
                }
            } else if (this.pending instanceof Hub) {
                if (this.cable.get_hub() == null && (c = (p = (Hub) this.pending).get_available_socket(get_position(), this)) != null) {
                    c.attach(this);
                    this.hub_connection = c;
                    this.attached_hub = p;
                    this.attached_object = p;
                    this.attached_type = 4;
                    this.attached = true;
                    int i4 = p.__unique_id;
                    this.oid = i4;
                    this.saved_oid = i4;
                }
            } else if (this.pending instanceof RocketEngine) {
                if (this.cable.get_rengine() == null) {
                    RocketEngine b2 = (RocketEngine) this.pending;
                    if (!b2.attached) {
                        this.attached_rengine = b2;
                        this.attached_object = b2;
                        this.attached_type = 3;
                        this.attached = true;
                        int i5 = b2.__unique_id;
                        this.oid = i5;
                        this.saved_oid = i5;
                        b2.attached = true;
                        b2.attached_cable_end = this;
                    }
                }
            } else if ((this.pending instanceof Button) && this.cable.get_button() == null && (this.cable.get_hub() == null || this.cable.get_hub().get_panel() == null)) {
                Button b3 = (Button) this.pending;
                if (!b3.attached) {
                    this.attached_button = b3;
                    this.attached_object = b3;
                    this.attached_type = 5;
                    this.attached = true;
                    int i6 = b3.__unique_id;
                    this.oid = i6;
                    this.saved_oid = i6;
                    b3.attached = true;
                    b3.panel_cable = this.cable;
                    b3.panel_cable_end = this;
                }
            }
            if (this.attached) {
                fix_anchor_pos();
                create_joint();
            }
            this.pending = null;
            return;
        }
        if (this.attached) {
            fix_anchor_pos();
            float reaction = this.joint.getReactionForce(50.0f).len2();
            boolean destroy = false;
            if (Game.mode == 3) {
                if (reaction > 62500.0f) {
                    destroy = true;
                }
            } else if (reaction > 1.0E7f) {
                destroy = true;
            }
            if (destroy) {
                if (Game.mode != 3) {
                    Game.connectanims.add(new Game.ConnectAnim(1, this.anchor.x, this.anchor.y));
                }
                this.cable.on_disconnect();
                detach();
            }
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void play() {
        this.saved_oid = this.oid;
        super.play();
    }

    private void create_joint() {
        if (this.joint != null) {
            this.world.destroyJoint(this.joint);
            this.joint = null;
        }
        fix_anchor_pos();
        super.translate(this.anchor.x, this.anchor.y);
        _djd.initialize(this.body, this.attached_object.body, this.anchor, this.anchor);
        this.joint = (DistanceJoint) this.world.createJoint(_djd);
        if (Game.do_connectanims) {
            Game.connectanims.add(new Game.ConnectAnim(0, this.anchor.x, this.anchor.y));
            SoundManager.play_connect();
        }
    }

    @Override // com.bithack.apparatus.objects.BaseObject
    public void update_properties() {
    }

    @Override // com.bithack.apparatus.objects.GrabableObject, com.bithack.apparatus.objects.BaseObject
    public void translate(float x, float y) {
        if (!this.attached) {
            super.translate(x, y);
            return;
        }
        if (this.anchor.dst(x, y) > 3.5f) {
            detach();
            super.translate(x, y);
            return;
        }
        if (this.attached_rengine == null && this.anchor.dst(x, y) > 2.5f) {
            detach();
            super.translate(x, y);
            return;
        }
        if (this.attached_type == 2) {
            Panel.Connection c = this.attached_panel.get_available_socket(tmp.set(x, y), this.__unique_id);
            if (this.connection != c) {
                this.connection.detach();
                this.connection = c;
                c.attach(this);
                create_joint();
                return;
            }
            return;
        }
        if (this.attached_type == 4) {
            Hub.Connection c2 = this.attached_hub.get_available_socket(tmp.set(x, y), this);
            if (c2 == null) {
                detach();
            } else if (this.hub_connection != c2) {
                this.hub_connection.detach();
                this.hub_connection = c2;
                c2.attach(this);
                create_joint();
            }
        }
    }

    @Override // com.bithack.apparatus.objects.BaseCableEnd
    public int get_unique_id() {
        return this.__unique_id;
    }

    @Override // com.bithack.apparatus.objects.BaseCableEnd
    public void detach() {
        if (this.attached) {
            if (this.attached_type == 4) {
                this.hub_connection.detach();
            } else if (this.attached_type == 2) {
                this.connection.detach();
            } else if (this.attached_type == 1) {
                this.attached_battery.disconnect_socket(this.socket);
            } else if (this.attached_type == 3) {
                this.attached_rengine.attached = false;
            } else if (this.attached_type == 5) {
                this.attached_button.attached = false;
            }
            if (this.joint != null) {
                this.world.destroyJoint(this.joint);
                this.joint = null;
            }
            if (Game.mode == 3) {
                this.oid = -1;
            } else {
                this.oid = -1;
                this.saved_oid = -1;
            }
            this.attached = false;
            this.attached_battery = null;
            this.attached_panel = null;
            this.attached_hub = null;
            this.attached_button = null;
            this.attached_rengine = null;
            this.attached_type = -1;
            if (Game.do_connectanims || Game.mode == 3) {
                SoundManager.play_disconnect();
            }
        }
    }

    public void attach_to_hub(Hub b) {
        if (!this.attached && Game.mode != 3) {
            this.pending = b;
        }
    }

    public void attach_to_battery(Battery b) {
        if (!this.attached && Game.mode != 3) {
            this.pending = b;
        }
    }

    public void attach_to_panel(Panel p) {
        if (!this.attached && Game.mode != 3) {
            this.pending = p;
        }
    }

    public void attach_to_rengine(RocketEngine p) {
        if (!this.attached && Game.mode != 3) {
            this.pending = p;
        }
    }

    @Override // com.bithack.apparatus.objects.BaseCableEnd
    public void update_pos() {
        fix_anchor_pos();
        this.body.setTransform(this.anchor, this.body.getAngle());
    }

    @Override // com.bithack.apparatus.objects.BaseRopeEnd
    public BaseRope get_baserope() {
        return this.cable;
    }

    public void attach_to_button(Button b) {
        if (!this.attached && Game.mode != 3) {
            this.pending = b;
        }
    }

    @Override // com.bithack.apparatus.objects.GrabableObject
    public void tja_translate(float x, float y) {
        translate(x, y);
    }
}
