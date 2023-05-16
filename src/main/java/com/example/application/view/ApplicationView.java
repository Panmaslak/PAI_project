package com.example.application.view;

import com.example.application.model.Subject;
import com.example.application.repository.SubjectRepository;
import com.vaadin.flow.component.Html;
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
import org.springframework.context.annotation.Scope;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
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
    Html leftSection =  new Html("<div>" +
            "<span style=\"white-space: nowrap;\">Łączna liczba punktów: </span>" + 120 +
            "<br><span>Twoje przedmioty:</span></div>");
    Html rightSection  = new Html("<div>Oblicz punkty ECTS</div>");
    Grid<Subject> subjectGrid = new Grid<>(Subject.class);
    private ComboBox<String> semesterComboBox = new ComboBox<>();;
    private MultiSelectComboBox<String> subjectsComboBox = new MultiSelectComboBox<>();
    Label selectedOptionsLabel = new Label("");
    Label missingECTSLabel = new Label("");
    Label gainedECTSLabel = new Label("");
    Label costLabel = new Label("");
    private Set<String> selectedOptions;
    AtomicReference<Integer> ECTSPointsFromSubjectsCombobox = new AtomicReference<>(0);
    Set<String> selectedSubjectNames = new HashSet<>();


    public ApplicationView(SubjectRepository subjectRepository){
        this.subjectRepository = subjectRepository;

        configureGrid();
        configureComboBoxes();
        configureLayouts();
        configureStyles();
        add(horizontalLayout, verticalLayout, selectedOptionsLabel, missingECTSLabel, gainedECTSLabel, costLabel);
    }

    private void configureGrid() {
        List<Subject> subjects = subjectRepository.findAll();
        subjectGrid.setItems(subjects);
        subjectGrid.removeColumnByKey("id");
        subjectGrid.getColumnByKey("name").setHeader("Nazwa");
        subjectGrid.getColumnByKey("ECTSPoints").setHeader("Punkty ECTS");
        subjectGrid.getColumnByKey("semester").setHeader("Semestr");
    }

    public void configureComboBoxes() {
        List<String> subjects = subjectRepository.findAll().stream().map(Subject::getName).collect(Collectors.toList());
        semesterComboBox.setLabel("Semestr");
        semesterComboBox.setItems("1", "2", "3", "4", "5", "6", "7");
        subjectsComboBox.setLabel("Nie zaliczone przedmioty");
        subjectsComboBox.setItems(subjects);

        subjectsComboBox.addValueChangeListener(event -> {
            selectedOptions = event.getValue();
            if (selectedOptions != null && !selectedOptions.isEmpty()) {
                updateSelectedSubjectNames();
                updateLabels();
            } else {
                updateSelectedSubjectNames();
                updateLabels();
            }
        });

        semesterComboBox.addValueChangeListener(event -> {
            String selectedSemester = event.getValue();
            List<String> subjectsForSemester = getSubjectsForSemester(selectedSemester);
            subjectsComboBox.setItems(subjectsForSemester);
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

    private void updateLabels(){
        ECTSPointsFromSubjectsCombobox.set(selectedSubjectNames.stream()
                .map(subjectName -> subjectRepository.findByName(subjectName).getECTSPoints())
                .reduce(0, Integer::sum));

        int cost = selectedSubjectNames.stream()
                .filter(name -> name.endsWith("(Laboratorium)") || name.endsWith("(Lektorat)") ||
                        name.endsWith("(Praktyka zawodowa)") || name.endsWith("(Projekt)") || name.endsWith("(Ćwiczenia)"))
                .mapToInt(name -> 650)
                .sum();

        selectedOptionsLabel.setText("Wybrane przedmioty: " + selectedOptions);

        gainedECTSLabel.setText("Punkty ECTS zdobyte w tym semestrze: " + (30 - ECTSPointsFromSubjectsCombobox.get()));

        missingECTSLabel.setText("Punkty ECTS utracone w tym semestrze: " + ECTSPointsFromSubjectsCombobox.get());

        costLabel.setText("Kwota do zapłacenia za warunki: " + cost);
    }

    private void updateSelectedSubjectNames() {
        selectedSubjectNames.clear();
        selectedOptions.forEach(name -> {
            selectedSubjectNames.add(name);
        });
    }


    private void configureLayouts() {
        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.BASELINE);
        verticalLayout.expand(rightSection);
        verticalLayout.setHorizontalComponentAlignment(Alignment.BASELINE, rightSection);

        horizontalLayout.setJustifyContentMode(JustifyContentMode.END);
        horizontalLayout.add(leftSection);
        comboBoxLayout.add(semesterComboBox, subjectsComboBox);
        verticalLayout.add(comboBoxLayout, subjectGrid);
    }

    private void configureStyles(){
        leftSection.getStyle().set("margin-left", "300px");
        leftSection.getStyle().set("position", "relative").set("right", "100px");
        leftSection.getStyle().set("font-size", "20px");

        rightSection.getStyle().set("margin-left", "300px");
        rightSection.getStyle().set("font-size", "20px");

        comboBoxLayout.getStyle().set("margin-left", "300px");

        selectedOptionsLabel.getStyle().set("margin-right", "300px");
        selectedOptionsLabel.getStyle().set("position", "relative").set("top", "240px").set("right", "420px");
        selectedOptionsLabel.setWidth("auto");

        missingECTSLabel.getStyle().set("position", "relative").set("top", "240px").set("right", "700px");
        missingECTSLabel.setWidth("auto");

        gainedECTSLabel.getStyle().set("position", "relative").set("top", "240px").set("right", "650px");
        gainedECTSLabel.setWidth("auto");

        costLabel.getStyle().set("position", "relative").set("top", "240px").set("right", "600px");
        costLabel.setWidth("auto");

        subjectGrid.getStyle().set("position", "relative").set("right", "500px").set("top", "-25px");


    }

}



