package com.curtiswilkerson.cannonwars.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.curtiswilkerson.cannonwars.CannonWars;
import com.curtiswilkerson.cannonwars.MainStage;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = CannonWars.V_WIDTH;
		config.height = CannonWars.V_HEIGHT;
		new LwjglApplication(new CannonWars(), config);
	}
}
