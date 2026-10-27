### Projeto Gerenciamento de Saúde | Projeto Integrador 4

**Disciplina:** Projeto Integrador 4 - Desenvolvimento de Sistemas para Dispositivos Móveis | Centro Universitário SENAC<br>
**Orientador:** Prof. João Felipe Fernandes Coghi

---

<picture>
<img width="187" height="187" alt="image" src="https://github.com/user-attachments/assets/06304ba8-e6a8-4a98-b167-49ba0a7aab4e" />
</picture>

# A&F Health Management

> Documentação do projeto **A&F Health Manegement**, desenvolvido para facilitar o acompanhamento médico e medicamentoso, com foco em pessoas idosas e seus cuidadores e/ou familiares.

---

## 1. Visão Geral

### 1.1 Descrição do Projeto

O **A&F Health Management** é um aplicativo mobile voltado para pessoas idosas, seus cuidadores e/ou familiares. Sua proposta central é organizar e simplificar o gerenciamento de medicamentos e agendamentos de consulta médica. Muitas pessoas idosas precisam lidar com um volume alto de informações críticas, como horários de remédios, consultas marcadas, exames pendentes e restrições médicas. Quando essas informações ficam dispersas em papéis, mensagens, lembretes informais ou na memória, aumenta o risco de esquecimentos, atrasos e prejuízos à saúde.

Diante desse cenário, o aplicativo busca reunir essas informações em um ambiente claro, direto e de fácil leitura, ajudando o usuário a lembrar quais medicamentos deve tomar, quando deve tomá-los, quais consultas e exames estão agendados. A proposta não é substituir o acompanhamento médico, mas servir como uma ferramenta de apoio à rotina de cuidado, reduzindo a sobrecarga cognitiva e favorecendo a adesão às recomendações de saúde.

### 1.2 Integrantes

| Integrante | GitHub |
|---|---|
| André Coutinho | [AndreCoutinhom](https://github.com/AndreCoutinhom) |
| André Rodrigues | [oandredev](https://github.com/oandredev) |
| Fernanda Souza | [souzafe13](https://github.com/souzafe13) |

---

## 2. Objetivos e Público-Alvo

O objetivo principal do projeto é oferecer uma ferramenta mobile simples e confiável para o gerenciamento de informações essenciais de saúde. O aplicativo deve permitir que a pessoa idosa, seu cuidador ou seu familiar, mantenha um registro claro das atividades críticas relacionadas à sua saúde, como uso de medicamentos, consultas, exames e restrições médicas.

O público-alvo é composto por pessoas idosas que utilizam smartphones Android, seus familiares e por cuidadores que auxiliam essas pessoas na organização da rotina médica. Por isso, o projeto deve considerar características como facilidade de leitura, baixa complexidade visual, comandos intuitivos e tolerância a limitações motoras ou cognitivas leves. A experiência de uso precisa ser acolhedora e objetiva, evitando termos técnicos desnecessários e reduzindo ao máximo a quantidade de passos para realizar tarefas importantes.

---

## 3. Funcionalidades

As funcionalidades básicas definidas para o aplicativo durante a realização do projeto integrador são:

- **Criação de perfis:** todas as funções do aplicativo são dispostas entre diferentes perfis cadastrados no mesmo usuário, permitindo que uma mesma conta ajude no gerenciamento da saúde de mais de uma pessoa.
- **Notificações e lembretes de uso de medicamentos:** o sistema deve alertar o usuário sobre horários e doses de medicamentos, ajudando a evitar esquecimentos e atrasos.
- **Notificações e lembretes de consultas e exames:** o aplicativo deve registrar e avisar sobre compromissos médicos agendados, permitindo melhor organização da rotina.
- **Agendamento de consultas e exames:** o usuário deve poder cadastrar e visualizar consultas e exames, mantendo um histórico organizado desses compromissos.
- **Lista de restrições alérgicas ou por ordem médica:** o aplicativo deve permitir o registro de alergias, restrições alimentares, medicamentos proibidos ou outras orientações médicas importantes.

Essas funcionalidades formam o núcleo do projeto e devem estar presentes de maneira funcional, integrada e compreensível na versão entregável.

---

## 4. Escopo

O escopo do projeto é delimitado para atender ao melhor interesse da pessoa idosa. As funcionalidades básicas devem ajudar o público a manter um registro claro e intuitivo das atividades críticas necessárias para a estabilidade de sua saúde. O aplicativo não deve oferecer mais informações do que o necessário para esse objetivo central, evitando complexidades visuais e excesso de informação na interface.

O aplicativo não deve se propor a realizar ações exclusivamente médicas, como prescrição e documentação de receitas, sendo a realização dessas ações sem a portação de CRM cabível de punição severa pelo STF, CFM e ANVISA. As funções do aplicativo devem, em seu limite, se aproximar em equivalência das ações realizadas pela própria pessoa idosa, seus cuidadores e familiares, assistindo na organização de orientações e prescrições já realizadas pelos médicos.

O projeto será iniciado com sua primeira versão entregável funcional até o dia **16/11/2026**, contendo Back-end e Front-end minimamente viáveis e integração totalmente funcional. Essa versão deve priorizar estabilidade, usabilidade e organização das informações essenciais, servindo como base para evoluções futuras.

---

## 5. Requisitos Qualitativos

A qualidade do aplicativo está diretamente relacionada à sua capacidade de ser compreendido e utilizado por pessoas idosas ou por cuidadores, muitas vezes em situações de rotina corrida ou de atenção limitada. Por isso, a interface deve priorizar textos legíveis, contraste adequado, botões grandes, linguagem simples e uma organização visual que reduza a necessidade de interpretação. A troca de telas deve ser mínima, e as ações principais devem estar sempre a poucos toques de distância.

Além disso, o sistema deve transmitir confiabilidade. Notificações precisam ser claras, horários devem ser facilmente editáveis e informações críticas, como medicamentos e alergias, não podem ser apresentadas de forma ambígua. A segurança e a privacidade dos dados de saúde também são requisitos qualitativos importantes, pois o aplicativo lida com informações sensíveis (restrições médicas que podem incluir alergias; medicamentos de uso contínuo). Espera-se, ainda, que o código seja organizado, documentado e testável, permitindo manutenção e evolução por parte da equipe.

---

## 6. Tecnologias Adotadas

### 6.1 Front-end Mobile

A versão do aplicativo desenvolvida até o dia **16/11/2026** será feita usando o framework [React Native](https://reactnative.dev/), compondo elementos visuais interativos com a interface nativa de telefones celulares móveis do sistema Android. Todos os programas são escritos na linguagem [Javascript](https://developer.mozilla.org/en-US/docs/Web/JavaScript).

### 6.2 Back-end e Banco de Dados

O banco de dados do aplicativo será mantido remotamente por meio do ecossistema [Spring](https://docs.spring.io/spring-framework/reference/index.html), modelando e condicionando interações em linguagem [Java](https://docs.oracle.com/en/java/). Essa estrutura será responsável por receber, processar e armazenar as informações enviadas pelo aplicativo, garantindo a comunicação entre o Front-end e a persistência dos dados.

### 6.3 Garantia de Qualidade (QA)

As práticas de QA do projeto acompanham uma documentação detalhada, organização de tarefas e realização de testes de qualidade para os requisitos funcionais e não funcionais. Os diagnósticos envolvem testes de acessibilidade, desempenho, qualidade de escrita de código, testes de caixa branca para o back-end e testes de caixa preta para o front-end.

---

## 7. Responsabilidades dos Integrantes

Cada integrante se encontra responsável pela apresentação de componentes da composição geral do projeto. Todos os integrantes contribuem igualmente na prática do projeto, mas cada integrante responderá por um dos componentes de forma que conduzirá o conhecimento e as práticas teóricas necessárias durante a condução do projeto.

### 7.1 [André Coutinho](https://github.com/AndreCoutinhom)


<p align="center">
  
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/77230027-e972-40ba-a4e2-5f4fda1e1ed3" hspace='1.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/b2eb8145-1533-4900-b3a9-fbd396eae22f" hspace='1.5' />
  <img src="https://skillicons.dev/icons?i=java" height="50" alt="Tech stack" />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/ac408ec2-5afd-4614-91a6-3999939a95da" hspace='1.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/9f7294d4-202c-49e0-bc7e-e3c99103ea5c" hspace='1.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/eb433a28-6fb7-49d8-a338-bf0be6a3a8a0" hspace='1.5' />
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/f9a534f5-468a-4b80-9a7b-0492ed8ef1c5" hspace='1.5' />
</p>


É o responsável pelas práticas de engenharia de software, conduzindo o projeto para uma documentação adequada, pesquisa de normas, testes de qualidade e adequação com o modelo de negócios. 

As principais ferramentas utilizadas por este membro são:

* **Trello:** Para comunicação de recursos, prioridades e etapas no andamento do projeto;
* **Google Planilhas:** Para a organização de requisitos funcionais, requisitos não funcionais e planos de teste;
* **JUnit:** Para a programação e geração de diagnósticos para testes unitários em Java;
* **JMeter:** Para a realização de testes de desempenho de requisições HTTP;
* **Lighthouse, WAVE e Accessibility Checker:** Para a realização de testes de acessibilidade.

### 7.2 [André Rodrigues](https://github.com/oandredev)

<img src="https://skillicons.dev/icons?i=git,github,java,idea,spring,postman" height="48" width="100%" alt="Tech stack" />

É o responsável pela composição do Back-end e controle de versão do projeto, conduzindo as atividades de modelagem de classes e estrutura dos dados trabalhados no projeto, garantindo fluidez e boas práticas de segurança no tratamento de informações dos usuários. 

As principais ferramentas utilizadas por este membro são:

* **Git/GitHub:** Para o controle e compartilhamento de versionamento do projeto;
* **IntelliJ:** Para a ambientação de todos os códigos, bancos de dados e configurações;
* **Spring:** Para a elaboração de camadas da API e visualização dos bancos de dados;
* **Postman:** Para a visualização e verificação de chamadas HTTP e estruturação de dados em JSON.

### 7.3 [Fernanda Souza](https://github.com/souzafe13)

<p align="center">
  <img height="48" alt="image" src="https://github.com/user-attachments/assets/c2e46d22-2563-4815-9274-0bf774e7c93d" />
  <img src="https://skillicons.dev/icons?i=figma,vscode,js,react" height="48" alt="Tech stack" />
</p>

É a responsável pela aplicação do Front-end, conduzindo a apresentação e estilização de recursos visuais do projeto e orquestrando as possibilidades de interação entre o sistema e os usuários.

As principais ferramentas utilizadas por este membro são:

* **PowerPoint:** Para a apresentação e organização da identidade visual do projeto;
* **Figma:** Para a arquitetura de fluxo de telas do aplicativo;
* **Visual Studio Code:** Para a ambientação de todos os códigos, marcadores e estilizadores;
* **React Native:** Para a elaboração de componentes mobile visuais, interativos e integrados com o back-end.

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

## 9. Links do Projeto

- **Trello:** [Projeto Gerenciamento de Saúde — Projeto Integrador 4](https://trello.com/invite/b/6a957dd06bc63054004c760f/ATTIb5f47bb184fd3f6f51453648ca4f2ccd94864106/projeto-gerenciamento-de-saude-projeto-integrador-4)
- **GitHub:** [projeto-gerenciamento-de-saude](https://github.com/oandredev/projeto-gerenciamento-de-saude)
