package com.disciplina;

import CS.disciplina.Entidade.Disciplina;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@AllArgsConstructor
public class DisciplinaController {

    public ArrayList<Disciplina> disciplinas;

    public DisciplinaController() {
        disciplinas = new ArrayList<>();
        disciplinas.add(new Disciplina("CD", "Construção de Software", "CD", null));
        disciplinas.add(new Disciplina("JK", "Probability and Statiscs", "JK", null));
        disciplinas.add(new Disciplina("EF", "Infraestrutura para gestão de dados", "EF", null));
        disciplinas.add(new Disciplina("LM", "Matemática aplicada em geofísica", "LM", null));
        disciplinas.add(new Disciplina("NP", "Fundamentos de processamento paralelo e distribuído", "NP", null));

    }
    @PostMapping("/cadEstudante")
    public void cadEstudante(@RequestParam String estudante, @RequestParam String disciplina ) {
        for (Disciplina disciplina1 : disciplinas) {
            if (disciplina1.equals(disciplina))
        disciplinas.add(disciplina1);
        }
    }

    @GetMapping("/disciplinas")
    public ArrayList<Disciplina> getDisciplinas() {return getDisciplinas();}

    @GetMapping("/horaDisciplina")
    public ArrayList<Disciplina> getHoraDisciplina() {return getHoraDisciplina();}
}