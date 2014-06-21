package com.mygdx.gravity;
import java.util.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;


public class Enemy {
	Rectangle rectRep;
	TextureRegion defImage;
	int damage;
	int give;
	float velocity;
	float acceleration = 50f;
	float slowdown = 0.000002f; 
	//how long have we been moving in either left/right or up/down. 
	//every time move in the same direction, add 1 (up/right), or -1 (left/down). 
	//every time change, set to 1 (up/right) or -1 (left/down)

	boolean alive; 

	public final static String DEFAULT_IMAGE = "enemy.png";
	public final static float DEFAULT_SIZE = 16;
	public final static int MIN_DMG = 6;
	public final static int MAX_DMG = 11;
	public final static int MIN_GIVE = 50;
	public final static int MAX_GIVE = 100;
	public final static float SCALE = 3.5f;
	public final static float MOVE_AMT_X = 3f;
	public final static float MOVE_AMT_Y = 2f;
	public final static int MAX_X = 0;
	public final static int MAX_Y = 0;

	public Enemy(float spawnX, float spawnY, int dmg, int give) {
		//It takes a nation of millions to hold us back.
		this.rectRep = new Rectangle(spawnX, spawnY, SCALE * DEFAULT_SIZE, SCALE * DEFAULT_SIZE);
		this.defImage = new TextureRegion(new Texture(Gdx.files.internal(DEFAULT_IMAGE)));
		this.damage = dmg;
		
		this.give = give;
		this.alive = true;
	}

	public void die() {
		this.alive = false;
		this.rectRep = null;
		this.defImage = null;
	}

	public void move(float amtX, float amtY, Hero hero) {
		//will angrily shake while trying to reach hero
		this.rectRep.x += amtX;
		this.rectRep.y += amtY;
		this.velocity += this.acceleration;
		this.rectRep.x += slowdown * Math.random() * velocity * (hero.rectRep.x - this.rectRep.x);
		this.rectRep.y += slowdown * Math.random() * velocity * (hero.rectRep.y - this.rectRep.y);

	}

	public boolean collide(Platform platform) {
		if (this.collide(platform.rectRep)) {

			return true;
		} else{
			return false;
		}
	}
	public boolean collide(Rectangle object) {
		boolean intersects = (this.rectRep.x <= object.x + object.width && this.rectRep.x + this.rectRep.width >= object.x && 
					  this.rectRep.y <= object.y + object.height && this.rectRep.y + this.rectRep.height >= object.y);
		return intersects;
	}	
}
