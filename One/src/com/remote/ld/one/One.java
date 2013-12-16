package com.remote.ld.one;

import com.esotericsoftware.minlog.Log;
import com.remote.ld.one.component.ComponentEnemy;
import com.remote.ld.one.component.ComponentFinish;
import com.remote.ld.one.component.ComponentMoney;
import com.remote.ld.one.component.ComponentPlayer;
import com.remote.ld.one.component.ComponentProjectile;
import com.remote.ld.one.gui.GuiInGameOne;
import com.remote.ld.one.gui.GuiMainMenu;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.Remote2DGame;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.entity.InsertableComponentList;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.world.Map;

public class One extends Remote2DGame {
	
	public static final float CONSOLE_COST = 499.99f;
	public static final float GAME_COST = 69.99f;
	
	public static void main(String[] args)
	{
		Remote2D.startRemote2D(new One());
	}

	@Override
	public void initGame() {
		Log.DEBUG();
		Fonts.add("Fipps","res/fonts/Fipps-Regular.otf",false);
		
		InsertableComponentList.addInsertableComponent("Player", ComponentPlayer.class);
		InsertableComponentList.addInsertableComponent("Projectile", ComponentProjectile.class);
		InsertableComponentList.addInsertableComponent("Enemy", ComponentEnemy.class);
		InsertableComponentList.addInsertableComponent("Money", ComponentMoney.class);
		InsertableComponentList.addInsertableComponent("Finish", ComponentFinish.class);
		
		Remote2D.guiList.push(new GuiMainMenu());
	}
	
	@Override
	public GuiMenu getNewInGameGui(Map map)
	{
		return new GuiInGameOne(map);
	}
	
}
