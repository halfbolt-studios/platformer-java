package net.halfbolt.platformer.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.player.Controller;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.tilemap.Tilemap;

public class World {
    private com.badlogic.gdx.physics.box2d.World w;
    private Player p;
    private Enemy e;
    private Tilemap map;
    private Boolean paused = false;

    public World(Controller control, OrthographicCamera cam) {
        w = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
        p = new Player(this, control);
        map = new Tilemap(this, "levels/level0");
        e = new Enemy(new Point(5, 5), this, cam);
    }

    public void update() {
        if (!paused) {
            w.step(Gdx.graphics.getDeltaTime(), 6, 2);
            p.update();
            e.update();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }
    }

    public com.badlogic.gdx.physics.box2d.World getWorld() {
        return w;
    }

    public Player getPlayer() {
        return p;
    }

    public Enemy getEnemy() {
        return e;
    }

    public Tilemap getTilemap() {
        return map;
    }

    public Tilemap getMap() {
        return map;
    }
}
