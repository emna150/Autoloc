# Atelier 5 — API REST AutoLoc

État : code préparé et contrôlé statiquement ; compilation et exécution de la collection à confirmer sur le PC avec MySQL. Les tests Agence et Client et un GET Swagger ont été réussis avant ces corrections, selon les résultats communiqués. Aucun résultat nouveau n’est présenté comme exécuté.

## A. Endpoints

Port conservé : 8082. Préfixe commun : `server.servlet.context-path=/api`.
Les contrôleurs déclarent uniquement leur nom de ressource (ex. `/contrats`).
Swagger : http://localhost:8082/api/swagger-ui.html ; OpenAPI : http://localhost:8082/api/v3/api-docs.

| Contrôleur | Verbe et route | Service appelé | Succès | Erreurs attendues |
|---|---|---|---|---|
| AgenceController | GET `/api/agences` | IAgenceService.findAll() | 200 | — |
| AgenceController | GET `/api/agences/{id}` | IAgenceService.findById(id) | 200 | 404 |
| AgenceController | POST `/api/agences` | IAgenceService.create(entité) | 201 | 400 |
| AgenceController | PUT `/api/agences/{id}` | IAgenceService.update(id, entité) | 200 | 400, 404 |
| AgenceController | DELETE `/api/agences/{id}` | IAgenceService.deleteById(id) | 204 | 404 |
| ClientController | GET `/api/clients` | IClientService.findAll() | 200 | — |
| ClientController | GET `/api/clients/{id}` | IClientService.findById(id) | 200 | 404 |
| ClientController | POST `/api/clients` | IClientService.create(entité) | 201 | 400 |
| ClientController | PUT `/api/clients/{id}` | IClientService.update(id, entité) | 200 | 400, 404 |
| ClientController | DELETE `/api/clients/{id}` | IClientService.deleteById(id) | 204 | 404 |
| ContratController | GET `/api/contrats` | IContratService.findAll() | 200 | — |
| ContratController | GET `/api/contrats/{id}` | IContratService.findById(id) | 200 | 404 |
| ContratController | POST `/api/contrats` | IContratService.create(entité) | 201 | 400 |
| ContratController | PUT `/api/contrats/{id}` | IContratService.update(id, entité) | 200 | 400, 404 |
| ContratController | DELETE `/api/contrats/{id}` | IContratService.deleteById(id) | 204 | 404 |
| EmployeController | GET `/api/employes` | IEmployeService.findAll() | 200 | — |
| EmployeController | GET `/api/employes/{id}` | IEmployeService.findById(id) | 200 | 404 |
| EmployeController | POST `/api/employes` | IEmployeService.create(entité) | 201 | 400 |
| EmployeController | PUT `/api/employes/{id}` | IEmployeService.update(id, entité) | 200 | 400, 404 |
| EmployeController | DELETE `/api/employes/{id}` | IEmployeService.deleteById(id) | 204 | 404 |
| EquipementController | GET `/api/equipements` | IEquipementService.findAll() | 200 | — |
| EquipementController | GET `/api/equipements/{id}` | IEquipementService.findById(id) | 200 | 404 |
| EquipementController | POST `/api/equipements` | IEquipementService.create(entité) | 201 | 400 |
| EquipementController | PUT `/api/equipements/{id}` | IEquipementService.update(id, entité) | 200 | 400, 404 |
| EquipementController | DELETE `/api/equipements/{id}` | IEquipementService.deleteById(id) | 204 | 404 |
| MaintenanceController | GET `/api/maintenances` | IMaintenanceService.findAll() | 200 | — |
| MaintenanceController | GET `/api/maintenances/{id}` | IMaintenanceService.findById(id) | 200 | 404 |
| MaintenanceController | POST `/api/maintenances` | IMaintenanceService.create(entité) | 201 | 400 |
| MaintenanceController | PUT `/api/maintenances/{id}` | IMaintenanceService.update(id, entité) | 200 | 400, 404 |
| MaintenanceController | DELETE `/api/maintenances/{id}` | IMaintenanceService.deleteById(id) | 204 | 404 |
| PaiementController | GET `/api/paiements` | IPaiementService.findAll() | 200 | — |
| PaiementController | GET `/api/paiements/{id}` | IPaiementService.findById(id) | 200 | 404 |
| ReservationController | GET `/api/reservations` | IReservationService.findAll() | 200 | — |
| ReservationController | GET `/api/reservations/{id}` | IReservationService.findById(id) | 200 | 404 |
| ReservationController | POST `/api/reservations` | IReservationService.create(entité) | 201 | 400 |
| ReservationController | PUT `/api/reservations/{id}` | IReservationService.update(id, entité) | 200 | 400, 404 |
| ReservationController | DELETE `/api/reservations/{id}` | IReservationService.deleteById(id) | 204 | 404 |
| VehiculeController | GET `/api/vehicules` | IVehiculeService.findAll() | 200 | — |
| VehiculeController | GET `/api/vehicules/{id}` | IVehiculeService.findById(id) | 200 | 404 |
| VehiculeController | POST `/api/vehicules` | IVehiculeService.create(entité) | 201 | 400 |
| VehiculeController | PUT `/api/vehicules/{id}` | IVehiculeService.update(id, entité) | 200 | 400, 404 |
| VehiculeController | DELETE `/api/vehicules/{id}` | IVehiculeService.deleteById(id) | 204 | 404 |

`POST /api/paiements` renvoie 405 : il n’existe aucune méthode POST sur ce contrôleur.
Les erreurs gérées renvoient `statut`, `message` et `horodatage` (UTC ISO-8601).
Les JSON illisibles sont traités en 400. Les autres erreurs techniques ne sont pas masquées par un gestionnaire générique.

### Adaptations au modèle réel

- Véhicule : identifiant JSON `idVehicule`, y compris dans les objets imbriqués.
- Équipement : seul `libelle` existe ; aucun champ quantité n’est inventé.
- Maintenance : dates et description ; aucun champ coût n’existe. Le cas 400 teste les dates inversées.
- Le port 8082 est conservé. Aucun changement de schéma ni de stratégie de cascade JPA existante.

## B. Annotations Jackson

| Association / attribut | Annotation retenue | Justification |
|---|---|---|
| Contrat.paiements | `@JsonManagedReference("contrat-paiements")` | Rend les paiements visibles et les accepte dans le JSON de création du contrat. |
| Paiement.contrat | `@JsonBackReference("contrat-paiements")` | Coupe le retour vers le contrat lors de la sérialisation ; rattache les enfants lors de la lecture du parent. |
| Contrat.reservation | `@JsonIgnore` | Côté inverse ; évite le cycle Réservation → Contrat → Réservation. |
| Agence.employes, Agence.vehicules | `@JsonIgnore` | Les enfants sont créés séparément en indiquant leur agence. |
| Client.reservations | `@JsonIgnore` | Une réservation reçoit le client par son identifiant. |
| Vehicule.reservations, Vehicule.maintenances | `@JsonIgnore` | Les enfants sont créés séparément en indiquant leur véhicule. |
| Equipement.vehicules | `@JsonIgnore` | Coupe le cycle ManyToMany, conserve Vehicule.equipements visible. |

`@JsonBackReference` participe à une paire parent/enfant nommée ; `@JsonIgnore` exclut une propriété du contrat JSON. Ne pas mettre `@JsonBackReference` sur Reservation.client : le client doit pouvoir être reçu dans le POST.

`ContratServiceImpl.create` fixe aussi le contrat sur chaque paiement avant `save`, afin de maintenir le côté propriétaire pour un appel Java direct. `cascade=ALL` propage PERSIST et REMOVE. `update` recopie uniquement dateSignature, montantTotal et valide ; il ne remplace pas la liste des paiements.

Scénario fourni : POST contrat avec deux paiements → GET de chacun → PUT du contrat sans paiements → GET des paiements conservés → DELETE contrat → GET des deux paiements en 404. Les identifiants sont mémorisés dynamiquement. Résultat réel de cascade : **à compléter après le Run Postman**. Aucun contrôle SQL exécuté ici.

## C. Postman et Swagger

Postman conserve les requêtes et leurs assertions, les rejoue dans l’ordre et mémorise les identifiants. Exemple : la collection stocke contratId et les identifiants des paiements, vérifie leur conservation après PUT et leur disparition après DELETE.

Swagger décrit les routes à partir du code, regroupe les ressources via `@Tag`, affiche les résumés `@Operation` et les statuts `@ApiResponse`. `@Parameter` décrit les identifiants. Exemple déjà réalisé : GET des agences depuis Try it out, réponse 200.

La collection compte 120 requêtes, réparties en dossiers : références temporaires, huit entités CRUD, Paiement en lecture seule et nettoyage. Les assertions After response vérifient les codes, certaines valeurs modifiées, le format d’erreur, les corps vides et la cascade. Elle est fournie pour exécution locale ; ses résultats ne sont pas préremplis.

## D. Anomalies SonarQube

Les deux occurrences S4684 ci-dessous ont réellement été affichées dans les captures de l’IDE. L’analyse SonarQube n’a pas été exécutée dans cet environnement.

| Emplacement | Règle | Observation | Traitement / état |
|---|---|---|---|
| AgenceController.create, paramètre Agence | java:S4684 | Entité persistante reçue directement : risque de liaison de champs internes. | Non supprimée : le sujet impose provisoirement les entités et reporte les DTO au chapitre 10. DTO à prévoir ensuite. Aucun masquage de la règle. |
| AgenceController.update, paramètre Agence | java:S4684 | Même risque lors d’une modification. | Copie des champs modifiables par le service déjà présente ; cela ne supprime pas le constat d’exposition. DTO reporté. |

Analyse IDE réelle fournie : **49 alertes dans 9 contrôleurs**, dont 33 demandes de suppression du conteneur `@ApiResponses` et 16 alertes S4684 liées aux entités reçues dans les POST/PUT.

| Emplacement constaté | Règle / message réel | Correction réalisée |
|---|---|---|
| AgenceController.findById | Remove the 'ApiResponses' wrapper from this annotation group (identifiant de règle à relever dans IDE) | Remplacement du conteneur par des annotations `@ApiResponse` répétées directement sur la méthode. |
| ClientController.findById | Même message ; deuxième occurrence constatée | Même simplification en conservant les réponses 200 et 404 documentées. |

La correction a été généralisée aux **33 conteneurs** des neuf contrôleurs. Les imports `ApiResponses` devenus inutiles ont été retirés. Les annotations de réponse, routes et appels de service sont conservés. Compilation et nouvelle analyse IDE à effectuer après remplacement ; le nombre d’alertes après correction n’est pas encore confirmé. Les 16 S4684 ne sont pas masquées : les DTO sont reportés au chapitre 10 conformément au sujet.

## E. Questions de compréhension

1. Le DispatcherServlet reçoit les requêtes Spring MVC. Les HandlerMapping retrouvent la méthode d’après le chemin et le verbe HTTP ; un HandlerAdapter l’appelle. Les convertisseurs HTTP transforment notamment le corps JSON et la réponse Java.
2. Le contrôleur dépend de IContratService pour conserver la séparation web/métier, permettre l’injection d’une implémentation et faciliter son remplacement. Le repository est un détail de la couche d’accès aux données.
3. POST renvoie 201 car une ressource est créée. DELETE renvoie 204 car l’opération réussit sans corps de réponse. Une ressource absente est un cas attendu : ResourceNotFoundException est traduite en 404, plutôt qu’en erreur serveur 500.
4. Les paiements appartiennent au contrat et sont créés par cascade. PaiementController ne propose que les lectures ; POST /api/paiements donne 405 Method Not Allowed.
5. Exposer les entités couple l’API au schéma, révèle des champs internes, ouvre un risque de mass assignment et peut produire des cycles JSON/proxies. Contrat.paiements est une composition reçue dans le parent : ManagedReference/BackReference convient. Agence.vehicules est ignoré côté inverse car un véhicule est créé séparément avec son agence.

## Vérifications restantes avant remise

- [ ] Compiler sur le PC : `.\mvnw.cmd -U compile`.
- [ ] Redémarrer et vérifier Swagger et `/api/v3/api-docs`.
- [ ] Exécuter la collection complète, une seule itération, environnement local sélectionné ; conserver le résultat réel.
- [ ] Vérifier la cascade et, dans MySQL, les contrat_id des deux paiements avant suppression.
- [ ] Relever les anomalies SonarQube réelles et compléter la section D.
- [ ] Contrôler le diff Git, puis commit et push.


## Validation sur le poste

- Compilation Maven réussie après les corrections.
- Swagger accessible à /api/swagger-ui.html.
- Exécution Postman signalée comme réussie.
- Règle SonarQube java:S1710 : 33 alertes corrigées ;
  leur disparition est confirmée après nouvelle analyse.
- Les alertes java:S4684 sur les entités restent documentées :
  les DTO sont prévus au chapitre suivant.