package com.bithack.apparatus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.bithack.apparatus.Game;
import com.bithack.apparatus.objects.BaseMotor;
import com.bithack.apparatus.objects.BaseObject;
import com.bithack.apparatus.objects.BaseRope;
import com.bithack.apparatus.objects.BaseRopeEnd;
import com.bithack.apparatus.objects.Damper;
import com.bithack.apparatus.objects.DamperEnd;
import com.bithack.apparatus.objects.DynamicMotor;
import com.bithack.apparatus.objects.GrabableObject;
import com.bithack.apparatus.objects.Hinge;
import com.bithack.apparatus.objects.Rope;
import com.bithack.apparatus.objects.StaticMotor;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class UndoManager {
    public static final int ACTION_ADD = 1;
    public static final int ACTION_HINGE = 3;
    public static final int ACTION_MOVE = 2;
    public static final int ACTION_RELAYER = 5;
    public static final int ACTION_RELEASE_HINGES = 4;
    public static final int ACTION_REMOVE = 0;
    public static final int NUM_STEPS = 21;
    public static final int STATE_IDLE = 0;
    public static final int STATE_STEP = 1;
    Game game;
    private GrabableObject pending_obj;
    private int type;
    private UndoState[] states = new UndoState[21];
    private int state = 0;
    private int step = 0;
    private int step_total = 0;
    private boolean step_modified = false;

    public static class UndoState {
        int id;
        int layer;
        int type;
        public ArrayList<GrabableObject> removed = new ArrayList<>();
        public ArrayList<ObjectWrapper> modified = new ArrayList<>();
        public ArrayList<HingeWrapper> hinges = new ArrayList<>();
        public ArrayList<BaseMotorWrapper> motors = new ArrayList<>();
        public ArrayList<Integer> added = new ArrayList<>();
        public ArrayList<Integer> added_motorhinges = new ArrayList<>();
        public ArrayList<Integer> added_hinges = new ArrayList<>();
        ArrayList<BaseObject.Property> properties = new ArrayList<>();
    }

    public UndoManager(Game game) {
        this.game = game;
        for (int x = 0; x < 21; x++) {
            this.states[x] = new UndoState();
        }
    }

    public void begin_step(int type, GrabableObject obj) {
        clear();
        this.step_modified = false;
        this.type = type;
        this.state = 1;
        Iterator<GrabableObject> it = this.game.om.all.iterator();
        while (it.hasNext()) {
            GrabableObject b = it.next();
            if (b instanceof BaseRope) {
                BaseRope r = (BaseRope) b;
                r.g1.stored_state.angle = r.g1.get_angle();
                r.g1.stored_state.position.set(r.g1.get_position());
                r.g2.stored_state.angle = r.g2.get_angle();
                r.g2.stored_state.position.set(r.g2.get_position());
            } else if (b instanceof Damper) {
                Damper r2 = (Damper) b;
                r2.g1.stored_state.angle = r2.g1.get_angle();
                r2.g1.stored_state.position.set(r2.g1.get_position());
                r2.g2.stored_state.angle = r2.g2.get_angle();
                r2.g2.stored_state.position.set(r2.g2.get_position());
            } else {
                b.stored_state.angle = b.get_angle();
                b.stored_state.position.set(b.get_position());
            }
        }
        if (type == 1) {
            this.states[this.step].added.add(Integer.valueOf(obj.__unique_id));
            this.step_modified = true;
        } else if (type == 5) {
            obj.update_properties();
            this.pending_obj = obj;
            this.states[this.step].id = obj.__unique_id;
            this.states[this.step].layer = this.pending_obj.layer;
            for (BaseObject.Property p : obj.properties) {
                this.states[this.step].properties.add(new BaseObject.Property(p.name, p.type, p.value));
            }
        }
        this.states[this.step].type = type;
    }

    public void commit_step() {
        if (this.state == 1) {
            int num_modified = 0;
            Iterator<GrabableObject> it = this.game.om.all.iterator();
            while (it.hasNext()) {
                GrabableObject b = it.next();
                if (b instanceof BaseRope) {
                    GrabableObject g1 = ((BaseRope) b).g1;
                    GrabableObject g2 = ((BaseRope) b).g2;
                    Vector2 tmp1 = g1.get_position();
                    Vector2 tmp2 = g2.get_position();
                    float a1 = g1.get_angle();
                    float a2 = g2.get_angle();
                    if (g1.stored_state.position.x != tmp1.x || g1.stored_state.position.y != tmp1.y || g1.stored_state.angle != a1 || g2.stored_state.position.x != tmp2.x || g2.stored_state.position.y != tmp2.y || g2.stored_state.angle != a2) {
                        this.states[this.step].modified.add(new ObjectWrapper(b));
                        num_modified++;
                    }
                } else if (b instanceof Damper) {
                    GrabableObject g12 = ((Damper) b).g1;
                    GrabableObject g22 = ((Damper) b).g2;
                    Vector2 tmp12 = g12.get_position();
                    Vector2 tmp22 = g22.get_position();
                    float a12 = g12.get_angle();
                    float a22 = g22.get_angle();
                    if (g12.stored_state.position.x != tmp12.x || g12.stored_state.position.y != tmp12.y || g12.stored_state.angle != a12 || g22.stored_state.position.x != tmp22.x || g22.stored_state.position.y != tmp22.y || g22.stored_state.angle != a22) {
                        this.states[this.step].modified.add(new ObjectWrapper(b));
                        num_modified++;
                    }
                } else {
                    float a = b.get_angle();
                    Vector2 tmp = b.get_position();
                    if (b.stored_state.position.x != tmp.x || b.stored_state.position.y != tmp.y || b.stored_state.angle != a) {
                        this.states[this.step].modified.add(new ObjectWrapper(b));
                        num_modified++;
                    }
                }
            }
            if (this.type == 5) {
                this.pending_obj.update_properties();
                int x = 0;
                while (x < this.states[this.step].properties.size()) {
                    int y = 0;
                    //while (true) {
                        if (y < this.pending_obj.properties.length) {
                            if (!this.pending_obj.properties[y].name.equals(this.states[this.step].properties.get(x).name)) {
                                y++;
                            } else if (this.pending_obj.properties[y].value.equals(this.states[this.step].properties.get(x).value)) {
                                this.states[this.step].properties.remove(x);
                                x--;
                            }
                        }
                    //}
                    x++;
                }
                if (this.pending_obj.layer != this.states[this.step].layer || !this.states[this.step].properties.isEmpty()) {
                    this.step_modified = true;
                } else {
                    this.step_modified = false;
                }
            }
            if (num_modified > 0 || this.step_modified) {
                if (this.step >= 19) {
                    this.step = 20;
                    UndoState u = this.states[0];
                    for (int x2 = 0; x2 < 20; x2++) {
                        this.states[x2] = this.states[x2 + 1];
                    }
                    this.states[20] = u;
                } else {
                    this.step++;
                    this.step_total = this.step;
                }
            }
            this.state = 0;
        }
    }

    public void clear() {
        this.states[this.step].modified.clear();
        this.states[this.step].properties.clear();
        this.states[this.step].added.clear();
        this.states[this.step].hinges.clear();
        this.states[this.step].motors.clear();
        this.states[this.step].removed.clear();
        this.states[this.step].added_motorhinges.clear();
        this.states[this.step].added_hinges.clear();
    }

    public void cancel_step() {
        this.state = 0;
        clear();
    }

    public void redo() {
    }

    public void save_hinge(Hinge h) {
        this.step_modified = true;
        this.states[this.step].hinges.add(new HingeWrapper(h));
    }

    public void save_basemotor(BaseMotor h) {
        this.step_modified = true;
        this.states[this.step].motors.add(new BaseMotorWrapper(h));
    }

    public void undo() {
        GrabableObject obj;
        if (this.step > 0 && this.state != 1) {
            this.step--;
            this.game.contact_handler.reset();
            Game.world.setContactFilter(this.game.falsefilter);
            Iterator<Integer> it = this.states[this.step].added.iterator();
            while (it.hasNext()) {
                GrabableObject o = this.game.om.find(it.next().intValue());
                if (o != null) {
                    this.game.disable_undo = true;
                    if (o == this.game.grabbed_object) {
                        this.game.remove_selected();
                    } else if (o instanceof BaseRope) {
                        this.game.remove_object(((BaseRope) o).g1);
                    } else if (o instanceof Damper) {
                        this.game.remove_object(((Damper) o).g1);
                    } else {
                        this.game.remove_object(o);
                    }
                    this.game.disable_undo = false;
                }
            }
            for (int x = 0; x < 1; x++) {
                Iterator<GrabableObject> it2 = this.game.om.all.iterator();
                while (it2.hasNext()) {
                    it2.next().set_active(false);
                }
                Iterator<ObjectWrapper> it3 = this.states[this.step].modified.iterator();
                while (it3.hasNext()) {
                    ObjectWrapper o2 = it3.next();
                    GrabableObject obj2 = this.game.om.find(o2.id);
                    if (obj2 != null && !this.states[this.step].added.contains(Integer.valueOf(o2.id))) {
                        if (obj2 instanceof BaseRope) {
                            BaseRope r = (BaseRope) obj2;
                            r.g1._saved_angle = o2.angle;
                            r.g1._saved_pos.set(o2.pos);
                            r.g2._saved_angle = o2.angle2;
                            r.g2._saved_pos.set(o2.pos2);
                            ((BaseRope) obj2).create_ropejoint();
                        } else if (obj2 instanceof Damper) {
                            Damper r2 = (Damper) obj2;
                            r2.g1._saved_angle = o2.angle;
                            r2.g1._saved_pos.set(o2.pos);
                            r2.g2._saved_angle = o2.angle2;
                            r2.g2._saved_pos.set(o2.pos2);
                        } else {
                            obj2._saved_angle = o2.angle;
                            obj2._saved_pos.set(o2.pos);
                        }
                        obj2.pause();
                    }
                }
            }
            if (this.states[this.step].type == 5 && (obj = this.game.om.find(this.states[this.step].id)) != null) {
                if (!this.states[this.step].properties.isEmpty()) {
                    Iterator<BaseObject.Property> it4 = this.states[this.step].properties.iterator();
                    while (it4.hasNext()) {
                        BaseObject.Property p = it4.next();
                        obj.set_property(p.name, p.value);
                    }
                    if (obj == this.game.grabbed_object) {
                        this.game.grab_object(obj);
                    }
                }
                if (obj.layer != this.states[this.step].layer) {
                    this.game.om.relayer(obj, this.states[this.step].layer);
                }
            }
            Iterator<GrabableObject> it5 = this.states[this.step].removed.iterator();
            while (it5.hasNext()) {
                GrabableObject o3 = it5.next();
                GrabableObject n = (GrabableObject) ObjectFactory.adapter.create(Game.world, o3.__group_id, o3.__child_id);
                if (o3.state.position != null) {
                    n.translate(o3.state.position.x, o3.state.position.y);
                    n.set_angle(o3.state.angle);
                }
                n.__unique_id = o3.__unique_id;
                n.layer = o3.layer;
                if (n instanceof Damper) {
                    n.translate(0.0f, 0.0f);
                }
                for (BaseObject.Property p2 : o3.properties) {
                    n.set_property(p2.name, p2.value);
                }
                n.pause();
                this.game.om.add(n);
            }
            Iterator<Integer> it6 = this.states[this.step].added_hinges.iterator();
            while (it6.hasNext()) {
                Integer x2 = it6.next();
                Iterator<Hinge> i = this.game.hinges.iterator();
                while (true) {
                    if (i.hasNext()) {
                        Hinge h = i.next();
                        if (h.__unique_id == x2.intValue()) {
                            Game.connectanims.add(new Game.ConnectAnim(1, h.get_position().x, h.get_position().y));
                            h.dispose(Game.world);
                            i.remove();
                            break;
                        }
                    }
                }
            }
            Iterator<Integer> it7 = this.states[this.step].added_motorhinges.iterator();
            while (it7.hasNext()) {
                Integer x3 = it7.next();
                Integer x4 = x3;
                boolean found = false;
                Iterator<StaticMotor> it8 = this.game.om.static_motors.iterator();
                while (true) {
                    if (!it8.hasNext()) {
                        break;
                    }
                    GrabableObject o4 = it8.next();
                    if (o4.__unique_id == x4.intValue()) {
                        ((BaseMotor) o4).detach();
                        Game.connectanims.add(new Game.ConnectAnim(1, o4.get_position().x, o4.get_position().y));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Iterator<DynamicMotor> it9 = this.game.om.layer0.dynamicmotors.iterator();
                    while (true) {
                        if (!it9.hasNext()) {
                            break;
                        }
                        GrabableObject o5 = it9.next();
                        if (o5.__unique_id == x4.intValue()) {
                            Game.connectanims.add(new Game.ConnectAnim(1, o5.get_position().x, o5.get_position().y));
                            ((BaseMotor) o5).detach();
                            break;
                        }
                    }
                    Iterator<DynamicMotor> it10 = this.game.om.layer1.dynamicmotors.iterator();
                    while (true) {
                        if (it10.hasNext()) {
                            GrabableObject o6 = it10.next();
                            if (o6.__unique_id == x4.intValue()) {
                                Game.connectanims.add(new Game.ConnectAnim(1, o6.get_position().x, o6.get_position().y));
                                ((BaseMotor) o6).detach();
                                break;
                            }
                        }
                    }
                }
            }
            Iterator<HingeWrapper> it11 = this.states[this.step].hinges.iterator();
            while (it11.hasNext()) {
                HingeWrapper w = it11.next();
                Hinge h2 = (Hinge) ObjectFactory.adapter.create(Game.world, 3, 1);
                h2.rot_extra = w.rot_extra;
                h2.type = w.type;
                h2.same_layer = w.same_layer;
                h2.__unique_id = w.my_id;
                int n1 = w.id1;
                int n2 = w.id2;
                GrabableObject o1 = null;
                GrabableObject o22 = null;
                Iterator<GrabableObject> it12 = this.game.om.all.iterator();
                while (it12.hasNext()) {
                    GrabableObject o7 = it12.next();
                    if (o7.__unique_id == n1) {
                        o1 = o7;
                    } else if (o7.__unique_id == n2) {
                        o22 = o7;
                    } else if (o7 instanceof Rope) {
                        Rope r3 = (Rope) o7;
                        if (r3.g1.__unique_id == n1) {
                            o1 = r3.g1;
                        }
                        if (r3.g1.__unique_id == n2) {
                            o22 = r3.g1;
                        }
                        if (r3.g2.__unique_id == n1) {
                            o1 = r3.g2;
                        }
                        if (r3.g2.__unique_id == n2) {
                            o22 = r3.g2;
                        }
                    } else if (o7 instanceof Damper) {
                        Damper r4 = (Damper) o7;
                        if (r4.g1.__unique_id == n1) {
                            o1 = r4.g1;
                        }
                        if (r4.g1.__unique_id == n2) {
                            o22 = r4.g1;
                        }
                        if (r4.g2.__unique_id == n1) {
                            o1 = r4.g2;
                        }
                        if (r4.g2.__unique_id == n2) {
                            o22 = r4.g2;
                        }
                    }
                    if (o1 != null && o22 != null) {
                        break;
                    }
                }
                if (o1 != null && o22 != null) {
                    h2.setup(o1, o22, w.anchor);
                    this.game.hinges.add(h2);
                    Game.connectanims.add(new Game.ConnectAnim(0, w.anchor.x, w.anchor.y));
                } else {
                    Gdx.app.log("error", "(UNDO) could not restore hinge");
                    h2.dispose(Game.world);
                }
            }
            Iterator<GrabableObject> it13 = this.game.om.all.iterator();
            while (it13.hasNext()) {
                it13.next().set_active(true);
            }
            Game.world.setContactFilter(this.game.contact_handler);
            this.step++;
            clear();
            this.step--;
        }
    }

    public static class BaseMotorWrapper {
        static final int DYNAMIC = 1;
        static final int STATIC = 0;
        int my_id;
        int o_id;

        public BaseMotorWrapper(BaseMotor h) {
            if (h.attached_object != null) {
                this.o_id = h.attached_object.__unique_id;
            } else {
                this.o_id = -1;
            }
            this.my_id = h.__unique_id;
        }
    }

    public static class HingeWrapper {
        Vector2 anchor;
        int id1;
        int id2;
        int my_id;
        float rot_extra;
        boolean same_layer;
        int type;

        public HingeWrapper(Hinge h) {
            this.same_layer = h.same_layer;
            this.rot_extra = h.rot_extra;
            this.id1 = h.body1_id;
            this.id2 = h.body2_id;
            this.anchor = new Vector2(h.get_position());
            this.type = h.type;
            this.my_id = h.__unique_id;
        }
    }

    public static class ObjectWrapper {
        float angle;
        float angle2;
        int id;
        Vector2 pos;
        Vector2 pos2;

        public ObjectWrapper(GrabableObject o) {
            this.id = o.__unique_id;
            if (o instanceof BaseRope) {
                BaseRope r = (BaseRope) o;
                this.pos = new Vector2(r.g1.stored_state.position);
                this.angle = r.g1.stored_state.angle;
                this.pos2 = new Vector2(r.g2.stored_state.position);
                this.angle2 = r.g2.stored_state.angle;
                return;
            }
            if (o instanceof Damper) {
                Damper r2 = (Damper) o;
                this.pos = new Vector2(r2.g1.stored_state.position);
                this.angle = r2.g1.stored_state.angle;
                this.pos2 = new Vector2(r2.g2.stored_state.position);
                this.angle2 = r2.g2.stored_state.angle;
                return;
            }
            this.pos = new Vector2(o.stored_state.position);
            this.angle = o.stored_state.angle;
        }
    }

    public boolean can_undo() {
        return this.step > 0;
    }

    public boolean can_redo() {
        return this.step_total > this.step;
    }

    public void reset() {
        this.step = 0;
        clear();
    }

    public void add_motor_hinge(BaseMotor h, GrabableObject b) {
        this.step_modified = true;
        this.states[this.step].added_motorhinges.add(Integer.valueOf(h.__unique_id));
    }

    public void add_hinge(Hinge hinge) {
        this.step_modified = true;
        this.states[this.step].added_hinges.add(Integer.valueOf(hinge.__unique_id));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void add_object(GrabableObject grabableObject) {
        this.step_modified = true;
        grabableObject.save_state();
        grabableObject.update_properties();
        if (grabableObject instanceof BaseRopeEnd) {
            ((BaseRopeEnd) grabableObject).get_baserope().update_properties();
            this.states[this.step].removed.add(((BaseRopeEnd) grabableObject).get_baserope());
        } else if (grabableObject instanceof DamperEnd) {
            ((DamperEnd) grabableObject).damper.update_properties();
            this.states[this.step].removed.add(((DamperEnd) grabableObject).damper);
        } else {
            this.states[this.step].removed.add(grabableObject);
        }
    }
}
