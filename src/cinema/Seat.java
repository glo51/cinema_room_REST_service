package cinema;

public class Seat {
    private final int row;
    private final int column;
    private int price;

    public Seat() {
        this.row = 0;
        this.column = 0;
        this.price = 0;
    }

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.price = this.row <= 4 ? 10 : 8;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public int getPrice() {
        return this.price;
    }

    void setPrice(int price) {
        this.price = price;
    }
}
