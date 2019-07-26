package com.curtiswilkerson.cannonwars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.curtiswilkerson.cannonwars.Background.Background;
import com.curtiswilkerson.cannonwars.Sprites.Cannon;
import com.curtiswilkerson.cannonwars.Sprites.CannonBall;
import com.curtiswilkerson.cannonwars.Sprites.GCannon;
import com.curtiswilkerson.cannonwars.Sprites.Platform;

public class MainStage implements Screen {

    private Platform platform;
    private Platform platform2;
    private Cannon cannon;
    private GCannon gcannon;
    private CannonWars game;
    private CannonBall cannonBall;

    private Texture tex;
    private Texture tex2;
    private Texture tex3;
    private Texture tex4;

    private TextureAtlas atlas;

    private World world;

    Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private Body player;

    private  Viewport viewport;

    public MainStage(CannonWars game){

        atlas = new TextureAtlas("Cannon.pack");

        this.game = game;

        camera = new OrthographicCamera();

        viewport = new FitViewport(90,50,camera);
        viewport.apply();

        world = new World(new Vector2(0,-98f),true);

        debugRenderer = new Box2DDebugRenderer();

        tex = new Texture("nightSky.png");
        tex2 = new Texture("mountain2.png");
        tex3 = new Texture("water.png");
        tex4 = new Texture("waterbottom.png");

        cannon = new Cannon(world,80,8);
        gcannon = new GCannon(world,10,8);


        platform = new Platform(world,80,5);
        platform2 = new Platform(world,10,5);

        world.setContactListener(new WorldContactListener());


    }

    //Input Handling
    public void handleInput(float dt){

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            cannon.body.applyLinearImpulse(new Vector2(-17.5f, 0), cannon.body.getWorldCenter(), true);

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            cannon.body.applyLinearImpulse(new Vector2(17.5f, 0), cannon.body.getWorldCenter(), true);

    }

    @Override
    public void show() {

    }


    public void update(float dt){

        world.step(1/60f,6,2);
        handleInput(dt);
        cannon.update(dt);
        gcannon.update(dt);
        platform.update(dt);
        platform2.update(dt);

    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(1,0,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(tex,0,15,90,40);
        game.batch.draw(tex2,-3,8,100,20);
        game.batch.draw(tex3,-3,-10,100,20);
        cannon.draw(game.batch);
        gcannon.draw(game.batch);
        platform.draw(game.batch);
        platform2.draw(game.batch);
        game.batch.draw(tex4,-8,-16,100,20);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public  void dispose(){
        world.dispose();
        debugRenderer.dispose();
        platform.dispose();
        platform2.dispose();
        cannon.dispose();
        gcannon.dispose();
    }


}
