package ship;

import main.Battleship;

public class Minesweep extends SHIP{
	
	public Minesweep(Battleship prog){
		isDeployed = false;
		length = 2;
		this.prog = prog;
		shipType = SHIP_TYPE.MINESWEEPER;
	}
	
	
}
