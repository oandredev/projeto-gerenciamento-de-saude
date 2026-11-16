### Projeto Gerenciamento de Saúde | Projeto Integrador 4

**Disciplina:** Projeto Integrador 4 - Desenvolvimento de Sistemas para Dispositivos Móveis | Centro Universitário SENAC<br>
**Orientador:** [Prof. João Felipe Fernandes Coghi](https://github.com/joaocoghi)

---

<picture>
<img width="187" height="187" alt="image" src="https://github.com/user-attachments/assets/06304ba8-e6a8-4a98-b167-49ba0a7aab4e" />
</picture>

# A&F Health Management

> Documentação do projeto **A&F Health Manegement**, desenvolvido para facilitar o acompanhamento médico e medicamentoso, com foco em pessoas idosas e seus cuidadores e/ou familiares.

---

## 1. Visão Geral

### 1.1 Descrição do Projeto

O **A&F Health Management** é um aplicativo mobile voltado para pessoas idosas, seus cuidadores e/ou familiares. Sua proposta central é organizar e simplificar o gerenciamento de medicamentos e agendamentos de consultas médicas e exames. Muitas pessoas idosas precisam lidar com um volume alto de informações críticas, como horários de remédios, consultas marcadas, exames pendentes e restrições médicas. Quando essas informações ficam dispersas em papéis, mensagens, lembretes informais ou na memória, aumenta o risco de esquecimentos, atrasos e prejuízos à saúde.

Diante desse cenário, o aplicativo busca reunir essas informações em um ambiente claro, direto e de fácil leitura, ajudando o usuário a lembrar quais medicamentos deve tomar, quando deve tomá-los, quais consultas e exames estão agendados. A proposta não é substituir o acompanhamento médico, mas servir como uma ferramenta de apoio à rotina de cuidado, reduzindo a sobrecarga cognitiva e favorecendo a adesão às recomendações de saúde. Desde o início do projeto foram buscados recursos de garantia de qualidade levando em consideração a conformidade legal, acessibilidade, usabilidade e segurança do aplicativo. [Uma série de diretrizes, modelos e documentos normativos legais](https://github.com/oandredev/projeto-gerenciamento-de-saude/tree/doc/anvisa-and-cfm-research/Pesquisa/Documentos%20usados%20nos%20prompts) foi organizada para que o projeto sempre cumpra com requisitos determinados por lei e autoridades acadêmicas.

### 1.2 Integrantes

| Integrante | GitHub |
|---|---|
| André Coutinho | [AndreCoutinhom](https://github.com/AndreCoutinhom) |
| André Rodrigues | [oandredev](https://github.com/oandredev) |
| Fernanda Souza | [souzafe13](https://github.com/souzafe13) |

---

## 2. Objetivos e Público-Alvo

O objetivo principal do projeto é oferecer uma ferramenta mobile simples e confiável para o gerenciamento de informações essenciais de saúde. O aplicativo deve permitir que a pessoa idosa, seu cuidador ou seu familiar, mantenha um registro claro das atividades críticas relacionadas à sua saúde, como uso de medicamentos, consultas, exames e restrições médicas.

O público-alvo é composto por pessoas idosas que utilizam smartphones Android e/ou iOS, seus familiares e por cuidadores que auxiliam essas pessoas na organização da rotina médica. Por isso, o projeto deve considerar características como facilidade de leitura, baixa complexidade visual, comandos intuitivos e tolerância a limitações motoras ou cognitivas leves. A experiência de uso precisa ser acolhedora e objetiva, evitando termos técnicos desnecessários e reduzindo ao máximo a quantidade de passos para realizar tarefas importantes.

---

## 3. Funcionalidades

As funcionalidades básicas definidas para o aplicativo durante a realização do projeto integrador são:

- **Cadastro de usuários:** a realização de todas as funções do aplicativo depende de um usuário cadastrado e registrado no banco de dados.
- **Criação de perfis:** todas as funções do aplicativo são dispostas entre diferentes perfis cadastrados no mesmo usuário, permitindo que uma mesma conta ajude no gerenciamento da saúde de mais de uma pessoa de forma customizada.
- **Notificações e lembretes de uso de medicamentos:** o sistema deve alertar o usuário sobre horários e doses de medicamentos, ajudando a evitar esquecimentos e atrasos.
- **Notificações e lembretes de consultas e exames:** o aplicativo deve registrar e avisar sobre compromissos médicos agendados, permitindo melhor organização da rotina.
- **Agendamento de consultas e exames:** o usuário deve poder cadastrar e visualizar consultas e exames, mantendo um histórico organizado desses compromissos.
- **Lista de restrições alérgicas ou por ordem médica:** o aplicativo deve permitir o registro de alergias, restrições alimentares, medicamentos proibidos ou outras orientações médicas importantes.

Essas funcionalidades formam o núcleo do projeto e devem estar presentes de maneira funcional, integrada e compreensível na versão entregável.

---

## 4. Escopo

O escopo do projeto é delimitado para atender ao melhor interesse da pessoa idosa. As funcionalidades básicas devem ajudar o público a manter um registro claro e intuitivo das atividades críticas necessárias para a melhor organização de seus compromissos com sua saúde. O aplicativo não deve oferecer mais informações do que o necessário para esse objetivo central, evitando complexidades visuais e excesso de informação na interface.

O aplicativo não deve se propor a realizar ações exclusivamente médicas, como prescrição e documentação de receitas e intermediação entre o aplicativo e o hospital; sendo que a realização dessas ações sem a portação de CRM é cabível de punição severa pelo STF, CFM e ANVISA. As funções do aplicativo devem, em seu limite, se aproximar em equivalência das ações realizadas pela própria pessoa idosa, seus cuidadores e familiares sem o uso de um aplicativo, assistindo na organização de orientações e prescrições já realizadas pelos médicos de forma autônoma e centrada em uma única interface.

O projeto será iniciado com sua primeira versão entregável funcional até o dia **23/11/2026**, contendo Back-end e Front-end minimamente viáveis e integração totalmente funcional. Essa versão deve priorizar estabilidade, usabilidade e organização das informações essenciais, servindo como base para evoluções futuras.

---

## 5. Requisitos

### Funcionais

Nossos requisitos funcionais foram determinados pelo modelo MoSCoW conforme a prioridade do que era considerado essencial para o aplicativo. Segue a tabela completa de requisitos funcionais abaixo.

| REQUISITOS FUNCIONAIS (RF) |                                |                                                                                                                                                                                                                                |
|----------------------------|--------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ID                         | Nome do RF                     | Descrição                                                                                                                                                                                                                      |
| RF01                       | Entrada de usuário             | A permissão de um cadastro e login de usuários do aplicativo                                                                                                                                                                   |
| RF02                       | Divisão de perfis              | Cada usuário pode criar múltiplos perfis para que o gerenciamento possa ocorrer levando em consideração diferentes conjuntos de dados.                                                                                         |
| RF03                       | Gerenciamento de medicamentos  | Agendamentos e notificações referentes a medicamentos que devem ser tomados com regras aplicadas às diferentes cores de tarja.                                                                                                 |
| RF04                       | Gerenciamento de consultas     | Agendamentos e notificações referentes a consultas médicas a serem utilizadas pelos representantes dos perfis incluindo dados obrigatórios e opcionais sobre o hospital onde a consulta será realizada e o médico responsável. |
| RF05                       | Registro de restrições médicas | Cada perfil pode ter uma lista de restrições médicas atreladas, de forma a mapear alergias ou outros impedimentos para o uso de determinados medicamentos.                                                                     |


---

### Não funcionais

Todos os requisitos não funcionais foram definidos com base na legislação brasileira sobre tratamento de dados sensíveis, atividades exclusivas para portadores de CRM, normas da Anvisa e recomendações de acessibilidade digital do NIC.br, CGI.br e da W3C.

| REQUISITOS NÃO-FUNCIONAIS (RNF) |                    |                                              |                                                                                                                                                                                                                                                                                                      |
|---------------------------------|--------------------|----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| RNF                             | Característica     | Subcaracterística                            | Descrição                                                                                                                                                                                                                                                                                            |
| 1                               | Conformidade Legal | Alinhamento com a Anvisa                     | A Agência Nacional de Vigilância Sanitária estabelece normas e recursos digitais que facilitam a compreensão de quais atributos um medicamento pode/deve ter atrelado. Documentos oficiais das normas foram incluídos em todos os prompts de LLM que ofereceram assistência na modelagem de classes. |
|                                 |                    | Alinhamento com o CRM                        | O Conselho Federal de Medicina estabelece quais atividades são exclusivas para portadores de CRM, delimitando, legalmente, o escopo de atividades que o aplicativo pode exercer e quais deve deixar às mão de profissionais da medicina.                                                             |
|                                 |                    | Alinhamento com LGPD                         | A documentação oficial da Lei Geral de Proteção dos Dados foi referência em todos os prompts de LLM durante a programação do back-end.                                                                                                                                                               |
| 2                               | Acessibilidade     | Fácil leitura                                | Fontes legíveis e em larga escala e alto contraste com as cores de fundo para evitar a vista forçada.                                                                                                                                                                                                |
|                                 |                    | Troca de telas mínima                        | Dinâmica informacional com o menor número de etapas possível mantendo a clareza das funções.                                                                                                                                                                                                         |
|                                 |                    | Componentes legíveis no modo acessibilidade  | Todos os componentes visuais do aplicativo devem ter textos atribuídos ao sistema de voz dos smartphones em modo acessibilidade.                                                                                                                                                                     |
|                                 |                    | Alinhamento com W3C                          | Documentação de diretrizes de acessibilidade da W3C incluída em todos os prompts de LLM que ofereceram assistência na programação de interface do aplicativo.                                                                                                                                        |
|                                 |                    | Alinhamento com NIC.br                       | Cartilhas de acessibilidade do NIC.br incluídas em todos os prompts de LLM que ofereceram assistência na programação de interface do aplicativo.                                                                                                                                                     |
|                                 |                    | Alinhamento com a lei de inclusão brasileira | A documentação oficial do estatuto da pessoa com deficiência foi referência em todos os prompts de LLM durante a programação do Front-End e determinação de requisitos não funcionais.                                                                                                               |
| 3                               | Segurança          | Filtragem de atributos com DTO               | Classes com dados sensíveis se comportam na API através de DTOs com grupos de atributos filtrados.                                                                                                                                                                                                   |
|                                 |                    | Autenticação JWT                             | Cada usuário cadastrado possui um token de autenticação com data de expiração.                                                                                                                                                                                                                       |
|                                 |                    | Segurança                                    | Criptografia de senhas com BCrypt                                                                                                                                                                                                                                                                    |


## 6. Tecnologias Adotadas

### 6.1 Front-end Mobile

A versão do aplicativo desenvolvida até o dia **23/11/2026** foi feita usando o framework [React Native](https://reactnative.dev/), compondo elementos visuais interativos com a interface nativa de telefones celulares móveis do sistema Android e iOS. Todos os programas são escritos na linguagem [Javascript](https://developer.mozilla.org/en-US/docs/Web/JavaScript).

### 6.2 Back-end e Banco de Dados

O banco de dados do aplicativo tem suas funções programadas e intermediadas em base [Spring](https://docs.spring.io/spring-framework/reference/index.html) e mantido por meio do sistema [H2 Database](https://h2database.com/html/main.html), modelando e condicionando interações em linguagem [Java](https://docs.oracle.com/en/java/). Essa estrutura é responsável por receber, processar e armazenar as informações enviadas pelo aplicativo, garantindo a comunicação entre o Front-end e a persistência dos dados.

### 6.3 Garantia de Qualidade (QA)

As práticas de QA do projeto acompanham uma documentação detalhada, organização de tarefas e realização de testes de qualidade para os requisitos funcionais e não funcionais. Os diagnósticos envolvem testes de acessibilidade, desempenho, qualidade de escrita de código, testes de caixa branca e testes de caixa preta para o back-end e para o front-end.

---

## 7. Responsabilidades dos Integrantes

Cada integrante se encontra responsável pela apresentação de componentes da composição geral do projeto. Todos os integrantes contribuem igualmente na prática do projeto, mas cada integrante responderá por um dos componentes de forma que conduzirá o conhecimento e as práticas teóricas necessárias durante a condução do projeto.

### 7.1 [André Coutinho](https://github.com/AndreCoutinhom)


<p align="center">
  
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/77230027-e972-40ba-a4e2-5f4fda1e1ed3" hspace='1.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/b2eb8145-1533-4900-b3a9-fbd396eae22f" hspace='1.5' />
  <img src="https://skillicons.dev/icons?i=java,idea" height="50" alt="Tech stack" />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/ac408ec2-5afd-4614-91a6-3999939a95da" hspace='1.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/9f7294d4-202c-49e0-bc7e-e3c99103ea5c" hspace='1.5' />
  <img src="https://skillicons.dev/icons?i=html,css,javascript" height="50" alt="Tech stack" />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/eb433a28-6fb7-49d8-a338-bf0be6a3a8a0" hspace='1.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/f9a534f5-468a-4b80-9a7b-0492ed8ef1c5" hspace='1.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/a7e42bbe-7002-41a6-a7b9-753b797b0491" hspace='1.5' />

</p>


É o responsável pelas práticas de engenharia de software, conduzindo o projeto para uma documentação adequada, definição de requisitos funcionais e não funcionais, pesquisa de normas legais, testes e documentação de qualidade e adequação com o modelo de negócios e conformidade legal. 

As principais ferramentas utilizadas por este membro são:

* **Trello:** Para comunicação de recursos, prioridades e etapas no andamento do projeto;
* **Google Planilhas:** Para a organização de requisitos funcionais, requisitos não funcionais e planos de teste;
* **JUnit:** Para a programação e geração de diagnósticos para [testes unitários](https://github.com/oandredev/projeto-gerenciamento-de-saude/tree/feat/new-project-base-tests-2/backend/health-management/src/test/java/edu/senacsp/health_management) em Java;
* **JMeter:** Para a realização de [testes de desempenho](https://github.com/oandredev/projeto-gerenciamento-de-saude/blob/feat/new-project-base-tests/Documenta%C3%A7%C3%A3o%20de%20Testes/Teste_de_Desempenho.md) de requisições HTTP;
* **HTML, CSS e Javascript:** Para a [adaptação da interface](https://github.com/oandredev/projeto-gerenciamento-de-saude/tree/feat/web-tests-interface/Web-Interface) mobile a um modelo legível pelos testes de acessibilidade;
* **Lighthouse e WAVE:** Para a realização de diagnósticos de acessibilidade;
* **Checkstyle:** Para checar a compatibilidade dos códigos [com boas práticas de escrita e garantia de compatibilidade de versionamento](https://github.com/oandredev/projeto-gerenciamento-de-saude/blob/feat/new-project-base-tests-2/backend/health-management/config/checkstyle/checkstyle.xml).

### 7.2 [André Rodrigues](https://github.com/oandredev)

<p align="center">
  
  <img src="https://skillicons.dev/icons?i=git,github,java,idea,vscode,spring" height="48" alt="Tech stack" />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/ecaa5d92-ec49-4467-b172-8dcdb7e7acaa" hspace='4.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/af0f3d3b-5319-4b7c-9e93-7190a0204713" hspace='1.5' />
  <img src="https://skillicons.dev/icons?i=postman" height="48" alt="Tech stack" />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/893b4983-b6cb-4c37-ad74-3c89f709f698" hspace='1.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/ec97ecdb-a018-45f2-a26c-614fb125609c" hspace='1.5' />


</p>


É o responsável pela composição do Back-end, gerenciamento do banco de dados e controle de versão do projeto, conduzindo as atividades de modelagem de classes e estrutura dos dados trabalhados no projeto, garantindo fluidez e boas práticas de segurança no tratamento de informações dos usuários. 

As principais ferramentas utilizadas por este membro são:

* **Git/GitHub:** Para o controle e compartilhamento de [versionamento do projeto](https://github.com/oandredev/projeto-gerenciamento-de-saude);
* **IntelliJ:** Para a [ambientação](https://github.com/oandredev/projeto-gerenciamento-de-saude/tree/main/backend/health-management) de todos os códigos, bancos de dados e configurações;
* **VS Code:** Para a realização de testes rápidos de integração com Front-End;
* **Spring:** Para a elaboração de [camadas da API](https://github.com/oandredev/projeto-gerenciamento-de-saude/tree/main/backend/health-management/src/main/java/edu/senacsp/health_management);
* **H2 Database:** Para o mantimento e visualização de atividades no banco de dados;
* **Swagger:** Para a realização de documentação automatizada da API;
* **Postman:** Para a visualização e verificação de chamadas HTTP e estruturação de dados em JSON.
* **Expo:** Para a realização de testes em interface mobile (sistema Android).

### 7.3 [Fernanda Souza](https://github.com/souzafe13)

<p align="center">
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/c2e46d22-2563-4815-9274-0bf774e7c93d" hspace='1.5' />
  <img src="https://skillicons.dev/icons?i=figma,vscode,js,react" height="48" alt="Tech stack" />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/893b4983-b6cb-4c37-ad74-3c89f709f698" hspace='1.5' />
  <img src="https://skillicons.dev/icons?i=apple" height="48" alt="Tech stack" />

</p>

É a responsável pela aplicação do Front-end, conduzindo a apresentação e estilização de recursos visuais do projeto e orquestrando as possibilidades de interação entre o sistema e os usuários.

As principais ferramentas utilizadas por este membro são:

* **PowerPoint:** Para a apresentação e organização da identidade visual do projeto;
* **Figma:** Para a arquitetura de fluxo de telas do aplicativo;
* **Visual Studio Code:** Para a ambientação de todos os códigos, marcadores e estilizadores;
* **React Native:** Para a elaboração de [componentes](https://github.com/oandredev/projeto-gerenciamento-de-saude/tree/main/frontend/src) mobile visuais, interativos e integrados com o back-end;
* **Expo:** Para a realização de testes em interface mobile (sistema iOS).

---

# 8. Uso de LLMs

<p align="center">
  <img height="50" alt="image" src="https://thumb.wikimedia.org/wikipedia/commons/thumb/b/ba/Deepseek-logo-icon.svg/1280px-Deepseek-logo-icon.svg.png?utm_source=commons.wikimedia.org&utm_campaign=index&utm_content=thumbnail" hspace='2.5' />
  <img src="https://uxwing.com/wp-content/themes/uxwing/download/brands-and-social-media/claude-ai-icon.png" height="50" alt="Tech stack" hspace='2.5' />
  <img height="50" alt="image" src="https://static.vecteezy.com/system/resources/previews/055/687/055/non_2x/rectangle-gemini-google-icon-symbol-logo-free-png.png" hspace='2.5' />
</p>

Todos os integrantes utilizaram LLMs para dinamizar o processo de construção de códigos. Declaramos que o uso de IA não comprometeu nosso aprendizado ou entendimento de funcionamento do projeto e que esse recurso somente foi buscado como uma forma de adequar as tarefas conforme a demanda de entregas e tempo disponível.

Os modelos utilizados foram: **DeepSeek**, **Claude** e **Gemini**.

---

## 9. [GitHub](https://github.com/oandredev/projeto-gerenciamento-de-saude)
