package com.curtiswilkerson.cannonwars.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Cannon extends Sprite implements InputProcessor {



    public enum State {ROLLING,TILT1,TILT2,STILL,STRAIGHTSHOT,MIDSHOT,HIGHTSHOT,REVERSEROLL};
    public State current;
    public State previous;
    public World world;
    public Body body;
    private TextureRegion texture;
    private TextureRegion cannonStill;
    private Sprite cannonSprite;
    private TextureRegion straightShot;
    private TextureRegion midShot;
    private TextureRegion highShot;
    private TextureRegion tilt;
    private float stateTimer;


    private Animation cannonRoll;
    private Animation cannonRollReverse;
    private Animation tiltOne;
    private Animation tiltTwo;
    private Animation tiltFireOne;
    private Animation tiltFireTwo;
    private Animation fireStraight;

    private TextureAtlas atlas;
    private TextureRegion region;


    Array<TextureAtlas.AtlasRegion> regions;
    Array<TextureAtlas.AtlasRegion> tilt1Region;
    Array<TextureAtlas.AtlasRegion> tilt2Region;
    Array<TextureAtlas.AtlasRegion> tilt1FireRegion;
    Array<TextureAtlas.AtlasRegion> tilt2FireRegion;
    Array<TextureAtlas.AtlasRegion> fireStraightRegion;

    private Array<CannonBall> cannonBall;


    public boolean regionHigh = false;
    public boolean regionMid = false;
    public boolean regionStraight = false;





    public Cannon(World world, float x, float y){

        this.world = world;
        current = State.STILL;
        previous = State.STILL;

         atlas = new TextureAtlas("Cannon.pack");

        //Animations && Textures
        highShot = new TextureRegion(atlas.findRegion("CannonHighShot/Action"));
        straightShot = new TextureRegion(atlas.findRegion("CannonStraight/Action"));
        midShot = new TextureRegion(atlas.findRegion("CannonMidShot/Action"));
        TextureRegion texture = new TextureRegion(atlas.findRegion("CannonStraight/Action"));
        tilt = new TextureRegion(atlas.findRegion("CannonTilt_1/Action",2));
        cannonStill = new TextureRegion(atlas.findRegion("CannonStraight/Action"));
        cannonSprite = new Sprite(cannonStill);
        cannonSprite.setBounds(0,0,10.0f,10.0f);

        regions = atlas.findRegions("CannonRoll/Action");
        cannonRoll = new Animation(1f/4f,regions);

        regions = atlas.findRegions("CannonRoll/Action");
        cannonRollReverse = new Animation(1f/4f,regions, Animation.PlayMode.LOOP_REVERSED);

        tilt1Region = atlas.findRegions("CannonTilt_0/Action");
        tilt1Region.removeIndex(0);
        tiltOne = new Animation(1f/1f,tilt1Region);

        tilt2Region = atlas.findRegions("CannonTilt_1/Action");
        tilt2Region.removeIndex(0);
        tiltTwo = new Animation(1f/1f,tilt2Region);

        fireStraightRegion = atlas.findRegions("CannonStraight/Action");
        fireStraight = new Animation(1f/12f,fireStraightRegion);

        tilt1FireRegion = atlas.findRegions("CannonMidShot/Action");
        tiltFireOne = new Animation(1f/12f,tilt1FireRegion);

        tilt2FireRegion = atlas.findRegions("CannonHighShot/Action");
        tiltFireTwo = new Animation(1f/12f,tilt2FireRegion);
        cannonBall = new Array<CannonBall>();


        defined(x,y);
        setRegion(cannonStill);

       Gdx.input.setInputProcessor(this);

    }


    //Create Body and Fixture
    public void defined(float x, float y){


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox((cannonSprite.getWidth()-5) /2 , (cannonSprite.getHeight()-7)/3);
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;

        body.createFixture(fixtureDef).setUserData(this);
    }

public void draw(SpriteBatch batch){
       cannonSprite.draw(batch);

       for(CannonBall ball : cannonBall)
            ball.draw(batch);
}



    //Updates
    public void update(float dt){
        stateTimer+= Gdx.graphics.getDeltaTime();
        cannonSprite.setPosition(body.getPosition().x -cannonSprite.getWidth()*.5f ,
                body.getPosition().y - cannonSprite.getHeight()+(16.3f *.5f ));
        cannonSprite.setRegion(getFrame(dt));




        for(CannonBall ball: cannonBall){
            ball.update(dt);
            if(ball.destroyed)
                cannonBall.removeValue(ball, true);
        }
    }



    public TextureRegion getFrame(float dt) {
        current = getState();


        switch (current) {

            case ROLLING:
                region = (TextureRegion) cannonRoll.getKeyFrame(stateTimer,true);

                regionMid = false;
                regionHigh = false;
                regionStraight = false;

                break;

            case REVERSEROLL:
                region = (TextureRegion) cannonRollReverse.getKeyFrame(stateTimer,true);

                regionMid = false;
                regionHigh = false;
                regionStraight = false;

                break;


            case TILT1:
                region = (TextureRegion) tiltOne.getKeyFrame(stateTimer);
                break;

            case STRAIGHTSHOT:

                region = (TextureRegion) fireStraight.getKeyFrame(stateTimer);

                regionStraight = true;
                regionHigh = false;
                regionMid = false;

                if(current == State.STRAIGHTSHOT && fireStraight.getKeyFrameIndex(stateTimer) == 3) {

                    if(stateTimer > .3f && !fireStraight.isAnimationFinished(stateTimer))
                        cannonBall.add(new CannonBall(world, cannonSprite.getX() + 3, cannonSprite.getY() + 2.5f, -120f, 25f));

                }
                break;

            case MIDSHOT:

                region = (TextureRegion) tiltFireOne.getKeyFrame(stateTimer);

                if( tiltFireOne.getKeyFrameIndex(stateTimer) == 10) {
                    regionMid = true;
                    regionHigh = false;
                    regionStraight = false;
                }

                if(current == State.MIDSHOT && tiltFireOne.getKeyFrameIndex(stateTimer) == 3) {

                    if(stateTimer > .3f && !tiltFireOne.isAnimationFinished(stateTimer))
                            cannonBall.add(new CannonBall(world, cannonSprite.getX() + 3, cannonSprite.getY() + 3.5f, -65, 50f));

                }

                break;

            case TILT2:
                region = (TextureRegion) tiltTwo.getKeyFrame(stateTimer);


                if(tiltTwo.isAnimationFinished(stateTimer))
                    region.setRegion(highShot);
                break;

            case HIGHTSHOT:

                region = (TextureRegion) tiltFireTwo.getKeyFrame(stateTimer);


                if(tiltFireTwo.isAnimationFinished(stateTimer)) {
                    regionHigh = true;
                    regionMid = false;
                    regionStraight = false;
                }
                if(current == State.HIGHTSHOT && tiltFireTwo.getKeyFrameIndex(stateTimer) == 3) {

                    if(stateTimer > .3f && !tiltFireTwo.isAnimationFinished(stateTimer))
                        cannonBall.add(new CannonBall(world,cannonSprite.getX() + 3f,cannonSprite.getY() + 4.5f,-43f,80f));
                }
                break;

            case STILL:
            default:
                if(regionHigh)
                    region = highShot;
                else if(regionMid)
                    region = midShot;
             //   else if(regionStraight)
                  //  region = straightShot;
                else
                region = straightShot;

                break;
        }
        stateTimer = current == previous ? stateTimer + dt : 0;
        previous = current;
        return region;
    }

    //State Setup
    public State getState(){


        if(body.getLinearVelocity().x <=-.1f)
            return State.ROLLING;

          if(body.getLinearVelocity().x >= .1f)
            return  State.REVERSEROLL;

          if(Gdx.input.isKeyPressed(Input.Keys.D))
            return State.STRAIGHTSHOT;

          if(Gdx.input.isKeyPressed(Input.Keys.A))
            return State.MIDSHOT;

          if(Gdx.input.isKeyPressed(Input.Keys.UP))
            return State.TILT2;

          if(Gdx.input.isKeyPressed(Input.Keys.S))
            return State.HIGHTSHOT;

          else
            return State.STILL;
    }


    public void dispose(){
        world.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {



        if(keycode == Input.Keys.A ) {
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if(keycode == Input.Keys.A){
                return false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
