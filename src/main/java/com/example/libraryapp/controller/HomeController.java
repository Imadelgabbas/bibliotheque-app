package com.example.libraryapp.controller;

import com.example.libraryapp.repository.LivreRepository;
import com.example.libraryapp.repository.AuteurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final LivreRepository livreRepository;
    private final AuteurRepository auteurRepository;

    public HomeController(LivreRepository livreRepository, AuteurRepository auteurRepository) {
        this.livreRepository = livreRepository;
        this.auteurRepository = auteurRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        long totalLivres = livreRepository.count();
        long totalAuteurs = auteurRepository.count();

        model.addAttribute("totalLivres", totalLivres);
        model.addAttribute("totalAuteurs", totalAuteurs);
        model.addAttribute("dernierLivre", livreRepository.findFirstByOrderByIdDesc().orElse(null));

        return "home";
    }
}