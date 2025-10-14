package com.turma.service;

import com.turma.entidade.Turma;
import com.turma.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurmaService {
    private TurmaRepository turmaRepository;

    @Autowired
    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    public String criaTurma(Turma turma){
        if (turmaRepository.existsByCodigo(turma.getCodigo())) {
            throw new IllegalArgumentException("Já existe uma turma com o código: " + turma.getCodigo());
        }
        turmaRepository.save(turma);
        return turma.getCodigo();
    }
}