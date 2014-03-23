package com.fourores.pixelpuncher.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {
	public Vector2 position;
	public Vector2 size;
	public Rectangle bounds;
	public Material material;
	private Chunk chunk;
	
	public static enum Material {
		STONE, STONE_CRACKED, STONE_CARVED, STONE_MOSSY, STONE_COMPACT
	}
	public Block(Chunk chunk, Material material) {
		this.chunk = chunk;
		position = new Vector2();
		size = new Vector2(5, 5);
		bounds = new Rectangle(position.x, position.y, size.x, size.y);
		
		
		setMaterial(material);
	}
	public Vector2 getOppositeCorner() {
		return position.cpy().add(size);
	}
	public Vector2 getWorldPosition() {
		return position.cpy().div(5);
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public Material getMaterial(){
		return material;
	}
	public Chunk getChunk() {
		return chunk;
	}
	public Vector2 getPosition() {
		return position;
	}
	public void update() {
		bounds.x = position.x;
		bounds.y = position.y;
		bounds.width = size.x;
		bounds.height = size.y;
	}
}
