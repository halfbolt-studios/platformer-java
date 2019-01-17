package net.halfbolt.platformer.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.render.Render;
import net.halfbolt.platformer.world.tilemap.Tilemap;

import java.util.ArrayList;

public class World {
    private com.badlogic.gdx.physics.box2d.World w;
    private ArrayList<Player> players = new ArrayList<>();
    private Enemy e;
    private Tilemap map;
    private Boolean paused = false;
    private Render render;

    public World(Render render) {
        this.render = render;
        w = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
        map = new Tilemap(this, "levels/level0");
        e = new Enemy(new Point(10, 15), this, render.getCamera());
    }

    public void update() {
        if (!paused) {
            w.step(Gdx.graphics.getDeltaTime(), 6, 2);
            players.forEach(Player::update);
            e.update();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }
    }

    public com.badlogic.gdx.physics.box2d.World getWorld() {
        return w;
    }

    public Player getPlayer(int i) {
        return players.get(i);
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

    public Render getRender() {
        return render;
    }

    public Player createPlayer() {
        return new Player(this);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void dispose() {
        w.dispose();
        players.forEach(Player::dispose);
    }
}
