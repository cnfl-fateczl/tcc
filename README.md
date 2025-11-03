# Gerencia Restaurante

Projeto Spring Boot com MySQL, containerizado com Docker, pronto para rodar sem precisar instalar Java ou MySQL localmente.

---

## Estrutura de Pacotes (Arquitetura Hexagonal)

```
com.gerencia_restaurante
│
├── adapters             # Implementações externas
│   ├── api              # Conexões com apis
│   └──  database        # Conexão com banco de dados
│
├── application          # Casos de uso / regras de negócio
│   └──  service          # Serviços da aplicação
│
├── domain               # Modelo de domínio
│   ├── entity           # Entidades do negócio
│   ├── valueobject      # Objetos de valor
│   └── exception        # Exceções do domínio
│
└── config               # Configurações da aplicação
```

> Observação: a camada de **persistência** implementa as interfaces definidas nos `ports` de saída e conecta o domínio ao banco de dados.

---

## Pré-requisitos

1. **Docker** instalado na sua máquina

    * [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)
    * Certifique-se de que o Docker está rodando (`docker --version`).

2. **Docker Compose** (vem incluso no Docker Desktop)

> Não é necessário instalar Java, Maven ou MySQL na máquina.

---

## Passo 1: Clonar o projeto

```bash
git clone <URL_DO_SEU_REPOSITORIO>
cd tcc
```

---

## Passo 2: Configurar variáveis de banco (opcional)

No `docker-compose.yml` você pode ajustar:

```yaml
MYSQL_ROOT_PASSWORD: root123
MYSQL_DATABASE: gerencia
MYSQL_USER: user
MYSQL_PASSWORD: user123
```

Esses dados também devem estar em `application.properties` do Spring Boot.

---

## Passo 3: Construir e subir os containers

```bash
docker compose up --build
```

O Docker irá:

* Criar o container MySQL (`mysql_gerencia`)
* Criar o container da aplicação Spring Boot (`gerencia_restaurante_app`)
* Mapear as portas padrão:

    * MySQL: `3306` (ou altere no `docker-compose.yml`)
    * Spring Boot: `8080`

> Se aparecer erro de porta ocupada, altere a porta do host no `docker-compose.yml`:
>
> ```yaml
> ports:
>   - "3308:3306"   # Host:Container
> ```

---

## Passo 4: Acessar a aplicação

* Spring Boot: [http://localhost:8080](http://localhost:8080)
* MySQL: use qualquer cliente (DBeaver, TablePlus) com as credenciais configuradas.

---

## Passo 5: Parar os containers

```bash
docker compose down
```

Isso remove os containers, mas mantém os volumes do MySQL.
Se quiser remover também os volumes:

```bash
docker compose down -v
```

---

## Observações

* O projeto já contém o **MySQL Connector**, então o Spring Boot consegue conectar automaticamente.
* A aplicação é empacotada como **fat jar**, então todas as dependências já estão incluídas no container.
