package com.mygdx.gravity;
import java.util.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;


public class Enemy {
	Rectangle rectRep;
	TextureRegion defImage;
	int damage; //what you take in $
	int give; //what you give in $
	float velocity;
	float acceleration = 50f; //the closer the hero is, the faster you move in his direction, but also...
	float slowdown = 0.000002f;  //the more you retreat in utter shaking anger

	boolean alive; 

	public final static String DEFAULT_IMAGE = "enemy.png";
	public final static float DEFAULT_SIZE = 16;
	public final static int MIN_DMG = 5;
	public final static int MAX_DMG = 10;
	public final static int MIN_GIVE = 50;
	public final static int MAX_GIVE = 100;
	public final static float SCALE = 3.5f; //scale up the 16px image to 54 * 54 on the 800*800 board
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
		this.rectRep = null; //stop colliding and physicsing!
		this.defImage = null; //stop rendering! 
	}

	public void move(float amtX, float amtY, Hero hero) {
		//will angrily shake while trying to reach hero at an accelerating speed
		this.rectRep.x += amtX;
		this.rectRep.y += amtY;
		//amtX and amtY should be random numbers in MainGameScreen.
		this.velocity += this.acceleration; 
		//more randomness -- all in the name of anger! 
		this.rectRep.x += slowdown * Math.random() * velocity * (hero.rectRep.x - this.rectRep.x); 
		this.rectRep.y += slowdown * Math.random() * velocity * (hero.rectRep.y - this.rectRep.y);

	}

	public boolean collide(Platform platform) {
		return  (this.collide(platform.rectRep));
	}
	public boolean collide(Rectangle object) {
		//this is a classic intersection formula for 2 rectangles that are at reference angles. 
		boolean intersects = (this.rectRep.x <= object.x + object.width && this.rectRep.x + this.rectRep.width >= object.x && 
					  this.rectRep.y <= object.y + object.height && this.rectRep.y + this.rectRep.height >= object.y);
		return intersects;
	}	
}
