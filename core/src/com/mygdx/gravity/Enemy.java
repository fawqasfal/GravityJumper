public class Enemy {
	Rectangle rectRep;
	final TextureRegion defImage;
	int damage;
	int give;
	boolean alive; 

	public final static String DEFAULT_IMAGE = "enemy.png";
	public final static float DEFAULT_SIZE = 16;
	public final static int SCALE = 4.5f;
	public final static int MOVE_AMT = 100;
	public Enemy(float spawnX, float spawnY, dmg, give) {
		//It takes a nation of millions to hold us back.
		this.rectRep = new Rectangle(spawnX, spawnY, SCALE * DEFAULT_SIZE, SCALE * DEFAULT_SIZE);
		this.defImage = new TextureRegion(new Texture(Gdx.files.internal(DEFAULT_IMAGE)));
		this.damage = damage;
		this.give = give;
		this.alive = true;
	}

	public void die() {
		this.alive = false;
		this.rectRep = null;
		this.defImage = null;
	}

	public void floatLeft(amt) {
		this.rectRep.x -= amt;
		int direction = randrange(-1,2) 
	}

	public int randrange(float low, float high) {
		//[low, high)
		return low + (int) Math.random() * (high - low);
	}
	
}