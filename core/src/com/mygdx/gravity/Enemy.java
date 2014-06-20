package com.mygdx.gravity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;


public class Enemy {
	Rectangle rectRep;
	TextureRegion defImage;
	int damage;
	int give;
	boolean alive; 

	public final static String DEFAULT_IMAGE = "enemy.png";
	public final static float DEFAULT_SIZE = 16;
	public final static float SCALE = 4.5f;
	public final static int MOVE_AMT = 100;
	public Enemy(float spawnX, float spawnY, int dmg, int give) {
		//It takes a nation of millions to hold us back.
		this.rectRep = new Rectangle(spawnX, spawnY, SCALE * DEFAULT_SIZE, SCALE * DEFAULT_SIZE);
		this.defImage = new TextureRegion(new Texture(Gdx.files.internal(DEFAULT_IMAGE)));
		this.damage = damage;
		this.give = give;
		this.alive = true;
	}

	public void die() {
		this.alive = false;
		this.rectRep = null;
		this.defImage = null;
	}

	public void floatLeft(int amt) {
		this.rectRep.x -= amt;
		floatY(amt);	
	}

	public void floatRight(int amt) {
		this.rectRep.x += amt;
		floatY(amt);
	}

	public void floatY(int amt) {
		int direction = randrange(-1,2);
		this.rectRep.y += direction * Math.log(amt) / Math.log(2);
	}

	public int randrange(int low, int high) {
		//[low, high)
		return low + (int) Math.floor(Math.random() * (high - low));
	}
	
}