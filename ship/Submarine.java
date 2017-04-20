package ship;

import main.Battleship;

public class Submarine extends SHIP{
	
	public Submarine(Battleship prog){
		length = 3;
		this.prog = prog;
		isDeployed = false;
		shipType = SHIP_TYPE.SUBMARINE;
	}

}
