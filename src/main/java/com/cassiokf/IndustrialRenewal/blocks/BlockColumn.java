package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Objects;

public class BlockColumn extends BlockAbstractSixWayConnections {

    public BlockColumn()
    {
        super(Block.Properties.of(Material.METAL), 8, 12);
    }

    protected boolean isValidConnection(final BlockState neighborState, final BlockState ownState, final IBlockReader world, final BlockPos ownPos, final Direction neighborDirection, BlockPos neighborPos)
    {
        Block nb = neighborState.getBlock();
        //if ((neighborState.isSolid() || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable)
        if (Block.canSupportRigidBlock(world, neighborPos)
                && neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {

            Block oppositBlock = world.getBlockState(ownPos.relative(neighborDirection.getOpposite())).getBlock();
            return oppositBlock instanceof BlockColumn || oppositBlock instanceof BlockPillar;
        }
        if (neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {
            if (nb instanceof BlockBrace)
            {
                return Objects.equals(neighborState.getValue(BlockBrace.FACING).getName(), neighborDirection.getOpposite().getName()) || Objects.equals(neighborState.getValue(BlockBrace.FACING).getName(), "down_" + neighborDirection.getName());
            }
            return nb instanceof BlockColumn || nb instanceof BlockPillar
//                    || (nb instanceof BlockHVIsolator && neighborState.get(BlockHVIsolator.FACING) == neighborDirection.getOpposite())
//                    || nb instanceof BlockPillarEnergyCable || nb instanceof BlockPillarFluidPipe
//                    || (nb instanceof BlockAlarm && neighborState.get(BlockAlarm.FACING) == neighborDirection)
                    || (nb instanceof BlockLight && neighborState.getValue(BlockLight.FACING) == neighborDirection.getOpposite());
        }
        if(nb instanceof BlockColumn)
            return true;
//        if (nb instanceof BlockLight)
//        {
//            return neighborState.get(BlockLight.FACING) == Direction.UP;
//        }
        if (nb instanceof BlockBrace)
        {
            return Direction.Plane.HORIZONTAL.test(neighborState.getValue(BlockBrace.FACING).getFacing());
        }
//        if (nb instanceof BlockFluidPipe)
//        {
//            return ownState.get(PIPE) > 0;
//        }
//        if (nb instanceof BlockCableTray)
//        {
//            return neighborState.get(BlockCableTray.BASE).equals(EnumBaseDirection.UP);
//        }
//        return !(nb instanceof BlockCatwalkLadder)
//                && !(nb instanceof BlockSignBase)
//                && !(nb instanceof BlockFireExtinguisher)
//                && !(nb instanceof BlockAlarm && !(neighborState.get(BlockAlarm.FACING) == neighborDirection))
//                && !nb.isAir(neighborState, world, ownPos.offset(neighborDirection));
        return false;
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        BlockState ownState = worldIn.getBlockState(currentPos);
        return isValidConnection(neighborState, ownState, worldIn, currentPos, neighborDirection, neighborPos);
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        switch (facing)
        {
            case DOWN:
                return state.getValue(DOWN);
            case UP:
                return state.getValue(UP);
            default:
            case NORTH:
                return state.getValue(NORTH);
            case SOUTH:
                return state.getValue(SOUTH);
            case WEST:
                return state.getValue(WEST);
            case EAST:
                return state.getValue(EAST);
        }
    }

    @Override
    public VoxelShape getVoxelShape(BlockState state, boolean collision)
    {
        float NORTHZ1;
        float SOUTHZ2;
        float WESTX1;
        float EASTX2;
        float DOWNY1;

        if (isConnected(state, Direction.NORTH))
        {
            NORTHZ1 = 0;
        } else
        {
            NORTHZ1 = 4;
        }
        if (isConnected(state, Direction.SOUTH))
        {
            SOUTHZ2 = 16;
        } else
        {
            SOUTHZ2 = 12;
        }
        if (isConnected(state, Direction.WEST))
        {
            WESTX1 = 0;
        } else
        {
            WESTX1 = 4;
        }
        if (isConnected(state, Direction.EAST))
        {
            EASTX2 = 16;
        } else
        {
            EASTX2 = 12;
        }
        if (isConnected(state, Direction.DOWN))
        {
            DOWNY1 = 0;
        } else
        {
            DOWNY1 = 5;
        }
        return Block.box(WESTX1, DOWNY1, NORTHZ1, EASTX2, 16, SOUTHZ2);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        //Utils.debug("neighbor changed", state, world, pos, block, neighbor, flag);
        for (Direction face : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        world.setBlockAndUpdate(pos, state);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }
}
