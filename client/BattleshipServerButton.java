package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class BattleshipServerButton extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 123L;
	//server Button
	private int y,x;
	private Battleship prog;
	boolean occupy;
	private String coord;
	public boolean isOccupy(){
		return occupy;
	}
	public BattleshipServerButton(int y, int x, Battleship prog){
		this.y = y;
		this.x = x;
		this.occupy = false;
		this.prog = prog;
		StringBuilder coord = new StringBuilder();
		coord.append(String.valueOf((char)(x+65)));
		coord.append(String.valueOf(y+1));
		this.coord= coord.toString();
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (prog.connectReady && prog.clientTurn){ //if the connectBtn is Click
					BattleshipServerButton btn = (BattleshipServerButton)e.getSource();
					prog.clientTurn = false;
					prog.setClientMessage(btn.coord);
					btn.setEnabled(false);
				
				}		
			}
		});
	}
	
}
