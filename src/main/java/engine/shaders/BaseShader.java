package engine.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import engine.utils.FileUtils;
import engine.utils.MathUtils;

public abstract class BaseShader {

	protected int programId;
	private int vertexId;
	private int fragmentId;

	public BaseShader(String vertexFileName, String fragmentFileName) {
		createProgram(vertexFileName, fragmentFileName);
		getUniformLocations();
	}

	protected int getUniformLocation(String name) {
		return GL46.glGetUniformLocation(programId, name);
	}

	public void start() {
		GL46.glUseProgram(programId);
	}

	public void stop() {
		GL46.glUseProgram(0);
	}

	protected void loadFloat(int location, float data) {
		GL46.glUniform1f(location, data);
	}

	protected void loadVector3f(int location, Vector3f data) {
		GL46.glUniform3f(location, data.x(), data.y(), data.z());
	}

	protected void loadVector2f(int location, Vector2f data) {
		GL46.glUniform2f(location, data.x(), data.y());
	}

	protected void loadVector4f(int location, Vector4f data) {
		GL46.glUniform4f(location, data.x(), data.y(), data.z(), data.w());
	}

	protected void loadInteger(int location, int data) {
		GL46.glUniform1i(location, data);
	}

	protected void loadMatrix4f(int location, Matrix4f data) {
		GL46.glUniformMatrix4fv(location, false, MathUtils.matrix4fToFloatBuffer(data));
	}

	protected void loadBoolean(int location, boolean data) {
		if (data) {
			loadFloat(location, 1.0f);
		} else {
			loadFloat(location, 0.0f);
		}
	}

	protected abstract void getUniformLocations();

	public void dispose() {
		GL46.glDetachShader(programId, fragmentId);
		GL46.glDetachShader(programId, vertexId);
		GL46.glUseProgram(0);
		GL46.glDeleteShader(vertexId);
		GL46.glDeleteShader(fragmentId);
		GL46.glDeleteProgram(programId);
	}

	private void createProgram(String vertexFileName, String fragmentFileName) {
		programId = GL46.glCreateProgram();
		String vertexSource = FileUtils.fileToString(Files.getCommonFolderPath(CommonFolders.Shaders) + "/" + vertexFileName);
		String fragmentSource = FileUtils.fileToString(Files.getCommonFolderPath(CommonFolders.Shaders) + "/" + fragmentFileName);;
		vertexId = createShader(GL46.GL_VERTEX_SHADER, vertexSource);
		fragmentId = createShader(GL46.GL_FRAGMENT_SHADER, fragmentSource);
		GL46.glUseProgram(programId);
		GL46.glAttachShader(programId, vertexId);
		GL46.glAttachShader(programId, fragmentId);
		GL46.glLinkProgram(programId);
		if (GL46.glGetProgrami(programId, GL46.GL_LINK_STATUS) == GL46.GL_FALSE) {
			Log.error(getClass(), "Failed to link shader program.");
			Log.error(getClass(), GL46.glGetProgramInfoLog(programId));
		}
		GL46.glValidateProgram(programId);
		if (GL46.glGetProgrami(programId, GL46.GL_VALIDATE_STATUS) == GL46.GL_FALSE) {
			Log.error(getClass(), "Failed to validate shader program.");
			Log.error(getClass(), GL46.glGetProgramInfoLog(programId));
		}
		GL46.glUseProgram(0);
	}

	private int createShader(int type, String source) {
		int shader = GL46.glCreateShader(type);
		GL46.glShaderSource(shader, source);
		GL46.glCompileShader(shader);
		if (GL46.glGetShaderi(shader, GL46.GL_COMPILE_STATUS) == GL46.GL_FALSE) {
			System.err.println("Failed to compile shader: ");
			System.err.println(GL46.glGetShaderInfoLog(shader));
			Log.info(getClass(), "Creating shader.");
			return 0;
		}
		return shader;
	}

}
