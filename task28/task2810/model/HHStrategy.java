package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";


    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        int page = 0;
        try {
            while (true) {
                Document document = getDocument(searchString, page);
                Elements elements = document.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
                if (elements.isEmpty()) break;
                for (Element e : elements) {
                    Elements link = e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title");
                    Elements salary = e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation");
                    Elements location = e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address");
                    Elements company = e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer");

                    Vacancy vacancy = new Vacancy();
                    vacancy.setCity(location.get(0).text());
                    vacancy.setCompanyName(company.get(0).text());
                    vacancy.setSalary(salary.size() > 0 ? salary.get(0).text() : "");
                    vacancy.setTitle(link.get(0).text());
                    vacancy.setSiteName("hh.ua");
                    vacancy.setUrl(link.get(0).attr("href"));
                    vacancies.add(vacancy);
                }
                page++;
            }
        } catch (IOException e) {

        }
        return vacancies.size() > 0 ? vacancies : new ArrayList<>();
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString, page))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36")
                .referrer("http://hh.ru/")
                .get();
    }
}
