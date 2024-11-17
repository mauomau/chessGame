public class Case {
    private int ligne;
    private int colonne;
    private Piece piece;  // La pièce sur cette case, ou null si la case est vide

    // constructeur
    public Case(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.piece = null;  // Initialement, la case est vide
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
