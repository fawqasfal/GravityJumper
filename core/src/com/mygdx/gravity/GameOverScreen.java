package com.mygdx.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Input.*;
public class GameOverScreen extends ScreenAdapter {
	final MainGravity game;
	OrthographicCamera camera;
    	static int w = MainGravity.WIDTH;
    	static int h = MainGravity.HEIGHT;
        int money;
	public GameOverScreen(MainGravity game, int money) {
		this.game = game;
        this.money = money;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, String.format("YOU DIED WITH %d DOLLARS.", this.money), 
            (w / 2) - 150,
            (h / 2));
        game.font.draw(game.batch, howRich(), (w / 2) - 150, (h / 2) - 20);
        game.batch.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.ENTER)) {
            Gdx.app.exit();
        }
	}
    public String howRich() {
        if (this.money < 0) return "IN DEBT! LOSER.";
        else if (this.money < 1000) return "PENNILESS HOBO.";
        else if (this.money < 2000) return "AVERAGE MIDDLE CLASS GOON.";
        else if (this.money < 4000) return "POSH TWAT!";
        else return "YOU TRIED TOO HARD.";
    }
}
