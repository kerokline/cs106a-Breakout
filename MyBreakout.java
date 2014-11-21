/*
 * File: MyBreakout.java
 * -------------------
 * Name: Kevin
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class MyBreakout extends GraphicsProgram {

	/* Width and height of application window in pixels */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;
    
    /* Declaring shorter variable names */
    private static int WIDTH = 400;
    private static int HEIGHT = 600;
	
	/* Ball Info */
	private static int BALL_RADIUS = 2;
	
	/* Paddle Info */
	private static int PADDLE_WIDTH = 10;
	private static int PADDLE_HEIGHT = 4;
	
	/* Brick Layout */
	private static int BRICK_ROWS = 6;
	private static int BRICK_COLUMNS = 10;
	
	/* Brick Info */
	private static double BRICK_HEIGHT = HEIGHT / BRICK_ROWS * 3;
	private static double BRICK_WIDTH = WIDTH / BRICK_COLUMNS + 2;
	
	/* Runs the Breakout program. */
	public void init() {}
	public void run() {
		fieldSetup();
		while (!gameOver()){
			moveBall();
		}
	}
	
	private void fieldSetup(){
		ball.setFilled(true);
		add(ball, (WIDTH - BALL_RADIUS)/2, HEIGHT - (BRICK_HEIGHT * 3));
		
		paddle.setFilled(true);
		add(paddle, (WIDTH - PADDLE_WIDTH)/2, HEIGHT - BALL_RADIUS - (BRICK_HEIGHT * 3));
		 
		for (int i = 0; i < BRICK_ROWS; i++){
			brick.setFilled(true);
			brick.setFillColor(rgen.nextColor());
			for (int j = 0; j < BRICK_COLUMNS; j++){
				add(brick, BRICK_WIDTH * (j + 1 ), BRICK_HEIGHT * (i + 1));
				bricksOnField++;
			}
		}
		
		addMouseListeners();
	}
	
	/* Track Mouse Movement & Clicks */
	/* Move paddle without going off screen or y axis change */
	public void mouseMoved (MouseEvent e){
		if (e.getX() < (WIDTH - (PADDLE_WIDTH/2)) && e.getX() > PADDLE_WIDTH/2) {
			paddle.setLocation(e.getX()-PADDLE_WIDTH/2, BRICK_HEIGHT * 3 - BALL_RADIUS);
		}
	}
	public void mouseClicked (MouseEvent e) {
		if (velocityX == 0) {
			velocityX = rgen.nextDouble(-1, 1);
			velocityY = 2 - velocityX;
		}
	}
	
	/* Ball Movement */
	private void moveBall() {
		ball.move(velocityX, velocityY);
		if (ball.getX() + velocityX < 0 || ball.getX() + velocityX > WIDTH) {
			velocityX *= -1;
		}
		if (ball.getY() + velocityY < 0) {
			velocityY *= -1;
		} else if (ball.getY() + velocityY > HEIGHT) {
			ball = null;
		}
	}
	
	private boolean gameOver() {
		return (bricksOnField < 1 || lives < 1);
	}
	
	/* Initialize running variables */
	private int lives = 3;
	private int bricksOnField = 0;
	private double velocityX = 0;
	private double velocityY = 0;
	
	/* Initialize random generator */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	/* Initialize objects */
	private GOval ball = new GOval (BALL_RADIUS, BALL_RADIUS);
	private GRect brick = new GRect (BRICK_WIDTH, BRICK_HEIGHT);
	private GRect paddle = new GRect (PADDLE_WIDTH, PADDLE_HEIGHT);
}
