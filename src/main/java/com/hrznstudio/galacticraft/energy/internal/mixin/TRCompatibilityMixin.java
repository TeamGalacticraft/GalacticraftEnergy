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

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.ItemAttributeAdder;
import alexiil.mc.lib.attributes.misc.Ref;
import com.hrznstudio.galacticraft.energy.GalacticraftEnergy;
import com.hrznstudio.galacticraft.energy.api.EnergyExtractable;
import com.hrznstudio.galacticraft.energy.api.EnergyInsertable;
import com.hrznstudio.galacticraft.energy.internal.BreakingWrapperReference;
import com.hrznstudio.galacticraft.energy.internal.WrapperBlockPos;
import com.hrznstudio.galacticraft.energy.internal.compat.tr.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.reborn.energy.Energy;

import java.util.Objects;
import java.util.function.Function;

/**
 * Handles the loading of TR energy compatibility code
 */
@Mixin(value = GalacticraftEnergy.class, remap = false)
public abstract class TRCompatibilityMixin {
    @Inject(method = "onInitialize", at = @At("RETURN"))
    private void addTRCompatibility(CallbackInfo info) {
        GalacticraftEnergy.CAPACITOR.appendItemAdder(createTRItemAdder(t -> t));
        GalacticraftEnergy.CAPACITOR_VIEW.appendItemAdder(createTRItemAdder(t -> t));
        GalacticraftEnergy.INSERTABLE.appendItemAdder(createTRItemAdder(EnergyInsertable::asPureInsertable));
        GalacticraftEnergy.EXTRACTABLE.appendItemAdder(createTRItemAdder(EnergyExtractable::asPureExtractable));
        GalacticraftEnergy.CAPACITOR.appendBlockAdder((world, pos, state, to) -> createTRBlockAdder(world, pos, to, t -> t));
        GalacticraftEnergy.CAPACITOR_VIEW.appendBlockAdder((world, pos, state, to) -> createTRBlockAdder(world, pos, to, t -> t));
        GalacticraftEnergy.INSERTABLE.appendBlockAdder((world, pos, state, to) -> createTRBlockAdder(world, pos, to, EnergyInsertable::asPureInsertable));
        GalacticraftEnergy.EXTRACTABLE.appendBlockAdder((world, pos, state, to) -> createTRBlockAdder(world, pos, to, EnergyExtractable::asPureExtractable));

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                return GalacticraftEnergy.CAPACITOR.getFirstOrNull(new BreakingWrapperReference<>(new Ref<>((ItemStack) i))) != null;
            } else if (i instanceof BlockEntity) {
                return GalacticraftEnergy.CAPACITOR.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), ((BlockEntity) i).getPos()) != null;
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemCapacitorTRWrapper(GalacticraftEnergy.CAPACITOR.getFirst(new BreakingWrapperReference<>(new Ref<>((ItemStack) o))));
            } else if (o instanceof BlockEntity) {
                return new BlockCapacitorTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                return GalacticraftEnergy.CAPACITOR_VIEW.getFirstOrNull(new BreakingWrapperReference<>(new Ref<>((ItemStack) i))) != null;
            } else if (i instanceof BlockEntity) {
                return GalacticraftEnergy.CAPACITOR_VIEW.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), ((BlockEntity) i).getPos()) != null;
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemCapacitorViewTRWrapper(GalacticraftEnergy.CAPACITOR_VIEW.getFirst(new BreakingWrapperReference<>(new Ref<>((ItemStack) o))));
            } else if (o instanceof BlockEntity) {
                return new BlockCapacitorViewTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                return GalacticraftEnergy.INSERTABLE.getFirstOrNull(new BreakingWrapperReference<>(new Ref<>((ItemStack) i))) != null;
            } else if (i instanceof BlockEntity) {
                return GalacticraftEnergy.INSERTABLE.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), ((BlockEntity) i).getPos()) != null;
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemCapacitorInsertableTRWrapper(GalacticraftEnergy.INSERTABLE.getFirst(new BreakingWrapperReference<>(new Ref<>((ItemStack) o))));
            } else if (o instanceof BlockEntity) {
                return new BlockCapacitorInsertableTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                return GalacticraftEnergy.EXTRACTABLE.getFirstOrNull(new BreakingWrapperReference<>(new Ref<>((ItemStack) i))) != null;
            } else if (i instanceof BlockEntity) {
                return GalacticraftEnergy.EXTRACTABLE.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), ((BlockEntity) i).getPos()) != null;
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemCapacitorExtractableTRWrapper(GalacticraftEnergy.EXTRACTABLE.getFirst(new BreakingWrapperReference<>(new Ref<>((ItemStack) o))));
            } else if (o instanceof BlockEntity) {
                return new BlockCapacitorExtractableTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });
    }

    @Unique
    private static <T> void createTRBlockAdder(World world, BlockPos pos, AttributeList<T> to, Function<TREnergyWrapper, T> function) {
        if (pos instanceof WrapperBlockPos) return;
        BlockEntity entity = world.getBlockEntity(pos);
        if (Energy.valid(entity)) {
            to.offer(function.apply(new TREnergyWrapper(Energy.of(entity).side(to.getSearchDirection()))));
        }
    }

    @Unique
    private static <T> ItemAttributeAdder<T> createTRItemAdder(Function<TREnergyWrapper, T> function) {
        return (reference, limitedConsumer, itemAttributeList) -> {
            if (reference instanceof BreakingWrapperReference) return;
            if (Energy.valid(reference.get())) {
                itemAttributeList.offer(function.apply(new TREnergyWrapper(Energy.of(reference.get()))));
            }
        };
    }
}
