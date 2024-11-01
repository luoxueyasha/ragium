package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTMetaMachineBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.META_MACHINE, pos, state),
    ExtendedScreenHandlerFactory<HTMachinePacket>,
    PropertyDelegateHolder {
    var machineEntity: HTMachineEntity? = null
        private set

    val definition: HTMachinePacket
        get() = machineEntity?.packet ?: HTMachinePacket.DEFAULT

    override fun setWorld(world: World) {
        super.setWorld(world)
        machineEntity?.onWorldUpdated(world)
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        machineEntity?.writeToNbt(nbt, registryLookup)
        super.writeNbt(nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup) {
        val machineType: HTMachineType = RagiumAPI
            .getInstance()
            .machineTypeRegistry
            .get(Identifier.of(nbt.getString("machine_type")))
            ?: HTMachineType.DEFAULT
        val tier: HTMachineTier = HTMachineTier.entries
            .firstOrNull { it.asString() == nbt.getString("tier") }
            ?: HTMachineTier.PRIMITIVE
        initMachineEntity(machineType, tier)
        machineEntity?.readFromNbt(nbt, registryLookup)
        super.readNbt(nbt, registryLookup)
    }

    override fun readComponents(components: ComponentsAccess) {
        super.readComponents(components)
        val machineType: HTMachineType =
            components.getOrDefault(HTMachineType.COMPONENT_TYPE, HTMachineType.DEFAULT)
        val tier: HTMachineTier = components.getOrDefault(HTMachineTier.COMPONENT_TYPE, HTMachineTier.PRIMITIVE)
        initMachineEntity(machineType, tier)
    }

    override fun addComponents(builder: ComponentMap.Builder) {
        super.addComponents(builder)
        machineEntity?.let {
            builder.add(HTMachineType.COMPONENT_TYPE, it.machineType)
            builder.add(HTMachineTier.COMPONENT_TYPE, it.tier)
        }
    }

    private fun initMachineEntity(machineType: HTMachineType, tier: HTMachineTier) {
        this.machineEntity = machineType[HTMachinePropertyKeys.MACHINE_FACTORY]?.create(machineType, tier)
        this.machineEntity?.setParentBE(this)
    }

    //    HTBlockEntityBase    //

    override fun asInventory(): HTSimpleInventory? = machineEntity?.parent

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = machineEntity?.onUse(state, world, pos, player, hit) ?: ActionResult.PASS

    override var ticks: Int
        get() = machineEntity?.ticks ?: 0
        set(value) {
            machineEntity?.ticks = value
        }

    override val tickRate: Int
        get() = machineEntity?.tickRate ?: 200

    override fun tickEach(
        world: World,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        machineEntity?.tickEach(world, pos, state, ticks)
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        machineEntity?.tickSecond(world, pos, state)
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        machineEntity?.createMenu(syncId, playerInventory, player)

    override fun getDisplayName(): Text = machineEntity?.displayName ?: RagiumBlocks.META_MACHINE.name

    override fun getScreenOpeningData(player: ServerPlayerEntity): HTMachinePacket = definition

    //    PropertyDelegateHolder    //

    override fun getPropertyDelegate(): PropertyDelegate =
        machineEntity?.propertyDelegate ?: ArrayPropertyDelegate(HTMachineEntity.MAX_PROPERTIES)
}
