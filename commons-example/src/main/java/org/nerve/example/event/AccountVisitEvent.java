package org.nerve.example.event;

import org.springframework.context.ApplicationEvent;

public class AccountVisitEvent extends ApplicationEvent {

    private String name;

    public AccountVisitEvent(String name) {
        super(name);

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
