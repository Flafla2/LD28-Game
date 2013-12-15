package com.remote.ld.one.gui;

import com.remote.ld.art.ArtList;
import com.remote.remote2d.editor.GuiEditor;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.StretchType;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiMainMenu extends GuiMenu {
	
	private String message = "";
	private long lastMessageTime = -1;
	
	
	public GuiMainMenu()
	{
		super();
		backgroundColor = 0x660000;
	}
	
	@Override
	public void renderBackground(float interpolation)
	{
		if(System.currentTimeMillis()-lastMessageTime < 5000)
			Fonts.get("Fipps").drawCenteredString(message, screenHeight()-20, 20, 0x000000);
		
		int imgsize = Math.min(screenWidth(), screenHeight());
		Renderer.drawRect(new Vector2(screenWidth()/2-imgsize/2,screenHeight()/2-imgsize/2), new Vector2(imgsize), ArtList.menacing_santa, 0xccaaaa, 1);
		Renderer.pushMatrix();
		Renderer.translate(new Vector2(10,screenHeight()/2-104));
		Fonts.get("Fipps").drawString("YOU ONLY GET", 0, 0, 40, 0x000000);
		Renderer.drawRect(new Vector2(0,40), new Vector2(512,128), ArtList.xbone_logo, 0xffffff, 1);
		Fonts.get("Fipps").drawString("FOR CHRISTMAS", 0, 168, 40, 0x000000);
		Renderer.popMatrix();
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		int width = Math.max(screenWidth()/2,200);
		int xpos = screenWidth()-width;
		
		buttonList.add(new GuiButton(0,new Vector2(xpos,screenHeight()/2-70),new Vector2(width,40),"Play"));
		buttonList.add(new GuiButton(1,new Vector2(xpos,screenHeight()/2-20),new Vector2(width,40),"Open Editor"));
		buttonList.add(new GuiButton(2,new Vector2(xpos,screenHeight()/2+30),new Vector2(width,40),"Exit"));
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
			setMessage("Playing isn't supported ;P");
		else if(button.id == 1)
			Remote2D.guiList.push(new GuiEditor());
		else if(button.id == 2)
			Remote2D.running = false;
	}
	
	@Override
	public StretchType getOverrideStretchType()
	{
		return StretchType.NONE;
	}
	
	private void setMessage(String message)
	{
		this.message = message;
		this.lastMessageTime = System.currentTimeMillis();
	}
	
	
}
