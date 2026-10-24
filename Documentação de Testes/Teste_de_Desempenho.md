# Teste de Desempenho

**Integrantes:**

* André Coutinho de Macedo Silva;
* André Rodrigues Farias da Silva;
* Fernanda Maria de Souza.

## Contexto

Os testes de desempenho realizados para esta atividade são referentes a um *endpoint* gerado em uma rede local como uma requisição HTTP do método POST, simulando o cadastro de novos usuários no sistema de gerenciamento de saúde.

## Ambiente de Testes

Os testes foram realizados usando o *software* [Apache JMeter](https://jmeter.apache.org) em um computador pessoal com um sistema operacional de 64 bits na edição Windows 11 Home na versão 25H2. Os grupos de usuário foram organizados com 10 *threads* de usuários, com tempo de inicialização em 10 segundos e um contador de Iteração em 5 loops.

O *back-end* do projeto possui uma regra em que não podem haver usuários cadastrados com endereços de e-mail repetidos. Por esse motivo o ambiente de testes também precisou contar com um CSV contendo uma lista de usuários teste, cada com um com um nome, e-mail e senha diferentes. Dessa forma, o testes de desempenho considera 10 usuários diferentes realizando o cadastro. O CSV cadastrado no JMeter contém os seguintes dados de usuário:

| email           | password       | name           |
|-----------------|----------------|----------------|
| test01@mail.com | Test01@1234567 | Primeiro Teste |
| test02@mail.com | Test02@1234567 | Segundo Teste  |
| test03@mail.com | Test03@1234567 | Terceiro Teste |
| test04@mail.com | Test04@1234567 | Quarto Teste   |
| test05@mail.com | Test05@1234567 | Quinto Teste   |
| test06@mail.com | Test06@1234567 | Sexto Teste    |
| test07@mail.com | Test07@1234567 | Setimo Teste   |
| test08@mail.com | Test08@1234567 | Oitavo Teste   |
| test09@mail.com | Test09@1234567 | Nono Teste     |
| test10@mail.com | Test10@1234567 | Decimo Teste   |



O CSV foi configurado conforme mostra a figura abaixo:

<img width="2000" height="701" alt="image" src="https://github.com/user-attachments/assets/c8f8c282-010b-405b-a15f-85fcf168df49" />



A requisição HTTP do teste de desempenho usa o protocolo http, o servidor `localhost` e a porta `8080`. A requisição é do tipo `POST` e o caminho é `/user/signup`. O corpo dos dados para a requisição responde aos parâmetros do CSV com o tipo de conteúdo no valor de aplicação JSON. A requisição toma a forma abaixo:

``` json
{
  "email": "${email}",
  "password": "${password}",
  "name": "${name}"
}
```

## Resultados

| timeStamp     | elapsed | label                         | responseCode | responseMessage | threadName                   | dataType | success | failureMessage | bytes | sentBytes | grpThreads | allThreads | URL                               | Latency | IdleTime | Connect |
|---------------|---------|-------------------------------|--------------|-----------------|------------------------------|----------|---------|----------------|-------|-----------|------------|------------|-----------------------------------|---------|----------|---------|
| 1792510495863 | 901     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 727   | 276       | 1          | 1          | http://localhost:8080/user/signup | 900     | 0        | 3       |
| 1792510496765 | 92      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 726   | 275       | 1          | 1          | http://localhost:8080/user/signup | 92      | 0        | 0       |
| 1792510496858 | 89      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 727   | 276       | 2          | 2          | http://localhost:8080/user/signup | 88      | 0        | 0       |
| 1792510496861 | 94      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 725   | 274       | 2          | 2          | http://localhost:8080/user/signup | 94      | 0        | 2       |
| 1792510496947 | 100     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 725   | 274       | 2          | 2          | http://localhost:8080/user/signup | 100     | 0        | 0       |
| 1792510496955 | 109     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 724   | 273       | 2          | 2          | http://localhost:8080/user/signup | 108     | 0        | 0       |
| 1792510497047 | 128     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 725   | 274       | 2          | 2          | http://localhost:8080/user/signup | 128     | 0        | 0       |
| 1792510497064 | 125     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 725   | 274       | 1          | 1          | http://localhost:8080/user/signup | 125     | 0        | 0       |
| 1792510497189 | 81      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 723   | 272       | 1          | 1          | http://localhost:8080/user/signup | 81      | 0        | 0       |
| 1792510497270 | 82      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 725   | 274       | 1          | 1          | http://localhost:8080/user/signup | 81      | 0        | 0       |
| 1792510497853 | 14      | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-3  | text     | false   |                | 480   | 276       | 1          | 1          | http://localhost:8080/user/signup | 13      | 0        | 2       |
| 1792510497867 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-3  | text     | false   |                | 480   | 275       | 1          | 1          | http://localhost:8080/user/signup | 5       | 0        | 0       |
| 1792510497872 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-3  | text     | false   |                | 480   | 276       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510497877 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-3  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510497882 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-3  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510498852 | 8       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-4  | text     | false   |                | 480   | 273       | 1          | 1          | http://localhost:8080/user/signup | 8       | 0        | 1       |
| 1792510498861 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-4  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510498865 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-4  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510498870 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-4  | text     | false   |                | 480   | 272       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510498874 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-4  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510499852 | 10      | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-5  | text     | false   |                | 480   | 276       | 1          | 1          | http://localhost:8080/user/signup | 9       | 0        | 3       |
| 1792510499862 | 6       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-5  | text     | false   |                | 480   | 275       | 1          | 1          | http://localhost:8080/user/signup | 6       | 0        | 0       |
| 1792510499869 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-5  | text     | false   |                | 480   | 276       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510499874 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-5  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510499878 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-5  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510500852 | 8       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-6  | text     | false   |                | 480   | 273       | 1          | 1          | http://localhost:8080/user/signup | 7       | 0        | 1       |
| 1792510500860 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-6  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510500865 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-6  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510500871 | 6       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-6  | text     | false   |                | 480   | 272       | 1          | 1          | http://localhost:8080/user/signup | 5       | 0        | 0       |
| 1792510500878 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-6  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 5       | 0        | 0       |
| 1792510501854 | 8       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-7  | text     | false   |                | 480   | 276       | 1          | 1          | http://localhost:8080/user/signup | 8       | 0        | 2       |
| 1792510501862 | 6       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-7  | text     | false   |                | 480   | 275       | 1          | 1          | http://localhost:8080/user/signup | 5       | 0        | 0       |
| 1792510501868 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-7  | text     | false   |                | 480   | 276       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510501873 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-7  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510501877 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-7  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510502852 | 9       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-8  | text     | false   |                | 480   | 273       | 1          | 1          | http://localhost:8080/user/signup | 8       | 0        | 3       |
| 1792510502862 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-8  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510502866 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-8  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510502870 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-8  | text     | false   |                | 480   | 272       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510502874 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-8  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510503855 | 7       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-9  | text     | false   |                | 480   | 276       | 1          | 1          | http://localhost:8080/user/signup | 6       | 0        | 1       |
| 1792510503862 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-9  | text     | false   |                | 480   | 275       | 1          | 1          | http://localhost:8080/user/signup | 5       | 0        | 0       |
| 1792510503867 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-9  | text     | false   |                | 480   | 276       | 1          | 1          | http://localhost:8080/user/signup | 5       | 0        | 0       |
| 1792510503872 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-9  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510503876 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-9  | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510504854 | 7       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-10 | text     | false   |                | 480   | 273       | 1          | 1          | http://localhost:8080/user/signup | 7       | 0        | 1       |
| 1792510504861 | 5       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-10 | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510504866 | 3       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-10 | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 3       | 0        | 0       |
| 1792510504869 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-10 | text     | false   |                | 480   | 272       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |
| 1792510504873 | 4       | Requisição HTTP - User Signup | 409          |                 | Grupo de Usuários Teste 1-10 | text     | false   |                | 480   | 274       | 1          | 1          | http://localhost:8080/user/signup | 4       | 0        | 0       |

### Sumário

| Rótulo                        | # Amostras | Média | Mín. | Máx. | Desvio Padrão | % de Erro | Vazão   | KB/s | Sent KB/sec | Média de Bytes |
|-------------------------------|------------|-------|------|------|---------------|-----------|---------|------|-------------|----------------|
| Requisição HTTP - User Signup | 50         | 40    | 3    | 901  | 128,39        | 80,000%   | 5,54693 | 2,87 | 1,49        | 529,0          |
| TOTAL                         | 50         | 40    | 3    | 901  | 128,39        | 80,000%   | 5,54693 | 2,87 | 1,49        | 529,0          |

### Gráfico

<img width="2222" height="1536" alt="image" src="https://github.com/user-attachments/assets/5abfefff-8a74-4be0-bc3a-cde1aafe4adb" />

## Ponto de Atenção

Percebemos que usando a regra do e-mail único, o teste de desempenho só é efetuado no primeiro loop, já que nos 4 seguintes, ele fará a requisição HTTP com 10 usuários já cadastrados. Por esse motivo temos 10 amostras com código de resposta `201` e 40 com `409` negando a aplicação.

Um outro teste foi preparado com as mesmas configurações no JMeter, mas agora sem a regra do e-mail único. Eis os resultados.

## Resultados 2

| timeStamp     | elapsed | label                         | responseCode | responseMessage | threadName                   | dataType | success | failureMessage | bytes | sentBytes | grpThreads | allThreads | URL                               | Latency | IdleTime | Connect |
|---------------|---------|-------------------------------|--------------|-----------------|------------------------------|----------|---------|----------------|-------|-----------|------------|------------|-----------------------------------|---------|----------|---------|
| 1792791179792 | 87      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 726   | 276       | 1          | 1          | http://localhost:8080/user/signup | 87      | 0        | 4       |
| 1792791179880 | 85      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 725   | 275       | 1          | 1          | http://localhost:8080/user/signup | 85      | 0        | 0       |
| 1792791179966 | 86      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 726   | 276       | 1          | 1          | http://localhost:8080/user/signup | 86      | 0        | 0       |
| 1792791180053 | 118     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 118     | 0        | 0       |
| 1792791180172 | 78      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-1  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 78      | 0        | 0       |
| 1792791180779 | 83      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 723   | 273       | 1          | 1          | http://localhost:8080/user/signup | 83      | 0        | 3       |
| 1792791180863 | 83      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 82      | 0        | 0       |
| 1792791180947 | 79      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 722   | 274       | 1          | 1          | http://localhost:8080/user/signup | 79      | 0        | 0       |
| 1792791181026 | 85      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 722   | 272       | 1          | 1          | http://localhost:8080/user/signup | 85      | 0        | 0       |
| 1792791181112 | 87      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-2  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 87      | 0        | 0       |
| 1792791181778 | 81      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-3  | text     | true    |                | 726   | 276       | 1          | 1          | http://localhost:8080/user/signup | 81      | 0        | 2       |
| 1792791181860 | 81      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-3  | text     | true    |                | 725   | 275       | 1          | 1          | http://localhost:8080/user/signup | 81      | 0        | 0       |
| 1792791181942 | 77      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-3  | text     | true    |                | 726   | 276       | 1          | 1          | http://localhost:8080/user/signup | 76      | 0        | 0       |
| 1792791182019 | 81      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-3  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 81      | 0        | 0       |
| 1792791182101 | 78      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-3  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 78      | 0        | 0       |
| 1792791182779 | 84      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-4  | text     | true    |                | 723   | 273       | 1          | 1          | http://localhost:8080/user/signup | 84      | 0        | 4       |
| 1792791182864 | 102     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-4  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 101     | 0        | 0       |
| 1792791182966 | 79      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-4  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 79      | 0        | 0       |
| 1792791183046 | 81      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-4  | text     | true    |                | 720   | 272       | 1          | 1          | http://localhost:8080/user/signup | 81      | 0        | 0       |
| 1792791183127 | 79      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-4  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 79      | 0        | 0       |
| 1792791183779 | 82      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-5  | text     | true    |                | 726   | 276       | 1          | 1          | http://localhost:8080/user/signup | 82      | 0        | 1       |
| 1792791183861 | 80      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-5  | text     | true    |                | 725   | 275       | 1          | 1          | http://localhost:8080/user/signup | 80      | 0        | 0       |
| 1792791183942 | 78      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-5  | text     | true    |                | 726   | 276       | 1          | 1          | http://localhost:8080/user/signup | 78      | 0        | 0       |
| 1792791184020 | 82      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-5  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 82      | 0        | 0       |
| 1792791184103 | 79      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-5  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 79      | 0        | 0       |
| 1792791184776 | 83      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-6  | text     | true    |                | 723   | 273       | 1          | 1          | http://localhost:8080/user/signup | 82      | 0        | 4       |
| 1792791184859 | 81      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-6  | text     | true    |                | 722   | 274       | 1          | 1          | http://localhost:8080/user/signup | 81      | 0        | 0       |
| 1792791184940 | 80      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-6  | text     | true    |                | 720   | 274       | 1          | 1          | http://localhost:8080/user/signup | 80      | 0        | 0       |
| 1792791185020 | 130     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-6  | text     | true    |                | 722   | 272       | 1          | 1          | http://localhost:8080/user/signup | 127     | 0        | 0       |
| 1792791185150 | 139     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-6  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 138     | 0        | 0       |
| 1792791185776 | 92      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-7  | text     | true    |                | 726   | 276       | 1          | 1          | http://localhost:8080/user/signup | 91      | 0        | 1       |
| 1792791185868 | 81      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-7  | text     | true    |                | 725   | 275       | 1          | 1          | http://localhost:8080/user/signup | 81      | 0        | 0       |
| 1792791185949 | 77      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-7  | text     | true    |                | 726   | 276       | 1          | 1          | http://localhost:8080/user/signup | 77      | 0        | 0       |
| 1792791186027 | 84      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-7  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 83      | 0        | 0       |
| 1792791186111 | 85      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-7  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 85      | 0        | 0       |
| 1792791186780 | 83      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-8  | text     | true    |                | 723   | 273       | 1          | 1          | http://localhost:8080/user/signup | 83      | 0        | 4       |
| 1792791186864 | 79      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-8  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 79      | 0        | 0       |
| 1792791186943 | 88      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-8  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 88      | 0        | 0       |
| 1792791187031 | 99      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-8  | text     | true    |                | 722   | 272       | 1          | 1          | http://localhost:8080/user/signup | 98      | 0        | 0       |
| 1792791187130 | 78      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-8  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 78      | 0        | 0       |
| 1792791187780 | 119     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-9  | text     | true    |                | 724   | 276       | 1          | 1          | http://localhost:8080/user/signup | 119     | 0        | 1       |
| 1792791187900 | 129     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-9  | text     | true    |                | 725   | 275       | 1          | 1          | http://localhost:8080/user/signup | 128     | 0        | 0       |
| 1792791188029 | 103     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-9  | text     | true    |                | 726   | 276       | 1          | 1          | http://localhost:8080/user/signup | 102     | 0        | 0       |
| 1792791188132 | 95      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-9  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 95      | 0        | 0       |
| 1792791188228 | 81      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-9  | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 81      | 0        | 0       |
| 1792791188777 | 105     | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-10 | text     | true    |                | 723   | 273       | 1          | 1          | http://localhost:8080/user/signup | 105     | 0        | 2       |
| 1792791188883 | 79      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-10 | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 79      | 0        | 0       |
| 1792791188962 | 78      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-10 | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 78      | 0        | 0       |
| 1792791189040 | 80      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-10 | text     | true    |                | 720   | 272       | 1          | 1          | http://localhost:8080/user/signup | 80      | 0        | 0       |
| 1792791189121 | 79      | Requisição HTTP - User Signup | 201          |                 | Grupo de Usuários Teste 1-10 | text     | true    |                | 724   | 274       | 1          | 1          | http://localhost:8080/user/signup | 79      | 0        | 0       |

### Sumário

| Rótulo                        | # Amostras | Média | Mín. | Máx. | Desvio Padrão | % de Erro | Vazão   | KB/s | Sent KB/sec | Média de Bytes |
|-------------------------------|------------|-------|------|------|---------------|-----------|---------|------|-------------|----------------|
| Requisição HTTP - User Signup | 50         | 88    | 77   | 139  | 14,75         | 0,000%    | 5,31463 | 3,76 | 1,42        | 723,9          |
| TOTAL                         | 50         | 88    | 77   | 139  | 14,75         | 0,000%    | 5,31463 | 3,76 | 1,42        | 723,9          |

### Gráfico

<img width="2222" height="1536" alt="image" src="https://github.com/user-attachments/assets/47bba784-c0cd-486f-a07e-33d1f738c7a4" />

## Relatório da Atividade

O tempo médio de resposta no teste configurado corretamente foi de 88ms, não havendo falhas e com uma taxa de 1,42 kilobytes por segundo. Observamos que, comparado ao primeiro teste feito com a regra do e-mail único, o desvio padrão foi bem mais baixo. Isso é explicado não somente pelas amostras com erro que tinham uma resposta em latência significativamente menor, mas pelo fato do banco precisar ter sido sempre reiniciado para alocar usuários com e-mails ainda não cadastrados. Sem a regra do e-mail único, o banco pôde permanecer aberto, diminuindo a latência de novos usuários e diminuindo a diferença de latência entre as amostras.

Podemos demonstrar isso reiniciando o banco e fazendo 5 sessões de entrada com 10 usuários simultâneos uma seguida da outra, todas sem a regra do e-mail único e inicializadas na mesma instância de abertura da aplicação.

<img width="789" height="572" alt="image" src="https://github.com/user-attachments/assets/2846e5ae-18cc-4d64-9b3c-0851d537ac6c" />

A partir do gráfico percebemos que o desvio padrão diminuiu enquanto alcançava uma estabilidade, mesmo com a mediana e a média já sendo estáveis desde o início. O que indica que, apesar da média de desempenho permanecer estável, mais e mais inicializações fazem com que a latência de diferentes usuários tenham um valor cada vez mais próximo e estável.

Diante disso, vejamos o que acontece quando, ainda na mesma inicialização do banco, aumentamos o número de usuários por teste de desempenho. Os próximos resultados representam os resultados unidos de três testes de desempenho consecutivos: um com 10 usuários simultâneos, outro com 50 e um último com 100:

| Rótulo                     | # Amostras | Média | Mín. | Máx. | Desvio Padrão | % de Erro | Vazão    | KB/s  | Sent KB/sec | Média de Bytes |
|----------------------------|------------|-------|------|------|---------------|-----------|----------|-------|-------------|----------------|
| User Signup - 10 usuários  | 50         | 83    | 75   | 152  | 14,02         | 0,000%    | 5,33333  | 3,78  | 1,43        | 726,1          |
| User Signup - 50 usuários  | 250        | 118   | 78   | 203  | 25,59         | 0,000%    | 24,41406 | 17,31 | 6,54        | 726,0          |
| User Signup - 100 usuários | 500        | 1693  | 96   | 3026 | 797,09        | 0,000%    | 27,17391 | 19,27 | 7,28        | 726,0          |
| TOTAL                      | 800        | 1100  | 75   | 3026 | 991,55        | 0,000%    | 4,58314  | 3,25  | 1,23        | 726,0          |

Podemos observar que quanto mais usuários simultâneos realizam o cadastro, maior fica a latência máxima, a vazão dos dados e o desvio padrão. Apesar disso, o aumento de latência máxima não é tão expressivo quanto o de latência mínima, o que pode indicar uma tendência à estabilidade de latência e um desvio padrão mais baixo, mesmo com mais usuários sendo cadastrados. Observemos o gráfico:

<img width="949" height="555" alt="image" src="https://github.com/user-attachments/assets/3fd0f278-4f37-4a99-b7ca-145412a51230" />

Tanto o desvio padrão como a média apresentam comportamentos similares conforme o número de usuários simultâneos cadastrados. Eles sobem, mas ainda com uma certa "gravidade" que o tendenciam a diminuir mesmo distantes dos testes anteriores. Se continuarmos a cadastrar 100 usuários simultâneos, o gráfico continua o comportamento, apresentado o resultado abaixo:

<img width="3049" height="557" alt="image" src="https://github.com/user-attachments/assets/8518616f-6d23-462f-9d43-4159c5bcabbc" />

Assim, o desvio padrão busca uma estabilidade, diminuindo a cada teste. Enquanto isso a média de latência sempre sobe, mas ainda com picos baixos que retardam o aumento desenfreado.

## Conclusão

Precisamos nos atentar a quantos usuários usam nosso sistema ao mesmo tempo. Quanto mais usuários se cadastram simultaneamente, maior a chance de um usuário ter uma latência mais alta do que a maioria. Outro cuidado que devemos ter é o período de inicialização do banco. Sempre que quisermos fazer atualizações de sistema, ou qualquer procedimento que peça a reinicialização do banco, estaremos aumentando as chances de que um usuário tenha uma latência maior para a realização do cadastro. Ainda assim, com os testes efetuados podemos ter mais segurança com relação à conformidade do desempenho, tendo ciência de que mesmo com muitos usuários, o sistema tende a se adaptar e encontrar uma estabilidade latência que diminuirá a discrepância de desempenho entre um usuário e outro.

---



