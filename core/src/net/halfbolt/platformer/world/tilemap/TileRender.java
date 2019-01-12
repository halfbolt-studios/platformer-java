package net.halfbolt.platformer.world.tilemap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.halfbolt.platformer.world.World;
import net.halfbolt.platformer.world.tilemap.tile.Tile;

public class TileRender {
    private World w;
    private SpriteBatch sb;
    public TileRender(World w, SpriteBatch sb, OrthographicCamera cam) {
        this.w = w;
        this.sb = sb;
    }

    public void render() {
        Layer layer = w.getTilemap().getLayer("graphics");
        if (!layer.isVisible()){
            return;
        }
        sb.begin();
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                Tile tile = layer.get(x, y);
                if (tile != null && tile.getID() != -1) {
                    TextureRegion region = tile.getTextureRegion();
                    sb.draw(region, x, y, 0.5f, 0.5f, 1, 1, 1, 1, tile.getRotation() * 90);
                }
            }
        }
        sb.end();
    }
}
