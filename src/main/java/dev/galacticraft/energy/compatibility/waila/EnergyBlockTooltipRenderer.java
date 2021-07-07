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

import dev.galacticraft.energy.impl.DefaultEnergyType;
import dev.galacticraft.energy.internal.Constant;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.awt.*;

public class EnergyBlockTooltipRenderer implements ITooltipRenderer {
    private static final Identifier TEXTURE = new Identifier(Constant.MOD_ID, "textures/gui/waila_energy_overlay.png");
    public static final EnergyBlockTooltipRenderer INSTANCE = new EnergyBlockTooltipRenderer();

    private EnergyBlockTooltipRenderer() {}

    @Override
    public Dimension getSize(NbtCompound nbt, ICommonAccessor iCommonAccessor) {
        return new Dimension(nbt.getIntArray("GCEnergy").length != 0 ? 128 : 0, (nbt.getIntArray("GCEnergy").length / 2) * 18);
    }

    @Override
    public void draw(MatrixStack matrices, NbtCompound nbt, ICommonAccessor iCommonAccessor, int x, int y) {
        int[] energyLevels = nbt.getIntArray("gc_energy");
        for (int i = 0; i < energyLevels.length; i += 2) {
            MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
            DrawableHelper.drawTexture(matrices, x + 1, y + 1 + i * 18, 0, 0, 0, 128, 16, 128, 128);
            DrawableHelper.drawTexture(matrices, x + 1, y + 1 + i * 18, 0, 0, 16, (int) (128 * (double) energyLevels[i] / (double) energyLevels[i + 1]), 16, 128, 128);

            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, DefaultEnergyType.INSTANCE.display(energyLevels[i]).append(" / ").append(DefaultEnergyType.INSTANCE.display(energyLevels[i + 1])), x + 5, y + 4, Formatting.WHITE.getColorValue());
        }
    }
}
