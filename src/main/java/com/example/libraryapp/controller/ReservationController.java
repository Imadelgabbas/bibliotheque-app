package com.example.libraryapp.controller;

import com.example.libraryapp.entity.Reservation;
import com.example.libraryapp.repository.ReservationRepository;
import com.example.libraryapp.repository.UserRepository;
import com.example.libraryapp.repository.LivreRepository;
import com.example.libraryapp.entity.AppUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final LivreRepository livreRepository;

    public ReservationController(ReservationRepository reservationRepository, UserRepository userRepository, LivreRepository livreRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.livreRepository = livreRepository;
    }

    @GetMapping("/mes-reservations")
    public String mesReservations(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            var user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return "redirect:/livres";
            }

            List<Reservation> reservations = reservationRepository.findByUser(user);

            model.addAttribute("reservations", reservations);
            return "mes-reservations";
        } catch (Exception e) {
            return "redirect:/livres";
        }
    }

    @GetMapping("/admin/reservations")
    public String adminReservations(Model model) {
        try {
            // Vérifier que l'utilisateur est ADMIN
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/livres";
            }

            // Récupérer toutes les réservations
            List<Reservation> reservations = reservationRepository.findAll();

            model.addAttribute("reservations", reservations);
            return "admin-reservations";
        } catch (Exception e) {
            return "redirect:/livres";
        }
    }

    @PostMapping("/reservations/annuler/{id}")
    public String annulerReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            var reservation = reservationRepository.findById(id).orElse(null);

            if (reservation == null) {
                redirectAttributes.addFlashAttribute("error", "Réservation non trouvée.");
                return "redirect:/mes-reservations";
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            if (!reservation.getUser().getEmail().equals(email)) {
                redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas autorisé à annuler cette réservation.");
                return "redirect:/mes-reservations";
            }

            // Remettre le stock +1
            var livre = reservation.getLivre();
            livre.setStock(livre.getStock() + 1);
            livreRepository.save(livre);

            // Supprimer la réservation
            reservationRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("success", "Réservation annulée. Stock rétabli.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'annulation : " + e.getMessage());
        }

        return "redirect:/mes-reservations";
    }
}




