package net.halfbolt.platformer.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import net.halfbolt.platformer.world.World;

import static com.badlogic.gdx.math.MathUtils.random;

public class Enemy {
    private Body body;
    private Vector2 targetPosition;
    private Array<Array<Node>> nodes;
    private final Array<Integer> path = new Array<Integer>();

    private boolean firstSampleTaken;
    private Vector2 firstSample;
    private boolean secondSampleTaken;
    private Vector2 secondSample;
    private float sampleTimer;

    private boolean searchOtherTarget = false;
    private float boostAmount = 0;

    public Enemy(World w) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(15, 10));

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
        targetPosition = new Vector2(w.getPlayer().getPos());

        //set up nodes
        /*nodes = new Array<Array<Node>>();
        Array<Node> nodeBand = new Array<Node>();
        for (int i = 0; i < 100; i++) {
            nodeBand.add(new Node());
        }
        for (int j = 0; j < 100; j++) {
            nodes.add(nodeBand);
        }*/
        firstSampleTaken = false;
        firstSample = new Vector2();
        secondSampleTaken = false;
        secondSample = new Vector2();
        sampleTimer  = 0;
    }

    public void render() {

    }

    public void update(World w) {
        body.setLinearVelocity(chasePlayer(w));
    }

    //TODO: probably delete this
    private void EnemyAI () {
        //look for enemy
        for (int i = 0; i < nodes.size; i++) {
            for (int j = 0; j < nodes.get(i).size; j++) {
                if (getPos().dst(i,j) < 1) {
                    nodes.get(i).get(j).type = 1;
                } else {
                    if (nodes.get(i).get(j).type == 1) {
                        nodes.get(i).get(j).type = 0;
                    }
                }
            }
        }
        //look for player
        for (int i = 0; i < nodes.size; i++) {
            for (int j = 0; j < nodes.get(i).size; j++) {
                if (targetPosition.dst(i,j) < 1) {
                    nodes.get(i).get(j).type = 3;
                } else {
                    if (nodes.get(i).get(j).type == 3) {
                        nodes.get(i).get(j).type = 0;
                    }
                }
            }
        }

        Vector2 enemyPathPos = new Vector2();
        for (int i = 0; i < nodes.size; i++) {
            for (int j = 0; j < nodes.get(i).size; j++) {
                if (nodes.get(i).get(j).type == 1) {
                    enemyPathPos = new Vector2(i, j);
                }
            }
        }
        //DepthFirst.searchPath(nodes, (int) enemyPathPos.x, (int) enemyPathPos.y, path);
    }

    private Vector2 chasePlayer (World w) {

        boolean blocked = false;
        boolean notMoving = false;
        boolean touchingTile = false;
        float speed = 1.5f;

        //take first coordinate sample of enemy location
        if (!firstSampleTaken) {
            firstSample = getPos().cpy();
            firstSampleTaken = true;
        }
        //start sampleTimer
        if (firstSampleTaken && !secondSampleTaken) {
            sampleTimer ++;
        }
        //take second coordinate sample of enemy location
        if (sampleTimer > 10 && !secondSampleTaken) {
            secondSample = getPos().cpy();
            sampleTimer = 0;
            secondSampleTaken = true;
        }
        //compare sample coordinates
        if (firstSampleTaken && secondSampleTaken) {
            notMoving = (firstSample.dst2(secondSample) < 0.3f);
            firstSampleTaken = false;
            secondSampleTaken = false;
        }

        //check if the enemy is touching a tile hit-box
        int tileSize = 1;
        float tileDistX = 0;
        float tileDistY = 0;
        TiledMapTileLayer layer = w.getTilemap().getLayer(0);
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                if (layer.getCell(x, y * -1 + layer.getHeight()) != null) {
                    //code to check if touching
                    Vector2 tilePos = new Vector2(x + (tileSize / 2f), y + (tileSize / 2f));
                    if (getPos().dst(tilePos) < (tileSize * 1.5f)) {
                        touchingTile = true;
                    }
                }
            }
        }
        //see if enemy is not moving and enemy is touching a tile hit-box
        if (notMoving && touchingTile) {
            blocked = true;
        }
        //update velocity if blocked
        if (!blocked) {
            targetPosition = new Vector2(w.getPlayer().getPos());
        }
        //update target
        if (blocked) {
            int randomInt = MathUtils.floor(MathUtils.random(0, 2));
            if (randomInt == 0) {
                boostAmount = -5;
            } else {
                boostAmount = 5;
            }
            searchOtherTarget = true;
        }
        //reset boost if close to player
        if (getPos().dst(w.getPlayer().getPos()) < 3f) {
            boostAmount = 0;
        }

        //boost away from player then come back from a different angle
        if (searchOtherTarget) {
            targetPosition = new Vector2(w.getPlayer().getPos().x - boostAmount, w.getPlayer().getPos().y + boostAmount);
            if (getPos().dst(targetPosition) < 1f) {
                searchOtherTarget = false;
            }
        }

        return new Vector2(((targetPosition.x - getPos().x)) * speed, ((targetPosition.y - getPos().y)) * speed);
    }

    public Vector2 getPos() {
        return body.getPosition();
    }
}
