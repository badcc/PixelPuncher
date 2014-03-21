package com.fourores.pixelpuncher.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.fourores.pixelpuncher.model.Block.Material;

public class World {
	public Array<Block> blocks = new Array<Block>();
	public Block[][] worldBlocks = new Block[0][0];
	public ArrayMap<Long, Chunk> chunks = new ArrayMap<Long, Chunk>();
	private Stage stage;
	public World(Stage stage) {
		this.stage = stage;
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
	}
	public Chunk getChunkAt(int x, int y) {
		return chunks.get(toLong((int)Math.ceil(x / 16) - 1, (int)Math.ceil(y / 16) - 1));
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
	public Array<Block> getBlocksInRegion(float startX, float endX, float startY, float endY) {
		Vector2 start = screenToWorld(startX, startY);
		Vector2 end = screenToWorld(endX, endY);
		Array<Block> retArray = new Array<Block>();
		for (float x = start.x; x < end.x; x++) {
			for (float y = start.y; y < end.y; y++) {
				Block block = getBlockAt((int)x, (int)y);
				if (block != null) {
					retArray.add(block);
				}
			}
		}
//		for (Block block : blocks) {
//			if (startX == endX) {
//				if (block.position.y > startY &&
//						block.position.y < endY) {
//					retArray.add(block);
//				}
//			}else if (startY == endY) {
//				if (block.position.x > startX &&
//						block.position.x < endX) {
//					retArray.add(block);
//				}
//			}else {
//				if (block.position.x > startX  &&
//						block.position.x < endX &&
//						block.position.y > startY &&
//						block.position.y < endY) {
//					retArray.add(block);
//				}
//			}
//		}
		return retArray;
	}
	public void render() {
//		Camera camera = stage.getCamera();
//		ArrayList<Chunk> chunksVisible = new ArrayList<Chunk>();
//		for (int x = (int) camera.position.x; x < (int) camera.position.x + camera.viewportWidth; x+=5) {
//			for (int y = (int) camera.position.y; y < (int) camera.position.y + camera.viewportHeight; y+=5) {
//				Chunk chunk = getChunkAt((int)screenToWorld(x), (int)screenToWorld(y));
//				if (chunk != null) {
//					if (chunksVisible.indexOf(chunk) == -1) {
//						chunksVisible.add(chunk);
//					}
//				}
//			}
//		}
//		
//		for (Chunk chunk : chunksVisible) {
//			chunk.render();
//		}
		for (int i = 0; i < chunks.size; i++) {
			Chunk chunk = chunks.getValueAt(i);
			chunk.render();
		}
	}
	
}
