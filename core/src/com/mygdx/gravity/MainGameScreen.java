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
		platforms.add(new Platform(0,0));
		//hero
		hero = new Hero(0, platforms.get(0).getRect().height);
	}

	public float randrange(float low, float high) {
		//[low, high)
		return low + (float) Math.random() * (high - low);
	}
	
	public void render (float delta) {
		camera.update();
		Gdx.gl.glClearColor(135f / 255f, 206f / 255f, 250f / 255f, 1); //sky blue
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //something OpenGL needs to do, who knows
		
		game.batch.setProjectionMatrix(camera.combined);

		shapeRenderer.setProjectionMatrix(camera.combined); 
		//something for going from GPU's color-bit matrices to pixels on screen? or something
		shapeRenderer.begin(ShapeType.Line); //ShapeType.Line vs ShapeType.Fill
		shapeRenderer.setColor(1, 1, 0, 1); //yellow debug outer lines on the rects	

 		game.batch.begin();
 			for (Platform platform : platforms) 
 				render(platform.getImage(), platform.getRect().x, platform.getRect().y, 
 					platform.getRect().width, platform.getRect().height);
 			render(hero.image, hero.getRect().x, hero.getRect().y, hero.getRect().width, hero.getRect().height);

 		if(Gdx.input.isKeyPressed(Keys.LEFT)) hero.moveLeft(300 * Gdx.graphics.getDeltaTime());
   		if(Gdx.input.isKeyPressed(Keys.RIGHT)) hero.moveRight(300 * Gdx.graphics.getDeltaTime());
   		
   		game.batch.end();
		shapeRenderer.end();
	}

	public void render(TextureRegion image, float x, float y, float width, float height) {
		game.batch.draw(image, x, y, width, height);
		shapeRenderer.rect((x), (y), (width), (height)); //debug render is in the bottom left, at 1/8th the size
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
