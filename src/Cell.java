import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import javax.swing.JFrame;
import com.sun.opengl.util.*;
import java.awt.*;

public class Cell
{
	public static final int TER_EMPTY = 0,
							TER_PATH = 1,						
							TER_TOWER = 2,
							TER_CASTLE = 3,
							TER_ENTRANCE = 4,
							NUMSPEC = 5;
	public int xLoc, zLoc, terrain, name;
	boolean noNZ, noNX, noPZ, noPX;
	public float[] ambient;
	
	public Cell(int ter, int x, int z)
	{
		terrain = ter;
		xLoc = x;
		zLoc = z;
		name = 0;
	}
	
	public void draw(GL gl, GLUT glut)
	{
		noNZ = false;
		noNX = false;
		noPZ = false;
		noPX = false;
		
		gl.glLoadIdentity();
		
		switch(terrain)
		{
		case Cell.TER_CASTLE:
			
			gl.glEnd();
			
			ambient = new float[]{0,.7f,0};
			gl.glColor3f(0, 0, 0);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[]{0,.5f,0,0}, 0);
			
			gl.glPushMatrix();
			
			gl.glTranslated(xLoc+1, 0, zLoc+1);
			gl.glRotated(90, -1,0,0);
			gl.glColor3d(1, 0, 0);
			gl.glTexGeni(GL.GL_S, GL.GL_TEXTURE_GEN_MODE, GL.GL_OBJECT_LINEAR);
			gl.glTexGendv(GL.GL_S, GL.GL_OBJECT_PLANE, new double[]{0,1,0,0}, 0);
			gl.glTexGendv(GL.GL_T, GL.GL_OBJECT_PLANE, new double[]{0,1,0,0}, 0);
			gl.glEnable(GL.GL_TEXTURE_GEN_S);
			gl.glEnable(GL.GL_TEXTURE_GEN_T);
			World.textures.bind();
			
			gl.glEnable(GL.GL_TEXTURE_2D);
			
			glut.glutSolidCylinder(1, 10, 8, 1);
			gl.glTranslated(0, 0, 0);
			
			gl.glTranslated(8, 0, 0);
			glut.glutSolidCylinder(1, 10, 8, 1);
			
			gl.glTranslated(0, -8, 0);
			glut.glutSolidCylinder(1, 10, 8, 1);
			
			gl.glTranslated(-8, 0, 0);
			glut.glutSolidCylinder(1, 10, 8, 1);
			
			gl.glTranslated(4, 4, 0);
			glut.glutSolidCylinder(3, 7, 8, 1);
			
			gl.glDisable(GL.GL_TEXTURE_2D);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[4], 0);
			gl.glColor3f(0,0,.4f);
			
			gl.glTranslated(-4, 4, 10);
			glut.glutSolidCone(1, 2, 8, 8);
			
			gl.glTranslated(8, 0, 0);
			glut.glutSolidCone(1, 2, 8, 8);
			
			gl.glTranslated(0, -8, 0);
			glut.glutSolidCone(1, 2, 8, 8);
			
			gl.glTranslated(-8, 0, 0);
			glut.glutSolidCone(1, 2, 8, 8);
			
			gl.glTranslated(4, 4, -3);
			glut.glutSolidCone(3, 3, 8, 8);
			
			gl.glPopMatrix();
			
			gl.glBegin(GL.GL_QUADS);
			
			
			
			
			
			
			
			
		case Cell.TER_TOWER:
		case Cell.TER_EMPTY:
		//	gl.glColor3d(0, .7, 0);
			ambient = new float[]{0,.7f,0};
			gl.glColor3f(0, 0, 0);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[]{0,.5f,0,0}, 0);
			break;
		case Cell.TER_PATH:
		case Cell.TER_ENTRANCE:
		//	gl.glColor3d(.8, .7, 0);
			ambient = new float[]{.8f,.7f,0};
			gl.glColor3f(0, 0, 0);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[]{.7f,.5f,0,0}, 0);
		}
		
		switch(terrain)
		{
		case Cell.TER_EMPTY:
			
		//	World.grassTex.bind();
			
		//	gl.glEnable(GL.GL_TEXTURE_2D);
			
//-----------------------------------------------------------------------------NEGATIVE Z---			
			if(zLoc != -100) if(World.cells[xLoc/10+10][zLoc/10+9].terrain == Cell.TER_EMPTY) noNZ = true;
			
//-----------------------------------------------------------------------------NEGATIVE X---			
			if(xLoc != -100) if(World.cells[xLoc/10+9][zLoc/10+10].terrain == Cell.TER_EMPTY) noNX = true;

//-----------------------------------------------------------------------------POSITIVE X---
			if(xLoc != 90) if(World.cells[xLoc/10+11][zLoc/10+10].terrain == Cell.TER_EMPTY) noPX = true;
			
//-----------------------------------------------------------------------------POSITIVE Z---
			if(zLoc != 90) if(World.cells[xLoc/10+10][zLoc/10+11].terrain == Cell.TER_EMPTY) noPZ = true;
			
			if(!noNZ)
			{
				gl.glNormal3d(0,5,-3);
				gl.glVertex3d(xLoc, 0, zLoc);
				gl.glVertex3d(xLoc+10, 0, zLoc);
				gl.glVertex3d(xLoc+7, 5, zLoc+3);
				gl.glVertex3d(xLoc+3, 5, zLoc+3);
			}
			
			if(!noNX)
			{
				gl.glNormal3d(-3,5,0);
				gl.glVertex3d(xLoc, 0, zLoc);
				gl.glVertex3d(xLoc, 0, zLoc+10);
				gl.glVertex3d(xLoc+3, 5, zLoc+7);
				gl.glVertex3d(xLoc+3, 5, zLoc+3);
			}
			
			if(!noPZ)
			{
				gl.glNormal3d(0,5,3);
				gl.glVertex3d(xLoc, 0, zLoc+10);
				gl.glVertex3d(xLoc+10, 0, zLoc+10);
				gl.glVertex3d(xLoc+7, 5, zLoc+7);
				gl.glVertex3d(xLoc+3, 5, zLoc+7);
			}
			
			if(!noPX)
			{
				gl.glNormal3d(3,5,0);
				gl.glVertex3d(xLoc+10, 0, zLoc);
				gl.glVertex3d(xLoc+10, 0, zLoc+10);
				gl.glVertex3d(xLoc+7, 5, zLoc+7);
				gl.glVertex3d(xLoc+7, 5, zLoc+3);
			}
			
			if(noNZ)
			{
				gl.glNormal3d(0, 1, 0);
				gl.glVertex3d(xLoc+3, 5, zLoc);
				gl.glVertex3d(xLoc+7, 5, zLoc);
				gl.glVertex3d(xLoc+7, 5, zLoc+3);
				gl.glVertex3d(xLoc+3, 5, zLoc+3);
				
				gl.glNormal3d(3,5,0);
				gl.glVertex3d(xLoc+10, 0, zLoc);
				gl.glVertex3d(xLoc+10, 0, zLoc+5);
				gl.glVertex3d(xLoc+7, 5, zLoc+5);
				gl.glVertex3d(xLoc+7, 5, zLoc);
				
				gl.glNormal3d(-3,5,0);
				gl.glVertex3d(xLoc, 0, zLoc);
				gl.glVertex3d(xLoc, 0, zLoc+5);
				gl.glVertex3d(xLoc+3, 5, zLoc+5);
				gl.glVertex3d(xLoc+3, 5, zLoc);
			}
			
			if(noPZ)
			{
				gl.glNormal3d(0, 1, 0);
				gl.glVertex3d(xLoc+3, 5, zLoc+7);
				gl.glVertex3d(xLoc+7, 5, zLoc+7);
				gl.glVertex3d(xLoc+7, 5, zLoc+10);
				gl.glVertex3d(xLoc+3, 5, zLoc+10);
				
				gl.glNormal3d(3,5,0);
				gl.glVertex3d(xLoc+10, 0, zLoc+5);
				gl.glVertex3d(xLoc+10, 0, zLoc+10);
				gl.glVertex3d(xLoc+7, 5, zLoc+10);
				gl.glVertex3d(xLoc+7, 5, zLoc+5);
				
				gl.glNormal3d(-3,5,0);
				gl.glVertex3d(xLoc, 0, zLoc+5);
				gl.glVertex3d(xLoc, 0, zLoc+10);
				gl.glVertex3d(xLoc+3, 5, zLoc+10);
				gl.glVertex3d(xLoc+3, 5, zLoc+5);
			}
			
			if(noNX)
			{
				gl.glNormal3d(0, 1, 0);
				gl.glVertex3d(xLoc, 5, zLoc+3);
				gl.glVertex3d(xLoc+3, 5, zLoc+3);
				gl.glVertex3d(xLoc+3, 5, zLoc+7);
				gl.glVertex3d(xLoc, 5, zLoc+7);
				
				gl.glNormal3d(0,5,3);
				gl.glVertex3d(xLoc, 0, zLoc+10);
				gl.glVertex3d(xLoc+5, 0, zLoc+10);
				gl.glVertex3d(xLoc+5, 5, zLoc+7);
				gl.glVertex3d(xLoc, 5, zLoc+7);
				
				gl.glNormal3d(0,5,-3);
				gl.glVertex3d(xLoc, 0, zLoc);
				gl.glVertex3d(xLoc+5, 0, zLoc);
				gl.glVertex3d(xLoc+5, 5, zLoc+3);
				gl.glVertex3d(xLoc, 5, zLoc+3);
			}
			
			if(noPX)
			{
				gl.glNormal3d(0, 1, 0);
				gl.glVertex3d(xLoc+7, 5, zLoc+3);
				gl.glVertex3d(xLoc+10, 5, zLoc+3);
				gl.glVertex3d(xLoc+10, 5, zLoc+7);
				gl.glVertex3d(xLoc+7, 5, zLoc+7);
				
				gl.glNormal3d(0,5,3);
				gl.glVertex3d(xLoc+5, 0, zLoc+10);
				gl.glVertex3d(xLoc+10, 0, zLoc+10);
				gl.glVertex3d(xLoc+10, 5, zLoc+7);
				gl.glVertex3d(xLoc+5, 5, zLoc+7);
				
				gl.glNormal3d(0,5,-3);
				gl.glVertex3d(xLoc+5, 0, zLoc);
				gl.glVertex3d(xLoc+10, 0, zLoc);
				gl.glVertex3d(xLoc+10, 5, zLoc+3);
				gl.glVertex3d(xLoc+5, 5, zLoc+3);
			}
			
	/*		if(noNZ && noNX)
			{
				gl.glNormal3d(0, 1, 0);
				gl.glVertex3d(xLoc, 5, zLoc);
				gl.glVertex3d(xLoc, 5, zLoc);
				gl.glVertex3d(xLoc+3, 5, zLoc+3);
				gl.glVertex3d(xLoc+3, 5, zLoc+3);
			}
			if(noPZ && noNX)
			{
				gl.glNormal3d(0, 1, 0);
				gl.glVertex3d(xLoc, 5, zLoc+10);
				gl.glVertex3d(xLoc, 5, zLoc+7);
				gl.glVertex3d(xLoc+3, 5, zLoc+7);
				gl.glVertex3d(xLoc+3, 5, zLoc+10);
			}
			if(noPZ && noPX)
			{
				gl.glNormal3d(0, 1, 0);
				gl.glVertex3d(xLoc+10, 5, zLoc+10);
				gl.glVertex3d(xLoc+10, 5, zLoc+7);
				gl.glVertex3d(xLoc+7, 5, zLoc+7);
				gl.glVertex3d(xLoc+7, 5, zLoc+10);
			}
			if(noNZ && noPX)
			{
				gl.glNormal3d(0, 1, 0);
				gl.glVertex3d(xLoc+10, 5, zLoc);
				gl.glVertex3d(xLoc+10, 5, zLoc+3);
				gl.glVertex3d(xLoc+7, 5, zLoc+3);
				gl.glVertex3d(xLoc+7, 5, zLoc);
			}*/
			
			gl.glNormal3d(0,1,0);
			gl.glTexCoord2d(0, 0);
			gl.glVertex3d(xLoc+3, 5, zLoc+3);
			gl.glTexCoord2d(1, 0);
			gl.glVertex3d(xLoc+7, 5, zLoc+3);
			gl.glTexCoord2d(1, 1);
			gl.glVertex3d(xLoc+7, 5, zLoc+7);
			gl.glTexCoord2d(0, 1);
			gl.glVertex3d(xLoc+3, 5, zLoc+7);
			
		//	gl.glDisable(GL.GL_TEXTURE_2D);
			
	/*		gl.glEnd();
			float[] ones = {1,1,1,1};
			float[] zeroes = new float[4];
			gl.glDepthMask(false);
			gl.glBegin(GL.GL_QUADS);
				
				float[] ambient = {(float)7/255, (float)113/255, (float)254/255, .25f};
				gl.glColor4fv(ambient, 0);
				gl.glVertex3d(xLoc, 0, zLoc);
				gl.glVertex3d(xLoc+10, 0, zLoc);
				gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, ones, 0);
				gl.glVertex3d(xLoc+10, 10, zLoc);
				gl.glVertex3d(xLoc, 10, zLoc);
				
				gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, zeroes, 0);
				gl.glVertex3d(xLoc, 0, zLoc+10);
				gl.glVertex3d(xLoc+10, 0, zLoc+10);
				gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, ones, 0);
				gl.glVertex3d(xLoc+10, 10, zLoc+10);
				gl.glVertex3d(xLoc, 10, zLoc+10);
				
				gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, zeroes, 0);
				gl.glVertex3d(xLoc, 0, zLoc);
				gl.glVertex3d(xLoc, 0, zLoc+10);
				gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, ones, 0);
				gl.glVertex3d(xLoc, 10, zLoc+10);
				gl.glVertex3d(xLoc, 10, zLoc);
				
				gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, zeroes, 0);
				gl.glVertex3d(xLoc+10, 0, zLoc);
				gl.glVertex3d(xLoc+10, 0, zLoc+10);
				gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, ones, 0);
				gl.glVertex3d(xLoc+10, 10, zLoc+10);
				gl.glVertex3d(xLoc+10, 10, zLoc);
			gl.glEnd();	
			gl.glDepthMask(true);
			gl.glBegin(GL.GL_LINES);*/
			
			break;
		default:
			gl.glNormal3d(xLoc, 100, zLoc);
			gl.glVertex3d(xLoc, 0, zLoc);
			
			//gl.glNormal3d(xLoc + 10, 0, zLoc);
			gl.glVertex3d(xLoc + 10, 0, zLoc);
			
	//		gl.glNormal3d(xLoc + 10, 0, zLoc + 10);
			gl.glVertex3d(xLoc + 10, 0, zLoc + 10);
			
		//	gl.glNormal3d(xLoc, 0, zLoc + 10);
			gl.glVertex3d(xLoc, 0, zLoc + 10);
		}
		
		
		
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[4], 0);
		
		if(terrain == Cell.TER_CASTLE)
		{
	//		gl.glMaterialfv(arg0, arg1, arg2)
		}
	}
	
	public void draw(Graphics g, int x, int y)
	{
		switch(terrain)
		{
		case Cell.TER_CASTLE:
			g.setColor(Color.red);
			break;
		case Cell.TER_TOWER:
			g.setColor(Color.blue);
			break;
		case Cell.TER_EMPTY:
			g.setColor(Color.green.darker());
			break;
		case Cell.TER_PATH:
			g.setColor(Color.yellow);
			break;
		case Cell.TER_ENTRANCE:
			g.setColor(Color.magenta.darker().darker().darker());
		}
		
		g.fillRect(x, y, 40, 40);
	}
}
