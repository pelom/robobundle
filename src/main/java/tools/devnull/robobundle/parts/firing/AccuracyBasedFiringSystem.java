/************************************************************************************
 * The MIT License                                                                  *
 *                                                                                  *
 * Copyright (c) 2013 Marcelo Guimarães <ataxexe at gmail dot com>                  *
 * -------------------------------------------------------------------------------- *
 * Permission  is hereby granted, free of charge, to any person obtaining a copy of *
 * this  software  and  associated documentation files (the "Software"), to deal in *
 * the  Software  without  restriction,  including without limitation the rights to *
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of *
 * the  Software, and to permit persons to whom the Software is furnished to do so, *
 * subject to the following conditions:                                             *
 *                                                                                  *
 * The  above  copyright notice and this permission notice shall be included in all *
 * copies or substantial portions of the Software.                                  *
 *                            --------------------------                            *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS *
 * FOR  A  PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR *
 * COPYRIGHT  HOLDERS  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER *
 * IN  AN  ACTION  OF  CONTRACT,  TORT  OR  OTHERWISE,  ARISING  FROM, OUT OF OR IN *
 * CONNECTION  WITH  THE  SOFTWARE  OR  THE  USE OR OTHER DEALINGS IN THE SOFTWARE. *
 ************************************************************************************/

package tools.devnull.robobundle.parts.firing;

import tools.devnull.robobundle.Bot;
import tools.devnull.robobundle.Enemy;
import tools.devnull.robobundle.Statistics;
import tools.devnull.robobundle.parts.FiringSystem;

import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.Rules.MIN_BULLET_POWER;

/**
 * @author Marcelo Guimarães
 */
public class AccuracyBasedFiringSystem implements FiringSystem {

  private static final double MEDIUM_BULLET_POWER = (MAX_BULLET_POWER + MIN_BULLET_POWER) / 2;

  private final Bot bot;

  private double accuracyToFireMax = 0.75;
  private double accuracyToFireMed = 0.4;
  private double accuracyToFireMin = 0.2;
  private double accuracyToNotFire = 0.05;

  public AccuracyBasedFiringSystem(Bot bot) {
    this.bot = bot;
  }

  public AccuracyBasedFiringSystem fireMaxAt(double accuracy) {
    accuracyToFireMax = accuracy;
    return this;
  }

  public AccuracyBasedFiringSystem fireMediumAt(double accuracy) {
    accuracyToFireMed = accuracy;
    return this;
  }

  public AccuracyBasedFiringSystem fireMinAt(double accuracy) {
    accuracyToFireMin = accuracy;
    return this;
  }

  public AccuracyBasedFiringSystem doNotFireAt(double accuracy) {
    accuracyToNotFire = accuracy;
    return this;
  }

  public double firePower() {
    if (bot.radar().hasTargetSet()) {
      Enemy enemy = bot.radar().target();
      Statistics statistics = bot.statistics().forEnemy(enemy);
      double accuracy = statistics.accuracy();
      if (statistics.fired() == 0) {
        return MEDIUM_BULLET_POWER;
      }
      if (accuracy >= accuracyToFireMax) {
        return MAX_BULLET_POWER;
      } else if (accuracy >= accuracyToFireMed) {
        return MEDIUM_BULLET_POWER;
      } else if (accuracy >= accuracyToFireMin) {
        return MIN_BULLET_POWER;
      }
    }
    return 0;
  }

  public void execute() {
    bot.gun().fire(firePower());
  }

}
