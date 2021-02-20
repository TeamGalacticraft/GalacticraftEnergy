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

package com.hrznstudio.galacticraft.energy;

import alexiil.mc.lib.attributes.Attributes;
import alexiil.mc.lib.attributes.DefaultedAttribute;
import com.hrznstudio.galacticraft.energy.api.*;
import com.hrznstudio.galacticraft.energy.impl.EmptyCapacitor;
import com.hrznstudio.galacticraft.energy.impl.EmptyEnergyExtractable;
import com.hrznstudio.galacticraft.energy.impl.RejectingEnergyInsertable;
import net.fabricmc.api.ModInitializer;

/**
 * Contains all the attributes for this mod.
 */
public class GalacticraftEnergy implements ModInitializer {
    /**
     * Capacitor attribute
     * Allows getting max and current energy levels and setting the amount stored
     */
    public static final DefaultedAttribute<Capacitor> CAPACITOR = Attributes.createDefaulted(Capacitor.class, EmptyCapacitor.NULL);

    /**
     * Capacitor view attribute
     * Allows getting max and current energy levels
     */
    public static final DefaultedAttribute<CapacitorView> CAPACITOR_VIEW = Attributes.createDefaulted(CapacitorView.class, EmptyCapacitor.NULL);

    /**
     * Energy Extractable attribute
     * Allows extracting energy
     */
    public static final DefaultedAttribute<EnergyExtractable> EXTRACTABLE = Attributes.createDefaulted(EnergyExtractable.class, EmptyEnergyExtractable.NULL);

    /**
     * Energy Extractable attribute
     * Allows inserting energy
     */
    public static final DefaultedAttribute<EnergyInsertable> INSERTABLE = Attributes.createDefaulted(EnergyInsertable.class, RejectingEnergyInsertable.NULL);

    @Override
    public void onInitialize() {}
}

