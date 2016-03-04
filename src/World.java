import java.awt.event.*;
import java.awt.image.*;
import java.text.NumberFormat;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.imageio.*;

import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

public class World extends JFrame
{
	
	
	
					/***************************************************/
						/******************************************/
							public final boolean ERRORS = false;
						/******************************************/
					/***************************************************/

							
							
							
/******************************************************************************************
 *										DECLARATIONS									  *
 ******************************************************************************************/							
	public static JFrame screen;
	public static JPanelExtender panel, menu;
	
	public double mouseX = 0, mouseY = 0, theta = 0, yTheta = 30, distance = 181.5;
	public double actualX = 0, actualZ = 0;
	public double[] modelview = new double[16];
	public double[] projection = new double[16];
	public int[] viewport = new int[4];
	
	public BufferedImage hud = new BufferedImage(200, 700, BufferedImage.TYPE_INT_RGB);
	public Graphics2D buffer = (Graphics2D)hud.getGraphics();
	
	public BufferedImage titleScreen = new BufferedImage(900,700, BufferedImage.TYPE_INT_RGB);
	public Graphics2D menuBuffer = (Graphics2D)titleScreen.getGraphics();
	public boolean isPlaying = false;
	Timer menuTimer = new Timer(50, new Listener());
	
	public GLCanvas canvas = new GLCanvas();
	public GLUT glut = new GLUT();
	public GLU glu = new GLU();
	public boolean movingLeft = false, movingRight = false, movingUp = false, movingDown = false, doSelectyThings = false;
	public static int fps = 30;
	public FPSAnimator anim = new FPSAnimator(canvas, fps);
	
	public static BufferedImage testImage;
	public static TextureData tex;
	public static Texture textures;
	public static int texNameInt;
	
	public static int[] startLocation = new int[2];
	public static int[] endLocation = new int[2];
	public static Cell[][] cells = new Cell[20][20];
	public static LinkedList<Tower> towers = new LinkedList<Tower>();
	public static LinkedList<Enemy> enemies = new LinkedList<Enemy>();
	public static LinkedList<Enemy> dyingEnemies = new LinkedList<Enemy>();
	public static LinkedList<Projectile> projectiles = new LinkedList<Projectile>();
	
	public boolean continueBuilding = false;
	public boolean buildingTowers = false;
	public Type towerType;
	public Type displayInfo;
	public boolean displayUpgradeInfo = false;
	public boolean displaySellInfo = false;
	public boolean displayRange = false;
	public boolean invincibility = false;
	public int randomCounter = 0;
	
	public static Tower selectedTower = null;
	
	public Image laser = Toolkit.getDefaultToolkit().createImage("laser.png");
	public Image missile = Toolkit.getDefaultToolkit().createImage("missile.png");
	public Image mgun = Toolkit.getDefaultToolkit().createImage("mgun.png");
	public Image upgrade = Toolkit.getDefaultToolkit().createImage("upgrade.png");
	public Image sell = Toolkit.getDefaultToolkit().createImage("sell.png");
	public Image range = Toolkit.getDefaultToolkit().createImage("range.png");
	public Image background = Toolkit.getDefaultToolkit().createImage("background.png");
	
	public static int[] enemiesMade, enemyCounter;
	public int wave, waveCounter = 250, activeWaves = 1, lowestWave = 0;
	public static int numberOfWaves = 0, enemiesPerWave = 0;
	public static boolean[] makeEnemies;
	
	public double munnies = 110;
	public double maxCastleHealth = 100, castleHealth = maxCastleHealth;
	
	public static NumberFormat nf = NumberFormat.getInstance();

	
/******************************************************************************************
 *										CONSTRUCTOR										  *
 ******************************************************************************************/
	public World(boolean b)
	{	
		setTitle("Tower Defense");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900,700);
		setLocation(100,100);
		
		menu = new JPanelExtender();
		menu.addMouseListener(new Listener());
		
		menu.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent evt)
			{
				mouseX = evt.getX();
				mouseY = evt.getY();
			}
		});
		
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent evt)
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER) setUpStuff();
			}
		});
		
		add(menu);
		
		setResizable(false);
		setVisible(true);
		
		menuTimer.start();
	}
	
	public World()
	{
	/*	buffer.setBackground(new Color(0,0,0,0));
		
		panel = new JPanelExtender();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(Box.createHorizontalStrut(200));
		
		panel.setSize(200,700);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		canvas.setSize(700,700);
		
		add(canvas);
		add(panel);
		
		canvas.addKeyListener(new Listener());
		canvas.addMouseListener(new Listener());
	//	canvas.addMouseMotionListener(new Listener());
		canvas.addMouseWheelListener(new Listener());
		canvas.addGLEventListener(new Listener());
		
		panel.addMouseListener(new Listener());
		panel.addMouseMotionListener(new Listener());
		
	//	pack();
		setResizable(false);
		setVisible(true);
		
		anim.start();*/
	}

/******************************************************************************************
 *										    MAIN   										  *
 ******************************************************************************************/	
	public static void main(String[] args)
	{
		nf.setMinimumFractionDigits(2);
		nf.setMinimumIntegerDigits(1);
		nf.setMaximumFractionDigits(2);
		
		try
		{
		//	grass = ImageIO.read(new File("grass.bmp"));
			testImage = ImageIO.read(new File("Block.bmp"));
		//	testImage = ImageIO.read(new File("Coffee bean.bmp"));
		//	testImage = ImageIO.read(new File("Santa Fe Stucco.bmp"));
		//	testImage = ImageIO.read(new File("testTexture.bmp"));
		}
		catch(IOException e){}
		
		tex = new TextureData(0, (int)java.lang.Double.POSITIVE_INFINITY, false, testImage);
		texNameInt = 1;
		
	/*	tex2 = new TextureData(0, (int)java.lang.Double.POSITIVE_INFINITY, false, grass);
		grassName = 2;*/
		
		for(int x = 0; x < cells.length; x ++)
		{
			for(int z = 0; z < cells.length; z ++)
			{
				cells[x][z] = new Cell(Cell.TER_EMPTY, 0, 0);
			}
		}
		
		for(int x = 0; x < cells.length; x ++)
		{
			for(int z = 0; z < cells.length; z ++)
			{
				cells[x][z].xLoc = (x-10)*10;
				cells[x][z].zLoc = (z-10)*10;
			}
		}
		
		
/*		for(int i = 0; i < 10; i ++)
		{
			while(true)
			{
				int x = (int)(Math.random()*20);
				int z = (int)(Math.random()*20);
				
				if(cells[x][z].terrain == Cell.TER_EMPTY)
				{
					towers.add(new Tower(0, (x-9)*10, (z-9)*10));
					cells[x][z].terrain = Cell.TER_TOWER;
					break;	
				}
			}
		}*/
		
		screen = new World(true);
	}
	
/******************************************************************************************
 *									   DRAWING NON-GL									  *
 ******************************************************************************************/
	public class JPanelExtender extends JPanel
	{
		public Color gray = new Color(127,127,127,127);
		public Color selected = new Color(0,127,127,127);
		
		public void paintComponent(Graphics g)
		{
			// System.out.println(isPlaying);
			if(isPlaying)
			{
				// System.out.println("drawing sidebar");
//---------------------------------------------------------------------------------BORDER---			
				buffer.setColor(Color.white);
				buffer.drawRect(0,0,200,700);
	
//----------------------------------------------------------------------------------MONEY---
				buffer.setColor(Color.cyan);
				buffer.setFont(new Font("Arial", Font.PLAIN, 20));
				buffer.drawString("$" + nf.format(munnies), 20, 30);
				
//-----------------------------------------------------------------------------WAVE STUFF---			
				buffer.drawString("Wave: " + wave + "/"  + numberOfWaves, 20, 650);
				if(wave < numberOfWaves) buffer.drawString("Next wave in: " + waveCounter, 20, 620);
				buffer.setColor(Color.red);
				buffer.drawString("Send Next Wave", 24, 590);
				buffer.draw3DRect(19, 569, 156, 26, true);
				
//---------------------------------------------------------------------------------HEALTH---
				buffer.setColor(Color.cyan);
				
				if(invincibility)
				{
					buffer.drawString("Health?!", 20, 405);
					buffer.setPaint(new GradientPaint(40+randomCounter, 0, Color.red, 160+randomCounter, 0, Color.green, true));
					randomCounter += 10;
				}
				else
				{
					buffer.drawString("Health:", 20, 405);
					buffer.setPaint(new GradientPaint(40, 0, Color.red, 160, 0, Color.green));
				}
				buffer.fillRect(40, 410, (int)(120*(castleHealth/maxCastleHealth)), 25);
				
				buffer.setColor(Color.white);
				buffer.drawRect(40, 410, 120, 25);
				
//----------------------------------------------------------------------------TOWER ICONS---			
				buffer.setColor(Color.cyan);
				buffer.drawString("Towers:", 20, 100);
				
				buffer.setColor(Color.white);
				
				buffer.drawImage(mgun, 40, 105, null);
				buffer.draw3DRect(39,104, 31,31, true);
				if(towerType == Type.MGUN)
				{
					buffer.setColor(selected);
					buffer.fillRect(39, 104, 31, 31);
					buffer.setColor(Color.white);
				}
				if(munnies < Type.MGUN.getCost())
				{
					buffer.setColor(gray);
					buffer.fillRect(39, 104, 31, 31);
					buffer.setColor(Color.white);
				}
				
				buffer.drawImage(missile, 75, 105, null);
				buffer.draw3DRect(74,104, 31,31, true);
				if(towerType == Type.MISSILE)
				{
					buffer.setColor(selected);
					buffer.fillRect(74, 104, 31, 31);
					buffer.setColor(Color.white);
				}
				
				if(munnies < Type.MISSILE.getCost())
				{
					buffer.setColor(gray);
					buffer.fillRect(74, 104, 31, 31);
					buffer.setColor(Color.white);
				}
				
				buffer.drawImage(laser, 110, 105, null);
				buffer.draw3DRect(109,104, 31,31, true);
				if(towerType == Type.LASER)
				{
					buffer.setColor(selected);
					buffer.fillRect(109, 104, 31, 31);
					buffer.setColor(Color.white);
				}
				if(munnies < Type.LASER.getCost())
				{
					buffer.setColor(gray);
					buffer.fillRect(109, 104, 31, 31);
					buffer.setColor(Color.white);
				}
				
//-------------------------------------------------------------------------SELECTED TOWER---			
				if(selectedTower != null)
				{
					switch(selectedTower.type)
					{
					case MGUN:
						buffer.drawImage(mgun, 10, 200, 30, 30, null);
						break;
					case MISSILE:
						buffer.drawImage(missile, 10, 200, 30, 30, null);
						break;
					case LASER:
						buffer.drawImage(laser, 10, 200, 30, 30, null);
						break;
					}
					
					buffer.setPaint(new GradientPaint(0, 320, Color.red, 0, 250, Color.yellow));
					buffer.fillRect(20, 240, 10, (int)(80*((selectedTower.cooldown-selectedTower.fireCounter)/selectedTower.cooldown)));
					
					buffer.setColor(Color.white);
					buffer.drawRect(9, 199, 31, 31);
					
					buffer.setFont(new Font("Arial", Font.PLAIN, 15));
					
					buffer.drawString(selectedTower.type.getName(), 44, 217);
					buffer.drawLine(44,219, 180, 219);
					buffer.setFont(new Font("Arial", Font.PLAIN, 12));
					
					buffer.drawString("Level: " + selectedTower.level, 44, 236);
					buffer.drawString("Damage: " + nf.format(selectedTower.damage), 44, 248);
					buffer.drawString("Range: " + nf.format(selectedTower.range), 44, 260);
					buffer.drawString("Cooldown: " + nf.format(selectedTower.cooldown), 44, 272);
					buffer.drawString("Splash: " + nf.format(selectedTower.splash), 44, 284);
					
					buffer.drawImage(range, 45, 289, 30, 30, null);
					buffer.draw3DRect(44, 288, 31, 31, true);
					
					buffer.drawImage(sell, 85, 289, 30, 30, null);
					buffer.draw3DRect(84, 288, 31, 31, true);
					
					buffer.drawImage(upgrade, 125, 289, 30, 30, null);
					buffer.draw3DRect(124, 288, 31, 31, true);
					if(munnies < selectedTower.upgradeCost)
					{
						buffer.setColor(gray);
						buffer.fillRect(124, 288, 31, 31);
						buffer.setColor(Color.white);
					}
					
					//-----------UPGRADE INFO---
					if(displayUpgradeInfo)
					{
						buffer.setColor(Color.white);
						buffer.setFont(new Font("Arial", Font.PLAIN, 10));
						buffer.drawString("Upgrade Tower", 80, 330);
						if(munnies < selectedTower.upgradeCost) buffer.setColor(Color.red);
						buffer.drawString("Cost: $" + nf.format(selectedTower.upgradeCost), 80, 340);
					}
					
					//--------------SELL INFO---
					if(displaySellInfo)
					{
						buffer.setColor(Color.white);
						buffer.setFont(new Font("Arial", Font.PLAIN, 10));
						buffer.drawString("Sell Tower", 80, 330);
						buffer.drawString("Returns $" + nf.format(selectedTower.sellCost), 80, 340);
					}
				}
				
				if(displayInfo != null)
				{
					buffer.setColor(Color.white);
					buffer.setFont(new Font("Arial", Font.PLAIN, 10));
					
					switch(displayInfo)
					{
					case MGUN:
						buffer.drawString(displayInfo.getName(), 50, 152);
						if(munnies < displayInfo.getCost()) buffer.setColor(Color.red);
						buffer.drawString("Cost: $" + displayInfo.getCost(), 50, 165);
						break;
					case MISSILE:
						buffer.drawString(displayInfo.getName(), 50, 152);
						if(munnies < displayInfo.getCost()) buffer.setColor(Color.red);
						buffer.drawString("Cost: $" + displayInfo.getCost(), 50, 165);
						break;
					case LASER:
						buffer.drawString(displayInfo.getName(), 50, 152);
						if(munnies < displayInfo.getCost()) buffer.setColor(Color.red);
						buffer.drawString("Cost: $" + displayInfo.getCost(), 50, 165);
						break;
					}
				}
				
				g.drawImage(hud, 0, 0, 200, 700, null);
				
				buffer.clearRect(0,0,200,700);
			}
/******************************************************************************************
 *									    DRAWING MENU 									  *
 ******************************************************************************************/	
			else
			{
				menuBuffer.clearRect(0,0, 900, 700);
				
				menuBuffer.drawImage(background, 0, 0, null);
				
				menuBuffer.setColor(new Color(0,0,0,128));
				menuBuffer.fillRect(0, 0, 900, 700);
				
				menuBuffer.setColor(Color.black);
				menuBuffer.fillRect(400,400,100,30);
				
				if(mouseX >= 400 && mouseX <= 500)
					if(mouseY >= 400 && mouseY <= 430)
					{
						menuBuffer.setColor(Color.cyan.darker().darker().darker());
						menuBuffer.fillRect(400, 400, 100, 30);
					}
				
				drawText(menuBuffer,"Tower Defense",170,300,72,2,1);
				
				menuBuffer.setColor(Color.cyan);
				menuBuffer.drawRect(400, 400, 100, 30);
				
				drawText(menuBuffer, "Play", 423, 422, 22, 1, .5);
				//drawText(menuBu)
				
				g.drawImage(titleScreen, 0, 0, 900, 700, null);
			}
		}
	}
	
	public class Listener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, ActionListener
	{
		public double[] pointZ1 = new double[3];
		public double[] pointZ2 = new double[3];
		
/******************************************************************************************
 *										 DRAWING GL										  *
 ******************************************************************************************/
		public void display(GLAutoDrawable glad)
		{
			// System.out.println(Arrays.toString(screen.getContentPane().getComponents()));
			if(castleHealth <= 0)
			{
				JOptionPane.showMessageDialog(screen, "Oh no! You've lost!");
				System.exit(0);
			}
			if(wave == numberOfWaves && enemies.size() == 0 && !makeEnemies[wave-1])
			{
				JOptionPane.showMessageDialog(screen, "You win!");
				System.exit(0);
			}
			
			waveCounter --;
			if(waveCounter == 0 && wave < numberOfWaves)
			{
				wave ++;
				waveCounter = 450 + enemiesPerWave/2*fps;
				if(wave <= numberOfWaves) makeEnemies[wave-1] = true;
			}
			
			for(int i = 0; i < wave; i ++)
			{
				if(i > numberOfWaves) break;
				
				enemyCounter[i] ++;
				
				if(enemiesMade[i] < enemiesPerWave && enemyCounter[i] % 15 == 0 && makeEnemies[i])
				{
					Enemy tempEnemy = new Enemy(10*(startLocation[0]-10)+5, 10*(startLocation[1]-10)+5, 100, .6, i);
					enemies.add(tempEnemy);
					enemiesMade[i] ++;
				}
				if(enemiesMade[i] >= enemiesPerWave)
				{
			//		enemiesMade[i] = 0;
					makeEnemies[i] = false;
			//		waveCounter = 450;
				}
			}
			
//---------------------------------------------------------------------------------MOVING---			
			if(!doSelectyThings)
			{	
				if(movingLeft) theta -= 5;
				if(movingRight) theta += 5;
				if(movingUp && yTheta < 90) yTheta += 2;
				if(movingDown && yTheta > 10) yTheta -= 2;
			}
				
			GL gl = glad.getGL();
			int i = 1;
			
//---------------------------------------------------------------------------------CAMERA---
			ByteBuffer aByteBuffer = ByteBuffer.allocateDirect(400);
			aByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
			IntBuffer hitResults = aByteBuffer.asIntBuffer();
			
			if(doSelectyThings)
			{
				gl.glSelectBuffer(100, hitResults);
				
				gl.glRenderMode(GL.GL_SELECT);
				
				gl.glInitNames();
				gl.glPushName(0);
			}
			
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			
			if(doSelectyThings) glu.gluPickMatrix(mouseX, mouseY, 1, 1, viewport, 0);
			
			double camX = distance*Math.sin(Math.toRadians(theta))*Math.cos(Math.toRadians(yTheta));
			double camY = distance*Math.sin(Math.toRadians(yTheta));
			double camZ = distance*Math.cos(Math.toRadians(theta))*Math.cos(Math.toRadians(yTheta));
			
			glu.gluPerspective(60, 1, 20, 323);
			glu.gluLookAt(camX, camY, camZ, 0, 0, 0, 0, 1, 0);
			
			gl.glMatrixMode(GL.GL_MODELVIEW);//*/
			
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			
			gl.glLoadIdentity();
			
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, new float[]{(float)camX, (float)camY, (float)camZ, 1}, 0);
			
//-----------------------------------------------------------------------------DRAW CELLS---
			
			for(int x = 0; x < cells.length; x ++)
			{
				for(int z = 0; z < cells.length; z ++)
				{
					gl.glLoadName(i);
					cells[x][z].name = i;
					i ++;
					
					gl.glBegin(GL.GL_QUADS);
						cells[x][z].draw(gl, glut);
					gl.glEnd();
				}
			}
			
			float[] ambient = {(float)7/255, (float)113/255, (float)254/255};
			gl.glColor3fv(ambient, 0);
			gl.glBegin(GL.GL_LINES);
//------------------------------------------------------------------------------DRAW GRID---
				for(int x = -10; x < 10; x ++)
				{
					for(int z = -10; z < 10; z ++)
					{
						gl.glVertex3d(-100,0.1,z*10);
						gl.glVertex3d(100,0.1,z*10);
						gl.glVertex3d(x*10,0.1,-100);
						gl.glVertex3d(x*10,0.1,100);
					}
				}
//--------------------------------------------------------------------------LIGHT PYRAMID---
/*				gl.glVertex3d(position[0],position[1],position[2]);
				gl.glVertex3d(-100,0,-100);
				gl.glVertex3d(position[0],position[1],position[2]);
				gl.glVertex3d(-100,0,100);
				gl.glVertex3d(position[0],position[1],position[2]);
				gl.glVertex3d(100,0,-100);
				gl.glVertex3d(position[0],position[1],position[2]);
				gl.glVertex3d(100,0,100);*/
				
			gl.glEnd();
			
//----------------------------------------------------------------------------DRAW TOWERS---			
			for(int j = 0; j < towers.size(); j ++)
			{
				Tower t = towers.get(j);
				
				gl.glLoadName(i);
				t.name = i;
				i ++;
				t.draw(gl, glut);
				if(!doSelectyThings) t.attack(gl, glut, buffer);
			}
			
//-----------------------------------------------------------DRAW RANGE OF SELECTED TOWER---
			if(selectedTower != null && !doSelectyThings && displayRange)
			{
				gl.glEnable(GL.GL_CLIP_PLANE0);
				gl.glDisable(GL.GL_LIGHTING);
			//	gl.glEnable(GL.GL_CLIP_PLANE1);
				gl.glDepthMask(false);
				gl.glPushMatrix();
				gl.glColor4d(1,0,0,.25);
			//	gl.glScaled(selectedTower.range, selectedTower.range, selectedTower.range);
				gl.glTranslated(selectedTower.xLoc - 5, -5, selectedTower.zLoc - 5);
			//	gl.glRotated(90, -1, 0, 0);
				glut.glutSolidSphere(selectedTower.range, 20, 20);
			//	glut.glutSolidCylinder(selectedTower.range, 6, 20, 1);
				gl.glPopMatrix();
				gl.glDepthMask(true);
				gl.glDisable(GL.GL_CLIP_PLANE0);
				gl.glEnable(GL.GL_LIGHTING);
			//	gl.glDisable(GL.GL_CLIP_PLANE1);
			}
			
//------------------------------------------------------------------DRAW AND MOVE ENEMIES---			
			for(int j = 0; j < enemies.size(); j ++)
			{
				Enemy e = enemies.get(j);
				if(e.hp <= 0)
				{
					munnies += e.treasure;
					enemies.remove(e);
					dyingEnemies.add(e);
					j --;
				}
				else
				{
					e.draw(gl, glut);
					if(!doSelectyThings) e.move();
				}
				
				if(cells[(int)(e.x/10+10)][(int)(e.z/10+10)].terrain == Cell.TER_CASTLE)
				{
					if(!invincibility) castleHealth -= e.level * 2;
					enemies.remove(e);
					e.removed = true;
				}
			}
			
//---------------------------------------------------------------------DRAW DYING ENEMIES---
			for(int j = 0; j < dyingEnemies.size(); j ++)
			{
				Enemy e = dyingEnemies.get(j);
				
				e.drawWireframe(gl, glut);
				
				if(e.alpha <= 0) dyingEnemies.remove(e);
			}
			
//--------------------------------------------------------------DRAW AND MOVE PROJECTILES---
			for(int j = 0; j < projectiles.size(); j ++)
			{
				Projectile p = projectiles.get(j);
				if(!doSelectyThings) p.move();
				p.draw(gl, glut);
				
				if(p.removeMe) projectiles.remove(p);
				
			}
			
//---------------------------------------------------------------------------CLICKY STUFF---			
			gl.glFlush();			
			
			if(doSelectyThings)
			{	
				int hits = gl.glRenderMode(GL.GL_RENDER);
				doSelectyThings = false;
		//		// System.out.println(hits);
				String str = "";
				
				int[] depths = new int[hits];
				boolean blah = false;
				
				for(int j = 0; j < hits; j ++)
				{
					depths[j] = hitResults.get(j*4+1);
				}
				
				Arrays.sort(depths);
				
				for(int j = 0; j < hits; j ++)
				{
					for(Tower t : towers)
					{
						if(hitResults.get(j*4+1) != depths[0]) break;
						if(hitResults.get(j*4+3) == t.name)
						{
							selectedTower = t;
							blah = true;
							break;
						}
					}
					if(blah) break;
					selectedTower = null;
					for(Cell[] c2 : cells)
					{
						if(hitResults.get(j*4+1) != depths[0]) break;
						for(Cell c : c2)
						{
							if(hitResults.get(j*4+3) == c.name)
							{
					//			// System.out.println("Cell: " + c.xLoc + " - " + c.zLoc);
								if(c.terrain == Cell.TER_EMPTY)
								{
									if(buildingTowers && munnies >= towerType.getCost())
									{
	/* Making a new tower ---> */		Tower tempTower = new Tower(towerType, c.xLoc+10, c.zLoc+10);
										towers.add(tempTower);
										c.terrain = Cell.TER_TOWER;
										munnies -= towerType.getCost();
										
										if(!continueBuilding)
										{
											buildingTowers = false;
											towerType = null;
										}
									}
								}
								blah = true;
								break;
							}
						}
					}
					if(blah) break;
					
					/*str += hitResults.get(j) + " ";
					if(j %4 == 3) str += '\n';*/
				}
				//// System.out.println(str);
			}
			
			gl.glRotated(-theta*2, 0, 1, 0);
			gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, modelview, 0);
			gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, projection, 0);
			gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
			
			panel.repaint();
		}

		public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
			// TODO Auto-generated method stub
			
		}

/******************************************************************************************
 *										INITIATION										  *
 ******************************************************************************************/
		public float[] position = {0,20,0,1};

		public void init(GLAutoDrawable glad)
		{
			GL gl = glad.getGL();
			
			textures = TextureIO.newTexture(tex);
	//		grassTex = TextureIO.newTexture(tex2);
			
			gl.glClearColor(0,0,0,0);
			
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			
			glu.gluPerspective(60, 1, 20, 323);
			glu.gluLookAt(0, 30*Math.sqrt(2), -171.5, 0, 0, 0, 0, 1, 0);
			
			gl.glMatrixMode(GL.GL_MODELVIEW);
			
//---------------------------------------------------------------------EXPLOSION CLIPPING---
			double[] planeEQ = {0,1,0,0};
			double[] planeEQ2 = {0,-1,0,10};
			gl.glClipPlane(GL.GL_CLIP_PLANE0, planeEQ, 0);
			gl.glClipPlane(GL.GL_CLIP_PLANE1, planeEQ2, 0);
			
//-------------------------------------------------------------------------------LIGHTING---			
			float[] specularz = {1, 1, 1, .5f};
			float[] shinyz = {1};
			float[] white = {1, 1, 1, 1};
			float[] ambient = {.5f, .5f, .5f, 1};
			float[] red = {.5f, .5f, .1f, 1};
			float[] lmodelAmbient = {.1f, .1f, .1f, 1};
			
			gl.glShadeModel(GL.GL_SMOOTH);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, ambient, 0);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, shinyz, 0);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, white, 0);
		//	gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, ambient, 0);
		/*	gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position, 0);
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, white, 0);
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specularz, 0);
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient, 0);*/
			
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, position, 0);
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, white, 0);
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, specularz, 0);
			gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, white, 0);
			
			gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, lmodelAmbient, 0);
			gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, GL.GL_TRUE);
			gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_TRUE);
			//gl.glLightModeli(GL.GL_LIGHT_MODEL_SPECULAR_VECTOR_APPLE, GL.GL_TRUE);
			gl.glLightModeli(GL.GL_LIGHT_MODEL_COLOR_CONTROL, GL.GL_SEPARATE_SPECULAR_COLOR);
			
//-------------------------------------------------------------------------------TEXTURES---			
			gl.glBindTexture(GL.GL_TEXTURE_2D, texNameInt);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, 128, 128, 0, GL.GL_RGB, GL.GL_BITMAP, tex.getBuffer());
			
	/*		gl.glBindTexture(GL.GL_TEXTURE_2D, grassName);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
			gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, 128, 128, 0, GL.GL_RGB, GL.GL_BITMAP, tex2.getBuffer());
		//	gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, 256, 256, 0, GL.GL_RGB, GL.GL_BITMAP, tex.getBuffer());*/
			
			gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
//-------------------------------------------------------------------------------ENABLIN'---
			gl.glEnable(GL.GL_DEPTH_TEST | GL.GL_LINE_SMOOTH);
			gl.glEnable(GL.GL_NORMALIZE);
			gl.glEnable(GL.GL_LIGHTING);
			gl.glEnable(GL.GL_LIGHT0 | GL.GL_LIGHT1);
			gl.glEnable(GL.GL_COLOR_MATERIAL);
			
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		}

		public void reshape(GLAutoDrawable glad, int x, int y, int width, int height)
		{
	//		GL gl = glad.getGL();
			
	//		gl.glViewport(0,0,700,700-36);
		}
		
		String str = "";
		
		public void keyPressed(KeyEvent evt)
		{
			int key = evt.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT) movingLeft = true;
			else if(key == KeyEvent.VK_RIGHT) movingRight = true;
			else if(key == KeyEvent.VK_UP) movingUp = true;
			else if(key == KeyEvent.VK_DOWN) movingDown = true;
			else
			{
				str += KeyEvent.getKeyText(key);
				if(key == KeyEvent.VK_ESCAPE) str = "";
				if(str.equalsIgnoreCase("turniptastic"))
				{
					invincibility = !invincibility;
					randomCounter = 0;
				}
			}
		//	if(key == KeyEvent.VK_C) munnies += 1337;
		}

		public void keyReleased(KeyEvent evt)
		{
			int key = evt.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT) movingLeft = false;
			if(key == KeyEvent.VK_RIGHT) movingRight = false;
			if(key == KeyEvent.VK_UP) movingUp = false;
			if(key == KeyEvent.VK_DOWN) movingDown = false;

		}

		public void keyTyped(KeyEvent evt)
		{
	/*		int myKey = evt.getKeyCode();
			
			if(myKey == KeyEvent.VK_UP) depth ++;
			if(myKey == KeyEvent.VK_DOWN) depth --;*/
			
		}

/******************************************************************************************
 *										MOUSECLICKED									  *
 ******************************************************************************************/
		public boolean mouseClicked = false;
		
		public void mouseClicked(MouseEvent evt)
		{
			
		}

		public void mouseEntered(MouseEvent evt)
		{
			
			
		}

		public void mouseExited(MouseEvent evt)
		{
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent evt)
		{
			if(!mouseClicked)
			{
				mouseX = evt.getX();
				mouseY = evt.getY();
				
				if(evt.getButton() == MouseEvent.BUTTON3)
				{
					buildingTowers = false;
					towerType = null;
				}
				else if(evt.getButton() == MouseEvent.BUTTON1)
				{
					if(evt.getComponent() == panel)
					{	
						if(mouseX >= 40 && mouseX <= 70)
							if(mouseY >= 105 && mouseY <= 135)
							{	buildingTowers = true;
								towerType = Type.MGUN;
							}
						
						if(mouseX >= 75 && mouseX <= 105)
							if(mouseY >= 105 && mouseY <= 135)
							{	buildingTowers = true;
								towerType = Type.MISSILE;
							}
						
						if(mouseX >= 110 && mouseX <= 140)
							if(mouseY >= 105 && mouseY <= 135)
							{	buildingTowers = true;
								towerType = Type.LASER;
							}
						
						if(mouseX >= 19 && mouseX <= 175)
							if(mouseY >= 569 && mouseY <= 595)
							{
								waveCounter = 1;
							}
						
						if(mouseX >= 124 && mouseX <= 155)
							if(mouseY >= 288 && mouseY <= 319)
							{
								if(selectedTower != null && munnies >= selectedTower.upgradeCost)
								{
									munnies -= selectedTower.upgradeCost;
									selectedTower.levelup();
								}
							}
						
						if(mouseX >= 84 && mouseX <= 115)
							if(mouseY >= 288 && mouseY <= 319)
							{
								munnies += selectedTower.sellCost;
							//	// System.out.println(cells[(int)((selectedTower.xLoc-5)/10+10)][(int)((selectedTower.zLoc-5)/10+10)].terrain);
								cells[(int)((selectedTower.xLoc-10)/10+10)][(int)((selectedTower.zLoc-5)/10+10)].terrain = Cell.TER_EMPTY;
								towers.remove(selectedTower);
								selectedTower = null;
							}
				/*		buffer.drawImage(sell, 85, 289, 30, 30, null);
					buffer.draw3DRect(84, 288, 31, 31, true);
					
					buffer.drawImage(upgrade, 125, 289, 30, 30, null);
					buffer.draw3DRect(124, 288, 31, 31, true);
						/*buffer.drawString("Send Next Wave", 24, 590);
						buffer.draw3DRect(19, 569, 156, 26, true);*/
					}
//---------------------------------------------------------------------STARTING THE GAME---
					else if(evt.getComponent() == menu)
					{
						if(mouseX >= 400 && mouseX <= 500)
							if(mouseY >= 400 && mouseY <= 430)
							{
								setUpStuff();
							}
					}
				
					if(evt.getComponent() == canvas)
					{
						mouseY = viewport[3] - mouseY;
						
						if(evt.isControlDown())
							continueBuilding = true;
						else
							continueBuilding = false;
						
						doSelectyThings =  true;
					}
				}
				mouseClicked = true;
			}
		}

		public void mouseReleased(MouseEvent evt)
		{
			mouseClicked = false;
		}

		public void mouseDragged(MouseEvent evt)
		{
			
		}

		public void mouseMoved(MouseEvent evt)
		{
			mouseX = evt.getX();
			mouseY = evt.getY();
			
			if(mouseX >= 40 && mouseX <= 70)
			{
				if(mouseY >= 105 && mouseY <= 135)
				{
					displayInfo = Type.MGUN;
				}
			}
			else if(mouseX >= 75 && mouseX <= 105)
			{
				if(mouseY >= 105 && mouseY <= 135)
				{
					displayInfo = Type.MISSILE;
				}
			}
			else if(mouseX >= 110 && mouseX <= 140)
			{
				if(mouseY >= 105 && mouseY <= 135)
				{
					displayInfo = Type.LASER;
				}
			}
			else displayInfo = null;
			
			if(mouseX >= 124 && mouseX <= 155)
			{
				if(mouseY >= 288 && mouseY <= 319)
				{
					displayUpgradeInfo = true;
				}
			}
			else displayUpgradeInfo = false;
			
			if(mouseX >= 84 && mouseX <= 115)
			{
				if(mouseY >= 288 && mouseY <= 319)
				{
					displaySellInfo = true;
				}
			}
			else displaySellInfo = false;
			
			if(mouseX >= 44 && mouseX <= 75)
			{
				if(mouseY >= 288 && mouseY <= 319)
				{
					displayRange = true;
				}
			}
			else displayRange = false;
		}

		public void mouseWheelMoved(MouseWheelEvent evt)
		{
			int clicks = evt.getWheelRotation();
			if(clicks < 0 && distance > 100) distance += clicks*5;
			if(clicks > 0 && distance < 200) distance += clicks*5;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			// System.out.println("repainting menu");
			menu.repaint();
		}
	}
	
	public void drawText(Graphics g, String str, int x, int y, int size, double xTrail, double yTrail)
	{
	/*		Color cyan1 = new Color(0,255,255,204);
			Color cyan2 = new Color(0,170,255,153);
			Color cyan3 = new Color(0,85,255,102);
			Color cyan4 = new Color(0,0,255,51);*/
		
			Color cyan1 = new Color(0,204,204,255);
			Color cyan2 = new Color(0, (int)(170*(double)153/255),153,255);
			Color cyan3 = new Color(0,(int)(85*(double)102/255),102,255);
			Color cyan4 = new Color(0,0,51,255);
			
			g.setFont(new Font("Courier New", Font.ITALIC, size));
			
			g.setColor(cyan4);
			g.drawString(str, (int)(x-2*xTrail), (int)(y+2*yTrail));
			g.setColor(cyan3);
			g.drawString(str, (int)(x-xTrail), (int)(y+yTrail));
			g.setColor(cyan2);
			g.drawString(str, x, y);
			g.setColor(cyan1);
			g.drawString(str, (int)(x+xTrail), (int)(y-yTrail));
			g.setColor(Color.cyan);
			g.drawString(str, (int)(x+2*xTrail), (int)(y-2*yTrail));
	}
	
	public void setUpStuff()
	{
		menuTimer.stop();
		screen.remove(menu);
		
		//----------
		int cur, curX, curY;
		
		try
		{
			String filename = JOptionPane.showInputDialog(screen, "What is the name of the mapfile to load? (no ext.)", "normal");
			
			Scanner reader = new Scanner(new FileReader(filename + ".txt"));
			
			numberOfWaves = reader.nextInt();
			enemiesPerWave = reader.nextInt();
			
			enemiesMade = new int[numberOfWaves+1];
			enemyCounter = new int[numberOfWaves+1];
			makeEnemies = new boolean[numberOfWaves+1];
			
			while (reader.hasNext()) {
				if (reader.hasNextInt()) {
					curX = reader.nextInt();
					curY = reader.nextInt();
					cur = reader.nextInt();
					cells[curX][curY].terrain = cur;
					if(cur == Cell.TER_ENTRANCE)
					{
						startLocation[0] = curX;
						startLocation[1] = curY;
					}
					else if(cur == Cell.TER_CASTLE)
					{
						endLocation[0] = curX;
						endLocation[1] = curY;
					}
			//		// System.out.println(curX + " - " + curY + " - " + cur);
				}
			}
			reader.close();
		}
		
		catch (FileNotFoundException e) {
			System.exit(1);
		}
		//----------
		
		screen.setContentPane(new JPanel());
		
		buffer.setBackground(new Color(0,0,0,0));
		
		panel = new JPanelExtender();
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(Box.createHorizontalStrut(200));
		
		panel.setPreferredSize(new Dimension(200,700));
		
		screen.setLayout(new BoxLayout(screen.getContentPane(), BoxLayout.X_AXIS));
		
		//panel.add(Box.createVerticalStrut(700));
		canvas.setPreferredSize(new Dimension(700,700));
		
		screen.add(canvas);
		screen.add(panel);
		
		Listener tempKeyListener = new Listener();
		
		canvas.addKeyListener(tempKeyListener);
		canvas.addMouseListener(new Listener());
	//	canvas.addMouseMotionListener(new Listener());
		canvas.addMouseWheelListener(new Listener());
		canvas.addGLEventListener(new Listener());
		
		panel.addMouseListener(new Listener());
		panel.addMouseMotionListener(new Listener());
		panel.addKeyListener(tempKeyListener);
		
		pack();
		isPlaying = true;
		
		anim.start();
	}
}
