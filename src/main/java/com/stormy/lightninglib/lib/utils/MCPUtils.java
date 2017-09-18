package com.stormy.lightninglib.lib.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.ListMultimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * This class is has methods which use ATs and/or Reflection<br>
 * to make dealing with private/private final mc methods<br>S
 * easier.
 *
 * @author p455w0rd
 *
 */
public class MCPUtils {

    public static void addResourcePack(IResourcePack pack) {
        List<Object> packList = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), ReflectionUtils.determineSRG("defaultResourcePacks"));
        packList.add(pack);
        ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), packList, ReflectionUtils.determineSRG("defaultResourcePacks"));
    }

    public static List<LayerRenderer<? extends EntityLivingBase>> getLayerRenderers(RenderLivingBase<?> renderPlayer) {
        return ReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, ReflectionUtils.determineSRG("layerRenderers"));
    }

    public static void setLayerRenderers(List<LayerRenderer<EntityPlayer>> layers, RenderPlayer renderPlayer) {
        ReflectionHelper.setPrivateValue(RenderLivingBase.class, renderPlayer, layers, ReflectionUtils.determineSRG("layerRenderers"));
    }

    public static int getTextureOffsetX(ModelRenderer modelRenderer) {
        return ReflectionHelper.getPrivateValue(ModelRenderer.class, modelRenderer, ReflectionUtils.determineSRG("textureOffsetX"));
    }

    public static int getTextureOffsetY(ModelRenderer modelRenderer) {
        return ReflectionHelper.getPrivateValue(ModelRenderer.class, modelRenderer, ReflectionUtils.determineSRG("textureOffsetY"));
    }

    public static TexturedQuad[] getQuadList(ModelBox modelBox) {
        return ReflectionHelper.getPrivateValue(ModelBox.class, modelBox, ReflectionUtils.determineSRG("quadList"));
    }

    public static DataParameter<Boolean> getEndermanScreaming(EntityEnderman enderman) {
        return ReflectionHelper.getPrivateValue(EntityEnderman.class, enderman, ReflectionUtils.determineSRG("SCREAMING"));
    }

    public static void setEndermanScreaming(EntityEnderman enderman, boolean isScreaming) {
        EntityDataManager dm = ReflectionHelper.getPrivateValue(Entity.class, enderman, ReflectionUtils.determineSRG("dataManager"));
        DataParameter<Boolean> screaming = getEndermanScreaming(enderman);
        dm.set(screaming, Boolean.valueOf(isScreaming));
        dm.setDirty(screaming);
        ReflectionHelper.setPrivateValue(Entity.class, enderman, dm, ReflectionUtils.determineSRG("dataManager"));
    }

    public static float getRenderItemZLevel(RenderItem ri) {
        return ReflectionHelper.getPrivateValue(RenderItem.class, ri, ReflectionUtils.determineZLevelSRG("zLevel", RenderItem.class));
    }

    public static void setRenderItemZLevel(RenderItem ri, float zLevel) {
        ReflectionHelper.setPrivateValue(RenderItem.class, ri, zLevel, ReflectionUtils.determineZLevelSRG("zLevel", RenderItem.class));
    }

    public static float getGuiZLevel(Gui gui) {
        return ReflectionHelper.getPrivateValue(Gui.class, gui, ReflectionUtils.determineZLevelSRG("zLevel", Gui.class));
    }

    public static void setGuiZLevel(Gui gui, float zLevel) {
        ReflectionHelper.setPrivateValue(Gui.class, gui, zLevel, ReflectionUtils.determineZLevelSRG("zLevel", Gui.class));
    }

    public static RenderItem getGuiScreenRenderItem(GuiScreen gui) {
        return ReflectionHelper.getPrivateValue(GuiScreen.class, gui, ReflectionUtils.determineSRG("itemRender"));
    }

    public static void setGuiScreenRendererZLevel(GuiScreen gui, float zLevel) {
        ReflectionHelper.setPrivateValue(RenderItem.class, getGuiScreenRenderItem(gui), zLevel, ReflectionUtils.determineZLevelSRG("zLevel", RenderItem.class));
    }

    public static boolean getGuiDragSplitting(GuiContainer gui) {
        return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("dragSplitting"));
    }

    public static Set<Slot> getGuiDragSplittingSlots(GuiContainer gui) {
        return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("dragSplittingSlots"));
    }

    public static int getGuiDragSplittingLimit(GuiContainer gui) {
        return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("dragSplittingLimit"));
    }

    public static Slot getGuiClickedSlot(GuiContainer gui) {
        return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("clickedSlot"));
    }

    public static ItemStack getGuiDraggedStack(GuiContainer gui) {
        return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("draggedStack"));
    }

    public static boolean getGuiIsRightMouseClick(GuiContainer gui) {
        return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("isRightMouseClick"));
    }

    public static int getGuiDragSplittingRemnant(GuiContainer gui) {
        return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("dragSplittingRemnant"));
    }

    public static void setGuiDragSplittingRemnant(GuiContainer gui, int amount) {
        ReflectionHelper.setPrivateValue(GuiContainer.class, gui, amount, ReflectionUtils.determineSRG("dragSplittingRemnant"));
    }

    public static CapabilityDispatcher getItemStackCapabilities(ItemStack stack) {
        return ReflectionHelper.getPrivateValue(ItemStack.class, stack, "capabilities");
    }

    public static void setItemStackCapabilities(ItemStack stack, CapabilityDispatcher dispatcher) {
        ReflectionHelper.setPrivateValue(ItemStack.class, stack, dispatcher, "capabilities");
    }

    public static NBTTagCompound getItemStackCapNBT(ItemStack stack) {
        return ReflectionHelper.getPrivateValue(ItemStack.class, stack, "capNBT");
    }

    public static void setItemStackItem(ItemStack stack, Item item) {
        ReflectionHelper.setPrivateValue(ItemStack.class, stack, item, ReflectionUtils.determineSRG("item"));
    }

    public static void setItemStackDelegate(ItemStack stack, ForgeRegistries delegate) {
        ReflectionHelper.setPrivateValue(ItemStack.class, stack, delegate, "delegate");
    }

    public static int getGuiContainerXSize(GuiContainer gui) {
        return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("xSize"));
    }

    public static int getGuiContainerYSize(GuiContainer gui) {
        return ReflectionHelper.getPrivateValue(GuiContainer.class, gui, ReflectionUtils.determineSRG("ySize"));
    }

    public static float getRainfall(Biome biome) {
        return ReflectionHelper.getPrivateValue(Biome.class, biome, ReflectionUtils.determineSRG("rainfall"));
    }

    public static void setRainfall(Biome biome, float rainfall) {
        ReflectionHelper.setPrivateValue(Biome.class, biome, rainfall, ReflectionUtils.determineSRG("rainfall"));
    }

    public static boolean isRainEnabled(Biome biome) {
        return ReflectionHelper.getPrivateValue(Biome.class, biome, ReflectionUtils.determineSRG("enableRain"));
    }

    public static void setRainEnabled(Biome biome, boolean enableRain) {
        ReflectionHelper.setPrivateValue(Biome.class, biome, enableRain, ReflectionUtils.determineSRG("enableRain"));
    }

    public static BiMap<Class<? extends Entity>, EntityRegistration> getEntityClassRegistry() {
        return ReflectionHelper.getPrivateValue(EntityRegistry.class, EntityRegistry.instance(), "entityClassRegistrations");
    }

    public static Map<String, ModContainer> getEntityNameRegistry() {
        return ReflectionHelper.getPrivateValue(EntityRegistry.class, EntityRegistry.instance(), "entityNames");
    }

    public static ListMultimap<ModContainer, EntityRegistration> getEntityRegistration() {
        return ReflectionHelper.getPrivateValue(EntityRegistry.class, EntityRegistry.instance(), "entityRegistrations");
    }

    public static void setLastDamageSource(EntityLivingBase entity, DamageSource source) {
        ReflectionHelper.setPrivateValue(EntityLivingBase.class, entity, source, ReflectionUtils.determineSRG("lastDamageSource"));
    }

    public static void setLastDamageStamp(EntityLivingBase entity, long time) {
        ReflectionHelper.setPrivateValue(EntityLivingBase.class, entity, time, ReflectionUtils.determineSRG("lastDamageStamp"));
    }

    public static List<Entity> getUnloadedEntityList(World world) {
        return ReflectionHelper.getPrivateValue(World.class, world, ReflectionUtils.determineSRG("unloadedEntityList"));
    }

    public static BlockPos getEntityLastPortalPos(Entity entity) {
        return ReflectionHelper.getPrivateValue(Entity.class, entity, ReflectionUtils.determineSRG("lastPortalPos"));
    }

    public static void setEntityLastPortalPos(Entity entity, BlockPos pos) {
        ReflectionHelper.setPrivateValue(Entity.class, entity, pos, ReflectionUtils.determineSRG("lastPortalPos"));
    }

    public static Vec3d getEntityLastPortalVec(Entity entity) {
        return ReflectionHelper.getPrivateValue(Entity.class, entity, ReflectionUtils.determineSRG("lastPortalVec"));
    }

    public static void setEntityLastPortalVec(Entity entity, Vec3d vector) {
        ReflectionHelper.setPrivateValue(Entity.class, entity, vector, ReflectionUtils.determineSRG("lastPortalVec"));
    }

    public static EnumFacing getEntityTeleportDirection(Entity entity) {
        return ReflectionHelper.getPrivateValue(Entity.class, entity, ReflectionUtils.determineSRG("teleportDirection"));
    }

    public static void setEntityTeleportDirection(Entity entity, EnumFacing direction) {
        ReflectionHelper.setPrivateValue(Entity.class, entity, direction, ReflectionUtils.determineSRG("teleportDirection"));
    }

    public static IntHashMap<Entity> getEntitiesForWorld(World world) {
        return ReflectionHelper.getPrivateValue(World.class, world, ReflectionUtils.determineSRG("entitiesById"));
    }

}
