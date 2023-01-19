package io.github.artemis_the_gr8.playerstats;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;


public class TestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("playerstats")
                .then(Commands.literal("minecraft:broken")
                        .then(Commands.argument("tools", ResourceKeyArgument.key(ForgeRegistries.ITEMS.getRegistryKey()))
                        .executes(TestCommand::execute)))
                .then(Commands.literal("minecraft:killed")
                        .then(Commands.argument("entities", ResourceKeyArgument.key(ForgeRegistries.ENTITIES.getRegistryKey()))
                        .executes(TestCommand::executeEntityCommand)))
                .then(Commands.argument("stats", ResourceKeyArgument.key(ForgeRegistries.STAT_TYPES.getRegistryKey()))
                        .executes(TestCommand::execute)));
    }

    private static int executeEntityCommand(CommandContext<CommandSourceStack> command) {
        if (command.getSource().getEntity() instanceof Player player) {
            player.sendMessage(new TextComponent("last child: " + command.getLastChild().toString()), Util.NIL_UUID);
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

    private static int execute(CommandContext<CommandSourceStack> command) {
        if(command.getSource().getEntity() instanceof Player player) {

            player.sendMessage(new TextComponent("input: " + command.getInput()), Util.NIL_UUID);
            for (ParsedCommandNode<?> node : command.getNodes()) {
                player.sendMessage(new TextComponent(node.toString()), Util.NIL_UUID);
            }
            player.sendMessage(new TextComponent("\n"), Util.NIL_UUID);

            if (player instanceof ServerPlayer serverPlayer) {
                Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse("minecraft:dirt"));
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
                    player.sendMessage(msg2, Util.NIL_UUID);
                }

                if (entity != null) {
                    int entityKilled = serverPlayer.getStats().getValue(Stats.ENTITY_KILLED, entity);
                    MutableComponent text = new TextComponent("You killed ")
                            .append(entity.getDescription())
                            .append(" " + entityKilled + " times");

                    player.sendMessage(text, Util.NIL_UUID);
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}