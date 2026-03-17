public enum RoomType {
    SINGLE, DOUBLE, SUITE;

    @Override
    public String toString() {
        switch(this) {
            case SINGLE: return "Single";
            case DOUBLE: return "Double";
            case SUITE: return "Suite";
            default: return "";
        }
    }
}