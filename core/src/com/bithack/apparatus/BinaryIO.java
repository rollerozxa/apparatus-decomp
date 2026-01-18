package com.bithack.apparatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class BinaryIO {
    private static byte[] b = new byte[32];
    public static boolean debug = false;

    public static void write_float(OutputStream s, float f) throws IOException {
        int v = Float.floatToIntBits(f);
        write_int(s, v);
    }

    public static void write_int(OutputStream s, int v) throws IOException {
        b[0] = (byte) ((v & 4278190080L) >> 24);
        b[1] = (byte) ((v & 16711680) >> 16);
        b[2] = (byte) ((v & 65280) >> 8);
        b[3] = (byte) (v & 255);
        s.write(b, 0, 4);
    }

    public static int read_int(InputStream s) throws IOException {
        s.read(b, 0, 4);
        return ((b[0] & 255) << 24) | ((b[1] & 255) << 16) | ((b[2] & 255) << 8) | (b[3] & 255);
    }

    public static float read_float(InputStream s) throws IOException {
        return Float.intBitsToFloat(read_int(s));
    }

    public static byte read_byte(InputStream s) throws IOException {
        s.read(b, 0, 1);
        return b[0];
    }
}
