package com.hrznstudio.galacticraft.energy.internal.compat.tr.gc_tr;

import alexiil.mc.lib.attributes.Simulation;
import com.hrznstudio.galacticraft.energy.api.EnergyInsertable;
import com.hrznstudio.galacticraft.energy.api.EnergyType;
import com.hrznstudio.galacticraft.energy.compat.tr.TREnergyType;
import com.hrznstudio.galacticraft.energy.internal.compat.CompatEnergy;
import team.reborn.energy.EnergyHandler;

public class TREnergyInsertable implements EnergyInsertable, CompatEnergy {
    private final EnergyHandler handler;

    public TREnergyInsertable(EnergyHandler handler) {
        this.handler = handler;
    }

    @Override
    public int tryInsert(EnergyType type, int amount, Simulation simulation) {
        return type.convertFrom(TREnergyType.INSTANCE, (int) (simulation.isSimulate() ? this.handler.simulate() : this.handler).insert(TREnergyType.INSTANCE.convertFrom(type, amount)));
    }

    @Override
    public EnergyInsertable asPureInsertable() {
        return this;
    }
}
