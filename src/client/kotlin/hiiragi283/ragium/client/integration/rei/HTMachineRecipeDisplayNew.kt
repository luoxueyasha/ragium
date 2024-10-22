package hiiragi283.ragium.client.integration.rei

import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeNew
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryIngredient
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.Identifier
import java.util.*

@Environment(EnvType.CLIENT)
sealed class HTMachineRecipeDisplayNew(val recipe: HTMachineRecipeNew<*>, val id: Identifier) : Display {
    fun getItemInput(index: Int): EntryIngredient = recipe.itemInputs.getOrNull(index)?.entryIngredient ?: EntryIngredient.empty()

    fun getFluidInput(index: Int): EntryIngredient = recipe.fluidInputs.getOrNull(index)?.entryIngredient ?: EntryIngredient.empty()

    fun getItemOutput(index: Int): EntryIngredient = recipe.itemOutputs.getOrNull(index)?.entryIngredient ?: EntryIngredient.empty()

    fun getFluidOutput(index: Int): EntryIngredient = recipe.fluidOutputs.getOrNull(index)?.entryIngredient ?: EntryIngredient.empty()

    val catalyst: EntryIngredient = recipe.catalyst.entryIngredient

    final override fun getDisplayLocation(): Optional<Identifier> = Optional.of(id)

    //    Simple    //

    class Simple(recipe: HTMachineRecipeNew.Simple, id: Identifier) : HTMachineRecipeDisplayNew(recipe, id) {
        override fun getInputEntries(): List<EntryIngredient> = buildList {
            add(getItemInput(0))
            add(getItemInput(1))
            add(getFluidInput(0))
        }

        override fun getOutputEntries(): List<EntryIngredient> = buildList {
            add(getItemOutput(0))
            add(getItemOutput(1))
            add(getFluidOutput(0))
        }

        override fun getCategoryIdentifier(): CategoryIdentifier<*> = recipe.machineType.categoryIdNew
    }

    //    Large    //

    class Large(recipe: HTMachineRecipeNew.Large, id: Identifier) : HTMachineRecipeDisplayNew(recipe, id) {
        override fun getInputEntries(): List<EntryIngredient> = buildList {
            add(getItemInput(0))
            add(getItemInput(1))
            add(getItemInput(2))
            add(getFluidInput(0))
            add(getFluidInput(1))
        }

        override fun getOutputEntries(): List<EntryIngredient> = buildList {
            add(getItemOutput(0))
            add(getItemOutput(1))
            add(getItemOutput(2))
            add(getFluidOutput(0))
            add(getFluidOutput(1))
        }

        override fun getCategoryIdentifier(): CategoryIdentifier<*> = recipe.machineType.categoryIdNew
    }
}