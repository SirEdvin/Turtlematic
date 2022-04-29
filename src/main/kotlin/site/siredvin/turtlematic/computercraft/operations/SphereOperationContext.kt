package site.siredvin.turtlematic.computercraft.operations

import java.io.Serializable

class SphereOperationContext(val radius: Int) : Serializable {

    companion object {
        fun of(radius: Int): SphereOperationContext {
            return SphereOperationContext(radius)
        }
    }
}