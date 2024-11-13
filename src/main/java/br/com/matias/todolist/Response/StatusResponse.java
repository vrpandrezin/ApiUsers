package br.com.matias.todolist.Response;

import lombok.Data;

@Data
public class StatusResponse {

    private String status;

    public StatusResponse(String status) {
        this.status = status;
    }
}
