package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HtmlView implements View {
    private final String filePath = "./4.JavaCollections/src/" + this.getClass().getPackage().getName().replaceAll("[.]", "/") + "/vacancies.html";
    private Controller controller;

    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            String content = getUpdatedFileContent(vacancies);
            updateFile(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Odessa");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        try {
            Document document = getDocument();
            Elements elements = document.getElementsByClass("template");
            Element elementsTemp = elements.clone().removeAttr("style").removeClass("template").get(0);

            Elements prevVacancies = document.getElementsByClass("vacancy");

            for (Element element : prevVacancies) {
                if (!element.hasClass("template")) {
                    element.remove();
                }
            }

            for (Vacancy vacancy : vacancies) {
                Element elementVacancy = elementsTemp.clone();

                Element vacancyLink = elementVacancy.getElementsByAttribute("href").get(0);
                vacancyLink.appendText(vacancy.getTitle());

                vacancyLink.attr("href", vacancy.getUrl());

                Element city = elementVacancy.getElementsByClass("city").get(0);
                city.appendText(vacancy.getCity());

                Element company = elementVacancy.getElementsByClass("companyName").get(0);
                company.appendText(vacancy.getCompanyName());

                Element salary = elementVacancy.getElementsByClass("salary").get(0);
                salary.appendText(vacancy.getSalary());

                elements.before(elementVacancy.outerHtml());

            }
            return document.html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Some exception occurred";
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), "UTF-8");
    }

    private void updateFile(String s) {
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
