public class Byzantine {

    public int ByzantineError() {
        int error = 0;
        try {
            error = 1 + (int) (Math.random() * ((10 - 1) + 1));
        } catch (Exception ex) {
            System.out.println("Byzantine error - " + ex.getMessage());
        }
        return error;
    }
}
