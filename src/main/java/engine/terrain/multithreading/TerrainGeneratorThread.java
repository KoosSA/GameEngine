package engine.terrain.multithreading;

import com.koossa.logger.Log;

import engine.terrain.Chunk;
import engine.terrain.TerrainManager;

public class TerrainGeneratorThread extends Thread {

	public TerrainGeneratorThread() {
		super();
		setName("Terrain generation thread.");
	}

	public boolean run = true;

	@Override
	public void run() {
		Log.info(getClass(), "Starting: " + getName());
		while (run) {
			gen();
			try {
				sleep(1000000000);
			} catch (InterruptedException e) {

			}
		}
		Log.info(getClass(), "Stopping: " + getName());
	}

	private void gen() {
		//int i = 0;
		while (!TerrainManager.getGenQueueIn().isEmpty()) {
			PassTerrainData data = TerrainManager.getGenQueueIn().poll();
			data.chunk = new Chunk(data.vertex_count, data.size, data.right, data.up);
			TerrainManager.getGenQueueOut().add(data);
			//System.out.println("Chunk generated: " + data.id + " ---- " + i++);
			if (!run) break;
		}
	}

}
