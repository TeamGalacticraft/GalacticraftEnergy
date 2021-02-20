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

package com.hrznstudio.galacticraft.energy.internal.compat.tr;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
import com.hrznstudio.galacticraft.energy.api.Capacitor;
import com.hrznstudio.galacticraft.energy.api.EnergyTransferable;
import com.hrznstudio.galacticraft.energy.api.EnergyType;
import com.hrznstudio.galacticraft.energy.compat.tr.TREnergyType;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.EnergyHandler;

public class TREnergyWrapper implements Capacitor, EnergyTransferable {
    private final EnergyHandler handler;

    public TREnergyWrapper(EnergyHandler handler) {
        this.handler = handler;
    }

    @Override
    public void setEnergy(int amount) {
        this.handler.set(amount);
    }

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

    @Override
    public int tryExtract(EnergyType type, int amount, Simulation simulation) {
        return ((int) this.handler.extract(amount));
    }

    @Override
    public int tryInsert(EnergyType type, int amount, Simulation simulation) {
        return amount - ((int) this.handler.insert(amount));
    }
}
