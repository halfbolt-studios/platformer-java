package net.minimist.platformer.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.minimist.platformer.world.World;

public class Player {
  private Body body;
  public Player(World w) {
    // First we create a body definition
    BodyDef bodyDef = new BodyDef();
    // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    // Set our body's starting position in the world
    bodyDef.position.set(new Vector2(0, 0));

    // Create our body in the world using our body definition
    body = w.getWorld().createBody(bodyDef);

    // Create a circle shape and set its radius to 6
    CircleShape circle = new CircleShape();
    circle.setRadius(2f);

    // Create a fixture definition to apply our shape to
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circle;
    fixtureDef.density = 0.5f;
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0.6f; // Make it bounce a little bit

    // Create our fixture and attach it to the body
    Fixture fixture = body.createFixture(fixtureDef);

    // Remember to dispose of any shapes after you're done with them!
    // BodyDef and FixtureDef don't need disposing, but shapes do.
    circle.dispose();
  }

  public void update() {
    Vector2 pos = body.getPosition();
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      body.applyLinearImpulse(-1f, 0, pos.x, pos.y, true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      body.applyLinearImpulse(1f, 0, pos.x, pos.y, true);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      body.applyLinearImpulse(0, -2f, pos.x, pos.y, true);
    }
  }
}
