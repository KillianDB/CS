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