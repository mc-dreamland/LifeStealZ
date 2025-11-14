package com.zetaplugins.lifestealz.events.death;

import com.zetaplugins.lifestealz.events.ZPlayerDeathEventBase;
import com.zetaplugins.lifestealz.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

@Getter
public class ZPlayerHeartGainCooldownEvent extends ZPlayerDeathEventBase {
    private final Player killer;
    
    private final long timeLeftOnCooldown;
    
    @Setter
    private boolean shouldDropHeartsInstead;
    
    @Setter
    private Component cooldownMessage;

    public ZPlayerHeartGainCooldownEvent(PlayerDeathEvent originalEvent, Player killer, long timeLeft) {
        super(originalEvent);
        this.killer = killer;
        this.timeLeftOnCooldown = timeLeft;
        this.shouldDropHeartsInstead = false;
        this.cooldownMessage = MessageUtils.getAndFormatMsg(
                false,
                "heartGainCooldown",
                "You have to wait before gaining another heart!",
                new MessageUtils.Replaceable("%time%", MessageUtils.formatTime(timeLeft))
        );
    }
}
