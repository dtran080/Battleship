package ship;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.Battleship;

abstract class SHIP {
	protected SHIP_TYPE shipType;
	protected Battleship prog; 
	private final int SIZE = 10;
	private int startY,startX,endY,endX; //first coordinate 0-9
	protected int length; //length of ship
	private List<Point> endCoord;
	protected boolean sunk, isDeployed;
	
	public boolean isSunk(){return false;}
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
			this.prog.placeShip(startY, startX, endY, endX, shipType.ordinal());
		}
	}
	public Point[] getEndCoord(){
		Point[] pArr = new Point[endCoord.size()];
		for (int i=0;i<pArr.length;i++){
			pArr[i] = endCoord.get(i);
		}
		return pArr;
	}
	private boolean inBound(int y, int x){
		return 0<=y && y<SIZE && 0<=x && x<SIZE;			
	}
	private void availableEndCoord(){
		Point p;
		int[][] direction = {{-length+1,0},{0,-length+1},{0,length-1},{length-1,0}};
		for (int[] dir:direction){
			int y1 = startY+dir[0];
			int x1 = startX+dir[1];
			if (inBound(y1,x1)){	//in bound
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