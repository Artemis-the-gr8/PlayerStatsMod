package io.github.artemis_the_gr8.playerstats;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;


public class TestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("playerstats").executes(TestCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> command) {
        if(command.getSource().getEntity() instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                int deaths = serverPlayer.getStats().getValue(Stats.CUSTOM, Stats.DEATHS);
                int mined_block = serverPlayer.getStats().getValue(Stats.BLOCK_MINED, ForgeRegistries.BLOCKS.getValue(Blocks.DIRT.getRegistryName()));

                TextComponent msg = new TextComponent("You died " + deaths + " times");
                TextComponent msg2 = new TextComponent("You mined " + mined_block + " dirt");
                player.sendMessage(msg, Util.NIL_UUID);
                player.sendMessage(msg2, Util.NIL_UUID);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}