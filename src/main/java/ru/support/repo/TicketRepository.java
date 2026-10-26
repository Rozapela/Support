package ru.support.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.support.entity.Ticket;
import ru.support.entity.TicketStatus;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Ticket> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, TicketStatus status);

    Optional<Ticket> findByIdAndUserId(Long id, Long userId);
}