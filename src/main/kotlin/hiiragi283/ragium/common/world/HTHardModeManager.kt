package hiiragi283.ragium.common.world

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.util.getManager
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState

class HTHardModeManager() : PersistentState() {
    companion object {
        const val KEY = "is_hard_mode"

        @JvmField
        val TYPE: Type<HTHardModeManager> = Type(::HTHardModeManager, ::fromNbt, null)

        @JvmStatic
        fun fromNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): HTHardModeManager =
            HTHardModeManager(nbt.getBoolean(KEY))

        @JvmStatic
        fun getOverworldManager(server: MinecraftServer): HTHardModeManager = getManager(server.overworld, TYPE, Ragium.MOD_ID)
    }

    var isHardMode: Boolean = false

    constructor(boolean: Boolean) : this() {
        isHardMode = boolean
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: RegistryWrapper.WrapperLookup): NbtCompound {
        nbt.putBoolean(KEY, isHardMode)
        return nbt
    }
}
