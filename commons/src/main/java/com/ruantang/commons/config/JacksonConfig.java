package com.ruantang.commons.config;

import com.fasterxml.jackson.core.JsonGenerator;
   import com.fasterxml.jackson.databind.JsonSerializer;
   import com.fasterxml.jackson.databind.ObjectMapper;
   import com.fasterxml.jackson.databind.SerializerProvider;
   import com.fasterxml.jackson.databind.module.SimpleModule;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;

   import java.io.IOException;

   @Configuration
   public class JacksonConfig {

       @Bean
       public ObjectMapper objectMapper() {
           ObjectMapper mapper = new ObjectMapper();
           SimpleModule module = new SimpleModule();
           module.addSerializer(Long.class, new JsonSerializer<Long>() {
               @Override
               public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                   gen.writeString(value.toString());
               }
           });
           mapper.registerModule(module);
           return mapper;
       }
   }
   