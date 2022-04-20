package site.siredvin.lib.operations

import java.io.Serializable

class SphereOperationContext(val radius: Int) : Serializable {

    companion object {
        fun of(radius: Int): SphereOperationContext {
            return SphereOperationContext(radius)
        }
    }
}