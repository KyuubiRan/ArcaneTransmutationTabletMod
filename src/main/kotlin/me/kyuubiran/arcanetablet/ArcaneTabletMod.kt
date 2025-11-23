package me.kyuubiran.arcanetablet

import me.kyuubiran.arcanetablet.client.ATClientSetup
import me.kyuubiran.arcanetablet.datagen.ATDataGen
import me.kyuubiran.arcanetablet.item.ATModItems
import me.kyuubiran.arcanetablet.menu.ATModMenus
import me.kyuubiran.arcanetablet.network.EmcSyncHandler
import me.kyuubiran.arcanetablet.network.NetworkHandler
import moze_intel.projecte.gameObjs.registries.PECreativeTabs
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.IModBusEvent
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * Main mod class.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(ArcaneTabletMod.ID)
object ArcaneTabletMod : IModBusEvent {
    const val ID = "arcanetablet"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        ATModItems.REGISTRY.register(MOD_BUS)
        ATModMenus.REGISTRY.register(MOD_BUS)

        MOD_BUS.register(NetworkHandler)
        MOD_BUS.register(ATDataGen)
        MOD_BUS.register(ATClientSetup)

        FORGE_BUS.addListener(EmcSyncHandler::onServerTick)
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        LOGGER.log(Level.INFO, "Hello! This is working!")
    }

    @SubscribeEvent
    fun onCreateTabEvent(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey == PECreativeTabs.PROJECTE.get()) {
            event.accept(ATModItems.ARCANE_TABLET)
        }
    }
}
