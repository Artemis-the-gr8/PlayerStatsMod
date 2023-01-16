package io.github.artemis_the_gr8.playerstats;

import com.mojang.logging.LogUtils;
import net.minecraft.stats.Stats;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(PlayerStats.MOD_ID)
public class PlayerStats {

    public static final String MOD_ID = "playerstats";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public PlayerStats() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        LOGGER.info("Setting up PlayerStats" );
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
//        AtomicInteger i = new AtomicInteger();
//        Stats.CUSTOM.iterator().forEachRemaining(resourceLocationStat -> LOGGER.info(i.getAndIncrement() + ". " + resourceLocationStat.getName()));
//        ForgeRegistries.ENTITIES.getEntries().forEach(resourceKeyEntityTypeEntry ->
//                LOGGER.info(i.getAndIncrement() + ". " + resourceKeyEntityTypeEntry.getValue().toString()));
    }

    @Mod.EventBusSubscriber(modid = PlayerStats.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ModEventListener {

        @SubscribeEvent
        public static void registerCommands(@NotNull RegisterCommandsEvent event) {
            TestCommand.register(event.getDispatcher());
        }
    }
}