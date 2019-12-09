package net.halfbolt.platformer.world;

import com.badlogic.gdx.physics.box2d.World;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.enemy.SmallEnemy;
import net.halfbolt.platformer.enemy.TankEnemy;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.render.Render;

import java.util.ArrayList;
import java.util.HashMap;

public class Level {
    private static final String TAG = Level.class.getName();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private HashMap<Point, MapSegment> segments = new HashMap<>();
    private World box2dWorld;
    private Render r;
    private LevelManager manager;

    public Level(Render r, String dir, World w, LevelManager manager) {
        this.box2dWorld = w;
        this.r = r;
        this.manager = manager;
        // class for prebuilt levels, will store set area of MapSegments
        segments.put(new Point(0, 0), new MapSegment(r, box2dWorld));
        
        enemies.add(new SmallEnemy(this, new Point(8, 15)));
        enemies.add(new SmallEnemy(this, new Point(10, 15)));
        enemies.add(new SmallEnemy(this, new Point(12, 15)));
        enemies.add(new TankEnemy(this, new Point(14, 15)));

    }

    public void update() {
        ArrayList<Enemy> remove = new ArrayList<>();
        for (Enemy e : enemies) {
            if (e.update()) {
                remove.add(e);
            }
        }
        for (Enemy e : remove) {
            enemies.remove(e);
        }
    }

    public MapSegment getSegment(Point p) {
        return segments.get(p);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public World getWorld() {
        return box2dWorld;
    }

    public Render getRender() {
        return r;
    }

    public LevelManager getLevelManager() {
        return manager;
    }

    public void dispose() {
        enemies.forEach(Enemy::dispose);
    }

    public void setupCallback() {

    }
}
