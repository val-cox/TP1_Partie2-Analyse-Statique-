# TP1_Partie2-Analyse-Statique-

# Analyseur de code Java (TP AST Parser)

## Objectif  
Ce projet a pour but d’analyser un projet Java afin d’extraire automatiquement des ** infos sur les classes, méthodes et attributs**, ainsi que d’afficher différents classements utiles pour l’étude d’un code source.  

---

## Fonctionnalités principales  

Le programme propose un menu interactif permettant de :  

1. **Afficher les informations générales** sur le code :  
   - Nombre total de classes  
   - Nombre de lignes de code  
   - Nombre de méthodes  
   - Nombre de packages  
   - Moyenne de méthodes par classe  
   - Moyenne d’attributs par classe  
   - Moyenne de lignes par méthode  

2. **Afficher le top 10% des classes avec le plus de méthodes**  

3. **Afficher le top 10% des classes avec le plus d’attributs**  

4. **Afficher l’intersection des classes présentes dans les deux top 10%**  

5. **Afficher les classes contenant plus de *X* méthodes** (valeur saisie par l’utilisateur)  

6. **Afficher le top 10% des méthodes les plus longues** (en nombre de lignes)  

7. **Afficher la méthode avec le plus grand nombre de paramètres**  

0. **Quitter le programme**  

---

##  Exemple d’exécution  

```text
Options:
1. Je veux connaître les infos générales sur le code
2. Je veux voir les 10% de classes qui ont le plus de méthodes
3. Je veux voir les 10% de classes qui ont le plus d’attributs
4. Je veux voir les classes qui sont à la fois dans les deux top 10%
5. Je veux voir les classes qui possèdent plus de X méthodes
6. Je veux voir les 10% des méthodes les plus longues
7. Je veux connaître la méthode qui a le plus grand nombre de paramètres
0. Je veux quitter

Votre choix: 1
1. Nombre de classes: 7
2. Nombre de lignes: 185
3. Nombre de méthodes: 29
4. Nombre de packages: 1
5. Moyenne de méthodes par classe: 4.142857142857143
6. Moyenne d’attributs par classe: 1.0
7. Moyenne de lignes de code par méthode: 4.344827586206897




## Utilisation:
entrer dans le repertoire du dossier:
ex:  cd Desktop/Rapport_TP_AST/tp-ast-parser
mvn clean install

Exécution:
mvn exec:java -Dexec.mainClass="fr.umontpellier.hai913i.tp_ast_parser.ProcesseurDuCode"

Structure du projet

src/main/java/fr/umontpellier/hai913i/tp_ast_parser/ProcesseurDuCode.java
Fichier principal contenant l’analyseur.

src/test/java/fr/umontpellier/hai913i/tp_ast_parser/CartoonAnimals.java
Exemple de fichier Java utilisé pour tester l’analyseur.

Pour tester avec un autre fichier Java, il suffit de l’ajouter dans le dossier de test (src/test/java/...) à la place de CartoonAnimals.java.
