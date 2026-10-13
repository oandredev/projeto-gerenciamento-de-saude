// NECESSÁRIA IMPORTAÇÃO DE BIBLIOTECAS JUNIT E HAMCREST


package edu.senacsp.health_management;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ProfileTest {

    @Test
    public void novosPerfis() {
        // Verificar criação de novos perfis no mesmo usuário.
    }

    @Test
    public void usuarioNaoExistente() {
        // Verificar se a tentativa de atrelação de um perfil a usuário não existente retorna exceção.
    }

    @Test
    public void nomeRepetido() {
        // Verificar se a criação de um novo perfil tem um nome igual ao de outro perfil no mesmo usuário.
    }

    @Test
    public void listaPerfis() {
        // Verificar se os perfis criados em um mesmo usuário estão atribuídos ao mesmo.
    }

    @Test
    public void listaPerfisSemUsuario() {
        // Verificar se a requisição de uma lista de perfis em usuário não existente retorna exceção.
    }

    @Test
    public void perfilEditado() {
        // Verificar se novos parâmetros de perfil estão atrelados ao perfil editado.
    }

    @Test
    public void perfilNaoExistenteEditado() {
        // Verificar se a tentativa de edição de um perfil não existente retorna exceção.
    }

    @Test
    public void perfilEditadoUsuarioErrado() {
        // Verificar se a falta de vínculo entre perfil editado e usuário retorna exceção.
    }

}
