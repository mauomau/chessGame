public class Tour extends Piece {

    public Tour(String couleur) {
        super(couleur, "Tour");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2) {
        // La tour se déplace uniquement en ligne droite, horizontalement ou verticalement
        return x1 == x2 || y1 == y2;
    }
}
