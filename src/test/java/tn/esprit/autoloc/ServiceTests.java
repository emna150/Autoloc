package tn.esprit.autoloc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.enums.*;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.*;
import tn.esprit.autoloc.web.dto.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ServiceTests {

    @PersistenceContext
    private EntityManager entityManager;

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

        AgenceDTO agence = new AgenceDTO();
        agence.setNom("Agence Test");
        agence.setVille("Tunis");
        agence.setAdresse("10 avenue de la République");
        agence.setTelephone("71000000");

        AgenceDTO creee = agenceService.create(agence);
        Long id = creee.getId();

        assertNotNull(id);
        System.out.println("Agence créée : " + id);

        var agences = agenceService.findAll();

        agences.forEach(a ->
                System.out.println(
                        "Agence : " + a.getId() + " - " + a.getNom()));

        assertEquals(nombreInitial + 1, agences.size());
        System.out.println("Nombre d'agences : " + agences.size());

        AgenceDTO trouvee = agenceService.findById(id);
        assertEquals("Agence Test", trouvee.getNom());

        AgenceDTO modification = new AgenceDTO();
        modification.setNom("Agence Modifiée");
        modification.setVille("Sousse");
        modification.setAdresse("20 avenue Habib Bourguiba");
        modification.setTelephone("73000000");

        agenceService.update(id, modification);

        AgenceDTO modifiee = agenceService.findById(id);
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

        ClientDTO premier = new ClientDTO();
        premier.setNom("Client Test Un");
        premier.setPrenom("Emna");
        premier.setEmail("client1@example.com");
        premier.setTelephone("20000001");
        premier.setNumPermis("TEST-P001");
        premier.setDateInscription(LocalDate.now());

        ClientDTO second = new ClientDTO();
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

        ClientDTO trouve = clientService.findById(id1);
        assertEquals("client1@example.com", trouve.getEmail());

        ClientDTO modification = new ClientDTO();
        modification.setNom("Client Modifié");
        modification.setPrenom("Emna");
        modification.setEmail("client.modifie@example.com");
        modification.setTelephone("20000003");
        modification.setNumPermis("TEST-P001");
        modification.setDateInscription(premier.getDateInscription());

        clientService.update(id1, modification);

        ClientDTO modifie = clientService.findById(id1);
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

        VehiculeDTO vehicule = new VehiculeDTO();
        vehicule.setImmatriculation("TEST-AT4-001");
        vehicule.setMarque("Toyota");
        vehicule.setModele("Yaris");
        vehicule.setCategorie(CategorieVehicule.values()[0]);
        vehicule.setStatut(StatutVehicule.values()[0]);
        vehicule.setTarifJournalier(new BigDecimal("100.00"));

        Long id = vehiculeService.create(vehicule).getId();

        assertNotNull(id);
        System.out.println("Véhicule créé : " + id);

        var vehicules = vehiculeService.findAll();

        vehicules.forEach(v ->
                System.out.println(
                        "Véhicule : " + v.getId()
                                + " - " + v.getImmatriculation()));

        assertEquals(nombreInitial + 1, vehicules.size());
        System.out.println("Nombre de véhicules : " + vehicules.size());

        VehiculeDTO trouve = vehiculeService.findById(id);
        assertEquals("TEST-AT4-001", trouve.getImmatriculation());

        VehiculeDTO modification = new VehiculeDTO();
        modification.setImmatriculation("TEST-AT4-001");
        modification.setMarque("Toyota");
        modification.setModele("Yaris Hybride");
        modification.setCategorie(vehicule.getCategorie());
        modification.setStatut(vehicule.getStatut());
        modification.setTarifJournalier(new BigDecimal("120.00"));

        vehiculeService.update(id, modification);

        VehiculeDTO modifie = vehiculeService.findById(id);
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

        EmployeDTO employe = new EmployeDTO();
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

        EmployeDTO trouve = employeService.findById(id);
        assertEquals("Employé Test", trouve.getNom());

        System.out.println("Employé retrouvé : " + trouve.getNom());
        System.out.println("Nombre d'employés : " + employes.size());

        EmployeDTO modification = new EmployeDTO();
        modification.setNom("Employé Modifié");
        modification.setPrenom("Sami");
        modification.setRole(employe.getRole());

        employeService.update(id, modification);

        EmployeDTO modifie = employeService.findById(id);
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

        EquipementDTO equipement = new EquipementDTO();
        equipement.setLibelle("GPS Test");

        Long id = equipementService.create(equipement).getId();

        assertNotNull(id);
        System.out.println("Équipement créé : " + id);

        var equipements = equipementService.findAll();

        assertEquals(nombreInitial + 1, equipements.size());
        assertTrue(equipements.stream()
                .anyMatch(e -> id.equals(e.getId())));

        EquipementDTO trouve = equipementService.findById(id);
        assertEquals("GPS Test", trouve.getLibelle());

        System.out.println("Équipement retrouvé : " + trouve.getLibelle());
        System.out.println("Nombre d'équipements : " + equipements.size());

        EquipementDTO modification = new EquipementDTO();
        modification.setLibelle("GPS Modifié");

        equipementService.update(id, modification);

        EquipementDTO modifie = equipementService.findById(id);
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

        Long clientId = clientService.create(ClientDTO.builder()
                .nom("Test")
                .prenom("Client")
                .email("reservation@example.com")
                .build()).getId();
        Long vehiculeId = vehiculeService.create(VehiculeDTO.builder()
                .immatriculation("TEST-RES-01")
                .build()).getId();

        ReservationRequestDTO reservation = new ReservationRequestDTO();
        reservation.setClientId(clientId);
        reservation.setVehiculeId(vehiculeId);
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

        ReservationResponseDTO trouvee = reservationService.findById(id);
        assertEquals(debut, trouvee.getDateDebut());
        assertEquals(clientId, trouvee.getClientId());
        assertEquals("Client Test", trouvee.getClientNomComplet());
        assertEquals(vehiculeId, trouvee.getVehiculeId());
        assertEquals("TEST-RES-01", trouvee.getVehiculeImmatriculation());

        System.out.println("Réservation retrouvée : " + trouvee.getId());
        System.out.println("Nombre de réservations : " + reservations.size());

        ReservationRequestDTO modification = new ReservationRequestDTO();
        modification.setDateDebut(debut);
        modification.setDateFin(debut.plusDays(5));
        modification.setStatut(reservation.getStatut());

        reservationService.update(id, modification);

        ReservationResponseDTO modifiee = reservationService.findById(id);
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

        Long reservationId = reservationService.create(new ReservationRequestDTO()).getId();
        ContratDTO contrat = new ContratDTO();
        contrat.setReservationId(reservationId);
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("300.00"));
        contrat.setValide(false);

        Long id = contratService.create(contrat).getId();

        assertNotNull(id);
        System.out.println("Contrat créé : " + id);

        var contrats = contratService.findAll();

        assertEquals(nombreInitial + 1, contrats.size());
        assertTrue(contrats.stream()
                .anyMatch(c -> id.equals(c.getId())));

        entityManager.flush();
        entityManager.clear();
        ContratDTO trouve = contratService.findById(id);
        assertEquals(reservationId, trouve.getReservationId());
        assertEquals(0, new BigDecimal("300.00")
                .compareTo(trouve.getMontantTotal()));
        assertFalse(trouve.getValide());

        System.out.println("Contrat retrouvé : " + trouve.getId());
        System.out.println("Nombre de contrats : " + contrats.size());

        ContratDTO modification = new ContratDTO();
        modification.setDateSignature(LocalDate.now());
        modification.setMontantTotal(new BigDecimal("350.00"));
        modification.setValide(true);

        contratService.update(id, modification);

        ContratDTO modifie = contratService.findById(id);
        assertEquals(0, new BigDecimal("350.00")
                .compareTo(modifie.getMontantTotal()));
        assertTrue(modifie.getValide());

        System.out.println(
                "Contrat modifié, montant : " + modifie.getMontantTotal());

        contratService.deleteById(id);

        assertThrows(ResourceNotFoundException.class,
                () -> contratService.findById(id));

        assertEquals(nombreInitial, contratService.findAll().size());
        assertEquals(reservationId, reservationService.findById(reservationId).getId());

        System.out.println("Suppression contrat vérifiée");
    }

    @Test
    void testPaiementService() {
        int nombreInitial = paiementService.findAll().size();

        // Les écritures passent par le Contrat.
        ContratDTO contrat = new ContratDTO();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("200.00"));
        contrat.setValide(true);

        PaiementDTO paiement = new PaiementDTO();
        paiement.setMontant(new BigDecimal("200.00"));
        paiement.setDatePaiement(LocalDate.now());
        paiement.setModePaiement(ModePaiement.values()[0]);

        contrat.setPaiements(List.of(paiement));

        ContratDTO cree = contratService.create(contrat);
        Long contratId = cree.getId();
        Long paiementId = cree.getPaiements().get(0).getId();

        assertNotNull(contratId);
        assertNotNull(paiementId);

        System.out.println(
                "Paiement créé via le contrat : " + paiementId);

        var paiements = paiementService.findAll();

        assertEquals(nombreInitial + 1, paiements.size());
        assertTrue(paiements.stream()
                .anyMatch(p -> paiementId.equals(p.getId())));

        PaiementDTO trouve = paiementService.findById(paiementId);

        assertEquals(0, new BigDecimal("200.00")
                .compareTo(trouve.getMontant()));
        assertEquals(contratId, trouve.getContratId());

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

        MaintenanceDTO maintenance = new MaintenanceDTO();
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

        MaintenanceDTO trouvee = maintenanceService.findById(id);
        assertEquals("Vidange Test", trouvee.getDescription());

        System.out.println(
                "Maintenance retrouvée : " + trouvee.getDescription());
        System.out.println("Nombre de maintenances : " + maintenances.size());

        MaintenanceDTO modification = new MaintenanceDTO();
        modification.setDateDebut(debut);
        modification.setDateFin(debut.plusDays(3));
        modification.setDescription("Vidange et changement des filtres");

        maintenanceService.update(id, modification);

        MaintenanceDTO modifiee = maintenanceService.findById(id);
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

    @Test
    void testAffectationVehicule() {
        Long premiereAgence = creerAgence("Affectation véhicule 1");
        Long secondeAgence = creerAgence("Affectation véhicule 2");
        Long id = vehiculeService.create(VehiculeDTO.builder()
                .immatriculation("TEST-AFF-01")
                .build()).getId();

        vehiculeService.affecterVehiculeAAgence(id, premiereAgence);
        assertEquals(premiereAgence, agenceEnBase("vehicule", "id_vehicule", id));

        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.affecterVehiculeAAgence(Long.MAX_VALUE, premiereAgence));
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.affecterVehiculeAAgence(id, Long.MAX_VALUE));
        assertEquals(premiereAgence, agenceEnBase("vehicule", "id_vehicule", id));

        vehiculeService.affecterVehiculeAAgence(id, secondeAgence);
        assertEquals(secondeAgence, agenceEnBase("vehicule", "id_vehicule", id));

        vehiculeService.desaffecterVehiculeDeAgence(id);
        vehiculeService.desaffecterVehiculeDeAgence(id);
        assertNull(agenceEnBase("vehicule", "id_vehicule", id));
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.desaffecterVehiculeDeAgence(Long.MAX_VALUE));
    }

    @Test
    void testAffectationEmploye() {
        Long premiereAgence = creerAgence("Affectation employé 1");
        Long secondeAgence = creerAgence("Affectation employé 2");
        Long id = employeService.create(EmployeDTO.builder()
                .nom("Affectation test")
                .role(RoleEmploye.values()[0])
                .build()).getId();

        employeService.affecterEmployeAAgence(id, premiereAgence);
        assertEquals(premiereAgence, agenceEnBase("employe", "id", id));

        assertThrows(ResourceNotFoundException.class,
                () -> employeService.affecterEmployeAAgence(Long.MAX_VALUE, premiereAgence));
        assertThrows(ResourceNotFoundException.class,
                () -> employeService.affecterEmployeAAgence(id, Long.MAX_VALUE));
        assertEquals(premiereAgence, agenceEnBase("employe", "id", id));

        employeService.affecterEmployeAAgence(id, secondeAgence);
        assertEquals(secondeAgence, agenceEnBase("employe", "id", id));

        employeService.desaffecterEmployeDeAgence(id);
        employeService.desaffecterEmployeDeAgence(id);
        assertNull(agenceEnBase("employe", "id", id));
        assertThrows(ResourceNotFoundException.class,
                () -> employeService.desaffecterEmployeDeAgence(Long.MAX_VALUE));
    }

    private Long creerAgence(String nom) {
        return agenceService.create(AgenceDTO.builder().nom(nom).build()).getId();
    }

    private Long agenceEnBase(String table, String colonneId, Long id) {
        entityManager.flush();
        entityManager.clear();
        Number agenceId = (Number) entityManager
                .createNativeQuery("SELECT agence_id FROM " + table + " WHERE " + colonneId + " = :id")
                .setParameter("id", id)
                .getSingleResult();
        return agenceId == null ? null : agenceId.longValue();
    }
}
