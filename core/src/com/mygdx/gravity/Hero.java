package com.mygdx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Hero {
	Rectangle rectRep;
	TextureRegion image;
	
	Texture moveRightSheet;
	Texture moveLeftSheet; 
	Animation leftAnimation;
	Animation rightAnimation;

	boolean isJumping;
	int direction;

	public static final int UP = 1;
	public static final int DOWN = -1;
	public static final String DEFAULT_IMAGE = "stable.png";
	public static final String DEFAULT_MOVE_LEFT_IMAGE = "leftmove.png";
	public static final String DEFAULT_MOVE_RIGHT_IMAGE = "rightmove.png";
	public static final int DEFAULT_START_X = 7; //7 pixels of nothing on the 32x32
	public static final int DEFAULT_START_Y = 1; //from top-to-bottom, we want to cut off the extra pixel on top of his head
	public static final int DEFAULT_IMG_WIDTH = 18; //extra 7 pixels of nothing on the right
	public static final int DEFAULT_IMG_HEIGHT = 31;
	public static final float SCALE = 4.5f;
	public static final float FRAMES_PER_SECOND = 30f;
 
	public Hero(float x, float y, float imgX, float imgY, float width, float height, 
				Texture image, Texture moveLeftSheet, Texture moveRightSheet) {
		this.rectRep = new Rectangle(x, y, SCALE * width, SCALE * height);
		this.image = new TextureRegion(image, (int) imgX, (int) imgY, (int) width, (int) height); 
		this.moveRightSheet = moveRightSheet;
		this.moveLeftSheet = moveLeftSheet;
		this.setSheets();
		this.isJumping = false;
		this.direction = 1;
	}

	public Hero(float spawnX, float spawnY) {
		this(spawnX, spawnY, DEFAULT_START_X, DEFAULT_START_Y, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, 
			new Texture(Gdx.files.internal(DEFAULT_IMAGE)), 
			new Texture(Gdx.files.internal(DEFAULT_MOVE_LEFT_IMAGE)), 
			new Texture(Gdx.files.internal(DEFAULT_MOVE_RIGHT_IMAGE)));
	}

	public void setSheets() {
		//breaks sprite sheets into Animations.
		TextureRegion[] leftMove = new TextureRegion[12];
		TextureRegion[] rightMove = new TextureRegion[12];
		for (int i = 0; i < 12; i++) {
			int l_startX = 0;
			int r_endX = 0;
			if (i >= 2 && i <= 4) l_startX = -1; //idiosyncracies in this specific frame 
			if (i == 3) r_endX = 1; 
			leftMove[i] = new TextureRegion(this.image, 32 * i + DEFAULT_START_X + l_startX, DEFAULT_START_Y,
				DEFAULT_IMG_WIDTH + -1 * l_startX, DEFAULT_IMG_HEIGHT);
			rightMove[i] = new TextureRegion(this.image, 32 * i + DEFAULT_START_X + r_endX, DEFAULT_START_Y,
				DEFAULT_IMG_WIDTH + r_endX, DEFAULT_IMG_HEIGHT);
		}

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
