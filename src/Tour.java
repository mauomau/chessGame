import java.util.*;

public class Tour extends Piece {
    public Tour(String couleur) {
        super(couleur, "Tour");
    }

    @Override
    public List<int[]> calculerMouvementsPossibles(int ligne, int colonne, Echiquier echiquier) {
        List<int[]> mouvements = new ArrayList<>();
        Piece[][] plateau = echiquier.obtenirTableauPieces();

        // Déplacements horizontaux et verticaux
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 ^ j == 0) { // Soit horizontal, soit vertical
                    int x = ligne + i, y = colonne + j;
                    while (echiquier.estDansLesLimites(x, y)) {
                        if (plateau[x][y] == null) {
                            mouvements.add(new int[]{x, y});
                        } else {
                            if (!plateau[x][y].getCouleur().equals(getCouleur())) {
                                mouvements.add(new int[]{x, y}); // Capture
                            }
                            break; // Obstacle rencontré
                        }
                        x += i;
                        y += j;
                    }
                }
            }
        }

        return mouvements;
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier) {
        if (x1 != x2 && y1 != y2) {
            return false; // Une tour ne peut se déplacer que sur une ligne droite (horizontale ou verticale)
        }
        return pasDObstacles(echiquier, x1, y1, x2, y2); // Vérifier qu'il n'y a pas d'obstacles
    }
}
