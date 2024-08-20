package site.siredvin.turtlematic.xplat

import site.siredvin.peripheralium.xplat.BaseInnerPlatform
import site.siredvin.peripheralium.xplat.BasePlatform
import site.siredvin.peripheralium.xplat.ModInformationTracker

object ModPlatform : BasePlatform {
    private var impl: BaseInnerPlatform? = null
    private val informationTracker = ModInformationTracker()

    fun configure(impl: BaseInnerPlatform) {
        this.impl = impl
    }

    override val baseInnerPlatform: BaseInnerPlatform
        get() {
            if (impl == null) {
                throw IllegalStateException("You should configure upw ModPlatform first")
            }
            return impl!!
        }

    override val modInformationTracker: ModInformationTracker
        get() = informationTracker
}
