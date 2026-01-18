package com.bithack.apparatus;

import com.bithack.apparatus.objects.Battery;
import com.bithack.apparatus.objects.Bucket;
import com.bithack.apparatus.objects.Button;
import com.bithack.apparatus.objects.Cable;
import com.bithack.apparatus.objects.CableEnd;
import com.bithack.apparatus.objects.ChristmasGift;
import com.bithack.apparatus.objects.Damper;
import com.bithack.apparatus.objects.DynamicMotor;
import com.bithack.apparatus.objects.GrabableObject;
import com.bithack.apparatus.objects.Hub;
import com.bithack.apparatus.objects.Knob;
import com.bithack.apparatus.objects.Marble;
import com.bithack.apparatus.objects.MetalBar;
import com.bithack.apparatus.objects.MetalCorner;
import com.bithack.apparatus.objects.MetalWheel;
import com.bithack.apparatus.objects.Mine;
import com.bithack.apparatus.objects.Panel;
import com.bithack.apparatus.objects.PanelCable;
import com.bithack.apparatus.objects.PanelCableEnd;
import com.bithack.apparatus.objects.Plank;
import com.bithack.apparatus.objects.RocketEngine;
import com.bithack.apparatus.objects.Rope;
import com.bithack.apparatus.objects.RopeEnd;
import com.bithack.apparatus.objects.StaticMotor;
import com.bithack.apparatus.objects.Weight;
import com.bithack.apparatus.objects.Wheel;
import java.util.ArrayList;
import java.util.Iterator;

public class ObjectManager {
    protected final ArrayList<Rope> ropes = new ArrayList<>();
    protected final ArrayList<Cable> cables = new ArrayList<>();
    protected final ArrayList<PanelCable> pcables = new ArrayList<>();
    protected final ArrayList<StaticMotor> static_motors = new ArrayList<>();
    protected final ArrayList<RocketEngine> rocketengines = new ArrayList<>();
    protected final ArrayList<Hub> hubs = new ArrayList<>();
    protected final ArrayList<Button> buttons = new ArrayList<>();
    protected final ArrayList<ChristmasGift> christmasgifts = new ArrayList<>();
    public final ArrayList<GrabableObject> all = new ArrayList<>();
    public final Layer layer0 = new Layer();
    public final Layer layer1 = new Layer();
    public final Layer layer2 = new Layer();

    public class Layer {
        protected final ArrayList<Plank> planks = new ArrayList<>();
        protected final ArrayList<Battery> batteries = new ArrayList<>();
        protected final ArrayList<MetalBar> bars = new ArrayList<>();
        public final ArrayList<MetalCorner> metalcorners = new ArrayList<>();
        protected final ArrayList<MetalWheel> metalwheels = new ArrayList<>();
        protected final ArrayList<Marble> marbles = new ArrayList<>();
        protected final ArrayList<Panel> controllers = new ArrayList<>();
        protected final ArrayList<Wheel> wheels = new ArrayList<>();
        protected final ArrayList<Bucket> buckets = new ArrayList<>();
        protected final ArrayList<DynamicMotor> dynamicmotors = new ArrayList<>();
        protected final ArrayList<Weight> weights = new ArrayList<>();
        protected final ArrayList<Knob> knobs = new ArrayList<>();
        protected final ArrayList<Mine> mines = new ArrayList<>();
        protected final ArrayList<Damper> dampers = new ArrayList<>();

        public Layer() {
        }

        public void clear() {
            this.planks.clear();
            this.batteries.clear();
            this.bars.clear();
            this.metalcorners.clear();
            this.knobs.clear();
            this.metalwheels.clear();
            this.marbles.clear();
            this.controllers.clear();
            this.wheels.clear();
            this.buckets.clear();
            this.dynamicmotors.clear();
            this.weights.clear();
            this.mines.clear();
            this.dampers.clear();
        }
    }

    public void clear() {
        this.layer0.clear();
        this.layer1.clear();
        this.layer2.clear();
        this.hubs.clear();
        this.ropes.clear();
        this.cables.clear();
        this.pcables.clear();
        this.buttons.clear();
        this.static_motors.clear();
        this.rocketengines.clear();
        this.christmasgifts.clear();
        this.all.clear();
    }

    public void add_to_layer(int layer, GrabableObject o) {
        Layer l;
        if (layer == 0) {
            l = this.layer0;
        } else {
            l = layer == 1 ? this.layer1 : this.layer2;
        }
        add_to_layer(l, o);
    }

    public void add_to_layer(Layer layer, GrabableObject o) {
        if (o instanceof Plank) {
            layer.planks.add((Plank) o);
        } else if (o instanceof Battery) {
            layer.batteries.add((Battery) o);
        } else if (o instanceof MetalBar) {
            layer.bars.add((MetalBar) o);
        } else if (o instanceof MetalCorner) {
            layer.metalcorners.add((MetalCorner) o);
        } else if (o instanceof Knob) {
            layer.knobs.add((Knob) o);
        } else if (o instanceof MetalWheel) {
            layer.metalwheels.add((MetalWheel) o);
        } else if (o instanceof Marble) {
            layer.marbles.add((Marble) o);
        } else if (o instanceof Panel) {
            layer.controllers.add((Panel) o);
        } else if (o instanceof Wheel) {
            layer.wheels.add((Wheel) o);
        } else if (o instanceof Bucket) {
            layer.buckets.add((Bucket) o);
        } else if (o instanceof DynamicMotor) {
            layer.dynamicmotors.add((DynamicMotor) o);
        } else if (o instanceof Weight) {
            layer.weights.add((Weight) o);
        } else if (o instanceof Mine) {
            layer.mines.add((Mine) o);
        } else if (o instanceof Damper) {
            layer.dampers.add((Damper) o);
        }
        this.all.add(o);
    }

    public void add(GrabableObject o) {
        Layer layer;
        if (o.layer == 0) {
            layer = this.layer0;
        } else {
            layer = o.layer == 1 ? this.layer1 : this.layer2;
        }
        if (o instanceof PanelCable) {
            this.pcables.add((PanelCable) o);
        }
        if (o instanceof Rope) {
            this.ropes.add((Rope) o);
        } else if (o instanceof Cable) {
            this.cables.add((Cable) o);
        } else if (o instanceof StaticMotor) {
            this.static_motors.add((StaticMotor) o);
        } else if (o instanceof RocketEngine) {
            this.rocketengines.add((RocketEngine) o);
        } else if (o instanceof Hub) {
            this.hubs.add((Hub) o);
        } else if (o instanceof Button) {
            this.buttons.add((Button) o);
        } else if (o instanceof ChristmasGift) {
            this.christmasgifts.add((ChristmasGift) o);
        } else {
            add_to_layer(layer, o);
            return;
        }
        this.all.add(o);
    }

    public void remove(GrabableObject o) {
        Layer layer;
        if (o instanceof RopeEnd) {
            this.ropes.remove(((RopeEnd) o).rope);
            this.all.remove(((RopeEnd) o).rope);
            return;
        }
        if (o instanceof CableEnd) {
            this.cables.remove(((CableEnd) o).cable);
            this.all.remove(((CableEnd) o).cable);
            return;
        }
        if (o instanceof PanelCableEnd) {
            this.pcables.remove(((PanelCableEnd) o).cable);
            this.all.remove(((PanelCableEnd) o).cable);
            return;
        }
        if (o instanceof StaticMotor) {
            this.static_motors.remove(o);
            this.all.remove(o);
            return;
        }
        if (o instanceof RocketEngine) {
            this.rocketengines.remove(o);
            this.all.remove(o);
            return;
        }
        if (o instanceof ChristmasGift) {
            this.christmasgifts.remove(o);
            this.all.remove(o);
            return;
        }
        if (o instanceof Hub) {
            this.hubs.remove(o);
            this.all.remove(o);
        } else if (o instanceof Button) {
            this.buttons.remove(o);
            this.all.remove(o);
        } else {
            if (o.layer == 0) {
                layer = this.layer0;
            } else {
                layer = o.layer == 1 ? this.layer1 : this.layer2;
            }
            remove_from_layer(layer, o);
        }
    }

    public void remove_from_layer(Layer layer, GrabableObject o) {
        if (o instanceof Plank) {
            layer.planks.remove(o);
        } else if (o instanceof MetalWheel) {
            layer.metalwheels.remove(o);
        } else if (o instanceof Panel) {
            layer.controllers.remove(o);
        } else if (o instanceof Knob) {
            layer.knobs.remove(o);
        } else if (o instanceof Wheel) {
            layer.wheels.remove(o);
        } else if (o instanceof MetalBar) {
            layer.bars.remove(o);
        } else if (o instanceof Bucket) {
            layer.buckets.remove(o);
        } else if (o instanceof Marble) {
            layer.marbles.remove(o);
        } else if (o instanceof Weight) {
            layer.weights.remove(o);
        } else if (o instanceof DynamicMotor) {
            layer.dynamicmotors.remove(o);
        } else if (o instanceof Battery) {
            layer.batteries.remove(o);
        } else if (o instanceof Mine) {
            layer.mines.remove(o);
        } else if (o instanceof MetalCorner) {
            layer.metalcorners.remove(o);
        } else if (o instanceof Damper) {
            layer.dampers.remove(o);
        }
        this.all.remove(o);
    }

    public GrabableObject find(int id) {
        for (GrabableObject o : this.all) {
            if (o.__unique_id == id) {
                return o;
            }
        }
        return null;
    }

    public void relayer(GrabableObject o, int layer) {
        Layer c_layer;
        Layer n_layer;
        int i = o.layer;
        if (o.layer == 0) {
            c_layer = this.layer0;
        } else {
            c_layer = o.layer == 1 ? this.layer1 : this.layer2;
        }
        if (layer == 0) {
            n_layer = this.layer0;
        } else {
            n_layer = layer == 1 ? this.layer1 : this.layer2;
        }
        if (c_layer != n_layer) {
            if (!(o instanceof RopeEnd)) {
                remove_from_layer(c_layer, o);
                add_to_layer(n_layer, o);
            }
            if (o instanceof Damper) {
                ((Damper) o).g1.layer = layer;
                ((Damper) o).g2.layer = layer;
            }
            o.layer = layer;
        }
    }
}
