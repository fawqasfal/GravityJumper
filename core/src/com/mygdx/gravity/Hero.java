package com.mygdx.gravity;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Input.*;;


public class Hero {
	Rectangle rectRep;
	Texture image;
	boolean isJumping;
	int direction;

	public Hero(float x, float y, int width, int height, String imgName) {
		this.rectRep = new Rectangle();
		this.rectRep.width = width;
		this.rectRep.height = height;
		this.rectRep.x = x;
		this.rectRep.y =y;
		image = new Texture(imgName);
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
		this.rectRep.y += direction * amt;
		
	}

	public boolean collides(Rectangle object) {
		if (this.rectRep.overlaps(object)) {
			isJumping = false;
			return true;
		} else{
			return false;
		}
	}
}