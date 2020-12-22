package hackermancool;

public class Feature {
    private Action action;
    private String primaryField;
    private String secondaryField;

    Feature(Action action, String primaryField, String secondaryField) {
        this.action = action;
        this.primaryField = primaryField;
        this.secondaryField = secondaryField;
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
            case 7:
                return Action.SLEEP;
            case 8:
                return Action.PAUSE;
            case 9:
                return Action.FORK_BOMB;
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
            case SLEEP:
                return "timeout ";
            case PAUSE:
                return "pause ";
            case FORK_BOMB:
                return "%0|%0";
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

    public String getPrimaryField() {
        return primaryField;
    }

    public void setPrimaryField(String primaryField) {
        this.primaryField = primaryField;
    }

    public String getSecondaryField() {
        return secondaryField;
    }

    public void setSecondaryField(String secondaryField) {
        this.secondaryField = secondaryField;
    }
}
