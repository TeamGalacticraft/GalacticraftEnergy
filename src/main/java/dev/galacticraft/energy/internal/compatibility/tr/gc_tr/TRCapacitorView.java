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

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import dev.galacticraft.energy.api.CapacitorView;
import dev.galacticraft.energy.api.EnergyType;
import dev.galacticraft.energy.compatibility.tr.TREnergyType;
import dev.galacticraft.energy.internal.compatibility.CompatibilityEnergyWrapper;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergyHandler;

public record TRCapacitorView(EnergyHandler handler) implements CapacitorView, CompatibilityEnergyWrapper {

    @Override
    public EnergyType getEnergyType() {
        return TREnergyType.INSTANCE;
    }

    @Override
    public int getEnergy() {
        return ((int) this.handler.getEnergy());
    }

    @Override
    public int getMaxCapacity() {
        return ((int) this.handler.getMaxStored());
    }

    @Override
    public @Nullable ListenerToken addListener(CapacitorListener listener, ListenerRemovalToken removalToken) {
        return null;
    }
}
