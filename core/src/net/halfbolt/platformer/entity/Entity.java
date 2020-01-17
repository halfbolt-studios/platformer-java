package net.halfbolt.platformer.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.Level;

public abstract class Entity {

    protected Level level;
    protected int maxHealth = 5;
    protected int health = 5;
    protected float speed = 30; // amount of force to apply on the object every frame
    protected boolean destroyed = false;
    protected float size = 0.45f;
    protected Body body;

    protected void init(Level w, Point pos) {
        this.level = w;
        health = maxHealth;
        createBody(pos);
    }

    protected void createBody(Point pos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.toVec());

        body = level.getWorld().createBody(bodyDef);
        body.setLinearDamping(30f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(size);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = getBits();

        body.createFixture(fixtureDef);
        circle.dispose();
    }

    public abstract void render();

    public boolean update() {
        if (destroyed) {
            return true;
        }
        if (health < 0) {
            destroy();
            return true;
        }
        return false;
    }

    public void damage(int damage) {
        health -= damage;
        level.getRender().shake();
    }

    public void destroy() {
        dispose();
        level.getWorld().destroyBody(body);
        destroyed = true;
    }

    public Body getBody() {
        return body;
    }

    protected abstract void dispose();

    public Vector2 getPos() {
        if (destroyed) {
            throw new RuntimeException("Calling getPos when destroyed!");
        }
        return body.getPosition();
    }

    public abstract short getBits();
}
