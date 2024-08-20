package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig.CommonConfig

object ConfigHolder {
    var commonConfigSpec: ForgeConfigSpec
    var commonConfig: CommonConfig

    init {
        val (key, value) = ForgeConfigSpec.Builder()
            .configure { builder: ForgeConfigSpec.Builder -> CommonConfig(builder) }
        commonConfig = key
        commonConfigSpec = value
    }
}
