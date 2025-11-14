package com.zetaplugins.lifestealz.events.death;

import com.zetaplugins.lifestealz.events.ZPlayerDeathEventBase;
import com.zetaplugins.lifestealz.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

@Getter
public class ZPlayerEliminationEvent extends ZPlayerDeathEventBase {
    private final Player killer; // Can be null for natural elimination
    
    private final boolean isNaturalElimination;
    
    @Setter
    private boolean shouldBanPlayer;
    
    @Setter
    private boolean shouldAnnounceElimination;
    
    @Setter
    private Component eliminationMessage;

    @Setter
    private Component kickMessage;

    public ZPlayerEliminationEvent(PlayerDeathEvent originalEvent, Player killer) {
        super(originalEvent);
        this.killer = killer;
        this.isNaturalElimination = killer == null;
        this.shouldBanPlayer = true;
        this.shouldAnnounceElimination = true;
        this.kickMessage = MessageUtils.getAndFormatMsg(
                false,
                "eliminatedJoin",
                "&cYou don't have any hearts left!"
        );
    }
}