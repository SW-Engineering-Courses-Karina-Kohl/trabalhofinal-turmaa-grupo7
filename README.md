[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/RBBavBFg)

# 🏭 Auditor de Carbono Industrial (EcoFactory)

Este projeto é uma aplicação Java Web clássica desenvolvida para calcular e auditar a emissão de gases de efeito estufa (CO₂) gerados pelo consumo de energia de máquinas industriais. 

A aplicação processa o consumo energético em kWh e o converte em emissões de carbono, gerando um ranking de criticidade ambiental por setor. O sistema funciona como um servidor web real, executado dentro de um contêiner **Docker** com **Apache Tomcat**, utilizando **Servlets** e **JSP**.

---

## 🚀 Tecnologias e Ferramentas

* **Linguagem:** Java 17
* **Web/Servidor:** Jakarta EE 6.0 (Servlets, JSP, JSTL), Apache Tomcat 10.1
* **Gerenciador de Dependências:** Maven
* **Infraestrutura:** Docker & Docker Compose
* **Testes e Qualidade:** JUnit 5.10, JaCoCo (Cobertura de Código)

---

## 🏗️ Arquitetura do Projeto (MVC)

O projeto foi estruturado seguindo o padrão **Model-View-Controller (MVC)** para garantir a separação de responsabilidades e facilitar a manutenção:

* **Model (`br.edu.ufrgs.model`):** Entidades de domínio que representam o consumo das máquinas, configurações dos setores, totalizadores e o enum de status ambiental.
* **Service (`br.edu.ufrgs.service`):** O motor de cálculo (`AuditoriaCarbonoService`). Concentra a lógica de agregação de consumo, aplicação dos fatores de emissão e definição de status.
* **Controller (`br.edu.ufrgs.controller`):** Servlets (`AuditoriaServlet`, `HomeServlet`) responsáveis por interceptar as requisições HTTP, orquestrar a leitura/processamento de dados e despachar os resultados para a View.
* **Util (`br.edu.ufrgs.util`):** Classes auxiliares para validação de dados, leitura e exportação de arquivos CSV (`LeitorCSV`, `ExportadorCSV`).
* **View (`/WEB-INF/resultadoAuditoria.jsp`):** Interface gráfica gerada dinamicamente via JSP e JSTL para exibir os relatórios e logs de erro ao usuário.

---

## ⚙️ Regras de Negócio e Motor de Cálculo

### 1. Motor de Cálculo (RF03)
A emissão de CO₂ de cada setor é calculada consolidando o consumo de todas as suas máquinas através da fórmula:

> **Emissão do Setor** = Σ (Consumo da Máquina em kWh) × Fator de Emissão do Setor

### 2. Matriz de Classificação Ambiental (RF04)
Após o cálculo, o sistema avalia o percentual de uso do limite mensal de emissão configurado para o setor e atribui um status:

| Percentual de Uso | Status Ambiental | Ação Recomendada |
| :--- | :--- | :--- |
| **Até 70%** | `ECO_FRIENDLY` | Manter operação |
| **71% a 100%** | `ALERTA` | Revisar manutenção |
| **Acima de 100%** | `ALTO_IMPACTO` | Plano de mitigação obrigatório |

### 3. Validação Dinâmica (RF02)
O sistema não possui dados fixos ("chumbados") no código. Todas as referências vêm do arquivo de configuração (`configuracoes.csv`). Se uma máquina referenciar um setor não cadastrado, o sistema ignora o registro e gera um **log de erro**, exibido na interface final.

---

## 📊 Diagrama de Classes

O projeto foi modelado e documentado através de um diagrama de classes.
👉 **[Acessar Diagrama de Classes (Lucidchart)](https://lucid.app/lucidchart/a1dd9849-e948-4670-aff0-72556a5e675a/edit?view_items=_qRVDnuDI_oz&page=HWEp-vi-RSFO&invitationId=inv_24f6c748-2db5-4fef-bf2f-299fc8822a23)**

---

## 🐳 Como Executar o Projeto (Docker)

O ambiente da aplicação é totalmente conteinerizado. Certifique-se de ter o [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e rodando na sua máquina.

**Passo a passo:**

1. Clone o repositório e acesse a pasta raiz do projeto no terminal.
2. Construa e suba a aplicação utilizando o Docker Compose:
   ```bash
   docker compose up --build -d

    ```

    Integrantes do Grupo: Aline Cardoso, Antônio Dário, Jean Carlo, Lucye Milach, Valentina Scolari

   