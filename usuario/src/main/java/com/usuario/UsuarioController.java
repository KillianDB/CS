package com.usuario;

import com.usuario.entidade.Usuario;
import com.usuario.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<Usuario> buscarPorMatricula(@PathVariable String matricula) {
        Optional<Usuario> usuario = usuarioRepository.findByMatricula(matricula);
        return usuario.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Usuario>> buscarPorNome(@PathVariable String nome) {
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome);
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            if (usuario.getMatricula() != null && 
                usuarioRepository.existsByMatricula(usuario.getMatricula())) {
                return ResponseEntity.badRequest()
                    .body("Já existe um usuário com a matrícula: " + usuario.getMatricula());
            }

            if (usuario.getEmail() != null &&
                usuarioRepository.existsByEmail(usuario.getEmail())) {
                return ResponseEntity.badRequest()
                    .body("Já existe um usuário com o email: " + usuario.getEmail());
            }

            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Usuário Service is running on port 8082");
    }
}