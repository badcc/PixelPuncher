package com.fourores.pixelpuncher.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.fourores.pixelpuncher.model.Block.Material;

public class World {
	public Array<Block> blocks = new Array<Block>();
	public Block[][] worldBlocks = new Block[0][0];
	public ArrayMap<Long, Chunk> chunks = new ArrayMap<Long, Chunk>();
	public Array<Chunk> chunksLoaded = new Array<Chunk>();
	
	private Stage stage;
	private Camera camera;
	public World(Stage stage) {
		this.stage = stage;
		camera = stage.getCamera();
	}
	public static long toLong(int msw, int lsw) {
        return ((long) msw << 32) + lsw - Integer.MIN_VALUE;
    }
	public void create(Stage stage) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		setBlockAt(0, 0);
		setBlockAt(0, 1, Material.STONE_CRACKED);
		setBlockAt(0, 2, Material.STONE_CARVED);
		setBlockAt(0, 3, Material.STONE_MOSSY);
		setBlockAt(0, 4, Material.STONE_COMPACT);
		setBlockAt(0, 5);
		
		for (int x = 1; x < 100 / 5; x++) {
			setBlockAt(x, 0);
		}
		for (int x = 30 / 5; x < 100 / 5; x++) {
			setBlockAt(x, 4);
		}
		for (int x = 1; x < 25 / 5; x++) {
			setBlockAt(x, 5);
		}
		for (int x = 40 / 5; x < 90 / 5; x++) {
			setBlockAt(x, 1);
		}
		
		for (int x = 95 / 5; x < 190 / 5; x++) {
			for (int y = 1; y < 10; y+=2) {
				if ((x + y) % 3 == 0)
					setBlockAt(x, y);
			}
		}
	}
	public Chunk getChunkAt(int x, int y) {
		return chunks.get(toLong((int)Math.ceil(x / 16) - 1, (int)Math.ceil(y / 16) - 1));
	}
	public Chunk getChunkAt(Vector2 position) {
		return getChunkAt((int)position.x, (int)position.y);
	}
	public Array<Chunk> getSurroundingChunks(Chunk chunk) {
		Array<Chunk> chunks = new Array<Chunk>();
		chunks.add(chunk);
		return null;
	}
	public Block getBlockAt(int x, int y) {
		Chunk chunk = getChunkAt(x, y);
		if (chunk != null)
			return chunk.getBlockAt(x, y);
		else
			return null;
	}
	public void setBlockAt(int x, int y, Material material) {
		Chunk chunk = getChunkAt(x, y);
		if (chunk == null) {
			chunk = new Chunk(stage);
			chunks.put(toLong((int)Math.ceil(x / 16) - 1, (int)Math.ceil(y / 16) - 1), chunk);
		}
		chunk.setBlockAt(x, y, material);
	}
	
	public void setBlockAt(int x, int y) {
		setBlockAt(x, y, Material.STONE);
	}
	public float screenToWorld(float i) {
		return Math.round(i / 5);
	}
	public Vector2 screenToWorld(float x, float y) {
		return new Vector2((float)Math.round(x / 5), (float)Math.round(y / 5));
	}
	
	public Vector2 blockToScreen(Vector2 vector) {
//		return new Vector2((float)Math.floor(vector.x / 5), (float)Math.floor(vector.y / 5));
		return vector.scl(5);
	}
	public Vector2 screenToBlock(Vector2 vector) {
		Vector2 newVector = vector.div(5);
		return new Vector2((float)Math.round(newVector.x), (float)Math.round(newVector.y));
	}
	public Array<Block> getBlocksInRegion(float startX, float endX, float startY, float endY) {
		Vector2 start = screenToBlock(new Vector2(startX, startY));
		Vector2 end = screenToBlock(new Vector2(endX, endY));
		Array<Block> retArray = new Array<Block>();
		for (float x = start.x; x < end.x; x++) {
			for (float y = start.y; y < end.y; y++) {
				Block block = getBlockAt((int)x, (int)y);
				if (block != null) {
					retArray.add(block);
				}
			}
		}
		return retArray;
	}
	public void render() {
		Chunk chunk = getChunkAt(screenToWorld(camera.position.x, camera.position.y));
		if (chunk != null)
			chunk.render();
//		for (int i = 0; i < chunks.size; i++) {
//			Chunk chunk = chunks.getValueAt(i);
//			chunk.render();
//		}
	}
	
}
