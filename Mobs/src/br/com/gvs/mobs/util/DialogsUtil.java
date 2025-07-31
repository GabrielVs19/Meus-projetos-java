package br.com.gvs.mobs.util;


import java.util.List;


public class DialogsUtil
{
	
	
	private List<String> spawn;
	private List<String> death;
	private List<String> kill;
	
	public DialogsUtil(List<String> spawn, List<String> death, List<String> kill)
	{
		this.spawn = spawn;
		this.death = death;
		this.kill = kill;
	}
	
	public String getSpawnDialog()
	{
		if(spawn.size() > 0)
		{
			return spawn.get((int) (Math.random() * spawn.size()));
		}
		return null;
	}
	
	public String getDeathDialog()
	{
		if(death.size() > 0)
		{
			return death.get((int) (Math.random() * death.size()));
		}
		return null;
	}
	
	public String getKillDialog()
	{
		if(kill.size() > 0)
		{
			return kill.get((int) (Math.random() * kill.size()));
		}
		return null;
	}
	
}
