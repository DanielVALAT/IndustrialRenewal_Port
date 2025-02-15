package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x2x3Base;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntity3x2x3MachineBase<TE extends TileEntity3x2x3MachineBase> extends TileEntity3x3x3MachineBase<TE>{
    public TileEntity3x2x3MachineBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
    public BlockPos masterPos = worldPosition;

    @Override
    public TE getMaster() {
        TileEntity te = level.getBlockEntity(masterPos);
        if(te != null && te instanceof TileEntity3x2x3MachineBase
                && ((TileEntity3x2x3MachineBase) te).isMaster()
                && instanceOf(te)) {
            masterTE = (TE) te;
            return masterTE;
        }
        else if (te == null){
            List<BlockPos> list = Utils.getBlocksIn3x2x3Centered(worldPosition);
            for (BlockPos currentPos : list)
            {
                TileEntity te2 = level.getBlockEntity(currentPos);
                if (te2 != null && te2 instanceof TileEntity3x3x3MachineBase
                        && ((TileEntity3x2x3MachineBase) te2).isMaster()
                        && instanceOf(te2))
                {
                    masterTE = (TE) te2;
                    return masterTE;
                }
            }
        }

        return null;
    }

    @Override
    public void setRemoved() {
        TileEntity3x2x3MachineBase te = (TileEntity3x2x3MachineBase) level.getBlockEntity(masterPos);
        if(te != null)
            te.breakMultiBlocks();
        super.setRemoved();
    }

    @Override
    public Direction getMasterFacing()
    {
        if (faceChecked) return Direction.from3DDataValue(faceIndex);
        if(level == null || getMaster() == null || level.getBlockState(getMaster().worldPosition) == null)
            return Direction.NORTH;

        Direction facing = level.getBlockState(getMaster().worldPosition).getValue(Block3x2x3Base.FACING);
        faceChecked = true;
        faceIndex = facing.get3DDataValue();
        return facing;
    }

    public Direction getMasterFacingDirect(){
        if (faceChecked) return Direction.from3DDataValue(faceIndex);
        Direction facing = level.getBlockState(worldPosition).getValue(Block3x2x3Base.FACING);
        faceChecked = true;
        faceIndex = facing.get3DDataValue();
        return facing;
    }

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x2x3Centered(centerPosition);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntity3x2x3MachineBase;
    }

    @Override
    public void breakMultiBlocks() {
        if (!this.isMaster())
        {
            if (getMaster() != null)
            {
                getMaster().breakMultiBlocks();
            }
            return;
        }
        if (!breaking)
        {
            breaking = true;
            onMasterBreak();
            List<BlockPos> list = getListOfBlockPositions(worldPosition);
            for (BlockPos currentPos : list)
            {
                Block block = level.getBlockState(currentPos).getBlock();
                if (block instanceof Block3x2x3Base) level.removeBlock(currentPos, false);
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putLong("masterPos", masterPos.asLong());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        masterPos = BlockPos.of(compound.getLong("masterPos"));
        super.load(state, compound);
    }
}
