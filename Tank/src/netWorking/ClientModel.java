package netWorking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import undecided.PlayerTank;
import undecided.Point;

import View.MasterView;
import View.Views;

public class ClientModel extends Observable implements Observer {
	String ip;
	public Socket socket = null;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	private Object unKnown;
	private MasterView m;
	// dont forget to set the p after changing to network tank view.
	public PlayerTank p;
	public boolean connected = false;
/**
 * 
 * @param m MasterView
 * @param o ip address
 * this class constructor takes in m and o  and creats a new socket
 * it then call listenStart() to start listening to socket. getting input stream if not null
 */
	public ClientModel(MasterView m, Object o) {
		ip = "127.0.0.1";
		this.m = m;
		if (o instanceof String) {
			ip = (String) o;
		}
		try {
			System.out.println("Trying to connect to remote host on " + ip);
			socket = new Socket(ip, 4000);
			listenStart();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connected();

	}
/**
 * this method will create a thread called listen as game listener
 * and then start it. 
 */
	public void listenStart() {
		Thread listen = new GameListener(this);
		listen.start();
	}

	public void ready() {
		Thread send = new ReadySender();
		send.start();
	}

	public void connected() {
		Thread connected = new ConnectSender();
		connected.start();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	private class ConnectSender extends Thread implements Runnable {
		public void run() {
			try { 
					out = new ObjectOutputStream(socket.getOutputStream());
					out.writeObject(new String("connected"));
					System.out.println("connected sent to host");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ReadySender extends Thread implements Runnable {
		public void run() {
			try {
				out.writeObject(new String("ready"));
				System.out.println("Ready sent to host");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
/**
 * 
 * this class will create a thread, tryint to read the input, and interpret the messages. 
 * 
 *
 */
	private class GameListener extends Thread implements Runnable, Observer {
		private ClientModel cm;
		private boolean first = true;

		public GameListener(ClientModel cm) {
			this.cm = cm;
		}

		@Override
		public void run() {

			while (in == null) {
				try {
					in = new ObjectInputStream(socket.getInputStream());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			while (true) {
				System.out.println("Trying to read");
				try {
					unKnown = in.readObject();
					System.out.println(in);
				}

				catch (SocketException e) {
					System.out.println("Socket Exception, connection lost");
					m.changeView(Views.TITLE, null);
				}

				catch (IOException e) {
					System.out.println("IOException");
					System.out.println(unKnown);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					m.changeView(Views.TITLE, null);
				}

				if (unKnown != null) {
					if (unKnown instanceof String) {
						if (unKnown.equals("Welcome!")) {
							if (first) {
								JOptionPane.showMessageDialog(null,
										"Connected to host!");
								first = false;
							}
						} else if (unKnown.equals("start")) {
							JOptionPane.showMessageDialog(null,
									"Changing to game view");
							cm.startGame();
						} else if (unKnown.equals("up")) {
							p.moveUp();
						} else if (unKnown.equals("down")) {
							p.moveDown();
						} else if (unKnown.equals("left")) {
							p.moveLeft();
						} else if (unKnown.equals("right")) {
							p.moveRight();
						}
					} else {
						SimpleShoot ss = (SimpleShoot) unKnown;
						p.shoot(new Point(ss.c, ss.r), ss.x, ss.y);
					}
				}

			}

		}

		@Override
		public void update(Observable arg0, Object arg1) {
			// TODO Auto-generated method stub

		}

	}
	/**
	 * sending object o to the remote host
	 * @param o
	 */

	public void sendObject(Object o) {
		try {

			out.writeObject(o);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void startGame() {
		m.changeView(Views.NETWORKTANKVIEW, this);
	}

}
