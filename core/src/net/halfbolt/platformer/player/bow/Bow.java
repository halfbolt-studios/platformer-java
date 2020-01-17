package net.halfbolt.platformer.player.bow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import net.halfbolt.platformer.entity.Entity;
import net.halfbolt.platformer.world.LevelManager;

public class Bow {

    private final Entity e;
    private float chargeAmount = 0;
    private ArrayList<Arrow> arrows = new ArrayList<>();
    private final LevelManager w;
    private Vector2 targetVector = new Vector2();
    private Vector2 unitCircleVector = new Vector2();

    public Bow(LevelManager w, Entity e) {
        this.w = w;
        this.e = e;
    }

    public void update() {
        if (w.getRender().getGui().getControl().getBowTarget(e) != null) {
            if (w.getRender().getGui().getControl().getBowPressed() && !w.getRender().getGui()
                    .getControl().getBowTarget(e).equals(e.getPos())) {
                float targetLength = 8;
                Vector2 targetPos = w.getRender().getGui().getControl().getBowTarget(e);
                targetVector.set(e.getPos().cpy().sub(targetPos));
                float dist = targetVector.len();
                unitCircleVector = targetVector.scl(1 / dist);
                targetVector.scl(targetLength);
                //target = w.getRender().getGui().getControl().getBowTarget(p);
            }
            if (w.getRender().getGui().getControl().getBowPressed()) {
                chargeAmount += Gdx.graphics.getDeltaTime();
                if (chargeAmount > 1) {
                    chargeAmount = 1;
                }
            } else {
                if (chargeAmount > 0) {
                    fireArrow();
                    w.getRender().getGui().getControl().bowProjection = new Vector2();
                }
                chargeAmount = 0;
            }
        } else {
            chargeAmount = 0;
        }
        ArrayList<Arrow> remove = new ArrayList<>();
        for (Arrow a : arrows) {
            if (a.update()) {
                remove.add(a);
            }
        }
        remove.forEach(a -> arrows.remove(a));
    }

    private void fireArrow() {
        arrows.add(new Arrow(w, e, chargeAmount, unitCircleVector));
    }

    public void render() {
        //float speed = 8;
        //Vector2 delta = new Vector2(target.x * chargeAmount * speed, target.y * chargeAmount * speed);
        /*float targetLength = 8;
        Vector2 targetPos = w.getRender().getGui().getControl().getGUITarget(p);
        Vector2 targetVector = p.getPos().cpy().sub(targetPos);
        float dist = targetVector.len();
        targetVector.scl(1 / dist);
        targetVector.scl(targetLength);*/

        if (chargeAmount > 0 && !w.getRender().getGui().getControl().getBowTarget(e)
                .equals(e.getPos())) {
            w.getRender().getSR().begin(ShapeRenderer.ShapeType.Filled);
            w.getRender().getSR().setColor(0.1f, 0.75f, 0, 1);
            w.getRender().getSR().rect(e.getPos().x - 0.5f, e.getPos().y - 1.2f, 1, 0.2f);
            w.getRender().getSR().rectLine(new Vector2(e.getPos().x, e.getPos().y), new Vector2(
                            e.getPos().x + (-1 * targetVector.x), e.getPos().y + (-1 * targetVector.y)),
                    0.5f);
            w.getRender().getSR().setColor(0.75f, 0.2f, 0, 1);
            w.getRender().getSR()
                    .rect(e.getPos().x - 0.5f, e.getPos().y - 1.2f, chargeAmount, 0.2f);
            w.getRender().getSR().end();
        }
        arrows.forEach(Arrow::render);
    }

    public void dispose() {
        arrows.forEach(Arrow::dispose);
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }
}
