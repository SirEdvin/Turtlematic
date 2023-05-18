package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig.CommonConfig

object ConfigHolder {
    var COMMON_SPEC: ForgeConfigSpec
    var COMMON_CONFIG: CommonConfig

    init {
        val (key, value) = ForgeConfigSpec.Builder()
            .configure { builder: ForgeConfigSpec.Builder -> CommonConfig(builder) }
        COMMON_CONFIG = key
        COMMON_SPEC = value
    }
}