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
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(0, 0));

        body = w.getWorld().createBody(bodyDef);
        body.setLinearDamping(8f);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.8f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;

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
