# Gerenciamento de Saúde

> Documentação inicial do projeto **Gerenciamento de Saúde**, desenvolvido para facilitar o acompanhamento médico, medicamentoso e de hábitos recomendados por profissionais de saúde, com foco em pessoas idosas e seus cuidadores.

## Sumário

1. [Visão Geral](#1-visão-geral)
2. [Objetivos e Público-Alvo](#2-objetivos-e-público-alvo)
3. [Funcionalidades](#3-funcionalidades)
4. [Escopo](#4-escopo)
5. [Requisitos Qualitativos](#5-requisitos-qualitativos)
6. [Tecnologias Adotadas](#6-tecnologias-adotadas)
7. [Responsabilidades dos Integrantes](#7-responsabilidades-dos-integrantes)
8. [Fluxo de Telas do Usuário](#8-fluxo-de-telas-do-usuário)
9. [Links do Projeto](#9-links-do-projeto)

---

## 1. Visão Geral

### 1.1 Descrição do Projeto

O **Gerenciamento de Saúde** é um aplicativo mobile voltado para pessoas idosas ou para cuidadores de pessoas idosas. Sua proposta central é organizar e simplificar o gerenciamento de documentos médicos, medicamentos e hábitos recomendados por médicos. Muitas pessoas idosas precisam lidar com um volume alto de informações críticas, como horários de remédios, consultas marcadas, exames pendentes e restrições médicas. Quando essas informações ficam dispersas em papéis, mensagens, lembretes informais ou na memória, aumenta o risco de esquecimentos, atrasos e prejuízos à saúde.

Diante desse cenário, o aplicativo busca reunir essas informações em um ambiente claro, direto e de fácil leitura, ajudando o usuário a lembrar quais medicamentos deve tomar, quando deve tomá-los, quais consultas e exames estão agendados e quais pendências médicas precisam de atenção. A proposta não é substituir o acompanhamento médico, mas servir como uma ferramenta de apoio à rotina de cuidado, reduzindo a sobrecarga cognitiva e favorecendo a adesão às recomendações de saúde.

### 1.2 Integrantes

| Integrante | GitHub |
|---|---|
| André Coutinho | [AndreCoutinhom](https://github.com/AndreCoutinhom) |
| André Rodrigues | [oandredev](https://github.com/oandredev) |
| Fernanda Souza | [souzafe13](https://github.com/souzafe13) |

---

## 2. Objetivos e Público-Alvo

O objetivo principal do projeto é oferecer uma ferramenta mobile simples e confiável para o gerenciamento de informações essenciais de saúde. O aplicativo deve permitir que a pessoa idosa, ou seu cuidador, mantenha um registro claro das atividades críticas relacionadas à sua saúde, como uso de medicamentos, consultas, exames e restrições médicas.

O público-alvo é composto por pessoas idosas que utilizam smartphones Android e por cuidadores que auxiliam essas pessoas na organização da rotina médica. Por isso, o projeto deve considerar características como facilidade de leitura, baixa complexidade visual, comandos intuitivos e tolerância a limitações motoras ou cognitivas leves. A experiência de uso precisa ser acolhedora e objetiva, evitando termos técnicos desnecessários e reduzindo ao máximo a quantidade de passos para realizar tarefas importantes.

---

## 3. Funcionalidades

### 3.1 Funcionalidades Básicas

As funcionalidades básicas definidas para a primeira versão do aplicativo são:

- **Notificações e lembretes de uso de medicamentos:** o sistema deve alertar o usuário sobre horários e doses de medicamentos, ajudando a evitar esquecimentos e atrasos.
- **Notificações e lembretes de consultas e exames:** o aplicativo deve registrar e avisar sobre compromissos médicos agendados, permitindo melhor organização da rotina.
- **Agendamento de consultas e exames:** o usuário deve poder cadastrar e visualizar consultas e exames, mantendo um histórico organizado desses compromissos.
- **Lista de restrições alérgicas ou por ordem médica:** o aplicativo deve permitir o registro de alergias, restrições alimentares, medicamentos proibidos ou outras orientações médicas importantes.

Essas funcionalidades formam o núcleo do projeto e devem estar presentes de maneira funcional, integrada e compreensível na versão entregável.

### 3.2 Funcionalidades em Discussão

Ainda estão em análise funcionalidades que podem ampliar o valor do aplicativo, desde que não comprometam sua simplicidade. Entre elas, destacam-se:

- Integração com APIs que armazenem o registro de vacinas, a Conta gov.br e a Carteira SUS.
- Mapeamento de hospitais ou postos de saúde próximos ao usuário.
- Oferecimento de alertas de ondas virais, convocações de vacinação ou pandemias.

Essas funcionalidades somente devem ser implementadas se apresentarem benefícios claros e objetivos para o melhor interesse da pessoa idosa, sem tornar a interface mais complexa, poluída ou difícil de usar.

---

## 4. Escopo

O escopo do projeto é delimitado para atender ao melhor interesse da pessoa idosa. As funcionalidades básicas devem ajudar o público a manter um registro claro e intuitivo das atividades críticas necessárias para a estabilidade de sua saúde. O aplicativo não deve oferecer mais informações do que o necessário para esse objetivo central, evitando complexidades visuais e excesso de informação na interface.

As funcionalidades ainda em discussão somente devem ser aplicadas se representarem benefícios claros e objetivos para o melhor interesse da pessoa idosa, sem comprometer uma interface simples, de fácil leitura e de uso intuitivo. Isso significa que qualquer nova integração, tela ou recurso precisa passar por uma avaliação de impacto sobre a experiência do usuário, sobre a manutenção do sistema e sobre a clareza da proposta principal.

O projeto será iniciado com sua primeira versão entregável funcional até o dia **16/11/2026**, contendo Back-end e Front-end minimamente viáveis e integração totalmente funcional. Essa versão deve priorizar estabilidade, usabilidade e organização das informações essenciais, servindo como base para evoluções futuras.

---

## 5. Requisitos Qualitativos

A qualidade do aplicativo está diretamente relacionada à sua capacidade de ser compreendido e utilizado por pessoas idosas ou por cuidadores, muitas vezes em situações de rotina corrida ou de atenção limitada. Por isso, a interface deve priorizar textos legíveis, contraste adequado, botões grandes, linguagem simples e uma organização visual que reduza a necessidade de interpretação. A troca de telas deve ser mínima, e as ações principais devem estar sempre a poucos toques de distância.

Além disso, o sistema deve transmitir confiabilidade. Notificações precisam ser claras, horários devem ser facilmente editáveis e informações críticas, como medicamentos e alergias, não podem ser apresentadas de forma ambígua. A segurança e a privacidade dos dados de saúde também são requisitos qualitativos importantes, pois o aplicativo lida com informações sensíveis. Espera-se, ainda, que o código seja organizado, documentado e testável, permitindo manutenção e evolução por parte da equipe.

---

## 6. Tecnologias Adotadas

### 6.1 Front-end Mobile

A versão do aplicativo desenvolvida até o dia **16/11/2026** será feita usando o framework [React Native](https://reactnative.dev/), compondo elementos visuais interativos com a interface nativa de telefones celulares móveis do sistema Android. A linguagem adotada para moldar os recursos visuais interativos será o [TypeScript](https://www.typescriptlang.org/docs/), que contribui para maior segurança de tipos e melhor organização do código.

### 6.2 Back-end e Banco de Dados

O banco de dados do aplicativo será mantido remotamente por meio do ecossistema [Spring](https://docs.spring.io/spring-framework/reference/index.html), modelando e condicionando interações em linguagem [Java](https://docs.oracle.com/en/java/). Essa estrutura será responsável por receber, processar e armazenar as informações enviadas pelo aplicativo, garantindo a comunicação entre o Front-end e a persistência dos dados.

---

## 7. Responsabilidades dos Integrantes

Cada integrante se encontra responsável pela apresentação de componentes da composição geral do projeto. Todos os integrantes contribuem igualmente na prática do projeto, mas cada integrante responderá por um dos componentes de forma que conduzirá o conhecimento e as práticas teóricas necessárias durante a condução do projeto.

### 7.1 André Coutinho

[André Coutinho](https://github.com/AndreCoutinhom) é o responsável pelas práticas de engenharia de software, conduzindo o projeto para uma documentação adequada, testes de qualidade e adequação com o modelo de negócios.

### 7.2 André Rodrigues

[André Rodrigues](https://github.com/oandredev) é o responsável pela composição do Back-end, conduzindo as atividades de modelagem de classes e estrutura dos dados trabalhados no projeto, garantindo fluidez no tratamento de informações dos usuários.

### 7.3 Fernanda Souza

[Fernanda Souza](https://github.com/souzafe13) é a responsável pela aplicação do Front-end, conduzindo a apresentação e estilização de recursos visuais do projeto e orquestrando as possibilidades de interação entre o sistema e os usuários.

---

## 8. Fluxo de Telas do Usuário

Até o momento da escrita desta documentação, o fluxo de tela dos usuários ainda não pôde ser definido. Ainda se julga necessário a adequação dos integrantes do projeto com conhecimentos de construção de interface mobile para que o fluxo seja documentado adequadamente.

Apesar disso, já existe uma diretriz preliminar: o fluxo de telas do usuário deve obter uma troca mínima de telas diferentes para que a interação com o usuário seja a mais simples possível e ocupe o menor fluxo de diferentes atividades. Essa diretriz orienta as decisões futuras de navegação e organização visual, reforçando o compromisso com a simplicidade e com a facilidade de uso.

---

## 9. Links do Projeto

- **Trello:** [Projeto Gerenciamento de Saúde — Projeto Integrador 4](https://trello.com/invite/b/6a957dd06bc63054004c760f/ATTIb5f47bb184fd3f6f51453648ca4f2ccd94864106/projeto-gerenciamento-de-saude-projeto-integrador-4)
- **GitHub:** [projeto-gerenciamento-de-saude](https://github.com/oandredev/projeto-gerenciamento-de-saude)
