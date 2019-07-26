package com.curtiswilkerson.cannonwars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class CannonWars extends Game {

	public static final int V_WIDTH = 980;
	public static final int V_HEIGHT = 550;
	public SpriteBatch batch;


	
	@Override
	public void create () {
		batch = new SpriteBatch();

		setScreen( new MainStage(this));

	}


	@Override
	public void render () {

		super.render();

	}

	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
