package br.com.matias.todolist.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
    private String status;
    private Object message;
}
