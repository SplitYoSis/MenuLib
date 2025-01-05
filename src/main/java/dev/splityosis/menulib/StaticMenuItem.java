package dev.splityosis.menulib;

import dev.splityosis.sysengine.actions.Actions;
import dev.splityosis.sysengine.utils.PapiUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticMenuItem {

    private String identifier;
    private ItemStack itemStack;
    private Actions onClick;
    private int[] slots;

    public StaticMenuItem(String identifier, int[] slots, ItemStack itemStack, Actions onClick) {
        this.identifier = identifier;
        this.itemStack = itemStack;
        this.slots = slots;
        this.onClick = onClick;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack getItemStack(Player player, Map<String, String> placeholders){
        return applyItemStackPlaceholders(player, itemStack, placeholders);
    }

    public int[] getSlots() {
        return slots;
    }

    public Actions getOnClick() {
        return onClick;
    }

    public MenuItem getMenuItem(Player player, Map<String, String> placeholders){
        return new MenuItem(null){
            @Override
            public ItemStack getDisplayItem() {
                return getItemStack(player, placeholders);
            }
        }.executes((inventoryClickEvent, menu) -> {
            if (getOnClick() == null) return;
            // THIS IS NOT AN ARRAY
            getOnClick().execute(player, placeholders);
        });
    }

    public static ItemStack applyItemStackPlaceholders(@Nullable Player player, ItemStack itemStack, @Nullable Map<String, String> fromToMap){
        if (fromToMap == null)
            fromToMap = new HashMap<>();
        ItemStack item = itemStack.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        String name = meta.getDisplayName();
        if (name != null) {
            for (Map.Entry<String, String> stringStringEntry : fromToMap.entrySet())
                name = name.replace(stringStringEntry.getKey(), stringStringEntry.getValue());
            name = PapiUtil.parsePlaceholders(player, name);
        }

        List<String> newLore = new ArrayList<>();
        List<String> lore = meta.getLore();
        if (lore != null)
            for (String s : lore) {
                String line = s;
                for (Map.Entry<String, String> stringStringEntry : fromToMap.entrySet())
                    line = line.replace(stringStringEntry.getKey(), stringStringEntry.getValue());
                newLore.add(PapiUtil.parsePlaceholders(player, line));
            }
        meta.setDisplayName(name);
        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }
}
