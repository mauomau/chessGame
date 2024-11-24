public class Case {
    private int x, y;  // Position de la case sur l'échiquier
    private Piece piece;  // Pièce présente sur la case

    // Constructeur
    public Case(int x, int y) {
        this.x = x;
        this.y = y;
        this.piece = null;  // Par défaut, la case est vide
    }

    // Getter pour la pièce sur la case
    public Piece getPiece() {
        return piece;
    }

    // Setter pour définir la pièce sur la case
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    // Méthode pour vérifier si la case est vide
    public boolean estVide() {
        return piece == null;
    }
}
