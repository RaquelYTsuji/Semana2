package com.semana2.Semana2.controllers;

import com.semana2.Semana2.dto.RequisicaoNovoProfessor;
import com.semana2.Semana2.models.Professor;
import com.semana2.Semana2.models.StatusProfessor;
import com.semana2.Semana2.repositories.ProfessorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
public class ProfessorController {
    @Autowired
    private ProfessorRepository professorRepository;

//    public ProfessorController(ProfessorRepository professorRepository){
//        this.professorRepository = professorRepository;
//    }

    @GetMapping("/professores")
    public ModelAndView index() {
        List<Professor> professores = this.professorRepository.findAll();
        ModelAndView mv = new ModelAndView("professores/index");
        mv.addObject("professores", professores);
        return mv;
    }

    @GetMapping("/professor/new")
    public ModelAndView nnew(){
        ModelAndView mv = new ModelAndView("professores/new");
        mv.addObject("statusProfessor", StatusProfessor.values());
        return mv;
    }

    @PostMapping("/professores")
    public String create(@Valid RequisicaoNovoProfessor requisicao, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println("\n*************** TEM ERROS ****************\n");
            return "redirect:/professor/new";
        }else{
            Professor professor = requisicao.toProfessor();
            this.professorRepository.save(professor);
            return "redirect:/professores";
        }
    }
}
