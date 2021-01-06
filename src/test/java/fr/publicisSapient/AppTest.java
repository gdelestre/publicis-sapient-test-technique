package fr.publicisSapient;

import fr.publicisSapient.helper.Helper;
import fr.publicisSapient.model.Lawnmower;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class AppTest 
{

    @Test
    public void changeDirectionNorthToEst(){
        Lawnmower lawnmower = new Lawnmower(2, 2, 'N');
        lawnmower.changeDirection('D');
        assertEquals('E',lawnmower.getOrientation());
    }

    @Test
    public void changeDirectionNorthToWest(){
        Lawnmower lawnmower = new Lawnmower(2, 2, 'N');
        lawnmower.changeDirection('G');
        assertEquals('W',lawnmower.getOrientation());
    }

    @Test
    public void changeDirectionSouthToEst(){
        Lawnmower lawnmower = new Lawnmower(2, 2, 'S');
        lawnmower.changeDirection('G');
        assertEquals('E',lawnmower.getOrientation());
    }

    @Test
    public void changeDirectionSouthToWest(){
        Lawnmower lawnmower = new Lawnmower(2, 2, 'S');
        lawnmower.changeDirection('D');
        assertEquals('W',lawnmower.getOrientation());
    }

    @Test
    public void changeDirectionEstToNorth(){
        Lawnmower lawnmower = new Lawnmower(2, 2, 'E');
        lawnmower.changeDirection('G');
        assertEquals('N',lawnmower.getOrientation());
    }

    @Test
    public void changeDirectionEstToSouth(){
        Lawnmower lawnmower = new Lawnmower(2, 2, 'E');
        lawnmower.changeDirection('D');
        assertEquals('S',lawnmower.getOrientation());
    }

    @Test
    public void changeDirectionWestToNorth(){
        Lawnmower lawnmower = new Lawnmower(2, 2, 'W');
        lawnmower.changeDirection('D');
        assertEquals('N',lawnmower.getOrientation());
    }

    @Test
    public void changeDirectionWestToSouth(){
        Lawnmower lawnmower = new Lawnmower(2, 2, 'W');
        lawnmower.changeDirection('G');
        assertEquals('S',lawnmower.getOrientation());
    }


    @Test
    public void canNotMoveTowardNorth(){
        //Coordonnées du coin supérieur droit de la pelouse.
        int[] lawnSize = new int[]{5,5};

        //Liste des positions occupées
        List<int[]> forbiddenPositions = new ArrayList<>();

        //Création d'une tondeuse orientée Nord qui est déjà tout en haut du jardin.
        Lawnmower lawnmower = new Lawnmower(2, 5, 'N');

        //La tondeuse ne peut pas aller en 2,6 (en dehors du jardin).
        assertFalse(lawnmower.canMove(2,6, lawnSize,forbiddenPositions));
    }

    @Test
    public void canNotMoveTowardSouth(){
        //Coordonnées du coin supérieur droit de la pelouse.
        int[] lawnSize = new int[]{5,5};

        //Liste des positions occupées
        List<int[]> forbiddenPositions = new ArrayList<>();

        //Création d'une tondeuse orientée S qui est déjà tout en bas du jardin.
        Lawnmower lawnmower = new Lawnmower(2, 0, 'S');

        //La tondeuse ne peut pas aller en 2,-1 (en dehors du jardin)
        assertFalse(lawnmower.canMove(2,-1, lawnSize,forbiddenPositions));
    }

    @Test
    public void canNotMoveTowardWest(){
        //Coordonnées du coin supérieur droit de la pelouse.
        int[] lawnSize = new int[]{5,5};

        //Liste des positions occupées
        List<int[]> forbiddenPositions = new ArrayList<>();

        //Création d'une tondeuse orientée W qui est déjà tout à gauche du jardin.
        Lawnmower lawnmower = new Lawnmower(0, 0, 'W');

        //La tondeuse ne peut pas aller en -1,0 (en dehors du jardin)
        assertFalse(lawnmower.canMove(-1,0, lawnSize,forbiddenPositions));
    }

    @Test
    public void canNotMoveTowardEast(){
        //Coordonnées du coin supérieur droit de la pelouse.
        int[] lawnSize = new int[]{5,5};

        //Liste des positions occupées
        List<int[]> forbiddenPositions = new ArrayList<>();

        //Création d'une tondeuse orientée E qui est déjà tout à droite du jardin.
        Lawnmower lawnmower = new Lawnmower(5, 0, 'E');

        //La tondeuse ne peut pas aller en 6,0 (en dehors du jardin)
        assertFalse(lawnmower.canMove(6,0, lawnSize,forbiddenPositions));
    }

    @Test
    public void canNotMoveLawnmowerHere(){
        //Coordonnées du coin supérieur droit de la pelouse
        int[] lawnSize = new int[]{5,5};

        //Liste des positions occupées
        List<int[]> forbiddenPositions = new ArrayList<>();
        int[] lawnmowerPosition = new int[]{2,2};

        //Ajout d'une tondeuse en 2,2
        forbiddenPositions.add(lawnmowerPosition);

        //Création d'une tondeuse juste en dessous orientée vers le N
        Lawnmower lawnmower = new Lawnmower(2, 1, 'N');

        //La tondeuse ne peut pas aller en 2,2 (présence d'une autre tondeuse)
        assertFalse(lawnmower.canMove(2,2, lawnSize,forbiddenPositions));
    }

    @Test
    public void canMoveTowardNorth(){
        //Coordonnées du coin supérieur droit de la pelouse
        int[] lawnSize = new int[]{5,5};

        //Liste des positions occupées
        List<int[]> forbiddenPositions = new ArrayList<>();

        //Création d'une tondeuse orientée vers le N
        Lawnmower lawnmower = new Lawnmower(2, 2, 'N');

        //Déplacement de la tondeuse
        lawnmower.move(lawnSize, forbiddenPositions);

        //La tondeuse a avancé de 1 vers le Nord. Son ordonée vaut donc 3 (2+1)
        assertEquals(3,lawnmower.getLawnmowerY());
    }

    @Test
    public void canMoveTowardSouth(){
        //Coordonnées du coin supérieur droit de la pelouse
        int[] lawnSize = new int[]{5,5};

        //Liste des positions occupées
        List<int[]> forbiddenPositions = new ArrayList<>();

        //Création d'une tondeuse orientée vers le Sud
        Lawnmower lawnmower = new Lawnmower(2, 2, 'S');

        //Déplacement de la tondeuse
        lawnmower.move(lawnSize, forbiddenPositions);

        //La tondeuse a avancé de 1 vers le Sud. Son ordonée vaut donc 1 (2-1)
        assertEquals(1,lawnmower.getLawnmowerY());
    }

    @Test
    public void canMoveTowardWest(){
        //Coordonnées du coin supérieur droit de la pelouse
        int[] lawnSize = new int[]{5,5};

        //Liste des positions occupées
        List<int[]> forbiddenPositions = new ArrayList<>();

        //Création d'une tondeuse orientée vers l'Ouest
        Lawnmower lawnmower = new Lawnmower(2, 2, 'W');

        //Déplacement de la tondeuse
        lawnmower.move(lawnSize, forbiddenPositions);

        //La tondeuse a avancé de 1 vers l'Ouest. Son abscisse vaut donc 1 (2-1)
        assertEquals(1,lawnmower.getLawnmowerX());
    }

    @Test
    public void canMoveTowardEast(){
        //Coordonnées du coin supérieur droit de la pelouse
        int[] lawnSize = new int[]{5,5};

        //Liste des positions occupées
        List<int[]> forbiddenPositions = new ArrayList<>();

        //Création d'une tondeuse orientée vers l'Est
        Lawnmower lawnmower = new Lawnmower(2, 2, 'E');

        //Déplacement de la tondeuse
        lawnmower.move(lawnSize, forbiddenPositions);

        //La tondeuse a avancé de 1 vers l'Est. Son abscisse vaut donc 3 (2+1)
        assertEquals(3,lawnmower.getLawnmowerX());
    }

    @Test
    public void successLoadData() throws Exception{
        //La méthode loadData de la classe Helper est privée, donc plus difficile d'accès pour les tests
        Method method = Helper.class.getDeclaredMethod("loadData", String.class);
        method.setAccessible(true);

        //Attribution du paramètre "data.txt" à mon objet method qui correspond à ma méthode "loadData"
        boolean canLoad = (boolean) method.invoke(Helper.class, "data.txt");

        //Il y a bien un fichier data.txt à la racine, donc canLoad vaut true.
        assertTrue(canLoad);
    }

    @Test
    public void failLoadData() throws Exception{
        //La méthode loadData de la classe Helper est privée, donc plus difficile d'accès pour les tests
        Method method = Helper.class.getDeclaredMethod("loadData", String.class);
        method.setAccessible(true);

        //Attribution du paramètre "data" à mon objet method qui correspond à ma méthode "loadData"
        boolean canLoad = (boolean) method.invoke(Helper.class, "data");

        //Il n'y a pas de fichier data à la racine, donc canLoad vaut false.
        assertFalse(canLoad);
    }




}
