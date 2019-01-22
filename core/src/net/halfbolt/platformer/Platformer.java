package net.halfbolt.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import net.halfbolt.platformer.render.Render;
import net.halfbolt.platformer.world.LevelManager;

public class Platformer extends ApplicationAdapter {
    private LevelManager manager;
    private Render render;

    @Override
    public void create() {
        render = new Render();
        manager = render.getManager();
        Gdx.input.setInputProcessor(new InputHandler(render));
    }

    @Override
    public void render() {
        manager.update();
        render.update();
        render.render();
    }

    @Override
    public void dispose() {
        manager.dispose();
        render.dispose();
    }

    @Override
    public void resize(int width, int height) {
        render.resize();
    }
}
