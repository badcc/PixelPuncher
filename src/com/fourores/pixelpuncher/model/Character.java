package com.fourores.pixelpuncher.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Character extends Actor {
	private Texture sheet;
	private TextureRegion[][] sheetRegion;
	
	public Vector2 position;
	public Vector2 velocity;
	public static long LONG_JUMP_TIME = 250l;
	public static float MAX_VELOCITY = 15f;
	public static float JUMP_VELOCITY = 98.1f * 0.75f;
	public static float GRAVITY = -9.81f * 0.5f;
	public static float DAMPING = 0.75f;
	
	public Rectangle bounds;
	
	public boolean isGrounded = false;
	private TextureRegion regionToDraw;
	
	public static ShapeRenderer debugR = new ShapeRenderer();
	
	public enum State {
		IDLE, WALKING, JUMPING, FALLING
	}
	public State state = State.IDLE;
	public long jumpStart = 0;
	private World world;
	private Camera camera;
	private boolean movingRight = false;
	private boolean walkingTextureRight = true;
	private Sound[] jumpSounds;
	private Sound hitGround;
	private float texRatio;
	public Character(World world, Camera camera) {
		this.world = world;
		this.camera = camera;
		position = new Vector2(0, 60);
		velocity = new Vector2();
		
		jumpSounds = new Sound[3];
		jumpSounds[0] = Gdx.audio.newSound(Gdx.files.internal("data/jump0.wav"));
		jumpSounds[1] = Gdx.audio.newSound(Gdx.files.internal("data/jump1.wav"));
		jumpSounds[2] = Gdx.audio.newSound(Gdx.files.internal("data/jump2.wav"));
		
		hitGround = Gdx.audio.newSound(Gdx.files.internal("data/hitGround.wav"));
		
		sheet = new Texture(Gdx.files.internal("data/char_sprite.png"));
		sheetRegion = TextureRegion.split(sheet, 13, 25);
		regionToDraw = sheetRegion[0][0];
		
		bounds = new Rectangle();
	}
	public void setStateTexture() {
		switch (state) {
		case WALKING:
			regionToDraw = sheetRegion[0][1];
			if (movingRight != walkingTextureRight){
				regionToDraw.flip(true, false);
				walkingTextureRight = movingRight;
			}
			break;
		case JUMPING:
			regionToDraw = sheetRegion[0][2];
			break;
		case FALLING:
			regionToDraw = sheetRegion[0][2];
			break;
		default: // case IDLE:
			regionToDraw = sheetRegion[0][0];
			break;
		}
	}
	public boolean isColliding(Array<Block> blocks) {
		for (Block block : blocks) {
			if (block.bounds.overlaps(bounds)) {
				return true;
			}
		}
		return false;
	}
	public void debugCollide() {
		Array<Block> blocks = world.getBlocksInRegion(position.x - 1, position.x + 5, bounds.y - 5f * texRatio, bounds.y + 5f * texRatio);
		for (Block block : blocks) {
			debugR.setProjectionMatrix(camera.combined);
			debugR.begin(ShapeType.Filled);
			debugR.setColor(Color.RED);
			debugR.rect(block.bounds.x, block.bounds.y, block.bounds.width, block.bounds.height);
			debugR.end();
		}
	}
	public void update(float delta) {
		texRatio = (float)regionToDraw.getRegionHeight()/(float)regionToDraw.getRegionWidth();
		
		bounds.height = 4f * texRatio;
		bounds.width = 4f;
		float accelXVel = Gdx.input.getAccelerometerY();
		if (accelXVel < -1 || accelXVel > 1)
			velocity.x = accelXVel * Character.MAX_VELOCITY;
		else
			velocity.x *= DAMPING;
		
		if (velocity.x < -1)
			movingRight = false;
		else if (velocity.x > 1)
			movingRight = true;
		
		velocity.add(0, GRAVITY);
		if (Gdx.input.isTouched() && (isGrounded || System.currentTimeMillis() - jumpStart < LONG_JUMP_TIME)) {
			velocity.y = Character.JUMP_VELOCITY;
			state = State.JUMPING;
			isGrounded = false;
			if (System.currentTimeMillis() - jumpStart > LONG_JUMP_TIME){
				jumpStart = System.currentTimeMillis();
				jumpSounds[(int) (Math.random() * jumpSounds.length)].play(0.5f);
			}
		}
		
		bounds.x = position.x + (velocity.x * delta);
		bounds.y = position.y + (velocity.y * delta);
		Array<Block> blocks;
		blocks = world.getBlocksInRegion(position.x - 2, position.x + 6, bounds.y - 5f * texRatio, bounds.y + 5f * texRatio);
		if (isColliding(blocks)){
			if (!isGrounded && state != State.JUMPING && state != State.WALKING){
				isGrounded = true;
				hitGround.play(0.4f);
			}
			velocity.y = 0;
		} else if (state == State.JUMPING || state == State.FALLING ) {
			isGrounded = false;
		}
		blocks = world.getBlocksInRegion(bounds.x - 5, bounds.x + 9, bounds.y - 2, bounds.y + 5f * texRatio);
		if (isColliding(blocks)) {
			velocity.x = 0;
		}
//		if (state != State.JUMPING && (velocity.x < -1 || velocity.x > 1) && isGrounded)
//			state = State.WALKING;
//		
//		if (isGrounded && velocity.x > -1 && velocity.x < 1)
//			state = State.IDLE;
//		
//		if (!isGrounded)
//			state = State.FALLING;
		
		
//		if (isGrounded && (velocity.x < -1 || velocity.x > 1) && velocity.y == 0 && state != State.FALLING && state != State.JUMPING)
//			state = State.WALKING;
//		if (isGrounded && state != State.JUMPING && state != State.WALKING && (velocity.x > -1 || velocity.x < 1) && velocity.y == 0)
//			state = State.IDLE;
//		if (!isGrounded && (state == State.JUMPING && velocity.y < 0) || (state == State.WALKING && velocity.y != 0))
//			state = State.FALLING;
		
		if (!isGrounded && state != State.FALLING && state != State.IDLE && velocity.y < 0)
			state = State.FALLING;
		if (isGrounded && (state == State.FALLING || state == State.WALKING) && (velocity.x > -1 && velocity.x < 1))
			state = State.IDLE;
		if (isGrounded && (state == State.IDLE || state == State.FALLING) && (velocity.x < -1 || velocity.x > 1))
			state = State.WALKING;
		
		Gdx.app.log("state", state + " < > " + isGrounded + " | " + velocity.y);
		
//		if (state != State.WALKING && state != State.JUMPING && state != State.FALLING && !isGrounded)
//			hitGround.play(0.4f);
		
		setStateTexture();
		position.add(velocity.cpy().scl(delta));
		camera.position.x = position.x;
		camera.position.y = position.y;
		
		if (position.y < -5*5)
			position = new Vector2(0, 60);
		
		/*
		 * IDLE, WALKING, JUMPING, FALLING
		 * 
		 * WALKING if tilted and grounded not JUMPING, FALLING
		 * FALLING if not grounded, WALKING
		 * IDLE if grounded not WALKING, JUMPING, FALLING
		 * not grounded if JUMPING, FALLING
		 */
	}
	@Override
	public void draw(SpriteBatch batch, float alpha) {
		
		batch.draw(regionToDraw, position.x, position.y, 4f, 4f * texRatio);
		batch.end();
		
//		debugCollide();
//		debugR.setProjectionMatrix(camera.combined);
//		debugR.begin(ShapeType.Line);
//		debugR.setColor(Color.BLUE);
//		debugR.rect(bounds.x, bounds.y, bounds.width, bounds.height);
//		debugR.end();
		
		batch.begin();
	}
	
}
