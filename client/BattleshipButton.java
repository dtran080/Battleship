package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import shipClient.*;
public class BattleshipButton extends JButton { 
	//Client Board button
	private int y,x;
	private Battleship prog;
	boolean occupy;
	
	public boolean isOccupy(){
		return occupy;
	}
	public BattleshipButton(int y, int x, Battleship prog){
		this.y = y;
		this.x = x;
		this.occupy = false;
		this.prog = prog;
		this.addActionListener(new BattleshipButtonActionListener());
	}
	@Override
	public String toString(){
		return prog.convertCoord(y, x);
	}
	private class BattleshipButtonActionListener implements ActionListener{
		private String coord;
		public BattleshipButtonActionListener(){
			this.coord =String.valueOf((char)(x+65));
			coord+=String.valueOf(y+1);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!prog.connectReady){	
				//before client connected to the server
				BattleshipButton source = (BattleshipButton)e.getSource();
				if (!source.isOccupy()){//print out coordinate
					System.out.printf("Client select: %s\n",coord);				
					prog.displayAvailCoord(y, x);
				} else {
					JOptionPane.showMessageDialog(null, "Can't place your ship here!");
				}
			}
		}		
	}
	
}
