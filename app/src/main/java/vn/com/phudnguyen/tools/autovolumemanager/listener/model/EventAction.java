package vn.com.phudnguyen.tools.autovolumemanager.listener.model;

public enum EventAction {
    MUTED("Muted Volume"),
    RESTORED("Restored Volume"),
    SERVICE_STARTED("Service Started"),
    SERVICE_STOPPED("Service Stopped");


    private final String displayText;

    EventAction(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
