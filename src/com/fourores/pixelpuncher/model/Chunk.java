package com.fourores.pixelpuncher.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.fourores.pixelpuncher.model.Block.Material;

public class Chunk {
	public Block[][] blocks = new Block[16][16];
	private static SpriteBatch batch;
	private static Texture sheet;
	private static TextureRegion[][] sheetRegion;
	
	public Chunk(SpriteBatch batch) {
		Chunk.batch = batch;
		sheet = new Texture(Gdx.files.internal("data/block_sprite.png"));
		sheetRegion = TextureRegion.split(sheet, 12, 12);
	}
	public TextureRegion getTextureFromMaterial(Material material) {
		switch (material) {
		case STONE_CRACKED:
			return sheetRegion[1][0];
		case STONE_CARVED:
			return sheetRegion[2][0];
		case STONE_MOSSY:
			return sheetRegion[3][0];
		case STONE_COMPACT:
			return sheetRegion[4][0];
		default: // case STONE:
			return sheetRegion[0][0];
		}
	}
	public Block getBlockAt(int x, int y) {
		return blocks[(int) (Math.abs(x) % 16)][(int) (Math.abs(y) % 16)];
	}
	public void setBlockAt(int x, int y, Material material) {
		Block block = getBlockAt(x, y) != null ? getBlockAt(x, y) : new Block(this, material);
		block.position.set(x * 5, y * 5);
		blocks[(int) (Math.abs(x) % 16)][(int) (Math.abs(y) % 16)] = block;
	}
	public void setBlockAt(int x, int y) {
		setBlockAt(x, y, Material.STONE);
	}
	public Vector2 blockToScreen(Vector2 vector) {
		return vector.div(5);
	}
	public Vector2 screenToBlock(Vector2 vector) {
		return vector.scl(5);
	}
	public void render() {
		batch.begin();
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				Block block = getBlockAt(x, y);
				if (block != null) {
					block.update();
					TextureRegion blockTexture = getTextureFromMaterial(block.getMaterial());
					Vector2 blockPosition = block.position;
					Vector2 blockSize = block.size;
					
					batch.draw(blockTexture, blockPosition.x, blockPosition.y, blockSize.x, blockSize.y);
				}
			}
		}
		batch.end();
	}
}
