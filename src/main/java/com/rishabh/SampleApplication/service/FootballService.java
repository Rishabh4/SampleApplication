package com.rishabh.SampleApplication.service;

import com.rishabh.SampleApplication.util.Constants;
import com.rishabh.SampleApplication.model.dto.FootballDetailsDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FootballService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService executorService = Executors.newFixedThreadPool(Constants.LOCKS);

    public List<FootballDetailsDto.FootballDetail> getMatchesByYear(Integer year) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        int totalRequests = getTotalRequests(year);

        if(totalRequests > 0) {
            List<CompletableFuture<List<FootballDetailsDto.FootballDetail>>> futureList = new ArrayList<>();
            Semaphore semaphore = new Semaphore(Constants.LOCKS); // Control concurrency with a Semaphore

            for (int i = 1; i <= totalRequests; i++) {
                semaphore.acquire();
                int finalI = i;
                CompletableFuture<List<FootballDetailsDto.FootballDetail>> future = CompletableFuture.supplyAsync(() ->
                {
                    try {
                        ResponseEntity<FootballDetailsDto> result =
                                restTemplate.getForEntity(Constants.MATCHES_URI + year + "&page=" + finalI, FootballDetailsDto.class);
                        return result != null && result.getBody() != null ? result.getBody().getData() : new ArrayList<>();
                    } finally {
                        semaphore.release(); // Release the permit after the task completes
                    }
                }, executorService);
                futureList.add(future);
            }
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
            allFutures.join(); // Wait for all futures to complete

            List<FootballDetailsDto.FootballDetail> resultList = futureList.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            long endTime = System.currentTimeMillis();
            double duration = (endTime - startTime) / 1000D;
            log.info("Transaction completed in {} seconds", duration);
            return resultList;
        }
        return new ArrayList<>();
    }

    private int getTotalRequests(Integer year) {
        ResponseEntity<FootballDetailsDto> result =
                restTemplate.getForEntity(Constants.MATCHES_URI + year + "&page=1", FootballDetailsDto.class);
        return result != null && result.getBody() != null ? result.getBody().getTotal_pages() : 0;
    }
}