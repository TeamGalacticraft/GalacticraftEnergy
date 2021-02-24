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

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

/**
 * Each energy type is a unit of energy.
 * They should all be able to be converted to {@link com.hrznstudio.galacticraft.energy.impl.DefaultEnergyType}
 * using the methods {@link #convertFromDefault(int)} and {@link #convertToDefault(int)}
 */
public interface EnergyType {
    default MutableText display(int amount) {
        return new LiteralText(String.valueOf(amount));
    }

    /**
     * Converts energy from {@code type} to this energy type
     *
     * @param type   The type of energy to convert from
     * @param amount The amount of energy to convert
     * @return The amount of energy that was converted
     * @see #convertFromDefault(int)
     */
    default int convertFrom(EnergyType type, int amount) {
        if (type == this) return amount;
        return this.convertFromDefault(type.convertToDefault(amount));
    }

    /**
     * Converts energy from this energy type to {@code type}
     *
     * @param type   The type of energy to convert to
     * @param amount The amount of energy to convert
     * @return The amount of energy that was converted
     * @see #convertToDefault(int)
     */
    default int convertTo(EnergyType type, int amount) {
        if (type == this) return amount;
        return type.convertFromDefault(this.convertToDefault(amount));
    }

    /**
     * Converts energy to {@link com.hrznstudio.galacticraft.energy.impl.DefaultEnergyType}
     *
     * @param amount The amount of energy to convert
     * @return The amount of energy that was converted
     * @see com.hrznstudio.galacticraft.energy.impl.DefaultEnergyType
     */
    int convertToDefault(int amount);

    /**
     * Converts energy from {@link com.hrznstudio.galacticraft.energy.impl.DefaultEnergyType} to this energy type
     *
     * @param amount The amount of energy to convert
     * @return The amount of energy that was converted
     * @see com.hrznstudio.galacticraft.energy.impl.DefaultEnergyType
     */
    int convertFromDefault(int amount);
}
