package com.nagarro.java_mini_assignment_2.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsResponse {
    private List<UserDetails> results;
    private Object info;
    public Object getInfo() {
        return info;
    }
    public void setInfo(Object info) {
        this.info = info;
    }
    public List<UserDetails> getResults() {
        return results;
    }
    public void setResults(List<UserDetails> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "UserDetailsResponse{" +
                "results=" + results.get(0).getName() +
                '}';
    }
}
