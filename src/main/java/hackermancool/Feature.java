package hackermancool;

public class Feature {
    private Action action;
    private String[] args;

    Feature(Action action, String[] args) {
        this.action = action;
        this.args = args;
    }

    public static Action indexToEnum(int index) {
        switch(index) {
            case 0:
                return Action.TITLE;
            case 1:
                return Action.COLOUR;
            case 2:
                return Action.TEXT;
            case 3:
                return Action.OPEN;
            case 4:
                return Action.CREATE_SECTION;
            case 5:
                return Action.JUMP_TO_SECTION;
            case 6:
                return Action.INFINITE_LOOP;
            default:
                return Action.NULL;
        }
    }

    public static String actionToCommand(Action action) {
        switch(action) {
            case TITLE:
                return "title ";
            case COLOUR:
                return "color ";
            case TEXT:
                return "echo ";
            case OPEN:
                return "start ";
            case CREATE_SECTION:
            case INFINITE_LOOP:
                return ":";
            case JUMP_TO_SECTION:
                return "goto ";
            default:
                return "@rem ";
        }
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
