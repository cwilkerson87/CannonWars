package com.curtiswilkerson.cannonwars;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.curtiswilkerson.cannonwars.Sprites.Cannon;
import com.curtiswilkerson.cannonwars.Sprites.CannonBall;
import com.curtiswilkerson.cannonwars.Sprites.GCannon;
import com.curtiswilkerson.cannonwars.Sprites.Platform;

import java.awt.event.ContainerListener;

public class WorldContactListener implements ContactListener {

    private GCannon gCannon;
    private CannonBall cannonBall;
    private Platform platform;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof GCannon || fixB.getUserData() instanceof GCannon){
            Fixture cannonball = fixA.getUserData() instanceof GCannon ? fixA : fixB;
            Fixture object = cannonball == fixA ? fixB : fixA;

            if(fixA.getUserData() instanceof GCannon && fixB.getUserData() instanceof CannonBall){
                gCannon.onHit();
               // cannonBall.isDestroy();
               // System.out.println("Cannon");

            }


        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
