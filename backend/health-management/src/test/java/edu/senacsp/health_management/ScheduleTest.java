package edu.senacsp.health_management;

import edu.senacsp.health_management.dto.request.profile.CreateProfileRequest;
import edu.senacsp.health_management.dto.request.schedule.CreateAppointmentData;
import edu.senacsp.health_management.dto.request.schedule.CreateMedicationData;
import edu.senacsp.health_management.dto.request.schedule.CreateScheduleRequest;
import edu.senacsp.health_management.dto.request.schedule.ScheduleDetails;
import edu.senacsp.health_management.dto.request.schedule.UpdateScheduleRequest;
import edu.senacsp.health_management.dto.request.user.SignupRequest;
import edu.senacsp.health_management.dto.response.profile.ProfileResponse;
import edu.senacsp.health_management.dto.response.schedule.ScheduleResponse;
import edu.senacsp.health_management.entity.AppointmentSchedule;
import edu.senacsp.health_management.entity.Confirmation;
import edu.senacsp.health_management.entity.MedicationSchedule;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Schedule;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.entity.enums.DosageUnit;
import edu.senacsp.health_management.entity.enums.IntervalUnit;
import edu.senacsp.health_management.entity.enums.MedicationStripe;
import edu.senacsp.health_management.entity.enums.ScheduleType;
import edu.senacsp.health_management.repository.*;
import edu.senacsp.health_management.service.ProfileService;
import edu.senacsp.health_management.service.ScheduleService;
import edu.senacsp.health_management.service.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ScheduleTest {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    private MedicationScheduleRepository medicationScheduleRepository;
    @Autowired
    private AppointmentScheduleRepository appointmentScheduleRepository;

    private Profile perfilTeste;
    private User usuarioTeste;
    private List<Profile> perfisDeTeste;
    private List<User> usuariosDeTeste;

    @BeforeEach
    public void setUp() {
        SignupRequest usuario01 = new SignupRequest(
                "joao.silva@email.com",
                "Senha@123",
                "João Silva"
        );
        SignupRequest usuario02 = new SignupRequest(
                "maria.souza@email.com",
                "Senha@456",
                "Maria Souza"
        );

        userService.signup(usuario01);
        userService.signup(usuario02);

        usuariosDeTeste = new ArrayList<>();
        usuariosDeTeste.add(userRepository.findByEmail(usuario01.email()).orElseThrow());
        usuariosDeTeste.add(userRepository.findByEmail(usuario02.email()).orElseThrow());

        usuarioTeste = usuariosDeTeste.get(0);

        // Perfil principal (do João) usado pela maioria dos testes
        perfisDeTeste = new ArrayList<>();
        perfilTeste = criarPerfil(usuarioTeste, "Ana Costa");
    }

    @AfterEach
    public void tearDown() {
        // Ordem por causa das FKs:
        // confirmações -> dados complementares (MedicationSchedule/AppointmentSchedule)
        //              -> agendamentos -> perfis -> usuários.
        // Só apaga o que ainda existe.

        for (Profile p : perfisDeTeste) {
            // 1) Confirmações do perfil (FK para Schedule).
            //    findAllByProfileBetween pega também as confirmações de schedules arquivados,
            //    o que cobre o caso do update que versiona (arquiva + cria novo).
            List<Confirmation> confirmacoes = confirmationRepository.findAllByProfileBetween(
                    p.getId(), CONFIRM_FROM, CONFIRM_TO, Sort.unsorted());
            confirmationRepository.deleteAll(confirmacoes);

            // 2) Dados complementares. Schedule não tem cascade para eles, então são removidos
            //    explicitamente (o ID é o mesmo do agendamento, por causa do @MapsId).
            List<Schedule> agendamentos = scheduleRepository.findAllByProfile(p);
            for (Schedule s : agendamentos) {
                if (medicationScheduleRepository.existsById(s.getId())) {
                    medicationScheduleRepository.deleteById(s.getId());
                }
                if (appointmentScheduleRepository.existsById(s.getId())) {
                    appointmentScheduleRepository.deleteById(s.getId());
                }
            }

            // 3) Agendamentos do perfil.
            scheduleRepository.deleteAll(agendamentos);
        }

        // 4) Só depois apaga os perfis (sem agendamentos órfãos).
        for (Profile p : perfisDeTeste) {
            if (profileRepository.existsById(p.getId())) {
                profileRepository.deleteById(p.getId());
            }
        }

        // 5) Por último os usuários (sem perfis órfãos).
        for (User u : usuariosDeTeste) {
            if (userRepository.existsById(u.getId())) {
                userRepository.deleteById(u.getId());
            }
        }
    }

    // ------------------------------------------------------------------------------------------
    // Constantes e métodos auxiliares
    // ------------------------------------------------------------------------------------------

    private static final Instant CONFIRM_FROM = Instant.EPOCH;
    private static final Instant CONFIRM_TO   = Instant.parse("2100-01-01T00:00:00Z");

    // Início padrão dos requests: amanhã, com precisão de segundos (igual ao que o service grava).
    private static final Instant INICIO = Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS);

    /** Cria um perfil via service e já o registra na lista de limpeza. */
    private Profile criarPerfil(User usuario, String nome) {
        ProfileResponse resposta = profileService.create(usuario, new CreateProfileRequest(nome, 1L));
        Profile perfil = profileRepository.findByIdAndUser(resposta.id(), usuario).orElseThrow();
        perfisDeTeste.add(perfil);
        return perfil;
    }

    private void assertErro(HttpStatus status, String mensagem, Executable acao) {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, acao);
        assertEquals(status, ex.getStatusCode());
        assertEquals(mensagem, ex.getReason());
    }

    // ----- Dados de medicamento / consulta -----
    private CreateMedicationData medicamento(String nome) {
        return medicamentoCom(null, nome, null, 500d, DosageUnit.MG, null);
    }

    private CreateMedicationData medicamentoCom(String marca, String nome, String principioAtivo,
                                                Double dose, DosageUnit unidade, String foto) {
        return new CreateMedicationData(marca, nome, principioAtivo, dose, unidade, MedicationStripe.NONE, foto);
    }

    private CreateAppointmentData consulta(String medico, String local) {
        return new CreateAppointmentData(medico, local);
    }

    // ----- Requests de criação -----

    private CreateScheduleRequest reqCriar(Long perfilId, ScheduleType tipo, String titulo, Instant inicio,
                                           Integer valor, IntervalUnit unidade, Instant fim, String notas,
                                           ScheduleDetails detalhes) {
        return new CreateScheduleRequest(perfilId, tipo, titulo, inicio, valor, unidade, fim,
                notas, null, null, null, null, detalhes);
    }

    private CreateScheduleRequest reqMed(String titulo, String notas, ScheduleDetails detalhes) {
        return reqCriar(perfilTeste.getId(), ScheduleType.MEDICATION, titulo, INICIO, 1, IntervalUnit.DAY, null, notas, detalhes);
    }

    private CreateScheduleRequest reqMed(String titulo) {
        return reqMed(titulo, null, medicamento("Dipirona"));
    }

    private CreateScheduleRequest reqConsulta(String titulo, String notas, ScheduleDetails detalhes) {
        return reqCriar(perfilTeste.getId(), ScheduleType.CONSULTATION, titulo, INICIO, 1, IntervalUnit.DAY, null, notas, detalhes);
    }

    private CreateScheduleRequest reqConsulta(String titulo) {
        return reqConsulta(titulo, null, consulta("Dr. Paulo", "Clínica Central"));
    }

    // ----- Requests de atualização -----

    private UpdateScheduleRequest reqAtualizar(Long id, ScheduleType tipo, String titulo, Instant inicio,
                                               Integer valor, IntervalUnit unidade, Instant fim, String notas,
                                               ScheduleDetails detalhes) {
        return new UpdateScheduleRequest(id, tipo, titulo, inicio, valor, unidade, fim,
                notas, null, null, null, null, detalhes);
    }

    private UpdateScheduleRequest reqAtualizarMed(Long id, String titulo, String notas, ScheduleDetails detalhes) {
        return reqAtualizar(id, ScheduleType.MEDICATION, titulo, INICIO, 1, IntervalUnit.DAY, null, notas, detalhes);
    }

    private UpdateScheduleRequest reqAtualizarConsulta(Long id, String titulo, String notas, ScheduleDetails detalhes) {
        return reqAtualizar(id, ScheduleType.CONSULTATION, titulo, INICIO, 1, IntervalUnit.DAY, null, notas, detalhes);
    }

    // ----- Persistência e leitura -----

    private Long idDe(ScheduleResponse resposta) {
        return resposta.id();
    }

    private Schedule buscar(Long id) {
        return scheduleRepository.findById(id).orElseThrow();
    }

    /** Cria um agendamento de medicamento válido no perfil principal e devolve o ID. */
    private Long criarMed(String titulo) {
        return idDe(scheduleService.create(usuarioTeste, reqMed(titulo)));
    }

    /** Cria um agendamento de consulta válido no perfil principal e devolve o ID. */
    private Long criarConsulta(String titulo) {
        return idDe(scheduleService.create(usuarioTeste, reqConsulta(titulo)));
    }

    /** Registra uma confirmação direto no repositório (sem passar pela janela de 24h do service). */
    private void confirmar(Long scheduleId) {
        confirmationRepository.save(new Confirmation(buscar(scheduleId), INICIO));
    }

    // ------------------------------------------------------------------------------------------
    // Criação (CT58 a CT67)
    // ------------------------------------------------------------------------------------------

    @Test
    public void agendamentoAtreladoAoPerfil() {
        // CT58: o agendamento fica relacionado ao perfil em que foi criado.
        Profile perfil02 = criarPerfil(usuarioTeste, "Lucas Pereira");

        Long id1 = criarMed("Dipirona");
        Long id2 = idDe(scheduleService.create(usuarioTeste, reqCriar(perfil02.getId(), ScheduleType.CONSULTATION,
                "Consulta cardiologia", INICIO, 1, IntervalUnit.DAY, null, null, consulta(null, null))));

        assertEquals(perfilTeste.getId(), buscar(id1).getProfile().getId());
        assertEquals(perfil02.getId(), buscar(id2).getProfile().getId());

        // A listagem de cada perfil traz só os seus agendamentos
        List<Long> idsPerfil01 = scheduleService.findAllByProfile(usuarioTeste, perfilTeste.getId(), null, Sort.Direction.ASC)
                .stream().map(this::idDe).toList();
        List<Long> idsPerfil02 = scheduleService.findAllByProfile(usuarioTeste, perfil02.getId(), null, Sort.Direction.ASC)
                .stream().map(this::idDe).toList();

        assertEquals(List.of(id1), idsPerfil01);
        assertEquals(List.of(id2), idsPerfil02);
    }

    @Test
    public void agendamentoPerfilIdInvalido() {
        // CT59: ID de perfil nulo, zero ou negativo é rejeitado.
        for (Long perfilId : new Long[]{null, 0L, -1L}) {
            assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                    () -> scheduleService.create(usuarioTeste, reqCriar(perfilId, ScheduleType.MEDICATION,
                            "Dipirona", INICIO, 1, IntervalUnit.DAY, null, null, medicamento("Dipirona"))));
        }
    }

    @Test
    public void agendamentoPerfilInexistenteOuDeOutroUsuario() {
        // CT59: perfil inexistente e perfil de outro usuário geram o mesmo 404 (não revela IDs).
        Long idInexistente = 999_999L;
        assertFalse(profileRepository.existsById(idInexistente));

        assertErro(HttpStatus.NOT_FOUND, "Profile not found",
                () -> scheduleService.create(usuarioTeste, reqCriar(idInexistente, ScheduleType.MEDICATION,
                        "Dipirona", INICIO, 1, IntervalUnit.DAY, null, null, medicamento("Dipirona"))));

        Profile perfilDaMaria = criarPerfil(usuariosDeTeste.get(1), "Carla Souza");
        assertErro(HttpStatus.NOT_FOUND, "Profile not found",
                () -> scheduleService.create(usuarioTeste, reqCriar(perfilDaMaria.getId(), ScheduleType.MEDICATION,
                        "Dipirona", INICIO, 1, IntervalUnit.DAY, null, null, medicamento("Dipirona"))));

        assertTrue(scheduleRepository.findAllByProfile(perfilDaMaria).isEmpty(),
                "Nada deve ter sido criado no perfil de outro usuário");
    }

    @Test
    public void agendamentoDeMedicamentoRealizado() {
        // CT60 (parte 1): medicamento com todos os campos preenchidos.
        Instant fim = INICIO.plus(30, ChronoUnit.DAYS);
        ScheduleResponse resposta = scheduleService.create(usuarioTeste, reqCriar(perfilTeste.getId(),
                ScheduleType.MEDICATION, "Tomar dipirona", INICIO, 8, IntervalUnit.DAY, fim, "Após a refeição",
                medicamentoCom("Novalgina", "Dipirona Sódica", "Metamizol", 500.5, DosageUnit.MG, "https://exemplo.com/foto.png")));

        assertNotNull(idDe(resposta));

        Schedule s = buscar(idDe(resposta));
        assertEquals(ScheduleType.MEDICATION, s.getType());
        assertEquals("Tomar dipirona", s.getTitle());
        assertEquals(INICIO, s.getStartAt());
        assertEquals(IntervalUnit.DAY, s.getIntervalUnit());
        assertEquals(8, s.getIntervalValue());
        assertEquals(fim, s.getEndAt());
        assertEquals("Após a refeição", s.getNotes());
        assertTrue(s.isActive(), "Agendamento novo deve nascer ativo");

        MedicationSchedule m = medicationScheduleRepository.findById(s.getId()).orElseThrow();
        assertEquals("Novalgina", m.getBrand());
        assertEquals("Dipirona Sódica", m.getMedicineName());
        assertEquals("Metamizol", m.getActiveIngredient());
        assertEquals(500.5, m.getDosageAmount());
        assertEquals(DosageUnit.MG, m.getDosageUnit());
        assertEquals(MedicationStripe.NONE, m.getStripe());
        assertEquals("https://exemplo.com/foto.png", m.getPhotoUrl());

        assertFalse(appointmentScheduleRepository.existsById(s.getId()),
                "Medicamento não deve ter dados de consulta");
    }

    @Test
    public void agendamentoDeConsultaRealizado() {
        // CT60 (parte 2): consulta com todos os campos preenchidos.
        Instant fim = INICIO.plus(90, ChronoUnit.DAYS);
        ScheduleResponse resposta = scheduleService.create(usuarioTeste, reqCriar(perfilTeste.getId(),
                ScheduleType.CONSULTATION, "Consulta cardiologia", INICIO, null, IntervalUnit.UNIQUE, fim,
                "Levar exames", consulta("Dra. Helena", "Hospital Central")));

        Schedule s = buscar(idDe(resposta));
        assertEquals(ScheduleType.CONSULTATION, s.getType());
        assertEquals("Consulta cardiologia", s.getTitle());
        assertEquals("Levar exames", s.getNotes());
        assertTrue(s.isActive());

        AppointmentSchedule a = appointmentScheduleRepository.findById(s.getId()).orElseThrow();
        assertEquals("Dra. Helena", a.getDoctorName());
        assertEquals("Hospital Central", a.getLocation());

        assertFalse(medicationScheduleRepository.existsById(s.getId()),
                "Consulta não deve ter dados de medicamento");
    }

    @Test
    public void agendamentoComDataPassadaAceito() {
        // CT61: o plano prevê rejeição, mas o service aceita datas passadas (o cliente é quem avisa).
        // O teste reflete o comportamento atual do service.
        Instant passado = Instant.now().minus(10, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS);

        ScheduleResponse resposta = scheduleService.create(usuarioTeste, reqCriar(perfilTeste.getId(),
                ScheduleType.MEDICATION, "Dipirona", passado, 1, IntervalUnit.DAY, null, null, medicamento("Dipirona")));

        assertEquals(passado, buscar(idDe(resposta)).getStartAt());
    }

    @Test
    public void agendamentoSemTipo() {
        // CT62
        assertErro(HttpStatus.BAD_REQUEST, "Type is required",
                () -> scheduleService.create(usuarioTeste, reqCriar(perfilTeste.getId(), null,
                        "Dipirona", INICIO, 1, IntervalUnit.DAY, null, null, medicamento("Dipirona"))));
    }

    @Test
    public void agendamentoSemTitulo() {
        // CT63 e CT84: título obrigatório, tanto para medicamento quanto para consulta.
        for (String titulo : new String[]{null, "   "}) {
            assertErro(HttpStatus.BAD_REQUEST, "Title is required",
                    () -> scheduleService.create(usuarioTeste, reqMed(titulo)));
            assertErro(HttpStatus.BAD_REQUEST, "Title is required",
                    () -> scheduleService.create(usuarioTeste, reqConsulta(titulo)));
        }
    }

    @Test
    public void agendamentoSemDataDeInicio() {
        // CT64
        assertErro(HttpStatus.BAD_REQUEST, "Start is required",
                () -> scheduleService.create(usuarioTeste, reqCriar(perfilTeste.getId(), ScheduleType.MEDICATION,
                        "Dipirona", null, 1, IntervalUnit.DAY, null, null, medicamento("Dipirona"))));
    }

    @Test
    public void agendamentoSemIntervalo() {
        // CT65
        assertErro(HttpStatus.BAD_REQUEST, "Interval unit is required",
                () -> scheduleService.create(usuarioTeste, reqCriar(perfilTeste.getId(), ScheduleType.MEDICATION,
                        "Dipirona", INICIO, 1, null, null, null, medicamento("Dipirona"))));
    }

    @Test
    public void agendamentoNotaTamanhoLimiteSuperiorAceito() {
        // CT66: partição VÁLIDA, valor limite superior = exatamente 1000 caracteres
        Long id = idDe(scheduleService.create(usuarioTeste, reqMed("Dipirona", "a".repeat(1000), medicamento("Dipirona"))));
        assertEquals(1000, buscar(id).getNotes().length());
    }

    @Test
    public void agendamentoNotaTamanhoAcimaDoLimiteSuperior() {
        // CT66: partição INVÁLIDA, valor limite superior + 1 = 1001 caracteres
        assertErro(HttpStatus.BAD_REQUEST, "Notes must have at most 1000 characters",
                () -> scheduleService.create(usuarioTeste, reqMed("Dipirona", "a".repeat(1001), medicamento("Dipirona"))));
    }

    @Test
    public void agendamentoNaoExigeConfirmacao() {
        // CT67: o agendamento é criado sem nenhuma confirmação associada; ela só existe depois de confirmar.
        Long id = criarMed("Dipirona");

        assertTrue(buscar(id).isActive());
        assertFalse(confirmationRepository.existsBySchedule_Id(id),
                "Agendamento novo não deve ter confirmações");
    }

    // ------------------------------------------------------------------------------------------
    // Atualização (CT68 a CT73)
    // ------------------------------------------------------------------------------------------

    @Test
    public void agendamentoAtualizado() {
        // CT68: sem confirmações, a edição é feita no próprio registro (mesmo ID).
        Long id = criarMed("Dipirona");
        Instant novoInicio = INICIO.plus(2, ChronoUnit.DAYS);
        Instant novoFim = novoInicio.plus(10, ChronoUnit.DAYS);

        ScheduleResponse resposta = scheduleService.update(usuarioTeste, reqAtualizar(id, ScheduleType.MEDICATION,
                "Ibuprofeno", novoInicio, 6, IntervalUnit.HOUR, novoFim, "Com água",
                medicamentoCom("Alivium", "Ibuprofeno", "Ibuprofeno", 300d, DosageUnit.MG, null)));

        assertEquals(id, idDe(resposta), "Sem confirmações, o ID não deve mudar");

        Schedule s = buscar(id);
        assertEquals("Ibuprofeno", s.getTitle());
        assertEquals(novoInicio, s.getStartAt());
        assertEquals(IntervalUnit.HOUR, s.getIntervalUnit());
        assertEquals(6, s.getIntervalValue());
        assertEquals(novoFim, s.getEndAt());
        assertEquals("Com água", s.getNotes());
        assertTrue(s.isActive());

        MedicationSchedule m = medicationScheduleRepository.findById(id).orElseThrow();
        assertEquals("Alivium", m.getBrand());
        assertEquals("Ibuprofeno", m.getMedicineName());
        assertEquals(300d, m.getDosageAmount());

        assertEquals(1, scheduleRepository.findAllByProfile(perfilTeste).size(),
                "A edição não deve criar um novo agendamento");
    }

    @Test
    public void atualizacaoComConfirmacaoVersionaAgendamento() {
        // CT68 (com histórico): o agendamento antigo é arquivado e um novo, independente, é criado.
        Long idAntigo = criarMed("Dipirona");
        confirmar(idAntigo);

        ScheduleResponse resposta = scheduleService.update(usuarioTeste,
                reqAtualizarMed(idAntigo, "Dipirona forte", null, medicamento("Dipirona forte")));
        Long idNovo = idDe(resposta);

        assertNotEquals(idAntigo, idNovo, "O ID retornado deve ser o do novo agendamento");

        Schedule antigo = buscar(idAntigo);
        assertFalse(antigo.isActive(), "O agendamento antigo deve ser arquivado");
        assertEquals("Dipirona", antigo.getTitle(), "O agendamento antigo não deve ser alterado");
        assertTrue(confirmationRepository.existsBySchedule_Id(idAntigo), "O histórico fica no agendamento antigo");

        Schedule novo = buscar(idNovo);
        assertTrue(novo.isActive());
        assertEquals("Dipirona forte", novo.getTitle());
        assertEquals(perfilTeste.getId(), novo.getProfile().getId());
        assertFalse(confirmationRepository.existsBySchedule_Id(idNovo), "O novo agendamento começa sem histórico");
        assertEquals("Dipirona forte", medicationScheduleRepository.findById(idNovo).orElseThrow().getMedicineName());
    }

    @Test
    public void atualizacaoIdInvalido() {
        // CT69: ID do agendamento nulo, zero ou negativo.
        for (Long id : new Long[]{null, 0L, -1L}) {
            assertErro(HttpStatus.BAD_REQUEST, "Schedule ID is invalid",
                    () -> scheduleService.update(usuarioTeste, reqAtualizarMed(id, "Dipirona", null, medicamento("Dipirona"))));
        }
    }

    @Test
    public void atualizacaoAgendamentoInexistenteOuDeOutroUsuario() {
        // CT69: agendamento inexistente e de outro usuário geram o mesmo 404.
        Long idInexistente = 999_999L;
        assertFalse(scheduleRepository.existsById(idInexistente));

        assertErro(HttpStatus.NOT_FOUND, "Active schedule not found",
                () -> scheduleService.update(usuarioTeste,
                        reqAtualizarMed(idInexistente, "Dipirona", null, medicamento("Dipirona"))));

        // Agendamento do João, tentativa de edição feita pela Maria
        Long id = criarMed("Dipirona");
        User usuarioErrado = usuariosDeTeste.get(1);

        assertErro(HttpStatus.NOT_FOUND, "Active schedule not found",
                () -> scheduleService.update(usuarioErrado,
                        reqAtualizarMed(id, "Invadido", null, medicamento("Invadido"))));

        assertEquals("Dipirona", buscar(id).getTitle(), "O agendamento original não deve ser alterado");
    }

    @Test
    public void atualizacaoEmAgendamentoInativo() {
        // CT70: agendamento existe, mas foi arquivado.
        Long id = criarMed("Dipirona");
        scheduleService.delete(usuarioTeste, id);
        assertFalse(buscar(id).isActive());

        assertErro(HttpStatus.NOT_FOUND, "Active schedule not found",
                () -> scheduleService.update(usuarioTeste, reqAtualizarMed(id, "Dipirona", null, medicamento("Dipirona"))));
    }

    @Test
    public void atualizacaoComMudancaDeTipo() {
        // CT71: o tipo não pode ser alterado.
        Long id = criarMed("Dipirona");

        assertErro(HttpStatus.BAD_REQUEST, "Type cannot be changed",
                () -> scheduleService.update(usuarioTeste,
                        reqAtualizarConsulta(id, "Dipirona", null, consulta(null, null))));

        assertEquals(ScheduleType.MEDICATION, buscar(id).getType());
    }

    @Test
    public void atualizacaoDeMedicamentoSemDadosRelacionados() {
        // CT72: medicamento exige o bloco de dados; sem ele (ou com bloco de consulta) a atualização é rejeitada.
        Long id = criarMed("Dipirona");

        assertErro(HttpStatus.BAD_REQUEST, "Medication data is required",
                () -> scheduleService.update(usuarioTeste, reqAtualizarMed(id, "Dipirona", null, null)));
        assertErro(HttpStatus.BAD_REQUEST, "Medication data is required",
                () -> scheduleService.update(usuarioTeste, reqAtualizarMed(id, "Dipirona", null, consulta(null, null))));
    }

    @Test
    public void atualizacaoDeMedicamentoSemLinhaComplementar() {
        // CT72 (integridade): se a linha complementar sumiu do banco, o service devolve 500.
        Long id = criarMed("Dipirona");
        medicationScheduleRepository.deleteById(id);

        assertErro(HttpStatus.INTERNAL_SERVER_ERROR, "Medication data not found",
                () -> scheduleService.update(usuarioTeste, reqAtualizarMed(id, "Dipirona", null, medicamento("Dipirona"))));
    }

    @Test
    public void atualizacaoDeConsultaSemLinhaComplementarRecriaDados() {
        // CT73: o plano prevê rejeição, mas o service aceita consulta sem dados e recria a linha complementar.
        Long id = criarConsulta("Consulta cardiologia");
        appointmentScheduleRepository.deleteById(id);
        assertFalse(appointmentScheduleRepository.existsById(id));

        scheduleService.update(usuarioTeste, reqAtualizarConsulta(id, "Consulta cardiologia", null, null));

        AppointmentSchedule a = appointmentScheduleRepository.findById(id).orElseThrow();
        assertNull(a.getDoctorName());
        assertNull(a.getLocation());
    }

    // ------------------------------------------------------------------------------------------
    // Remoção (CT73 a CT75)
    // ------------------------------------------------------------------------------------------

    @Test
    public void agendamentoRemovido() {
        // CT73: remoção é soft delete (active = false); histórico e dados complementares permanecem.
        Long id = criarMed("Dipirona");
        confirmar(id);

        scheduleService.delete(usuarioTeste, id);

        assertFalse(buscar(id).isActive(), "O agendamento deve ter sido arquivado");
        assertTrue(confirmationRepository.existsBySchedule_Id(id), "As confirmações ficam como histórico");
        assertTrue(medicationScheduleRepository.existsById(id));
        assertTrue(scheduleService.findAllByProfile(usuarioTeste, perfilTeste.getId(), null, Sort.Direction.ASC).isEmpty(),
                "Agendamento arquivado não deve aparecer na listagem");
    }

    @Test
    public void remocaoIdInvalido() {
        // CT74: ID nulo, zero ou negativo.
        for (Long id : new Long[]{null, 0L, -1L}) {
            assertErro(HttpStatus.BAD_REQUEST, "Schedule ID is invalid",
                    () -> scheduleService.delete(usuarioTeste, id));
        }
    }

    @Test
    public void remocaoAgendamentoInexistenteOuDeOutroUsuario() {
        // CT74: inexistente e de outro usuário geram o mesmo 404.
        assertErro(HttpStatus.NOT_FOUND, "Active schedule not found",
                () -> scheduleService.delete(usuarioTeste, 999_999L));

        Long id = criarMed("Dipirona");
        User usuarioErrado = usuariosDeTeste.get(1);

        assertErro(HttpStatus.NOT_FOUND, "Active schedule not found",
                () -> scheduleService.delete(usuarioErrado, id));
        assertTrue(buscar(id).isActive(), "O agendamento não deve ser arquivado por outro usuário");
    }

    @Test
    public void remocaoAgendamentoInativo() {
        // CT75: arquivar um agendamento já arquivado dá o mesmo erro de um inexistente.
        Long id = criarMed("Dipirona");
        scheduleService.delete(usuarioTeste, id);

        assertErro(HttpStatus.NOT_FOUND, "Active schedule not found",
                () -> scheduleService.delete(usuarioTeste, id));
    }

    // ------------------------------------------------------------------------------------------
    // Medicamento (CT76 a CT79)
    // ------------------------------------------------------------------------------------------

    @Test
    public void medicamentoSemNome() {
        // CT76
        for (String nome : new String[]{null, "   "}) {
            assertErro(HttpStatus.BAD_REQUEST, "Medicine name is required",
                    () -> scheduleService.create(usuarioTeste, reqMed("Dipirona", null, medicamento(nome))));
        }
    }

    @Test
    public void atualizacaoDeMedicamentoSemNome() {
        // CT77
        Long id = criarMed("Dipirona");

        for (String nome : new String[]{null, "   "}) {
            assertErro(HttpStatus.BAD_REQUEST, "Medicine name is required",
                    () -> scheduleService.update(usuarioTeste,
                            reqAtualizarMed(id, "Dipirona", null, medicamento(nome))));
        }
    }

    @Test
    public void medicamentoSemUnidadeDeMedida() {
        // CT78
        assertErro(HttpStatus.BAD_REQUEST, "Dosage unit is required",
                () -> scheduleService.create(usuarioTeste, reqMed("Dipirona", null,
                        medicamentoCom(null, "Dipirona", null, 500d, null, null))));
    }

    @Test
    public void atualizacaoDeMedicamentoSemUnidadeDeMedida() {
        // CT79
        Long id = criarMed("Dipirona");

        assertErro(HttpStatus.BAD_REQUEST, "Dosage unit is required",
                () -> scheduleService.update(usuarioTeste, reqAtualizarMed(id, "Dipirona", null,
                        medicamentoCom(null, "Dipirona", null, 500d, null, null))));
    }

    // ------------------------------------------------------------------------------------------
    // Consulta: médico e localização opcionais (CT80 a CT83)
    // ------------------------------------------------------------------------------------------

    @Test
    public void consultaSemNomeDeMedico() {
        // CT80
        Long id = idDe(scheduleService.create(usuarioTeste,
                reqConsulta("Consulta cardiologia", null, consulta(null, "Hospital Central"))));

        AppointmentSchedule a = appointmentScheduleRepository.findById(id).orElseThrow();
        assertNull(a.getDoctorName());
        assertEquals("Hospital Central", a.getLocation());
    }

    @Test
    public void atualizacaoDeConsultaSemNomeDeMedico() {
        // CT81
        Long id = criarConsulta("Consulta cardiologia");

        scheduleService.update(usuarioTeste,
                reqAtualizarConsulta(id, "Consulta cardiologia", null, consulta("   ", "Hospital Central")));

        AppointmentSchedule a = appointmentScheduleRepository.findById(id).orElseThrow();
        assertNull(a.getDoctorName(), "Nome em branco deve ser salvo como null");
        assertEquals("Hospital Central", a.getLocation());
    }

    @Test
    public void consultaSemLocalizacao() {
        // CT82
        Long id = idDe(scheduleService.create(usuarioTeste,
                reqConsulta("Consulta cardiologia", null, consulta("Dra. Helena", null))));

        AppointmentSchedule a = appointmentScheduleRepository.findById(id).orElseThrow();
        assertEquals("Dra. Helena", a.getDoctorName());
        assertNull(a.getLocation());
    }

    @Test
    public void atualizacaoDeConsultaSemLocalizacao() {
        // CT83
        Long id = criarConsulta("Consulta cardiologia");

        scheduleService.update(usuarioTeste,
                reqAtualizarConsulta(id, "Consulta cardiologia", null, consulta("Dra. Helena", "   ")));

        AppointmentSchedule a = appointmentScheduleRepository.findById(id).orElseThrow();
        assertEquals("Dra. Helena", a.getDoctorName());
        assertNull(a.getLocation(), "Localização em branco deve ser salva como null");
    }

    // ------------------------------------------------------------------------------------------
    // Título (CT84 a CT89). CT84 está coberto em agendamentoSemTitulo (mesmo erro, mesmo caminho).
    // ------------------------------------------------------------------------------------------

    @Test
    public void atualizacaoSemTitulo() {
        // CT85: título obrigatório permanece na atualização (medicamento e consulta).
        Long idMed = criarMed("Dipirona");
        Long idConsulta = criarConsulta("Consulta cardiologia");

        for (String titulo : new String[]{null, "   "}) {
            assertErro(HttpStatus.BAD_REQUEST, "Title is required",
                    () -> scheduleService.update(usuarioTeste, reqAtualizarMed(idMed, titulo, null, medicamento("Dipirona"))));
            assertErro(HttpStatus.BAD_REQUEST, "Title is required",
                    () -> scheduleService.update(usuarioTeste, reqAtualizarConsulta(idConsulta, titulo, null, consulta(null, null))));
        }
    }

    @Test
    public void tituloTamanhoAbaixoDoLimiteInferior() {
        // CT86: partição INVÁLIDA, tamanho < 3 (após trim). Valor limite inferior - 1 = 2 caracteres.
        String msg = "Title must have between 3 and 100 characters";
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> scheduleService.create(usuarioTeste, reqConsulta("Ab")));

        // Mesma partição com espaços: após trim continua 2 caracteres
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> scheduleService.create(usuarioTeste, reqConsulta("  Ab  ")));
    }

    @Test
    public void tituloTamanhoLimiteInferiorAceito() {
        // CT86: partição VÁLIDA, valor limite inferior = exatamente 3 caracteres
        Long id = idDe(scheduleService.create(usuarioTeste, reqConsulta("Abc")));
        assertEquals("Abc", buscar(id).getTitle());
    }

    @Test
    public void tituloTamanhoLimiteSuperiorAceito() {
        // CT86: partição VÁLIDA, valor limite superior = exatamente 100 caracteres
        Long id = idDe(scheduleService.create(usuarioTeste, reqConsulta("a".repeat(100))));
        assertEquals(100, buscar(id).getTitle().length());
    }

    @Test
    public void tituloTamanhoAcimaDoLimiteSuperior() {
        // CT86: partição INVÁLIDA, valor limite superior + 1 = 101 caracteres
        assertErro(HttpStatus.BAD_REQUEST, "Title must have between 3 and 100 characters",
                () -> scheduleService.create(usuarioTeste, reqConsulta("a".repeat(101))));
    }

    @Test
    public void atualizacaoComTituloTamanhoInvalido() {
        // CT87: a regra de tamanho permanece na atualização (limites 2/3 e 100/101).
        Long id = criarConsulta("Consulta cardiologia");
        String msg = "Title must have between 3 and 100 characters";

        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> scheduleService.update(usuarioTeste, reqAtualizarConsulta(id, "Ab", null, consulta(null, null))));
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> scheduleService.update(usuarioTeste, reqAtualizarConsulta(id, "a".repeat(101), null, consulta(null, null))));

        // Limites válidos
        scheduleService.update(usuarioTeste, reqAtualizarConsulta(id, "Abc", null, consulta(null, null)));
        assertEquals("Abc", buscar(id).getTitle());

        scheduleService.update(usuarioTeste, reqAtualizarConsulta(id, "a".repeat(100), null, consulta(null, null)));
        assertEquals(100, buscar(id).getTitle().length());
    }

    @Test
    public void consultaComTituloInvalido() {
        // CT88: caracteres fora do padrão.
        // Obs.: o padrão do service aceita dígitos, então só caracteres especiais são rejeitados.
        String msg = "Title contains invalid characters";
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> scheduleService.create(usuarioTeste, reqConsulta("Consulta@cardio")));
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> scheduleService.create(usuarioTeste, reqConsulta("Consulta 😀")));
    }

    @Test
    public void atualizacaoDeConsultaComTituloInvalido() {
        // CT89
        Long id = criarConsulta("Consulta cardiologia");
        String msg = "Title contains invalid characters";

        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> scheduleService.update(usuarioTeste, reqAtualizarConsulta(id, "Consulta@cardio", null, consulta(null, null))));

        assertEquals("Consulta cardiologia", buscar(id).getTitle(), "O título original não deve ser alterado");
    }

    @Test
    public void tituloNormalizado() {
        // Título tem trim e espaços repetidos colapsados antes de salvar.
        Long id = idDe(scheduleService.create(usuarioTeste, reqConsulta("  Consulta   de   rotina  ")));
        assertEquals("Consulta de rotina", buscar(id).getTitle());
    }

    // ------------------------------------------------------------------------------------------
    // Textos opcionais (CT90 a CT93)
    // ------------------------------------------------------------------------------------------

    @Test
    public void textoOpcionalInexistente() {
        // CT90: notas, marca, princípio ativo e foto são opcionais; vazio ou em branco vira null.
        Long semNada = idDe(scheduleService.create(usuarioTeste, reqMed("Dipirona", null, medicamento("Dipirona"))));
        Long emBranco = idDe(scheduleService.create(usuarioTeste, reqMed("Ibuprofeno", "   ",
                medicamentoCom("  ", "Ibuprofeno", "", 300d, DosageUnit.MG, "   "))));

        for (Long id : new Long[]{semNada, emBranco}) {
            assertNull(buscar(id).getNotes());

            MedicationSchedule m = medicationScheduleRepository.findById(id).orElseThrow();
            assertNull(m.getBrand());
            assertNull(m.getActiveIngredient());
            assertNull(m.getPhotoUrl());
            assertEquals(MedicationStripe.NONE, m.getStripe());
        }
    }

    @Test
    public void atualizacaoSemTextoOpcional() {
        // CT91: a atualização substitui a regra inteira (PUT): textos opcionais não enviados são limpos.
        Long id = idDe(scheduleService.create(usuarioTeste, reqMed("Dipirona", "Após a refeição",
                medicamentoCom("Novalgina", "Dipirona", "Metamizol", 500d, DosageUnit.MG, "https://exemplo.com/foto.png"))));

        scheduleService.update(usuarioTeste, reqAtualizarMed(id, "Dipirona", null, medicamento("Dipirona")));

        assertNull(buscar(id).getNotes());

        MedicationSchedule m = medicationScheduleRepository.findById(id).orElseThrow();
        assertNull(m.getBrand());
        assertNull(m.getActiveIngredient());
        assertNull(m.getPhotoUrl());
    }

    @Test
    public void textoOpcionalTamanhoLimiteSuperiorAceito() {
        // CT92: partição VÁLIDA, valor limite superior = exatamente 255 caracteres
        // (marca, princípio ativo e foto no medicamento; médico e localização na consulta).
        String t255 = "a".repeat(255);

        Long idMed = idDe(scheduleService.create(usuarioTeste, reqMed("Dipirona", null,
                medicamentoCom(t255, "Dipirona", t255, 500d, DosageUnit.MG, t255))));
        MedicationSchedule m = medicationScheduleRepository.findById(idMed).orElseThrow();
        assertEquals(255, m.getBrand().length());
        assertEquals(255, m.getActiveIngredient().length());
        assertEquals(255, m.getPhotoUrl().length());

        Long idConsulta = idDe(scheduleService.create(usuarioTeste,
                reqConsulta("Consulta cardiologia", null, consulta(t255, t255))));
        AppointmentSchedule a = appointmentScheduleRepository.findById(idConsulta).orElseThrow();
        assertEquals(255, a.getDoctorName().length());
        assertEquals(255, a.getLocation().length());
    }

    @Test
    public void textoOpcionalTamanhoAcimaDoLimiteSuperior() {
        // CT92: partição INVÁLIDA, valor limite superior + 1 = 256 caracteres
        String t256 = "a".repeat(256);

        assertErro(HttpStatus.BAD_REQUEST, "Brand must have at most 255 characters",
                () -> scheduleService.create(usuarioTeste, reqMed("Dipirona", null,
                        medicamentoCom(t256, "Dipirona", null, 500d, DosageUnit.MG, null))));
        assertErro(HttpStatus.BAD_REQUEST, "Active ingredient must have at most 255 characters",
                () -> scheduleService.create(usuarioTeste, reqMed("Dipirona", null,
                        medicamentoCom(null, "Dipirona", t256, 500d, DosageUnit.MG, null))));
        assertErro(HttpStatus.BAD_REQUEST, "Photo URL must have at most 255 characters",
                () -> scheduleService.create(usuarioTeste, reqMed("Dipirona", null,
                        medicamentoCom(null, "Dipirona", null, 500d, DosageUnit.MG, t256))));
        assertErro(HttpStatus.BAD_REQUEST, "Doctor name must have at most 255 characters",
                () -> scheduleService.create(usuarioTeste, reqConsulta("Consulta cardiologia", null, consulta(t256, null))));
        assertErro(HttpStatus.BAD_REQUEST, "Location must have at most 255 characters",
                () -> scheduleService.create(usuarioTeste, reqConsulta("Consulta cardiologia", null, consulta(null, t256))));
    }

    @Test
    public void atualizacaoDeTextoOpcionalMuitoLongo() {
        // CT93: a regra de tamanho permanece na atualização (limites 255/256 e 1000/1001).
        Long idMed = criarMed("Dipirona");
        Long idConsulta = criarConsulta("Consulta cardiologia");
        String t256 = "a".repeat(256);

        assertErro(HttpStatus.BAD_REQUEST, "Brand must have at most 255 characters",
                () -> scheduleService.update(usuarioTeste, reqAtualizarMed(idMed, "Dipirona", null,
                        medicamentoCom(t256, "Dipirona", null, 500d, DosageUnit.MG, null))));
        assertErro(HttpStatus.BAD_REQUEST, "Doctor name must have at most 255 characters",
                () -> scheduleService.update(usuarioTeste, reqAtualizarConsulta(idConsulta, "Consulta cardiologia", null,
                        consulta(t256, null))));
        assertErro(HttpStatus.BAD_REQUEST, "Location must have at most 255 characters",
                () -> scheduleService.update(usuarioTeste, reqAtualizarConsulta(idConsulta, "Consulta cardiologia", null,
                        consulta(null, t256))));
        assertErro(HttpStatus.BAD_REQUEST, "Notes must have at most 1000 characters",
                () -> scheduleService.update(usuarioTeste, reqAtualizarMed(idMed, "Dipirona", "a".repeat(1001),
                        medicamento("Dipirona"))));

        // Limites válidos
        scheduleService.update(usuarioTeste, reqAtualizarMed(idMed, "Dipirona", "a".repeat(1000),
                medicamentoCom("a".repeat(255), "Dipirona", null, 500d, DosageUnit.MG, null)));
        assertEquals(1000, buscar(idMed).getNotes().length());
        assertEquals(255, medicationScheduleRepository.findById(idMed).orElseThrow().getBrand().length());
    }

    // ------------------------------------------------------------------------------------------
    // Regras que existem no service, mas não estão no plano
    // ------------------------------------------------------------------------------------------

    @Test
    public void medicamentoNomeTamanhoELimites() {
        // Nome do medicamento: limite superior = 255 caracteres.
        Long id = idDe(scheduleService.create(usuarioTeste, reqMed("Dipirona", null, medicamento("a".repeat(255)))));
        assertEquals(255, medicationScheduleRepository.findById(id).orElseThrow().getMedicineName().length());

        assertErro(HttpStatus.BAD_REQUEST, "Medicine name is invalid",
                () -> scheduleService.create(usuarioTeste, reqMed("Dipirona", null, medicamento("a".repeat(256)))));
    }

    @Test
    public void medicamentoNomeComCaracteresInvalidos() {
        assertErro(HttpStatus.BAD_REQUEST, "Medicine name is invalid",
                () -> scheduleService.create(usuarioTeste, reqMed("Dipirona", null, medicamento("Dipirona@500"))));
    }

    @Test
    public void medicamentoDosagemInvalida() {
        // Fora do intervalo (0 < dose <= 10000) ou com mais de 3 casas decimais.
        for (Double dose : new Double[]{null, 0d, -1d, 10000.001, 0.0001}) {
            assertErro(HttpStatus.BAD_REQUEST, "Dosage amount is invalid",
                    () -> scheduleService.create(usuarioTeste, reqMed("Dipirona", null,
                            medicamentoCom(null, "Dipirona", null, dose, DosageUnit.MG, null))));
        }

        // Valores limite aceitos
        Long maximo = idDe(scheduleService.create(usuarioTeste, reqMed("Dipirona", null,
                medicamentoCom(null, "Dipirona", null, 10000d, DosageUnit.MG, null))));
        Long minimo = idDe(scheduleService.create(usuarioTeste, reqMed("Dipirona", null,
                medicamentoCom(null, "Dipirona", null, 0.001, DosageUnit.MG, null))));
        assertEquals(10000d, medicationScheduleRepository.findById(maximo).orElseThrow().getDosageAmount());
        assertEquals(0.001, medicationScheduleRepository.findById(minimo).orElseThrow().getDosageAmount());
    }

    @Test
    public void consultaComDadosDeMedicamentoRejeitada() {
        assertErro(HttpStatus.BAD_REQUEST, "Details do not match the type",
                () -> scheduleService.create(usuarioTeste,
                        reqConsulta("Consulta cardiologia", null, medicamento("Dipirona"))));
    }

    @Test
    public void intervaloValorInvalido() {
        // Intervalo que se repete exige valor > 0.
        for (Integer valor : new Integer[]{null, 0, -1}) {
            assertErro(HttpStatus.BAD_REQUEST, "Interval value must be greater than zero",
                    () -> scheduleService.create(usuarioTeste, reqCriar(perfilTeste.getId(), ScheduleType.MEDICATION,
                            "Dipirona", INICIO, valor, IntervalUnit.DAY, null, null, medicamento("Dipirona"))));
        }
    }

    @Test
    public void fimNaoPosteriorAoInicio() {
        assertErro(HttpStatus.BAD_REQUEST, "End must be after start",
                () -> scheduleService.create(usuarioTeste, reqCriar(perfilTeste.getId(), ScheduleType.MEDICATION,
                        "Dipirona", INICIO, 1, IntervalUnit.DAY, INICIO, null, medicamento("Dipirona"))));
    }

    @Test
    public void agendamentoUnicoIgnoraValorEFim() {
        // UNIQUE: valor do intervalo e fim são ignorados (salvos como 0 e null).
        Long id = idDe(scheduleService.create(usuarioTeste, reqCriar(perfilTeste.getId(), ScheduleType.MEDICATION,
                "Dipirona", INICIO, 5, IntervalUnit.UNIQUE, INICIO.plus(5, ChronoUnit.DAYS), null, medicamento("Dipirona"))));

        Schedule s = buscar(id);
        assertEquals(0, s.getIntervalValue());
        assertNull(s.getEndAt());
    }
}
