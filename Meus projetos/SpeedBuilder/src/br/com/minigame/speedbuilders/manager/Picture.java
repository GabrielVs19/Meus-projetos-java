package br.com.minigame.speedbuilders.manager;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import br.com.minigame.speedbuilders.SpeedBuilders;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;


public class Picture
{
	
	
	private static CuboidClipboard cuboid;
	private static Map<Vector, BaseBlock> blocks = new HashMap<Vector, BaseBlock>();
	
	public static void set(String picname)
	{
		blocks.clear();
		try
		{
			File file = new File(SpeedBuilders.getInstance().getDataFolder() + "/Pictures/", picname);
			
			SchematicFormat schematic = SchematicFormat.getFormat(file);
			cuboid = schematic.load(file);
			
			for(int x = 0; x < cuboid.getSize().getBlockX(); x++)
			{
				for(int y = 1; y < cuboid.getSize().getBlockY(); y++)
				{
					for(int z = 0; z < cuboid.getSize().getBlockZ(); z++)
					{
						Vector vector = new Vector(x, y, z);
						BaseBlock baseBlock = cuboid.getBlock(vector);
						
						if(baseBlock == null || baseBlock.isAir())
						{
							continue;
						}
						
						blocks.put(vector, baseBlock);
					}
				}
			}
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(DataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Map<Vector, BaseBlock> getBlocks()
	{
		return blocks;
	}
	
	public static void paste(Location location)
	{
		if(cuboid == null)
		{
			return;
		}
		
		EditSession editSession = new EditSession(BukkitUtil.getLocalWorld(Bukkit.getWorlds().get(0)), 99999999);
		
		try
		{
			cuboid.paste(editSession, BukkitUtil.toVector(location), true);
		}
		catch(MaxChangedBlocksException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void clear(Location location)
	{
		if(cuboid == null)
		{
			return;
		}
		
		for(Entry<Vector, BaseBlock> b : blocks.entrySet())
		{
			Vector vector = b.getKey();
			Vector offset = cuboid.getOffset();
			
			int x = location.getBlockX() + vector.getBlockX() + offset.getBlockX();
			int y = location.getBlockY() + vector.getBlockY() + offset.getBlockY();
			int z = location.getBlockZ() + vector.getBlockZ() + offset.getBlockZ();
			Block block = new Location(location.getWorld(), x, y, z).getBlock();
			
			if(block == null)
			{
				continue;
			}
			block.setType(Material.AIR);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static int verify(Location location)
	{
		if(cuboid == null)
		{
			return 100;
		}
		
		int score = 0;
		
		for(Entry<Vector, BaseBlock> b : blocks.entrySet())
		{
			BaseBlock baseBlock = b.getValue();
			Vector vector = b.getKey();
			Vector offset = cuboid.getOffset();
			
			int x = location.getBlockX() + vector.getBlockX() + offset.getBlockX();
			int y = location.getBlockY() + vector.getBlockY() + offset.getBlockY();
			int z = location.getBlockZ() + vector.getBlockZ() + offset.getBlockZ();
			Block block = new Location(location.getWorld(), x, y, z).getBlock();
			
			if(block.getTypeId() != baseBlock.getId() || block.getData() != baseBlock.getData())
			{
				continue;
			}
			
			score++;
		}
		
		score = (score * 100) / getBlocks().size();
		return score == 0 ? 1 : score;
	}
}
