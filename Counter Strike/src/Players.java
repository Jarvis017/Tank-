import javax.swing.*;

import java.util.Timer;
import java.util.TimerTask;

public class Players {
	int x;
	int y;
	java.util.Timer t;
	Timer t2;
	int health=4;
	private int gun_shots=1;
	private boolean canMove = true;
	public JLabel health_label;
	public Players(int initX, int initY) {
		super();
		this.x = initX;
		this.y = initY;
		health_label = new JLabel(String.valueOf(health));
	}

	//moving method( pass the board and the new direction that he choose )
	public void move(Board board, int direction) {
		
		// if player or bot have same direction with there fields
		if (board.getField(x, y).direction == direction) {
			int newX = x, newY = y;
			switch (direction) {
				case Board.UP:
					newY--;
					break;
				case Board.RIGHT:
					newX++;
					break;
				case Board.BOTTOM:
					newY++;
					break;
				case Board.LEFT:
					newX--;
					break;
			}
			if (board.getField(newX, newY).state == Board.EMPTY) {
				if (canMove) {
					board.movePos(x, y, newX, newY, board.getField(x, y).state, board.getField(x, y).direction,board.getPlayer(x, y));
					canMove = false;
					new java.util.Timer().schedule(new TimerTask() { // reload time
						@Override
						public void run() {
							canMove = true; // ammo rounds
						}
					}, 100);
					x = newX;
					y = newY;
				}
			}
		}
		else {
			
			//match direction of field with player or bot  
			if (board.getField(x, y).state == Board.PLAYER) board.getField(x, y).set_player(direction,board.getPlayer(x, y).health_label);
			else if (board.getField(x, y).state == Board.BOT) board.getField(x, y).set_bot(direction,board.getPlayer(x, y).health_label);
		}
	}

	public void shoot(Board board, int direction){
		if(gun_shots > 0) {
			switch (direction){
				case Board.UP:
					for (int i = y-1; i > 0; i--) {
						if (checkPos(board, x, i)) break;
					}
					break;
				case Board.RIGHT:
					for (int i = x+1; i < board.width; i++) {
						if (checkPos(board, i, y)) break;
					}
					break;
				case Board.BOTTOM:
					for (int i = y+1; i < board.length; i++) {
						if (checkPos(board, x, i)) break;
					}
					break;
				case Board.LEFT:
					for (int i = x-1; i > 0; i--) {
						if (checkPos(board, i, y)) break;
					}
					break;
			}
			gun_shots--;
			new java.util.Timer().schedule(new TimerTask() { // reload time
				@Override
				public void run() {
					gun_shots = 1; // ammo rounds
				}
			}, 1000);
		}
	}
	
	
	//checking for empty fields around the player
	public boolean checkDirection(Board board, int direction){
		boolean b = false;
		switch (direction) {
			case Board.UP:
				if(board.getField(x, y-1).state == Board.EMPTY) b = true;
				break;
			case Board.RIGHT:
				if(board.getField(x+1, y).state == Board.EMPTY) b = true;
				break;
			case Board.BOTTOM:
				if(board.getField(x, y+1).state == Board.EMPTY) b = true;
				break;
			case Board.LEFT:
				if(board.getField(x-1, y).state == Board.EMPTY) b = true;
				break;
		}
		return b;
	}
	
	//check for wall
	private boolean checkPos(Board board, int x, int y) {
		int state = board.getField(x, y).state;
		if(state == Board.WALL) return true;
		else if(state == Board.BOT || state==Board.PLAYER){
			if(board.getPlayer(x, y).health > 1)
				board.getPlayer(x, y).decreaseHealth();
			else {
				board.getField(x, y).set_empty();
				if(state == Board.PLAYER) {
					System.out.println("player lost");
					board.setVisible(false);
					new Menu();
				}
				
				if(state==Board.BOT) {
					t.cancel();
					t2.cancel();
					int finish=0;
					for(int i=0;i<board.length-1;i++) {
						for(int j=0;j<board.width-1;j++) {
							if(board.getField(i, j).state==Board.BOT) {
								finish++;
							}
						}
					}
					if(finish==0) {
						System.out.println("playar win");
					}
				}
				
				
			}
			return true;
		}
		return false;
	}

	private void decreaseHealth() {
		health--;
		health_label.setText(String.valueOf(health));
	}
	public void startRandom(Board board) {
		// bot's movement
		if(this.health>0) {
				 t = new java.util.Timer();
				 t2 = new java.util.Timer();
				t.schedule(new TimerTask() { 
					@Override
					public void run() {
						
						//move of bot's after 1 second
						
							int direction = (int) (Math.random() * 4);
							
							//check for the empty fields and then , moving
							while (!checkDirection(board, direction)) direction = (int) (Math.random() * 4);
							move(board, direction);

							// make sure we move in the chosen direction if it's possible, bot's might make Jumps !!
							if(checkDirection(board, direction)) move(board, direction);
						
					}
				}, 0, 1000);
				t2.schedule(new TimerTask() {
					
					
					public void run() {
						shoot(board, board.getField(x, y).direction);
					}
				},500,1000);
	}
	}
}
