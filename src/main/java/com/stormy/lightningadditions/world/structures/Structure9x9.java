package com.stormy.lightningadditions.world.structures;

import com.stormy.lightningadditions.reference.ModInformation;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.util.Map;
import java.util.Random;

import static net.minecraft.util.Rotation.*;

public class Structure9x9 /*implements IWorldGenerator*/{

    public static final ResourceLocation STRUCTURE = new ResourceLocation(ModInformation.MODID, "9x9");
    public static final ResourceLocation LOOT = new ResourceLocation(ModInformation.MODID, "9x9_loot");

    //Code for world gen
//    @Override
//    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
//        if (!(world instanceof WorldServer))
//            return;
//
//        if (!ConfigurationHandler.enableStructure1){
//            return;
//        }
//
//        WorldServer serverworld = (WorldServer) world;
//
//        int x = chunkX * 16 + random.nextInt(16);
//        int z = chunkZ * 16 + random.nextInt(16);
//
//        BlockPos xzPos = new BlockPos(x, 1, z);
//        Biome biome = world.getBiomeForCoordsBody(xzPos);
//
//        if (biome == Biomes.PLAINS || biome == Biomes.DESERT || biome == Biomes.EXTREME_HILLS || biome == Biomes.BEACH || biome == Biomes.FOREST || biome == Biomes.FOREST_HILLS || biome == Biomes.ICE_PLAINS) {
//            if (random.nextInt(ConfigurationHandler.structureRarity2) == 0) { //Rarity
//                BlockPos pos = new BlockPos(x, WorldUtil.findEmptySpot(world, x, z), z);
//                generateStructure(serverworld, pos, random);
//            }
//        }
//
//    }

    public static void generateStructure(WorldServer world, BlockPos pos, Random random, Rotation rotation, EnumFacing chestOrientation) {

        MinecraftServer server = world.getMinecraftServer();
        Template template = world.getStructureTemplateManager().getTemplate(server, STRUCTURE);
        PlacementSettings settings = new PlacementSettings();
        settings.setRotation(rotation);

        Vec3i vecOffset = new Vec3i(0, 0, 0);

        switch (rotation) {
            default:
            case NONE:
                vecOffset = new Vec3i(-4, 0, 0);
                break;
            case CLOCKWISE_90:
                template.addBlocksToWorld(world, pos.add(0, 0, -4), settings);
                vecOffset = new Vec3i(0, 0, -4);
                break;
            case CLOCKWISE_180:
                vecOffset = new Vec3i(4, 0, 0);
                break;
            case COUNTERCLOCKWISE_90:
                vecOffset = new Vec3i(0, 0, 4);
                break;
        }

        template.addBlocksToWorld(world, pos.add(vecOffset), settings);

        if (!world.isAirBlock(pos.offset(chestOrientation).down())) world.setBlockState(pos.offset(chestOrientation), Blocks.WOODEN_PRESSURE_PLATE.getDefaultState());

        Map<BlockPos, String> dataBlocks = template.getDataBlocks(pos, settings);
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            String[] tokens = entry.getValue().split(" ");
            if (tokens.length == 0)
                return;

            BlockPos dataPos = entry.getKey().add(vecOffset);

            String s = tokens[0].toLowerCase();
            if (s.equals("lootchest")) {

                EnumFacing chestFacing = settings.getRotation().rotate(chestOrientation);
                IBlockState chestState = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, chestFacing);
                world.setBlockState(dataPos, chestState);

                TileEntity tile = world.getTileEntity(dataPos);
                if (tile != null && tile instanceof TileEntityLockableLoot)
                    ((TileEntityLockableLoot) tile).setLootTable(LOOT, random.nextLong());

            }

        }

    }

}
