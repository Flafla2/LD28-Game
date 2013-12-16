package com.remote.ld.one.gui;

import com.remote.remote2d.editor.GuiEditor;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiWin extends GuiMenu {
	
	public GuiWin()
	{
		super();
		backgroundColor = 0x33ff33;
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(0,new Vector2(screenWidth()/2-300,screenHeight()/2-20),new Vector2(600,40),"Return to Main Menu"));
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		Fonts.get("Fipps").drawCenteredString("You Won!", 20, 50, 0xffffff);
		Fonts.get("Fipps").drawCenteredString("Health: "+GuiInGameOne.health, 80, 20, 0x000000);
		Fonts.get("Fipps").drawCenteredString("Money: "+GuiInGameOne.money, 110, 20, 0x000000);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			while(Remote2D.guiList.size() > 1 && !(Remote2D.guiList.peek() instanceof GuiEditor))
				Remote2D.guiList.pop();
		}
	}
	
}
