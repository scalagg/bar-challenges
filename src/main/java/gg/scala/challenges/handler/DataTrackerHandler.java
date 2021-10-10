package gg.scala.challenges.handler;

import gg.scala.challenges.BarChallengesPlugin;
import gg.scala.challenges.tracker.DataTracker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.evilblock.cubed.util.ClassUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@NoArgsConstructor
@SuppressWarnings("all")
public class DataTrackerHandler {

    private final Set<Class<?>> dataTrackerClasses = new HashSet<>();

    public void setupDefaultDataTrackerClasses() {
        BarChallengesPlugin.getInstance().getConfig().getStringList("data-tracker.impl.registration")
                .forEach(pkg -> {
                    final Collection<Class<?>> classCollection = ClassUtils.getClassesInPackage(BarChallengesPlugin.getInstance(), pkg);

                    classCollection.forEach(clazz -> {
                        try {
                            final Object object = clazz.newInstance();

                            if (object instanceof DataTracker) {
                                this.dataTrackerClasses.add(clazz);
                            }
                        } catch (Exception exception) {
                            Logger.getGlobal().info("[Challenges] Couldn't register the data tracker " + clazz.getSimpleName() + " in " + pkg + ".");
                        }
                    });
                });
    }

    public Class<? extends DataTracker> getTrackerByClassName(String name) {
        return (Class<? extends DataTracker>) this.dataTrackerClasses.stream()
                .filter(aClass -> aClass.getSimpleName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
