package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumEnvironmentBridge
import hiiragi283.ragium.api.energy.HTEnergyType
import hiiragi283.ragium.api.trade.HTTradeOfferRegistry
import hiiragi283.ragium.api.widget.HTFluidWidget
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.ModInitializer
import net.minecraft.fluid.Fluid

object RagiumCommon : ModInitializer, RagiumEnvironmentBridge {
    override fun onInitialize() {
        RagiumAPI.log { info("Registering game objects...") }
        RagiumConfig.init()

        RagiumComponentTypes
        HTEnergyType

        RagiumAdvancementCriteria
        RagiumBlockEntityTypes
        RagiumBlocks
        RagiumEntityTypes.init()
        RagiumRecipeSerializers
        RagiumRecipeTypes

        InternalRagiumAPI.initMachineType()
        RagiumContentRegister.registerContents()

        HTHardModeResourceCondition.init()
        HTTradeOfferRegistry.init()
        RagiumBlockEntityTypes.init()
        RagiumCommands.init()
        RagiumEventHandlers.init()
        RagiumFeatures.init()
        RagiumItemGroup.init()
        RagiumMaterialItemRecipes.init()
        RagiumNetworks

        RagiumContentRegister.initRegistry()

        InternalRagiumAPI.integrationCache.forEach(Runnable::run)

        RagiumAPI.log { info("Ragium initialized!") }
    }

    //    RagiumEnvironmentBridge    //

    override val environment: EnvType = EnvType.SERVER

    override fun createFluidWidget(fluid: Fluid): HTFluidWidget = HTFluidWidget(fluid)

    override fun createFluidWidget(fluid: Fluid, amount: Long): HTFluidWidget = HTFluidWidget(fluid, amount)
}
