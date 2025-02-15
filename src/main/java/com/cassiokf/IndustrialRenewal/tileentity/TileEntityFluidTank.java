package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityFluidTank extends TileEntityTowerBase<TileEntityFluidTank> implements ITickableTileEntity {


    public static final int CAPACITY = Config.FLUID_TANK_CAPACITY.get();
    private CustomFluidTank tank = new CustomFluidTank(CAPACITY){
        @Override
        public void onFluidChange() {
            sync();
            super.onFluidChange();
        }
    };
    private LazyOptional<FluidTank> tankHandler = LazyOptional.of(()->tank);
    private static final int MAX_TRANSFER = Config.FLUID_TANK_TRANSFER_RATE.get();

    private boolean firstLoad = false;
    private int tick = 0;

    private int maxCapcity = 0;
    private int sumCurrent = 0;

    public TileEntityFluidTank(){
        super( ModTileEntities.FLUID_TANK_TILE.get());
    }

    public TileEntityFluidTank(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public void setFirstLoad(){
        if(!level.isClientSide && isMaster()){
            if(isBase()){
                if (tower == null || tower.isEmpty())
                    loadTower();
            }
            else
                this.tower = getBase().tower;
        }
    }

    @Override
    public void loadTower(){
        TileEntityFluidTank chunk = this;
        tower = new ArrayList<>();
        while(chunk != null){
            if(!tower.contains(chunk))
                tower.add(chunk);
            chunk = chunk.getAbove();
        }
    }

    @Override
    public void tick() {
        if (!level.isClientSide && isMaster())
        {
            if (!firstLoad) {
                firstLoad = true;
                setFirstLoad();
            }
            if(isBase()){
                if (tank.getFluidAmount() > 0) {
                    TileEntity te = level.getBlockEntity(worldPosition.below().relative(getMasterFacing().getOpposite(), 2));
                    if (te != null) {
                        IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getMasterFacing()).orElse(null);
                        if (handler != null) {
                            tank.drainInternal(handler.fill(tank.drainInternal(MAX_TRANSFER, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
            else if(isTop()){
                passFluidDown();
            }
            if(tick >= 5){
                tick = 0;
                //Utils.debug("tank", worldPosition, tank.getFluidAmount());
                maxCapcity = getSumMaxFluid();
                sumCurrent = getSumCurrentFluid();
                sync();
            }
            tick++;
        }
    }

    public void passFluidDown(){
        if(getBase().tower != null && !getBase().tower.isEmpty())
        for(TileEntityTowerBase<TileEntityFluidTank> TE : getBase().tower){
            //Utils.debug("TE", TE);
            if(TE instanceof TileEntityFluidTank){
                TileEntityFluidTank bankTE = ((TileEntityFluidTank) TE);

                //Utils.debug("condition 3", bankTE, !bankTE.isFull());
                if(!bankTE.isFull() && bankTE != this) {
                    //Utils.debug("condition 3", bankTE);
                    this.tank.drainInternal(bankTE.tank.fill(this.tank.drainInternal(MAX_TRANSFER, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                    break;
                }
                else continue;
            }
        }
    }

    public boolean isFull(){
        return tank.getFluidAmount() >= tank.getCapacity();
    }

    public int getSumMaxFluid(){
        //int max = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int max = tower.stream().map(te -> (((TileEntityFluidTank) te).tank.getCapacity())).reduce(0, Integer::sum);
        return max;
    }

    public int getSumCurrentFluid(){
        //int current = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int current = tower.stream().map(te -> (((TileEntityFluidTank) te).tank.getFluidAmount())).reduce(0, Integer::sum);
        return current;
    }

    public String getFluidName()
    {
        String name = tank.getFluidAmount() > 0 ? tank.getFluid().getDisplayName().getString() : "Empty";
        return name + ": " + Utils.formatEnergyString((sumCurrent / FluidAttributes.BUCKET_VOLUME)).replace("FE", "B") + " / " + Utils.formatEnergyString((maxCapcity / FluidAttributes.BUCKET_VOLUME)).replace("FE", "B");
    }

    public float getFluidAngle()
    {
        return Utils.normalizeClamped(sumCurrent, 0, maxCapcity) * 180f;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        TileEntityFluidTank master = getMaster();

        if (master != null) {
            Direction masterFacing = getMasterFacing();

            if (masterFacing != null) {
                Direction downFace = masterFacing.getOpposite();

                if (side == null) {
                    System.out.println("Side is null in getCapability (Located at: " + worldPosition + ")");
                    return super.getCapability(cap, side);
                }

                if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
                    if (side == downFace && this.worldPosition != null && this.worldPosition.equals(master.worldPosition.below().relative(downFace))) {
                        return master.tankHandler.cast();
                    }

                    if (side == Direction.UP && this.worldPosition != null && this.worldPosition.equals(master.worldPosition.above())) {
                        return master.tankHandler.cast();
                    }
                }
            } else {
                System.out.println("MasterFacing is null, cannot continue. (Located at: " + worldPosition + ")");
            }
        } else {
            System.out.println("Master is null, cannot continue. (Located at: " + worldPosition + ")");
        }

        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("max", maxCapcity);
        compound.putInt("current", sumCurrent);
        tank.writeToNBT(compound);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        maxCapcity = compound.getInt("max");
        sumCurrent = compound.getInt("current");
        tank.readFromNBT(compound);
        super.load(state, compound);
    }
}
