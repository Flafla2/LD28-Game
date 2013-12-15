package com.remote.ld.one.component;

import java.util.ArrayList;
import java.util.Random;

import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.io.R2DFileUtility;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentEnemy extends Component {
	
	private static final int FRONT_LINE_OF_SIGHT = 500;
	private static final int BACK_LINE_OF_SIGHT = 100;
	private static final int PURSUIT_LINE_OF_SIGHT = 1000;
	private static final int FIRING_DISTANCE_GAMEBOX = 500;
	private static final int FIRING_DISTANCE_CONSOLE = 300;
	private static final int MINIMUM_DISTANCE_TO_PLAYER = 50;
	private static final Random random = new Random();

	public Vector2 environmentAcceleration = new Vector2(0,10);
	public String consolePrefab;
	public String gamePrefab;
	public Entity player;
	
	private Vector2 velocity = new Vector2(0,0);
	private boolean consoleR2DExists = false;
	private boolean gameR2DExists = false;
	private boolean grounded = false;
	private long lastAttack = -1;
	private long waitAttack = 1000;
	private long lastIdleWalk = -1;
	private long waitIdleWalk = 1000;
	private Vector2 lastCorrection = new Vector2(0,0);
	private EnemyState state = EnemyState.IDLE;
	/**
	 * 1 is right, -1 is left
	 */
	private byte direction = 1;
	private boolean pursuit = false;

	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		consoleR2DExists = R2DFileUtility.R2DExists(consolePrefab);
		gameR2DExists = R2DFileUtility.R2DExists(gamePrefab);
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}

	@Override
	public void tick(int i, int j, int k) {
		decide();
		
		velocity = velocity.add(environmentAcceleration);
		
		velocity.x = 0;
		if(state == EnemyState.WALK)
			velocity.x = 20*direction;
		
		if(!lastCorrection.equals(new Vector2(0,0)) && grounded)
			velocity.y = -60;
		
		Vector2 correction = map.getCorrection(entity.getPosGlobal().getColliderWithDim(entity.dim), velocity);
		velocity = velocity.add(correction);
		grounded = velocity.y == 0 && correction.y != 0;
		entity.pos = entity.pos.add(velocity);
			
	}

	@Override
	public void apply() {
		
	}
	
	/**
	 * Basic AI on what to do.  Run this every tick.  Shamefully complicated, but who cares.
	 */
	private void decide()
	{
		float thisSightPos = entity.pos.x + entity.dim.x /2;
		float playerSightPos = player.pos.x + player.dim.x /2;
		float distance = (playerSightPos-thisSightPos)*direction; // positive if we are facing, negative if we are not
		
		boolean canSeeNormal = distance > 0 ? Math.abs(distance) <= FRONT_LINE_OF_SIGHT : Math.abs(distance) <= BACK_LINE_OF_SIGHT;
		boolean canSeePursuit = Math.abs(distance) <= PURSUIT_LINE_OF_SIGHT;
		boolean canSee = pursuit ? canSeePursuit : canSeeNormal;
		
		boolean firingDistance_console = Math.abs(distance) <= FIRING_DISTANCE_CONSOLE;
		boolean firingDistance_game = Math.abs(distance) <= FIRING_DISTANCE_GAMEBOX;
		switch(state)
		{
		case IDLE:
			if(canSee)
			{
				if(distance < 0)
					direction *= -1;
				if(System.currentTimeMillis()-lastAttack > waitAttack && (firingDistance_console || firingDistance_game))
					state = EnemyState.ATTACK;
				else if(Math.abs(distance) > MINIMUM_DISTANCE_TO_PLAYER)
					state = EnemyState.WALK;
				pursuit = true;
				break;
			} else
				pursuit = false;
			
			if(random.nextInt(100) == 0)
				direction *= -1;
			if(random.nextInt(200) == 0)
			{
				state = EnemyState.WALK;
				lastIdleWalk = System.currentTimeMillis();
				waitIdleWalk = 500+random.nextInt(1000);
			}
			break;
		case WALK:
			if(canSee)
			{
				if(distance < 0)
					direction *= -1;
				pursuit = true;
				if(System.currentTimeMillis()-lastAttack > waitAttack && (firingDistance_console || firingDistance_game))
					state = EnemyState.ATTACK;
				else if(Math.abs(distance) <= MINIMUM_DISTANCE_TO_PLAYER)
					state = EnemyState.IDLE;
			} else if(System.currentTimeMillis()-lastIdleWalk > waitIdleWalk)
			{
				state = EnemyState.IDLE;
				pursuit = false;
			} else
				pursuit = false;
			break;
		case ATTACK:
			if(!canSee)
			{
				state = EnemyState.IDLE;
				pursuit = false;
				break;
			}
			
			Entity proj = null;
			if(consoleR2DExists && firingDistance_console)
			{
				proj = map.getEntityList().instantiatePrefab(consolePrefab);
				state = EnemyState.WALK;
				lastAttack = System.currentTimeMillis();
				waitAttack = random.nextInt(2000)+500;
			} else if(gameR2DExists && firingDistance_game)
			{
				proj = map.getEntityList().instantiatePrefab(gamePrefab);
				state = EnemyState.WALK;
				lastAttack = System.currentTimeMillis();
				waitAttack = random.nextInt(2000)+500;
			}
			
			if(proj != null)
			{
				if(direction >= 0)
					proj.pos.x = entity.pos.x-proj.dim.x;
				else
					proj.pos.x = entity.pos.x+entity.dim.x;
				
				proj.pos.y = entity.pos.y;
				ArrayList<ComponentProjectile> comps = entity.getComponentsOfType(ComponentProjectile.class);
				if(comps.size()>0)
					comps.get(0).velocity.x = Math.abs(comps.get(0).velocity.x)*direction;
			}
			break;
		}
	}
	
	private enum EnemyState
	{
		IDLE, WALK, ATTACK;
	}

}
