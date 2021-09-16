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

package dev.galacticraft.energy.internal.compatibility.tr.tr_gc;

import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import dev.galacticraft.energy.GalacticraftEnergy;
import dev.galacticraft.energy.compatibility.tr.TREnergyType;
import dev.galacticraft.energy.internal.compatibility.CompatibilityEnergyWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public record BlockExtractableTRWrapper(World world, BlockPos pos) implements EnergyStorage, CompatibilityEnergyWrapper {

    @Override
    public double getStored(EnergySide energySide) {
        return GalacticraftEnergy.EXTRACTABLE.getFirst(this.world, this.pos, SearchOptions.inDirection(Direction.values()[energySide.ordinal()])).attemptExtraction(TREnergyType.INSTANCE, 1024, Simulation.SIMULATE);
    }

    @Override
    public void setStored(double v) {
        GalacticraftEnergy.EXTRACTABLE.getFirst(this.world, this.pos).attemptExtraction(TREnergyType.INSTANCE, (int) v, Simulation.ACTION);
    }

    @Override
    public double getMaxStoredPower() {
        return this.getStored(EnergySide.UNKNOWN);
    }

    @Override
    public EnergyTier getTier() {
        return EnergyTier.INFINITE;
    }

    @Override
    public double getMaxInput(EnergySide side) {
        return 0;
    }
}
