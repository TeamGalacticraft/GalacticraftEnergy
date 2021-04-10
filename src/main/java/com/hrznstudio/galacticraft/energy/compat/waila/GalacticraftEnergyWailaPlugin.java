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

package com.hrznstudio.galacticraft.energy.compat.waila;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import alexiil.mc.lib.attributes.AttributeProviderBlockEntity;
import com.hrznstudio.galacticraft.energy.GalacticraftEnergy;
import com.hrznstudio.galacticraft.energy.api.CapacitorView;
import com.hrznstudio.galacticraft.energy.impl.DefaultEnergyType;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.mobius.waila.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

public class GalacticraftEnergyWailaPlugin implements IWailaPlugin {
    private static final Identifier TEXTURE = new Identifier("galacticraft-energy", "textures/gui/waila_energy_overlay.png");
    private static final IComponentProvider COMPONENT_PROVIDER = new IComponentProvider() {
        @Override
        public void appendBody(List<Text> tooltip, IDataAccessor accessor, IPluginConfig config) {
            if (GalacticraftEnergy.CAPACITOR_VIEW.getFirstOrNull(accessor.getWorld(), accessor.getPosition()) != null) {
                tooltip.add(new RenderableTextComponent(new Identifier("galacticraft-energy", "tooltip_renderer"), accessor.getServerData()));
            }
        }
    };
    private static void appendData(CompoundTag data, ServerPlayerEntity player, World world, BlockEntity blockEntity) {
        AttributeList<CapacitorView> list = GalacticraftEnergy.CAPACITOR_VIEW.getAll(world, blockEntity.getPos());
        if (list.getCount() > 0) {
            int[] energyLevels = new int[list.getCount() * 2];
            for (int i = 0; i < list.getCount(); i += 2) {
                energyLevels[i] = list.get(i).getEnergyAs(DefaultEnergyType.INSTANCE);
                energyLevels[i + 1] = list.get(i).getMaxCapacityAs(DefaultEnergyType.INSTANCE);
            }
            data.putIntArray("gc_energy", energyLevels);
        }
    }

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerBlockDataProvider(GalacticraftEnergyWailaPlugin::appendData, AttributeProvider.class);
        registrar.registerBlockDataProvider(GalacticraftEnergyWailaPlugin::appendData, AttributeProviderBlockEntity.class);

        registrar.registerComponentProvider(COMPONENT_PROVIDER, TooltipPosition.BODY, AttributeProvider.class);
        registrar.registerComponentProvider(COMPONENT_PROVIDER, TooltipPosition.BODY, AttributeProviderBlockEntity.class);

        registrar.registerTooltipRenderer(new Identifier("galacticraft-energy", "tooltip_renderer"), new ITooltipRenderer() {
            @Override
            public Dimension getSize(CompoundTag data, ICommonAccessor accessor) {
                return new Dimension(data.getIntArray("gc_energy").length > 0 ? 128 : 0, (data.getIntArray("gc_energy").length / 2) * 18);
            }

            @Override
            public void draw(MatrixStack matrices, CompoundTag data, ICommonAccessor accessor, int x, int y) {
                int[] energyLevels = data.getIntArray("gc_energy");
                for (int i = 0; i < energyLevels.length; i += 2) {
                    MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
                    DrawableHelper.drawTexture(matrices, x + 1, y + 1 + i * 18, 0, 0, 0, 128, 16, 128, 128);
                    DrawableHelper.drawTexture(matrices, x + 1, y + 1 + i * 18, 0, 0, 16, (int) (128 * (double) energyLevels[i] / (double) energyLevels[i + 1]), 16, 128, 128);

                    MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, DefaultEnergyType.INSTANCE.display(energyLevels[i]).append(" / ").append(DefaultEnergyType.INSTANCE.display(energyLevels[i + 1])), x + 5, y + 4, Formatting.WHITE.getColorValue());
                }
            }
        });
    }
}
