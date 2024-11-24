public class Roi extends Piece {
    public Roi(String couleur) {
        super(couleur, "Roi");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier) {
        // Le roi se déplace d'une case dans n'importe quelle direction
        if (Math.abs(x2 - x1) <= 1 && Math.abs(y2 - y1) <= 1) {
            // Vérifier qu'il n'y a pas d'obstacle et qu'il ne capture pas une pièce de la même couleur
            Piece pieceDestination = echiquier[x2][y2];
            return pieceDestination == null || !pieceDestination.getCouleur().equals(getCouleur());
        }
        return false;
    }
}
