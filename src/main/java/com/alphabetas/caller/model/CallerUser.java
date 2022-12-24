package com.alphabetas.caller.model;

import com.alphabetas.caller.model.enums.ConfigEmojiEnum;
import com.alphabetas.caller.model.enums.UserStates;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "caller_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CallerUser.UserId.class)
public class CallerUser {


    @Id
    @Column(name = "user_id")
    private Long userId;

    private String firstname;

    private String username;

    @Id
    @ManyToOne
    private CallerChat callerChat;

    @OneToMany(mappedBy = "callerUser", cascade = CascadeType.REMOVE)
    private Set<CallerName> names;

    @Enumerated(EnumType.STRING)
    private UserStates userState = UserStates.OFF;

    @Enumerated(EnumType.STRING)
    private ConfigEmojiEnum emojiEnum = ConfigEmojiEnum.ALL;

    public CallerUser(Long userId, String firstname, String username, CallerChat callerChat) {
        this.userId = userId;
        this.firstname = firstname;
        this.username = username;
        this.callerChat = callerChat;
        names = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallerUser that = (CallerUser) o;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "CallerUser{" +
                "userId=" + userId +
                ", firstname='" + firstname + '\'' +
                ", username='" + username + '\'' +
                ", callerChat=" + callerChat +
                ", userState=" + userState +
                ", emojiEnum=" + emojiEnum +
                '}';
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserId implements Serializable {
        private Long userId;

        private Long callerChat;


    }
}
