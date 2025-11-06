package com.itarqos.selenium.client;

import com.itarqos.selenium.config.SeleniumConfig;
import com.itarqos.selenium.render.RenderTaskScheduler;
import com.itarqos.selenium.util.SeleniumLog;
import com.itarqos.selenium.util.FrameBudgetController;
import com.itarqos.selenium.util.CullingStats;
import com.itarqos.selenium.util.MovementPredictor;
import com.itarqos.selenium.world.SubIdentifierManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.rendering.v1.InvalidateRenderStateCallback;

public class SeleniumClient implements ClientModInitializer {
    public static SeleniumConfig CONFIG;

    @Override
    public void onInitializeClient() {
        // Load config once on client init
        CONFIG = SeleniumConfig.load();
        SeleniumLog.setVerbose(CONFIG.verboseLogging);
        SeleniumLog.info("Client initialized. Config loaded: entityCulling=%s, chunkCulling=%s, renderScheduler=%s",
                CONFIG.enableEntityCulling, CONFIG.enableChunkCulling, CONFIG.enableRenderScheduler);
        SeleniumLog.debug("Verbose logging enabled - detailed performance metrics will be logged");

        // HUD overlay for FPS and (conditional) counters
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            var mc = MinecraftClient.getInstance();
            if (mc == null || mc.textRenderer == null) return;

            boolean debug = mc.getDebugHud() != null && mc.getDebugHud().shouldShowDebugHud();
            boolean showCounters = (CONFIG != null && CONFIG.enableDebugMode) || debug;

            // Optional simple overlay
            if (CONFIG != null && CONFIG.showCullingOverlay) {
                int fps = mc.getCurrentFps();
                String msg = "FPS: " + fps;
                int x = 4;
                int y = 4;
                int sw = context.getScaledWindowWidth();
                int sh = context.getScaledWindowHeight();
                int tw = mc.textRenderer.getWidth(msg);
                int th = 10; // approx line height

                SeleniumConfig.OverlayPosition pos = CONFIG.overlayPosition;
                if (pos != null) {
                    switch (pos) {
                        case TOP_LEFT -> { x = 4; y = 4; }
                        case TOP_RIGHT -> { x = sw - tw - 4; y = 4; }
                        case BOTTOM_LEFT -> { x = 4; y = sh - th - 4; }
                        case BOTTOM_RIGHT -> { x = sw - tw - 4; y = sh - th - 4; }
                        case CENTER -> { x = (sw - tw) / 2; y = (sh - th) / 2; }
                    }
                }
                context.drawTextWithShadow(mc.textRenderer, msg, x, y, 0xFFFFFF);
            }

            // F3 (debug) badge: top-right, light green
            if (debug) {
                String badge = "Optimized by Selenium";
                int w = context.getScaledWindowWidth();
                int x = w - mc.textRenderer.getWidth(badge) - 4;
                int y = 4; // top-right corner avoids main debug left column
                context.drawTextWithShadow(mc.textRenderer, badge, x, y, 0x55FF55);
            }

            // Show counters only in F3 debug, unless Debug Mode forces them always-on
            if (showCounters) {
                int x = 4;
                // Stack below either FPS (if shown) or start at top-left if not
                boolean overlayTopLeft = CONFIG != null && CONFIG.showCullingOverlay && CONFIG.overlayPosition == SeleniumConfig.OverlayPosition.TOP_LEFT;
                int yStart = overlayTopLeft ? 4 + 12 : 4;
                int line = yStart;
                context.drawTextWithShadow(mc.textRenderer, "Entities Culled: " + com.itarqos.selenium.util.CullingStats.getEntitiesCulled(), x, line, 0xA0FFA0);
                line += 10;
                context.drawTextWithShadow(mc.textRenderer, "BlockEntities Culled: " + com.itarqos.selenium.util.CullingStats.getBlockEntitiesCulled(), x, line, 0xA0FFA0);
                line += 10;
                context.drawTextWithShadow(mc.textRenderer, "Slices Flushed: " + com.itarqos.selenium.util.CullingStats.getSlicesFlushed(), x, line, 0xA0FFA0);
                line += 10;
                context.drawTextWithShadow(mc.textRenderer, "Slices Debounced: " + com.itarqos.selenium.util.CullingStats.getSlicesDebounced(), x, line, 0xA0FFA0);
                line += 10;
                context.drawTextWithShadow(mc.textRenderer, "Slices Skipped Empty: " + com.itarqos.selenium.util.CullingStats.getSlicesSkippedEmpty(), x, line, 0xA0FFA0);
                line += 10;
                if (CONFIG != null && CONFIG.enableRenderScheduler) {
                    context.drawTextWithShadow(mc.textRenderer, "Render Tasks Queued: " + RenderTaskScheduler.get().getQueuedTaskCount(), x, line, 0xA0FFA0);
                }
            }
        });

        // Reset counter each client tick so value represents per-tick culls
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Update movement predictor with current frame timing
            try {
                float td = 0.0f;
                if (client != null && client.getRenderTickCounter() != null) {
                    td = client.getRenderTickCounter().getTickDelta(false);
                }
                MovementPredictor.get().update(td);
                SeleniumLog.debug("Movement predictor updated with tick delta: %.3f", td);
            } catch (Throwable t) {
                SeleniumLog.error("Movement predictor update failed", t);
            }
            CullingStats.reset();
            // Flush any dirty subidentifier slices that became visible this tick
            SubIdentifierManager.get().flushVisible();
            
            // End frame for render scheduler
            if (CONFIG != null && CONFIG.enableRenderScheduler) {
                RenderTaskScheduler.get().endFrame();
            }
        });
        
        // Render scheduler frame hooks
        WorldRenderEvents.START.register(context -> {
            // Begin frame timing for QoS controller
            FrameBudgetController.get().beginFrame();
            if (CONFIG != null && CONFIG.enableRenderScheduler) {
                RenderTaskScheduler.get().beginFrame();
                SeleniumLog.debug("Render frame started");
            }
        });
        
        WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
            if (CONFIG != null && CONFIG.enableRenderScheduler) {
                RenderTaskScheduler.get().processTasks();
                SeleniumLog.debug("Processing render tasks before entities");
            }
        });

        // Clear sub-identifier state when the world renderer is invalidated (F3+A, resource reload, options changes)
        InvalidateRenderStateCallback.EVENT.register(() -> {
            SubIdentifierManager.get().onWorldReset();
            if (CONFIG != null && CONFIG.enableRenderScheduler) {
                RenderTaskScheduler.get().clear();
                SeleniumLog.info("Render state invalidated - cleared scheduler tasks");
            }
        });

        // Purge per-chunk state when a chunk unloads on the client
        ClientChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
            if (chunk != null) {
                SubIdentifierManager.get().onChunkUnload(chunk.getPos());
                SeleniumLog.debug("Chunk unloaded at %s", chunk.getPos());
            }
        });
    }

    public static void saveConfig() {
        if (CONFIG != null) {
            SeleniumConfig.save(CONFIG);
        }
    }
}
