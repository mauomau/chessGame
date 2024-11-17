public class Pion extends Piece{
    public Pion(String couleur) {
        super(couleur, "Pion");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2) {
        // Le pion se déplace d'une case vers le haut (vers la ligne 0), ou de deux cases s'il est encore sur sa ligne de départ
        if (getCouleur().equals("Blanc")) {
            if (x1 == 6) {
                return (x2 == 5 && y1 == y2) || (x2 == 4 && y1 == y2); // Première case de départ (deux cases)
            } else {
                return x2 == x1 - 1 && y1 == y2; // Déplacement normal d'une case
            }
        } else if (getCouleur().equals("Noir")) {
            // Le pion noir avance d'une case vers le bas, ou de deux cases s'il est sur sa case de départ
            if (x1 == 1) {
                return (x2 == 2 && y1 == y2) || (x2 == 3 && y1 == y2); // Deux cases depuis la ligne de départ
            } else {
                return x2 == x1 + 1 && y1 == y2; // Déplacement normal d'une case
            }
        } else {
            return false;
        }
    }
}
