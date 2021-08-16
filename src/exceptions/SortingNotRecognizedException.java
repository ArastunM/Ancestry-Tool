package exceptions;

/**
 * Thrown when given sorting type is not recognized
 */
public class SortingNotRecognizedException extends IllegalArgumentException {
    public SortingNotRecognizedException(String sortingType) {
        super(sortingType + ", is not a recognized sorting type");
    }
}
