public class Tour extends Piece {
    public Tour(String couleur) {
        super(couleur, "Tour");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier) {
        if (x1 != x2 && y1 != y2) {
            return false; // Une tour ne peut se déplacer que sur une ligne droite (horizontale ou verticale)
        }
        return pasDObstacles(echiquier, x1, y1, x2, y2); // Vérifier qu'il n'y a pas d'obstacles
    }
}
