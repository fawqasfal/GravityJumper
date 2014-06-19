package com.mygdx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;

public class Hero {
	Rectangle rectRep;
	TextureRegion image;
	boolean isJumping;
	int direction;

	public static final int UP = 1;
	public static final int DOWN = -1;

	public static final String DEFAULT_IMAGE = "stable.png";
	public static final int DEFAULT_START_X = 7;
	public static final int DEFAULT_START_Y = 1; //from top-to-bottom, we want to cut off the extra pixel on top of his head
	public static final int DEFAULT_IMG_WIDTH = 18;
	public static final int DEFAULT_IMG_HEIGHT = 32;
	public static final float SCALE = 4.5f;
 
	public Hero(float x, float y, float imgX, float imgY, float width, float height, Texture image) {
		this.rectRep = new Rectangle(x, y, SCALE * width, SCALE * height);
		this.image = new TextureRegion(image, DEFAULT_START_X, DEFAULT_START_Y, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT); 
		this.isJumping = false;
		this.direction = 1;
	}

	public Hero(float spawnX, float spawnY) {
		this(spawnX, spawnY, DEFAULT_START_X, DEFAULT_START_Y, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, 
			new Texture(Gdx.files.internal(DEFAULT_IMAGE)));
	}
	public void moveLeft(float amt) {
		this.rectRep.x -= amt;
	}

	public void moveRight(float amt) {
		this.rectRep.x += amt;
	}

	//character's "jump" is really flipping up and down until it hits a platform of some sort, much like Gravity Guy
	public void jump() {
		this.isJumping = true;
		this.direction = -1 * direction;
	}

	public void jumpIter(float amt) {
		if (this.isJumping) this.rectRep.y += direction * amt;		
	}

	public boolean collides(Platform platform) {
		if (this.rectRep.overlaps(platform.rectRep)) {
			this.isJumping = false;
			return true;
		} else{
			return false;
		}
	}
	public Rectangle getRect() {
		//GET WRECKED! 
		return this.rectRep;
	}
}
