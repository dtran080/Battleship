package server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import server.BattleshipServer;

public class SHIP {
	protected SHIP_TYPE shipType;
	protected BattleshipServer prog; 
	private int startY,startX,endY,endX; //first coordinate 0-9
	protected int length,order; //length of ship
	private List<Point> endCoord;
	protected boolean sunk, isDeployed;
	
	public boolean isSunk(){return false;}
	public SHIP(int length,BattleshipServer prog, int order){
		this.order = order;
		this.length = length;
		isDeployed = false;
		this.prog = prog;
	}
	public void deploy(){
		isDeployed = true;
	}
	public void setStartCoord(int y, int x){
		if (!isDeployed){
			this.startY = y;
			this.startX = x;
			endCoord = new ArrayList<Point>();
			availableEndCoord();
		} else {
			JOptionPane.showMessageDialog(null,"Ship's already deployed");
		}
	}
	public void setEndCoord(int y,int x){
		if (isDeployed){
			this.endY = y;
			this.endX = x;
			this.prog.placeShip(startY, startX, endY, endX);
		}
	}
	public Point[] getEndCoord(){
		Point[] pArr = new Point[endCoord.size()];
		for (int i=0;i<pArr.length;i++){
			pArr[i] = endCoord.get(i);
		}
		return pArr;
	}
	
	private void availableEndCoord(){
		Point p;
		int[][] direction = {{-length+1,0},{0,-length+1},{0,length-1},{length-1,0}};
		for (int[] dir:direction){
			int y1 = startY+dir[0];
			int x1 = startX+dir[1];
			if (BattleshipServer.inBound(y1,x1)){	//in bound
				if (prog.isFree(startY, startX, y1, x1)){ //free block
					p = new Point();
					p.y = y1;
					p.x = x1;
					endCoord.add(p);
				}
			}
		}
	}
	
}
enum SHIP_TYPE{CARRIER,BATTLESHIP,FRIGATE,SUBMARINE,MINESWEEPER} //5,4,3,3,2