package io.github.yuko1101.neoportals.portal

import io.github.yuko1101.neoportals.NeoPortals
import org.bukkit.Location
import org.bukkit.entity.ItemDisplay

object PortalManager {
    private val portals = mutableMapOf<Location, Portal>()

    fun loadAll() {
        val worlds = NeoPortals.instance.server.worlds
        for (w in worlds) {
            val portalEntities = w.getEntitiesByClass(ItemDisplay::class.java)
            for (e in portalEntities) {
                val portal = Portal(e)
                portals[portal.location] = portal
            }
        }
    }

    fun destroyAll(shouldBreak: Boolean) {
        for (portal in portals.values) {
            portal.destroy(shouldBreak)
        }
    }

    fun getPortal(location: Location): Portal? {
        return portals[location]
    }

    fun getPortals(): List<Portal> {
        return portals.values.toList()
    }

    // TODO: consider entity hitbox instead of distanceLimit
    fun getNearestPortal(location: Location, distanceLimit: Double = Double.MAX_VALUE): Portal? {
        var nearestPortal: Portal? = null
        var nearestDistance = distanceLimit

        val portals = this.portals.filter { it.value.location.world == location.world }
        for (portal in portals.values) {
            val distance = portal.location.distance(location)
            if (distance < nearestDistance) {
                nearestPortal = portal
                nearestDistance = distance
            }
        }
        return nearestPortal
    }

    fun addPortal(portal: Portal) {
        portals[portal.location] = portal
    }

    fun removePortal(portal: Portal) {
        if (portals.containsValue(portal)) portals.remove(portal.location)
    }
}