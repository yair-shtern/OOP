/**
 * A renderer factory.
 */
public class RendererFactory {
    private static final String CONSOLE = "console";
    private static final String NONE = "none";

    /**
     * A default constructor.
     */
    public RendererFactory() {
    }

    /**
     * Build renderer. gets a string and return Renderer object.
     *
     * @param type - renderer type.
     * @return Renderer object if type is valid input, null otherwise.
     */
    public Renderer buildRenderer(String type) {
        switch (type) {
            case CONSOLE:
                return new ConsoleRenderer();
            case NONE:
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
