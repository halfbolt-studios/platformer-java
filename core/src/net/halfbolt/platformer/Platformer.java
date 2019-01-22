package net.halfbolt.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import net.halfbolt.platformer.render.Render;
import net.halfbolt.platformer.world.World;

public class Platformer extends ApplicationAdapter {
    private World world;
    private Render render;

    @Override
    public void create() {
        render = new Render();
        world = render.getWorld();
        Gdx.input.setInputProcessor(new InputHandler(render));
    }

    @Override
    public void render() {
        world.update();
        render.update();
        render.render();
    }

    @Override
    public void dispose() {
        world.dispose();
        render.dispose();
    }

    @Override
    public void resize(int width, int height) {
        render.resize();
    }
}
