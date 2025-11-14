package com.zetaplugins.lifestealz.events.death;

import com.zetaplugins.lifestealz.events.ZPlayerDeathEventBase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.entity.PlayerDeathEvent;

@Getter
public class ZPlayerNaturalDeathEvent extends ZPlayerDeathEventBase {
    @Setter
    private double heartsToLose;

    @Setter
    private boolean shouldDropHearts;

    @Setter
    private String deathMessage;

    public ZPlayerNaturalDeathEvent(PlayerDeathEvent originalEvent, double heartsToLose) {
        super(originalEvent);
        this.heartsToLose = heartsToLose;
        this.shouldDropHearts = false;
        this.deathMessage = originalEvent.getDeathMessage();
    }
}