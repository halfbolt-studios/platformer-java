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
import java.util.ArrayList;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.world.LevelManager;
import net.halfbolt.platformer.world.tilemap.TileRender;

public class Render {

    private LevelManager levelManager;
    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private OrthographicCamera cam;
    private TileRender tileRender;
    private final int tilesWide = 20;
    private SpriteBatch sb;
    private Boolean debug = false;
    private GuiRender gui;
    private ShapeRenderer sr = new ShapeRenderer();

    private RayHandler lights;
    private float zoomVel = 0f;

    public Render() {
        cam = new OrthographicCamera();
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

        sb = new SpriteBatch();
        sb.setProjectionMatrix(cam.combined);

        levelManager = new LevelManager(this);
        gui = new GuiRender(this);

        levelManager.addPlayer(levelManager.createPlayer());

        tileRender = new TileRender(levelManager, sb);
    }

    public RayHandler getLights() {
        return lights;
    }

    public void setupLights() {
        lights = new RayHandler(levelManager.getWorld());
        lights.setAmbientLight(new Color(0.075f, 0, 0.25f, 0.5f));
    }

    public void drawPolyFilled(Vector2 offset, ArrayList<Vector2> verts, Color color) {
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
        sr.setProjectionMatrix(cam.combined);
        lights.setCombinedMatrix(cam);

        cam.update();

        debugRenderer.render(levelManager.getWorld(), cam.combined);

        tileRender.render();
        levelManager.getCurrentLevel().getEnemies().forEach(Enemy::render);

        if (debug) {
            levelManager.getCurrentLevel().getEnemies().forEach(Enemy::debugRender);
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(0, 1, 0, 1f);
            Point p = new Point(gui.getTileFromCursor());
            sr.rect(p.getX(), p.getY(), 1, 1);
            sr.end();
        }

        lights.render();

        levelManager.getPlayers().forEach(Player::render);
        gui.render();

        cam.zoom += Gdx.graphics.getDeltaTime() * zoomVel;
        if (cam.zoom < .99f) {
            zoomVel = 1;
        }
        if (cam.zoom > 1) {
            zoomVel = 0;
            cam.zoom = 1;
        }
    }

    public void update() {
        lights.update();
        float borderX = (float) Gdx.graphics.getWidth() / 2.5f;
        float borderY = (float) Gdx.graphics.getHeight() / 2.5f;
        // TODO: now that we have multiple players, we want the cam to zoom and move to the average of all players
        Vector3 pos = cam.project(new Vector3(levelManager.getPlayer(0).getPos().x,
                levelManager.getPlayer(0).getPos().y, 0));
        float div = 200f;
        if (pos.x < borderX) {
            cam.position.set(cam.position.x + (pos.x - borderX) / div, cam.position.y, 0);
        }
        if (pos.x > Gdx.graphics.getWidth() - borderX) {
            cam.position.set(cam.position.x + (pos.x - Gdx.graphics.getWidth() + borderX) / div,
                    cam.position.y, 0);
        }
        if (pos.y < borderY) {
            cam.position.set(cam.position.x, cam.position.y - (pos.y - borderY) / div, 0);
        }
        if (pos.y > Gdx.graphics.getHeight() - borderY) {
            cam.position.set(cam.position.x,
                    cam.position.y - (pos.y - Gdx.graphics.getHeight() + borderY) / div, 0);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debug = !debug;
        }
    }

    public void resize() {
        cam.setToOrtho(true,
                tilesWide,
                tilesWide * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
        gui.resize();
    }

    public void touchDragged(int x, int y, int cursor) {
        gui.touchDragged(x, y, cursor);
    }

    public void touchDown(int x, int y, int cursor) {
        gui.touchDown(x, y, cursor);
    }

    public void touchUp(int x, int y, int cursor) {
        gui.touchUp(x, y, cursor);
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public void dispose() {
        lights.dispose();
        sb.dispose();
        debugRenderer.dispose();
        gui.dispose();
        sr.dispose();
    }

    public SpriteBatch getBatch() {
        return sb;
    }

    public GuiRender getGui() {
        return gui;
    }

    public ShapeRenderer getSR() {
        return sr;
    }

    public void shake() {
        zoomVel = -1f;
    }

    public LevelManager getManager() {
        return levelManager;
    }
}
