package com.cock.cocktail.infrastructure.config_based;

import com.cock.cocktail.domain.DescriptorRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * YAML 설정 파일 기반 Descriptor 로더
 */
@Configuration
public class ConfigBasedDescriptorLoader {

    @Bean
    public DescriptorRegistry descriptorRegistry() {
        try {
            var yamlMapper = new ObjectMapper(new YAMLFactory());
            var keywordsResource = new ClassPathResource("keywords.yml");
            return yamlMapper.readValue(keywordsResource.getInputStream(), DescriptorRegistry.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load descriptor configuration", e);
        }
    }
}
