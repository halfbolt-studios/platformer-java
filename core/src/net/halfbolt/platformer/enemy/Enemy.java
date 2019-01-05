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
    int offsetMinX, offsetMaxX, offsetMinY, offsetMaxY;

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

        this.cam = cam;
        offsetMinX = 5;
        offsetMaxX = 5;
        offsetMinY = 5;
        offsetMaxY = 5;
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
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLUE);
        sr.rect((int) (getPos().x - 0.5f), (int) (getPos().y + 0.5f), 1, 1);
        sr.end();
    }

    public void update() {
        if (pathNode == null || pathNode.getChild() == null || !new Point(w.getPlayer().getPos()).equals(target)) {
            pathNode = Pathfind.findPath(new Point(body.getPosition()), new Point(w.getPlayer().getPos()), w, offsetMinX, offsetMinY, offsetMaxX, offsetMaxY);
            target = new Point(w.getPlayer().getPos());
        }
        try {
            //move enemy to next path node
            if (body.getPosition().dst(pathNode.getPos().toVec()) < 1.2) {
                pathNode = pathNode.getChild();
            }
            Vector2 targetVec = pathNode.getPos().toVec().sub(body.getPosition());
            targetVec = new Vector2((targetVec.x - 0.5f) * 100, (targetVec.y + 0.5f) * 100);
            body.applyForce(targetVec, body.getPosition(), true);
            if (offsetMinX > 5) {
                offsetMinX --;
            }
            if (offsetMaxX > 5) {
                offsetMaxX --;
            }
            if (offsetMinY > 5) {
                offsetMinY --;
            }
            if (offsetMaxY > 5) {
                offsetMaxY --;
            }
        } catch (NullPointerException e) {
            if (offsetMinX < 50) {
                offsetMinX ++;
            }
            if (offsetMaxX < 50) {
                offsetMaxX ++;
            }
            if (offsetMinY < 50) {
                offsetMinY ++;
            }
            if (offsetMaxY < 50) {
                offsetMaxY ++;
            }
        }
    }

    public Vector2 getPos() {
        return body.getPosition();
    }
}
