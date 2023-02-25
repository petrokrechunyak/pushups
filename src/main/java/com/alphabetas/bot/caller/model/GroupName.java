package com.alphabetas.bot.caller.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "group_names")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="chat_id")
    private CallerChat chat;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "users_group_names",
            joinColumns = { @JoinColumn(name = "group_name_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id"),
                                   @JoinColumn(name = "chat_id") }
    )
    private Set<CallerUser> users;

    public GroupName(CallerChat chat, String name) {
        this.chat = chat;
        this.name = name;
    }
}
