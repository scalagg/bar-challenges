package gg.scala.challenges.handler;

import gg.scala.challenges.BarChallengesPlugin;
import gg.scala.challenges.challenge.DailyChallenge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.evilblock.cubed.util.ClassUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
@NoArgsConstructor
public class ChallengeHandler {

    private final LinkedList<DailyChallenge<?>> registeredChallenges = new LinkedList<>();

    public void registerDefaultChallenges() {
        BarChallengesPlugin.getInstance().getConfig().getStringList("challenges.impl.registration")
                .forEach(pkg -> {
                    final Collection<Class<?>> classCollection = ClassUtils.getClassesInPackage(BarChallengesPlugin.getInstance(), pkg);

                    classCollection.forEach(clazz -> {
                        try {
                            final Object object = clazz.newInstance();

                            if (object instanceof DailyChallenge) {
                                this.registeredChallenges.add((DailyChallenge<?>) object);
                            }
                        } catch (Exception exception) {
                            Logger.getGlobal().info("[Challenges] Couldn't register the challenge " + clazz.getSimpleName() + " in " + pkg + ".");
                        }
                    });
                });
    }
}
