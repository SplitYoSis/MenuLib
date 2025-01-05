package dev.splityosis.menulib;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;

public class MenuStaticItemsMap extends ArrayList<StaticMenuItem> {

    public void apply(Menu menu, Player player, Map<String, String> placeholders){
        for (StaticMenuItem staticMenuItem : this) {
            for (int slot : staticMenuItem.getSlots()) {
                menu.setStaticItem(slot, staticMenuItem.getMenuItem(player, placeholders));
            }
        }
    }
}
