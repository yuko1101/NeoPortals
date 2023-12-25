package io.github.yuko1101.neoportals.portal

import io.github.yuko1101.neoportals.NeoPortals
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.persistence.PersistentDataType
import kotlin.math.floor

class Portal(val itemDisplay: ItemDisplay) {
    val location: Location = itemDisplay.location
    val blockFace = itemDisplay.facing
    var initialized = false

    fun destroy(shouldBreak: Boolean) {
        val block = location.block
        if (block.blockData.material == Material.NETHER_PORTAL && shouldBreak) block.breakNaturally()
        itemDisplay.remove()
        PortalManager.removePortal(this)
    }

    companion object {
        fun create(location: Location, destination: Location, color: Color, adjustLoc: Boolean = true): Portal {
            if (adjustLoc) {
                location.adjustLoc()
                destination.adjustLoc()
            }

            if (location.world == null) throw IllegalArgumentException("Location must have world")
            val itemDisplay = location.world!!.spawn(location, ItemDisplay::class.java)
            itemDisplay.itemStack = getPortalItemStack(color)
            itemDisplay.setDestination(destination)

            location.world!!.getBlockAt(location).type = Material.NETHER_PORTAL

            val portal = Portal(itemDisplay)
            PortalManager.addPortal(portal)
            return portal
        }

        fun getPortalItemStack(color: Color): ItemStack {
            val itemStack = ItemStack(Material.LEATHER_HORSE_ARMOR)
            val meta = itemStack.itemMeta!! as LeatherArmorMeta
            meta.setColor(color)
            meta.setCustomModelData(3213)
            itemStack.itemMeta = meta
            return itemStack
        }

        fun ItemDisplay.setDestination(destination: Location) {
            val container = persistentDataContainer.adapterContext.newPersistentDataContainer()
            container.set(NamespacedKey(NeoPortals.instance, "world"), PersistentDataType.STRING, destination.world!!.name)
            container.set(NamespacedKey(NeoPortals.instance, "x"), PersistentDataType.DOUBLE, destination.x)
            container.set(NamespacedKey(NeoPortals.instance, "y"), PersistentDataType.DOUBLE, destination.y)
            container.set(NamespacedKey(NeoPortals.instance, "z"), PersistentDataType.DOUBLE, destination.z)
            persistentDataContainer.set(NamespacedKey(NeoPortals.instance, "destination"), PersistentDataType.TAG_CONTAINER, container)
        }

        fun ItemDisplay.getDestination(): Location? {
            val container =
                persistentDataContainer.get(NamespacedKey(NeoPortals.instance, "destination"), PersistentDataType.TAG_CONTAINER)
                    ?: return null
            val worldName = container.get(NamespacedKey(NeoPortals.instance, "world"), PersistentDataType.STRING)
            val x = container.get(NamespacedKey(NeoPortals.instance, "x"), PersistentDataType.DOUBLE)
            val y = container.get(NamespacedKey(NeoPortals.instance, "y"), PersistentDataType.DOUBLE)
            val z = container.get(NamespacedKey(NeoPortals.instance, "z"), PersistentDataType.DOUBLE)
            if (worldName == null || x == null || y == null || z == null) return null
            return Location(NeoPortals.instance.server.getWorld(worldName), x, y, z)
        }

        fun Location.adjustLoc(): Location {
            x = floor(x) + 0.5
            y = floor(y) + 0.5
            z = floor(z) + 0.5
            return this
        }
    }

}