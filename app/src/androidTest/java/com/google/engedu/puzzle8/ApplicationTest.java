package com.google.engedu.puzzle8;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;
import android.util.Log;


import org.junit.runner.RunWith;

import org.junit.Before;
import org.junit.Test;


@RunWith(AndroidJUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


//    @Test
//    public void runTestIsSameBoard() {
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
//        Bitmap bmp = Bitmap.createBitmap(10, 10, conf);
//        PuzzleBoard pb1 = new PuzzleBoard(bmp, 10);
//        PuzzleBoard pb2 = new PuzzleBoard(bmp, 30);
//
//        assertTrue(pb1.isSameBoard(pb2));
//        pb1.swapTiles(0, 5);
//        assertFalse(pb1.isSameBoard(pb2));
//
//
//
//    }

    @Test
    public void checkSolved() {
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
//        Bitmap bmp = Bitmap.createBitmap(10, 10, conf);
//        PuzzleBoard pb1 = new PuzzleBoard(bmp, 10, 10);
//        pb1.swapTiles(7, 8);
//        pb1.swapTiles(4,7);
//        pb1.swapTiles(3,4);
//        int dist = pb1.totalManhattanDistance();
//        assertTrue(dist == 6);




    }

//[0,1,2,3,4,5,6,7,null]
//    0,1,2
//    3,null,5
//    6,4,7
//       md = 0
//
//  [1,2,3,5,7,null,4,6,0]
//    1,2,3
//    5,7,null
//    4,6,0
//md = 1+1+3+2+1+1+1+2+4

}