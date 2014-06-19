package com.mygdx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
	TextureRegion image;
	Rectangle rectRep;

	public static final int DEFAULT_IMAGE_HEIGHT = 15;
	public static final String DEFAULT_IMAGE = "platform.png";

	public Platform(Texture image, float startX, float startY, float imgX, float imgY, int width, int height) {
		this.image = new TextureRegion(image, imgX, imgY, width, height);
		this.rectRep = new Rectangle(startX, startY, width, height);
	}
	public Platform(float spawnX, float spawnY) {
		Texture defImage = new Texture(Gdx.files.internal(DEFAULT_IMAGE));
		new Platform(defImage, spawnX, spawnY, 0, 0, 64, DEFAULT_IMAGE_HEIGHT);
	}
}
