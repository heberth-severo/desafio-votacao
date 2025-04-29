package com.hvas.votacao.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CpfValidationService {

    @Value("${user.info.url}")
    private String userInfoUrl;

    public boolean podeVotar(String cpf) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = userInfoUrl + "/users/" + cpf;
            CpfValidationResponse response = restTemplate.getForObject(url, CpfValidationResponse.class);
            return response != null && "ABLE_TO_VOTE".equals(response.getStatus());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                return false;
            }
            throw e;
        }
    }

    @Getter
    @Setter
    public static class CpfValidationResponse {
        private String status;
    }
}

