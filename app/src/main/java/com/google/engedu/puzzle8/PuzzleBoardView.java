package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private int counter = 0;

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap, View parent) {
        int width = imageBitmap.getWidth() / 4;
        int height = imageBitmap.getHeight() / 4;
        puzzleBoard = new PuzzleBoard(imageBitmap, width, height, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(100);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        final Random random = new Random();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animation == null && puzzleBoard != null && counter < 40) {
                    ArrayList<PuzzleBoard> boards = puzzleBoard.neighbours();
                    puzzleBoard = boards.get(random.nextInt(boards.size()));
                    puzzleBoard.setPreviousBoard(null);
                    invalidate();
                    counter++;
                    shuffle();
                } else {
                    counter = 0;
                }


            }
        }, 50);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {
        long time1 = System.currentTimeMillis();

        PriorityQueue<PuzzleBoard> queue = new PriorityQueue<>(9, new Comparator<PuzzleBoard>() {
            @Override
            public int compare(PuzzleBoard puzzleBoard, PuzzleBoard t1) {
                return puzzleBoard.priority() - t1.priority();
            }
        });
        animation = new ArrayList<>();
        puzzleBoard.setPreviousBoard(null);
        HashSet<PuzzleBoard> visited = new HashSet<>();

        queue.offer(puzzleBoard);

        while (!queue.isEmpty()) {

            PuzzleBoard board = queue.poll();
            visited.add(board); //So we don't consider this board more than once

            //if board is solution
            if (board.resolved()) {
                Log.d("solve_timer", ""+(System.currentTimeMillis()-time1));
                //If it is the solution, create an ArrayList of all the PuzzleBoards leading to this solution
                // (you will need to create a getter for PuzzleBoard.previousBoard).
                ArrayList<PuzzleBoard> boards = new ArrayList<>();
                boards.add(board);

                while (board.getPreviousBoard() != null && !board.getPreviousBoard().equals(board)) {
                    boards.add(board.getPreviousBoard());
                    board = board.getPreviousBoard();
                }
                Collections.reverse(boards);
                animation = (ArrayList<PuzzleBoard>) boards.clone();
                invalidate();

                return;
            } else {
                for (PuzzleBoard b : board.neighbours()) {
                    if (board.getPreviousBoard() == null || (!b.equals(board.getPreviousBoard()) && !visited.contains(b)))
                        queue.offer(b);
                    //Log.d("solve_test", b + " with priority:" + b.priority() + " md: " + b.totalManhattanDistance());
                }
            }
        }

    }



}
