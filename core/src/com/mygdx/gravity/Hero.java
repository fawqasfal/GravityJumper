package com.mygdx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.*;

public class Hero {
	Rectangle rectRep;
	TextureRegion[][] textureRegions; //moveLeft, moveRight, moveLeftFlipped, moveRightFlipped 
	TextureRegion currImage; 
	boolean isJumping;
	int vDirection;
	int hDirection;
	int coin = HEALTH; 
	int curr = 0;
	long sinceLast;
	float velocity = INIT_V;

	public static final int HEALTH = 1000;
	public static final int FEET_UP = 1;
	public static final int FEET_DOWN = -1;
	public static final String DEFAULT_MOVE_LEFT_IMAGE = "leftmove.png";
	public static final String DEFAULT_MOVE_RIGHT_IMAGE = "rightmove.png";
	public static final int DEFAULT_START_X = 7; //7 pixels of nothing on the 32x32
	public static final int DEFAULT_START_Y = 1; //from top-to-bottom, we want to cut off the extra pixel on top of his head
	public static final int DEFAULT_IMG_WIDTH = 18; //extra 7 pixels of nothing on the right and left : 32 - 7 * 2
	public static final int DEFAULT_IMG_HEIGHT = 31;
	public static final float SCALE = 4.5f;
	public static final float FRAMES_PER_SECOND = 0.12f;
	public static final float GRAVITY = 30f;
	public static final float INIT_V = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 0;
 	public static final int MOVE_AMT = 600;

	public Hero(float x, float y, float imgX, float imgY, float width, float height, 
				Texture moveLeftSheet, Texture moveRightSheet) {
		this.rectRep = new Rectangle(x, y, SCALE * width, SCALE * height);
		this.textureRegions = setSheets(moveRightSheet, moveLeftSheet);
		this.currImage = textureRegions[LEFT][0]; //choosing moveLeft specifically is arbitrary
		this.isJumping = false;
		this.vDirection = -1;
		this.sinceLast = System.nanoTime();
	}

	public Hero(float spawnX, float spawnY) {
		this(spawnX, spawnY, DEFAULT_START_X, DEFAULT_START_Y, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, 
			new Texture(Gdx.files.internal(DEFAULT_MOVE_LEFT_IMAGE)), 
			new Texture(Gdx.files.internal(DEFAULT_MOVE_RIGHT_IMAGE)));
	}

	public void chooseTexture() {
			this.currImage = this.textureRegions[this.vDirection + this.hDirection + 1][curr];
	}
	public void animate() {
		float slInSecs = (System.nanoTime() - sinceLast) / (float) (Math.pow(10,9));
 		float timePerFrame = Hero.FRAMES_PER_SECOND / textureRegions[LEFT].length; //specifically choosing moveleft is arbitrary
 		if (slInSecs > timePerFrame) {
 			curr = ((int)(curr + slInSecs / timePerFrame)) % textureRegions[LEFT].length;
 			sinceLast = System.nanoTime();
 		}
	}
	public void moveLeft(float amt, boolean animate) {
		this.hDirection = LEFT;
		this.rectRep.x -= amt;
		if (animate) animate();
		else {
			curr = 0;
			sinceLast = 0;
		}
	}

	public void moveRight(float amt, boolean animate) {
		//animate would be false in case of : collision with enemy, NOT colliding with platform
		this.hDirection = RIGHT;
		this.rectRep.x += amt;
		if (animate) animate();
		 else {
 			curr = 0; //reset animation 
 			sinceLast = 0;
		}
	}

	public void moveVert(float amt) {
		this.rectRep.y += amt;
	}
	public void stabilize() {
		this.curr = 0;
		this.sinceLast = 0;
	}

	//character's "jump" is really flipping up and down until it hits a platform of some sort, much like Gravity Guy
	public void jump() {
		this.isJumping = true;
		this.vDirection = -1 * vDirection;
		this.getRect().y += 20 * vDirection;
	}

	public float jumpIter(float accel) {
		if (this.isJumping) this.velocity += this.vDirection * accel;
		return this.velocity;		
	}

	public void gainCoin(int amt) {
		this.coin += amt;
	}

	public void loseCoin(int amt) {
		this.coin -= amt;
	}

	public boolean collide(Rectangle object) {
		boolean intersects = (this.rectRep.x <= object.x + object.width && this.rectRep.x + this.rectRep.width >= object.x && 
					  this.rectRep.y <= object.y + object.height && this.rectRep.y + this.rectRep.height >= object.y);
		return intersects;
	}


	public boolean collide(Enemy enemy) {
		boolean loseCoin  = true;
		if (this.collide(enemy.rectRep)) {
			if ((vDirection == FEET_UP && rectRep.y < enemy.rectRep.y) || 
				(vDirection == FEET_DOWN && enemy.rectRep.y < rectRep.y))
				loseCoin = false;
			if (loseCoin) loseCoin(enemy.damage);
			else { 
				gainCoin(enemy.give);
				this.jump();
				enemy.die();
			}

		}
		return loseCoin;
	}
 	public boolean collide(Platform platform) {
		if (this.collide(platform.rectRep)) {
			this.isJumping = false;
			if (this.vDirection == FEET_UP) {
				this.rectRep.y = platform.rectRep.y - this.rectRep.height;
			} else {
				this.rectRep.y = platform.rectRep.y + platform.rectRep.height;
			}
			this.velocity = INIT_V;
			return true;
		} else{
			return false;
		}
	}

	public Rectangle getRect() {
		//GET WRECKED! 
		return this.rectRep;
	}

	public TextureRegion[][] setSheets(Texture moveLeftSheet, Texture moveRightSheet) {
		//breaks sprite sheet textures into 2darray with flipped sheets as well

		//this whole method breaks my heart. thats why i put it at the bottom. ugly piece of shit. no one likes you. youre at the bottom,
		//like an untouchable in the ancient indian caste system.
		// i wish i had more time to implement something more generalizible.
		//but this game simply doesnt have that scope and im late as it is. 
		TextureRegion[] moveLeft = new TextureRegion[12];
		TextureRegion[] moveRight = new TextureRegion[12];
		TextureRegion[] moveLeftFlipped = new TextureRegion[12];
		TextureRegion[] moveRightFlipped = new TextureRegion[12];
		int l_startX = 0;
		int r_endX = 0; //idiosyncracies in these specific frames
		for (int i = 0; i < 12; i++) {
			if (i >= 2 && i <= 4) l_startX = -1; //idiosyncracies in this specific frame 
			if (i == 3) r_endX = 1; 
			moveLeft[i] = new TextureRegion(moveLeftSheet, 32 * i + DEFAULT_START_X + l_startX, DEFAULT_START_Y,
				DEFAULT_IMG_WIDTH + -1 * l_startX, DEFAULT_IMG_HEIGHT);

			moveLeftFlipped[i] = new TextureRegion(moveLeft[i]);
			moveLeftFlipped[i].flip(false, true);
			moveRight[i] = new TextureRegion(moveRightSheet, 32 * i + DEFAULT_START_X + r_endX, DEFAULT_START_Y,
				DEFAULT_IMG_WIDTH + r_endX, DEFAULT_IMG_HEIGHT);

			moveRightFlipped[i] = new TextureRegion(moveRight[i]);
			moveRightFlipped[i].flip(false, true);
		}

		TextureRegion[][] answer = {moveLeft, moveRight, moveLeftFlipped, moveRightFlipped}; 
		return answer;
	}
}
