package game;

import engine.logic.Game;
import engine.settings.WindowSettings;
import engine.terrain.TerrainGenerator;

public class TestGame extends Game {

	public static void main(String[] args) {
		TestGame app = new TestGame();
		WindowSettings.fullscreen = false;
		WindowSettings.vsync = true;
		app.start();
	}

	@Override
	protected void init() {

		TerrainGenerator.init();

	}

	@Override
	protected void update(float delta) {

	}

	@Override
	protected void render() {

	}

	@Override
	protected void dispose() {

	}

}
