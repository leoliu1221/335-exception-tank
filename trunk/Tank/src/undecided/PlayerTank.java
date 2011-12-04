package undecided;

import java.awt.Image;

import java.util.LinkedList;
import java.util.Observable;

import javax.swing.ImageIcon;

import rectangles.TankRectangle;

/**
 * 
 * @author Team Exception
 * 
 *         This class is the tank object that is controlled by the player that
 *         can be moved by arrow keys and shoot independently via the mouse. It
 *         contains the tanks location, health, and shape that detects
 *         collisions.
 * 
 */
public class PlayerTank extends Observable {

	// X and Y coordinates for the Tank's location
	private Point p;

	// the speed at which the tank moves across the screen
	private int speed;

	// PlayerTank's associated rectangle
	private TankRectangle t;
	private int health;
	private Direction d;
	private Image img;

	/**
	 * Class constructor
	 * 
	 * @param p
	 *            location of the player controlled tank
	 */
	public PlayerTank(Point p) {
		this.p = p;
		speed = 5;
		health = 1;
		d = Direction.EAST;
		t = new TankRectangle(p.col - 25, p.row - 25);
		img = new ImageIcon("images/tank.png").getImage();

	}

	/**
	 * 
	 * @return TankRectangle returns the shape of the tank that detects
	 *         collisions
	 */
	public TankRectangle getRectangle() {
		return t;
	}

	/**
	 * 
	 * @return Direction returns direction in which the tank is facing
	 */
	public Direction getDirection() {
		return d;
	}

	/**
	 * 
	 * @return Point returns the player controlled tank's position
	 */
	public Point getLocation() {
		return p;
	}
	
	public Image getImage() {
		return img;
	}

	/**
	 * 
	 * @param p
	 *            this location at which the tank is to be set at
	 */
	public void setLocation(Point p) {
		this.p = p;
	}
	
	public int getHealth() {
		return health;
	}


	public void setHealth(int x) {
		if(x == 2) {
			img = new ImageIcon("images/tankShield.png").getImage();
		}
		health = x;
	}

	/**
	 * This class will determine the speed of the tank depending on the terrain
	 * that the tank is traversing across
	 * 
	 * @param t
	 *            this is the terrain in which the tank is on whether grass,
	 *            ice, or sand
	 */
	public void setSpeed(int x) {
		speed = x;

	}

	/**
	 * 
	 * @return int this returns the current speed of the tank
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * 
	 * @return Point returns the point that is above the tank's current position
	 */
	public boolean moveUp() {
		LinkedList<Obstacle> obs = TankView.obstacleList;
		LinkedList<EnemyTank> enemies = TankView.enemyList;
		d = Direction.NORTH;
		p = new Point(p.row - this.speed, p.col);
		t = new TankRectangle(p.col - 25, p.row - 25);
		for(int i = 0; i < enemies.size(); i++) {
			EnemyTank e = enemies.get(i);
			if(e.getRectangle().intersects(t)) {
				p = new Point(p.row + this.speed, p.col);
				t = new TankRectangle(p.col - 25, p.row - 25);
				return false;
			}
		}
		for (int i = 0; i < obs.size(); i++) {
			Obstacle o = obs.get(i);
			if (o instanceof ImmovableBlock) {
				ImmovableBlock b = (ImmovableBlock) o;
				if (b.getRectangle().intersects(t)) {
					p = new Point(p.row + this.speed, p.col);
					t = new TankRectangle(p.col - 25, p.row - 25);
					return false;
				
				}
			}
			if (o instanceof Crate) {
				Crate c = (Crate) o;
				if(c.getRectangle().intersects(t)) {
					if(!c.move(d)) {
						p = new Point(p.row + this.speed, p.col);
						t = new TankRectangle(p.col - 25, p.row - 25);
						return false;
					}
				}
			}
			if (o instanceof TNT) {
				TNT c = (TNT) o;
				if(c.getRectangle().intersects(t)) {
					if(!c.move(d)) {
						p = new Point(p.row + this.speed, p.col);
						t = new TankRectangle(p.col - 25, p.row - 25);
						return false;
					}
				}
			}

		}

		if (p.row < 30) {
			p = new Point(p.row + this.speed, p.col);
			t = new TankRectangle(p.col - 25, p.row - 25);
			return false;
		}
		notifyObservers(this);
		setChanged();
		return true;
	}

	/**
	 * 
	 * @return Point returns the point that is below the tank's current position
	 */
	public boolean moveDown() {
		LinkedList<Obstacle> obs = TankView.obstacleList;
		LinkedList<EnemyTank> enemies = TankView.enemyList;
		d = Direction.SOUTH;
		p = new Point(p.row + this.speed, p.col);
		t = new TankRectangle(p.col - 25, p.row - 25);
		for(int i = 0; i < enemies.size(); i++) {
			EnemyTank e = enemies.get(i);
			if(e.getRectangle().intersects(t)) {
				p = new Point(p.row - this.speed, p.col);
				t = new TankRectangle(p.col - 25, p.row - 25);
				return false;
			}
		}
		for (int i = 0; i < obs.size(); i++) {
			Obstacle o = obs.get(i);
			if (o instanceof ImmovableBlock) {
				ImmovableBlock b = (ImmovableBlock) o;
				if (b.getRectangle().intersects(t)) {
					p = new Point(p.row - this.speed, p.col);
					t = new TankRectangle(p.col - 25, p.row - 25);
					return false;
				}

			}
			if (o instanceof Crate) {
				Crate c = (Crate) o;
				if(c.getRectangle().intersects(t)) {
					if(!c.move(d)) {
						p = new Point(p.row - this.speed, p.col);
						t = new TankRectangle(p.col - 25, p.row - 25);
						return false;
					}
				}
			}
			if (o instanceof TNT) {
				TNT c = (TNT) o;
				if(c.getRectangle().intersects(t)) {
					if(!c.move(d)) {
						p = new Point(p.row - this.speed, p.col);
						t = new TankRectangle(p.col - 25, p.row - 25);
						return false;
					}
				}
			}
		}

		if (p.row > 665) {
			p = new Point(p.row - this.speed, p.col);
			t = new TankRectangle(p.col - 25, p.row - 25);
			return false;
		}
		notifyObservers(this);
		setChanged();
		return true;
	}

	/**
	 * 
	 * @return Point returns the point that is to the right of the tank's
	 *         current position
	 */
	public boolean moveRight() {
		LinkedList<Obstacle> obs = TankView.obstacleList;
		LinkedList<EnemyTank> enemies = TankView.enemyList;
		d = Direction.EAST;
		p = new Point(p.row, p.col + this.speed);
		t = new TankRectangle(p.col - 25, p.row - 25);
		for(int i = 0; i < enemies.size(); i++) {
			EnemyTank e = enemies.get(i);
			if(e.getRectangle().intersects(t)) {
				p = new Point(p.row, p.col - this.speed);
				t = new TankRectangle(p.col - 25, p.row - 25);
				return false;
			}
		}
		for (int i = 0; i < obs.size(); i++) {
			Obstacle o = obs.get(i);
			if (o instanceof ImmovableBlock) {
				ImmovableBlock b = (ImmovableBlock) o;
				if (b.getRectangle().intersects(t)) {
					p = new Point(p.row, p.col - this.speed);
					t = new TankRectangle(p.col - 25, p.row - 25);
					return false;
				}

			}
			if (o instanceof Crate) {
				Crate c = (Crate) o;
				if(c.getRectangle().intersects(t)) {
					if(!c.move(d)) {
						p = new Point(p.row, p.col - this.speed);
						t = new TankRectangle(p.col - 25, p.row - 25);
						return false;
					}
				}
			}
			if (o instanceof TNT) {
				TNT c = (TNT) o;
				if(c.getRectangle().intersects(t)) {
					if(!c.move(d)) {
						p = new Point(p.row, p.col - this.speed);
						t = new TankRectangle(p.col - 25, p.row - 25);
						return false;
					}
				}
			}

		}

		if (p.col > 955) {
			p = new Point(p.row, p.col - this.speed);
			t = new TankRectangle(p.col - 25, p.row - 25);
			return false;
		}
		notifyObservers(this);
		setChanged();
		return true;
	}

	/**
	 * 
	 * @return Point returns the point that is to the left of the tank's current
	 *         position
	 */
	public boolean moveLeft() {
		LinkedList<Obstacle> obs = TankView.obstacleList;
		LinkedList<EnemyTank> enemies = TankView.enemyList;
		d = Direction.WEST;
		p = new Point(p.row, p.col - this.speed);
		t = new TankRectangle(p.col - 25, p.row - 25);
		for(int i = 0; i < enemies.size(); i++) {
			EnemyTank e = enemies.get(i);
			if(e.getRectangle().intersects(t)) {
				p = new Point(p.row, p.col + this.speed);
				t = new TankRectangle(p.col - 25, p.row - 25);
				return false;
			}
		}
		for (int i = 0; i < obs.size(); i++) {
			Obstacle o = obs.get(i);
			if (o instanceof ImmovableBlock) {
				ImmovableBlock b = (ImmovableBlock) o;
				if (b.getRectangle().intersects(t)) {
					p = new Point(p.row, p.col + this.speed);
					t = new TankRectangle(p.col - 25, p.row - 25);
					return false;
				}

			}
			if (o instanceof Crate) {
				Crate c = (Crate) o;
				if(c.getRectangle().intersects(t)) {
					if(!c.move(d)) {
						p = new Point(p.row, p.col + this.speed);
						t = new TankRectangle(p.col - 25, p.row - 25);
						return false;
					}
				}
			}
			if (o instanceof TNT) {
				TNT c = (TNT) o;
				if(c.getRectangle().intersects(t)) {
					if(!c.move(d)) {
						p = new Point(p.row, p.col + this.speed);
						t = new TankRectangle(p.col - 25, p.row - 25);
						return false;
					}
				}
			}

		}

		if (p.col < 30) {
			p = new Point(p.row, p.col + this.speed);
			t = new TankRectangle(p.col - 25, p.row - 25);
			return false;
		}
		notifyObservers(this);
		setChanged();
		return true;
	}

	/**
	 * The method shoot determines the direction of the tank and shoots a single
	 * animated projectile in the appropriate direction
	 */
	
	
	// new version!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void shoot(Point p, int x, int y) {
		
		PlayerProjectile missle = new PlayerProjectile(p, x, y, this);
			notifyObservers(missle);
			setChanged();
	
	}
	
	

	/**
	 * 
	 * @param damage
	 *            this is the damage that the player controlled tank is to
	 *            receive
	 */
	public void recieveDamage(int damage) {
		health = health - damage;
		if (this.isDead()) {
			t = new TankRectangle(-100, -100);
			p = new Point(-1, -1);
			TankView.tankList.remove(this);
		}
		else if (health == 1) {
			ImmuneThread it = new ImmuneThread();
			it.start();
		}

	}
	
	private class ImmuneThread extends Thread {
		private int timePassed;
		
		public ImmuneThread() {
			timePassed = 0;
		}
		
		@Override
		public void run() {
			while(timePassed < 17) {
			if(timePassed < 16) {
				health = 10000;
			}
			if(timePassed == 16) {
				health = 1;
			}
			if(timePassed % 2 == 0) {
				img = new ImageIcon("images/tank.png").getImage(); 
			}
			if(timePassed %2 == 1) {
				img = new ImageIcon("images/tankShield.png").getImage(); 
				
			}
			timePassed++;
			try {
				Thread.sleep(125);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	}
	/**
	 * 
	 * @return boolean returns whether this player controlled tank is alive or
	 *         at zero health
	 */
	public boolean isDead() {
		return (health == 0);
	}
	
}
