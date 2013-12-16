package com.remote.ld.one.gui;

import com.remote.remote2d.engine.StretchType;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.gui.GuiInGame;
import com.remote.remote2d.engine.world.Map;

public class GuiInGameOne extends GuiInGame {
	
	public static float health = 100;
	public static float money = 0;
	public static int currentWeapon = 1;

	public GuiInGameOne(Map map) {
		super(map);
		backgroundColor = 0x99ccff;
	}
	
	public void render(float interpolation)
	{
		map.render(false, interpolation);
		Fonts.get("Fipps").drawString("Health: "+Math.max(health, 0), 10, 10, 20, 0x000000);
		Fonts.get("Fipps").drawString("Money: $"+Math.max(money, 0), 10, 40, 20, 0x000000);
		Fonts.get("Fipps").drawString("Current Weapon: "+weaponName(), 10, 60, 20, 0x000000);
	}
	
	public static String weaponName()
	{
		if(currentWeapon == 0)
			return "XBOX ONE";
		else
			return "CALL OF DUTY: MODERN DOGE";
	}
	
	@Override
	public StretchType getOverrideStretchType()
	{
		return StretchType.SCALE;
	}

}
