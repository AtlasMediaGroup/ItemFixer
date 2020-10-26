package ru.leymooo.fixer;

import java.io.File;

import com.comphenix.protocol.wrappers.nbt.NbtType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import ru.leymooo.fixer.utils.VersionUtils;

public class Main extends JavaPlugin {

    private MagicAPI mapi;
    private ItemChecker checker;
    private ProtocolManager manager;
    private boolean longArraysSupported;

    @Override
    public void onEnable() {
        if (!isSupportedVersion()) {
            getLogger().warning("ItemFixer does not support your server version");
            setEnabled(false);
            return;
        }
        checkLongArraySupport();
        saveDefaultConfig();
        checkNewConfig();
        PluginManager pmanager = Bukkit.getPluginManager();
        mapi = getMagicAPI();
        checker = new ItemChecker(this);
        manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new NBTListener(this));
        pmanager.registerEvents(new NBTBukkitListener(this), this);
        pmanager.registerEvents(new TextureFix(this), this);
        Bukkit.getConsoleSender().sendMessage("§b[ItemFixer] §aenabled");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        manager.removePacketListeners(this);
        NBTListener.cancel.invalidateAll();
        NBTListener.cancel = null;
        mapi = null;
        checker = null;
        manager = null;
    }

    public ItemChecker.CheckStatus checkItem(ItemStack stack, Player p) {
        return checker.isHackedItem(stack, p);
    }

    public boolean isMagicItem(ItemStack it) {
        return mapi != null && mapi.isWand(it);
    }

    private void checkNewConfig() {
        if (!getConfig().isSet("ignored-tags")) {
            File config = new File(getDataFolder(),"config.yml");
            config.delete();
            saveDefaultConfig();
        }
        if (getConfig().isSet("max-pps")) {
            getConfig().set("max-pps", null);
            getConfig().set("max-pps-kick-msg", null);
            saveConfig();
        }
    }   

    private MagicAPI getMagicAPI() {
        Plugin magicPlugin = Bukkit.getPluginManager().getPlugin("Magic");
        if (magicPlugin == null || !magicPlugin.isEnabled() || !(magicPlugin instanceof MagicAPI)) {
            return null;
        }
        return (MagicAPI) magicPlugin;
    }

    public boolean isSupportedVersion() {
        return !VersionUtils.isVersion(13);// now ItemFixer does not support 1.13+
    }

    private void checkLongArraySupport() {
        try {
            NbtType type = NbtType.TAG_LONG_ARRAY;
            this.longArraysSupported = NbtType.valueOf("TAG_LONG_ARRAY") != null || type != null;
        } catch (Throwable e) {}
    }

    public boolean isLongArraysSupported() {
        return longArraysSupported;
    }
}
