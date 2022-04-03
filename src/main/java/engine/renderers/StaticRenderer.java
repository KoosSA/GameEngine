package engine.renderers;

import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import engine.models.Model;
import engine.shaders.StaticShader;
import engine.utils.Camera;
import engine.utils.MathUtils;

public class StaticRenderer {

	private StaticShader shader;
	private Camera cam;

	public StaticRenderer(Camera cam) {
		shader = new StaticShader();
		this.cam = cam;
		shader.start();
		shader.loadProjectionMatrix(cam.getProjectionMatrix());
		shader.stop();
	}

	public void render(List<Model> toRender) {
		shader.start();

		shader.loadViewMatrix(cam.getViewMatrix());

		toRender.forEach(model -> {
			model.getMeshList().forEach(mesh -> {

				shader.loadMaterial(mesh.getMaterial());

				GL30.glBindVertexArray(mesh.getRawModel().getVaoId());

				shader.loadTransformationMatrix(MathUtils.getTransformationMatrix(new Vector3f(0,0,0), new Vector3f(), new Vector3f(1)));



				GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getRawModel().getCount(), GL30.GL_UNSIGNED_INT, 0);

				GL30.glBindVertexArray(0);

			});
		});

		shader.stop();
	}

	public void dispose() {
		shader.dispose();
	}

}
