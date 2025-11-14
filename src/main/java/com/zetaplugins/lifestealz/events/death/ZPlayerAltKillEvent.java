package com.zetaplugins.lifestealz.events.death;

import com.zetaplugins.lifestealz.events.ZPlayerDeathEventBase;
import com.zetaplugins.lifestealz.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

@Getter
public class ZPlayerAltKillEvent extends ZPlayerDeathEventBase {
    private final Player suspectedAlt;
    
    private final String sharedIP;
    
    @Setter
    private boolean shouldPreventKill;
    
    @Setter
    private boolean shouldLogAttempt;
    
    @Setter
    private boolean shouldSendMessage;
    
    @Setter
    private Component warningMessage;

    public ZPlayerAltKillEvent(PlayerDeathEvent originalEvent, Player suspectedAlt, String sharedIP) {
        super(originalEvent);
        this.suspectedAlt = suspectedAlt;
        this.sharedIP = sharedIP;
        this.shouldPreventKill = true;
        this.shouldLogAttempt = true;
        this.shouldSendMessage = true;
        this.warningMessage = MessageUtils.getAndFormatMsg(
                false,
                "altKill",
                "&cPlease don't kill alts! This attempt has been logged!"
        );
    }
}