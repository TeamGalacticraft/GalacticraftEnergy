/*
 * Copyright (c) 2021 Team Galacticraft
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

package dev.galacticraft.energy.internal.mixin;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.ItemAttributeAdder;
import alexiil.mc.lib.attributes.misc.Ref;
import dev.galacticraft.energy.GalacticraftEnergy;
import dev.galacticraft.energy.api.Capacitor;
import dev.galacticraft.energy.api.CapacitorView;
import dev.galacticraft.energy.api.EnergyExtractable;
import dev.galacticraft.energy.api.EnergyInsertable;
import dev.galacticraft.energy.internal.compatibility.CompatibilityEnergyWrapper;
import dev.galacticraft.energy.internal.compatibility.tr.gc_tr.TRCapacitor;
import dev.galacticraft.energy.internal.compatibility.tr.gc_tr.TRCapacitorView;
import dev.galacticraft.energy.internal.compatibility.tr.gc_tr.TREnergyExtractable;
import dev.galacticraft.energy.internal.compatibility.tr.gc_tr.TREnergyInsertable;
import dev.galacticraft.energy.internal.compatibility.tr.CompatibilityLoopBreaker;
import dev.galacticraft.energy.internal.compatibility.tr.WrappedBlockPos;
import dev.galacticraft.energy.internal.compatibility.tr.WrapperReference;
import dev.galacticraft.energy.internal.compatibility.tr.tr_gc.*;
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
import team.reborn.energy.EnergyHandler;

import java.util.Objects;
import java.util.function.Function;

/**
 * Handles the loading of TR energy compatibility code
 */
@Mixin(value = GalacticraftEnergy.class, remap = false)
public abstract class TRCompatibilityMixin {
    @Inject(method = "onInitialize", at = @At("RETURN"))
    private void addTRCompatibility(CallbackInfo info) {
        GalacticraftEnergy.CAPACITOR.appendItemAdder(createTRItemAdder(TRCapacitor::new));
        GalacticraftEnergy.CAPACITOR_VIEW.appendItemAdder(createTRItemAdder(TRCapacitorView::new));
        GalacticraftEnergy.INSERTABLE.appendItemAdder(createTRItemAdder(TREnergyInsertable::new));
        GalacticraftEnergy.EXTRACTABLE.appendItemAdder(createTRItemAdder(TREnergyExtractable::new));
        GalacticraftEnergy.CAPACITOR.appendBlockAdder((world, pos, state, to) -> createTRBlockAdder(world, pos, to, t -> t));
        GalacticraftEnergy.CAPACITOR_VIEW.appendBlockAdder((world, pos, state, to) -> createTRBlockAdder(world, pos, to, Capacitor::createView));
        GalacticraftEnergy.INSERTABLE.appendBlockAdder((world, pos, state, to) -> createTRBlockAdder(world, pos, to, EnergyInsertable::getPureInsertable));
        GalacticraftEnergy.EXTRACTABLE.appendBlockAdder((world, pos, state, to) -> createTRBlockAdder(world, pos, to, EnergyExtractable::getPureExtractable));

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                Capacitor capacitor = GalacticraftEnergy.CAPACITOR.getFirstOrNull((new WrapperReference<>(new Ref<>((ItemStack)i))));
                return capacitor != null && !(capacitor instanceof CompatibilityEnergyWrapper);
            } else if (i instanceof BlockEntity) {
                Capacitor capacitor = GalacticraftEnergy.CAPACITOR.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), new WrappedBlockPos(((BlockEntity) i).getPos()));
                return capacitor != null && !(capacitor instanceof CompatibilityEnergyWrapper);
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
                CapacitorView capView = GalacticraftEnergy.CAPACITOR_VIEW.getFirstOrNull((new WrapperReference<>(new Ref<>((ItemStack)i))));
                return capView != null && !(capView instanceof CompatibilityEnergyWrapper);
            } else if (i instanceof BlockEntity) {
                CapacitorView capView = GalacticraftEnergy.CAPACITOR_VIEW.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), new WrappedBlockPos(((BlockEntity) i).getPos()));
                return capView != null && !(capView instanceof CompatibilityEnergyWrapper);
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
                EnergyInsertable insertable = GalacticraftEnergy.INSERTABLE.getFirstOrNull((new WrapperReference<>(new Ref<>((ItemStack)i))));
                return insertable != null && !(insertable instanceof CompatibilityEnergyWrapper);
            } else if (i instanceof BlockEntity) {
                EnergyInsertable insertable = GalacticraftEnergy.INSERTABLE.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), new WrappedBlockPos(((BlockEntity) i).getPos()));
                return insertable != null && !(insertable instanceof CompatibilityEnergyWrapper);
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemInsertableTRWrapper(GalacticraftEnergy.INSERTABLE.getFirst((ItemStack) o));
            } else if (o instanceof BlockEntity) {
                return new BlockInsertableTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });

        Energy.registerHolder(i -> {
            if (i instanceof ItemStack) {
                EnergyExtractable extractable = GalacticraftEnergy.EXTRACTABLE.getFirstOrNull((new WrapperReference<>(new Ref<>((ItemStack)i))));
                return extractable != null && !(extractable instanceof CompatibilityEnergyWrapper);
            } else if (i instanceof BlockEntity) {
                EnergyExtractable extractable = GalacticraftEnergy.EXTRACTABLE.getFirstOrNull(Objects.requireNonNull(((BlockEntity) i).getWorld()), new WrappedBlockPos(((BlockEntity) i).getPos()));
                return extractable != null && !(extractable instanceof CompatibilityEnergyWrapper);
            }
            return false;
        }, o -> {
            if (o instanceof ItemStack) {
                return new ItemExtractableTRWrapper(GalacticraftEnergy.EXTRACTABLE.getFirst((ItemStack) o));
            } else if (o instanceof BlockEntity) {
                return new BlockExtractableTRWrapper(((BlockEntity) o).getWorld(), ((BlockEntity) o).getPos());
            }
            return null;
        });
    }

    @Unique
    private static <T> void createTRBlockAdder(World world, BlockPos pos, AttributeList<T> to, Function<TRCapacitor, T> function) {
        if (pos instanceof CompatibilityLoopBreaker) return;
        BlockEntity entity = world.getBlockEntity(pos);
        if (Energy.valid(entity)) {
            to.offer(function.apply(new TRCapacitor(Energy.of(entity).side(to.getSearchDirection()))));
        }
    }

    @Unique
    private static <T> ItemAttributeAdder<T> createTRItemAdder(Function<EnergyHandler, T> function) {
        return (reference, limitedConsumer, itemAttributeList) -> {
            if (reference instanceof CompatibilityLoopBreaker) return;
            if (Energy.valid(reference.get())) {
                itemAttributeList.offer(function.apply(Energy.of(reference.get())));
            }
        };
    }
}
