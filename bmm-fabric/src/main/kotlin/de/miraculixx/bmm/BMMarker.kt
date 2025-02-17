package de.miraculixx.bmm

import de.miraculixx.bmm.api.Loader
import de.miraculixx.bmm.map.MarkerManager
import de.miraculixx.bmm.utils.message.cmp
import de.miraculixx.bmm.utils.message.consoleAudience
import de.miraculixx.bmm.utils.message.plus
import de.miraculixx.bmm.utils.message.prefix
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.kyori.adventure.platform.fabric.FabricServerAudiences
import net.minecraft.server.MinecraftServer
import java.io.File


class BMMarker : ModInitializer {
    private lateinit var blueMapInstance: BlueMap
    private lateinit var config: File

    override fun onInitialize() {
        MarkerCommand()
        ServerLifecycleEvents.SERVER_STARTING.register(ServerLifecycleEvents.ServerStarting { server: MinecraftServer? ->
            val adventure = FabricServerAudiences.of(server!!)
            consoleAudience = adventure.console()
            config = File("config/bm-marker")
            if (!config.exists()) config.mkdirs()
            blueMapInstance = BlueMap(config, Loader.FABRIC, server.serverVersion)
        })

        ServerLifecycleEvents.SERVER_STOPPED.register(ServerLifecycleEvents.ServerStopped {
            blueMapInstance.disable()
            MarkerManager.saveAllMarker(config)
            consoleAudience.sendMessage(prefix + cmp("Successfully saved all data! Good Bye :)"))
        })
    }
}