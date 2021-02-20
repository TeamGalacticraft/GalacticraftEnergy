package com.hrznstudio.galacticraft.energy.impl;

import com.hrznstudio.galacticraft.energy.api.EnergyType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

/**
 * Galacticraft Joules (gJ)
 *
 * Reference values:
 * 1 Coal ->
 */
public enum DefaultEnergyType implements EnergyType { //todo: energy values
    INSTANCE;

    @Override
    public MutableText display(int amount) {
        if (amount > 1_000_000) {
            return new LiteralText((round((((double)amount) / 1000.0), 3)) + "G gJ");
        } else if (amount > 10_000) {
            return new LiteralText((round((((double)amount) / 1000.0), 1)) + "K gJ");
        }
        return new LiteralText(amount + " gJ");
    }

    @Override
    public int convertToDefault(int amount) {
        return 0;
    }

    @Override
    public int convertFromDefault(int amount) {
        return 0;
    }

    // https://stackoverflow.com/a/22186845
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
