package engine.models;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;

import engine.physics.Physics;
import engine.utils.MathUtils;

public class ModelInstance {

	private Vector3f position;
	private Quaternionf rotation;
	private Vector3f scale;
	private Model model;
	private PhysicsRigidBody rigidBody;

	public ModelInstance(Model model) {
		position = new Vector3f();
		rotation = new Quaternionf().identity();
		scale = new Vector3f(1, 1, 1);
		this.model = model;
	}

	public ModelInstance(Model model, CollisionShape collShape, float mass, Physics physics) {
		rigidBody = new PhysicsRigidBody(collShape, mass);
		scale = new Vector3f(1, 1, 1);
		this.model = model;
		physics.addObjectToPhysicsWorld(rigidBody);
		rigidBody.setUserObject(this);
	}

	public Vector3f getPosition() {
		return rigidBody != null ? rigidBody.getTransform(null).getTranslation() : position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Quaternionf getRotation() {
		return rigidBody != null ? rigidBody.getTransform(null).getRotation()  : rotation;
	}

	public void setRotation(Quaternionf rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return rigidBody != null ? rigidBody.getTransform(null).getScale() : scale;
	}

	public void setScale(float x, float y, float z) {
		this.scale.set(x,y,z);
		if (rigidBody != null) {
			rigidBody.setPhysicsScale(rigidBody.getScale(null).set(x,y,z));
			rigidBody.activate();
		}
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public PhysicsRigidBody getRigidBody() {
		return rigidBody;
	}

	public Matrix4f getTransformationMatrix() {
		return MathUtils.getTransformationMatrix(getPosition(), getRotation(), getScale());
	}

}
