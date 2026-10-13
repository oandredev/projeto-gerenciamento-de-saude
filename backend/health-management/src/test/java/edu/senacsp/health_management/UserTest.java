// NECESSÁRIA IMPORTAÇÃO DE BIBLIOTECAS JUNIT E HAMCREST


package edu.senacsp.health_management;

import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.dto.request.user.SignupRequest;
import edu.senacsp.health_management.entity.User;
import edu.senacsp.health_management.repository.UserRepository;
import edu.senacsp.health_management.service.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User usuarioTeste;
    private List<User> usuariosDeTeste;

    @Before
    public void setUp() {
        // Dois usuários teste com nome, email e senha válidas
        SignupRequest usuario01 = new SignupRequest(
                "João Silva",
                "joao.silva@email.com",
                "Senha@123"
        );
        SignupRequest usuario02 = new SignupRequest(
                "Maria Souza",
                "maria.souza@email.com",
                "Senha@456"
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

    @After
    public void tearDown() {
        // Limpar o banco de dados usado no teste
        userRepository.deleteAll(usuariosDeTeste);
    }

    @Test
    public void novosUsuarios() {
        // Verificar se novos usuários com dados completos estarão cadastrados no banco.

    }

    @Test
    public void usuarioJaCadastrado() {
        // Verificar se o novo cadastro de um usuário já presente no banco de dados retorna exceção.
    }

    @Test
    public void emailInvalido() {
        // Verificar se o sistema permite a inserção de texto sem '@' como campo de email.
    }

    @Test
    public void login() {
        // Verificar o acesso do sistema através de email e senha registrados no banco.
    }

    @Test
    public void loginEmailInvalido() {
        // Verificar se a tentativa de acesso com e-mail não cadastrado no banco retorna exceção.
    }

    @Test
    public void loginSenhaInvalida() {
        // Verificar se a tentativa de acesso com e-mail cadastrado, mas com senha inválida, retorna exceção.
    }

    @Test
    public void nomeAlterado() {
        // Verifica a continuidade de andamento do sistema com nome de cadastro alterado.
    }

    @Test
    public void emailAlterado() {
        // Verifica a continuidade de andamento do sistema com email de cadastro alterado.
    }

    @Test
    public void senhaAlterada() {
        // Verifica a continuidade de andamento do sistema com senha de cadastro alterada.
    }

}
