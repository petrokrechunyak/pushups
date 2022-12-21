package com.alphabetas.caller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "names", uniqueConstraints=
@UniqueConstraint(columnNames={"chat_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Names {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "caller_user_id")
    private CallerUser callerUser;

    @Column(name = "chat_id")
    private Long chatId;

    private String name;

    public Names(CallerUser callerUser, Long chatId, String name) {
        this.callerUser = callerUser;
        this.chatId = chatId;
        this.name = name;
    }
}