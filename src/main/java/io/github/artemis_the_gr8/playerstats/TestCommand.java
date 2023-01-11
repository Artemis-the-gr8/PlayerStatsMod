package io.github.artemis_the_gr8.playerstats;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public class TestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("playerstats").executes(TestCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> command) {
        if(command.getSource().getEntity() instanceof Player player) {
            player.sendMessage(new TextComponent("hello world"), Util.NIL_UUID);
        }
        return Command.SINGLE_SUCCESS;
    }
}