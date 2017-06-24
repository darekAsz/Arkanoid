package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Created by Darek on 24.06.2017.
 */
public class Gameplay extends JPanel implements KeyListener,ActionListener{

    private  boolean play = false;
    private int score = 0;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballX = 120;
    private int ballY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private int horizontalBricksAmount = 7;
    private int verticalBricksAmount = 3;
    private int totalBricks = verticalBricksAmount * horizontalBricksAmount;

    private MapGenerator gameMap;
    public Gameplay(){
        gameMap = new MapGenerator(verticalBricksAmount,horizontalBricksAmount);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }


    public void paint(Graphics g ){
        //background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);


        //drawing map
        gameMap.draw((Graphics2D)g);

        //borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        //score
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,590,30);

        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX,550,100,8);

        //ball
        g.setColor(Color.yellow);
        g.fillOval(ballX,ballY,20,20);

        if(totalBricks <= 0){
            play = false;
            ballXdir=0;
            ballYdir=0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("you won, your score: "+score,230,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press enter to restart",230,350);
        }

        if(ballY >570){ //ball fall down
            play = false;
            ballXdir=0;
            ballYdir=0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("game over, your score: "+score,190,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press enter to restart",230,350);

        }


        g.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){ //collision ball with paddle
            if(new Rectangle(ballX,ballY,20,20).intersects(new Rectangle(playerX,550,100,8))){
                ballYdir = -ballYdir;
            }

            //collision ball with bricks
           A:  for (int i = 0; i < gameMap.map.length ; i++) {
                for (int j = 0; j < gameMap.map[0].length ; j++) {
                    if(gameMap.map[i][j] > 0){
                        int brickX = j * gameMap.brickWidth + 80;
                        int brickY = i * gameMap.brickHeight + 50;
                        int brickWidth = gameMap.brickWidth;
                        int brickHeight = gameMap.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect = new Rectangle(ballX,ballY,20,20);


                        if(ballRect.intersects(brickRect)){
                            gameMap.setBrickValue(0,i,j);
                            totalBricks--;
                            score++;

                            if (ballX + 19 <= brickRect.x || ballX + 1 >= brickRect.x + brickWidth) {
                                ballXdir = - ballXdir;
                            }
                            else{
                                ballYdir = - ballYdir;
                            }

                            break A;
                         }
                    }
                }
            }


            ballX +=ballXdir;
            ballY +=ballYdir;
            if(ballX < 0){
                ballXdir = -ballXdir;
            }
            if(ballY < 0){
                ballYdir = -ballYdir;
            }
            if(ballX > 670){
                ballXdir = -ballXdir;
            }
        }


        repaint(); //call the paint method
    }



    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600){
                playerX=600;
            }
            else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX=10;
            }
            else {
                moveLeft();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballX = 120;
                ballY = 350;
                ballXdir = -1;
                ballYdir = -2;
                score = 0;
                playerX = 310;
                totalBricks = 21;
                gameMap = new MapGenerator(verticalBricksAmount,horizontalBricksAmount);
                repaint();
            }
        }

    }

    public void moveRight(){
        play=true;
        playerX +=20;

    }
    public void moveLeft(){
        play=true;
        playerX -=20;

    }

    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyReleased(KeyEvent e) { }
}

