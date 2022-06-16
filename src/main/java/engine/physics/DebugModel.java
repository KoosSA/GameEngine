package engine.physics;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import com.jme3.bullet.collision.PhysicsCollisionObject;

public class DebugModel {

	private FloatBuffer verts;
	private PhysicsCollisionObject rigidBody;
	private static Map<Object, DebugModel> currentList = new HashMap<>();


	public DebugModel(FloatBuffer verts, PhysicsCollisionObject rigidBody) {
		this.verts = verts;
		this.rigidBody = rigidBody;
		currentList.putIfAbsent(rigidBody, this);
	}


	public FloatBuffer getVerts() {
		return verts;
	}

	public PhysicsCollisionObject getRigidBody() {
		return rigidBody;
	}


	public static DebugModel get(Object object) {
		return currentList.get(object);
	}

	public static Map<Object, DebugModel> getCurrentList() {
		return currentList;
	}

}
