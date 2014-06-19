package com.mygdx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.*;

public class Hero {
	Rectangle rectRep;
	final TextureRegion defImage;
	final TextureRegion defImageFlipped;


	TextureRegion[] moveLeft;
	TextureRegion[] moveRight;
	TextureRegion[] moveLeftFlipped;
	TextureRegion[] moveRightFlipped;

	TextureRegion currImage; 
	boolean isJumping;
	int direction;

	float velocity = INIT_V;
	public static final int FEET_UP = 1;
	public static final int FEET_DOWN = -1;
	public static final String DEFAULT_IMAGE = "stable.png";
	public static final String DEFAULT_MOVE_LEFT_IMAGE = "leftmove.png";
	public static final String DEFAULT_MOVE_RIGHT_IMAGE = "rightmove.png";
	public static final int DEFAULT_START_X = 7; //7 pixels of nothing on the 32x32
	public static final int DEFAULT_START_Y = 1; //from top-to-bottom, we want to cut off the extra pixel on top of his head
	public static final int DEFAULT_IMG_WIDTH = 18; //extra 7 pixels of nothing on the right
	public static final int DEFAULT_IMG_HEIGHT = 31;
	public static final float SCALE = 4.5f;
	public static final float FRAMES_PER_SECOND = 0.12f;
	public static final float GRAVITY = 18f;
	public static final float INIT_V = 20;
 
	public Hero(float x, float y, float imgX, float imgY, float width, float height, 
				Texture image, Texture moveLeftSheet, Texture moveRightSheet) {
		this.rectRep = new Rectangle(x, y, SCALE * width, SCALE * height);
		this.defImage = new TextureRegion(image, (int) imgX, (int) imgY, (int) width, (int) height); 
		this.defImageFlipped = new TextureRegion(this.defImage);
		this.defImageFlipped.flip(false, true);
		this.currImage = this.defImage;
		this.setSheets(moveRightSheet, moveLeftSheet);
		this.isJumping = false;
		this.direction = -1;
	}

	public Hero(float spawnX, float spawnY) {
		this(spawnX, spawnY, DEFAULT_START_X, DEFAULT_START_Y, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, 
			new Texture(Gdx.files.internal(DEFAULT_IMAGE)), 
			new Texture(Gdx.files.internal(DEFAULT_MOVE_LEFT_IMAGE)), 
			new Texture(Gdx.files.internal(DEFAULT_MOVE_RIGHT_IMAGE)));
	}

	public void setSheets(Texture moveLeftSheet, Texture moveRightSheet) {
		//breaks sprite sheets into Animations.
		this.moveLeft = new TextureRegion[12];
		this.moveRight = new TextureRegion[12];
		this.moveLeftFlipped = new TextureRegion[12];
		this.moveRightFlipped = new TextureRegion[12];
		for (int i = 0; i < 12; i++) {
			int l_startX = 0;
			int r_endX = 0;
			if (i >= 2 && i <= 4) l_startX = -1; //idiosyncracies in this specific frame 
			if (i == 3) r_endX = 1; 
			this.moveLeft[i] = new TextureRegion(moveLeftSheet, 32 * i + DEFAULT_START_X + l_startX, DEFAULT_START_Y,
				DEFAULT_IMG_WIDTH + -1 * l_startX, DEFAULT_IMG_HEIGHT);
			this.moveLeftFlipped[i] = new TextureRegion(moveLeft[i]);
			this.moveRight[i] = new TextureRegion(moveRightSheet, 32 * i + DEFAULT_START_X + r_endX, DEFAULT_START_Y,
				DEFAULT_IMG_WIDTH + r_endX, DEFAULT_IMG_HEIGHT);
			this.moveRightFlipped[i] = new TextureRegion(moveRight[i]);
		}

		for (int k = 0; k < 12; k++) {
			this.moveLeftFlipped[k].flip(false, true);
			this.moveRightFlipped[k].flip(false, true);
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
		this.getRect().y += 20 * direction;
	}

	public float jumpIter(float accel) {
		if (this.isJumping) this.velocity += accel;
		return this.velocity;		
	}

	public boolean collides(Rectangle object) {
		boolean intersects;
		Rectangle rect = this.rectRep;
		intersects = (rect.x + rect.width < object.x || object.x + object.width < rect.x 
			|| object.y + object.height < rect.y || rect.y + rect.height < object.y);
		return !intersects;
	}
 	public boolean collides(Platform platform) {
		if (this.collides(platform.rectRep)) {
			this.isJumping = false;
			this.velocity = INIT_V;
			return true;
		} else{
			return false;
		}
	}

	public boolean collides(Array<Platform> platforms) {
		for (Platform platform : platforms) {
			if (this.collides(platform)) return true;
		}
		return false;
	}
	public Rectangle getRect() {
		//GET WRECKED! 
		return this.rectRep;
	}
}
