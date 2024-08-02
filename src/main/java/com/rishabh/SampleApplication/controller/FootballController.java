package com.rishabh.SampleApplication.controller;

import com.rishabh.SampleApplication.model.dto.FootballDetailsDto;
import com.rishabh.SampleApplication.service.FootballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class FootballController {

    @Autowired
    private FootballService footballService;

    @GetMapping("/football/matches/{year}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<FootballDetailsDto.FootballDetail> getMatches(@PathVariable("year") Integer year) throws ExecutionException, InterruptedException {
        return footballService.getMatchesByYear(year);
    }

    @GetMapping("/open/data")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public String openEndpoint() {
        return "Open data";
    }

    @GetMapping("/jwt")
    public String getJwtDetails() {
        // Extract and return JWT details
        return "JWT Details";
    }
}
