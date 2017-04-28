package server;

//import static server.BattleshipServer.sock;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class BattleshipClientButton extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//the other board's button
	private int y,x;
	private BattleshipServer prog;
	boolean occupy;
	private String coord = "";
	public boolean isOccupy(){
		return occupy;
	}
	public BattleshipClientButton(int y, int x, BattleshipServer prog){
		this.y = y;
		this.x = x;
		this.occupy = false;
		this.prog = prog;
		StringBuilder coord = new StringBuilder();
		coord.append(String.valueOf((char)(x+65)));
		coord.append(String.valueOf(y+1));
		this.coord = coord.toString();
		this.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (prog.connectReady&&prog.serverTurn){
					BattleshipClientButton btn = (BattleshipClientButton) e.getSource();
					prog.serverTurn = false;
					prog.serverMessage = btn.coord;	
					btn.setEnabled(false);				
			}
			}
		});
	}
}
