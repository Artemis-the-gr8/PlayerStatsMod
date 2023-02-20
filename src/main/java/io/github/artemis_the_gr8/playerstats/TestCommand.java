package io.github.artemis_the_gr8.playerstats;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;


public class TestCommand {

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(getStatCommandBuilder());
//        dispatcher.register(Commands.literal("playerstats")
//                .then(Commands.literal("minecraft:broken")
//                        .then(Commands.argument("tools", ResourceKeyArgument.key(ForgeRegistries.ITEMS.getRegistryKey()))
//                        .executes(this::execute)))
//                .then(Commands.literal("minecraft:killed")
//                        .then(Commands.argument("entities", ResourceKeyArgument.key(ForgeRegistries.ENTITIES.getRegistryKey()))
//                        .executes(this::entityCommand)))
//                .then(Commands.argument("stats", ResourceKeyArgument.key(ForgeRegistries.STAT_TYPES.getRegistryKey()))
//                        .executes(this::execute)));
    }

    private LiteralArgumentBuilder<CommandSourceStack> getStatCommandBuilder() {
        LiteralArgumentBuilder<CommandSourceStack> statCommand = Commands.literal("playerstats");
//        statCommand.then(Commands.argument("stat_type", ResourceKeyArgument.key(ForgeRegistries.STAT_TYPES.getRegistryKey())));

        for (StatType<?> statType : ForgeRegistries.STAT_TYPES) {
            if (statType == Stats.CUSTOM) {
                //TODO make a RequiredArgumentBuilder here, and do statCommand.then() after the if/else

                statCommand.then(Commands.argument("custom", ResourceKeyArgument.key(Stats.CUSTOM.getRegistry().key()))
                        .executes(this::execute));
            } else {
                statCommand.then(Commands.literal(statType.getRegistryName().toString())
                        .then(Commands.argument("stat_type", ResourceKeyArgument.key(statType.getRegistry().key()))
                                .executes(this::execute)));
            }
        }

        return statCommand;
    }

    private int entityCommand(CommandContext<CommandSourceStack> command) {
        if (command.getSource().getEntity() instanceof Player player) {
            player.sendMessage(new TextComponent("last child: " + command.getLastChild().toString()), Util.NIL_UUID);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

    private String getTranslationKey(Stat<ResourceLocation> pStat) {
        return "stat." + pStat.getValue().toString().replace(':', '.');
    }

    private int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        if(command.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            try {
                serverPlayer.sendMessage(new TextComponent("input: " + command.getInput()), Util.NIL_UUID);

                ResourceKey<?> customStat = command.getArgument("custom", ResourceKey.class);
                if (customStat != null) {

                    ResourceLocation location = Registry.CUSTOM_STAT.get(customStat.location());
                    if (location != null && ResourceLocation.isValidResourceLocation(location.toString())) {
                        try {
                            Stat<ResourceLocation> stat = Stats.CUSTOM.get(location);
                            int statValue = serverPlayer.getStats().getValue(stat);
                            MutableComponent statMsg = new TranslatableComponent(getTranslationKey(stat))
                                    .append(": ")
                                    .append(statValue + "\n");
                            serverPlayer.sendMessage(statMsg, Util.NIL_UUID);
                        } catch (Exception e) {
                            LogUtils.getLogger().error("exception", e);
                        }
                    }
                }

                /*Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse("minecraft:dirt"));
                EntityType<?> entity = null;
                for (Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entityType : ForgeRegistries.ENTITIES.getEntries()) {
                    if (entityType.getValue().toString().equalsIgnoreCase("entity.quark.wraith")) {
                        entity = entityType.getValue();
                    }
                }

                if (block != null) {
                    int minedBlock = serverPlayer.getStats().getValue(Stats.BLOCK_MINED, block);
                    MutableComponent msg2 = new TextComponent("You mined ")
                            .append(block.getName())
                            .append(" " + minedBlock + " times");
                    serverPlayer.sendMessage(msg2, Util.NIL_UUID);
                }

                if (entity != null) {
                    int entityKilled = serverPlayer.getStats().getValue(Stats.ENTITY_KILLED, entity);
                    MutableComponent text = new TextComponent("You killed ")
                            .append(entity.getDescription())
                            .append(" " + entityKilled + " times");

                    serverPlayer.sendMessage(text, Util.NIL_UUID);
                }*/

            } catch (Exception e) {
                LogUtils.getLogger().error("exception", e);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}