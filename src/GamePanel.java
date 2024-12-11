import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 600; //in pixels
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 20;	 //hier ein Size ähnlich wie ein Pixel
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	//static final int DELAY = 75;	  //Gamespeed - Higher the number slower the game
	static int DELAY = 100; 				//final entfernt, damit sich Geschwindikheit veränderbar ist - siehe checkApple()
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6; 					//Größe SN50E-Schlange bei Beginn
	int applesEaten;
	int appleX;							//Apfel x-Postion
	int appleY;							//Apfel y-Postition
	char direction = 'R'; 				//R= Right | L= Left | U= Up | D= Down ... Direction Start
	boolean running = false;
	Timer timer;
	Random random;
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			//Felder 
			//der folgende If-Code kann deaktiviert werden. Folgen: Linien der Units nicht sichtbar
			for(int i =0 ; i<SCREEN_HEIGHT/UNIT_SIZE ; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); 	//Vertikale Teiluung -> Teilt das Fenster in Units
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); 		//Horizontale Teilung -> Teilt das Fenster in Units
			}
			
			//Draw Apple
			g.setColor(Color.red); //Farbe Apple
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //Form Apple
		
			//Draw SNAKE
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
		}
		else {
			gameOver(g);
			
		}
		
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Sans serif",Font.ROMAN_BASELINE, 25));
		FontMetrics metrics = getFontMetrics(g.getFont());
		//Position Text
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
	}

	
	
	public void newApple() { 
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move() {
		for(int i = bodyParts ; i>0 ; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		//Bewegung Snake
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE; 
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;	
			break;
		}
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			//Snake wird größer, durch essen:
			bodyParts++; 
			applesEaten++;
			newApple(); 		//neues Apfel wird generiert
		}
	}
	
	public void checkCollisions() {
		//checks if head collides with body
		//beim Weglassen der for-Schleife: Schlange stirbt nicht, wenns denn Körper berührt	
		for(int i = bodyParts; i>0; i--) {
			if((x[0] == x[i])&& (y[0] == y[i])){
				running = false;
			}
		
		//checks if head touches left boreder
		if(x[0] < 0) {
			running = false;
		}
		//checks if head touches right border
		if(x[0] > SCREEN_WIDTH ) {
			running = false;
		}
		//check if head touches top border
		if(y[0] <0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
		}
	}
	
	public void gameOver(Graphics g) {
		
		//Score text
		g.setColor(Color.red);
		g.setFont(new Font("Sans serif",Font.ROMAN_BASELINE, 2));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		//Position Text
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

	
		//Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		//Position Text
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
		//Führt Snake zum Bewegen
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter {
    	@Override
    	//Steuerung Snake
    	public void keyPressed(KeyEvent e) {
    		switch(e.getKeyCode()){
    		case KeyEvent.VK_LEFT: //Richtung = Buchstabe, dann folgende Buchstabe
    			if(direction != 'R') {
    				direction = 'L';
    			}
    			break;
    		case KeyEvent.VK_RIGHT:
    			if (direction != 'L') {
    				direction = 'R';
    			}
    			break;
    		case KeyEvent.VK_UP:
    			if (direction != 'D') {
    				direction = 'U';
    			}
    			break;
    		case KeyEvent.VK_DOWN:
    			if (direction != 'U' ) {
    				direction = 'D';
    			break;
    			}
    		}
   
    	}
    }
}
