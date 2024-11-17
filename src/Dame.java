public class Dame extends  Piece{
    public Dame(String couleur) {
        super(couleur, "Dame");
    }

    @Override
    public boolean estMouvementValide(int x1, int y1, int x2, int y2) {
        // La dame se déplace horizontalement, verticalement ou en diagonale
        return x1 == x2 || y1 == y2 || Math.abs(x2 - x1) == Math.abs(y2 - y1);
    }

}
