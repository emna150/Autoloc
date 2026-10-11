package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;
import java.time.LocalDate;
import java.util.UUID;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.CategorieVehicule;
import tn.esprit.autoloc.domain.enums.StatutVehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import java.math.BigDecimal;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.enums.RoleEmploye;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TpAutolocApplicationTests {

    @Autowired
    private IAgenceRepository agenceRepository;

    @Test
    void testAgenceRepository() {
        long nombreAvant = agenceRepository.count();

        Agence agence = new Agence();
        agence.setNom("Agence Test");
        agence.setVille("Tunis");
        agence.setAdresse("10 avenue de la Liberte");
        agence.setTelephone("20123456");

        Agence enregistree = agenceRepository.save(agence);
        Long id = enregistree.getId();

        assertNotNull(id);
        System.out.println("save : id=" + id
                + ", nom=" + enregistree.getNom());

        try {
            List<Agence> agences = agenceRepository.findAll();
            assertTrue(agences.stream()
                    .anyMatch(a -> id.equals(a.getId())));

            System.out.println("findAll :");
            agences.forEach(a -> System.out.println(
                    "id=" + a.getId() + ", nom=" + a.getNom()
                            + ", ville=" + a.getVille()));

            Agence retrouvee = agenceRepository.findById(id)
                    .orElseThrow();
            assertEquals("Agence Test", retrouvee.getNom());
            System.out.println("findById : " + retrouvee.getNom());

            assertTrue(agenceRepository.existsById(id));
            System.out.println("existsById : "
                    + agenceRepository.existsById(id));

            long nombreApres = agenceRepository.count();
            assertEquals(nombreAvant + 1, nombreApres);
            System.out.println("count : " + nombreApres);
        } finally {
            agenceRepository.deleteById(id);
        }

        assertFalse(agenceRepository.existsById(id));
        assertTrue(agenceRepository.findById(id).isEmpty());
        assertEquals(nombreAvant, agenceRepository.count());

        System.out.println("Apres suppression, existsById : "
                + agenceRepository.existsById(id));
    }
    @Autowired
    private IClientRepository clientRepository;

    @Test
    void testClientRepository() {
        long nombreAvant = clientRepository.count();
        String reference = UUID.randomUUID().toString();

        Client client1 = new Client();
        client1.setNom("Ellafi");
        client1.setPrenom("Emna");
        client1.setEmail("emna." + reference + "@example.com");
        client1.setTelephone("20123456");
        client1.setNumPermis("P1-" + reference);
        client1.setDateInscription(LocalDate.now());

        Client client2 = new Client();
        client2.setNom("Test");
        client2.setPrenom("Ahmed");
        client2.setEmail("ahmed." + reference + "@example.com");
        client2.setTelephone("20987654");
        client2.setNumPermis("P2-" + reference);
        client2.setDateInscription(LocalDate.now());

        try {
            client1 = clientRepository.save(client1);
            client2 = clientRepository.save(client2);

            assertNotNull(client1.getId());
            assertNotNull(client2.getId());
            assertNotEquals(client1.getId(), client2.getId());

            System.out.println("save : client1=" + client1.getId()
                    + ", client2=" + client2.getId());

            List<Client> clients = clientRepository.findAll();
            Long id1 = client1.getId();
            Long id2 = client2.getId();

            assertTrue(clients.stream()
                    .anyMatch(c -> id1.equals(c.getId())));
            assertTrue(clients.stream()
                    .anyMatch(c -> id2.equals(c.getId())));

            System.out.println("findAll clients :");
            clients.forEach(c -> System.out.println(
                    "id=" + c.getId() + ", nom=" + c.getNom()
                            + ", prenom=" + c.getPrenom()));

            Client retrouve = clientRepository.findById(id1)
                    .orElseThrow();
            assertEquals(client1.getEmail(), retrouve.getEmail());
            System.out.println("findById : " + retrouve.getPrenom());

            assertTrue(clientRepository.existsById(id1));
            System.out.println("Identifiant existant : "
                    + clientRepository.existsById(id1));

            Long idInexistant = -1L;
            while (clientRepository.existsById(idInexistant)) {
                idInexistant--;
            }

            assertFalse(clientRepository.existsById(idInexistant));
            System.out.println("Identifiant inexistant : "
                    + clientRepository.existsById(idInexistant));

            long nombreApres = clientRepository.count();
            assertEquals(nombreAvant + 2, nombreApres);
            System.out.println("count clients : " + nombreApres);
        } finally {
            if (client2.getId() != null) {
                clientRepository.deleteById(client2.getId());
            }
            if (client1.getId() != null) {
                clientRepository.deleteById(client1.getId());
            }
        }
    }
    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Test
    void testVehiculeRepository() {
        long nombreAvant = vehiculeRepository.count();

        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("TEST-" + UUID.randomUUID());
        vehicule.setMarque("Toyota");
        vehicule.setModele("Yaris");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("100.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule enregistre = vehiculeRepository.save(vehicule);
        Long id = enregistre.getIdVehicule();

        assertNotNull(id);
        System.out.println("save vehicule : id=" + id);

        try {
            List<Vehicule> vehicules = vehiculeRepository.findAll();
            assertTrue(vehicules.stream()
                    .anyMatch(v -> id.equals(v.getIdVehicule())));

            System.out.println("findAll vehicules :");
            vehicules.forEach(v -> System.out.println(
                    "id=" + v.getIdVehicule()
                            + ", marque=" + v.getMarque()
                            + ", modele=" + v.getModele()));

            long nombreApres = vehiculeRepository.count();
            assertEquals(nombreAvant + 1, nombreApres);
            System.out.println("count vehicules : " + nombreApres);

            enregistre.setTarifJournalier(new BigDecimal("120.00"));
            Vehicule modifie = vehiculeRepository.save(enregistre);
            assertEquals(id, modifie.getIdVehicule());

            Vehicule relu = vehiculeRepository.findById(id)
                    .orElseThrow();

            assertEquals(0, new BigDecimal("120.00")
                    .compareTo(relu.getTarifJournalier()));

            System.out.println("Tarif apres modification : "
                    + relu.getTarifJournalier());
        } finally {
            vehiculeRepository.deleteById(id);
        }

        assertFalse(vehiculeRepository.existsById(id));
        assertEquals(nombreAvant, vehiculeRepository.count());

        System.out.println("Apres suppression, vehicule existe : "
                + vehiculeRepository.existsById(id));
    }
    @Autowired
    private IEquipementRepository equipementRepository;

    @Test
    void testEquipementRepository() {
        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS Test");

        Equipement enregistre = equipementRepository.save(equipement);
        Long id = enregistre.getId();
        assertNotNull(id);

        System.out.println("save equipement : id=" + id);

        try {
            Equipement retrouve = equipementRepository.findById(id)
                    .orElseThrow();

            assertEquals("GPS Test", retrouve.getLibelle());
            System.out.println("findById equipement : "
                    + retrouve.getLibelle());

            boolean existe = equipementRepository.existsById(id);
            assertTrue(existe);
            System.out.println("existsById equipement : " + existe);
        } finally {
            equipementRepository.deleteById(id);
        }

        assertFalse(equipementRepository.existsById(id));
        System.out.println("Apres suppression, equipement existe : "
                + equipementRepository.existsById(id));
    }
    @Autowired
    private IEmployeRepository employeRepository;

    @Test
    void testEmployeRepository() {
        Employe employe = new Employe();
        employe.setNom("Test");
        employe.setPrenom("Ahmed");
        employe.setRole(RoleEmploye.AGENT);

        Employe enregistre = employeRepository.save(employe);
        Long id = enregistre.getId();
        assertNotNull(id);

        System.out.println("save employe : id=" + id);

        try {
            Employe retrouve = employeRepository.findById(id)
                    .orElseThrow();

            assertEquals("Test", retrouve.getNom());
            assertEquals("Ahmed", retrouve.getPrenom());
            assertEquals(RoleEmploye.AGENT, retrouve.getRole());

            System.out.println("findById employe : "
                    + retrouve.getPrenom() + ", role=" + retrouve.getRole());

            boolean existe = employeRepository.existsById(id);
            assertTrue(existe);
            System.out.println("existsById employe : " + existe);
        } finally {
            employeRepository.deleteById(id);
        }

        assertFalse(employeRepository.existsById(id));
        System.out.println("Apres suppression, employe existe : "
                + employeRepository.existsById(id));
    }
    @Autowired
    private IMaintenanceRepository maintenanceRepository;

    @Test
    void testMaintenanceRepository() {
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.of(2026, 10, 11));
        maintenance.setDateFin(LocalDate.of(2026, 10, 12));
        maintenance.setDescription("Vidange Test");

        Maintenance enregistree = maintenanceRepository.save(maintenance);
        Long id = enregistree.getId();
        assertNotNull(id);

        System.out.println("save maintenance : id=" + id);

        try {
            Maintenance retrouvee = maintenanceRepository.findById(id)
                    .orElseThrow();

            assertEquals("Vidange Test", retrouvee.getDescription());
            assertEquals(maintenance.getDateDebut(), retrouvee.getDateDebut());
            assertEquals(maintenance.getDateFin(), retrouvee.getDateFin());

            System.out.println("findById maintenance : "
                    + retrouvee.getDescription()
                    + ", debut=" + retrouvee.getDateDebut()
                    + ", fin=" + retrouvee.getDateFin());

            boolean existe = maintenanceRepository.existsById(id);
            assertTrue(existe);
            System.out.println("existsById maintenance : " + existe);
        } finally {
            maintenanceRepository.deleteById(id);
        }

        assertFalse(maintenanceRepository.existsById(id));
        System.out.println("Apres suppression, maintenance existe : "
                + maintenanceRepository.existsById(id));
    }
}