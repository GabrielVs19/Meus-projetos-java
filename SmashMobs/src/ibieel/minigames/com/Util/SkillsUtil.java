package ibieel.minigames.com.Util;

public class SkillsUtil {

	public static enum SkillType{
		SWORD, AXE, SHOVEL, DOUBLE_JUMP, PASSIVE_1, PASSIVE_2;
	}
	
	public static enum CastType{
		INSTANT("Instântanea"), CASTING("Carregável");
		
		private String name;
		private CastType(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
	}
	
	public static enum Skills{
		DOUBLE_JUMP("Double-Jump", CastType.INSTANT, SkillType.DOUBLE_JUMP, 1.60, "Dá um pulo duplo ao clicar duas vezes no espaço."),
		DOUBLE_JUMP_GALINHA("Double-Jump galinha", CastType.INSTANT, SkillType.DOUBLE_JUMP, (DOUBLE_JUMP.getCooldown() / 2), "Dá um pulo duplo com tempo de recarga diminuido pela metade."),
		INFERNO("Inferno", CastType.CASTING, SkillType.SWORD, 2.5, "Rajada de blaze powders que da dano e coloca fogo no alvo."),
		RAJADA_DE_FLECHAS("Rajada de flechas", CastType.CASTING, SkillType.SWORD, 2.0, "Rajada de flechas que causam dano no alvo."),
		RAJADA_DE_OVOS("Rajada de ovos", CastType.CASTING, SkillType.SWORD, 2.0, "Rajada de ovos que causam dano e knockback no alvo."),
		ABALO_SISMICO("Abalo sísmico", CastType.INSTANT, SkillType.PASSIVE_1, 0.0, "Ao cair de uma altura maior de 3 blocos da dano em todos em volta, o dano é relativo a altura."),
		SALTO_SISMICO("Salto sísmico", CastType.CASTING, SkillType.SWORD, 3.5, "Carrega um salto que te arremessa pelos ares, a altura é relativa ao tempo que você carregou a skill."),
		TEIA("Teia", CastType.CASTING, SkillType.SWORD, 2.0, "Atira várias teias que causam dano e lentidão nos alvos."),
		VENENO("Veneno", CastType.INSTANT, SkillType.PASSIVE_1, 0.0, "A cada ataque tem chance de deixar o alvo envenenado."),
		GRANADA("Granada", CastType.INSTANT, SkillType.SHOVEL, 4.0, "Atira uma granada que explode causando dano ao alvo ou todos que estão perto."),
		ESCUDO_MAGICO("Escudo mágico", CastType.INSTANT, SkillType.AXE, 9.0, "Cria um escudo que protege de 20% de todo dano tomado."),
		AUTO_EXPLOSAO("Auto-explosão", CastType.CASTING, SkillType.SWORD, 6.6, "Se auto-explode causando dano a todos em volta e te arremessa para longe."),
		GALINHA_EXPLOSIVA("Galinha explosiva", CastType.INSTANT, SkillType.SHOVEL, 3.8, "Atira uma galinha que explode ao chocar-se com algum alvo ou depois de alguns segundos."),
		EXPLOSAO_DE_OSSOS("Explosão de ossos", CastType.INSTANT, SkillType.AXE, 3.8, "Cria uma explosão de ossos que joga todos perto de você longe."),
		BOTE("Bote", CastType.INSTANT, SkillType.AXE, 2.0, "Avança na direção em que está olhando e causa dano caso colida com algum alvo."),
		ESCALAR_PAREDES("Escalar paredes", CastType.INSTANT, SkillType.PASSIVE_2, 0.0, "Permite que escale paredes."),
		TERREMOTO("Terremoto", CastType.INSTANT, SkillType.AXE, 8.5, "O golem bate com muita força no chão criando um terremoto e fazendo com que ocorram falhas no terreno."),
		TELEPORTE("Teleporte", CastType.INSTANT, SkillType.AXE, 5.3, "Se teleporta para onde está olhando."),
		LADRAO_DE_BLOCOS("Ladrão de blocos", CastType.CASTING, SkillType.SWORD, 2.0, "O enderman rouba um bloco do the end e arremessa contra seus alvos."),
		VELOCIDADE("Efeito de velocidade I", CastType.INSTANT, SkillType.PASSIVE_2, 0.0, "Efeito de velocidade I na partida toda."),
		CONGELAR("Congelar", CastType.INSTANT, SkillType.SHOVEL, 13.0, "Atira um gelo que se colidir com algum jogador faz ele congelar."),
		ATIRADOR_DE_CABECAS("Atirador de cabeças", CastType.INSTANT, SkillType.SHOVEL, 3.0, "Atira cabeça do wither."),
		BOLA_DE_NEVE("Bola de neve", CastType.CASTING, SkillType.SWORD, 2.5, "Atira bolas de neve que causam dano e knockback."),
		PASSOS_CONGELANTES("Passos congelantes", CastType.INSTANT, SkillType.AXE, 15.0, "Cria sob seus pés um caminho de gelo."),
		VENENO_DO_WITHER("Veneno do wither", CastType.INSTANT, SkillType.PASSIVE_1, 0.0, "A cada ataque tem chance de deixar o alvo envenenado com o efeito do wither."),
		AVANCO_MORTIFERO("Avanço mortifero", CastType.INSTANT, SkillType.AXE, 9.5, "Avança na direção que está olhando deixando todos os alvos que colidir em chamas e dando dano."),
		REGENERAR_VIDA("Regenerar vida", CastType.CASTING, SkillType.SWORD, 6.0, "Envoca os poderes do wither e regenera sua vida enquanto estiver segurando com o botão direito."),
		BACON("Bacon", CastType.INSTANT, SkillType.SWORD, 2.5, "Atira um bacon que causa uma pequena explosão e arremesa o alvo longe."),
		CLONAGEM("Clonagem", CastType.INSTANT, SkillType.AXE, 15.0, "Cria clones de si mesmo para confundir o inimigo e todo dano recebido é reduzido."),
		PORCO_EXPLOSIVO("Porco explosivo", CastType.INSTANT, SkillType.SHOVEL, 4.0, "Atira um porco que explode ao chocar-se com algum alvo ou depois de alguns segundos."),
		EXPLOSAO_DE_MASSA("Explosão de massa", CastType.INSTANT, SkillType.SWORD, 10.0, "O slime aumenta sua massa ao máximo fazendo com que ele exploda e cause dano a todos a sua volta."),
		GOSMA_VENENOSA("Gosma venenosa", CastType.INSTANT, SkillType.PASSIVE_2, 0.0, "A cada ataque tem chance de deixar o alvo com uma gosma venenosa que causa lentidão e dano."),
		ENCOLHER("Encolher", CastType.INSTANT, SkillType.AXE, 8.3, "Você encolhe ao máximo fazendo com que seus inimigos tenham dificuldade de atacar você."),
		ALCATEIA("Alcateia", CastType.INSTANT, SkillType.AXE, 15.0, "O lobo chama sua alcateia para ajudar ele, todos os inimigos que forem atacados pelo lobo também serão pela alcateia."),
		UIVO("Uivo", CastType.INSTANT, SkillType.SHOVEL, 10.0, "O lobo dá sua grande uivada e deixa seus inimigos a sua volta atordoados e mais vulneráveis (fracos)."),
		MORDIDA_MORTAL("Mordida fatal", CastType.INSTANT, SkillType.SWORD, 6.0, "O próximo ataque do lobo dará o dobro de dano."),
		PRISAO_DE_TEIA("Prisão de teia", CastType.INSTANT, SkillType.SHOVEL, 13.0, "Cria uma prisão de teia sob seu alvo."),
		ALIADOS("Aliados do nether", CastType.INSTANT, SkillType.AXE, 15.0, "O porco zumbi chama seus aliados do nether para atacar seus alvos."),
		LANCA_CHAMAS("Lança chamas", CastType.CASTING, SkillType.SWORD, 4.0, "Atira chamas em seus alvos."),
		FURIA("Fúria", CastType.INSTANT, SkillType.SHOVEL, 10.0, "O porco zumbi fica enfurecido, e enquanto ele está enfurecido o seu ataque causa mais dano e todo dano sofrido é reduzido.");
		private String name;
		private String descricao;
		private SkillType skillType;
		private CastType castType;
		private double cooldown;
		
		
		private Skills(String name, CastType castType, SkillType skillType, double cooldown, String descricao) {
			this.name = name;
			this.skillType = skillType;
			this.cooldown = cooldown;
			this.descricao = descricao;
			this.castType = castType;
		}
		
		public String getName(){
			return this.name;
		}
		
		public String getDesc(){
			return this.descricao;
		}
		
		public SkillType getSkillType(){
			return this.skillType;
		}
		
		public double getCooldown(){
			return this.cooldown;
		}
		
		public CastType getCastType(){
			return this.castType;
		}
	}

}
