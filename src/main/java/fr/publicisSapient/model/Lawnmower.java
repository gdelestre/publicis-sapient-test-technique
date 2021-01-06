package fr.publicisSapient.model;

import java.util.List;

public class Lawnmower {

    //Correspond à l'abscisse de la tondeuse
    private int lawnmowerX;

    //Correspond à l'ordonnée de la tondeuse
    private int lawnmowerY;

    //Correspond à l'orientation de la tondeuse
    private char orientation;

    public Lawnmower(int lawnmowerX, int lawnmowerY, char orientation) {
        this.lawnmowerX = lawnmowerX;
        this.lawnmowerY = lawnmowerY;
        this.orientation = orientation;
    }

    /**
     * Getter pour retourner la position en X de la tondeuse (pour les tests)
     * @return la position en X de la tondeuse
     */
    public int getLawnmowerX() {
        return lawnmowerX;
    }

    /**
     * Getter pour retourner la position en Y de la tondeuse (pour les tests)
     * @return la position en Y de la tondeuse
     */
    public int getLawnmowerY() {
        return lawnmowerY;
    }

    /**
     * Getter pour retourner l'orientation de la tondeuse (pour les tests)
     * @return l'orientation de la tondeuse
     */
    public char getOrientation() {
        return orientation;
    }

    /**
     * Cette méthode permet de changer la direction de notre tondeuse
     * @param direction : correspond à la nouvelle direction
     */
    public void changeDirection(char direction){
        //Il existe différentes possibilités en fonction de l'orientation d'origine de la tondeuse
        switch (orientation){
            //La tondeuse est dirigée vers le Nord (N)
            case 'N':
                //Si la direction indiquée est Droite (D), la nouvelle orientation sera l'Est (E)
                //Sinon (la direction indiquée est Gauche), la nouvelle orientation sera l'Ouest (W)
                this.orientation = (direction == 'D') ? 'E' : 'W';
                break;

            //La tondeuse est dirigée vers l'Est (E)
            case 'E':
                //Si la direction indiquée est Droite (D), la nouvelle orientation sera le Sud (S)
                //Sinon (la direction indiquée est Gauche), la nouvelle orientation sera le Nord (N)
                this.orientation = (direction == 'D') ? 'S' : 'N';
                break;

            //La tondeuse est dirigée vers le Sud (S)
            case 'S':
                //Si la direction indiquée est Droite (D), la nouvelle orientation sera l'Ouest (O)
                //Sinon (la direction indiquée est Gauche), la nouvelle orientation sera l'Est (W)
                this.orientation = (direction == 'D') ? 'W' : 'E';
                break;

            //La tondeuse est dirigée vers l'Ouest (W)
            case 'W':
                //Si la direction indiquée est Droite (D), la nouvelle orientation sera le Nord (N)
                //Sinon (la direction indiquée est Gauche), la nouvelle orientation sera le Sud (S)
                this.orientation = (direction == 'D') ? 'N' : 'S';
                break;
        }
    }

    /**
     * Méthode qui permet de faire avancer notre tondeuse
     *
     * cf méthode canMove
     * @param gardenSize : correspond à la taille du jardin. Permet de savoir si la tondeuse est arrivée à une extrémité du jardin
     * @param allLLawnmowerPosition : correspond à un tableau qui regroupe les positions des tondeuses sur la pelouse.
     */
    public void move(int[] gardenSize, List<int[]> allLLawnmowerPosition){
        //Il existe différentes possibilités en fonction de l'orientation de la tondeuse
        switch (orientation){
            //La tondeuse est dirigée vers le Nord (N), elle ne peut avancer que vers le haut (Y+1)
            case 'N':
                //Si la tondeuse peut avancer, on ajoute 1 à son ordonnée actuelle, sinon elle garde sa position
                this.lawnmowerY = canMove(this.lawnmowerX, this.lawnmowerY+1, gardenSize, allLLawnmowerPosition) ? lawnmowerY + 1 : lawnmowerY;
                break;

            //La tondeuse est dirigée vers l'Est (E), elle ne peut avancer que vers la droite (X+1)
            case 'E':
                //Si la tondeuse peut avancer, on ajoute 1 à son abscisse actuelle, sinon elle garde sa position
                this.lawnmowerX = canMove(this.lawnmowerX+1, this.lawnmowerY, gardenSize, allLLawnmowerPosition) ? lawnmowerX + 1 : lawnmowerX;
                break;

            //La tondeuse est dirigée vers le Sud (S), elle ne peut avancer que vers le bas (Y-1)
            case 'S':
                //Si la tondeuse peut avancer, on enlève 1 à son ordonnée actuelle, sinon elle garde sa position
                this.lawnmowerY = canMove(this.lawnmowerX, this.lawnmowerY-1, gardenSize, allLLawnmowerPosition) ? lawnmowerY - 1 : lawnmowerY;
                break;

            //La tondeuse est dirigée vers l'Ouest (W), elle ne peut avancer que vers la gauche (X-1)
            case 'W':
                //Si la tondeuse peut avancer, on enlève 1 à son abscisse actuelle, sinon elle garde sa position
                this.lawnmowerX = canMove(this.lawnmowerX-1, this.lawnmowerY, gardenSize, allLLawnmowerPosition) ? lawnmowerX - 1 : lawnmowerX;
                break;
        }
    }

    /**
     *Méthode qui permet de déterminer si notre tondeuse peut avancer ou non.
     * @param newLawnmowerX : correspond à l'abscisse souhaitée par le déplacement
     * @param newLawnmowerY : correspond à l'ordonnée souhaitée par le déplacement
     * @param lawnSize : correspond à la taille de la pelouse.
     * @param allLLawnmowerPosition : correspond à un tableau qui regroupe les positions des tondeuses une fois leurs déplacements finis.
     * @return True si notre tondeuse peut avancer, False si elle ne peut pas.
     */
    public boolean canMove(int newLawnmowerX, int newLawnmowerY, int[] lawnSize, List<int[]> allLLawnmowerPosition){
        /*Si une nouvelle valeur de X ou Y est négative, ou supérieure à la taille de notre pelouse
         : la tondeuse ne peut pas effectuer ce déplacement*/
        if(newLawnmowerX < 0 || newLawnmowerY <0 || newLawnmowerX > lawnSize[0] || newLawnmowerY > lawnSize[1]){
            return false;

        //Sinon elle peut avancer, sauf si une tondeuse bloque sa progression
        }else{
            //On vérifie s'il y a des tondeuses sur la pelouse
            if (allLLawnmowerPosition.size() != 0) {
                //On parcourt la liste de toutes les tondeuses qui ont fini leurs déplacements
                for (int[] position : allLLawnmowerPosition) {

                    //S'il y a déjà une tondeuse aux nouvelles coordonnées souhaitées, notre tondeuse ne peut pas avancer
                    if (newLawnmowerX == position[0] && newLawnmowerY == position[1]) {
                        return false;
                    }
                }
            }
            //S'il n'y a aucune tondeuse qui nous bloque, la tondeuse peut avancer
            return true;
        }
    }

    /**
     * Méthode qui affiche les coordonnées et l'orientation de la tondeuse
     * @return les coordonnées de la tondeuse
     */
    public int[] givePosition(){
        System.out.println(this.lawnmowerX+" "+this.lawnmowerY+" "+this.orientation);
        return new int[]{lawnmowerX,lawnmowerY};
    }

}
