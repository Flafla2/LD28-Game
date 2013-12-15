package com.remote.ld.one.gui;

import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.gui.GuiInGame;
import com.remote.remote2d.engine.world.Map;

public class GuiInGameOne extends GuiInGame {
	
	public static float health = 100;
	public static float money = 0;

	public GuiInGameOne(Map map) {
		super(map);
		backgroundColor = 0x99ccff;
	}
	
	public void render(float interpolation)
	{
		
		map.render(false, interpolation);
		Fonts.get("Fipps").drawString("Health: "+health, 10, 10, 20, 0x000000);
		Fonts.get("Fipps").drawString("Money: $"+money, 10, 40, 20, 0x000000);
	}

}
