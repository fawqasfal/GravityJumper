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
	TextureRegion[] punchLeft;
	TextureRegion[] punchRight;
	TextureRegion[] punchLeftFlipped;
	TextureRegion[] punchRightFlipped;

	TextureRegion currImage; 
	boolean isPunchingLeft = false;
	boolean isPunchingRight = false;
	boolean isJumping;
	int direction;

	float velocity = INIT_V;
	public static final int FEET_UP = 1;
	public static final int FEET_DOWN = -1;
	public static final String DEFAULT_IMAGE = "stable.png";
	public static final String DEFAULT_MOVE_LEFT_IMAGE = "leftmove.png";
	public static final String DEFAULT_MOVE_RIGHT_IMAGE = "rightmove.png";
	public static final String DEFAULT_PUNCH_LEFT_SHEET = "leftpunch.png";
	public static final String DEFAULT_PUNCH_RIGHT_SHEET = "rightpunch.png";
	public static final int DEFAULT_START_X = 7; //7 pixels of nothing on the 32x32
	public static final int DEFAULT_START_Y = 1; //from top-to-bottom, we want to cut off the extra pixel on top of his head
	public static final int DEFAULT_IMG_WIDTH = 18; //extra 7 pixels of nothing on the right
	public static final int DEFAULT_IMG_HEIGHT = 31;
	public static final float SCALE = 4.5f;
	public static final float FRAMES_PER_SECOND = 0.12f;
	public static final float FRAMES_PER_SECOND_PUNCH = 0.12f;
	public static final float GRAVITY = 18f;
	public static final float INIT_V = 30;
 
	public Hero(float x, float y, float imgX, float imgY, float width, float height, 
				Texture image, Texture moveLeftSheet, Texture moveRightSheet,
				Texture punchLeftSheet, Texture punchRightSheet) {
		this.rectRep = new Rectangle(x, y, SCALE * width, SCALE * height);
		this.defImage = new TextureRegion(image, (int) imgX, (int) imgY, (int) width, (int) height); 
		this.defImageFlipped = new TextureRegion(this.defImage);
		this.defImageFlipped.flip(false, true);
		this.currImage = this.defImage;
		this.setSheets(moveRightSheet, moveLeftSheet, punchLeftSheet, punchRightSheet);
		this.isJumping = false;
		this.direction = -1;
	}

	public Hero(float spawnX, float spawnY) {
		this(spawnX, spawnY, DEFAULT_START_X, DEFAULT_START_Y, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, 
			new Texture(Gdx.files.internal(DEFAULT_IMAGE)), 
			new Texture(Gdx.files.internal(DEFAULT_MOVE_LEFT_IMAGE)), 
			new Texture(Gdx.files.internal(DEFAULT_MOVE_RIGHT_IMAGE)),
			new Texture(Gdx.files.internal(DEFAULT_PUNCH_LEFT_SHEET)),
			new Texture(Gdx.files.internal(DEFAULT_PUNCH_RIGHT_SHEET)));
	}

	public void setSheets(Texture moveLeftSheet, Texture moveRightSheet, Texture leftPunchSheet, Texture rightPunchSheet) {
		setMoveSheets(moveLeftSheet, moveRightSheet);
		setPunchSheets(leftPunchSheet, rightPunchSheet);
	}

	public void setPunchSheets(Texture leftPunchSheet, Texture rightPunchSheet) {
		this.punchLeft = new TextureRegion[9];
		this.punchRight = new TextureRegion[9];
		this.punchLeftFlipped = new TextureRegion[9];
		this.punchRightFlipped = new TextureRegion[9];
		for (int i = 0; i < 9; i++) {
			int punch = 0;
			if (i >= 5) punch = i;
			this.punchLeft[i] = new TextureRegion(leftPunchSheet, 32 * i + DEFAULT_START_X + -1 * punch, DEFAULT_START_Y, 
				DEFAULT_IMG_WIDTH + punch, DEFAULT_IMG_HEIGHT);
			this.punchLeftFlipped[i] = this.punchLeft[i];
			this.punchLeftFlipped[i].flip(false, true);
			this.punchRight[i] = new TextureRegion(rightPunchSheet, 32 * i + DEFAULT_START_X, DEFAULT_START_Y,
				DEFAULT_IMG_WIDTH + punch, DEFAULT_IMG_HEIGHT);
			this.punchRightFlipped[i] = this.punchRight[i];
			this.punchRightFlipped[i].flip(false, true);
		}
	}
	public void setMoveSheets(Texture moveLeftSheet, Texture moveRightSheet) {
		//breaks sprite sheets into Animations.
		this.moveLeft = new TextureRegion[12];
		this.moveRight = new TextureRegion[12];
		this.moveLeftFlipped = new TextureRegion[12];
		this.moveRightFlipped = new TextureRegion[12];
		for (int i = 0; i < 12; i++) {
			int l_startX = 0;
			int r_endX = 0;
			int punch = 0; 
			if (i >= 2 && i <= 4) l_startX = -1; //idiosyncracies in this specific frame 
			if (i == 3) r_endX = 1; 
			this.moveLeft[i] = new TextureRegion(moveLeftSheet, 32 * i + DEFAULT_START_X + l_startX, DEFAULT_START_Y,
				DEFAULT_IMG_WIDTH + -1 * l_startX, DEFAULT_IMG_HEIGHT);
			this.moveLeftFlipped[i] = new TextureRegion(moveLeft[i]);
			this.moveLeftFlipped[i].flip(false, true);
			this.moveRight[i] = new TextureRegion(moveRightSheet, 32 * i + DEFAULT_START_X + r_endX, DEFAULT_START_Y,
				DEFAULT_IMG_WIDTH + r_endX, DEFAULT_IMG_HEIGHT);
			this.moveRightFlipped[i] = new TextureRegion(moveRight[i]);
			this.moveRightFlipped[i].flip(false, true);

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

	public void punchLeft() {
		this.isPunchingLeft = !this.isPunchingLeft; 
	}

	public void punchRight() {
		this.isPunchingRight = !this.isPunchingRight;
	}

	public void punchLeftIter(int currLeftIndex, boolean flipped) {
		TextureRegion[] animation;
		if (!flipped) 
			animation = this.punchLeft;
		else
			animation = this.punchLeftFlipped;
		this.rectRep = new Rectangle(animation[currLeftIndex].getRegionX(), animation[currLeftIndex].getRegionY(),
			animation[currLeftIndex].getRegionWidth(), animation[currLeftIndex].getRegionHeight());
	}

	public void punchRightIter(int currRightIndex, boolean flipped) {
		TextureRegion[] animation;
		if (!flipped) 
			animation = this.punchRight;
		else
			animation = this.punchRightFlipped;
		this.rectRep = new Rectangle(animation[currRightIndex].getRegionX(), animation[currRightIndex].getRegionY(),
			SCALE * animation[currRightIndex].getRegionWidth(), SCALE * animation[currRightIndex].getRegionHeight());
	}
	public float jumpIter(float accel) {
		this.velocity += accel;
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
