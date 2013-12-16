package com.remote.ld.one.component;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.remote.ld.one.gui.GuiDead;
import com.remote.ld.one.gui.GuiInGameOne;
import com.remote.remote2d.engine.AudioHandler;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Animation;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.io.R2DFileUtility;
import com.remote.remote2d.engine.logic.ColliderBox;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentPlayer extends Component {
	
	public float health = 100;
	public float money = 5000;
		
	public Vector2 environmentAcceleration = new Vector2(0,10);
	public String xboxPrefab;
	public String gamePrefab;
	
	public int levelLeft = 0;
	public int levelRight = 1000;
	
	public Vector2 colliderPos = new Vector2(0,0);
	public Vector2 colliderDim = new Vector2(10,10);
	
	public Animation walkAnim;
	public Animation throwAnim;
	
	public static final float CONSOLE_COST = 499.99f;
	public static final float GAME_COST = 69.99f;
	
	private Vector2 velocity = new Vector2(0,0);
	private long lastThrow = -1;
	private boolean xboxExists = false;
	private boolean gameExists = false;
	private boolean grounded = false;
	private int direction = 1;

	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		xboxExists = R2DFileUtility.R2DExists(xboxPrefab);
		gameExists = R2DFileUtility.R2DExists(gamePrefab);
		GuiInGameOne.health = health;
		GuiInGameOne.money = money;
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		if(!editor)
			return;
		GL11.glLineWidth(5);
        
        Renderer.drawLine(new Vector2(levelLeft, entity.pos.y), new Vector2(levelLeft,entity.pos.y+entity.dim.y), 0x000ff, 1.0f);
        Renderer.drawLine(new Vector2(levelRight, entity.pos.y), new Vector2(levelRight,entity.pos.y+entity.dim.y), 0x000ff, 1.0f);
        
		GL11.glLineWidth(1);
		
		colliderPos.add(entity.pos).getColliderWithDim(colliderDim).drawCollider(0x00ff00);
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}

	@Override
	public void tick(int i, int j, int k) {
		boolean a = Keyboard.isKeyDown(Keyboard.KEY_A);
		boolean d = Keyboard.isKeyDown(Keyboard.KEY_D);
		boolean mouse = Remote2D.hasMouseBeenPressed();
		boolean space = Remote2D.getIntegerKeyboardList().contains(Keyboard.KEY_SPACE);
		
		velocity = velocity.add(environmentAcceleration);
		
		velocity.x = 0;
		if(a)
			velocity.x = -20;
		if(d)
			velocity.x = 20;
		
		if(Remote2D.getIntegerKeyboardList().contains(Keyboard.KEY_A))
			direction = -1;
		else if(Remote2D.getIntegerKeyboardList().contains(Keyboard.KEY_D))
			direction = 1;
		
		if(space && grounded)
		{
			AudioHandler.playSound("res/sound/jump.wav", false, false);
			velocity.y = -60;
		}
		
		Vector2 correction = map.getCorrection(colliderPos.add(entity.pos).getColliderWithDim(colliderDim), velocity);
		velocity = velocity.add(correction);
		grounded = velocity.y == 0 && correction.y != 0;
		entity.pos = entity.pos.add(velocity);
		
		map.camera.pos = entity.pos.add(new Vector2(entity.dim.x/2, entity.dim.y/2-100));
		ColliderBox renderarea = map.camera.getMapRenderArea();
        if(renderarea.pos.x < levelLeft)
        	renderarea.pos.x = levelLeft;
        if(renderarea.pos.x+renderarea.dim.x > levelRight)
        	renderarea.pos.x = levelRight-renderarea.dim.x;
        map.camera.pos = renderarea.pos.add(map.camera.getDimensions().divide(new Vector2(2)));
        map.camera.updatePos();
        
        if(Keyboard.isKeyDown(Keyboard.KEY_1))
        	GuiInGameOne.currentWeapon = 1;
        else if(Keyboard.isKeyDown(Keyboard.KEY_2))
        	GuiInGameOne.currentWeapon = 0;
		
		if(mouse && exists() && hasMoney())
		{
			Entity e;
			if(GuiInGameOne.currentWeapon == 0)
				e = map.getEntityList().instantiatePrefab(xboxPrefab);
			else
				e = map.getEntityList().instantiatePrefab(gamePrefab);
			spendMoney();
			ArrayList<ComponentProjectile> proj = e.getComponentsOfType(ComponentProjectile.class);
			if(direction > 0)
			{
				e.pos = new Vector2(entity.pos.x+entity.dim.x,entity.pos.y);
				proj.get(0).velocity.x = Math.abs(proj.get(0).velocity.x);
			}
			else
			{
				e.pos = new Vector2(entity.pos.x-e.dim.x,entity.pos.y);
				proj.get(0).velocity.x = -Math.abs(proj.get(0).velocity.x);
			}
			lastThrow = System.currentTimeMillis();
			AudioHandler.playSound("res/sound/throw.wav", false, false);
		} else if(mouse && !hasMoney() && exists())
			AudioHandler.playSound("res/sound/no_shoot.wav", false, false);
		
		if(System.currentTimeMillis()-lastThrow < 500 && !entity.material.getAnimation().getPath().equals(throwAnim.getPath()))
			entity.material.setAnimation(throwAnim);
		else if(System.currentTimeMillis()-lastThrow > 500 && !entity.material.getAnimation().getPath().equals(walkAnim.getPath()))
			entity.material.setAnimation(walkAnim);
		
		if(entity.material.getAnimation() != null)
		{
			if(a || d)
				entity.material.getAnimation().animate = true;
			else
			{
				entity.material.getAnimation().animate = false;
				entity.material.getAnimation().setCurrentFrame(0);
			}
			
			entity.material.getAnimation().flippedX = direction < 0;
		}
			
	}
	
	public boolean exists()
	{
		if(GuiInGameOne.currentWeapon == 0)
			return xboxExists;
		else
			return gameExists;
	}
	
	public boolean hasMoney()
	{
		if(GuiInGameOne.currentWeapon == 0)
			return money >= CONSOLE_COST;
		else
			return money >= GAME_COST;
	}
	
	public void spendMoney()
	{
		if(GuiInGameOne.currentWeapon == 0)
			money -= CONSOLE_COST;
		else
			money -= GAME_COST;
		GuiInGameOne.money = round(this.money,2);
	}
	
	public void hurt(float damage)
	{
		health -= damage;
		if(health <= 0)
			Remote2D.guiList.push(new GuiDead());
		GuiInGameOne.health = round(health,2);
		
		AudioHandler.playSound("res/sound/hurt.wav", false, false);
	}
	
	public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	
	public void addMoney(float money)
	{
		this.money += money;
		GuiInGameOne.money = round(this.money,2);
		
		AudioHandler.playSound("res/sound/Pickup_coin.wav", false, false);
	}

	@Override
	public void apply() {
		
	}

}
