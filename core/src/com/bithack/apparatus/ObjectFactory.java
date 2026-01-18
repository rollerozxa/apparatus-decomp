package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.objects.BaseObject;
import com.bithack.apparatus.objects.Damper;
import com.bithack.apparatus.objects.GrabableObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ObjectFactory {
    private static BodyDef _anchor_bd;
    private static FixtureDef _anchor_fd;
    private static BodyDef _bd;
    private static FixtureDef _fd;
    private static BodyDef _k_bd;
    private static FixtureDef _k_fd;
    private static PolygonShape _k_shape;
    private static PolygonShape _rect_shape;
    public static Adapter adapter;
    public static boolean _initialized = false;

    public static abstract class Adapter {
        public abstract BaseObject create(World world, int i, int i2);

        public abstract GrabableObject wrap(GrabableObject grabableObject);
    }

    public static void _init() {
        _k_bd = new BodyDef();
        _k_shape = new PolygonShape();
        _k_fd = new FixtureDef();
        _k_fd.shape = _k_shape;
        _k_bd.type = BodyDef.BodyType.KinematicBody;
        _k_shape.setAsBox(2.0f, 2.0f);
        _k_fd.isSensor = true;
        _initialized = true;
        _bd = new BodyDef();
        _anchor_bd = new BodyDef();
        _fd = new FixtureDef();
        _rect_shape = new PolygonShape();
        _fd.shape = _rect_shape;
        _anchor_fd = new FixtureDef();
        _anchor_fd.filter.maskBits = (short) 0;
        _anchor_fd.shape = _rect_shape;
        _anchor_bd.type = BodyDef.BodyType.StaticBody;
        _bd.type = BodyDef.BodyType.DynamicBody;
    }

    public static void set_adapter(Adapter adapter2) {
        adapter = adapter2;
    }

    public static Body create_rectangular_body(World world, float w, float h, float density, float friction, float restitution, int maskbits) {
        Body body = world.createBody(_bd);
        _fd.density = density;
        _fd.restitution = restitution;
        _fd.friction = friction;
        _fd.filter.maskBits = (short) (65535 & maskbits);
        _bd.position.set(0.0f, 0.0f);
        _rect_shape.setAsBox(w, h);
        body.createFixture(_fd);
        return body;
    }

    public static Body create_rectangular_body(World world, float w, float h, float density, float friction, float restitution) {
        Body body = world.createBody(_bd);
        _fd.density = density;
        _fd.restitution = restitution;
        _fd.friction = friction;
        _fd.filter.maskBits = (short) -1;
        _bd.position.set(0.0f, 0.0f);
        _rect_shape.setAsBox(w, h);
        body.createFixture(_fd);
        return body;
    }

    public static Body create_anchor_body(World world) {
        _anchor_bd.position.set(0.0f, 0.0f);
        _rect_shape.setAsBox(0.1f, 0.1f);
        Body body = world.createBody(_anchor_bd);
        body.createFixture(_anchor_fd);
        return body;
    }

    public static Body create_kinematic_body(World world) {
        Body r = world.createBody(_k_bd);
        r.createFixture(_k_fd);
        return r;
    }

    public static BaseObject[] read_from_stream(World world, InputStream s) throws IOException {
        byte[] b = new byte[16];
        int num_added_objects = 0;
        BufferedInputStream testx = new BufferedInputStream(s);
        testx.read(b, 0, 2);
        int num_objects = ((b[0] & 255) << 8) | (b[1] & 255);
        Gdx.app.log("DEBUG", "Number of objects: " + num_objects);
        BaseObject[] objects = new BaseObject[num_objects];
        BinaryIO.debug = true;
        if (num_objects <= 0) {
            return objects;
        }

        while (true) {
            if (testx.read(b, 0, 6) != -1) {
                int group = b[0] & 255;
                int child = ((b[1] & 255) << 8) | (b[2] & 255);
                int unique = ((b[3] & 255) << 8) | (b[4] & 255);
                int num_properties = b[5] & 255;
                Gdx.app.log("DEBUG", "Group: " + group + ", Child: " + child + ", Unique: " + unique + ", Properties: " + num_properties);
                BaseObject o = null;
                if (group != 0) {
                    o = adapter.create(world, group, child);
                }
                float x = BinaryIO.read_float(testx);
                float y = BinaryIO.read_float(testx);
                float angle = BinaryIO.read_float(testx);
                float scale = BinaryIO.read_float(testx);
                int layer = BinaryIO.read_byte(testx);
                Gdx.app.log("DEBUG", "Position: (" + x + ", " + y + "), Angle: " + angle + ", Scale: " + scale + ", Layer: " + layer);
                if (o != null) {
                    o.__unique_id = unique;
                    if (o instanceof GrabableObject) {
                        ((GrabableObject) o).set_active(false);
                    }
                    o.layer = layer;
                    if (o instanceof Damper) {
                        ((Damper) o).g1.layer = layer;
                        ((Damper) o).g2.layer = layer;
                    }
                    o.translate(x, y);
                    o.set_angle(angle);
                    if (o.allow_scale) {
                        o.set_scale(scale);
                    }
                    if (num_properties > 0) {
                        boolean error = false;
                        int p = 0;
                        while (true) {
                            if (p < num_properties) {
                                byte[] bname = new byte[BinaryIO.read_int(testx)];
                                testx.read(bname);
                                String name = new String(bname);
                                Object value = null;
                                switch (testx.read()) {
                                    case 0:
                                        byte[] bval = new byte[BinaryIO.read_int(testx)];
                                        testx.read(bval);
                                        value = new String(bval);
                                        break;
                                    case 1:
                                        value = BinaryIO.read_int(testx);
                                        break;
                                    case 2:
                                        if (testx.read() != 1) {
                                            value = Boolean.FALSE;
                                        } else {
                                            value = Boolean.TRUE;
                                        }
                                        break;
                                    case 3:
                                        value = BinaryIO.read_float(testx);
                                        break;
                                    default:
                                        error = true;
                                        break;
                                }
                                o.set_property(name, value);
                                Gdx.app.log("DEBUG", "Property: " + name + " = " + value);
                                if (error) {
                                    Gdx.app.log("ERROR", "Unknown property value type");
                                } else {
                                    p++;
                                }
                            } else
                                break;
                        }
                    }
                    objects[num_added_objects] = o;
                    num_added_objects++;
                    if (num_added_objects >= num_objects) {
                        break;
                    }
                } else if (num_properties > 0) {
                    Gdx.app.log("ERROR", "Could not create object, but object has " + num_properties + " properties. Bailing out!");
                }
            } else
                break;
        }
        if (num_added_objects == num_objects) {
            return objects;
        }
        BaseObject[] nobjects = new BaseObject[num_added_objects];
        System.arraycopy(objects, 0, nobjects, 0, num_added_objects);
        return nobjects;
    }
}
