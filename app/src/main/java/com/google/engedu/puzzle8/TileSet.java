package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mark on 5/1/16.
 * A singleton to store puzzle tiles, so that we don't keep making them when we make a new board
 */
public class TileSet {


    private ArrayList<PuzzleTile> tiles = new ArrayList<>();

    private static TileSet ourInstance = null;

    public static TileSet getInstance(Bitmap bm, int parentWidth, int parentHeight, boolean newInstance) {
        if (ourInstance == null || newInstance) {
            ourInstance = new TileSet(bm, parentWidth, parentHeight);
        }

        return ourInstance;
    }

    private TileSet(Bitmap bitmap, int parentWidth, int parentHeight) {

        bitmap = Bitmap.createScaledBitmap(bitmap, parentWidth, parentHeight, true);

        //Rotate bitmap if not in landscape mode --- for my phone
        if (bitmap.getHeight() < bitmap.getWidth()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, parentWidth, parentHeight, matrix, true);
        }

        int counter = 0;
        for (int j = 0; j < PuzzleBoard.NUM_TILES; j++) {
            for (int i = 0; i < PuzzleBoard.NUM_TILES; i++) {
                if (counter < PuzzleBoard.NUM_TILES * PuzzleBoard.NUM_TILES - 1)//leave last one empty
                    tiles.add( new PuzzleTile(Bitmap.createBitmap(bitmap, i * (bitmap.getWidth() / PuzzleBoard.NUM_TILES),
                            j * (bitmap.getHeight() / PuzzleBoard.NUM_TILES),
                            bitmap.getWidth() / PuzzleBoard.NUM_TILES, bitmap.getHeight() / PuzzleBoard.NUM_TILES), j * PuzzleBoard.NUM_TILES + i));
                else tiles.add(null);
                counter++;
            }
        }
    }

    public PuzzleTile getTile(int i) {
        return tiles.get( i);
    }

    public ArrayList<PuzzleTile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<PuzzleTile> tiles) {
        this.tiles = tiles;
    }
}
