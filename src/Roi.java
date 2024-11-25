import java.util.*;

public class Roi extends Piece {
    public Roi(String couleur) {
        super(couleur, "Roi");
    }

    @Override
    public List<int[]> calculerMouvementsPossibles(int ligne, int colonne, Echiquier echiquier) {
        List<int[]> mouvements = new ArrayList<>();
        Piece[][] plateau = echiquier.obtenirTableauPieces();

        // Tous les mouvements possibles (1 case dans toutes les directions)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) { // Ignorer (0, 0)
                    int x = ligne + dx, y = colonne + dy;
                    if (echiquier.estDansLesLimites(x, y)) {
                        if (plateau[x][y] == null || !plateau[x][y].getCouleur().equals(getCouleur())) {
                            mouvements.add(new int[]{x, y});
                        }
                    }
                }
            }
        }

        return mouvements;
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
