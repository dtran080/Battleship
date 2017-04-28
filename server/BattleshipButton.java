package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BattleshipButton extends JButton { 
	//Server Board button
	private int y,x;
	private BattleshipServer prog;
	boolean occupy;
	
	public boolean isOccupy(){
		return occupy;
	}
	public BattleshipButton(int y, int x, BattleshipServer prog){
		this.y = y;
		this.x = x;
		this.occupy = false;
		this.prog = prog;
		BattleshipButtonActionListener bbal = new BattleshipButtonActionListener(y,x);
		this.addActionListener(bbal);
	}
	@Override
	public String toString(){
		return prog.convertCoord(y, x);
	}
	private class BattleshipButtonActionListener implements ActionListener{
		private int y,x;
		public BattleshipButtonActionListener(int y, int x){
			this.y=y;
			this.x=x;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!prog.connectReady){	
				//before the server connected to Client
				BattleshipButton source = (BattleshipButton)e.getSource();
				if (!source.isOccupy()){//print out coordinate				
					System.out.printf("Server select: %s\n",source.toString());				
					prog.displayAvailCoord(y, x);
				} else {
					JOptionPane.showMessageDialog(null, "Can't place your ship here!");
				}
			}
		}		
	}
	
}
