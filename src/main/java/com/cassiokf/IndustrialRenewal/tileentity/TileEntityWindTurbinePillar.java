package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.BlockWindTurbinePillar;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

//import com.cassiokf.IndustrialRenewal.util.MultiBlockHelper;

public class TileEntityWindTurbinePillar extends TileEntityMultiBlocksTube<TileEntityWindTurbinePillar> implements ICapabilityProvider {

    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private LazyOptional<IEnergyStorage> dummyEnergy = LazyOptional.of(this::createEnergyDummy);

    private float amount;//For Lerp

    private int tick;

    private Direction[] faces = new Direction[]{Direction.UP, Direction.DOWN};
    private BlockPos turbinePos;
    private boolean isBase;

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(1024, 1024, 1024)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityWindTurbinePillar.this.setChanged();
            }
        };
    }

    private IEnergyStorage createEnergyDummy()
    {
        return new CustomEnergyStorage(0, 0, 0)
        {
            @Override
            public boolean canReceive()
            {
                return false;
            }
        };
    }

    public TileEntityWindTurbinePillar()
    {
        super(ModTileEntities.TURBINE_PILLAR_TILE.get());
    }

    @Override
    public void doTick()
    {
        if (isMaster())
        {
            if (!level.isClientSide)
            {
                IEnergyStorage thisEnergy = energyStorage.orElse(null);
                energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(1024 * getPosSet().size(), thisEnergy.getEnergyStored())));
                int energyReceived = 0;
                for (BlockPos currentPos : getPosSet().keySet())
                {
                    TileEntity te = level.getBlockEntity(currentPos);
                    Direction face = getPosSet().get(currentPos);
                    if (thisEnergy != null && te != null)
                    {
                        IEnergyStorage eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
                        if (eStorage != null && eStorage.canReceive())
                        {
                            energyReceived += eStorage.receiveEnergy(thisEnergy.extractEnergy(1024, true), false);
                            thisEnergy.extractEnergy(energyReceived, false);
                        }
                    }
                }

                outPut = energyReceived;

                if (oldOutPut != outPut)
                {
                    oldOutPut = outPut;
                    this.sync();
                }
            } else if (getTurbinePos() != null && isBase)
            {
                if (tick % 10 == 0)
                {
                    tick = 0;
                    this.sync();
                    if (!(level.getBlockEntity(turbinePos) instanceof TileEntityWindTurbineHead))
                    {
                        forceNewTurbinePos();
                    }
                }
                tick++;
            }
        }
    }

    @Override
    public Direction[] getFacesToCheck()
    {
        return faces;
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityWindTurbinePillar;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        isBase = getIsBase();
        if (isBase) forceNewTurbinePos();
        if (level.isClientSide) return;
        for (Direction face : Direction.Plane.HORIZONTAL)
        {
            BlockPos currentPos = worldPosition.relative(face);
            if (isBase)
            {
                BlockState state = level.getBlockState(currentPos);
                TileEntity te = level.getBlockEntity(currentPos);
                boolean hasMachine = !(state.getBlock() instanceof BlockWindTurbinePillar)
                        && te != null && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent();

//                if(te == null)
//                    return;
//                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
//                if (hasMachine && energyStorage != null && energyStorage.canReceive())

                if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null).canReceive())
                    if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                    else if (!isMasterInvalid()) getMaster().removeMachine(worldPosition, currentPos);
            } else
            {
                if (!isMasterInvalid()) getMaster().removeMachine(worldPosition, currentPos);
            }
        }
        this.sync();
    }

    private BlockPos getTurbinePos()
    {
        if (turbinePos != null) return turbinePos;
        return forceNewTurbinePos();
    }


    private BlockPos forceNewTurbinePos()
    {
        int n = 1;
        while (level.getBlockEntity(worldPosition.above(n)) instanceof TileEntityWindTurbinePillar)
        {
            n++;
        }
        if (level.getBlockEntity(worldPosition.above(n)) instanceof TileEntityWindTurbineHead) turbinePos = worldPosition.above(n);
        else turbinePos = null;
        return turbinePos;
    }

    public Direction getBlockFacing()
    {
        return getBlockState().getValue(BlockWindTurbinePillar.FACING);
    }

    public float getGenerationforGauge()
    {
        float currentAmount = getEnergyGenerated();
        float totalCapacity = TileEntityWindTurbineHead.energyGeneration;
        currentAmount = currentAmount / totalCapacity;
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1) * 90f;
    }

    public int getEnergyGenerated()
    {
        if(getMaster() == null || getMaster().getTurbinePos() == null)
            return 0;
        return getMaster().outPut;
    }

    public String getText()
    {
        if (getMaster() == null || getMaster().getTurbinePos() == null) return "No Turbine";
        return getEnergyGenerated() + " FE/t";
    }

    public boolean isBase()
    {
        return isBase;
    }

    public boolean getIsBase()
    {
        BlockState state = level.getBlockState(worldPosition.below());
        return !(state.getBlock() instanceof BlockWindTurbinePillar);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (facing == null)
            return super.getCapability(capability, facing);

        if (capability == CapabilityEnergy.ENERGY && (facing == Direction.UP))
            return getMaster().energyStorage.cast();
        if (capability == CapabilityEnergy.ENERGY && (isBase()))
            return dummyEnergy.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        //industrialrenewal.LOGGER.info("load compound");
        energyStorage.ifPresent(h -> {
            ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("energy"));
            //industrialrenewal.LOGGER.info("energyStorage present, load success");
        });
        this.isBase = compound.getBoolean("base");
        TileEntityWindTurbinePillar te = null;
        if (compound.contains("masterPos") && hasLevel())
            te = (TileEntityWindTurbinePillar) level.getBlockEntity(BlockPos.of(compound.getLong("masterPos")));
        if (te != null) this.setMaster(te);
        super.load(state, compound);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound)
    {
        //industrialrenewal.LOGGER.info("save compound");
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
            //industrialrenewal.LOGGER.info("energyStorage present, save success");
        });
        compound.putBoolean("base", this.isBase);
        if (getMaster() != null) compound.putLong("masterPos", getMaster().getBlockPos().asLong());
        return super.save(compound);
    }
    private boolean canConnectTo(final Direction neighborDirection)
    {
        final BlockPos neighborPos = worldPosition.relative(neighborDirection);
        final BlockState neighborState = level.getBlockState(neighborPos);

        if (neighborDirection == Direction.DOWN)
        {
            return !(neighborState.getBlock() instanceof BlockWindTurbinePillar);
        }
        TileEntity te = level.getBlockEntity(neighborPos);
        return te != null
                && te.getCapability(CapabilityEnergy.ENERGY, neighborDirection.getOpposite()).isPresent();
    }
}

