package dev.splityosis.menulib;

import dev.splityosis.sysengine.configlib.ConfigLib;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class MenuLib implements Listener {

    private static boolean isSetup = false;

    public static void setup(JavaPlugin plugin){
        if (isSetup) return;
        plugin.getServer().getPluginManager().registerEvents(new MenuLib(), plugin);
        isSetup = true;

        try {
            ConfigLib.getMapperRegistry().registerMappers(new MenuStaticItemsMapMapper());
        } catch (Exception e){
            //
        }
    }

    private final List<InventoryAction> goodActions = Arrays.asList(InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_HALF, InventoryAction.HOTBAR_SWAP, InventoryAction.MOVE_TO_OTHER_INVENTORY);

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof Menu)) return;
        e.setCancelled(true);
        if (!(e.getClickedInventory().getHolder() instanceof Menu)) return;
        if (!goodActions.contains(e.getAction())) return;
        if (!(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT)) return;
        Menu menu = (Menu) e.getClickedInventory().getHolder();
        MenuItem menuItem = menu.getItem(e.getSlot());
        if (menuItem == null) return;
        Sound sound = menuItem.getSound();
        if (sound != null)
            ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), sound, menuItem.getSoundVolume(), menuItem.getSoundPitch());
        if (menuItem.getMenuItemExecuter() != null)
            menuItem.getMenuItemExecuter().onClick(e, menu);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e){
        if (e.getInventory().getHolder() instanceof Menu)
            e.setCancelled(true);
    }

    public static boolean isIsSetup() {
        return isSetup;
    }
}
