package vn.com.phudnguyen.tools.autovolumemanager.listener.model;

import lombok.Data;

@Data
public class ServiceState {
    private int beforeMuted = 0;
    private Rule appliedRule;
}
