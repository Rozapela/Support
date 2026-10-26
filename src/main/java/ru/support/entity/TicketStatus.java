package ru.support.entity;

public enum TicketStatus {
    OPEN("Открыта"),
    IN_PROGRESS("В работе"),
    CLOSED("Закрыта");

    private final String title;

    TicketStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}