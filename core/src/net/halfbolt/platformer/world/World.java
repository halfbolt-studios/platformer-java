package net.halfbolt.platformer.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.enemy.SmallEnemy;
import net.halfbolt.platformer.enemy.TankEnemy;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.player.bow.Arrow;
import net.halfbolt.platformer.render.Render;
import net.halfbolt.platformer.world.tilemap.Tilemap;

import java.util.ArrayList;

public class World {
    private com.badlogic.gdx.physics.box2d.World w;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Tilemap map;
    private Boolean paused = false;
    private Render render;

    public World(Render render) {
        this.render = render;
        w = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
        map = new Tilemap(this, "levels/level0");
        enemies.add(new SmallEnemy(this, new Point(8, 15)));
        enemies.add(new SmallEnemy(this, new Point(10, 15)));
        enemies.add(new SmallEnemy(this, new Point(12, 15)));
        enemies.add(new TankEnemy(this, new Point(14, 15)));
        setupCallback();
    }

    private void setupCallback() {
        w.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                System.out.println("Contact!");
                Enemy enemy = null;
                for (Enemy e : enemies) {
                    if (e.getBody().equals(contact.getFixtureA().getBody())) {
                        enemy = e;
                        break;
                    } else if (e.getBody().equals(contact.getFixtureB().getBody())) {
                        enemy = e;
                        break;
                    }
                }
                if (enemy == null) {
                    return;
                }
                Arrow arrow = null;
                for (Player p : players) {
                    for (Arrow a : p.getBow().getArrows()) {
                        if (a.getBody().equals(contact.getFixtureA().getBody())) {
                            arrow = a;
                            break;
                        } else if (a.getBody().equals(contact.getFixtureB().getBody())) {
                            arrow = a;
                            break;
                        }
                    }
                    if (arrow != null) {
                        break;
                    }
                }
                if (arrow == null) {
                    return;
                }
                enemy.damage(arrow.getDamage());
                arrow.destroy();
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public void update() {
        if (!paused) {
            w.step(Gdx.graphics.getDeltaTime(), 6, 2);
            players.forEach(Player::update);
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

    public ArrayList<Enemy> getEnemies() {
        return enemies;
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
