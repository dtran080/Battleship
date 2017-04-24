package ship;

import client.Battleship;

public class Frigate extends SHIP {
	public Frigate(Battleship prog){
		isDeployed = false;
		length = 3;
		this.prog = prog;
		shipType = SHIP_TYPE.FRIGATE;
	}
	
	
}
