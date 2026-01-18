package com.bithack.apparatus.objects;

public interface BaseCableEnd extends BaseRopeEnd {
    void detach();

    int get_unique_id();

    void update_pos();
}
