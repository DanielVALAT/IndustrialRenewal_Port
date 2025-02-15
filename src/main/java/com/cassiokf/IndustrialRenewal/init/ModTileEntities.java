package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.tileentity.*;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityCargoLoader;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityFluidLoader;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, References.MODID);

    public static RegistryObject<TileEntityType<TileEntitySolarPanelBase>> SOLAR_PANEL_BASE =
            TILE_ENTITIES.register("solar_panel_tile", ()-> TileEntityType.Builder.of(
                    TileEntitySolarPanelBase::new, ModBlocks.SPANEL.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntitySolarPanelFrame>> SOLAR_PANEL_FRAME =
            TILE_ENTITIES.register("solar_panel_frame", ()-> TileEntityType.Builder.of(
                    TileEntitySolarPanelFrame::new, ModBlocks.SPANEL_FRAME.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityBatteryBank>> BATTERY_BANK_TILE =
            TILE_ENTITIES.register("battery_bank_tile", ()-> TileEntityType.Builder.of(
                    TileEntityBatteryBank::new, ModBlocks.BATTERYBANK.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityBarrel>> BARREL_TILE =
            TILE_ENTITIES.register("barrel_tile", ()-> TileEntityType.Builder.of(
                    TileEntityBarrel::new, ModBlocks.BARREL.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityPortableGenerator>> PORTABLE_GENERATOR_TILE =
            TILE_ENTITIES.register("portable_generator_tile", ()-> TileEntityType.Builder.of(
                    TileEntityPortableGenerator::new, ModBlocks.PORTABLE_GENERATOR.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityTrash>> TRASH_TILE =
            TILE_ENTITIES.register("trash_tile", ()-> TileEntityType.Builder.of(
                    TileEntityTrash::new, ModBlocks.TRASH.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityWindTurbinePillar>> TURBINE_PILLAR_TILE =
            TILE_ENTITIES.register("turbine_pillar_tile", ()-> TileEntityType.Builder.of(
                    TileEntityWindTurbinePillar::new, ModBlocks.TURBINE_PILLAR.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityWindTurbineHead>> WIND_TURBINE_TILE =
            TILE_ENTITIES.register("wind_turbine_tile", ()-> TileEntityType.Builder.of(
                    TileEntityWindTurbineHead::new, ModBlocks.WIND_TURBINE.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityEnergyCableLV>> ENERGYCABLE_LV_TILE =
            TILE_ENTITIES.register("energycable_lv_tile", ()-> TileEntityType.Builder.of(
                    TileEntityEnergyCableLV::new, ModBlocks.ENERGYCABLE_LV.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityEnergyCableMV>> ENERGYCABLE_MV_TILE =
            TILE_ENTITIES.register("energycable_mv_tile", ()-> TileEntityType.Builder.of(
                    TileEntityEnergyCableMV::new, ModBlocks.ENERGYCABLE_MV.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityEnergyCableHV>> ENERGYCABLE_HV_TILE =
            TILE_ENTITIES.register("energycable_hv_tile", ()-> TileEntityType.Builder.of(
                    TileEntityEnergyCableHV::new, ModBlocks.ENERGYCABLE_HV.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityFluidPipe>> FLUIDPIPE_TILE =
            TILE_ENTITIES.register("fluidpipe_tile", ()-> TileEntityType.Builder.of(
                    TileEntityFluidPipe::new, ModBlocks.FLUID_PIPE.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityElectricPump>> ELECTRIC_PUMP_TILE =
            TILE_ENTITIES.register("electric_pump_tile", ()-> TileEntityType.Builder.of(
                    TileEntityElectricPump::new, ModBlocks.ELECTRIC_PUMP.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntitySteamBoiler>> STEAM_BOILER_TILE =
            TILE_ENTITIES.register("steam_boiler_tile", ()-> TileEntityType.Builder.of(
                    TileEntitySteamBoiler::new, ModBlocks.STEAM_BOILER.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntitySteamTurbine>> STEAM_TURBINE_TILE =
            TILE_ENTITIES.register("steam_turbine_tile", ()-> TileEntityType.Builder.of(
                    TileEntitySteamTurbine::new, ModBlocks.STEAM_TURBINE.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityMiner>> MINER_TILE =
            TILE_ENTITIES.register("miner_tile", ()-> TileEntityType.Builder.of(
                    TileEntityMiner::new, ModBlocks.MINER.get()).build(null));


    public static RegistryObject<TileEntityType<TileEntityLocker>> LOCKER_TILE =
            TILE_ENTITIES.register("locker_tile", ()-> TileEntityType.Builder.of(
                    TileEntityLocker::new, ModBlocks.MINER.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityStorageChest>> STORAGE_CHEST_TILE =
            TILE_ENTITIES.register("storage_chest_tile", ()-> TileEntityType.Builder.of(
                    TileEntityStorageChest::new, ModBlocks.MINER.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityIndustrialBatteryBank>> INDUSTRIAL_BATTERY_TILE =
            TILE_ENTITIES.register("ind_battery_tile", ()-> TileEntityType.Builder.of(
                    TileEntityIndustrialBatteryBank::new, ModBlocks.INDUSTRIAL_BATTERY_BANK.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityFluidTank>> FLUID_TANK_TILE =
            TILE_ENTITIES.register("fluid_tank_tile", ()-> TileEntityType.Builder.of(
                    TileEntityFluidTank::new, ModBlocks.FLUID_TANK.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityLathe>> LATHE_TILE =
            TILE_ENTITIES.register("lathe_tile", ()-> TileEntityType.Builder.of(
                    TileEntityLathe::new, ModBlocks.LATHE.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityCargoLoader>> CARGO_LOADER =
            TILE_ENTITIES.register("cargo_loader", ()-> TileEntityType.Builder.of(
                    TileEntityCargoLoader::new, ModBlocks.CARGO_LOADER.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityFluidLoader>> FLUID_LOADER =
            TILE_ENTITIES.register("fluid_loader", ()-> TileEntityType.Builder.of(
                    TileEntityFluidLoader::new, ModBlocks.FLUID_LOADER.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityDamIntake>> DAM_INTAKE =
            TILE_ENTITIES.register("dam_intake", ()-> TileEntityType.Builder.of(
                    TileEntityDamIntake::new, ModBlocks.DAM_INTAKE.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityDamOutlet>> DAM_OUTLET =
            TILE_ENTITIES.register("dam_outlet", ()-> TileEntityType.Builder.of(
                    TileEntityDamOutlet::new, ModBlocks.DAM_OUTLET.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityHighPressureFluidPipe>> HIGH_PRESSURE_PIPE =
            TILE_ENTITIES.register("high_pressure_pipe", ()-> TileEntityType.Builder.of(
                    TileEntityHighPressureFluidPipe::new, ModBlocks.HIGH_PRESSURE_PIPE.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityDamTurbine>> DAM_TURBINE_TILE =
            TILE_ENTITIES.register("dam_turbine_tile", ()-> TileEntityType.Builder.of(
                    TileEntityDamTurbine::new, ModBlocks.DAM_TURBINE.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityDamGenerator>> DAM_GENERATOR =
            TILE_ENTITIES.register("dam_generator", ()-> TileEntityType.Builder.of(
                    TileEntityDamGenerator::new, ModBlocks.DAM_GENERATOR.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityConveyor>> CONVEYOR_TILE =
            TILE_ENTITIES.register("conveyor_tile", ()-> TileEntityType.Builder.of(
                    TileEntityConveyor::new, ModBlocks.CONVEYOR.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityConveyorHopper>> CONVEYOR_HOPPER_TILE =
            TILE_ENTITIES.register("conveyor_hopper_tile", ()-> TileEntityType.Builder.of(
                    TileEntityConveyorHopper::new, ModBlocks.CONVEYOR_HOPPER.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityConveyorInserter>> CONVEYOR_INSERTER_TILE =
            TILE_ENTITIES.register("conveyor_inserter_tile", ()-> TileEntityType.Builder.of(
                    TileEntityConveyorInserter::new, ModBlocks.CONVEYOR_INSERTER.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityFluidValve>> FLUID_VALVE_TILE =
            TILE_ENTITIES.register("fluid_valve_tile", ()-> TileEntityType.Builder.of(
                    TileEntityFluidValve::new, ModBlocks.FLUID_VALVE.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityEnergySwitch>> ENERGY_SWITCH_TILE =
            TILE_ENTITIES.register("energy_switch_tile", ()-> TileEntityType.Builder.of(
                    TileEntityEnergySwitch::new, ModBlocks.ENERGY_SWITCH.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityTransformer>> TRANSFORMER_TILE =
            TILE_ENTITIES.register("transformer_tile", ()-> TileEntityType.Builder.of(
                    TileEntityTransformer::new, ModBlocks.TRANSFORMER.get()).build(null));

    public static RegistryObject<TileEntityType<TileEntityWireIsolator>> ISOLATOR_TILE =
            TILE_ENTITIES.register("isolator_tile", ()-> TileEntityType.Builder.of(
                    TileEntityWireIsolator::new, ModBlocks.HV_ISOLATOR.get()).build(null));

    public static void register(IEventBus bus){
        TILE_ENTITIES.register(bus);
    }
}
