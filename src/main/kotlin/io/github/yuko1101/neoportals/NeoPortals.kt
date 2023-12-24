package io.github.yuko1101.neoportals

import io.github.yuko1101.neoportals.commands.NeoPortalCommand
import io.github.yuko1101.neoportals.listener.EventListener
import io.github.yuko1101.neoportals.portal.PortalManager
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class NeoPortals : JavaPlugin() {

    companion object {
        lateinit var instance: NeoPortals
            private set
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(EventListener, this)
        instance = this

        PortalManager.loadAll()

        getCommand("neoportals")?.setExecutor(NeoPortalCommand)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}