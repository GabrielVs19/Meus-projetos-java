package br.com.gvs.menuVipB;

public enum IconType {
	
	HAT(ClickType.COMMAND, 0, "/hat"),
	TAG(ClickType.COMMAND, 1, "/tag"),
	FERTILIZAR(ClickType.COMMAND, 2, "/fertilizar"),
	MONTARIA(ClickType.COMMAND, 7, "/montaria"),
	BENCH(ClickType.COMMAND, 23, "/bench"),
	FURNACE(ClickType.COMMAND, 22, "/furnace"),
	BAU(ClickType.COMMAND, 21, "/bau"),
	KIT_NBVIP(ClickType.COMMAND, 6, "/kit nbvip"),
	PVP_OFF(ClickType.COMMAND, 8, "/pvp off"),
	WARP_VIP(ClickType.COMMAND, 34, "/warp vip"),
	WARP_REGION(ClickType.COMMAND, 35, "/warp region"),
	WARP_BLOCOS(ClickType.COMMAND, 27, "/warp blocos"),
	MINERAR(ClickType.COMMAND, 28, "/minerar"),
	DUMMY(ClickType.COMMAND, 33, "/warp dummy"),
	SPAWN(ClickType.COMMAND, 29, "/warp spawn"),
	DESENCANTAR(ClickType.COMMAND, 31, "/desencantar'"),
	LAST_KILL(ClickType.VIEW, 48),
	LAST_DEATH(ClickType.VIEW, 47),
	VIP(ClickType.VIEW, 49),
	KARMA(ClickType.VIEW, 50),
	INSURE(ClickType.COMMAND, 51, "/insure"),
	MC(ClickType.COMMAND, 4, "/mc");
	
	
	private ClickType clickType;
	private int slot;
	private String command;
	
	IconType(ClickType clickType, int slot){
		this.slot = slot;
		this.clickType = clickType;
	}
	
	IconType(ClickType clickType, int slot, String command){
		this.clickType = clickType;
		this.slot = slot;
		this.command = command;
	}
	
	public String getCommand(){
		return this.command;
	}
	
	public int getSlot(){
		return this.slot;
	}
	
	public ClickType getClickType(){
		return this.clickType;
	}
	
	public static IconType getIconTypeBySlot(int slot){
		for(IconType ic : values()){
			if(ic.getSlot() == slot){
				return ic;
			}
		}
		return null;
	}
	
	public enum ClickType{
		COMMAND, VIEW;	
	}
}
