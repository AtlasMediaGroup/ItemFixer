package ru.leymooo.fixer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import ru.leymooo.fixer.utils.VersionUtils;

@SuppressWarnings("deprecation")
public class TextureFix implements Listener {

    private HashMap<Material,Integer> limit = new HashMap<Material, Integer>();
    private HashSet<Material> ignore = new HashSet<Material>();
    private Main plugin;

    public TextureFix(Main main) {
        this.plugin = main;
        //Вроде все предменты что имеют SubId
        //Material, MaxSubId
        limit.put(Material.STONE, 6);
        limit.put(Material.DIRT, 2);
        limit.put(Material.LEGACY_WOOD, 5);
        limit.put(Material.LEGACY_SAPLING, 5);
        limit.put(Material.SAND, 1);
        limit.put(Material.LEGACY_LOG, 3);
        limit.put(Material.LEGACY_LEAVES, 3);
        limit.put(Material.SPONGE, 1);
        limit.put(Material.SANDSTONE, 2);
        limit.put(Material.LEGACY_LONG_GRASS, 2);
        limit.put(Material.LEGACY_WOOL, 15);
        limit.put(Material.LEGACY_RED_ROSE, 8);
        limit.put(Material.LEGACY_DOUBLE_STEP, 7);
        limit.put(Material.LEGACY_STEP, 7);
        limit.put(Material.LEGACY_STAINED_GLASS, 15);
        limit.put(Material.LEGACY_MONSTER_EGGS, 5);
        limit.put(Material.LEGACY_SMOOTH_BRICK, 3);
        limit.put(Material.LEGACY_WOOD_DOUBLE_STEP, 5);
        limit.put(Material.LEGACY_WOOD_STEP, 5);
        limit.put(Material.LEGACY_COBBLE_WALL, 1);
        limit.put(Material.QUARTZ_BLOCK, 2);
        limit.put(Material.LEGACY_STAINED_CLAY, 15);
        limit.put(Material.LEGACY_STAINED_GLASS, 15);
        limit.put(Material.LEGACY_STAINED_GLASS_PANE, 15);
        limit.put(Material.LEGACY_LEAVES_2, 1);
        limit.put(Material.LEGACY_LOG_2, 1);
        limit.put(Material.PRISMARINE, 2);
        limit.put(Material.LEGACY_CARPET, 15);
        limit.put(Material.LEGACY_DOUBLE_PLANT, 5);
        limit.put(Material.RED_SANDSTONE, 2);
        limit.put(Material.COAL, 1);
        limit.put(Material.LEGACY_RAW_FISH, 3);
        limit.put(Material.LEGACY_COOKED_FISH, 1);
        limit.put(Material.LEGACY_INK_SACK, 15);
        limit.put(Material.LEGACY_SKULL_ITEM, 5);
        limit.put(Material.GOLDEN_APPLE, 1);
        limit.put(Material.LEGACY_BANNER, 15);
        limit.put(Material.ANVIL, 2);
        if (VersionUtils.isVersion(12)) {
            limit.put(Material.LEGACY_CONCRETE, 15);
            limit.put(Material.LEGACY_CONCRETE_POWDER, 15);
            limit.put(Material.LEGACY_BED, 15);
        }
        //Предметы с прочностью.
        ignore.addAll(Arrays.asList(Material.MAP, Material.LEGACY_EMPTY_MAP, Material.LEGACY_CARROT_STICK, Material.BOW, Material.FISHING_ROD, Material.FLINT_AND_STEEL, Material.SHEARS));
        if (VersionUtils.getMajorVersion() == 8) {
            ignore.add(Material.LEGACY_MONSTER_EGG);
            ignore.add(Material.POTION);
            limit.put(Material.LEGACY_SKULL_ITEM, 4);
        }
        if (Material.matchMaterial("SHIELD") != null) {
            ignore.add(Material.SHIELD);
            ignore.add(Material.ELYTRA);
        }

        //Деревянные инструменты
        ignore.addAll(Arrays.asList(Material.LEGACY_WOOD_SPADE, Material.LEGACY_WOOD_PICKAXE, Material.LEGACY_WOOD_AXE, Material.LEGACY_WOOD_SWORD, Material.LEGACY_WOOD_HOE));
        //Золотые инструменты
        ignore.addAll(Arrays.asList(Material.LEGACY_GOLD_SPADE, Material.LEGACY_GOLD_PICKAXE, Material.LEGACY_GOLD_AXE, Material.LEGACY_GOLD_SWORD, Material.LEGACY_GOLD_HOE));
        //Каменные инструменты
        ignore.addAll(Arrays.asList(Material.LEGACY_STONE_SPADE, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SWORD, Material.STONE_HOE));
        //Железные инструменты
        ignore.addAll(Arrays.asList(Material.LEGACY_IRON_SPADE, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SWORD, Material.IRON_HOE));
        //Алмазные инструменты
        ignore.addAll(Arrays.asList(Material.LEGACY_DIAMOND_SPADE, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SWORD, Material.DIAMOND_HOE));
        //Разная броня
        ignore.addAll(Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS));
        ignore.addAll(Arrays.asList(Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET, Material.IRON_LEGGINGS));
        ignore.addAll(Arrays.asList(Material.LEGACY_GOLD_BOOTS, Material.LEGACY_GOLD_CHESTPLATE, Material.LEGACY_GOLD_HELMET, Material.LEGACY_GOLD_LEGGINGS));
        ignore.addAll(Arrays.asList(Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_LEGGINGS));
        ignore.addAll(Arrays.asList(Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_LEGGINGS));
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onHold(PlayerItemHeldEvent e) {
        ItemStack it = e.getPlayer().getInventory().getItem(e.getNewSlot());
        if (isInvalide(it)) {
            e.setCancelled(true);
            e.getPlayer().getInventory().remove(it);
            e.getPlayer().updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent e) {
        ItemStack it = e.getItem();
        if (isInvalide(it)) {
            e.setCancelled(true);
            e.getPlayer().getInventory().remove(it);
            e.getPlayer().updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onClick(InventoryClickEvent e) {
        ItemStack it = e.getCurrentItem();
        if (e.getWhoClicked().getType() == EntityType.PLAYER && isInvalide(it)) {
            e.setCancelled(true);
            e.getWhoClicked().getInventory().remove(it);
            ((Player)e.getWhoClicked()).updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPickup(PlayerPickupItemEvent e) { //Deprecated
        ItemStack it = e.getItem().getItemStack();
        if (isInvalide(it)) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    private boolean isInvalide(ItemStack it) {
        if (it != null && it.getType()!=Material.AIR && it.getDurability() != 0) {
            if (limit.containsKey(it.getType())) {
                return (it.getDurability() < 0 || (it.getDurability() > limit.get(it.getType())));
            }
            return !ignore.contains(it.getType());
        }
        return false;
    }
}
