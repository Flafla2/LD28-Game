package com.remote.ld.one;

import com.remote.ld.one.gui.GuiMainMenu;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.Remote2DGame;
import com.remote.remote2d.engine.art.Fonts;

public class One extends Remote2DGame {
	
	public static void main(String[] args)
	{
		Remote2D.startRemote2D(new One());
	}

	@Override
	public void initGame() {
		Fonts.add("Fipps","res/fonts/Fipps-Regular.otf",false);
		Remote2D.guiList.push(new GuiMainMenu());
	}
	
}
