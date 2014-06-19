package com.mygdx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
	TextureRegion image;
	Rectangle rectRep;

	public static final int DEFAULT_IMAGE_HEIGHT = 5;
	public static final int DEFAULT_IMAGE_WIDTH = 17;
	public static final String DEFAULT_IMAGE = "platform.png";
	public static final float SCALE = 5f;
	public Platform(Texture image, float startX, float startY, int width, int height) {
		this.image = new TextureRegion(image, 0, 64 - height, width, height); 
		/*given platform image is 64x64. you may ask why i didnt make it 16x16 or at least 32x32, this is because
		  originally the platform was much more intricate but it was also really ugly because it turns out I don't have
		  the artistic skills to be that indulgent. then i was too lazy to re-open the pixel art editor and remake the image
		  in smaller form.*/ 
		this.rectRep = new Rectangle(startX, startY, width * SCALE, height * SCALE);
	}
	public Platform(float spawnX, float spawnY) {
		this(new Texture(Gdx.files.internal(DEFAULT_IMAGE)), spawnX, spawnY, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
	}

	public TextureRegion getImage() {
		return this.image;
	}

	public Rectangle getRect() {
		return this.rectRep;
	}


}
