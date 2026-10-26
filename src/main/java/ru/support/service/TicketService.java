package ru.support.service;

import org.springframework.stereotype.Service;
import ru.support.entity.Ticket;
import ru.support.entity.TicketStatus;
import ru.support.entity.User;
import ru.support.repo.TicketRepository;
import ru.support.repo.UserRepository;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public List<Ticket> listForUser(Long userId, TicketStatus status) {
        if (status == null) {
            return ticketRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }
        return ticketRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
    }

    public Ticket getForUser(Long id, Long userId) {
        return ticketRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));
    }

    public Ticket create(Long userId, String subject, String description, TicketStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Ticket t = new Ticket();
        t.setUser(user);
        t.setSubject(subject);
        t.setDescription(description);
        t.setStatus(status);
        return ticketRepository.save(t);
    }

    public Ticket update(Long id, Long userId, String subject, String description, TicketStatus status) {
        Ticket t = getForUser(id, userId);
        t.setSubject(subject);
        t.setDescription(description);
        t.setStatus(status);
        return ticketRepository.save(t);
    }

    public void delete(Long id, Long userId) {
        Ticket t = getForUser(id, userId);
        ticketRepository.delete(t);
    }
}