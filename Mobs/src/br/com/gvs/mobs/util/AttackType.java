package br.com.gvs.mobs.util;

public enum AttackType {

	PASSIVE(0, "§f"), NEUTRAL(1, "§e"), HOSTILE(2, "§c");

	private int attackID;
	private String prefix;

	private AttackType(int attackID, String prefix) {
		this.attackID = attackID;
		this.prefix = prefix;
	}

	public int getAttackID() {
		return this.attackID;
	}

	public String getPrefix() {
		return prefix;
	}

	public static AttackType getAttackTypeByID(int id) {
		for (AttackType at : values()) {
			if (at.getAttackID() == id) {
				return at;
			}
		}
		return null;
	}

}
