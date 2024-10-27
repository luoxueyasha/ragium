package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTExporterBlockEntity(pos: BlockPos, state: BlockState) :
    HTTransporterBlockEntityBase(RagiumBlockEntityTypes.EXPORTER, pos, state) {
    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        if (world.isClient) return
        if (world.isReceivingRedstonePower(pos)) return
        // transfer containment
        StorageUtil.move(
            getBackStorage(world, pos, state, ItemStorage.SIDED),
            getFrontStorage(world, pos, state, ItemStorage.SIDED),
            { true },
            8,
            null,
        )
        StorageUtil.move(
            getBackStorage(world, pos, state, FluidStorage.SIDED),
            getFrontStorage(world, pos, state, FluidStorage.SIDED),
            { true },
            FluidConstants.BUCKET,
            null,
        )
    }
}
