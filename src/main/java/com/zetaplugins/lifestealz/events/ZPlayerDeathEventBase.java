package com.zetaplugins.lifestealz.events;

import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.event.entity.PlayerDeathEvent;

@Getter
public abstract class ZPlayerDeathEventBase extends ZEvent {
    @Delegate(excludes = {org.bukkit.event.Event.class, org.bukkit.event.Cancellable.class})
    protected final PlayerDeathEvent originalEvent;

    public ZPlayerDeathEventBase(PlayerDeathEvent originalEvent) {
        this.originalEvent = originalEvent;
    }
}