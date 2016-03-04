import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;

import java.util.Scanner;
import java.io.*;
import java.util.Date;
import java.awt.geom.AffineTransform;

public class GridEdit extends JFrame
{
	public GraphicsPanel gp = new GraphicsPanel();
	public BufferedImage image = new BufferedImage(1000+10, 800+36, BufferedImage.TYPE_INT_RGB);
	//public BufferedImage images = new BufferedImage(300, 800, BufferedImage.TYPE_INT_RGB);
	public Graphics2D buffer = (Graphics2D)image.getGraphics();
	
	//Updates XX times per second
	Timer graphicsTimer = new Timer(25, new GraphicsUpdater());
	
	public Cell[][] map = new Cell[20][20];
	public long timeSinceLastSave = 0, timeLastSaved = new Date().getTime();
	public static JFrame window;
	public String filename;
	public int numberOfWaves = 0, enemiesPerWave = 0;
	
	public JTextField waves, enemies;
	
	public BufferedImage cursor = new BufferedImage(12,21,BufferedImage.TYPE_INT_RGB);
	
	public GridEdit()
	{
		Container content = getContentPane();
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		JPanel drawPanel = new JPanel();
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
	//	typePanel.add(Box.createHorizontalStrut(200));
		drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.Y_AXIS));
		drawPanel.add(Box.createHorizontalStrut(1000));
		drawPanel.add(gp);
		
		gp.addMouseMotionListener(new MouseMotionHandler());
		gp.addMouseListener(new MouseHandler());
		
		content.add(drawPanel);
		
		buffer.setBackground(Color.black);
		
		for(int i = 0; i < map.length; i ++) { 
			for(int j = 0; j < map.length; j ++) {
				map[i][j] = new Cell(Cell.TER_EMPTY, i, j);
			}
		}
		
		int cur, curX, curY;
		
		try
		{
			filename = JOptionPane.showInputDialog(this, "Enter the name of the mapfile to edit (no ext.):");
			
			if(filename == null || filename.equals("")) System.exit(0);
			
			Scanner reader = new Scanner(new FileReader(filename + ".txt"));
			numberOfWaves = reader.nextInt();
			enemiesPerWave = reader.nextInt();
			
			
		/*	waves.setVisible(true);
			enemies.setVisible(true);*/
			
		/*	add(waves);
			add(enemies);*/
			
			while (reader.hasNext()) {
				if (reader.hasNextInt()) {
					curX = reader.nextInt();
					curY = reader.nextInt();
					cur = reader.nextInt();
					map[curX][curY].terrain = cur;
				}
			}
			reader.close();
		}
		
		catch (FileNotFoundException e) {
			System.out.println("File not found, please try again.");
			//System.exit(1);
		}
		
		waves = new JTextField("" + numberOfWaves);
		enemies = new JTextField("" + enemiesPerWave);
		
		typePanel.add(new JLabel("Number of waves:", JLabel.LEFT));
		typePanel.add(waves);
		typePanel.add(new JLabel("Number of enemies per wave:", JLabel.LEFT));
		typePanel.add(enemies);
		
		content.add(typePanel);
		
		graphicsTimer.start();
	}
	
	public static void main(String aaarrggghhs[])
	{
		window = new GridEdit();		
		window.setSize(1200+10, 800+36);
		window.setLocation(20,20);
		window.setTitle("Map Editor");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
		
	public class GraphicsUpdater implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			gp.refreshGraphics();
		}
	}
	
	public class GraphicsPanel extends JApplet
	{
		public static final long serialVersionUID = 1; 
		
		public GraphicsPanel()
		{
			
		}
		public void paint(Graphics g)
		{
			drawGraphics(g);
		}
		
		public void update(Graphics g)
		{
			paint(g);
		}
		public void refreshGraphics()
		{
			repaint();
		}
	}
	
	public void drawGraphics(Graphics g)
	{
		buffer.clearRect(0,0,1000+10, 800+36);
		
		for(int i = 0; i < map.length; i ++) {
			 for(int j = 0; j < map.length; j ++) {
				 map[i][j].draw(buffer, i*40, j*40);
			 }
		}
		
		buffer.setColor(Color.black);
		for(int x = 0; x < map.length; x ++)
		{
			for(int y = 0; y < map.length; y ++)
			{
				buffer.drawLine(0, y*40, 800, y*40);
				buffer.drawLine(x*40, 0, x*40, 800);
			}
		}
		
	/*	buffer.setColor(Color.black);
		buffer.fillRect(800,0,300,800);
		
		buffer.setColor(Color.white);
		buffer.drawRect(0,0,1100,800);
		buffer.drawLine(800, 0, 800, 800);*/
		
		AffineTransform savedAT = buffer.getTransform();
		buffer.translate(800, 0);
		
		buffer.setColor(Color.green);
		buffer.fillRect(50, 50, 50, 50);
		buffer.drawString("Empty", 110, 75);
		buffer.setColor(Color.white);
		buffer.drawRect(50, 50, 50, 50);
		
		buffer.setColor(Color.yellow);
		buffer.fillRect(50, 150, 50, 50);
		buffer.drawString("Path", 110, 175);
		buffer.setColor(Color.white);
		buffer.drawRect(50, 150, 50, 50);
		
		buffer.setColor(Color.blue);
		buffer.fillRect(50, 250, 50, 50);
		buffer.drawString("Tower", 110, 275);
		buffer.setColor(Color.white);
		buffer.drawRect(50, 250, 50, 50);
		
		buffer.setColor(Color.red);
		buffer.fillRect(50, 350, 50, 50);
		buffer.drawString("Castle", 110, 375);
		buffer.setColor(Color.white);
		buffer.drawRect(50, 350, 50, 50);
		
		buffer.setColor(Color.magenta.darker().darker());
		buffer.fillRect(50, 450, 50, 50);
		buffer.drawString("Entrance", 110, 475);
		buffer.setColor(Color.white);
		buffer.drawRect(50, 450, 50, 50);
		
		buffer.setTransform(savedAT);
		
		timeSinceLastSave = new Date().getTime() - timeLastSaved;
		
		super.setTitle("Map Editor - " + timeSinceLastSave/1000);
		
		g.drawImage(image,0,0,1000+10, 800+36,null);
	}
	
	public class MouseMotionHandler implements MouseMotionListener
	{
		public void mouseMoved(MouseEvent evt)
		{
			
		}
		public void mouseDragged(MouseEvent evt)
		{
			
		}
	}
	public class MouseHandler implements MouseListener
	{
		public void mousePressed(MouseEvent evt)
		{
			int mouseX = evt.getX();
			int mouseY = evt.getY();
			
			if(mouseX <= 800)
			{
				if (evt.getButton() == MouseEvent.BUTTON1) {
					map[mouseX/40][mouseY/40].terrain++;
					map[mouseX/40][mouseY/40].terrain %= Cell.NUMSPEC;
				}
				if(evt.getButton() == MouseEvent.BUTTON3)
				{
					map[mouseX/40][mouseY/40].terrain--;
					map[mouseX/40][mouseY/40].terrain = (map[mouseX/40][mouseY/40].terrain + Cell.NUMSPEC) % Cell.NUMSPEC;
				}
				else if (evt.getButton() == MouseEvent.BUTTON2) {
					try {
						PrintWriter writer = new PrintWriter(filename + ".txt");
						writer.println(waves.getText());
						writer.println(enemies.getText());
						for (int i=0; i < map.length; i++) {
							for (int j=0; j < map.length; j++) {
								if (map[i][j].terrain != Cell.TER_EMPTY) {
									writer.println("" + i + ' ' + j + ' ' + map[i][j].terrain);
								}
							}
						}
						writer.close();
						timeLastSaved = new Date().getTime();
					}
					
					catch (FileNotFoundException e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
			else
			{
			/*	try
				{
					if(mouseX <= 900 && mouseX >= 850)
					{
						if(mouseY >= 50 && mouseY <= 100)
						{
							cursor = ImageIO.read(new File("1_cursor.png"));
							Cursor mouse = Toolkit.getDefaultToolkit().createCustomCursor(null, new Point(0,0), "Green");
							window.setCursor(mouse);
							
						}
						else if(mouseY >= 150 && mouseY <= 200)
						{
							
						}
						else if(mouseY >= 250 && mouseY <= 300)
						{
							
						}
					}
				}
				catch(IOException e)
				{
					
				}*/
			}
		}
		public void mouseReleased(MouseEvent evt)
		{
			
		}
		public void mouseClicked(MouseEvent evt)
		{
			
		}
		public void mouseEntered(MouseEvent evt)
		{
				
		}
		public void mouseExited(MouseEvent evt)
		{
			
		}
	}
}
