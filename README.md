# 🍽️ Gerencia Restaurante

Projeto desenvolvido em **Spring Boot** com **MySQL**, utilizando **Docker** para containerização — pronto para rodar sem precisar instalar Java ou MySQL localmente.

---

## 🧱 Estrutura de Pacotes (Arquitetura Hexagonal)

```
com.gerencia_restaurante
│
├── adapters                 # Implementações externas
│   ├── api                  # Integrações externas / endpoints REST
│   ├── database             # Conexão e persistência com o banco de dados
│   └── web                  # Controladores REST (ex: ProdutoController)
│
├── application              # Casos de uso e lógica de negócio
│   ├── mapper               # Conversões entre entidades e DTOs
│   ├── port.in              # Portas de entrada (use cases)
│   └── service              # Serviços principais da aplicação
│
├── domain                   # Modelo de domínio
│   ├── entity               # Entidades principais (ex: Produto)
│   ├── exception            # Exceções específicas do domínio
│   └── valueobject          # Objetos de valor
│
├── repository               # Interfaces e implementações de persistência
│
└── config                   # Configurações gerais da aplicação
```

> 💡 A camada de **adapters** implementa as interfaces definidas em **ports**, conectando o domínio com o mundo externo (banco de dados, APIs etc).

---

## ⚙️ Pré-requisitos

1. **Docker** instalado
    * [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)
    * Verifique a instalação:
      ```bash
      docker --version
      ```

2. **Docker Compose** (já incluso no Docker Desktop)

> ✅ Não é necessário instalar **Java**, **Maven** ou **MySQL** localmente.

---

## 🚀 Como rodar o projeto

### 🧩 1. Clonar o repositório

```bash
git clone <URL_DO_SEU_REPOSITORIO>
cd gerencia-restaurante
```

---

### ⚙️ 2. (Opcional) Configurar variáveis do banco

Edite o arquivo `docker-compose.yml` caso deseje alterar as credenciais:

```yaml
MYSQL_ROOT_PASSWORD: root123
MYSQL_DATABASE: gerencia
MYSQL_USER: user
MYSQL_PASSWORD: user123
```

Esses valores também devem estar refletidos no `application.properties` do projeto.

---

### 🐳 3. Construir e subir os containers

```bash
docker compose up --build
```

O Docker irá:

* Criar o container do **MySQL** (`mysql_gerencia`)
* Criar o container da aplicação **Spring Boot** (`gerencia_restaurante_app`)
* Mapear as portas:

| Serviço        | Porta Container | Porta Host |
|----------------|-----------------|-------------|
| MySQL          | 3306            | 3306        |
| Spring Boot API| 8080            | 8080        |

> ⚠️ Se ocorrer erro de porta ocupada, altere o mapeamento no `docker-compose.yml`:
>
> ```yaml
> ports:
>   - "3308:3306"   # Host:Container
> ```

---

### 🌐 4. Acessar a aplicação

* **API Base:** [http://localhost:8080](http://localhost:8080)
* **MySQL:** acesse via cliente (DBeaver, TablePlus etc.)

---

## 🧾 Endpoints REST (CRUD de Produto)

| Método | Endpoint | Descrição |
|--------|-----------|------------|
| **GET** | `/produto/listagem` | Retorna todos os produtos cadastrados |
| **GET** | `/produto/{id}` | Retorna um produto específico pelo ID |
| **GET** | `/produto/nome?nome={nome}` | Busca produtos pelo nome |
| **POST** | `/produto` | Cadastra um novo produto |
| **PUT** | `/produto/{id}` | Atualiza um produto existente |
| **DELETE** | `/produto/{id}` | Remove um produto pelo ID |

📦 **Exemplo de JSON (POST / PUT)**

```json
{
  "nome": "Burrito de Frango",
  "categoria": "Burritos",
  "descricao": "Burrito de frango grelhado com queijo artesanal e pimenta",
  "precoProduto": 31.90
}
```

---

## 🔁 Atualizando a imagem Docker após mudanças no código

Sempre que o projeto for atualizado (novas classes, mudanças em endpoints etc.), siga este procedimento:

```bash
# 1. Parar e remover containers antigos
docker compose down

# 2. Reconstruir a imagem com o novo código
docker compose up --build
```

Isso recompila o projeto dentro do container e aplica todas as alterações.

---

## 🧨 Parar containers

```bash
docker compose down
```

Para remover volumes do banco também:

```bash
docker compose down -v
```

---

## 💡 Observações Finais

* O **Spring Boot** utiliza o conector **MySQL JDBC** já configurado no projeto.
* O build gera um **fat jar**, incluindo todas as dependências no container.
* Ideal para **ambientes de desenvolvimento** e **testes rápidos** sem instalação local.

---

Commit de teste por Léo - 02.11
Commit de teste por Nic - 02.11
Commit de teste por Cas - 03.11
