package main;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CellRenderer extends JCheckBox implements ListCellRenderer{
	public static final String db [] = {"Carrier","Battleship","Frigate","Submarine","Minesweeper"};
	//SHIP LIST
	   
	  public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
	  {       
	  		setText (" " + db[index]);       
	  		setSelected (isSelected);     
	  		return (this);    
	  } 
}
