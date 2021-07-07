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

package dev.galacticraft.energy.compatibility.waila;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.misc.NullVariant;
import dev.galacticraft.energy.GalacticraftEnergy;
import dev.galacticraft.energy.api.CapacitorView;
import dev.galacticraft.energy.internal.compatibility.CompatibilityEnergyWrapper;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class EnergyBlockDataProvider implements IServerDataProvider<BlockEntity> {
    public static final EnergyBlockDataProvider INSTANCE = new EnergyBlockDataProvider();

    private EnergyBlockDataProvider() {}

    @Override
    public void appendServerData(NbtCompound nbt, ServerPlayerEntity player, World world, BlockEntity blockEntity) {
        AttributeList<CapacitorView> list = GalacticraftEnergy.CAPACITOR_VIEW.getAll(world, blockEntity.getPos());
        int j = 0;
        for (int i = 0; i < list.getCount(); i++) {
            CapacitorView view = list.get(i);
            if (!(view instanceof CompatibilityEnergyWrapper) && !(view instanceof NullVariant)) {
                j++;
            }
        }
        int[] out = new int[j * 2];
        j = 0;
        for (int i = 0; i < list.getCount(); i++) {
            CapacitorView view = list.get(i);
            if (!(view instanceof CompatibilityEnergyWrapper) && !(view instanceof NullVariant)) {
                out[j++] = view.getEnergy();
                out[j++] = view.getMaxCapacity();
            }
        }
        nbt.putIntArray("GCEnergy", out);
    }
}
