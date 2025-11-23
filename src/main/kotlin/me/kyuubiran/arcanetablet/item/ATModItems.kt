package me.kyuubiran.arcanetablet.item

import me.kyuubiran.arcanetablet.ArcaneTabletMod
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ATModItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(ArcaneTabletMod.ID)

    val ARCANE_TABLET: DeferredItem<ArcaneTabletItem> = REGISTRY.registerItem("arcane_tablet") { ArcaneTabletItem() }
}