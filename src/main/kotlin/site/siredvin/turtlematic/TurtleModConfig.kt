package site.siredvin.turtlematic

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import site.siredvin.turtlematic.Turtlematic
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded
import java.util.HashMap

@Config(name = Turtlematic.MOD_ID)
class TurtleModConfig : ConfigData {
    @Excluded
    var test: Map<String, String> = buildMap {
        put("x1", "x2")
        put("x2", "x3")
    }

    fun println() {
        println(test)
    }
}