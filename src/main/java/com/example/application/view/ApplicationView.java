package com.example.application.view;

import com.example.application.model.Subject;
import com.example.application.repository.SubjectRepository;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
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
    private MultiSelectComboBox<String> subjectsComboBox = new MultiSelectComboBox<>();
    Label selectedOptionsLabel;
    Button calculateButton = new Button("Oblicz");

    private Set<String> selectedOptions;


    public ApplicationView(SubjectRepository subjectRepository){
        this.subjectRepository = subjectRepository;

        configureGrid();
        configureComboBoxes();
        configureLayouts();
        configureStyles();
        configureCalculateButton();
        resultsSection.setVisible(false);
        add(horizontalLayout, verticalLayout, selectedOptionsLabel);
    }

    private void configureGrid() {
        List<Subject> subjects = subjectRepository.findAll();
        subjectGrid.setItems(subjects);
        subjectGrid.removeColumnByKey("id");
        subjectGrid.getColumnByKey("name").setHeader("Nazwa");
        subjectGrid.getColumnByKey("ECTSPoints").setHeader("Punkty ECTS");
    }

    private void configureComboBoxes() {
        List<String> subjects = subjectRepository.findAll().stream().map(Subject::getName).collect(Collectors.toList());
        semesterComboBox.setLabel("Semestr");
        semesterComboBox.setItems("1", "2", "3", "4", "5", "6", "7");
        subjectsComboBox.setLabel("Nie zaliczone przedmioty");
        subjectsComboBox.setItems(subjects);

        semesterComboBox.addValueChangeListener(event -> {
            String selectedSemester = event.getValue();
            List<String> subjectsForSemester = getSubjectsForSemester(selectedSemester);
            subjectsComboBox.setItems(subjectsForSemester);
        });

        subjectsComboBox.addValueChangeListener(event -> {
            selectedOptions = event.getValue();
            if(selectedOptions != null && !selectedOptions.isEmpty()) {
                String optionsText = String.join(", ", selectedOptions);
                String labelText = "Wybrane przedmioty:\n" + optionsText;
                selectedOptionsLabel.setText(labelText);
            }else {
                selectedOptionsLabel.setText("");
            }
        });
        selectedOptionsLabel = new Label();
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
        leftSection.getStyle().set("position", "relative").set("right", "100px");
        leftSection.getStyle().set("font-size", "20px");

        rightSection.getStyle().set("margin-left", "300px");
        rightSection.getStyle().set("font-size", "20px");

        comboBoxLayout.getStyle().set("margin-left", "300px");

        calculateButton.getStyle().set("margin-left", "300px");
        calculateButton.getStyle().set("margin-bottom", "60px");

        resultsSection.getStyle().set("margin-left", "300px");
        resultsSection.getStyle().set("margin-bottom", "60px");
        resultsSection.getStyle().set("font-size", "20px");

        selectedOptionsLabel.getStyle().set("margin-right", "300px");
        selectedOptionsLabel.getStyle().set("position", "relative").set("top", "180px").set("right", "150px");

        subjectGrid.getStyle().set("position", "relative").set("right", "550px").set("top", "-200px");

        selectedOptionsLabel.setWidth("1500px");
        selectedOptionsLabel.setHeight("100px");
    }

    private void configureCalculateButton(){
        calculateButton.addClickListener(e -> {
            resultsSection.setVisible(true);

            List <Integer> MissingECTSPoints = subjectRepository.findAll().stream().map(Subject::getECTSPoints).collect(Collectors.toList());



        });
    }
    private List<String> getSubjectsForSemester(String selectedSemester) {
        List<Subject> allSubjects = subjectRepository.findAll();
        List<String> subjectsForSemester = allSubjects.stream()
                .filter(subject -> subject.getSemester().equals(selectedSemester))
                .map(Subject::getName)
                .collect(Collectors.toList());
        return subjectsForSemester;
    }








}



