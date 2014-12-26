/*
 * Copyright 2014 Aleksander.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.filippop1.antibot.option;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import pl.filippop1.antibot.AntiBotPlugin;
import pl.filippop1.antibot.BotPlayer;

public class AccountsOption extends Option implements Listener {
    public AccountsOption() {
        super("accounts");
    }
    
    @Override
    public void reload(boolean enabled) {
        AntiBotPlugin plugin = (AntiBotPlugin) Bukkit.getPluginManager().getPlugin("Anti-Bot");
        if (enabled) {
            Bukkit.getPluginManager().registerEvents(this, plugin);
        } else {
            HandlerList.unregisterAll(this);
        }
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        BotPlayer bot = new BotPlayer(e.getPlayer(), e.getAddress());
        if (bot.firstJoin()) {
            bot.register();
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, this.translate(AntiBotPlugin.getConfiguration().getKickMessage(), e));
        }
    }
    
    private String translate(String message, PlayerLoginEvent event) {
        Validate.notNull(message, "message can not be null");
        Validate.notNull(event, "event can not be null");
        return message
                .replace("$player", event.getPlayer().getName())
                .replace("$hostname", event.getHostname())
                .replace("$uuid", event.getPlayer().getUniqueId().toString())
                .replace("$ip", event.getAddress().getHostAddress());
    }
}
