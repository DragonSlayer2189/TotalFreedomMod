package me.totalfreedom.totalfreedommod.command;

import java.text.SimpleDateFormat;
import java.util.Date;
import me.totalfreedom.totalfreedommod.player.FPlayer;
import me.totalfreedom.totalfreedommod.rank.Rank;
import me.totalfreedom.totalfreedommod.util.FUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SUPER_ADMIN, source = SourceType.BOTH, permission = "tfm.admin.mute")
@CommandParameters(description = "Temporarily mutes an online player.", usage = "/<command> <player> [duration] [reason]", aliases = "tmute")
public class Command_tempmute extends FreedomCommand
{

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        Player player = getPlayer(args[0]);
        if (player == null)
        {
            msg(PLAYER_NOT_FOUND);
            return true;
        }
        if (plugin.al.isAdmin(player))
        {
            msg(player.getName() + " is a superadmin, and can't be muted.");
            return true;
        }

        Date expires = FUtil.parseDateOffset("5m");
        if (args.length >= 2)
        {
            Date parsed = FUtil.parseDateOffset(args[1]);
            if (parsed != null)
            {
                expires = parsed;
            }
        }

        int reasonEnd = args.length;
        String reason = reasonEnd >= 3 ? StringUtils.join(args, " ", 2, reasonEnd) : null;

        long delayTicks = Math.max(1L, (expires.getTime() - new Date().getTime()) / 50L);

        FPlayer playerdata = plugin.pl.getPlayer(player);
        playerdata.setMuted(true, delayTicks);

        FUtil.adminAction(sender.getName(), "Temporarily muted " + player.getName() + " until " + DATE_FORMAT.format(expires), true);
        msg("Temporarily muted " + player.getName() + " until " + DATE_FORMAT.format(expires) + ".");

        Component muteMessage = Component.text("You have been temporarily muted until " + DATE_FORMAT.format(expires) + ".", NamedTextColor.RED);
        if (reason != null && !reason.isEmpty())
        {
            muteMessage = muteMessage
                    .append(Component.text("\nReason: ", NamedTextColor.RED))
                    .append(Component.text(reason, NamedTextColor.GOLD));
        }
        player.sendMessage(muteMessage);

        return true;
    }
}
