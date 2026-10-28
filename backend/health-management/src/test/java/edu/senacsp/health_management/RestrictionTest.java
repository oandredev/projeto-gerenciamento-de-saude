package edu.senacsp.health_management;

import edu.senacsp.health_management.dto.request.profile.CreateProfileRequest;
import edu.senacsp.health_management.dto.request.restriction.CreateRestrictionRequest;
import edu.senacsp.health_management.dto.request.restriction.UpdateRestrictionRequest;
import edu.senacsp.health_management.dto.request.user.SignupRequest;
import edu.senacsp.health_management.dto.response.profile.ProfileResponse;
import edu.senacsp.health_management.dto.response.restriction.RestrictionResponse;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.Restriction;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.entity.enums.RestrictionSeverity;
import edu.senacsp.health_management.entity.enums.RestrictionType;
import edu.senacsp.health_management.repository.ProfileRepository;
import edu.senacsp.health_management.repository.RestrictionRepository;
import edu.senacsp.health_management.repository.UserRepository;
import edu.senacsp.health_management.service.ProfileService;
import edu.senacsp.health_management.service.RestrictionService;
import edu.senacsp.health_management.service.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RestrictionTest {

    @Autowired
    private RestrictionService restrictionService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;

    @Autowired
    private RestrictionRepository restrictionRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;

    // Dois valores distintos de cada enum, para os testes que alteram tipo/gravidade.
    private static final RestrictionType TIPO_A = RestrictionType.FOOD;
    private static final RestrictionType TIPO_B = RestrictionType.MEDICATION;
    private static final RestrictionSeverity GRAV_A = RestrictionSeverity.MILD;
    private static final RestrictionSeverity GRAV_B = RestrictionSeverity.SEVERY;

    private Profile perfilTeste;
    private User usuarioTeste;
    private List<Profile> perfisDeTeste;
    private List<User> usuariosDeTeste;

    @BeforeEach
    public void setUp() {
        // Dois usuários teste com nome, email e senha válidas
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
        // Ordem por causa das FKs: restrições -> perfis -> usuários.
        // Só apaga o que ainda existe.
        for (Profile p : perfisDeTeste) {
            restrictionRepository.deleteAll(restrictionRepository.findAllByProfileId(p.getId()));
        }

        for (Profile p : perfisDeTeste) {
            if (profileRepository.existsById(p.getId())) {
                profileRepository.deleteById(p.getId());
            }
        }

        for (User u : usuariosDeTeste) {
            if (userRepository.existsById(u.getId())) {
                userRepository.deleteById(u.getId());
            }
        }
    }

    // ------------------------------------------------------------------------------------------
    // Métodos auxiliares
    // ------------------------------------------------------------------------------------------

    /** Cria um perfil via service e já o registra na lista de limpeza. */
    private Profile criarPerfil(User usuario, String nome) {
        ProfileResponse resposta = profileService.create(usuario, new CreateProfileRequest(nome, 1L));
        Profile perfil = profileRepository.findByIdAndUser(resposta.id(), usuario).orElseThrow();
        perfisDeTeste.add(perfil);
        return perfil;
    }

    /** Cria uma restrição válida via service e devolve a entidade persistida. */
    private Restriction criarRestricao(Profile perfil, String titulo, String nota) {
        RestrictionResponse resposta = restrictionService.create(usuarioTeste,
                new CreateRestrictionRequest(perfil.getId(), RestrictionType.OTHER, RestrictionSeverity.SEVERY, titulo, nota));
        return restrictionRepository.findById(resposta.id()).orElseThrow();
    }

    /** Request de criação válido, variando só o título (para os testes de validação). */
    private CreateRestrictionRequest reqCriar(String titulo) {
        return new CreateRestrictionRequest(perfilTeste.getId(), RestrictionType.OTHER, RestrictionSeverity.SEVERY, titulo, null);
    }

    private UpdateRestrictionRequest reqAtualizar(Long perfilId, RestrictionType tipo, RestrictionSeverity grav,
                                                  String titulo, String nota, Boolean ativo) {
        return new UpdateRestrictionRequest(perfilId, tipo, grav, titulo, nota, ativo);
    }

    private void assertErro(HttpStatus status, String mensagem, Executable acao) {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, acao);
        assertEquals(status, ex.getStatusCode());
        assertEquals(mensagem, ex.getReason());
    }

    // ------------------------------------------------------------------------------------------
    // Criação (CT41 a CT45)
    // ------------------------------------------------------------------------------------------

    @Test
    public void novaListaDeRestricoes() {
        // CT41: criação de várias restrições em um perfil com todas as características.
        RestrictionResponse r1 = restrictionService.create(usuarioTeste, new CreateRestrictionRequest(
                perfilTeste.getId(), RestrictionType.FOOD, RestrictionSeverity.SEVERY,
                "Alergia a amendoim", "Reação grave"));
        RestrictionResponse r2 = restrictionService.create(usuarioTeste, new CreateRestrictionRequest(
                perfilTeste.getId(), RestrictionType.SUBSTANCE, RestrictionSeverity.MILD,
                "Alergia a algodão", "Luvas sem látex"));
        RestrictionResponse r3 = restrictionService.create(usuarioTeste, new CreateRestrictionRequest(
                perfilTeste.getId(), RestrictionType.MEDICATION, RestrictionSeverity.MODERATE,
                "Intolerância a dipirona", "Prescrito, 2024/2025"));
        RestrictionResponse r4 = restrictionService.create(usuarioTeste, new CreateRestrictionRequest(
                perfilTeste.getId(), RestrictionType.OTHER, RestrictionSeverity.MILD,
                "Sensibilidade à luz", "Evitar sol forte"));

        assertNotNull(r1.id());
        assertNotNull(r2.id());
        assertNotNull(r3.id());
        assertNotNull(r4.id());

        // Dados salvos correspondem aos enviados (títulos com acento e pontuação são aceitos)
        Restriction e1 = restrictionRepository.findById(r1.id()).orElseThrow();
        assertEquals(RestrictionType.FOOD, e1.getType());
        assertEquals(RestrictionSeverity.SEVERY, e1.getSeverity());
        assertEquals("Alergia a amendoim", e1.getTitle());
        assertEquals("Reação grave", e1.getNote());
        assertTrue(e1.isActive(), "Restrição nova deve nascer ativa");

        Restriction e2 = restrictionRepository.findById(r2.id()).orElseThrow();
        assertEquals(RestrictionType.SUBSTANCE, e2.getType());
        assertEquals(RestrictionSeverity.MILD, e2.getSeverity());
        assertEquals("Alergia a algodão", e2.getTitle());

        Restriction e3 = restrictionRepository.findById(r3.id()).orElseThrow();
        assertEquals(RestrictionType.MEDICATION, e3.getType());
        assertEquals(RestrictionSeverity.MODERATE, e3.getSeverity());

        Restriction e4 = restrictionRepository.findById(r4.id()).orElseThrow();
        assertEquals(RestrictionType.OTHER, e4.getType());

        assertEquals(4, restrictionRepository.findAllByProfileId(perfilTeste.getId()).size(),
                "O perfil deve ter 4 restrições no banco");
    }

    @Test
    public void restricoesEmPerfilValido() {
        // CT42: as restrições ficam realmente relacionadas ao perfil em que foram criadas.
        Profile perfil02 = criarPerfil(usuarioTeste, "Lucas Pereira");

        Restriction a1 = criarRestricao(perfilTeste, "Alergia a dipirona", null);
        Restriction a2 = criarRestricao(perfilTeste, "Celíaco", null);
        Restriction b1 = criarRestricao(perfil02, "Diabetes tipo 2", null);

        assertEquals(perfilTeste.getId(), a1.getProfile().getId());
        assertEquals(perfilTeste.getId(), a2.getProfile().getId());
        assertEquals(perfil02.getId(), b1.getProfile().getId());

        // A listagem de cada perfil traz só as suas restrições
        List<Long> idsPerfil01 = restrictionService.findAllByProfile(usuarioTeste, perfilTeste.getId()).stream()
                .map(RestrictionResponse::id).toList();
        List<Long> idsPerfil02 = restrictionService.findAllByProfile(usuarioTeste, perfil02.getId()).stream()
                .map(RestrictionResponse::id).toList();

        assertEquals(2, idsPerfil01.size());
        assertTrue(idsPerfil01.contains(a1.getId()));
        assertTrue(idsPerfil01.contains(a2.getId()));
        assertFalse(idsPerfil01.contains(b1.getId()), "Restrição de outro perfil não deve aparecer");

        assertEquals(1, idsPerfil02.size());
        assertTrue(idsPerfil02.contains(b1.getId()));
    }

    @Test
    public void restricaoPerfilIdInvalido() {
        // CT43: ID de perfil nulo, zero ou negativo é rejeitado.
        assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                () -> restrictionService.create(usuarioTeste,
                        new CreateRestrictionRequest(null, RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null)));
        assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                () -> restrictionService.create(usuarioTeste,
                        new CreateRestrictionRequest(0L, RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null)));
        assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                () -> restrictionService.create(usuarioTeste,
                        new CreateRestrictionRequest(-1L, RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null)));
    }

    @Test
    public void restricaoPerfilInexistenteOuDeOutroUsuario() {
        // CT43: perfil inexistente e perfil de outro usuário geram o mesmo 404 (não revela IDs).
        Long idInexistente = 999_999L;
        assertFalse(profileRepository.existsById(idInexistente));

        assertErro(HttpStatus.NOT_FOUND, "Profile not found",
                () -> restrictionService.create(usuarioTeste,
                        new CreateRestrictionRequest(idInexistente, RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null)));

        // Perfil da Maria, tentativa feita pelo João
        Profile perfilDaMaria = criarPerfil(usuariosDeTeste.get(1), "Carla Souza");
        assertErro(HttpStatus.NOT_FOUND, "Profile not found",
                () -> restrictionService.create(usuarioTeste,
                        new CreateRestrictionRequest(perfilDaMaria.getId(), RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null)));

        assertTrue(restrictionRepository.findAllByProfileId(perfilDaMaria.getId()).isEmpty(),
                "Nada deve ter sido criado no perfil de outro usuário");
    }

    @Test
    public void restricaoSemTipo() {
        // CT44
        assertErro(HttpStatus.BAD_REQUEST, "Type is required",
                () -> restrictionService.create(usuarioTeste,
                        new CreateRestrictionRequest(perfilTeste.getId(), null, RestrictionSeverity.SEVERY, "Alergia", null)));
    }

    @Test
    public void restricaoSemGravidade() {
        // CT45
        assertErro(HttpStatus.BAD_REQUEST, "Severity is required",
                () -> restrictionService.create(usuarioTeste,
                        new CreateRestrictionRequest(perfilTeste.getId(), RestrictionType.OTHER, null, "Alergia", null)));
    }

    @Test
    public void restricaoAtualizada() {
        // CT46: atualização de todos os campos, arquivamento e reativação.
        Restriction original = criarRestricao(perfilTeste, "Alergia a dipirona", "Reação grave");
        Long id = original.getId();

        restrictionService.update(usuarioTeste, id,
                reqAtualizar(perfilTeste.getId(), RestrictionType.MEDICATION, RestrictionSeverity.SEVERY, "Alergia a dipirona", "Revisado", true));

        Restriction editada = restrictionRepository.findById(id).orElseThrow();
        assertEquals(RestrictionType.MEDICATION, editada.getType());
        assertEquals(RestrictionSeverity.SEVERY, editada.getSeverity());
        assertEquals("Alergia a dipirona", editada.getTitle());
        assertEquals("Revisado", editada.getNote());
        assertTrue(editada.isActive());
        assertEquals(perfilTeste.getId(), editada.getProfile().getId());
        assertEquals(1, restrictionRepository.findAllByProfileId(perfilTeste.getId()).size(),
                "A edição não deve criar uma nova restrição");

        // Arquivamento (soft delete) + nota em branco vira null
        restrictionService.update(usuarioTeste, id,
                reqAtualizar(perfilTeste.getId(), RestrictionType.MEDICATION, RestrictionSeverity.SEVERY, "Alergia a dipirona", "   ", false));
        Restriction arquivada = restrictionRepository.findById(id).orElseThrow();
        assertFalse(arquivada.isActive(), "A restrição deve ter sido arquivada (active = false)");
        assertNull(arquivada.getNote(), "Nota em branco deve ser salva como null");

        // Reativação
        restrictionService.update(usuarioTeste, id,
                reqAtualizar(perfilTeste.getId(), RestrictionType.MEDICATION, RestrictionSeverity.SEVERY, "Alergia a dipirona", null, true));
        assertTrue(restrictionRepository.findById(id).orElseThrow().isActive(),
                "A restrição deve ter sido reativada");
    }

    @Test
    public void atualizacaoSemTipo() {
        // CT47
        Long id = criarRestricao(perfilTeste, "Alergia", null).getId();

        assertErro(HttpStatus.BAD_REQUEST, "Type is required",
                () -> restrictionService.update(usuarioTeste, id,
                        reqAtualizar(perfilTeste.getId(), null, RestrictionSeverity.SEVERY, "Alergia", null, true)));
    }

    @Test
    public void atualizacaoSemGravidade() {
        // CT48
        Long id = criarRestricao(perfilTeste, "Alergia", null).getId();

        assertErro(HttpStatus.BAD_REQUEST, "Severity is required",
                () -> restrictionService.update(usuarioTeste, id,
                        reqAtualizar(perfilTeste.getId(), RestrictionType.OTHER, null, "Alergia", null, true)));
    }

    @Test
    public void atualizacaoSemActive() {
        // Regra exclusiva da atualização: o campo active é obrigatório.
        Long id = criarRestricao(perfilTeste, "Alergia", null).getId();

        assertErro(HttpStatus.BAD_REQUEST, "Active info is required",
                () -> restrictionService.update(usuarioTeste, id,
                        reqAtualizar(perfilTeste.getId(), RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null, null)));
    }

    @Test
    public void atualizacaoSemPerfil() {
        // CT49: validação do perfil permanece na atualização.
        Profile perfil02 = criarPerfil(usuarioTeste, "Lucas Pereira");
        Long id = criarRestricao(perfilTeste, "Alergia", null).getId();

        // ID de perfil inválido
        assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                () -> restrictionService.update(usuarioTeste, id,
                        reqAtualizar(null, RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null, true)));
        assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                () -> restrictionService.update(usuarioTeste, id,
                        reqAtualizar(0L, RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null, true)));

        // Perfil inexistente
        assertErro(HttpStatus.NOT_FOUND, "Restriction not found",
                () -> restrictionService.update(usuarioTeste, id,
                        reqAtualizar(999_999L, RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null, true)));

        // Perfil existente (do mesmo usuário), mas que não é o dono da restrição: não move a restrição
        assertErro(HttpStatus.NOT_FOUND, "Restriction not found",
                () -> restrictionService.update(usuarioTeste, id,
                        reqAtualizar(perfil02.getId(), RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Outro título", null, false)));

        Restriction intacta = restrictionRepository.findById(id).orElseThrow();
        assertEquals(perfilTeste.getId(), intacta.getProfile().getId());
        assertEquals("Alergia", intacta.getTitle());
        assertTrue(intacta.isActive());
    }

    @Test
    public void atualizacaoIdInvalido() {
        // CT50: ID da restrição nulo, zero ou negativo.
        UpdateRestrictionRequest req = reqAtualizar(perfilTeste.getId(), RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null, true);

        assertErro(HttpStatus.BAD_REQUEST, "Restriction ID is invalid",
                () -> restrictionService.update(usuarioTeste, null, req));
        assertErro(HttpStatus.BAD_REQUEST, "Restriction ID is invalid",
                () -> restrictionService.update(usuarioTeste, 0L, req));
    }

    @Test
    public void atualizacaoSemRestricao() {
        // CT50: restrição inexistente e restrição de outro usuário geram o mesmo 404.
        Long idInexistente = 999_999L;
        assertFalse(restrictionRepository.existsById(idInexistente));

        assertErro(HttpStatus.NOT_FOUND, "Restriction not found",
                () -> restrictionService.update(usuarioTeste, idInexistente,
                        reqAtualizar(perfilTeste.getId(), RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", null, true)));

        // Restrição do João, tentativa de edição feita pela Maria
        Long id = criarRestricao(perfilTeste, "Alergia", null).getId();
        User usuarioErrado = usuariosDeTeste.get(1);

        assertErro(HttpStatus.NOT_FOUND, "Restriction not found",
                () -> restrictionService.update(usuarioErrado, id,
                        reqAtualizar(perfilTeste.getId(), RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Invadida", null, false)));

        Restriction intacta = restrictionRepository.findById(id).orElseThrow();
        assertEquals("Alergia", intacta.getTitle());
        assertEquals(RestrictionType.OTHER, intacta.getType());
        assertEquals(RestrictionSeverity.SEVERY, intacta.getSeverity());
        assertTrue(intacta.isActive());
    }

    @Test
    public void restricaoSemTitulo() {
        // CT51
        assertErro(HttpStatus.BAD_REQUEST, "Title is required",
                () -> restrictionService.create(usuarioTeste, reqCriar(null)));
        assertErro(HttpStatus.BAD_REQUEST, "Title is required",
                () -> restrictionService.create(usuarioTeste, reqCriar("   ")));
    }

    @Test
    public void tituloInvalido() {
        // CT52: caracteres fora do padrão, ou iniciando com pontuação.
        String msg = "Title contains invalid characters";
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> restrictionService.create(usuarioTeste, reqCriar("Alergia@dipirona")));
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> restrictionService.create(usuarioTeste, reqCriar("-Alergia")));
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> restrictionService.create(usuarioTeste, reqCriar("Alergia 😀")));
    }

    @Test
    public void tituloTamanhoAbaixoDoLimiteInferior() {
        // Partição INVÁLIDA: tamanho < 3 (após trim)
        // Valor limite inferior: 2 caracteres
        String msg = "Title must have between 3 and 50 characters";
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> restrictionService.create(usuarioTeste, reqCriar("Ab")));

        // Mesma partição com espaços: após trim continua 2 caracteres
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> restrictionService.create(usuarioTeste, reqCriar("  Ab  ")));
    }

    @Test
    public void tituloTamanhoLimiteInferiorAceito() {
        // Partição VÁLIDA: valor limite inferior = exatamente 3 caracteres
        Restriction minimo = criarRestricao(perfilTeste, "Abc", null);
        assertEquals("Abc", minimo.getTitle());
    }

    @Test
    public void tituloTamanhoLimiteSuperiorAceito() {
        // Partição VÁLIDA: valor limite superior = exatamente 50 caracteres
        Restriction maximo = criarRestricao(perfilTeste, "a".repeat(50), null);
        assertEquals(50, maximo.getTitle().length());
    }

    @Test
    public void tituloTamanhoAcimaDoLimiteSuperior() {
        // Partição INVÁLIDA: tamanho > 50
        // Valor limite superior: 51 caracteres
        assertErro(HttpStatus.BAD_REQUEST, "Title must have between 3 and 50 characters",
                () -> restrictionService.create(usuarioTeste, reqCriar("a".repeat(51))));
    }

    @Test
    public void tituloNormalizado() {
        // Título tem trim e espaços repetidos colapsados antes de salvar.
        Restriction r = criarRestricao(perfilTeste, "  Sou   uma   pessoa   celíaca  ", null);
        assertEquals("Sou uma pessoa celíaca", r.getTitle());
    }

    // ------------------------------------------------------------------------------------------
    // Anotação (CT55 e CT56)
    // ------------------------------------------------------------------------------------------

    @Test
    public void anotacaoInexistente() {
        // CT55: anotação é opcional. Salva como null no banco; a resposta devolve "".
        RestrictionResponse semNota = restrictionService.create(usuarioTeste, reqCriar("Alergia a dipirona"));
        assertEquals("", semNota.note());
        assertNull(restrictionRepository.findById(semNota.id()).orElseThrow().getNote());

        // Nota só com espaços também vira null
        Restriction notaEmBranco = criarRestricao(perfilTeste, "Alergia a camarão", "   ");
        assertNull(notaEmBranco.getNote());
    }

    @Test
    public void anotacaoTamanhoLimiteSuperiorAceito() {
        // Partição VÁLIDA: 0 ≤ tamanho ≤ 1000
        // Valor limite superior: exatamente 1000 caracteres
        Restriction maximo = criarRestricao(perfilTeste, "Alergia", "a".repeat(1000));
        assertEquals(1000, maximo.getNote().length());
    }

    @Test
    public void anotacaoTamanhoAcimaDoLimiteSuperior() {
        // Partição INVÁLIDA: tamanho > 1000
        // Valor limite superior: 1001 caracteres
        assertErro(HttpStatus.BAD_REQUEST, "Note must have at most 1000 characters",
                () -> restrictionService.create(usuarioTeste, new CreateRestrictionRequest(
                        perfilTeste.getId(), RestrictionType.OTHER, RestrictionSeverity.SEVERY, "Alergia", "a".repeat(1001))));
    }

    @Test
    public void anotacaoVaziaAceita() {
        // Partição VÁLIDA (limite inferior): anotação vazia (0 caracteres)
        Restriction semNota = criarRestricao(perfilTeste, "Alergia", "");
        assertTrue(semNota.getNote() == null || semNota.getNote().isEmpty());

        Restriction semNotaNula = criarRestricao(perfilTeste, "Alergia", null);
        assertTrue(semNotaNula.getNote() == null || semNotaNula.getNote().isEmpty());
    }

    // ------------------------------------------------------------------------------------------
    // Listagem (não coberta no plano, mas existe no service)
    // ------------------------------------------------------------------------------------------

    @Test
    public void listaRestricoesAtivasEInativas() {
        // A listagem traz ativas e inativas; perfil sem restrições devolve lista vazia.
        assertTrue(restrictionService.findAllByProfile(usuarioTeste, perfilTeste.getId()).isEmpty(),
                "Perfil sem restrições deve devolver lista vazia");

        Restriction ativa = criarRestricao(perfilTeste, "Alergia a dipirona", null);
        Restriction inativa = criarRestricao(perfilTeste, "Alergia a camarão", null);
        restrictionService.update(usuarioTeste, inativa.getId(),
                reqAtualizar(perfilTeste.getId(), RestrictionType.FOOD, RestrictionSeverity.SEVERY, "Alergia a camarão", null, false));

        List<Long> ids = restrictionService.findAllByProfile(usuarioTeste, perfilTeste.getId()).stream()
                .map(RestrictionResponse::id).toList();

        assertEquals(2, ids.size());
        assertTrue(ids.contains(ativa.getId()));
        assertTrue(ids.contains(inativa.getId()), "Restrição arquivada também deve ser listada");
    }

    @Test
    public void listaRestricoesPerfilInvalido() {
        assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                () -> restrictionService.findAllByProfile(usuarioTeste, null));
        assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                () -> restrictionService.findAllByProfile(usuarioTeste, 0L));

        // Inexistente e de outro usuário: mesmo 404
        assertErro(HttpStatus.NOT_FOUND, "Profile not found",
                () -> restrictionService.findAllByProfile(usuarioTeste, 999_999L));

        Profile perfilDaMaria = criarPerfil(usuariosDeTeste.get(1), "Carla Souza");
        assertErro(HttpStatus.NOT_FOUND, "Profile not found",
                () -> restrictionService.findAllByProfile(usuarioTeste, perfilDaMaria.getId()));
    }
}
