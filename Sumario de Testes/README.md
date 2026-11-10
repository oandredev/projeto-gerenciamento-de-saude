# Relatório Sumário de Testes

## 1. Testes Back-End

O [Back-End](https://github.com/oandredev/projeto-gerenciamento-de-saude/tree/main/backend/health-management) do projeto consiste em 7 entidades com 4 classes principais de serviço nas quais foram baseados os casos de teste: *User*, *Profile*, *Restrictions*, *Schedule*. Os testes dos *scripts* do *back-end* foram realizados de três maneiras: caixa preta (**Postman**), caixa branca (**JUnit**), desempenho (**JMeter**) e *linting* (**Checkstyle**).

### 1.1 Testes de caixa preta com Postman

Para cada classe testada foram realizados dois cenários: um inválido, com pelo menos um campo inválido, e outro válido, com todos os campos corretos. Foram anexados as evidências de cada teste, com prints da requisição (URL e body) e da resposta (JSON retornado, status HTTP, tempo de resposta em ms e consumo de memória). Os testes foram feitos com o método POST, cobrindo cadastro de usuário, login, criação de Profile, criação de Restriction e criação de Schedule.

#### Signup inválido pela senha

<img width="886" height="365" alt="image" src="https://github.com/user-attachments/assets/e55f9ae4-90e8-4321-b449-07cb35c5521d" />
<img width="886" height="194" alt="image" src="https://github.com/user-attachments/assets/ccc3968d-d22d-4c48-ba24-12368d11c745" />

#### Signup válido
<img width="886" height="341" alt="image" src="https://github.com/user-attachments/assets/1c245dfa-561a-42ec-a759-544e47b2fd27" />
<img width="886" height="400" alt="image" src="https://github.com/user-attachments/assets/136cf54c-eca9-4d84-b735-1ef46aa026b0" />

#### Evidência signup válido (H2 Console)
<img width="886" height="189" alt="image" src="https://github.com/user-attachments/assets/319244ad-c24c-4d6e-8135-5ec6938c8a27" />

#### Login inválido pela senha
<img width="886" height="373" alt="image" src="https://github.com/user-attachments/assets/75ec3597-e43f-452b-ac51-53867fa90943" />
<img width="886" height="198" alt="image" src="https://github.com/user-attachments/assets/1d104da2-ab5d-4a78-a4ff-34daa9a6ddf4" />

#### Login válido
<img width="886" height="198" alt="image" src="https://github.com/user-attachments/assets/2585a805-22cf-4889-a6f8-9609e13776f6" />
<img width="886" height="398" alt="image" src="https://github.com/user-attachments/assets/21d2b8bf-3a0a-41e0-baff-1058eff8d0af" />

#### Evidência de token gerado
<img width="786" height="546" alt="image" src="https://github.com/user-attachments/assets/8b39e8c0-de41-4b06-b852-4a907924de16" />

#### Profile inválido pelo nome com números
<img width="886" height="288" alt="image" src="https://github.com/user-attachments/assets/729cee0b-644b-4d8c-b555-2eebec0b1b74" />
<img width="889" height="375" alt="image" src="https://github.com/user-attachments/assets/15b5669f-983f-4bed-963d-af9fe62ea335" />
<img width="886" height="187" alt="image" src="https://github.com/user-attachments/assets/79541ad7-6c5c-4076-a4c2-86fc424cd728" />

#### Profile válido
<img width="886" height="371" alt="image" src="https://github.com/user-attachments/assets/a7e549f0-9fd4-49af-86d3-41ed6549e433" />
<img width="886" height="376" alt="image" src="https://github.com/user-attachments/assets/3757e104-5393-4599-9f28-dacbdf1b22e5" />

#### Evidência de profile válido (H2 Console)
<img width="886" height="281" alt="image" src="https://github.com/user-attachments/assets/71f4939b-6b1c-46c8-bd64-f53d519197c7" />

#### Restriction inválido por falta de token
<img width="886" height="410" alt="image" src="https://github.com/user-attachments/assets/b36f28b2-63ee-473f-9bc6-88eeee8d4814" />
<img width="886" height="195" alt="image" src="https://github.com/user-attachments/assets/b35e8cb2-0fc0-4bbb-9110-f5cac80fb620" />

#### Restriction válido
<img width="886" height="414" alt="image" src="https://github.com/user-attachments/assets/0eaf89c5-19b6-421a-8963-6a79e5b012bc" />
<img width="886" height="453" alt="image" src="https://github.com/user-attachments/assets/0634d576-4256-4d16-8044-9421c6f8d238" />

#### Evidência de restriction válido (H2 Console)
<img width="886" height="166" alt="image" src="https://github.com/user-attachments/assets/8836d5e6-e104-433d-8deb-8a44fe6921d5" />

#### Schedule inválido por intervalo negativo
<img width="886" height="834" alt="image" src="https://github.com/user-attachments/assets/139dea8b-5357-42e1-859f-2a816e206fb5" />
<img width="886" height="196" alt="image" src="https://github.com/user-attachments/assets/61ce8f5c-3b27-41b3-bb5b-1b2f0d4f767e" />

#### Schedule válido
<img width="886" height="804" alt="image" src="https://github.com/user-attachments/assets/3cb8b4d0-3203-4826-bd57-4d1fcde1e760" />
<img width="886" height="810" alt="image" src="https://github.com/user-attachments/assets/9075bff4-b1ab-431b-a52f-39838cb22659" />

#### Evidências de schedule válido (H2 Console)
<img width="819" height="166" alt="image" src="https://github.com/user-attachments/assets/f446842e-fe99-48c7-a844-e2705ea0c131" />
<img width="819" height="113" alt="image" src="https://github.com/user-attachments/assets/0e0ba60e-8412-48a0-92a4-310ffa1ea967" />
<img width="816" height="211" alt="image" src="https://github.com/user-attachments/assets/a71596db-8dc1-4b38-b08c-5e03942d11d4" />

A consistência de funcionalidade do back-end também foi demonstrada através da plataforma **Swagger**, que roda o back-end localmente conforme demonstrado nas imagens abaixo:

<img width="886" height="969" alt="image" src="https://github.com/user-attachments/assets/1534821a-041d-4b49-8260-932f9ff2fbc3" />
<img width="886" height="920" alt="image" src="https://github.com/user-attachments/assets/b8e6591a-9217-4ea9-93c7-2d92de8b933e" />
<img width="886" height="819" alt="image" src="https://github.com/user-attachments/assets/05adcc28-f279-41cd-8e10-1b489e427b4e" />
<img width="886" height="617" alt="image" src="https://github.com/user-attachments/assets/66417d5c-70a9-4d12-bdbe-c16215c02749" />
<img width="886" height="862" alt="image" src="https://github.com/user-attachments/assets/c42138b1-b629-43ff-9a9e-c83e825ec1ed" />
<img width="886" height="395" alt="image" src="https://github.com/user-attachments/assets/50758a69-a8a3-44c6-8047-da4e2ce206af" />



### 1.2 Testes de caixa branca com JUnit

Para cada classe de serviços foi feita uma classe de testes que compunha os casos de teste do plano. Em todos os casos de teste em que havia uma condição baseada em tamanho limite foi utilizado o método de partição de equivalência por valor limite.

#### UserTest.java

``` java
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
```

#### ProfileTest.java

``` java
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
import org.junit.jupiter.api.function.Executable;

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

    // ---------- Auxiliar ----------
    private void assertErro(HttpStatus status, String mensagem, Executable acao) {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, acao);
        assertEquals(status, ex.getStatusCode());
        assertEquals(mensagem, ex.getReason());
    }

    // ---------- Create ----------
    @Test
    public void criarPerfilNomeObrigatorio() {
        assertErro(HttpStatus.BAD_REQUEST, "Name is required",
                () -> profileService.create(usuarioTeste, new CreateProfileRequest(null, 1L)));
        assertErro(HttpStatus.BAD_REQUEST, "Name is required",
                () -> profileService.create(usuarioTeste, new CreateProfileRequest("   ", 1L)));
    }

    @Test
    public void criarPerfilNomeTamanhoInvalido() {
        assertErro(HttpStatus.BAD_REQUEST, "Name must have between 3 and 255 characters",
                () -> profileService.create(usuarioTeste, new CreateProfileRequest("Jo", 1L)));
        assertErro(HttpStatus.BAD_REQUEST, "Name must have between 3 and 255 characters",
                () -> profileService.create(usuarioTeste, new CreateProfileRequest("a".repeat(256), 1L)));
    }

    @Test
    public void criarPerfilNomeComCaracteresInvalidos() {
        assertErro(HttpStatus.BAD_REQUEST, "Name must contain only letters",
                () -> profileService.create(usuarioTeste, new CreateProfileRequest("Pedro123", 1L)));
    }

    @Test
    public void criarPerfilAvatarInvalido() {
        assertErro(HttpStatus.BAD_REQUEST, "Avatar ID is invalid",
                () -> profileService.create(usuarioTeste, new CreateProfileRequest("Carlos Lima", null)));
        assertErro(HttpStatus.BAD_REQUEST, "Avatar ID is invalid",
                () -> profileService.create(usuarioTeste, new CreateProfileRequest("Carlos Lima", 0L)));
        assertErro(HttpStatus.BAD_REQUEST, "Avatar ID is invalid",
                () -> profileService.create(usuarioTeste, new CreateProfileRequest("Carlos Lima", -1L)));
    }

    // ---------- Update ----------
    @Test
    public void editarPerfilIdInvalido() {
        UpdateProfileRequest req = new UpdateProfileRequest("Pedro Silva", 1L, true);

        assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                () -> profileService.update(usuarioTeste, null, req));
        assertErro(HttpStatus.BAD_REQUEST, "Profile ID is invalid",
                () -> profileService.update(usuarioTeste, 0L, req));
    }

    @Test
    public void editarPerfilAvatarInvalido() {
        Long id = perfisDeTeste.get(0).getId();

        assertErro(HttpStatus.BAD_REQUEST, "Avatar ID is invalid",
                () -> profileService.update(usuarioTeste, id, new UpdateProfileRequest("Pedro Silva", null, true)));
        assertErro(HttpStatus.BAD_REQUEST, "Avatar ID is invalid",
                () -> profileService.update(usuarioTeste, id, new UpdateProfileRequest("Pedro Silva", 0L, true)));
    }

    @Test
    public void editarPerfilActiveObrigatorio() {
        Long id = perfisDeTeste.get(0).getId();

        assertErro(HttpStatus.BAD_REQUEST, "Active info is required",
                () -> profileService.update(usuarioTeste, id, new UpdateProfileRequest("Pedro Silva", 1L, null)));
    }

    @Test
    public void editarPerfilNomeInvalido() {
        Long id = perfisDeTeste.get(0).getId();

        assertErro(HttpStatus.BAD_REQUEST, "Name is required",
                () -> profileService.update(usuarioTeste, id, new UpdateProfileRequest(null, 1L, true)));
        assertErro(HttpStatus.BAD_REQUEST, "Name must have between 3 and 255 characters",
                () -> profileService.update(usuarioTeste, id, new UpdateProfileRequest("Jo", 1L, true)));
        assertErro(HttpStatus.BAD_REQUEST, "Name must contain only letters",
                () -> profileService.update(usuarioTeste, id, new UpdateProfileRequest("Pedro123", 1L, true)));
    }

    @Test
    public void editarPerfilNomeDuplicadoMensagem() {
        // Cria um segundo perfil para o mesmo usuário e registra para limpeza no tearDown
        ProfileResponse segundo = profileService.create(usuarioTeste, new CreateProfileRequest("Ana Souza", 2L));
        perfisDeTeste.add(profileRepository.findByIdAndUser(segundo.id(), usuarioTeste).orElseThrow());

        // Tenta renomear "Ana Souza" para "Pedro Silva" (já usado pelo perfil do setUp)
        assertErro(HttpStatus.CONFLICT, "This profile name is already being used by this same user",
                () -> profileService.update(usuarioTeste, segundo.id(), new UpdateProfileRequest("Pedro Silva", 2L, true)));
    }

}
```

#### RestrictionTest.java

``` java
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
```

#### ScheduleTest.java

``` java
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
```

Ao final, foi feito uma classe de suíte de testes:

#### TestSuite.java

``` java
package edu.senacsp.health_management;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        UserTest.class,
        ProfileTest.class,
        RestrictionTest.class,
        ScheduleTest.class
})
public class TestSuite {
}
```

Rodando a suíte de testes, foi certificado de que 123 casos de teste foram bem-sucedidos:

#### Evidência de sucesso de testes unitários
<img width="753" height="365" alt="image" src="https://github.com/user-attachments/assets/6290d7c6-cad8-4801-aad8-f893ab7a8ddb" />

### 1.3 Testes de Desempenho com JMeter

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

O teste retornou os seguintes resultados:

#### Sumário
| Rótulo                        | # Amostras | Média | Mín. | Máx. | Desvio Padrão | % de Erro | Vazão   | KB/s | Sent KB/sec | Média de Bytes |
|-------------------------------|------------|-------|------|------|---------------|-----------|---------|------|-------------|----------------|
| Requisição HTTP - User Signup | 50         | 40    | 3    | 901  | 128,39        | 80,000%   | 5,54693 | 2,87 | 1,49        | 529,0          |
| TOTAL                         | 50         | 40    | 3    | 901  | 128,39        | 80,000%   | 5,54693 | 2,87 | 1,49        | 529,0          |

#### Gráfico

<img width="2222" height="1536" alt="image" src="https://github.com/user-attachments/assets/5abfefff-8a74-4be0-bc3a-cde1aafe4adb" />

Percebemos que usando a regra do e-mail único, o teste de desempenho só é efetuado no primeiro loop, já que nos 4 seguintes, ele fará a requisição HTTP com 10 usuários já cadastrados. Por esse motivo temos 10 amostras com código de resposta `201` e 40 com `409` negando a aplicação.

Um outro teste foi preparado com as mesmas configurações no JMeter, mas agora sem a regra do e-mail único. Eis os resultados:

#### Sumário

| Rótulo                        | # Amostras | Média | Mín. | Máx. | Desvio Padrão | % de Erro | Vazão   | KB/s | Sent KB/sec | Média de Bytes |
|-------------------------------|------------|-------|------|------|---------------|-----------|---------|------|-------------|----------------|
| Requisição HTTP - User Signup | 50         | 88    | 77   | 139  | 14,75         | 0,000%    | 5,31463 | 3,76 | 1,42        | 723,9          |
| TOTAL                         | 50         | 88    | 77   | 139  | 14,75         | 0,000%    | 5,31463 | 3,76 | 1,42        | 723,9          |

#### Gráfico

<img width="2222" height="1536" alt="image" src="https://github.com/user-attachments/assets/47bba784-c0cd-486f-a07e-33d1f738c7a4" />

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

Assim, o desvio padrão busca uma estabilidade quando o mesmo número de usuários simultâneos se cadastra, diminuindo a cada teste. Enquanto isso a média de latência sempre sobe, mas ainda com picos baixos que retardam o aumento desenfreado. No entanto, durante o crescimento de usuários, o desvio padrão apenas aumenta, e o coeficiente padrão chega a um total de 90,14%, representando uma instabilidade nos dados. Se o crescimento de usuários for exponencial, a instabilidade da diferença de latência ficará cada vez maior.

> Nota: Alguns resultados brutos do teste de desempenho são muito volumosos para o formato deste arquivo. A documentação dos testes de desempenho completa pode ser acessada por meio [deste link](https://github.com/oandredev/projeto-gerenciamento-de-saude/blob/feat/new-project-base-tests/Documentação%20de%20Testes/Teste_de_Desempenho.md).


### 1.4 Linting de código com Checkstyle

O linting do back-end foi planejado de forma a garantir práticas de boa escrita de código como quebras de linha no final dos arquivos, eliminação de importações e variáveis locais não utilizadas, blocos `if` sem chaves (`{}`). Mais especificamente declaramos que o maior arquivo do projeto não pode ter mais de 600 linhas e de que os erros previstos com mensagens de erro personalizadas devem impedir o surgimento de uma mensagem de erro `RuntimeException`, como uma forma de asseguramento de que foram cobertos possíveis erros que ocorreriam enquanto o programa estava sendo rodado.

#### checkstyle.xml

``` xml
<?xml version="1.0"?>
<!DOCTYPE module PUBLIC
  "-//Puppy Crawl//DTD Check Configuration 1.3//EN"
  "https://checkstyle.org/dtds/configuration_1_3.dtd">

<!-- Nenhum arquivo pode ter mais de 600 linhas -->
<module name="Checker">
  <module name="FileLength">
    <property name="max" value="600"/>
  </module>
</module>

<!-- Todos os blocos de código estarão entre chaves ({}) -->
<module name="Checker">
  <module name="TreeWalker">
    <module name="NeedBraces"/>
  </module>
</module>

<!-- Todo arquivo termina com uma quebra de linha -->
<module name="Checker">
  <module name="NewlineAtEndOfFile"/>
</module>

<!-- Todas as importações são utilizadas sem redundância -->
<module name="Checker">
  <module name="TreeWalker">
    <module name="UnusedImports"/>
  </module>
</module>

<module name="Checker">
  <module name="TreeWalker">
    <module name="RedundantImport"/>
  </module>
</module>

<!-- Todas as variáveis locais são utilizadas -->
<module name="Checker">
  <module name="TreeWalker">
    <module name="UnusedLocalVariable">
    </module>
  </module>
</module>

<!-- Nenhum throw responderá com mensagens padrão de erros de execução. Problemas previstos no código possuem suas próprias mensagens de erro -->
<module name="Checker">
  <module name="TreeWalker">
    <module name="IllegalThrows">
      <property name="illegalClassNames" value="RuntimeException"/>
    </module>
  </module>
</module>
```

No primeiro momento, o checkstyle registrou 76 violações conforme o *log* abaixo:

#### Primeira Evidência de registro de violações (print do *log*)
<img width="1489" height="299" alt="image" src="https://github.com/user-attachments/assets/aefdb3e4-b6e2-417e-9ab0-c5cf88c56239" />

#### Segunda Evidência de registro de violações (texto completo do *log*)
``` txt
[INFO] There are 76 errors reported by Checkstyle 14.4.0 with config/checkstyle/checkstyle.xml ruleset.
[ERROR] src\main\java\edu\senacsp\health_management\advice\GlobalExceptionHandler.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\advice\SuccessResponseWrapper.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\annotation\ApiMessage.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\config\OpenApiConfig.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\config\SecurityConfig.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\controller\AppointmentScheduleController.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\controller\ConfirmationController.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\controller\MedicationScheduleController.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\controller\OccurrenceController.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\controller\ProfileController.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\controller\RestrictionController.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\controller\ScheduleController.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\controller\UserController.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\confirmation\CreateConfirmationRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\profile\CreateProfileRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\profile\UpdateProfileRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\restriction\CreateRestrictionRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\restriction\UpdateRestrictionRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\schedule\CreateAppointmentData.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\schedule\CreateMedicationData.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\schedule\CreateScheduleRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\schedule\ScheduleDetails.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\schedule\UpdateScheduleRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\user\LoginRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\user\SignupRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\request\user\UpdateUserRequest.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\confirmation\ConfirmationResponse.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\general\ErrorResponse.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\general\SuccessResponse.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\occurrence\OccurrenceResponse.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\profile\ProfileResponse.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\restriction\RestrictionResponse.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\schedule\AppointmentDto.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\schedule\ExtraScheduleDetails.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\schedule\MedicationDto.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\schedule\ScheduleResponse.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\user\AuthResponse.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\dto\response\user\UserResponse.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\AppointmentSchedule.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\Confirmation.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\enums\DosageUnit.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\enums\MedicationStripe.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\enums\OccurrenceStatus.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\enums\ReminderUnit.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\enums\RestrictionSeverity.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\enums\RestrictionType.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\enums\ScheduleType.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\MedicationSchedule.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\Profile.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\Restriction.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\Schedule.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\entity\User.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\HealthManagementApplication.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\repository\AppointmentScheduleRepository.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\repository\ConfirmationRepository.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\repository\MedicationScheduleRepository.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\repository\ProfileRepository.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\repository\RestrictionRepository.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\repository\ScheduleRepository.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\repository\UserRepository.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\security\JwtAuthFilter.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\service\AppointmentScheduleService.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\service\ConfirmationService.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\service\JwtService.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\service\MedicationScheduleService.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\service\OccurrenceService.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\service\OccurrenceService.java:[104,9] (blocks) NeedBraces: A estrutura 'if' deve utilizar '{}'s.
[ERROR] src\main\java\edu\senacsp\health_management\service\ProfileService.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\service\RestrictionService.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\service\ScheduleService.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\service\UserService.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\util\OccurrenceUtil.java:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[ERROR] src\main\java\edu\senacsp\health_management\util\OccurrenceUtil.java:[34,13] (blocks) NeedBraces: A estrutura 'if' deve utilizar '{}'s.
[ERROR] src\main\java\edu\senacsp\health_management\util\OccurrenceUtil.java:[50,13] (blocks) NeedBraces: A estrutura 'if' deve utilizar '{}'s.
[ERROR] src\main\java\edu\senacsp\health_management\util\OccurrenceUtil.java:[51,13] (blocks) NeedBraces: A estrutura 'if' deve utilizar '{}'s.
[ERROR] src\main\resources\application.properties:[1] (misc) NewlineAtEndOfFile: O arquivo não termina com uma linha em branco.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.804 s
[INFO] Finished at: 2026-11-06T22:00:15-03:00
```

Foram 73 violações relacionadas a arquivos não terminados com quebra de linha e 3 de estruturas `if` sem o uso de chaves. Após a correção das violações identificadas, a mensagem do checkstyle não registrou nenhuma violação:

#### Evidência de sucesso no linting
<img width="661" height="311" alt="image" src="https://github.com/user-attachments/assets/e481050c-f5b6-48fc-b76c-6dc76b42ba01" />

## 2. Teste Front-End

O [Front-End](https://github.com/oandredev/projeto-gerenciamento-de-saude/tree/main/frontend) do projeto apresenta 13 telas com um fluxo desejado de dinâmica entre as telas e elementos recomendados de acessibilidade. Os testes do front-end utilizaram estratégias de caixa preta de variação entre estados usando os programas compilados por um *smartphone* através do **Expo** e diagnósticos de acessibilidade usando **Lighthouse** e **Wave**. 

### 2.1 Testes de caixa preta

Até o momento atual do projeto os testes de caixa preta do front-end compuseram a entrada válida e inválida de um usuário cadastrado por meio da análise de transição de estados. O fluxograma abaixo determina quais os estados esperados diante de diferentes ações de cadastro e login:

#### Fluxograma de transição de estados
<picture>
<img width="650" height="420" alt="image" src="https://github.com/user-attachments/assets/ed2d968e-6612-4cfb-aaac-4fcd68bc1862" />
</picture>

A primeira etapa consiste no cadastro de um usuário válido conforme a imagem abaixo:

#### Tela de cadastro com campos preenchidos

<picture>
<img width="370" height="710" alt="image" src="https://github.com/user-attachments/assets/ffed5f7d-c6de-4268-a1d2-51cd04d4faf8" />
</picture>

A expectativa dessa ação que o cadastro seja efetuado no banco de dados. O estado do banco deve envolver o usuário com o respectivo e-mail cadastrado e a senha criptografada. O teste foi bem-sucedido:

#### Evidência de usuário cadastrado com senha criptografada (H2 Console)

<img width="1785" height="537" alt="image" src="https://github.com/user-attachments/assets/ab1234a3-f13e-4a17-89f6-d4eb03946ee3" />

Após efetuar o cadastro são feitos dois outros testes envolvendo a página de login: um com a senha inválida e outro com a senha inválida. Com a senha inválida digitada o estado da tela deve compilar uma mensagem escrita: "E-mail ou senha incorretos". O teste foi bem-sucedido:

#### Tela com login inválido e mensagem de erro
<picture>
<img width="370" height="710" alt="image" src="https://github.com/user-attachments/assets/fef102ac-ef3a-4bf3-a7e5-400a7b5a983e" />
</picture>

Com o login válido, o botão entrar deve alterar o estado da tela para que compile a página inicial, que saúda o usuário chamando-o por seu primeiro nome. O teste foi bem-sucedido:

#### Tela com login válido
<picture>
<img width="370" height="710" alt="tela_login_valido" src="https://github.com/user-attachments/assets/35abe582-e7b0-40fb-94d2-0ecf5497c0cd"/>
</picture>

#### Tela de página inicial
<picture>
<img width="370" height="710" alt="image" src="https://github.com/user-attachments/assets/68cddc8c-8730-432b-968f-8d52cf4417aa" />
</picture>

A dinâmica de fluxo de telas respeitou a transição de estados esperada:

#### Transição de estados (telas)

<picture>
<img width="720" height="950" alt="image" src="https://github.com/user-attachments/assets/c7ed5e14-3442-4320-9632-a4924f24e66a" />
</picture>

### 2.2 Diagnósticos de acessibilidade

Para realizar os diagnósticos de acessibilidade considerou-se a tela de Login. Os programas criados para o React Native foram adaptados para uma versão com funções equivalente usando base em **HTML5**, estilização em **CSS** e programação com **Javascript**. A adaptação resultou em uma versão puramente visual e sem funções atreladas à interface, com o intuito único de avaliar a consistência de princípios de acessibilidade nos componentes. A interface foi então publicada no Vercel para receber o diagnóstico das plataformas através de sua URL.

Os primeiros testes revelaram uma pontuação elevada conforme os parâmetros do Lighthouse, superando a expectativa de pontuação registrada no plano de testes (95):

#### Evidência de pontuação Lighthouse
<img width="990" height="649" alt="image" src="https://github.com/user-attachments/assets/01a7cb34-604b-42d1-99d5-eab3a5587337" />

#### Evidência de auditorias aprovadas do Lighthouse
<img width="992" height="498" alt="image" src="https://github.com/user-attachments/assets/a213690c-acec-4051-ba9b-b295f8bf4552" />

No entanto, o mesmo não pôde ser dito sobre o diagnóstico de acessibilidade do Wave, que ofereceu uma pontuação mediana no contexto geral, passando positivamente em contraste, mas com uma pontuação abaixo da planejada (7.0):

#### Evidência de pontuação Wave
<img width="337" height="890" alt="image" src="https://github.com/user-attachments/assets/d9e7f3fd-3a30-4fa3-b54a-5cd2ffb25c5b" />

#### Evidência de pontuação de contraste Wave
<img width="338" height="738" alt="image" src="https://github.com/user-attachments/assets/e6c419f4-3e79-4118-a2f3-fd4d5510e1cb" />

A pontuação do Wave pode ser explicada pelo fato de que a plataforma pontua conforme a contagem de `aria-label`; recurso de acessibilidade para deficientes visuais que é nativo do **HTML5**. Como o React Native não possui o `aria-label`, não foi obtida nenhuma pontuação nesse aspecto.

Uma segunda tentativa de diagnóstico, apresentando uma versão do programa adaptado contendo `aria-label`, revelou que a pontuação em ambas as plataformas foi maior, concedendo a pontuação do Wave minimamente desejada e uma pontuação ainda maior no Lighthouse destacando a presença de `aria-label`:

#### Evidência de pontuação Lighthouse (segunda tentativa)
<img width="977" height="481" alt="image" src="https://github.com/user-attachments/assets/7269b727-004b-4616-be52-9395b3b4426e" />

#### Evidência de auditorias aprovadas do Lighthouse (segunda tentativa)
<img width="975" height="676" alt="image" src="https://github.com/user-attachments/assets/7186921d-888a-4d1f-85bf-c1801137575c" />

#### Evidência de pontuação Wave (segunda tentativa)
<img width="341" height="929" alt="image" src="https://github.com/user-attachments/assets/218b0424-9ed3-499b-b5da-e37e2108e8c8" />

#### Evidência de pontuação de contraste Wave (segunda tentativa)
<img width="338" height="738" alt="image" src="https://github.com/user-attachments/assets/e6c419f4-3e79-4118-a2f3-fd4d5510e1cb" />

Apesar de ainda serem destacados perdas de pontos no Wave, é reconhecido que os erros apontados se referem a elementos típicos de interfaces para web, e não para aplicativos *mobile*. Portanto, nem todos os pontos puderam ser aproveitados. No entanto, a presença dos `aria-label` que fizeram a pontuação de acessibilidade aumentar, foi adaptada para o React Native, criando uma versão da tela de login em que o equivalente para o React Native; o componente `acessibility`, estivesse presente:

#### LoginScreen.js

``` javascript
import { useState } from 'react';
import {
    KeyboardAvoidingView,
    Platform,
    Pressable,
    ScrollView,
    StyleSheet,
    Text,
    TextInput,
    View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import Logo from '../components/Logo';
import { colors } from '../styles/theme';
import { login as loginRequest } from '../services/authService';

export default function LoginScreen({ navigation }) {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    async function handleLogin() {
        if (!login || !password) {
            setError('Digite o e-mail e a senha.');
            return;
        }
        setError('');
        setLoading(true);
        try {
            await loginRequest({ email: login, password });
            navigation.replace('ProfileSelect');
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <KeyboardAvoidingView
            style={styles.container}
            behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        >
            <View style={styles.header}>
                <Logo size={44} />
                <View style={styles.headerText}>
                    <Text style={styles.brand}>
                        A<Text style={styles.brandAmp}>&</Text>F
                    </Text>
                    <Text style={styles.brandSub}>Health Management</Text>
                </View>
            </View>
            <View style={styles.headerLine} />

            <ScrollView
                contentContainerStyle={styles.scroll}
                keyboardShouldPersistTaps="handled"
            >
                <View style={styles.help}>
                    <Ionicons name="help-circle-outline" size={32} color={colors.blue} />
                    <View style={styles.helpTextBox}>
                        <Text style={styles.helpText}>
                            Dúvidas no acesso? Veja o passo a passo.
                        </Text>
                        <Text style={styles.helpLink}>Acessar manual.</Text>
                    </View>
                </View>

                <Text style={styles.label}>E-mail</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Digite seu e-mail"
                    placeholderTextColor={colors.placeholder}
                    keyboardType="email-address"
                    autoCapitalize="none"
                    value={login}
                    onChangeText={setLogin}
                />

                <View style={styles.passwordRow}>
                    <Text style={styles.label}>Senha</Text>
                    <Text style={styles.forgot}>Esqueci minha senha</Text>
                </View>
                <TextInput
                    style={styles.input}
                    placeholder="Digite sua senha"
                    placeholderTextColor={colors.placeholder}
                    secureTextEntry
                    value={password}
                    onChangeText={setPassword}
                />

                {error !== '' && <Text style={styles.error}>{error}</Text>}

                <Pressable style={styles.primaryButton}>
                    <Text style={styles.primaryText}>Entrar com biometria</Text>
                    <Ionicons name="finger-print" size={22} color={colors.orangeLight} />
                </Pressable>

                <Pressable
                    style={styles.outlineBlue}
                    onPress={handleLogin}
                    disabled={loading}
                >
                    <Text style={styles.outlineBlueText}>
                        {loading ? 'Entrando...' : 'Entrar'}
                    </Text>
                </Pressable>

                <Text style={styles.register}>Ainda não é cadastrado?</Text>
                <Pressable
                    style={styles.outlineOrange}
                    onPress={() => navigation.navigate('SignUp')}
                >
                    <Text style={styles.outlineOrangeText}>Fazer cadastro</Text>
                </Pressable>
            </ScrollView>
        </KeyboardAvoidingView>
    );
}
```

#### Evidência de alteração do código da tela de login

``` javascript
import { useState } from 'react';
import {
    KeyboardAvoidingView,
    Platform,
    Pressable,
    ScrollView,
    StyleSheet,
    Text,
    TextInput,
    View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import Logo from '../components/Logo';
import { colors } from '../styles/theme';

export default function LoginScreen({ navigation }) {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    function handleLogin() {
        navigation.replace('MainTabs');
    }

    return (
        <KeyboardAvoidingView
            style={styles.container}
            behavior={Platform.OS === 'ios' ? 'padding' : undefined}
        >
            <View style={styles.header}>
                <Logo size={44} />
                <View style={styles.headerText}>
                    <Text style={styles.brand}>
                        A<Text style={styles.brandAmp}>&</Text>F
                    </Text>
                    <Text style={styles.brandSub}>Health Management</Text>
                </View>
            </View>
            <View style={styles.headerLine} />

            <ScrollView
                contentContainerStyle={styles.scroll}
                keyboardShouldPersistTaps="handled"
            >
                <View style={styles.help}>
                    <View
                        accessibilityElementsHidden
                        importantForAccessibility="no-hide-descendants"
                    >
                        <Ionicons name="help-circle-outline" size={32} color={colors.blue} />
                    </View>
                    <View style={styles.helpTextBox}>
                        <Text style={styles.helpText}>
                            Dúvidas no acesso? Veja o passo a passo.
                        </Text>
                        <Pressable
                            accessible
                            accessibilityRole="link"
                            accessibilityLabel="Abrir o manual de ajuda"
                        >
                            <Text style={styles.helpLink}>Acessar manual.</Text>
                        </Pressable>
                    </View>
                </View>

                <Text style={styles.label}>E-mail</Text>
                <TextInput
                    style={styles.input}
                    placeholder="Digite seu e-mail"
                    placeholderTextColor={colors.placeholder}
                    keyboardType="email-address"
                    autoCapitalize="none"
                    value={login}
                    onChangeText={setLogin}
                    accessible
                    accessibilityLabel="Digite seu e-mail"
                />

                <View style={styles.passwordRow}>
                    <Text style={styles.label}>Senha</Text>
                    <Pressable
                        accessible
                        accessibilityRole="link"
                        accessibilityLabel="Recuperar minha senha"
                    >
                        <Text style={styles.forgot}>Esqueci minha senha</Text>
                    </Pressable>
                </View>
                <TextInput
                    style={styles.input}
                    placeholder="Digite sua senha"
                    placeholderTextColor={colors.placeholder}
                    secureTextEntry
                    value={password}
                    onChangeText={setPassword}
                    accessible
                    accessibilityLabel="Digite sua senha"
                />

                <Pressable
                    style={styles.primaryButton}
                    accessible
                    accessibilityRole="button"
                    accessibilityLabel="Entrar com a sua digital"
                >
                    <Text style={styles.primaryText}>Entrar com biometria</Text>
                    <Ionicons name="finger-print" size={22} color={colors.orangeLight} />
                </Pressable>

                <Pressable
                    style={styles.outlineBlue}
                    onPress={handleLogin}
                    accessible
                    accessibilityRole="button"
                    accessibilityLabel="Entrar no aplicativo"
                >
                    <Text style={styles.outlineBlueText}>Entrar</Text>
                </Pressable>

                <Text style={styles.register}>Ainda não é cadastrado?</Text>
                <Pressable
                    style={styles.outlineOrange}
                    onPress={() => navigation.navigate('SignUp')}
                    accessible
                    accessibilityRole="button"
                    accessibilityLabel="Fazer o cadastro"
                >
                    <Text style={styles.outlineOrangeText}>Fazer cadastro</Text>
                </Pressable>
            </ScrollView>
        </KeyboardAvoidingView>
    );
}
```

---
