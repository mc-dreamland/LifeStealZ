package com.zetaplugins.lifestealz;

import com.zetaplugins.lifestealz.util.*;
import com.zetaplugins.lifestealz.util.revive.ReviveTaskManager;
import com.zetaplugins.zetacore.ZetaCorePlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.zetaplugins.lifestealz.api.LifeStealZAPI;
import com.zetaplugins.lifestealz.api.LifeStealZAPIImpl;
import com.zetaplugins.lifestealz.caches.EliminatedPlayersCache;
import com.zetaplugins.lifestealz.caches.OfflinePlayerCache;
import com.zetaplugins.lifestealz.util.commands.CommandManager;
import com.zetaplugins.lifestealz.util.customblocks.ReviveBeaconEffectManager;
import com.zetaplugins.lifestealz.util.customitems.recipe.RecipeManager;
import com.zetaplugins.lifestealz.util.geysermc.GeyserManager;
import com.zetaplugins.lifestealz.util.geysermc.GeyserPlayerFile;
import com.zetaplugins.lifestealz.storage.MariaDBStorage;
import com.zetaplugins.lifestealz.storage.MySQLStorage;
import com.zetaplugins.lifestealz.storage.Storage;
import com.zetaplugins.lifestealz.storage.SQLiteStorage;
import com.zetaplugins.lifestealz.util.worldguard.WorldGuardManager;

import java.io.File;

public final class LifeStealZ extends ZetaCorePlugin {

    @Getter
    private VersionChecker versionChecker;
    @Getter
    private Storage storage;
    @Getter
    private WorldGuardManager worldGuardManager;
    @Getter
    private LanguageManager languageManager;
    @Getter
    private ConfigManager configManager;
    @Getter
    private RecipeManager recipeManager;
    @Getter
    private GeyserManager geyserManager;
    @Getter
    private GeyserPlayerFile geyserPlayerFile;
    @Getter
    private WebHookManager webHookManager;
    @Getter
    private GracePeriodManager gracePeriodManager;
    @Getter
    private BypassManager bypassManager;
    @Getter
    private EliminatedPlayersCache eliminatedPlayersCache;
    @Getter
    private OfflinePlayerCache offlinePlayerCache;
    @Getter
    private AsyncTaskManager asyncTaskManager;
    @Getter
    private ReviveBeaconEffectManager reviveBeaconEffectManager;
    @Getter
    private ReviveTaskManager reviveTaskManager;
    private final boolean hasWorldGuard = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
    private final boolean hasPlaceholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    private final boolean hasGeyser = Bukkit.getPluginManager().getPlugin("floodgate") != null;

    @Override
    public void onLoad() {
        getLogger().info("Loading LifeStealZ...");

        if (Bukkit.getName().toLowerCase().contains("spigot") || Bukkit.getName().toLowerCase().contains("craftbukkit")) {
            getLogger().severe("---------------------------------------------------");
            getLogger().severe("LifeStealZ does not support Spigot or Bukkit!");
            getLogger().severe("Please use Paper or any fork of Paper (like Purpur). If you need further assistance, please join our Discord server:");
            getLogger().severe("https://strassburger.org/discord");
            getLogger().severe("---------------------------------------------------");
        }

        if (hasWorldGuard()) {
            getLogger().info("WorldGuard found! Enabling WorldGuard support...");
            worldGuardManager = new WorldGuardManager();
            getLogger().info("WorldGuard found! Enabled WorldGuard support!");
        }
    }

    @Override
    public void onEnable() {
        if (hasGeyser()) {
            getLogger().info("Geyser found, enabling Bedrock player support.");
            geyserPlayerFile = new GeyserPlayerFile();
            geyserManager = new GeyserManager();
        }

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        asyncTaskManager = new AsyncTaskManager();
        reviveBeaconEffectManager = new ReviveBeaconEffectManager(this);
        reviveTaskManager = new ReviveTaskManager();

        languageManager = new LanguageManager(this);
        configManager = new ConfigManager(this);

        storage = createPlayerDataStorage();
        storage.init();

        recipeManager = new RecipeManager(this);
        recipeManager.registerRecipes();

        versionChecker = new VersionChecker(this, "l8Uv7FzS");
        gracePeriodManager = new GracePeriodManager(this);
        bypassManager = new BypassManager(this);
        webHookManager = new WebHookManager(this);

        eliminatedPlayersCache = new EliminatedPlayersCache(this);
        offlinePlayerCache = new OfflinePlayerCache(this);

        new CommandManager(this).registerCommands();

        new EventManager(this).registerListeners();

        initializeBStats();

        if (hasPlaceholderApi()) {
            PapiExpansion papiExpansion = new PapiExpansion(this);
            if (papiExpansion.canRegister()) {
                papiExpansion.register();
                getLogger().info("PlaceholderAPI found! Enabled PlaceholderAPI support!");
            }
        }

        getLogger().info("LifeStealZ enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Canceling all running tasks...");
        asyncTaskManager.cancelAllTasks();
        reviveBeaconEffectManager.clearAllEffects();
        getLogger().info("LifeStealZ disabled!");
    }

    public static LifeStealZ getInstance() {
        return JavaPlugin.getPlugin(LifeStealZ.class);
    }

    public static LifeStealZAPI getAPI() {
        return new LifeStealZAPIImpl(getInstance());
    }

    public boolean hasWorldGuard() {
        return hasWorldGuard;
    }

    public boolean hasPlaceholderApi() {
        return hasPlaceholderApi;
    }

    public boolean hasGeyser() {
        return hasGeyser;
    }

    private Storage createPlayerDataStorage() {
        return switch (getConfigManager().getStorageConfig().getString("type").toLowerCase()) {
            case "mysql" -> {
                getLogger().info("Using MySQL storage");
                yield new MySQLStorage(this);
            }
            case "sqlite" -> {
                getLogger().info("Using SQLite storage");
                yield new SQLiteStorage(this);
            }
            case "mariadb" -> {
                getLogger().info("Using MariaDB storage");
                yield new MariaDBStorage(this);
            }
            default -> {
                getLogger().warning("Invalid storage type in config.yml! Using SQLite storage as fallback.");
                yield new SQLiteStorage(this);
            }
        };
    }

    public static void setMaxHealth(Player player, double maxHealth) {
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            attribute.setBaseValue(maxHealth);
        }
    }

    private void initializeBStats() {
        int pluginId = 18735;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new Metrics.SimplePie("storage_type", () -> getConfigManager().getStorageConfig().getString("type")));
        metrics.addCustomChart(new Metrics.SimplePie("language", () -> getConfig().getString("lang")));
    }

    public File getPluginFile() {
        return this.getFile();
    }
}
