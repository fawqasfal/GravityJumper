package com.mygdx.gravity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
	TextureRegion image;
	Rectangle rectRep;

	public static final int DEFAULT_IMAGE_HEIGHT = 15;

	public Platform(Texture image, float startX, float startY, int width, int height) {
		this.image = new TextureRegion(image, startX, startY, width, height);
		this.rectRep = new Rectangle(startX, startY, width, height);
	}
	public Platform(Texture image) {
		new Platform(image, 0, 0, 64, DEFAULT_IMAGE_HEIGHT);
	}
}