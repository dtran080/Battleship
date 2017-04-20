package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class BattleshipServerButton extends JButton{
	//server Button
	private int y,x;
	private Battleship prog;
	boolean occupy;
	public boolean isOccupy(){
		return occupy;
	}
	public BattleshipServerButton(int y, int x, Battleship prog){
		this.y = y;
		this.x = x;
		this.occupy = false;
		this.prog = prog;
		BattleshipServerActionListener bsal = new BattleshipServerActionListener(y,x,prog);
		this.addActionListener(bsal);
	}
	@Override
	public String toString(){
		String coord =String.valueOf((char)(x+65));
		coord+=String.valueOf(y+1);
		return coord;
	}
	private class BattleshipServerActionListener implements ActionListener{
		private int y,x;
		private Battleship prog;
		public BattleshipServerActionListener(int y, int x, Battleship prog){
			this.y=y;
			this.x=x;
			this.prog = prog;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			//print out coordinate
			String coord =String.valueOf((char)(x+65));
			coord+=String.valueOf(y+1);
			System.out.printf("Server: %s\n",coord);
			
		}
		
	}
}
