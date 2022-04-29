package engine.utils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

public class TextureArray {

	private int id = 0;
	private int width = 0;
	private int height = 0;
	private int channels = 0;
	private int numLayers = 0;
	private String name = "";

	public TextureArray(String name, String collectionFolderName, int texWidth, int texHeight) {
		loadTexture(name, texWidth, texHeight);
		this.name = name;
		Log.info(getClass(), "Texture array loaded: " + name);
	}

	private void loadTexture(String collectionFolderName, int texWidth, int texHeight) {

		int[] width = new int[1];
		int[] height = new int[1];
		int[] channels = new int[1];

		File collection = new File(Files.getCommonFolderPath(CommonFolders.Textures), collectionFolderName);
		numLayers  = collection.list().length;

		id = GL46.glGenTextures();
		GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, id);

		GL46.glTexStorage3D(GL46.GL_TEXTURE_2D_ARRAY, 1, GL46.GL_RGBA8, texWidth, texHeight, numLayers);

		for (int layer = 0; layer < numLayers; layer++) {
			ByteBuffer image = STBImage.stbi_load(collection.listFiles()[layer].getAbsolutePath(), width, height, channels, STBImage.STBI_rgb_alpha);
			if (image == null) {
				try {
					byte[] bytes = getClass().getResourceAsStream(collection.listFiles()[layer].getAbsolutePath()).readAllBytes();
					ByteBuffer buff = BufferUtils.createByteBuffer(bytes.length);
					buff.put(bytes);
					buff.flip();
					image = STBImage.stbi_load_from_memory(buff, width, height, channels, STBImage.STBI_rgb_alpha);
				} catch (IOException e) {
					e.printStackTrace();
					Log.error(getClass(), "Texture in array texture not loaded: " + collection.listFiles()[layer].getAbsolutePath());
					return;
				}
			}
			GL46.glTexSubImage3D(GL46.GL_TEXTURE_2D_ARRAY, 0, 0, 0, layer, texWidth, texHeight, numLayers, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
			STBImage.stbi_image_free(image);
		}

		GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);
		GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);

		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
		this.width = width[0];
		this.height = height[0];
		this.channels = channels[0];

	}


	public void dispose() {
		Log.info(getClass(), "Disposing texture array: " + name);
		GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);
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

	public int getNumLayers() {
		return numLayers;
	}

}
