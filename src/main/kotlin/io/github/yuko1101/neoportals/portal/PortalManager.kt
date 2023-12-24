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

    fun addPortal(portal: Portal) {
        portals[portal.location] = portal
    }

    fun removePortal(portal: Portal) {
        if (portals.containsValue(portal)) portals.remove(portal.location)
    }
}