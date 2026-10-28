# Atelier 4 —— Partie 1: Couche Service, IoC et injection de dépendances

## A. Comparaison des modes d’injection

| Critère | Constructeur | Attribut |
|---|---|---|
| Champ final possible | Oui | Non |
| Dépendance visible dans le constructeur | Oui | Non |
| Dépendance obligatoire avec new | Oui | Non : le repository peut rester null |
| Signalement SonarQube observé | Aucun | Injection par attribut déconseillée |

L’injection par constructeur est retenue pour tous les services.
@RequiredArgsConstructor génère le constructeur des champs final.

## B. Services

| Service | Dépendance injectée | Mode |
|---|---|---|
| AgenceServiceImpl | IAgenceRepository | Constructeur |
| EmployeServiceImpl | IEmployeRepository | Constructeur |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur |
| EquipementServiceImpl | IEquipementRepository | Constructeur |
| ClientServiceImpl | IClientRepository | Constructeur |
| ReservationServiceImpl | IReservationRepository | Constructeur |
| ContratServiceImpl | IContratRepository | Constructeur |
| PaiementServiceImpl | IPaiementRepository | Constructeur |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur |

Justification commune : chaque service dépend d’une interface.
Sa dépendance est obligatoire, conservée dans un champ final et
fournie par Spring. Les services ne stockent aucun état propre à un appel.

PaiementServiceImpl expose uniquement la lecture, conformément au TP.

## C. Diagnostic des erreurs du conteneur

### Message 1 — Aucun bean IContratService

Exception attendue : NoSuchBeanDefinitionException, éventuellement
enveloppée dans UnsatisfiedDependencyException.

Causes probables :
- @Service absent sur ContratServiceImpl.
- Classe placée hors du package racine scanné.

Correction : ajouter @Service et placer la classe dans
tn.esprit.autoloc.service.impl.

### Message 2 — Plusieurs beans du même type

Exception attendue : NoUniqueBeanDefinitionException.

Deux corrections possibles :
- Mettre @Primary sur la classe de l’implémentation par défaut.
- Mettre @Qualifier("emailNotificateur") sur le paramètre du
  constructeur qui reçoit la dépendance.

### Message 3 — Dépendance circulaire

Exception attendue : BeanCurrentlyInCreationException.

ClientServiceImpl attend ReservationServiceImpl et réciproquement.
Avec des constructeurs, chaque objet exige que l’autre existe déjà :
Spring ne peut donc construire aucun des deux.

Correction : extraire la responsabilité commune dans un troisième
service, ou réorganiser les appels pour supprimer le cycle.

## D. Analyse SonarQube for IDE

### Anomalie observée et corrigée

Message :
"Remove this field injection and use constructor injection instead."

Cause : repository injecté directement dans un attribut avec @Autowired.

Correction : champ final et @RequiredArgsConstructor.
L’avertissement a disparu après correction.

### Expérience avec RuntimeException

Le remplacement temporaire de ResourceNotFoundException par
RuntimeException a été effectué.

La règle S112 attendue dans l’énoncé n’a pas été affichée dans
l’environnement utilisé. Elle n’est donc pas déclarée comme observée.

ResourceNotFoundException a ensuite été restaurée.

### Résultat final

L’analyse des packages service et exception ne signale aucune anomalie.

Une seule anomalie SonarQube a été effectivement observée à ce stade :
l’objectif de trois anomalies relevées reste à compléter.

## E. Questions de compréhension

### 1. Qui crée ContratServiceImpl ?

Le conteneur Spring appelle son constructeur lors du démarrage,
grâce à @Service et au scan des composants.

### 2. Pourquoi dépendre de IContratService ?

L’interface réduit le couplage et permet de remplacer l’implémentation
ou de fournir un substitut dans les tests.

### 3. Pourquoi un service singleton doit-il rester sans état ?

La même instance est partagée entre plusieurs appels et threads.
Un champ "clientCourant" pourrait être écrasé par une autre requête
et provoquer un traitement sur le mauvais client.

### 4. Pourquoi charger l’entité avant update ?

Cela vérifie son existence et permet de modifier uniquement les
attributs prévus, en conservant l’identifiant et les collections
qui ne doivent pas être remplacées.

### 5. Annotations et service Paiement

@Component déclare une classe détectée par le scan.
@Bean déclare un objet à partir d’une méthode de configuration.

@Primary désigne le candidat par défaut parmi plusieurs beans.
@Qualifier sélectionne explicitement un bean à un point d’injection.

IPaiementService expose uniquement la lecture : les écritures
doivent passer par le Contrat selon la règle de gestion du TP.