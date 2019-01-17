package net.halfbolt.platformer.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.halfbolt.platformer.player.lantern.Lantern;
import net.halfbolt.platformer.render.Render;
import net.halfbolt.platformer.world.World;

import java.util.ArrayList;

public class Player {
    private Lantern lantern;
    private Body body;
    private Controller control;
    public float health;
    private ShapeRenderer sr = new ShapeRenderer();
    private OrthographicCamera cam;

    public Player(World w) {
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
        control = w.getRender().getControl();
        cam = w.getRender().getCamera();
        health = 60;

        w.getRender().setupLights();

        lantern = new Lantern(w, this);
    }

    public void render() {
        sr.setProjectionMatrix(cam.combined);
        lantern.render();
        drawHealth();
    }

    private void drawHealth() {
        drawHeart(new Vector2(getPos().x + 0.5f, getPos().y - 1.2f), 0.4f, health < 40);
        drawHeart(new Vector2(getPos().x, getPos().y - 1.2f), 0.4f, health < 20);
        drawHeart(new Vector2(getPos().x - 0.5f, getPos().y - 1.2f), 0.4f, health < 0);
    }

    //draw hearts for health
    private void drawHeart(Vector2 pos, float size, boolean filled) {
        Color color;
        //change color based on health
        if (filled) {
            //grey color
            color = Color.GRAY;
        } else {
            //dark red color
            color = new Color(0.7f, 0, 0, 1f);
        }
        ArrayList<Vector2> verts = new ArrayList<>();
        verts.add(new Vector2(0,0)); // center (to make the method draw all triangles from here
        verts.add(new Vector2(size * (0f / 1f), size * (1f / 2f))); // bottom
        verts.add(new Vector2(size * (7f / 16f), size * (0f / 1f))); // middle right
        verts.add(new Vector2(size * (3f / 8f), size * (-3f / 8f))); // top right
        verts.add(new Vector2(size * (1f / 8f), size * (-1f / 2f))); // top right middle
        verts.add(new Vector2(size * (0f / 1f), size * (-2f / 8f))); // center
        verts.add(new Vector2(size * (-1f / 8f), size * (-1f / 2f))); // top left
        verts.add(new Vector2(size * (-3f / 8f), size * (-3f / 8f))); // top left middle
        verts.add(new Vector2(size * (-7f / 16f), size * (0f / 1f))); // middle left
        Render.drawPolyFilled(sr, pos, verts, color);
    }

    public void update() {
        body.setLinearVelocity(body.getLinearVelocity().add(control.getMovementDelta()));
    }

    public Vector2 getPos() {
        return body.getPosition();
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        sr.dispose();
        control.dispose();
        lantern.dispose();
    }
}
