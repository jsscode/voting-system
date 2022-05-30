package com.codejss.votingsystem.controllers;


import com.codejss.votingsystem.models.Candidate;
import com.codejss.votingsystem.models.Student;
import com.codejss.votingsystem.repositories.CandidateRepo;
import com.codejss.votingsystem.repositories.StudentRepo;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class VoteController {

    private final StudentRepo studentRepo;

    private final CandidateRepo candidateRepo;

    public VoteController(StudentRepo studentRepo, CandidateRepo candidateRepo) {
        this.studentRepo = studentRepo;
        this.candidateRepo = candidateRepo;
    }

    @GetMapping("/vote")
    public String index(Model model) {
        model.addAttribute("title", "Elecciones Estudiantiles Electrónicas 2022");
        return "findStudent";
    }

    @PostMapping("/vote")
    public String doLogin(@RequestParam String document, Model model, RedirectAttributes flash, HttpSession session) {

        var student = studentRepo.findByDocument(document);
        if (student != null) {
            session.setAttribute("student", student);
            if (student.getHasVoted() == null || student.getHasVoted()) {
                return "alreadyVoted";
            } else {
                List<Candidate> candidates = candidateRepo.findAll();
                model.addAttribute("title", "Elecciones Estudiantiles Electrónicas 2022");
                model.addAttribute("candidates", candidates);
                return "vote";
            }
        }
        flash.addFlashAttribute("hasVoted", "Alumno no encontrado");
        return "redirect:/vote";

    }

    @GetMapping("/voteFor")
    public synchronized String voteFor(@RequestParam Long id, HttpSession session) {


        var student = (Student) session.getAttribute("student");
        if (student != null) {
            if (!student.getHasVoted()) {
                student.setHasVoted(true);
                var candidate = candidateRepo.findById(id);
                candidate.ifPresent(value -> value.setNumbersOfVotes(value.getNumbersOfVotes() + 1));
                candidate.ifPresent(candidateRepo::save);
                studentRepo.save(student);

                return "voted";
            }
        }
        return "alreadyVoted";

    }

    @GetMapping("/kinder")
    public String kinderGarden(Model model) {

        var candidates = candidateRepo.findAll();
        if (candidates.isEmpty()) {
            return "redirect:/candidate/form";
        } else {
            model.addAttribute("title", "Elecciones Estudiantiles Electrónicas 2022");
            model.addAttribute("candidates", candidates);
            return "kinderVote";
        }

    }

    @GetMapping("/kinderVoted")
    public String kinderVoted() {
        return "kinderVoted";
    }

    @GetMapping("/results")
    public String getResults(Model model, RedirectAttributes flash) {

        var candidates = candidateRepo.findAll();

        if (candidates.isEmpty()) {
            flash.addFlashAttribute("hasVoted", "No hay Candidatos");
            return "redirect:/vote";
        }

        Long totalVotes = candidates.stream().mapToLong(Candidate::getNumbersOfVotes).sum();

        model.addAttribute("title", "Resultados de las Elecciones Estudiantiles Electrónicas 2022");
        model.addAttribute("totalVotes", totalVotes);
        model.addAttribute("candidates", candidates);
        return "results";
    }

}
