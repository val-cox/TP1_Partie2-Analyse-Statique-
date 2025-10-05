package fr.umontpellier.hai913i.tp_ast_parser;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.Scanner;

// Les informations sur les classes
class LesClasses {
    private String nomClasse;
    private int nombreDeMethodes;
    private int nombreAttributs;

    public LesClasses(String nomClasse, int nombreDeMethodes, int nombreAttributs) {
        this.nomClasse = nomClasse;
        this.nombreDeMethodes = nombreDeMethodes;
        this.nombreAttributs = nombreAttributs;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public int getNombreDeMethodes() {
        return nombreDeMethodes;
    }

    public int getNombreAttributs() {
        return nombreAttributs;
    }
}

// Les informations sur les méthodes
class LesMethodes {
    private String nomClasse;
    private String nomMethode;
    private int nombreDeLignes;
    private int nombreDeParametres;

    public LesMethodes(String nomClasse, String nomMethode, int nombreDeLignes, int nombreDeParametres) {
        this.nomClasse = nomClasse;
        this.nomMethode = nomMethode;
        this.nombreDeLignes = nombreDeLignes;
        this.nombreDeParametres = nombreDeParametres;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public String getNomMethode() {
        return nomMethode;
    }

    public int getNombreDeLignes() {
        return nombreDeLignes;
    }

    public int getNombreDeParametres() {
        return nombreDeParametres;
    }
}

public class ProcesseurDuCode {

    private int nombreDeLignes = 0;
    private Set<String> nombreDePackages = new HashSet<>();
    private List<LesClasses> postClasses = new ArrayList<>();
    private List<LesMethodes> postMethodes = new ArrayList<>();

    public void analyser(String cheminDuFichier) throws IOException {
        String source = Files.readString(Paths.get(cheminDuFichier));
        nombreDeLignes += source.split("\n").length;

        ASTParser parseur = ASTParser.newParser(AST.JLS8);
        parseur.setSource(source.toCharArray());
        parseur.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit cu = (CompilationUnit) parseur.createAST(null);

        if (cu.getPackage() != null) {
            nombreDePackages.add(cu.getPackage().getName().toString());
        }

        cu.accept(new ASTVisitor() {
            @Override
            public boolean visit(TypeDeclaration noeud) {
                String nomClasse = (cu.getPackage() != null ? cu.getPackage().getName() + "." : "")
                        + noeud.getName().toString();
                int nombreMethodesDansClasse = noeud.getMethods().length;
                int nombreAttributsDansClasse = noeud.getFields().length;

                postClasses.add(new LesClasses(nomClasse, nombreMethodesDansClasse, nombreAttributsDansClasse));

                for (MethodDeclaration methode : noeud.getMethods()) {
                    int ligneDebut = cu.getLineNumber(methode.getStartPosition());
                    int ligneFin = cu.getLineNumber(methode.getStartPosition() + methode.getLength());
                    int lignesDeCode = ligneFin - ligneDebut + 1;
                    int nombreParametres = methode.parameters().size();

                    postMethodes.add(new LesMethodes(nomClasse, methode.getName().toString(), lignesDeCode, nombreParametres));
                }
                return super.visit(noeud);
            }
        });
    }

    // Réponses aux questions de 1 à 7
    public void afficherResultats() {
        int nombreDeClasses = postClasses.size();
        int totalMethodes = 0;
        int totalAttributs = 0;
        int totalLignesDeMethodes = 0;
        int nombreTotalDeMethodes = 0;

        for (LesClasses classe : postClasses) {
            totalMethodes += classe.getNombreDeMethodes();
            totalAttributs += classe.getNombreAttributs();
        }

        for (LesMethodes methode : postMethodes) {
            totalLignesDeMethodes += methode.getNombreDeLignes();
            nombreTotalDeMethodes++;
        }

        System.out.println("Nombre de classes: " + nombreDeClasses);
        System.out.println("Nombre de lignes: " + nombreDeLignes);
        System.out.println("Nombre de méthodes: " + totalMethodes);
        System.out.println("Nombre de packages: " + nombreDePackages.size());

        if (nombreDeClasses > 0) {
            System.out.println("Moyenne de méthodes par classe: " + ((double) totalMethodes / nombreDeClasses));
            System.out.println("Moyenne d’attributs par classe: " + ((double) totalAttributs / nombreDeClasses));
        } else {
            System.out.println("Moyenne de méthodes par classe: ");
            System.out.println("Moyenne d’attributs par classe: ");
        }

        if (nombreTotalDeMethodes > 0) {
            System.out.println("Moyenne de lignes de code par méthode: " + ((double) totalLignesDeMethodes / nombreTotalDeMethodes));
        } else {
            System.out.println("Moyenne de lignes de code par méthode:");
        }
    }

    // question 8
    public List<LesClasses> top10PourcentMethodes() {
        List<LesClasses> classesTrieesParMethodes = new ArrayList<>(postClasses);

        Collections.sort(classesTrieesParMethodes, new Comparator<LesClasses>() {
            @Override
            public int compare(LesClasses c1, LesClasses c2) {
                return c2.getNombreDeMethodes() - c1.getNombreDeMethodes();
            }
        });

        int nombreTotalDeClasses = classesTrieesParMethodes.size();
        int nombreDansTop10 = Math.max(1, (int) Math.ceil(nombreTotalDeClasses * 0.1));

        List<LesClasses> resultat = new ArrayList<>();
        for (int indice = 0; indice < nombreDansTop10; indice++) {
            resultat.add(classesTrieesParMethodes.get(indice));
        }

        return resultat;
    }

    public void afficherTop10PourcentMethodes() {
        System.out.println("\n 10% des classes avec le plus de méthodes:");
        for (LesClasses classe : top10PourcentMethodes()) {
            System.out.println("Nom de la classe: " + classe.getNomClasse());
            System.out.println("Nombre de méthodes: " + classe.getNombreDeMethodes());
            System.out.println("Nombre d’attributs: " + classe.getNombreAttributs());
            System.out.println();
        }
    }

    // question 9
    public List<LesClasses> top10PourcentAttributs() {
        List<LesClasses> classesTrieesParAttributs = new ArrayList<>(postClasses);

        Collections.sort(classesTrieesParAttributs, new Comparator<LesClasses>() {
            @Override
            public int compare(LesClasses c1, LesClasses c2) {
                return c2.getNombreAttributs() - c1.getNombreAttributs();
            }
        });

        int nombreTotalDeClasses = classesTrieesParAttributs.size();
        int nombreDansTop10 = Math.max(1, (int) Math.ceil(nombreTotalDeClasses * 0.1));

        List<LesClasses> resultat = new ArrayList<>();
        for (int indice = 0; indice < nombreDansTop10; indice++) {
            resultat.add(classesTrieesParAttributs.get(indice));
        }

        return resultat;
    }

    public void afficherTop10PourcentAttributs() {
        System.out.println("\n 10% des classes avec le plus d’attributs:");
        for (LesClasses classe : top10PourcentAttributs()) {
            System.out.println("Nom de la classe: " + classe.getNomClasse());
            System.out.println("Nombre de méthodes: " + classe.getNombreDeMethodes());
            System.out.println("Nombre d’attributs: " + classe.getNombreAttributs());
            System.out.println();
        }
    }

    // question 10
    public void afficherIntersectionTopClasses() {
        List<LesClasses> topMethodes = top10PourcentMethodes();
        List<LesClasses> topAttributs = top10PourcentAttributs();

        Set<String> nomsTopMethodes = new HashSet<>();
        for (LesClasses classe : topMethodes) {
            nomsTopMethodes.add(classe.getNomClasse());
        }

        System.out.println("\n Classes présentes des 10% de méthodes et attributs:");
        for (LesClasses classe : topAttributs) {
            if (nomsTopMethodes.contains(classe.getNomClasse())) {
                System.out.println("Nom de la classe: " + classe.getNomClasse());
                System.out.println("Nombre de méthodes: " + classe.getNombreDeMethodes());
                System.out.println("Nombre d’attributs: " + classe.getNombreAttributs());
                System.out.println();
            }
        }
    }

    // question 11
    public void afficherClassesAvecPlusDeXMethodes(int X) {
        System.out.println("\n Classes avec plus de " + X + " méthodes:");
        for (LesClasses classe : postClasses) {
            if (classe.getNombreDeMethodes() > X) {
                System.out.println("Nom de la classe: " + classe.getNomClasse());
                System.out.println("Nombre de méthodes: " + classe.getNombreDeMethodes());
                System.out.println("Nombre d’attributs: " + classe.getNombreAttributs());
                System.out.println();
            }
        }
    }

    // question 12
    public void afficherTop10PourcentMethodesLongues() {
        List<LesMethodes> methodesTriees = new ArrayList<>(postMethodes);

        Collections.sort(methodesTriees, new Comparator<LesMethodes>() {
            @Override
            public int compare(LesMethodes m1, LesMethodes m2) {
                return m2.getNombreDeLignes() - m1.getNombreDeLignes();
            }
        });

        int nombreTotalDeMethodes = methodesTriees.size();
        int nombreDansTop10 = Math.max(1, (int) Math.ceil(nombreTotalDeMethodes * 0.1));

        System.out.println("\n 10% des méthodes les plus longues:");
        for (int indice = 0; indice < nombreDansTop10; indice++) {
            LesMethodes methode = methodesTriees.get(indice);
            System.out.println("Classe: " + methode.getNomClasse());
            System.out.println("Méthode: " + methode.getNomMethode());
            System.out.println("Nombre de lignes: " + methode.getNombreDeLignes());
            System.out.println("Nombre de paramètres: " + methode.getNombreDeParametres());
            System.out.println();
        }
    }

    // question 13
    public void afficherMaxParametres() {
        int maxParametres = 0;
        LesMethodes methodeAvecMax = null;

        for (LesMethodes methode : postMethodes) {
            if (methode.getNombreDeParametres() > maxParametres) {
                maxParametres = methode.getNombreDeParametres();
                methodeAvecMax = methode;
            }
        }

        System.out.println("\n Nombre maximal de paramètres trouvés: " + maxParametres);
        if (methodeAvecMax != null) {
            System.out.println("Classe: " + methodeAvecMax.getNomClasse());
            System.out.println("Méthode: " + methodeAvecMax.getNomMethode());
            System.out.println("Nombre de lignes: " + methodeAvecMax.getNombreDeLignes());
            System.out.println("Nombre de paramètres: " + methodeAvecMax.getNombreDeParametres());
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ProcesseurDuCode proc = new ProcesseurDuCode();

        System.out.print("Donnez le chemin du dossier contenant vos fichiers Java: ");
        String cheminDossier = scanner.nextLine();

        Files.walk(Paths.get(cheminDossier))
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    try {
                        proc.analyser(p.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        while (true) {
            System.out.println("\n Options:");
            System.out.println("1. Je veux connaître les infos générales sur le code");
            System.out.println("2. Je veux voir les 10% de classes qui ont le plus de méthodes");
            System.out.println("3. Je veux voir les 10% de classes qui ont le plus d’attributs");
            System.out.println("4. Je veux voir les classes qui sont à la fois dans les deux top 10%");
            System.out.println("5. Je veux voir les classes qui possèdent plus de X méthodes");
            System.out.println("6. Je veux voir les 10% des méthodes les plus longues");
            System.out.println("7. Je veux connaître la méthode qui a le plus grand nombre de paramètres");
            System.out.println("0. Je veux quitter");
            System.out.print("Votre choix: ");

            int choix = scanner.nextInt();

            if (choix == 1) {
                proc.afficherResultats();
            } else if (choix == 2) {
                proc.afficherTop10PourcentMethodes();
            } else if (choix == 3) {
                proc.afficherTop10PourcentAttributs();
            } else if (choix == 4) {
                proc.afficherIntersectionTopClasses();
            } else if (choix == 5) {
                System.out.print("Entrez une valeur pour X: ");
                int x = scanner.nextInt();
                proc.afficherClassesAvecPlusDeXMethodes(x);
            } else if (choix == 6) {
                proc.afficherTop10PourcentMethodesLongues();
            } else if (choix == 7) {
                proc.afficherMaxParametres();
            } else if (choix == 0) {
                System.out.println("Fin du programme.");
                break;
            } else {
                System.out.println("Choix invalide.");
            }
        }

        scanner.close();
    }
}
