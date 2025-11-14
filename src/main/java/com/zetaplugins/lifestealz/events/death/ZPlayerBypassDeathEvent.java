package com.zetaplugins.lifestealz.events.death;

import com.zetaplugins.lifestealz.events.ZPlayerDeathEventBase;
import com.zetaplugins.lifestealz.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

@Getter
public class ZPlayerBypassDeathEvent extends ZPlayerDeathEventBase {
    private final Player killer; // Can be null
    
    private final boolean isHeartLossBlocked;
    
    private final boolean isHeartGainBlocked;
    
    @Setter
    private Component messageToVictim;
    
    @Setter
    private Component messageToKiller;

    public ZPlayerBypassDeathEvent(PlayerDeathEvent originalEvent, Player killer,
                                  boolean heartLossBlocked, boolean heartGainBlocked) {
        super(originalEvent);
        this.killer = killer;
        this.isHeartLossBlocked = heartLossBlocked;
        this.isHeartGainBlocked = heartGainBlocked;
        this.messageToVictim = MessageUtils.getAndFormatMsg(
                false,
                "bypassVictim",
                "&aYour hearts are protected; you didn't lose any on death."
        );
        this.messageToKiller = killer != null ? MessageUtils.getAndFormatMsg(
                false,
                "bypassKiller",
                "&e%player% had bypass active; you didn't gain a heart.",
                new MessageUtils.Replaceable("%player%", originalEvent.getEntity().getName())
        ) : Component.text("");
    }
}
