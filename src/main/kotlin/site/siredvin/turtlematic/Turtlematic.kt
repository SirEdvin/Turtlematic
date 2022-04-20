package site.siredvin.turtlematic
import dan200.computercraft.api.ComputerCraftAPI
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.turtle.AutomataCoreTurtleUpgrade


@Suppress("UNUSED")
object Turtlematic: ModInitializer {
    const val MOD_ID = "turtlematic"

    @Suppress("MoveLambdaOutsideParentheses")
    val TAB: ItemGroup = FabricItemGroupBuilder.build(
        Identifier(MOD_ID, "main"),
        { ItemStack(Items.AUTOMATA_CORE) }
    )

    override fun onInitialize() {
        println("Example mod has been initialized.")
        Items.register()
        ComputerCraftAPI.registerTurtleUpgrade(AutomataCoreTurtleUpgrade())
        AutoConfig.register(TurtleModConfig::class.java) { config, clazz -> Toml4jConfigSerializer(config, clazz) }
        val turtleModConfig = AutoConfig.getConfigHolder(TurtleModConfig::class.java)
        turtleModConfig.config.println()
    }
}