package net.halfbolt.platformer.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.halfbolt.platformer.world.World;

public class Player {
    private Body body;
    private Controller control;
    public float health;
    private ShapeRenderer sr = new ShapeRenderer();
    private OrthographicCamera cam;

    public Player(World w, Controller control, OrthographicCamera cam) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(5, 15));

        body = w.getWorld().createBody(bodyDef);
        body.setLinearDamping(8f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.8f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);

        circle.dispose();
        this.control = control;
        this.cam = cam;
        health = 60;
    }

    public void render() {
        sr.setProjectionMatrix(cam.combined);
        Heart(sr, new Vector2(getPos().x - 0.5f, getPos().y - 1.2f), 0.4f, 0);
        Heart(sr, new Vector2(getPos().x, getPos().y - 1.2f), 0.4f, 20);
        Heart(sr, new Vector2(getPos().x + 0.5f, getPos().y - 1.2f), 0.4f, 40);
    }

    //draw hearts for health
    private void Heart (ShapeRenderer renderer, Vector2 position, float size, float dieAmount) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        //change color based on health
        if (health <= dieAmount) {
            //grey color
            renderer.setColor(Color.GRAY);
        } else {
            //dark red color
            renderer.setColor(new Color(Color.RED.r * 0.7f, Color.RED.g * 0.7f, Color.RED.b * 0.7f, 1f));
        }
        //the position is the center
        //bottom right
        renderer.triangle(position.x, position.y - (size / 6f), position.x, position.y + (size / 2), position.x + (size / 2), position.y);
        //middle right
        renderer.triangle(position.x, position.y - (size / 6f), position.x + (size / 2), position.y, position.x + (size * (3 / 8f)), position.y - (size / 2));
        //top right
        renderer.triangle(position.x, position.y - (size / 6f), position.x + (size * (3 / 8f)), position.y - (size / 2), position.x + (size / 6), position.y - (size / 2));
        //bottom left
        renderer.triangle(position.x, position.y - (size / 6f), position.x, position.y + (size / 2), position.x - (size / 2), position.y);
        //middle left
        renderer.triangle(position.x, position.y - (size / 6f), position.x - (size / 2), position.y, position.x - (size * (3 / 8f)), position.y - (size / 2));
        //top left
        renderer.triangle(position.x, position.y - (size / 6f), position.x - (size * (3 / 8f)), position.y - (size / 2), position.x - (size / 6), position.y - (size / 2));
        renderer.end();
    }

    public void update() {
        body.setLinearVelocity(body.getLinearVelocity().add(control.getMovementDelta()));
    }

    public Vector2 getPos() {
        return body.getPosition();
    }
}
