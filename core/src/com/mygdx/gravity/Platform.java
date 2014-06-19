package com.mygdx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
	TextureRegion image;
	Rectangle rectRep;

	public static final int DEFAULT_IMAGE_HEIGHT = 5;
	public static final String DEFAULT_IMAGE = "platform.png";
	public static final float SCALE = 5f;
	public Platform(Texture image, float startX, float startY, int width, int height) {
		this.image = new TextureRegion(image, 0, width - height, width, height);
		/*why width - height? Assuming default image is a power-of-two, this is imgHeight - actualHeightOfObject.
		actualHeightOfObject is different from imgHeight because the platform is not a power of two, meaning there is 
		going to be some blank space at the top that we're going to get rid of. */
		this.rectRep = new Rectangle(startX, startY, width * SCALE, height * SCALE);
	}
	public Platform(float spawnX, float spawnY) {
		this(new Texture(Gdx.files.internal(DEFAULT_IMAGE)), spawnX, spawnY, 64, DEFAULT_IMAGE_HEIGHT);
	}

	public TextureRegion getImage() {
		return this.image;
	}

	public Rectangle getRect() {
		return this.rectRep;
	}


}
