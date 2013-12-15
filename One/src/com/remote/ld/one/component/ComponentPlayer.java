package com.remote.ld.one.component;

import org.lwjgl.input.Keyboard;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.io.R2DFileUtility;
import com.remote.remote2d.engine.logic.ColliderBox;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentPlayer extends Component {
	
	public float health = 1;
	public float money = 1337;
	
	public Vector2 environmentAcceleration = new Vector2(0,10);
	public String xboxPrefab;
	
	private Vector2 velocity = new Vector2(0,0);
	private boolean r2DExists = false;
	private boolean grounded = false;

	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		r2DExists = R2DFileUtility.R2DExists(xboxPrefab);
		Log.debug("XBox Prefab doesn't exist!!!");
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		
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
		
		if(space && grounded)
			velocity.y = -60;
		
		Vector2 correction = map.getCorrection(entity.getPosGlobal().getColliderWithDim(entity.dim), velocity);
		velocity = velocity.add(correction);
		grounded = velocity.y == 0 && correction.y != 0;
		entity.pos = entity.pos.add(velocity);
		
		Vector2 globalPos = entity.getPosGlobal();
		ColliderBox renderarea = entity.getMap().camera.getMapRenderArea();
        float right = renderarea.pos.x+renderarea.dim.x*2/3;
        float left = renderarea.pos.x;
        if(globalPos.x+entity.getDim().x > right)
                entity.getMap().camera.pos.x += (entity.pos.x+entity.getDim().x)-right;
        if(globalPos.x < left)
                entity.getMap().camera.pos.x -= left-entity.pos.x;
		
		if(mouse && r2DExists)
			map.getEntityList().instantiatePrefab(xboxPrefab).pos = new Vector2(entity.pos.x+entity.dim.x,entity.pos.y);
			
	}

	@Override
	public void apply() {
		
	}

}
