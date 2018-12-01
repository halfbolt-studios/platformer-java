package net.minimist.platformer.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.minimist.platformer.world.World;

public class Player {
  private Body body;
  private Controller control;
  public Player(World w, Controller control) {
    // First we create a body definition
    BodyDef bodyDef = new BodyDef();
    // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    // Set our body's starting position in the world
    bodyDef.position.set(new Vector2(0, 0));

    // Create our body in the world using our body definition
    body = w.getWorld().createBody(bodyDef);
    body.setLinearDamping(8f);

    // Create a circle shape and set its radius to 6
    CircleShape circle = new CircleShape();
    circle.setRadius(2f);

    // Create a fixture definition to apply our shape to
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circle;
    fixtureDef.density = 0.5f;
    fixtureDef.friction = 0.4f;
    fixtureDef.restitution = 0.6f; // Make it bounce a little bit

    body.createFixture(fixtureDef);

    circle.dispose();
    this.control = control;
  }

  public void render() {
  }

  public void update() {
    body.setLinearVelocity(body.getLinearVelocity().add(control.getMovementDelta()));
  }

  public Vector2 getPos() {
    return body.getPosition();
  }
}
