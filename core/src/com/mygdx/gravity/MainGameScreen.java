package com.mygdx.gravity;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; 
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MainGameScreen extends ScreenAdapter {
	MainGravity game; //has the spritebatch that does the drawing that we will be needing
	float time;
	Hero hero; 
	Music gameMusic; 
	OrthographicCamera camera;
	Array<Platform> platforms; 
	Array<Enemy> enemies;
	int w = MainGravity.WIDTH; //dimensions of camera. default set to the dimensions of game screen if player doesnt change it
	int h = MainGravity.HEIGHT;
	
	//debugging-renders the game's rectangles (characters without textures) with yellow lines in the bottom 1/8th of the screen  

	public static final int MAX_ENEMY_AMT = 20;
	public static final int TIME = 297;
	public static final float ENEMY_DENSITY_RATE = 0.4f;
	public static final float LEAST_Y = Platform.DEFAULT_IMAGE_HEIGHT * Platform.SCALE; 
	public static final float MOST_Y  = MainGravity.HEIGHT - LEAST_Y; 
	public static final float DEBUG_SIZE_CONSTANT = 0.25f;
	public MainGameScreen(MainGravity game) {
		this.game = game; 
		camera = new OrthographicCamera(); 
   		camera.setToOrtho(false, w, h); 
   		/*false just means render the game world as if the bottom of the screen was 0 and the top of the screen was height, 
   		as opposed to top-down, which is how Rectangles work*/

		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3")); //Music objects auto-loop, as opposed to Sound objects
		platforms = new Array<Platform>(); //Array is libgdx's "better" ArrayList i guess thats what they say
		platformSpawner();
		enemies = new Array<Enemy>();
		hero = new Hero((MainGravity.WIDTH - Hero.DEFAULT_IMG_WIDTH * Hero.SCALE) / 2, 
						Platform.DEFAULT_IMAGE_HEIGHT * Platform.SCALE); //spawns hero in the middle on top of a platform
		//SCALE because DEFAULT_IMAGE_WIDTH deals with the 16x16 sprite sheet, but we want player to see something a bit bigger
		time = TIME;
	}

	public void platformSpawner() {
		for (int i = 0; i < w; i += Platform.DEFAULT_IMAGE_WIDTH * Platform.SCALE) { 
			platforms.add(new Platform(i,0));
			platforms.add(new Platform(i, MainGravity.HEIGHT - Platform.DEFAULT_IMAGE_HEIGHT * Platform.SCALE)); //top of screen
		}
	}

	public void birthEnemy(float leastX, float mostX, float leastY, float mostY) {
		float spawnX = leastX + new Random().nextFloat() * (mostX - leastX);
		float spawnY = leastY + new Random().nextFloat() * (mostY - leastY);

		int damage = Enemy.MIN_DMG + new Random().nextInt(Enemy.MAX_DMG - Enemy.MIN_DMG);
		int give = Enemy.MIN_GIVE + new Random().nextInt(Enemy.MAX_GIVE - Enemy.MIN_GIVE);
	
		Enemy newBaby = new Enemy(spawnX, spawnY, damage, give);

		enemies.add(newBaby);
	}

	public void enemySpawner() {
		if (enemies.size < MAX_ENEMY_AMT && Math.random() < ENEMY_DENSITY_RATE)
			birthEnemy(0, MainGravity.WIDTH, LEAST_Y, MOST_Y);
	}

	public void handleCollisions() {
		 for (Platform platform : platforms)
			hero.collide(platform); //stops jumping
		for (Enemy enemy : enemies) {
				hero.collide(enemy); //gets or loses health
				if (!enemy.alive)
					enemies.removeValue(enemy, true);
		}
	}

	public void tooFarCheck() {
		//if objects moves too far right, loop him back to the left side of screen. same with up/down
		for (Enemy enemy : enemies) {
				tooFarCheck(enemy.rectRep);
		}
		tooFarCheck(hero.rectRep);
	}

	public void tooFarCheck(Rectangle x) {
		if (x.y >= MainGravity.HEIGHT)
			x.y = 0;
		if (x.y < 0)
			x.y = MainGravity.HEIGHT;
		if (x.x > MainGravity.WIDTH)
			x.x = 0; 
		if (x.x + x.width < 0)
			x.x = MainGravity.WIDTH; //offset to make it look prettier
	}

	public void renderSetup() {
		camera.update(); //every frame
		Gdx.gl.glClearColor(135f / 255f, 206f / 255f, 250f / 255f, 1); //sky blue
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //something OpenGL needs to do, who knows
		game.batch.setProjectionMatrix(camera.combined); //something for going from GPU's color-bit matrices to pixels on screen?
	}

	public void renderObjects() {
 		game.batch.begin();
 			for (Platform platform : platforms) 
 				render(platform.getImage(), platform.getRect().x, platform.getRect().y, 
 					platform.getRect().width, platform.getRect().height);
 			
 			hero.chooseTexture();
 			render(hero.currImage, hero.getRect().x, hero.getRect().y, hero.getRect().width, hero.getRect().height);
 			for (Enemy enemy : enemies)
 					render(enemy.defImage, enemy.rectRep.x, enemy.rectRep.y, enemy.rectRep.width, enemy.rectRep.height);
 			String money = new String();
 			if  (hero.coin < 0) money = "(DEBT!)";
 			game.font.draw(game.batch,"MONEY : " + hero.coin + money, 0f, MainGravity.HEIGHT - 50f);
 			game.font.draw(game.batch, "TIME : " + String.format("%.1f",time), 0f, MainGravity.HEIGHT - 100f);
   		game.batch.end();

	}

	public void render(float delta) {
		updateTime();
		inputHandler();
		enemySpawner();
		handleMovement();
		handleCollisions();
		tooFarCheck();
		renderSetup();
		renderObjects();
	}

	public void updateTime() {
		time -= Gdx.graphics.getDeltaTime();
		if (time <= 0) {
			gameMusic.stop();
			game.setScreen(new GameOverScreen(game, hero.coin));
		}
	}
	public void render(TextureRegion image, float x, float y, float width, float height) {
		//renders to spritebatch and debugger 
		game.batch.draw(image, x, y, width, height);
	}

	public boolean should_animate() {
		boolean animate = false;
		for (Platform platform : platforms) 
			if (hero.collide(platform)) {
				animate = true;
				break;
			}
		return animate;
	}
	public void handleMovement() {
		if (hero.isJumping) 
			hero.moveVert(hero.jumpIter(Hero.GRAVITY) * Gdx.graphics.getDeltaTime()); //time since last frame
		for (Enemy enemy : enemies) {
			enemy.move((-0.5f + (float)Math.random()) * 20f, (-0.5f + (float)Math.random()) * 10f, hero);

		}
	}
	public void inputHandler() {	
		boolean animate = should_animate();
		if(Gdx.input.isKeyPressed(Keys.A)) 
 			hero.moveLeft(Hero.MOVE_AMT * Gdx.graphics.getDeltaTime(), animate);
		else if(Gdx.input.isKeyPressed(Keys.D)) 
 			hero.moveRight(Hero.MOVE_AMT * Gdx.graphics.getDeltaTime(), animate);
 		else 
 			hero.stabilize();

 		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)) 
 			if (!hero.isJumping) hero.jump();
 		
	}
	public void dispose() {
	     game.batch.dispose();
	}
	
	public void show() {
	    // start the playback of the background music
	    // when the screen is shown
	    gameMusic.play(); 
	    gameMusic.setLooping(true);
	}

}
