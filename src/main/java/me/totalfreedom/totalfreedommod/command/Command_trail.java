package me.totalfreedom.totalfreedommod.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = "Trails rainbow wool behind you as you walk/fly.", usage = "/<command> [off]")
public class Command_trail extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length > 0 && "off".equals(args[0]))
        {
            plugin.tr.remove(playerSender);
            msg("Trail disabled.");
        }
        else
        {
            plugin.tr.add(playerSender);
            msg("Trail enabled. Use \"/trail off\" to disable.");
        }

        return true;
    }

    @Override
    public List<String> getTabCompleteOptions(CommandSender sender, Command command, String alias, String[] args)
    {
        if (args.length == 1 && plugin.al.isAdmin(sender))
        {
            return Arrays.asList("off");
        }

        return Collections.emptyList();
    }

}
