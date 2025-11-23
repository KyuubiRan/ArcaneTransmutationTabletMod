package me.kyuubiran.arcanetablet.datagen

import me.kyuubiran.arcanetablet.ArcaneTabletMod
import me.kyuubiran.arcanetablet.item.ATModItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ATModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(
    output,
    ArcaneTabletMod.ID, existingFileHelper
) {

    override fun registerModels() {
        basicItem(ATModItems.ARCANE_TABLET.get())
    }
}