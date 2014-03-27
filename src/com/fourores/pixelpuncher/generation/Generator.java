package com.fourores.pixelpuncher.generation;

import com.badlogic.gdx.math.Vector2;
import com.fourores.pixelpuncher.model.Chunk;

public abstract class Generator {
	public abstract Chunk getChunkAt(int chunkX, int chunkY);
	public Chunk getChunkAt(Vector2 vector) {
		return getChunkAt((int)vector.x, (int)vector.y);
	}
//	public abstract Chunk doesChunkExistAt(int x, int y);
	public long toLong(int msw, int lsw) {
        return ((long) msw << 32) + lsw - Integer.MIN_VALUE;
    }
	public Vector2 posToChunkPos(float x, float y) {
		float newX = x / 16;
		float newY = y / 16;
		Vector2 retVec = new Vector2(newX, newY);
		if (newX <= 0){
			retVec.x = (float)Math.floor(newX);
		}
		if (newY <= 0) {
			retVec.y = (float)Math.floor(newY);
		}
//		if (newY < 0)
//			retVec.y = (float)Math.floor(newY);
//		else {
//			retVec.y = (float)Math.ceil(newY) - 1;
//		}
		return retVec;
	}
}