package com.alphabetas.bot.caller.model;

import com.alphabetas.bot.caller.command.marriage.model.MarriageModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.HashSet;
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

    @ColumnDefault("0")
    private Integer memberCount;

    @OneToMany(mappedBy = "callerChat", cascade = CascadeType.REMOVE)
    private Set<CallerUser> callerUsers;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE)
    private Set<CallerName> callerNames;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE)
    private Set<GroupName> groupNames;

    @OneToOne(mappedBy = "chat", cascade = CascadeType.REMOVE)
    private ChatConfig config;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE)
    private Set<MessageCount> messageCounts;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.REMOVE)
    private Set<MarriageModel> marriageModels;

    public CallerChat(Long id, String title) {
        this.id = id;
        this.title = title;
        callerNames = new HashSet<>();
        callerUsers = new HashSet<>();
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
