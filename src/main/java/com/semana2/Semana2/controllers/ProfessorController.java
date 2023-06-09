package com.semana2.Semana2.controllers;

import com.semana2.Semana2.dto.RequisicaoFormProfessor;
import com.semana2.Semana2.models.Professor;
import com.semana2.Semana2.models.StatusProfessor;
import com.semana2.Semana2.repositories.ProfessorRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping(value = "/professores")
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

    @GetMapping("/professores/new")
    public ModelAndView nnew(RequisicaoFormProfessor requisicao){
        ModelAndView mv = new ModelAndView("professores/new");
        mv.addObject("listaStatusProfessor", StatusProfessor.values());
        return mv;
    }

    @PostMapping("/professores")
    public ModelAndView create(@Valid RequisicaoFormProfessor requisicao, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println("\n*************** TEM ERROS ****************\n");
            ModelAndView mv = new ModelAndView("professores/new");
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        }else{
            Professor professor = requisicao.toProfessor();
            this.professorRepository.save(professor);
            return new ModelAndView("redirect:/professores/" + professor.getId());
        }
    }

    @GetMapping("/professores/{id}")
    public ModelAndView show(@PathVariable Long id){
        Optional<Professor> optional = this.professorRepository.findById(id);
        if (optional.isPresent()){
            Professor professor = optional.get();
            ModelAndView mv = new ModelAndView("professores/show");
            mv.addObject("professor", professor);
            return mv;
        }else{
            System.out.println("$$$$$$$$$$ Não achou o professor de ID: " +id+ "$$$$$$$$$$$$$");
            return this.retornaErroProfessor("SHOW ERROR: Professor #" + id + " nao encontrado no banco");
        }
    }

    @GetMapping("/professores/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormProfessor requisicao){
        Optional<Professor> optional = this.professorRepository.findById(id);
        if (optional.isPresent()){
            Professor professor = optional.get();
            requisicao.fromProfessor(professor);
            ModelAndView mv = new ModelAndView("professores/edit");
            mv.addObject("professorID", professor.getId());
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        }else{
            System.out.println("$$$$$$$$$$ Não achou o professor de ID: " +id+ "$$$$$$$$$$$$$");
            return this.retornaErroProfessor("EDIT ERROR: Professor #" + id + " nao encontrado no banco");
        }
    }

    @PostMapping("/professores/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid RequisicaoFormProfessor requisicao, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println("\n*************** TEM ERROS ****************\n");
            ModelAndView mv = new ModelAndView("professores/edit");
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        }else{
            Optional<Professor> optional = this.professorRepository.findById(id);
            if (optional.isPresent()){
                Professor professor = requisicao.toProfessor(optional.get());
                this.professorRepository.save(professor);

//                professor.setNome(requisicao.getNome());
//                professor.setSalario(requisicao.getSalario());
//                professor.setStatusProfessor(requisicao.getStatusProfessor());
                return new ModelAndView("redirect:/professores/" + professor.getId());
            }else{
                System.out.println("########## Não achou o professor de ID: " +id+ "##########");
                return this.retornaErroProfessor("UPDATE ERROR: Professor #" + id + " nao encontrado no banco");
            }
        }
    }

    @GetMapping("/professores/{id}/delete")
    public ModelAndView delete(@PathVariable Long id){
        ModelAndView mv = new ModelAndView("redirect:/professores");

        try{
            this.professorRepository.deleteById(id);
            mv.addObject("mensagem", "Professor #" + id + " deletado com sucesso");
            mv.addObject("erro", false);
        }catch (EmptyResultDataAccessException e){
            mv = this.retornaErroProfessor("DELETE ERROR: Professor #" + id + " nao encontrado no banco");
            System.out.println(e);
        }
        return mv;
    }

    private ModelAndView retornaErroProfessor(String msg){
        ModelAndView mv = new ModelAndView("redirect:/professores");
        mv.addObject("mensagem", msg);
        mv.addObject("erro", true);
        return mv;
    }

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }
}
