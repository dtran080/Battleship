package ship;

import client.Battleship;

public class Carrier extends SHIP{
	
	public Carrier(Battleship prog){
		isDeployed = false;
		length = 5;
		this.prog = prog;
		shipType = SHIP_TYPE.CARRIER;
	}
	
}
