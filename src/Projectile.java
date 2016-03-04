import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;

public class Projectile
{
	public static final int TYPE_MGUN = 0, 
							TYPE_MISSILE = 1,
							TYPE_LASER = 2;
	
	public Enemy numberOne;
	
	public int type;
	public double x, y, z, speed, damage;
	public boolean removeMe = false;
	
	/*-------Missile--*/
	public boolean exploding = false;
	public int explosionSize = -1337;
	public double maxExplosionSize;
	public Enemy target;
	
	/*-------Laser----*/
	public double angle, radius = 0;
	
	private double xDist, yDist, zDist, theta, phi;
	
	public Projectile(int type, double x, double y, double z, double speed, Enemy e)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.speed = speed;
		target = e;
	}
	
	public Projectile(int type, double x, double y, double z, double damage, double speed, double splash, Enemy e)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.damage = damage;
		target = e;
		maxExplosionSize = splash;
		this.speed = speed;
	}
	
	public Projectile(int type, double x, double y, double z, double damage, double angle)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.damage = damage;
		this.angle = angle;
	}
	
	public void move()
	{
		switch(type)
		{
		case TYPE_MGUN:
			xDist = target.x2 - x;
			yDist = -y;
			zDist = target.z2 - z;
			
			theta = Math.atan2(zDist, xDist);
			phi = Math.atan2(yDist, Math.hypot(xDist, zDist));
			
		//	System.out.println(phi + " - " + theta);
			double tempSpeed = (.75*Math.random() + .5)*speed;
			
			z += tempSpeed*Math.sin(theta)*Math.cos(phi);
			y += tempSpeed*Math.sin(phi);
			x += tempSpeed*Math.cos(theta)*Math.cos(phi);
			
			if(Math.hypot(xDist, Math.hypot(yDist, zDist)) <= speed/2) removeMe = true;
			break;
		case TYPE_MISSILE:
		
			if(!exploding)
			{
				xDist = target.x2 - x;
				yDist = -y;
				zDist = target.z2 - z;
				
				theta = Math.atan2(zDist, xDist);
				phi = Math.atan2(yDist, Math.hypot(xDist, zDist));
				
			//	System.out.println(phi + " - " + theta);
				
				z += speed*Math.sin(theta)*Math.cos(phi);
				y += speed*Math.sin(phi);
				x += speed*Math.cos(theta)*Math.cos(phi);
				
				if(Math.hypot(xDist, Math.hypot(yDist, zDist)) <= speed/2)
				{
					exploding = true;
					for(int i = 0; i < World.enemies.size(); i ++)
					{
						Enemy e = World.enemies.get(i);
						if(Math.hypot(e.x - target.x, e.z - target.z) <= maxExplosionSize)
						{
							e.resetDamagedRecently();
							e.hp -= damage;
						}
					}
					explosionSize = 1;
				}
			}
			break;
		case TYPE_LASER:
			
			break;
		}
	}
	
	public void draw(GL gl, GLUT glut)
	{
		switch(type)
		{
		case TYPE_MGUN:
			xDist = target.x2 - x;
			yDist = -y;
			zDist = target.z2 - z;
			
			theta = Math.atan2(zDist, xDist);
			phi = Math.atan2(yDist, Math.hypot(xDist, zDist));
			
			gl.glPushMatrix();
			
			gl.glTranslated(x-5, y, z-5);
			
			gl.glRotated(Math.toDegrees(theta)-90, 0, -1, 0);
			gl.glRotated(Math.toDegrees(phi), -1, 0, 0);
		//	gl.glRotated(90, 0, 1, 0);
			//gl.glRotated(90, 1, 0, 0);
			gl.glColor3d(0,0,0);
			
			glut.glutSolidCone(.25,1, 8, 1);
			
			
			gl.glPopMatrix();
			break;
		case TYPE_MISSILE:
			if(!exploding)
			{
				xDist = target.x2 - x;
				yDist = -y;
				zDist = target.z2 - z;
				
				theta = Math.atan2(zDist, xDist);
				phi = Math.atan2(yDist, Math.hypot(xDist, zDist));
				
				gl.glPushMatrix();
				
				gl.glTranslated(x-5, y, z-5);
				
				gl.glRotated(Math.toDegrees(theta)-90, 0, -1, 0);
				gl.glRotated(Math.toDegrees(phi), -1, 0, 0);
			//	gl.glRotated(90, 0, 1, 0);
				//gl.glRotated(90, 1, 0, 0);
				gl.glColor3d(1,0,0);
				
				glut.glutSolidCone(1, 5, 8, 1);
				
				
				gl.glPopMatrix();
				
			/*	gl.glBegin(GL.GL_LINES);
					gl.glVertex3d(x, y, z);
					gl.glVertex3d(target.x, 0, target.z);
				gl.glEnd();*/
			}
			else
			{
				gl.glPushMatrix();
				gl.glTranslated(x-5, y, z-5);
				
				gl.glColor4d(1,.5,0, .75);
				boolean flue = false;
				boolean tralse = true;
				gl.glDepthMask(flue);
				gl.glEnable(GL.GL_CLIP_PLANE0);
				glut.glutSolidSphere(explosionSize, 16, 16);
				gl.glDisable(GL.GL_CLIP_PLANE0);
				gl.glDepthMask(tralse);
				explosionSize += maxExplosionSize/10;
				if(explosionSize >= maxExplosionSize) removeMe = true;
				
				gl.glPopMatrix();
			}
			break;
		case TYPE_LASER:
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[]{0,.5f,1}, 0);
			gl.glColor4f(0, 0, 1, .5f);
			
			if(radius < 5) radius ++;
			else removeMe = true;

			gl.glDepthMask(false);
			gl.glPushMatrix();
			gl.glTranslated(x, y, z);
			gl.glRotated(45, 0, 1, 0);
			gl.glScaled(-1, 1, 1);
			gl.glRotated(-45, 0, 1, 0);
			gl.glRotated(Math.toDegrees(angle), 0, 1, 0);
			glut.glutSolidCylinder(radius, 400, 8, 1);
			gl.glPopMatrix();
			gl.glDepthMask(true);
			gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, new float[3], 0);
		}
	}
}
