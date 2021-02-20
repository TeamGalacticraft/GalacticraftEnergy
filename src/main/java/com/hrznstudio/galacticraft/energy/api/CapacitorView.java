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

package com.hrznstudio.galacticraft.energy.api;

import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import org.jetbrains.annotations.Nullable;

public interface CapacitorView {
    /**
     * @return The energy type of this capacitor
     * @see EnergyType
     */
    EnergyType getEnergyType();

    /**
     * Returns the amount of energy in this capacitor
     * @return The amount of energy in this capacitor
     * @see #getEnergyType()
     */
    int getEnergy();

    /**
     * Returns the amount of energy in the capacitor in {@code type} units
     * @param type The type of energy to convert to
     * @return The amount of energy in this capacitor in {@code type} units
     * @see #getEnergy()
     */
    default int getEnergyAs(EnergyType type) {
        return this.getEnergyType().convertTo(type, this.getEnergy());
    }

    /**
     * Returns the maximum amount of energy in this capacitor
     * @return The maximum amount of energy in this capacitor
     * @see #getEnergyType()
     */
    int getMaxCapacity();

    /**
     * Returns the maximum amount of energy in this capacitor in {@code type} units
     * @param type The type of energy to convert to
     * @return The maximum amount of energy in this capacitor in {@code type} units
     * @see #getMaxCapacity()
     */
    default int getMaxCapacityAs(EnergyType type) {
        return this.getEnergyType().convertTo(type, this.getMaxCapacity());
    }

    /**
     * Adds a listener that will be fired when the capacitor's energy value is changed
     * @param listener The listener to add
     * @param removalToken A token that will be fired when the listener is removed from the capacitor
     * @return A token that represents the listener, or null if the listener could not be added.
     */
    @Nullable ListenerToken addListener(CapacitorListener listener, ListenerRemovalToken removalToken);

    /**
     * A listener for the Capacitor
     */
    interface CapacitorListener {
        void onChanged(CapacitorView capacitorView);
    }
}
