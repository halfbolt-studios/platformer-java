package net.halfbolt.platformer.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    private Body body;
    private Node pathNode;
    private Point target;
    private World w;
    private ShapeRenderer sr = new ShapeRenderer();
    private OrthographicCamera cam;
    private int offsetAmount;
    private float lastHit;
    private final float hitTime = 500; // time it takes to hit player in millis
    private final float speed = 30; // amount of force to apply on the object every frame

    public Enemy(Point pos, World w, OrthographicCamera cam) {
        this.w = w;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.toVec());

        body = w.getWorld().createBody(bodyDef);
        body.setLinearDamping(30f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.45f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);
        circle.dispose();

        this.cam = cam;
        offsetAmount = 5;
    }

    public void debugRender() {
        Node child = pathNode;
        sr.setProjectionMatrix(cam.combined);
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

    private void hitPlayer(Player player) {
        // TODO: animation here
        if (TimeUtils.millis() - lastHit > hitTime) { // if last hit was half a second ago (or longer)
            player.health -= MathUtils.random(0.5f, 3);
            lastHit = TimeUtils.millis();
        }
    }

    public void update() {
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
            if (p.getPos().dst(getPos()) < minDistance) {
                closest = p;
            }
        }
        findPath(closest);

    }

    private void findPath(Player p) {
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
        targetVec.setLength(speed);
        body.applyForce(targetVec, body.getPosition(), true);
    }

    public Vector2 getPos() {
        return body.getPosition();
    }
}
