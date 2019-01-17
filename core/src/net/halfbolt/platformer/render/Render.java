package net.halfbolt.platformer.render;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import net.halfbolt.platformer.player.Controller;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.World;
import net.halfbolt.platformer.world.tilemap.TileRender;

import java.util.ArrayList;

public class Render {
    private World w;
    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private OrthographicCamera cam;
    private Controller control;
    private TileRender tileRender;
    private final int tilesWide = 20;
    private SpriteBatch sb;
    private Boolean debug = false;

    private RayHandler lights;

    public Render() {
        cam = new OrthographicCamera();
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        control = new Controller();
        w = new World(this);

        w.addPlayer(w.createPlayer());

        sb = new SpriteBatch();
        sb.setProjectionMatrix(cam.combined);

        tileRender = new TileRender(w, sb);
    }

    public RayHandler getLights() {
        return lights;
    }

    public void setupLights() {
        lights = new RayHandler(w.getWorld());
        lights.setAmbientLight(new Color(0.075f, 0, 0.25f, 0.5f));
    }

    public static void drawPolyFilled(ShapeRenderer sr, Vector2 offset, ArrayList<Vector2> verts, Color color) {
        Vector2 startVert = verts.get(0);
        verts.remove(0);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        for (int i = 0; i < verts.size() - 1; i++) {
            sr.triangle(startVert.x + offset.x, startVert.y + offset.y,
                    verts.get(i).x + offset.x, verts.get(i).y + offset.y,
                    verts.get(i + 1).x + offset.x, verts.get(i + 1).y + offset.y);
        }
        sr.triangle(startVert.x + offset.x, startVert.y + offset.y,
                verts.get(verts.size() - 1).x + offset.x, verts.get(verts.size() - 1).y + offset.y,
                verts.get(0).x + offset.x, verts.get(0).y + offset.y);
        sr.end();
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);
        lights.setCombinedMatrix(cam);

        cam.update();

        // For debugging box2d
//        if (debug) {
            debugRenderer.render(w.getWorld(), cam.combined);
//        }

        // For levels
        tileRender.render();
        if (debug) {
            w.getEnemy().debugRender();
        }

        lights.render();

        w.getPlayers().forEach(Player::render);
        control.render();
    }

    public void update() {
        lights.update();
        float borderX = (float) Gdx.graphics.getWidth() / 2.5f;
        float borderY = (float) Gdx.graphics.getHeight() / 2.5f;
        // TODO: now that we have multiple players, we want the cam to zoom and move to the average of all players
        Vector3 pos = cam.project(new Vector3(w.getPlayer(0).getPos().x, w.getPlayer(0).getPos().y, 0));
        float div = 200f;
        if (pos.x < borderX) {
            cam.position.set(cam.position.x + (pos.x - borderX) / div, cam.position.y, 0);
        }
        if (pos.x > Gdx.graphics.getWidth() - borderX) {
            cam.position.set(cam.position.x + (pos.x - Gdx.graphics.getWidth() + borderX) / div, cam.position.y, 0);
        }
        if (pos.y < borderY) {
            cam.position.set(cam.position.x, cam.position.y - (pos.y - borderY) / div, 0);
        }
        if (pos.y > Gdx.graphics.getHeight() - borderY) {
            cam.position.set(cam.position.x, cam.position.y - (pos.y - Gdx.graphics.getHeight() + borderY) / div, 0);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debug = !debug;
        }
    }

    public void resize() {
        cam.setToOrtho(true,
                tilesWide,
                tilesWide * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
    }

    public World getWorld() {
        return w;
    }

    public void touchDragged(int x, int y) {
        control.touchDragged(x, y);
    }

    public void touchDown(int x, int y) {
        control.touchDown(x, y);
    }

    public void touchUp(int x, int y) {
        control.touchUp(x, y);
    }

    public Controller getControl() {
        return control;
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public void dispose() {
        lights.dispose();
        sb.dispose();
        debugRenderer.dispose();
        control.dispose();
    }
}
