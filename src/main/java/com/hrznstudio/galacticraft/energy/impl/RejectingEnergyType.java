package com.hrznstudio.galacticraft.energy.impl;

import com.hrznstudio.galacticraft.energy.api.EnergyType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

public enum RejectingEnergyType implements EnergyType {
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
