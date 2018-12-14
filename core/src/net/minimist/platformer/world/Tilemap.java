package net.minimist.platformer.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Tilemap {
    private TiledMap map;
    private OrthogonalTiledMapRenderer render;
    public Tilemap(String filename, OrthographicCamera cam) {
        map = new TmxMapLoader().load(filename);
        render = new OrthogonalTiledMapRenderer(map, 1);
        render.setView(cam);
    }

    public void render() {
        render.render();
    }

    public TiledMapTileLayer getLayer(int i) {
        return (TiledMapTileLayer) map.getLayers().get(i);
    }

    public TiledMap getMap() {
        return map;
    }
}
