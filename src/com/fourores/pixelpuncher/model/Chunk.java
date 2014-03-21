package com.fourores.pixelpuncher.model;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.fourores.pixelpuncher.model.Block.Material;

public class Chunk {
	public Block[][] blocks = new Block[16][16];
	private Stage stage;
	public Chunk(Stage stage) {
		this.stage = stage;
	}
	public Block getBlockAt(int x, int y) {
		return blocks[(int) (Math.abs(x) % 16)][(int) (Math.abs(y) % 16)];
	}
	public void setBlockAt(int x, int y, Material material) {
		Block block = getBlockAt(x, y) != null ? getBlockAt(x, y) : new Block(stage, this, material);
		block.position.set(x * 5, y * 5);
		blocks[(int) (Math.abs(x) % 16)][(int) (Math.abs(y) % 16)] = block;
	}
	
	public void render() {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				Block block = getBlockAt(x, y);
				if (block != null) {
					block.render(stage.getSpriteBatch());
				}
			}
		}
	}
}
