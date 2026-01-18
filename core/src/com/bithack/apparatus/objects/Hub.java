package com.bithack.apparatus.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.ObjectFactory;
import com.bithack.apparatus.graphics.G;
import com.bithack.apparatus.graphics.MiscRenderer;
import com.bithack.apparatus.objects.BaseObject;
import java.io.IOException;
import java.util.jar.JarOutputStream;

public class Hub extends GrabableObject implements FreeObject {
    protected static BodyDef _bd;
    protected static FixtureDef _fd;
    protected static PolygonShape _shape;
    Connection[] connections;
    public Fixture f;
    private Fixture[] sensors;
    private final World world;
    protected static boolean initialized = false;
    protected static Vector2 _tmp = new Vector2();
    private static Vector2[] sensor_pos = {new Vector2(0.0f, 1.0f), new Vector2(0.0f, -1.0f)};
    public float panel_output = 0.0f;
    int num_connections = 5;

    public class Connection {
        BaseCable cable;
        BaseCableEnd cableend;
        Hub panel;
        public int type;
        Vector2 pos = new Vector2();
        int cableend_id = -1;
        boolean available = true;

        public Connection(Hub p, Vector2 pos) {
            this.panel = null;
            this.panel = p;
            this.pos.set(pos);
        }

        public void play() {
        }

        public void attach(BaseCableEnd end) {
            if (this.available) {
                this.type = ((BaseCable) end.get_baserope()).cable_type;
                this.cableend_id = end.get_unique_id();
                this.cableend = end;
                this.cable = (BaseCable) end.get_baserope();
                this.available = false;
            }
        }

        public void detach() {
            if (!this.available) {
                this.cable = null;
                this.cableend = null;
                this.cableend_id = -1;
                this.available = true;
            }
        }
    }

    public Hub(World world) {
        this.connections = null;
        if (!initialized) {
            init();
        }
        if (!Plank._initialized) {
            Plank._init();
        }
        this.world = world;
        this.layer = 0;
        reshape();
        this.body.setUserData(this);
        this.sandbox_only = false;
        this.fixed_layer = true;
        this.properties = new BaseObject.Property[0];
        this.connections = new Connection[this.num_connections];
        for (int x = 0; x < this.num_connections; x++) {
            this.connections[x] = new Connection(this, new Vector2(0.25f + (((-0.5f) * this.num_connections) / 2.0f) + (x * 0.5f), 0.0f));
        }
        this.ingame_type = BodyDef.BodyType.DynamicBody;
        this.build_type = BodyDef.BodyType.DynamicBody;
    }

    @Override
    public void translate(float x, float y) {
        super.translate(x, y);
        for (int i = 0; i < this.connections.length; i++) {
            if (!this.connections[i].available) {
                this.connections[i].cableend.update_pos();
            }
        }
    }

    public void disconnect_all() {
        if (this.connections != null) {
            for (int x = 0; x < this.connections.length; x++) {
                if (!this.connections[x].available) {
                    this.connections[x].cableend.detach();
                }
            }
        }
    }

    private static void init() {
        if (!initialized) {
            initialized = true;
            _shape = new PolygonShape();
            _fd = new FixtureDef();
            _fd.isSensor = true;
            _fd.density = 0.0f;
            _fd.shape = _shape;
        }
    }

    private void create_sensors() {
        destroy_sensors();
        for (int x = 0; x < sensor_pos.length; x++) {
            _shape.setAsBox(1.0f + (Math.abs(sensor_pos[x].y) * 0.5f), 0.25f + (Math.abs(sensor_pos[x].x) * 0.5f), sensor_pos[x].cpy().mul(0.7f), 0.0f);
            this.sensors[x] = this.body.createFixture(_fd);
            this.sensors[x].setUserData(sensor_pos[x]);
        }
    }

    private void destroy_sensors() {
        for (int x = 0; x < sensor_pos.length; x++) {
            if (this.sensors[x] != null) {
                if (this.body.getFixtureList().contains(this.sensors[x], false)) {
                    this.body.destroyFixture(this.sensors[x]);
                }
                this.sensors[x] = null;
            }
        }
    }

    @Override
    public void on_click() {
    }

    public void render_box() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1);
            G.gl.glRotatef(get_state().angle * 57.295776f, 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(2.5f, 0.7f, 0.5f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
        }
    }

    public void render_sockets() {
        if (!this.culled) {
            Vector2 pos = get_state().position;
            G.gl.glPushMatrix();
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1.25f);
            G.gl.glRotatef((float) (get_state().angle * 57.29577951308232d), 0.0f, 0.0f, 1.0f);
            for (int x = 0; x < this.connections.length; x++) {
                G.gl.glPushMatrix();
                G.gl.glTranslatef(this.connections[x].pos.x, this.connections[x].pos.y, 0.0f);
                G.gl.glScalef(0.4f, 0.4f, 0.25f);
                MiscRenderer.hqcubemesh.render(4);
                G.gl.glPopMatrix();
            }
            G.gl.glPopMatrix();
        }
    }

    @Override
    public void render() {
        if (!this.culled) {
            G.gl.glPushMatrix();
            Vector2 pos = get_state().position;
            G.gl.glTranslatef(pos.x, pos.y, this.layer + 1);
            G.gl.glRotatef(get_state().angle * 57.295776f, 0.0f, 0.0f, 1.0f);
            G.gl.glScalef(2.0f, 1.2f, 0.5f);
            MiscRenderer.hqcubemesh.render(4);
            G.gl.glPopMatrix();
            if (this.culled) {
            }
        }
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
    public void set_property(String name, Object value) {
        super.set_property(name, value);
    }

    @Override
    public void write_to_stream(JarOutputStream s) throws IOException {
        super.write_to_stream(s);
    }

    public static void init_materials() {
    }

    @Override
    public void set_layer() {
    }

    @Override
    public void reshape() {
        init();
        disconnect_all();
        this.sensors = new Fixture[sensor_pos.length];
        if (this.body != null) {
            this.world.destroyBody(this.body);
        }
        this.body = ObjectFactory.create_rectangular_body(this.world, 1.25f, 0.35f, 1.0f, 0.2f, 0.1f);
        this.f = this.body.getFixtureList().get(0);
        this.body.setUserData(this);
        create_sensors();
    }

    private int get_type() {
        for (int x = 0; x < this.connections.length; x++) {
            if (!this.connections[x].available) {
                return this.connections[x].type;
            }
        }
        return 0;
    }

    public Connection get_available_socket(Vector2 pos, BaseCableEnd c_end) {
        Connection nearest = null;
        float lastdist = 1.0E8f;
        int o_id = c_end.get_unique_id();
        int mytype = get_type();
        if (mytype == 0 || mytype == ((BaseCable) c_end.get_baserope()).cable_type) {
            if (((BaseCable) c_end.get_baserope()).cable_type == 1 && mytype == 1) {
                if (((PanelCableEnd) c_end).cable.get_panel() != null) {
                    for (int x = 0; x < this.connections.length; x++) {
                        if (!this.connections[x].available && ((PanelCable) this.connections[x].cable).get_panel() != null && ((PanelCableEnd) c_end).cable != ((PanelCable) this.connections[x].cable)) {
                            return null;
                        }
                        if (!this.connections[x].available && ((PanelCable) this.connections[x].cable).get_button() != null) {
                            return null;
                        }
                    }
                }
                if (((PanelCableEnd) c_end).cable.get_button() != null) {
                    for (int x2 = 0; x2 < this.connections.length; x2++) {
                        if (!this.connections[x2].available && ((PanelCable) this.connections[x2].cable).get_panel() != null) {
                            return null;
                        }
                    }
                }
            }
            for (int x3 = 0; x3 < this.connections.length; x3++) {
                if (this.connections[x3].available || this.connections[x3].cableend_id == o_id) {
                    float dist = this.body.getWorldPoint(this.connections[x3].pos).dst(pos);
                    if (dist < lastdist) {
                        lastdist = dist;
                        nearest = this.connections[x3];
                    }
                }
            }
        }
        return nearest;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Connection _get_available_socket(GrabableObject grabableObject) {
        Vector2 pos = grabableObject.body.getPosition();
        return get_available_socket(pos, (BaseCableEnd) grabableObject);
    }

    @Override
    public void play() {
        super.play();
        this.panel_output = 0.0f;
        update();
    }

    @Override
    public void dispose(World world) {
        for (int x = 0; x < this.connections.length; x++) {
            if (!this.connections[x].available) {
                this.connections[x].cableend.detach();
            }
        }
        super.dispose(world);
    }

    public void update() {
        BaseMotor m;
        int type = get_type();
        float voltage = 0.0f;
        float current = 0.0f;
        int num_motors = 0;
        if (type == 2) {
            for (int x = 0; x < this.connections.length; x++) {
                if (!this.connections[x].available) {
                    Cable c = (Cable) this.connections[x].cable;
                    Battery b = c.get_battery();
                    if (b != null && b.on) {
                        voltage += b.voltage * b.output;
                        current += b.current * b.output;
                    } else if (c.get_motor() != null) {
                        num_motors++;
                    }
                }
            }
        } else if (type == 1 && get_panel() != null) {
            for (int x2 = 0; x2 < this.connections.length; x2++) {
                if (!this.connections[x2].available) {
                    PanelCable c2 = (PanelCable) this.connections[x2].cable;
                    RocketEngine e = c2.get_rengine();
                    if (e != null) {
                        e.set_output(this.panel_output);
                    } else {
                        Battery b2 = c2.get_battery();
                        if (b2 != null) {
                            b2.set_output(this.panel_output);
                        }
                    }
                }
            }
        }
        for (int x3 = 0; x3 < this.connections.length; x3++) {
            if (!this.connections[x3].available) {
                BaseCable bc = this.connections[x3].cable;
                if (bc.cable_type == 2 && (m = ((Cable) bc).get_motor()) != null) {
                    m.set_input(voltage / num_motors, current / num_motors);
                    m.update();
                }
            }
        }
    }

    @Override
    public void tja_translate(float x, float y) {
        translate(x, y);
    }

    public Panel get_panel() {
        Panel p;
        if (get_type() == 1) {
            for (int x = 0; x < this.connections.length; x++) {
                if (!this.connections[x].available && (p = ((PanelCable) this.connections[x].cable).get_panel()) != null) {
                    return p;
                }
            }
        }
        return null;
    }

    public boolean is_connected_to_panel(Panel pending) {
        if (get_type() == 1) {
            for (int x = 0; x < this.connections.length; x++) {
                if (!this.connections[x].available && ((PanelCable) this.connections[x].cable).get_panel() == pending) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void update_properties() {
    }

    public Button get_first_button() {
        Button p;
        if (get_type() == 1) {
            for (int x = 0; x < this.connections.length; x++) {
                if (!this.connections[x].available && (p = ((PanelCable) this.connections[x].cable).get_button()) != null) {
                    return p;
                }
            }
        }
        return null;
    }
}
