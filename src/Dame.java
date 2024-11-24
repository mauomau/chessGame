public class Dame extends Piece {
    public Dame(String couleur) {
        super(couleur, "Dame");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier) {
        // Vérifier la validité du mouvement (horizontal, vertical ou diagonal)
        if (x1 != x2 && y1 != y2 && Math.abs(x2 - x1) != Math.abs(y2 - y1)) {
            return false; // La dame se déplace horizontalement, verticalement ou en diagonale
        }

        // Vérifier qu'il n'y a pas d'obstacle
        return pasDObstacles(echiquier, x1, y1, x2, y2);
    }
}
