Microsserviços Estudante e Disciplina - refazendo o SARC da PUCRS (Trabalho da disciplina de Construção de Software)

Este projeto consiste em dois microsserviços Spring Boot:
- *Disciplina Service* - Gerencia disciplinas (porta 8081)
- *Estudante Service* - Gerencia estudantes (porta 8082)

Ambos os microsserviços utilizam:
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- Banco de dados H2 (em memória)
- Docker para containerização

Modelagem lógica BD
<img width="264" height="448" alt="image" src="https://github.com/user-attachments/assets/7d2a2753-3924-4f99-9583-beacd52a9a99" />


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
docker-compose logs -f disciplina-service
docker-compose logs -f estudante-service
```

### Executando individualmente

*Disciplina Service:*
```bash
cd disciplina
docker build -t disciplina-service .
docker run -p 8081:8081 disciplina-service
```

*Estudante Service:*
```bash
cd estudante
docker build -t estudante-service .
docker run -p 8082:8082 estudante-service
```

## **APIs Disponíveis**

### Disciplina Service (porta 8081)

*Base URL:* `http://localhost:8081/disciplina`

- `GET /disciplina/codigo/{codigo}` - Buscar disciplina por código
- `POST /disciplina` - Criar nova disciplina
- `POST /disciplina/codigo/{codigo}/estudantes/{estudanteId}` - Adicionar estudante à disciplina
- `GET /disciplina/nome/{nome}` - Buscar disciplinas por nome
- `GET /disciplina/codigo/{codigo}/horario` - Buscar horário da disciplina por código
- `GET /disciplina/health` - Health check

### Estudante Service (porta 8082)

*Base URL:* `http://localhost:8082/estudante`

- `GET /estudante/matricula/{matricula}` - Buscar estudante por matrícula
- `GET /estudante/nome/{nome}` - Buscar estudantes por nome
- `POST /estudante` - Criar novo estudante
- `GET /estudante/health` - Health check

## Console H2

Para acessar o console do banco H2 para debugging:

- *Disciplina Service:* http://localhost:8081/h2-console
- *Estudante Service:* http://localhost:8082/h2-console

*Configurações de conexão:*
- JDBC URL: `jdbc:h2:mem:disciplina_db` (ou `estudante_db`)
- Username: `sa`
- Password: (deixar em branco)

## Exemplos de uso

### Criar uma disciplina
```bash
curl -X POST http://localhost:8081/disciplina \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "CS101",
    "nome": "Introdução à Computação",
    "horario": "2NP-4NP"
  }'
```

### Criar um estudante
```bash
curl -X POST http://localhost:8082/estudante \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "matricula": "20230001",
    "email": "joao.silva@edu.pucrs.br"
  }'
```

### Adicionar estudante a uma disciplina
```bash
curl -X POST http://localhost:8081/disciplinas/CS101/estudante/1
```

## Arquitetura

```
┌─────────────────────┐    ┌─────────────────────┐
│  Disciplina Service │    │  Estudante Service  │
│     (Port 8081)     │    │     (Port 8082)     │
├─────────────────────┤    ├─────────────────────┤
│  REST Controller    │    │  REST Controller    │
│  Service Layer      │    │  Service Layer      │
│  Repository (JPA)   │    │  Repository (JPA)   │
│  H2 Database        │    │  H2 Database        │
└─────────────────────┘    └─────────────────────┘
```

## Estrutura do Projeto

```
CS/
├── disciplina/
│   ├── src/main/java/com/disciplina/
│   │   ├── DisciplinaApplication.java
│   │   ├── DisciplinaController.java
│   │   ├── entidade/Disciplina.java
│   │   └── repository/DisciplinaRepository.java
│   ├── src/main/resources/application.properties
│   ├── Dockerfile
│   └── pom.xml
├── estudante/
│   ├── src/main/java/com/estudante/
│   │   ├── EstudanteApplication.java
│   │   ├── EstudanteController.java
│   │   ├── entidade/Estudante.java
│   │   └── repository/EstudanteRepository.java
│   ├── src/main/resources/application.properties
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml
└── README.md
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
# Terminal 1 - Disciplina Service
cd disciplina
mvn spring-boot:run

# Terminal 2 - Estudante Service  
cd estudante
mvn spring-boot:run
```

Os serviços estarão disponíveis nas mesmas portas (8081 e 8082).
