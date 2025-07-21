package com.RoomyExpense.tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Roomy Expense Tracker")
                .version("1.0.00")
                .description("Una app para administrar y dividir los gastos entre personas que comparten un inmueble.")
            );
    }
}
