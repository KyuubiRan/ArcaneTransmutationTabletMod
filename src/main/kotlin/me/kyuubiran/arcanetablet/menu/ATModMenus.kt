package me.kyuubiran.arcanetablet.menu

import me.kyuubiran.arcanetablet.ArcaneTabletMod
import me.kyuubiran.arcanetablet.menu.ArcaneTabletMenu
import net.minecraft.core.registries.Registries
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ATModMenus {
    val REGISTRY: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, ArcaneTabletMod.ID)

    val ARCANE_TABLET_MENU: DeferredHolder<MenuType<*>?, MenuType<ArcaneTabletMenu?>?> =
        REGISTRY.register("arcane_tablet_menu", Supplier {
            IMenuTypeExtension.create { id, inv, _ ->
                ArcaneTabletMenu(id, inv)
            }
        })
}