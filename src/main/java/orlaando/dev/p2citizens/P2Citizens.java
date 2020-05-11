package orlaando.dev.p2citizens;

import com.plotsquared.core.api.PlotAPI;
import com.plotsquared.core.plot.Plot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Level;

public class P2Citizens extends JavaPlugin implements Listener {
    private PlotAPI plotAPI = null;

    @Override
    public void onEnable() {
        Bukkit.getLogger().log(Level.INFO, "Searching for PlotSquared..");
        PluginManager manager = Bukkit.getServer().getPluginManager();
        final Plugin plotsquared = manager.getPlugin("PlotSquared");
        Bukkit.getLogger().log(Level.INFO, "Found PlotSquared!");

        if (plotsquared == null || !plotsquared.isEnabled()) {
            Bukkit.getLogger().log(Level.INFO,  "Unable to locate PlotSquared!");
            manager.disablePlugin(this);
            return;
        }

        // Do PlotSquared related stuff
        plotAPI = new PlotAPI();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (event.getMessage().toLowerCase().startsWith("/npc") && !player.hasPermission("p2citizens.bypass")) {

            Plot plot = plotAPI.wrapPlayer(playerUUID).getCurrentPlot();
            if (plot == null) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You're not in a plot.");
                return;
            }

           if (!plot.getOwner().equals(playerUUID)) {
               event.setCancelled(true);
               player.sendMessage(ChatColor.RED + "You must be the plot owner to do that.");
            }
        }
    }
}
