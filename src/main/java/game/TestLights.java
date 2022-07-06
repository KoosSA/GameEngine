package game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.jme3.bullet.PhysicsSpace.BroadphaseType;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;

import engine.io.GameInput;
import engine.io.KeyBindings;
import engine.lights.Light;
import engine.logic.Game;
import engine.managers.AssetManager;
import engine.models.Model;
import engine.models.ModelInstance;
import engine.physics.Physics;
import engine.settings.WindowSettings;
import engine.terrain.TerrainManager;
import engine.utils.Loader;

public class TestLights extends Game {

	Physics physics;
	PhysicsRigidBody rb;
	PhysicsRigidBody f;

	public static void main(String[] args) {
		TestLights app = new TestLights();
		WindowSettings.fullscreen = false;
		WindowSettings.vsync = true;
		app.start();
	}

	@Override
	protected void init() {
		physics = new Physics(BroadphaseType.DBVT);

		Loader.loadAllTextures();
		Loader.loadAllModels();

		Model m = Loader.getModel("box.fbx");
		Model m1 = Loader.getModel("sphere.fbx");

		List<ModelInstance> l = new ArrayList<ModelInstance>();
		AssetManager.getModelsToRender().put(m, l);

		List<ModelInstance> l1 = new ArrayList<ModelInstance>();
		AssetManager.getModelsToRender().put(m1, l1);

		new Light(new Vector3f(0,1000,0), new Vector3f(1,1,1));

		TerrainManager.init(physics, 70, 3, 0.3f, 2589, 128, 400);

		physics.enableDebug();
	}

	@Override
	public void handleInput(float delta, GameInput input) {
		super.handleInput(delta, input);
		if (input.isKeyJustPressed(GLFW.GLFW_KEY_ENTER)) {
			ModelInstance mi = new ModelInstance(Loader.getModel("box.fbx"), new BoxCollisionShape(0.5f, 0.5f, 0.5f), 0.0f, physics);
			AssetManager.getModelsToRender().get(mi.getModel()).add(mi);
			//mi.getRigidBody().applyCentralImpulse(new Vector3f(0, 20, 0));
			//mi.getRigidBody().applyTorqueImpulse(new Vector3f(0, 0, 20));
			//System.out.println(assetManager.getModelsToRender().get(mi.getModel()).size() + " == " +  1/delta);
			mi.getRigidBody().setPhysicsLocation(cam.getPointInFrontOfCam(7));
			physics.addToDebugRenderer(mi.getRigidBody());
		}

		if (input.isKeyJustPressed(KeyBindings.INTERACT)) {

		}

	}

	@Override
	protected void update(float delta) {
		physics.update(delta);

	}

	@Override
	protected void render() {

		physics.debugDraw(cam);

	}

	@Override
	protected void dispose() {
		physics.dispose();
	}

}
