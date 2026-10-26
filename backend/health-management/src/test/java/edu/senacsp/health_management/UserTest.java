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
import org.junit.jupiter.api.function.Executable;

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

        // Define um usuário principal para o teste
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
        assertNotNull(resposta03, "O cadastro do usuário 03 não deve retornar null");
        assertNotNull(resposta04, "O cadastro do usuário 04 não deve retornar null");
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

    // ---------- Auxiliar ----------
    private void assertErro(HttpStatus status, String mensagem, Executable acao) {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, acao);
        assertEquals(status, ex.getStatusCode());
        assertEquals(mensagem, ex.getReason());
    }

    private SignupRequest signupValido(String email, String senha, String nome) {
        return new SignupRequest(email, senha, nome);
    }

    // ---------- Signup: nome ----------
    @Test
    public void signupNomeObrigatorio() {
        assertErro(HttpStatus.BAD_REQUEST, "Name is required",
                () -> userService.signup(signupValido("novo@email.com", "Senha@123", null)));
        assertErro(HttpStatus.BAD_REQUEST, "Name is required",
                () -> userService.signup(signupValido("novo@email.com", "Senha@123", "   ")));
    }

    @Test
    public void signupNomeTamanhoInvalido() {
        assertErro(HttpStatus.BAD_REQUEST, "Name must have between 3 and 255 characters",
                () -> userService.signup(signupValido("novo@email.com", "Senha@123", "Jo")));
        assertErro(HttpStatus.BAD_REQUEST, "Name must have between 3 and 255 characters",
                () -> userService.signup(signupValido("novo@email.com", "Senha@123", "a".repeat(256))));
    }

    @Test
    public void signupNomeComCaracteresInvalidos() {
        assertErro(HttpStatus.BAD_REQUEST, "Name must contain only letters",
                () -> userService.signup(signupValido("novo@email.com", "Senha@123", "João123")));
    }

    // ---------- Signup: email ----------
    @Test
    public void signupEmailObrigatorio() {
        assertErro(HttpStatus.BAD_REQUEST, "Email is required",
                () -> userService.signup(signupValido(null, "Senha@123", "Novo Usuario")));
        assertErro(HttpStatus.BAD_REQUEST, "Email is required",
                () -> userService.signup(signupValido("  ", "Senha@123", "Novo Usuario")));
    }

    @Test
    public void signupEmailTamanhoInvalido() {
        assertErro(HttpStatus.BAD_REQUEST, "Email must have between 6 and 255 characters",
                () -> userService.signup(signupValido("a@b.c", "Senha@123", "Novo Usuario")));
        assertErro(HttpStatus.BAD_REQUEST, "Email must have between 6 and 255 characters",
                () -> userService.signup(signupValido("a".repeat(250) + "@b.com", "Senha@123", "Novo Usuario")));
    }

    // ---------- Signup: senha ----------
    @Test
    public void signupSenhaObrigatoria() {
        assertErro(HttpStatus.BAD_REQUEST, "Password is required",
                () -> userService.signup(signupValido("novo@email.com", null, "Novo Usuario")));
        assertErro(HttpStatus.BAD_REQUEST, "Password is required",
                () -> userService.signup(signupValido("novo@email.com", "   ", "Novo Usuario")));
    }

    @Test
    public void signupSenhaCurta() {
        assertErro(HttpStatus.BAD_REQUEST, "Password must have at least 8 characters",
                () -> userService.signup(signupValido("novo@email.com", "Aa1@aa", "Novo Usuario")));
    }

    @Test
    public void signupSenhaComCaracteresInvalidos() {
        String msg = "Password contains invalid characters (emojis and spaces are not allowed)";
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> userService.signup(signupValido("novo@email.com", "Senha @123", "Novo Usuario")));
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> userService.signup(signupValido("novo@email.com", "Senha@123😀", "Novo Usuario")));
    }

    @Test
    public void signupSenhaLongaDemais() {
        // 73 bytes (limite é 72)
        String senhaLonga = "Aa1@" + "a".repeat(69);
        assertErro(HttpStatus.BAD_REQUEST, "Password is too long",
                () -> userService.signup(signupValido("novo@email.com", senhaLonga, "Novo Usuario")));
    }

    @Test
    public void signupSenhaSemComplexidade() {
        String msg = "Password must contain lowercase, uppercase, number and special character";
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> userService.signup(signupValido("novo@email.com", "SENHA@123", "Novo Usuario"))); // sem minúscula
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> userService.signup(signupValido("novo@email.com", "senha@123", "Novo Usuario"))); // sem maiúscula
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> userService.signup(signupValido("novo@email.com", "Senha@abc", "Novo Usuario"))); // sem número
        assertErro(HttpStatus.BAD_REQUEST, msg,
                () -> userService.signup(signupValido("novo@email.com", "Senha1234", "Novo Usuario"))); // sem especial
    }

    // ---------- Login ----------
    @Test
    public void loginEmailOuSenhaNulos() {
        assertErro(HttpStatus.UNAUTHORIZED, "Email or password are invalid",
                () -> userService.login(new LoginRequest(null, "Senha@123", false)));
        assertErro(HttpStatus.UNAUTHORIZED, "Email or password are invalid",
                () -> userService.login(new LoginRequest("joao.silva@email.com", null, false)));
    }

    @Test
    public void loginRememberObrigatorio() {
        assertErro(HttpStatus.BAD_REQUEST, "Remember is required",
                () -> userService.login(new LoginRequest("joao.silva@email.com", "Senha@123", null)));
    }

    @Test
    public void loginMensagemParaEmailNaoCadastrado() {
        assertErro(HttpStatus.UNAUTHORIZED, "Email or password are invalid",
                () -> userService.login(new LoginRequest("inexistente@email.com", "Senha@123", false)));
    }

    @Test
    public void loginMensagemParaSenhaIncorreta() {
        assertErro(HttpStatus.UNAUTHORIZED, "Email or password are invalid",
                () -> userService.login(new LoginRequest("joao.silva@email.com", "Errada@123", false)));
    }

    @Test
    public void loginContaArquivada() {
        // Arquiva a conta do usuário principal (active = false)
        userService.update(usuarioTeste,
                new UpdateUserRequest("joao.silva@email.com", "Senha@123", "João Silva", false));

        assertErro(HttpStatus.UNAUTHORIZED, "This account was archived",
                () -> userService.login(new LoginRequest("joao.silva@email.com", "Senha@123", false)));
    }

    // ---------- Update ----------
    @Test
    public void updateActiveObrigatorio() {
        assertErro(HttpStatus.BAD_REQUEST, "Active info is required",
                () -> userService.update(usuarioTeste,
                        new UpdateUserRequest("joao.silva@email.com", "Senha@123", "João Silva", null)));
    }

    @Test
    public void updateEmailDeOutroUsuario() {
        // Tenta trocar o email do João pelo da Maria
        assertErro(HttpStatus.CONFLICT, "Email already in use",
                () -> userService.update(usuarioTeste,
                        new UpdateUserRequest("maria.souza@email.com", "Senha@123", "João Silva", true)));
    }

    @Test
    public void updateUsuarioNaoEncontrado() {
        // Cria e remove um usuário para obter uma entidade com ID que não existe mais no banco
        SignupRequest temp = signupValido("temporario@email.com", "Senha@123", "Usuario Temporario");
        userService.signup(temp);
        User fantasma = userRepository.findByEmail(temp.email()).orElseThrow();
        userRepository.deleteById(fantasma.getId());

        assertErro(HttpStatus.UNAUTHORIZED, "User not found",
                () -> userService.update(fantasma,
                        new UpdateUserRequest("outro.email@email.com", "Senha@123", "Usuario Temporario", true)));
    }

}
