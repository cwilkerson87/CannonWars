package com.curtiswilkerson.cannonwars.Background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Background extends Sprite {

    private Sprite bg;
    private Texture texture;
    private World world;


    public Background(){

        texture = new Texture("nightSky.png");
        bg = new Sprite(texture);
    }

    public void update(float dt){
        bg.setPosition(0,0);
    }

    public void draw(Batch batch, float parentAlpha) {
        bg.draw(batch);

    }


    public void dispose(){
        texture.dispose();
    }

}
