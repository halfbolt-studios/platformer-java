package net.halfbolt.platformer.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.player.Controller;
import net.halfbolt.platformer.player.Player;

public class World {
    private com.badlogic.gdx.physics.box2d.World w;
    private Player p;
    private Enemy e;
    private Tilemap map;
    private TileManager manager;

    public World(Controller control, OrthographicCamera cam) {
        w = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
        p = new Player(this, control);
        e = new Enemy(this);
        map = new Tilemap("tilemaps/level0.tmx", cam);
        manager = new TileManager(this);
    }

    public void update() {
        w.step(1 / 60f, 6, 2);
        p.update();
        e.update(this);
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
}
