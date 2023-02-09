package com.alphabetas.bot.caller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "caller_chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallerChat {

    @Id
    private Long id;

    private String title;

    @OneToMany(mappedBy = "callerChat", cascade = CascadeType.REMOVE)
    private Set<CallerUser> callerUsers;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE)
    private Set<CallerName> callerNames;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "chat")
    private ChatConfig config;

    public CallerChat(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallerChat that = (CallerChat) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CallerChat{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
