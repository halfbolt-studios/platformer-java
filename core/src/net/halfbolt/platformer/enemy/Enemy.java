package net.halfbolt.platformer.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.TimeUtils;
import net.halfbolt.platformer.enemy.pathfind.Node;
import net.halfbolt.platformer.enemy.pathfind.Pathfind;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.World;

public class Enemy {
    public static final short enemyBits = 0x0002;
    private Body body;
    private Node pathNode;
    private Point target;
    protected World w;
    private ShapeRenderer sr = new ShapeRenderer();
    private int offsetAmount = 5;
    private float lastHit;
    protected final float hitTime = 500; // time it takes to hit player in millis
    protected float speed = 30; // amount of force to apply on the object every frame
    protected float size = 0.45f;
    private float stunTimer = 0;
    protected int maxHealth = 5;
    private int health = 5;
    private boolean destroyed = false;

    protected void init(World w, Point pos) {
        this.w = w;
        health = maxHealth;
        createBody(pos);
    }

    protected void createBody(Point pos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.toVec());

        body = w.getWorld().createBody(bodyDef);
        body.setLinearDamping(30f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(size);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.filter.categoryBits = enemyBits;

        body.createFixture(fixtureDef);
        circle.dispose();
    }

    public void debugRender() {
        Node child = pathNode;
        sr.setProjectionMatrix(w.getRender().getCamera().combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        while (child != null && child.getChild() != null) {
            sr.setColor(Color.RED);
            sr.rect(child.getPos().getX(), child.getPos().getY(), 1, 1);
            sr.setColor(Color.GREEN);
            sr.line(child.getPos().toVec().add(0.5f, 0.5f), child.getChild().getPos().toVec().add(0.5f, 0.5f));
            child = child.getChild();
        }
        sr.end();
    }

    public void render() {
        if (health < maxHealth) {
            w.getRender().getSR().begin(ShapeRenderer.ShapeType.Filled);
            w.getRender().getSR().setColor(0.9f, 0.2f, 0, 1);
            w.getRender().getSR().rect(getPos().x - 0.5f,getPos().y - 1.2f, 1, 0.2f);
            w.getRender().getSR().setColor(0.1f, 0.75f, 0, 1);
            w.getRender().getSR().rect(getPos().x - 0.5f,getPos().y - 1.2f, (float) health / maxHealth, 0.2f);

            w.getRender().getSR().setColor(0f, 0f, 0, 1);
            for (int i = 0; i < maxHealth; i++) {
                w.getRender().getSR().rect(getPos().x - 0.5f + (float) i / maxHealth - 0.01f,getPos().y - 1.2f,
                        0.02f, 0.2f);
            }
            w.getRender().getSR().end();
        }
    }

    private void hitPlayer(Player player) {
        // TODO: animation here
        if (TimeUtils.millis() - lastHit > hitTime) { // if last hit was half a second ago (or longer)
            player.health -= MathUtils.random(0.5f, 3);
            lastHit = TimeUtils.millis();
        }
    }

    public boolean update() {
        if (destroyed) {
            return true;
        }
        if (health < 0) {
            destroy();
            return true;
        }
        //damage player
        w.getPlayers().forEach((p) -> {
            if (getPos().dst(p.getPos()) <= 1.5f) {
                hitPlayer(p);
            }
        });
        //path finding to get to player
        float minDistance = Float.MAX_VALUE;
        Player closest = null;
        for (Player p : w.getPlayers()) {
            if (p.getLantern().getPos().dst(getPos()) < 2) {
                stunTimer = 0.5f;
                //make lantern flash when it stuns enemy
                p.getLantern().lanternFlash();
            }
            if (p.getPos().dst(getPos()) < minDistance) {
                closest = p;
            }
        }
        findPath(closest);
        return false;
    }

    private void destroy() {
        w.getWorld().destroyBody(body);
        destroyed = true;
    }

    private void findPath(Player p) {
        if (destroyed) {
            throw new RuntimeException("Calling findPath when destroyed!");
        }
        if (stunTimer > 0) {
            stunTimer -= Gdx.graphics.getDeltaTime();
            if (stunTimer < 0) {
                stunTimer = 0;
            } else {
                return;
            }
        }
        if (pathNode == null || pathNode.getChild() == null || !new Point(p.getPos()).equals(target)) {
            pathNode = Pathfind.findPath(new Point(body.getPosition()), new Point(p.getPos()), w, offsetAmount);
            target = new Point(p.getPos());
        }
        if (pathNode == null || pathNode.getChild() == null) {
            if (offsetAmount < 50) {
                offsetAmount ++;
            }
            return;
        }
        if (offsetAmount > 5) {
            offsetAmount --;
        }
        //move enemy to next path node
        if (body.getPosition().dst(pathNode.getPos().toVec()) < 1.5) {
            pathNode = pathNode.getChild();
        }
        Vector2 targetVec = pathNode.getPos().toVec().add(new Vector2(0.5f, 0.5f)).sub(body.getPosition());
        targetVec.setLength(speed * body.getMass());
        body.applyForce(targetVec, body.getPosition(), true);
    }

    public Vector2 getPos() {
        if (destroyed) {
            throw new RuntimeException("Calling getPos when destroyed!");
        }
        return body.getPosition();
    }

    public Body getBody() {
        return body;
    }

    public void damage(int damage) {
        health -= damage;
        w.getRender().shake();
    }
}
