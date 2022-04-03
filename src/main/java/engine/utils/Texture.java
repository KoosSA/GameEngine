package engine.utils;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

public class Texture {
	
	private int id = 0;
	private int width = 0;
	private int height = 0;
	private int channels = 0;
	private String name = "";
	
	public Texture(String name) {
		loadTexture(name);
		this.name = name;
		Log.info(getClass(), "Texture loaded: " + name);
	}
	
	private void loadTexture(String name) {
		id = GL15.glGenTextures();
		int[] width = new int[1];
		int[] height = new int[1];
		int[] channels = new int[1];
		String path = Files.getCommonFolderPath(CommonFolders.Textures) + "/" + name;
		ByteBuffer image = STBImage.stbi_load(path, width, height, channels, STBImage.STBI_rgb_alpha);
		if (image == null) {
			try {
				byte[] bytes = getClass().getResourceAsStream(path).readAllBytes();
				ByteBuffer buff = BufferUtils.createByteBuffer(bytes.length);
				buff.put(bytes);
				buff.flip();
				image = STBImage.stbi_load_from_memory(buff, width, height, channels, STBImage.STBI_rgb_alpha);
			} catch (IOException e) {
				e.printStackTrace();
				Log.error(getClass(), "Texture not loaded: " + path);
				return;
			}
		}
		GL15.glBindTexture(GL15.GL_TEXTURE_2D, id);

		GL15.glPixelStorei(GL15.GL_UNPACK_ALIGNMENT, 1);
		GL15.glTexImage2D(GL15.GL_TEXTURE_2D, 0, GL15.GL_RGBA, width[0], height[0], 0, GL15.GL_RGBA,
				GL15.GL_UNSIGNED_BYTE, image);
		GL15.glTexParameteri(GL15.GL_TEXTURE_2D, GL15.GL_TEXTURE_MIN_FILTER, GL15.GL_LINEAR_MIPMAP_LINEAR);
		GL15.glTexParameteri(GL15.GL_TEXTURE_2D, GL15.GL_TEXTURE_MAG_FILTER, GL15.GL_LINEAR_MIPMAP_LINEAR);
		GL30.glGenerateMipmap(GL15.GL_TEXTURE_2D);
		GL15.glBindTexture(GL15.GL_TEXTURE_2D, 0);
		this.width = width[0];
		this.height = height[0];
		this.channels = channels[0];
		STBImage.stbi_image_free(image);
	}

	public void dispose() {
		Log.info(getClass(), "Disposing texture: " + name);
		GL15.glBindTexture(GL15.GL_TEXTURE_2D, 0);
		GL15.glDeleteTextures(id);
	}

	public int getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getChannels() {
		return channels;
	}

}
