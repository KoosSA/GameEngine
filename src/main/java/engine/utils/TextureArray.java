package engine.utils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

public class TextureArray extends Texture {

	private int numLayers = 0;
	private String name = "";
	private String collectionFolderName;
	private List<String> names = new ArrayList<String>();

	public TextureArray(String name, String collectionFolderName, int texWidth, int texHeight) {
		super(name);
		this.width = texWidth;
		this.height = texHeight;
		this.collectionFolderName = collectionFolderName;
	}

	@Override
	protected void loadTexture(String name) {

		int[] w = new int[1];
		int[] h = new int[1];
		int[] c = new int[1];

		File collection = new File(Files.getCommonFolderPath(CommonFolders.Textures), collectionFolderName);
		numLayers  = collection.list().length;

		id = GL46.glGenTextures();
		GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, id);

		GL46.glTexStorage3D(GL46.GL_TEXTURE_2D_ARRAY, 1, GL46.GL_RGBA8, width, height, numLayers);

		for (int layer = 0; layer < numLayers; layer++) {
			names.add(collection.listFiles()[layer].getName());
			ByteBuffer image = STBImage.stbi_load(collection.listFiles()[layer].getAbsolutePath(), w, h, c, STBImage.STBI_rgb_alpha);
			if (image == null) {
				try {
					byte[] bytes = getClass().getResourceAsStream(collection.listFiles()[layer].getAbsolutePath()).readAllBytes();
					ByteBuffer buff = BufferUtils.createByteBuffer(bytes.length);
					buff.put(bytes);
					buff.flip();
					image = STBImage.stbi_load_from_memory(buff, w, h, c, STBImage.STBI_rgb_alpha);
				} catch (IOException e) {
					e.printStackTrace();
					Log.error(getClass(), "Texture in array texture not loaded: " + collection.listFiles()[layer].getAbsolutePath());
					return;
				}
			}
			GL46.glTexSubImage3D(GL46.GL_TEXTURE_2D_ARRAY, 0, 0, 0, layer, width, height, numLayers, GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, image);
			STBImage.stbi_image_free(image);
		}

		GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);
		GL46.glTexParameteri(GL46.GL_TEXTURE_2D_ARRAY, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR);

		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
	}

	@Override
	public void dispose() {
		Log.info(getClass(), "Disposing texture array: " + name);
		GL46.glBindTexture(GL46.GL_TEXTURE_2D_ARRAY, 0);
		GL46.glDeleteTextures(id);
	}

	public int getNumLayers() {
		return numLayers;
	}

	public int getLayerIndex(String name) {
		return names.indexOf(name);
	}

}
