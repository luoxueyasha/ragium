package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.common.init.RagiumEnergyProviders
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.enums.EnumEntries

object RagiumMachineConditions {
    @JvmField
    val NONE: (World, BlockPos) -> Boolean = { _: World, _: BlockPos -> false }

    @JvmField
    val BLAZING_HEAT: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
        val downPos: BlockPos = pos.down()
        RagiumEnergyProviders.BLAZING_HEAT.find(
            world,
            downPos,
            world.getBlockState(downPos),
            world.getBlockEntity(downPos),
            Direction.UP,
        ) ?: false
    }

    @JvmField
    val ELECTRIC: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
        RagiumEnergyProviders.ENERGY
        val blockEntity: BlockEntity? = world.getBlockEntity(pos)
        (blockEntity as? HTMachineBlockEntityBase)?.tier?.recipeCost?.let { recipeCost: Long ->
            RagiumEnergyProviders.ENERGY
                .find(world, pos, world.getBlockState(pos), blockEntity, null)
                ?.amount
                ?.let { it >= recipeCost }
        } ?: false
    }

    @JvmField
    val HEAT: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
        val downPos: BlockPos = pos.down()
        RagiumEnergyProviders.HEAT.find(
            world,
            downPos,
            world.getBlockState(downPos),
            world.getBlockEntity(downPos),
            Direction.UP,
        ) ?: false
    }

    @JvmField
    val ROCK_GENERATOR: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
        val directions: EnumEntries<Direction> = Direction.entries
        if (directions.any { world.getBlockState(pos.offset(it)).isOf(Blocks.WATER) }) {
            directions.any { world.getBlockState(pos.offset(it)).isOf(Blocks.LAVA) }
        } else {
            false
        }
    }
}
