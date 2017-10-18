package com.stormy.lightningadditions.network.messages;

import com.stormy.lightningadditions.tile.resource.TileEntityDisplayCase;
import com.stormy.lightningadditions.utility.logger.LALogger;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDisplayCase implements IMessage, IMessageHandler<MessageDisplayCase, IMessage> {

    private BlockPos pos;
    
    private double translateX;
    private double translateY;
    private double translateZ;

    private double rotateX;
    private double rotateY;
    private double rotateZ;

    private double scale;

    public MessageDisplayCase() {

    }

    public MessageDisplayCase(BlockPos pos, double translateX, double translateY, double translateZ, double rotateX, double rotateY, double rotateZ, double scale) {
        this.pos = pos;
        this.translateX = translateX;
        this.translateY = translateY;
        this.translateZ = translateZ;
        this.rotateX = rotateX;
        this.rotateY = rotateY;
        this.rotateZ = rotateZ;
        this.scale = scale;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.translateX = buf.readDouble();
        this.translateY = buf.readDouble();
        this.translateZ = buf.readDouble();
        this.rotateX = buf.readDouble();
        this.rotateY = buf.readDouble();
        this.rotateZ = buf.readDouble();
        this.scale = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeDouble(this.translateX);
        buf.writeDouble(this.translateY);
        buf.writeDouble(this.translateZ);
        buf.writeDouble(this.rotateX);
        buf.writeDouble(this.rotateY);
        buf.writeDouble(this.rotateZ);
        buf.writeDouble(this.scale);
    }

    @Override
    public IMessage onMessage(MessageDisplayCase message, MessageContext ctx) {

        World world = ctx.getServerHandler().player.world;
        TileEntity te = world.getTileEntity(message.pos);
        
        if (te instanceof TileEntityDisplayCase){
            TileEntityDisplayCase ted = (TileEntityDisplayCase) te;

            NBTTagCompound nbt = te.getTileData();

            nbt.setDouble("tx", message.translateX);
            nbt.setDouble("ty", message.translateY);
            nbt.setDouble("tz", message.translateZ);
            nbt.setDouble("rx", message.rotateX);
            nbt.setDouble("ry", message.rotateY);
            nbt.setDouble("rz", message.rotateZ);
            nbt.setDouble("s", message.scale);

            ted.markDirty();

        }

        return null;
    }
}
