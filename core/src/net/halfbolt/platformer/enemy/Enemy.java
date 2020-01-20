package net.halfbolt.platformer.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import net.halfbolt.platformer.enemy.pathfind.Node;
import net.halfbolt.platformer.enemy.pathfind.Pathfind;
import net.halfbolt.platformer.entity.Entity;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.player.Player;

public abstract class Enemy extends Entity {

    private Node pathNode;
    private Point target;
    private ShapeRenderer sr = new ShapeRenderer();
    private int offsetAmount = 5;
    private float lastHit;
    protected final float hitTime = 500; // time it takes to hit player in millis
    private float stunTimer = 0;

    protected Enemy() {
        category = Enemy.class;
    }

    public void debugRender() {
        Node child = pathNode;
        sr.setProjectionMatrix(level.getRender().getCamera().combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        while (child != null && child.getChild() != null) {
            sr.setColor(Color.RED);
            sr.rect(child.getPos().getX(), child.getPos().getY(), 1, 1);
            sr.setColor(Color.GREEN);
            sr.line(child.getPos().toVec().add(0.5f, 0.5f),
                    child.getChild().getPos().toVec().add(0.5f, 0.5f));
            child = child.getChild();
        }
        sr.end();
    }

    @Override
    public void render() {
        if (health < maxHealth) {
            level.getRender().getSR().begin(ShapeRenderer.ShapeType.Filled);
            level.getRender().getSR().setColor(0.9f, 0.2f, 0, 1);
            level.getRender().getSR().rect(getPos().x - 0.5f, getPos().y - 1.2f, 1, 0.2f);
            level.getRender().getSR().setColor(0.1f, 0.75f, 0, 1);
            level.getRender().getSR()
                    .rect(getPos().x - 0.5f, getPos().y - 1.2f, (float) health / maxHealth, 0.2f);

            level.getRender().getSR().setColor(0f, 0f, 0, 1);
            for (int i = 0; i < maxHealth; i++) {
                level.getRender().getSR()
                        .rect(getPos().x - 0.5f + (float) i / maxHealth - 0.01f, getPos().y - 1.2f,
                                0.02f, 0.2f);
            }
            level.getRender().getSR().end();
        }
    }

    private void hitPlayer(Player player) {
        // TODO: animation here
        if (TimeUtils.millis() - lastHit
                > hitTime) { // if last hit was half a second ago (or longer)
            player.health -= MathUtils.random(0.5f, 3);
            lastHit = TimeUtils.millis();
        }
    }

    @Override
    public boolean update() {
        if (super.update()) {
            return true;
        }
        //damage player
        level.getLevelManager().getPlayers().forEach((p) -> {
            if (getPos().dst(p.getPos()) <= 1.5f) {
                hitPlayer(p);
            }
        });
        //path finding to getSegment to player
        float minDistance = Float.MAX_VALUE;
        Entity closest = null;
        for (Player p : level.getLevelManager().getPlayers()) {
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

    private void findPath(Entity p) {
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
        if (pathNode == null || pathNode.getChild() == null || !new Point(p.getPos())
                .equals(target)) { // TODO: change it to get correct segment
            pathNode = Pathfind.findPath(new Point(body.getPosition()), new Point(p.getPos()),
                    level.getSegment(new Point(0, 0)), offsetAmount);
            target = new Point(p.getPos());
        }
        if (pathNode == null || pathNode.getChild() == null) {
            if (offsetAmount < 50) {
                offsetAmount++;
            }
            return;
        }
        if (offsetAmount > 5) {
            offsetAmount--;
        }
        //move enemy to next path node
        if (body.getPosition().dst(pathNode.getPos().toVec()) < 1.5) {
            pathNode = pathNode.getChild();
        }
        Vector2 targetVec = pathNode.getPos().toVec().add(new Vector2(0.5f, 0.5f))
                .sub(body.getPosition());
        targetVec.setLength(
                speed * body.getMass() * body.getLinearDamping() * Gdx.graphics.getDeltaTime());
        body.applyForce(targetVec, body.getPosition(), true);
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
