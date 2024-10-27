package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.World
import java.util.*
import kotlin.jvm.optionals.getOrNull

class HTMachineRecipe(
    val definition: HTMachineDefinition,
    val itemInputs: List<HTIngredient.Item>,
    val fluidInputs: List<HTIngredient.Fluid>,
    val catalyst: HTIngredient.Item?,
    val itemOutputs: List<HTRecipeResult.Item>,
    val fluidOutputs: List<HTRecipeResult.Fluid>,
    val sizeType: SizeType,
) : Recipe<HTMachineInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineDefinition.CODEC
                        .fieldOf("definition")
                        .forGetter(HTMachineRecipe::definition),
                    HTIngredient.ITEM_CODEC
                        .listOf()
                        .optionalFieldOf("item_inputs", listOf())
                        .forGetter(HTMachineRecipe::itemInputs),
                    HTIngredient.FLUID_CODEC
                        .listOf()
                        .optionalFieldOf("fluid_inputs", listOf())
                        .forGetter(HTMachineRecipe::fluidInputs),
                    HTIngredient.ITEM_CODEC
                        .optionalFieldOf("catalyst")
                        .forGetter { Optional.ofNullable(it.catalyst) },
                    HTRecipeResult.ITEM_CODEC
                        .listOf()
                        .optionalFieldOf("item_outputs", listOf())
                        .forGetter(HTMachineRecipe::itemOutputs),
                    HTRecipeResult.FLUID_CODEC
                        .listOf()
                        .optionalFieldOf("fluid_outputs", listOf())
                        .forGetter(HTMachineRecipe::fluidOutputs),
                ).apply(instance, Companion::createRecipe)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineRecipe> = PacketCodec.tuple(
            HTMachineDefinition.PACKET_CODEC,
            HTMachineRecipe::definition,
            HTIngredient.ITEM_PACKET_CODEC.toList(),
            HTMachineRecipe::itemInputs,
            HTIngredient.FLUID_PACKET_CODEC.toList(),
            HTMachineRecipe::fluidInputs,
            PacketCodecs.optional(HTIngredient.ITEM_PACKET_CODEC),
            { Optional.ofNullable(it.catalyst) },
            HTRecipeResult.ITEM_PACKET_CODEC.toList(),
            HTMachineRecipe::itemOutputs,
            HTRecipeResult.FLUID_PACKET_CODEC.toList(),
            HTMachineRecipe::fluidOutputs,
            Companion::createRecipe,
        )

        @JvmStatic
        fun createRecipe(
            definition: HTMachineDefinition,
            itemInputs: List<HTIngredient.Item>,
            fluidInputs: List<HTIngredient.Fluid>,
            catalyst: Optional<HTIngredient.Item>,
            itemOutputs: List<HTRecipeResult.Item>,
            fluidOutputs: List<HTRecipeResult.Fluid>,
        ): HTMachineRecipe {
            val type: HTMachineType = definition.type
            check(type.isProcessor()) { "Only accepts processor machine!" }
            check(type.contains(HTMachinePropertyKeys.RECIPE_SIZE)) { "Machine type must have recipe size property!" }
            check(fluidInputs.size <= 2) { "Fluid inputs must be 2 or less!" }
            check(fluidOutputs.size <= 2) { "Fluid outputs must be 2 or less!" }
            check(itemInputs.size <= 3) { "Item inputs must be 3 or less!" }
            check(itemOutputs.size <= 3) { "Item outputs must be 3 or less!" }
            val bool1: Boolean = fluidInputs.size == 2
            val bool2: Boolean = fluidOutputs.size == 2
            val bool3: Boolean = itemInputs.size == 3
            val bool4: Boolean = itemOutputs.size == 3
            val bool5: Boolean = type[HTMachinePropertyKeys.RECIPE_SIZE] == SizeType.LARGE
            val sizeType: SizeType = when {
                bool1 || bool2 || bool3 || bool4 || bool5 -> SizeType.LARGE
                else -> SizeType.SIMPLE
            }
            return HTMachineRecipe(
                definition,
                itemInputs,
                fluidInputs,
                catalyst.getOrNull(),
                itemOutputs,
                fluidOutputs,
                sizeType,
            )
        }
    }

    val machineType: HTMachineType
        get() = definition.type
    val tier: HTMachineTier
        get() = definition.tier
    val firstOutput: ItemStack
        get() = itemOutputs.getOrNull(0)?.stack ?: ItemStack.EMPTY

    //    Recipe    //

    override fun matches(input: HTMachineInput, world: World): Boolean {
        val bool1: Boolean = input.type == this.machineType
        val bool2: Boolean = input.tier >= this.tier
        val bool3: Boolean = sizeType == input.sizeType
        val bool4: Boolean =
            input.itemInputs
                .getOrNull(0)
                ?.let { itemInputs.getOrNull(0)?.test(it) }
                ?: true
        val bool5: Boolean =
            input.itemInputs
                .getOrNull(1)
                ?.let { itemInputs.getOrNull(1)?.test(it) }
                ?: true
        val bool6: Boolean =
            input.fluidInputs
                .getOrNull(0)
                ?.let { fluidInputs.getOrNull(0)?.test(it) }
                ?: true
        return when (input.sizeType) {
            SizeType.SIMPLE -> {
                bool1 && bool2 && bool3 && bool4 && bool5 && bool6
            }

            SizeType.LARGE -> {
                val bool7: Boolean =
                    input.itemInputs
                        .getOrNull(2)
                        ?.let { itemInputs.getOrNull(2)?.test(it) }
                        ?: true
                val bool8: Boolean =
                    input.fluidInputs
                        .getOrNull(1)
                        ?.let { fluidInputs.getOrNull(1)?.test(it) }
                        ?: true
                bool1 && bool2 && bool3 && bool4 && bool5 && bool6 && bool7 && bool8
            }
        }
    }

    override fun craft(input: HTMachineInput, lookup: RegistryWrapper.WrapperLookup): ItemStack = getResult(lookup)

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getResult(registriesLookup: RegistryWrapper.WrapperLookup): ItemStack = firstOutput

    override fun isIgnoredInRecipeBook(): Boolean = true

    override fun showNotification(): Boolean = false

    override fun createIcon(): ItemStack = definition.iconStack

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MACHINE

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MACHINE

    override fun isEmpty(): Boolean = true

    //    SizeType    //

    enum class SizeType(val invSize: Int, val storageSize: Int) {
        SIMPLE(5, 2),
        LARGE(7, 4),
    }
}
