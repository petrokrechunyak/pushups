package com.alphabetas.caller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "caller_names", uniqueConstraints=
@UniqueConstraint(columnNames={"chat_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallerName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="caller_chat_id", referencedColumnName="caller_chat_id",
            insertable = false, updatable = false)
    @JoinColumn(name="caller_user_id", referencedColumnName="user_id",
            insertable = false, updatable = false)
    private CallerUser callerUser;

    @Column(name = "caller_user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "caller_chat_id")
    private CallerChat chat;

    private String name;

    public CallerName(Long userId, CallerChat callerChat, String name) {
        this.userId = userId;
        this.chat = callerChat;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CallerName{" +
                "callerUser=" + callerUser +
                ", callerChat=" + chat +
                ", name='" + name + '\'' +
                '}';
    }
}
