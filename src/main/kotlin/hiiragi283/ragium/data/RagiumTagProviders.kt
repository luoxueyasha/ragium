package hiiragi283.ragium.data

import hiiragi283.ragium.api.component.HTModularToolComponent
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.tags.RagiumEnchantmentTags
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.EnchantmentTags
import net.minecraft.registry.tag.TagKey
import java.util.concurrent.CompletableFuture

object RagiumTagProviders {
    @JvmStatic
    fun init(pack: FabricDataGenerator.Pack) {
        pack.addProvider(RagiumTagProviders::BlockProvider)
        pack.addProvider(RagiumTagProviders::EnchantmentProvider)
        // pack.addProvider(RagiumTagProviders::FluidProvider)
        pack.addProvider(RagiumTagProviders::ItemProvider)
    }

    //    Block    //

    private class BlockProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.BlockTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            fun add(tagKey: TagKey<Block>, block: Block) {
                getOrCreateTagBuilder(tagKey).add(block)
            }
            
            // vanilla
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.POROUS_NETHERRACK)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.OBLIVION_CLUSTER)

            add(BlockTags.HOE_MINEABLE, RagiumBlocks.SPONGE_CAKE)

            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.BASIC_CASING)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.ADVANCED_CASING)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.MANUAL_GRINDER)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.DATA_DRIVE)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.DRIVE_SCANNER)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.NETWORK_INTERFACE)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.SHAFT)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.ALCHEMICAL_INFUSER)
            add(BlockTags.PICKAXE_MINEABLE, RagiumBlocks.META_MACHINE)

            buildList {
                addAll(RagiumContents.Ores.entries)
                addAll(RagiumContents.StorageBlocks.entries)
                addAll(RagiumContents.Hulls.entries)
                addAll(RagiumContents.Coils.entries)
            }.forEach { add(BlockTags.PICKAXE_MINEABLE, it.value) }
        }
    }

    //    Enchantment    //

    private class EnchantmentProvider(output: FabricDataOutput, completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.EnchantmentTagProvider(output, completableFuture) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            getOrCreateTagBuilder(EnchantmentTags.TRADEABLE)
                .add(RagiumEnchantments.SMELTING)
                .add(RagiumEnchantments.SLEDGE_HAMMER)
                .add(RagiumEnchantments.BUZZ_SAW)

            getOrCreateTagBuilder(EnchantmentTags.TREASURE)
                .add(RagiumEnchantments.SMELTING)
                .add(RagiumEnchantments.SLEDGE_HAMMER)
                .add(RagiumEnchantments.BUZZ_SAW)

            getOrCreateTagBuilder(RagiumEnchantmentTags.MODIFYING_EXCLUSIVE_SET)
                .add(RagiumEnchantments.SMELTING)
                .add(RagiumEnchantments.SLEDGE_HAMMER)
                .add(RagiumEnchantments.BUZZ_SAW)
        }
    }

    //    Fluid    //

    /*private class FluidProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.FluidTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
        }
    }*/

    //    Item    //

    private class ItemProvider(output: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
        FabricTagProvider.ItemTagProvider(output, registryLookup) {
        override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
            fun add(tagKey: TagKey<Item>, item: ItemConvertible) {
                getOrCreateTagBuilder(tagKey).add(item.asItem())
            }

            // conventional
            add(RagiumItemTags.BASALTS, Items.BASALT)
            add(RagiumItemTags.BASALTS, Items.POLISHED_BASALT)
            add(RagiumItemTags.BASALTS, Items.SMOOTH_BASALT)
            add(RagiumItemTags.COPPER_DUSTS, RagiumContents.Dusts.COPPER)
            add(RagiumItemTags.COPPER_PLATES, RagiumContents.Plates.COPPER)
            add(RagiumItemTags.GOLD_DUSTS, RagiumContents.Dusts.GOLD)
            add(RagiumItemTags.GOLD_PLATES, RagiumContents.Plates.GOLD)
            add(RagiumItemTags.INVAR_BLOCKS, RagiumContents.StorageBlocks.INVAR)
            add(RagiumItemTags.INVAR_INGOTS, RagiumContents.Ingots.INVAR)
            add(RagiumItemTags.INVAR_PLATES, RagiumContents.Plates.INVAR)
            add(RagiumItemTags.IRON_DUSTS, RagiumContents.Dusts.IRON)
            add(RagiumItemTags.IRON_PLATES, RagiumContents.Plates.IRON)
            add(RagiumItemTags.NICKEL_BLOCKS, RagiumContents.StorageBlocks.NICKEL)
            add(RagiumItemTags.NICKEL_DUSTS, RagiumContents.Dusts.NICKEL)
            add(RagiumItemTags.NICKEL_INGOTS, RagiumContents.Ingots.NICKEL)
            add(RagiumItemTags.RAGINITE_ORES, RagiumContents.Ores.DEEP_RAGINITE)
            add(RagiumItemTags.RAGINITE_ORES, RagiumContents.Ores.NETHER_RAGINITE)
            add(RagiumItemTags.SILICON_PLATES, RagiumContents.Plates.SILICON)
            add(RagiumItemTags.SILVER_BLOCKS, RagiumContents.StorageBlocks.SILVER)
            add(RagiumItemTags.SILVER_DUSTS, RagiumContents.Dusts.SILVER)
            add(RagiumItemTags.SILVER_INGOTS, RagiumContents.Ingots.SILVER)
            add(RagiumItemTags.SILVER_PLATES, RagiumContents.Plates.SILVER)
            add(RagiumItemTags.STEEL_BLOCKS, RagiumContents.StorageBlocks.STEEL)
            add(RagiumItemTags.STEEL_INGOTS, RagiumContents.Ingots.STEEL)
            add(RagiumItemTags.STEEL_PLATES, RagiumContents.Plates.STEEL)
            add(RagiumItemTags.SULFUR_DUSTS, RagiumContents.Dusts.SULFUR)

            buildList {
                addAll(RagiumContents.Ores.entries)
                addAll(RagiumContents.StorageBlocks.entries)

                addAll(RagiumContents.Dusts.entries)
                addAll(RagiumContents.Ingots.entries)
                addAll(RagiumContents.Plates.entries)
                addAll(RagiumContents.RawMaterials.entries)

                addAll(RagiumContents.Hulls.entries)
                addAll(RagiumContents.Coils.entries)
                addAll(RagiumContents.Motors.entries)
                addAll(RagiumContents.Circuits.entries)

                addAll(RagiumContents.Armors.entries)
                addAll(RagiumContents.Tools.entries)

                addAll(RagiumContents.Foods.entries)
                addAll(RagiumContents.Misc.entries)
                addAll(RagiumContents.Fluids.entries)
            }.forEach { content: HTContent<out ItemConvertible> ->
                content.tagKey?.let { add(it, content) }
            }
            
            RagiumContents.Element.entries.forEach { add(ConventionalItemTags.DUSTS, it.dustItem) }
            // ragium
            add(RagiumItemTags.ALKALI, RagiumContents.Dusts.ASH)
            add(RagiumItemTags.ALKALI, RagiumContents.Fluids.SODIUM_HYDROXIDE)

            add(RagiumItemTags.ORGANIC_OILS, RagiumContents.Fluids.TALLOW)
            add(RagiumItemTags.ORGANIC_OILS, RagiumContents.Fluids.SEED_OIL)

            getOrCreateTagBuilder(RagiumItemTags.PROTEIN_FOODS)
                .add(Items.ROTTEN_FLESH)
                .addOptionalTag(ConventionalItemTags.RAW_MEAT_FOODS)
                .addOptionalTag(ConventionalItemTags.COOKED_MEAT_FOODS)
                .addOptionalTag(ConventionalItemTags.RAW_FISH_FOODS)
                .addOptionalTag(ConventionalItemTags.COOKED_FISH_FOODS)

            buildList {
                addAll(HTModularToolComponent.Behavior.entries)
            }.forEach { add(RagiumItemTags.TOOL_MODULES, it) }
        }
    }
}
