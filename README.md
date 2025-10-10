Microsserviços Usuario, Turma e Reserva - refazendo o SARC da PUCRS (Trabalho da disciplina de Construção de Software)

Este projeto consiste em três microsserviços Spring Boot:
- *Turma Service* - Gerencia turmas e disciplinas (porta 8081)
- *Usuario Service* - Gerencia estudantes, professores e administradores (porta 8082)
- *Reserva Service* - Gerencia reservas de salas e de perifericos (porta 8083)

Ambos os microsserviços utilizam:
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- Banco de dados H2 (em memória)
- Docker para containerização
  

## Modelagem lógica BD

<img width="1049" height="573" alt="image" src="https://github.com/user-attachments/assets/5787921f-4b7a-441c-a603-93cd2240e011" />


## Como executar

### Pré-requisitos
- Docker
- Docker Compose

### Executando com Docker Compose

1. *Build e executar todos os serviços:*
```bash
docker-compose up -d --build
```

2. *Parar os serviços:*
```bash
docker-compose down
```

3. *Ver logs de todos os serviços:*
```bash
docker-compose logs -f
```

4. *Ver logs de serviços específicos:*
```bash
docker-compose logs -f turma-service
docker-compose logs -f usuario-service
docker-compose logs -f reserva-service
```

### Executando individualmente

*Turma Service:*
```bash
cd turma
docker build -t turma-service .
docker run -p 8081:8081 turma-service
```

*Usuario Service:*
```bash
cd usuario
docker build -t usuario-service .
docker run -p 8082:8082 usuario-service
```

*Reserva Service:*
```bash
cd reversa
docker build -t reserva-service .
docker run -p 8083:8083 reserva-service
```

## **APIs Disponíveis**

### Turma Service (porta 8081)

*Base URL:* `http://localhost:8081/turmas`

- `GET /turmas/codigo/{codigo}` - Buscar turma por código
- `POST /turmas` - Criar nova turma
- `POST /turmas/codigo/{codigo}/estudantes/{estudanteId}` - Adicionar estudante à turma
- `GET /disciplinas/nome/{nome}` - Buscar disciplinas por nome
- `GET /turmas/codigo/{codigo}/horario` - Buscar horário da turma por código
- `GET /disciplinas/health` - Health check disciplina
- `GET /turmas/health` - Health check turma

### Usuario Service (porta 8082)

*Base URL:* `http://localhost:8082/usuarios`

- `GET /usuarios/matricula/{matricula}` - Buscar usuario por matrícula
- `GET /usuarios/nome/{nome}` - Buscar usuario por nome
- `POST /usuarios` - Criar novo usuario
- `GET /usuarios/health` - Health check usuario
  
### Reserva Service (porta 8083)

*Base URL:* `http://localhost:8083/reservas`

- `GET /reservas/codigo/{codigo}` - Buscar reserva por codigo
- `GET /reservas/horario/{horario}` - Buscar reserva por horario
- `POST /reservas` - Criar nova reserva
- `GET /reservas/health` - Health check reservas

## Console H2

Para acessar o console do banco H2 para debugging:

- *Turma Service:* http://localhost:8081/h2-console
- *Usuario Service:* http://localhost:8082/h2-console
- *Reserva Service:* http://localhost:8083/h2-console

*Configurações de conexão:*
- JDBC URL: `jdbc:h2:mem:turma_db` (ou `usuario_db`, ou `reserva_db`)
- Username: `sa`
- Password: (deixar em branco)

## Exemplos de uso

### Criar uma disciplina
```bash
curl -X POST http://localhost:8081/disciplinas \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "CS101",
    "nome": "Introdução à Computação"
  }'
```

### Criar um estudante
```bash
curl -X POST http://localhost:8082/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "matricula": "20230001",
    "email": "joao.silva@edu.pucrs.br",
    "senha": "123456",
    "tipoUsuario": "ESTUDANTE"
  }'
```

### Adicionar estudante a uma turma
```bash
curl -X POST http://localhost:8081/turmas/CS101/estudante/1
```

## Arquitetura

```
┌─────────────────────┐    ┌─────────────────────┐  ┌─────────────────────┐
│  Turma Service      │    │    Usuario Service  │  |  Reserva Service    |
│     (Port 8081)     │    │     (Port 8082)     │  |   (Port 8083)       |
├─────────────────────┤    ├─────────────────────┤  ├─────────────────────┤
│  REST Controller    │    │  REST Controller    │  │  REST Controller    │  
│  Service Layer      │    │  Service Layer      │  │  Service Layer      │
│  Repository (JPA)   │    │  Repository (JPA)   │  │  Repository (JPA)   │
│  H2 Database        │    │  H2 Database        │  │  H2 Database        │
└─────────────────────┘    └─────────────────────┘  └─────────────────────┘
```

## Estrutura do Projeto

```
CS/
├── .github
│   └── workflows
|       └── dev_pr.yml
├── turma
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── reserva
│   │   │   │           ├── ReservaApplication.java
│   │   │   │           ├── ReservaController.java
│   │   │   │           ├── entidade
│   │   │   │           │   └── Reserva.java
│   │   │   │           │   └── ReservaSala.java
│   │   │   │           │   └── ReservaPeriferico.java
│   │   │   │           │   └── Sala.java
│   │   │   │           │   └── Periferico.java
│   │   │   │           │   └── TipoSala.java
│   │   │   │           │   └── TipoPeriferico.java
│   │   │   │           └── repository
│   │   │   │               └── ReservaRepository.java
│   │   │   └── resources
│   │   │       └── application.properties
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── reserva
│   │                   ├── ReservaControllerTest.java
│   │                   └── entidade
│   │                       └── SalaTest.java
│   │                       └── PerifericoTest.java
│   │                       └── ReservaSalaTest.java
│   │                       └── ReservaPerifericoTest.java
│   │                       └── ReservaTest.java
│   └── target
│       └── classes
│           └── application.properties
├── turma
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── turma
│   │   │   │           ├── TurmaApplication.java
│   │   │   │           ├── DisciplinaController.java
│   │   │   │           ├── TurmaController.java
│   │   │   │           ├── entidade
│   │   │   │           │   └── Disciplina.java
│   │   │   │           │   └── Turma.java
│   │   │   │           └── repository
│   │   │   │               └── DisciplinaRepository.java
│   │   │   │               └── TurmaRepository.java
│   │   │   └── resources
│   │   │       └── application.properties
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── turma
│   │                   ├── DisciplinaControllerTest.java
│   │                   ├── TurmaControllerTest.java
│   │                   └── entidade
│   │                       └── DisciplinaTest.java
│   │                       └── TurmaTest.java
│   └── target
│       └── classes
│           └── application.properties
│
├── usuario
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── usuario
│   │   │   │           ├── UsuarioApplication.java
│   │   │   │           ├── UsuarioController.java
│   │   │   │           ├── entidade
│   │   │   │           │   └── Usuario.java
│   │   │   │           │   └── TipoUsuario.java
│   │   │   │           └── repository
│   │   │   │               └── UsuarioRepository.java
│   │   │   └── resources
│   │   │       └── application.properties
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── usuario
│   │                   ├── UsuarioControllerTest.java
│   │                   └── entidade
│   │                       └── UsuarioTest.java
│   └── target
│       └── classes
│           └── application.properties
│
├── README.md
└── docker-compose.yml
```

## Resolução de problemas comuns

1. *Porta já em uso:*
   - Verifique se as portas 8081 e 8082 estão livres
   - Use `netstat -an | findstr "8081"` para verificar

2. *Erro de build do Docker:*
   - Certifique-se de que o Docker está executando
   - Tente limpar o cache: `docker system prune -a`

3. *Aplicação não responde:*
   - Verifique os logs: `docker-compose logs -f`
   - Verifique os health checks: `docker ps`

## Desenvolvimento

Para desenvolvimento local sem Docker:

1. Execute cada aplicação Spring Boot diretamente:
```bash
# Terminal 1 - Turma Service
cd turma
mvn spring-boot:run

# Terminal 2 - Usuario Service  
cd usuario
mvn spring-boot:run

#Terminal 3 - Reserva Service
cd reserva
mvn spring-boot:run
```

Os serviços estarão disponíveis nas mesmas portas (8081, 8082 e  8083).
