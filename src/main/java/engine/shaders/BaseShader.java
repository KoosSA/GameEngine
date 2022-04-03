package engine.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
		return GL20.glGetUniformLocation(programId, name);
	}
	
	public void start() {
		GL20.glUseProgram(programId);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	protected void loadFloat(int location, float data) {
		GL20.glUniform1f(location, data);
	}
	
	protected void loadVector3f(int location, Vector3f data) {
		GL20.glUniform3f(location, data.x(), data.y(), data.z());
	}
	
	protected void loadVector2f(int location, Vector2f data) {
		GL20.glUniform2f(location, data.x(), data.y());
	}
	
	protected void loadVector4f(int location, Vector4f data) {
		GL20.glUniform4f(location, data.x(), data.y(), data.z(), data.w());
	}
	
	protected void loadInteger(int location, int data) {
		GL20.glUniform1i(location, data);
	}
	
	protected void loadMatrix4f(int location, Matrix4f data) {
		GL20.glUniformMatrix4fv(location, false, MathUtils.matrix4fToFloatBuffer(data));
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
		GL20.glDetachShader(programId, fragmentId);
		GL20.glDetachShader(programId, vertexId);
		GL20.glUseProgram(0);
		GL20.glDeleteShader(vertexId);
		GL20.glDeleteShader(fragmentId);
		GL20.glDeleteProgram(programId);
	}
	
	private void createProgram(String vertexFileName, String fragmentFileName) {
		programId = GL30.glCreateProgram();
		String vertexSource = FileUtils.fileToString(Files.getCommonFolderPath(CommonFolders.Shaders) + "/" + vertexFileName);
		String fragmentSource = FileUtils.fileToString(Files.getCommonFolderPath(CommonFolders.Shaders) + "/" + fragmentFileName);;
		vertexId = createShader(GL20.GL_VERTEX_SHADER, vertexSource);
		fragmentId = createShader(GL20.GL_FRAGMENT_SHADER, fragmentSource);
		GL20.glUseProgram(programId);
		GL20.glAttachShader(programId, vertexId);
		GL20.glAttachShader(programId, fragmentId);
		GL20.glLinkProgram(programId);
		if (GL20.glGetProgrami(programId, GL30.GL_LINK_STATUS) == GL30.GL_FALSE) {
			Log.error(getClass(), "Failed to link shader program.");
			Log.error(getClass(), GL30.glGetProgramInfoLog(programId));
		}
		GL20.glValidateProgram(programId);
		if (GL20.glGetProgrami(programId, GL30.GL_VALIDATE_STATUS) == GL30.GL_FALSE) {
			Log.error(getClass(), "Failed to validate shader program.");
			Log.error(getClass(), GL30.glGetProgramInfoLog(programId));
		}
		GL20.glUseProgram(0);
	}
	
	private int createShader(int type, String source) {
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, source);
		GL20.glCompileShader(shader);
		if (GL20.glGetShaderi(shader, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
			System.err.println("Failed to compile shader: ");
			System.err.println(GL30.glGetShaderInfoLog(shader));
			Log.info(getClass(), "Creating shader.");
			return 0;
		}
		return shader;
	}

}
