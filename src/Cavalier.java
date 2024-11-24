public class Cavalier extends Piece {
    public Cavalier(String couleur) {
        super(couleur, "Cavalier");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2, boolean capture, Piece[][] echiquier) {
        // Le cavalier se déplace en "L", soit deux cases dans une direction et une dans l'autre
        if (Math.abs(x2 - x1) == 2 && Math.abs(y2 - y1) == 1 || Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 2) {
            // Le cavalier n'a pas d'obstacle, il peut sauter par-dessus d'autres pièces
            return true;
        }
        return false;
    }
}
