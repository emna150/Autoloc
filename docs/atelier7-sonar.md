# Atelier 7 — Compte rendu SonarQube for IDE

L’analyse locale Java a couvert les 85 fichiers de `src/main/java`, y compris les entités, les repositories, les services, les contrôleurs, les DTO et les mappers. Le moteur d’analyse de SonarQube for IDE 6.3.0 a signalé 15 alertes avant correction et une après la seconde analyse.

| Règle | Fichier | Correction |
| --- | --- | --- |
| S1144 | `AutoLocManager.java` | Suppression de la méthode privée `envoyerSMS`, qui n’avait aucun appelant. |
| S2184 | `AutoLocManager.java` | Utilisation des valeurs `120.0`, `180.0` et `150.0` pour effectuer la multiplication en `double` dès le calcul du prix. |
| S106 | `AutoLocManager.java` | Remplacement de `System.out` par le logger SLF4J, avec des messages paramétrés pour les valeurs dynamiques. |
| S8696 | `TarificationStrategyFactory.java` | Comparaison des jours avec `equals`. Les trois jours reconnus comme jours de week-end par le projet restent vendredi, samedi et dimanche. |
| S8688 | `ReservationRequestValidator.java` | La comparaison à la date du jour utilise explicitement le fuseau horaire de la JVM, comme la configuration par défaut de Bean Validation. |

L’alerte S1172 concerne le paramètre `typeVehicule` de la méthode publique `louerVehicule`. La signature publique a été conservée pour éviter de casser ses appelants potentiels. Les résultats peuvent varier selon la version du plugin et le profil de règles IntelliJ.

## Vérification

- Les 36 tests Maven ont réussi, dont les 13 tests de validation MVC. La suite qui accède à la base a été exécutée avec H2 en mode MySQL.
- La création des paiements passe par la création du contrat. Un paiement inclus dans un nouveau contrat exige un montant positif et un mode de paiement. Le service lui associe le contrat au moment de l’enregistrement.
- Pour une capture IntelliJ, sélectionner `src/main/java` dans l’arborescence, puis utiliser le menu contextuel **SonarQube for IDE → Analyze with SonarQube for IDE**. Le résultat apparaît dans l’onglet **Report**.
