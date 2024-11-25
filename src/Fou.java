import java.util.*;

public class Fou extends Piece {
    public Fou(String couleur) {
        super(couleur, "Fou");
    }

    @Override
    public List<int[]> calculerMouvementsPossibles(int ligne, int colonne, Echiquier echiquier) {
        List<int[]> mouvements = new ArrayList<>();
        Piece[][] plateau = echiquier.obtenirTableauPieces();

        // Déplacements diagonaux
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
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

        return mouvements;
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier) {
        // Le fou se déplace en diagonale, donc la différence absolue entre x et y doit être égale
        if (Math.abs(x2 - x1) == Math.abs(y2 - y1)) {
            // Vérifier les obstacles
            return pasDObstacles(echiquier, x1, y1, x2, y2);
        }
        return false;
    }
}
