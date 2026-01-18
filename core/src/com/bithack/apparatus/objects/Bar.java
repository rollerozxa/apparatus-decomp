package com.bithack.apparatus.objects;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bithack.apparatus.SilhouetteMesh;

/* loaded from: classes.dex */
public abstract class Bar extends GrabableObject {
    public static SilhouetteMesh _silhouette;
    public Mesh projected_silhouette_mesh;
    protected Mesh silhouette_mesh;
    protected static boolean _initialized = false;
    private static Vector2 tmp2 = new Vector2();
    protected float last_angle = 4512.0f;
    public Vector3 size = new Vector3();
    public float z = 0.8f;

    @Override // com.bithack.apparatus.objects.GrabableObject
    public abstract void reshape();

    public static void _init() {
        if (!_initialized) {
            _silhouette = new SilhouetteMesh(new SilhouetteMesh.Edge[]{new SilhouetteMesh.Edge(new Vector3(-0.5f, 0.5f, 0.5f), new Vector3(-0.5f, -0.5f, 0.5f), 0, 1), new SilhouetteMesh.Edge(new Vector3(0.5f, 0.5f, 0.5f), new Vector3(-0.5f, 0.5f, 0.5f), 0, 2), new SilhouetteMesh.Edge(new Vector3(0.5f, -0.5f, 0.5f), new Vector3(0.5f, 0.5f, 0.5f), 0, 3), new SilhouetteMesh.Edge(new Vector3(-0.5f, -0.5f, 0.5f), new Vector3(0.5f, -0.5f, 0.5f), 0, 4), new SilhouetteMesh.Edge(new Vector3(-0.5f, 0.5f, -0.5f), new Vector3(-0.5f, 0.5f, 0.5f), 2, 1), new SilhouetteMesh.Edge(new Vector3(-0.5f, -0.5f, -0.5f), new Vector3(-0.5f, -0.5f, 0.5f), 1, 4), new SilhouetteMesh.Edge(new Vector3(-0.5f, 0.5f, -0.5f), new Vector3(-0.5f, -0.5f, -0.5f), 1, 5), new SilhouetteMesh.Edge(new Vector3(0.5f, 0.5f, -0.5f), new Vector3(-0.5f, 0.5f, -0.5f), 2, 5), new SilhouetteMesh.Edge(new Vector3(0.5f, 0.5f, -0.5f), new Vector3(0.5f, 0.5f, 0.5f), 2, 3), new SilhouetteMesh.Edge(new Vector3(0.5f, -0.5f, -0.5f), new Vector3(0.5f, 0.5f, -0.5f), 3, 5), new SilhouetteMesh.Edge(new Vector3(0.5f, -0.5f, -0.5f), new Vector3(0.5f, -0.5f, 0.5f), 4, 3), new SilhouetteMesh.Edge(new Vector3(-0.5f, -0.5f, -0.5f), new Vector3(0.5f, -0.5f, -0.5f), 4, 5)}, new SilhouetteMesh.Face[]{new SilhouetteMesh.Face(new Vector3(0.0f, 0.0f, 1.0f)), new SilhouetteMesh.Face(new Vector3(-1.0f, 0.0f, 0.0f)), new SilhouetteMesh.Face(new Vector3(0.0f, 1.0f, 0.0f)), new SilhouetteMesh.Face(new Vector3(1.0f, 0.0f, 0.0f)), new SilhouetteMesh.Face(new Vector3(0.0f, -1.0f, 0.0f)), new SilhouetteMesh.Face(new Vector3(0.0f, 0.0f, -1.0f))});
            _initialized = true;
        }
    }
}
