package exceptions;

/**
 * Thrown there arent any samples matches for given id
 */
public class SampleNotFoundException extends IllegalArgumentException {
    public SampleNotFoundException(String id) {
        super("no matching sample in the database for given id: " + id);
    }
}
