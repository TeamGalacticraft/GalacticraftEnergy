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

package com.hrznstudio.galacticraft.energy.impl;

import alexiil.mc.lib.attributes.misc.NullVariant;
import com.hrznstudio.galacticraft.energy.api.EnergyType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

/**
 * An energy type that will always return {@code zero} when converted.
 */
public enum EmptyEnergyType implements EnergyType, NullVariant {
    INSTANCE;

    @Override
    public MutableText display(int amount) {
        return new LiteralText("null");
    }

    @Override
    public int convertFrom(EnergyType type, int amount) {
        return 0;
    }

    @Override
    public int convertTo(EnergyType type, int amount) {
        return 0;
    }

    @Override
    public int convertToDefault(int amount) {
        return 0;
    }

    @Override
    public int convertFromDefault(int amount) {
        return 0;
    }
}
