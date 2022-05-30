package com.codejss.votingsystem.controllers;

import com.codejss.votingsystem.models.Candidate;
import com.codejss.votingsystem.repositories.CandidateRepo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping("/candidate")
@SessionAttributes("candidate")
public class CandidateController {

    private final CandidateRepo candidateRepo;

    private final static String TITLE = "Formulario para crear Candidato";

    public CandidateController(CandidateRepo candidateRepo) {
        this.candidateRepo = candidateRepo;
    }

    @GetMapping("/form")
    public String showForm(Model model){
        model.addAttribute("title",TITLE);
        model.addAttribute("candidate",new Candidate());
        return "candidateForm";
    }

    @PostMapping("/form")
    public String save(@Valid Candidate candidate,BindingResult result, Model model,
                       @RequestParam("photocandidate") MultipartFile photo, @RequestParam("flag") MultipartFile image,
                       RedirectAttributes flash, SessionStatus status){

        if (result.hasErrors()) {
            model.addAttribute("title", TITLE);
            return "candidateForm";
        }

        var directoryPath = Paths.get("src//main//resources//static/uploads");
        var absolutePath = directoryPath.toFile().getAbsolutePath();

        uploadFile(photo, absolutePath);
        uploadFile(image, absolutePath);

        if(!photo.isEmpty()){
            candidate.setPhoto(photo.getOriginalFilename());
        }
        if (!image.isEmpty()){
            candidate.setPoliticalFlag(image.getOriginalFilename());
        }

        candidateRepo.save(candidate);
        flash.addFlashAttribute("success","El candidato se guardó correctamente");
        status.setComplete();
        return "redirect:/candidate/form";
    }

    private void uploadFile(MultipartFile image, String absolutePath) {
        if (!image.isEmpty()) {
            try {
                var bytes = image.getBytes();
                var completePath = Paths.get(absolutePath + "//" + image.getOriginalFilename());
                Files.write(completePath, bytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
