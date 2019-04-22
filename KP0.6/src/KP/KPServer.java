package KP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class KPServer {

	static int x = 13;
	private ServerSocket ss = null;
	private Socket s = null;
	private List<Socket> ls = new LinkedList<Socket>();
	private String[] fiftyTwoCards = new KP().fiftyTwoCards();
	private String[] dir = new String[4];
	int count = 0;
	public KPServer() {
		try {
			ss = new ServerSocket(8001);
		} catch (IOException e) {
			e.printStackTrace();
		}
		dir[0] = "南";
		dir[1] = "西";
		dir[2] = "北";
		dir[3] = "东";

	}

	public void startServer() {
		for (int i = 0; i < 4; i++) {
			try {
				s = ss.accept();
				ls.add(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Thread(new MoreClient(s, dir[i])).start();
		
		}
		

	}

	public void fapai() {
		int i = 0;

		DataOutputStream dos = null;

		while (i < 52) {
			for (int k = 0; k < 4; k++) {
				try {
					dos = new DataOutputStream(ls.get(k).getOutputStream());
					dos.writeUTF(fiftyTwoCards[i++]);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	
	class MoreClient implements Runnable {

		private Socket s = null;
		private DataOutputStream dos = null;
		private DataInputStream dis = null;
		private String dir = null;

		public MoreClient(Socket s, String dir) {
			this.s = s;

			this.dir = dir;
			try {
				dos = new DataOutputStream(s.getOutputStream());
				dis = new DataInputStream(s.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void clientDirection() {
			try {
				dos.writeUTF(dir);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void recfapai() {
			int k = 1;
			try {
				k = dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(k == 0)
				count++;
			if(count == 4)
				fapai();
		
		}

		public void recCardFromClient() {
			String str2 = null;
			String dir = null;

			while (true) {
				try {
					dir = dis.readUTF();
					str2 = dis.readUTF();
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (int i = 0; i < ls.size(); i++) {
					if (ls.get(i) != this.s) {

						try {
							dos = new DataOutputStream(ls.get(i).getOutputStream());
							dos.writeUTF(dir);
							dos.writeUTF(str2);
							System.out.println(str2);
							dos.flush();
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}

				}
			}

		}

		@Override
		public void run() {
			this.clientDirection();
			this.recfapai();
			this.recCardFromClient();

		}

	}

	public static void main(String[] args) {
		new KPServer().startServer();
	}

}
