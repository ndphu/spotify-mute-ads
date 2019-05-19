package vn.com.phudnguyen.tools.autovolumemanager.listener.model;

import android.graphics.drawable.Drawable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.Column;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageInfo {
    private String key;

    private String name;

    @Column(name = "package_name")
    private String packageName;

    private Drawable icon;

    public static final List<PackageInfo> CACHES = new ArrayList<>();
}
