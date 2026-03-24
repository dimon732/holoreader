package com.example.holoreader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class HoloReaderClient implements ClientModInitializer {

    private static final List<String> nearbyHolograms = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;

            nearbyHolograms.clear();

            client.world.getEntitiesByClass(
                ArmorStandEntity.class,
                client.player.getBoundingBox().expand(10),
                e -> e.isInvisible() && e.isCustomNameVisible()
            ).forEach(e -> {
                Text name = e.getCustomName();
                if (name != null) {
                    nearbyHolograms.add(name.getString());
                }
            });

            int y = 10;
            for (String line : nearbyHolograms) {
                drawContext.drawText(
                    client.textRenderer,
                    line,
                    10, y, 0x00FF00, true
                );
                y += 12;
            }
        });
    }
}
