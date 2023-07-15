package com.alphabetas.bot.caller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "caller_chat_configs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatConfig {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @OneToOne
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private CallerChat chat;

    private Integer nameLimit;

    @ColumnDefault("true")
    private boolean rpEnabled;

    @ColumnDefault("false")
    private boolean aggressionEnabled;

    @ColumnDefault("false")
    private boolean allowSpace;

    public ChatConfig(Long chatId, Integer nameLimit) {
        this.chatId = chatId;
        this.nameLimit = nameLimit;
        this.aggressionEnabled = false;
        this.rpEnabled = true;
        this.allowSpace = false;
    }

    public void setNameLimit(Integer nameLimit) {
        if (nameLimit < 0)
            this.nameLimit = 0;
        else
            this.nameLimit = nameLimit;
    }
}
