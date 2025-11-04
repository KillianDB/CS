package com.turma.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.turma.dto.TurmaDTO;
import com.turma.entidade.Disciplina;
import com.turma.repository.DisciplinaRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.turma.dto.MatriculaDTO;
import com.turma.entidade.Turma;
import com.turma.repository.TurmaRepository;

@Service
public class TurmaService {
    private final TurmaRepository turmaRepository;
    private final DisciplinaRepository disciplinaRepository;

    @Autowired
    public TurmaService(TurmaRepository turmaRepository, DisciplinaRepository disciplinaRepository) {
        this.turmaRepository = turmaRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public String criaTurma(TurmaDTO turmaDTO) {
        String codigo = turmaDTO.getCodigo();

        if (turmaRepository.existsByCodigo(codigo)) {
            throw new IllegalArgumentException("Já existe uma turma com o código: " + codigo);
        }

        Disciplina disciplina = disciplinaRepository.findDisciplinaByCodigo(turmaDTO.getDisciplinaCodigo());

        if(disciplina == null){
            throw new RuntimeException("Disciplina não encontrada");
        }

        Turma turma = new Turma(
                turmaDTO.getCodigo(),
                turmaDTO.getHorario(),
                disciplina,
                turmaDTO.getIdProfessor()
        );

        return turmaRepository.save(turma).getCodigo();
    }

    public void atualizaCalendario(String codigoTurma, String horario) throws Exception {
        Optional<Turma> turma = turmaRepository.findById(codigoTurma);

        if(turma.isEmpty()) throw new Exception("A turma não existe");

        turma.get().setHorario(horario);
        turmaRepository.save(turma.get());
    }

    public void addAlunos(MultipartFile arquivo) throws Exception {
        String extensaoArquivo = arquivo.getContentType();
        List<MatriculaDTO> matriculas;
        assert extensaoArquivo != null;
        if (extensaoArquivo.equals("text/csv"))
            matriculas = lerCSV(arquivo);
        else
            matriculas = lerXLSX(arquivo);

        Map<String, List<String>> turmaEstudante = matriculas.stream()
                .collect(Collectors.groupingBy(
                        MatriculaDTO::codTurma,
                        Collectors.mapping(MatriculaDTO::codEstudante, Collectors.toList())));

        List<String> codTurmas = new ArrayList<>(turmaEstudante.keySet());
        List<Turma> turmas = turmaRepository.findByCodigoIn(codTurmas);

        Map<String, Turma> turmaMap = turmas.stream()
                .collect(Collectors.toMap(Turma::getCodigo, Function.identity()));

        for (var entry : turmaEstudante.entrySet()) {
            Turma turma = turmaMap.get(entry.getKey());
            if (turma == null)
                continue;

            for (String codEstudantes : entry.getValue()) {
                turma.adicionaEstudante(codEstudantes);
            }
        }

        turmaRepository.saveAll(turmas);
    }

    public List<MatriculaDTO> lerCSV(MultipartFile csv) throws IOException {
        try (InputStream stream = csv.getInputStream()) {
            String line;
            String delimiter = ",";
            List<MatriculaDTO> matriculas = new ArrayList<>();

            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(delimiter);
                String codigo = data[0];
                String estudante = data[1];
                matriculas.add(new MatriculaDTO(codigo, estudante));
            }

            return matriculas;
        }
    }

    public List<MatriculaDTO> lerXLSX(MultipartFile xlsx) throws Exception {
        Workbook workbook = WorkbookFactory.create(xlsx.getInputStream());

        List<MatriculaDTO> matriculas = new ArrayList<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);

            for (Row row : sheet) {
                Cell cellCodTurma = row.getCell(0);
                Cell cellCodEstudante = row.getCell(1);
                String codigo;
                String estudante;
                if (cellCodTurma.getCellType() == CellType.STRING
                        && cellCodEstudante.getCellType() == CellType.STRING) {
                    codigo = cellCodTurma.getRichStringCellValue().getString();
                    estudante = cellCodEstudante.getRichStringCellValue().getString();
                    matriculas.add(new MatriculaDTO(codigo, estudante));
                } else {
                    throw new Exception("XLSX com o formato errado");
                }
            }
        }
        return matriculas;
    }
}