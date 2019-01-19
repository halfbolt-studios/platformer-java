package net.halfbolt.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import net.halfbolt.platformer.render.GuiRender;
import net.halfbolt.platformer.render.Render;
import net.halfbolt.platformer.world.World;

public class Platformer extends ApplicationAdapter {
    World world;
    Render render;
    GuiRender guiRender;

    @Override
    public void create() {
        render = new Render();
        world = render.getWorld();
        guiRender = new GuiRender(render);
        Gdx.input.setInputProcessor(new InputHandler(render));
    }

    @Override
    public void render() {
        world.update();
        render.update();
        render.render();
        guiRender.render();
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
