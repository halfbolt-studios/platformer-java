package net.halfbolt.platformer.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import net.halfbolt.platformer.entity.Entity;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.player.bow.Bow;
import net.halfbolt.platformer.player.controller.Controller;
import net.halfbolt.platformer.player.lantern.Lantern;
import net.halfbolt.platformer.world.LevelManager;

public class Player extends Entity {

    private Lantern lantern;
    private Controller control;
    public float health;
    private OrthographicCamera cam;
    private Bow bow;
    private LevelManager manager;

    public Player(LevelManager w) {
        this.manager = w;

        size = 0.5f;
        speed = 400;
        maxHealth = 3;
        category = Player.class;
        init(w.get(0), new Point(5, 15));
        body.setLinearDamping(8f);

        control = w.getRender().getGui().getControl();
        cam = w.getRender().getCamera();
        health = 60;

        w.getRender().setupLights();

        lantern = new Lantern(w, this);

        bow = new Bow(w, this);
    }

    @Override
    public void render() {
        lantern.render();
        bow.render();
        drawHealth();
        if (lantern.getPos().dst(getPos()) > 2) {
            //lantern.resetPos();
            //System.out.println("Out of range!");
        }
    }

    private void drawHealth() {
        drawHeart(new Vector2(getPos().x + 0.5f, getPos().y - 1.5f), 0.4f, health < 40);
        drawHeart(new Vector2(getPos().x, getPos().y - 1.5f), 0.4f, health < 20);
        drawHeart(new Vector2(getPos().x - 0.5f, getPos().y - 1.5f), 0.4f, health < 0);
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
        verts.add(new Vector2(0, 0)); // center (to make the method draw all triangles from here
        verts.add(new Vector2(size * (0f / 1f), size * (1f / 2f))); // bottom
        verts.add(new Vector2(size * (7f / 16f), size * (0f / 1f))); // middle right
        verts.add(new Vector2(size * (3f / 8f), size * (-3f / 8f))); // top right
        verts.add(new Vector2(size * (1f / 8f), size * (-1f / 2f))); // top right middle
        verts.add(new Vector2(size * (0f / 1f), size * (-2f / 8f))); // center
        verts.add(new Vector2(size * (-1f / 8f), size * (-1f / 2f))); // top left
        verts.add(new Vector2(size * (-3f / 8f), size * (-3f / 8f))); // top left middle
        verts.add(new Vector2(size * (-7f / 16f), size * (0f / 1f))); // middle left
        manager.getRender().drawPolyFilled(pos, verts, color);
    }

    @Override
    public boolean update() {
        if (super.update()) {
            return true;
        }
        body.applyForce(control.getMovementDelta()
                        .setLength(speed * body.getMass() * body.getLinearDamping() * Gdx.graphics
                                .getDeltaTime()),
                body.getPosition(), true);
        lantern.update();
        bow.update();
        return false;
    }

    @Override
    public void dispose() {
        control.dispose();
        lantern.dispose();
        bow.dispose();
    }

    public Lantern getLantern() {
        return lantern;
    }

    public Bow getBow() {
        return bow;
    }
}
