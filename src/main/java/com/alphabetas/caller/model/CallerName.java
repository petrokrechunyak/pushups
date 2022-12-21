package com.alphabetas.caller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "caller_names", uniqueConstraints=
@UniqueConstraint(columnNames={"caller_chat_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallerName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "caller_user_id")
    private CallerUser callerUser;

    @ManyToOne
    @JoinColumn(name = "caller_chat_id")
    private CallerChat callerChat;

    private String name;

    public CallerName(CallerUser callerUser, CallerChat callerChat, String name) {
        this.callerUser = callerUser;
        this.callerChat = callerChat;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CallerName{" +
                "callerUser=" + callerUser +
                ", callerChat=" + callerChat +
                ", name='" + name + '\'' +
                '}';
    }
}
