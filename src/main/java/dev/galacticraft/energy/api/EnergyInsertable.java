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

public interface EnergyInsertable {
    /**
     * Inserts energy into this {@link EnergyInsertable energy insertable}
     *
     * @param type       The type of energy to insert
     * @param amount     The amount of energy in the specified energy type to insert
     * @param simulation Whether to perform the action or just simulate it
     * @return The amount of energy that could not be inserted
     */
    int attemptInsertion(EnergyType type, int amount, Simulation simulation);

    /**
     * Tests if this {@link EnergyInsertable energy insertable} will fully accept the given amount of energy
     * It does not actually modify the {@link EnergyInsertable energy insertable}
     * @param type The type of energy being inserted
     * @param amount The amount of energy being inserted
     * @return whether or not this {@link EnergyInsertable energy insertable} will fully accept the energy
     */
    default boolean wouldAccept(EnergyType type, int amount) {
        return this.attemptInsertion(type, amount, Simulation.SIMULATE) == 0;
    }

    default EnergyInsertable getPureInsertable() {
        return new EnergyInsertable() {
            @Override
            public int attemptInsertion(EnergyType type, int amount, Simulation simulation) {
                return EnergyInsertable.this.attemptInsertion(type, amount, simulation);
            }

            @Override
            public EnergyInsertable getPureInsertable() {
                return this;
            }
        };
    }
}
