package hiiragi283.ragium.common.util

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.common.item.HTFluidCellItem
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.component.ComponentChanges
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.state.State
import net.minecraft.state.property.Property
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.Function
import kotlin.enums.EnumEntries

fun <T : Enum<T>> nextEnum(entry: T, values: Array<T>): T = values[(entry.ordinal + 1) % values.size]

fun <T : Enum<T>> nextEnum(entry: T, values: EnumEntries<T>): T = values[(entry.ordinal + 1) % values.size]

//    Either    //

fun <T : Any> Either<out T, out T>.mapCast(): T = map(Function.identity(), Function.identity())

//    BlockState    //

fun World.modifyBlockState(pos: BlockPos, mapping: (BlockState) -> BlockState): Boolean {
    val stateIn: BlockState = getBlockState(pos)
    return setBlockState(pos, mapping(stateIn))
}

fun <O : Any, S : Any, T : Comparable<T>> State<O, S>.getOrNull(property: Property<T>): T? = when (contains(property)) {
    true -> get(property)
    false -> null
}

operator fun <O : Any, S : Any> State<O, S>.contains(property: Property<*>): Boolean = contains(property)

operator fun <O : Any, S : Any, T : Comparable<T>, U : State<O, S>> U.set(property: Property<T>, value: T): U = apply {
    with(property, value)
}

//    CauldronBehavior    //

fun CauldronBehavior.CauldronBehaviorMap.register(item: Item, behavior: CauldronBehavior) {
    map[item] = behavior
}

//    Fluid    //

fun Fluid.getCell(): Item? = HTFluidCellItem.get(this)

fun Fluid.getCellOrThrow(): Item = HTFluidCellItem.getOrThrow(this)

//    ItemStack    //

fun buildItemStack(
    item: ItemConvertible?,
    count: Int = 1,
    builderAction: ComponentChanges.Builder.() -> Unit = {},
): ItemStack {
    if (item == null) return ItemStack.EMPTY
    val item1: Item = item.asItem()
    if (item1 == Items.AIR) return ItemStack.EMPTY
    val entry: RegistryEntry<Item> = Registries.ITEM.getEntry(item1)
    val changes: ComponentChanges = ComponentChanges.builder().apply(builderAction).build()
    return ItemStack(entry, count, changes)
}

//    Transaction    //

inline fun <R : Any> useTransaction(action: (Transaction) -> R): R = Transaction.openOuter().use(action)

//    Recipe    //


//    Reflection    //

inline fun <reified T : Any> Any.getFilteredInstances(): List<T> = this::class.java.declaredFields
    .onEach { it.isAccessible = true }
    .map { it.get(this) }
    .filterIsInstance<T>()

//    World    //

fun dropStackAt(player: PlayerEntity, stack: ItemStack) {
    dropStackAt(player.world, player.blockPos, stack)
}

fun dropStackAt(world: World, pos: BlockPos, stack: ItemStack) {
    val itemEntity = ItemEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
    itemEntity.setPickupDelay(0)
    world.spawnEntity(itemEntity)
}