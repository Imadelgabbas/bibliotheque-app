package com.example.libraryapp.controller;

import com.example.libraryapp.entity.Auteur;
import com.example.libraryapp.repository.AuteurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuteurController {

    private final AuteurRepository auteurRepository;

    public AuteurController(AuteurRepository auteurRepository) {
        this.auteurRepository = auteurRepository;
    }

    @GetMapping("/auteurs")
    public String listeAuteurs(Model model) {
        model.addAttribute("auteurs", auteurRepository.findAll());
        return "auteurs";
    }

    @GetMapping("/auteurs/ajouter")
    public String afficherFormulaire(Model model) {
        model.addAttribute("auteur", new Auteur());
        return "ajouter-auteur";
    }

    @PostMapping("/auteurs/save")
    public String saveAuteur(Auteur auteur) {
        auteurRepository.save(auteur);
        return "redirect:/auteurs";
    }

    @GetMapping("/auteurs/edit/{id}")
    public String afficherFormulairModification(@PathVariable Long id, Model model) {
        model.addAttribute("auteur", auteurRepository.findById(id).orElse(null));
        return "modifier-auteur";
    }

    @PostMapping("/auteurs/update")
    public String updateAuteur(Auteur auteur) {
        auteurRepository.save(auteur);
        return "redirect:/auteurs";
    }

    @GetMapping("/auteurs/delete/{id}")
    public String deleteAuteur(@PathVariable Long id) {
        auteurRepository.deleteById(id);
        return "redirect:/auteurs";
    }
}

