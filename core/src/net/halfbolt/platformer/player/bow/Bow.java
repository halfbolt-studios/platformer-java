package net.halfbolt.platformer.player.bow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.World;

import java.util.ArrayList;

public class Bow {
    private final Player p;
    private float chargeAmount = 0;
    private ArrayList<Arrow> arrows = new ArrayList<>();
    private final World w;

    public Bow(World w, Player p) {
        this.w = w;
        this.p = p;
    }

    public void update() {
        if (Gdx.input.isButtonPressed(0)) {
            chargeAmount += Gdx.graphics.getDeltaTime();
            if (chargeAmount > 1) {
                chargeAmount = 1;
            }
        } else {
            if (chargeAmount > 0) {
                fireArrow();
            }
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
        arrows.add(new Arrow(w, p, chargeAmount));
    }

    public void render() {
        if (chargeAmount > 0) {
            w.getRender().getSR().begin(ShapeRenderer.ShapeType.Filled);
            w.getRender().getSR().setColor(0.1f, 0.75f, 0, 1);
            w.getRender().getSR().rect(p.getPos().x - 0.5f,p.getPos().y - 1.2f, 1, 0.2f);
            w.getRender().getSR().setColor(0.75f, 0.2f, 0, 1);
            w.getRender().getSR().rect(p.getPos().x - 0.5f,p.getPos().y - 1.2f, chargeAmount, 0.2f);
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
