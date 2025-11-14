package com.zetaplugins.lifestealz.events.death;

import com.zetaplugins.lifestealz.events.ZPlayerDeathEventBase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

@Getter
public class ZPlayerPvPDeathEvent extends ZPlayerDeathEventBase {
    private final Player killer;
    
    @Setter
    private double heartsToLose;
    
    @Setter
    private double heartsKillerGains;
    
    @Setter
    private boolean shouldDropHearts;
    
    @Setter
    private boolean killerShouldGainHearts;
    
    @Setter
    private String deathMessage;

    public ZPlayerPvPDeathEvent(PlayerDeathEvent originalEvent, Player killer, double heartsToLose, double heartsKillerGains) {
        super(originalEvent);
        this.killer = killer;
        this.heartsToLose = heartsToLose;
        this.heartsKillerGains = heartsKillerGains;
        this.shouldDropHearts = false;
        this.killerShouldGainHearts = true;
        this.deathMessage = originalEvent.getDeathMessage();
    }
}
