# Account management : CQRS & Event Sourcing

> Réalisé par : HACHMI Mohamed Amine

### I. Structure du projet :

![structure.png](screenshots/structure.png)

1. Common API :

![img.png](screenshots/commonapi.png)

2. Account Command Side :

![img.png](screenshots/command.png)

3. Account Query Side :

![img.png](screenshots/query.png)

### II. Axon Framework :
J'ai ensuite lancé le serveur Axon, et ajouté des CommandBus pour AccountQuerySide et AccountCommandSide :

![img.png](screenshots/axonserver.png)

![img.png](screenshots/commandbus.png)

![img.png](screenshots/axondashboard.png)

### III. Execution :

![img.png](screenshots/postman1.png)

![img.png](screenshots/postman2.png)

![img.png](screenshots/log.png)

******************************
-> Erreur !
J'ai rencontré l'erreur suivante concernant la requête GetAllAccounts et GetAccountById

![img.png](screenshots/img.png)

-> Solution : 
pour résoudre le problème, j'ai ajouté une méthode
```
List<Operation> findOperationsByAccount(Account a);
```
au niveau de OperationRepository, et elle permet de récupérer les opérations d'un compte.
Puis je l'ai utilisé au niveau du service AccountQueryHandler.

![img.png](screenshots/img_1.png)

******************************

![img.png](screenshots/postman3.png)

![img.png](screenshots/h2.png)
