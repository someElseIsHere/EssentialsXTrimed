package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.VersionUtil;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class Commandinvsee extends EssentialsCommand {
    public Commandinvsee() {
        super("invsee");
    }

    //This method has a hidden param, which if given will display the equip slots. #easteregg
    @Override
    protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length < 1) {
            throw new NotEnoughArgumentsException();
        }

        final User invUser = getPlayer(server, user, args, 0);
        if (user == invUser) {
            user.sendTl("invseeNoSelf");
            throw new NoChargeException();
        }

        final Inventory inv;

        if (args.length > 1 && user.isAuthorized("essentials.invsee.equip")) {
            inv = server.createInventory(invUser.getBase(), 9, user.playerTl("equipped"));
            inv.setContents(invUser.getBase().getInventory().getArmorContents());
            if (VersionUtil.getServerBukkitVersion().isHigherThanOrEqualTo(VersionUtil.v1_9_4_R01)) {
                inv.setItem(4, invUser.getBase().getInventory().getItemInOffHand());
            }
        } else {
            ItemStack[] content = invUser.getBase().getInventory().getContents();
            if (content.length > 27) {
                inv = server.createInventory(invUser.getBase(), 54);
                inv.setContents(invUser.getBase().getInventory().getContents());
            }
            else inv = invUser.getBase().getInventory();
        }
        user.getBase().closeInventory();
        user.getBase().openInventory(inv);
        user.setInvSee(true);
    }

    @Override
    protected List<String> getTabCompleteOptions(final Server server, final User user, final String commandLabel, final String[] args) {
        if (args.length == 1) {
            final List<String> suggestions = getPlayers(server, user);
            suggestions.remove(user.getName());
            return suggestions;
        } else {
            //if (args.length == 2) {
            //    return Lists.newArrayList("equipped");
            //}
            return Collections.emptyList();
        }
    }
}
