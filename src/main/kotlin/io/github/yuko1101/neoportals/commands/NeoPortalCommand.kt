package io.github.yuko1101.neoportals.commands

import io.github.yuko1101.neoportals.portal.Portal
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object NeoPortalCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, cmd: Command, name: String, argsArray: Array<out String>): Boolean {
        val args = argsArray.iterator()
        try {
            when (args.next()) {
                "portal" -> {
                    when (args.next()) {
                        "create" -> {
                            val worldName = args.next()
                            val x = args.next().toDouble()
                            val y = args.next().toDouble()
                            val z = args.next().toDouble()
                            val color = args.next().toInt()

                            val world: World? = Bukkit.getWorld(worldName)
                            if (world == null) {
                                sender.sendMessage("§cWorld $worldName not found.")
                                return true
                            }

                            val location = Location(world, x, y, z)

                            val destWorldName = args.next()
                            val destX = args.next().toDouble()
                            val destY = args.next().toDouble()
                            val destZ = args.next().toDouble()

                            val destWorld: World? = Bukkit.getWorld(destWorldName)
                            if (destWorld == null) {
                                sender.sendMessage("§cWorld $destWorldName not found.")
                                return true
                            }

                            val dest = Location(destWorld, destX, destY, destZ)

                            val portal = Portal.create(location, dest, Color.fromRGB(color))
                            sender.sendMessage("§aPortal created at ${location.x}, ${location.y}, ${location.z} in world ${location.world!!.name}")
                        }
                    }
                }
            }
        } catch (e: NoSuchElementException) {
            sender.sendMessage("§cInvalid arguments. Run \"/neoportal help\" for help.")
        }

        return true
    }
}