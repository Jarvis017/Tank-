import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

public class Board extends JFrame {
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	public static final int BOT = 1001;
	public static final int PLAYER = 1002;
	public static final int WALL = 1003;
	public static final int EMPTY = 1004;
	public static ImageIcon ICON_EARTH = null;
	public static ImageIcon ICON_WALL = null;
	public static ImageIcon ICON_PLAYER_UP = null;
	public static ImageIcon ICON_PLAYER_RIGHT = null;
	public static ImageIcon ICON_PLAYER_BOTTOM = null;
	public static ImageIcon ICON_PLAYER_LEFT = null;
	public static ImageIcon ICON_ENEMY_UP = null;
	public static ImageIcon ICON_ENEMY_RIGHT = null;
	public static ImageIcon ICON_ENEMY_BOTTOM = null;
	public static ImageIcon ICON_ENEMY_LEFT = null;

	public int length, width, size;
	private Field[][] field;
	private Players[] players;

    Board() {
		this.addKeyListener(keyClickHandler);
		
		// size of each house
		length = 12; width = 20; size = 50; 
		createIcons();
		initBoard();

		initPlayers();
	}
    
    //make the field of the board
    private void initBoard() {
        JPanel board = new JPanel(new GridLayout(length, width));
        this.setBounds(0, 0, width * size, length * size);
        this.add(board);
        field = new Field[length][width];

        //add field to the board
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = new Field();
                board.add(field[i][j].label);
            }
        }

        // disable edge of board
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == length - 1 || j == 0 || j == width - 1)
                    field[i][j].set_wall();
            }
        }

        // 40% wall
        int wallCounter = 0;
        while (wallCounter < (0.4 * (length - 2) * (width - 2))){
            int randX = (int) (Math.random()*(width-2) + 1);
            int randY = (int) (Math.random()*(length-2) + 1);
            
         // valid option for a wall
            if (field[randY][randX].state == EMPTY) { 
                field[randY][randX].set_wall();
                wallCounter++;
            }
        }

        board.setVisible(true);
    }

    
    //set icon of the game(player and rotation of it , bot's and rotation)
	private void createIcons(){
		try {
			BufferedImage player_up = ImageIO.read(new File("player_up.png"));
			ICON_PLAYER_UP = new ImageIcon(player_up.getScaledInstance(size, size, Image.SCALE_SMOOTH));
			ICON_PLAYER_RIGHT = new ImageIcon(rotateCw(player_up).getScaledInstance(size, size, Image.SCALE_SMOOTH));
			ICON_PLAYER_BOTTOM = new ImageIcon(rotateCw(rotateCw(player_up)).getScaledInstance(size, size, Image.SCALE_SMOOTH));
			ICON_PLAYER_LEFT = new ImageIcon(rotateCw(rotateCw(rotateCw(player_up))).getScaledInstance(size, size, Image.SCALE_SMOOTH));
			BufferedImage enemy_up = ImageIO.read(new File("enemy_up.png"));
			ICON_ENEMY_UP = new ImageIcon(enemy_up.getScaledInstance(size, size, Image.SCALE_SMOOTH));
			ICON_ENEMY_RIGHT = new ImageIcon(rotateCw(enemy_up).getScaledInstance(size, size, Image.SCALE_SMOOTH));
			ICON_ENEMY_BOTTOM = new ImageIcon(rotateCw(rotateCw(enemy_up)).getScaledInstance(size, size, Image.SCALE_SMOOTH));
			ICON_ENEMY_LEFT = new ImageIcon(rotateCw(rotateCw(rotateCw(enemy_up))).getScaledInstance(size, size, Image.SCALE_SMOOTH));
			ICON_EARTH = new ImageIcon(ImageIO.read(new File("field.png")).getScaledInstance(size, size, Image.SCALE_SMOOTH));
			ICON_WALL = new ImageIcon(ImageIO.read(new File("stone.png")).getScaledInstance(size, size, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//make player and bot's
	private void initPlayers() {
		
		//make 4 player( 1 player and 3 bot's ) 
		players = new Players[4];
		
		//set player in the corner
		players[0] = new Players(1, length-2);
		
		//set first direction of the player
		field[players[0].y][players[0].x].set_player(UP,players[0].health_label);
		
		//set bot's in random field
		for (int i = 1; i < players.length; i++) {
			int initX = (int) (Math.random() * (width-2)) + 1;
			int initY = (int) (Math.random() * (length-2)) + 1;
			players[i] = new Players(initX,initY);
			field[players[i].y][players[i].x].set_bot(LEFT,players[i].health_label);
			// could do this with initX, initY
			try { // make sure bot's have space around them to avoid bugs with movement handler
				if(players[i].y < length - 1 && players[i].x < width - 1) {
					field[players[i].y + 1][players[i].x].set_empty();
					field[players[i].y][players[i].x + 1].set_empty();
					field[players[i].y + 1][players[i].x + 1].set_empty();
				} else {
					field[players[i].y - 1][players[i].x].set_empty();
					field[players[i].y][players[i].x - 1].set_empty();
					field[players[i].y - 1][players[i].x - 1].set_empty();
				}
			} catch (Exception ignored){};
		}
		for(int i=1;i<players.length;i++) {
			players[i].startRandom(this);
		}
		
	}

	//player's key for moving
	private KeyListener keyClickHandler = new KeyListener() {
		@Override public void keyTyped(KeyEvent keyEvent) {}
		@Override
		public void keyPressed(KeyEvent keyEvent) {
			switch (keyEvent.getKeyCode()) {
				case KeyEvent.VK_UP:
					players[0].move(Board.this, UP);
					break;
				case KeyEvent.VK_RIGHT:
					players[0].move(Board.this, RIGHT);
					break;
				case KeyEvent.VK_DOWN:
					players[0].move(Board.this, BOTTOM);
					break;
				case KeyEvent.VK_LEFT:
					players[0].move(Board.this, LEFT);
					break;
				case KeyEvent.VK_SPACE:
					int shootingDirection = field[players[0].y][players[0].x].direction;
					players[0].shoot(Board.this, shootingDirection);
					break;
			}
		}
		@Override public void keyReleased(KeyEvent keyEvent) {}
	};

	//change Coordinates of player or bots 
	public void movePos(int oldX, int oldY, int newX, int newY, int newState, int direction,Players player){
		if (newState == Board.PLAYER) {
			field[newY][newX].set_player(direction,player.health_label);
			field[oldY][oldX].set_empty();
		}
		else if (newState == Board.BOT) {
			field[newY][newX].set_bot(direction,player.health_label);
			field[oldY][oldX].set_empty();
		}
	}

	//rotating the image
	public static BufferedImage rotateCw( BufferedImage img ) 
	{
		int width  = img.getWidth();
		int height = img.getHeight();
		BufferedImage newImage = new BufferedImage(height, width, img.getType() );

		for( int i=0 ; i < width ; i++ )
			for( int j=0 ; j < height ; j++ )
				newImage.setRGB( height-1-j, i, img.getRGB(i,j) );

		return newImage;
	}

	public Field getField(int x, int y){
		return field[y][x];
	}

	public Players getPlayer(int x, int y){
		Players player = null;
		for (Players p:players) {
			if (p.x == x && p.y == y) {
				player = p;
			}
		}
		return player;
	}
}
