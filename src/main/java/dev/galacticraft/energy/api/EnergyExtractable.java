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

package dev.galacticraft.energy.api;

import alexiil.mc.lib.attributes.Simulation;
import dev.galacticraft.energy.impl.DefaultEnergyType;

public interface EnergyExtractable {
    /**
     * Extracts energy from this {@link EnergyExtractable}.
     * @param type       The type of energy to extract
     * @param amount     The amount of energy in the specified energy type to extract
     * @param simulation Whether to perform the action or just simulate it
     * @return The amount of energy that was extracted (in the specified energy type)
     */
    int attemptExtraction(EnergyType type, int amount, Simulation simulation);

    /**
     * Extracts energy from this {@link EnergyExtractable}.
     * @param type       The type of energy to extract
     * @param amount     The amount of energy in the specified energy type to extract
     * @return The amount of energy that was extracted (in the specified energy type)
     */
    default int extract(EnergyType type, int amount) {
        return this.attemptExtraction(type, amount, Simulation.ACTION);
    }

    /**
     * Extracts energy from this {@link EnergyExtractable}.
     * @param amount     The amount of energy in the specified energy type to extract
     * @return The amount of energy that was extracted (in the default energy type [gJ])
     */
    default int extract(int amount) {
        return this.attemptExtraction(DefaultEnergyType.INSTANCE, amount, Simulation.ACTION);
    }

    default boolean couldExtractAnything() {
        return this.attemptExtraction(DefaultEnergyType.INSTANCE, 1, Simulation.SIMULATE) == 1;
    }

    default EnergyExtractable getPureExtractable() {
        return new EnergyExtractable() {
            @Override
            public int attemptExtraction(EnergyType type, int amount, Simulation simulation) {
                return EnergyExtractable.this.getPureExtractable().attemptExtraction(type, amount, simulation);
            }

            @Override
            public EnergyExtractable getPureExtractable() {
                return this;
            }
        };
    }
}
