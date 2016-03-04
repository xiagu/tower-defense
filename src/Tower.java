import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;
import java.util.Arrays;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Tower
{
	public static final int TYPE_MGUN = 1,
							TYPE_MISSILE = 2,
							TYPE_LASER = 3;
	
	
	
	//public static final String[] 
	/*public static final double[] STAT_RANGE_L1 = {75, 50, 1337},
								 STAT_DAMAGE_L1 = {1, 15, 30},
								 STAT_SPLASH_L1 = {0, 10, 0};
	public static final int[] STAT_COOLDOWN_L1 = {0, 10, 25},
							  COST = {25,40,200};*/
	
	public TowerType type;
	public int xLoc, zLoc, name, fireCounter = 0, level = 1;
	public int height = 15;
	public double range, damage, splash, cooldown, upgradeCost, sellCost, moneyPaid;
	
	public Enemy target;
	
	public Tower(TowerType type, int x, int z)
	{
		this.type = type;
		this.xLoc = x;
		this.zLoc = z;
		this.name = 0;
		range = type.getRange();
		damage = type.getDamage();
		splash = type.getSplash();
		cooldown = type.getCooldown();
		upgradeCost = .75*type.getCost();
		moneyPaid = type.getCost();
		sellCost = .8*type.getCost();
	}
	
	public void draw(GL gl, GLUT glut)
	{
/*		gl.glPushMatrix();
		gl.glTranslated(xLoc - 5, 0, zLoc-5);
		gl.glRotated(90, -1,0,0);
		gl.glColor3d(1, 0, 0);
		glut.glutWireCylinder(3, 10, 20, 20);
		
		gl.glRotated(90, 1,0,0);
		gl.glColor3d(0, 0, 1);
		
		gl.glTranslated(-3, 12, 0);
		gl.glScaled(.2, 1, 1);
		glut.glutWireCube(8);
		gl.glScaled(5, 1, 1);
		gl.glTranslated(6, 0, 0);
		gl.glScaled(.2, 1, 1);
		glut.glutWireCube(8);
		gl.glScaled(5, 1, 1);
		gl.glTranslated(-3, 0, -3);
		gl.glScaled(1, 1, .2);
		glut.glutWireCube(8);
		gl.glScaled(1, 1, 5);
		gl.glTranslated(0, 0, 6);
		gl.glScaled(1, 1, .2);
		glut.glutWireCube(8);
		
		gl.glPopMatrix();*/
		
		gl.glPushMatrix();
		
		gl.glTranslated(xLoc - 5, 0, zLoc-5);
		gl.glRotated(90, -1,0,0);
		gl.glColor3d(1, 0, 0);
		gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_OBJECT_LINEAR);
		gl.glTexGendv(GL.GL_S, GL.GL_OBJECT_PLANE, new double[]{0,1,0,0}, 0);
		gl.glTexGendv(GL.GL_T, GL.GL_OBJECT_PLANE, new double[]{0,1,0,0}, 0);
		gl.glEnable(GL.GL_TEXTURE_GEN_S);
		gl.glEnable(GL.GL_TEXTURE_GEN_T);
		World.textures.bind();
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		glut.glutSolidCylinder(3, height - 5, 8, 1);
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glRotated(90, 1,0,0);
		
		if(World.selectedTower != null && World.selectedTower.equals(this))
		{
			gl.glColor3d(1,1,1);
			gl.glPushMatrix();
			gl.glScaled(1.01, 1, 1.01);
			
			gl.glRotated(-90, 1,0,0);
			glut.glutWireCylinder(3, height - 5, 8, 1);
			gl.glRotated(90, 1,0,0);
			
			gl.glTranslated(-3.2,height-4,-3.2);
			gl.glScaled(.2,1,.2);
			glut.glutWireCube(8);
			gl.glScaled(1,.5,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,2,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,.5,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,2,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glTranslated(-32,0,0);
			
			gl.glScaled(5,1,5);
			gl.glTranslated(0, 0, 6.4);
			gl.glScaled(.2,1,.2);
			glut.glutWireCube(8);
			gl.glTranslated(8, 0, 0);
			gl.glScaled(1,.5,1);
			glut.glutWireCube(8);
			gl.glScaled(1,2,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,.5,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,2,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glTranslated(-32,0,0);
			gl.glScaled(5,1,5);
			
			gl.glRotated(90, 0,1,0);
			
			gl.glScaled(.2,1,.2);
			glut.glutWireCube(8);
			gl.glScaled(1,.5,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,2,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,.5,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,2,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glTranslated(-32,0,0);
			
			gl.glScaled(5,1,5);
			gl.glTranslated(0, 0, 6.4);
			gl.glScaled(.2,1,.2);
			glut.glutWireCube(8);
			gl.glTranslated(8, 0, 0);
			gl.glScaled(1,.5,1);
			glut.glutWireCube(8);
			gl.glScaled(1,2,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,.5,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glScaled(1,2,1);
			gl.glTranslated(8, 0, 0);
			glut.glutWireCube(8);
			gl.glPopMatrix();
		}
		
		switch(type)
		{
		case MGUN:
			gl.glColor3d(1, 0, 0);
			break;
		case MISSILE:
			gl.glColor3d(.25f,0,1);
			break;
		case LASER:
			gl.glColor3d(0, 0, 1);
		}
		
		
		
	/*	gl.glTranslated(-3, 12, 0);
		gl.glScaled(.25, .75, 1);
		glut.glutSolidCube(8);
		gl.glScaled(4, 1, 1);
		gl.glTranslated(6, 0, 0);
		gl.glScaled(.25, 1, 1);
		glut.glutSolidCube(8);
		gl.glScaled(4, 1, 1);
		gl.glTranslated(-3, 0, -3);
		gl.glScaled(1, 1, .25);
		glut.glutSolidCube(8);
		gl.glScaled(1, 1, 4);
		gl.glTranslated(0, 0, 6);
		gl.glScaled(1, 1, .25);
		glut.glutSolidCube(8);*/
		
		//gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, new float[]{.25f,.25f,.25f,.25f}, 0);
		
	//	gl.glNormal3d(0, 0, 1);
		gl.glTranslated(-3.2,height-4,-3.2);
		gl.glScaled(.2,1,.2);
		glut.glutSolidCube(8);
		gl.glScaled(1,.5,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,2,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,.5,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,2,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glTranslated(-32,0,0);
		
		gl.glScaled(5,1,5);
		gl.glTranslated(0, 0, 6.4);
		gl.glScaled(.2,1,.2);
		glut.glutSolidCube(8);
		gl.glTranslated(8, 0, 0);
		gl.glScaled(1,.5,1);
		glut.glutSolidCube(8);
		gl.glScaled(1,2,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,.5,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,2,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glTranslated(-32,0,0);
		gl.glScaled(5,1,5);
		
		gl.glRotated(90, 0,1,0);
		
		gl.glScaled(.2,1,.2);
		glut.glutSolidCube(8);
		gl.glScaled(1,.5,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,2,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,.5,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,2,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glTranslated(-32,0,0);
		
		gl.glScaled(5,1,5);
		gl.glTranslated(0, 0, 6.4);
		gl.glScaled(.2,1,.2);
		glut.glutSolidCube(8);
		gl.glTranslated(8, 0, 0);
		gl.glScaled(1,.5,1);
		glut.glutSolidCube(8);
		gl.glScaled(1,2,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,.5,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		gl.glScaled(1,2,1);
		gl.glTranslated(8, 0, 0);
		glut.glutSolidCube(8);
		
		//glut.glutSolidIcosahedron();
		
		
		
	/*	gl.glTranslated(0, 10, 0);
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
			gl.glTexCoord2d(0,0);
			gl.glVertex3d(-4,8,-4);
			
			gl.glTexCoord2d(0,1);
			gl.glVertex3d(4,8,-4);
			
			gl.glTexCoord2d(1,1);
			gl.glVertex3d(-4,0,-4);
			
			gl.glTexCoord2d(1,0);
			gl.glVertex3d(4,0,-4);
			//--------
			gl.glTexCoord2d(0,0);
			gl.glVertex3d(4,8,-4);
			
			gl.glTexCoord2d(0,1);
			gl.glVertex3d(4,0,4);
			//--------			
			gl.glTexCoord2d(1,1);
			gl.glVertex3d(4,8,4);
			
			gl.glTexCoord2d(1,0);
			gl.glVertex3d(-4,0,4);
			//--------			
			gl.glTexCoord2d(0,0);
			gl.glVertex3d(-4,8,4);
			
			gl.glTexCoord2d(0,1);
			gl.glVertex3d(-4,0,-4);
			
			gl.glTexCoord2d(1,1);
			gl.glVertex3d(-4,8,-4);
		gl.glEnd();
		
		gl.glBegin(GL.GL_TRIANGLE_STRIP);
			gl.glTexCoord2d(0,1);
			gl.glVertex3d(-2,8,-2);
			
			gl.glTexCoord2d(1,1);
			gl.glVertex3d(2,8,-2);
			
			gl.glTexCoord2d(0,0);
			gl.glVertex3d(-2,0,-2);
			
			gl.glTexCoord2d(1,0);
			gl.glVertex3d(2,0,-2);
			//--------
			gl.glTexCoord2d(0,1);
			gl.glVertex3d(2,8,-2);
			
			gl.glTexCoord2d(1,1);
			gl.glVertex3d(2,0,2);
			//--------			
			gl.glTexCoord2d(0,0);
			gl.glVertex3d(2,8,2);
			
			gl.glTexCoord2d(1,0);
			gl.glVertex3d(-2,0,2);
			//--------			
			gl.glTexCoord2d(0,1);
			gl.glVertex3d(-2,8,2);
			
			gl.glTexCoord2d(1,1);
			gl.glVertex3d(-2,0,-2);
			
			gl.glTexCoord2d(0,0);
			gl.glVertex3d(-2,8,-2);
		gl.glEnd();
		
		gl.glBegin(GL.GL_TRIANGLES);
			//gl.glNormal3d(0, 1, 0);
			//-----------
			gl.glVertex3d(-4,8,-4);
			gl.glVertex3d(-4,8,4);
			gl.glVertex3d(-2,8,4);
			//-----------
			gl.glVertex3d(-4,8,-4);
			gl.glVertex3d(-2,8,-4);
			gl.glVertex3d(-2,8,4);
			//-----------
			gl.glVertex3d(-2,8,-2);
			gl.glVertex3d(-2,8,-4);
			gl.glVertex3d(2,8,-2);
			//-----------
			gl.glVertex3d(-2,8,-4);
			gl.glVertex3d(2,8,-2);
			gl.glVertex3d(2,8,-4);
			//-----------
			gl.glVertex3d(4,8,-4);
			gl.glVertex3d(4,8,4);
			gl.glVertex3d(2,8,4);
			//-----------
			gl.glVertex3d(4,8,-4);
			gl.glVertex3d(2,8,-4);
			gl.glVertex3d(2,8,4);
			//-----------
			gl.glVertex3d(-2,8,2);
			gl.glVertex3d(-2,8,4);
			gl.glVertex3d(2,8,2);
			//-----------
			gl.glVertex3d(-2,8,4);
			gl.glVertex3d(2,8,2);
			gl.glVertex3d(2,8,4);
			
			gl.glVertex3d(-2,8,-2);
			gl.glVertex3d(2,8,-2);
			gl.glVertex3d(2,0,-2);
			gl.glVertex3d(2,8,-2);
			gl.glVertex3d(2,0,-2);
			gl.glVertex3d(2,8,2);
			gl.glVertex3d(2,0,-2);
			gl.glVertex3d(2,8,2);
			gl.glVertex3d(2,0,2);
			gl.glVertex3d(2,8,2);
			gl.glVertex3d(2,0,2);
			gl.glVertex3d(-2,8,2);
			gl.glVertex3d(2,0,2);
			gl.glVertex3d(-2,8,2);
			gl.glVertex3d(-2,0,2);
			gl.glVertex3d(-2,8,2);
			gl.glVertex3d(-2,0,2);
			gl.glVertex3d(-2,8,-2);
			gl.glVertex3d(-2,0,2);
			gl.glVertex3d(-2,8,-2);
			gl.glVertex3d(-2,0,-2);
			gl.glVertex3d(-2,8,-2);
			gl.glVertex3d(-2,0,-2);
			
			
		gl.glEnd();//*/
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glPopMatrix();
	}
	
	public void attack(GL gl, GLUT glut, Graphics2D g)
	{
		if(target != null && target.removed) target = null;
		
		if(fireCounter > cooldown)
		{	
			if(type != TowerType.LASER)
			{
			/* if(target != null) */ fireCounter = 0;
				
				if(target == null || target.hp <= 0 || Math.hypot(xLoc - target.x, zLoc - target.z) > range)
				{
					switch(type)
					{
					case MGUN:
						double weakest = Double.MAX_VALUE; // range of the tower
						double dist;
						int i = 0, nearI = -1337;
						for(Enemy e : World.enemies)
						{
							dist = Math.hypot(xLoc - e.x, zLoc - e.z); 
							if(dist < range && e.hp < weakest)
							{
								weakest = e.hp;
								nearI = i;
								target = World.enemies.get(nearI);
							}
							i++;
						}
						if(nearI == -1337) target = null;
						break;
					case MISSILE:
						double nearest = range; // range of the tower
						dist = 0;
						i = 0; nearI = -1337;
						for(Enemy e : World.enemies)
						{
							dist = Math.hypot(xLoc - e.x, zLoc - e.z); 
							if(dist < nearest)
							{
								nearest = dist;
								nearI = i;
								target = World.enemies.get(nearI);
							}
							i++;
						}
						if(nearI == -1337) target = null;
						break;
					}
				}
				
				if(target != null)
				{
					switch(type)
					{
					case MGUN:
						/*gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[]{0,.5f,1}, 0);
						gl.glColor4f(0, 0, 1, .8f);
						gl.glLineWidth(50);
						gl.glDisable(GL.GL_DEPTH_TEST);
						gl.glBegin(GL.GL_LINES);
							gl.glVertex3d(xLoc-5, 15, zLoc-5);
							gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[]{1,0,0,1}, 0);
							gl.glVertex3d(target.x, 1.5, target.z);
						gl.glEnd();
						gl.glEnable(GL.GL_DEPTH_TEST);
						gl.glLineWidth(1);
						gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[3], 0);*/
						Projectile temp = new Projectile(Projectile.TYPE_MGUN, xLoc, height, zLoc, 5, target);
						World.projectiles.add(temp);
						target.hp -= damage;
						target.resetDamagedRecently();
						break;
					case MISSILE:
						Projectile tempP = new Projectile(Projectile.TYPE_MISSILE, xLoc, height, zLoc, damage, 4, splash, target);
						World.projectiles.add(tempP);
						break;
					}
				}
			}
			else
			{
				if(World.enemies.size() != 0) fireCounter = 0;
				
				int interval = 128;
				
				int most = -1337;
				int prevHit = -1;
				int hit = 0;
				int k = 0;
				int consecutiveBestHitsCounter = 0;
				double bestAngle = Double.POSITIVE_INFINITY;
				int[] enemiesHit = new int[World.enemies.size()];
				int[] bestEnemiesHit = new int[World.enemies.size()];
				java.util.Arrays.fill(bestEnemiesHit, -1);

				for(double i = 0; i < 2*Math.PI; i += Math.PI/interval)
				{
					hit = 0;
					k = 0;
					enemiesHit = new int[World.enemies.size()];
					Arrays.fill(enemiesHit, -1);

					int[] xs = {xLoc, xLoc+300, xLoc+300, xLoc};
					int[] zs = {zLoc-5, zLoc-5, zLoc+5, zLoc+5};

					for(int j = 0; j < 4; j ++)
					{	
						int[] newXS = new int[xs.length];
						System.arraycopy(xs, 0, newXS, 0, xs.length);

						xs[j] = (int)(Math.cos(i)*(xs[j]-xLoc) - Math.sin(i)*(zs[j]-zLoc)) + xLoc;
						zs[j] = (int)(Math.sin(i)*(newXS[j]-xLoc) + Math.cos(i)*(zs[j]-zLoc)) + zLoc;

						/*Math.cos(theta)*(p.xpoints[i]-490) - Math.sin(theta)*(p.ypoints[i]-490);
					double newY = Math.sin(theta)*(p.xpoints[i]-490) + Math.cos(theta)*(p.ypoints[i]-490);*/
					}

					Polygon laser = new Polygon(xs, zs, 4);
					//			System.out.println(laser);

					/*		g.setColor(red);
					g.drawLine(xs[0]-xLoc + 250, zs[0]-zLoc + 350, xs[1]-xLoc + 250, zs[1]-zLoc + 350);
					g.drawLine(xs[1]-xLoc + 250, zs[1]-zLoc + 350, xs[2]-xLoc + 250, zs[2]-zLoc + 350);
					g.drawLine(xs[2]-xLoc + 250, zs[2]-zLoc + 350, xs[3]-xLoc + 250, zs[3]-zLoc + 350);
					g.drawLine(xs[3]-xLoc + 250, zs[3]-zLoc + 350, xs[0]-xLoc + 250, zs[0]-zLoc + 350);*/
					//	if(Math.round(Math.toDegrees(i))%45 == 0) System.out.print("----------------------"); 
					//	System.out.println(Math.toDegrees(i) + " - " + Arrays.toString(xs) + " - " + Arrays.toString(zs));
				//	System.out.println("------------------------");
					for(int j = 0; j < World.enemies.size(); j ++)
					{
						//					System.out.println(bestAngle);
						//	System.out.println(k + " - " + Arrays.toString(enemiesHit) + " - " + Arrays.toString(bestEnemiesHit));
						Enemy e = World.enemies.get(j);
						
				/*		gl.glBegin(GL.GL_LINES);
							gl.glVertex3d(xLoc-5, 15, zLoc-5);
							gl.glVertex3d(e.x, 1.5, e.z);
						gl.glEnd();*/
						
						if(laser.contains(e.x, e.z))
						{
							enemiesHit[k] = j;
							k ++;
							hit ++;
						}
					}
					//System.out.println(consecutiveBestHitsCounter);
					
					if(most <= hit)
					{
						most = hit;
						//bestAngle = i;
						//bestEnemiesHit = Arrays.copyOf(enemiesHit, 0);
			//			bestEnemiesHit = Arrays.copyOf(enemiesHit, enemiesHit.length);
						System.arraycopy(enemiesHit, 0, bestEnemiesHit, 0, enemiesHit.length);
						
						if(hit == prevHit)
						{							
							consecutiveBestHitsCounter ++;
						}
						else
						{
							consecutiveBestHitsCounter = 0;
						}
						
						bestAngle = i - ((double)consecutiveBestHitsCounter*Math.PI/interval/2);
					}
					
					prevHit = hit;
				}

				if(World.enemies.size() != 0)
				{
					Projectile tempP = new Projectile(Projectile.TYPE_LASER, xLoc-5, 5, zLoc-5, damage, bestAngle);
					World.projectiles.add(tempP);

					for(int j = 0; j < bestEnemiesHit.length; j ++)
					{
						if(bestEnemiesHit[j] != -1)
						{
							Enemy temp = World.enemies.get(bestEnemiesHit[j]);

							temp.hp -= damage;
							temp.resetDamagedRecently();
						}
						else break;
					}
				}
			}
		}
		fireCounter ++;
	}

	public void levelup()
	{
		level ++;
		damage *= 1.4;
		cooldown *= .95;
		splash *= 1.15;
		range *= 1.15;
		
		height += 1;
		
		moneyPaid += upgradeCost;
		sellCost = .8*moneyPaid;
		
		upgradeCost *= 1.4;
		
		
	}
}
