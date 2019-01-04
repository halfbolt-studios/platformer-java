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

        pathNode = Pathfind.findPath(new Point(body.getPosition()), new Point(w.getPlayer().getPos()), w);
        target = new Point(w.getPlayer().getPos());
        this.cam = cam;
    }

    public void render() {
        Node child = pathNode;
        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        while (child != null) {
            sr.setColor(Color.RED);
            sr.rect(child.getPos().getX(), child.getPos().getY(), 1, 1);
            child = child.getChild();
        }
        sr.end();
    }

    public void update() {
        if (pathNode == null || pathNode.getChild() == null || !new Point(w.getPlayer().getPos()).equals(target)) {
            pathNode = Pathfind.findPath(new Point(body.getPosition()), new Point(w.getPlayer().getPos()), w);
            target = new Point(w.getPlayer().getPos());
        }
        if (body.getPosition().dst(pathNode.getPos().toVec()) < 2) {
            pathNode = pathNode.getChild();
        }
        Vector2 targetVec = pathNode.getPos().toVec().sub(body.getPosition());
        targetVec = new Vector2(targetVec.x * 100, targetVec.y * 100);
        body.applyForceToCenter(targetVec, true);
    }

    public Vector2 getPos() {
        return body.getPosition();
    }
}
