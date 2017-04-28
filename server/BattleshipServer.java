package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleshipServer extends JFrame {
	
	BattleshipButton[][] ourBoard;	//server board
	BattleshipClientButton[][] theirBoard; //client board
	final static int size = 10;
	int btnSize = 40;
	JPanel uPanel, cPanel, uShip; //panel's list
	JButton connectBtn;
	JList<String> shipList;
	private int[] shipLength = {5,4,3,3,2};
	ArrayList<Integer> deployedShip;
	//ship type
	SHIP[] shipArr = new SHIP[5];
	int selectedShip = -1;
	String serverMessage;
	ServerSocket serverS;
	boolean connectReady = false;
	volatile boolean serverTurn = false;
	private Vector<Vector<Point>> shipCoord;
	private final String[] command = {"READY","HIT","MISS","SUNK"};
	private int ourShipCount,theirShipCount;
	private final Color[] shipColor = {Color.BLUE,Color.green,Color.ORANGE,Color.yellow,Color.MAGENTA};
	
	public BattleshipServer(){ 
		//main frame
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("BattleshipServer Server");		
		setBounds(0,0,btnSize*(size+1)*5/2,btnSize*(size+1));
		getContentPane().setLayout(null);
		//change windows
		initializeWindow();
		//add panel to frame
		add(uPanel);
		add(uShip);
		add(cPanel);
		
	}

	private void initializeWindow() {
		//user's panel and board
		String lblvalue = "";
		uPanel = new JPanel(); 
		setLayout(null);	
		uPanel.setLayout(new GridLayout(size+1, size+1));
		uPanel.setBounds(0,0,btnSize*(size+1),btnSize*(size+1));
		uPanel.add(new JLabel("     "));
		for (int i=1;i<=size;i++){
			lblvalue = String.format("%4s", String.valueOf(Character.toChars(i+64)));
			uPanel.add(new JLabel(lblvalue));
		}
		ourBoard= new BattleshipButton[size][size];
		for (int i=0;i<size;i++){
			lblvalue = String.format("%2d", i+1);
			uPanel.add(new JLabel(lblvalue));
			for (int j=0;j<size;j++){
				ourBoard[i][j] = new BattleshipButton(i,j,this);
				ourBoard[i][j].setPreferredSize(new Dimension(btnSize,btnSize));
				uPanel.add(ourBoard[i][j]);
			}
		}
		//ship list
		selectedShip = -1;		//index of selected ship
		shipCoord = new Vector<Vector<Point>>();
		ourShipCount=theirShipCount=5;
		uShip = new JPanel(new BorderLayout());	
		uShip.setBounds(btnSize*(size+1),0,btnSize*(size+1)/2,btnSize*(size+1));
		JLabel lbl1 = new JLabel("Ship list: ");
		lbl1.setSize(50,btnSize);
		uShip.add(lbl1,BorderLayout.NORTH);	
		//Ship choice button
		shipList = new JList(CellRenderer.db);
	    shipList.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
	    shipList.setCellRenderer(new CellRenderer());
  
	    
	    class LL implements ListSelectionListener { 
	    	//selection listener
	    	@Override
	    	public void valueChanged (ListSelectionEvent e) {
	    		if (e.getValueIsAdjusting()==true) 
	    			return;
	    		final JList l = (JList) e.getSource();
	    		final int selected = l.getSelectedIndex();
	    		System.out.printf ("ClisetStartCoordent Selecting: %s\n",CellRenderer.db[selected]);
	    		l.setSelectedIndex(selected);
	    		selectedShip = selected;	//change the index of selected ship
		     }
	    }	    
	    shipList.addListSelectionListener(new LL());
	    shipList.setSize(80,btnSize*CellRenderer.db.length);
	    
	    deployedShip = new ArrayList<Integer>(); //list of deployed ship	    
	    uShip.add(shipList,BorderLayout.CENTER);	//add the list to the panel
	    //connect Button
	    connectBtn = new JButton("Connect");
	    connectBtn.setPreferredSize(new Dimension(50,20));
	    connectBtn.setEnabled(false);
	    connectBtn.putClientProperty("prog", this);
	    try {
			serverS = new ServerSocket(9090,100);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    connectBtn.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				btn.setEnabled(false);
				BattleshipServer prog = (BattleshipServer)btn.getClientProperty("prog"); //reference to Battleship
				prog.connectReady = true;
				//System.out.println("Server is running...");
				prog.runServer();
				
			}
		});
	    uShip.add(connectBtn,BorderLayout.SOUTH);
	    serverMessage = new String();
		
		//computer board //Server
		cPanel = new JPanel(); 
		cPanel.setLayout(new GridLayout(size+1, size+1));
		cPanel.setBounds(btnSize*(size+1)*3/2,0,btnSize*(size+1),btnSize*(size+1));
		cPanel.add(new JLabel("     "));
		for (int i=1;i<=size;i++){
			lblvalue = String.format("%4s", String.valueOf(Character.toChars(i+64)));
			cPanel.add(new JLabel(lblvalue));
		}
		
		//initialize Server's Board
		theirBoard= new BattleshipClientButton[size][size];
		for (int i=0;i<size;i++){
			lblvalue = String.format("%2d", i+1);
			cPanel.add(new JLabel(lblvalue));
			for (int j=0;j<size;j++){
				theirBoard[i][j] = new BattleshipClientButton(i,j,this);
				theirBoard[i][j].putClientProperty("prog", this);
				theirBoard[i][j].putClientProperty("coord", new int[]{i,j});
				theirBoard[i][j].setPreferredSize(new Dimension(btnSize,btnSize));
				cPanel.add(theirBoard[i][j]);
			}
		}
		//ship and play	
	}
	
	static Socket sock;
	void runServer(){
		try{
			if (connectReady){
				sock = serverS.accept();
				DisposableServer localServer = new DisposableServer(sock);
				localServer.start();
				
			}
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
	public int[] convertBoardCoordtoPoint(String coord){
		int x1=coord.charAt(0)-65;
		int y1=Integer.parseInt(coord.substring(1))-1;
		return new int[]{y1,x1};
	}
	public int isSunk(){
		for(int i=0;i<shipLength.length;i++){
			if(shipLength[i]==0){
				shipLength[i]--;
				ourShipCount--;
				return i;
			}
		}
		return -1;
	}
	private class DisposableServer extends Thread 
	{
		DataInputStream reader;
		Socket socket;
		DataOutputStream writer;		
		public DisposableServer(Socket sock) {
			try {
				socket = sock;
				reader = new DataInputStream(socket.getInputStream());
				writer = new DataOutputStream(socket.getOutputStream()); 
				
			}catch (IOException ioe) { } 
		}
		public void run() {
			try
			{				
				writer.writeUTF(command[0]); //READY
		        String clientCoord = reader.readUTF();
		        if (clientCoord.equals("READY")){	//READY
		        	System.out.println("READY received from client");
		        	serverTurn = false;        
			        while (ourShipCount>0&&theirShipCount>0){	//infinite terminate when the game is over
			        	//System.out.printf("Server Turn: %s \n",String.valueOf(serverTurn));
			        	if (!serverTurn){ //client's attacking
			        		clientCoord = reader.readUTF();        		
							System.out.println("Server read: " + clientCoord +" from client");
							int res = fire(clientCoord); //result after hit;
							System.out.printf("Client %s the target\n",command[res]);
							writer.writeUTF(command[res]);	//send result to the client
							serverTurn = true;
			        	}
			        	else { //server's attacking	, serverTurn = true      
			        		while (serverTurn){
			        			
			        		}        		
			        		System.out.println("Server sending: "+serverMessage);
			        		writer.writeUTF(serverMessage);
			        		int[] coord = convertBoardCoordtoPoint(serverMessage);
							String res = reader.readUTF(); //too see if hit or miss
							System.out.println(res);
							theirBoard[coord[0]][coord[1]].setBackground((res.equals("MISS"))?Color.cyan:Color.RED); 
							if(res.equals("SUNK")){
								theirShipCount--;
								JOptionPane.showMessageDialog(null, "Ship is sunk!");
							}
			        	}
			        }
			           	JOptionPane.showMessageDialog(null, (ourShipCount==0)?"YOU LOSE":"YOU WON");
			           	//close
			           	socket.close();
			           	serverS.close();
			        
		        }
			}catch (IOException ioe) { ioe.printStackTrace(); }
		} // run
	} // DisposableServer
	
	public void displayAvailCoord(int y,int x){
		Point[] pos;
		if (deployedShip.contains(selectedShip)){
			JOptionPane.showMessageDialog(null,"Ship's already deployed");
			return;
		}		
		if (selectedShip!=-1){
			int shipLength = -1;
			switch (selectedShip){
				case 0: //carrier; length 5
					shipLength = 5;			
					break;
				case 1: //battleship, length 4 
					shipLength = 4;
					break;
				case 2:case 3: //frigate, submarine
					shipLength = 3;
					break;
				case 4:
					shipLength = 2;
					break;					
			}
			shipArr[selectedShip] = new SHIP(shipLength,this,selectedShip); //initialize ship
			shipArr[selectedShip].setStartCoord(y, x);
			pos = shipArr[selectedShip].getEndCoord();
		}else{
			JOptionPane.showMessageDialog(null,"Please selected the ship");
			return;
		}
		if (pos.length>0){	//checking if there are end coordinate available
			String[] availCoordStr = new String[pos.length+1];
			for (int i=0;i<pos.length;i++){
				availCoordStr[i] = convertCoord(pos[i].y, pos[i].x);
			} 
			availCoordStr[pos.length] = "Nevermind";
			JList<String> availCoordList = new JList<String>(availCoordStr);  //available coordinate
			availCoordList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			JOptionPane.showMessageDialog(this, availCoordList, 
					String.format("Original: [%s]",convertCoord(y, x)),
					JOptionPane.INFORMATION_MESSAGE);
			
			int selectEndPos = availCoordList.getSelectedIndex(); 			
			if (selectEndPos>-1 && selectEndPos<pos.length){
				//DEPLOY SHIP HERE
				Point p = pos[selectEndPos];
				System.out.printf("Selected Option: %d; Coord: %s\n",selectEndPos,availCoordStr[selectEndPos]);
				shipArr[selectedShip].deploy();
				shipArr[selectedShip].setEndCoord(p.y, p.x);	//finalize ship Coord
				deployedShip.add(selectedShip);
								
				if(deployedShip.size()==5){	// Check if all 5 ships are deployed All
					JOptionPane.showMessageDialog(this,"All ships are deployed.\nReady to Connect?");
					connectBtn.setEnabled(true);
					this.shipList.setEnabled(false);
					connectReady = true;
				}				
				return;
			}
		} else JOptionPane.showMessageDialog(null,"No available coordinate");
	}
	public boolean isFree(int startY, int startX, int endY, int endX){
		int[] range = new int[4];
		if (startY<=endY){ 
			range[0] = startY;
			range[1] = endY;				
		} else {
			range[1] = startY;
			range[0] = endY;	
		}
		if (startX<=endX){ 
			range[2] = startX;
			range[3] = endX;				
		} else {
			range[3] = startX;
			range[2] = endX;	
		}
		for (int i=range[0];i<=range[1];i++)
			for (int j=range[2];j<=range[3];j++){
				if (ourBoard[i][j].isOccupy())
					return false;
			}
		return true;
	}
	private int isFired(Point p){ //order of the ship that is hit
		for (int i=0;i<5;i++){
			for (int j=0;j<shipCoord.get(i).size();j++){
				if (shipCoord.get(i).get(j).equals(p)){
					//check if 2 point are the same
					return i;
				}
			}
		}
		return -1;
	}
	public int fire(String coord){ //when
		int x1=coord.charAt(0)-65;
		int y1=Integer.parseInt(coord.substring(1))-1;
		//System.out.printf("Convert: %s to y=%d, x=%d\n", coord,y1,x1);
		Point p = new Point();
		p.y = y1;
		p.x = x1;
		int shipHit = isFired(p);
		System.out.println("Ship hit: "+shipHit);
		if (shipHit>=0){ //ship is hit
			ourBoard[y1][x1].setBackground(Color.BLACK); //destroyed
			shipLength[shipHit] -=1;
			System.out.println(Arrays.toString(shipLength));
			//ship hit ==> 
			if(isSunk()!=-1)return 3;
			return 1;	//hit
		} //ship is missed
		ourBoard[y1][x1].setBackground(Color.CYAN);
		return 2;	//miss
	}
	public void placeShip(int startY, int startX, int endY, int endX){
		//display ship onto the Button Board
		int[] range = new int[4];
		if (startY<=endY){ 
			range[0] = startY;
			range[1] = endY;				
		} else {
			range[1] = startY;
			range[0] = endY;	
		}
		if (startX<=endX){ 
			range[2] = startX;
			range[3] = endX;				
		} else {
			range[3] = startX;
			range[2] = endX;	
		}
		//System.out.printf("%d %d %d %d \n",range[0],range[1],range[2],range[3]);
		int dir[][] = {{-1,-1},{-1,0},{-1,1},{0,1},{0,-1},{1,-1},{1,0},{1,1}};
		Vector<Point> sCoord= new Vector<Point>();
		for (int i=range[0];i<=range[1];i++)
			for (int j=range[2];j<=range[3];j++){
				ourBoard[i][j].setBackground(shipColor[selectedShip]);
				ourBoard[i][j].occupy = true;
				Point p = new Point();
				p.y = i;
				p.x = j;
				sCoord.add(p);
				for (int[] d:dir){
					int y1 = i+d[0];
					int x1 = j+d[1];
					if (BattleshipServer.inBound(y1, x1)){
						ourBoard[y1][x1].occupy = true;
					}
				}
			}
		shipCoord.add(sCoord);	
		
	}
	public String convertCoord(int y,int x){
		StringBuilder coord = new StringBuilder();
		coord.append(String.valueOf((char)(x+65)));
		coord.append(String.valueOf(y+1));
		return coord.toString();
	}
	public void setServerMessage(String message){
		serverMessage = message;
	}
	public String getServerMessage(){
		return serverMessage;
	}
	public static boolean inBound(int y, int x){
		return 0<=y && y<size && 0<=x && x<size;			
	}
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new BattleshipServer().setVisible(true);
			}
		});
	}
	
}


