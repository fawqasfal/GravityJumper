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
	public static Array<Platform> platforms; 
	int w = MainGravity.WIDTH;
	int h = MainGravity.HEIGHT;
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
			platforms.add(new Platform(i, MainGravity.HEIGHT - Platform.DEFAULT_IMAGE_HEIGHT * Platform.SCALE));
		}

		//hero
		hero = new Hero(0, platforms.get(0).getRect().height);
	}

	public void render (float delta) {
		camera.update();
		Gdx.gl.glClearColor(135f / 255f, 206f / 255f, 250f / 255f, 1); //sky blue
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //something OpenGL needs to do, who knows
 		inputHandler();
		game.batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined); 
		//something for going from GPU's color-bit matrices to pixels on screen? or something
		shapeRenderer.begin(ShapeType.Line); //ShapeType.Line vs ShapeType.Fill
		shapeRenderer.setColor(1, 1, 0, 1); //yellow debug outer lines on the rects	

		for (Platform platform : platforms)
			hero.collides(platform); //stops jumping

		if (hero.isJumping) hero.getRect().y += hero.jumpIter(Hero.GRAVITY) * Gdx.graphics.getDeltaTime();
		if (hero.getRect().y > MainGravity.HEIGHT)
			hero.getRect().setY(0);
		if (hero.getRect().y < 0)
			hero.getRect().setY(MainGravity.HEIGHT + hero.getRect().height);

 		game.batch.begin();
 			for (Platform platform : platforms) 
 				render(platform.getImage(), platform.getRect().x, platform.getRect().y, 
 					platform.getRect().width, platform.getRect().height);
 			hero.chooseTexture();
 			render(hero.currImage, hero.getRect().x, hero.getRect().y, hero.getRect().width, hero.getRect().height);

   		game.batch.end();
		shapeRenderer.end();
	}

	public void render(TextureRegion image, float x, float y, float width, float height) {
		game.batch.draw(image, x, y, width, height);
		shapeRenderer.rect((x / 4), (y / 4), (width / 4), (height / 4)); //debug render is in the bottom left, at 1/8th the size
	}

	public void inputHandler() {
		boolean animate = false;
		for (Platform platform : platforms) 
			if (hero.collides(platform)) {
				animate = true;
				break;
			}	
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
 			hero.moveLeft(Hero.MOVE_AMT * Gdx.graphics.getDeltaTime(), animate);
 			if (hero.getRect().x + hero.getRect().width < 0)
 				hero.getRect().setX(MainGravity.WIDTH);
 		}
		else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
 			hero.moveRight(Hero.MOVE_AMT * Gdx.graphics.getDeltaTime(), animate);
 			if (hero.getRect().x > MainGravity.WIDTH) 
				hero.getRect().x = -10;
 		}
 		else {
 			hero.stabilize();
 		}

 		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN)) {
 			if (!hero.isJumping) hero.jump();
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
