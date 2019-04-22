package KP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;

public class KPFrame {
	static int x = 10;
	private Socket s = null;
	private JFrame f;
	private JLayeredPane jpl;
	private JButton selectedCard;
	private JButton playButton;
	private List<JButton> al = new LinkedList<JButton>();
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private final static int WIDTH = 960;
	private final static int HEIGTH = 530;
	private String dir = null;
	public KPFrame() {
		try {
			s = new Socket("127.0.0.1", 8001);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void launchFrame() {
		f = new JFrame();
		f.setSize(WIDTH, HEIGTH);
		f.setLocation(300, 5);
		f.setVisible(true);
		f.setResizable(false);
		/*
		 * f.addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) { try { dos.close();
		 * dis.close(); s.close(); } catch (IOException e1) { e1.printStackTrace(); }
		 * 
		 * super.windowClosing(e); System.exit(0); }
		 * 
		 * });
		 */
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setAlwaysOnTop(true);
		Direction();
		backgroundPicture();
		this.faPaiButton();
		thirteenCardsButtons();

		playButton();

		new Thread(new RevServe()).start();
		f.setContentPane(jpl);

	}

	private void faPaiButton() {
		JButton faButton = new JButton("准备");
		faButton.setSize(60, 55);
		faButton.setLocation(10, 10);
		faButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dos.writeInt(0);
					faButton.setVisible(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		jpl.add(faButton, new Integer(12));
		f.setContentPane(jpl);
	}

	public void Direction() {
		try {
			dir = dis.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void thirteenCardsButtons() {
		jpl.setSize(WIDTH, HEIGTH);
		String str = null;
		cardMonitor cm = new cardMonitor();
		for (int i = 0; i < 13; i++) {
			try {
				str = dis.readUTF();
				JButton jb = new JButton();
				jb.setSize(107, 167);
				jb.setLocation(150 + 50 * i, 330);
				ImageIcon img = new ImageIcon("img//" + str);
				jb.setName(str);
				jb.setIcon(img);
				jb.addActionListener(cm);
				al.add(jb);
				jpl.add(jb, new Integer(i + 1));
				f.setContentPane(jpl);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void playButton() {

		playButton = new JButton("出牌");
		playButton.setLocation(80, 220);
		playButton.setSize(60, 55);
		playButton.setVisible(false);
		jpl.add(playButton, new Integer(14));
		playButton.addActionListener(new playButtonMonitor());

	}

	public void backgroundPicture() {
		jpl = new JLayeredPane();
		JLabel jlb = new JLabel();
		ImageIcon icon = new ImageIcon("img//牌桌图片.jpg");
		jlb.setSize(1280, 1024);
		jlb.setIcon(icon);
		jpl.add(jlb);
		f.setContentPane(jpl);
	}

	public static void main(String[] args) {
		new KPFrame().launchFrame();
	}

	class cardMonitor implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			for (int i = 0; i < al.size(); i++) {
				JButton jb = al.get(i);
				if (e.getSource() == jb) {
					if (jb.getY() == 270) {
						jb.setLocation(jb.getX(), 330);
						selectedCard = null;
						playButton.setVisible(false);
					} else {
						jb.setLocation(jb.getX(), 270);
						selectedCard = jb;
						playButton.setVisible(true);
					}
					for (int j = 0; j < al.size(); j++) {
						if (j != i) {
							al.get(j).setLocation(al.get(j).getX(), 330);
						}

					}

				}

			}

		}

	}

	class playButtonMonitor implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			selectedCard.setLocation(400, 165);
			try {
				dos.writeUTF(dir);
				dos.writeUTF(selectedCard.getName());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			jpl.setLayer(selectedCard, x++);
			al.remove(selectedCard);
			for (int i = 0; i < al.size(); i++) {
				al.get(i).setLocation(150 + 50 * i, 330);
			}
			playButton.setVisible(false);

		}

	}

	class RevServe implements Runnable {

		String str = null;
		JButton jb = null;

		@Override
		public void run() {
			String dir2 = null;
			int x = 100;
			ImageIcon img;
			while (true) {
				try {
					dir2 = dis.readUTF();
					str = dis.readUTF();
					jb = new JButton();
					img = new ImageIcon("img//" + str);
					jb.setIcon(img);
					jb.setSize(107, 167);
					switch (dir2) {
					case "东":
						jb.setLocation(550, 85);
						break;
					case "南":
						switch (dir) {
						case "东":
							jb.setLocation(550, 85);
							break;
						case "西":
							jb.setLocation(250, 85);
							break;
						case "北":
							jb.setLocation(400, 0);
							break;
						}
						break;
					case "西":
						jb.setLocation(250, 85);
						break;
					case "北":
						jb.setLocation(400, 0);
						break;
					}

					jpl.add(jb, new Integer(x++));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}
}
