package net.halfbolt.platformer.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import net.halfbolt.platformer.enemy.pathfind.Node;
import net.halfbolt.platformer.enemy.pathfind.Pathfind;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;

public class Enemy {
    private Body body;
    private Node pathNode;
    private Point target;
    private World w;
    private Texture tex;
    private ShapeRenderer sr = new ShapeRenderer();
    private OrthographicCamera cam;
    private static int addMinX, addMinY, addMaxX, addMaxY;

    public Enemy(Point pos, World w, OrthographicCamera cam) {
        this.w = w;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.toVec());

        body = w.getWorld().createBody(bodyDef);
        body.setLinearDamping(30f);
        body.setAngularDamping(5f);

        CircleShape circle = new CircleShape();
        circle.setRadius(0.8f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);
        circle.dispose();

        addMinX = 5;
        addMaxX = 5;
        addMinY = 5;
        addMaxY = 5;
        pathNode = Pathfind.findPath(new Point(body.getPosition()), new Point(w.getPlayer().getPos()), w, addMinX, addMinY, addMaxX, addMaxY);
        target = new Point(w.getPlayer().getPos());
        this.cam = cam;
    }

    public void render() {
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

    public void update() {
        if (pathNode == null || pathNode.getChild() == null || !new Point(w.getPlayer().getPos()).equals(target)) {
            pathNode = Pathfind.findPath(new Point(body.getPosition()), new Point(w.getPlayer().getPos()), w, addMinX, addMinY, addMaxX, addMaxY);
            target = new Point(w.getPlayer().getPos());
        }
        //check to see if there is a possible route between player and enemy in the map segment
        try {
            //move enemy to next path node
            if (body.getPosition().dst(pathNode.getPos().toVec()) < 1.5) {
                pathNode = pathNode.getChild();
            }
            Vector2 targetVec = pathNode.getPos().toVec().sub(body.getPosition());
            targetVec = new Vector2(targetVec.x * 100, targetVec.y * 100);
            body.applyForceToCenter(targetVec, true);

            //try to keep map segment borders as they originally were
            if (addMinX > 5) {
                addMinX--;
            }
            if (addMaxX > 5) {
                addMaxX--;
            }
            if (addMinY > 5) {
                addMinY--;
            }
            if (addMaxY > 5) {
                addMaxY--;
            }

            //if not, increase map segment borders
        } catch (NullPointerException e) {
            //Increase map segment borders when there is no possible route in the map segment without hitting tiles from the enemy to the player
            if (addMinX < 40) {
                addMinX++;
            }
            if (addMaxX < 40) {
                addMaxX++;
            }
            if (addMinY < 40) {
                addMinY++;
            }
            if (addMaxY < 40) {
                addMaxY++;
            }
        }
    }

    public Vector2 getPos() {
        return body.getPosition();
    }
}
