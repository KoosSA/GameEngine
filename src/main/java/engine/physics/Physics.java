package engine.physics;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL46;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsSpace.BroadphaseType;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.util.DebugShapeFactory;
import com.jme3.system.NativeLibraryLoader;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import engine.shaders.PhysicsDebugShader;
import engine.utils.Camera;
import engine.utils.MathUtils;

public class Physics {

	private PhysicsSpace world;
	private Vector3f gravity = new Vector3f(0,-10,0);
	private List<PhysicsRayTestResult> rayResultList = new ArrayList<PhysicsRayTestResult>();
	private boolean result = false;
	private boolean canRender = false, debug = false;
	private PhysicsDebugShader shader;
	private int vao, vbo;
	private List<DebugModel> renderBuffer = new ArrayList<DebugModel>();

	public Physics(BroadphaseType broadphaseType) {
		Log.info(getClass(), "Bullet library loding result: " +  (result = NativeLibraryLoader.loadLibbulletjme(true, Files.getFolder("Libs"), "Release", "Sp")));
		if (!result) {
			Log.error(this, "Failed to initialise bullet physics.");
			return;
		}
		world = new PhysicsSpace(broadphaseType);
		world.setGravity(gravity);
		shader = new PhysicsDebugShader();
	}

	public void update(float delta) {
		if (!result) return;
		world.update(delta, 2);
	}

	public void setGravity(float x, float y, float z) {
		this.gravity.set(x, y, z);
		if (!result) return;
		world.setGravity(gravity);
	}

	public PhysicsSpace getWorld() {
		if (!result) return null;
		return world;
	}

	public List<PhysicsRayTestResult> getRayCastResultList(Vector3f start, Vector3f end){
		if (!result) return null;
		rayResultList.clear();
		rayResultList = world.rayTest(start, end);
		return rayResultList;
	}

	public PhysicsRayTestResult getNearestRayCastResult(Vector3f start, Vector3f end) {
		if (!result) return null;
		getRayCastResultList(start, end);
		if (rayResultList.size() <= 0) {
			Log.info(this, "No ray hits were detected - returning null.");
			return null;
		}
		return rayResultList.get(0);
	}

	public void addObjectToPhysicsWorld(Object object) {
		if (!result) return;
		world.add(object);
	}

	public void removeObjectFromPhysicsWorld(Object object) {
		if (!result) return;
		world.remove(object);
	}

	public void addToDebugRenderer(Object object) {
		DebugModel dm = null;
		if (DebugModel.getCurrentList().containsKey(object)) {
			dm = DebugModel.get(object);
		} else {
			FloatBuffer fb = DebugShapeFactory.getDebugTriangles(((PhysicsCollisionObject)object).getCollisionShape() , DebugShapeFactory.highResolution);
			fb.flip();
			dm = new DebugModel(fb, (PhysicsCollisionObject) object);
		}
		renderBuffer.add(dm);
	}

	public void removeFromDebugRenderer(Object object) {
		DebugModel dm = null;
		if (DebugModel.getCurrentList().containsKey(object)) {
			dm = DebugModel.get(object);
			if (renderBuffer.contains(dm)) {
				renderBuffer.remove(dm);
			}
		}
	}

	public void dispose() {
		if (!result) return;
		Log.info(this, "Disposing bullet physics.");
		canRender = false;
		shader.dispose();
		world.destroy();
		result = false;
		disposeDebugMeshes();
		shader.dispose();
	}

	private void disposeDebugMeshes() {
		if (!debug) return;
		GL46.glDeleteBuffers(vbo);
		GL30.glBindVertexArray(0);
		GL46.glDeleteVertexArrays(vao);
	}

	public void enableDebug() {
		vao = GL46.glGenVertexArrays();
		vbo = GL46.glGenBuffers();
		canRender = true;
		debug = true;
	}

	public void disableDebug() {
		canRender = false;
		disposeDebugMeshes();
		debug = false;
	}

	public void debugDraw(Camera cam) {
		if (!canRender) return;
		shader.start();
		shader.loadViewMatrix(cam.getViewMatrix());
		shader.loadProjectionMatrix(cam.getProjectionMatrix());
		GL30.glDisable(GL30.GL_CULL_FACE);
		GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);
		GL30.glLineWidth(4);

		GL30.glBindVertexArray(vao);
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
		GL46.glEnableVertexAttribArray(0);

		//world.getRigidBodyList().forEach(rb -> {
			//FloatBuffer verts = DebugShapeFactory.getDebugTriangles(rb.getCollisionShape(), DebugShapeFactory.lowResolution);
			//verts.flip();
		GL46.glVertexAttribPointer(0, 3, GL46.GL_FLOAT, false, 0, 0);

		for (int i = 0; i < renderBuffer.size(); i++) {
			GL46.glBufferData(GL46.GL_ARRAY_BUFFER, renderBuffer.get(i).getVerts(), GL46.GL_DYNAMIC_DRAW);

			shader.loadTransformationMatrix(MathUtils.getTransformationMatrix(renderBuffer.get(i).getRigidBody().getPhysicsLocation(null), renderBuffer.get(i).getRigidBody().getPhysicsRotation(null)));
			shader.loadColour(getDebugColour(renderBuffer.get(i).getRigidBody()));


			GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, renderBuffer.get(i).getVerts().capacity()/3);

		}

		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
		GL46.glBindVertexArray(0);

		GL30.glBindVertexArray(0);
		shader.stop();
		GL30.glEnable(GL30.GL_CULL_FACE);
		GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_FILL);
	}

	private Vector3f getDebugColour(PhysicsCollisionObject physicsCollisionObject) {
		if (physicsCollisionObject.isActive()) {
			return DebugColours.RB_AWAKE;
		} else {
			if (physicsCollisionObject.isStatic()) return DebugColours.RB_STATIC;
			return DebugColours.RB_ASLEEP;
		}

	}

	public boolean isDebug() {
		return debug;
	}




}
