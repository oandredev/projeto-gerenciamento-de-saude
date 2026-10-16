package edu.senacsp.health_management;

import edu.senacsp.health_management.dto.request.user.LoginRequest;
import edu.senacsp.health_management.dto.request.user.SignupRequest;
import edu.senacsp.health_management.dto.request.user.UpdateUserRequest;
import edu.senacsp.health_management.dto.response.user.AuthResponse;
import edu.senacsp.health_management.dto.response.user.UserResponse;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.repository.UserRepository;
import edu.senacsp.health_management.service.UserService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User usuarioTeste;
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

        // Cadastra os usuários usando o UserService
        userService.signup(usuario01);
        userService.signup(usuario02);

        // Recupera as entidades persistidas para uso nos testes
        usuariosDeTeste = new ArrayList<>();
        usuariosDeTeste.add(userRepository.findByEmail(usuario01.email()).orElseThrow());
        usuariosDeTeste.add(userRepository.findByEmail(usuario02.email()).orElseThrow());

        // Define um usuário principal para o teste (opcional)
        usuarioTeste = usuariosDeTeste.get(0);
    }

    @AfterEach
    public void tearDown() {
        // Limpar o banco de dados usado no teste.
        // Remove por id, pois nos testes de alteração a entidade em memória
        // pode estar desatualizada (nome/email/senha antigos).
        for (User u : usuariosDeTeste) {
            userRepository.deleteById(u.getId());
        }
    }

    @Test
    public void novosUsuarios() {
        // Verificar se novos usuários com dados completos estarão cadastrados no banco.
        // Cadastrando o terceiro usuário
        SignupRequest usuario03 = new SignupRequest(
                "carlos.pereira@email.com",
                "Senha@789",
                "Carlos Pereira"
        );
        UserResponse resposta03 = userService.signup(usuario03);
        usuariosDeTeste.add(userRepository.findByEmail(usuario03.email()).orElseThrow());

        // Cadastrando o quarto usuário
        SignupRequest usuario04 = new SignupRequest(
                "ana.lima@email.com",
                "Senha@321",
                "Ana Lima"
        );
        UserResponse resposta04 = userService.signup(usuario04);
        usuariosDeTeste.add(userRepository.findByEmail(usuario04.email()).orElseThrow());

        // Assert de que nenhum dos novos usuários cadastrados retornou null
        assertNotNull(String.valueOf(resposta03), "O cadastro do usuário 03 não deve retornar null");
        assertNotNull(String.valueOf(resposta04), "O cadastro do usuário 04 não deve retornar null");

        // Assert de que a lista de testes agora possui tamanho 4
        assertEquals(4, usuariosDeTeste.size(), "A lista de usuários de teste deve ter 4 elementos");
    }

    @Test
    public void usuarioJaCadastrado() {
        // Como no @BeforeEach o email "joao.silva@email.com" já foi cadastrado,
        // esta requisição representa uma tentativa de cadastro duplicado.
        SignupRequest usuario03 = new SignupRequest(
                "joao.silva@email.com",
                "Senha@123",
                "João Silva"
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.signup(usuario03)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Email already in use", exception.getReason());
    }

    @Test
    public void emailInvalido() {
        // Verificar se o sistema permite a inserção de texto sem '@' como campo de email.
        SignupRequest usuarioEmailInvalido = new SignupRequest(
                "carlos.pereira.email.com",
                "Senha@789",
                "Carlos Pereira"
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.signup(usuarioEmailInvalido)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Email format is invalid", exception.getReason());

        // O usuário com email inválido não pode ter sido persistido
        assertTrue(userRepository.findByEmail(usuarioEmailInvalido.email()).isEmpty(),
                "Usuário com email inválido não deve ser salvo no banco");
    }

    @Test
    public void login() {
        // Verificar o acesso do sistema através de email e senha registrados no banco.
        LoginRequest loginRequest = new LoginRequest(
                "joao.silva@email.com",
                "Senha@123",
                false
        );

        AuthResponse resposta = userService.login(loginRequest);

        assertNotNull(resposta, "O login não deve retornar null");
        assertNotNull(resposta.token(), "O login deve retornar um token");
        assertFalse(resposta.token().isBlank(), "O token não deve ser vazio");
        assertEquals("joao.silva@email.com", resposta.userResponse().email());
        assertEquals("João Silva", resposta.userResponse().name());
    }

    @Test
    public void loginEmailInvalido() {
        // Verificar se a tentativa de acesso com e-mail não cadastrado no banco retorna exceção.
        LoginRequest loginRequest = new LoginRequest(
                "inexistente@email.com",
                "Senha@123",
                false
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.login(loginRequest)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Email or password are invalid", exception.getReason());
    }

    @Test
    public void loginSenhaInvalida() {
        // Verificar se a tentativa de acesso com e-mail cadastrado, mas com senha inválida, retorna exceção.
        LoginRequest loginRequest = new LoginRequest(
                "joao.silva@email.com",
                "SenhaErrada@999",
                false
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.login(loginRequest)
        );

        // Mesma mensagem do email inexistente, para não revelar quais emails estão cadastrados
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Email or password are invalid", exception.getReason());
    }

    @Test
    public void nomeAlterado() {
        // Verifica a continuidade de andamento do sistema com nome de cadastro alterado.
        UpdateUserRequest request = new UpdateUserRequest(
                "joao.silva@email.com",
                "Senha@123",
                "João Pedro Silva",
                true
        );

        UserResponse resposta = userService.update(usuarioTeste, request);

        assertEquals("João Pedro Silva", resposta.name());

        // Confirma a alteração no banco
        User atualizado = userRepository.findByEmail("joao.silva@email.com").orElseThrow();
        assertEquals("João Pedro Silva", atualizado.getName());

        // O sistema continua funcionando: o login segue válido com o novo nome
        AuthResponse login = userService.login(
                new LoginRequest("joao.silva@email.com", "Senha@123", false));
        assertEquals("João Pedro Silva", login.userResponse().name());
    }

    @Test
    public void emailAlterado() {
        // Verifica a continuidade de andamento do sistema com email de cadastro alterado.
        UpdateUserRequest request = new UpdateUserRequest(
                "joao.novo@email.com",
                "Senha@123",
                "João Silva",
                true
        );

        UserResponse resposta = userService.update(usuarioTeste, request);

        assertEquals("joao.novo@email.com", resposta.email());

        // O email antigo deixou de existir e o novo está salvo no banco
        assertTrue(userRepository.findByEmail("joao.silva@email.com").isEmpty(),
                "O email antigo não deve mais existir");
        assertTrue(userRepository.findByEmail("joao.novo@email.com").isPresent(),
                "O novo email deve estar salvo no banco");

        // Login com o novo email funciona
        AuthResponse login = userService.login(
                new LoginRequest("joao.novo@email.com", "Senha@123", false));
        assertNotNull(login.token());

        // Login com o email antigo falha
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.login(new LoginRequest("joao.silva@email.com", "Senha@123", false))
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    public void senhaAlterada() {
        // Verifica a continuidade de andamento do sistema com senha de cadastro alterada.
        UpdateUserRequest request = new UpdateUserRequest(
                "joao.silva@email.com",
                "NovaSenha@789",
                "João Silva",
                true
        );

        userService.update(usuarioTeste, request);

        // Login com a nova senha funciona
        AuthResponse login = userService.login(
                new LoginRequest("joao.silva@email.com", "NovaSenha@789", false));
        assertNotNull(login.token(), "Login com a nova senha deve gerar token");

        // Login com a senha antiga falha
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.login(new LoginRequest("joao.silva@email.com", "Senha@123", false))
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Email or password are invalid", exception.getReason());
    }
}
