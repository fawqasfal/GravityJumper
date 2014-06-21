package com.mygdx.gravity.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.gravity.MainGravity;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "GRAVITY JUMP";
		config.useGL30 = true;
		config.width = MainGravity.WIDTH;
		config.height = MainGravity.HEIGHT;		
		new LwjglApplication(new MainGravity(), config);
	}
}
