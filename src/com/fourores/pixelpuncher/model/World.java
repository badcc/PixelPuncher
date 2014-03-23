package com.fourores.pixelpuncher.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.fourores.pixelpuncher.generation.Generator;
import com.fourores.pixelpuncher.generation.TestGenerator;

public class World {
//	public ArrayMap<Long, Chunk> chunks = new ArrayMap<Long, Chunk>();
	
	private Camera camera;
	public Generator worldGenerator;
	public World(Stage stage) {
		camera = stage.getCamera();
		worldGenerator = new TestGenerator(stage.getSpriteBatch());
	}
	public static long toLong(int msw, int lsw) {
        return ((long) msw << 32) + lsw - Integer.MIN_VALUE;
    }
	public void create(Stage stage) {
//		setBlockAt(0, 0);
//		setBlockAt(0, 1, Material.STONE_CRACKED);
//		setBlockAt(0, 2, Material.STONE_CARVED);
//		setBlockAt(0, 3, Material.STONE_MOSSY);
//		setBlockAt(0, 4, Material.STONE_COMPACT);
//		setBlockAt(0, 5);
//		
//		for (int x = 1; x < 100 / 5; x++) {
//			setBlockAt(x, 0);
//		}
//		for (int x = 30 / 5; x < 100 / 5; x++) {
//			setBlockAt(x, 4);
//		}
//		for (int x = 1; x < 25 / 5; x++) {
//			setBlockAt(x, 5);
//		}
//		for (int x = 40 / 5; x < 90 / 5; x++) {
//			setBlockAt(x, 1);
//		}
//		for (int x = 90 / 5; x < 300; x++) {
//			setBlockAt(x, 1);
//		}
	}
//	public Chunk getChunkAt(int x, int y) {
//		Vector2 chunkLocation = new Vector2((int)Math.ceil(x / 16) - 1, (int)Math.ceil(y / 16) - 1);
//		return chunks.get(toLong((int)chunkLocation.x, (int)chunkLocation.y));
//	}
	public Chunk getChunkAt(int x, int y) {
		return worldGenerator.getChunkAt(worldGenerator.posToChunkPos(x,  y));
	}
	public Chunk getChunkAt(Vector2 position) {
		return getChunkAt((int)position.x, (int)position.y);
	}
	public Block getBlockAt(int x, int y) {
		Chunk chunk = getChunkAt(x, y);
		if (chunk != null)
			return chunk.getBlockAt(x, y);
		else
			return null;
	}
	public Array<Chunk> getSurroundingChunks(Chunk chunk) {
		Array<Chunk> chunks = new Array<Chunk>();
		if (chunk == null)
			return chunks;
		
		Vector2 primaryPosition = screenToWorld(camera.position.x, camera.position.y);
		
		Chunk chunkAbove = getChunkAt((int)primaryPosition.x, (int)primaryPosition.y + 16);
		Chunk chunkBelow = getChunkAt((int)primaryPosition.x, (int)primaryPosition.y - 16);
		Chunk chunkRight = getChunkAt((int)primaryPosition.x + 16, (int)primaryPosition.y);
		Chunk chunkLeft = getChunkAt((int)primaryPosition.x - 16, (int)primaryPosition.y);
		// chunkBelow & chunkLeft 'y' could be -1, but let's be uniform!
		Chunk chunkTopRight = getChunkAt((int)primaryPosition.x + 16, (int)primaryPosition.y + 16);
		Chunk chunkTopLeft = getChunkAt((int)primaryPosition.x - 16, (int)primaryPosition.y + 16);
		Chunk chunkBottomRight = getChunkAt((int)primaryPosition.x + 16, (int)primaryPosition.y - 16);
		Chunk chunkBottomLeft = getChunkAt((int)primaryPosition.x - 16, (int)primaryPosition.y - 16);
		
		chunks.add(chunk);
		if (chunkAbove != null) chunks.add(chunkAbove);
		if (chunkBelow != null) chunks.add(chunkBelow);
		if (chunkRight != null) chunks.add(chunkRight);
		if (chunkLeft != null) chunks.add(chunkLeft);
		
		if (chunkTopRight != null) chunks.add(chunkTopRight);
		if (chunkTopLeft != null) chunks.add(chunkTopLeft);
		if (chunkBottomRight != null) chunks.add(chunkBottomRight);
		if (chunkBottomLeft != null) chunks.add(chunkBottomLeft);
		
		return chunks;
	}
//	public void setBlockAt(int x, int y, Material material) {
//		Chunk chunk = getChunkAt(x, y);
//		if (chunk == null) {
//			chunk = new Chunk(stage);
//			chunks.put(toLong((int)Math.ceil(x / 16) - 1, (int)Math.ceil(y / 16) - 1), chunk);
//		}
//		chunk.setBlockAt(x, y, material);
//	}
//	public void setBlockAt(int x, int y) {
//		setBlockAt(x, y, Material.STONE);
//	}
	public float screenToWorld(float i) {
		return Math.round(i / 5);
	}
	public Vector2 screenToWorld(float x, float y) {
		return new Vector2((float)Math.round(x / 5), (float)Math.round(y / 5));
	}
	public Vector2 blockToScreen(Vector2 vector) {
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
//		chunk.render();
		Array<Chunk> chunksToRender = getSurroundingChunks(chunk);
		for (Chunk chunkToRender : chunksToRender)
			chunkToRender.render();
	}
	
}
