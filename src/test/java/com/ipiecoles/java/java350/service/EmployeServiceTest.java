package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    @Test
    public void testEmbaucheEmployeCommercialPleinTempsBTS() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        Poste poste = Poste.COMMERCIAL;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //findLastMatricule = 00345
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("00345");
        //findByMatricule = null
        Mockito.when(employeRepository.findByMatricule("C00346")).thenReturn(null);
        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        //Then
        //vérifier si l'employé est bien créer en DB (avec tous ses paramètres)
        //Initialisation des capteurs d'arguments
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getMatricule()).isEqualTo("C00346");
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
        Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        //1521.22 * 1.2 * 1.0
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
    }

    @Test
    public void testEmbaucheEmployeLimiteMatricule() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        Poste poste = Poste.COMMERCIAL;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //findLastMatricule = 99999
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");

        Assertions.assertThatThrownBy(() -> {employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);})
                .isInstanceOf(EmployeException.class).hasMessage("Limite des 100000 matricules atteinte !");
/*
        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("Aurait dû planter");
        }
        catch (Exception e){
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        }

        //Then
*/
    }

    @Test
    public void testPerformanceCommercialCANull() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = "C12345";
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 10;
        Double tempsPartiel = 1.0;

        Long caTraite = null;
        Long objectifCa = 2000000L;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        //When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("L'employé n'aurait pas dû être enregistré");
        }
        catch(EmployeException e){
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le chiffre d'affaire traité ne peut être négatif ou null !");
        }
        //Then
    }

    @Test
    public void testPerformanceCommercialObjectifNull() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = "C12345";
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 10;
        Double tempsPartiel = 1.0;

        Long caTraite = 1000000L;
        Long objectifCa = null;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        //When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("L'employé n'aurait pas dû être enregistré");
        }
        catch(EmployeException e){
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
        }
        //Then
    }

    @Test
    public void testPerformanceCommercialMatriculeNull() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = null;
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 10;
        Double tempsPartiel = 1.0;

        Long caTraite = 1000000L;
        Long objectifCa = 2000000L;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        //When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("L'employé n'aurait pas dû être enregistré");
        }
        catch(EmployeException e){
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule ne peut être null et doit commencer par un C !");
        }
        //Then
    }

    @Test
    public void testPerformanceCommercialMatriculeInexistant() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = "C";
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 10;
        Double tempsPartiel = 1.0;

        Long caTraite = 1000000L;
        Long objectifCa = 2000000L;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        //When
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("L'employé n'aurait pas dû être enregistré");
        }
        catch(EmployeException e){
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Le matricule " + matricule + " n'existe pas !");
        }
        //Then
    }
    @Test
    public void testPerformanceCommercialEpicFail() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = "C12345";
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 10;
        Double tempsPartiel = 1.0;

        Long caTraite = 10L;
        Long objectifCa = 2000000L;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(6.0);

        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(1);

    }

    @Test
    public void testPerformanceCommercialSoClose() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = "C12345";
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 4;
        Double tempsPartiel = 1.0;

        Long caTraite = 900000L;
        Long objectifCa = 1000000L;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(6.0);

        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(2);

    }
    @Test
    public void testPerformanceCommercialSucces() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = "C12345";
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 2;
        Double tempsPartiel = 1.0;

        Long caTraite = 2000000L;
        Long objectifCa = 2000000L;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(6.0);

        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(2);

    }

    @Test
    public void testPerformanceCommercialPlus20() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = "C12345";
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 2;
        Double tempsPartiel = 1.0;

        Long caTraite = 1100000L;
        Long objectifCa = 1000000L;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(6.0);

        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(3);

    }

    @Test
    public void testPerformanceCommercialAwesome() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = "C12345";
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 2;
        Double tempsPartiel = 1.0;

        Long caTraite = 1300000L;
        Long objectifCa = 1000000L;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(6.0);

        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(6);

    }

    @Test
    public void testPerformanceCommercialEpicVictory() throws EmployeException {
        //Given
        String nom = "Das";
        String prenom = "Ju";
        String matricule = "C12345";
        LocalDate dateEmbauche = LocalDate.now();
        Double salaire = 1000.0;
        Integer performance = 3;
        Double tempsPartiel = 1.0;

        Long caTraite = 1300000L;
        Long objectifCa = 1000000L;

        Employe employe = new Employe(nom, prenom, matricule, dateEmbauche, salaire, performance, tempsPartiel);

        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(6.0);

        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(8);

    }

}
