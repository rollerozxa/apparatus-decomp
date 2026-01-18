package com.bithack.apparatus;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.bithack.apparatus.objects.BaseMotor;
import com.bithack.apparatus.objects.BaseObject;
import com.bithack.apparatus.objects.Battery;
import com.bithack.apparatus.objects.Button;
import com.bithack.apparatus.objects.CableEnd;
import com.bithack.apparatus.objects.DamperEnd;
import com.bithack.apparatus.objects.DynamicMotor;
import com.bithack.apparatus.objects.GrabableObject;
import com.bithack.apparatus.objects.Hub;
import com.bithack.apparatus.objects.Marble;
import com.bithack.apparatus.objects.MetalBar;
import com.bithack.apparatus.objects.MetalWheel;
import com.bithack.apparatus.objects.Panel;
import com.bithack.apparatus.objects.PanelCableEnd;
import com.bithack.apparatus.objects.Plank;
import com.bithack.apparatus.objects.RocketEngine;
import com.bithack.apparatus.objects.RopeEnd;
import com.bithack.apparatus.objects.StaticMotor;
import com.bithack.apparatus.objects.Wheel;

public class ContactHandler implements ContactListener, ContactFilter {
    Game game;
    protected static FixturePair _tmp = new FixturePair(null, null);
    public static Vector2 tmp2 = new Vector2();
    public static FixturePair[] fixture_pairs = new FixturePair[4096];
    public static int num_fixture_pairs = 0;

    public ContactHandler(Game game) {
        this.game = game;
        for (int x = 0; x < 4096; x++) {
            fixture_pairs[x] = new FixturePair();
        }
    }

    private boolean should_collide_sensor(Fixture sensor, Fixture b) {
        if (b.isSensor()) {
            return should_collide_sensor_sensor(sensor, b);
        }
        Object objA = sensor.getBody().getUserData();
        Object objB = b.getBody().getUserData();
        if (objA instanceof Panel) {
            if (objB instanceof Plank) {
                _tmp.set(sensor, b);
                if (!contains_fixture_pair(_tmp)) {
                    add_fixture_pair(_tmp, true);
                }
            }
        } else if (objA instanceof Hub) {
            if ((objB instanceof Plank) && ((Hub) objA).layer == ((Plank) objB).layer) {
                _tmp.set(sensor, b);
                if (!contains_fixture_pair(_tmp)) {
                    add_fixture_pair(_tmp, true);
                }
            }
        } else if (objA instanceof CableEnd) {
            if (objB instanceof Battery) {
                ((CableEnd) objA).attach_to((GrabableObject) objB);
            } else {
                if (objB instanceof BaseMotor) {
                    ((CableEnd) objA).attach_to((GrabableObject) objB);
                    return false;
                }
                if (objB instanceof Hub) {
                    ((CableEnd) objA).attach_to_hub((Hub) objB);
                }
            }
        } else if (objA instanceof PanelCableEnd) {
            if (objB instanceof Battery) {
                ((PanelCableEnd) objA).attach_to_battery((Battery) objB);
            } else if (objB instanceof Panel) {
                ((PanelCableEnd) objA).attach_to_panel((Panel) objB);
            } else if (objB instanceof Hub) {
                ((PanelCableEnd) objA).attach_to_hub((Hub) objB);
            } else if (objB instanceof RocketEngine) {
                ((PanelCableEnd) objA).attach_to_rengine((RocketEngine) objB);
            } else if (objB instanceof Button) {
                ((PanelCableEnd) objA).attach_to_button((Button) objB);
            }
        } else if (objA instanceof StaticMotor) {
            if (objB instanceof Plank) {
                _tmp.set(sensor, b);
                if (!contains_fixture_pair(_tmp)) {
                    add_fixture_pair(_tmp, false);
                }
            } else if (objB instanceof Wheel) {
                _tmp.set(sensor, b);
                if (!contains_fixture_pair(_tmp)) {
                    add_fixture_pair(_tmp, false);
                }
            }
        } else if (objA instanceof Plank) {
            if (((objB instanceof Plank) || ((objB instanceof Wheel) && !(objB instanceof MetalWheel))) && ((GrabableObject) objB).layer == ((GrabableObject) objA).layer) {
                _tmp.set(sensor, b);
                if (!contains_fixture_pair(_tmp)) {
                    add_fixture_pair(_tmp, true);
                }
            }
        } else if (objA instanceof DamperEnd) {
            if (objB instanceof Plank) {
                if (((Plank) objB).layer == ((DamperEnd) objA).layer) {
                    _tmp.set(sensor, b);
                    if (!contains_fixture_pair(_tmp)) {
                        add_fixture_pair(_tmp, true);
                    }
                }
            } else if ((objB instanceof DynamicMotor) && ((DynamicMotor) objB).layer == ((DamperEnd) objA).layer) {
                _tmp.set(sensor, b);
                if (!contains_fixture_pair(_tmp)) {
                    add_fixture_pair(_tmp, true);
                }
            }
        } else if ((objA instanceof DynamicMotor) && (objB instanceof Plank) && ((Plank) objB).layer == ((DynamicMotor) objA).layer) {
            _tmp.set(sensor, b);
            if (!contains_fixture_pair(_tmp)) {
                add_fixture_pair(_tmp, true);
            }
        }
        if ((objA instanceof RocketEngine) && (objB instanceof Plank) && ((Plank) objB).layer == ((RocketEngine) objA).layer) {
            _tmp.set(sensor, b);
            if (!contains_fixture_pair(_tmp)) {
                add_fixture_pair(_tmp, true);
            }
        }
        return false;
    }

    private boolean should_collide_sensor_sensor(Fixture sensor1, Fixture sensor2) {
        Object objA = sensor1.getBody().getUserData();
        Object objB = sensor2.getBody().getUserData();
        if ((objA instanceof MetalBar) && (objB instanceof MetalBar)) {
            Vector2 s1 = (Vector2) sensor1.getUserData();
            Vector2 s2 = (Vector2) sensor2.getUserData();
            if (((s1.x != s2.x && s1.y == s2.y) || (s1.x == s2.x && s1.y != s2.y)) && ((BaseObject) objA).layer == ((BaseObject) objB).layer) {
                if (((MetalBar) objA).grabbed) {
                    ((MetalBar) objA).create_corner((MetalBar) objB, s1, s2);
                } else if (((MetalBar) objB).grabbed) {
                    ((MetalBar) objB).create_corner((MetalBar) objA, s2, s1);
                }
            }
        } else if (objA instanceof CableEnd) {
            if (objB instanceof BaseMotor) {
                ((CableEnd) objA).attach_to((GrabableObject) objB);
                return false;
            }
        } else if ((objB instanceof CableEnd) && (objA instanceof BaseMotor)) {
            ((CableEnd) objB).attach_to((GrabableObject) objA);
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldCollide(Fixture a, Fixture b) {
        if (a == null || b == null) {
            return false;
        }
        Body A = a.getBody();
        Body B = b.getBody();
        if (A.getUserData() != null && B.getUserData() != null) {
            if (a.isSensor()) {
                return should_collide_sensor(a, b);
            }
            if (b.isSensor()) {
                return should_collide_sensor(b, a);
            }
            Object objA = A.getUserData();
            Object objB = B.getUserData();
            if (((BaseObject) A.getUserData()).layer != ((BaseObject) B.getUserData()).layer) {
                if (Game.mode != Game.MODE_PLAY && (A.getUserData() instanceof GrabableObject) && (B.getUserData() instanceof GrabableObject)) {
                    _tmp.set(a, b);
                    if (!contains_fixture_pair(_tmp)) {
                        add_fixture_pair(_tmp, false);
                    }
                }
                return false;
            }
            if (((BaseObject) objA).ghost || ((BaseObject) objB).ghost) {
                return false;
            }
        }
        return true;
    }

    public void reset() {
        num_fixture_pairs = 0;
    }

    public void clean() {
        int x = 0;
        while (x < num_fixture_pairs) {
            if (fixture_pairs[x].invalid) {
                if (x != num_fixture_pairs - 1) {
                    FixturePair tmp = fixture_pairs[x];
                    fixture_pairs[x] = fixture_pairs[num_fixture_pairs - 1];
                    fixture_pairs[num_fixture_pairs - 1] = tmp;
                }
                num_fixture_pairs--;
            } else {
                fixture_pairs[x].invalid = true;
                fixture_pairs[x].processed = false;
                x++;
            }
        }
    }

    protected static FixturePair contains_similar_fixture_pair(FixturePair fp, FixturePair exclude) {
        for (int x = 0; x < num_fixture_pairs; x++) {
            if (fixture_pairs[x] != exclude && fixture_pairs[x].bodies_equal(fp)) {
                return fixture_pairs[x];
            }
        }
        return null;
    }

    protected static boolean contains_fixture_pair(FixturePair fp) {
        for (int x = 0; x < num_fixture_pairs; x++) {
            if (fixture_pairs[x].equals(fp)) {
                return true;
            }
        }
        return false;
    }

    protected static void add_fixture_pair(FixturePair fp, boolean same_layer) {
        if (num_fixture_pairs < fixture_pairs.length) {
            fixture_pairs[num_fixture_pairs].set(fp);
            if (same_layer) {
                fixture_pairs[num_fixture_pairs].same_layer = true;
            } else {
                fixture_pairs[num_fixture_pairs].same_layer = false;
            }
            num_fixture_pairs++;
        }
    }

    private void handle_hinge_plank_contact(StaticMotor hinge, Plank plank) {
        hinge.attach(plank);
    }

    @Override
    public void beginContact(Contact c) {
        if (Game.mode == Game.MODE_PLAY) {
            if ((c.getFixtureA().getUserData() instanceof Battery) && c.getFixtureB().getBody().getLinearVelocity().len2() > 10.0f) {
                Battery b = (Battery) c.getFixtureA().getUserData();
                b.toggle_onoff();
            }
            if ((c.getFixtureB().getUserData() instanceof Battery) && c.getFixtureA().getBody().getLinearVelocity().len2() > 10.0f) {
                Battery b2 = (Battery) c.getFixtureB().getUserData();
                b2.toggle_onoff();
            }
            Fixture a = c.getFixtureA();
            Fixture b3 = c.getFixtureB();
            Object objA = a.getBody().getUserData();
            Object objB = b3.getBody().getUserData();
            if (objA instanceof Marble) {
                SoundManager.handle_marble_hit((Marble) objA, (GrabableObject) objB);
                return;
            }
            if (objA instanceof MetalWheel) {
                SoundManager.handle_metal_hit((MetalWheel) objA, (GrabableObject) objB);
                return;
            }
            if ((objA instanceof Plank) || (objA instanceof Wheel)) {
                SoundManager.handle_wood_hit((GrabableObject) objA, (GrabableObject) objB);
                return;
            }
            if (objB instanceof Marble) {
                SoundManager.handle_marble_hit((Marble) objB, (GrabableObject) objA);
                return;
            }
            if (objB instanceof MetalWheel) {
                SoundManager.handle_metal_hit((MetalWheel) objB, (GrabableObject) objA);
            } else if ((objB instanceof Plank) || (objB instanceof Wheel)) {
                SoundManager.handle_wood_hit((GrabableObject) objB, (GrabableObject) objA);
            }
        }
    }

    @Override
    public void endContact(Contact c) {
        Fixture a = c.getFixtureA();
        Fixture b = c.getFixtureB();
        try {
            if (a.getBody().getUserData() instanceof Marble) {
                SoundManager.stop_marble_roll((Marble) a.getBody().getUserData());
            }
            if (b.getBody().getUserData() instanceof Marble) {
                SoundManager.stop_marble_roll((Marble) b.getBody().getUserData());
            }
        } catch (Exception e) {
        }
    }

    public static class FixturePair implements QueryCallback {
        public Fixture a;
        public Fixture b;
        private int query_count;
        private static Vector2 _tmp = new Vector2();
        private static Vector2 _p1 = new Vector2();
        private static Vector2 _p2 = new Vector2();
        private static Vector2 _p3 = new Vector2();
        private static Vector2 _p4 = new Vector2();
        static Vector2 tmp = new Vector2();
        static Vector2 iresult = new Vector2();
        Vector2 point = new Vector2();
        boolean invalid = true;
        private boolean processed = false;
        boolean same_layer = false;

        public FixturePair() {
            set(null, null);
        }

        public FixturePair(Fixture fa, Fixture fb) {
            set(fa, fb);
        }

        public void set(FixturePair fp) {
            set(fp.a, fp.b);
        }

        public void set(Fixture fa, Fixture fb) {
            this.a = fa;
            this.b = fb;
            this.invalid = true;
            this.processed = false;
            this.same_layer = false;
        }

        public Vector2 get_intersection_point() {
            if (this.processed) {
                if (this.invalid) {
                    return null;
                }
                return this.point;
            }
            this.processed = true;
            GrabableObject ga = (GrabableObject) this.a.getBody().getUserData();
            GrabableObject gb = (GrabableObject) this.b.getBody().getUserData();
            Vector2 pt = null;
            if (this.same_layer) {
                if (ga instanceof Plank) {
                    if (gb instanceof Wheel) {
                        pt = ((Plank) ga).check_sensor_intersection(this.a, (Wheel) gb);
                    } else {
                        Plank p = (Plank) ga;
                        pt = p.check_sensor_intersection(this.a, (Plank) gb);
                        if (pt != null) {
                            ContactHandler._tmp.set(p.f, ((Plank) gb).f);
                            FixturePair tmp2 = ContactHandler.contains_similar_fixture_pair(ContactHandler._tmp, this);
                            if (tmp2 != null && tmp2.processed && !tmp2.invalid) {
                                pt = null;
                            }
                        }
                    }
                } else if (ga instanceof DynamicMotor) {
                    ContactHandler._tmp.set(((DynamicMotor) ga).f, ((Plank) gb).f);
                    pt = (!ContactHandler.contains_fixture_pair(ContactHandler._tmp) || ContactHandler._tmp.get_intersection_point() == null) ? intersect_dynamicmotor((DynamicMotor) ga) : null;
                } else if (ga instanceof RocketEngine) {
                    pt = intersect_rengine_plank((RocketEngine) ga, (Plank) gb);
                } else if (ga instanceof DamperEnd) {
                    pt = intersect_damper_lol((DamperEnd) ga);
                } else if (ga instanceof Panel) {
                    pt = intersect_panel_plank((Panel) ga, (Plank) gb);
                } else if (ga instanceof Hub) {
                    pt = intersect_hub_plank((Hub) ga, (Plank) gb);
                }
            } else if (ga instanceof BaseMotor) {
                pt = gb.layer == 2 ? null : intersect_motor((BaseMotor) ga);
            } else if (gb instanceof BaseMotor) {
                pt = ga.layer == 2 ? null : intersect_motor((BaseMotor) gb);
            } else if (ga instanceof Plank) {
                if (gb instanceof Plank) {
                    pt = intersect_plank_plank((Plank) ga, (Plank) gb);
                } else if (gb instanceof Wheel) {
                    pt = intersect_plank_wheel((Plank) ga, (Wheel) gb);
                } else if (gb instanceof RopeEnd) {
                    pt = intersect_plank_rope((Plank) ga, (RopeEnd) gb);
                } else if (gb instanceof DamperEnd) {
                    pt = intersect_damper_lol((DamperEnd) gb);
                }
            } else if (ga instanceof Wheel) {
                if (gb instanceof Plank) {
                    pt = intersect_plank_wheel((Plank) gb, (Wheel) ga);
                } else if (gb instanceof RopeEnd) {
                    pt = intersect_wheel_rope((Wheel) ga, (RopeEnd) gb);
                }
            } else if (ga instanceof RopeEnd) {
                if (gb instanceof Plank) {
                    pt = intersect_plank_rope((Plank) gb, (RopeEnd) ga);
                } else if (gb instanceof Wheel) {
                    pt = intersect_wheel_rope((Wheel) gb, (RopeEnd) ga);
                }
            } else if ((ga instanceof DamperEnd) && ((gb instanceof Plank) || (gb instanceof DynamicMotor))) {
                pt = intersect_damper_lol((DamperEnd) ga);
            }
            if (pt == null) {
                return null;
            }
            this.invalid = false;
            this.point.set(pt);
            return this.point;
        }

        private Vector2 intersect_damper_lol(DamperEnd ga) {
            return query_intersection_point(ga.body.getWorldPoint(ContactHandler.tmp2.set(0.0f, -1.25f)));
        }

        private Vector2 intersect_plank_rope(Plank ga, RopeEnd gb) {
            return query_intersection_point(gb.get_position());
        }

        private Vector2 intersect_wheel_rope(Wheel ga, RopeEnd gb) {
            return query_intersection_point(gb.get_position());
        }

        private Vector2 intersect_rengine_plank(RocketEngine ga, Plank gb) {
            ContactHandler.tmp2.set(0.0f, 1.25f);
            return query_intersection_point(ga.body.getWorldPoint(ContactHandler.tmp2));
        }

        private Vector2 intersect_hub_plank(Hub ga, Plank gb) {
            Vector2 t = (Vector2) this.a.getUserData();
            float angle = (float) Math.atan2(t.y, t.x);
            float bangle = ga.body.getAngle();
            tmp.set(ga.get_position()).add((float) Math.cos(bangle + angle), (float) Math.sin(bangle + angle));
            Vector2 r = query_intersection_point(tmp);
            if (r != null) {
                return r;
            }
            tmp.set(t);
            if (t.x != 0.0f) {
                tmp.y += 0.5f;
            } else if (t.y != 0.0f) {
                tmp.x += 0.5f;
            }
            float angle2 = (float) Math.atan2(tmp.y, tmp.x);
            ContactHandler.tmp2.set(ga.get_position()).add((float) Math.cos(bangle + angle2), (float) Math.sin(bangle + angle2));
            Vector2 r2 = query_intersection_point(ContactHandler.tmp2);
            if (r2 != null) {
                return r2;
            }
            tmp.set(t);
            if (t.x != 0.0f) {
                tmp.y -= 0.5f;
            } else if (t.y != 0.0f) {
                tmp.x -= 0.5f;
            }
            float angle3 = (float) Math.atan2(tmp.y, tmp.x);
            ContactHandler.tmp2.set(ga.get_position()).add((float) Math.cos(bangle + angle3), (float) Math.sin(bangle + angle3));
            return query_intersection_point(ContactHandler.tmp2);
        }

        private Vector2 intersect_panel_plank(Panel ga, Plank gb) {
            Vector2 t = (Vector2) this.a.getUserData();
            float angle = (float) Math.atan2(t.y, t.x);
            float bangle = ga.body.getAngle();
            tmp.set(ga.get_position()).add((float) Math.cos(bangle + angle), (float) Math.sin(bangle + angle));
            Vector2 r = query_intersection_point(tmp);
            if (r != null) {
                return r;
            }
            tmp.set(t);
            if (t.x != 0.0f) {
                tmp.y += 0.5f;
            } else if (t.y != 0.0f) {
                tmp.x += 0.5f;
            }
            float angle2 = (float) Math.atan2(tmp.y, tmp.x);
            ContactHandler.tmp2.set(ga.get_position()).add((float) Math.cos(bangle + angle2), (float) Math.sin(bangle + angle2));
            Vector2 r2 = query_intersection_point(ContactHandler.tmp2);
            if (r2 != null) {
                return r2;
            }
            tmp.set(t);
            if (t.x != 0.0f) {
                tmp.y -= 0.5f;
            } else if (t.y != 0.0f) {
                tmp.x -= 0.5f;
            }
            float angle3 = (float) Math.atan2(tmp.y, tmp.x);
            ContactHandler.tmp2.set(ga.get_position()).add((float) Math.cos(bangle + angle3), (float) Math.sin(bangle + angle3));
            return query_intersection_point(ContactHandler.tmp2);
        }

        private Vector2 intersect_dynamicmotor(DynamicMotor ga) {
            Vector2 t = (Vector2) this.a.getUserData();
            float angle = (float) Math.atan2(t.y, t.x);
            float bangle = ga.body.getAngle();
            tmp.set(ga.get_position()).add((float) Math.cos(bangle + angle), (float) Math.sin(bangle + angle));
            Vector2 r = query_intersection_point(tmp);
            if (r != null) {
                return r;
            }
            tmp.set(t);
            if (t.x != 0.0f) {
                tmp.y += 0.5f;
            } else if (t.y != 0.0f) {
                tmp.x += 0.5f;
            }
            float angle2 = (float) Math.atan2(tmp.y, tmp.x);
            ContactHandler.tmp2.set(ga.get_position()).add((float) Math.cos(bangle + angle2), (float) Math.sin(bangle + angle2));
            Vector2 r2 = query_intersection_point(ContactHandler.tmp2);
            if (r2 != null) {
                return r2;
            }
            tmp.set(t);
            if (t.x != 0.0f) {
                tmp.y -= 0.5f;
            } else if (t.y != 0.0f) {
                tmp.x -= 0.5f;
            }
            float angle3 = (float) Math.atan2(tmp.y, tmp.x);
            ContactHandler.tmp2.set(ga.get_position()).add((float) Math.cos(bangle + angle3), (float) Math.sin(bangle + angle3));
            return query_intersection_point(ContactHandler.tmp2);
        }

        private Vector2 intersect_motor(BaseMotor h) {
            Vector2 pt = query_intersection_point(h.body.getPosition());
            return pt;
        }

        public static Vector2 get_line_segment_circle_intersection_point(Vector2 p1, Vector2 p2, Vector2 sc, double r, Vector2 plankpos) {
            if (p1.x == p2.x) {
                p1.x += 1.0E-5f;
            }
            if (p1.y == p2.y) {
                p1.y += 1.0E-5f;
            }
            float dx = p2.x - p1.x;
            float dy = p2.y - p1.y;
            float dr = (float) Math.sqrt((dx * dx) + (dy * dy));
            float D = (p1.x * p2.y) - (p2.x * p1.y);
            float discriminant = (float) ((((r * r) * dr) * dr) - (D * D));
            if (discriminant < 0.0f) {
                return null;
            }
            double v = (Math.signum(dy) * dx) * Math.sqrt(discriminant);
            double v1 = Math.abs(dy) * Math.sqrt(discriminant);
            float x1 = ((float) ((D * dy) + v)) / (dr * dr);
            float y1 = ((float) (((-D) * dx) + v1)) / (dr * dr);
            float x2 = ((float) ((D * dy) - v)) / (dr * dr);
            float y2 = ((float) (((-D) * dx) - v1)) / (dr * dr);
            tmp.set(x1, y1);
            if (tmp.dst(x2, y2) < 1.5f) {
                return iresult.set((0.9f * x2) + sc.x, (0.9f * y2) + sc.y).add((0.9f * x1) + sc.x, (0.9f * y1) + sc.y).mul(0.5f);
            }
            if (plankpos.dst(sc.x + x1, sc.y + y1) > plankpos.dst(sc.x + x2, sc.y + y2)) {
                return iresult.set((0.9f * x2) + sc.x, (0.9f * y2) + sc.y);
            }
            return iresult.set((0.9f * x1) + sc.x, (0.9f * y1) + sc.y);
        }

        private Vector2 intersect_plank_wheel(Plank a, Wheel b) {
            float aa = a.body.getAngle();
            _tmp.set((float) Math.cos(aa), (float) Math.sin(aa));
            _tmp.mul(4.0f);
            _p1.set(_tmp);
            _p2.set(_p1).mul(-1.0f);
            _p1.add(a.get_position());
            _p2.add(a.get_position());
            float dist = Intersector.intersectSegmentCircleDisplace(_p1, _p2, b.get_position(), 0.8f * b.size, _tmp);
            if (dist != Float.POSITIVE_INFINITY && query_intersection_point(b.get_position()) != null) {
                return b.get_position();
            }
            if (b.size > 1.9f) {
                _p1.sub(b.get_position());
                _p2.sub(b.get_position());
                Vector2 pt = get_line_segment_circle_intersection_point(_p1, _p2, b.get_position(), b.size, a.body.getPosition());
                return query_intersection_point(pt);
            }
            return null;
        }

        private Vector2 intersect_plank_plank(Plank a, Plank b) {
            float aa = a.body.getAngle();
            float ab = b.body.getAngle();
            float da = (float) (Math.abs((aa % 3.141592653589793d) - (ab % 3.141592653589793d)) % 3.141592653589793d);
            if (da < 0.05f || da > 3.091592652844735d) {
                _tmp.set(a.body.getPosition()).add(b.body.getPosition()).mul(0.5f);
                Vector2 r = query_intersection_point(_tmp);
                if (r == null) {
                    if (a.layer < b.layer) {
                        a = b;
                    }
                    _tmp.set(-((a.size.x / 2.0f) - 0.25f), 0.0f);
                    Vector2 r2 = query_intersection_point(a.body.getWorldPoint(_tmp));
                    if (r2 == null) {
                        _tmp.set((a.size.x / 2.0f) - 0.25f, 0.0f);
                        return query_intersection_point(a.body.getWorldPoint(_tmp));
                    }
                    return r2;
                }
                return r;
            }
            _p1.set(a.body.getPosition());
            _p2.set(_p1).add((float) Math.cos(aa), (float) Math.sin(aa));
            _p3.set(b.body.getPosition());
            _p4.set(_p3).add((float) Math.cos(ab), (float) Math.sin(ab));
            if (Intersector.intersectLines(_p1, _p2, _p3, _p4, _tmp)) {
                return query_intersection_point(_tmp);
            }
            return null;
        }

        private Vector2 query_intersection_point(Vector2 p) {
            if (p == null) {
                return null;
            }
            if (this.a.testPoint(p) && this.b.testPoint(p)) {
                return p;
            }
            return null;
        }

        public void setup() {
        }

        public boolean equals(Object _p) {
            if (!(_p instanceof FixturePair)) {
                return false;
            }
            FixturePair p = (FixturePair) _p;
            return (p.a == this.a && p.b == this.b) || (p.a == this.b && p.b == this.a);
        }

        public boolean bodies_equal(Object _p) {
            if (!(_p instanceof FixturePair)) {
                return false;
            }
            FixturePair p = (FixturePair) _p;
            return (p.a.getBody() == this.a.getBody() && p.b.getBody() == this.b.getBody()) || (p.a.getBody() == this.b.getBody() && p.b.getBody() == this.a.getBody());
        }

        @Override
        public boolean reportFixture(Fixture f) {
            if (f == this.a || f == this.b) {
                this.query_count++;
                return true;
            }
            return true;
        }

        public void apply_joint(World world) {
            RevoluteJointDef rjd = new RevoluteJointDef();
            rjd.collideConnected = false;
            rjd.initialize(this.a.getBody(), this.b.getBody(), this.point);
            world.createJoint(rjd);
        }
    }

    @Override
    public void postSolve(Contact arg0, ContactImpulse arg1) {
    }

    @Override
    public void preSolve(Contact arg0, Manifold arg1) {
    }
}
