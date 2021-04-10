package com.hrznstudio.galacticraft.energy.internal.compat.tr.gc_tr;

import alexiil.mc.lib.attributes.Simulation;
import com.hrznstudio.galacticraft.energy.api.EnergyExtractable;
import com.hrznstudio.galacticraft.energy.api.EnergyInsertable;
import com.hrznstudio.galacticraft.energy.api.EnergyTransferable;
import com.hrznstudio.galacticraft.energy.api.EnergyType;
import com.hrznstudio.galacticraft.energy.compat.tr.TREnergyType;
import com.hrznstudio.galacticraft.energy.internal.compat.CompatEnergy;
import team.reborn.energy.EnergyHandler;

public class TREnergyTransferable implements EnergyTransferable, CompatEnergy {
    private final EnergyHandler handler;

    public TREnergyTransferable(EnergyHandler handler) {
        this.handler = handler;
    }

    @Override
    public int tryExtract(EnergyType type, int amount, Simulation simulation) {
        return type.convertFrom(TREnergyType.INSTANCE, (int) (simulation.isSimulate() ? this.handler.simulate() : this.handler).extract(TREnergyType.INSTANCE.convertFrom(type, amount)));
    }

    @Override
    public EnergyExtractable asPureExtractable() {
        return new TREnergyExtractable(this.handler);
    }

    @Override
    public int tryInsert(EnergyType type, int amount, Simulation simulation) {
        return type.convertFrom(TREnergyType.INSTANCE, (int) (simulation.isSimulate() ? this.handler.simulate() : this.handler).insert(TREnergyType.INSTANCE.convertFrom(type, amount)));
    }

    @Override
    public EnergyInsertable asPureInsertable() {
        return new TREnergyInsertable(this.handler);
    }
}
