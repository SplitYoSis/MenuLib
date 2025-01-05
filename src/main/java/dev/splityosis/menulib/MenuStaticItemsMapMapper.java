package dev.splityosis.menulib;

import dev.splityosis.sysengine.actions.Actions;
import dev.splityosis.sysengine.configlib.bukkit.ConfigurationSection;
import dev.splityosis.sysengine.configlib.configuration.AbstractMapper;
import dev.splityosis.sysengine.configlib.manager.ConfigManager;
import dev.splityosis.sysengine.configlib.mappers.ActionsMapper;
import dev.splityosis.sysengine.configlib.mappers.ItemStackMapper;
import org.bukkit.inventory.ItemStack;

public class MenuStaticItemsMapMapper implements AbstractMapper<MenuStaticItemsMap> {

    @Override
    public MenuStaticItemsMap getFromConfig(ConfigManager configManager, ConfigurationSection config, String path) {
        MenuStaticItemsMap menuStaticItemsMap = new MenuStaticItemsMap();
        ItemStackMapper itemStackConfigLogic = new ItemStackMapper();
        ActionsMapper actionsConfigLogic = new ActionsMapper();

        ConfigurationSection section = config.getConfigurationSection(path);
        if (section != null)
            for (String identifier : section.getKeys(false)) {
                String slotsStr = section.getString(identifier + ".slot");
                String[] splitSlots = slotsStr.replace(" ", "").split(",");
                int[] slots = new int[splitSlots.length];

                for (int i = 0; i < splitSlots.length; i++) {
                    try {
                        slots[i] = Integer.parseInt(splitSlots[i]);
                    } catch (Exception e){
                        slots[i] = -1;
                    }
                }
                ItemStack item = itemStackConfigLogic.getFromConfig(configManager, section, identifier + ".display-item");
                Actions onClick = actionsConfigLogic.getFromConfig(configManager, section, identifier + ".on-click-actions");
                menuStaticItemsMap.add(new StaticMenuItem(identifier, slots, item, onClick));
            }
        return menuStaticItemsMap;
    }

    @Override
    public void setInConfig(ConfigManager configManager, MenuStaticItemsMap menuStaticItemsMap, ConfigurationSection config, String path) {
        ItemStackMapper itemStackConfigLogic = new ItemStackMapper();
        ActionsMapper actionsConfigLogic = new ActionsMapper();

        menuStaticItemsMap.forEach((staticMenuItem) -> {
            String slots = arrayToString(staticMenuItem.getSlots());
            config.set(path + "." + staticMenuItem.getIdentifier() + ".slot", slots);
            itemStackConfigLogic.setInConfig(configManager, staticMenuItem.getItemStack(), config, path + "." + staticMenuItem.getIdentifier() + ".display-item");
            actionsConfigLogic.setInConfig(configManager, staticMenuItem.getOnClick(), config, path + "." + staticMenuItem.getIdentifier() + ".on-click-actions");
        });
    }

    private static String arrayToString(int[] arr){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            stringBuilder.append(arr[i]);
            if (i != arr.length - 1)
                stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }
}
