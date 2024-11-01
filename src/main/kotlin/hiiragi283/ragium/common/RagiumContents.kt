package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.*
import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.block.HTBuddingCrystalBlock
import hiiragi283.ragium.common.block.HTCropsBlock
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.item.*
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
import net.minecraft.block.*
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.FoodComponents
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.*
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.text.Text
import net.minecraft.util.Rarity
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import java.awt.Color

object RagiumContents : HTContentRegister {
    //    Ores    //

    enum class Ores(override val material: RagiumMaterials, val baseStone: Block) : HTContent.Material<Block> {
        CRUDE_RAGINITE(RagiumMaterials.CRUDE_RAGINITE, Blocks.STONE) {
            override val entry: HTRegistryEntry<Block> =
                HTRegistryEntry.ofBlock(RagiumAPI.id("raginite_ore"))
            override val enPattern: String = "Raginite Ore"
            override val jaPattern: String = "ラギナイト鉱石"
        },
        BAUXITE(RagiumMaterials.BAUXITE, Blocks.STONE) {
            override val entry: HTRegistryEntry<Block> =
                HTRegistryEntry.ofBlock(RagiumAPI.id("bauxite_ore"))
            override val enPattern: String = "Bauxite Ore"
            override val jaPattern: String = "ボーキサイト鉱石"
        },
        DEEP_RAGINITE(RagiumMaterials.RAGINITE, Blocks.DEEPSLATE) {
            override val entry: HTRegistryEntry<Block> =
                HTRegistryEntry.ofBlock(RagiumAPI.id("deepslate_raginite_ore"))
            override val enPattern: String = "Deep Raginite Ore"
            override val jaPattern: String = "深層ラギナイト鉱石"
        },
        DEEP_BAUXITE(RagiumMaterials.BAUXITE, Blocks.DEEPSLATE) {
            override val entry: HTRegistryEntry<Block> =
                HTRegistryEntry.ofBlock(RagiumAPI.id("deepslate_bauxite_ore"))
            override val enPattern: String = "Deep Bauxite Ore"
            override val jaPattern: String = "深層ボーキサイト鉱石"
        },
        NETHER_RAGINITE(RagiumMaterials.RAGINITE, Blocks.NETHERRACK) {
            override val entry: HTRegistryEntry<Block> =
                HTRegistryEntry.ofBlock(RagiumAPI.id("nether_raginite_ore"))
            override val enPattern: String = "Nether Raginite Ore"
            override val jaPattern: String = "ネザーラギナイト鉱石"
        },
        END_RAGI_CRYSTAL(RagiumMaterials.RAGI_CRYSTAL, Blocks.END_STONE) {
            override val entry: HTRegistryEntry<Block> =
                HTRegistryEntry.ofBlock(RagiumAPI.id("end_ragi_crystal_ore"))
            override val enPattern: String = "End Ragi-Crystal Ore"
            override val jaPattern: String = "エンドラギクリスタリル鉱石"
        }, ;

        val dropMineral: ItemConvertible
            get() = when (this) {
                CRUDE_RAGINITE -> RawMaterials.CRUDE_RAGINITE
                BAUXITE -> RawMaterials.BAUXITE
                DEEP_RAGINITE -> RawMaterials.RAGINITE
                DEEP_BAUXITE -> RawMaterials.BAUXITE
                NETHER_RAGINITE -> RawMaterials.RAGINITE
                END_RAGI_CRYSTAL -> Gems.RAGI_CRYSTAL
            }

        override val tagKey: TagKey<Item> = ConventionalItemTags.ORES
    }

    //    Storage Blocks    //

    enum class StorageBlocks(override val material: RagiumMaterials) : HTContent.Material<Block> {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        ALUMINUM(RagiumMaterials.ALUMINUM),
        INVAR(RagiumMaterials.INVAR),
        NICKEL(RagiumMaterials.NICKEL),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("${name.lowercase()}_block"))
        override val enPattern: String = "Block of %s"
        override val jaPattern: String = "%sブロック"
        override val tagKey: TagKey<Item> = ConventionalItemTags.STORAGE_BLOCKS
    }

    //    Dusts    //

    enum class Dusts(override val material: RagiumMaterials) : HTContent.Material<Item> {
        CRUDE_RAGINITE(RagiumMaterials.CRUDE_RAGINITE),
        RAGINITE(RagiumMaterials.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterials.RAGI_CRYSTAL),

        ALUMINA(RagiumMaterials.ALUMINA),
        ASH(RagiumMaterials.ASH),
        COPPER(RagiumMaterials.COPPER),
        GOLD(RagiumMaterials.GOLD),
        IRON(RagiumMaterials.IRON),
        NICKEL(RagiumMaterials.NICKEL),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_dust"))
        override val enPattern: String = "%s Dust"
        override val jaPattern: String = "%sの粉"
        override val tagKey: TagKey<Item> = ConventionalItemTags.DUSTS
    }

    //    Gems    //

    enum class Gems(override val material: RagiumMaterials) : HTContent.Material<Item> {
        FLUORITE(RagiumMaterials.FLUORITE),
        OBLIVION_CRYSTAL(RagiumMaterials.OBLIVION_CRYSTAL),
        RAGI_CRYSTAL(RagiumMaterials.RAGI_CRYSTAL),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))
        override val enPattern: String = "%s"
        override val jaPattern: String = "%s"
        override val tagKey: TagKey<Item> = ConventionalItemTags.GEMS
    }

    //    Ingots    //

    enum class Ingots(override val material: RagiumMaterials) : HTContent.Material<Item> {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        ALUMINUM(RagiumMaterials.ALUMINUM),
        INVAR(RagiumMaterials.INVAR),
        NICKEL(RagiumMaterials.NICKEL),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_ingot"))
        override val enPattern: String = "%s Ingot"
        override val jaPattern: String = "%sインゴット"
        override val tagKey: TagKey<Item> = ConventionalItemTags.INGOTS
    }

    //    Plates    //

    enum class Plates(override val material: RagiumMaterials) : HTContent.Material<Item> {
        // tier1
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        IRON(RagiumMaterials.IRON),
        COPPER(RagiumMaterials.COPPER),

        // tier2
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        ALUMINUM(RagiumMaterials.ALUMINUM),
        GOLD(RagiumMaterials.GOLD),
        INVAR(RagiumMaterials.INVAR),
        PLASTIC(RagiumMaterials.PLASTIC),
        SILICON(RagiumMaterials.SILICON),
        STEEL(RagiumMaterials.STEEL),

        // tier3
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ENGINEERING_PLASTIC(RagiumMaterials.ENGINEERING_PLASTIC),
        STELLA(RagiumMaterials.STELLA),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_plate"))
        override val enPattern: String = "%s Plate"
        override val jaPattern: String = "%s板"
        override val tagKey: TagKey<Item> = RagiumItemTags.PLATES
    }

    //    Raw Materials    //

    enum class RawMaterials(override val material: RagiumMaterials) : HTContent.Material<Item> {
        BAUXITE(RagiumMaterials.BAUXITE),
        CRUDE_RAGINITE(RagiumMaterials.CRUDE_RAGINITE),
        RAGINITE(RagiumMaterials.RAGINITE),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("raw_${name.lowercase()}"))
        override val enPattern: String = "Raw %s"
        override val jaPattern: String = "%sの原石"
        override val tagKey: TagKey<Item> = ConventionalItemTags.RAW_MATERIALS
    }

    //    Armors    //

    enum class Armors(override val material: RagiumMaterials, private val armorType: HTArmorType, private val multiplier: Int) :
        HTContent.Material<Item>,
        HTTranslationFormatter {
        STEEL_HELMET(RagiumMaterials.STEEL, HTArmorType.HELMET, 25),
        STEEL_CHESTPLATE(RagiumMaterials.STEEL, HTArmorType.CHESTPLATE, 25),
        STEEL_LEGGINGS(RagiumMaterials.STEEL, HTArmorType.LEGGINGS, 25),
        STEEL_BOOTS(RagiumMaterials.STEEL, HTArmorType.BOOTS, 25),
        STELLA_GOGGLE(RagiumMaterials.STELLA, HTArmorType.HELMET, 33) {
            override val enPattern: String = "%s Goggles"
            override val jaPattern: String = "%s ゴーグル"
        },
        STELLA_JACKET(RagiumMaterials.STELLA, HTArmorType.CHESTPLATE, 33) {
            override val enPattern: String = "%s Jacket"
            override val jaPattern: String = "%s ジャケット"
        },
        STELLA_LEGGINGS(RagiumMaterials.STELLA, HTArmorType.LEGGINGS, 33) {
            override val jaPattern: String = "%s レギンス"
        },
        STELLA_BOOTS(RagiumMaterials.STELLA, HTArmorType.BOOTS, 33) {
            override val jaPattern: String = "%s ブーツ"
        },
        ;

        internal fun createItem(): ArmorItem = armorType.createItem(material.armor!!, multiplier)

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))
        override val enPattern: String by armorType::enPattern
        override val jaPattern: String by armorType::jaPattern

        override val tagKey: TagKey<Item> = armorType.armorTag
    }

    //    Tools    //

    enum class Tools(override val material: RagiumMaterials, val toolType: HTToolType) :
        HTContent.Material<Item>,
        HTTranslationFormatter by toolType {
        STEEL_AXE(RagiumMaterials.STEEL, HTToolType.AXE),
        STEEL_HOE(RagiumMaterials.STEEL, HTToolType.HOE),
        STEEL_PICKAXE(RagiumMaterials.STEEL, HTToolType.PICKAXE),
        STEEL_SHOVEL(RagiumMaterials.STEEL, HTToolType.SHOVEL),
        STEEL_SWORD(RagiumMaterials.STEEL, HTToolType.SWORD),
        ;

        internal fun createItem(): ToolItem = toolType.createToolItem(material.tool!!, itemSettings())

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))

        override val tagKey: TagKey<Item> = toolType.toolTag
    }

    //    Hulls    //

    enum class Hulls(override val material: RagiumMaterials) : HTContent.Material<Block> {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("${name.lowercase()}_hull"))
        override val enPattern: String = "%s Hull"
        override val jaPattern: String = "%s筐体"
        override val tagKey: TagKey<Item> = RagiumItemTags.HULLS
    }

    //    Coils    //

    enum class Coils(override val material: RagiumMaterials) : HTContent.Material<Block> {
        COPPER(RagiumMaterials.COPPER),
        GOLD(RagiumMaterials.GOLD),
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("${name.lowercase()}_coil"))
        override val enPattern: String = "%s Coil"
        override val jaPattern: String = "%sコイル"
        override val tagKey: TagKey<Item> = RagiumItemTags.COILS
    }

    //    Motors    //

    enum class Motors(val tier: HTMachineTier) :
        HTContent<Block>,
        HTTranslationProvider by tier {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val entry: HTRegistryEntry<Block> =
            HTRegistryEntry.ofBlock(RagiumAPI.id("${name.lowercase()}_motor"))
        override val tagKey: TagKey<Item> = RagiumItemTags.MOTORS
    }

    //    Circuits    //

    enum class Circuits(val tier: HTMachineTier) :
        HTContent<Item>,
        HTTranslationProvider by tier {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_circuit"))

        override val tagKey: TagKey<Item> = RagiumItemTags.CIRCUITS
    }

    //    Crops    //

    enum class Crops(val cropName: String, val seedName: String) : ItemConvertible {
        CANOLA("canola", "canola_seeds"),
        SWEET_POTATO("sweet_potatoes", "sweet_potato"),
        ;

        val cropBlock = HTCropsBlock()
        val seedItem = AliasedBlockItem(cropBlock, itemSettings())

        override fun asItem(): Item = seedItem
    }

    //    Foods    //

    enum class Foods : HTContent<Item> {
        BEE_WAX {
            override val tagKey: TagKey<Item> = ConventionalItemTags.DUSTS
        },
        BUTTER {
            override fun food(): FoodComponent = FoodComponents.APPLE
        },
        CANDY_APPLE,
        CARAMEL {
            override fun food(): FoodComponent = FoodComponents.DRIED_KELP
        },
        CHOCOLATE {
            override fun food(): FoodComponent = FoodComponent
                .Builder()
                .nutrition(3)
                .saturationModifier(0.3f)
                .statusEffect(
                    StatusEffectInstance(StatusEffects.STRENGTH, 10 * 20, 0),
                    1.0f,
                ).snack()
                .alwaysEdible()
                .build()
        },
        CHOCOLATE_APPLE {
            override fun food(): FoodComponent = FoodComponents.COOKED_CHICKEN
        },
        CHOCOLATE_BREAD {
            override fun food(): FoodComponent = FoodComponents.COOKED_BEEF
        },
        FLOUR {
            override val tagKey: TagKey<Item> = ConventionalItemTags.DUSTS
        },
        DOUGH,
        MINCED_MEAT {
            override val tagKey: TagKey<Item> = ConventionalItemTags.DUSTS
        },
        PULP {
            override val tagKey: TagKey<Item> = ConventionalItemTags.DUSTS
        },
        ;

        internal open fun food(): FoodComponent? = null

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))
    }

    //    Misc    //

    enum class Misc : HTContent<Item> {
        ALCHEMY_STUFF {
            override fun createSettings(): Item.Settings = super.createSettings().rarity(Rarity.EPIC).maxCount(1)
        },
        BACKPACK {
            override fun createItem(): Item = HTBackpackItem
        },
        BASALT_MESH,
        CRAFTER_HAMMER {
            override fun createItem(): Item = HTCrafterHammerItem
        },
        DYNAMITE {
            override fun createItem(): Item = HTDynamiteItem
        },
        EMPTY_FLUID_CUBE,
        ENGINE,
        FORGE_HAMMER {
            override fun createItem(): Item = HTForgeHammerItem
        },
        HEART_OF_THE_NETHER {
            override fun createSettings(): Item.Settings = super.createSettings().rarity(Rarity.UNCOMMON)
        },
        OBLIVION_CUBE_SPAWN_EGG {
            override fun createItem(): Item = SpawnEggItem(
                RagiumEntityTypes.OBLIVION_CUBE,
                0x000000,
                0xffffff,
                itemSettings(),
            )
        },
        PROCESSOR_SOCKET,
        RAGI_ALLOY_COMPOUND,
        RAGI_CRYSTAL_PROCESSOR,
        REMOVER_DYNAMITE {
            override fun createItem(): Item = HTRemoverDynamiteItem
        },
        SOAP_INGOT,
        SOLAR_PANEL,
        TRADER_CATALOG,
        ;

        internal open fun createSettings(): Item.Settings = itemSettings()

        internal open fun createItem(): Item = Item(createSettings())

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id(name.lowercase()))
    }

    //    Elements    //

    enum class Element(
        private val suitableBiome: TagKey<Biome>,
        override val enName: String,
        override val jaName: String,
        mapColor: MapColor,
    ) : HTTranslationProvider,
        StringIdentifiable {
        RAGIUM(ConventionalBiomeTags.IS_NETHER, "Ragium", "ラギウム", MapColor.RED),
        RIGIUM(ConventionalBiomeTags.IS_WASTELAND, "Rigium", "リギウム", MapColor.YELLOW),
        RUGIUM(ConventionalBiomeTags.IS_JUNGLE, "Rugium", "ルギウム", MapColor.GREEN),
        REGIUM(ConventionalBiomeTags.IS_OCEAN, "Regium", "レギウム", MapColor.BLUE),
        ROGIUM(ConventionalBiomeTags.IS_END, "Rogium", "ロギウム", MapColor.PURPLE),
        ;

        private val settings: AbstractBlock.Settings =
            blockSettings().mapColor(mapColor).strength(1.5f).sounds(BlockSoundGroup.AMETHYST_BLOCK)
        val buddingBlock: HTBuddingCrystalBlock = HTBuddingCrystalBlock(this, settings.ticksRandomly().requiresTool())
        val clusterBlock: AmethystClusterBlock = AmethystClusterBlock(
            7.0f,
            3.0f,
            settings
                .solid()
                .nonOpaque()
                .luminance { 5 }
                .pistonBehavior(PistonBehavior.DESTROY),
        )
        val dustItem = Item(itemSettings())

        val pendantItem = Item(itemSettings().maxDamage(63))
        val ringItem = Item(itemSettings().maxDamage(63))

        val translationKey = "element.${asString()}"
        val text: Text = Text.translatable(translationKey)

        fun isSuitableBiome(world: World, pos: BlockPos): Boolean = world.getBiome(pos).isIn(suitableBiome)

        //    StringIdentifiable    //

        override fun asString(): String = name.lowercase()
    }

    //    Fluids    //

    enum class Fluids(val color: Color, override val enName: String, override val jaName: String) :
        HTContent<Item>,
        HTTranslationProvider {
        // Vanilla
        WATER(Color(0x0033ff), "Water", "水"),
        LAVA(Color(0xff6600), "Lava", "溶岩") {
            override fun createItem(): Item = object : HTFluidCubeItem() {
                override fun onDrink(stack: ItemStack, world: World, user: LivingEntity) {
                    if (!world.isClient) {
                        user.setOnFireFromLava()
                        dropStackAt(user, Items.OBSIDIAN.defaultStack)
                    }
                }
            }
        },
        MILK(Color(0xffffff), "Milk", "牛乳") {
            override fun createItem(): Item = object : HTFluidCubeItem() {
                override fun onDrink(stack: ItemStack, world: World, user: LivingEntity) {
                    if (!world.isClient) {
                        user.clearStatusEffects()
                    }
                }
            }
        },
        HONEY(Color(0xffcc33), "Honey", "蜂蜜") {
            override fun createItem(): Item = object : HTFluidCubeItem() {
                override fun onDrink(stack: ItemStack, world: World, user: LivingEntity) {
                    if (!world.isClient) {
                        user.removeStatusEffect(StatusEffects.POISON)
                    }
                }
            }
        },

        // Molten Materials

        // Organics
        TALLOW(Color(0xcc9933), "Tallow", "獣脂"),
        SEED_OIL(Color(0x99cc33), "Seed Oil", "種油"),
        GLYCEROL(Color(0x99cc66), "Glycerol", "グリセロール"),

        // Foods
        BATTER(Color(0xffcc66), "Batter", "バッター液"),
        CHOCOLATE(Color(0x663300), "Chocolate", "チョコレート") {
            override fun createItem(): Item = Item(
                itemSettings().food(
                    FoodComponent
                        .Builder()
                        .alwaysEdible()
                        .statusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 20 * 5, 1), 1.0f)
                        .usingConvertsTo(Misc.EMPTY_FLUID_CUBE)
                        .build(),
                ),
            )
        },
        STARCH_SYRUP(Color(0x99ffff), "Starch Syrup", "水あめ"),
        SWEET_BERRIES(Color(0x990000), "Sweet Berries", "スイートベリー") {
            override fun createItem(): Item = Item(
                itemSettings().food(
                    FoodComponent
                        .Builder()
                        .alwaysEdible()
                        .statusEffect(StatusEffectInstance(StatusEffects.SPEED, 20 * 5, 0), 1.0f)
                        .usingConvertsTo(Misc.EMPTY_FLUID_CUBE)
                        .build(),
                ),
            )
        },

        // Natural Resources
        SALT_WATER(Color(0x003399), "Salt Water", "塩水"),
        CRUDE_OIL(Color(0x000000), "Crude Oil", "原油"),

        // Elements
        HYDROGEN(Color(0x0000cc), "Hydrogen", "水素"),
        NITROGEN(Color(0x66cccc), "Nitrogen", "窒素"),
        OXYGEN(Color(0x99ccff), "Oxygen", "酸素"),
        CHLORINE(Color(0xccff33), "Chlorine", "塩素"),

        // Non-organic Chemical Compounds
        NITRIC_ACID(Color(0xcc99ff), "Nitric Acid", "硝酸"),
        SODIUM_HYDROXIDE(Color(0x000099), "Sodium Hydroxide Solution", "水酸化ナトリウム水溶液"),
        SULFURIC_ACID(Color(0xff3300), "Sulfuric Acid", "硫酸"),

        // Oil products
        REFINED_GAS(Color(0xcccccc), "Refined Gas", "精製ガス"),
        NAPHTHA(Color(0xff9900), "Naphtha", "ナフサ"),
        RESIDUAL_OIL(Color(0x000033), "Residual Oil", "残渣油"),

        METHANE(Color(0xcc0099), "Methane", "メタン"),
        METHANOL(Color(0xcc00ff), "Methanol", "メタノール"),
        LPG(Color(0xffff33), "LPG", "LGP"),
        ETHYLENE(Color(0x999999), "Ethylene", "エチレン"),
        ETHANOL(Color(0xccffff), "Ethanol", "エタノール"),

        AROMATIC_COMPOUNDS(Color(0x666699), "Aromatic Compounds", "芳香族化合物"),

        LUBRICANT(Color(0x996633), "Lubricant", "潤滑油"),
        ASPHALT(Color(0x000066), "Asphalt", "アスファルト"),

        // Fuels
        BIO_FUEL(Color(0x99ff00), "Bio Fuel", "バイオ燃料"),
        FUEL(Color(0xcc6633), "Fuel", "燃料"),
        NITRO_FUEL(Color(0xff33333), "Nitro Fuel", "ニトロ燃料"),

        // Explodes
        NITRO_GLYCERIN(Color(0x99cc66), "Nitroglycerin", "ニトログリセリン"),
        TRINITROTOLUENE(Color(0x666699), "Trinitrotoluene", "トリニトロトルエン"),
        ;

        override val entry: HTRegistryEntry<Item> =
            HTRegistryEntry.ofItem(RagiumAPI.id("${name.lowercase()}_fluid_cube"))

        internal open fun createItem(): Item = Item(itemSettings())

        override val tagKey: TagKey<Item>? = RagiumItemTags.FLUID_CUBES
    }
}
