import java.util.*;

public class Dame extends Piece {
    public Dame(String couleur) {
        super(couleur, "Dame");
    }

    @Override
    public List<int[]> calculerMouvementsPossibles(int ligne, int colonne, Echiquier echiquier) {
        List<int[]> mouvements = new ArrayList<>();
        Piece[][] plateau = echiquier.obtenirTableauPieces();

        // Déplacements horizontaux, verticaux et diagonaux
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) { // Ignorer (0, 0) (pas de mouvement)
                    int x = ligne + dx, y = colonne + dy;
                    while (echiquier.estDansLesLimites(x, y)) {
                        if (plateau[x][y] == null) {
                            mouvements.add(new int[]{x, y});
                        } else {
                            if (!plateau[x][y].getCouleur().equals(getCouleur())) {
                                mouvements.add(new int[]{x, y}); // Capture
                            }
                            break; // Obstacle rencontré
                        }
                        x += dx;
                        y += dy;
                    }
                }
            }
        }

        return mouvements;
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
