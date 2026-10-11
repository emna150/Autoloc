# Atelier 3 — Spring Data JPA et Repository

## Choix des interfaces

Les neuf repositories sont placés dans tn.esprit.autoloc.repository.
Leurs noms commencent par I.

JpaRepository fournit les opérations CRUD, des résultats en List,
le tri, la pagination et des méthodes JPA comme saveAndFlush().

| Interface | Étend | Justification |
|---|---|---|
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD, listes, tri et pagination des agences. |
| IEmployeRepository | JpaRepository<Employe, Long> | CRUD, listes, tri et pagination des employés. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | CRUD, listes, tri et pagination des véhicules. |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD, listes, tri et pagination des équipements. |
| IClientRepository | JpaRepository<Client, Long> | CRUD, listes, tri et pagination des clients. |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD, listes, tri et pagination des réservations. |
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, résultats en List et saveAndFlush(). |
| IPaiementRepository | JpaRepository<Paiement, Long> | Lecture des paiements ; création et retrait via le contrat. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD, listes, tri et pagination des maintenances. |

## Validation

Le démarrage de l'application affiche :
Found 9 JPA repository interfaces.



## Anomalies SonarQube for IDE corrigées

L'analyse initiale a détecté 8 imports inutilisés dans 6 fichiers.
Ces imports ont été supprimés. La nouvelle analyse affiche 0 problème.

Trois exemples de corrections dans Equipement.java :

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Import java.time.LocalDate inutilisé | Remove this unused import : aucun attribut ne l'utilise. | Suppression de l'import. |
| Import java.math.BigDecimal inutilisé | Remove this unused import : aucun attribut ne l'utilise. | Suppression de l'import. |
| Import tn.esprit.autoloc.domain.enums.* inutilisé | Remove this unused import : aucune énumération n'est utilisée. | Suppression de l'import. |

## Tests des repositories

Six tests réalisés : Agence, Client, Vehicule, Equipement,
Employe et Maintenance.

Résultat : 6 tests, 0 échec, 0 erreur, 0 test ignoré.
Les résultats de la console sont conservés dans docs/resultats-tests.txt.