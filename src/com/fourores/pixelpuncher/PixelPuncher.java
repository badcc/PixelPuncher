package com.fourores.pixelpuncher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.fourores.pixelpuncher.model.Character;
import com.fourores.pixelpuncher.model.World;

public class PixelPuncher implements ApplicationListener {
	private Camera camera;
	private Character character;
	private World world;
	private Stage stage;
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		Texture.setEnforcePotImages(false);
		
		stage = new Stage(100, 100 * h/w, true);
		camera = stage.getCamera();
		world = new World(stage);
		
		character = new Character(world, camera);
		stage.addActor(character);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render() {
//		float alpha = (float)Math.abs(Math.sin(Math.floor(System.currentTimeMillis()) / 100000));
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		float delta = Gdx.graphics.getDeltaTime();
		character.update(delta);
		
		stage.draw();
		world.render();
		
		Gdx.gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
