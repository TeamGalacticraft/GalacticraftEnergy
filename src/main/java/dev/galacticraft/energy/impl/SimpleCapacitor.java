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

package dev.galacticraft.energy.impl;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.misc.Saveable;
import dev.galacticraft.energy.api.Capacitor;
import dev.galacticraft.energy.api.EnergyType;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SimpleCapacitor implements Capacitor, Saveable {
    private final Map<CapacitorListener, ListenerRemovalToken> listeners = new HashMap<>();
    private final EnergyType type;
    private final int capacity;
    private int energy;

    public SimpleCapacitor(EnergyType type, int capacity) {
        this.type = type;
        this.capacity = capacity;
    }

    @Override
    public void setEnergy(int amount) {
        this.energy = amount;
        for (CapacitorListener listener : this.listeners.keySet()) {
            listener.onChanged(this);
        }
    }

    @Override
    public EnergyType getEnergyType() {
        return this.type;
    }

    @Override
    public int getEnergy() {
        return this.energy;
    }

    @Override
    public int getMaxCapacity() {
        return this.capacity;
    }

    @Override
    public @Nullable ListenerToken addListener(CapacitorListener listener, ListenerRemovalToken removalToken) {
        this.listeners.put(listener, removalToken);
        return () -> {
            if (this.listeners.remove(listener) == null) throw new AssertionError();
        };
    }

    @Override
    public NbtCompound toTag(NbtCompound tag) {
        tag.putInt("Energy", energy);
        return tag;
    }

    @Override
    public void fromTag(NbtCompound tag) {
        this.energy = tag.getInt("Energy");
    }
}
