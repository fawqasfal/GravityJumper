package com.mygdx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class MainGravity extends Game {
	SpriteBatch batch;
	BitmapFont font;
	BitmapFont tutFont;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	
	public void create() {
		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("emulogic.ttf"));
		font = generator.generateFont(14);
		tutFont = generator.generateFont(9);
		this.setScreen(new MainMenuScreen(this));
	}
	public void render() {
		super.render();
	}

	public void dispose() {
	    batch.dispose();
	    font.dispose();
	}
}
