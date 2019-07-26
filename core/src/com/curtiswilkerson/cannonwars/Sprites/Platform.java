package com.curtiswilkerson.cannonwars.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Platform extends Sprite {

    private Texture texture;
    private Sprite platformSprite;
    private World world;

    public Body body;


    public Platform(World world,float x, float y){

        this.world = world;
        texture = new Texture("platform.png");
        platformSprite = new Sprite(texture);
        platformSprite.setBounds(0,0,15 ,3);
        defined(x,y);

    }

    public void defined(float x,float y){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(x,y);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();


        shape.setAsBox((platformSprite.getWidth()/2) , platformSprite.getHeight()/2);
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        PolygonShape boundaries = new PolygonShape();
        shape.setAsBox(platformSprite.getWidth()/2,platformSprite.getHeight()/2);

        body.createFixture(fixtureDef).setUserData(this);

    }


    public void draw(SpriteBatch batch){
        platformSprite.draw(batch);
    }


      public void update(float dt){
            platformSprite.setPosition(body.getPosition().x - platformSprite.getWidth()*.5f,
                    body.getPosition().y - platformSprite.getHeight()*.5f);
      }

      public float getPositionX(){

        System.out.println(platformSprite.getX());
       return platformSprite.getX();

      }

      public Sprite getPlatform(){
        return platformSprite;
      }


    public void dispose(){
        texture.dispose();
        world.dispose();

    }

}
