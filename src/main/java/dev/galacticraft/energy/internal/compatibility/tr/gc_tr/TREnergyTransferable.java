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

package dev.galacticraft.energy.internal.compatibility.tr.gc_tr;

import alexiil.mc.lib.attributes.Simulation;
import dev.galacticraft.energy.api.EnergyExtractable;
import dev.galacticraft.energy.api.EnergyInsertable;
import dev.galacticraft.energy.api.EnergyTransferable;
import dev.galacticraft.energy.api.EnergyType;
import dev.galacticraft.energy.compatibility.tr.TREnergyType;
import dev.galacticraft.energy.internal.compatibility.CompatibilityEnergyWrapper;
import team.reborn.energy.EnergyHandler;

public record TREnergyTransferable(EnergyHandler handler) implements EnergyTransferable, CompatibilityEnergyWrapper {

    @Override
    public int attemptExtraction(EnergyType type, int amount, Simulation simulation) {
        return type.convertFrom(TREnergyType.INSTANCE, (int) (simulation.isSimulate() ? this.handler.simulate() : this.handler).extract(TREnergyType.INSTANCE.convertFrom(type, amount)));
    }

    @Override
    public EnergyExtractable getPureExtractable() {
        return new TREnergyExtractable(this.handler);
    }

    @Override
    public int attemptInsertion(EnergyType type, int amount, Simulation simulation) {
        return type.convertFrom(TREnergyType.INSTANCE, (int) (simulation.isSimulate() ? this.handler.simulate() : this.handler).insert(TREnergyType.INSTANCE.convertFrom(type, amount)));
    }

    @Override
    public EnergyInsertable getPureInsertable() {
        return new TREnergyInsertable(this.handler);
    }
}
