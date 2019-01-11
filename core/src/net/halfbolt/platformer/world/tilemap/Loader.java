package net.halfbolt.platformer.world.tilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;
import net.halfbolt.platformer.world.tilemap.tile.Tile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;

public class Loader {
    public static Tilemap load(World w, String filename) {
        Texture tileset = new Texture(filename + "/tex.png");
        String file = Gdx.files.internal(filename + "/map.xml").readString();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        Document document;
        try {
            db = dbf.newDocumentBuilder();
            document = db.parse(new InputSource(new StringReader(file)));
        } catch (Exception e) {
            System.out.println("Warning: " + e);
            return null;
        }
        document.getDocumentElement().normalize();
        Element tilemap = document.getDocumentElement();
        int width = Integer.parseInt(tilemap.getAttributes().getNamedItem("tileswide").getNodeValue());
        int height = Integer.parseInt(tilemap.getAttributes().getNamedItem("tileshigh").getNodeValue());
        int tileWidth = Integer.parseInt(tilemap.getAttributes().getNamedItem("tilewidth").getNodeValue());
        int tileHeight = Integer.parseInt(tilemap.getAttributes().getNamedItem("tileheight").getNodeValue());

        HashMap<Integer, Layer> layers = new HashMap<>();
        NodeList layerNodes = tilemap.getElementsByTagName("layer");
        for (int i = 0; i < layerNodes.getLength(); i++) {
            if (layerNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element layerNode = (Element) layerNodes.item(i);
            HashMap<Point, Tile> map = new HashMap<>();
            int layerNum = Integer.parseInt(layerNode.getAttributes().getNamedItem("number").getTextContent());
            NodeList tiles = layerNode.getElementsByTagName("tile");
            for (int j = 0; j < tiles.getLength(); j++) {
                Node tileNode = tiles.item(j);
                if (tileNode == null || tileNode.getNodeType() != Node.ELEMENT_NODE) {
                    System.out.println(tileNode);
                    continue;
                }
                Element tile = (Element) tiles.item(j);
                Point pos = new Point(Integer.parseInt(tile.getAttribute("x")), Integer.parseInt(tile.getAttribute("y")));
                map.put(pos,
                        createTile(w,
                                pos,
                                width,
                                height,
                                tileWidth,
                                tileHeight,
                                Integer.parseInt(tile.getAttributes().getNamedItem("tile").getNodeValue()),
                                tileset,
                                Integer.parseInt(tile.getAttributes().getNamedItem("rot").getNodeValue())));
            }
            layers.put(layerNum, new Layer(width, height, tileWidth, tileHeight, map, true));
        }

        return new Tilemap(width,
                height,
                tileWidth,
                tileHeight,
                layers);
    }

    private static Tile createTile(World w, Point pos, int width, int height, int tileWidth, int tileHeight, int tileID, Texture tileset, int rot) {
        TextureRegion region = new TextureRegion(tileset, tileID % 8 * tileWidth, tileID / 8 * tileHeight, tileWidth, tileHeight);
        region.flip(false, true);
        return new Tile(w, pos, region, tileID, rot);
    }
}
