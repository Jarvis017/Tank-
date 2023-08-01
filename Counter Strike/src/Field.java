import javax.swing.*;
import java.awt.*;

public class Field {

	public int state;
	public int direction;
	public JLabel label;
	public JPanel frame;

	public Field() {
		super();
		state = Board.EMPTY;
		direction = Board.UP;
		label = new JLabel(Board.ICON_EARTH);
		frame = new JPanel();
		frame.setPreferredSize(new Dimension(50,50));
		frame.add(label, BorderLayout.CENTER);
	}
	
	//change the field to wall
	public void set_wall() {
		state = Board.WALL;
		this.label.setIcon(Board.ICON_WALL);
	}
	
	//change the player direction
	public void set_player(int direction,JLabel health) {
		state = Board.PLAYER;
		Dimension d = this.label.getSize();
		health.setSize(d);
		this.direction = direction;
		switch (direction){
			case Board.UP:
				this.label.setIcon(Board.ICON_PLAYER_UP);
				this.label.add(health);
				break;
			case Board.RIGHT:
				this.label.setIcon(Board.ICON_PLAYER_RIGHT);
				this.label.add(health);
				break;
			case Board.BOTTOM:
				this.label.setIcon(Board.ICON_PLAYER_BOTTOM);
				this.label.add(health);
				break;
			case Board.LEFT:
				this.label.setIcon(Board.ICON_PLAYER_LEFT);
				this.label.add(health);
				break;
		}
	}
	
	//change the bot's direction
	public void set_bot(int direction,JLabel health) {
		state = Board.BOT;
		Dimension d = this.label.getSize();
		health.setSize(d);
		this.direction = direction; 
		switch (direction){
			case Board.UP:
				this.label.setIcon(Board.ICON_ENEMY_UP);
				this.label.add(health);
				break;
			case Board.RIGHT:
				this.label.setIcon(Board.ICON_ENEMY_RIGHT);
				this.label.add(health);
				break;
			case Board.BOTTOM:
				this.label.setIcon(Board.ICON_ENEMY_BOTTOM);
				this.label.add(health);
				break;
			case Board.LEFT:
				this.label.setIcon(Board.ICON_ENEMY_LEFT);
				this.label.add(health);
				break;
		}
	}
	
	//make the field empty
	public void set_empty() {
		this.state = Board.EMPTY;
		this.label.setIcon(Board.ICON_EARTH);
		this.label.setText("");
	}
}

