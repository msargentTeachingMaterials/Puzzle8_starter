package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;


public class PuzzleBoard {

    public static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };


    private PuzzleBoard previousBoard;
    private ArrayList<PuzzleTile> tiles;
    int steps = 0;



    //    Then implement the constructor for PuzzleBoard.
//    It should take the passed-in Bitmap object and divide it into NUM_TILES x NUM_TILES equal-sized pieces.
//    (Hint: You can use the Bitmap.createBitmap and Bitmap.createScaledBitmap methods to do so.)
//    Then use each "chunk" of the bitmap to initialize a tile object. Remember to leave the last tile
//    null to represent the 'empty' tile!



    public PuzzleBoard(Bitmap bitmap, int parentWidth, int parentHeight, boolean newPic) {
        TileSet tileSet = TileSet.getInstance(bitmap, parentWidth, parentHeight, newPic);

        tiles = tileSet.getTiles();
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        previousBoard = otherBoard;
        steps = otherBoard.steps + 1;
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }


    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);

    }


    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> boards = new ArrayList<>();

        //locate the empty square in the current board
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i) == null) {

                //get row and col of tile from position in array
                int tileC = i % NUM_TILES;
                int tileR = i / NUM_TILES;
                //consider all the neighbours of the empty square (using the NEIGHBOUR_COORDS array)
                for (int j = 0; j < NEIGHBOUR_COORDS.length; j++) {
                    int nC = NEIGHBOUR_COORDS[j][0] + tileC;
                    int nR = NEIGHBOUR_COORDS[j][1] + tileR;

                    //if the neighbouring square is valid (within the boundaries of the puzzle)
                    if (nC <= 2 && nR <= 2 && nC >= 0 && nR >= 0) {
                        //make a copy of the current board (using the provided copy constructor)
                        PuzzleBoard puzzleBoard = new PuzzleBoard(this);
                        //move the tile in that square to the empty square
                        //get the new square number
                        int newT = XYtoIndex(nC, nR);
                        puzzleBoard.swapTiles(i, newT);
                        //and add this copy of the board to the list of neighbours to be returned
                        boards.add(puzzleBoard);

                    }
                }
                break;
            }
        }

        return boards;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    private int[] indexToXY(int index) {
        return new int[]{index % NUM_TILES, index / NUM_TILES};
    }

    public int manhattanDistanceTile(int index, int value) {
        int[] curr = indexToXY(index);
        int[] goal = indexToXY(value);//final position is value index

        //return diff in rows + diff in columns
        return Math.abs(curr[0] - goal[0]) + Math.abs(curr[1] - goal[1]);

    }

    public int totalManhattanDistance() {
        int distance = 0;
        int counter = 0;


        for (PuzzleTile tile : tiles) {
            if (tile != null) {
                distance += manhattanDistanceTile(counter, tile.getNumber());
                counter++;
            }

        }
        return distance;
    }

    public int priority() {
        return totalManhattanDistance() + steps;
    }


    public String toString() {
        String value = "";
        for (PuzzleTile tile : tiles) {
            String val = (tile != null) ? ""+tile.getNumber() : "empty";
            value = value + val + ", ";
        }
        return value;
    }

    public ArrayList<PuzzleTile> getTiles() {
        return tiles;
    }

    public void setPreviousBoard(PuzzleBoard previousBoard) {
        this.previousBoard = previousBoard;
    }
    public PuzzleBoard getPreviousBoard() {
        return previousBoard;
    }

}
