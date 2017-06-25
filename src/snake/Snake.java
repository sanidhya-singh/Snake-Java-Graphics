/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

/**
 *
 * @author Sanidhya
 */
public class Snake extends JFrame implements KeyListener, ActionListener {
    Timer t = new Timer(125,this);
    private SnakePanel s = new SnakePanel();
    LinkedList list = new LinkedList();
    boolean gamePaused = false;
    boolean cheated = false;
    int x = 0;
    int y = 0;
    public Snake() {
        t.start();
        s.setFocusable(true);
        s.addKeyListener(this);
        add(s);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,400);
        setLocationRelativeTo(null);
        setTitle("Snake");
    }
    public static void main(String[] args) {
        // TODO code application logic here
        JOptionPane.showMessageDialog(null, "SNAKE Game by Sanidhya\n\nUse Keys:\n                                W > Move Up\nA > Move Left       S > Move Down       D > Move Right\n\nH > Help\nP > Pause (Press again to resume)\nQ or ESC > Exit", "Welcome Screen", JOptionPane.DEFAULT_OPTION);
        new Snake();        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        if(ch == 'w') {
            s.moveUp();
        }
        else if(ch == 's') {
            s.moveDown();
        }
        else if(ch == 'a') {
            s.moveLeft();
        }
        else if(ch == 'd') {
            s.moveRight();
        }
        else if(ch == ' ') {
            Point p = new Point();
            p.x = s.sX;
            p.y = s.sY;
            list.addLast(p);
        }
        else if(ch == 27 || ch == 'q') {
            t.stop();
            playGameOverSound();
            JOptionPane.showMessageDialog(null, "You quit the Game!\nClosing...", "Closing" , JOptionPane.CLOSED_OPTION);            
            dispose();
        }
        else if(ch == 'p') {
            if(gamePaused) {
                gamePaused = false;
                t.start();
            }
            else {
                gamePaused = true;
                t.stop();
                s.repaint();
            }
        }
        else if(ch == 'c') {
            cheated = true;
            t.setDelay(70);
        }
        else {
            t.stop();
            s.repaint();
            gamePaused = true;
            JOptionPane.showMessageDialog(null, "SNAKE Game by Sanidhya\n\nUse Keys:\n                                W > Move Up\nA > Move Left       S > Move Down       D > Move Right\n\nH > Help\nP > Pause (Press again to resume)\nQ or ESC > Exit", "Help", JOptionPane.WARNING_MESSAGE);
            gamePaused = false;
            t.start();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
      
    }

    @Override
    public void keyReleased(KeyEvent e) {
     
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        s.sX += x;
        s.sY += y;        
        if(checkCollision(s.sX, s.sY))  {    
            playGameOverSound();
            JOptionPane.showMessageDialog(null, "Game Over\nFinal Score : " + (list.size() + 1));
            t.stop();
            dispose();
            new Choice();
        }
        if(!cheated) {
            if(list.size() >= 3)
                t.setDelay(100);
            if(list.size() >= 6)
                t.setDelay(80);
            if(list.size() >= 9)
                t.setDelay(70);
            if(list.size() >= 12)
                t.setDelay(65);
            if(list.size() >= 15)
                t.setDelay(50);
            if(list.size() >= 18)
                t.setDelay(40);
            if(list.size() >= 21)
                t.setDelay(30);
            if(list.size() >= 24)
                t.setDelay(20);
        }
        s.repaint();
    }
    
    public boolean checkCollision(int x, int y) {
        if(x + 5 < 0 || x + 20 > 400 || y + 5 < 0 || y + 40 > 400) {
            return true;
        }
        if(!list.isEmpty()) {
            for(int i=0; i<list.size()-1; i++) {
                Point p = (Point)list.get(i);
                if(x > p.x - 10 && x < p.x + 10 && y > p.y - 10 && y < p.y + 10) {
                    return true;                    
                }                    
            }
        }
        return false;
    }
    
    class SnakePanel extends JPanel {
        public int length = 1;
        int sX = 200;
        int sY = 200;
        int foodX = 300;
        int foodY = 300;
        boolean eaten = false;
        public void generateFood() {
            foodX = (int)(Math.random()*300) + 20;
            foodY = (int)(Math.random()*300) + 20;
            foodX -= foodX%10;
            foodY -= foodY%10;
            if(checkCollision(foodX, foodY))
                generateFood();
        }
        public SnakePanel() {
            setVisible(true);
            setBackground(Color.black);
            setSize(400,400);
        }
        public void moveDown() {
            y = 10;
            x = 0;
        }
        
        public void moveUp() {
            y = -10;
            x = 0;
        }
        
        public void moveLeft() {
            x = -10;
            y = 0;
        }
        
        public void moveRight() {
            x = 10;
            y = 0;
        }
        
        public void foodEaten() {
            if(sX > foodX - 10 && sX < foodX + 10 && sY > foodY - 10 && sY < foodY + 10) {
                playSound();
                generateFood();
                Point p = new Point();
                p.x = sX;
                p.y = sY;
                list.addLast(p);
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.black);
            g.fillRect(0,0,400,400);            
            g.setColor(Color.DARK_GRAY);
            for(int i=0; i<400; i+=10) {
                for(int j=0; j<400; j+=10) {
                    g.drawRect(i, j, 10, 10);
                }
            }
            g.setColor(Color.orange);
            g.fillRect(foodX, foodY, 10, 10);
            g.setColor(Color.white);
            g.drawRect(foodX, foodY, 10, 10);
            g.drawRect(foodX-1, foodY-1, 12, 12);
            foodEaten();
            Point pAdd = new Point();
            pAdd.x = sX;
            pAdd.y = sY;
            list.addLast(pAdd);
            if(!list.isEmpty()) {
                for(int i=0; i<list.size(); i++) {
                    Point p = (Point)list.get(i);
                    if(i == list.size()-1) 
                        g.setColor(Color.red);
                    else
                        g.setColor(Color.blue);
                    g.fillRect(p.x, p.y, 10, 10);         
                    g.setColor(Color.white);
                    g.drawRect(p.x, p.y, 10, 10);
                    g.drawRect(p.x-1, p.y-1, 12, 12);
                }
            }
            list.removeFirst();            
            g.setColor(Color.yellow);
            g.drawString("Your Score : ", 0, 10);  
            g.setColor(Color.white);
            g.drawString(String.valueOf(list.size() + 1), 80, 10);
            if(gamePaused) {
                g.setColor(Color.red);
                g.drawString("GAME PAUSED", 145, 180);
            }
        }
    }
    private void playSound() {
        try {
            String soundName = "Eaten.wav";
            AudioInputStream audioInputStream = null;
            audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void playGameOverSound() {
        try {
            String soundName = "GameOver.wav";
            AudioInputStream audioInputStream = null;
            audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class Choice extends JFrame implements ActionListener{
    private JButton yes = new JButton("Yes");
    private JButton no = new JButton("No");
    private JPanel question = new JPanel();
    private JLabel label = new JLabel("Do you wish to play again?");
    Font font = new Font("Verdana", Font.BOLD, 20);
    public Choice() {
        question.setBackground(Color.white);
        question.setForeground(Color.black);
        question.setEnabled(false);
        question.setSize(400,50);
        add(question, BorderLayout.CENTER);
        question.setLayout(new FlowLayout(FlowLayout.CENTER));
        question.add(label);
        question.setForeground(Color.black);
        label.setFont(font);        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1,2));
        p.setVisible(true);
        p.add(yes);
        p.add(no);
        add(p, BorderLayout.SOUTH);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        yes.addActionListener(this);
        no.addActionListener(this);
        yes.setBackground(Color.white);
        yes.setForeground(Color.black);
        yes.setSize(200,100);
        no.setBackground(Color.white);
        no.setForeground(Color.black);
        no.setSize(200,100);
        setTitle("#OneLastTime");
        setSize(400,150);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == yes) {
            dispose();
            new Snake();
        }
        else if(e.getSource() == no) {
            dispose();
        }
    }
}
