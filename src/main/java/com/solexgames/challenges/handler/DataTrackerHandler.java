package com.solexgames.challenges.handler;

import com.solexgames.challenges.BarChallengesPlugin;
import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.challenges.tracker.DataTracker;
import com.solexgames.core.util.BukkitUtil;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@NoArgsConstructor
public class DataTrackerHandler {

    private final Set<Class<?>> dataTrackerClasses = new HashSet<>();

    public void setupDefaultDataTrackerClasses() {
        BarChallengesPlugin.getInstance().getConfig().getStringList("data-tracker.impl.registration")
                .forEach(pkg -> {
                    final Collection<Class<?>> classCollection = BukkitUtil.getClassesInPackage(BarChallengesPlugin.getInstance(), pkg);

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
}
