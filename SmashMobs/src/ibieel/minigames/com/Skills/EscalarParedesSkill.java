package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EscalarParedesSkill implements Listener {

	private Skills skill = Skills.ESCALAR_PAREDES;
	private static Map<String, ArrayList<Block>> vineMap = new HashMap<String, ArrayList<Block>>();
	private static ArrayList<Integer> noVineBlocks = new ArrayList<Integer>();
	public SmashMobs plugin;
	public EscalarParedesSkill(SmashMobs plugin){
		this.plugin = plugin;
	}
	
	 @SuppressWarnings("deprecation")
		@EventHandler
		  public void onPlayerMove(PlayerMoveEvent event)
		  {
		    Player player = event.getPlayer();
		    if(SkillEventUtil.playerHasSkill(player, skill)){
		      BlockFace bf = yawToFace(player.getLocation().getYaw());
		      Block block = player.getLocation().getBlock().getRelative(bf);
		      if (block.getType() != Material.AIR)
		      {
		        for (int i = 0; i < 300; i++)
		        {
		          Block temp = block.getLocation().add(0.0D, i, 0.0D).getBlock();
		          Block opp = player.getLocation().add(0.0D, i, 0.0D).getBlock();
		          Block aboveOpp = opp.getLocation().add(0.0D, 1.0D, 0.0D).getBlock();
		          int counter = 0;
		          for (int k = 0; k < noVineBlocks.size(); k++) {
		            if ((temp.getType() != Material.AIR) && (temp.getTypeId() != noVineBlocks.get(k).intValue())) {
		              counter++;
		            }
		          }
		          if ((counter != noVineBlocks.size()) || ((opp.getType() != Material.AIR) && (opp.getType() != Material.LONG_GRASS) && (opp.getType() != Material.YELLOW_FLOWER) && (opp.getType() != Material.RED_ROSE))) {
		            break;
		          }
		          if (aboveOpp.getType() == Material.AIR)
		          {
		            player.sendBlockChange(opp.getLocation(), Material.VINE, (byte)0);
		            addVines(player, opp);
		          }
		          player.setFallDistance(0.0F);
		        }
		      }
		      else
		      {
		        for (int i = 0; i < getVines(player).size(); i++) {
		          player.sendBlockChange(((Block)getVines(player).get(i)).getLocation(), Material.AIR, (byte)0);
		        }
		        getVines(player).clear();
		      }
		    }
		  }
		  
		  public static ArrayList<Block> getVines(Player player)
		  {
		    if (vineMap.containsKey(player.getName())) {
		      return vineMap.get(player.getName());
		    }
		    ArrayList<Block> temp = new ArrayList<Block>();
		    return temp;
		  }
		  
		  public void setVines(Player player, ArrayList<Block> vines)
		  {
		    vineMap.put(player.getName(), vines);
		  }
		  
		  public void addVines(Player player, Block vine)
		  {
		    ArrayList<Block> updated = new ArrayList<Block>();
		    updated = getVines(player);
		    updated.add(vine);
		    setVines(player, updated);
		  }
		  
		  public BlockFace yawToFace(float yaw)
		  {
		    BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
		    return axis[(java.lang.Math.round(yaw / 90.0F) & 0x3)];
		  }
		  
		  @SuppressWarnings("deprecation")
		public static void defineNoVineBlocks(){
		    noVineBlocks.add(Integer.valueOf(Material.THIN_GLASS.getId()));
		    noVineBlocks.add(Integer.valueOf(44));
		    noVineBlocks.add(Integer.valueOf(126));
		    noVineBlocks.add(Integer.valueOf(Material.WOOD_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.JUNGLE_WOOD_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.BIRCH_WOOD_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.SPRUCE_WOOD_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.COBBLESTONE_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.BRICK_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.WOOD_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.SMOOTH_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.NETHER_BRICK_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.SANDSTONE_STAIRS.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.FENCE.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.FENCE_GATE.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.NETHER_FENCE.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.LADDER.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.VINE.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.BED.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.BED_BLOCK.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.IRON_FENCE.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.SNOW.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.SIGN.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.LEVER.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.TRAP_DOOR.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.PISTON_EXTENSION.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.PISTON_MOVING_PIECE.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.TRIPWIRE_HOOK.getId()));
		    noVineBlocks.add(Integer.valueOf(93));
		    noVineBlocks.add(Integer.valueOf(94));
		    noVineBlocks.add(Integer.valueOf(Material.BOAT.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.MINECART.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.CAKE.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.CAKE_BLOCK.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.WATER.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.STATIONARY_WATER.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.LAVA.getId()));
		    noVineBlocks.add(Integer.valueOf(Material.STATIONARY_LAVA.getId()));
		  }
	
	

}
