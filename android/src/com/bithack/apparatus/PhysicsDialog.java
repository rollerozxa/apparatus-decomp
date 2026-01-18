package com.bithack.apparatus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class PhysicsDialog {
    final ApparatusApplication activity;
    Dialog dialog;
    final RadioButton high;
    final RadioButton low;
    final RadioButton medium;
    int selected = 1;
    final View view;

    public PhysicsDialog(ApparatusApplication app) {
        this.activity = app;
        AlertDialog.Builder builder = new AlertDialog.Builder(app);
        this.view = LayoutInflater.from(app).inflate(R.layout.physicsmenu, (ViewGroup) null);
        builder.setTitle(L.get("simulation_settings"));
        builder.setView(this.view);
        this.low = (RadioButton) this.view.findViewById(R.id.physics_low);
        this.medium = (RadioButton) this.view.findViewById(R.id.physics_medium);
        this.high = (RadioButton) this.view.findViewById(R.id.physics_high);
        this.medium.setChecked(true);
        this.low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.bithack.apparatus.PhysicsDialog.1
            @Override
            public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                if (isChecked) {
                    PhysicsDialog.this.selected = 0;
                }
            }
        });
        this.medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.bithack.apparatus.PhysicsDialog.2
            @Override
            public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                if (isChecked) {
                    PhysicsDialog.this.selected = 1;
                }
            }
        });
        this.high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.bithack.apparatus.PhysicsDialog.3
            @Override
            public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                if (isChecked) {
                    PhysicsDialog.this.selected = 2;
                }
            }
        });
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.PhysicsDialog.4
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int quality = PhysicsDialog.this.selected;
                PhysicsDialog.this.activity.run_on_gl_thread(new Runnable() { // from class: com.bithack.apparatus.PhysicsDialog.4.1
                    @Override
                    public void run() {
                        Game.physics_stability = quality;
                    }
                });
                Settings.set("physics_stability", Integer.toString(quality));
                Settings.save();
            }
        });
        builder.setNegativeButton(L.get("cancel"), new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.PhysicsDialog.5
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.dialog = builder.create();
    }

    public Dialog get_dialog() {
        return this.dialog;
    }

    public void prepare() {
        this.selected = Game.physics_stability;
        switch (this.selected) {
            case 0:
                this.low.setChecked(true);
                break;
            case 1:
                this.medium.setChecked(true);
                break;
            case 2:
                this.high.setChecked(true);
                break;
        }
    }
}
