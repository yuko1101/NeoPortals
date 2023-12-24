package io.github.yuko1101.neoportals.listener

import io.github.yuko1101.neoportals.portal.Portal
import io.github.yuko1101.neoportals.portal.PortalManager
import org.bukkit.Material
import org.bukkit.block.data.Directional
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFormEvent

object EventListener : Listener {
    @EventHandler
    fun onBlockChange(event: BlockFormEvent) {
        val block = event.block
        if (block.blockData.material != Material.NETHER_PORTAL) return
        val newBlockFace = (event.newState.blockData as Directional).facing
        val portal: Portal? = PortalManager.getPortal(block.location)
        if (portal != null) {
            if (portal.blockFace != newBlockFace || event.newState.type != Material.NETHER_PORTAL) {
                portal.destroy(shouldBreak = event.newState.type.isAir)
            }
        }
    }
}