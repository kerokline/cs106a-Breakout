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
import java.util.concurrent.TimeUnit;

public class MyBreakout extends GraphicsProgram {

	/* Width and height of application window in pixels */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;
    
    /* Declaring shorter variable names */
    private static int WIDTH = 400;
    private static int HEIGHT = 600;
	
	/* Ball Info */
	private static int BALL_RADIUS = WIDTH / 100;
	
	/* Brick Layout */
	private static int BRICK_ROWS = 6;
	private static int BRICK_COLUMNS = 10;
	private static int BUFFER = 2;
	
	/* Brick Info */
	private static double BRICK_HEIGHT = HEIGHT / (BRICK_ROWS * 3);
	private static double BRICK_WIDTH = WIDTH / (BRICK_COLUMNS + BUFFER * 2);
	
	/* Paddle Info */
	private static double PADDLE_WIDTH = BRICK_WIDTH * 1.5;
	private static double PADDLE_HEIGHT = BRICK_HEIGHT * .125;
	
	/* Ball Speed Constants */
	private static double START_SPEED = .002;
	private static double SPEED_INCREMENT = .000125;
	
	/* Runs the Breakout program. */
	public void run() {
		fieldSetup();
		while (!gameOver()){
			moveBall();
		} 
		displayMessage();
	}
	
	private void fieldSetup(){
		ball = new GOval (BALL_RADIUS, BALL_RADIUS);
		 ball.setFilled(true);
		 add(ball, (WIDTH - BALL_RADIUS)/2, HEIGHT - (BRICK_HEIGHT * 3));
		
		paddle = new GRect (PADDLE_WIDTH, PADDLE_HEIGHT);
		 paddle.setFilled(true);
		 add(paddle,(WIDTH - PADDLE_WIDTH)/2, HEIGHT + (BALL_RADIUS * 2) - (BRICK_HEIGHT * 3));
		 
		for (int i = 0; i < BRICK_ROWS; i++){
			changeBrickColor();
			for (int j = 0; j < BRICK_COLUMNS; j++){
				GRect brick = new GRect (BRICK_WIDTH, BRICK_HEIGHT);
				 add(brick, BRICK_WIDTH * (j + BUFFER), BRICK_HEIGHT * (i + BUFFER));
				 brick.setFilled(true);
				 brick.setFillColor(brickColor);
				 bricksOnField++;
			}
		}
		
		addMouseListeners();
	}
	
	public void displayMessage(){
		if (lives < 1) {
			GLabel loss = new GLabel("So Sorry. You've Lost :(.", WIDTH/2, HEIGHT/2);
			add(loss);
			loss.move(-loss.getWidth()/2,-loss.getHeight()/2);
		} else {
			GLabel victory = new GLabel("CONGRATULATIONS! You Win!", WIDTH/2, HEIGHT/2);
			add(victory);
			victory.move(-victory.getWidth()/2, -victory.getHeight()/2);
		}
	}
	
	/* Track Mouse Movement & Clicks */
	/* Move paddle without going off screen or y axis change */
	public void mouseMoved (MouseEvent e){
		if (e.getX() < (WIDTH - (PADDLE_WIDTH/2)) && e.getX() > PADDLE_WIDTH/2) {
			paddle.setLocation(e.getX()-PADDLE_WIDTH/2, HEIGHT + (BALL_RADIUS * 2) - (BRICK_HEIGHT * 3));
		}
	}
	public void mousePressed (MouseEvent e) {
		if (velocityX == 0) {
			velocityX = rgen.nextDouble(-START_SPEED, START_SPEED);
			velocityY = -2 * START_SPEED + Math.abs(velocityX);
		}
	}
	
	/* Ball Movement and Collision Interactions*/
	private void moveBall() {
		if (velocityX == 0) {
			ball.setLocation(paddle.getX()+(paddle.getWidth()/2), ball.getY());
		}
		
		ball.move(velocityX, velocityY);
		GObject potentialObject = getElementAt(ball.getX() + velocityX, ball.getY() + velocityY);
		
		if (ball.getX() + velocityX < 0 || ball.getX() + velocityX > WIDTH) {
			velocityX *= -1;
		}
		if (ball.getY() + velocityY < 0) {
			velocityY *= -1;
		}
		
		if (potentialObject == paddle) {
			velocityY *= -1;
			if (velocityX > 0) {
				velocityX += SPEED_INCREMENT;
			} else {
				velocityX -= SPEED_INCREMENT;
			}
			if (velocityY > 0) {
				velocityY += SPEED_INCREMENT;
			} else {
				velocityY -= SPEED_INCREMENT;
			}
		}
		if (potentialObject != null && potentialObject != paddle) {
			if (ball.getX() + velocityX > potentialObject.getX() + potentialObject.getWidth() - ball.getWidth() && velocityX < 0) {
				velocityX *= -1;
			} 
			if (ball.getX() + velocityX < potentialObject.getX() + ball.getWidth() && velocityX > 0) {
				velocityX *= -1;
			} 
			if (ball.getY() + velocityY > potentialObject.getY() + potentialObject.getHeight() - ball.getHeight() && velocityY < 0) {
				velocityY *= -1;
			} 
			if (ball.getY() + velocityY < potentialObject.getY() + ball.getHeight() && velocityY > 0) {
				velocityY *= -1;
			}
			remove(potentialObject);
			bricksOnField--;
		}
		
		if (ball.getY() + velocityY > HEIGHT) {
			velocityX = 0;
			velocityY = 0;
			ball.setLocation((WIDTH - BALL_RADIUS)/2, HEIGHT - (BRICK_HEIGHT * 3));
			lives--;
		}
	}
	
	/* End Conditions */
	private boolean gameOver() {
		return (bricksOnField < 1 || lives < 1);
	}
	
	/* Workaround to make rows of color */
	private void changeBrickColor(){
		brickColor = rgen.nextColor();
	}
	
	/* Initialize running variables */
	private int lives = 3;
	private int bricksOnField = 0;
	private Color brickColor = Color.RED;
	private double velocityX = 0;
	private double velocityY = 0;
	
	/* Initialize random generator */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	/* Initialize objects */
	private GOval ball;
	private GRect paddle; 
}
