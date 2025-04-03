package com.bexos.fourth_service.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app")
public class PropertyService {
    private String name;
    private String version;
    private boolean production;
    private MetaData metaData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    private static class MetaData{
        private LocalDate release_date;
        private String author;
        private List<String> tags;
        private List<Map<String, String>> dependencies;

        public LocalDate getRelease_date() {
            return release_date;
        }

        public void setRelease_date(LocalDate release_date) {
            this.release_date = release_date;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<Map<String, String>> getDependencies() {
            return dependencies;
        }

        public void setDependencies(List<Map<String, String>> dependencies) {
            this.dependencies = dependencies;
        }
    }
}
