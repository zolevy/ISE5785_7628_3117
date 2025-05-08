package renderer;

/**
 * Marker interface to indicate that a class supports cloning.
 *
 * <p>This interface serves as a custom alternative to {@link java.lang.Cloneable}.
 * It does not declare any methods but may be used in custom frameworks or codebases
 * to mark classes as cloneable, possibly to avoid importing {@code java.lang.Cloneable} directly.</p>
 *
 * <p>Note: To enable cloning functionality, the class must also override {@code Object.clone()}
 * and handle {@code CloneNotSupportedException} appropriately.</p>
 */
public interface Cloneable {
}