package com.curtiswilkerson.cannonwars.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class CannonBall extends Sprite {

    private World world;
    public Body body;
    public FixtureDef fixtureDef;
    private Sprite sprite;


    float x,y;
    float stateTimer;

    Texture texture;
    public  boolean destroyed;
    public  boolean setToDestroy;


    public CannonBall(World world,float px, float py,float vx,float vy){
       this.world = world;

        texture = new Texture("ball.png");
        sprite = new Sprite(texture);
        sprite.setBounds(0,0,10,10);
        sprite.setRegion(texture);

        definedBody(px,py,vx,vy);




    }

    public void definedBody(float px, float py,float vx,float vy){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(px,py);
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(.1f);
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;
        //fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);

        body.setLinearVelocity(vx,vy);



    }

    public Vector2 getVelocity(){
        return  body.getLinearVelocity();

    }


    public Body fire(){

        return body;
    }

    public boolean isDestroy(){
       return destroyed;

    }

    public void setToDestroy(){
       setToDestroy = true;
    }



    public void update(float dt){
        stateTimer+= dt;
        sprite.setPosition(body.getPosition().x - sprite.getWidth()*.5f ,body.getPosition().y - sprite.getHeight()*.5f );
        sprite.setRegion(sprite);
        if((stateTimer > 2.5f || setToDestroy) && !destroyed) {
            world.destroyBody(body);
            destroyed = true;

        }


    }


    public void draw(SpriteBatch batch){
        sprite.draw(batch);

    }
}
