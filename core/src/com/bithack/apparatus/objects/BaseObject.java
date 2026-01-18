package com.bithack.apparatus.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bithack.apparatus.BinaryIO;
import com.bithack.apparatus.Game;
import java.io.IOException;
import java.util.jar.JarOutputStream;

public abstract class BaseObject {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type;
    public int __child_id;
    public int __group_id;
    public int __unique_id;
    public String __custom_id = "<invalid>";
    public int pipeline = 3;
    public int texture_id = 0;
    public boolean allow_scale = false;
    public float scale = 0.0f;
    public int layer = 0;
    public boolean ghost = false;
    public Property[] properties = new Property[0];
    public State state = new State();
    public State stored_state = new State();

    public static class State {
        public float angle;
        public Vector2 position = new Vector2();
    }

    public abstract void dispose(World world);

    public abstract float get_angle();

    public abstract float get_bb_radius();

    public abstract Vector2 get_position();

    public abstract void on_click();

    public abstract void render();

    public abstract void set_angle(float f);

    public abstract void step(float f);

    public abstract void translate(float f, float f2);

    public abstract void update_properties();

    static /* synthetic */ int[] $SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type() {
        int[] iArr = $SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type;
        if (iArr == null) {
            iArr = new int[Property.Type.valuesCustom().length];
            try {
                iArr[Property.Type.BOOLEAN.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Property.Type.FLOAT.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Property.Type.INT.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Property.Type.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type = iArr;
        }
        return iArr;
    }

    public static class Property {
        private static /* synthetic */ int[] $SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type;
        public String name;
        public Type type;
        public Object value;

        public enum Type {
            STRING,
            INT,
            BOOLEAN,
            FLOAT;

            /* renamed from: values, reason: to resolve conflict with enum method */
            public static Type[] valuesCustom() {
                Type[] typeArrValuesCustom = values();
                int length = typeArrValuesCustom.length;
                Type[] typeArr = new Type[length];
                System.arraycopy(typeArrValuesCustom, 0, typeArr, 0, length);
                return typeArr;
            }
        }

        static /* synthetic */ int[] $SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type() {
            int[] iArr = $SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type;
            if (iArr == null) {
                iArr = new int[Type.valuesCustom().length];
                try {
                    iArr[Type.BOOLEAN.ordinal()] = 3;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Type.FLOAT.ordinal()] = 4;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Type.INT.ordinal()] = 2;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Type.STRING.ordinal()] = 1;
                } catch (NoSuchFieldError e4) {
                }
                $SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type = iArr;
            }
            return iArr;
        }

        public Property(String name, Type type, Object value) {
            this.name = name;
            this.type = type;
            switch ($SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type()[type.ordinal()]) {
                case 1:
                    this.value = (String) value;
                    break;
                case 2:
                    this.value = (Integer) value;
                    break;
                case 3:
                    this.value = (Boolean) value;
                    break;
                case 4:
                    this.value = (Float) value;
                    break;
            }
        }
    }

    public State get_state() {
        return this.state;
    }

    public void save_state() {
        this.state.position.set(get_position());
        this.state.angle = get_angle();
    }

    public void set_scale(float scale) {
    }

    public void on_player_touch() {
    }

    public void on_tick(float delta) {
    }

    public void set_game(Game game) {
    }

    public void set_property(String property, Object value) {
        for (Property p : this.properties) {
            if (p.name.equals(property)) {
                p.value = value;
                return;
            }
        }
    }

    public void write_to_stream(JarOutputStream s) throws IOException {
        byte[] b = new byte[12];
        int[] v = new int[4];
        if (this.__group_id == 0) {
            this.__child_id = this.__custom_id.getBytes().length;
        }
        b[0] = (byte) (this.__group_id & 255);
        b[1] = (byte) ((this.__child_id >> 8) & 255);
        b[2] = (byte) (this.__child_id & 255);
        b[3] = (byte) ((this.__unique_id >> 8) & 255);
        b[4] = (byte) (this.__unique_id & 255);
        b[5] = (byte) this.properties.length;
        s.write(b, 0, 6);
        if (this.__group_id == 0) {
            Gdx.app.log("BaseObject", "writing custom object: " + this.__custom_id + " " + getClass().getName());
            s.write(this.__custom_id.getBytes());
        }
        Vector2 pos = get_position();
        v[0] = Float.floatToIntBits(pos.x);
        v[1] = Float.floatToIntBits(pos.y);
        v[2] = Float.floatToIntBits(get_angle());
        b[0] = (byte) ((v[0] & (-16777216)) >> 24);
        b[1] = (byte) ((v[0] & 16711680) >> 16);
        b[2] = (byte) ((v[0] & 65280) >> 8);
        b[3] = (byte) (v[0] & 255);
        b[4] = (byte) ((v[1] & (-16777216)) >> 24);
        b[5] = (byte) ((v[1] & 16711680) >> 16);
        b[6] = (byte) ((v[1] & 65280) >> 8);
        b[7] = (byte) (v[1] & 255);
        b[8] = (byte) ((v[2] & (-16777216)) >> 24);
        b[9] = (byte) ((v[2] & 16711680) >> 16);
        b[10] = (byte) ((v[2] & 65280) >> 8);
        b[11] = (byte) (v[2] & 255);
        s.write(b);
        BinaryIO.write_float(s, this.scale);
        s.write(this.layer);
        for (Property p : this.properties) {
            byte[] bname = p.name.getBytes();
            BinaryIO.write_int(s, bname.length);
            s.write(bname);
            switch ($SWITCH_TABLE$com$bithack$apparatus$objects$BaseObject$Property$Type()[p.type.ordinal()]) {
                case 2:
                    s.write(1);
                    BinaryIO.write_int(s, (Integer) p.value);
                    break;
                case 3:
                    s.write(2);
                    if (((Boolean) p.value).equals(Boolean.TRUE)) {
                        s.write(1);
                    } else {
                        s.write(0);
                    }
                    break;
                case 4:
                    s.write(3);
                    BinaryIO.write_float(s, (Float) p.value);
                    break;
                default:
                    s.write(0);
                    byte[] strb = ((String) p.value).getBytes();
                    BinaryIO.write_int(s, strb.length);
                    s.write(strb);
                    break;
            }
        }
    }

    public Body[] get_body_list() {
        return new Body[0];
    }

    public void rotate(float delta) {
    }
}
