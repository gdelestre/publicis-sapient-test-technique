package fr.publicisSapient.helper;

import fr.publicisSapient.model.Lawnmower;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public abstract class Helper {

    //Liste qui va stocker les données du fichier d'entrée
    private static final List<String> data = new ArrayList<>();

    //Liste qui va stocker toutes les positions des tondeuses sur la pelouse
    private static final List<int[]> forbiddenPositions = new ArrayList<>();

    //Tableau qui va contenir les coordonnées du coin supérieur droit de la pelouse
    private static final int[] lawnCoord = new int[2];

    //Permet de récupérer les données saisies au clavier (pour le nom du fichier d'entrée)
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * Méthode qui exécute le programme
     */
    public static void startProgram() {

        boolean canStart = false;

        //Tant que les données n'ont pas été chargées, les tondeuses ne bougeront pas
        while (!canStart){
            // 1) Demande le nom du fichier de données
            String name = askForDataName();

            // 2) Chargement des données
            //Si les données n'ont pas été chargées, il y a le message ci-dessous
            if (!loadData(name)) {
                System.out.println("Les données n'ont pas pu être chargées.\n");
                System.out.println("Tapez q pour quitter ou autre chose pour recommencer");

                //Récupération de la valeur saisie
                String exit = keyboard.nextLine();

                //Si celle-ci est égale à q, le programme s'arrête
                if(exit.equals("q")){
                    System.exit(0);
                }

            //Sinon, on peut continuer
            }else{
                canStart = true;
                keyboard.close();
            }
        }

        // 2) Récupération des coordonnées du coin supérieur droit de la pelouse
        if (!loadLawnSize()) {
            System.out.println("Les coordonnées du coin supérieur droit de la pelouse ne sont pas bonnes.");
            System.exit(0);
        }

        // 3-1) Récupération de la liste des coordonnées des tondeuses sur la pelouse
        // 3-2) Ajout de ses coordonnées à la liste des coordonnées interdites
        // 3-3) Récupération des indices des tondeuses qui veulent occuper une place déjà prise par une autre tondeuse.
        Set<Integer> lawnmowerIndex = loadLawnmowerPosition();

        //Si la taille de la liste est supérieure à 0. Il y a au moins une fois, deux tondeuses sur le même emplacement
        if (lawnmowerIndex.size() != 0) {

            //Parcours des index des tondeuses à supprimer
            for (int index : lawnmowerIndex) {
                System.out.println("Suppression de la tondeuse en ligne " + index + " ainsi que sa série d'instructions");

                // 3-3-1) Suppression de la tondeuse
                data.remove(index);
                // 3-3-2) Suppression de sa série d'instructions
                // NB : Comme on a supprimé une ligne précédemment, les indices sont décalés.
                data.remove(index);
            }
        }

        // 4) Chargement des informations pour toutes les tondeuses du fichier d'entrée
        List<String> lawnmowerInformations = loadLawnmowersInformations();
        if (lawnmowerInformations.size() == 0) {
            System.out.println("Aucune tondeuse n'a été trouvée");
            System.exit(0);
        } else {
            // 5) Pour chaque tondeuse chargée....
            for (String lawnmowerInfoTemp : lawnmowerInformations) {
                /* Séparation des informations en 2 parties:
                    - d'abord les données permettant de créer la tondeuse (lawnmowerInfo[0])
                    - puis la série d'instructions à effectuer pour déplacer la tondeuse (lawnmowerInfo[1]) */
                String[] lawnmowerInfo = lawnmowerInfoTemp.split("-");

                // 5-1) Modélisation de la tondeuse
                Lawnmower lawnmower = createLawnmower(lawnmowerInfo[0]);

                // 5-2) La tondeuse va démarrer. Suppression de sa position des positions interdites.
                int[] initialPosition = new int[]{lawnmower.getLawnmowerX(), lawnmower.getLawnmowerY()};
                deleteLawnmowerPositionToForbiddenPosition(initialPosition);

                // 5-3) Exécution de la série d'instructions
                executeInstructions(lawnmowerInfo[1], lawnmower);

                // 5-4) Affichage de la position de la tondeuse suite à la série d'instructions
                int[] finalPosition;
                finalPosition = lawnmower.givePosition();

                // 5-5) Ajout de la tondeuse à la liste des positions interdites
                addLawnmowerPositionToForbiddenPosition(finalPosition);
            }
        }
    }

    /**
     * Méthode qui demande le nom du fichier de données d'entrée
     * @return le nom du fichier
     */
    private static String askForDataName() {
        System.out.println("Le fichier de données doit être au même niveau que le programme.");
        System.out.println("Veuillez indiquer le nom complet du fichier de données (avec l'extension):");
        return keyboard.nextLine();
    }

    /**
     * Méthode qui charge les données contenues dans le fichier d'entrée
     *
     * @return true si cela a marché, sinon false.
     */
    private static boolean loadData(String dataName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataName));
            String line;
            //parcours le fichier. Tant qu'il y a une ligne, elle sera ajoutée à la variable data
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
            br.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Méthode qui récupère les coordonnées du coin supérieur droit de la pelouse
     * Stocke les coordonnées dans un tableau lawnCoord
     *
     * @return true si cela a marché, sinon false.
     */
    private static boolean loadLawnSize() {
        String size = data.get(0);
        if (size.matches("^[0-9][ ][0-9]$")) {
            String[] lawnSize = size.split(" ");
            lawnCoord[0] = Integer.parseInt(lawnSize[0]);
            lawnCoord[1] = Integer.parseInt(lawnSize[1]);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Méthode qui récupère toutes les positions initiales des tondeuses
     * et les ajoute dans une liste de positions interdites.
     *
     * @return le numéro de ligne dans le fichier data des tondeuses
     * qui ont les mêmes coordonnées qu'une tondeuse déjà présente
     */
    private static Set<Integer> loadLawnmowerPosition() {

        //HashSet qui va contenir les index des tondeuses à effacer s'il y a un doublon
        Set<Integer> lawnmowerIndex = new HashSet<>();

        //Vérification que le fichier d'entrée contienne potentiellement des tondeuses
        if (data.size() > 1) {
            /* Parcours du fichier d'entrée :
                - début à la seconde ligne (indice 1) car la première ligne (indice 0)
                contient les coordonnées du coin supérieur droit de la pelouse.
                -saut de deux car pour chaque tondeuse il y a ses coordonnées puis sa série d'instructions
             */
            for (int i = 1; i < data.size(); i = i + 2) {

                // Vérification de la validité de la position initiale de la tondeuse et de son orientation
                if (data.get(i).matches("^[0-9][ ][0-9][ ][NESW]$")) {

                    //Les coordonnées sont séparées par un espace.
                    String[] coordWithOrientation = data.get(i).split(" ");

                    // Récupération de l'abscisse puis casting en int
                    int x = Integer.parseInt(coordWithOrientation[0]);

                    // Récupération de l'ordonnée puis casting en int
                    int y = Integer.parseInt(coordWithOrientation[1]);
                    int[] coord = new int[]{x, y};

                    /*Si les coordonnées sont déjà présentes dans la liste, il y a déjà une tondeuse à cet emplacement
                      Cette tondeuse sera supprimée, ainsi que sa série d'instructions */
                    if (containPosition(coord)) {
                        // Récupération du numéro de ligne dans le fichier data qui correspond à la tondeuse
                        lawnmowerIndex.add(i);
                    } else {
                        //Ajout des coordonées à la liste des positions interdites
                        addLawnmowerPositionToForbiddenPosition(coord);
                    }
                }
            }
        }
        return lawnmowerIndex;
    }

    /**
     * Méthode qui récupère sous forme d'une chaîne de caractère :
     *  -les coordonnées avec l'orientation d'une tondeuse
     *  -la série d'instructions pour cette tondeuse
     *
     * @return la liste des informations pour toutes les tondeuses
     */
    private static List<String> loadLawnmowersInformations() {
        List<String> lawnmowersInformations = new ArrayList<>();

        int lawnmowerNumber = 1;

        //Parcourt du fichier data avec un saut de 2 car d'abord il y a la tondeuse, puis sa série d'instructions
        for (int i = 1; i < data.size(); i = i + 2) {
            try {
                // Si les données sont valides
                if (data.get(i).matches("^[0-9][ ][0-9][ ][NESW]$") &&
                        data.get(i + 1).matches("^[ADG]+$")) {

                    /*Récupère d'abord les données pour créer un objet tondeuse ( data.get(i) )
                      La ligne d'en dessous contient ses instructions ( data.get(i+1) )
                      Séparation des deux données avec le symbole "-" */

                    String information = data.get(i) + "-" + data.get(i + 1);
                    lawnmowersInformations.add(information);
                    lawnmowerNumber++;

                // Si les données ne sont pas valides
                } else {
                    System.out.println("Les données pour la tondeuse n°" + lawnmowerNumber + " ne sont pas valides");
                }
            } catch (IndexOutOfBoundsException ex) {
                System.out.println("Il n'y a pas le même nombre de tondeuses que de séries d'instructions");
            }
        }
        return lawnmowersInformations;
    }

    /**
     * Méthode qui permet de créer un objet tondeuse
     *
     * @param lawnmowerInformations : la chaîne de caractères contenant les informations nécessaires
     * @return l'objet tondeuse créé
     */
    private static Lawnmower createLawnmower(String lawnmowerInformations) {
        // Les informations pour la création d'un objet tondeuse sont séparés par un espace
        String[] infomations = lawnmowerInformations.split(" ");

        int lawnmowerX = Integer.parseInt(infomations[0]);
        int lawnmowerY = Integer.parseInt(infomations[1]);
        char lawnmowerOrientation = infomations[2].charAt(0);

        return new Lawnmower(lawnmowerX, lawnmowerY, lawnmowerOrientation);
    }

    /**
     * Méthode qui exécute la série d'instructions d'une tondeuse
     *
     * @param instructions : chaîne de caractères qui contient la série d'instructions
     * @param lawnmower    : la tondeuse concernée par la la série d'instructions
     */
    private static void executeInstructions(String instructions, Lawnmower lawnmower) {
        // Parcours de la série d'instructions
        for (int i = 0; i < instructions.length(); i++) {
            // Si l'instruction est un A : il faut faire avancer la tondeuse
            if (instructions.charAt(i) == 'A') {
                lawnmower.move(lawnCoord, forbiddenPositions);

            // Sinon, il faut changer son orientation
            } else {
                lawnmower.changeDirection(instructions.charAt(i));
            }
        }
    }

    /**
     * Méthode qui ajoute la postion de la tondeuse suite à sa série d'instructions
     * à la liste des positions occupées
     *
     * @param position : position de la tondeuse à la fin de sa série d'instructions.
     */
    private static void addLawnmowerPositionToForbiddenPosition(int[] position) {
        forbiddenPositions.add(position);
    }

    /**
     * Méthode qui supprime la postion de la tondeuse des positions interdites
     *
     * @param positionToDelete : position de la tondeuse
     */
    private static void deleteLawnmowerPositionToForbiddenPosition(int[] positionToDelete) {
        //Variable qui stocke l'indice de la position à supprimer (si elle est trouvée).
        int index = -1;

        //Parcours de la liste des positions interdites
        for (int i = 0; i < forbiddenPositions.size(); i++) {
            int[] position = forbiddenPositions.get(i);
            //Si la position de la tondeuse correspond à une position dans la liste
            if (position[0] == positionToDelete[0] && position[1] == positionToDelete[1]) {
                //Suppression de la position de la liste des positions interdites
               index = i;
            }
        }
        forbiddenPositions.remove(index);
    }

    /**
     * Méthode qui vérifie la présence d'une position dans la liste des positions interdites
     *
     * @param positionToSearch : position de la tondeuse
     */
    private static boolean containPosition(int[] positionToSearch) {
        for (int[] position : forbiddenPositions) {
            if (position[0] == positionToSearch[0] && position[1] == positionToSearch[1]) {
                return true;
            }
        }
        return false;
    }
}