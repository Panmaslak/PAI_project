package com.example.application.view;

import com.example.application.model.Subject;
import com.example.application.repository.SubjectRepository;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Kalkulator ECTS")
public class ApplicationView extends HorizontalLayout {

    SubjectRepository subjectRepository;
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    VerticalLayout verticalLayout = new VerticalLayout();
    HorizontalLayout comboBoxLayout = new HorizontalLayout();
    Html leftSection = new Html("<div>" +
            "<span style=\"white-space: nowrap;\">Łączna liczba twoich punktów</span><br/>" +
            "<span>Twoje przedmioty:</span></div>");

    Html rightSection = new Html("<div>Oblicz punkty ECTS</div>");
    Html resultsSection = new Html("<div>Punkty ECTS zdobyte w tym semestrze:<br>" +
            "Liczba puntów ECTS stracona w tym semetrze:<br>Aktualny dług punktowy:<br>" +
            "Łączna kwota do zapłacenia za warunki:</div>");
    Grid<Subject> subjectGrid = new Grid<>(Subject.class);

    private ComboBox<String> semesterComboBox = new ComboBox<>();;
    private ComboBox<String> subjectsComboBox = new ComboBox<>();
    Button calculateButton = new Button("Oblicz");


    public ApplicationView(SubjectRepository subjectRepository){
        this.subjectRepository = subjectRepository;

        configureGrid();
        configureComboBoxes();
        configureLayouts();
        configureStyles();
        add(horizontalLayout, verticalLayout);
    }

    private void configureGrid() {
        List<Subject> subjects = subjectRepository.findAll();
        //List<String> columnOrder = Arrays.asList("name", "ECTSPoints");
        subjectGrid.setItems(subjects);
        subjectGrid.removeColumnByKey("id");
        subjectGrid.getColumnByKey("name").setHeader("Nazwa");
        subjectGrid.getColumnByKey("ECTSPoints").setHeader("Punkty ECTS");
    }

    private void configureComboBoxes() {
        List<String> subjects = subjectRepository.findAll().stream().map(Subject::getName).collect(Collectors.toList());
        semesterComboBox.setLabel("Semestr");
        semesterComboBox.setItems("I", "II", "II", "IV", "V", "VI", "VII");
        subjectsComboBox.setLabel("Nie zaliczone przedmioty");
        subjectsComboBox.setItems(subjects);
    }

    private void configureLayouts() {
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.BASELINE);
        verticalLayout.expand(rightSection);
        verticalLayout.setHorizontalComponentAlignment(Alignment.BASELINE, rightSection);

        horizontalLayout.setJustifyContentMode(JustifyContentMode.END);
        horizontalLayout.add(leftSection);
        comboBoxLayout.add(semesterComboBox, subjectsComboBox);
        verticalLayout.add(rightSection, comboBoxLayout, calculateButton, resultsSection, subjectGrid);
    }

    private void configureStyles(){
        leftSection.getStyle().set("margin-left", "300px");
        leftSection.getStyle().set("font-size", "20px");

        rightSection.getStyle().set("margin-left", "300px");
        rightSection.getStyle().set("font-size", "20px");

        comboBoxLayout.getStyle().set("margin-left", "300px");
        calculateButton.getStyle().set("margin-left", "300px");

        resultsSection.getStyle().set("margin-left", "300px");
        resultsSection.getStyle().set("font-size", "20px");

        subjectGrid.getStyle().set("margin-right", "100px");
        subjectGrid.getStyle().set("position", "relative");
        subjectGrid.getStyle().set("right", "560px");
        subjectGrid.getStyle().set("top", "-250px");
    }



}



