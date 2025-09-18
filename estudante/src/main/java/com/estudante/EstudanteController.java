package com.estudante;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController("/estudante")
public class EstudanteController {
    ArrayList<Estudante> estudantes;
    public EstudanteController() {
         estudantes = new ArrayList<>();
    };

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<Estudante> getEstudante(@PathVariable("matricula") String matricula) {
        Estudante resp = estudantes.stream()
                .filter(estudante -> estudante.getId().equals(matricula))
                .findFirst()
                .orElse(null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp);
    };

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Estudante>> getEstudanteByName(@PathVariable("nome") String nome) {
        List<Estudante> resp = estudantes.stream()
                .filter(estudante -> estudante.getName().contains(nome))
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(resp);
    }

    @PostMapping("/")
    public ResponseEntity<Estudante> createEstudante(@RequestBody Estudante estudante) {
        estudantes.add(estudante);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}