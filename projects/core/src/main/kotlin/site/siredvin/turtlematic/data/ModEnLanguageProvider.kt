package site.siredvin.turtlematic.data

import net.minecraft.data.PackOutput
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.computercraft.peripheral.automatas.*
import site.siredvin.turtlematic.computercraft.peripheral.forged.*
import site.siredvin.turtlematic.computercraft.peripheral.misc.*
import site.siredvin.turtlematic.util.toCreative
import site.siredvin.turtlematic.util.toNetherite
import site.siredvin.turtlematic.util.toStarbound

class ModEnLanguageProvider(output: PackOutput) : ModLanguageProvider(output, "en_us") {
    override fun addTranslations() {
        add(Items.AUTOMATA_CORE.get(), "Automata core")
        add(Items.HUSBANDRY_AUTOMATA_CORE.get(), "Husbandry automata core")
        add(Items.END_AUTOMATA_CORE.get(), "End automata core")
        add(Items.PROTECTIVE_AUTOMATA_CORE.get(), "Protective automata core")
        add(Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE.get(), "Netherite husbandry automata core")
        add(Items.NETHERITE_END_AUTOMATA_CORE.get(), "Netherite end automata core")
        add(Items.NETHERITE_PROTECTIVE_AUTOMATA_CORE.get(), "Netherite protective automata core")
        add(Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE.get(), "Starbound husbandry automata core")
        add(Items.STARBOUND_END_AUTOMATA_CORE.get(), "Starbound end automata core")
        add(Items.STARBOUND_PROTECTIVE_AUTOMATA_CORE.get(), "Starbound protective automata core")
        add(Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE.get(), "Creative husbandry automata core")
        add(Items.CREATIVE_END_AUTOMATA_CORE.get(), "Creative end automata core")
        add(Items.CREATIVE_PROTECTIVE_AUTOMATA_CORE.get(), "Creative protective automata core")
        add(Items.ENORMOUS_AUTOMATA_CORE.get(), "Enormous automata core")

        add(Items.FORGED_AUTOMATA_CORE.get(), "Forged automata core", "§5It seems too empty for use it now. Feed it with villager soul first")


        add(Items.BREWING_AUTOMATA_CORE.get(), "Brewing automata core")
        add(Items.SMITHING_AUTOMATA_CORE.get(), "Smithing automata core")
        add(Items.ENCHANTING_AUTOMATA_CORE.get(), "Enchanting automata core")
        add(Items.MASON_AUTOMATA_CORE.get(), "Mason automata core")
        add(Items.MERCANTILE_AUTOMATA_CORE.get(), "Mercantile automata core")

        add(Items.STARBOUND_BREWING_AUTOMATA_CORE.get(), "Starbound brewing automata core")
        add(Items.STARBOUND_SMITHING_AUTOMATA_CORE.get(), "Starbound smithing automata core")
        add(Items.STARBOUND_ENCHANTING_AUTOMATA_CORE.get(), "Starbound enchanting automata core")
        add(Items.STARBOUND_MASON_AUTOMATA_CORE.get(), "Starbound mason automata core")
        add(Items.STARBOUND_MERCANTILE_AUTOMATA_CORE.get(), "Starbound mercantile automata core")

        add(Items.CREATIVE_BREWING_AUTOMATA_CORE.get(), "Creative brewing automata core")
        add(Items.CREATIVE_SMITHING_AUTOMATA_CORE.get(), "Creative smithing automata core")
        add(Items.CREATIVE_ENCHANTING_AUTOMATA_CORE.get(), "Creative enchanting automata core")
        add(Items.CREATIVE_MASON_AUTOMATA_CORE.get(), "Creative mason automata core")
        add(Items.CREATIVE_MERCANTILE_AUTOMATA_CORE.get(), "Creative mercantile automata core")

        add(Items.SOUL_VIAL.get(), "Soul Vial", "§7Storage for raw souls. Click on soul sand to fill it")
        add(Items.FILLED_SOUL_VIAL.get(), "Filled soul vial", "§5You can hear strange noises from this vial ...")
        add(Items.SOUL_SCRAPPER.get(), "Soul scrapper", "§5Mysterious device, that somehow allow turtle to imprison souls into specific automata cores")
        add(Items.TURTLE_CHATTER.get(), "Turtle chatter", "§7Strange device that allows turtle to create crude chat bubbles")
        add(Items.CREATIVE_CHEST.get(), "Creative chest", "§7Creative item, that allows turtle to generate items")
        add(Items.CHUNK_VIAL.get(), "Chunk vial", "§7With this vial, turtle will always load its chunk")

        add(ModText.CREATIVE_TAB, "Turtlematic")
        add(ModText.CORE_FEED_BY_PLAYER, "You just can't force yourself to feed a soul to this core")

        add(ModTooltip.CONSUMED_ENTITIES, "Consumed:")
        add(ModTooltip.CONSUMED_ENTITIES_RECORD, "  %s/%s of %s")
        add(ModTooltip.CORE_CONFIGURATION, "Core details:")
        add(ModTooltip.INTERACTION_RADIUS, "  Interaction radius: %s")
        add(ModTooltip.MAX_FUEL_CONSUMPTION_RATE, "  Max fuel consumption rate: %s")
        add(ModTooltip.COOLDOWN_REDUCE_FACTOR, "  Cooldown reduce factor: %s")
        add(ModTooltip.PRESS_FOR_RECIPE, "[§3Left ctrl§r] show recipe")

        add(ModTooltip.ITEM_DISABLED, "§4Item disabled in configuration")
        add(ModTooltip.RECIPE_MISSING, "§4Recipe missing for some reason")
        add(ModTooltip.SOUL_UPGRADE_FROM, "  Upgrade from: %s")
        add(ModTooltip.REQUIRED_SOULS, "  Required souls: %s")
        add(ModTooltip.DURABILITY_REFUND_CHANCE, "  §6§nItem has chance to not loose durability on use")
        add(ModTooltip.DURABILITY_REFUND, "  §6§nItem will not loose durability on use")
        add(ModTooltip.STARBOUND_GENERATION, "  §6§nPeriodically generate small amount of fuel points")
        add(ModTooltip.ENCHANTMENT_WIPE_CHANCE, "  §6§nChance %d%%§6§n to lost one enchantment on extract")
        add(ModTooltip.ENCHANTMENT_NO_WIPE, "  §6§nEnchantment extracting always perfect")
        add(ModTooltip.ENCHANTMENT_TREASURE_ALLOWED, "  §6§nTreasure enchantment can be applied too")
        add(ModTooltip.FUEL_CONSUMPTION_DISABLED, "  §6§nFuel consumption disabled")
        add(ModTooltip.SINGLE_GROWN, "  §6§nPeriodically accelerate grown of random crop in interaction radius")
        add(ModTooltip.AREA_GROWN, "  §6§nPeriodically accelerate grown of all crops in interaction radius")
        add(ModTooltip.CAN_DISABLE_ANIMAL_AI, "  §6§nCan disable animals AI")
        add(ModTooltip.CAN_DISABLE_HOSTILE_AI, "  §6§nCan disable hostile mobs AI")
        add(ModTooltip.HAS_TRADE_ABILITIES, "  §6§nCan see and perform merchant trades")
        add(ModTooltip.CAN_RESTORE_TRADES, "  §6§nCan restock villager trades")

        add(ModTooltip.SOUL_VIAL_PROGRESS, "§5Already %s from %s souls stored")

        addTurtle(AutomataCorePeripheral.UPGRADE_ID, "Automata")
        addTurtle(HusbandryAutomataCorePeripheral.UPGRADE_ID, "Husbandry Automata")
        addTurtle(EndAutomataCorePeripheral.UPGRADE_ID, "End Automata")
        addTurtle(ProtectiveAutomataCorePeripheral.UPGRADE_ID, "Protective Automata")

        addTurtle(HusbandryAutomataCorePeripheral.UPGRADE_ID.toNetherite(), "Netherite Husbandry Automata")
        addTurtle(EndAutomataCorePeripheral.UPGRADE_ID.toNetherite(), "Netherite End Automata")
        addTurtle(ProtectiveAutomataCorePeripheral.UPGRADE_ID.toNetherite(), "Netherite Protective Automata")

        addTurtle(HusbandryAutomataCorePeripheral.UPGRADE_ID.toStarbound(), "Starbound Husbandry Automata")
        addTurtle(EndAutomataCorePeripheral.UPGRADE_ID.toStarbound(), "Starbound End Automata")
        addTurtle(ProtectiveAutomataCorePeripheral.UPGRADE_ID.toStarbound(), "Starbound Protective Automata")

        addTurtle(HusbandryAutomataCorePeripheral.UPGRADE_ID.toCreative(), "Creative Husbandry Automata")
        addTurtle(EndAutomataCorePeripheral.UPGRADE_ID.toCreative(), "Creative End automata")
        addTurtle(ProtectiveAutomataCorePeripheral.UPGRADE_ID.toCreative(), "Creative Protective automata")

        addTurtle(EnormousAutomataCorePeripheral.UPGRADE_ID, "Enormous Automata")

        addTurtle(BrewingAutomataCorePeripheral.UPGRADE_ID, "Brewing Automata")
        addTurtle(SmithingAutomataCorePeripheral.UPGRADE_ID, "Smithing Automata")
        addTurtle(EnchantingAutomataCorePeripheral.UPGRADE_ID, "Enchanting Automata")
        addTurtle(MasonAutomataCorePeripheral.UPGRADE_ID, "Mason Automata")
        addTurtle(MercantileAutomataCorePeripheral.UPGRADE_ID, "Mercantile Automata")

        addTurtle(BrewingAutomataCorePeripheral.UPGRADE_ID.toStarbound(), "Starbound Brewing Automata")
        addTurtle(SmithingAutomataCorePeripheral.UPGRADE_ID.toStarbound(), "Starbound Smithing Automata")
        addTurtle(EnchantingAutomataCorePeripheral.UPGRADE_ID.toStarbound(), "Starbound Enchanting Automata")
        addTurtle(MasonAutomataCorePeripheral.UPGRADE_ID.toStarbound(), "Starbound Mason Automata")
        addTurtle(MercantileAutomataCorePeripheral.UPGRADE_ID.toStarbound(), "Starbound Mercantile Automata")

        addTurtle(BrewingAutomataCorePeripheral.UPGRADE_ID.toCreative(), "Creative Brewing Automata")
        addTurtle(SmithingAutomataCorePeripheral.UPGRADE_ID.toCreative(), "Creative Smithing Automata")
        addTurtle(EnchantingAutomataCorePeripheral.UPGRADE_ID.toCreative(), "Creative Enchanting Automata")
        addTurtle(MasonAutomataCorePeripheral.UPGRADE_ID.toCreative(), "Creative Mason Automata")
        addTurtle(MercantileAutomataCorePeripheral.UPGRADE_ID.toCreative(), "Creative Mercantile Automata")

        addTurtle(SoulScrapperPeripheral.UPGRADE_ID, "Soul scrapping")
        addTurtle(TurtleChatterPeripheral.UPGRADE_ID, "Chatting")
        addTurtle(CreativeChestPeripheral.UPGRADE_ID, "Creativity")
        addTurtle(PistonPeripheral.UPGRADE_ID, "Piston")
        addTurtle(StickyPistonPeripheral.UPGRADE_ID, "Sticky Piston")
        addTurtle(LavaBucketPeripheral.UPGRADE_ID, "Trashing")
        addTurtle(ChunkVialPeripheral.UPGRADE_ID, "Loading")
        addTurtle(BowPeripheral.UPGRADE_ID, "Shooting")
    }
}