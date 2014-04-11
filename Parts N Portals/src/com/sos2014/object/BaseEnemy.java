package com.sos2014.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sos2014.manager.ResourcesManager;

public abstract class BaseEnemy extends AnimatedSprite {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private Body body;

	private boolean goRight = false;
	private boolean goLeft = false;

	private int footContacts = 0;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public BaseEnemy(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY, ResourcesManager.getInstance().enemy, vbo);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);

	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) 
	{
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		//dynamic bodies can collide with each other and kinematic and static bodies

		body.setUserData("enemy");
		body.setFixedRotation(true); //wont tumble I assume

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) 
			{
				
				super.onUpdate(pSecondsElapsed);//This is very important to be in this exact spot
				
				camera.onUpdate(0.1f);

				if (getY() <= 0) //Body falls below bottom of scene
				{
					onDie();
				}

				if (goRight) 
				{
					body.setLinearVelocity(new Vector2(3, body.getLinearVelocity().y));//with the speed of 3 move right
					//I think that this is where we could add code to get the character to face the right direction
				}
				if (goLeft) 
				{
					body.setLinearVelocity(new Vector2(-3, body.getLinearVelocity().y));//with the speed f 3 move left
					//I think that this is where we could add code to get the character to face the right direction
				}
			}
		});
	}

	public void setRunning() {
		goRight = true;
		goLeft =false;

		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };

		animate(PLAYER_ANIMATE, 0, 2, false);
	}

	public void animateMe() {
		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };

		animate(PLAYER_ANIMATE, 0, 2, false);
	}

	public void setLeft() {
		goRight = false;
		goLeft = true;

	}

	public void jump() {
		if (footContacts < 1) {
			return;
		}
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
	}

	public void increaseFootContacts() {
		footContacts++;
	}

	public void decreaseFootContacts() {
		footContacts--;
	}

	public abstract void onDie();
}