package hotpursuit;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;




public class Game extends JPanel implements KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean playing = false;
	private boolean die = false;
	private static int dif;
	
	private ArrayList<Integer> scores;
	protected static int size;
	protected static int[] maze;
	protected static int kocka;
	
	
	//l�trehozom a playert meg a rendoroket
	static Character p1 = new Player();
	
	static Character po1 = new Police();
	static Character po2 = new Police();
	static Character po3 = new Police();
	static Character po4 = new Police();
	
	
	
	public Game() {
		
		scores = new ArrayList<Integer>();
		addKeyListener(this);
		
		maze = new int[kocka * kocka];
		
		setDif(1); // neh�zs�g, alap�rtelemezetten 1
		kocka = 20;	// 20*20 t�mb a p�lya m�rete (ranodm, sz�m, a megfele� �rt�kek v�ltoztat�s�val ez n�velhet�/ cs�kkenthet�)
		size = kocka*24; // 24 pixel egy kocka sz�less�ge
		

	}
	

	public boolean isDie() {
		return die;
	}


	public void setDie(boolean die) {
		this.die = die;
	}



	public void collision(Character police, Character player) {	
		
		//24*24 pixel m�ret� egy kocka ez�rt a collisiont az eg�sz kock�n kell ellen�rizni
		if (player.getX() > police.getX()-(24/2) && player.getX() < police.getX()+(24/2) && player.getY() > police.getY()-(24/2) && player.getY() < police.getY()+(24/2) && playing)
		{die = true;	//ha �ttk�zik akkor meghal, ekkor meg�hv�dik a death f�ggv�ny �s ha az �lete nem nulla akkor csak cs�kken egyel.
		
		}
		
		
	}


	
	public  ArrayList<Integer> FileRead() throws IOException
	{
		
		Scanner s = new Scanner(new File("highScores.txt"));
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNext()){
		    list.add(s.next());
		}
		s.close();
		
		list.add(String.valueOf(p1.getScore()));		
		
		ArrayList<Integer> intlist = new ArrayList<Integer>(list.size());
		 for (String myInt : list) 
         { 
			 intlist.add(Integer.valueOf(myInt)); 
         }
		
		return intlist;
	}
	
	public void FileWrite(ArrayList<Integer> scores2) throws IOException
	{
		
		ArrayList<String> s = new ArrayList<String>(scores2.size());
		 for (Integer myInt : scores2) 
        {
			 s.add(String.valueOf(myInt)); 
        }
		
	        FileWriter writer = new FileWriter("highScores.txt");
	        
	        for (int i=0;i<s.size();i++) {
	            String str = s.get(i);
	            writer.write(str);
	            if(i < s.size()-1)
	            	writer.write("\n");
	            }
	        
	        
	        writer.close();  
	        
        
		
	}
	
	

	public void Maze_check() {

		//megvizsg�lom h�ny darab felvehet� csillag van a mezon
		int db = 0;
		for (int j = 0; j < (kocka * kocka); j++) {
			if (maze[j] == 16)
				db++;

		}
		//meghat�rozom a max pontot
		db = db + p1.getScore();

		boolean nyert = true;
		if (p1.getScore() % db != 0) {
			nyert = false;
		}
		
		//ha �sszeszedte az �sszes csillagot akkor resetel�dik a j�t�k.
		if (nyert) {
			LoadMaze();
			p1.restart(10,11);
		}
	}

	public void death(Character p12) throws IOException {
		p12.setLife(p12.getLife() - 1);
		if (p12.getLife() == 0) {
			playing = false;
			scores = FileRead();
			FileWrite(scores);
			//System.out.println(scores);
			p12.setScore(0);
			
		}
		Restart(po1);
	}


	public void SetGame(int difficulties, Character p12, Character po12, Character po22) {

		switch (difficulties) {
		case 1: {
			p12.setLife(3);
		}
			break;
		case 2: {
			p12.setLife(3);
			po12.SetSpeed(12);
			po22.SetSpeed(12);
			
		}
			break;
		case 3: {
			p12.SetSpeed(24);
		}
			break;

		}

		p1.setScore(0);
		LoadMaze();

	}
	
	public void LoadMaze() {
		int i;
		// random map generator - not working properly
		/*
		 * Random rd = new Random(); for (i = 0; i < kocka * kocka; i++) { if (i<= 20 ||
		 * i >= 380 || i%20==0 || (i+1)%20==0) maze[i] = 17; else if (maze[i] != 17)
		 * {maze[i] = rd.nextInt(2)+16;} else {maze[i] = 0;}
		 * System.out.println(maze[i]); }
		 */
		
		
		/* A MAZE GENER�L�SA / MAGYAR�ZAT
		 * 
		 * A egy t�mb�t t�lt fel 17 �s 16 os sz�mokkal, megadott felt�teleknek megfelel�en
		 * 17 = falllal
		 * 16 = csillagal
		 * Az if felt�ltelben a sz�mok csak random sz�mok, mert valahogy kellett egy olyan mazet gener�lni amit ki is lehet j�tszani 
		 * (nem lesz csillag olyan helyen ahova nem lehet eljutni mert fal z�rja k�rbe pl)
		 * 
		 */
		
		for (i = 0; i < kocka * kocka; i++) {		//map generator with given condition
			if (i <= 20 || i >= 380 || i % 20 == 0 || (i + 1) % 20 == 0)
				maze[i] = 17;
			else if (i % 13 == 2 || i % 17 == 0 || i % 11 == 0 && i != 22) {
				maze[i] = 17;
			} else {
				maze[i] = 16;
			}

		}
		
		
		Restart(po1);
	}

	public void Restart(Character po12) {

		po12.restart(15, 15);
		po2.restart(10, 15);
		po3.restart(3, 5);
		po4.restart(18, 3);
		p1.restart(10,11);
		die = false;
	}

	

	@Override
	public void keyPressed(KeyEvent e) {
		if (playing) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				p1.SetDir('L');
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				p1.SetDir('R');
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				p1.SetDir('U');
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				p1.SetDir('D');
			}  
			else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // back to main menu
				playing = false;
				p1.setScore(0);
			}
		} else {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) { // start game
				playing = true;
				
				
				SetGame(1, p1, po1, po2);
			} else if (e.getKeyCode() == KeyEvent.VK_1) {
				SetGame(1, p1,  po1, po2);
				setDif(1);
			} else if (e.getKeyCode() == KeyEvent.VK_2) {
				SetGame(2, p1, po1, po2);
				setDif(2);

			} else if (e.getKeyCode() == KeyEvent.VK_3) {
				SetGame(3, p1, po1, po2);
				setDif(3);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		

	}


	public static int getDif() {
		return dif;
	}


	public static void setDif(int dif) {
		Game.dif = dif;
	}


	
	

	


	


}