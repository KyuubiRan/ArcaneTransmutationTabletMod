package me.kyuubiran.arcanetablet.datagen

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.data.event.GatherDataEvent

object ATDataGen {

    @SubscribeEvent
    fun onGenerateEvent(event: GatherDataEvent) {
        val generator = event.generator
        val packOutput = generator.packOutput
        val existingFileHelper = event.existingFileHelper

        generator.addProvider(event.includeServer(), ATModItemModelProvider(packOutput, existingFileHelper))
    }
}