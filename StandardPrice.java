public class StandardPrice implements PriceStrategy {
    @Override
    public double calculatePrice(Reservation r) {
        return r.getPrice();
    }
}