package tn.esprit.autoloc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ServiceTests {

    @Autowired
    private IAgenceService agenceService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IEquipementService equipementService;

    @Autowired
    private IReservationService reservationService;

    @Autowired
    private IContratService contratService;

    @Autowired
    private IPaiementService paiementService;

    @Autowired
    private IMaintenanceService maintenanceService;

    @Test
    void testAgenceService() {
        int nombreInitial = agenceService.findAll().size();

        Agence agence = new Agence();
        agence.setNom("Agence Test");
        agence.setVille("Tunis");
        agence.setAdresse("10 avenue de la République");
        agence.setTelephone("71000000");

        Agence creee = agenceService.create(agence);
        Long id = creee.getId();

        assertNotNull(id);
        System.out.println("Agence créée : " + id);

        var agences = agenceService.findAll();

        agences.forEach(a ->
                System.out.println(
                        "Agence : " + a.getId() + " - " + a.getNom()));

        assertEquals(nombreInitial + 1, agences.size());
        System.out.println("Nombre d'agences : " + agences.size());

        Agence trouvee = agenceService.findById(id);
        assertEquals("Agence Test", trouvee.getNom());

        Agence modification = new Agence();
        modification.setNom("Agence Modifiée");
        modification.setVille("Sousse");
        modification.setAdresse("20 avenue Habib Bourguiba");
        modification.setTelephone("73000000");

        agenceService.update(id, modification);

        Agence modifiee = agenceService.findById(id);
        assertEquals("Agence Modifiée", modifiee.getNom());
        assertEquals("Sousse", modifiee.getVille());

        System.out.println("Agence modifiée : " + modifiee.getNom());

        agenceService.deleteById(id);

        assertThrows(ResourceNotFoundException.class,
                () -> agenceService.findById(id));

        assertEquals(nombreInitial, agenceService.findAll().size());

        System.out.println("Suppression agence vérifiée");
    }

    @Test
    void testClientService() {
        int nombreInitial = clientService.findAll().size();

        Client premier = new Client();
        premier.setNom("Client Test Un");
        premier.setPrenom("Emna");
        premier.setEmail("client1@example.com");
        premier.setTelephone("20000001");
        premier.setNumPermis("TEST-P001");
        premier.setDateInscription(LocalDate.now());

        Client second = new Client();
        second.setNom("Client Test Deux");
        second.setPrenom("Amine");
        second.setEmail("client2@example.com");
        second.setTelephone("20000002");
        second.setNumPermis("TEST-P002");
        second.setDateInscription(LocalDate.now());

        Long id1 = clientService.create(premier).getId();
        Long id2 = clientService.create(second).getId();

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);

        System.out.println("Clients créés : " + id1 + " et " + id2);

        var clients = clientService.findAll();

        clients.forEach(c ->
                System.out.println(
                        "Client : " + c.getId() + " - " + c.getNom()));

        assertEquals(nombreInitial + 2, clients.size());
        System.out.println("Nombre de clients : " + clients.size());

        Client trouve = clientService.findById(id1);
        assertEquals("client1@example.com", trouve.getEmail());

        Client modification = new Client();
        modification.setNom("Client Modifié");
        modification.setPrenom("Emna");
        modification.setEmail("client.modifie@example.com");
        modification.setTelephone("20000003");
        modification.setNumPermis("TEST-P001");
        modification.setDateInscription(premier.getDateInscription());

        clientService.update(id1, modification);

        Client modifie = clientService.findById(id1);
        assertEquals("Client Modifié", modifie.getNom());
        assertEquals("client.modifie@example.com", modifie.getEmail());

        System.out.println("Client modifié : " + modifie.getNom());

        clientService.deleteById(id1);

        assertThrows(ResourceNotFoundException.class,
                () -> clientService.findById(id1));

        assertEquals(id2, clientService.findById(id2).getId());
        assertEquals(nombreInitial + 1, clientService.findAll().size());

        System.out.println("Premier client supprimé, second conservé");
    }

    @Test
    void testVehiculeService() {
        int nombreInitial = vehiculeService.findAll().size();

        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("TEST-AT4-001");
        vehicule.setMarque("Toyota");
        vehicule.setModele("Yaris");
        vehicule.setCategorie(CategorieVehicule.values()[0]);
        vehicule.setStatut(StatutVehicule.values()[0]);
        vehicule.setTarifJournalier(new BigDecimal("100.00"));

        Long id = vehiculeService.create(vehicule).getIdVehicule();

        assertNotNull(id);
        System.out.println("Véhicule créé : " + id);

        var vehicules = vehiculeService.findAll();

        vehicules.forEach(v ->
                System.out.println(
                        "Véhicule : " + v.getIdVehicule()
                                + " - " + v.getImmatriculation()));

        assertEquals(nombreInitial + 1, vehicules.size());
        System.out.println("Nombre de véhicules : " + vehicules.size());

        Vehicule trouve = vehiculeService.findById(id);
        assertEquals("TEST-AT4-001", trouve.getImmatriculation());

        Vehicule modification = new Vehicule();
        modification.setImmatriculation("TEST-AT4-001");
        modification.setMarque("Toyota");
        modification.setModele("Yaris Hybride");
        modification.setCategorie(vehicule.getCategorie());
        modification.setStatut(vehicule.getStatut());
        modification.setTarifJournalier(new BigDecimal("120.00"));

        vehiculeService.update(id, modification);

        Vehicule modifie = vehiculeService.findById(id);
        assertEquals("Yaris Hybride", modifie.getModele());
        assertEquals(0, new BigDecimal("120.00")
                .compareTo(modifie.getTarifJournalier()));

        System.out.println("Véhicule modifié : " + modifie.getModele());

        vehiculeService.deleteById(id);

        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.findById(id));

        assertEquals(nombreInitial, vehiculeService.findAll().size());

        System.out.println("Suppression véhicule vérifiée");
    }

    @Test
    void testEmployeService() {
        int nombreInitial = employeService.findAll().size();

        Employe employe = new Employe();
        employe.setNom("Employé Test");
        employe.setPrenom("Ali");
        employe.setRole(RoleEmploye.values()[0]);

        Long id = employeService.create(employe).getId();

        assertNotNull(id);
        System.out.println("Employé créé : " + id);

        var employes = employeService.findAll();

        assertEquals(nombreInitial + 1, employes.size());
        assertTrue(employes.stream()
                .anyMatch(e -> id.equals(e.getId())));

        Employe trouve = employeService.findById(id);
        assertEquals("Employé Test", trouve.getNom());

        System.out.println("Employé retrouvé : " + trouve.getNom());
        System.out.println("Nombre d'employés : " + employes.size());

        Employe modification = new Employe();
        modification.setNom("Employé Modifié");
        modification.setPrenom("Sami");
        modification.setRole(employe.getRole());

        employeService.update(id, modification);

        Employe modifie = employeService.findById(id);
        assertEquals("Employé Modifié", modifie.getNom());
        assertEquals("Sami", modifie.getPrenom());

        System.out.println("Employé modifié : " + modifie.getNom());

        employeService.deleteById(id);

        assertThrows(ResourceNotFoundException.class,
                () -> employeService.findById(id));

        assertEquals(nombreInitial, employeService.findAll().size());

        System.out.println("Suppression employé vérifiée");
    }

    @Test
    void testEquipementService() {
        int nombreInitial = equipementService.findAll().size();

        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS Test");

        Long id = equipementService.create(equipement).getId();

        assertNotNull(id);
        System.out.println("Équipement créé : " + id);

        var equipements = equipementService.findAll();

        assertEquals(nombreInitial + 1, equipements.size());
        assertTrue(equipements.stream()
                .anyMatch(e -> id.equals(e.getId())));

        Equipement trouve = equipementService.findById(id);
        assertEquals("GPS Test", trouve.getLibelle());

        System.out.println("Équipement retrouvé : " + trouve.getLibelle());
        System.out.println("Nombre d'équipements : " + equipements.size());

        Equipement modification = new Equipement();
        modification.setLibelle("GPS Modifié");

        equipementService.update(id, modification);

        Equipement modifie = equipementService.findById(id);
        assertEquals("GPS Modifié", modifie.getLibelle());

        System.out.println("Équipement modifié : " + modifie.getLibelle());

        equipementService.deleteById(id);

        assertThrows(ResourceNotFoundException.class,
                () -> equipementService.findById(id));

        assertEquals(nombreInitial, equipementService.findAll().size());

        System.out.println("Suppression équipement vérifiée");
    }

    @Test
    void testReservationService() {
        int nombreInitial = reservationService.findAll().size();
        LocalDate debut = LocalDate.now().plusDays(10);

        Reservation reservation = new Reservation();
        reservation.setDateDebut(debut);
        reservation.setDateFin(debut.plusDays(3));
        reservation.setStatut(StatutReservation.values()[0]);

        Long id = reservationService.create(reservation).getId();

        assertNotNull(id);
        System.out.println("Réservation créée : " + id);

        var reservations = reservationService.findAll();

        assertEquals(nombreInitial + 1, reservations.size());
        assertTrue(reservations.stream()
                .anyMatch(r -> id.equals(r.getId())));

        Reservation trouvee = reservationService.findById(id);
        assertEquals(debut, trouvee.getDateDebut());

        System.out.println("Réservation retrouvée : " + trouvee.getId());
        System.out.println("Nombre de réservations : " + reservations.size());

        Reservation modification = new Reservation();
        modification.setDateDebut(debut);
        modification.setDateFin(debut.plusDays(5));
        modification.setStatut(reservation.getStatut());

        reservationService.update(id, modification);

        Reservation modifiee = reservationService.findById(id);
        assertEquals(debut.plusDays(5), modifiee.getDateFin());

        System.out.println(
                "Réservation modifiée, date de fin : "
                        + modifiee.getDateFin());

        reservationService.deleteById(id);

        assertThrows(ResourceNotFoundException.class,
                () -> reservationService.findById(id));

        assertEquals(nombreInitial, reservationService.findAll().size());

        System.out.println("Suppression réservation vérifiée");
    }

    @Test
    void testContratService() {
        int nombreInitial = contratService.findAll().size();

        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("300.00"));
        contrat.setValide(false);
        contrat.setPaiements(new ArrayList<>());

        Long id = contratService.create(contrat).getId();

        assertNotNull(id);
        System.out.println("Contrat créé : " + id);

        var contrats = contratService.findAll();

        assertEquals(nombreInitial + 1, contrats.size());
        assertTrue(contrats.stream()
                .anyMatch(c -> id.equals(c.getId())));

        Contrat trouve = contratService.findById(id);
        assertEquals(0, new BigDecimal("300.00")
                .compareTo(trouve.getMontantTotal()));
        assertFalse(trouve.isValide());

        System.out.println("Contrat retrouvé : " + trouve.getId());
        System.out.println("Nombre de contrats : " + contrats.size());

        Contrat modification = new Contrat();
        modification.setDateSignature(LocalDate.now());
        modification.setMontantTotal(new BigDecimal("350.00"));
        modification.setValide(true);

        contratService.update(id, modification);

        Contrat modifie = contratService.findById(id);
        assertEquals(0, new BigDecimal("350.00")
                .compareTo(modifie.getMontantTotal()));
        assertTrue(modifie.isValide());

        System.out.println(
                "Contrat modifié, montant : " + modifie.getMontantTotal());

        contratService.deleteById(id);

        assertThrows(ResourceNotFoundException.class,
                () -> contratService.findById(id));

        assertEquals(nombreInitial, contratService.findAll().size());

        System.out.println("Suppression contrat vérifiée");
    }

    @Test
    void testPaiementService() {
        int nombreInitial = paiementService.findAll().size();

        // Les écritures passent par le Contrat.
        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("200.00"));
        contrat.setValide(true);
        contrat.setPaiements(new ArrayList<>());

        Paiement paiement = new Paiement();
        paiement.setMontant(new BigDecimal("200.00"));
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(ModePaiement.values()[0]);

        // Renseigner les deux côtés de l'association.
        paiement.setContrat(contrat);
        contrat.getPaiements().add(paiement);

        Contrat cree = contratService.create(contrat);
        Long contratId = cree.getId();
        Long paiementId = paiement.getId();

        assertNotNull(contratId);
        assertNotNull(paiementId);

        System.out.println(
                "Paiement créé via le contrat : " + paiementId);

        var paiements = paiementService.findAll();

        assertEquals(nombreInitial + 1, paiements.size());
        assertTrue(paiements.stream()
                .anyMatch(p -> paiementId.equals(p.getId())));

        Paiement trouve = paiementService.findById(paiementId);

        assertEquals(0, new BigDecimal("200.00")
                .compareTo(trouve.getMontant()));
        assertEquals(contratId, trouve.getContrat().getId());

        System.out.println(
                "Paiement retrouvé, montant : " + trouve.getMontant());
        System.out.println("Nombre de paiements : " + paiements.size());

        // La suppression du contrat supprime son paiement par cascade.
        contratService.deleteById(contratId);

        assertThrows(ResourceNotFoundException.class,
                () -> paiementService.findById(paiementId));

        assertThrows(ResourceNotFoundException.class,
                () -> contratService.findById(contratId));

        assertEquals(nombreInitial, paiementService.findAll().size());

        System.out.println(
                "Suppression du paiement par cascade vérifiée");
    }

    @Test
    void testMaintenanceService() {
        int nombreInitial = maintenanceService.findAll().size();
        LocalDate debut = LocalDate.now().plusDays(1);

        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(debut);
        maintenance.setDateFin(debut.plusDays(2));
        maintenance.setDescription("Vidange Test");

        Long id = maintenanceService.create(maintenance).getId();

        assertNotNull(id);
        System.out.println("Maintenance créée : " + id);

        var maintenances = maintenanceService.findAll();

        assertEquals(nombreInitial + 1, maintenances.size());
        assertTrue(maintenances.stream()
                .anyMatch(m -> id.equals(m.getId())));

        Maintenance trouvee = maintenanceService.findById(id);
        assertEquals("Vidange Test", trouvee.getDescription());

        System.out.println(
                "Maintenance retrouvée : " + trouvee.getDescription());
        System.out.println("Nombre de maintenances : " + maintenances.size());

        Maintenance modification = new Maintenance();
        modification.setDateDebut(debut);
        modification.setDateFin(debut.plusDays(3));
        modification.setDescription("Vidange et changement des filtres");

        maintenanceService.update(id, modification);

        Maintenance modifiee = maintenanceService.findById(id);
        assertEquals(
                "Vidange et changement des filtres",
                modifiee.getDescription());
        assertEquals(debut.plusDays(3), modifiee.getDateFin());

        System.out.println(
                "Maintenance modifiée : " + modifiee.getDescription());

        maintenanceService.deleteById(id);

        assertThrows(ResourceNotFoundException.class,
                () -> maintenanceService.findById(id));

        assertEquals(nombreInitial, maintenanceService.findAll().size());

        System.out.println("Suppression maintenance vérifiée");
    }
}