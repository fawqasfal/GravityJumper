package com.mygdx.gravity;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Input.*;;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; 
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MainGameScreen extends ScreenAdapter {
	MainGravity game;
	Texture bgImage;
	Texture platform;
	Hero hero;
	Music gameMusic;
	OrthographicCamera camera;
	Array<Platform> platforms; 
	int w = MainGravity.WIDTH;
	int h = MainGravity.HEIGHT;

	long sinceLastL = 0;
	long sinceLastR = 0;
	int currLeft = -1;
	int currRight = -1; //holds indices of the textureregion[]s that we need to choose for animation of hero
	ShapeRenderer shapeRenderer; //debugging purposes
	
	public MainGameScreen(MainGravity game) {
		this.game = game; //has the spritebatch that does the drawing that we will be needing

		camera = new OrthographicCamera(); //p.o.v of game world
   		camera.setToOrtho(false, w, h); 
   		/*false just means that highest point of camera-world = h and lowest point = 0, instead of the other way around as it was
   		with TextureRegions*/

   		shapeRenderer = new ShapeRenderer(); //renders the rectangle objects as opposed to textures. for debugging
	
		//sound
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));		
		//platforms
		platforms = new Array<Platform>();
		for (int i = 0; i < w; i += Platform.DEFAULT_IMAGE_WIDTH * Platform.SCALE) {
			platforms.add(new Platform(i,0));
		}

		//hero
		hero = new Hero(0, platforms.get(0).getRect().height);
		sinceLastL = System.nanoTime();
		sinceLastR = System.nanoTime();
	}

	public float randrange(float low, float high) {
		//[low, high)
		return low + (float) Math.random() * (high - low);
	}
	
	public void render (float delta) {
		camera.update();
		Gdx.gl.glClearColor(135f / 255f, 206f / 255f, 250f / 255f, 1); //sky blue
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //something OpenGL needs to do, who knows
 		inputHandler();
		game.batch.setProjectionMatrix(camera.combined);
		TextureRegion heroTexture; 
		shapeRenderer.setProjectionMatrix(camera.combined); 
		//something for going from GPU's color-bit matrices to pixels on screen? or something
		shapeRenderer.begin(ShapeType.Line); //ShapeType.Line vs ShapeType.Fill
		shapeRenderer.setColor(1, 1, 0, 1); //yellow debug outer lines on the rects	

 		game.batch.begin();
 			for (Platform platform : platforms) 
 				render(platform.getImage(), platform.getRect().x, platform.getRect().y, 
 					platform.getRect().width, platform.getRect().height);
 			if (currLeft != -1)
 				heroTexture = hero.moveLeft[currLeft];
 			else if (currRight != -1)
 				heroTexture = hero.moveRight[currRight]; 
 			else
 				heroTexture = hero.image;
 			render(heroTexture, hero.getRect().x, hero.getRect().y, hero.getRect().width, hero.getRect().height);

   		game.batch.end();
		shapeRenderer.end();
	}

	public void render(TextureRegion image, float x, float y, float width, float height) {
		game.batch.draw(image, x, y, width, height);
		shapeRenderer.rect((x / 4), (y / 4), (width / 4), (height / 4)); //debug render is in the bottom left, at 1/8th the size
	}

	public void inputHandler() {
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
 			hero.moveLeft(300 * Gdx.graphics.getDeltaTime());
 			float slInSecs = (System.nanoTime() - sinceLastL) / (float) (Math.pow(10,9));
 			float timePerFrame = Hero.FRAMES_PER_SECOND / hero.moveLeft.length;
 			if (slInSecs > timePerFrame) {
 				currLeft = (int)(currLeft + slInSecs / timePerFrame) % 12;
 				sinceLastL = System.nanoTime();
 			}
 		} else {
 			currLeft = -1; //reset animation if hes not moving left
 			sinceLastL = 0;
 		}
   		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
 			hero.moveRight(300 * Gdx.graphics.getDeltaTime());
 			float slInSecs = (System.nanoTime() - sinceLastR) / (float) (Math.pow(10,9));
 			float timePerFrame = Hero.FRAMES_PER_SECOND / hero.moveRight.length;
 			if (slInSecs > timePerFrame) {
 				currRight = (int)(currRight + slInSecs / timePerFrame) % 12;
 				sinceLastR = System.nanoTime();
 			}
 		} else {
 			currRight= -1; //reset animation if hes not moving left
 			sinceLastR = 0;
 		}
	}
	public void dispose() {
	     game.batch.dispose();
	}
	
	public void show() {
	    // start the playback of the background music
	    // when the screen is shown
	    //gameMusic.play(); -- commented out because im listening to led zeppelin now
	}

}
