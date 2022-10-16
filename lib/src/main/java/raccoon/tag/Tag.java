package raccoon.tag;

record Tag(String value) {
    @Override
    public String toString() {
        return value;
    }
}
