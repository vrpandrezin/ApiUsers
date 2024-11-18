package br.com.matias.todolist.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusResponse {
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object message;
}
