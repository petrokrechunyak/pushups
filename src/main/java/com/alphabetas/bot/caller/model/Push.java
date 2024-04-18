package com.alphabetas.bot.caller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Objects;

@Table(name = "pushups")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Push {

    @Id
    private Long userId;

    private String username;

    private String firstname;

    private Integer allPushUps;

    private Integer monthDay;

    private Integer month;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Push push = (Push) o;
        return userId.equals(push.userId);
    }

    public Integer incrementPushUps(Integer toIncrement) {
        allPushUps += toIncrement;
        return allPushUps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
