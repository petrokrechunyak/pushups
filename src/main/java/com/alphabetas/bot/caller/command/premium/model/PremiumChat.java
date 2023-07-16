package com.alphabetas.bot.caller.command.premium.model;

import com.alphabetas.bot.caller.model.CallerChat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "premium_chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PremiumChat {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @OneToOne
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private CallerChat chat;

    private boolean premium;

    private Long activeTo;

    public PremiumChat(Long chatId) {
        this.chatId = chatId;
    }
}
