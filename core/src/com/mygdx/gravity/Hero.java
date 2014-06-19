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
	public static final float DEFAULT_START_X = 7;
	public static final float DEFAULT_START_Y = 0;
	public static final int DEFAULT_IMG_WIDTH = 18;
	public static final int DEFAULT_IMG_HEIGHT = 31;

	public Hero(float x, float y, float imgX, float imgY, int width, int height, Texture image) {
		this.rectRep = new Rectangle(x, y, width, height);
		this.image = new TextureRegion(image, imgX, imgY, width, height);
	}

	public Hero(float spawnX, float spawnY) {
		Texture imgTexture = new Texture(Gdx.files.internal(DEFAULT_IMAGE));
		new Hero(spawnX, spawnY, DEFAULT_START_X, DEFAULT_START_Y, DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, imgTexture);
	}
	public void moveLeft(int amt) {
		this.rectRep.x -= amt;
	}

	public void moveRight(int amt) {
		this.rectRep.x += amt;
	}

	//character's "jump" is really flipping up and down until it hits a platform of some sort, much like Gravity Guy
	public void jump() {
		this.isJumping = true;
		this.direction = -1 * direction;
	}

	public void jumpIter(int amt) {
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
}
