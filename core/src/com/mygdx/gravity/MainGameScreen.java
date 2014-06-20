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

	long sinceLastL = 0;
	long sinceLastR = 0;
	long sinceLastPL = 0;
	long sinceLastPR = 0;
	int currLeft = -1;
	int currRight = -1; //holds indices of the textureregion[]s that we need to choose for animation of hero
	int punchLeft = 0;
	int punchRight = 0;
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
		shapeRenderer.setProjectionMatrix(camera.combined); 
		//something for going from GPU's color-bit matrices to pixels on screen? or something
		shapeRenderer.begin(ShapeType.Line); //ShapeType.Line vs ShapeType.Fill
		shapeRenderer.setColor(1, 1, 0, 1); //yellow debug outer lines on the rects	
		if (hero.isPunchingRight) {
 				float slprInSecs = (System.nanoTime() - sinceLastPR) / (float) (Math.pow(10,9));
 				float timePerFrame = Hero.FRAMES_PER_SECOND_PUNCH / hero.punchRight.length;
 				if (slprInSecs > timePerFrame) {
 					punchRight = (int) (punchRight + slprInSecs / timePerFrame);
 					sinceLastPR = System.nanoTime();
 					if (punchRight > hero.punchRight.length) {
 						punchRight = 0;
 						//hero.isPunchingRight = false;
 						sinceLastPR = 0;
					}
 				}
 			}
 			else if (hero.isPunchingLeft) {
 				hero.isPunchingLeft = true;
 				float slplInSecs = (System.nanoTime() - sinceLastPL) / (float) (Math.pow(10,9));
 				float timePerFrame = Hero.FRAMES_PER_SECOND_PUNCH / hero.punchLeft.length;
 				if (slplInSecs > timePerFrame) {
 					punchLeft = (int) (punchLeft + slplInSecs / timePerFrame);
 					sinceLastPL = System.nanoTime();
 					if (punchLeft > hero.punchLeft.length) {
 						punchLeft = 0;
 						//hero.isPunchingLeft = false;
 						sinceLastPL = 0;
					}
 				}
 			}
		hero.collides(platforms); //stops jumping
		if (hero.isJumping) hero.getRect().y += hero.direction * hero.jumpIter(Hero.GRAVITY) * Gdx.graphics.getDeltaTime();
		if (hero.getRect().y > MainGravity.HEIGHT)
			hero.getRect().setY(0 - MainGravity.HEIGHT);
		if (hero.getRect().y < 0)
			hero.getRect().setY(MainGravity.HEIGHT + hero.getRect().height);

 		game.batch.begin();
 			for (Platform platform : platforms) 
 				render(platform.getImage(), platform.getRect().x, platform.getRect().y, 
 					platform.getRect().width, platform.getRect().height);
 			chooseTexture();
 			render(hero.currImage, hero.getRect().x, hero.getRect().y, hero.getRect().width, hero.getRect().height);

   		game.batch.end();
		shapeRenderer.end();
	}

	public void render(TextureRegion image, float x, float y, float width, float height) {
		game.batch.draw(image, x, y, width, height);
		shapeRenderer.rect((x / 4), (y / 4), (width / 4), (height / 4)); //debug render is in the bottom left, at 1/8th the size
	}

	public void chooseTexture() {
		if (hero.isPunchingRight || hero.isPunchingLeft) {
			if (hero.isPunchingLeft)
				if (hero.direction == Hero.FEET_UP) {
					hero.currImage = hero.punchLeftFlipped[punchLeft];
					hero.punchLeftIter(punchLeft, true);
				}
				else {
					hero.currImage = hero.punchLeft[punchLeft];
					hero.punchLeftIter(punchLeft, false);
				}
			else
				if (hero.direction == Hero.FEET_UP) {
					hero.currImage = hero.punchRightFlipped[punchRight];
					hero.punchRightIter(punchRight, true);
				}
				else {
					hero.currImage = hero.punchRightFlipped[punchRight]; 
					hero.punchRightIter(punchRight, false);
				}
			return;
		}
		if (currLeft != -1)
			if (hero.direction == Hero.FEET_UP)
				hero.currImage = hero.moveLeftFlipped[currLeft];
			else
				hero.currImage = hero.moveLeft[currLeft];
		else if (currRight != -1)
			if (hero.direction == Hero.FEET_UP)
				hero.currImage = hero.moveRightFlipped[currRight];
			else
				hero.currImage = hero.moveRight[currRight];
		else
			if (hero.direction == Hero.FEET_UP)
				hero.currImage = hero.defImageFlipped;
			else
				hero.currImage = hero.defImage;
	}

	public void inputHandler() {
		if (hero.isPunchingLeft || hero.isPunchingRight) return;
		if(Gdx.input.isKeyPressed(Keys.A)) {
 			hero.moveLeft(300 * Gdx.graphics.getDeltaTime());
 			if (hero.getRect().x + hero.getRect().width < 0)
 				hero.getRect().setX(MainGravity.WIDTH);
 			if (hero.collides(platforms)) {
 				float slInSecs = (System.nanoTime() - sinceLastL) / (float) (Math.pow(10,9));
 				float timePerFrame = Hero.FRAMES_PER_SECOND / hero.moveLeft.length;
 				if (slInSecs > timePerFrame) {
 					currLeft = (int)(currLeft + slInSecs / timePerFrame) % 12;
 					sinceLastL = System.nanoTime();
 				}
 			}
 		} else {
 			currLeft = -1; //reset animation if hes not moving left
 			sinceLastL = 0;
 		}
   		if(Gdx.input.isKeyPressed(Keys.D)) {
 			hero.moveRight(300 * Gdx.graphics.getDeltaTime());
 			if (hero.getRect().x > MainGravity.WIDTH) 
 				hero.getRect().x = -10;
 			if (hero.collides(platforms)) {
 				float slInSecs = (System.nanoTime() - sinceLastR) / (float) (Math.pow(10,9));
 				float timePerFrame = Hero.FRAMES_PER_SECOND / hero.moveRight.length;
 				if (slInSecs > timePerFrame) {
 					currRight = (int)(currRight + slInSecs / timePerFrame) % 12;
 					sinceLastR = System.nanoTime();
 				}
 			}
 		} else {
 			currRight= -1; //reset animation if hes not moving left
 			sinceLastR = 0;
 		}

 		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)) {
 			if (!hero.isJumping) hero.jump();
 		}
 		if ((Gdx.input.isKeyPressed(Keys.K) || Gdx.input.isKeyPressed(Keys.J)) &&
 			!Gdx.input.isKeyPressed(Keys.W) &&
 			!Gdx.input.isKeyPressed(Keys.S) &&
 			!Gdx.input.isKeyPressed(Keys.A) && 
 			!Gdx.input.isKeyPressed(Keys.D)) {
 			System.out.println("we get here");
 			if (Gdx.input.isKeyPressed(Keys.K))
 				hero.isPunchingRight = true;
 			else
 				hero.isPunchingLeft = true;
 		}

 		if (hero.isPunchingLeft) {
 			hero.isPunchingRight = false; 
 		} else if (hero.isPunchingRight) {
 			hero.isPunchingLeft = false;
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
