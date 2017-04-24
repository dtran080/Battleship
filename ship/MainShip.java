package ship;

import client.Battleship;

public class MainShip extends SHIP {
	
	public MainShip(Battleship prog){
		isDeployed = false;
		length = 4; 
		this.prog = prog;
		shipType = SHIP_TYPE.BATTLESHIP;
	}
	
}
