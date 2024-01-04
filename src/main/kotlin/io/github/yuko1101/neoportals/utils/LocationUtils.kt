package io.github.yuko1101.neoportals.utils

import io.github.yuko1101.neoportals.portal.Portal
import io.github.yuko1101.neoportals.portal.Portal.Companion.getDestination
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity

object LocationUtils {
    fun Entity.teleportWith(portal: Portal) {
        portal.itemDisplay.getDestination()?.let {
            val to = deltaLocation(this.location, portal.location, it, portal.blockFace)
            to.pitch = this.location.pitch
            to.yaw = this.location.yaw
            this.teleport(to)
        }
    }

    fun deltaLocation(from: Location, portalLoc: Location, dest: Location, direction: BlockFace): Location {
        val delta = from - portalLoc

        if (direction.modX != 0) {
            delta.x = 0.0
        } else if (direction.modZ != 0) {
            delta.z = 0.0
        }

        val to = dest + Location(dest.world, delta.x, delta.y, delta.z)

        return to
    }


    private operator fun Location.minus(by: Location): Location {
        if (this.world != by.world) throw IllegalArgumentException("Locations must have same world")
        return Location(this.world, this.x - by.x, this.y - by.y, this.z - by.z)
    }

    private operator fun Location.plus(by: Location): Location {
        if (this.world != by.world) throw IllegalArgumentException("Locations must have same world")
        return Location(this.world, this.x + by.x, this.y + by.y, this.z + by.z)
    }
}