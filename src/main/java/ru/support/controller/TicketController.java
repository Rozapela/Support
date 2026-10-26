package ru.support.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.support.entity.Ticket;
import ru.support.entity.TicketStatus;
import ru.support.entity.User;
import ru.support.repo.UserRepository;
import ru.support.service.TicketService;

@Controller
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public TicketController(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    private Long currentUserId(Authentication auth) {
        User u = userRepository.findByLogin(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Нет пользователя"));
        return u.getId();
    }

    @GetMapping("/")
    public String list(@RequestParam(required = false) TicketStatus status,
                       Authentication auth,
                       Model model) {
        Long userId = currentUserId(auth);
        model.addAttribute("tickets", ticketService.listForUser(userId, status));
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("login", auth.getName());
        return "index";
    }

    @GetMapping("/ticket/{id}")
    public String view(@PathVariable Long id, Authentication auth, Model model) {
        Ticket t = ticketService.getForUser(id, currentUserId(auth));
        model.addAttribute("ticket", t);
        return "view";
    }

    @GetMapping("/ticket/new")
    public String newForm(Model model) {
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("isEdit", false);
        return "form";
    }

    @GetMapping("/ticket/{id}/edit")
    public String editForm(@PathVariable Long id, Authentication auth, Model model) {
        Ticket t = ticketService.getForUser(id, currentUserId(auth));
        model.addAttribute("ticket", t);
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("isEdit", true);
        return "form";
    }

    @PostMapping("/ticket/save")
    public String save(@RequestParam(required = false) Long id,
                       @RequestParam String subject,
                       @RequestParam String description,
                       @RequestParam TicketStatus status,
                       Authentication auth) {
        Long userId = currentUserId(auth);
        if (subject.isBlank() || description.isBlank()) {
            return id == null ? "redirect:/ticket/new" : "redirect:/ticket/" + id + "/edit";
        }
        if (id == null) {
            ticketService.create(userId, subject, description, status);
        } else {
            ticketService.update(id, userId, subject, description, status);
        }
        return "redirect:/";
    }

    @PostMapping("/ticket/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth) {
        ticketService.delete(id, currentUserId(auth));
        return "redirect:/";
    }
}