package io.github.yuko1101.neoportals.listener

import io.github.yuko1101.neoportals.portal.Portal.Companion.getDestination
import io.github.yuko1101.neoportals.portal.PortalManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.scheduler.BukkitRunnable

object EventListener : BukkitRunnable(), Listener {

    override fun run() {
        val portals = PortalManager.getPortals()
        for (portal in portals) {
            if (portal.initialized) {
                portal.initialized = true
                continue
            }
            val block = portal.location.block
            // TODO: Check block face
            if (block.type != Material.NETHER_PORTAL) {
                portal.destroy(shouldBreak = block.type.isAir)
                println("Portal destroyed")
                continue
            }
        }
    }

    @EventHandler
    fun onTeleport(event: PlayerTeleportEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return
        val portal = PortalManager.getNearestPortal(event.from, 2.0)
        if (portal != null) {
            event.isCancelled = true
            portal.itemDisplay.getDestination()?.let { event.player.teleport(it) }
        }
    }
}