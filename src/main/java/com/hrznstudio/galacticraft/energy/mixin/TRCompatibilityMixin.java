package com.hrznstudio.galacticraft.energy.mixin;

import alexiil.mc.lib.attributes.ItemAttributeAdder;
import com.hrznstudio.galacticraft.energy.GalacticraftEnergy;
import com.hrznstudio.galacticraft.energy.internal.compat.tr.TREnergyWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.reborn.energy.Energy;

@Mixin(value = GalacticraftEnergy.class, remap = false)
public abstract class TRCompatibilityMixin {
    @Inject(method = "onInitialize", at = @At("RETURN"))
    private void addTRCompatibility(CallbackInfo info) {
        GalacticraftEnergy.CAPACITOR.appendItemAdder(createTRAdder());
        GalacticraftEnergy.CAPACITOR_VIEW.appendItemAdder(createTRAdder());
        GalacticraftEnergy.INSERTABLE.appendItemAdder(createTRAdder());
        GalacticraftEnergy.EXTRACTABLE.appendItemAdder(createTRAdder());
    }

    private @Unique <T> ItemAttributeAdder<T> createTRAdder() {
        return (reference, limitedConsumer, itemAttributeList) -> {
            if (Energy.valid(reference.get())) {
                itemAttributeList.offer(new TREnergyWrapper(Energy.of(reference.get())));
            }
        };
    }
}
