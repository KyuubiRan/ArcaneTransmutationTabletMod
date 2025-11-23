package me.kyuubiran.arcanetablet.client

import me.kyuubiran.arcanetablet.client.gui.screen.ArcaneTabletScreen
import me.kyuubiran.arcanetablet.menu.ATModMenus
import me.kyuubiran.arcanetablet.menu.ArcaneTabletMenu
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

internal object ATClientSetup {

    @SubscribeEvent
    fun init(event: RegisterMenuScreensEvent) {
        event.register(ATModMenus.ARCANE_TABLET_MENU.get()) { menu: ArcaneTabletMenu, inv, _ ->
            ArcaneTabletScreen(menu, inv)
        }
    }
}