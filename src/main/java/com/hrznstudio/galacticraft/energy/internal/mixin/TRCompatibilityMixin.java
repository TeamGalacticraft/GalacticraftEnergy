/*
 * Copyright (c) 2021 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hrznstudio.galacticraft.energy.internal.mixin;

import alexiil.mc.lib.attributes.ItemAttributeAdder;
import com.hrznstudio.galacticraft.energy.GalacticraftEnergy;
import com.hrznstudio.galacticraft.energy.internal.compat.tr.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.reborn.energy.Energy;

import java.util.Objects;

/**
 * Handles the loading of TR energy compatibility code
 */
@Mixin(value = GalacticraftEnergy.class, remap = false)
public abstract class TRCompatibilityMixin {
    @Inject(method = "onInitialize", at = @At("RETURN"))
    private void addTRCompatibility(CallbackInfo info) {
        GalacticraftEnergy.CAPACITOR.appendItemAdder(createTRAdder());
        GalacticraftEnergy.CAPACITOR_VIEW.appendItemAdder(createTRAdder());
        GalacticraftEnergy.INSERTABLE.appendItemAdder(createTRAdder());
        GalacticraftEnergy.EXTRACTABLE.appendItemAdder(createTRAdder());

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                return GalacticraftEnergy.CAPACITOR.getFirstOrNull((ItemStack) i) != null;
            } else if (i instanceof BlockEntity) {
                return GalacticraftEnergy.CAPACITOR.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), ((BlockEntity) i).getPos()) != null;
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemCapacitorTRWrapper(GalacticraftEnergy.CAPACITOR.getFirst((ItemStack) o));
            } else if (o instanceof BlockEntity) {
                return new BlockCapacitorTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                return GalacticraftEnergy.CAPACITOR_VIEW.getFirstOrNull((ItemStack) i) != null;
            } else if (i instanceof BlockEntity) {
                return GalacticraftEnergy.CAPACITOR_VIEW.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), ((BlockEntity) i).getPos()) != null;
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemCapacitorViewTRWrapper(GalacticraftEnergy.CAPACITOR_VIEW.getFirst((ItemStack) o));
            } else if (o instanceof BlockEntity) {
                return new BlockCapacitorViewTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                return GalacticraftEnergy.INSERTABLE.getFirstOrNull((ItemStack) i) != null;
            } else if (i instanceof BlockEntity) {
                return GalacticraftEnergy.INSERTABLE.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), ((BlockEntity) i).getPos()) != null;
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemCapacitorInsertableTRWrapper(GalacticraftEnergy.INSERTABLE.getFirst((ItemStack) o));
            } else if (o instanceof BlockEntity) {
                return new BlockCapacitorInsertableTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                return GalacticraftEnergy.EXTRACTABLE.getFirstOrNull((ItemStack) i) != null;
            } else if (i instanceof BlockEntity) {
                return GalacticraftEnergy.EXTRACTABLE.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), ((BlockEntity) i).getPos()) != null;
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemCapacitorExtractableTRWrapper(GalacticraftEnergy.EXTRACTABLE.getFirst((ItemStack) o));
            } else if (o instanceof BlockEntity) {
                return new BlockCapacitorExtractableTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });
    }

    private @Unique <T> ItemAttributeAdder<T> createTRAdder() {
        return (reference, limitedConsumer, itemAttributeList) -> {
            if (Energy.valid(reference.get())) {
                itemAttributeList.offer(new TREnergyWrapper(Energy.of(reference.get())));
            }
        };
    }
}
