package br.com.matias.todolist.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErroResponse {

    private String erro;

    public ErroResponse(String erro) {
        this.erro = erro;
    }
}
