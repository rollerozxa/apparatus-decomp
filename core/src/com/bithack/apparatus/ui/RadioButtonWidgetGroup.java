package com.bithack.apparatus.ui;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class RadioButtonWidgetGroup {
    ArrayList<RadioWidget> widgets = new ArrayList<>();

    public int add_button(RadioWidget b) {
        this.widgets.add(b);
        return this.widgets.size() - 1;
    }

    protected void click(int n) {
        Iterator<RadioWidget> it = this.widgets.iterator();
        while (it.hasNext()) {
            RadioWidget b = it.next();
            b.set_checked(false);
        }
        this.widgets.get(n).set_checked(true);
    }
}
