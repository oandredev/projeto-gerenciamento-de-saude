# Gerado com DeepSeek 🐋

**Prompt:**
> [Documentos anexados](/Pesquisa/Documentos%20usados%20nos%20prompts)
>
> Estou projetando um aplicativo mobile para usuários de remédio, consultas e exames médicos frequentes, com público foco em pessoas idosas e/ou cuidadores. A principal função do aplicativo é oferecer lembretes, avisos prévios e notificações sobre medicamentos receitados, consultas e exames agendados. O projeto precisa levar em consideração as normas e regulamentos do CFM e da Anvisa sobre receituários para rotular os medicamentos de acordo com as receitas médicas.
>
> Preciso definir, com base nos regulamentos e modelos de receita, possíveis requisitos não funcionais do aplicativo, que sejam sustentados por possíveis dificuldades que usuários de remédio já possuem na interação tradicional com receitas médicas. Ex: pacientes que tomam remédios de alto controle e se esquecem de pedir uma nova receita antes que o remédio acabe. É necessário saber de possíveis cenários adversos que, através de recursos simples do aplicativo, podem ser evitados com mais facilidade.
>
> Além disso, é necessário saber as possíveis influências que o novo SNCR possa ter na dinâmica do aplicativo como uma API. Mesmo que seja impossível usar a API sem obter a profissão médica, pense em cenários nos quais a integração dessa API com o aplicativo podem representar soluções de bom interesse para os usuários do aplicativo.
>
> Abaixo estão requisitos não funcionais, cenários adversos e possibilidades de integração com o SNCR para um aplicativo mobile de lembretes de medicamentos, consultas e exames, voltado a idosos e cuidadores. O texto considera as normas citadas: Portaria SVS/MS nº 344/1998, RDC Anvisa nº 1.000/2025, Resoluções CFM nº 2.299/2021 e nº 2.309/2022, manuais do SNCR e documentação da API SNCR.

## 1. Premissas regulatórias e de escopo

O aplicativo **não deve**:
- emitir, assinar ou dispensar receitas;
- substituir a validação do farmacêutico, da Vigilância Sanitária ou do SNCR;
- calcular dose sem que ela conste expressamente da receita;
- armazenar ou trafegar dados sensíveis sem base legal e consentimento.

O aplicativo **deve**:
- rotular medicamentos conforme a receita: nome do medicamento ou substância (DCB), concentração, forma farmacêutica, quantidade, posologia, via, duração, horários, advertências obrigatórias e dados do prescritor/paciente;
- exibir advertências específicas de controlados: Talidomida, retinoides sistêmicos, misoprostol, opioides, psicotrópicos, anabolizantes etc.;
- respeitar validade e limites da Portaria 344/98: NRA/NRB/NRR válidas por 30 dias, C1/C5 com quantidade para até 60 dias de tratamento (e validade de 30 dias para dispensação), NRA com máximo de 5 ampolas ou 30 dias, NRB com até 60 dias, entre outros;
- tratar dados conforme LGPD, CFM 2.299/2021 (documentos médicos eletrônicos) e CFM 2.309/2022 (compartilhamento de dados médicos).

## 2. Requisitos não funcionais (RNF)

| ID | Categoria | Requisito | Justificativa |
|---|---|---|---|
| RNF01 | Acessibilidade | Suportar leitores de tela, fontes escaláveis, alto contraste, comandos por voz, feedback sonoro, visual e por vibração. | Público idoso e cuidadores com baixa visão, audição ou letramento digital. |
| RNF02 | Usabilidade assistida | Modo cuidador com múltiplos perfis e permissões granulares; confirmação de dose em um toque; onboarding guiado. | Idosos podem depender de terceiros para configurar e monitorar. |
| RNF03 | Confiabilidade de lembretes | Alarmes locais mesmo offline; reagendamento após reinício; redundância via push + SMS + chamada quando crítico. | Remédios de alto controle não podem depender de internet instável. |
| RNF04 | Segurança | Criptografia em trânsito e repouso; MFA; controle de acesso por papel; notificações discretas sem nome do medicamento na tela bloqueada. | Dados de saúde são sensíveis (LGPD art. 5º, II). |
| RNF05 | Privacidade | Consentimento explícito; minimização de dados; direito de exclusão; trilha de auditoria de acessos do cuidador. | LGPD e CFM 2.309/2022. |
| RNF06 | Interoperabilidade | Suporte a HL7 FHIR, QR Code de receita eletrônica, Gov.br, SNCR e sistemas de farmácia. | Receitas eletrônicas já usam assinatura ICP-Brasil e QR de verificação. |
| RNF07 | Rastreabilidade | Logs imutáveis de lembretes, confirmações, atrasos, alterações de prescrição e acessos. | Auditoria e segurança do paciente. |
| RNF08 | Desempenho | Sincronização em até 5 s; notificações em tempo real; baixo consumo de bateria e dados. | Uso diário por idosos, muitas vezes com aparelhos simples. |
| RNF09 | Disponibilidade | Serviços de nuvem com 99,9% de disponibilidade; lembretes críticos não dependem da nuvem. | Falha de servidor não pode impedir dose. |
| RNF10 | Conformidade legal | Aderência à Portaria 344/98, RDC 1.000/2025, CFM 2.299/2021, CFM 2.309/2022, LGPD. | Evitar responsabilidade civil e sanitária. |
| RNF11 | Manutenibilidade | Regras de validade, limites e advertências configuráveis por norma. | Anvisa e CFM atualizam normas com frequência. |
| RNF12 | Localização | Português brasileiro, linguagem simples, terminologia leiga. | Reduzir erro de interpretação. |
| RNF13 | Retenção e descarte | Dados de prescrição mantidos apenas pelo tempo necessário; descarte seguro. | LGPD e boas práticas. |
| RNF14 | Rotulagem fiel | Não alterar dose, via, concentração ou posologia; apenas transcrever da receita. | Erro de rotulagem pode causar dano grave. |

## 3. Cenários adversos e recursos simples do aplicativo

| Cenário adverso | Dificuldade tradicional | Recurso do app | Base regulatória |
|---|---|---|---|
| Paciente de alto controle esquece de renovar receita antes do fim | Receita vence em 30 dias; quantidade pode durar 60 dias, mas dispensação só vale 30 dias. | Alerta “renove sua receita” 7–10 dias antes do fim da validade e do estoque; checklist para consulta. | Portaria 344/98, arts. 41, 45, 50. |
| Usar receita vencida | Paciente não confere data. | App valida data de emissão e validade; bloqueia lembrete se vencida; orienta nova consulta. | Portaria 344/98. |
| Receita eletrônica não é verificada | Paciente não sabe se é válida. | Leitura de QR Code e consulta ao SNCR/ITI; exibe status “autorizada”, “inválida” ou “cancelada”. | RDC 1.000/2025; CFM 2.299/2021. |
| Confundir medicamentos com nomes parecidos | Idoso pode trocar caixas. | Rótulo com DCB, concentração, forma, cor da caixa, foto e horário; leitura de código de barras. | Boas práticas de segurança do paciente. |
| Dose duplicada | Esquecimento ou confusão. | Registro de confirmação; bloqueio de nova dose no intervalo; alerta ao cuidador. | Segurança do paciente. |
| Perder consulta ou exame | Múltiplos agendamentos. | Sincronização com calendário; lembretes 7 dias, 1 dia, 2 h antes; preparo e local. | Usabilidade. |
| Cuidador não sabe se idoso tomou remédio | Falta de comunicação. | Notificação ao cuidador se dose não confirmada em X minutos. | RNF02. |
| Perda ou roubo de receituário controlado | Trâmites burocráticos. | App orienta Boletim de Ocorrência e comunicação à Visa local; guarda cópia digital criptografada. | Portaria 344/98, art. 39. |
| Talidomida/retinoides com risco teratogênico | Advertências complexas. | Exibe advertência obrigatória, checklist de consentimento, lembretes de exames e métodos contraceptivos. | Portaria 344/98, Anexos VII, XV, XVI. |
| Limite de quantidade excedido | Paciente não sabe que receita só permite 5 ampolas/30 dias. | Alerta quando a prescrição estiver próxima do limite; orienta farmácia. | Portaria 344/98, arts. 43, 46, 50, 59. |

## 4. Influência do SNCR e da API no aplicativo

### 4.1 O que a API SNCR é e não é

A API SNCR foi feita para **prescritores e plataformas de prescrição eletrônica** obterem numerações de notificações de receita autorizadas pela Vigilância Sanitária. Ela exige:
- autenticação via Gov.br;
- profissional com inscrição ativa em CFM, CFMV ou CFO;
- validação de conselho, UF e documento;
- limites de 50 números por tipo, por prescritor, por dia, e 3.000 por mês.

Portanto, um app para pacientes/cuidadores **não pode** chamar diretamente a API como se fosse médico. A integração precisa ser indireta, via backend autorizado, clínica, plataforma de prescrição ou farmácia.

### 4.2 Cenários de integração benéficos ao usuário

1. **Leitura de QR Code da receita eletrônica**  
   O app escaneia o QR da receita, extrai número, tipo, data, validade, prescritor e paciente, e consulta o SNCR/ITI para verificar autenticidade. Isso evita receita falsa ou vencida.

2. **Recebimento de prescrição via clínica/plataforma integrada**  
   Se a clínica usa uma plataforma que consome a API SNCR, o app pode receber a prescrição já validada e criar lembretes automaticamente. O paciente não precisa digitar.

3. **Confirmação de dispensação pela farmácia**  
   Farmácias integradas ao SNCR podem enviar evento de dispensação ao app. O app atualiza estoque, calcula próxima renovação e avisa quando o remédio estiver acabando.

4. **Monitoramento de status da numeração**  
   O app pode exibir se a receita está “válida”, “autorizada”, “cancelada” ou “inválida”. Para alto controle, isso ajuda a evitar uso de numeração cancelada.

5. **Alerta de renovação baseado em validade e consumo**  
   Com data de emissão, validade (30 dias para NRA/NRB/NRR) e posologia, o app calcula quando a receita vencerá e quando o remédio acabará. Se a receita vencer antes, alerta para nova consulta.

6. **Modo cuidador com verificação de receitas**  
   O cuidador pode escanear a receita, verificar status no SNCR e ser alertado se a numeração não estiver autorizada.

7. **Integração futura com portal do paciente Gov.br**  
   Embora a API atual seja para prescritores, é possível que o SNCR evolua para permitir ao paciente consultar suas próprias numerações. O app pode se preparar para autenticação Gov.br do paciente e consumo de dados autorizados.

### 4.3 Requisitos técnicos para integração

- **Backend-for-frontend**: nunca embutir credenciais da API SNCR no app.
- **OAuth 2.0/OIDC com Gov.br** para profissionais; para pacientes, usar Gov.br apenas como identidade, se aplicável.
- **JWT de curta duração**, session_id de uso único e expiração de 30 s, conforme manual da API.
- **CORS e domínios .br**, respeitando as restrições da API.
- **Auditoria e LGPD**: registrar quem acessou, quando e para quê.
- **Validação de assinatura digital** via ITI (validar.iti.gov.br).
- **Fallback offline**: lembretes críticos continuam funcionando sem internet.

## 5. Considerações finais

O aplicativo deve ser um **assistente de adesão e organização**, não um sistema de prescrição. Seus maiores ganhos para idosos e cuidadores vêm de:
- rotulagem fiel e acessível;
- lembretes confiáveis e multimodais;
- alertas de renovação antes do fim da validade/estoque;
- verificação de autenticidade e status da receita;
- integração indireta com SNCR via QR Code, clínicas, plataformas e farmácias;
- conformidade rigorosa com Anvisa, CFM e LGPD.

Com esses requisitos, o app reduz riscos como uso de receita vencida, falta de medicamento controlado, dose duplicada e perda de consultas, ao mesmo tempo em que respeita as competências da Vigilância Sanitária e do prescritor.

---

**Comentário:**
> É melhor não considerarmos o cenário da API do SNCR dentro do aplicativo por conta de todas as barreiras legais. Mesmo assim, a documentação da API ajuda bastante na estruturação de dados e na [modelagem de classes](https://notebook.google.com/notebook/451886a3-f2f9-4464-9522-43a775bfbf4c/artifact/ff329a71-18dd-49c6-a25b-cd38f868a977?utm_source=nlm_web_share&utm_medium=google_oo&utm_campaign=art_share_1&utm_content=&utm_smc=nlm_web_share_google_oo_art_share_1_).
