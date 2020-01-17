package net.halfbolt.platformer.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.player.bow.Arrow;
import net.halfbolt.platformer.render.Render;

public class LevelManager {

    private static final String TAG = LevelManager.class.getName();
    private List<Level> levels = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private World box2dWorld;
    private int currentLevel = 0;
    private boolean paused = false;
    private Render render;

    public LevelManager(Render r) {
        this.render = r;
        box2dWorld = new World(new Vector2(0, 0), true);

        levels.add(new Level(r, "", box2dWorld, this));

        setupCallback();
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    private void setupCallback() {
        box2dWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Enemy enemy = null;
                for (Enemy e : getCurrentLevel().getEnemies()) {
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
            box2dWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);
            players.forEach(Player::update);
            getCurrentLevel().update();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
        }
        //TODO: Make it pause when you unplug controller
        if (Controllers.getControllers().size > 0) {
            //Gdx.app.log(TAG, Controllers.getControllers().get(0) + "");
            Controllers.getControllers().get(0).addListener(new ControllerAdapter() {
                @Override
                public void connected(Controller controller) {
                    paused = false;
                    Gdx.app.log(TAG, "unpaused");
                    super.connected(controller);
                }

                @Override
                public void disconnected(Controller controller) {
                    paused = true;
                    Gdx.app.log(TAG, "paused");
                    super.disconnected(controller);
                }

            });
        }
    }

    public Player createPlayer() {
        return new Player(this);
    }

    public Level get(int i) {
        return levels.get(i);
    }

    public World getWorld() {
        return box2dWorld;
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevel);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int i) {
        return players.get(i);
    }


    public Enemy getClosetEnemy(Vector2 pos) {
        float closestDist = Float.MAX_VALUE;
        Enemy closest = null;
        for (Enemy e : getCurrentLevel().getEnemies()) {
            if (pos.dst(e.getPos()) < closestDist) {
                closest = e;
                closestDist = pos.dst(e.getPos());
            }
        }
        return closest;
    }

    public void dispose() {
        players.forEach(Player::dispose);
        levels.forEach(Level::dispose);
    }

    public Render getRender() {
        return render;
    }
}
