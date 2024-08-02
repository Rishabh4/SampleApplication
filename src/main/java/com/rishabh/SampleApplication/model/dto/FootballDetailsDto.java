package com.rishabh.SampleApplication.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FootballDetailsDto {
    private Integer page;
    private Integer per_page;
    private Integer total;
    private Integer total_pages;
    private List<FootballDetail> data;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPer_page() {
        return per_page;
    }

    public void setPer_page(Integer per_page) {
        this.per_page = per_page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public List<FootballDetail> getData() {
        return data;
    }

    public void setData(List<FootballDetail> data) {
        this.data = data;
    }

    public static class FootballDetail {
        private String competition;
        private Integer year;
        private String team1;
        private String team2;
        private String team1goals;
        private String team2goals;

        public String getCompetition() {
            return competition;
        }

        public void setCompetition(String competition) {
            this.competition = competition;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public String getTeam1() {
            return team1;
        }

        public void setTeam1(String team1) {
            this.team1 = team1;
        }

        public String getTeam2() {
            return team2;
        }

        public void setTeam2(String team2) {
            this.team2 = team2;
        }

        public String getTeam1goals() {
            return team1goals;
        }

        public void setTeam1goals(String team1goals) {
            this.team1goals = team1goals;
        }

        public String getTeam2goals() {
            return team2goals;
        }

        public void setTeam2goals(String team2goals) {
            this.team2goals = team2goals;
        }
    }

}
