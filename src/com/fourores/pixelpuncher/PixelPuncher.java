package com.fourores.pixelpuncher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.fourores.pixelpuncher.model.Character;
import com.fourores.pixelpuncher.model.World;

public class PixelPuncher implements ApplicationListener {
	private OrthographicCamera camera;
	private Character character;
	private World world;
	private SpriteBatch batch;
	private float ambientIntensity = 0.45f;
	private Vector3 ambientColor = new Vector3(0.3f, 0.3f, 0.5f);
	
	private FrameBuffer fbo;
	
	private ShaderProgram defaultShader;
	private ShaderProgram ambientShader;
	private ShaderProgram lightShader;
	private ShaderProgram finalShader;
	
	private String vertexShader;
	private String defaultPixelShader;
	private String ambientPixelShader;
	private String lightPixelShader;
	private String finalPixelShader;
	
	private Texture light;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		Texture.setEnforcePotImages(false);
		
//		stage = new Stage(100, 100 * h/w, true);
		camera = new OrthographicCamera(100, 100 * h/w);
		batch = new SpriteBatch();
		world = new World(camera, batch);
		
		character = new Character(world, camera);
//		stage.addActor(character);
		
		ShaderProgram.pedantic = false;
		vertexShader = Gdx.files.internal("shader/vertexShader.glsl").readString();
		defaultPixelShader = Gdx.files.internal("shader/defaultPixelShader.glsl").readString();
		ambientPixelShader = Gdx.files.internal("shader/ambientPixelShader.glsl").readString();
		lightPixelShader = Gdx.files.internal("shader/lightPixelShader.glsl").readString();
		finalPixelShader = Gdx.files.internal("shader/pixelShader.glsl").readString();
		
		defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);
		ambientShader = new ShaderProgram(vertexShader, ambientPixelShader);
		lightShader = new ShaderProgram(vertexShader, lightPixelShader);
		finalShader = new ShaderProgram(vertexShader, finalPixelShader);
		
		ambientShader.begin();
		ambientShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
				ambientColor.z, ambientIntensity);
		ambientShader.end();
		
		lightShader.begin();
		lightShader.setUniformi("u_lightmap", 1);
		lightShader.end();
		
		finalShader.begin();
		finalShader.setUniformi("u_lightmap", 1);
		finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
				ambientColor.z, ambientIntensity);
		finalShader.end();
		
		light = new Texture(Gdx.files.internal("data/ambient.png"));
		int width = (int) 100;
		int height = (int) (100 * h/w);
		
		fbo = new FrameBuffer(Format.RGBA8888, width, height, false);
		 
		lightShader.begin();
		lightShader.setUniformf("resolution", width, height);
		lightShader.end();

		finalShader.begin();
		finalShader.setUniformf("resolution", width, height);
		finalShader.end();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();
		character.update(delta);

		batch.begin();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setShader(finalShader);
		world.render(batch);
//		batch.begin();
		
		batch.setProjectionMatrix(camera.combined);
		batch.setShader(defaultShader);
		batch.enableBlending();
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		fbo.begin();
		float lightSize = (float) Math.sin(Math.floor(System.currentTimeMillis() * 500) / 100000) * 4 + 55;
		batch.draw(light, character.position.x - (lightSize / 2) + 2, character.position.y - (lightSize / 2) + 4, lightSize, lightSize);
		fbo.getColorBufferTexture().bind(1);
		fbo.end();
		
		light.bind(0);
//		world.render(batch);
		
		character.render(batch);
		batch.end();
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
