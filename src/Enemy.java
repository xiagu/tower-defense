import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;

public class Enemy
{
	public double x, z, mHP, hp, speed, heading, x2, z2, treasure;
	public int damagedRecently = 0, level;
	public float[] color;
	public float alpha = 1;
	public boolean removed = false;
	
	public static final float[][] COLORS = {{1, 0, 0, 1},		//--red
											{.5f, .25f, 0, 1},	//--orange
											{1, 1, 0, 1},		//--yellow
											{.25f, .4f, 0, 1},	//--yellow-green
											{0, .75f, 0, 1},	//--green
											{0, .25f, .25f, 1},	//--teal
											{0, 1, 1, 1},		//--cyan
											{0, .2f, .75f, 1},	//--blue-green
											{0, 0, 1, 1},		//--blue
											{.3f, 0, .5f, 1},	//--violet
											{1, 0, 1, 1},		//--magenta
											{.2f, 0, .2f, 1}};	//--red-purply
	
	
	public Enemy(boolean numberOne) {throw new UnsupportedOperationException("n00b");}
	
	public Enemy(double xx, double zz, double h, double s, int wave)
	{
		x = xx;
		z = zz;
		x2 = xx + 5;
		z2 = zz + 5;
		mHP = h + 40*Math.sqrt(wave);
		hp = mHP;
		speed = s + .2*Math.sqrt(wave);
		level = wave;
		treasure = 7 + .5*Math.sqrt(wave);
		
		color = COLORS[wave%COLORS.length];
		
		heading = 0;
	}
	
	public void draw(GL gl, GLUT glut)
	{
		gl.glColor4fv(color, 0);
		gl.glPushMatrix();
		gl.glTranslated(x, 1.5, z);
		gl.glRotated(-heading, 0, 1, 0);
		switch(level/12 % 3)
		{
		case 0: gl.glScaled(3,3,3);
			//	gl.glTranslated(0, .5, 0);
				glut.glutSolidOctahedron();
				break;
		case 1: gl.glScaled(1.5,1.5,1.5);
			//	gl.glTranslated(0, .5, 0);
				glut.glutSolidDodecahedron();
				break;
		case 2: gl.glScaled(3,3,3);
			//	gl.glTranslated(0, .5, 0);
				glut.glutSolidIcosahedron();
				break;
		}
		gl.glPopMatrix();
		
		if(damagedRecently > 0)
		{
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, new float[4], 0);
			
			gl.glLineWidth(10);
			gl.glBegin(GL.GL_LINES);
				gl.glColor3d(1,0,0);
				gl.glVertex3d(x-4, 5, z);
				gl.glColor3d(1*((mHP-hp)/mHP),1*(hp/mHP),0);
				gl.glVertex3d(x-4+8*(hp/mHP), 5, z);
			gl.glEnd();
			gl.glLineWidth(1);
			damagedRecently --;
			
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, new float[]{.5f, .5f, .5f, 1}, 0);
		}
		
	}
	
	public void drawWireframe(GL gl, GLUT glut)
	{
		float[] newColor = {color[0], color[1], color[2], alpha};
		
		gl.glColor4fv(newColor, 0);
		gl.glPushMatrix();
		gl.glTranslated(x, 1.5, z);
		gl.glRotated(-heading, 0, 1, 0);
		switch(level/12 % 3)
		{
		case 0: gl.glScaled(3,3,3);
		//		gl.glTranslated(0, .5, 0);
				glut.glutSolidOctahedron();
				break;
		case 1: gl.glScaled(1.5,1.5,1.5);
				glut.glutSolidDodecahedron();
				break;
		case 2: gl.glScaled(3,3,3);
				glut.glutSolidIcosahedron();
				break;
		}
		gl.glPopMatrix();
		
		alpha -= .05;
	}
	
	public void move()
	{
		if(hp != 0)
		{
			double xF = x + speed*Math.cos(Math.toRadians(heading));
			double zF = z + speed*Math.sin(Math.toRadians(heading));
			
			// don't question it ---v
			int arrayX = (int)((xF+4.99*Math.signum(Math.cos(Math.toRadians(heading))))/10+11)-1;
			int arrayZ = (int)((zF+4.99*Math.signum(Math.sin(Math.toRadians(heading))))/10+11)-1;;
			
			int cArrayX = (int)(x/10+10);
			int cArrayZ = (int)(z/10+10);
			
			double extraXDist = speed;
			double extraZDist = speed;
			
			//System.out.println(heading);
			
			if(	(heading == 90 && arrayZ == 20) ||
				(heading == 270 && arrayZ == -1) ||
				(heading == 0 && arrayX == 20)  ||
				(heading == 180 && arrayX == -1) || 
				(World.cells[arrayX][arrayZ].terrain != Cell.TER_PATH && World.cells[arrayX][arrayZ].terrain != Cell.TER_ENTRANCE && World.cells[arrayX][arrayZ].terrain != Cell.TER_CASTLE))
			{
				if((float)xF % 5 != 0)
				{
					extraXDist -= (float)xF % 5;
					x = Math.floor(x/10)*10+5; 
				}
				if((float)zF % 5 != 0)
				{
					extraZDist -= (float)zF % 5;
					z = Math.floor(z/10)*10+5; 
				}
				
				switch((int)heading)
				{
				case 0:
					if(cArrayZ != 0 && (World.cells[cArrayX][cArrayZ-1].terrain == Cell.TER_PATH || World.cells[cArrayX][cArrayZ-1].terrain == Cell.TER_CASTLE))
						heading = 270;
					else if(cArrayZ != 19 && (World.cells[cArrayX][cArrayZ+1].terrain == Cell.TER_PATH || World.cells[cArrayX][cArrayZ+1].terrain == Cell.TER_CASTLE))
						heading = 90;
					else
						heading = 180;
					break;
				case 90:
					if(cArrayX != 0 && (World.cells[cArrayX-1][cArrayZ].terrain == Cell.TER_PATH || World.cells[cArrayX-1][cArrayZ].terrain == Cell.TER_CASTLE))
						heading = 180;
					else if(cArrayX != 19 && (World.cells[cArrayX+1][cArrayZ].terrain == Cell.TER_PATH || World.cells[cArrayX+1][cArrayZ].terrain == Cell.TER_CASTLE))
						heading = 0;
					else
						heading = 270;
					break;
				case 180:
					if(cArrayZ != 0 && (World.cells[cArrayX][cArrayZ-1].terrain == Cell.TER_PATH || World.cells[cArrayX][cArrayZ-1].terrain == Cell.TER_CASTLE))
						heading = 270;
					else if(cArrayZ != 19 && (World.cells[cArrayX][cArrayZ+1].terrain == Cell.TER_PATH || World.cells[cArrayX][cArrayZ+1].terrain == Cell.TER_CASTLE))
						heading = 90;
					else
						heading = 0;
					break;
				case 270:
					if(cArrayX != 0 && (World.cells[cArrayX-1][cArrayZ].terrain == Cell.TER_PATH || World.cells[cArrayX-1][cArrayZ].terrain == Cell.TER_CASTLE))
						heading = 180;
					else if(cArrayX != 19 && (World.cells[cArrayX+1][cArrayZ].terrain == Cell.TER_PATH || World.cells[cArrayX+1][cArrayZ].terrain == Cell.TER_CASTLE))
						heading = 0;
					else
						heading = 90;
				}
			}
			
		/*	x += speed*Math.cos(Math.toRadians(heading));
			z += speed*Math.sin(Math.toRadians(heading));*/
			x += extraXDist*Math.cos(Math.toRadians(heading));
			z += extraZDist*Math.sin(Math.toRadians(heading));
			x2 = x + 5;
			z2 = z + 5;
		}
	}
	
	public void resetDamagedRecently()
	{
		damagedRecently = 50;
	}
}
