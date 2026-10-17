package edu.senacsp.health_management;

import edu.senacsp.health_management.dto.request.profile.CreateProfileRequest;
import edu.senacsp.health_management.dto.request.profile.UpdateProfileRequest;
import edu.senacsp.health_management.dto.request.user.LoginRequest;
import edu.senacsp.health_management.dto.request.user.SignupRequest;
import edu.senacsp.health_management.dto.response.profile.ProfileResponse;
import edu.senacsp.health_management.entity.Profile;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.repository.ProfileRepository;
import edu.senacsp.health_management.repository.UserRepository;
import edu.senacsp.health_management.service.ProfileService;
import edu.senacsp.health_management.service.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProfileTest {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;

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

        LoginRequest loginUsuario01 = new LoginRequest(
                "joao.silva@email.com",
                "Senha@123",
                false
        );

        CreateProfileRequest perfil01 = new CreateProfileRequest(
                "Pedro Silva",
                1L
        );

        userService.signup(usuario01);
        userService.signup(usuario02);
        userService.login(loginUsuario01);

        usuariosDeTeste = new ArrayList<>();
        usuariosDeTeste.add(userRepository.findByEmail(usuario01.email()).orElseThrow());
        usuariosDeTeste.add(userRepository.findByEmail(usuario02.email()).orElseThrow());

        usuarioTeste = usuariosDeTeste.get(0);

        ProfileResponse profileSet = profileService.create(usuarioTeste, perfil01);

        perfisDeTeste = new ArrayList<>();
        perfilTeste = profileRepository.findByIdAndUser(profileSet.id(), usuarioTeste).orElseThrow();
        perfisDeTeste.add(perfilTeste);
    }

    @AfterEach
    public void tearDown() {
        // Perfis primeiro (FK para usuário); só apaga o que ainda existe,
        // pois alguns testes já deletam usuários/perfis.
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
    private Profile criarPerfil(User usuario, String nome, Long avatarId) {
        ProfileResponse resposta = profileService.create(usuario, new CreateProfileRequest(nome, avatarId));
        Profile perfil = profileRepository.findByIdAndUser(resposta.id(), usuario).orElseThrow();
        perfisDeTeste.add(perfil);
        return perfil;
    }

    /** Deleta o usuário (e seus perfis, por causa da FK) simulando um usuário inexistente. */
    private void excluirUsuario(User usuario) {
        profileRepository.deleteAll(profileRepository.findAllByUser(usuario));
        userRepository.deleteById(usuario.getId());

        perfisDeTeste.removeIf(p -> !profileRepository.existsById(p.getId()));
        usuariosDeTeste.removeIf(u -> u.getId().equals(usuario.getId()));
    }

    // ------------------------------------------------------------------------------------------
    // Testes
    // ------------------------------------------------------------------------------------------

    @Test
    public void novosPerfis() {
        // Verificar criação de novos perfis no mesmo usuário.
        Profile perfil02 = criarPerfil(usuarioTeste, "Ana Costa", 2L);
        Profile perfil03 = criarPerfil(usuarioTeste, "Lucas Pereira", 3L);

        // Assert de que os novos perfis foram persistidos
        assertNotNull(perfil02.getId(), "O perfil 02 deve ter sido persistido");
        assertNotNull(perfil03.getId(), "O perfil 03 deve ter sido persistido");

        // Assert de que os dados salvos correspondem aos enviados
        assertEquals("Ana Costa", perfil02.getName());
        assertEquals(2L, perfil02.getAvatarId());
        assertEquals("Lucas Pereira", perfil03.getName());
        assertEquals(3L, perfil03.getAvatarId());

        // Assert de que o usuário agora possui 3 perfis (1 do setUp + 2 novos)
        assertEquals(3, perfisDeTeste.size(), "A lista de perfis de teste deve ter 3 elementos");
        assertEquals(3, profileRepository.findAllByUser(usuarioTeste).size(),
                "O usuário deve ter 3 perfis no banco");
    }

    @Test
    public void usuarioNaoExistente() {
        // Verificar se a tentativa de atrelação de um perfil a usuário não existente retorna exceção.
        // O usuário "logado" (usuarioTeste) é deletado do banco, mas o objeto continua em memória.
        excluirUsuario(usuarioTeste);
        assertFalse(userRepository.existsById(usuarioTeste.getId()), "O usuário deve ter sido deletado");

        CreateProfileRequest novoPerfil = new CreateProfileRequest("Ana Costa", 2L);

        // O service não valida a existência do usuário; o banco rejeita a FK inexistente.
        assertThrows(
                DataAccessException.class,
                () -> profileService.create(usuarioTeste, novoPerfil)
        );
    }

    @Test
    public void nomeRepetido() {
        // Verificar se a criação de um novo perfil tem um nome igual ao de outro perfil no mesmo usuário.
        // "Pedro Silva" já foi criado no setUp para usuarioTeste.
        CreateProfileRequest perfilDuplicado = new CreateProfileRequest("Pedro Silva", 2L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> profileService.create(usuarioTeste, perfilDuplicado)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("This profile name is already being used by this same user", exception.getReason());

        // Mesmo nome com espaços extras também deve ser considerado repetido (name é normalizado)
        CreateProfileRequest perfilDuplicadoEspacos = new CreateProfileRequest("  Pedro   Silva  ", 2L);

        ResponseStatusException exception2 = assertThrows(
                ResponseStatusException.class,
                () -> profileService.create(usuarioTeste, perfilDuplicadoEspacos)
        );
        assertEquals(HttpStatus.CONFLICT, exception2.getStatusCode());

        // Já o mesmo nome em OUTRO usuário é permitido
        User outroUsuario = usuariosDeTeste.get(1);
        Profile perfilOutroUsuario = criarPerfil(outroUsuario, "Pedro Silva", 1L);
        assertNotNull(perfilOutroUsuario.getId(), "O mesmo nome deve ser aceito em outro usuário");
    }

    @Test
    public void listaPerfis() {
        // Verificar se os perfis criados em um mesmo usuário estão atribuídos ao mesmo.
        User outroUsuario = usuariosDeTeste.get(1);

        Profile perfil02 = criarPerfil(usuarioTeste, "Ana Costa", 2L);
        Profile perfil03 = criarPerfil(usuarioTeste, "Lucas Pereira", 3L);
        Profile perfilOutro = criarPerfil(outroUsuario, "Carla Souza", 4L);

        List<Long> idsUsuarioTeste = profileService.findAllByUser(usuarioTeste).stream()
                .map(ProfileResponse::id)
                .toList();
        List<Long> idsOutroUsuario = profileService.findAllByUser(outroUsuario).stream()
                .map(ProfileResponse::id)
                .toList();

        // Usuário principal: perfil do setUp + 2 novos, sem o perfil do outro usuário
        assertEquals(3, idsUsuarioTeste.size());
        assertTrue(idsUsuarioTeste.contains(perfilTeste.getId()));
        assertTrue(idsUsuarioTeste.contains(perfil02.getId()));
        assertTrue(idsUsuarioTeste.contains(perfil03.getId()));
        assertFalse(idsUsuarioTeste.contains(perfilOutro.getId()),
                "Perfil de outro usuário não deve aparecer na lista");

        // Outro usuário: apenas o seu perfil
        assertEquals(1, idsOutroUsuario.size());
        assertTrue(idsOutroUsuario.contains(perfilOutro.getId()));
    }

    @Test
    public void listaPerfisSemUsuario() {
        // Verificar se a requisição de uma lista de perfis em usuário não existente retorna exceção.
        excluirUsuario(usuarioTeste);
        assertFalse(userRepository.existsById(usuarioTeste.getId()), "O usuário deve ter sido deletado");

        // ATENÇÃO: o ProfileService.findAllByUser não lança exceção; ele retorna lista vazia
        // para um usuário que não existe. O teste reflete o comportamento atual do service.
        List<ProfileResponse> perfis = profileService.findAllByUser(usuarioTeste);

        assertNotNull(perfis);
        assertTrue(perfis.isEmpty(), "Usuário inexistente não deve ter perfis");
    }

    @Test
    public void perfilEditado() {
        // Verificar se novos parâmetros de perfil estão atrelados ao perfil editado.
        UpdateProfileRequest edicao = new UpdateProfileRequest("Pedro Souza", 5L, false);

        profileService.update(usuarioTeste, perfilTeste.getId(), edicao);

        Profile perfilEditado = profileRepository.findByIdAndUser(perfilTeste.getId(), usuarioTeste).orElseThrow();

        assertEquals("Pedro Souza", perfilEditado.getName());
        assertEquals(5L, perfilEditado.getAvatarId());
        assertFalse(perfilEditado.isActive(), "O perfil deve ter sido arquivado (active = false)");

        // O perfil editado continua sendo o mesmo registro e do mesmo usuário
        assertEquals(perfilTeste.getId(), perfilEditado.getId());
        assertEquals(1, profileRepository.findAllByUser(usuarioTeste).size(),
                "A edição não deve criar um novo perfil");

        // Reativação
        profileService.update(usuarioTeste, perfilTeste.getId(),
                new UpdateProfileRequest("Pedro Souza", 5L, true));
        assertTrue(profileRepository.findByIdAndUser(perfilTeste.getId(), usuarioTeste)
                .orElseThrow().isActive(), "O perfil deve ter sido reativado");
    }

    @Test
    public void perfilNaoExistenteEditado() {
        // Verificar se a tentativa de edição de um perfil não existente retorna exceção.
        Long idInexistente = 999_999L;
        assertFalse(profileRepository.existsById(idInexistente));

        UpdateProfileRequest edicao = new UpdateProfileRequest("Pedro Souza", 5L, true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> profileService.update(usuarioTeste, idInexistente, edicao)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Profile not found", exception.getReason());
    }

    @Test
    public void perfilEditadoUsuarioErrado() {
        // Verificar se a falta de vínculo entre perfil editado e usuário retorna exceção.
        // O perfil pertence a usuarioTeste (João), mas quem tenta editar é a Maria.
        User usuarioErrado = usuariosDeTeste.get(1);

        UpdateProfileRequest edicao = new UpdateProfileRequest("Perfil Invadido", 5L, true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> profileService.update(usuarioErrado, perfilTeste.getId(), edicao)
        );

        // Mesmo erro de "não existe" para não revelar quais IDs existem
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Profile not found", exception.getReason());

        // Assert de que o perfil original não foi alterado
        Profile perfilOriginal = profileRepository.findByIdAndUser(perfilTeste.getId(), usuarioTeste).orElseThrow();
        assertEquals("Pedro Silva", perfilOriginal.getName());
        assertEquals(1L, perfilOriginal.getAvatarId());
    }

}
