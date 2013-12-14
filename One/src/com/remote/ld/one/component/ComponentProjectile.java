package com.remote.ld.one.component;

import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentProjectile extends Component {
	
	public Vector2 environmentAcceleration = new Vector2(0,10);
	public Vector2 velocity = new Vector2(0,0);
	private boolean grounded = false;
	private long groundedTimer = -1;
	private int blinknumber = -1;

	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		
	}

	@Override
	public void renderAfter(boolean arg0, float arg1) {
		
	}

	@Override
	public void renderBefore(boolean arg0, float arg1) {
		
	}

	@Override
	public void tick(int arg0, int arg1, int arg2) {
		if(grounded)
		{
			long timepassed = System.currentTimeMillis()-groundedTimer;
			if(timepassed > 2000)
				map.getEntityList().removeEntityFromList(entity);
			if(timepassed > 1000 && timepassed/100 > blinknumber)
			{
				blinknumber = (int) (timepassed/100);
				if(entity.material.getAlpha() > 0)
					entity.material.setAlpha(0);
				else
					entity.material.setAlpha(1);
			}
				
			return;
		}
		velocity = velocity.add(environmentAcceleration);
		Vector2 correction = map.getCorrection(entity.pos.getColliderWithDim(entity.dim), velocity);
		velocity = velocity.add(correction);
		grounded = velocity.y == 0 && correction.y != 0;
		entity.pos = entity.pos.add(velocity);
		if(grounded)
			groundedTimer = System.currentTimeMillis();
	}

	@Override
	public void apply() {
		
	}

}
