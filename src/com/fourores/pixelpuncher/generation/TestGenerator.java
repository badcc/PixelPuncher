package com.fourores.pixelpuncher.generation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ArrayMap;
import com.fourores.pixelpuncher.model.Block.Material;
import com.fourores.pixelpuncher.model.Chunk;

public class TestGenerator extends Generator {
	public ArrayMap<Long, Chunk> chunks = new ArrayMap<Long, Chunk>();
	public SpriteBatch batch;
	public TestGenerator(SpriteBatch batch) {
		this.batch = batch;
	}
	@Override
	public Chunk getChunkAt(int chunkX, int chunkY) {
		long chunkKey = toLong(chunkX, chunkY);
		Chunk chunk = chunks.get(chunkKey);
		if (chunk == null) {
			chunk = new Chunk(batch);
			chunks.put(chunkKey, chunk);
		}
		
		for (int x = 0; x < 16; x++){
			float pos = (chunkX * 16 + x);
			float y = (float) (Math.abs(Math.sin(pos / 15) * 10));
			Gdx.app.log("y", "y: " + y);
			chunk.setBlockAt((int)pos, (int)y, Material.STONE_MOSSY);
			chunk.setBlockAt((int)pos, (int)y-1, Material.STONE_CARVED);
			for (int y2 = 0; y2 < y-2; y2++)
				chunk.setBlockAt(chunkX * 16 + x, y2, (Math.round(Math.random() * 3) == 0 ? Material.STONE_CRACKED : Material.STONE));
		}
		return chunk;
	}
	
//	@Override
//	public Chunk doesChunkExistAt(int x, int y) {
//		Vector2 chunkPos = posToChunkPos(x, y);
//		return null;
//	}
}
