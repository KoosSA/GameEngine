package engine.utils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

public class MathUtils {

	private static FloatBuffer matrix4fBuffer = BufferUtils.createFloatBuffer(16);
	private static Matrix4f transformationMatrix = new Matrix4f();

	public static FloatBuffer matrix4fToFloatBuffer(Matrix4f matrix4f) {
		matrix4f.get(matrix4fBuffer);
		return matrix4fBuffer;
	}

	public static FloatBuffer floatArrayToBuffer(float[] data) {
		FloatBuffer buff = BufferUtils.createFloatBuffer(data.length);
		buff.put(data);
		buff.flip();
		return buff;
	}

	public static IntBuffer intArrayToBuffer(int[] data) {
		IntBuffer buff = BufferUtils.createIntBuffer(data.length);
		buff.put(data);
		buff.flip();
		return buff;
	}

	public static Matrix4f getTransformationMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
		transformationMatrix.identity();
		transformationMatrix.setTranslation(position);
		transformationMatrix.setRotationXYZ(Math.toRadians(rotation.x()),  Math.toRadians(rotation.y()), Math.toRadians(rotation.z()));
		transformationMatrix.scale(scale);
		return transformationMatrix;
	}

	public static Matrix4f getTransformationMatrix(Vector3f position, Quaternionf rotation, Vector3f scale) {
		transformationMatrix.identity();
		transformationMatrix.setTranslation(position);
		transformationMatrix.rotate(rotation);
		transformationMatrix.scale(scale);
		return transformationMatrix;
	}

	public static Matrix4f getTransformationMatrix(Vector3f position, Vector3f scale) {
		transformationMatrix.identity();
		transformationMatrix.setTranslation(position);
		transformationMatrix.setRotationXYZ(Math.toRadians(0),  Math.toRadians(0), Math.toRadians(0));
		transformationMatrix.scale(scale);
		return transformationMatrix;
	}

	public static Matrix4f getTransformationMatrix(Vector3f position) {
		transformationMatrix.identity();
		transformationMatrix.setTranslation(position);
		transformationMatrix.setRotationXYZ(Math.toRadians(0),  Math.toRadians(0), Math.toRadians(0));
		transformationMatrix.scale(1);
		return transformationMatrix;
	}

	public static Matrix4f getTransformationMatrix(Vector3f position, Quaternionf rotation) {
		transformationMatrix.identity();
		transformationMatrix.setTranslation(position);
		transformationMatrix.rotate(rotation);
		transformationMatrix.scale(1);
		return transformationMatrix;
	}

	public static float loop(float value, float min, float max) {
		if (value >= max) value = min + (value - max);
		if (value <= min) value = max - (min - value);
		return value;
	}

	public static float[] ListToArrayFloat(List<Float> list) {
		float[] arr = new float[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	public static int[] ListToArrayInteger(List<Integer> list) {
		int[] arr = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	public static float clamp(float value, int min, int max) {
		if (value > max) value = max;
		if (value < min) value = min;
		return value;
	}



}
