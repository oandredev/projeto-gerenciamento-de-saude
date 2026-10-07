# Análise de Acessibilidade do Site Cuidado Para Todos

**Autores:**
* André Coutinho de Macedo Silva;
* André Rodrigues Farias da Silva;
* Fernanda Maria de Souza

## Justificativa

O projeto deste grupo trata-se de uma plataforma mobile para o gerenciamento do uso de medicamentos e agendamentos de consulta, voltado para pessoas idosas com baixo letramento digital, seus cuidadores ou médicos de família. O projeto, portanto, pede uma grande dedicação à acessibilidade e conformes com leis federais de proteção de dados sensíveis e normas regulatórias de órgãos oficiais da saúde como a Anvisa e o CFM.

O site [Cuidado para Todos](https://www.cuidadoparatodos.com.br), criado pelo médico de família e comunicador **Lucas Cardim** e pelo cientista da computação **Davi Rios**, possui diversas sinergias com nosso projeto. O site consiste em um gerador de receitas médicas para que profissionais da medicina possam criar seus receituários junto a pacientes. O foco da plataforma é trazer uma abordagem embasada nos benefícios da comunicação visual através de ilustrações, ícones e vídeos para pessoas não letradas. Mais informações sobre a problemática que alimenta a necessidade da criação da plataforma, e a história de sua execução, podem ser assistidos no episódio [O impacto do analfabetismo na saúde e o papel do médico de família](https://youtu.be/BSfmTNkiiOc?si=EixBHbTJFJmdJlZ9) do programa [DrauzioCast](https://www.youtube.com/playlist?list=PLLcA2I5B3SEPD6qZ2clzptOVJcpSiu3XK).

Levando em consideração a sinergia de propósitos e público-alvo entre o nosso projeto e o Cuidado para Todos, o grupo decidiu realizar uma análise de acessibilidade do site com base na ferramenta [Lighthouse](https://chromewebstore.google.com/detail/lighthouse/blipmdconlkpinefehnmjammfjpmpbjk?hl=pt-br). Dessa forma podemos utilizar o projeto como inspiração ou contra exemplo de recursos disponibilizados em acessibilidade.

## Considerações iniciais

O Lighthouse gera um diagnóstico de acessibilidade se baseia em 5 parâmetros específicos: **Desempenho**, **Acessibilidade**, **Práticas Recomendadas**, **SEO** e **Navegação Agêntica**. Para o propósito deste projeto que se trata de um projeto de um aplicativo mobile, com necessidade de acessibilidade e segurança de dados, esta análise irá compor somente os três primeiros parâmetros, tendo em vista que a otimização de motor de busca e a navegação por IA não influenciam na acessibilidade de um aplicativo.

O site Cuidado para Todos tem um total de seis páginas. A análise foi feita seguindo o diagnóstico de todas as páginas na visualização para mobile.

## Análise de Desempenho

### Métricas por página

#### Inicial/Receitas

<img width="946" height="697" alt="image" src="https://github.com/user-attachments/assets/adb37a81-514d-481d-9bc5-0ed61650ecf1" />

#### Biblioteca

<img width="948" height="696" alt="image" src="https://github.com/user-attachments/assets/cef3bb74-0d5c-4b3d-8d10-7ed61be6aa6b" />

#### Materiais

<img width="950" height="699" alt="image" src="https://github.com/user-attachments/assets/1b9d4284-50e8-42e7-b5bc-1f4b5f0dfdf3" />

#### Utilitários

<img width="948" height="699" alt="image" src="https://github.com/user-attachments/assets/24f210ed-b25f-4403-9ab8-583389491ee2" />

#### Sobre

<img width="948" height="698" alt="image" src="https://github.com/user-attachments/assets/67823d7f-cd10-4615-8057-082054bed686" />

#### Privacidade

<img width="950" height="699" alt="image" src="https://github.com/user-attachments/assets/9ccc99c2-d5be-48d4-aa10-4bc7a62990e7" />


### Análise para o projeto

O site apresenta pontuação de desempenho com média de 50% de eficiência, sendo os principais problemas:

* Use ciclos de vida eficientes de cache (frequência: 6/6);

> Exemplo em página inicial:
> <img width="949" height="245" alt="image" src="https://github.com/user-attachments/assets/7df64933-2770-4b80-bbf1-2bd240b32787" />

* Solicitações que bloquearam a renderização (frequência: 6/6);

> Exemplo em Biblioteca
> <img width="950" height="332" alt="image" src="https://github.com/user-attachments/assets/0527bb06-570a-4b52-9131-b03776f3ddbd" />

* Descoberta de solicitações de LCP (frequência: 4/6);

> Exemplo em Materiais
> <img width="951" height="337" alt="image" src="https://github.com/user-attachments/assets/b5bf8648-ebb2-4d28-a35a-abf480865a0d" />

* Árvore de dependência da rede (frequência: 6/6).

> Exemplo em Privacidade
> <img width="762" height="761" alt="image" src="https://github.com/user-attachments/assets/81c17cc9-c20b-4878-aebe-a453b9312485" />


Em nosso projeto, será implementado o método de carregamento sob demanda com a intenção de isolar as funcionalidades, fazendo com que esta individualização resulte na redução do uso da memória RAM, considerando que um dispositivo móvel possui, em sua grande maioria, menos capacidade de processamento. A solução apresentada permite que o usuário desfrute da aplicação sem maiores intercorrências de desempenho, tendo em vista que as informações apenas serão carregadas quando houver necessidade.

## Análise de Acessibilidade

### Métricas por página

#### Inicial/Receitas

<img width="142" height="154" alt="image" src="https://github.com/user-attachments/assets/952078e5-9076-47b8-85ec-99168f9b223c" />

#### Biblioteca

<img width="138" height="162" alt="image" src="https://github.com/user-attachments/assets/807caa07-04a0-4b7d-975e-31c083b3f62e" />

#### Materiais

<img width="138" height="162" alt="image" src="https://github.com/user-attachments/assets/807caa07-04a0-4b7d-975e-31c083b3f62e" />

#### Utilitários

<img width="138" height="162" alt="image" src="https://github.com/user-attachments/assets/807caa07-04a0-4b7d-975e-31c083b3f62e" />

#### Sobre

<img width="138" height="153" alt="image" src="https://github.com/user-attachments/assets/d74ed86a-e388-407f-8964-2f5bd01d1f01" />

#### Privacidade

<img width="138" height="153" alt="image" src="https://github.com/user-attachments/assets/d74ed86a-e388-407f-8964-2f5bd01d1f01" />

### Análise para o projeto

O site obteve uma média de pontuação em acessibilidade no valor de 93,16%, podendo servir como um bom exemplo de requisitos mínimos de acessibilidade. No entanto, os principais problemas identificados nesse aspecto foram:

* Os botões não têm um nome acessível (frequência: 1/6);

> Caso em página inicial
> <img width="951" height="411" alt="image" src="https://github.com/user-attachments/assets/0cdd20d2-e507-4be0-adc1-e6526cd00d75" />

* As cores de primeiro e segundo plano não têm uma taxa de contraste suficiente (frequência: 6/6);

> Exemplo em Biblioteca
> <img width="951" height="823" alt="image" src="https://github.com/user-attachments/assets/aa472f0c-cc4f-4b9e-9f33-2a5218ab631c" />

* As listas não contêm apenas elementos `<li>` e elementos compatíveis com script (`<script>` e `<template>`) (frequência: 1/6).

> Caso em página inicial
> <img width="951" height="437" alt="image" src="https://github.com/user-attachments/assets/273ad1c0-c3c5-4da4-84fc-a35a792d4fd6" />

O site utiliza selos ARIA em alguns pontos do site, mas faltou colocar em funções cruciais como botões. Consequentemente os requisitos funcionais da plataforma são exclusivos para pessoas videntes.
Em nosso projeto, absolutamente TODAS AS FUNÇÕES deverão ter um label ARIA.

A seleção de cores do site analisado possui uma assinatura leve, o que faz com que alguns textos estejam escritos com tons de cinza ao invés de pretos. Isso, no entanto representa um baixo contraste que pode afetar a maneira como alguns usuários com visão delimitada o utilizam. Já que o site é voltado para médicos, isso pode ser um problema ainda maior, já que muitos médicos estão mais acostumados com textos de alto contraste.
No nosso projeto, a paleta de cores da interface será previamente pensada para criar contraste entre o texto e as cores de fundo.

O site dispõe recursos visuais estruturados em HTML de forma que seja indiferente para pessoas videntes, mas que será interpretado de uma forma específica por leitores de tela.
No nosso projeto, todos os recursos devem receber a estrutura devida em HTML para suas funções. A união de elementos de forma visual não será suficiente.

## Análise de Práticas Recomendadas

### Métricas por página

#### Inicial/Receitas

<img width="227" height="156" alt="image" src="https://github.com/user-attachments/assets/9619179e-e478-4dce-a518-2969a4df16aa" />

#### Biblioteca

<img width="227" height="156" alt="image" src="https://github.com/user-attachments/assets/9619179e-e478-4dce-a518-2969a4df16aa" />

#### Materiais

<img width="227" height="156" alt="image" src="https://github.com/user-attachments/assets/9619179e-e478-4dce-a518-2969a4df16aa" />

#### Utilitários

<img width="236" height="172" alt="image" src="https://github.com/user-attachments/assets/42e6e4bb-e577-4a92-997a-4bd22e56ebe9" />

#### Sobre

<img width="238" height="183" alt="image" src="https://github.com/user-attachments/assets/dbea091e-efc8-46ff-9544-753efdd85bee" />

#### Privacidade

<img width="227" height="156" alt="image" src="https://github.com/user-attachments/assets/9619179e-e478-4dce-a518-2969a4df16aa" />

### Análise para o Projeto

A pontuação média do site para o segmento de práticas recomendadas foi de 95,5%, servindo de bom exemplo para boas práticas de segurança digital. Os problemas relatados pelo Lighthouse se enquadram mais como recomendações de refinamento de segurança, tais como:

* Mapas de origem ausentes no JavaScript principal grande (frequência: 6/6);

> Exemplo em página inicial
> <img width="949" height="302" alt="image" src="https://github.com/user-attachments/assets/3fbf699c-6638-4d7c-a578-be004c02ea63" />

*  Erros do navegador foram registrados no console (frequência: 1/6);

> Caso em Utilitários
> <img width="949" height="323" alt="image" src="https://github.com/user-attachments/assets/7fd8c637-9736-456a-b0b0-55f1f90fded5" />

* Usa cookies de terceiros (frequência: 1/6);

> Caso em Sobre
> <img width="949" height="335" alt="image" src="https://github.com/user-attachments/assets/6217e946-a188-422d-9fff-37bcb8469fb6" />

* Os problemas foram registrados no painel Issues do Chrome Devtools (frequência: 1/6).

> Caso em Sobre
> <img width="950" height="266" alt="image" src="https://github.com/user-attachments/assets/b3751c86-ab11-4f78-8d70-a2d678c5f5eb" />

Em nosso projeto, já estamos seguindo diversas boas práticas em segurança. O Cuidado para Todos aborda algumas necessidades de refinamento em segurança, mas nem todas são aplicáveis para aplicativos mobile fora do domínio web, como é o caso de problemas identificados pelo painel do Chrome. Seguindo as recomendações do Lighthouse, abordaríamos, se possível, a prática de criação de mapas origens Javascript, já que esse foi o único ponto de recomendação presente, mesmo nas páginas que apresentaram pontuação 100%.

## Conclusão

O site Cuidado para Todos possui diversas sinergias com o projeto que está sendo criado no momento. A partir das análises feitas no site, temos uma clareza maior de alguns dos problemas e necessidades de refinamento que nosso aplicativo possa ter, mesmo que esteja bem embasado em acessibilidade, possua um bom desempenho de memória em smartphones e esteja sendo construído com boas práticas de segurança de dados. Aproveitaremos o diagnóstico Lighthouse gerado no Cuidado para Todos para melhorarmos ainda mais a colocação prática dos propósitos nos quais o projeto é baseado.

---
