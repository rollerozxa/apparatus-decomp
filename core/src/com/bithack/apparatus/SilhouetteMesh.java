package com.bithack.apparatus;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.bithack.apparatus.graphics.G;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class SilhouetteMesh {
    private Edge[] edges;
    private Face[] faces;
    private int s_count;
    private float[] s_vertices;
    private SilhouetteMesh transformed;
    private static Matrix4 tmat = new Matrix4();
    private static Matrix4 tmpmat = new Matrix4();
    private static final Vector3 s1 = new Vector3();
    private static final Vector3 s2 = new Vector3();
    private static final Vector3 tmp = new Vector3();
    private static final Ray tmpray = new Ray(new Vector3(0.0f, 0.0f, 0.0f), new Vector3(0.0f, 0.0f, 0.0f));
    private static final Plane yaxis = new Plane(new Vector3(0.0f, 0.0f, 1.0f), new Vector3(0.0f, 0.0f, -0.5f));
    public Vector3 translation = new Vector3();
    private Vector3 scaling = new Vector3();
    private float angle = 0.0f;
    LinkedList<Vertex> slist = new LinkedList<>();
    Vector3 bias1 = new Vector3();
    Vector3 bias2 = new Vector3();

    public static class Edge {
        int f1;
        int f2;
        Vector3 p1;
        Vector3 p2;

        public Edge(Edge e) {
            this.p1 = new Vector3();
            this.p2 = new Vector3();
            this.f1 = e.f1;
            this.f2 = e.f2;
        }

        public Edge(Vector3 p1, Vector3 p2, int f1, int f2) {
            this.p1 = p1;
            this.p2 = p2;
            this.f1 = f1;
            this.f2 = f2;
        }
    }

    public static class Face {
        boolean back;
        Vector3 normal;

        public Face(Face f) {
            this.normal = new Vector3();
        }

        public Face(boolean back) {
            this.back = back;
            this.normal = new Vector3(0.0f, 0.0f, 0.0f);
        }

        public Face(Vector3 normal) {
            this.normal = normal;
        }
    }

    public SilhouetteMesh(Edge[] edges, Face[] faces) {
        this.edges = edges;
        this.faces = faces;
        this.s_vertices = new float[edges.length * 6 * 3 * 2];
    }

    public SilhouetteMesh transform(float tx, float ty, float tz, float sx, float sy, float sz, float angle) {
        if (this.transformed == null) {
            this.transformed = dup();
        }
        tmat.setToRotation(G.vec_rot, angle);
        tmat.mul(tmpmat.setToScaling(sx, sy, sz));
        for (int x = 0; x < this.edges.length; x++) {
            this.transformed.edges[x].p1.set(this.edges[x].p1);
            this.transformed.edges[x].p2.set(this.edges[x].p2);
            this.transformed.edges[x].p1.mul(tmat);
            this.transformed.edges[x].p2.mul(tmat);
        }
        for (int x2 = 0; x2 < this.faces.length; x2++) {
            this.transformed.faces[x2].normal.set(this.faces[x2].normal);
            this.transformed.faces[x2].back = this.faces[x2].back;
            this.transformed.faces[x2].normal.mul(tmat).nor();
        }
        return this.transformed;
    }

    public SilhouetteMesh transform_mark_backfaces(float tx, float ty, float tz, float sx, float sy, float sz, float angle, Vector3 lightdir) {
        if (this.transformed == null) {
            this.transformed = dup();
        }
        tmat.setToRotation(G.vec_rot, angle);
        tmpmat.setToScaling(sx, sy, sz);
        tmp.set(lightdir);
        tmp.mul(-1.0f);
        for (int x = 0; x < this.faces.length; x++) {
            this.transformed.faces[x].normal.set(this.faces[x].normal.tmp2().mul(tmat).nor());
            this.transformed.faces[x].back = tmp.dot(this.transformed.faces[x].normal) > 0.0f;
        }
        for (int x2 = 0; x2 < this.edges.length; x2++) {
            if (this.transformed.faces[this.edges[x2].f1].back != this.transformed.faces[this.edges[x2].f2].back) {
                this.transformed.edges[x2].p1.set(this.edges[x2].p1);
                this.transformed.edges[x2].p2.set(this.edges[x2].p2);
                this.transformed.edges[x2].p1.mul(tmpmat).mul(tmat);
                this.transformed.edges[x2].p2.mul(tmpmat).mul(tmat);
            }
        }
        return this.transformed;
    }

    public void mark_back_faces(Vector3 lightdir) {
        tmp.set(lightdir);
        tmp.mul(-1.0f);
        for (Face f : this.faces) {
            f.back = tmp.dot(f.normal) > 0.0f;
        }
    }

    public Mesh generate_mesh(Mesh mesh, Vector3 lightdir) {
        return generate_mesh(mesh, lightdir, 6);
    }

    public Mesh generate_projected_mesh(Mesh mesh, Vector3 lightdir) {
        if (mesh == null) {
            mesh = new Mesh(true, (this.edges.length * 4) + 4, 0, new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(16, 2, "a_texcoords"));
        }
        s1.set(this.translation.set(0.0f, 0.0f, 0.0f));
        s1.z += 0.25f;
        tmpray.set(s1, lightdir);
        Intersector.intersectRayPlane(tmpray, yaxis, s1);
        this.s_count = 0;
        for (Edge e : this.edges) {
            if (this.faces[e.f1].back != this.faces[e.f2].back) {
                tmp.set(lightdir).mul(-10.0f).add(e.p1);
                tmpray.set(tmp, lightdir);
                Intersector.intersectRayPlane(tmpray, yaxis, tmp);
                this.s_vertices[this.s_count + 0] = tmp.x + s1.x;
                this.s_vertices[this.s_count + 1] = tmp.y + s1.y;
                this.s_vertices[this.s_count + 2] = tmp.x + s1.x;
                this.s_vertices[this.s_count + 3] = tmp.y + s1.y;
                this.s_count += 4;
                tmp.set(lightdir).mul(-10.0f).add(e.p2);
                tmpray.set(tmp, lightdir);
                Intersector.intersectRayPlane(tmpray, yaxis, tmp);
                this.s_vertices[this.s_count + 0] = tmp.x + s1.x;
                this.s_vertices[this.s_count + 1] = tmp.y + s1.y;
                this.s_vertices[this.s_count + 2] = tmp.x + s1.x;
                this.s_vertices[this.s_count + 3] = tmp.y + s1.y;
                this.s_count += 4;
            }
        }
        this.s_vertices[this.s_count + 0] = this.s_vertices[0];
        this.s_vertices[this.s_count + 1] = this.s_vertices[1];
        this.s_vertices[this.s_count + 2] = this.s_vertices[2];
        this.s_vertices[this.s_count + 3] = this.s_vertices[3];
        this.s_count += 4;
        mesh.setVertices(this.s_vertices, 0, this.s_count);
        return mesh;
    }

    private class Vertex {
        double a;
        Vector2 pos = new Vector2();
        Vector2 uv = new Vector2();

        Vertex(float x, float y, float u, float v) {
            this.pos.set(x, y);
            this.uv.set(u, v);
            this.a = Math.atan2(y, x);
        }

        /* JADX WARN: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:6:0x0010  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void insert() {
            boolean found = false;
            for (int x = 0; x < SilhouetteMesh.this.slist.size(); x++) {
                Vertex n = SilhouetteMesh.this.slist.get(x);
                if (this.a < n.a || (this.a > 0.0d && n.a < 0.0d)) {
                    if (this.pos.dst(n.pos) >= 0.2f) {
                        SilhouetteMesh.this.slist.add(x, this);
                        found = true;
                        if (found) {
                            SilhouetteMesh.this.slist.add(SilhouetteMesh.this.slist.size(), this);
                            return;
                        }
                        return;
                    }
                    return;
                }
            }
            if (found) {
            }
        }
    }

    public int generate_projected_soft_mesh(float[] umbra, float[] penumbra, int umbra_offset, int penumbra_offset, int[] size_out, Vector3 lightdir) {
        s1.set(this.translation.set(0.0f, 0.0f, 0.0f));
        s1.z += 0.25f;
        this.slist.clear();
        for (Edge e : this.edges) {
            if (this.faces[e.f1].back != this.faces[e.f2].back) {
                tmp.set(lightdir).mul(-10.0f).add(e.p1);
                tmpray.set(tmp, lightdir);
                Intersector.intersectRayPlane(tmpray, yaxis, tmp);
                Vertex v = new Vertex(s1.x + tmp.x, s1.y + tmp.y, s1.x + tmp.x, s1.y + tmp.y);
                v.insert();
                tmp.set(lightdir).mul(-10.0f).add(e.p2);
                tmpray.set(tmp, lightdir);
                Intersector.intersectRayPlane(tmpray, yaxis, tmp);
                Vertex v2 = new Vertex(s1.x + tmp.x, s1.y + tmp.y, s1.x + tmp.x, s1.y + tmp.y);
                v2.insert();
            }
        }
        Vertex f = this.slist.get(0);
        Vertex v3 = new Vertex(f.pos.x, f.pos.y, f.pos.x, f.pos.y);
        v3.insert();
        this.s_count = 0;
        Iterator<Vertex> it = this.slist.iterator();
        while (it.hasNext()) {
            Vertex vv = it.next();
            umbra[this.s_count + umbra_offset + 0] = vv.pos.x;
            umbra[this.s_count + umbra_offset + 1] = vv.pos.y;
            umbra[this.s_count + umbra_offset + 2] = vv.pos.x;
            umbra[this.s_count + umbra_offset + 3] = vv.pos.y;
            this.s_count += 4;
        }
        size_out[0] = size_out[0] + this.s_count;
        this.s_count = 0;
        Iterator<Vertex> it2 = this.slist.iterator();
        while (it2.hasNext()) {
            Vertex vv2 = it2.next();
            penumbra[this.s_count + penumbra_offset + 0] = vv2.pos.x;
            penumbra[this.s_count + penumbra_offset + 1] = vv2.pos.y;
            penumbra[this.s_count + penumbra_offset + 2] = 1.0f;
            penumbra[this.s_count + penumbra_offset + 3] = 0.0f;
            penumbra[this.s_count + penumbra_offset + 4] = 0.0f;
            penumbra[this.s_count + penumbra_offset + 5] = 1.5f;
            this.s_count += 6;
            tmp.set(vv2.pos.x, vv2.pos.y, 0.0f);
            tmp.nor();
            tmp.mul(0.2f);
            penumbra[this.s_count + penumbra_offset + 0] = vv2.pos.x + (tmp.x * 20.0f);
            penumbra[this.s_count + penumbra_offset + 1] = vv2.pos.y + (tmp.y * 20.0f);
            penumbra[this.s_count + penumbra_offset + 2] = 0.0f;
            penumbra[this.s_count + penumbra_offset + 3] = 1.0f;
            penumbra[this.s_count + penumbra_offset + 4] = 0.0f;
            penumbra[this.s_count + penumbra_offset + 5] = 1.0f;
            this.s_count += 6;
        }
        size_out[1] = size_out[1] + this.s_count;
        return 0;
    }

    public int generate_projected_mesh(float[] array, int offset, Vector3 lightdir) {
        s1.set(this.translation.set(0.0f, 0.0f, 0.0f));
        s1.z += 0.25f;
        tmpray.set(s1, lightdir);
        Intersector.intersectRayPlane(tmpray, yaxis, s1);
        this.slist.clear();
        for (Edge e : this.edges) {
            if (this.faces[e.f1].back != this.faces[e.f2].back) {
                tmp.set(lightdir).mul(-10.0f).add(e.p1);
                tmpray.set(tmp, lightdir);
                Intersector.intersectRayPlane(tmpray, yaxis, tmp);
                Vertex v = new Vertex(s1.x + tmp.x, s1.y + tmp.y, s1.x + tmp.x, s1.y + tmp.y);
                v.insert();
                tmp.set(lightdir).mul(-10.0f).add(e.p2);
                tmpray.set(tmp, lightdir);
                Intersector.intersectRayPlane(tmpray, yaxis, tmp);
                Vertex v2 = new Vertex(s1.x + tmp.x, s1.y + tmp.y, s1.x + tmp.x, s1.y + tmp.y);
                v2.insert();
            }
        }
        Vertex f = this.slist.get(0);
        Vertex v3 = new Vertex(f.pos.x, f.pos.y, f.pos.x, f.pos.y);
        v3.insert();
        this.s_count = 0;
        Iterator<Vertex> it = this.slist.iterator();
        while (it.hasNext()) {
            Vertex vv = it.next();
            array[this.s_count + offset + 0] = vv.pos.x;
            array[this.s_count + offset + 1] = vv.pos.y;
            array[this.s_count + offset + 2] = vv.pos.x;
            array[this.s_count + offset + 3] = vv.pos.y;
            this.s_count += 4;
        }
        return this.s_count;
    }

    public Mesh generate_mesh(Mesh mesh, Vector3 lightdir, int depth) {
        if (mesh == null) {
            mesh = new Mesh(true, this.edges.length * 6, 0, new VertexAttribute(1, 3, ShaderProgram.POSITION_ATTRIBUTE));
        }
        this.s_count = 0;
        for (Edge e : this.edges) {
            if (this.faces[e.f1].back != this.faces[e.f2].back) {
                boolean change_winding = false;
                if (this.bias1.z > 0.0f) {
                    this.bias1.z = 0.0f;
                }
                if (e.p1.z != e.p2.z) {
                    tmp.set(e.p1).nor();
                    double adegrees = Math.atan2(e.p1.y, e.p1.x) * 57.29577951308232d;
                    if (adegrees > 110.0d || adegrees < -75.0d) {
                        change_winding = false;
                    } else {
                        change_winding = true;
                    }
                }
                if (change_winding) {
                    s1.set(lightdir).mul(4.0f).add(e.p1);
                    s2.set(lightdir).mul(4.0f).add(e.p2);
                    this.s_vertices[this.s_count + 0] = s1.x;
                    this.s_vertices[this.s_count + 1] = s1.y;
                    this.s_vertices[this.s_count + 2] = s1.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = e.p1.x + this.bias1.x;
                    this.s_vertices[this.s_count + 1] = e.p1.y + this.bias1.y;
                    this.s_vertices[this.s_count + 2] = e.p1.z + this.bias1.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = e.p2.x + this.bias2.x;
                    this.s_vertices[this.s_count + 1] = e.p2.y + this.bias2.y;
                    this.s_vertices[this.s_count + 2] = e.p2.z + this.bias2.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = s1.x;
                    this.s_vertices[this.s_count + 1] = s1.y;
                    this.s_vertices[this.s_count + 2] = s1.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = e.p2.x + this.bias2.x;
                    this.s_vertices[this.s_count + 1] = e.p2.y + this.bias2.y;
                    this.s_vertices[this.s_count + 2] = e.p2.z + this.bias2.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = s2.x;
                    this.s_vertices[this.s_count + 1] = s2.y;
                    this.s_vertices[this.s_count + 2] = s2.z;
                    this.s_count += 3;
                } else {
                    s1.set(lightdir).mul(4.0f).add(e.p1);
                    s2.set(lightdir).mul(4.0f).add(e.p2);
                    this.s_vertices[this.s_count + 0] = s1.x;
                    this.s_vertices[this.s_count + 1] = s1.y;
                    this.s_vertices[this.s_count + 2] = s1.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = e.p2.x + this.bias2.x;
                    this.s_vertices[this.s_count + 1] = e.p2.y + this.bias2.y;
                    this.s_vertices[this.s_count + 2] = e.p2.z + this.bias2.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = e.p1.x + this.bias1.x;
                    this.s_vertices[this.s_count + 1] = e.p1.y + this.bias1.y;
                    this.s_vertices[this.s_count + 2] = e.p1.z + this.bias1.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = s1.x;
                    this.s_vertices[this.s_count + 1] = s1.y;
                    this.s_vertices[this.s_count + 2] = s1.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = s2.x;
                    this.s_vertices[this.s_count + 1] = s2.y;
                    this.s_vertices[this.s_count + 2] = s2.z;
                    this.s_count += 3;
                    this.s_vertices[this.s_count + 0] = e.p2.x + this.bias2.x;
                    this.s_vertices[this.s_count + 1] = e.p2.y + this.bias2.y;
                    this.s_vertices[this.s_count + 2] = e.p2.z + this.bias2.z;
                    this.s_count += 3;
                }
            }
        }
        mesh.setVertices(this.s_vertices, 0, this.s_count);
        return mesh;
    }

    public int generate_mesh(float[] array, int offset, Vector3 lightdir) {
        return generate_mesh(array, offset, lightdir, 6);
    }

    public int generate_mesh(float[] array, int offset, Vector3 lightdir, int depth) {
        this.s_count = 0;
        for (Edge e : this.edges) {
            if (this.faces[e.f1].back != this.faces[e.f2].back) {
                boolean change_winding = false;
                if (e.p1.z != e.p2.z) {
                    tmp.set(e.p1).nor();
                    double adegrees = Math.atan2(e.p1.y, e.p1.x) * 57.29577951308232d;
                    if (adegrees > 110.0d || adegrees < -75.0d) {
                        change_winding = false;
                    } else {
                        change_winding = true;
                    }
                }
                if (change_winding) {
                    s1.set(lightdir).mul(2.0f).add(e.p1);
                    s2.set(lightdir).mul(2.0f).add(e.p2);
                    s1.add(this.translation);
                    s2.add(this.translation);
                    e.p1.add(this.translation);
                    e.p2.add(this.translation);
                    array[this.s_count + offset + 0] = s1.x;
                    array[this.s_count + offset + 1] = s1.y;
                    array[this.s_count + offset + 2] = s1.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = e.p1.x;
                    array[this.s_count + offset + 1] = e.p1.y;
                    array[this.s_count + offset + 2] = e.p1.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = e.p2.x;
                    array[this.s_count + offset + 1] = e.p2.y;
                    array[this.s_count + offset + 2] = e.p2.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = s1.x;
                    array[this.s_count + offset + 1] = s1.y;
                    array[this.s_count + offset + 2] = s1.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = e.p2.x;
                    array[this.s_count + offset + 1] = e.p2.y;
                    array[this.s_count + offset + 2] = e.p2.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = s2.x;
                    array[this.s_count + offset + 1] = s2.y;
                    array[this.s_count + offset + 2] = s2.z;
                    this.s_count += 3;
                } else {
                    s1.set(lightdir).mul(2.0f).add(e.p1);
                    s2.set(lightdir).mul(2.0f).add(e.p2);
                    s1.add(this.translation);
                    s2.add(this.translation);
                    e.p1.add(this.translation);
                    e.p2.add(this.translation);
                    array[this.s_count + offset + 0] = s1.x;
                    array[this.s_count + offset + 1] = s1.y;
                    array[this.s_count + offset + 2] = s1.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = e.p2.x;
                    array[this.s_count + offset + 1] = e.p2.y;
                    array[this.s_count + offset + 2] = e.p2.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = e.p1.x;
                    array[this.s_count + offset + 1] = e.p1.y;
                    array[this.s_count + offset + 2] = e.p1.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = s1.x;
                    array[this.s_count + offset + 1] = s1.y;
                    array[this.s_count + offset + 2] = s1.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = s2.x;
                    array[this.s_count + offset + 1] = s2.y;
                    array[this.s_count + offset + 2] = s2.z;
                    this.s_count += 3;
                    array[this.s_count + offset + 0] = e.p2.x;
                    array[this.s_count + offset + 1] = e.p2.y;
                    array[this.s_count + offset + 2] = e.p2.z;
                    this.s_count += 3;
                }
            }
        }
        return this.s_count;
    }

    private SilhouetteMesh dup() {
        Edge[] nedges = new Edge[this.edges.length];
        Face[] nfaces = new Face[this.faces.length];
        SilhouetteMesh m = new SilhouetteMesh(nedges, nfaces);
        for (int x = 0; x < nedges.length; x++) {
            nedges[x] = new Edge(this.edges[x]);
        }
        for (int x2 = 0; x2 < nfaces.length; x2++) {
            nfaces[x2] = new Face(this.faces[x2]);
        }
        return m;
    }

    public int get_num_edges() {
        return this.s_count / 6;
    }

    public float[] get_vertices() {
        return this.s_vertices;
    }

    public void translate_modelview() {
        G.gl.glTranslatef(this.translation.x, this.translation.y, this.translation.z);
    }
}
