package com.solexgames.challenges.handler;

import com.solexgames.challenges.challenge.DailyChallenge;
import com.solexgames.challenges.challenge.impl.TenKillsChallenge;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@Getter
public class ChallengeHandler {

    private final Set<DailyChallenge<?>> registeredChallenges = new HashSet<>();

    public ChallengeHandler() {
        this.registeredChallenges.add(new TenKillsChallenge());
    }
}
