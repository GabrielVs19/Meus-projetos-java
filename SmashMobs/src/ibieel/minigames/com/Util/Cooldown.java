package ibieel.minigames.com.Util;

import ibieel.minigames.com.Util.SkillsUtil.Skills;


public class Cooldown {

	private String playerName;
	private Skills skill;
	private double time = 0;
	public Cooldown(String player, Skills skill){
		this.playerName = player;
		this.skill = skill;
		this.time = 0;
	}

	public String getPlayer(){
		return this.playerName;
	}

	public Skills getSkill(){
		return this.skill;
	}

	public double getTime(){
		return this.time;
	}
	
	public void setTime(double time){
		this.time = time;
	}

}
