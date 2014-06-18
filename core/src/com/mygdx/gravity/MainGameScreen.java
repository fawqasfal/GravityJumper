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
	OrthographicCamera camera;
	int w = MainGravity.WIDTH;
	int h = MainGravity.HEIGHT;

	
	public MainGameScreen(MainGravity game) {
		this.game = game;

		//images

		//sound
		//play in show

   		camera = new OrthographicCamera();
   		camera.setToOrtho(false, w, h);
		
		//hero

		//platforms
	}

	public float randrange(float low, float high) {
		//[low, high)
		return low + (float) Math.random() * (high - low);
	}
	
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(camera.combined);
 		game.batch.begin();
   		game.batch.end();
   		camera.update();
	}


	public void dispose() {
	     game.batch.dispose();
	}
	

}
