package com.bithack.apparatus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;

/* loaded from: classes.dex */
public class ControlsDialog {
    final ApparatusApplication activity;
    final CheckBox c_reset;
    Dialog dialog;
    final SeekBar s_smoothness;
    final SeekBar s_speed;
    int smoothness;
    int speed;
    final View view;

    public ControlsDialog(ApparatusApplication app) {
        this.activity = app;
        AlertDialog.Builder builder = new AlertDialog.Builder(app);
        this.view = LayoutInflater.from(app).inflate(R.layout.controlsmenu, (ViewGroup) null);
        builder.setTitle(L.get("controlssettings"));
        builder.setView(this.view);
        this.s_smoothness = (SeekBar) this.view.findViewById(R.id.camerasmoothness);
        this.s_speed = (SeekBar) this.view.findViewById(R.id.cameraspeed);
        this.c_reset = (CheckBox) this.view.findViewById(R.id.resetcam);
        this.s_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.bithack.apparatus.ControlsDialog.1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seek) {
                ControlsDialog.this.speed = seek.getProgress();
            }
        });
        this.s_smoothness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.bithack.apparatus.ControlsDialog.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seek) {
                ControlsDialog.this.smoothness = seek.getProgress();
            }
        });
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.ControlsDialog.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                final int camerasmoothness = ControlsDialog.this.smoothness;
                final int cameraspeed = ControlsDialog.this.speed;
                final boolean reset = ControlsDialog.this.c_reset.isChecked();
                Settings.set("camerasmoothness", Integer.toString(camerasmoothness));
                Settings.set("enablespeed", Integer.toString(cameraspeed));
                Settings.set("camerareset", reset ? "yes" : "no");
                ControlsDialog.this.activity.run_on_gl_thread(new Runnable() { // from class: com.bithack.apparatus.ControlsDialog.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Game.camera_smoothness = camerasmoothness;
                        Game.camera_speed = cameraspeed;
                        Game.camera_reset = reset;
                    }
                });
                Settings.save();
            }
        });
        builder.setNegativeButton(L.get("cancel"), new DialogInterface.OnClickListener() { // from class: com.bithack.apparatus.ControlsDialog.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.dialog = builder.create();
    }

    public Dialog get_dialog() {
        return this.dialog;
    }

    public void prepare() {
        this.smoothness = Game.camera_smoothness;
        this.speed = Game.camera_speed;
        this.s_smoothness.setProgress(Game.camera_smoothness);
        this.s_speed.setProgress(Game.camera_speed);
        this.c_reset.setChecked(Game.camera_reset);
    }
}
