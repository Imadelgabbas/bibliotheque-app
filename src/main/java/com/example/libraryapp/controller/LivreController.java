package com.example.libraryapp.controller;

import com.example.libraryapp.entity.Livre;
import com.example.libraryapp.repository.LivreRepository;
import com.example.libraryapp.repository.AuteurRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
public class LivreController {

    private final LivreRepository livreRepository;
    private final AuteurRepository auteurRepository;

    public LivreController(LivreRepository livreRepository, AuteurRepository auteurRepository) {
        this.livreRepository = livreRepository;
        this.auteurRepository = auteurRepository;
    }

    @GetMapping("/livres")
    public String listeLivres(
            @RequestParam(value = "titre", required = false) String titre,
            @RequestParam(value = "auteurId", required = false) Long auteurId,
            @RequestParam(value = "datePublication", required = false) String datePublication,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 5);
        Page<Livre> livresPage;

        // Appliquer le filtre selon le paramètre rempli
        if (titre != null && !titre.isEmpty()) {
            livresPage = livreRepository.findByTitreContainingIgnoreCase(titre, pageable);
        } else if (auteurId != null) {
            livresPage = livreRepository.findByAuteurId(auteurId, pageable);
        } else if (datePublication != null && !datePublication.isEmpty()) {
            livresPage = livreRepository.findByDatePublication(datePublication, pageable);
        } else {
            livresPage = livreRepository.findAll(pageable);
        }

        model.addAttribute("livresPage", livresPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", livresPage.getTotalPages());
        model.addAttribute("auteurs", auteurRepository.findAll());
        return "livres";
    }

    @GetMapping("/livres/ajouter")
    public String afficherFormulaire(Model model) {
        model.addAttribute("livre", new Livre());
        model.addAttribute("auteurs", auteurRepository.findAll());
        return "ajouter-livre";
    }

    @PostMapping("/livres/save")
    public String saveLivre(Livre livre, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            livreRepository.save(livre);
            redirectAttributes.addFlashAttribute("success", "Livre ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout du livre.");
        }
        return "redirect:/livres";
    }

    @GetMapping("/livres/delete/{id}")
    public String deleteLivre(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            livreRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Livre supprimé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du livre.");
        }
        return "redirect:/livres";
    }

    @GetMapping("/livres/edit/{id}")
    public String afficherFormulairModification(@PathVariable Long id, Model model) {
        model.addAttribute("livre", livreRepository.findById(id).orElse(null));
        model.addAttribute("auteurs", auteurRepository.findAll());
        return "modifier-livre";
    }

    @PostMapping("/livres/update")
    public String updateLivre(Livre livre, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            livreRepository.save(livre);
            redirectAttributes.addFlashAttribute("success", "Livre modifié avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification du livre.");
        }
        return "redirect:/livres";
    }
}