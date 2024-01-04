package io.github.yuko1101.neoportals.listener

import io.github.yuko1101.neoportals.portal.PortalManager
import io.github.yuko1101.neoportals.utils.LocationUtils.teleportWith
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPortalEvent
import org.bukkit.event.player.PlayerPortalEvent
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
    fun onTeleport(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return
        val portal = PortalManager.getNearestPortal(event.from, 2.0)
        if (portal != null) {
            event.isCancelled = true
            event.player.teleportWith(portal)
        }
    }

    @EventHandler
    fun onEntityTeleport(event: EntityPortalEvent) {
        if (event.from.world == null || event.to?.world == null) return
        val isFromNether = event.from.world!!.environment == World.Environment.NETHER
        val isToNether = event.to!!.world!!.environment == World.Environment.NETHER
        val isThroughNetherGate = (!isFromNether && isToNether) || (isFromNether && !isToNether)
        if (!isThroughNetherGate) return

        val portal = PortalManager.getNearestPortal(event.from, 2.0)
        if (portal != null) {
            event.isCancelled = true
            event.entity.teleportWith(portal)
        }
    }
}

