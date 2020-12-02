package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.banning.Ban;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.ADMIN, source = SourceType.BOTH)
@CommandParameters(description = "Unbans the specified IP.", usage = "/<command> <ip> [-q]")
public class Command_unbanip extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 0)
        {
            return false;
        }

        boolean silent = false;

        String ip = args[0];

        if (!FUtil.isValidIPv4(ip))
        {
            msg(ip + " is not a valid IP address.", ChatColor.RED);
            return true;
        }

        Ban ban = plugin.bm.getByIp(ip);

        if (ban == null)
        {
            msg("The IP " + ip + " is not banned.", ChatColor.RED);
            return true;
        }

        if (ban.hasUsername())
        {
            msg("This ban is not an IP-only ban.");
            return true;
        }

        if (args.length > 1 && args[1].equals("-q"))
        {
            silent = true;
        }

        plugin.bm.removeBan(ban);

        if (!silent)
        {
            FUtil.staffAction(sender.getName(), "Removed the IP " + ip + "from the ban list", true);
        }
        else
        {
            msg("Quietly removed the IP " + ip + " from the ban list.");
        }
        return true;
    }
}