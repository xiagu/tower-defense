
public enum TowerType
{
	MGUN	(75, 1, 0, 0, 40, "Machine Gun Turret"),
	MISSILE	(50, 15, 10, 20, 60, "Missile Turret"),
	LASER	(1337, 30, 0, 50, 200, "Laser Turret");
	
/*	private double RANGE_L1 = {75, 50, 1337},
	 				 DAMAGE_L1 = {1, 15, 30},
	 				 SPLASH_L1 = {0, 10, 0};
	private int COOLDOWN_L1 = {0, 10, 25},
				  COST = {25,40,200};*/
	
	private double range, damage, splash;
	private int cooldown, cost;
	private String name;
	
	private TowerType(double range, double damage, double splash, int cooldown, int cost, String name)
	{
		this.range = range;
		this.damage = damage;
		this.splash = splash;
		this.cooldown = cooldown;
		this.cost = cost;
		this.name = name;
	}

	public int getCooldown() {
		return cooldown;
	}

	public int getCost() {
		return cost;
	}

	public double getDamage() {
		return damage;
	}

	public double getRange() {
		return range;
	}

	public double getSplash() {
		return splash;
	}

	public String getName() {
		return name;
	}
	

}
