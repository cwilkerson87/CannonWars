package com.curtiswilkerson.cannonwars.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class GCannon extends Sprite{


    public enum State {ROLLING,TILT1,TILT2,STILL,STRAIGHTSHOT,MIDSHOT,HIGHTSHOT,REVERSEROLL};
    public GCannon.State current;
    public GCannon.State previous;
    public World world;
    public Body body;
    public FixtureDef fixtureDef;

    private Sprite cannonSprite;

    private TextureRegion texture;
    private TextureRegion cannonStill;
    private TextureRegion straightShot;
    private TextureRegion midShot;
    private TextureRegion highShot;

    private float stateTimer;

    private boolean forward = false;
    private boolean backward = false;
    private boolean straight = false;
    private boolean mid = false;
    private boolean high = false;

    public boolean fire;
    public boolean move;
    public boolean stop = false;

    public static boolean hit;

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



   public Random random ;

   public GCannon(World world, float x, float y){

        this.world = world;
        current = State.STILL;
        previous = State.STILL;

        atlas = new TextureAtlas("Cannon.pack");

        //Animations && Textures
        highShot = new TextureRegion(atlas.findRegion("GCannonHighShot/Action"));
        straightShot = new TextureRegion(atlas.findRegion("GCannonStraight/Action"));
        midShot = new TextureRegion(atlas.findRegion("GCannonMidShot/Action"));

        texture = new TextureRegion(atlas.findRegion("GCannonStraight/Action"));
        cannonStill = new TextureRegion(atlas.findRegion("GCannonStraight/Action"));
        cannonSprite = new Sprite(cannonStill);
        cannonSprite.setBounds(0,0,10.0f,10.0f);


        regions = atlas.findRegions("GCannonRoll/Action");
        cannonRoll = new Animation(1f/4f,regions);

        regions = atlas.findRegions("GCannonRoll/Action");
        cannonRollReverse = new Animation(1f/4f,regions, Animation.PlayMode.LOOP_REVERSED);

        tilt1Region = atlas.findRegions("GCannonTilt_0/Action");
        tilt1Region.removeIndex(0);
        tiltOne = new Animation(1f/1f,tilt1Region);

        tilt2Region = atlas.findRegions("GCannonTilt_1/Action");
        tilt2Region.removeIndex(0);
        tiltTwo = new Animation(1f/1f,tilt2Region);

        fireStraightRegion = atlas.findRegions("GCannonStraight/Action");
        fireStraight = new Animation(1f/12f,fireStraightRegion);

        tilt1FireRegion = atlas.findRegions("GCannonMidShot/Action");
        tiltFireOne = new Animation(1f/12f,tilt1FireRegion);

        tilt2FireRegion = atlas.findRegions("GCannonHighShot/Action");
        tiltFireTwo = new Animation(1f/12f,tilt2FireRegion);


        defined(x,y);

        setRegion(cannonStill);
        hit = false;
        random = new Random();

        move = true;
        fire = true;
        cannonBall = new Array<CannonBall>();

    }


    //Create Body and Fixture
    public void defined(float x, float y){


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);
        body = world.createBody(bodyDef);

        fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox((cannonSprite.getWidth()-5) /2 , (cannonSprite.getHeight()-7)/3);
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;
        fixtureDef.isSensor = false;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public void draw(SpriteBatch batch){
        cannonSprite.flip(true,false);
        cannonSprite.draw(batch);

        for(CannonBall ball : cannonBall)
            ball.draw(batch);
    }

    //Updates
    public void update(float dt){
        cannonSprite.setPosition(body.getPosition().x - cannonSprite.getWidth()*.5f ,body.getPosition().y - cannonSprite.getHeight()+(16.3f *.5f ));
        cannonSprite.setRegion(getFrame(dt));

        for(CannonBall ball: cannonBall){
            ball.update(dt);
            if(ball.destroyed)
                cannonBall.removeValue(ball, true);


        }


        if(move) {
            move();
        }



            if (cannonSprite.getX() >= 9) {
                stop();
                fire();
                fire = true;
            }


       /* if(hit){

            body.applyLinearImpulse(new Vector2(-70,0),body.getWorldCenter(),true);
            // body.applyForce(new Vector2(-80,0),body.getWorldCenter(),true);
            hit = false;

        }

*/

    }

    public TextureRegion getFrame(float dt) {
        current = getState();


        switch (current) {

            case ROLLING:
                region = (TextureRegion) cannonRoll.getKeyFrame(stateTimer,true);
                break;

            case REVERSEROLL:
                region = (TextureRegion) cannonRollReverse.getKeyFrame(stateTimer,true);
                break;


            case TILT1:

                region = (TextureRegion) tiltOne.getKeyFrame(stateTimer);
                break;

            case STRAIGHTSHOT:

                    region = (TextureRegion) fireStraight.getKeyFrame(stateTimer);

                    if(fireStraight.getKeyFrameIndex(stateTimer) == 3)
                        cannonBall.add(new CannonBall(world, cannonSprite.getX() + 5f, cannonSprite.getY() + 2.2f, 120f, 23f));


                if(fireStraight.isAnimationFinished(stateTimer))
                    region.setRegion(straightShot);
                break;

            case MIDSHOT:

                    region = (TextureRegion) tiltFireOne.getKeyFrame(stateTimer);

                    if (tiltFireOne.getKeyFrameIndex(stateTimer) == 3)
                         cannonBall.add(new CannonBall(world, cannonSprite.getX() + 5.2f, cannonSprite.getY() + 2.3f, 65, 50f));

                    if(tiltFireOne.isAnimationFinished(stateTimer)) {
                        region.setRegion(midShot);
                }
                break;

            case TILT2:
                region = (TextureRegion) tiltTwo.getKeyFrame(stateTimer);
                break;

            case HIGHTSHOT:

                    region = (TextureRegion) tiltFireTwo.getKeyFrame(stateTimer);

                    if (tiltFireTwo.getKeyFrameIndex(stateTimer) == 3) {
                       // fixtureDef.isSensor = true;
                        cannonBall.add(new CannonBall(world,cannonSprite.getX() + 7f,cannonSprite.getY() + 5.5f,43f,80f));
                    }

                if(tiltFireOne.isAnimationFinished(stateTimer))
                    region.setRegion(highShot);

                break;

            case STILL:
            default:
                if(current == State.TILT1||previous == State.MIDSHOT){
                    region = midShot;
                    //  else if(current == State.TILT2 || previous == State.HIGHTSHOT)
                    //  region.setRegion(highShot);
                }
                else {
                    region = cannonStill;
                }
                break;
        }
        stateTimer = current == previous ? stateTimer + dt : 0;
        previous = current;
        return region;
    }

    //State Setup
    public State getState(){

        random = new Random();



//Finish setting up random movement

        if(backward ){
            body.applyLinearImpulse(new Vector2(-17.5f, 0), body.getWorldCenter(), true);
            return State.REVERSEROLL;
        }

        if(forward){
            body.applyLinearImpulse(new Vector2(17.5f, 0), body.getWorldCenter(), true);
            return State.ROLLING;
        }

        if(straight)
            return State.STRAIGHTSHOT;

        if(mid)
            return State.MIDSHOT;

        if(Gdx.input.isKeyPressed(Input.Keys.UP))
            return State.TILT2;

        if(high)
            return State.HIGHTSHOT;

        else
            return State.STILL;
    }

    public void move() {


            if (cannonSprite.getX() == 5.0f && !backward) {
                forward = true;
                backward = false;
            }

            if (cannonSprite.getX() >= 10.0f) {
                backward = true;
                forward = false;
            }

            if (cannonSprite.getX() <= 0.0f) {
                forward = true;
                backward = false;
            }

    }
    public void fire() {

        int fireShot = random.nextInt(4);

        if (fire) {
            switch (fireShot) {

                case 1:

                    if (stateTimer > 1) {
                        straight = true;
                        mid = false;
                        high = false;
                        fire = false;
                    }

                    break;

                case 2:
                    if (stateTimer > 1) {
                        mid = true;
                        straight = false;
                        high = false;
                        fire = false;
                    }
                    break;

                case 3:
                    if(stateTimer > 1){
                        high = true;
                        straight = false;
                        mid = false;
                        fire = false;
                    }
                    break;
            }
        }
    }

    public void stop(){
        forward = false;
        backward = false;
    }

//Made static for WorldContactListener
    public static void onHit(){
        hit = true;
    }


    public void dispose(){
        world.dispose();
    }


}
