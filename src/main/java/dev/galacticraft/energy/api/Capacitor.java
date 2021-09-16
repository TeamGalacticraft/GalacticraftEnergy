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

import alexiil.mc.lib.attributes.Convertible;
import alexiil.mc.lib.attributes.ListenerRemovalToken;
import alexiil.mc.lib.attributes.ListenerToken;
import alexiil.mc.lib.attributes.Simulation;
import dev.galacticraft.energy.impl.CapacitorWrapper;
import org.jetbrains.annotations.Nullable;

public interface Capacitor extends CapacitorView, Convertible, EnergyTransferable {
    /**
     * Sets the amount of energy in this capacitor
     *
     * @param amount The amount of energy to set
     */
    void setEnergy(int amount);

    /**
     * Inserts energy from this capacitor.
     *
     * @param amount     The amount of energy in the capacitor's native energy type to extract
     * @param simulation Whether to perform the action or just simulate it
     * @return The amount of energy that could not be inserted (in the specified energy type)
     * @see #getEnergyType()
     */
    default int insert(int amount, Simulation simulation) {
        int insertable = Math.min(this.getMaxCapacity() - this.getEnergy(), amount);
        if (simulation.isAction()) this.setEnergy(this.getEnergy() + insertable);
        return amount - insertable;
    }

    /**
     * Inserts energy from this capacitor.
     *
     * @param type       The type of energy to insert
     * @param amount     The amount of energy in the specified energy type to insert
     * @param simulation Whether to perform the action or just simulate it
     * @return The amount of energy that could not be inserted (in the specified energy type)
     * @see #insert(int, Simulation)
     */
    default int insert(EnergyType type, int amount, Simulation simulation) {
        return type.convertFrom(this.getEnergyType(), insert(type.convertTo(this.getEnergyType(), amount), simulation));
    }

    /**
     * Inserts energy from this capacitor.
     *
     * @param type   The type of energy to insert
     * @param amount The amount of energy in the specified energy type to insert
     * @return The amount of energy that could not be inserted (in the specified energy type)
     * @see #insert(int, Simulation)
     */
    default int insert(EnergyType type, int amount) {
        return type.convertFrom(this.getEnergyType(), insert(type.convertTo(this.getEnergyType(), amount), Simulation.ACTION));
    }

    /**
     * Inserts energy from this capacitor.
     *
     * @param amount The amount of energy in the capacitor's native energy type to insert
     * @return The amount of energy that could not be inserted
     * @see #insert(int, Simulation)
     * @see #getEnergyType()
     */
    default int insert(int amount) {
        return insert(amount, Simulation.ACTION);
    }

    /**
     * Extracts energy from this capacitor.
     *
     * @param amount     The amount of energy in the capacitor's native energy type to extract
     * @param simulation Whether to perform the action or just simulate it
     * @return The amount of energy that was extracted (in the specified energy type)
     * @see #getEnergyType()
     */
    default int extract(int amount, Simulation simulation) {
        amount = Math.min(this.getEnergy(), amount);
        if (simulation.isAction()) this.setEnergy(this.getEnergy() - amount);
        return amount;
    }

    /**
     * Extracts energy from this capacitor.
     *
     * @param type       The type of energy to extract
     * @param amount     The amount of energy in the specified energy type to extract
     * @param simulation Whether to perform the action or just simulate it
     * @return The amount of energy that was extracted (in the specified energy type)
     * @see #extract(int, Simulation)
     */
    default int extract(EnergyType type, int amount, Simulation simulation) {
        return type.convertFrom(this.getEnergyType(), extract(type.convertTo(this.getEnergyType(), amount), simulation));
    }

    /**
     * Extracts energy from this capacitor.
     *
     * @param type   The type of energy to extract
     * @param amount The amount of energy in the specified energy type to extract
     * @return The amount of energy that was extracted (in the specified energy type)
     * @see #extract(int, Simulation)
     */
    default int extract(EnergyType type, int amount) {
        return type.convertFrom(this.getEnergyType(), extract(type.convertTo(this.getEnergyType(), amount), Simulation.ACTION));
    }

    default int attemptInsertion(EnergyType type, int amount, Simulation simulation) {
        return this.insert(type, amount, simulation);
    }

    default int attemptExtraction(EnergyType type, int amount, Simulation simulation) {
        return this.extract(type, amount, simulation);
    }

    /**
     * Extracts energy from this capacitor.
     *
     * @param amount The amount of energy in the capacitor's native energy type to extract
     * @return The amount of energy that was extracted
     * @see #extract(int, Simulation)
     * @see #getEnergyType()
     */
    default int extract(int amount) {
        return extract(amount, Simulation.ACTION);
    }

    default EnergyTransferable getTransferable() {
        return new CapacitorWrapper(this);
    }

    default EnergyInsertable getInsertable() {
        return this.getTransferable();
    }

    default EnergyExtractable getExtractable() {
        return this.getTransferable();
    }

    default CapacitorView createView() {
        return new CapacitorView() {
            @Override
            public EnergyType getEnergyType() {
                return Capacitor.this.getEnergyType();
            }

            @Override
            public int getEnergy() {
                return Capacitor.this.getEnergy();
            }

            @Override
            public int getMaxCapacity() {
                return Capacitor.this.getMaxCapacity();
            }

            @Override
            public @Nullable ListenerToken addListener(CapacitorListener listener, ListenerRemovalToken removalToken) {
                return Capacitor.this.addListener(listener, removalToken);
            }
        };
    }

    @Override
    default <T> T convertTo(Class<T> otherType) {
        return Convertible.offer(otherType, this.getTransferable());
    }
}
