package com.remote.ld.one.component;

import java.util.ArrayList;

import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.ColliderBox;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentProjectile extends Component {
	
	public Vector2 environmentAcceleration = new Vector2(0,10);
	public Vector2 velocity = new Vector2(0,0);
	public float damage = 10;
	public boolean enemy = false;
	public Vector2 colliderPos;
	public Vector2 colliderDim;
	private boolean grounded = false;
	private boolean despawn = false;
	
	private long despawnTimer = -1;
	private int blinknumber = -1;

	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		if(editor)
			colliderPos.add(entity.pos).getColliderWithDim(colliderDim).drawCollider(0x00ff00);
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}

	@Override
	public void tick(int arg0, int arg1, int arg2) {
		if(despawn)
		{
			long timepassed = System.currentTimeMillis()-despawnTimer;
			if(timepassed > 1000)
				map.getEntityList().removeEntityFromList(entity);
			if(timepassed/100 > blinknumber)
			{
				blinknumber = (int) (timepassed/100);
				if(entity.material.getAlpha() > 0)
					entity.material.setAlpha(0);
				else
					entity.material.setAlpha(1);
			}
		}
		if(grounded)
			return;
		velocity = velocity.add(environmentAcceleration);
		Vector2 correction = map.getCorrection(entity.pos.add(colliderPos).getColliderWithDim(colliderDim), velocity);
		velocity = velocity.add(correction);
		grounded = velocity.y == 0 && correction.y != 0;
		if(grounded)
			despawn = true;
		entity.pos = entity.pos.add(velocity);
		if(grounded)
			despawnTimer = System.currentTimeMillis();
		
		if(!despawn)
			checkHit();
	}
	
	private void checkHit()
	{
		for(int x=0;x<map.getEntityList().size();x++)
		{
			Entity e = map.getEntityList().get(x);
			if(enemy)
			{
				ArrayList<ComponentPlayer> comps = e.getComponentsOfType(ComponentPlayer.class);
				if(comps.size() > 0)
				{
					ColliderBox coll = comps.get(0).colliderPos.add(e.pos).getColliderWithDim(comps.get(0).colliderDim);
					if(entity.pos.add(colliderPos).getColliderWithDim(colliderDim).getCollision(coll, new Vector2(0,0)).collides)
					{
						comps.get(0).hurt(damage);
						velocity.x *= -1;
						despawn = true;
						despawnTimer = System.currentTimeMillis();
					}
				}
			} else
			{
				ArrayList<ComponentEnemy> comps = e.getComponentsOfType(ComponentEnemy.class);
				if(comps.size() > 0)
				{
					ColliderBox coll = comps.get(0).colliderPos.add(e.pos).getColliderWithDim(comps.get(0).colliderDim);
					if(entity.pos.add(colliderPos).getColliderWithDim(colliderDim).getCollision(coll, new Vector2(0,0)).collides)
					{
						comps.get(0).hurt(damage);
						velocity.x *= -1;
						despawn = true;
						despawnTimer = System.currentTimeMillis();
					}
				}
			}
		}
	}

	@Override
	public void apply() {
		
	}

}
