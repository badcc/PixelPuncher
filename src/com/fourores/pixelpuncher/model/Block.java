package com.fourores.pixelpuncher.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Block {
	public Vector2 position;
	public Vector2 size;
	public Rectangle bounds;
	private Texture sheet;
	private TextureRegion[][] sheetRegion;
	private TextureRegion regionToDraw;
	private Chunk chunk;
	
	public ShapeRenderer debugR = new ShapeRenderer();
	private static Stage stage;
	
	public static enum Material {
		STONE, STONE_CRACKED, STONE_CARVED, STONE_MOSSY, STONE_COMPACT
	}
	public Block(Stage stage, Chunk chunk, Material material) {
		Block.stage = stage;
		this.chunk = chunk;
		position = new Vector2();
		size = new Vector2(5, 5);
		bounds = new Rectangle(position.x, position.y, size.x, size.y);
		
		sheet = new Texture(Gdx.files.internal("data/block_sprite.png"));
		sheetRegion = TextureRegion.split(sheet, 12, 12);
		setMaterial(material);
	}
	public void setMaterial(Material material) {
		switch (material) {
		case STONE_CRACKED:
			regionToDraw = sheetRegion[1][0];
			break;
		case STONE_CARVED:
			regionToDraw = sheetRegion[2][0];
			break;
		case STONE_MOSSY:
			regionToDraw = sheetRegion[3][0];
			break;
		case STONE_COMPACT:
			regionToDraw = sheetRegion[4][0];
			break;
		default: // case STONE:
			regionToDraw = sheetRegion[0][0];
			break;
		}
	}
//	public void render(SpriteBatch batch) {
//		batch.begin();
//		camera = stage.getCamera();
////		if (Math.floor(position.x) < camera.position.x + camera.viewportWidth/ 2 &&
////				Math.floor(position.y) < camera.position.y + camera.viewportHeight / 2) {
//			bounds.x = position.x;
//			bounds.y = position.y;
//			bounds.width = size.x;
//			bounds.height = size.y;
//			batch.draw(regionToDraw, position.x, position.y, size.x, size.y * regionToDraw.getRegionHeight()/regionToDraw.getRegionWidth());
//			batch.end();
//			Gdx.app.log("x/y", "x: " + position.x + ", y: " + position.y);
//			debugR.setProjectionMatrix(camera.combined);
//			debugR.begin(ShapeType.Line);
//			debugR.setColor(Color.RED);
//			debugR.rect(bounds.x, bounds.y, bounds.width, bounds.height);
//			debugR.end();
////		}
//	}
	public Chunk getChunk() {
		return chunk;
	}
	public void render(SpriteBatch batch) {
		bounds.x = position.x;
		bounds.y = position.y;
		bounds.width = size.x;
		bounds.height = size.y;
		batch.begin();
//		float alpha = (float)Math.abs(Math.sin(Math.floor(System.currentTimeMillis()) / 100000));
//		if (alpha < 0.3f)
//			alpha = 0.3f;
//		batch.setColor(1, 1, 1, alpha);
		
		batch.draw(regionToDraw, position.x, position.y, size.x, size.y * regionToDraw.getRegionHeight()/regionToDraw.getRegionWidth());
		batch.end();
		
	}
	
}
