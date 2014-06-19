package com.mygdx.gravity;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Input.*;;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;

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

	
	public MainGameScreen(MainGravity game) {
		this.game = game;

		//sound
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		//play in show

   		camera = new OrthographicCamera();
   		camera.setToOrtho(false, w, h);
		
		
		//platforms
		platforms = new Array<Platform>();
		platforms.add(new Platform(0,0));
	}

	public float randrange(float low, float high) {
		//[low, high)
		return low + (float) Math.random() * (high - low);
	}
	
	public void render (float delta) {
		Gdx.gl.glClearColor(135f / 255f, 206f / 255f, 250f / 255f, 1); //sky blue
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(camera.combined);
 		game.batch.begin();
 			for (Platform platform : platforms) {
 				game.batch.draw(platform.getImage(), platform.getRect().x, platform.getRect().y, 
 					platform.getRect().width, platform.getRect().height);
 			}
   		game.batch.end();


   		camera.update();
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
