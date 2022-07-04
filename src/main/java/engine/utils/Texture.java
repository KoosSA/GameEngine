package engine.utils;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

public class Texture {

	protected int id = 0;
	protected int width = 0;
	protected int height = 0;
	private int channels = 0;
	private String name = "";

	public Texture(String name) {
		loadTexture(name);
		this.name = name;
		Log.info(getClass(), "Texture loaded: " + name);
	}

	protected void loadTexture(String name) {
		id = GL46.glGenTextures();
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
			} catch (Exception e) {
				e.printStackTrace();
				Log.error(getClass(), "Texture not found or loaded: " + path);
				return;
			}
		}
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, id);

		GL46.glPixelStorei(GL46.GL_UNPACK_ALIGNMENT, 1);
		GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width[0], height[0], 0, GL46.GL_RGBA,
				GL46.GL_UNSIGNED_BYTE, image);
		GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);
		GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);
		GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D);
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
		this.width = width[0];
		this.height = height[0];
		this.channels = channels[0];
		STBImage.stbi_image_free(image);
	}

	public void dispose() {
		Log.info(getClass(), "Disposing texture: " + name);
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
		GL46.glDeleteTextures(id);
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
