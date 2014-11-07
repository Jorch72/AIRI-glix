package com.arisux.airi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.biome.BiomeGenEnd;
import net.minecraft.world.biome.BiomeGenHell;
import net.minecraft.world.chunk.Chunk;

public final class SpawnSystem
{
	private int maxAnimals = 40;
	private int maxMobs = 60;
	private int maxAquatic = 10;
	public BiomeGenBase standardBiomes[];
	public List<String> biomeList;
	public List<Class<? extends Entity>>[] entityClasses;
	protected List<SpawnListEntry>[] customMobSpawnList;
	protected List<SpawnListEntry>[] customCreatureSpawnList;
	protected List<SpawnListEntry>[] customAquaticSpawnList;
	private static HashMap<ChunkCoordIntPair, Boolean> eligibleChunksForSpawning = new HashMap<ChunkCoordIntPair, Boolean>();
	private List<Class<? extends Entity>> vanillaClassList;

	@SuppressWarnings("unchecked")
	public SpawnSystem()
	{
		biomeList = new ArrayList<String>();
		try
		{
			LinkedList<BiomeGenBase> linkedlist = new LinkedList<BiomeGenBase>();
			for (BiomeGenBase biomegenbase : BiomeGenBase.getBiomeGenArray())
			{
				if (biomegenbase == null)
					continue;
				biomeList.add(biomegenbase.biomeName);

				if (!(biomegenbase instanceof BiomeGenHell) && !(biomegenbase instanceof BiomeGenEnd))
				{
					linkedlist.add(biomegenbase);
				}
			}

			standardBiomes = (BiomeGenBase[]) linkedlist.toArray(new BiomeGenBase[0]);

			customCreatureSpawnList = new List[biomeList.size()];
			customMobSpawnList = new List[biomeList.size()];
			customAquaticSpawnList = new List[biomeList.size()];
			entityClasses = new List[3];
			vanillaClassList = new ArrayList<Class<? extends Entity>>();
			vanillaClassList.add(EntityChicken.class);
			vanillaClassList.add(EntityCow.class);
			vanillaClassList.add(EntityPig.class);
			vanillaClassList.add(EntitySheep.class);
			vanillaClassList.add(EntityWolf.class);
			vanillaClassList.add(EntitySquid.class);
			vanillaClassList.add(EntityOcelot.class);
			clearLists();
		} catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public void initialize()
	{
		this.setMaxMobs(AIRI.instance().settings.propertyList.get(Settings.Setting.MAX_MOBS).getInt());
		this.setMaxAnimals(AIRI.instance().settings.propertyList.get(Settings.Setting.MAX_ANIMALS).getInt());
		this.setMaxAquatic(AIRI.instance().settings.propertyList.get(Settings.Setting.MAX_AQUATIC).getInt());
	}

	protected static ChunkPosition getRandomSpawningPointInChunk(World worldObj, int par1, int par2)
	{
		Chunk chunk = worldObj.getChunkFromChunkCoords(par1, par2);
		int i = par1 * 16 + worldObj.rand.nextInt(16);
		int j = worldObj.rand.nextInt(chunk == null ? worldObj.getActualHeight() : chunk.getTopFilledSegment() + 16 - 1);
		int k = par2 * 16 + worldObj.rand.nextInt(16);
		return new ChunkPosition(i, j, k);
	}

	public void clearLists()
	{
		for (int x = 0; x < biomeList.size(); x++)
		{
			customCreatureSpawnList[x] = new ArrayList<SpawnListEntry>();
			customMobSpawnList[x] = new ArrayList<SpawnListEntry>();
			customAquaticSpawnList[x] = new ArrayList<SpawnListEntry>();
		}
		for (int x = 0; x < 3; x++)
		{
			entityClasses[x] = new ArrayList<Class<? extends Entity>>();
		}
	}

	public final int doSpecificSpawning(WorldServer worldObj, Class<?> entityClass, int max, EnumCreatureType enumcreaturetype)
	{
		eligibleChunksForSpawning.clear();
		int countTotal;
		int var6;

		for (countTotal = 0; countTotal < worldObj.playerEntities.size(); ++countTotal)
		{
			EntityPlayer entityplayer = (EntityPlayer) worldObj.playerEntities.get(countTotal);
			int var5 = MathHelper.floor_double(entityplayer.posX / 16.0D);
			var6 = MathHelper.floor_double(entityplayer.posZ / 16.0D);
			byte var7 = 8;

			for (int var8 = -var7; var8 <= var7; ++var8)
			{
				for (int var9 = -var7; var9 <= var7; ++var9)
				{
					boolean var10 = var8 == -var7 || var8 == var7 || var9 == -var7 || var9 == var7;
					ChunkCoordIntPair var11 = new ChunkCoordIntPair(var8 + var5, var9 + var6);
					if (!var10)
					{
						eligibleChunksForSpawning.put(var11, Boolean.valueOf(false));
					} else if (!eligibleChunksForSpawning.containsKey(var11))
					{
						eligibleChunksForSpawning.put(var11, Boolean.valueOf(true));
					}
				}
			}

		}
		countTotal = 0;
		ChunkCoordinates chunkcoordspawn = worldObj.getSpawnPoint();

		Iterator<ChunkCoordIntPair> iterator = eligibleChunksForSpawning.keySet().iterator();
		label113:
		while (iterator.hasNext())
		{
			ChunkCoordIntPair var10 = (ChunkCoordIntPair) iterator.next();

			ChunkPosition chunkpos = getRandomSpawningPointInChunk(worldObj, var10.chunkXPos * 16, var10.chunkZPos * 16);
			int chunkX = chunkpos.chunkPosX;
			int chunkY = chunkpos.chunkPosY;
			int chunkZ = chunkpos.chunkPosZ;

			if (!worldObj.isBlockNormalCubeDefault(chunkX, chunkY, chunkZ, true) && worldObj.getBlock(chunkX, chunkY, chunkZ).getMaterial() == enumcreaturetype.getCreatureMaterial())
			{
				int countSpawn = 0;

				for (int var21 = 0; var21 < 3; ++var21)
				{
					int tempPosX = chunkX;
					int tempPosY = chunkY;
					int tempPosZ = chunkZ;
					byte var25 = 6;

					for (int var26 = 0; var26 < 4; ++var26)
					{
						tempPosX += worldObj.rand.nextInt(var25) - worldObj.rand.nextInt(var25);
						tempPosY += worldObj.rand.nextInt(1) - worldObj.rand.nextInt(1);
						tempPosZ += worldObj.rand.nextInt(var25) - worldObj.rand.nextInt(var25);
						if (canCreatureTypeSpawnAtLocation(enumcreaturetype, worldObj, tempPosX, tempPosY, tempPosZ))
						{
							float finalPosX = (float) tempPosX + 0.5F;
							float finalPosY = (float) tempPosY;
							float finalPosZ = (float) tempPosZ + 0.5F;
							if (worldObj.getClosestPlayer((double) finalPosX, (double) finalPosY, (double) finalPosZ, 24.0D) == null)
							{
								float distSpawnX = finalPosX - (float) chunkcoordspawn.posX;
								float distSpawnY = finalPosY - (float) chunkcoordspawn.posY;
								float distSpawnZ = finalPosZ - (float) chunkcoordspawn.posZ;
								float sqDist = distSpawnX * distSpawnX + distSpawnY * distSpawnY + distSpawnZ * distSpawnZ;
								if (sqDist >= 576.0F)
								{
									EntityLiving entityliving;
									try
									{
										entityliving = (EntityLiving) entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{worldObj});
									} catch (Exception exception)
									{
										exception.printStackTrace();
										return countTotal;
									}

									entityliving.setLocationAndAngles((double) finalPosX, (double) finalPosY, (double) finalPosZ, worldObj.rand.nextFloat() * 360.0F, 0.0F);
									if (entityliving.getCanSpawnHere())
									{
										++countSpawn;
										countTotal += countSpawn;
										if (countTotal > max)
										{
											return countTotal;
										}

										worldObj.spawnEntityInWorld(entityliving);
										if (countSpawn >= entityliving.getMaxSpawnedInChunk())
										{
											continue label113;
										}
									}

								}
							}
						}
					}
				}
			}
		}
		return countTotal;
	}

	@SuppressWarnings("unchecked")
	public final int doCustomSpawning(WorldServer worldObj, boolean spawnMobs, boolean spawnAnmls)
	{
		if (!spawnMobs && !spawnAnmls)
		{
			return 0;
		} else
		{
			eligibleChunksForSpawning.clear();
			int countTotal;
			int var6;

			for (countTotal = 0; countTotal < worldObj.playerEntities.size(); ++countTotal)
			{
				EntityPlayer entityplayer = (EntityPlayer) worldObj.playerEntities.get(countTotal);
				int var5 = MathHelper.floor_double(entityplayer.posX / 16.0D);
				var6 = MathHelper.floor_double(entityplayer.posZ / 16.0D);
				byte var7 = 8;

				for (int var8 = -var7; var8 <= var7; ++var8)
				{
					for (int var9 = -var7; var9 <= var7; ++var9)
					{
						boolean var10 = var8 == -var7 || var8 == var7 || var9 == -var7 || var9 == var7;
						ChunkCoordIntPair var11 = new ChunkCoordIntPair(var8 + var5, var9 + var6);

						if (!var10)
						{
							eligibleChunksForSpawning.put(var11, Boolean.valueOf(false));
						} else if (!eligibleChunksForSpawning.containsKey(var11))
						{
							eligibleChunksForSpawning.put(var11, Boolean.valueOf(true));
						}
					}
				}
			}

			countTotal = 0;
			ChunkCoordinates chunkcoordspawn = worldObj.getSpawnPoint();
			EnumCreatureType[] enumcreaturevalues = EnumCreatureType.values();
			var6 = enumcreaturevalues.length;

			for (int enumType = 0; enumType < var6; ++enumType)
			{
				EnumCreatureType enumcreaturetype = enumcreaturevalues[enumType];

				int enumC = countSpawnedEntities(worldObj, enumcreaturetype);

				if ((!enumcreaturetype.getPeacefulCreature() || spawnAnmls) && (enumcreaturetype.getPeacefulCreature() || spawnMobs) && (enumC < getMax(enumcreaturetype)))//* eligibleChunksForSpawning.size() / 256))
				{
					Iterator<ChunkCoordIntPair> iterator = eligibleChunksForSpawning.keySet().iterator();
					ArrayList<ChunkCoordIntPair> tmp = new ArrayList<ChunkCoordIntPair>(eligibleChunksForSpawning.keySet());
					Collections.shuffle(tmp);
					iterator = tmp.iterator();

					label108:
					while (iterator.hasNext())
					{
						ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator.next();

						if (!((Boolean) eligibleChunksForSpawning.get(chunkcoordintpair)).booleanValue())
						{
							ChunkPosition chunkpos = getRandomSpawningPointInChunk(worldObj, chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
							int posX = chunkpos.chunkPosX;
							int posY = chunkpos.chunkPosY;
							int posZ = chunkpos.chunkPosZ;

							if (!worldObj.isBlockNormalCubeDefault(posX, posY, posZ, true) && worldObj.getBlock(posX, posY, posZ).getMaterial() == enumcreaturetype.getCreatureMaterial())
							{
								int spawnedMob = 0;
								int spawnCount = 0;

								while (spawnCount < 3)
								{
									int temposX = posX;
									int temposY = posY;
									int temposZ = posZ;
									byte var20 = 6;
									SpawnListEntry spawnlistentry = null;
									int spawnAttempt = 0;

									while (true)
									{
										if (spawnAttempt < 4)

										{
											label101:
											{
												temposX += worldObj.rand.nextInt(var20) - worldObj.rand.nextInt(var20);
												temposY += worldObj.rand.nextInt(1) - worldObj.rand.nextInt(1);
												temposZ += worldObj.rand.nextInt(var20) - worldObj.rand.nextInt(var20);

												//if(canCreatureTypeSpawnAtLocation(enumcreaturetype, worldObj, tempPosX, tempPosY, tempPosZ))
												if (canCreatureTypeSpawnAtLocation(enumcreaturetype, worldObj, temposX, temposY, temposZ))
												{
													float spawnX = (float) temposX + 0.5F;
													float spawnY = (float) temposY;
													float spawnZ = (float) temposZ + 0.5F;
													//changed so creatures spawn closer
													if (worldObj.getClosestPlayer((double) spawnX, (double) spawnY, (double) spawnZ, 12.0D) == null)
													{
														float var26 = spawnX - (float) chunkcoordspawn.posX;
														float var27 = spawnY - (float) chunkcoordspawn.posY;
														float var28 = spawnZ - (float) chunkcoordspawn.posZ;
														float spawnDist = var26 * var26 + var27 * var27 + var28 * var28;

														if (spawnDist >= 576.0F)
														{
															if (spawnlistentry == null)
															{
																spawnlistentry = getRandomCustomMob(worldObj, enumcreaturetype, temposX, temposY, temposZ);

																if (spawnlistentry == null)
																{
																	break label101;
																}
															}

															EntityLiving entityliving;

															try
															{
																entityliving = (EntityLiving) spawnlistentry.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{worldObj});
															} catch (Exception exception)
															{
																exception.printStackTrace();
																return countTotal;
															}

															entityliving.setLocationAndAngles((double) spawnX, (double) spawnY, (double) spawnZ, worldObj.rand.nextFloat() * 360.0F, 0.0F);

															try
															{
																if (entityliving.getCanSpawnHere())
																{
																	++spawnedMob;
																	worldObj.spawnEntityInWorld(entityliving);
																	//creatureSpecificInit(entityliving, worldObj, spawnX, spawnY, spawnZ);

																	if (spawnedMob >= entityliving.getMaxSpawnedInChunk())
																	{
																		continue label108;
																	}
																}

																countTotal += spawnedMob;
															} catch (Exception exception)
															{
																//System.out.println("spawn crash: " + exception);
															}
														}
													}
												}

												++spawnAttempt;
												continue;
											}
										}

										++spawnCount;
										break;
									}
								}
							}
						}
					}
				}
			}

			return countTotal;
		}
	}

	public void registerSpawnsForEntity(Class<? extends Entity> class1, int i, int max, EnumCreatureType enumcreaturetype)
	{
		registerSpawnsForEntity(class1, i, 4, max, enumcreaturetype, null);
	}

	public void registerSpawnsForEntity(Class<? extends Entity> class1, int i, EnumCreatureType enumcreaturetype)
	{
		registerSpawnsForEntity(class1, i, 4, 5, enumcreaturetype, null);
	}

	public void registerSpawnsForEntity(Class<? extends Entity> class1, int i, int max, EnumCreatureType enumcreaturetype, BiomeGenBase abiomegenbase[])
	{
		registerSpawnsForEntity(class1, i, 4, max, enumcreaturetype, abiomegenbase);
	}

	public void registerSpawnsForEntity(Class<? extends Entity> class1, int i, EnumCreatureType enumcreaturetype, BiomeGenBase abiomegenbase[])
	{
		registerSpawnsForEntity(class1, i, 4, 5, enumcreaturetype, abiomegenbase);
	}

	public void registerSpawnsForEntity(Class<? extends Entity> class1, int i, int j, int k, EnumCreatureType enumcreaturetype)
	{
		registerSpawnsForEntity(class1, i, j, k, enumcreaturetype, null);
	}

	@SuppressWarnings("unchecked")
	public void registerSpawnsForEntity(Class<? extends Entity> class1, int i, int j, int k, EnumCreatureType enumcreaturetype, BiomeGenBase abiomegenbase[])
	{
		if (class1 == null)
		{
			throw new IllegalArgumentException("spawns.entity.null");
		}
		if (enumcreaturetype == null)
		{
			throw new IllegalArgumentException("spawns.list.null");
		}
		if (abiomegenbase == null)
		{
			abiomegenbase = standardBiomes;
		}

		int x1 = getEnumIndex(enumcreaturetype);

		{
			boolean flag = false;
			for (Iterator<?> iterator = entityClasses[x1].iterator(); iterator.hasNext(); )
			{
				if (iterator != null)
				{
					Class<? extends Entity> class2 = (Class<? extends Entity>) iterator.next();
					if (class2 == class1)
					{
						flag = true;
						break;
					}
				}
			}

			if (!flag)
			{
				entityClasses[x1].add(class1);
			}
		}

		for (int l = 0; l < abiomegenbase.length; l++)
		{
			List<SpawnListEntry>[] fulllist = getCustomSpawnableList(enumcreaturetype);

			if (fulllist != null)
			{
				int x = biomeList.indexOf(abiomegenbase[l].biomeName);

				boolean flag = false;
				for (Iterator<?> iterator = fulllist[x].iterator(); iterator.hasNext(); )
				{
					if (iterator != null)
					{
						SpawnListEntry spawnlistentry = (SpawnListEntry) iterator.next();
						if (spawnlistentry.entityClass == class1)
						{
							spawnlistentry.itemWeight = i;
							spawnlistentry.minGroupCount = j;
							spawnlistentry.maxGroupCount = k;
							flag = true;
							break;
						}
					}
				}

				if (!flag)
				{
					fulllist[x].add(new SpawnListEntry(class1, i, j, k));
				}
			}
		}

	}

	public void removeSpawnsForEntity(Class<? extends Entity> class1, EnumCreatureType enumcreaturetype)
	{
		removeSpawnsForEntity(class1, enumcreaturetype, null);
	}

	public void removeSpawnsForEntity(Class<? extends Entity> class1, EnumCreatureType enumcreaturetype, BiomeGenBase abiomegenbase[])
	{
		if (class1 == null)
		{
			throw new IllegalArgumentException("spawns.entity.null");
		}
		if (enumcreaturetype == null)
		{
			throw new IllegalArgumentException("spawns.list.null");
		}
		if (abiomegenbase == null)
		{
			abiomegenbase = standardBiomes;
		}

		for (int l = 0; l < abiomegenbase.length; l++)
		{
			List<SpawnListEntry>[] fulllist = getCustomSpawnableList(enumcreaturetype);

			if (fulllist != null)
			{
				int x = biomeList.indexOf(abiomegenbase[l].biomeName);
				for (Iterator<SpawnListEntry> iterator = fulllist[x].iterator(); iterator.hasNext(); )
				{
					if (iterator != null)
					{
						SpawnListEntry spawnlistentry = (SpawnListEntry) iterator.next();
						if (spawnlistentry.entityClass == class1)
						{
							iterator.remove();
						}
					}
				}

			}

		}

	}

	private int getEnumIndex(EnumCreatureType enumcreaturetype)
	{
		if (enumcreaturetype == EnumCreatureType.monster)
		{
			return 0;
		}
		if (enumcreaturetype == EnumCreatureType.creature)
		{
			return 1;
		}
		if (enumcreaturetype == EnumCreatureType.waterCreature)
		{
			return 2;
		} else
		{
			return 0;
		}
	}

	public int countSpawnedEntities(World world, EnumCreatureType enumcreaturetype)
	{
		int i = getEnumIndex(enumcreaturetype);
		int finalcount = 0;

		{
			for (Iterator<Class<? extends Entity>> iterator = entityClasses[i].iterator(); iterator.hasNext(); )
			{
				if (iterator != null)
				{
					Class<? extends Entity> class1 = (Class<? extends Entity>) iterator.next();
					if (class1 != null)
					{
						finalcount += world.countEntities(class1);
					}
				}
			}
		}
		return finalcount;
	}

	private List<SpawnListEntry>[] getCustomSpawnableList(EnumCreatureType enumcreaturetype)
	{
		if (enumcreaturetype == EnumCreatureType.monster)
		{
			return customMobSpawnList;
		}
		if (enumcreaturetype == EnumCreatureType.creature)
		{
			return customCreatureSpawnList;
		}
		if (enumcreaturetype == EnumCreatureType.waterCreature)
		{
			return customAquaticSpawnList;
		} else
		{
			return null;
		}
	}

	private List<SpawnListEntry> getCustomBiomeSpawnList(List<SpawnListEntry>[] lists, BiomeGenBase biomegenbase)
	{
		int x = biomeList.indexOf(biomegenbase.biomeName);
		if (x >= 0)
		{
			return lists[x];
		}
		return null;
	}

	private int getMax(EnumCreatureType enumcreaturetype)
	{
		if (enumcreaturetype == EnumCreatureType.monster)
		{
			return getMaxMobs();
		}
		if (enumcreaturetype == EnumCreatureType.creature)
		{
			return getMaxAnimals();
		}
		if (enumcreaturetype == EnumCreatureType.waterCreature)
		{
			return getMaxAquatic();
		} else
		{
			return -1;
		}
	}

	public int getMaxAnimals()
	{
		return maxAnimals;
	}

	public void setMaxAnimals(int max)
	{
		maxAnimals = max;
	}

	public int getMaxMobs()
	{
		return maxMobs;
	}

	public void setMaxMobs(int max)
	{
		maxMobs = max;
	}

	public int getMaxAquatic()
	{
		return maxAquatic;
	}

	public void setMaxAquatic(int max)
	{
		maxAquatic = max;
	}

	private boolean canCreatureTypeSpawnAtLocation(EnumCreatureType enumcreaturetype, World world, int i, int j, int k)
	{
		if (enumcreaturetype.getCreatureMaterial() == Material.water)
		{
			return world.getBlock(i, j, k).getMaterial().isLiquid() && !world.isBlockNormalCubeDefault(i, j + 1, k, true);
		} else
		{
			return world.isBlockNormalCubeDefault(i, j - 1, k, true) && !world.isBlockNormalCubeDefault(i, j, k, true) && !world.getBlock(i, j, k).getMaterial().isLiquid() && !world.isBlockNormalCubeDefault(i, j + 1, k, true);
		}
	}

	protected final int entityDespawnCheck(WorldServer worldObj, EntityLiving entityliving)
	{
		if (isNearTorch(entityliving, 12D, worldObj))
		{
			return 0;
		}

		if (entityliving instanceof EntityWolf && ((EntityWolf) entityliving).isTamed())
		{
			return 0;
		}

		EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(entityliving, -1D);
		if (entityplayer != null) //entityliving.canDespawn() &&
		{
			double d = ((Entity) (entityplayer)).posX - entityliving.posX;
			double d1 = ((Entity) (entityplayer)).posY - entityliving.posY;
			double d2 = ((Entity) (entityplayer)).posZ - entityliving.posZ;
			double d3 = d * d + d1 * d1 + d2 * d2;
			if (d3 > 16384D)
			{
				entityliving.setDead();
				return 1;
			}
			if (entityliving.getAge() > 600 && worldObj.rand.nextInt(800) == 0)
			{
				if (d3 < 1024D)
				{
					//TODO Fix
					//entityliving.entityAge = 0;
				} else
				{
					entityliving.setDead();
					return 1;
				}
			}
		}
		return 0;
	}

	public final int countEntities(Class<? extends Entity> class1, World worldObj)
	{
		int i = 0;
		for (int j = 0; j < worldObj.loadedEntityList.size(); j++)
		{
			Entity entity = (Entity) worldObj.loadedEntityList.get(j);
			if (class1.isAssignableFrom(entity.getClass()))
			{
				i++;
			}
		}

		return i;
	}

	public final int despawnVanillaAnimals(WorldServer worldObj)
	{
		int count = 0;
		for (int j = 0; j < worldObj.loadedEntityList.size(); j++)
		{
			Entity entity = (Entity) worldObj.loadedEntityList.get(j);
			if ((entity instanceof EntityLiving) && (entity instanceof EntityCow || entity instanceof EntitySheep || entity instanceof EntityPig || entity instanceof EntityChicken || entity instanceof EntitySquid || entity instanceof EntityWolf))
			{
				count += entityDespawnCheck(worldObj, (EntityLiving) entity);

			}
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public final int despawnMob(WorldServer worldObj, Class<? extends Entity>... classList)
	{
		List<Class<? extends Entity>> list = new ArrayList<Class<? extends Entity>>();
		for (int i = 0; i < classList.length; i++)
		{
			list.add(classList[i]);
		}
		return despawnMob(worldObj, list);
	}

	public final int despawnMob(WorldServer worldObj, List<Class<? extends Entity>> classList)
	{
		int count = 0;
		if (classList == null)
		{
			classList = vanillaClassList;
		}

		for (int j = 0; j < worldObj.loadedEntityList.size(); j++)
		{
			Entity entity = (Entity) worldObj.loadedEntityList.get(j);
			if (!(entity instanceof EntityLiving))
			{
				continue;
			}
			for (Iterator<Class<? extends Entity>> iterator = classList.iterator(); iterator.hasNext(); )
			{
				if (iterator != null)
				{
					Class<? extends Entity> class2 = (Class<? extends Entity>) iterator.next();
					if (class2 == entity.getClass())
					{
						count += entityDespawnCheck(worldObj, (EntityLiving) entity);
					}
				}
			}
		}
		
		return count;
	}

	public final int despawnMobWithMinimum(WorldServer worldObj, Class<? extends Entity> class1, int minimum)
	{
		int killedcount = 0;
		int mobcount = countEntities(class1, worldObj);
		for (int j = 0; j < worldObj.loadedEntityList.size(); j++)
		{
			if ((mobcount - killedcount) <= minimum)
			{
				worldObj.updateEntities();
				return killedcount;
			}
			Entity entity = (Entity) worldObj.loadedEntityList.get(j);
			if (!(entity instanceof EntityLiving))
			{
				continue;
			}
			if (class1 == entity.getClass())
			{
				killedcount += entityDespawnCheck(worldObj, (EntityLiving) entity);
			}
		}
		worldObj.updateEntities();
		return killedcount;

	}

	public SpawnListEntry getRandomCustomMob(World worldObj, EnumCreatureType enumCreatureType, int posX, int posY, int posZ)
	{
		List<SpawnListEntry> list = getPossibleCustomCreatures(worldObj, enumCreatureType, posX, posY, posZ);

		if (list == null || list.isEmpty())
		{
			return null;
		} else
		{
			return (SpawnListEntry) WeightedRandom.getRandomItem(worldObj.rand, list);
		}
	}

	public List<SpawnListEntry> getPossibleCustomCreatures(World worldObj, EnumCreatureType enumcreaturetype, int posX, int posY, int posZ)
	{
		BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(posX, posZ);
		if (biomegenbase == null)
		{
			return null;
		} else
		{
			return getCustomBiomeSpawnList(getCustomSpawnableList(enumcreaturetype), biomegenbase);
		}
	}

	public static boolean isNearTorch(Entity entity, Double dist, World worldObj)
	{
		AxisAlignedBB axisalignedbb = entity.boundingBox.expand(dist, dist / 2D, dist);
		int i = MathHelper.floor_double(axisalignedbb.minX);
		int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
		int k = MathHelper.floor_double(axisalignedbb.minY);
		int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
		int i1 = MathHelper.floor_double(axisalignedbb.minZ);
		int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);

		for (int k1 = i; k1 < j; k1++)
		{
			for (int l1 = k; l1 < l; l1++)
			{
				for (int i2 = i1; i2 < j1; i2++)
				{
					Block j2 = worldObj.getBlock(k1, l1, i2);

					if (j2 != Blocks.air)
					{
						String nameToCheck = j2.getUnlocalizedName();
						if (nameToCheck != null && nameToCheck != "")
						{
							if (nameToCheck.equals("tile.torch"))
								return true;
						}
					}
				}
			}
		}

		return false;
	}
}
