package site.siredvin.turtlematic.common.configuration

import net.neoforged.neoforge.common.ModConfigSpec
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig.CommonConfig

object ConfigHolder {
    var commonConfigSpec: ModConfigSpec
    var commonConfig: CommonConfig

    init {
        val (key, value) = ModConfigSpec.Builder()
            .configure { builder: ModConfigSpec.Builder -> CommonConfig(builder) }
        commonConfig = key
        commonConfigSpec = value
    }
}
